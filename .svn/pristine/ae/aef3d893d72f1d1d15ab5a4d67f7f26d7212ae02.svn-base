package com.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * @说明 登录信息使用的加密工具
 * */
public class CryptUtil {
	
	private final static String ENCODING = "UTF-8";

	/**
	 * 加密
	 * @param txt
	 * @param key
	 * @return
	 */
	public static String encryptString(String txt, String key) {
		if (txt == null || key == null) {
			return null;
		}
		try {
			byte[] bs = txt.getBytes("UTF-8");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gos = new GZIPOutputStream(baos);
			gos.write(bs);
			gos.finish();
			gos.close();
			byte[] tmp = baos.toByteArray();
			baos.close();
			String s = encrypt(tmp, key);
			String v = Md5.toMD5(s + key);
			return v + s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解密
	 * @param txt
	 * @param key
	 * @return
	 */
	public static String decryptString(String txt, String key) {
		try {
			if (txt.length() > 32) {
				String v = txt.substring(0, 32);
				String s = txt.substring(32);
				if (v.equals(Md5.toMD5(s + key))) {
					byte[] bs = decrypt(s, key);
					ByteArrayInputStream in = new ByteArrayInputStream(bs);
					GZIPInputStream zIn = new GZIPInputStream(in);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buf = new byte[1024];
					while (true) {
						int len = zIn.read(buf);
						if (len <= 0) {
							break;
						}
						baos.write(buf, 0, len);
					}
					byte[] ret = baos.toByteArray();
					return new String(ret, "UTF-8");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密函数
	 * 
	 * @param byte[]
	 *            等待加密的数据
	 * @param string
	 *            私有密匙(用于解密和加密)
	 * @return string 原字串经过私有密匙加密后的结果
	 */
	private static String encrypt(byte[] data, String key) {
		if (data == null || key == null) {
			return null;
		}
		try {
			Random random = new Random();
			byte seed = (byte) ((random.nextInt()>>>1) % 10);
			String encryptKey = Md5.toMD5(key);

			int ctr = seed;

			byte[] encryptKeyByte = encryptKey.getBytes(ENCODING);
			byte[] ret = new byte[data.length + 1];
			ret[0] = seed;

			for (int i = 0; i < data.length; i++) {
				ctr = ctr == encryptKeyByte.length ? 0 : ctr;
				ret[i + 1] = (byte) (data[i] ^ encryptKeyByte[ctr++]);
			}

			return base64Encode(ret);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Passport 解密函数
	 * 
	 * @param string
	 *            加密后的字串
	 * @param string
	 *            私有密匙(用于解密和加密)
	 * @return byte[] 字串经过私有密匙解密后的结果
	 */
	private static byte[] decrypt(String txt, String key) {
		if (txt == null || key == null) {
			return null;
		}
		try {
			byte[] data = base64Decode(txt);
			String encryptKey = Md5.toMD5(key);

			int ctr = data[0];

			byte[] encryptKeyByte = encryptKey.getBytes(ENCODING);
			byte[] ret = new byte[data.length - 1];

			for (int i = 1; i < data.length; i++) {
				ctr = ctr == encryptKeyByte.length ? 0 : ctr;
				ret[i-1] = (byte) (data[i] ^ encryptKeyByte[ctr++]);
			}
			return ret;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static byte[] base64Decode(String txt) {
		if (txt == null) {
			return null;
		}
		byte[] bs = null;
		try {
			bs = txt.getBytes(ENCODING);
			return Base64.decode(bs);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String base64Encode(byte[] data) {
		if (data == null) {
			return null;
		}
		byte[] result = Base64.encode(data);
		String s = null;
		try {
			s = new String(result, ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}
}
