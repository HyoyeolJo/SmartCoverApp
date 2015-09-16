package com.secmem.android.app.ViewAdapter;

import java.util.ArrayList;

import com.secmem.android.app.Data.coverappData;
import com.secmem.android.app.coverappb.R;
import com.secmem.android.app.coverappb.R.id;
import com.secmem.android.app.coverappb.R.layout;
import com.secmem.android.app.coverappb.CommonInfo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class MessagePagerAdapter extends PagerAdapter {
	
	Context mContext;
	Handler handler;
	int themeSet;
	
	
	
	public MessagePagerAdapter(Context context, Handler handler, int themeset) {
		super();
		mContext = context;
		this.handler = handler;
		themeSet=themeset;
		// TODO Auto-generated constructor stub
	}
	
	 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	
		View v = new View(mContext);
		final LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	

			v = inflater.inflate(R.layout.message_info, (ViewGroup) null, false);
			switch(themeSet)
			{
			case 0:
				((RelativeLayout)v.findViewById(R.id.message_info_layout)).setBackgroundResource(R.drawable.layout_bg_blue);
				break;
			case 1:
				((RelativeLayout)v.findViewById(R.id.message_info_layout)).setBackgroundResource(R.drawable.layout_bg_gray);
				break;
			case 2:
				((RelativeLayout)v.findViewById(R.id.message_info_layout)).setBackgroundResource(R.drawable.layout_bg_mint);
				break;
			case 3:
				((RelativeLayout)v.findViewById(R.id.message_info_layout)).setBackgroundResource(R.drawable.layout_bg_red);
				break;
			case 4:
				((RelativeLayout)v.findViewById(R.id.message_info_layout)).setBackgroundResource(R.drawable.layout_bg_yellow);
				break;
			default:
				break;
			}
			
			((TextView) v.findViewById(R.id.messageContent)).setText(coverappData.messageNotificationList.get(position).getMessage());
			((TextView) v.findViewById(R.id.messageSender)).setText(coverappData.messageNotificationList.get(position).getName());
			((TextView) v.findViewById(R.id.messageSender)).setSelected(true);
			((TextView) v.findViewById(R.id.messageTime)).setText(coverappData.messageNotificationList.get(position).getTime());
			((ImageView) v.findViewById(R.id.notification_icon)).setImageDrawable(coverappData.messageNotificationList.get(position).getImgDrawable());
			
			
					
	
		Log.d(CommonInfo.TAG, "instantiateItem : "+position);
    	
 
        container.addView(v);
        
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
        Log.d(CommonInfo.TAG, "destroyItem : "+position);
    }

    @Override
    public int getCount() {
        return coverappData.messageNotificationList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}