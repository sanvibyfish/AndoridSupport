package com.pitaya.framework.utils;

import java.util.Stack;

import android.app.Activity;

public class ActivityStack {

	private static Stack<Activity> stacks;
	private static ActivityStack instance;

	private ActivityStack() {
	}

	public static ActivityStack getActivityStack() {
		if (instance == null) {
			instance = new ActivityStack();
		}
		return instance;
	}

	public void popActivity() {
		Activity activity = stacks.lastElement();
		if (activity != null) {
			activity.finish();
			stacks.pop();
			activity = null;
		}
	}

	public Activity currentActivity() {
		if(stacks.size() > 0 ){
			Activity activity = stacks.lastElement();
			return activity;
		}else{
			return null;
		}
	}

	public void pushActivity(Activity activity) {
		if (stacks == null) {
			stacks = new Stack<Activity>();
		}
		stacks.add(activity);
	}
	
}
