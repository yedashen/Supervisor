package com.tyj.supervisor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tyj.supervisor.calendar.CalendarAdapter;
import com.tyj.supervisor.calendar.CalendarBean;
import com.tyj.supervisor.calendar.CalendarDateView;
import com.tyj.supervisor.calendar.CalendarUtil;
import com.tyj.supervisor.calendar.CalendarView;

import java.util.Date;

/**
 * @author ChenYe
 *         created by on 2018/1/3 0003. 17:39
 **/

public class CalendarDialog extends Dialog {

    private int mDuration = 600;
    private int mEndDuration = 400;
    private CalendarDateView mDateView;
    private LinearLayout mAnimationLayout;
    private RelativeLayout mOutLayout;
    private TextView mTitleTv;
    private AnimatorSet mShowAnimation;
    private AnimatorSet mDismissAnimation;
    private OnSelectDateListener mListener;

    public CalendarDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_vp);
        mDateView = (CalendarDateView) findViewById(R.id.calendar_view);
        mAnimationLayout = (LinearLayout) findViewById(R.id.animation_layout);
        mOutLayout = (RelativeLayout) findViewById(R.id.out_layout);
        mTitleTv = (TextView) findViewById(R.id.title);
        initCalendarDateView(mDateView);
        int[] data = CalendarUtil.getYMD(new Date());
        mTitleTv.setText(data[0] + "/" + data[1] + "/" + data[2]);
        mOutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowAnimation.isRunning()) {
                    mShowAnimation.cancel();
                }
                mOutLayout.setClickable(false);
                mDismissAnimation.start();
            }
        });
        setupShowAnimation();
        setupDismissAnimation();
        mTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //暂时把这里定为确定按钮的效果
                dismiss();
                if (mListener != null) {
                    mListener.onSelectClick(convertDate());
                }
            }
        });
    }

    private String convertDate() {
        String date = mTitleTv.getText().toString();
        String[] strings = date.split("/");
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            if (string.length() < 2) {
                result.append("0" + string + "-");
            } else {
                result.append(string + "-");
            }
        }
        return result.substring(0, result.length() - 1);
    }

    private void initCalendarDateView(CalendarDateView dateView) {
        dateView.setAdapter(new CalendarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_xiaomi, null);
                }
                TextView text = (TextView) convertView.findViewById(R.id.text);
                text.setText("" + bean.day);
                if (bean.monthFlag != 0) {
                    text.setTextColor(0xff9299a1);
                } else {
                    text.setTextColor(0xff444444);
                }
                return convertView;
            }
        });
        mDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CalendarBean bean) {
                mTitleTv.setText(bean.year + "/" + bean.month + "/" + bean.day);
            }
        });
    }

    @Override
    public void show() {
        super.show();
        mShowAnimation.start();
        mOutLayout.setClickable(true);
    }

    private void setupShowAnimation() {
        mShowAnimation = new AnimatorSet();
        mShowAnimation.playTogether(
//                //设置view按照Y周旋转的过程，从-90°到0°
//                ObjectAnimator.ofFloat(this, "rotationY", -90, 0).setDuration(mDuration),
//                //设置view在X轴的位移过程
                ObjectAnimator.ofFloat(mAnimationLayout, "translationY", -800, -600, 0).setDuration(mDuration),
                ObjectAnimator.ofFloat(mAnimationLayout, "translationX", -500, -300, 0).setDuration(mDuration),
//
//                //定义view的旋转过程，从1080度转到0度，旋转两圈
//                ObjectAnimator.ofFloat(mainLayout, "rotation", 360, 0).setDuration(mDuration),
//                //定义view的透明度从全隐，到全显的过程，setDuration(mDuration)是设置动画时间
                ObjectAnimator.ofFloat(mAnimationLayout, "alpha", 0, 1).setDuration(mDuration * 3 / 2),
                //定义view基于scaleX的缩放过程
                ObjectAnimator.ofFloat(mAnimationLayout, "scaleX", 0.1f, 0.5f, 1).setDuration(mDuration),
                ObjectAnimator.ofFloat(mAnimationLayout, "scaleY", 0.1f, 0.5f, 1).setDuration(mDuration)
        );
    }

    public void setupDismissAnimation() {
        mDismissAnimation = new AnimatorSet();
        mDismissAnimation.playTogether(
                ObjectAnimator.ofFloat(mAnimationLayout, "translationY", 0, -600, -800).setDuration(mEndDuration),
                ObjectAnimator.ofFloat(mAnimationLayout, "translationX", 0, -300, -500).setDuration(mEndDuration),
                ObjectAnimator.ofFloat(mAnimationLayout, "alpha", 1, 0).setDuration(mEndDuration * 3 / 2),
                ObjectAnimator.ofFloat(mAnimationLayout, "scaleX", 1, 0.5f, 0.1f).setDuration(mEndDuration),
                ObjectAnimator.ofFloat(mAnimationLayout, "scaleY", 1, 0.5f, 0.1f).setDuration(mEndDuration)
        );
        mDismissAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dismiss();
            }
        });
    }

    public interface OnSelectDateListener {
        /**
         * 从日历中选好了日期
         *
         * @param date
         */
        void onSelectClick(String date);
    }

    public void setSelectDateListener(OnSelectDateListener listener) {
        this.mListener = listener;
    }
}
