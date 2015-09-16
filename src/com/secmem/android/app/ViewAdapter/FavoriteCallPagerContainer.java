package com.secmem.android.app.ViewAdapter;


import com.secmem.android.app.Data.coverappData;
import com.secmem.android.app.coverappb.R;
import com.secmem.android.app.coverappb.R.drawable;
import com.secmem.android.app.coverappb.CommonInfo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FavoriteCallPagerContainer extends FrameLayout implements ViewPager.OnPageChangeListener {
	 
	private ViewPager mPager;
    private boolean mNeedsRedraw = false;
    private int selectedIndex = 0;
    private LayoutInflater inflater;
    private Context mContext;
    private int totalCount=0;
 
    private TextView favorite_pager_count;
    
    public void updateCountText(){
    	favorite_pager_count.setText(1+"/"+totalCount);
    }
    
    public void setFavorite_pager_count(TextView favorite_pager_count,int count) {
		this.favorite_pager_count = favorite_pager_count;
		totalCount=count;
	}
    
    public void setFavorite_pager_count(int count) {
		totalCount=count;
	}
 
	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
    public FavoriteCallPagerContainer(Context context) {
        super(context);
        mContext = context;
        init();
        
    }
    public FavoriteCallPagerContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }
 
    public FavoriteCallPagerContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
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
        Log.d(CommonInfo.TAG,"onPageScrolled "+ position);
    }
  
    @Override
    public void onPageSelected(int position) { 
    	//Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
    	Log.d(CommonInfo.TAG,"onPageSelected "+ position);
    	setSelectedIndex(position);
    	favorite_pager_count.setText(position+1+"/"+totalCount);
    	((FavoriteCallPagerAdapter)mPager.getAdapter()).setSelectedIdx(position);
    }
 
    @Override
    public void onPageScrollStateChanged(int state) {
        mNeedsRedraw = (state != ViewPager.SCROLL_STATE_IDLE);
        Log.d(CommonInfo.TAG,"onPageScrollStateChanged "+ mNeedsRedraw);
    }
}