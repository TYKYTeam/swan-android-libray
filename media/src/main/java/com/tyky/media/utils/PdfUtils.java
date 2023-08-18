package com.tyky.media.utils;

import com.blankj.utilcode.util.PathUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.socks.library.KLog;
import com.tyky.media.R;
import com.tyky.webviewBase.BaseApplication;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class PdfUtils {
    private static final String FONT_NAME = "hwxw.ttf"; //华文新魏字体
    private static volatile PdfUtils instance;

    private PdfUtils() {
    }

    /**
     * 获取实例
     */
    public static PdfUtils getInstance() {
        if (null == instance) {
            synchronized (FileDownloadUtil.class) {
                if (instance == null) {
                    instance = new PdfUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 将工程需要的资源文件拷贝到SD卡中使用
     */
    public String getFontPath() {
        File fontFile = new File(PathUtils.getExternalAppFilesPath(), FONT_NAME);
        if (fontFile.exists()) {
            return fontFile.getPath();
        }
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = BaseApplication.getAppContext().getResources().openRawResource(R.raw.hwxw);
            fileOutputStream = new FileOutputStream(fontFile);
            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = inputStream.read(buffer, 0, 1024)) >= 0) {
                fileOutputStream.write(buffer, 0, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fontFile.getPath();
    }

    //转换某一个文件
    public void convert2PdfOneByOne(File file, FileToPdfListener fileToPdfListener) {
        String pdfFileName = FileUtils.getTransformFileName(file, "pdf");
        File pdfFile = FileUtils.getFile(pdfFileName);
        try {
            InputStream inputStream = new FileInputStream(file);
            WordExtractor extractor = new WordExtractor(inputStream);
            String[] content = extractor.getParagraphText();
            String fontPath = getFontPath();
            BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font fontChinese = new Font(bfChinese, 12, Font.NORMAL);
            FileOutputStream outputStream = new FileOutputStream(pdfFile);
            Document doc = new Document(PageSize.A4, 72, 72, 72, 72);
            PdfWriter pdfWriter = PdfWriter.getInstance(doc, outputStream);
            pdfWriter.setInitialLeading(20);
            doc.open();
            for (String s : content) {
                Paragraph p = new Paragraph(s, fontChinese);
                doc.add(p);
            }
            doc.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            fileToPdfListener.onTransformFail();
        }

        fileToPdfListener.onTransformSuccess(pdfFile);
    }

    public void docx2Pdf(File file, FileToPdfListener fileToPdfListener) {
        String pdfFileName = FileUtils.getTransformFileName(file, "pdf");
        File outPdfFile = FileUtils.getFile(pdfFileName);
        try {
            FileInputStream src = new FileInputStream(file);
            // pdf文件的尺寸
            Document pdfDocument = new Document(PageSize.A4, 72, 72, 72, 72);

            FileOutputStream out = new FileOutputStream(outPdfFile);

            PdfWriter pdfWriter = PdfWriter.getInstance(pdfDocument, out);
            XWPFDocument doc = new XWPFDocument(src);
            pdfWriter.setInitialLeading(20);
            List<XWPFParagraph> plist = doc.getParagraphs();
            pdfWriter.open();
            pdfDocument.open();
            for (int i = 0; i < plist.size(); i++) {
                XWPFParagraph pa = plist.get(i);
                List<XWPFRun> runs = pa.getRuns();
                for (int j = 0; j < runs.size(); j++) {
                    XWPFRun run = runs.get(j);
                    List<XWPFPicture> piclist = run.getEmbeddedPictures();
                    Iterator<XWPFPicture> iterator = piclist.iterator();
                    while (iterator.hasNext()) {
                        XWPFPicture pic = iterator.next();
                        XWPFPictureData picdata = pic.getPictureData();
                        byte[] bytepic = picdata.getData();


                        Image imag = Image.getInstance(bytepic);
                        imag.scalePercent(50);
                        pdfDocument.add(imag);
                    }
                    // 中文字体的解决
                    BaseFont bfChinese = BaseFont.createFont(getFontPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                    Font font = new Font(bfChinese, 12, Font.NORMAL);
                    String text = run.getText(-1);
                    byte[] bs;
                    if (text != null) {
                        bs = text.getBytes();
                        String str = new String(bs);
                        Chunk chObj1 = new Chunk(str, font);
                        pdfDocument.add(chObj1);
                    }
                }
                pdfDocument.add(new Chunk(Chunk.NEWLINE));
            }
            //需要关闭，不然无法获取到输出流
            pdfDocument.close();
            pdfWriter.close();
        } catch (Exception e) {
            KLog.d("docx转pdf文件异常");
            fileToPdfListener.onTransformFail();
        }
        fileToPdfListener.onTransformSuccess(outPdfFile);
    }

    /**
     * doc转为pdf，先将doc转为html，再将html转为pdf，因为使用poi无法直接将doc转为pdf
     */
    public File doc2pdf(File sourceFile) {
        // doc转换html
        File htmlFile = doc2Html(sourceFile);
        if (!htmlFile.exists()) {
            // 转换失败
            return FileUtils.getFile(FileUtils.getTransformFileName(sourceFile, "pdf"));
        }
        // html转换pdf
        return htmlToPdf(htmlFile);
    }

    /**
     * html转为pdf
     */
    public File htmlToPdf(File file) {
        com.itextpdf.text.Document document = null;
        PdfWriter writer = null;
        FileInputStream htmlInputStream = null;
        FileOutputStream out = null;
        String pdfFileName = FileUtils.getTransformFileName(file, "pdf");
        // 目标文件
        File outPdfFile = FileUtils.getFile(pdfFileName);
        try {
            // 目标 pdf输出到文件
            out = new FileOutputStream(outPdfFile);

            document = new com.itextpdf.text.Document(PageSize.A4);
            writer = PdfWriter.getInstance(document, out);

            document.open();
            // html输入流
            String htmlFileName = file.getName();
            htmlInputStream = new FileInputStream(htmlFileName);
            if (htmlInputStream == null) {
                KLog.d("html输入流获取失败");
                return new File("");
            }
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, htmlInputStream,
                    Charset.forName("UTF-8"), new FontProvider() {
                        @Override
                        public boolean isRegistered(String s) {
                            return false;
                        }

                        @Override
                        public Font getFont(String s, String s1, boolean embedded, float size, int style, BaseColor baseColor) {
                            // 配置字体
                            Font font = null;
                            try {
                                // 中文字体的解决
                                BaseFont bfChinese = BaseFont.createFont(getFontPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                                font = new Font(bfChinese, 12, Font.NORMAL);
                                font.setColor(baseColor);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return font;
                        }
                    });

        } catch (Exception e) {
            KLog.d("html 转换pdf异常: " + e.getMessage());
        } finally {
            if (document != null) {
                document.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (htmlInputStream != null) {
                try {
                    htmlInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return outPdfFile;
    }

    /**
     * doc文件转为html
     */
    public File doc2Html(File file) {
        FileInputStream docInput = null;
        String htmlFileName = FileUtils.getTransformFileName(file, "html");
        // 目标文件
        File outFile = FileUtils.getFile(htmlFileName);
        try {
            docInput = new FileInputStream(file);
            // doc文档对象对应的实体
            HWPFDocument document = new HWPFDocument(docInput);
            // 创建一个文档转换对象
            WordToHtmlConverter converter = new WordToHtmlConverter(DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument());
            // 设置存储图片的管理者
            converter.setPicturesManager(new PicturesManager() {
                @Override
                public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
                    return null;
                }
            });
            // 使用外观模式，将hwpfDocument文档对象设置给HtmlDocumentFacade中Document属性
            converter.processDocument(document);
            // 获取转换器中的document文档
            org.w3c.dom.Document htmlDocument = converter.getDocument();
            // 充当文档对象模型（dom）
            DOMSource domSource = new DOMSource(htmlDocument);
            //  转换器 该对象用于将源树转换为结果树
            Transformer serializer = TransformerFactory.newInstance().newTransformer();

            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");

            // 设置输出文件
            outFile = FileUtils.getFile(htmlFileName);

            StreamResult streamResult = new StreamResult(outFile);
            // 转换 将输入的源树转化为结果树并且输出到streamResult中
            serializer.transform(domSource, streamResult);
        } catch (Exception e) {
            KLog.d("doc转html异常 doc2Html: " + e);
        } finally {
            try {
                if (docInput != null) {
                    docInput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outFile;
    }

    public void toPdf(File sourceFile, FileToPdfListener fileToPdfListener) {
        String fileName = sourceFile.getName();
        if (fileName.endsWith(".doc")) {
            convert2PdfOneByOne(sourceFile, fileToPdfListener);
            return;
        }
        // docx
        docx2Pdf(sourceFile, fileToPdfListener);
    }

    public interface FileToPdfListener {
        void onTransformFail();

        void onTransformSuccess(File file);
    }

}
