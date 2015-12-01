package com.common.utils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author xiangchao.fan
 * @date 2012.9.5
 */
public class FileUtil {

	private static FileUtil sInstance = new FileUtil();
	
	public static FileUtil getInstance(){
		return sInstance;
	}
	
	/**************************************************************************/
	/**
     * 删除文件夹
     * @param folderPath
     *            String 文件夹绝对路径 
	 * @param filterFolder
	 *            String 不需要删除的文件夹名 默认可为null 
     */
	public void delFolder(String folderPath, String filterFolder) {
		try {
			delAllFile(folderPath, filterFolder); // 删除完里面所有内容
			File myFilePath = new File(folderPath);
			myFilePath.delete();  // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void writeToFile(byte[] bytes, String fileName){
		try {
			FileOutputStream fos = new FileOutputStream("/sdcard/"+fileName);
			fos.write(bytes);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	/**
	 * 删除文件夹里面的所有文件
	 * @param path
	 *            String 文件夹绝对路径 
	 * @param filterFolder
	 *            String 不需要删除的文件夹名 默认可为null 格式："[folderName2/]folderName1/" 
	 */
	private void delAllFile(String path, String filterFolder) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				if(filterFolder != null && filterFolder.endsWith(temp.getName() + File.separator)){
					continue;
				}
				delAllFile(path + "/" + tempList[i], filterFolder);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i], filterFolder);// 再删除空文件夹
			}
		}
	}
    /**************************************************************************/
	
}
