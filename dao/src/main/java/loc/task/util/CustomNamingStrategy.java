package loc.task.util;

import org.hibernate.cfg.DefaultNamingStrategy;

public class CustomNamingStrategy extends DefaultNamingStrategy {
    public String classToTableName(String className) {

        String inTableName = new String(className);
        int in = inTableName.length();
        inTableName = inTableName.replaceAll("[A-Z]", "");
        int out = inTableName.length();
        int result = in - out;
//        System.out.print(in + " - " + out + " = " + result + " inCamel " + inTableName);
        if (result > 1) {
            String regex = "([A-Z][a-z]+)";
            String replacement = "$1_";
            className = className.replaceAll(regex, replacement);
//            className = className.replaceAll(regex, replacement).toLowerCase();
            className = className.substring(0, className.length() - 1);
        }
        className=className.toLowerCase();


        return className;
//        return "T_" + super.classToTableName(className).toUpperCase();
    }

    public String propertyToColumnName(String propName) {
        return "F_" + super.propertyToColumnName(propName);
    }

    public String columnName(String columnName) {
        return columnName;
    }

    public String tableName(String tableName) {

        return tableName;
    }
}


