package com.tyj.supervisor.calendar;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.tyj.supervisor.calendar.CalendarUtil.getDayOfWeek;


/**
 * @author ChenYe
 */

public class CalendarFactory {

    private static HashMap<String, List<CalendarBean>> cache = new HashMap<>();
    private static final String TAG = CalendarFactory.class.getSimpleName();

    /**
     * 获取指定年、月的每一天并封装成一个对象，然后装到集合里面返回
     *
     * @param y
     * @param m
     * @return
     */
    public static List<CalendarBean> getMonthOfDayList(int y, int m) {

        Log.i(TAG, "year:" + y + "  month:" + m);
        //下面做了一个优化，如果之前取到过该月份的数据就之间拿出来返回
        String key = y + "" + m;
        if (cache.containsKey(key)) {
            List<CalendarBean> list = cache.get(key);
            if (list == null) {
                cache.remove(key);
            } else {
                return list;
            }
        }

        List<CalendarBean> list = new ArrayList<CalendarBean>();
        cache.put(key, list);

        //计算出当月第一天是星期几
        int fweek = getDayOfWeek(y, m, 1);
        int total = CalendarUtil.getDayOfMonth(y, m);

        //根据星期推出前面还有几个显示
        for (int i = fweek - 1; i > 0; i--) {
            CalendarBean bean = geCalendarBean(y, m, 1 - i);
            bean.monthFlag = -1;
            list.add(bean);
        }

        //获取当月的天数
        for (int i = 0; i < total; i++) {
            CalendarBean bean = geCalendarBean(y, m, i + 1);
            list.add(bean);
        }

        //显示多出当月的天数,这里我用的是要么6行要么5行
        for (int i = 0; i < 42 - (fweek - 1) - total; i++) {
            CalendarBean bean = geCalendarBean(y, m, total + i + 1);
            bean.monthFlag = 1;
            list.add(bean);
        }
        return list;
    }


    public static CalendarBean geCalendarBean(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);

        CalendarBean bean = new CalendarBean(year, month, day);
        bean.week = getDayOfWeek(year, month, day);
        return bean;
    }
}
