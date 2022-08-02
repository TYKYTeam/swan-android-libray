package com.tyky.auth.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.socks.library.KLog;
import com.tyky.auth.bean.Piece;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class GestureUnlockView extends View implements View.OnTouchListener {

    /**
     * 每个块的信息
     */
    List<Piece> pieces = new ArrayList<>();
    /**
     * 滑动的位置信息
     */
    List<Integer> positionList = new ArrayList<>();

    /**
     * 用来存储第一次设置手势
     */
    List<Integer> tempPositionList = new ArrayList<>();

    /**
     * 各个画笔信息
     */
    private Paint borderPaint;
    private Paint centerPaint;
    private Paint errorPaint;

    /**
     * 是否为解锁模式(不是解锁模式，则是设置手势）
     */
    private boolean isUnlockMode = false;

    /**
     * 是否显示错误（会显示红色的颜色）
     */
    private boolean isError = false;

    /**
     * 最少要画有模块
     */
    private int minPiece = 4;

    /**
     * 当前手指的坐标
     */
    private Point point;

    public GestureUnlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GestureUnlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();


    }

    private void init() {
        borderPaint = new Paint();

        borderPaint.setColor(Color.BLACK);       //设置画笔颜色
        borderPaint.setStyle(Paint.Style.STROKE);  //设置画笔模式为填充
        borderPaint.setStrokeWidth(2f);

        centerPaint = new Paint();
        centerPaint.setColor(Color.BLUE);
        centerPaint.setStrokeWidth(5f);
        centerPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充

        errorPaint = new Paint();
        errorPaint.setColor(Color.RED);
        centerPaint.setStrokeWidth(5f);
        errorPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充

        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        if (pieces.size() == 0) {
            KLog.d("宽度：" + width);
            KLog.d("高度：" + height);


            int min = Math.min(width, height);
            //判断最短边能够被3整除
            int d = min / 3;
            double r = d / 2.0;
            if (min % 3 != 0) {
                min = d * 3;
            }

            //按行循环（改y）
            for (int i = 0; i < 3; i++) {
                //按列循环（改x）
                for (int j = 0; j < 3; j++) {

                    double x = d * j + r;
                    double y = d * i + r;
                    int position = 3 * i + j;
                    Piece piece = new Piece(position, x, y, d);
                    pieces.add(piece);
                }
            }
            for (Piece piece : pieces) {
                KLog.d(piece.toString());
            }


        }
        //canvas.drawColor(Color.WHITE);

        //画直线，当手指没有在一个圆上的时候
        int size = positionList.size();
        //最后一个点不用在画出直线
        if (point != null && (size > 0 && size < 9)) {
            Integer index = positionList.get(size - 1);
            Piece piece = pieces.get(index);
            float x = piece.getX();
            float y = piece.getY();
            boolean b = piece.pointIsInCircle(point.x, point.y, 0.6 * piece.getR());
            //如果点不在圆内，不画直线
            if (!b) {
                canvas.drawLine(x, y, point.x, point.y, centerPaint);
            }
        }
        //画九个点的圆形
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            canvas.drawCircle(piece.getX(), piece.getY(), piece.getR() * 0.6f, borderPaint);
        }

        KLog.d("positionList长度：", positionList.size());
        if (positionList.size() > 0) {
            //验证错误
            if (isError) {
                drawCircleAndLine(canvas, errorPaint);
            } else {
                drawCircleAndLine(canvas, centerPaint);
            }
        }


    }

    /**
     * 划实心圆和线条
     *
     * @param canvas 画布
     * @param paint  画笔
     */
    private void drawCircleAndLine(Canvas canvas, Paint paint) {
        for (int i = 0; i < positionList.size(); i++) {
            int position = positionList.get(i);
            Piece piece = pieces.get(position);
            canvas.drawCircle(piece.getX(), piece.getY(), piece.getR() * 0.2f, paint);

            //如果不是最后一个点
            if (i != positionList.size() - 1) {
                int nextPosition = positionList.get(i + 1);
                Piece nextPiece = pieces.get(nextPosition);
                canvas.drawLine(piece.getX(), piece.getY(), nextPiece.getX(), nextPiece.getY(), paint);
            }
        }
    }

    public boolean isUnlockMode() {
        return isUnlockMode;
    }

    public void setUnlockMode(boolean unlockMode) {
        isUnlockMode = unlockMode;
    }

    private OnVertifyListener listener;

    public void setOnVertifyListener(OnVertifyListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //取最短边，设置宽高（即正方形）
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int min = Math.min(widthSpecSize, heightSpecSize);
        setMeasuredDimension(min, min);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        KLog.d("action", action);
        KLog.d(x + "," + y);
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                int currentPosition = findCurrentPosition(x, y);

                //如果位置信息长度大于0且数据为在List里
                if (currentPosition >= 0 && !positionList.contains(currentPosition)) {
                    positionList.add(currentPosition);
                    point = null;
                } else {
                    //画直线
                    point = new Point((int) x, (int) y);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //当前已经划出的滑块数
                int currentSize = positionList.size();

                String result = StringUtils.join(positionList, ",");
                KLog.d("结果:" + result);
                //判断
                if (isUnlockMode) {

                } else {
                    //手势设置
                    int size = tempPositionList.size();
                    if (size <= 0) {
                        //第一次设置手势，size为0
                        if (currentSize >= minPiece) {
                            //至少要划四个点
                            tempPositionList.addAll(positionList);
                            if (listener != null) {
                                listener.onConfirmSuccess("请再次确认你的手势");
                            }
                        } else {
                            //没有四个点，提示错误
                            showError();
                            if (listener != null) {
                                listener.onError("至少需要连接" + minPiece + "个点，请重试");
                            }
                        }
                    } else {
                        //必须要画四个点
                        String temp = StringUtils.join(tempPositionList, ",");
                        if (temp.equals(result)) {
                            if (listener != null) {
                                //手势设置成功
                                listener.onSetSuccess();
                            }
                        } else {
                            if (listener != null) {
                                listener.onError("两次手势不一致，请重试！");
                            }
                            showError();
                        }
                    }
                }
                postDelayed(() -> {
                    positionList.clear();
                    invalidate();
                    isError = false;
                }, 500);

                break;
        }
        return true;
    }

    /**
     * 展示错误的红线
     */
    private void showError() {
        isError = true;
        invalidate();
    }

    /**
     * 找当前的位置
     *
     * @return
     */
    public int findCurrentPosition(float x, float y) {
        for (Piece piece : pieces) {
            boolean b = piece.pointIsInCircle(x, y);
            if (b) {
                KLog.d("得到postion：" + piece.getPosition());
                return piece.getPosition();
            }
        }
        return -1;
    }

    /**
     * 监听器
     */
    public interface OnVertifyListener {
        /**
         * 解锁成功
         */
        void onUnlockSuccess();

        /**
         * 解锁失败
         */
        void onUnlockError();

        /**
         * 手势第一次确认成功
         * @param msg 信息
         */
        void onConfirmSuccess(String msg);

        /**
         * 手势设置成功
         */
        void onSetSuccess();

        /**
         * 错误信息提示
         * @param error
         */
        void onError(String error);
    }
}
