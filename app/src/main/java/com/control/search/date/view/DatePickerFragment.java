package com.control.search.date.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * 日期选择框
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private OnDateSelectedListener listener;
    private int mYear, mMonth, mDay;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 根据传入的参数设置初始日期
        Bundle bundle = getArguments();
        if (bundle != null) {
            mYear = bundle.getInt("year", 0);
            mMonth = bundle.getInt("month", 0);
            mDay = bundle.getInt("day", 0);
        }
        // 设置当前时间为初始日期
        if (mYear == 0 || mMonth == 0 || mDay == 0) {
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        }
        return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (listener != null) {
            listener.onDateSelected(year, month, day);
        }
    }

    public interface OnDateSelectedListener {
        void onDateSelected(int year, int month, int day);
    }

    public void setDateSelectedListener(OnDateSelectedListener listener) {
        this.listener = listener;
    }
}