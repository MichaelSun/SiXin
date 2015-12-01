package com.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.common.app.AbstractRenrenApplication;


public class VersionCheckUtil {
	
	/**
     * @author xiangchao.fan
     * 检测版本号
     * @return true:版本号不同，false：版本号相同
     */
    public static boolean checkVersion(Context context){
    	
    	SharedPreferences SSP = AbstractRenrenApplication.getAppContext()
    			.getSharedPreferences(
    					"versionSP",
    					AbstractRenrenApplication.getAppContext().MODE_WORLD_READABLE
    							| AbstractRenrenApplication.getAppContext().MODE_WORLD_WRITEABLE);
    	;
    	
    	int oldVersionCode = SSP.getInt("application_version_code", 0);
    	
    	PackageManager pm = context.getPackageManager();
    	PackageInfo pInfo = null;
    	try {
			pInfo = pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
    	int newVersionCode = pInfo.versionCode;
    	
    	SharedPreferences.Editor editor = SSP.edit();
    	editor.putInt("application_version_code", newVersionCode);
    	editor.commit();
    	
    	if(newVersionCode <= oldVersionCode)
    		return false;
    	else 
    		return true;
    }
}
