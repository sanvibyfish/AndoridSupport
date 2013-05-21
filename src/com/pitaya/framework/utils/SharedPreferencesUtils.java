package com.pitaya.framework.utils;

import org.json.JSONObject;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 用于数据存储
 * @author sanvi
 * 
 */
public class SharedPreferencesUtils {

	private static SharedPreferencesUtils instance;

	public static SharedPreferencesUtils getInstance(Context context) {
		if (instance == null) {
			instance = new SharedPreferencesUtils(context);
		}
		return instance;
	}

	private SharedPreferences settings;
	

	public SharedPreferencesUtils(Context context) {
		settings = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void set(String key, String value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public void remove(String key){
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		editor.commit();
	}
	
	public String get(String key) {
		return settings.getString(key, "");
	}
	
	

	
}
