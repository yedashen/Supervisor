package com.tyj.supervisor.calendar;

/**
 * @author ChenYe
 *         created by on 2018/1/3 0003. 09:16
 *         日历（viewPager）里面月（item）的每一个日期数据bean
 **/

public class CalendarBean {
    public int year;
    public int month;
    public int day;
    public int week;

    /**
     * -1 代表是当前显示的月份的上一个月份的“尾巴”，比如当前日历显示的是8月，可是8月第一天是周三，那么周日、周一、周二要么显示空的，要么显示上一个月的最后几天
     * 0 代表这个对象是属于当前月份的
     * 1 代表是当前显示的月份的下一个月份的“头部”，比如当前日历显示的是8月，可是8月最后一天是周四，那么周五、周六要么显示空的，要么显示下一个月的前几天
     */
    public int monthFlag;

    public CalendarBean(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public String toString() {
        String s = year + "/" + month + "/" + day;
        return s;
    }
}
