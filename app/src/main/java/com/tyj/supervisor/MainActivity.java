package com.tyj.supervisor;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tyj.supervisor.calendar.CalendarDateView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author ChenYe
 *         <p>
 *         需求：
 *         （1）点击左右箭头切换viewPager，从而切换日期。
 *         （2）手动滑动viewpager来切换日期。
 *         （3）点击日历自己选定日期。
 *         只要日期改变，那么就刷新viewpager下面的列表，目前这里使用tv的更换setText来达到相同效果
 */
public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TextView mResultTv;
    private LinearLayout mainLayout;
    private CalendarDateView dateView;
    private RelativeLayout mOutLayout;
    private Dialog mDialog;
    private CalendarDialog mCalendarDialog;
    private static final String TAG = "MainActivity";
    private DateAdapter mDateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.date_vp);
        mResultTv = (TextView) findViewById(R.id.result_tv);
        initViewPager();
    }

    private void initViewPager() {
        mDateAdapter = new DateAdapter(this);
        mViewPager.setAdapter(mDateAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mResultTv.setText(mDateAdapter.getTime(position));
            }
        });
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
    }

    public void selectDate(View view) {
        if (mCalendarDialog == null) {
            mCalendarDialog = new CalendarDialog(this);
            mCalendarDialog.setSelectDateListener(new CalendarDialog.OnSelectDateListener() {
                @Override
                public void onSelectClick(String date) {
                    //通过日历选好日期之后干两件事，第一件事是把viewpager的日期改成显示选定的日期
                    mResultTv.setText(date);
                    try {
                        mDateAdapter.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime(), mViewPager.getCurrentItem());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        mResultTv.setText(new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()));
                        Toast.makeText(MainActivity.this, "日期转换出错了", Toast.LENGTH_SHORT).show();
                        mDateAdapter.setDate(System.currentTimeMillis(), mViewPager.getCurrentItem());
                    }
                }
            });
        }
        mCalendarDialog.show();
    }

    public void preDay(View view) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
    }

    public void nextDay(View view) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
    }
}
