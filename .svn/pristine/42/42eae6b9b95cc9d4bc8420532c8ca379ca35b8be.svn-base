package com.data.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author dingwei.chen
 * @说明 日志输出器,支持变换颜色和中文输出
 * */
public final class LogPrinter {

	public static final String LOG = "json";
	public static final String FILE_PATH = "F:/log/a.log";
	public static final boolean IS_OUTPUT_FILE = false;
	public static void log1(String log){
		File file = new File(FILE_PATH);
		if(file.exists()){
			file.delete();
		}
		file.mkdirs();
		Runtime runtime = Runtime.getRuntime();
		String cmd = "adb shell logcat";
		if(log!=null && !log.equals("$")){
			cmd+=" -s "+log;
		}
		Process process = null;
		BufferedReader reader = null;
		try {
			process = runtime.exec(cmd);
			InputStream inputstream = process.getInputStream();
			reader 
				= new BufferedReader(new InputStreamReader(inputstream));
			String str = null;
			int k = 0;
			
			while(true){
				str = reader.readLine();
				int index = str.indexOf(":");
				if(index!=-1){
					str = str.substring(index+1);
				}else{
					continue;
				}
				if(str.trim().length()==0){
					continue;
				}
				str = str.replace("\\", "");
				str = str.replace("}", "}\r\n");
				str = str.replace("]", "]\r\n");
				str = str.replace(",", ",\r\n");
				str = str.replace("{", "\r\n{");
				str = str.replace(">", ">\r\n");
				if(str.contains("clear screen")){
					for(int t = 0; t <1000; t++){
						System.out.println("\r\n");
					}
					continue;
				}
				if(!str.contains("&&&E")&&!str.contains("[Method]")){
					System.out.println(str);
					System.out.println("=============================================================");
				}else{
					if(str.contains("&&&E")){
						str = str.substring(str.indexOf(":"));
						System.err.println(str);
						System.out.println("=============================================================");
					}else{
						System.err.println(str);
					}
				}
				
				if(IS_OUTPUT_FILE){
					PrintWriter pw = new PrintWriter(new FileOutputStream(new File("f:/a.log"),true) );
					pw.write(str);
					pw.flush();
					pw.close();
				}
				k++;
			}
		} catch (Exception e) {
			System.err.println("ADB 链接出错");
			e.printStackTrace();
			killProcess(process);
		}finally{
			try {
				reader.close();
			} catch (IOException e) {}
			killProcess(process);
		}
		System.exit(0);
	}
	
	public static void main(String...args){
		Scanner scanner = new Scanner(System.in);
		System.out.println("(input '$' will print all log)");
		System.out.println("input log name:");
		String log = scanner.next();
		log1(log);
	}
	
	
	public static void killProcess(Process process){
		if(process!=null){
			process.destroy();
		}
	}
	
	
}
