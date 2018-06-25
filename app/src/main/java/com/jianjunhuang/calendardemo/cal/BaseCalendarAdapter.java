package com.jianjunhuang.calendardemo.cal;

import java.util.List;

/**
 * Created by jianjunhuang on 18-1-17.
 */

public abstract class BaseCalendarAdapter {

    private OnDataChange onDataChange;

    public void registerOnDataChange(OnDataChange onDataChange) {
        this.onDataChange = onDataChange;
    }

    public abstract List<ItemData> getItemData(int year, int month, int day, List<ItemData> data);

    public void notifyDataChange() {
        onDataChange.onChange();
    }

    public interface OnDataChange {
        void onChange();
    }

    public class ItemData {
        private String msg;
        private int color;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
