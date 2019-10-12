package com.behappy.datelibrary;

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
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.util.List;

public class DateDialogFragment extends DialogFragment {

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
//        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//                Log.i(TAG, "onDateSelected: "+ date.getDate() );
//            }
//        });
        calendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                Log.i(TAG, "onRangeSelected: " + dates);
                ((DateCalculationActivity)getActivity()).setSelectedDate(dates);
                DateDialogFragment.this.dismiss();
            }
        });
    }

    private static final String TAG = "behappy";
}
