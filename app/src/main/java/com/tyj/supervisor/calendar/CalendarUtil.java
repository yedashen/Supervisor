package com.tyj.supervisor.calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * @author ChenYe
 */
public class CalendarUtil {

    /**
     * 获取指定年份、月份、日期的日期在当月是周几
     *
     * @param y
     * @param m
     * @param day
     * @return
     */
    public static int getDayOfWeek(int y, int m, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取指定年份、月份的一个月有多少天
     *
     * @param y
     * @param m
     * @return
     */
    public static int getDayOfMonth(int y, int m) {
        Calendar cal = Calendar.getInstance();
        cal.set(y, m - 1, 1);
        int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
        return dateOfMonth;
    }

    /**
     * 返回指定Date对象的年、月（+1）、日
     *
     * @param date
     * @return
     */
    public static int[] getYMD(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new int[]{cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)};
    }

}
