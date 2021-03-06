package com.core.voice;


/**
 * @author dingwei.chen
 * */
public class PCMUtil {

	// 获得音量
	public static int getVolume(short[] pcms, int r) {
		if (pcms == null) {
			return 0;
		}
		// 计算音量代码start
		double v = 0;
		// 将 buffer 内容取出，进行平方和运算
		for (int i = 0; i < r; i++) {
			// 这里没有做运算的优化，为了更加清晰的展示代码
			v += pcms[i] * pcms[i];
		}
		double volume = (int) Math.sqrt((v / r));
		if (volume > 0 && volume <= 1000) {
			return 1;
		} else if (volume > 1000 && volume <= 3000) {
			return 2;
		} else if (volume > 3000 && volume <= 8000) {
			return 3;
		} else if (volume > 8000 && volume <= 15000) {
			return 4;
		} else if (volume > 15000 && volume <= 32768) {
			return 5;
		}
		return 0;
	}

}
