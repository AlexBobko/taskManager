package loc.task.loader;

import loc.task.util.HibernateUtil;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PersonLoader {
    private static Logger log = Logger.getLogger(PersonLoader.class);
    public static HibernateUtil util = null;

    public static void main(String[] args) throws Exception {

        Locale.setDefault(Locale.US);
        util = HibernateUtil.getHibernateUtil();
        System.out.println("Start Menu");
        SimpleDateFormat sdfout = new SimpleDateFormat("yyyy.MM.dd");
        String date = "2012.12.20";
//        java.sql.Date sqlDate = new java.sql.Date(sdfout.parse(date).getTime());

        MenuLoader.menu();
    }

}/*
        String inTableName = new String(tableName);
        int in = inTableName.length();
        inTableName = inTableName.replaceAll("[A-Z]", "");
        int out = inTableName.length();
        int result = in - out;
//        System.out.print(in + " - " + out + " = " + result + " inCamel " + inTableName);
        if (result > 1) {
            String regex = "([A-Z][a-z]+)";
            String replacement = "$1_";
            tableName = tableName.replaceAll(regex, replacement).toLowerCase();
            tableName = tableName.substring(0, tableName.length() - 1);
        }else {
            tableName.concat("s").toLowerCase(); //TODO s на es ?
        }
//        System.out.println("*************" + camel);
*/

