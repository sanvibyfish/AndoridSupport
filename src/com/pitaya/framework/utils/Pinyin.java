package com.pitaya.framework.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * 拼音支持
 * @author sanvi
 *
 */
public class Pinyin {

	/**
	 * 获取字符串的首字符拼音
	 * @param str
	 * @return
	 */
	public static String getPinYinHeadChar(String str) {
		String convert = "";
			char word = str.charAt(0);
			// 提取汉字的首字母
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		return convert;
	}
}
