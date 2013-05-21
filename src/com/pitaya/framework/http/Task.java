package com.pitaya.framework.http;

import com.pitaya.framework.http.BaseTask.OnInvokeAterListener;
import com.pitaya.framework.http.BaseTask.OnInvokeBeforeListener;

import android.app.ProgressDialog;
import android.content.Context;

public class Task extends BaseTask implements ReturnResult{

	private OnTaskRequestListener onTaskRequestListener;
	
	public OnTaskRequestListener getOnTaskRequestListener() {
		return onTaskRequestListener;
	}

	public Task setOnTaskRequestListener(OnTaskRequestListener onTaskRequestListener) {
		this.onTaskRequestListener = onTaskRequestListener;
		return this;
	}
	
	
	public Task setOnInvokeBeforeListener(
			OnInvokeBeforeListener onInvokeBeforeListener) {
		super.setOnInvokeBeforeListener(onInvokeBeforeListener);
		return this;
	}
	
	public Task setOnInvokeAfterListener(OnInvokeAterListener onInvokeAfterListener) {
		super.setOnInvokeAfterListener(onInvokeAfterListener);
		return this;
	}
	

	public Task(Context context) {
		super(context);
	}
	
	
	public Task(String preDialogMessage, Context context) {
		super(preDialogMessage, context);
	}

	@Override
	public Result getResult() {
		if(onTaskRequestListener != null) {
			return onTaskRequestListener.getResult();
		}else{
			return null;
		}
	}

	@Override
	public void request() throws Exception {
		if(onTaskRequestListener != null) {
			onTaskRequestListener.onRequest();
		}
	}
	
	
	public interface OnTaskRequestListener {
		public void onRequest() throws Exception;
		public Result getResult();
	}

}
