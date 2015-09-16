package com.secmem.android.app.view;



import java.util.Calendar;

import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;

public class DigitalDate extends android.widget.DigitalClock {
	private  String day = null;
    Calendar mCalendar;
    private String m12 = "MM월dd일";//h:mm:ss aa
    private String m24 = "MM월dd일";//k:mm:ss
    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;
    private Handler mHandler;
    Context mContext;

    private boolean mTickerStopped = false;

    String mFormat;

    public DigitalDate(Context context) {
        super(context);
        initClock(context);
    }

    public DigitalDate(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {
        Resources r = context.getResources();

        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
        int day_of_week = mCalendar.get ( Calendar.DAY_OF_WEEK );
        if ( day_of_week == 1 )
          day=" 일요일";
        else if ( day_of_week == 2 )
        	day=" 월요일";
        else if ( day_of_week == 3 )
        	day= " 화요일";
        else if ( day_of_week == 4 )
        	day=" 수요일";
        else if ( day_of_week == 5 )
        	day=" 목요일";
        else if ( day_of_week == 6 )
        	day=" 금요일";
        else if ( day_of_week == 7 )
        	day=" 토요일";
        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mFormatChangeObserver);
        m12=m12+day;
        m24=m24+day;
        setFormat();
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
                public void run() {
                    if (mTickerStopped) return;
                    mCalendar.setTimeInMillis(System.currentTimeMillis());
                    setText(DateFormat.format(mFormat, mCalendar));
                    invalidate();
                    long now = SystemClock.uptimeMillis();
                    long next = now + (1000 - now % 1000);
                    mHandler.postAtTime(mTicker, next);
                }
            };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return android.text.format.DateFormat.is24HourFormat(getContext());
    }

    private void setFormat() {
        if (get24HourMode()) {
            mFormat = m24;
        } else {
            mFormat = m12;
        }
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }
}