package com.renren.mobile.chat.base;


import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;

import com.common.binder.LocalBinderPool;
import com.common.statistics.BackgroundUtils;
import com.common.statistics.Htf;

public class BaseFragmentActivity extends FragmentActivity {
	
	@Override
	protected void onStart() {
		//记录按icon键主动运行次数
		BackgroundUtils.getInstance().dealAppRunState(Htf.INITIATIVE_RUN, true);
		if(LocalBinderPool.getInstance().isContainBinder()){
			try {
				LocalBinderPool.getInstance().obtainBinder().changeAppGround(true);
			} catch (RemoteException ignore) {}
		}
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		BackgroundUtils.getInstance().dealAppRunState();
		super.onStop();
	}

	@Override
	protected void onPause() {
		if(LocalBinderPool.getInstance().isContainBinder()){
			try {
				LocalBinderPool.getInstance().obtainBinder().changeAppGround(BackgroundUtils.getInstance().isAppOnForeground());
			} catch (RemoteException ignore) {}
		}
		super.onPause();
	}
	
	
}
