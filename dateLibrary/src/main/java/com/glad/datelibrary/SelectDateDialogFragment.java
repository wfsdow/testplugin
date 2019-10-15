package com.glad.datelibrary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

/**
 *@date 2019/10/11
 *@author ningwei
 *@description 选择单个日期的dialog
 */
public class SelectDateDialogFragment extends DialogFragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.date_selector_dialog, container,false);
        initView();
        return rootView;
    }

    private void initView() {
        MaterialCalendarView calendarView = rootView.findViewById(R.id.calendarView);

        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Log.i(TAG, "onDateSelected: " + date);
                if (getActivity() instanceof CountdownActivity) {
                    ((CountdownActivity)getActivity()).setTime(date);
                }
                SelectDateDialogFragment.this.dismiss();
            }
        });
    }

    private static final String TAG = "behappy";
}
