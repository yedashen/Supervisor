package com.tyj.supervisor.calendar;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

/**
 * @author ChenYe
 *         created by on 2018/1/3 0003. 09:08
 *         日历(ViewPager)的月份(item)
 **/

public class CalendarView extends ViewGroup {

    private static final String TAG = CalendarView.class.getSimpleName();
    /**
     * 行
     */
    private int mRow = 6;

    /**
     * 列
     */
    private int mColumn = 7;

    private OnItemClickListener mItemClickListener;
    private int mItemWidth;
    private int mItemHeight;
    private List<CalendarBean> mDateList;
    private boolean isToday;
    private int mSelectPosition;
    private CalendarAdapter mAdapter;


    public interface OnItemClickListener {
        /**
         * 日期点击事件
         *
         * @param view
         * @param position
         * @param bean
         */
        void onItemClick(View view, int position, CalendarBean bean);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public CalendarView(Context context, int row) {
        super(context);
        this.mRow = row;
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //这个视图是否自己绘制
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY));
        mItemWidth = parentWidth / mColumn;
        mItemHeight = mItemWidth;

        View view = getChildAt(0);
        if (view == null) {
            return;
        }

        LayoutParams params = view.getLayoutParams();
        if (params != null && params.height > 0) {
            mItemHeight = params.height;
        }
        setMeasuredDimension(parentWidth, mItemHeight * mRow);

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(MeasureSpec.makeMeasureSpec(mItemWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mItemHeight, MeasureSpec.EXACTLY));
        }

        Log.i(TAG, "onMeasure() called with: itemHeight = [" + mItemHeight + "], itemWidth = [" + mItemWidth + "]");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            layoutChild(getChildAt(i), i, l, t, r, b);
        }
    }

    private void layoutChild(View view, int position, int l, int t, int r, int b) {
        int cc = position % mColumn;
        int cr = position / mColumn;

        int itemWidth = view.getMeasuredWidth();
        int itemHeight = view.getMeasuredHeight();

        l = cc * itemWidth;
        t = cr * itemHeight;
        r = l + itemWidth;
        b = t + itemHeight;
        view.layout(l, t, r, b);
    }

    public int getItemHeight() {
        return mItemHeight;
    }

    public void setData(List<CalendarBean> beans, boolean isToday) {
        this.mDateList = beans;
        this.isToday = isToday;
        setItem();
        requestLayout();
    }

    private void setItem() {
        mSelectPosition = -1;

        if (mAdapter == null) {
            throw new RuntimeException("请先设置接口回调");
        }
        for (int i = 0; i < mDateList.size(); i++) {
            CalendarBean bean = mDateList.get(i);
            View view = getChildAt(i);
            View childView = mAdapter.getView(view, this, bean);
            if (view == null || view != childView) {
                addViewInLayout(childView, i, childView.getLayoutParams(), true);
            }
            /**
             * 这个日历viewPager默认设置的item的position是Integer.MAX_VALUE/2，初始化之后左右滑动完毕之后，
             * 如果instantiateItem的position是Integer.MAX_VALUE/2的话就isToday == true,其实
             * isToday 为true代表是最开始进来就显示的那个月，比如是2018-02月进来的那么最开始的那个月就是2018-02
             * ,之所以把加isToday这个判断，是想：
             * （1）一般的初始化的时候把一号标记为选中，但是假如今天是2018-02-04号，那么当月最开始的标记不放在
             *    一号，二十标记为4号。
             * （2）当默认的那个月的item销毁之后又初始化的话，还是达到（1）的效果。
             */
            if (isToday && mSelectPosition == -1) {
                //是最开始的那个月 && 默认选中那个日期还没有找出来
                int[] date = CalendarUtil.getYMD(new Date());
                if (bean.year == date[0] && bean.month == date[1] && bean.day == date[2]) {
                    //把当前日期作为默认选中的日期
                    mSelectPosition = i;
                }
            } else {
                //不是最开始的那个月
                if (mSelectPosition == -1 && bean.day == 1) {
                    //默认选中的那天选一号
                    mSelectPosition = i;
                }
            }

            childView.setSelected(mSelectPosition == i);
            setItemClick(childView, i, bean);
        }
    }

    private void setItemClick(final View view, final int position, final CalendarBean bean) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectPosition != -1) {
                    getChildAt(mSelectPosition).setSelected(false);
                    getChildAt(position).setSelected(true);
                }
                mSelectPosition = position;

                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(view, position, bean);
                }
            }
        });
    }

    public void setAdapter(CalendarAdapter adapter) {
        this.mAdapter = adapter;
    }

    public Object[] getSelect() {
        return new Object[]{getChildAt(mSelectPosition), mSelectPosition, mDateList.get(mSelectPosition)};
    }

    public int[] getSelectPosition() {
        Rect rect = new Rect();
        try {
            getChildAt(mSelectPosition).getHitRect(rect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //为什么下面的第四个参数返回的不是底部
        return new int[]{rect.left, rect.top, rect.right, rect.top};
    }
}
