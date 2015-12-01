package com.common.network;

/**
 * @author dingwei.chen1988@gmail.com
 * @说明 网络请求
 * */
public interface NetRequestListener<T> {
	public long getKey();
	public void setKey(long key);
	/*网络请求成功*/
	public  void onNetError(int errorCode,String erroMeg);
	/*网络请求失败*/
	public  void onNetSuccess();
	/*得到超时时间*/
	public  long getNetTimeOutTime();
	/*得到发送报文*/
	public  String getSendNetMessage();
	/*是否同步*/
	public boolean isSyn();
	//
	public void onSuccessRecive(T data);
}
