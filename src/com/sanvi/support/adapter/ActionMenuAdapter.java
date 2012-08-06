package com.sanvi.support.adapter;

import java.util.Date;
import java.util.List;

import com.sanvi.support.R;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Sanvi E-mail:sanvibyfish@gmail.com
 * @version 创建时间：2010-9-2 下午01:20:31
 */
public class ActionMenuAdapter extends BaseAdapter {


	private Context context;

	public ActionMenuAdapter(Context c) {
	   this.context = c;
	   mInflater = (LayoutInflater) c
	     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private List<String> items = null;


	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	private LayoutInflater mInflater = null;

	private String item;
	private static final String TAG = "ActionMenuAdapter";

	@Override
	public int getCount() {
	   if (items != null) {
	    return items.size();
	   } else {
	    return 0;
	   }

	}

	@Override
	public String getItem(int position) {
	   return items.get(position);
	}

	@Override
	public long getItemId(int position) {
	   return position;
	}

	public static class ViewHolder {
	 public TextView textViewActionMenuButtonName;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	   // TODO Auto-generated method stub
	   ViewHolder holder = null;
	  
	   if (convertView == null) {
	    holder = new ViewHolder();
	    convertView = mInflater.inflate(R.layout.action_menu_item, null);
	    holder.textViewActionMenuButtonName = (TextView) convertView.findViewById(R.id.action_menu_button_name);
	   } else {
	    holder = (ViewHolder) convertView.getTag();
	   }
	   item = items.get(position);
	   holder.textViewActionMenuButtonName.setText(item);
	   convertView.setTag(holder);
	   return convertView;
	}


}
