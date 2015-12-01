package com.sixin.widgets.interpolator;

import android.view.animation.Interpolator;

/**
 * Copy from Android's ViewPager.
 */
public class DecelerateInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float t) {
        t -= 1.0f;
        return t * t * t * t * t + 1.0f;
    }
}
