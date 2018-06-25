package com.jianjunhuang.calendardemo.cal;

/**
 * Created by jianjunhuang on 18-1-17.
 */

public class CalItem {
    private int year;
    private int month;
    private int day;
    private String msg;
    private int type;
    public static final int BIRTHDAY = 0;
    public static final int DUTY = 1;

    public CalItem(int year, int month, int day, String msg, int type) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.msg = msg;
        this.type = type;
    }

    public CalItem() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
