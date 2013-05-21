package com.pitaya.framework.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pitaya.framework.adapter.ActionMenuAdapter;
import com.pitaya.framework.utils.ActivityUtils;
import com.sanvi.support.R;

public class ActionMenu extends PopupWindow{

	List<Button> buttons = new ArrayList();
	HashMap<Integer,Button> buttonMap = new HashMap<Integer,Button>();
	private Context context;
	private LinearLayout layoutActionMenu;
	private View view;
	private View contentView;
	public List<String> items = new ArrayList<String>();
	
	private OnInitListener onInitListener;
	private ListView listView;
	private ActionMenuAdapter adapter;
	private AdapterView.OnItemClickListener onItemClickListener;
	public void setOnItemClickListener(
			AdapterView.OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public OnInitListener getOnInitListener() {
		return onInitListener;
	}

	public void setOnInitListener(OnInitListener onInitListener) {
		this.onInitListener = onInitListener;
	}

	public interface OnInitListener {
		public void init(View contentView);
	}
	
	

	public ActionMenu(Context context) {
		super(context);
		LayoutInflater inflater =  (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
		contentView = inflater.inflate(R.layout.action_menu, null);
		setContentView(contentView);
		setOutsideTouchable(true);
		this.context = context; 
		init();
	}
	
	public ActionMenu(Context context,int layoutId) {
		super(context);
		LayoutInflater inflater =  (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
		contentView = inflater.inflate(layoutId, null);
		setContentView(contentView);
		setOutsideTouchable(true);
		this.context = context; 
		setBackgroundDrawable(new BitmapDrawable());
	}
	
	public void showTop(View v, int width,int offsetY){		
		setFocusable(true); 
		setHeight(LayoutParams.MATCH_PARENT);
		setWidth(width);
		 showAtLocation(v, Gravity.TOP|Gravity.LEFT, 0, offsetY);
			if(onInitListener != null) {
				onInitListener.init(contentView);
			}
	}
	
	public void show(View view){
		show(view, view.getWidth());
	}
	public void show(View view, int width){
		setFocusable(true); 
		setHeight(LayoutParams.WRAP_CONTENT);
		setWidth(width);
		showAsDropDown(view);
		if(onInitListener != null) {
			onInitListener.init(contentView);
		}
		
		
		if(onItemClickListener != null) {
			listView.setOnItemClickListener(onItemClickListener);
		}
	}
	
	
	
	public void showInBottom(View view ){
		setFocusable(true); 
		setAnimationStyle(R.style.PopupAnimation);
		setHeight(LayoutParams.WRAP_CONTENT);
		setWidth(ActivityUtils.getWindowDisplay(context).getWidth());
		showAtLocation(view.getRootView(),Gravity.BOTTOM,0,0);
		if(onInitListener != null) {
			onInitListener.init(contentView);
		}
		
		if(onItemClickListener != null) {
			listView.setOnItemClickListener(onItemClickListener);
		}
	}
	
	private void init(){
		layoutActionMenu  = (LinearLayout) contentView.findViewById(R.id.action_menu_layout);
		
		listView = (ListView) contentView.findViewById(R.id.action_menu_listview);
		adapter = new ActionMenuAdapter(context);
		listView.setAdapter(adapter);
		setBackgroundDrawable(new BitmapDrawable());
	}
	
	public Button add(int btnId,String text){
		Button btn = new Button(context);
		btn.setText(text);
		btn.setBackgroundResource(R.drawable.button);
		btn.setTextColor(context.getResources().getColor(android.R.color.white));
		btn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,60));
		buttonMap.put(btnId, btn);
		if(layoutActionMenu.getChildCount() % 2 != 0){
			TextView textView = new TextView(context);
			textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,1));
			textView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
			layoutActionMenu.addView(textView);
		}
		layoutActionMenu.addView(btn);
		return btn;
	}
	
	public void addItem(String item){
		items.add(item);
		adapter.setItems(items);
		adapter.notifyDataSetChanged();
	}
	
}
