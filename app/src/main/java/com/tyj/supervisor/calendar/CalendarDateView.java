package com.tyj.supervisor.calendar;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import static com.tyj.supervisor.calendar.CalendarFactory.getMonthOfDayList;


/**
 * @author ChenYe
 *         created by on 2018/1/3 0003. 09:07
 *         日历ViewPager，每一个item就是一个月份
 **/

public class CalendarDateView extends ViewPager {

    private HashMap<Integer, CalendarView> mViews = new HashMap<>();
    private LinkedList<CalendarView> mCache = new LinkedList<>();
    private CalendarView.OnItemClickListener mOnItemClickListener;
    private int mRow = 6;
    private CalendarAdapter mAdapter;

    public CalendarDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int calendarHeight = 0;
        if (getAdapter() != null) {
            CalendarView view = (CalendarView) getChildAt(0);
            if (view != null) {
                calendarHeight = view.getMeasuredHeight();
            }
        }
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(calendarHeight, MeasureSpec.EXACTLY));
    }

    private void init() {
        final int[] dateArr = CalendarUtil.getYMD(new Date());
        setAdapter(new PagerAdapter() {
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
                CalendarView view;
                if (!mCache.isEmpty()) {
                    view = mCache.removeFirst();
                } else {
                    view = new CalendarView(container.getContext(), mRow);
                }

                view.setOnItemClickListener(mOnItemClickListener);
                view.setAdapter(mAdapter);
                view.setData(getMonthOfDayList(dateArr[0], dateArr[1] + position - Integer.MAX_VALUE / 2), position == Integer.MAX_VALUE / 2);

                container.addView(view);
                mViews.put(position, view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(((View) object));
                mCache.addLast(((CalendarView) object));
                mViews.remove(position);
            }
        });

        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (mOnItemClickListener != null) {
                    CalendarView view = mViews.get(position);
                    Object[] obs = view.getSelect();
                    mOnItemClickListener.onItemClick(((View) obs[0]), ((int) obs[1]), ((CalendarBean) obs[2]));
                }
            }
        });
    }

    private void initDefaultItem() {
        setCurrentItem(Integer.MAX_VALUE / 2, false);
        getAdapter().notifyDataSetChanged();
    }

    public void setAdapter(CalendarAdapter adapter) {
        this.mAdapter = adapter;
        initDefaultItem();
    }

    public void setOnItemClickListener(CalendarView.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
