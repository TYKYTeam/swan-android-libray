package com.tyky.storage;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tyky.storage.bean.SqlParamModel;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

import java.util.List;
import java.util.Map;

@WebViewInterface("storage")
public class StorageJsInterface {
    Gson gson = GsonUtils.getGson();

    MySqlHelper mySqlHelper = new MySqlHelper();

    /**
     * 保存数据
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String saveStorage(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String key = paramModel.getKey();
        String value = paramModel.getValue();

        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return gson.toJson(ResultModel.errorParam());
        }
        SPUtils instance = SPUtils.getInstance();
        instance.put(key, value, true);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 获取Sp中的数据
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String getStorage(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String key = paramModel.getKey();
        if (StringUtils.isEmpty(key)) {
            return gson.toJson(ResultModel.errorParam());
        }
        String result = SPUtils.getInstance().getString(key, "");
        return gson.toJson(ResultModel.success(result));
    }

    /**
     * 原生查询（sql）
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String createTable(String paramStr) {
        SqlParamModel sqlParamModel = gson.fromJson(paramStr, SqlParamModel.class);
        String tableName = sqlParamModel.getTableName();
        String columns = sqlParamModel.getColumns();
        String columnTypes = sqlParamModel.getColumnTypes();
        if (StringUtils.isTrimEmpty(tableName) || StringUtils.isTrimEmpty(columns) || StringUtils.isTrimEmpty(columnTypes)) {
            return gson.toJson(ResultModel.errorParam());
        }
        mySqlHelper.createTable(sqlParamModel);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 插入数据
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String insertTable(String paramStr) {
        SqlParamModel sqlParamModel = gson.fromJson(paramStr, SqlParamModel.class);
        String tableName = sqlParamModel.getTableName();
        String columns = sqlParamModel.getColumns();
        String values = sqlParamModel.getValues();
        if (StringUtils.isTrimEmpty(tableName) || StringUtils.isTrimEmpty(columns) || StringUtils.isTrimEmpty(values)) {
            return gson.toJson(ResultModel.errorParam());
        }
        if (mySqlHelper.tableIsExist(tableName)) {
            int row = mySqlHelper.insertTable(sqlParamModel);
            return gson.toJson(ResultModel.success(row));
        } else {
            return gson.toJson(ResultModel.errorParam(tableName + "表不存在，请先执行创表操作！"));
        }

    }

    /**
     * 更新数据
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String updateTable(String paramStr) {
        SqlParamModel sqlParamModel = gson.fromJson(paramStr, SqlParamModel.class);
        String tableName = sqlParamModel.getTableName();
        String columns = sqlParamModel.getColumns();
        String values = sqlParamModel.getValues();
        String where = sqlParamModel.getWhere();
        String whereValues = sqlParamModel.getWhereValues();

        if (StringUtils.isTrimEmpty(tableName) || StringUtils.isTrimEmpty(columns) || StringUtils.isTrimEmpty(values) || StringUtils.isTrimEmpty(where) || StringUtils.isTrimEmpty(whereValues)) {
            return gson.toJson(ResultModel.errorParam());
        }
        if (mySqlHelper.tableIsExist(tableName)) {
            int row = mySqlHelper.updateTable(sqlParamModel);
            return gson.toJson(ResultModel.success(row));
        } else {
            return gson.toJson(ResultModel.errorParam(tableName + "表不存在,请先执行创表操作！"));
        }
    }

    /**
     * 更新数据
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String queryTable(String paramStr) {
        SqlParamModel sqlParamModel = gson.fromJson(paramStr, SqlParamModel.class);
        String tableName = sqlParamModel.getTableName();

        if (StringUtils.isTrimEmpty(tableName)) {
            return gson.toJson(ResultModel.errorParam());
        }
        if (mySqlHelper.tableIsExist(tableName)) {
            List<Map> maps = mySqlHelper.queryTable(sqlParamModel);
            return gson.toJson(ResultModel.success(maps));
        } else {
            return gson.toJson(ResultModel.errorParam(tableName + "表不存在,请先执行创表操作！"));
        }
    }

    /**
     * 删除数据
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String deleteRecord(String paramStr) {
        SqlParamModel sqlParamModel = gson.fromJson(paramStr, SqlParamModel.class);
        String tableName = sqlParamModel.getTableName();

        if (StringUtils.isTrimEmpty(tableName)) {
            return gson.toJson(ResultModel.errorParam());
        }
        if (mySqlHelper.tableIsExist(tableName)) {
            int row = mySqlHelper.deleteRecord(sqlParamModel);
            return gson.toJson(ResultModel.success(row));
        } else {
            return gson.toJson(ResultModel.errorParam(tableName + "表不存在,请先执行创表操作！"));
        }
    }

    /**
     * 判断表是否存在
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String isTableExists(String paramStr) {
        SqlParamModel sqlParamModel = gson.fromJson(paramStr, SqlParamModel.class);
        String tableName = sqlParamModel.getTableName();

        if (StringUtils.isTrimEmpty(tableName)) {
            return gson.toJson(ResultModel.errorParam());
        }
        if (mySqlHelper.tableIsExist(tableName)) {
            boolean b = mySqlHelper.tableIsExist(sqlParamModel.getTableName());
            return gson.toJson(ResultModel.success(b));
        } else {
            return gson.toJson(ResultModel.errorParam(tableName + "表不存在,请先执行创表操作！"));
        }
    }


    /**
     * 原生查询（sql）
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String rowQuery(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String content = paramModel.getContent();
        if (StringUtils.isTrimEmpty(content)) {
            return gson.toJson(ResultModel.errorParam());
        }
        if (content.startsWith("select")) {
            List<Map> result = mySqlHelper.query(content);
            return gson.toJson(ResultModel.success(result));
        }
        if (content.startsWith("update") || content.startsWith("delete")) {
            int row = mySqlHelper.executeUpdateDelete(content);
            return gson.toJson(ResultModel.success(row));
        }
        if (content.startsWith("insert")) {
            int row = mySqlHelper.insert(content);
            return gson.toJson(ResultModel.success(row));
        } else {
            mySqlHelper.executeSql(content);
            return gson.toJson(ResultModel.success(""));
        }

    }
}
