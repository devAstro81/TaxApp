package com.bbs.widgets;
 
import com.bbs.taxapp.R;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
 
public class BBSCalculatorSubResult extends RelativeLayout{
    
	private ImageView img_result;
	private BBSTextView txt_result;
	private BBSTextView txt_result_desc;
	
    public BBSCalculatorSubResult(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public BBSCalculatorSubResult(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BBSCalculatorSubResult(Context context) {
        super(context);
        initView();
    }
    
    private void initView() {
        View view = inflate(getContext(), R.layout.widget_calc_sub_result, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        addView(view, lp);
        
        img_result = (ImageView) view.findViewById(R.id.img_result);
        txt_result = (BBSTextView) view.findViewById(R.id.txt_result);
        txt_result_desc = (BBSTextView) view.findViewById(R.id.txt_result_desc);
        
        // Set Font as Roboto-light
        Typeface roboto_light = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        txt_result.setTypeface(roboto_light);
        txt_result_desc.setTypeface(roboto_light);
        
    }

    public void setViewValues(int resourceId, String result, int result_desc) {
    	
    	img_result.setImageResource(resourceId);
        txt_result.setText(result);
        txt_result_desc.setText(getContext().getString(result_desc));
    }
    
    public void setResultValue(String result) {
    	txt_result.setText(result);
    }
}