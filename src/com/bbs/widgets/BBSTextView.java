package com.bbs.widgets;

import com.bbs.utils.UiUtil;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class BBSTextView extends TextView {
	public BBSTextView(Context context) {
		super(context);
		createView(context, null);
	}

	public BBSTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		createView(context, attrs);
	}

	public BBSTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		createView(context, attrs);
	}
	
	private void createView(Context context, AttributeSet attrs) {
		
		boolean regular = true;
		
		if(getTypeface() != null) {
			int idx = getTypeface().getStyle();
	
			if((idx&1) != 0)
				regular = false;
		}
		
		if(!this.isInEditMode()) {
			Typeface tf = UiUtil.getFont(context, regular);
			if(tf != null)
				setTypeface(tf);
		}
	}
	
	@Override
	public void onMeasure(int widthSepc, int heightSpec) {
		super.onMeasure(widthSepc, heightSpec);
	}
		
}
