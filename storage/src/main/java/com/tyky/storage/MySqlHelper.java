package com.tyky.storage;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.socks.library.KLog;
import com.tyky.storage.bean.SqlParamModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

public class MySqlHelper extends SQLiteOpenHelper {

    private SQLiteDatabase writableDatabase;

    public MySqlHelper() {
        super(ActivityUtils.getTopActivity(), "tyky.db", null, 1);
        writableDatabase = getWritableDatabase();
    }

    public MySqlHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 创表
     *
     * @param sqlParamModel
     */
    public void createTable(SqlParamModel sqlParamModel) {
        //对应参数获取
        String tableName = sqlParamModel.getTableName();
        String columns = sqlParamModel.getColumns();
        String columnTypes = sqlParamModel.getColumnTypes();
        //切割参数，获取数据
        String[] columnArr = columns.split(",");
        String[] columnTypeArr = columnTypes.split(",");

        //拼接创表的sql语句
        StringBuilder stringBuffer = new StringBuilder("create table if not exists ");
        stringBuffer.append(tableName).append(" ");
        stringBuffer.append("(");
        //编译拼接参数数据
        for (int i = 0; i < columnArr.length; i++) {
            String column = columnArr[i];
            String columnType = columnTypeArr[i];
            stringBuffer.append(column).append(" ").append(columnType);
            if (i != columnArr.length - 1) {
                stringBuffer.append(",");
            }
        }
        stringBuffer.append(")");
        String sql = stringBuffer.toString();
        KLog.d("执行sql：" + sql);
        //使用API执行sql语句

        writableDatabase.beginTransaction();
        try {
            writableDatabase.execSQL(sql);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    /**
     * 插入数据
     *
     * @param sqlParamModel
     */
    public int insertTable(SqlParamModel sqlParamModel) {
        try {
            //参数的获取
            String tableName = sqlParamModel.getTableName();
            String columns = sqlParamModel.getColumns();
            String values = sqlParamModel.getValues();

            //切割获取数据
            String[] columnArr = columns.split(",");
            String[] columnTypeArr = values.split(",");

            ContentValues contentValues = new ContentValues();
            //拼接数据的数值
            for (int i = 0; i < columnArr.length; i++) {
                String column = columnArr[i];
                String value = columnTypeArr[i];
                contentValues.put(column, value);
            }
            //数据库的读写操作
            writableDatabase.beginTransaction();
            long row;
            try {
                writableDatabase.setTransactionSuccessful();
                //影响的行数，之后返回结果
                row = writableDatabase.insert(tableName, null, contentValues);
            } finally {
                writableDatabase.endTransaction();
            }
            return (int) row;
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * 更新数据
     *
     * @param sqlParamModel
     */
    public int updateTable(SqlParamModel sqlParamModel) {
        try {
            //参数的获取
            String tableName = sqlParamModel.getTableName();
            String columns = sqlParamModel.getColumns();
            String values = sqlParamModel.getValues();
            String where = sqlParamModel.getWhere();
            String whereValues = sqlParamModel.getWhereValues();
            String[] whereValueArr = whereValues.split(",");
            //切割获取数据
            String[] columnArr = columns.split(",");
            String[] columnTypeArr = values.split(",");

            ContentValues contentValues = new ContentValues();
            //拼接数据的数值
            for (int i = 0; i < columnArr.length; i++) {
                String column = columnArr[i];
                String value = columnTypeArr[i];
                contentValues.put(column, value);
            }
            //数据库的读写操作
            writableDatabase.beginTransaction();
            long row;
            try {
                //影响的行数，之后返回结果
                row = writableDatabase.update(tableName, contentValues, where, whereValueArr);
                writableDatabase.setTransactionSuccessful();
            } finally {
                writableDatabase.endTransaction();
            }
            return (int) row;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 查询数据
     *
     * @param sqlParamModel
     * @return
     */
    @SuppressLint("Recycle")
    public List<Map> queryTable(SqlParamModel sqlParamModel) {
        //参数的获取
        String tableName = sqlParamModel.getTableName();
        String where = sqlParamModel.getWhere();
        String columns = sqlParamModel.getColumns();

        String[] columnArr = null;
        if (!StringUtils.isEmpty(columns)) {
            columnArr = columns.split(",");
        }

        //获取前端传来的参数
        String whereValues = sqlParamModel.getWhereValues();
        String groupBy = sqlParamModel.getGroupBy();
        String having = sqlParamModel.getHaving();
        String orderBy = sqlParamModel.getOrderBy();
        String limit = sqlParamModel.getLimit();
        String[] whereValueArr = null;
        if (!TextUtils.isEmpty(whereValues)) {
            whereValueArr = whereValues.split(",");
        }

        List<Map> list = new ArrayList<>();
        //开启事务
        writableDatabase.beginTransaction();
        try {
            Cursor cursor = writableDatabase.query(tableName, columnArr, where, whereValueArr, groupBy, having, orderBy, limit);
            //Cursor cursor = writableDatabase.query(tableName, columnArr, where, whereValueArr, groupBy, having, orderBy, limit);
            //遍历游标，取得所有数据
            while (cursor.moveToNext()) {
                Map<String, Object> map = new HashMap<>();
                int columnCount = cursor.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = cursor.getColumnName(i);
                    String value = cursor.getString(i);
                    map.put(columnName, value);
                }
                list.add(map);
            }
        } finally {
            //事务结束
            writableDatabase.endTransaction();
        }
        return list;
    }

    /**
     * 执行sql语句（一般是创表语句）
     *
     * @param sql
     */
    public void excuteSql(String sql) {

        writableDatabase.beginTransaction();
        try {
            writableDatabase.execSQL(sql);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public List<Map> query(String sql) {
        writableDatabase.beginTransaction();
        List<Map> list = new ArrayList<>();
        try {
            Cursor cursor = writableDatabase.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                Map<String, Object> map = new HashMap<>();

                int columnCount = cursor.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = cursor.getColumnName(i);
                    String value = cursor.getString(i);
                    map.put(columnName, value);
                }
                list.add(map);
            }

            cursor.close();
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }

        return list;
    }

    /**
     * 表是否存在
     *
     * @param tabName
     * @return
     */
    public boolean tableIsExist(String tabName) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }

        writableDatabase.beginTransaction();
        Cursor cursor = null;
        try {
            String sql = "select count(1) as c from sqlite_master where type ='table' and name ='" + tabName.trim() + "' ";
            cursor = writableDatabase.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            writableDatabase.endTransaction();
        }
        return result;
    }

}
