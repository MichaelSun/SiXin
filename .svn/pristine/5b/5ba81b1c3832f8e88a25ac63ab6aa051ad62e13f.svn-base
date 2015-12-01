package com.renren.mobile.web.animation;

import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

/**
 * at 4:26 PM, 4/18/12
 *
 * @author afpro
 */
public class ImagePopupAnimationView extends View {
    final RectF src, dst;
    final Bitmap image;
    final long duration;
    final long beginTime = System.currentTimeMillis();
    final Intent intent;
    final Handler handler = new Handler();
    final RectF rect = new RectF();

    public static void show(Context context, Bitmap bitmap, RectF src, RectF dst, long duration, Intent after) {
        final View view = new ImagePopupAnimationView(context, src, dst, bitmap, duration, after);
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(view, new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT)
        );
    }

    class Invalidate implements Runnable {
        @Override
        public void run() {
            long cost = System.currentTimeMillis() - beginTime;
            invalidate();
            if (cost <= duration) {
                handler.postDelayed(new Invalidate(), 30);
            } else {
                handler.post(new Remove());
            }
        }
    }

    class Remove implements Runnable {
        @Override
        public void run() {
            final Context context = getContext();
            final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.removeView(ImagePopupAnimationView.this);

            if (intent != null) {
                context.startActivity(intent);
            }
        }
    }

    public ImagePopupAnimationView(Context context, RectF src, RectF dst, Bitmap image, long duration, Intent intent) {
        super(context);
        this.src = new RectF(src);
        this.dst = new RectF(dst);
        this.image = image;
        this.duration = duration;
        this.intent = intent;
        handler.post(new Invalidate());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setMatrix(null);
        long gone = System.currentTimeMillis() - beginTime;
        final float ratio = gone > duration ? 1 : gone / (float) duration;
        rect.left = src.left + (dst.left - src.left) * ratio;
        rect.right = src.right + (dst.right - src.right) * ratio;
        rect.top = src.top + (dst.top - src.top) * ratio;
        rect.bottom = src.bottom + (dst.bottom - src.bottom) * ratio;

        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(image, null, rect, null);
    }
}
