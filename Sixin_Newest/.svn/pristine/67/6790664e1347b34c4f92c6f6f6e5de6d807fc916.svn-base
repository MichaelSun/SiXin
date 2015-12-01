package com.renren.mobile.chat.ui.imageviewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Define a helper class to load images from both internal and external storage.
 */
public final class ImageLoader {
	
    private static final String LOG_TAG = ImageLoader.class.getName();

    public static Bitmap getBitmapFromFile(String path, int maxResolutionX, int maxResolutionY) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = UriTexture.computeSampleSize(path, maxResolutionX, maxResolutionY);

        options.inDither = false;
        options.inJustDecodeBounds = false;
        return getBitmapFromFile(path, options);
    }

    public static Bitmap getBitmapFromFile(String path, int maxResolutionX, int maxResolutionY,
            Bitmap.Config config, BitmapFactory.Options options) {
        if (options == null) {
            options = new BitmapFactory.Options();
        }
        options.inSampleSize = UriTexture.computeSampleSize(path, maxResolutionX, maxResolutionY);

        options.inDither = false;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = config;

        return getBitmapFromFile(path, options);

    }

    public static Bitmap getBitmapFromFile(String path, BitmapFactory.Options opt) {
        Bitmap bitmap = null;
        int retry = 0;
        while (++retry < 4) { 
            try {
                if (opt != null) {
                    bitmap = BitmapFactory.decodeFile(path, opt);
                } else {
                    bitmap = BitmapFactory.decodeFile(path);
                }
                return bitmap;
            } catch (OutOfMemoryError ex) {
                //Log.e(LOG_TAG, ex.getMessage());
                System.gc();
                //Log.d(LOG_TAG, "out of memory, try to GC");
                if (opt == null) {
                    opt = new BitmapFactory.Options();
                    opt.inSampleSize = 2;
                    opt.inDither = false;
                    opt.inJustDecodeBounds = false;
                } else {
                    if (retry > 1) {
                        opt.inSampleSize *= 2;

                    }
                }
                //Log.d(LOG_TAG, "try to increase sample size to " + opt.inSampleSize);

            } catch (Exception ex) {
                // Try to recover
                //Log.e(LOG_TAG, ex.getMessage());
                return null;
            } finally {

            }
        }
        //Log.d(LOG_TAG, "memory exhausted, unable to get the image from file");
        return null;
    }

}
