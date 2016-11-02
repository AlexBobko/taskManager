package utils;

//import org.slf4j.Logger;

import org.apache.log4j.Logger;

public class MyLogger {
    private static MyLogger instance;
    private Logger logger;
    private MyLogger(){}

    public static MyLogger getInstance(){
        if(instance == null){
            synchronized (MyLogger.class) {
                if(instance == null){
                    instance = new MyLogger();
                }
            }
        }
        return instance;
    }


}
