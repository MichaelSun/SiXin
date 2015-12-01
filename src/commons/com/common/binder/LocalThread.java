package com.common.binder;

/**
 * @author dingwei.chen
 * */
abstract class LocalThread extends Thread{

	public abstract void loop() throws Exception;
	public abstract void onException(Exception e);
	
	
	@Override
	public void run() {
		while(true){
			try {
				loop();
			} catch (Exception e) {
				this.onException(e);
			}
		}
	}
	
}
