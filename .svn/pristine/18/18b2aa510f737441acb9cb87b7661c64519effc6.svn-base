package com.common.manager;

import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;

/**
 * 注册管理类
 * @author shichao.song
 * 2012-8-13
 */
public class RegisterManager {
	private static RegisterManager registerInstance;
	
	public RegisterManager(){
		
	}
	
	public static RegisterManager getRegisterManager(){
		if(registerInstance == null){
			registerInstance = new RegisterManager();
		}
		return registerInstance;
	}
	
	/**
	 * 获取验证码
	 * @param number 邮箱或者手机号
	 * @param response
	 */
	public void getVerifycode(String number, INetResponse response){
		McsServiceProvider.getProvider().getVerifycode(number, response);
	}
	
	/**
	 * 验证验证码
	 * @param number 邮箱或者手机号
	 * @param verifycode
	 * @param response
	 */
	public void submitRegister(String number, String verifycode, INetResponse response){
		McsServiceProvider.getProvider().submitWithVerifycode(number, verifycode, response);
		
	}
	
	/**
	 * 注册接口
	 * @param number
	 * @param pwd
	 * @param captcha
	 * @param response
	 */
	public void register(String number, String pwd, String captcha, int gender, String firstName, String lastName, INetResponse response){
		McsServiceProvider.getProvider().register(number, pwd, captcha, gender, firstName, lastName, response);
	}
	
	/**
	 * 获取忘记密码的验证码
	 * @param number
	 * @param response
	 */
	public void getForgetPwdIdentifyCode(String number, INetResponse response){
		McsServiceProvider.getProvider().getForgetPwdIdentifyCode(number, response);
	}
	
	/**
	 * 验证验证码是否正确
	 * @param number
	 * @param verifycode
	 * @param response
	 */
	public void identifyForgetPwdCode(String number, String verifycode, INetResponse response){
		McsServiceProvider.getProvider().identifyForgetPwdCode(number, verifycode, response);
	}
	
	/**
	 * 忘记密码后，通过验证码修改密码
	 * @param number
	 * @param newPwd
	 * @param captcha
	 * @param response
	 */
	public void modifyForgetPwd(String number, String newPwd, String captcha, INetResponse response){
		McsServiceProvider.getProvider().modifyForgetPwd(number, newPwd, captcha, response);
	}
	
	
}
