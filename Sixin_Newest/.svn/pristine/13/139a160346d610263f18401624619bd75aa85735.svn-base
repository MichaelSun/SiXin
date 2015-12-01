package com.renren.mobile.chat.ui.imageviewer;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utils {
	
	private static final String TAG = Utils.class.getName();

	private static final int UNCONSTRAINED = -1;

	public static Bitmap resizeBitmap(Bitmap src, int maxDstWidth,
			int maxDstHeight, boolean recycleSrc) {
		if (src == null)
			return null;
		int w = src.getWidth();
		int h = src.getHeight();
		if (w <= maxDstWidth && h <= maxDstHeight)
			return src;
		float scale = Math.min((maxDstWidth + 0F) / w, (maxDstHeight + 0F) / h);
		Bitmap result = null;
		try {
			// add a Math.max here to prevent a zero width or height.
			result = Bitmap.createScaledBitmap(src,
					Math.max(1, (int) (scale * w)),
					Math.max(1, (int) (scale * h)), true);
		} catch (Exception ex) {
			//Log.e(TAG, ex.getMessage());
		}
		if (recycleSrc)
			src.recycle();
		return result;
	}

	public static String getMd5Hash(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String md5 = number.toString(16);

			while (md5.length() < 32)
				md5 = "0" + md5;
			return md5;
		} catch (NoSuchAlgorithmException e) {
			//Log.e("MD5", e.getMessage());
			return String.valueOf(input.hashCode());
		}
	}

	/*
	 * Compute the sample size as a function of minSideLength and
	 * maxNumOfPixels. minSideLength is used to specify that minimal width or
	 * height of a bitmap. maxNumOfPixels is used to specify the maximal size in
	 * pixels that is tolerable in terms of memory usage. The function returns a
	 * sample size based on the constraints. Both size and minSideLength can be
	 * passed in as IImage.UNCONSTRAINED, which indicates no care of the
	 * corresponding constraint. The functions prefers returning a sample size
	 * that generates a smaller bitmap, unless minSideLength =
	 * IImage.UNCONSTRAINED. Also, the function rounds up the sample size to a
	 * power of 2 or multiple of 8 because BitmapFactory only honors sample size
	 * this way. For example, BitmapFactory downsamples an image by 2 even
	 * though the request is 3. So we round up the sample size to avoid OOM.
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	public static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math
				.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math
				.min(Math.floor(w / minSideLength),
						Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == UNCONSTRAINED)
				&& (minSideLength == UNCONSTRAINED)) {
			return 1;
		} else if (minSideLength == UNCONSTRAINED) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

}
