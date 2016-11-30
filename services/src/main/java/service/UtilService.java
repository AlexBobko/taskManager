package service;

import java.util.Date;

public class UtilService {
    public static Date convertDate(String postDate) {
        String[] dl = postDate.split("/");
        int year = Integer.parseInt(dl[2]);
        int month = Integer.parseInt(dl[0]);
        int dayOfMonth = Integer.parseInt(dl[1]);
        int defaultHour = 20;
        int defaultMinute = 15;
        Date bodyDeadline = new Date(year, month, dayOfMonth, defaultHour, defaultMinute);
        return bodyDeadline;
    }
}
