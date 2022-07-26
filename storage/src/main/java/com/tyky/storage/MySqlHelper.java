package com.tyky.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blankj.utilcode.util.ActivityUtils;
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
        writableDatabase.execSQL(sql);
        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
        
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
            //影响的行数，之后返回结果
            long row = writableDatabase.insert(tableName, null, contentValues);
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            
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
            //影响的行数，之后返回结果

            long row = writableDatabase.update(tableName, contentValues, where, whereValueArr);

            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            

            return (int) row;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 执行sql语句（一般是创表语句）
     *
     * @param sql
     */
    public void excuteSql(String sql) {

        writableDatabase.beginTransaction();
        writableDatabase.execSQL(sql);
        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
        

    }

    public List<Map> query(String sql) {

        writableDatabase.beginTransaction();

        Cursor cursor = writableDatabase.rawQuery(sql, null);
        List<Map> list = new ArrayList<>();
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
        writableDatabase.endTransaction();
        
        return list;
    }


    /**
     * 插入数据
     *
     * @return
     */
    public int insert(SqlParamModel sqlParamModel) {
        String tableName = sqlParamModel.getTableName();

        writableDatabase.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "name-value");

        long rows = writableDatabase.insert("tableName", null, contentValues);
        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
        
        return (int) rows;
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

        } catch (Exception e) {

        }
        return result;
    }

}
