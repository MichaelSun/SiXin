package com.renren.mobile.web.branches.api_11;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * at 8:42 PM, 4/16/12
 *
 * @author afpro
 */
public class HardwareAccelerate {
    public static void enable(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    public static void enable(Activity activity) {
        enable(activity.getWindow());
    }

    public static void disable(Window window) {
        window.setFlags(0, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    public static void disable(Activity activity) {
        activity.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    public static void disableLayer(View view) {
        if (view != null) {
            view.setLayerType(View.LAYER_TYPE_NONE, null);
        }
    }

    public static void enableHardwareLayer(View view) {
        if (view != null) {
            view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }
}
