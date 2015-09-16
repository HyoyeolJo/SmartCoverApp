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



public class ArcAnimationSelectPagerAdapter extends PagerAdapter {
	
	Context mContext;
	Handler handler;
	
	public ArcAnimationSelectPagerAdapter(Context context, Handler handler) {
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
			v = inflater.inflate(R.layout.arc1_menu_item, (ViewGroup) null, false);
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.arc1_item))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("arcAnimation", "0");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});

			break;
		case 1:
			v = inflater.inflate(R.layout.arc2_menu_item, (ViewGroup) null, false);
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.arc2_item))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("arcAnimation", "1");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});

			break;

		case 2:
			v = inflater.inflate(R.layout.arc3_menu_item, (ViewGroup) null, false);
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.arc3_item))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("arcAnimation", "2");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});
			break;
	
		case 3:
			v = inflater.inflate(R.layout.arc4_menu_item, (ViewGroup) null, false);
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.arc4_item))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("arcAnimation", "3");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});
			break;
		case 4:
			v = inflater.inflate(R.layout.arc5_menu_item, (ViewGroup) null, false);
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.arc5_item))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("arcAnimation", "4");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});
			break;			
		case 5:
			v = inflater.inflate(R.layout.arc6_menu_item, (ViewGroup) null, false);
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.arc6_item))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("arcAnimation", "5");
							Message msg = new Message();
							msg.setData(b1);
							handler.sendMessage(msg);
						}
					});
			break;					
		case 6:
			v = inflater.inflate(R.layout.arc7_menu_item, (ViewGroup) null, false);
			setScale(v,0.7f,1.0f);
			((ImageView) v.findViewById(R.id.arc7_item))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Bundle b1 = new Bundle();
							b1.putString("arcAnimation", "6");
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
        return 7;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}