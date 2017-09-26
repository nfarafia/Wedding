package com.vergiliy.wedding;

import android.support.v4.view.ViewPager;
import android.view.View;

// PageTransformer
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.9f;
    private static final float MIN_ALPHA = 0.7f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        // This page is way off-screen to the left.
        if (position < -1) { // [-Infinity,-1)
            view.setAlpha(0);

            // Modify the default slide transition to shrink the page as well
        } else if (position <= 1) { // [-1,1]

            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
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
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            // This page is way off-screen to the right.
        } else { // (1,+Infinity]
            view.setAlpha(0);
        }
    }
}