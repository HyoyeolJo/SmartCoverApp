package com.secmem.android.app.coverappb;



import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CommonInfo {
	public static final String TAG = "Samsung CoverAPP";
	
	/*
	 * 화면 업데이트 요청 시, 세부  TYPE 정의
	 * TYPE_HOME_UPDATE : 커버 홈 화면 요청 값
	 * TYPE_BATTARY_UPDATE : 배터리 상태 값 화면 업데이트 요청 값
	 * TYPE_SMS_UPDATE : SMS 문자 수신 화면 업데이트 요청 값
	 * TYPE_FAVORITECALL_UPDATE : 즐겨 찾기 전화 리스트 요청 값
	 * TYPE_SETTING_UPDATE : Hot Key Setting 화면 업데이트 요청 값
	 * TYPE_CUSTOM_UPDATE : 커스텀 위젯 레이아웃 화면 업데이트 요청 값
	 */
	
	public static final int TYPE_HOME_UPDATE = 100;
	public static final int TYPE_HOME_MENU = 101;
	public static final int TYPE_BATTARY_UPDATE = 102;
	public static final int TYPE_SMS_UPDATE = 103;
	public static final int TYPE_FAVORITECALL_UPDATE = 104;
	public static final int TYPE_SETTING_UPDATE = 105;
	public static final int TYPE_MUSIC_UPDATE = 106;
	public static final int TYPE_MUSICSELECT_UPDATE = 107;
	public static final int TYPE_CUSTOM_UPDATE = 108;
	public static final int TYPE_ALARM_UPDATE = 109;
	public static final int TYPE_POP_UP = 110;
	public static final int TYPE_MUSIC_EQUALIZER_UPDATE = 111;
	public static final int TYPE_SETTING_MENU_UPDATE = 112;
	public static final int TYPE_MASKSETTING_UPDATE = 113;
	public static final int TYPE_ARCANIMATION_MENU_UPDATE = 114;
	public static final int TYPE_IRONTHEME_MENU_UPDATE = 115;
	public static final int TYPE_CALLING_UPDATE = 116;
	 
	public static final String ALARM_STARTED_INTENT_NAME = "ALARM_STARTED_INTENT_NAME";
	public static final String ALARM_STOPPED_INTENT_NAME = "ALARM_STOPPED_INTENT_NAME";
	
	public static final int SHOW_PRESS = 1;
	public static final int LONG_PRESS = 2;
	public static final int TAP = 3; 
	
	public static int getPixelToDp(Context context, int pixel){
	      double dp = 0;
	      try{
	         DisplayMetrics metrics = context.getResources().getDisplayMetrics();
	         dp =  pixel/ (metrics.densityDpi);
	         Log.d("DP", "DP : "+metrics.densityDpi);
	         
	      }catch(Exception e){
	         
	      }
	      return (int)dp;
	   }
	   
	public static int getDpToPixel(Context context, int DP){
	      float px = 0;
	      try{
	         px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DP, context.getResources().getDisplayMetrics());
	      }catch(Exception e){
	         
	      }
	      return (int)px;
	}
	
	public static void setFont(ViewGroup group, Typeface font) {
	    int count = group.getChildCount();
	    View v;
	    for (int i = 0; i < count; i++) {
	        v = group.getChildAt(i);
	        if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
	       // 	if(v.getId()==R.id.ram_info || v.getId()==R.id.battry_info)continue;//숫자는 기본 폰트
	            ((TextView) v).setTypeface(font);
	        } else if (v instanceof ViewGroup)
	            setFont((ViewGroup) v, font);
	    }
	}
	
	public static int isNetworkStat( Context context ) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo lte_4g = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
        boolean blte_4g = false;
     
        int v = -1;
        if(lte_4g != null)                             
            blte_4g = lte_4g.isConnected();
        if( mobile != null ) {
            if (mobile.isConnected() || blte_4g)
                  v = 2;
        } 
        if ( wifi.isConnected() || blte_4g ){
                  v = 1;
        }
         
        return v; 
	}
	
	public static double getUsedMemorySize() {

		double usedSize = 0.0f;
	    try {

	       
	        double maxMemory = Runtime.getRuntime().maxMemory() / ( 1024.0f );
	        double allocateMemory = Debug.getNativeHeapAllocatedSize() / ( 1024.0f );
	     
	        Log.i( "", "최대 메모리 : " + maxMemory + "KB " );
	        Log.i( "", "사용 메모리 : " + allocateMemory + "KB " );
	        
	        usedSize = (allocateMemory / maxMemory) * 100;
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return usedSize;

	}
	
	public static final int[] SETTING_MENU_ITEM_DRAWABLES = {R.drawable.arc_menu, R.drawable.iron_man_line_menu,R.drawable.mask_menu};
	   
	   
	   
	
}

