package com.secmem.android.app.view;



import com.secmem.android.app.coverappb.CommonInfo;
import com.secmem.android.app.coverappb.R;
import com.secmem.android.app.coverappb.coverappb;
import com.secmem.android.app.coverappb.coverappb.ServiceDecorView;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ArcMenu extends RelativeLayout {
    public ArcLayout getmArcLayout() {
		return mArcLayout;
	}

	public void setmArcLayout(ArcLayout mArcLayout) {
		this.mArcLayout = mArcLayout;
	}

	public ImageView getmHintView() {
		return mHintView;
	}

	public void setmHintView(ImageView mHintView) {
		this.mHintView = mHintView;
	}

	public ViewGroup getControlLayout() {
		return controlLayout;
	}

	public void setControlLayout(ViewGroup controlLayout) {
		this.controlLayout = controlLayout;
	}

	private ArcLayout mArcLayout;

    private ImageView mHintView;
    private Handler handler;
    
    Context mContext;
    
    Animation hintSwitchAnimation;
    int selectedIdx;
    

    ViewGroup controlLayout;
    
    
    Animation open_animation;
    Animation close_animation;

    
    public void setHandler(Handler handler){
    	this.handler = handler;
    }

    public ArcMenu(Context context) {
        super(context);
        init(context);
        Log.d(CommonInfo.TAG, "ArcMenu(Context context)");

    }

    public ArcMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        applyAttrs(attrs);
        Log.d(CommonInfo.TAG, "ArcMenu(Context context, AttributeSet attrs)");

    }

    public void init(Context context) {
    	mContext = context;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.arc_menu, this);
        
        
        

        mArcLayout = (ArcLayout) findViewById(R.id.item_layout);

       controlLayout = (ViewGroup) findViewById(R.id.control_layout);
       controlLayout.setClickable(true);
       
       mHintView = (ImageView) findViewById(R.id.control_hint);

       controlLayout.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			hintSwitchAnimation = createHintSwitchAnimation(mArcLayout.isExpanded());
            mHintView.startAnimation( hintSwitchAnimation);
            mArcLayout.switchState(true);
		}
	});
	
   

    }
    


    private void applyAttrs(AttributeSet attrs) {//여기가 각도를 설정하여 위로 올렸음.
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ArcLayout, 0, 0);

            float fromDegrees = a.getFloat(R.styleable.ArcLayout_fromDegrees, ArcLayout.DEFAULT_FROM_DEGREES)
            		+30.0f;
            float toDegrees = a.getFloat(R.styleable.ArcLayout_toDegrees, ArcLayout.DEFAULT_TO_DEGREES)-30.0f;
            mArcLayout.setArc(fromDegrees, toDegrees);
            
            Log.d(CommonInfo.TAG, "applyAttrs fromDegrees: "+fromDegrees);
        	Log.d(CommonInfo.TAG, "applyAttrs toDegrees: "+toDegrees);
 
            int defaultChildSize = mArcLayout.getChildSize();
            int newChildSize = a.getDimensionPixelSize(R.styleable.ArcLayout_childSize, defaultChildSize);
            mArcLayout.setChildSize(newChildSize);

            a.recycle();
        }
    }

    public void addItem(View item, OnClickListener listener) {
        mArcLayout.addView(item);
        item.setOnClickListener(getItemClickListener(listener));
    }

    private OnClickListener getItemClickListener(final OnClickListener listener) {
        return new OnClickListener() {

            @Override
            public void onClick(final View viewClicked) {
                Animation animation = bindItemAnimation(viewClicked, true, 1000);
                animation.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    	((coverappb)mContext).playSounds(1);
                    	((coverappb)mContext).getmCoverMainView().setEnabled(false);
                    	
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    	((coverappb)mContext).getmCoverMainView().setEnabled(true);
                        postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                itemDidDisappear();
		                      	Bundle b1 = new Bundle();
		    					b1.putString("ArcMenu", selectedIdx+"");
		    					Message msg = new Message();
		    					msg.setData(b1);
		    					handler.sendMessage(msg);
		    		               final int itemCount = mArcLayout.getChildCount();
		    		                for (int i = 0; i < itemCount; i++) {
		    		                    View item = mArcLayout.getChildAt(i);
		    		                    item.setVisibility(View.GONE);
		    		                }
		    					
		    					//선택된 설정 메뉴 인덱스 전달
                            }
                        }, 0);
                        
                    }
                });

                final int itemCount = mArcLayout.getChildCount();
                for (int i = 0; i < itemCount; i++) {
                    View item = mArcLayout.getChildAt(i);
                    if (viewClicked != item) {
                        bindItemAnimation(item, false, 300);
                    }
                }

                mArcLayout.invalidate();
                mHintView.startAnimation(createHintSwitchAnimation2(true));

                if (listener != null) {
                    listener.onClick(viewClicked);
                }
            }
        };
    }
    
    public void initArcMenu(ArcMenu menu, int[] itemDrawables) {
        final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(mContext);
            item.setImageResource(itemDrawables[i]);

            final int position = i;
            menu.addItem(item, new OnClickListener() {

                @Override
                public void onClick(View v) {
                	//touchTxt.setText("Setting Menu : "+(position+1));
                	selectedIdx = position+1;
                }
            });
        }
    }

    private Animation bindItemAnimation(final View child, final boolean isClicked, final long duration) {
        Animation animation = createItemDisapperAnimation(duration, isClicked);
        child.setAnimation(animation);

        return animation;
    }

    private void itemDidDisappear() {
        final int itemCount = mArcLayout.getChildCount();
        for (int i = 0; i < itemCount; i++) {
            View item = mArcLayout.getChildAt(i);
            item.clearAnimation();
        }

        mArcLayout.switchState(false);
    }

    private static Animation createItemDisapperAnimation(final long duration, final boolean isClicked) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, isClicked ? 2.0f : 0.0f, 1.0f, isClicked ? 2.0f : 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));

        animationSet.setDuration(duration);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setFillAfter(true);

        return animationSet;
    }
    private Animation createHintSwitchAnimation2(final boolean expanded) {
        Animation animation = new RotateAnimation(expanded ? 45 : 0, expanded ? 0 : 45, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setStartOffset(0);
        animation.setDuration(700);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setFillAfter(true);
        
        animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
				mHintView.setEnabled(false);
				mArcLayout.setEnabled(false);
				controlLayout.setEnabled(false);
				
				final int itemCount = mArcLayout.getChildCount();
                for (int i = 0; i < itemCount; i++) {
                    View item = mArcLayout.getChildAt(i);
                    item.setEnabled(false);
                }
                ((coverappb)mContext).getmCoverMainView().setEnabled(false);
                
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mHintView.setEnabled(true);
				mArcLayout.setEnabled(true);
				controlLayout.setEnabled(true);
				
				final int itemCount = mArcLayout.getChildCount();
                for (int i = 0; i < itemCount; i++) {
                    View item = mArcLayout.getChildAt(i);
                    item.setEnabled(true);
                }
                ((coverappb)mContext).getmCoverMainView().setEnabled(true);
			}
		});

        return animation;
    }
    
    private Animation createHintSwitchAnimation(final boolean expanded) {
        Animation animation = new RotateAnimation(expanded ? 45 : 0, expanded ? 0 : 45, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setStartOffset(0);
        animation.setDuration(700);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setFillAfter(true);
        
        animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				mHintView.setEnabled(false);
				mArcLayout.setEnabled(false);
				controlLayout.setEnabled(false);
				
				final int itemCount = mArcLayout.getChildCount();
                for (int i = 0; i < itemCount; i++) {
                    View item = mArcLayout.getChildAt(i);
                    item.setEnabled(false);
                }
                ((coverappb)mContext).getmCoverMainView().setEnabled(false);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mHintView.setEnabled(true);
				mArcLayout.setEnabled(true);
				controlLayout.setEnabled(true);
				
				final int itemCount = mArcLayout.getChildCount();
                for (int i = 0; i < itemCount; i++) {
                    View item = mArcLayout.getChildAt(i);
                    item.setEnabled(true);
                }
                
				Log.d(CommonInfo.TAG, "menu!! "+expanded);
				if(expanded){
                    Bundle b1 = new Bundle();
    				b1.putString("ArcMenu", "menuClose");
    				Message msg = new Message();
    				msg.setData(b1);
    				handler.sendMessage(msg);
				}
			
				 ((coverappb)mContext).getmCoverMainView().setEnabled(true);
			}
		});

        return animation;
    }
}
