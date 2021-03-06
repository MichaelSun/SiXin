package com.common.messagecenter.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.xml.sax.SAXException;

import android.text.TextUtils;
import android.util.Log;

public class Utils {

	private static BufferedWriter writer;
	private static Calendar cal;
	private static int count = 0;

	private static void createLogFile() {
		try {
			cal = Calendar.getInstance();
			String time = cal.get(Calendar.DAY_OF_MONTH) + "_"
					+ cal.get(Calendar.HOUR_OF_DAY) + "_"
					+ cal.get(Calendar.MINUTE) + "_" + cal.get(Calendar.SECOND);
			File file = new File("/mnt/sdcard", String.format(
					"/network_%s_%d.txt", time, count++));
			file.createNewFile();
			writer = new BufferedWriter(new FileWriter(file));
			// System.setErr(new PrintStream(new FileOutputStream("/mnt/sdcard"
			// + String.format("/network_err_%s_%d.txt", time, count++))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HttpClient createHttpClient() {
		BasicHttpParams params = new BasicHttpParams();
		params.setIntParameter(HttpConnectionParams.SO_TIMEOUT, 75000);
		params.setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 75000);
		params.setIntParameter(HttpConnectionParams.SOCKET_BUFFER_SIZE, 8192);
		return new DefaultHttpClient(params);
	}

	public static HttpClient createHttpClient(int time) {
		BasicHttpParams params = new BasicHttpParams();
		params.setIntParameter(HttpConnectionParams.SO_TIMEOUT, time);
		params.setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, time);
		params.setIntParameter(HttpConnectionParams.SOCKET_BUFFER_SIZE, 8192);
		return new DefaultHttpClient(params);
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
		} finally {
			try {
				is.close();
			} catch (IOException ignored) {
			}
		}
		return sb.toString();
	}

	public static void escapeXMLText(String text, StringBuilder to) {
		if (TextUtils.isEmpty(text) || to == null) {
			return;
		}

		final int length = text.length();
		for (int index = 0; index < length; index++) {
			char c = text.charAt(index);
			switch (c) {
			case '<':
				to.append("&lt;");
				break;
			case '>':
				to.append("&gt;");
				break;
			case '&':
				to.append("&amp;");
				break;
			case '\'':
				to.append("&apos;");
				break;
			case '"':
				to.append("&quot;");
				break;
			default:
				to.append(c);
				break;
			}
		}
	}

	public static String getTagName(String uri, String localName, String qName) {
		// {qName} | null(localName为空) | {localName}(uri为空) | {uri}:{localName}
		if (!TextUtils.isEmpty(qName)) {
			return qName;
		}
		if (TextUtils.isEmpty(localName)) {
			return null;
		}
		if (TextUtils.isEmpty(uri)) { // localName此时不为空
			return localName;
		}
		return String.format("%s:%s", uri, localName);
	}

	/**
	 * 转换单个XML节点
	 * 
	 * @param element
	 *            要抓换的节点
	 * @param buf
	 *            输出
	 * @param depth
	 *            0:不递归; -1:无限递归; 其他:有 public static byte[] getBytes(InputStream
	 *            is) throws IOException {
	 * 
	 *            int len; int size = 1024; byte[] buf;
	 * 
	 *            if (is instanceof ByteArrayInputStream) { size =
	 *            is.available(); buf = new byte[size]; len = is.read(buf, 0,
	 *            size); } else { ByteArrayOutputStream bos = new
	 *            ByteArrayOutputStream(); buf = new byte[size]; while ((len =
	 *            is.read(buf, 0, size)) != -1) bos.write(buf, 0, len); buf =
	 *            bos.toByteArray(); } return buf; }限递归
	 */
	public static void convertSingleElement(Element element, StringBuilder buf,
			int depth) {
		if (element == null || buf == null) {
			return;
		}
		String tagName = getTagName(element.uri, element.localName, element.tag);
		if (TextUtils.isEmpty(tagName)) {
			return;
		}
		buf.append('<');
		buf.append(tagName);
		// <{tagName}
		final List<Element.Attribute> attrs = element.attrs;
		for (Element.Attribute attr : attrs) {
			String attrKey = getTagName("", attr.localName, attr.qName);
			if (TextUtils.isEmpty(attrKey)) {
				continue;
			}
			buf.append(' ');
			buf.append(attrKey);
			buf.append("='");
			escapeXMLText(TextUtils.isEmpty(attr.value) ? "" : attr.value, buf);
			buf.append('\'');
			// {attrKey}='{attr.value}'
		}
		final String text = element.text;
		final boolean hasTextNode = !TextUtils.isEmpty(text);
		final List<Element> childs = element.childs;
		// <{tagName} {attrKey}='{attr.value}'...

		// <{tagName} {attrKey}='{attr.value}'... >
		if (hasTextNode) {
			buf.append('>');
			escapeXMLText(text, buf);
			// <{tagName} {attrKey}='{attr.value}'... >{text}
		}
		if ((depth == 0 || childs.isEmpty())) {
			if (hasTextNode) {
				buf.append("</");
				buf.append(tagName);
				buf.append('>');
				// <{tagName} {attrKey}='{attr.value}'... >{text}</{tagName}>
			} else {
				if (element.flag()) {
					buf.append(" />");
				} else {
					buf.append(" >");
				}
			}
		} else {
			if (!hasTextNode) {
				buf.append('>');
				// <{tagName} {attrKey}='{attr.value}'... >
			}
			depth--;
			for (Element e : childs) {
				convertSingleElement(e, buf, depth);
			}
			buf.append("</");
			buf.append(tagName);
			buf.append(">");
			// <{tagName} {attrKey}='{attr.value}'... >...</{tagName}>
		}
	}

	public static String convertSingleElement(Element element, int depth) {
		StringBuilder sb = new StringBuilder();
		convertSingleElement(element, sb, depth);
		return sb.toString();
	}

	/**
	 * 将一个Element转换成字符串
	 * 
	 * @param element
	 *            要转换的Element
	 * @return 转换后的字符串
	 */
	public static String convertSingleElement(Element element) {
		return convertSingleElement(element, -1);
	}

	private static StackTraceElement whereCallFrom(int depth) {
		Throwable t = new Throwable();
		StackTraceElement[] stack = t.getStackTrace();
		return stack[1 + depth];
	}

	private static String formatException(Exception e) {
		if (e == null) {
			return null;
		}

		StringWriter writer = new StringWriter();
		PrintWriter err = new PrintWriter(writer);
		e.printStackTrace(err);
		return writer.toString();
	}

	private static String formatLogContent(String str) {
		if (TextUtils.isEmpty(str)) {
			return "\t";
		} else {
			StringBuilder sb = new StringBuilder("//**\n");
			String[] lines = str.split("\n");
			int cnt = lines.length;
			for (int i = 0; i < cnt; i++) {
				sb.append("| ").append(lines[i]).append('\n');
			}
			sb.append("\\\\**\n");
			return sb.toString();
		}
	}

	static boolean mDebug = true;

	private static void log(int log, String tag, int depth, Object... objs) {
		if (!mDebug) {
			return;
		}
		tag = String.format(tag, objs);
		try {
			if (objs != null) {
				for (int i = 0; i < objs.length; i++) {
					if (objs[i] instanceof Exception) {
						objs[i] = formatException((Exception) objs[i]);
					}
				}
			}
			StackTraceElement ori = whereCallFrom(depth);
			String pos = String.format(
					"%d:%s.%s()->%d",
					Thread.currentThread().getId(),
					ori.getClassName().substring(
							ori.getClassName().lastIndexOf(".") + 1), ori
							.getMethodName(), ori.getLineNumber())
					+ "\n";
			String result = pos;
			if (objs != null && objs.length > 0) {
				if (objs[0] instanceof String) {
					Object[] args = new Object[objs.length];
					System.arraycopy(objs, 1, args, 0, objs.length - 1);
					String format = (String) objs[0];
					try {
						result += String.format(format, args);
					} catch (Exception e) {
						result += format;
					}

				} else {
					String out = "";
					for (int i = 0; i < objs.length; ++i) {
						out += "arg" + i + " = " + objs[i] + ";\t";
					}
					result += out;
				}
			}
			String logStr = formatLogContent(result);
			Log.println(log, tag, logStr);
			// toLogFile(logStr+"thread id:"+Thread.currentThread().getId());
		} catch (Exception e) {
		}

	}

	// /**
	// * 把不同变量的值输出出来，格式是 arg1=1, arg2=ture, arg3=aaa...
	// *
	// * @param objs
	// * 要输出的参数列表，假如第一个参数是字符串，把该参数当作String的格式字符串
	// */
	// public static void log(Object... objs) {
	// if(!mDebug){
	// return ;
	// }
	// log("ChatDemo", 2, objs);
	// }

	/**
	 * 输出字符串到Log
	 * 
	 * @param format
	 *            要输出的字符串
	 */
	public static void log(String message) {
		log(Log.INFO, "wt", 2, message, null);
	}

	public static void l(String message, Object... arg) {
		log(Log.INFO, "wt", 2, message, null);
	}

	public static void l(int p, String message, Object... arg) {
		log(p, "wt", 2, message, arg);
	}

	/**
	 * 输出字符串到Log
	 * 
	 * @param format
	 *            要输出的字符串
	 */
	public static void log(String tag, String message) {
		if (!mDebug) {
			return;
		}
		log(Log.INFO, tag, 2, message, new Object[] {});
	}

	// /**
	// * 只输出当前代码所在的行，格式为："{线程ID}:{类名}.{方法名}()->{当前代码所在行数}"
	// */
	// public static void log() {
	// if(!mDebug){
	// return ;
	// }
	// log("ChatDemo", 2, "", new Object[] {});
	// }

	// private static void toLogFile(String logStr) {
	// Calendar cal = Calendar.getInstance();
	// String time = String.format("at %d:%d:%d \n", cal.get(Calendar.HOUR),
	// cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
	// try {
	// if (writer == null) {
	// createLogFile();
	// }else {
	// writer.write(time);
	// writer.write(logStr);
	// writer.flush();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 结束Log的输出
	 * 
	 * @parma isReCreateLog 是否重新创建Log文件
	 */
	public static void stopLog(boolean isReCreateLog) {
		try {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (isReCreateLog) {
			createLogFile();
		}
	}

	public static Element getElementFromString(String str) {
		if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str.trim())) {
			return null;
		}
		Parser p = new Parser();
		try {
			p.parse(new ByteArrayInputStream(str.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		}
		return p.getRoot();
	}

	public static byte[] getBytes(InputStream is) {

		int len;
		int size = 4096;
		byte[] buf = null;

		try {
		if (is instanceof ByteArrayInputStream) {
				size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			while ((len = is.read(buf, 0, size)) != -1)
				bos.write(buf, 0, len);
			buf = bos.toByteArray();
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buf;
	}

}
