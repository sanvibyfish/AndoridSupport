package com.pitaya.framework.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class NoBarGridView extends GridView {
	public NoBarGridView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 

    public NoBarGridView(Context context) { 
        super(context); 
    } 

    public NoBarGridView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 

    @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

        int expandSpec = MeasureSpec.makeMeasureSpec( 
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec); 
    } 
}
