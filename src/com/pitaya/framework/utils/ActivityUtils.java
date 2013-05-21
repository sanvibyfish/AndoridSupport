package com.pitaya.framework.utils;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewAnimator;


/**
 * @author Sanvi E-mail:sanvibyfish@gmail.com
 * @version 创建时间：2010-8-25 上午11:48:00
 */
public class ActivityUtils {

	public static final int REGISTER_ACTIVITY = 10;
	
	private static final String TAG = "ActivityUtils";
	
	public static boolean isTopActivity(Context context,String packageName){
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo>  tasksInfo = activityManager.getRunningTasks(1);  
        if(tasksInfo.size() > 0){  
            //应用程序位于堆栈的顶层  
            if(packageName.equals(tasksInfo.get(0).topActivity.getPackageName())){  
                return true;  
            }  
        }  
        return false;  
    }  
	
	public static int randomColor(){
		  return Color.argb(127, ((Long)Math.round(Math.random() * 255)).intValue(), ((Long)Math.round(Math.random() * 255)).intValue(),((Long)Math.round(Math.random() * 255)).intValue() );
	}
	
	/**
	 * This method convets dp unit to equivalent device specific value in pixels. 
	 * 
	 * @param dp A value in dp(Device independent pixels) unit. Which we need to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent Pixels equivalent to dp according to device
	 */
	public static float convertDpToPixel(float dp,Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi/160f);
	    return px;
	}
	/**
	 * This method converts device specific pixels to device independent pixels.
	 * 
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent db equivalent to px value
	 */
	public static float convertPixelsToDp(float px,Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;

	}


	
	public static int getStatusBarHeight(Activity activity){
		Rect rect = new Rect();   
	    Window win = activity.getWindow();   
	    win.getDecorView().getWindowVisibleDisplayFrame(rect);   
	    return (int) convertPixelsToDp(rect.top,activity);   
	}
	
	public static String getRealPathFromURI(Uri contentUri,Activity activity) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
	public static String getPathFromUri(Uri contentUri){
		return contentUri.getPath();
	}
	
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		}catch (Exception e) {
			Log.e(TAG, "Exception", e);
		}
		return versionName;
	}
	
	/**
	 * 窗体跳转
	 * @param old
	 * @param cls
	 */
	public static void jump(Context old, Class<?> cls, int requestCode,Bundle mBundle){
		jump(old, cls, requestCode,mBundle,false);
	}
	
	/**
	 * 窗体跳转
	 * @param old
	 * @param cls
	 */
	public static void jump(Context old, Class<?> cls, int requestCode,Bundle mBundle,boolean clearTop){
		   Intent intent = new Intent();  
           intent.setClass(old, cls);
           if(mBundle == null){
        	   mBundle = new Bundle();
        	   
           }   
           Activity oldActivity = (Activity) old;
           mBundle.putString("from", oldActivity.getClass().getSimpleName());
           intent.putExtras(mBundle);
    	   
           Activity activity = (Activity) old;
           if(clearTop){
        	   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	   activity.finish();
           }
           activity.startActivityForResult(intent, requestCode); 
	}
	
	public static void jumpWithNoHistory(Context old,Class<?> cls,int requestCode,Bundle mBundle){
		   Intent intent = new Intent();  
           intent.setClass(old, cls);
           if(mBundle != null){
        	   intent.putExtras(mBundle);
           }
           
           Activity activity = (Activity) old;
           intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
           activity.startActivityForResult(intent, requestCode); 
		
	}
	
	
	
	public static void jump(Context old, Class<?> cls, int requestCode){
		jump(old, cls, requestCode,null);
	}
	
	
	public static void back(Context old, Intent intent){
		   Activity activity = (Activity) old;
		   activity.finish();
		   activity.setResult(Activity.RESULT_OK, intent);
	}
	
	/**
	 * 添加控件(会删除之前layout所有控件)
	 * @param layout
	 * @param view
	 */
	public static void addViewOnly(ViewGroup layout, View view){
		try {
			if(layout.getChildCount() > 0){
				layout.removeAllViews();
			}
			layout.addView(view);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void displayImage(Context context, String url,ImageView imageView){
		imageView.setTag(url);
		ImageLoader.getInstance(context).displayImage(url, imageView);
	}
	
	
	public static void displayImage(Context context, String url,ImageView imageView,String cacheDir){
		imageView.setTag(url);
		ImageLoader.getInstance(context,cacheDir).displayImage(url, imageView);
	}
	
	public static void runInUIThread(Context context, final Toast toast){
		final Activity activity = (Activity)context;
	      activity.runOnUiThread(new Runnable() {
	           public void run() {
	        	   toast.show();
	           }
	       });
	}
	
	public static Display getWindowDisplay(Context context){
		return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
	}
	
	public static String getPhoneNumber(Context context){
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = telephonyManager.getLine1Number();
		return phoneNumber;  
	}
	
	public static String getDeviceId(Context context){
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}
	public static String getString(Context context,final int mStringId){
		return context.getResources().getString(mStringId);
	}
	
	public static int getColor(Context context,final int mColorId){
		return context.getResources().getColor(mColorId);
	}
	
	public static float getPX(Context context, int dipValue){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics());
	}
}
