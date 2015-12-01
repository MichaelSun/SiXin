package com.renren.mobile.chat.command;

import java.util.Map;


/**
 * @author dingwei.chen
 * @说明 命令模型
 * */
public abstract class Command {
	public boolean mIsClearText = true;
	public Command(){}
	
	public abstract void onStartCommand();
	public abstract void onEndCommand(Map<String,Object> returnData);
	
	
}
