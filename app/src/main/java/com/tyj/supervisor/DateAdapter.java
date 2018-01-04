package com.tyj.supervisor;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ChenYe
 *         created by on 2018/1/2 0002. 16:18
 *         自动在初始化的item的时候把日期值设置进去。
 *         把getCount的中间的一个值记住，然后把当前position与这个值进行运算差值来计算应该显示的日期
 **/

public class DateAdapter extends PagerAdapter {

    private LayoutInflater mInflater;
    private final Calendar mCalendar;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private Date mDate = new Date(0L);
    private long mDefaultTimeMills;
    private Map<Integer, String> mTimes = new HashMap<>();
    private Map<Integer, TextView> mTepTvMap = new HashMap<>();

    public DateAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        mCalendar = Calendar.getInstance();
        mDefaultTimeMills = System.currentTimeMillis();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.item_date_adapter, null);
        TextView dateTv = (TextView) view.findViewById(R.id.date_tv);
        mCalendar.setTimeInMillis(mDefaultTimeMills);
        int day = mCalendar.get(Calendar.DATE);
        mCalendar.set(Calendar.DATE, day + (position - Integer.MAX_VALUE / 2));
        mDate.setTime(mCalendar.getTimeInMillis());
        dateTv.setText(mFormatter.format(mDate));
        container.addView(view);
        mTimes.put(position, mFormatter.format(mDate));
        mTepTvMap.put(position, dateTv);
        Log.e("DateAdapter", "instantiateItem():" + position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
        mTimes.remove(position);
        mTepTvMap.remove(position);
    }

    /**
     * 通过日历指定日期，那么当前adapter里面的日历的默认值就不能再用mCalendar.setTimeInMillis(System.currentTimeMillis());
     * 必须用活的
     */
    public void setDate(long millis, int position) {
        mDefaultTimeMills = millis;

        String curTime = mFormatter.format(new Date(mDefaultTimeMills));
        mTepTvMap.get(position).setText(curTime);
        mTimes.put(position, curTime);
        Log.e("DateAdapter", "curTime:" + curTime);

        String nextTime = mFormatter.format(new Date(mDefaultTimeMills + 24 * 60 * 60 * 1000));
        mTepTvMap.get(position + 1).setText(nextTime);
        mTimes.put(position + 1, nextTime);
        Log.e("DateAdapter", "nextTime:" + nextTime);

        String preTime = mFormatter.format(new Date(mDefaultTimeMills - 24 * 60 * 60 * 1000));
        mTepTvMap.get(position - 1).setText(preTime);
        mTimes.put(position - 1, preTime);
        Log.e("DateAdapter", "preTine:" + preTime);
    }

    public String getTime(int position) {
        return mTimes.get(position);
    }
}
