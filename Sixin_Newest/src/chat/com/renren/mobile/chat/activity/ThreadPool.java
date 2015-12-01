package com.renren.mobile.chat.activity;

import java.util.LinkedList;
import java.util.List;

import com.renren.mobile.chat.RenrenChatApplication;

/**
 * @author dingwei.chen
 * */
public final class ThreadPool implements Runnable{

	private List<Runnable> mCommands = new LinkedList<Runnable>();
	private Thread mCoreThread = new Thread(this);
	private boolean mIsStop = false;
	private ThreadPool(){
		mCoreThread.start();
	}
	private static ThreadPool sInstance = new ThreadPool();
	public static ThreadPool obtain(){
		return sInstance;
	}
	
	
	public void execute(Runnable runable){
		synchronized (mCommands) {
			mCommands.add(runable);
			mCommands.notify();
		}
	}
	public void executeMainThread(Runnable r){
		this.removeCallbacks(r);
		RenrenChatApplication.HANDLER.post(r);
	}
	public void executeMainThread(Runnable r,long time){
		RenrenChatApplication.HANDLER.postDelayed(r,time);
	}
	
	public void removeCallbacks(Runnable r){
		RenrenChatApplication.HANDLER.removeCallbacks(r);
	}
	public void shutDown(){
		mCoreThread.interrupt();
	}
	@Override
	public void run() {
			while(!mIsStop){
				Runnable command = this.obtainCommandSyn();
				command.run();
			}
		
	}
	
	private Runnable obtainCommandSyn(){
		while(true){
			synchronized (mCommands) {
				Runnable currentCommand = null;
				if(mCommands.size()>0){
					currentCommand = mCommands.remove(0);
					return currentCommand;
				}else{
					try {
						mCommands.wait();
					} catch (InterruptedException e) {}
				}
			}
		}
	}
	
	
}
