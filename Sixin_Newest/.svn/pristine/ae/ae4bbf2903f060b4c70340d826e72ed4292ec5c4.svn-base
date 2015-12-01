package com.renren.mobile.chat.friends;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.common.utils.Methods;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;


public class FlashChecker {
	public static final int LOW_ANDROID_VERSION = 1 << 0;
	public static final int FLASH_PLAYER_NOT_FOUND = 1 << 1;
	public static final int LOW_SYSTEM_MEMORY = 1 << 2;
	public static final int CHECK_SYSTEM_MEMORY_FAILED = 1 << 3;
	public static final int LOW_ARM_VERSION = 1 << 4;
	public static final int CHECK_ARM_VERSION_FAILED = 1 << 5;
	public static final int GLES2_NOT_SUPPORTED = 1 << 6;

	private static final String ADOBE_PACKAGE_NAME = "com.adobe.flashplayer";
	private static final String MEMORY_INFO_FILE = "/proc/meminfo";
	private static final String CPU_INFO_FILE = "/proc/cpuinfo";

	private static int status = 0;
	private static Boolean checked = false;

	private final int statusClone;

	public FlashChecker(Context context) {
		synchronized (checked) {
			if (!checked) {
				status = checkFlash(context);
				checked = true;
			}
			statusClone = status;
		}
	}

	public int getStatus() {
		return statusClone;
	}

	public boolean test(int mask) {
		return (statusClone & mask) == mask;
	}

	/*
	 * android requirements
	 * 	ARMv7 processor with vector FPU, minimum 550MHz, OpenGL ES 2.0, H.264 and AAC HW decoders
	 * 	Android™ 2.2, 2.3, 3.0, 3.1, and 3.2
	 * 	256MB of RAM
	 * 	Android web browser
	 * 本函数测试项
	 * 	Android 2.2+
	 * 	Flash player plugin
	 * 	ARMv7+
	 * 	256MB RAM
	 */
	private static int checkFlash(Context context) {
		PackageManager packageManager = context.getPackageManager();
		int status = 0;

		// 检测操作系统版本
		if (!Methods.fitApiLevel(8)) {
			status |= LOW_ANDROID_VERSION;
		}
		
		// 检测OpenGL ES 2.0
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if(am.getDeviceConfigurationInfo().reqGlEsVersion < 0x00020000) {
			status |= GLES2_NOT_SUPPORTED;
		}

		// 查找adobe flash player
		try {
			packageManager.getPackageInfo(ADOBE_PACKAGE_NAME, 0);
		} catch (NameNotFoundException e) {
			status |= FLASH_PLAYER_NOT_FOUND;
		}

		// 检测内存大小
		int memSizeInKb = 0;
		BufferedReader memInfoReader = null;
		try {
			memInfoReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(MEMORY_INFO_FILE))));
			String line;
			Pattern digit = Pattern.compile("\\d+", Pattern.CASE_INSENSITIVE);
			while ((line = memInfoReader.readLine()) != null) {
				if (line.toLowerCase().startsWith("memtotal")) {
					Matcher matcher = digit.matcher(line);
					if (matcher.find()) {
						memSizeInKb = Integer.parseInt(matcher.group());
						break;
					}
				}
			}
			memInfoReader.close();
		} catch (Exception e) {
			status |= CHECK_SYSTEM_MEMORY_FAILED;
		} finally {
			if (memInfoReader != null) {
				try {
					memInfoReader.close();
				} catch (Exception e) {
				}
			}
		}
		if (((status & CHECK_SYSTEM_MEMORY_FAILED) == 0)
				&& memSizeInKb < 256 * 1024) {
			status |= LOW_SYSTEM_MEMORY;
		}

		// 检测ARM版本
		int armVersion = -1;
		BufferedReader cpuInfoReader = null;
		try {
			cpuInfoReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(CPU_INFO_FILE))));
			String line;
			Pattern getVersion = Pattern.compile("(?<=armv)\\d+",
					Pattern.CASE_INSENSITIVE);
			while ((line = cpuInfoReader.readLine()) != null) {
				if (line.toLowerCase().startsWith("processor")) {
					Matcher matcher = getVersion.matcher(line);
					if (matcher.find()) {
						armVersion = Integer.parseInt(matcher.group());
						break;
					}
				}
			}
		} catch (Exception e) {
			status |= CHECK_ARM_VERSION_FAILED;
		} finally {
			try {
				if (cpuInfoReader != null) {
					cpuInfoReader.close();
				}
			} catch (Exception e) {
			}
		}
		if (((status & CHECK_ARM_VERSION_FAILED) == 0) && armVersion < 7) {
			status |= LOW_ARM_VERSION;
		}

		// 通过所有检测
		return status;
	}
}
