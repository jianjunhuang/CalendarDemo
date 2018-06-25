package com.jianjunhuang.calendardemo;

import android.graphics.Color;


import com.jianjunhuang.calendardemo.cal.BaseCalendarAdapter;
import com.jianjunhuang.calendardemo.cal.CalItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.jianjunhuang.calendardemo.cal.CalItem.BIRTHDAY;


/**
 * Created by jianjunhuang on 18-1-17.
 */

public class CalAdapter extends BaseCalendarAdapter {

    private Map<String, List<CalItem>> mMap;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public CalAdapter(Map<String, List<CalItem>> mMap) {
        this.mMap = mMap;
    }

    private static final String TAG = "CalAdapter";

    @Override
    public List<ItemData> getItemData(int year, int month, int day, List<ItemData> data) {
        if (data == null) {
            data = new ArrayList<>();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month-1,day);


        List<CalItem> calItems = mMap.get(simpleDateFormat.format(calendar.getTime()));
        if (calItems != null) {
            for (int i = 0; i < calItems.size(); i++) {
                ItemData itemData = new ItemData();
                CalItem calItem = calItems.get(i);
                if (calItem.getType() == BIRTHDAY) {
                    itemData.setColor(Color.parseColor("#f4511e"));
                } else {
                    itemData.setColor(Color.parseColor("#039be5"));
                }
                itemData.setMsg(calItem.getMsg());
                data.add(itemData);
            }
        }


        return data;
    }

    public void setOnDataChange(Map<String, List<CalItem>> mMap) {
        this.mMap = mMap;
        notifyDataChange();
    }
}
