package com.renren.mobile.chat.ui.imageviewer;

import android.media.ExifInterface;

public final class Shared {
	// Constants.
	public static final int INVALID = -1;

	public static final int INFINITY = Integer.MAX_VALUE;

	public static int degreesToExifOrientation(float normalizedAngle) {
		if (Util.floatEquals(normalizedAngle, 0.0f)) {
			return ExifInterface.ORIENTATION_NORMAL;
		} else if (Util.floatEquals(normalizedAngle, 90.0f)) {
			return ExifInterface.ORIENTATION_ROTATE_90;
		} else if (Util.floatEquals(normalizedAngle, 180.0f)) {
			return ExifInterface.ORIENTATION_ROTATE_180;
		} else if (Util.floatEquals(normalizedAngle, 270.0f)) {
			return ExifInterface.ORIENTATION_ROTATE_270;
		}
		return ExifInterface.ORIENTATION_NORMAL;
	}

	public static int degreesToExifOrientation(int degree) {
		degree = (degree + 360) % 360;
		if (degree == 0) {
			return ExifInterface.ORIENTATION_NORMAL;
		} else if (degree == 90) {
			return ExifInterface.ORIENTATION_ROTATE_90;
		} else if (degree == 180) {
			return ExifInterface.ORIENTATION_ROTATE_180;
		} else if (degree == 270) {
			return ExifInterface.ORIENTATION_ROTATE_270;
		}
		return ExifInterface.ORIENTATION_NORMAL;
	}

	public static float exifOrientationToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}
}
