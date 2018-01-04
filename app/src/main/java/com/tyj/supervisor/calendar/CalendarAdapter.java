package com.tyj.supervisor.calendar;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author ChenYe
 *         created by on 2018/1/3 0003. 09:40
 **/

public interface CalendarAdapter {
    /**
     * 实现这个接口来对日历(ViewPager)的每个item(月)里面的每个日期的容器进行加载自定义xml的，我们可以把自己想要设置的布局赋值
     * 给convertView，然后对控件进行设置值。
     *
     * @param convertView 我上面描述的容器
     * @param parentView  用来提供获取context(parentView.getContext())的
     * @param bean        容器里面里的数据对象
     * @return
     */
    View getView(View convertView, ViewGroup parentView, CalendarBean bean);
}
