package com.core.util;

import com.common.emotion.database.Emotion_DB;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

/**
 * @author dingwei.chen
 * */
public abstract class AbstractApplication extends Application {

	public static final Handler HANDLER = new Handler();
	private static Context sContext = null;
	@Override
	public void onCreate() {
		super.onCreate();
		ServiceMappingUtil.getInstance().mappingService(SystemService.class, this);
		sContext = this;
		this.initAfterOnCreate();
	}
	public static Context getAppContext(){
		return sContext;
	}
	public abstract void initAfterOnCreate();
	
	protected String getStringFromResource(int strId){
		return getResources().getString(strId);
	}
	public static Resources getAppResources(){
		return getAppContext().getResources();
	}
}
