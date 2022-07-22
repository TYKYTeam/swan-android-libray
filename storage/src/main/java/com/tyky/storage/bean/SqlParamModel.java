package com.tyky.storage.bean;

import com.tyky.webviewBase.model.ParamModel;

public class SqlParamModel  extends ParamModel {
    private String tableName;
    private String columns;
    private String columnTypes;

    private String values;



    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getColumnTypes() {
        return columnTypes;
    }

    public void setColumnTypes(String columnTypes) {
        this.columnTypes = columnTypes;
    }
}
