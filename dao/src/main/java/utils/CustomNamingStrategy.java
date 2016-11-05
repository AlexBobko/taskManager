package utils;

import org.hibernate.cfg.DefaultNamingStrategy;

public class CustomNamingStrategy extends DefaultNamingStrategy {

    public String classToTableName(String className) {
        return className;
    }
/*
    private String regex = "([A-Z][a-z]+)";
    private String replacement = "$1_";

    public String classToTableName(String className) {
        String tableName = className.replaceAll(regex, replacement).toLowerCase();
        tableName = tableName.substring(0, tableName.length() - 1);
        return super.classToTableName(tableName);
    } */

    public String propertyToColumnName(String propName) {
        return super.propertyToColumnName(propName);
    }

    public String columnName(String columnName) {
        return columnName;
    }

    public String tableName(String tableName) {
        return tableName;
    }
}