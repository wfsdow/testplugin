package com.behappy.datelibrary;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

/**
 *@date 2019/10/12
 *@author ningwei
 *@description 计算日期间隔的工具类
 */
public class DateCalculationActivity extends AppCompatActivity {

    private TextView startTV;
    private TextView endTV;
    private TextView getTimeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_calculation);

        initView();
    }

    private void initView() {
        startTV = findViewById(R.id.start);
        endTV = findViewById(R.id.end);
        getTimeTV = findViewById(R.id.getTime);

        getTimeTV.setOnClickListener( v -> {
            showDialog();
        });
    }

    public void showDialog() {
        DialogFragment dialog = new DateDialogFragment();
        dialog.show(getSupportFragmentManager(),"timeSelector");
    }

    private static final String TAG = "DateCalculationActivity";

    public void setSelectedDate(List<CalendarDay> list) {
        if (list == null || list.size() < 2) {
            return;
        }
        CalendarDay start = list.get(0);
        CalendarDay end = list.get(list.size()-1);

        startTV.setText(start.toString());
        endTV.setText(end.toString());

       int interval =  end.getDate().getDayOfYear() - start.getDate().getDayOfYear();
       getTimeTV.setText(String.valueOf(interval));
    }
}
