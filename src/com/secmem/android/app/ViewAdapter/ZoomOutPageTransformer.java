package com.secmem.android.app.ViewAdapter;

import android.support.v4.view.ViewPager;
import android.view.View;

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

   private static final float MIN_SCALE = 0.55f;         // Page전환 시 기존 View의 축소율.
   private static final float MIN_ALPHA = 0.5f;
   private float min_alpha = 0.5f;
   private float min_scale = 0.85f;
   
   public ZoomOutPageTransformer(){
   }

   public ZoomOutPageTransformer(float min_scale, float min_alpha){
	   this.min_alpha = min_alpha;
	   this.min_scale = min_scale;
   }

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
       //     view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(min_scale, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }
            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(min_alpha +
                    (scaleFactor - min_scale) /
                    (1 - min_scale) * (1 - min_alpha));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right
        }
    }

}