package com.renren.mobile.chat.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.text.TextUtils;

import com.renren.mobile.chat.RenrenChatApplication;
/**
 * 内存储工具
 * @author zhenning.yang
 *
 */
public class RRInternalStorage {
    /**
     * 将一个字符串保存到文件中
     * 
     * @param filename
     *            文件名字
     * @param content
     *            内容
     * @return
     */
    public static boolean saveString2File(String filename, String content) {
    	StackTraceElement[] elements = Thread.currentThread().getStackTrace();
    	for(int k = 0 ; k < elements.length && k<9; k++){
    	}
    	if(TextUtils.isEmpty(filename)){
	    return false;
	}
	FileOutputStream fos;
	
	try {
	    RenrenChatApplication.getmContext();
	    fos = RenrenChatApplication.getmContext().openFileOutput(filename, Context.MODE_PRIVATE);
	    fos.write(content.getBytes());
	    fos.close();
	    return true;
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    return false;
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    /**
     * 从指定的文件中得到String
     * 
     * @param filename
     *            文件名
     * @return
     */
    public static String getStringFromFile(String filename) {
	FileInputStream fis;
	try {
		File f = new File(filename);
		if (!f.exists()){
			return null;
		}
	    fis = RenrenChatApplication.getmContext().openFileInput(filename);
	    byte[] buffer = new byte[100];
	    int length = -1;
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    while ((length = fis.read(buffer)) != -1) {
	    	baos.write(buffer, 0, length);
	    }
	    byte[] b = baos.toByteArray();
	    baos.close();
	    fis.close();
	    return new String(b);

	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    return null;
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}
    }

}
