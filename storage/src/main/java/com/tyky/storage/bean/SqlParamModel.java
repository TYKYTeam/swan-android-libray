package com.tyky.storage.bean;

import com.tyky.webviewBase.model.ParamModel;

public class SqlParamModel extends ParamModel {
    private String tableName;
    private String columns;
    private String columnTypes;

    private String values;

    private String where ;
    private String whereValues ;

    private String groupBy = "";
    private String having = "";
    private String orderBy = "";
    private String limit = "";

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getHaving() {
        return having;
    }

    public void setHaving(String having) {
        this.having = having;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getWhereValues() {
        return whereValues;
    }

    public void setWhereValues(String whereValues) {
        this.whereValues = whereValues;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

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
