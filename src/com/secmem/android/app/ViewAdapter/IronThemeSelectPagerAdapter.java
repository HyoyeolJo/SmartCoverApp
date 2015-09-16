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



public class IronThemeSelectPagerAdapter extends PagerAdapter {
	
	Context mContext;
	Handler handler;
	
	public IronThemeSelectPagerAdapter(Context context, Handler handler) {
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
			v = inflater.inflate(R.layout.iron1_theme_menu_item, (ViewGroup) null, false);
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.iron1_theme_item))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("ironTheme", "0");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});

			break;
		case 1:
			v = inflater.inflate(R.layout.iron2_theme_menu_item, (ViewGroup) null, false);
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.iron2_theme_item))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("ironTheme", "1");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});

			break;

		case 2:
			v = inflater.inflate(R.layout.iron3_theme_menu_item, (ViewGroup) null, false);
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.iron3_theme_item))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("ironTheme", "2");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});
			break;
		case 3:
			v = inflater.inflate(R.layout.iron4_theme_menu_item, (ViewGroup) null, false);
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.iron4_theme_item))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("ironTheme", "3");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});
			break;			
		case 4:
			v = inflater.inflate(R.layout.iron5_theme_menu_item, (ViewGroup) null, false);
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.iron5_theme_item))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("ironTheme", "4");
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
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}