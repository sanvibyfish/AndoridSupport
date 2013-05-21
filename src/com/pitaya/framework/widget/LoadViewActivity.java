package com.pitaya.framework.widget;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import com.sanvi.support.R;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ViewAnimator;

/**
 * @author Sanvi E-mail:sanvibyfish@gmail.com
 * @version ??��?��?�?010-9-15 �??03:43:43
 */
public class LoadViewActivity extends ActivityGroup {

	LinearLayout contentViewLayout;
	private LocalActivityManager activityManager;
	private LinearLayout.LayoutParams contentViewLayoutParams;
	private Stack<String> mIds;
	
	public void clearStack(ViewAnimator mAnimator){
		mIds.clear();
		mAnimator.removeAllViews();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentViewLayout = new LinearLayout(this);
		activityManager = getLocalActivityManager();
		mIds = new Stack<String>();
		contentViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT); 
	}

	
	public View getContentView(int index, Intent intent){
		contentViewLayout.removeAllViews();
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		View view = activityManager.startActivity(String.valueOf(index), intent).getDecorView();
//		view.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
		contentViewLayout.addView(view, contentViewLayoutParams);
		
		return contentViewLayout;
	}
	
	
	public void jump(ViewAnimator mAnimator, Intent intent,String mId){
		jump(mAnimator, intent, mId,true, null);
	}
	
	public void jump(ViewAnimator mAnimator, Intent intent,String mId,Bundle bundle){
		jump(mAnimator, intent, mId,true, bundle);
	}
	
	
	public void jump(ViewAnimator mAnimator, Intent intent,String mId,boolean hasAnimation){
		jump(mAnimator, intent, mId,hasAnimation, null);
	}
	
	private int getIndex(String mId){
		 int mIndex = -1;  
	        /** Check the activity existed or not. */  
	        for (int i = 0; i < mIds.size(); i++) {  
	            if (mIds.get(i).equals(mId)) {  
	                mIndex = i;  
	                break;  
	            }  
	        }  
	        if (mIndex == -1) {  
	            mIds.push(mId);  
	            for (int i = 0; i < mIds.size(); i++) {  
	                if (mIds.get(i).equals(mId)) {  
	                    mIndex = i;  
	                    break;  
	                }  
	            }  
	        }
	        return mIndex;
	}
	
	
	public void jump(ViewAnimator mAnimator, Intent intent,String mId,boolean hasAnimation,Bundle bundle){
		if(bundle == null) {
			bundle = new Bundle();
		}
		if(mIds.size() != 0){
			Activity oldActivity = activityManager.getActivity(mIds.lastElement());
			bundle.putString("from", oldActivity.getClass().getSimpleName());
		}
		intent.putExtras(bundle);
		Log.i(LoadViewActivity.class.getSimpleName(),"mId:" + mId);
		 int mIndex = getIndex(mId);  
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 View view = activityManager.startActivity(mId, intent).getDecorView(); 
		 mAnimator.addView(view);
		 if(hasAnimation){
			 mAnimator.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.out_righttoleft));
		 }else{
			 mAnimator.setInAnimation(null);
		 }
		 mAnimator.setDisplayedChild(mIndex);  
		 
	}
	
	public void back(ViewAnimator mAnimator){
		String oldId = mIds.pop();
		Activity oldActivity = activityManager.getActivity(oldId);
		mAnimator.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.in_lefttoright));
		Log.i(LoadViewActivity.class.getSimpleName(),"oldActivity Name:" + oldActivity.getClass().getSimpleName());
		mAnimator.removeView(oldActivity.getWindow().getDecorView());
		if(mIds.size() > 0){
			Activity activity = activityManager.getActivity(mIds.lastElement());
			
			Intent intent = activity.getIntent();
			Bundle bundle = intent.getExtras();
			if(bundle == null){
				bundle = new Bundle();
			}
			intent.putExtras(bundle);
			bundle.putString("from", oldActivity.getClass().getSimpleName());
			Log.i(LoadViewActivity.class.getSimpleName(),"current Activity Name:" + activity.getClass().getSimpleName());
			mAnimator.setDisplayedChild(getIndex(mIds.lastElement()));
		}
	
	}

}
