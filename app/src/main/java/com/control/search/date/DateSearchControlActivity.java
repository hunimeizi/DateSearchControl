package com.control.search.date;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.control.search.date.view.SearchByDateView;

/**
 * 日期搜索控件
 */
public class DateSearchControlActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_search_control);
        initView();
    }

    private void initView() {
        SearchByDateView activity_date_search_control_searchByDateView = (SearchByDateView) findViewById(R.id.activity_date_search_control_searchByDateView);
        activity_date_search_control_searchByDateView.setOnSearchListener(new SearchByDateView.OnSearchListener() {
            @Override
            public void onSearch(String startDate, String endDate, String username) {
                Toast.makeText(DateSearchControlActivity.this, "开始日期为：" + startDate + "   结束日期为：" + endDate, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
