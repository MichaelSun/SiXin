package com.renren.mobile.chat.ui.imageviewer;

import java.io.InputStream;

import android.graphics.BitmapFactory;

public class UriTexture {

    protected String mUri;

    public UriTexture(String imageUri) {
        mUri = imageUri;
    }

    public static int computeSampleSize(InputStream stream, int maxResolutionX, int maxResolutionY) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, null, options);
        int maxNumOfPixels = maxResolutionX * maxResolutionY;
        int minSideLength = Math.min(maxResolutionX, maxResolutionY) / 2;
        return Utils.computeSampleSize(options, minSideLength, maxNumOfPixels);
    }

    public static int computeSampleSize(String path, int maxResolutionX, int maxResolutionY) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int maxNumOfPixels = maxResolutionX * maxResolutionY;
        int minSideLength = Math.min(maxResolutionX, maxResolutionY) / 2;
        return Utils.computeSampleSize(options, minSideLength, maxNumOfPixels);
    }

}
