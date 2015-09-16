package com.secmem.android.app.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;



public class ImanMaskDrawable extends Drawable{
	
	 private Context mContext;

	 private Paint mIconPaint;
	 private Paint mContourPaint;

	 private Rect mPaddingBounds;
	 private RectF mPathBounds;

	 private Path mPath;

	 private int mIconPadding;
	 private int mContourWidth;

	 private int mIntrinsicWidth;
	 private int mIntrinsicHeight;

	 private boolean mDrawContour;

	 private char[] mIconUtfChars;
	 
	 public ImanMaskDrawable(Context context) {
		 mContext = context;

	     mIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	     mContourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	     mContourPaint.setStyle(Paint.Style.STROKE);

	     mPath = new Path();

	     mPathBounds = new RectF();
	     mPaddingBounds = new Rect();
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

}


