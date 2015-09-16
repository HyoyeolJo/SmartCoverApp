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

public class IronThemeSelectPagerContainer extends FrameLayout implements ViewPager.OnPageChangeListener {
	 
    private ViewPager mPager;
    boolean mNeedsRedraw = false;
    ImageView close;
    


	ImageView swipe_selected;
    TextView mark_name;
    TextView mark_count;
    boolean check = true;
    boolean coverCheck = false;
    


	public ImageView getCloseImage(){
    	return close;
    }
	
    public boolean isCoverCheck() {
		return coverCheck;
	}

	public void setCoverCheck(boolean coverCheck) {
		this.coverCheck = coverCheck;
	}
    
    public void viewVisible(){
        close.setVisibility(View.VISIBLE);
        swipe_selected.setVisibility(View.VISIBLE);
        mark_name.setVisibility(View.VISIBLE);
        mark_count.setVisibility(View.VISIBLE);
    }
  
    public IronThemeSelectPagerContainer(Context context) {
        super(context);
        init();
    }
 
    public IronThemeSelectPagerContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
 
    public IronThemeSelectPagerContainer(Context context, AttributeSet attrs, int defStyle) {
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
            close = (ImageView)getChildAt(2);
            swipe_selected = (ImageView)getChildAt(1);
            mark_name = (TextView)getChildAt(3);
            mark_count = (TextView)getChildAt(4);
            Log.d(CommonInfo.TAG, "onFinishInflate     -");
            
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

        Log.d(CommonInfo.TAG, "onPageScrolled     ---position : "+ position);
        if(check==true){
        	
        	check = false;
        }
        else
        {
        	Log.d(CommonInfo.TAG, "onPageScrolled     ---shiT!!!!!!!!!!!!!!!!fck1!! : ");
        	close.setVisibility(View.GONE);
        	swipe_selected.setVisibility(View.GONE);
        	mark_name.setVisibility(View.GONE);
        	mark_count.setVisibility(View.GONE);
        }
        
        if(coverCheck==true){
        	viewVisible();
        	coverCheck=false;
        }
        
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
    	
    	 Log.d(CommonInfo.TAG, "onPageSelected     ---position : "+ position);

    	 switch(position)
    	 {
    	 case 0:
    		 swipe_selected.setImageResource(R.drawable.swipe_right_line);
    		 mark_name.setText("War Machine\n"+"Mark II");
    		 mark_count.setText("1/5");
    		 break;
    	 case 1:
    		 swipe_selected.setImageResource(R.drawable.swipe_line);
    		 mark_name.setText("Iron Man\n"+"Mark I");
    		 mark_count.setText("2/5");
    		 break;
    	 case 2:
    		 swipe_selected.setImageResource(R.drawable.swipe_line);
    		 mark_name.setText("GAMMA\n"+"Mark XXVI");
    		 mark_count.setText("3/5");
    		 break;
    	 case 3:
    		 swipe_selected.setImageResource(R.drawable.swipe_line);
    		 mark_name.setText("Iron Man\n"+"Mark III");
    		 mark_count.setText("4/5");
    		 break;
    	 case 4:
    		 swipe_selected.setImageResource(R.drawable.swipe_left_line);
    		 mark_name.setText("JACK\n"+"Mark XXVIII");
    		 mark_count.setText("5/5");
    		 break;
    	 default:
    			 break;
    	 }
    	 
    	

    	

    }
 
    @Override
    public void onPageScrollStateChanged(int state) {
        mNeedsRedraw = (state != ViewPager.SCROLL_STATE_IDLE);

        close.setVisibility(View.VISIBLE);
        swipe_selected.setVisibility(View.VISIBLE);
        mark_name.setVisibility(View.VISIBLE);
        mark_count.setVisibility(View.VISIBLE);
        Log.d(CommonInfo.TAG,"onPageScrollStateChanged "+ mNeedsRedraw);
    }
}