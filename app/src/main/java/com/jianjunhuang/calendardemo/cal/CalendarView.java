package com.jianjunhuang.calendardemo.cal;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.jianjunhuang.calendardemo.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jianjunhuang on 18-1-16.
 */

public class CalendarView extends View implements BaseCalendarAdapter.OnDataChange {

    private Paint weekPaint;
    private Paint dayPaint;
    private Paint itemTextPaint;
    private Paint linePaint;
    private Paint itemPaint;
    private Paint todayPaint;
    private Paint moreBmpPaint;

    private float textH;

    private int textSize = 26;

    private float margin = 10;

    /**
     * 当前天
     */
    private int currentDay;
    /**
     * 当月第一天在星期几
     */
    private int firstIndex;
    /**
     * 当前月的天数
     */
    private int daysOfMonth;

    private String[] weeks = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    private Calendar mCalendar;

    private BaseCalendarAdapter mAdapter;

    private List<BaseCalendarAdapter.ItemData> mItemList = new ArrayList<>();

    private Bitmap moreBitmap;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        weekPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        weekPaint.setColor(Color.DKGRAY);
        weekPaint.setTextSize(textSize);

        dayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dayPaint.setColor(Color.DKGRAY);
        dayPaint.setTextSize(textSize);

        itemTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        itemTextPaint.setColor(Color.WHITE);
        itemTextPaint.setTextSize(textSize);

        itemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        itemPaint.setColor(Color.parseColor("#039be5"));

        todayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        todayPaint.setColor(Color.parseColor("#039be5"));

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.parseColor("#f1f1f1"));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);

        Paint.FontMetrics weekFm = weekPaint.getFontMetrics();
        textH = (float) Math.ceil(weekFm.descent - weekFm.ascent);

        moreBmpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        moreBmpPaint.setColor(Color.parseColor("#f1f1f1"));

        moreBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_more_horiz_grey_800_24dp);

        initCal();
    }

    public void setAdapter(BaseCalendarAdapter mCalendarAdapter) {
        this.mAdapter = mCalendarAdapter;
        mAdapter.registerOnDataChange(this);
        invalidate();
    }


    private float getWeekHeight() {
        return 40f;
    }

    private float getCellWidth() {
        return getWidth() / 7;
    }

    private float getCellHeight() {
        return (getHeight() - getWeekHeight()) / 6;
    }

    private void initCal() {
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date());
        currentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), 1);
        firstIndex = mCalendar.get(Calendar.DAY_OF_WEEK);
        if (firstIndex == 1) {
            firstIndex = 7;
        } else {
            firstIndex--;
        }
        daysOfMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.i(TAG, "initCal: " + mCalendar.getTime().toString());
    }

    public void setMonth(Date date) {
        mCalendar.setTime(new Date());
        int nowYear = mCalendar.get(Calendar.YEAR);
        int nowMonth = mCalendar.get(Calendar.MONTH);
        int nowDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mCalendar.setTime(date);
        if (nowYear == mCalendar.get(Calendar.YEAR) && nowMonth == mCalendar.get(Calendar.MONTH)) {
            currentDay = nowDay;
        } else {
            currentDay = -1;
        }

        mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), 1);
        firstIndex = mCalendar.get(Calendar.DAY_OF_WEEK);
        if (firstIndex == 1) {
            firstIndex = 7;
        } else {
            firstIndex--;
        }
        daysOfMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        invalidate();
    }

    public Calendar getCal() {
        return mCalendar;
    }

    public void nextMonth() {
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        if (month == 11) {
            month = 0;
            year++;
        } else {
            month++;
        }
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        setMonth(mCalendar.getTime());
    }

    public void preMonth() {
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        if (month == 0) {
            month = 11;
            year--;
        } else {
            month--;
        }
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        setMonth(mCalendar.getTime());
    }

    private static final String TAG = "CalendarView";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#fafafa"));
        //week
        for (int i = 0; i < weeks.length; i++) {
            float weekX = i * getCellWidth() + margin;

            float weekY = textH + margin;
            canvas.drawText(weeks[i], weekX, weekY, weekPaint);
        }

        //day
        int day = 1;
        int tmp = 0;
        //行
        for (int i = 0; i < 6; i++) {
            //列
            for (int j = 0; j < 7; j++) {
                if (tmp >= firstIndex - 1 && tmp < daysOfMonth + firstIndex - 1) {
                    float x = j * getCellWidth() + margin;
                    float y = i * getCellHeight() + getWeekHeight() + textH + margin * 2;
                    if (day == currentDay) {
                        float textLen = dayPaint.measureText(day + "");
                        float rx = x + textLen / 2;
                        float ry = y - textH / 2 + 3;
                        canvas.drawCircle(rx, ry, textSize / 1.2f, todayPaint);
                        dayPaint.setColor(Color.WHITE);
                        canvas.drawText(day + "", x, y, dayPaint);
                        dayPaint.setColor(Color.DKGRAY);
                    } else {
                        canvas.drawText(day + "", x, y, dayPaint);
                    }
                    //item
                    if (mAdapter != null) {
                        mItemList.clear();
                        int year = mCalendar.get(Calendar.YEAR);
                        int month = mCalendar.get(Calendar.MONTH);
                        mItemList = mAdapter.getItemData(year, month + 1, day, mItemList);
                    }
                    if (mItemList != null) {

                        /**
                         * 1. 求所有 Item 所需要的高度
                         * 2. 求剩余的高度
                         * 3. 相减求出能绘制的个数 n
                         * 5. 绘制 n - 1 个item
                         * 6. 绘制省略号
                         */
                        float itemHeight = (textH + margin);
                        float leftHeight = getCellHeight() - getWeekHeight() - textH - margin;
                        int itemNum = (int) (leftHeight / itemHeight);

                        if (itemNum >= mItemList.size()) {
                            for (int k = 0; k < mItemList.size(); k++) {

                                BaseCalendarAdapter.ItemData itemData = mItemList.get(k);

                                float left = j * getCellWidth() + margin;
                                float top = i * getCellHeight() + getWeekHeight() + textH + margin * 4 + (k * (textH + margin * 2));
                                float right = j * getCellWidth() + getCellWidth() - margin;
                                float bottom = top + textH + margin;
                                itemPaint.setColor(itemData.getColor());
                                canvas.drawRoundRect(left, top, right, bottom, 10, 10, itemPaint);
                                canvas.drawText(itemData.getMsg(), left + margin / 2, bottom - margin, itemTextPaint);
                            }
                        } else {
                            int k = 0;
                            for (k = 0; k < itemNum - 1; k++) {

                                BaseCalendarAdapter.ItemData itemData = mItemList.get(k);

                                float left = j * getCellWidth() + margin;
                                float top = i * getCellHeight() + getWeekHeight() + textH + margin * 4 + (k * (textH + margin * 2));
                                float right = j * getCellWidth() + getCellWidth() - margin;
                                float bottom = top + textH + margin;
                                itemPaint.setColor(itemData.getColor());
                                canvas.drawRoundRect(left, top, right, bottom, 10, 10, itemPaint);
                                canvas.drawText(itemData.getMsg(), left + margin / 2, bottom - margin, itemTextPaint);
                            }
                            if (itemNum > 0) {

                                float left = j * getCellWidth() ;
                                float top = i * getCellHeight() + getWeekHeight() + textH + margin * 2 + (k * (textH + margin * 2));
                                float right = j * getCellWidth() + getCellWidth() - margin;
                                float bottom = top + textH + margin;
                                canvas.drawBitmap(moreBitmap, left , top - margin, moreBmpPaint);
                            }
                        }
                    }
                    day++;
                }
                tmp++;
            }
            canvas.drawLine(0, (i + 1) * getCellHeight() + getWeekHeight() - margin, getWidth(), (i + 1) * getCellHeight() + getWeekHeight(), linePaint);
        }
    }

    @Override
    public void onChange() {
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        moreBitmap.recycle();
    }

    private PointF focusPoint = new PointF();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onDayClickListener != null) {

            if (event.getY() < getWeekHeight()) {
                return true;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    focusPoint.set(event.getX(), event.getY());
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    float pX = focusPoint.x;
                    float pY = focusPoint.y - getWeekHeight();

                    float height = getHeight() - getWeekHeight();
                    float weight = getWidth();

                    float cellHeight = getCellHeight();
                    float cellWidth = getCellWidth();


                    float x = event.getX();
                    float y = event.getY() - getWeekHeight();

                    int line = (int) Math.floor(x / cellWidth);
                    int col = (int) Math.floor(y / cellHeight);

                    if (Math.floor(pX / cellWidth) == line && Math.floor(pY / cellHeight) == col) {
                        int number = line + col * 7 + 1;
                        int day = number - firstIndex + 1;
                        int year = getCal().get(Calendar.YEAR);
                        int month = getCal().get(Calendar.MONTH) + 1;

                        mItemList.clear();
                        mItemList = mAdapter.getItemData(year, month, day, mItemList);
                        onDayClickListener.onDyClick(year, month, day, mItemList);
                    }
                }
            }
        }
        return true;
    }

    private OnDayClickListener onDayClickListener;

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
    }

    public interface OnDayClickListener {

        void onDyClick(int year, int month, int day, List<BaseCalendarAdapter.ItemData> mList);

    }
}
