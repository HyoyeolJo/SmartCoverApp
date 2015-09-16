package com.secmem.android.app.view;


import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;


public class RoundedDrawable extends Drawable{
	private Bitmap mBitmap;
	private Paint mPaint;
	private RectF mRectF;
	private int mBitmapWidth;
	private int mBitmapHeight;
	
	public RoundedDrawable(Bitmap bitmap) {
		// TODO Auto-generated constructor stub
		mBitmap = bitmap;
		mRectF = new RectF();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		final BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		mPaint.setShader(shader);
		
		mBitmapWidth = mBitmap.getWidth();
		mBitmapHeight = mBitmap.getHeight();
	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawOval(mRectF, mPaint);
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		if(mPaint.getAlpha()!=alpha){
			mPaint.setAlpha(alpha);
			invalidateSelf();
		}
		
	}
	
	@Override
	protected void onBoundsChange(Rect bounds) {
		// TODO Auto-generated method stub
		super.onBoundsChange(bounds);
		mRectF.set(bounds);
	}

	
	@Override
	public int getIntrinsicWidth() {
		// TODO Auto-generated method stub
		return mBitmapWidth;
	}



	@Override
	public int getIntrinsicHeight() {
		// TODO Auto-generated method stub
		return mBitmapHeight;
	}


	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		mPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return PixelFormat.TRANSLUCENT;
	}
	
	@Override
	public void setFilterBitmap(boolean filter) {
		// TODO Auto-generated method stub
		mPaint.setFilterBitmap(filter);
		invalidateSelf();
	}

	
	@Override
	public void setDither(boolean dither) {
		// TODO Auto-generated method stub
		super.setDither(dither);
		invalidateSelf();
	}	
	
	public Bitmap getBitmap(){
		return mBitmap;
	}
	

}
