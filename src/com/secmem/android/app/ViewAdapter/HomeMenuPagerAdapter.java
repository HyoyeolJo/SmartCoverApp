package com.secmem.android.app.ViewAdapter;

import com.secmem.android.app.coverappb.R;
import com.secmem.android.app.coverappb.R.id;
import com.secmem.android.app.coverappb.R.layout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;



public class HomeMenuPagerAdapter extends PagerAdapter {
	
	Context mContext;
	Handler handler;
	
	public HomeMenuPagerAdapter(Context context, Handler handler) {
		super();
		mContext = context;
		this.handler = handler;
		// TODO Auto-generated constructor stub
	}
	
	
	private void setScale(View view, float min_scale, float min_alpha){
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        
		 float scaleFactor = min_scale;
         float vertMargin = pageHeight * (1 - scaleFactor) / 2;
         float horzMargin = pageWidth * (1 - scaleFactor) / 2;


         view.setTranslationX(-horzMargin + vertMargin / 2);

         // Scale the page down (between MIN_SCALE and 1)
         view.setScaleX(scaleFactor);
         view.setScaleY(scaleFactor);

         // Fade the page relative to its size. 
         view.setAlpha(min_alpha +
                 (scaleFactor - min_scale) /
                 (1 - min_scale) * (1 - min_alpha));
	}
	
	 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	
		View v = new View(mContext);
		final LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
    	switch (position) {
		case 0:
			v = inflater.inflate(R.layout.call_menu_item, (ViewGroup) null, false);
			v.setTag("ic_action_call_focus");
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.callBtn))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("menu", "call");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});


			break;
		case 1:
			v = inflater.inflate(R.layout.sms_menu_item, (ViewGroup) null, false);
			v.setTag("ic_action_email_focus");
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.smsBtn))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("menu", "sms");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});


			break;

		case 2:
			v = inflater.inflate(R.layout.music_menu_item, (ViewGroup) null, false);
			v.setTag("ic_action_headphones_focus");
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.musicBtn))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("menu", "music");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});
			

			
			break;
		case 3:
			v = inflater.inflate(R.layout.setting_menu_item, (ViewGroup) null, false);
			v.setTag("ic_action_settings_focus");
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.settingBtn))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("menu", "setting");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});
			

			
			break;

		case 4:
			v = inflater.inflate(R.layout.custom_menu_item, (ViewGroup) null, false);
			v.setTag("ic_action_view_as_grid_focus");
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.customBtn))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("menu", "custom");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});
			break;			

		default:

			break;
		}
		
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}