package com.pitaya.framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;


/**
 * @author Sanvi E-mail:sanvibyfish@gmail.com
 * @version 创建时间�?010-8-31 下午01:22:13
 */
public class StringUtils {

	private static final String TAG = "StringUtils";

	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}
	
	public static boolean isEmpty(String str){
		if(str != null && !"".equals(str)){
			return false;
		}
		return true;
	}
	
	public static String getQuery(NameValuePair... nameValuePairs){
		return URLEncodedUtils.format(stripNulls(nameValuePairs), HTTP.UTF_8);
	}
	
	public static boolean isEmail(String strEmail) {
		String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}
	
	
	public static String getQueryNotEncode(NameValuePair... nameValuePairs){
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (NameValuePair param : stripNulls(nameValuePairs)) {
			if(i == 0) {
				sb.append(param.getName()).append("=").append("\"").append(param.getValue()).append("\"");
			}else{
				sb.append("&").append(param.getName()).append("=").append("\"").append(param.getValue()).append("\"");
			}
			i++;
		}
		return sb.toString();
	}
	
	
    private static List<NameValuePair> stripNulls(NameValuePair... nameValuePairs) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (int i = 0; i < nameValuePairs.length; i++) {
            NameValuePair param = nameValuePairs[i];
            if (param.getValue() != null) {
                params.add(param);
            }
        }
        return params;
    }
}
