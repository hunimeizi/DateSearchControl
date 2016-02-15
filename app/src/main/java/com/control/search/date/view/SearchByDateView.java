package com.control.search.date.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.control.search.date.R;
import com.control.search.date.utils.TimeSet;

/**
 * 日期搜索控件
 *
 * author lyb
 */
public class SearchByDateView extends LinearLayout {
    private Context context;

    private View usernameV;
    private EditInputBox editInputBox;
    private EditText usernameEt;

    private TextView startDateTv;
    private TextView endDateTv;
    private Button searchBtn;

    private String startDate = "";
    private String endDate = "";

    private OnSearchListener onSearchListener;

    public SearchByDateView(Context context) {
        this(context, null);
    }

    public SearchByDateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchByDateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.view_search, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        usernameV = findViewById(R.id.search_view_username_v);
        editInputBox = (EditInputBox) findViewById(R.id.search_view_username_et);
        startDateTv = (TextView) findViewById(R.id.search_view_start_date_tv);
        endDateTv = (TextView) findViewById(R.id.search_view_end_date_tv);
        searchBtn = (Button) findViewById(R.id.search_view_search_bt);

        usernameEt = editInputBox.getInputEditText();

        startDateTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                int year = 0, month = 0, day = 0;
                if (!TextUtils.isEmpty(startDate)) {
                    String[] strings = startDate.split("-");
                    if (strings.length == 3) {
                        year = Integer.parseInt(strings[0]);
                        month = Integer.parseInt(strings[1]) - 1;
                        day = Integer.parseInt(strings[2]);
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putInt("year", year);
                bundle.putInt("month", month);
                bundle.putInt("day", day);
                datePickerFragment.setArguments(bundle);
                datePickerFragment.setDateSelectedListener(new StartDateSelectedListener());
                datePickerFragment.show(((Activity) context).getFragmentManager(), "Dialog");
            }
        });

        endDateTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                int year = 0, month = 0, day = 0;
                if (!TextUtils.isEmpty(endDate)) {
                    String[] strings = endDate.split("-");
                    if (strings.length == 3) {
                        year = Integer.parseInt(strings[0]);
                        month = Integer.parseInt(strings[1]) - 1;
                        day = Integer.parseInt(strings[2]);
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putInt("year", year);
                bundle.putInt("month", month);
                bundle.putInt("day", day);
                datePickerFragment.setArguments(bundle);
                datePickerFragment.setDateSelectedListener(new EndDateSelectedListener());
                datePickerFragment.show(((Activity) context).getFragmentManager(), "Dialog");
            }
        });

        searchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEt.getText().toString().trim();
                if (onSearchListener != null) {
                    onSearchListener.onSearch(startDate, endDate, username);
                }
            }
        });
    }

    private class StartDateSelectedListener implements DatePickerFragment.OnDateSelectedListener {
        @Override
        public void onDateSelected(int year, int month, int day) {
            String date = String.valueOf(year) + "-" + (month + 1) + "-" + day;
            String nowDate = TimeSet.getDate4();
            long resultNow = TimeSet.timeDateFormat(date, nowDate);
            long resultEnd = 0;
            if (!TextUtils.isEmpty(endDate)) {
                resultEnd = TimeSet.timeDateFormat(date, endDate);
            }
            if (resultEnd > 0 || resultNow > 0) {
                // 超出结束日期或大于当前日期
                startDate = "";
                startDateTv.setText(startDate);
                Toast.makeText(context, context.getString(R.string.date_pick_tip_start), Toast.LENGTH_SHORT).show();
            } else {
                startDate = date;
                startDateTv.setText(startDate);
            }
        }
    }

    private class EndDateSelectedListener implements DatePickerFragment.OnDateSelectedListener {

        @Override
        public void onDateSelected(int year, int month, int day) {
            String date = String.valueOf(year) + "-" + (month + 1) + "-" + day;
            String nowDate = TimeSet.getDate4();
            long resultNow = TimeSet.timeDateFormat(date, nowDate);
            long resultStart = 0;
            if (!TextUtils.isEmpty(startDate)) {
                resultStart = TimeSet.timeDateFormat(date, startDate);
            }
            if (resultStart < 0 || resultNow > 0) {
                // 小于开始日期或大于当前日期
                endDate = "";
                endDateTv.setText(endDate);
                Toast.makeText(context, context.getString(R.string.date_pick_tip_end), Toast.LENGTH_SHORT).show();
            } else {
                endDate = date;
                endDateTv.setText(endDate);
            }
        }
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    public interface OnSearchListener {
        void onSearch(String startDate, String endDate, String username);
    }

    public void setUsernameViewVisible() {
        editInputBox.setVisibility(VISIBLE);
        usernameV.setVisibility(VISIBLE);
    }
}
