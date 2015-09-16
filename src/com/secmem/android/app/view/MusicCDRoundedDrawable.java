package com.secmem.android.app.view;

import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class MusicCDRoundedDrawable extends Drawable{
	private Bitmap mBitmap;
	private Paint mPaint;
	private RectF mRectF;
	private int mBitmapWidth;
	private int mBitmapHeight;

	public MusicCDRoundedDrawable(Bitmap bitmap) {
		// TODO Auto-generated constructor stub
		
		mBitmap = bitmap;
		mRectF = new RectF();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		final BitmapShader shader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		mPaint.setShader(shader);
				
		mBitmapWidth = mBitmap.getWidth();
		mBitmapHeight = mBitmap.getHeight();
		
		Canvas newCanvas = new Canvas(mBitmap);
		Paint p1 = new Paint(); 
		Paint p2 = new Paint();
		p2.setAntiAlias(true);
		p2.setDither(true);
		p2.setColor(Color.LTGRAY);
		p1.setAntiAlias(true);
		p1.setDither(true);
		p1.setColor(Color.WHITE);
		newCanvas.drawCircle((float)mBitmapWidth/2, (float)mBitmapHeight/2, mBitmapWidth/8, p2);
		newCanvas.drawCircle((float)mBitmapWidth/2, (float)mBitmapHeight/2, mBitmapWidth/16, p1);
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
