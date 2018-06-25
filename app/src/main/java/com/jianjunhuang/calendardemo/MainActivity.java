package com.jianjunhuang.calendardemo;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianjunhuang.calendardemo.cal.BaseCalendarAdapter;
import com.jianjunhuang.calendardemo.cal.CalItem;
import com.jianjunhuang.calendardemo.cal.CalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CalendarView.OnDayClickListener {
    private Map<String, List<CalItem>> mMap = new HashMap<>();
    private CalendarView calendarView;
    private ImageView nextMonthBtn;

    private ImageView preMonthBtn;

    private TextView calDateTv;

    private CalAdapter calAdapter;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        initData();

    }

    private void initListener() {
        nextMonthBtn.setOnClickListener(this);
        preMonthBtn.setOnClickListener(this);
        calDateTv.setOnClickListener(this);
        calendarView.setOnDayClickListener(this);
    }

    private void setDateTv() {
        Calendar calendar = calendarView.getCal();
        calDateTv.setText(calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1));
    }

    private void initView() {
        calendarView = findViewById(R.id.cal_view);
        nextMonthBtn = findViewById(R.id.cal_next_month_btn);
        preMonthBtn = findViewById(R.id.cal_pre_month_btn);
        calDateTv = findViewById(R.id.cal_date_tv);
        calAdapter = new CalAdapter(new HashMap<String, List<CalItem>>());
        calendarView.setAdapter(calAdapter);
        setDateTv();
    }

    private void initData() {
        String date = "2018-06-25";
        List<CalItem> calItems = new ArrayList<>();
        CalItem calItem = new CalItem();
        calItem.setMsg("haha");
        calItem.setType(CalItem.BIRTHDAY);
        CalItem calItem2 = new CalItem();
        calItem2.setMsg("haha");
        calItem2.setType(CalItem.DUTY);
        calItems.add(calItem2);
        mMap.put(date, calItems);

        List<CalItem> calItems1 = new ArrayList<>();
        calItems1.add(calItem);
        calItems1.add(calItem);
        calItems1.add(calItem);
        calItems1.add(calItem);
        calItems1.add(calItem);

        mMap.put("2018-06-29",calItems1);

        calAdapter.setOnDataChange(mMap);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cal_next_month_btn: {
                calendarView.nextMonth();
                break;
            }
            case R.id.cal_pre_month_btn: {
                calendarView.preMonth();
                break;
            }
            case R.id.cal_date_tv: {
                calendarView.setMonth(new Date());
                break;
            }
        }
        setDateTv();
    }

    @Override
    public void onDyClick(int year, int month, int day, List<BaseCalendarAdapter.ItemData> mList) {

    }
}
