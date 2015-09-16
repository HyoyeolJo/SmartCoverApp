package com.secmem.android.app.ViewAdapter;

import com.secmem.android.app.coverappb.R;
import com.secmem.android.app.coverappb.R.drawable;
import com.secmem.android.app.coverappb.CommonInfo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeMenuPagerContainer extends FrameLayout implements ViewPager.OnPageChangeListener {
	 
    private ViewPager mPager;
    boolean mNeedsRedraw = false;
 
    public HomeMenuPagerContainer(Context context) {
        super(context);
        init();
    }
 
    public HomeMenuPagerContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
 
    public HomeMenuPagerContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
 
    private void init() {
        //Disable clipping of children so non-selected pages are visible
        setClipChildren(false);
 
        //Child clipping doesn't work with hardware acceleration in Android 3.x/4.x
        //You need to set this value here if using hardware acceleration in an
        // application targeted at these releases.
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
 
    @Override
    protected void onFinishInflate() {
        try {
            mPager = (ViewPager) getChildAt(0);
            mPager.setOnPageChangeListener(this);
        } catch (Exception e) {
            throw new IllegalStateException("The root child of PagerContainer must be a ViewPager");
        }
    }
 
    public ViewPager getViewPager() {
        return mPager;
    }
 
    private Point mCenter = new Point();
    private Point mInitialTouch = new Point();
 
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenter.x = w / 2;
        mCenter.y = h / 2;
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //We capture any touches not already handled by the ViewPager
        // to implement scrolling from a touch outside the pager bounds.
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialTouch.x = (int)ev.getX();
                mInitialTouch.y = (int)ev.getY();
            default:
                ev.offsetLocation(mCenter.x - mInitialTouch.x, mCenter.y - mInitialTouch.y);
                break;
        }
 
        return mPager.dispatchTouchEvent(ev);
    }
 
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Force the container to redraw on scrolling.
        //Without this the outer pages render initially and then stay static

        if (mNeedsRedraw) invalidate();
      
    	for(int i=0; i<mPager.getChildCount(); i++){
    		View v = (View)mPager.getChildAt(i);
    		
    		if(v.getTag().equals("ic_action_call_focus")){
    			changeImageResource(v,R.id.callBtn, i, position, R.drawable.ic_action_call_focus, R.drawable.ic_action_call);
    		}else if(v.getTag().equals("ic_action_email_focus")){
    			changeImageResource(v,R.id.smsBtn, i, position, R.drawable.ic_action_email_focus, R.drawable.ic_action_email);
    		}else if(v.getTag().equals("ic_action_headphones_focus")){
    			changeImageResource(v,R.id.musicBtn,i, position, R.drawable.ic_action_headphones_focus, R.drawable.ic_action_headphones);
    		}else if(v.getTag().equals("ic_action_settings_focus")){
    			changeImageResource(v,R.id.settingBtn, i, position, R.drawable.ic_action_settings_focus, R.drawable.ic_action_settings);
    		}else if(v.getTag().equals("ic_action_view_as_grid_focus")){
    			changeImageResource(v,R.id.customBtn, i, position, R.drawable.ic_action_view_as_grid_focus, R.drawable.ic_action_view_as_grid);
    		}
 
    	}
        Log.d(CommonInfo.TAG,"onPageScrolled "+ position);
    }
    
    private void changeImageResource(View v,int chidId,int i,int position, int resource,int orgresource){
    	ImageView menu = (ImageView)v.findViewById(chidId);
 		if(i==position){
 			menu.setImageResource(resource);
		}else{
			menu.setImageResource(orgresource);
		}   
    } 
 
    @Override
    public void onPageSelected(int position) { 
    	//Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
    	
    	for(int i=0; i<mPager.getChildCount(); i++){
    		View v = (View)mPager.getChildAt(i);
    		
    		if(v.getTag().equals("ic_action_call_focus")){
    			changeImageResource(v,R.id.callBtn, i, position, R.drawable.ic_action_call_focus, R.drawable.ic_action_call);
    		}else if(v.getTag().equals("ic_action_email_focus")){
    			changeImageResource(v,R.id.smsBtn, i, position, R.drawable.ic_action_email_focus, R.drawable.ic_action_email);
    		}else if(v.getTag().equals("ic_action_headphones_focus")){
    			changeImageResource(v,R.id.musicBtn,i, position, R.drawable.ic_action_headphones_focus, R.drawable.ic_action_headphones);
    		}else if(v.getTag().equals("ic_action_settings_focus")){
    			changeImageResource(v,R.id.settingBtn, i, position, R.drawable.ic_action_settings_focus, R.drawable.ic_action_settings);
    		}else if(v.getTag().equals("ic_action_view_as_grid_focus")){
    			changeImageResource(v,R.id.customBtn, i, position, R.drawable.ic_action_view_as_grid_focus, R.drawable.ic_action_view_as_grid);
    		}
 
    	}
    	
    	
    	Log.d(CommonInfo.TAG,"onPageSelected "+ position);
    }
 
    @Override
    public void onPageScrollStateChanged(int state) {
        mNeedsRedraw = (state != ViewPager.SCROLL_STATE_IDLE);

        
        Log.d(CommonInfo.TAG,"onPageScrollStateChanged "+ mNeedsRedraw);
    }
}