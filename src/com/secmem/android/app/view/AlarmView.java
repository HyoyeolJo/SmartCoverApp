package com.secmem.android.app.view;


import java.util.Calendar;

import com.secmem.android.app.coverappb.R;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlarmView extends LinearLayout {

   private Context mContext;
   private View rootView;
   
   private TextView alarm_Am_Pm;
   private TextView alarm_Hour;
   private TextView alarm_Minute;
   private TextView alarm_Month;
   private TextView alarm_Day;
   private TextView alarm_Day_Of_Week;
   
   private ImageView alarmOffButton;
   
   
   public AlarmView(Context context) {
      super(context);
      // TODO Auto-generated constructor stub
      mContext = context;
      
      inflate();
      
      init();
   }
   public void inflate() {
      LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      rootView = inflater.inflate(R.layout.alarm_view, this);
   }

   public void init() {
      //alarmText = (TextView)rootView.findViewById(R.id.cover_alarm_view_data);
      

      
      
      alarmOffButton = (ImageView)rootView.findViewById(R.id.cover_alarm_view_off_alarm);
      
      alarmOffButton.setOnClickListener(new OnClickListener() {
         
         @Override
         public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent sendIntent = new Intent("com.samsung.sec.android.clockpackage.alarm.ALARM_STOP");
              sendIntent.putExtra("bDismiss", true);
              mContext.sendBroadcast(sendIntent);
         }
      });
   }
   
   public void setAlarmStatus(int id, boolean status) {
      //alarmText.setText(getAlarmData(id));
      long alarmTime = getAlarmData(id);
      Calendar cTime = Calendar.getInstance();
      
      int mTime = cTime.get(Calendar.AM_PM);      // 0 : AM, 1 : PM
      
      int mYear = cTime.get(Calendar.YEAR);
        int mMonth = cTime.get(Calendar.MONTH) + 1;
        int mDay = cTime.get(Calendar.DAY_OF_MONTH);
        
        int mHour = cTime.get(Calendar.HOUR);
        int mMinute = cTime.get(Calendar.MINUTE) + 1;
        
        int day_of_weeks = cTime.get(Calendar.DAY_OF_WEEK);
        
        if(mTime == 0)
           alarm_Am_Pm.setText("오전");
        else
           alarm_Am_Pm.setText("오후");
        
        alarm_Hour.setText(String.valueOf(mHour));
        alarm_Minute.setText(String.valueOf(mMinute));
        alarm_Month.setText(String.valueOf(mMonth));
        alarm_Day.setText(String.valueOf(mDay));
        
        switch(day_of_weeks) {
        case 0 : 
           alarm_Day_Of_Week.setText("일요일"); 
           break;
        case 1 : 
           alarm_Day_Of_Week.setText("월요일");
           break;
        case 2 : 
           alarm_Day_Of_Week.setText("화요일");
           break;
        case 3 : 
           alarm_Day_Of_Week.setText("수요일");
           break;
        case 4 : 
           alarm_Day_Of_Week.setText("목요일");
           break;
        case 5 : 
           alarm_Day_Of_Week.setText("금요일");
           break;
        case 6 : 
           alarm_Day_Of_Week.setText("토요일");
           break;
        }
        
   }
   
   private long getAlarmData(int id) {
        Log.d("CoverAlarmView", " getAlarmData() : Fetching alarm  record of alarm  id " + id);

        Uri mUri = Uri.parse("content://com.samsung.sec.android.clockpackage/alarm");

        Cursor c = mContext.getContentResolver().query(mUri, null, null, null, null);
        if (c != null) {
            Log.d("CoverAlarmView", "no of columns are " + c.getColumnCount());
            if (c.moveToFirst()) {
                do {
                    if (c.getInt(0) == id) {
                       
                        Calendar cTime = Calendar.getInstance();
                        
                        long currentTime = cTime.getTimeInMillis();
                        
                        cTime.setTimeInMillis(c.getLong(2));
                        
                        return c.getLong(2);
                        
                    }
                } while (c.moveToNext());
            }
            c.close();
        }
        
        Calendar cTime = Calendar.getInstance();
        
        long currentTime = cTime.getTimeInMillis();
        
        return currentTime;
    }
}