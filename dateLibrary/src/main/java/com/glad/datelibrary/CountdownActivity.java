package com.glad.datelibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CountdownActivity extends AppCompatActivity {

    private TextView seletedTimeTV;
    private TextView resetTV;
    private TextView remainTimeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        initViews();
    }

    private void initViews() {
        seletedTimeTV = findViewById(R.id.timeTV);
        resetTV = findViewById(R.id.resetTV);
        remainTimeTV = findViewById(R.id.remainTV);

        String timeStr = readSelectedTime(this);
        if (TextUtils.isEmpty(timeStr)) {
            seletedTimeTV.setText("请选择指定倒计时的时间");
        }else {
            setSelectedTime(seletedTimeTV, remainTimeTV, timeStr);
        }

        resetTV.setOnClickListener(v -> {
            //点击重置按钮，更新选中日期
            DialogFragment dialog = new SelectDateDialogFragment();
            dialog.show(getSupportFragmentManager(),"timeSelector");
        });


    }

    //设置选中的时间，与当前时间的间隔
    private void setSelectedTime(TextView seletedTimeTV, TextView remainTimeTV, String timeStr) {
        seletedTimeTV.setText(timeStr);
        remainTimeTV.setText(getIntervalTime(timeStr, new Date()) + "天");
    }

    //从首选项中读取保存的时间
    private String readSelectedTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences("data_module", Context.MODE_PRIVATE);
        return sp.getString("selected_time", "");
    }

    private void updateSelectedTime(Context context, String dateStr) {
        SharedPreferences sp = context.getSharedPreferences("data_module", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("selected_time", dateStr);
        edit.apply();

        setSelectedTime(seletedTimeTV, remainTimeTV, dateStr);
    }

    /**
     * 格式为yyyy-MM-dd的两个日期字符串的间隔天数。
     * @param start
     * @param start
     * @return
     */
    public int getIntervalTime(String end, Date start) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date endDate = sdf.parse(end);

            //将当前时间转换为标准格式
            String startDateStr = sdf.format(start);
            start = sdf.parse(startDateStr);
            long interval = (endDate.getTime() - start.getTime()) / (1000 * 3600 * 24);
            return (int) interval;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  0;
    }

    public void setTime(CalendarDay date) {
        updateSelectedTime(this, date.getDate().toString());
    }
}
