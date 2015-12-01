package com.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.util.Log;

public class ZIPUtil {

	public static void unzip(String filePath,String outputDirectory){
		try {
			ZipInputStream stream = new ZipInputStream( new FileInputStream(new File(filePath)));
			unzip(stream, outputDirectory);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void unzip(InputStream zipFileName, String outputDirectory) {
		try {
			ZipInputStream in = new ZipInputStream(zipFileName);
			// 获取ZipInputStream中的ZipEntry条目，一个zip文件中可能包含多个ZipEntry，
			// 当getNextEntry方法的返回值为null，则代表ZipInputStream中没有下一个ZipEntry，
			// 输入流读取完成；
			
			ZipEntry entry = in.getNextEntry();
			while (entry != null) {

				// 创建以zip包文件名为目录名的根目录
				File file = new File(outputDirectory);
				file.mkdirs();
				//Log.d("xialong","file isDirectory: "+entry.isDirectory());
				if (entry.isDirectory()) {
				//	Log.d("xialong","file isDirectory ");
					String name = entry.getName();
					name = name.substring(0, name.length() - 1);
				//	Log.d("xialong","filepath:"+name+"||"+ outputDirectory + File.separator
				//			+ name);
					file = new File(outputDirectory + File.separator + name);
					file.mkdirs();
					//Log.d("xialong","file create:"+name+"|||||"+file.mkdir()+"");

				} else {
//					Log.d("xialong","filepath:"+entry.getName()+"||"+ outputDirectory + File.separator
//							+ entry.getName());
					file = new File(outputDirectory + File.separator
							+ entry.getName());
					//Log.d("xialong","filepath"+ file.getPath());
					file.createNewFile();
					FileOutputStream out = new FileOutputStream(file);
					int b;
					while ((b = in.read()) != -1) {
						out.write(b);
					}
					out.close();
				}
				// 读取下一个ZipEntry
				entry = in.getNextEntry();
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
}
