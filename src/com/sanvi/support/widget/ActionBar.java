package com.sanvi.support.widget;



import com.sanvi.support.R;

import android.content.Context;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ActionBar extends LinearLayout {

	private TextView line;
	private Button actionBar;
	
	public ActionBar(Context context) {
		super(context);
		line = new TextView(context);
		setOrientation(LinearLayout.HORIZONTAL);
		setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT));
		line.setLayoutParams(new LayoutParams(1,45));
		actionBar = new Button(context);
		actionBar.setBackgroundResource(R.drawable.button);
		actionBar.setTextColor(getResources().getColor(android.R.color.white));
		actionBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT));
		addView(line);
		addView(actionBar);
	}
	
	
	public void setText(String text) {
		actionBar.setText(text);
	}
	
	public void setOnClickListener(OnClickListener l){
		actionBar.setOnClickListener(l);
	}
	
	
}
