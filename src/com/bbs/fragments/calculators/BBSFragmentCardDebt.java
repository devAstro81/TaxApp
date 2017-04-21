package com.bbs.fragments.calculators;

import com.bbs.api.BBSApiSettings;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.listeners.BBSCalcResultListener;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.utils.UiUtil;
import com.bbs.widgets.BBSCalculatorResult;
import com.bbs.widgets.BBSTextView;
import com.bbs.widgets.BBSHeader.BBSHeaderButtonType;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class BBSFragmentCardDebt extends BBSFragmentBase implements OnCheckedChangeListener, BBSCalcResultListener{
	public static String PAGEID = "CardDebt"; 
	
	private BBSCalculatorResult wid_result_time;
	private BBSCalculatorResult wid_result_payment;
	
	private RadioGroup radioGroup;
	private RelativeLayout monthsContainer;
	private RelativeLayout paymentContainer;
	
	private EditText txt_balance;
	private EditText txt_rate;
	private EditText txt_months;
	private EditText txt_payment;
	
	private Button btn_clear;
	private Button btn_calc;
	
	boolean pay_calc;
	float val_b;
	float val_r;
	float val_n;
	float val_p;
	
	BBSCalcResultListener mListener;
	
	//constructor
	public BBSFragmentCardDebt() {
		super();
		
		mPageID = PAGEID;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		mListener = this;
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_calc_debt, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.credit_card), 
				BBSHeaderButtonType.BBSButtonBack, 
				null);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		wid_result_time = (BBSCalculatorResult) view.findViewById(R.id.wid_result_time);
		wid_result_time.setViewValues(R.drawable.ico_clock, "0.0", R.string.years_required_to_meet_goal);
		wid_result_payment = (BBSCalculatorResult) view.findViewById(R.id.wid_result_payment);
		wid_result_payment.setViewValues(R.drawable.ico_money, "0.00", R.string.payment_required_to_meet_goal);
		
		txt_balance = (EditText) view.findViewById(R.id.card_text);
		txt_rate = (EditText) view.findViewById(R.id.rate_text);
		txt_months = (EditText) view.findViewById(R.id.months_text);
		txt_payment = (EditText) view.findViewById(R.id.payment_text);
		
		radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
	    radioGroup.setOnCheckedChangeListener(this);
	    
	    monthsContainer = (RelativeLayout) view.findViewById(R.id.months_container);
	    paymentContainer = (RelativeLayout) view.findViewById(R.id.payment_container);
	    
	    enableDisableView(monthsContainer, true);
    	enableDisableView(paymentContainer, false);
		
		btn_clear = (Button) view.findViewById(R.id.btn_clear);
		UiUtil.applyButtonEffect(btn_clear, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				initValues();		
			}
		});
		
		btn_calc = (Button) view.findViewById(R.id.btn_calc);
		UiUtil.applyButtonEffect(btn_calc, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				if (txt_balance.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_initial_amount));
					return;
				} else {
					val_b = Float.parseFloat(txt_balance.getText().toString());	
				}
				
				if (txt_rate.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_interest_rate));
					return;
				} else {
					val_r = Float.parseFloat(txt_rate.getText().toString());	
				}
				
				if (pay_calc) {
					if (txt_months.getText().toString().equals("")) {
						UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_months_until_debt_free));
						return;
					} else {
						val_n = Float.parseFloat(txt_months.getText().toString());	
					}	
					
					BBSApiSettings.getInstance(mMainActivity).calcDebtPay(val_b, val_r, val_n, mListener);
				} else {
					if (txt_payment.getText().toString().equals("")) {
						UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_payment_per_month));
						return;
					} else {
						val_p = Float.parseFloat(txt_payment.getText().toString());	
					}
					
					BBSApiSettings.getInstance(mMainActivity).calcDebtTime(val_b, val_r, val_p, mListener);
				}
			}
		});
		
		initValues();
		
		mRootLayout = view;
		return view;
	}
	
	private void initValues() {
		wid_result_time.setResultValue("0.0");
		wid_result_payment.setResultValue("0.00");
		
		pay_calc = true;
		enableDisableView(monthsContainer, true);
    	enableDisableView(paymentContainer, false);
    	
    	val_b = 0;
    	val_r = 0;
    	val_n = 0;
    	val_p = 0;
    	
    	txt_balance.setText("");
    	txt_rate.setText("");
    	txt_months.setText("");
    	txt_payment.setText("");
	}
	
	@Override
	public void onNavBarLeftButtonClicked() {
		mMainActivity.popLastPage();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId) {
        case R.id.radio_month:
        	pay_calc = true;
        	txt_payment.setText("");
        	enableDisableView(monthsContainer, true);
        	enableDisableView(paymentContainer, false);
        	break;
        case R.id.radio_payment:
        	pay_calc = false;
        	txt_months.setText("");
        	enableDisableView(monthsContainer, false);
        	enableDisableView(paymentContainer, true);
            break;
		} 
	}
	
	private void enableDisableView(View view, boolean enabled) {
	    view.setEnabled(enabled);
 
	    if ( view instanceof ViewGroup ) {
	        ViewGroup group = (ViewGroup)view;

	        for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
	            enableDisableView(group.getChildAt(idx), enabled);
	            
	            if (group.getChildAt(idx).getClass() == BBSTextView.class) {
	            	
	            	// Set TextView color
	            	BBSTextView a = (BBSTextView)group.getChildAt(idx);
	            	if(enabled) {
	            		a.setTextColor(getResources().getColor(R.color.slide_menu_text_color_active));	
	            	} else {
	            		a.setTextColor(getResources().getColor(R.color.bbs_disabled_element));
	            	}
	            } else if (group.getChildAt(idx).getClass() == EditText.class) {
	            	
	            	// Set EditText color
	            	EditText a = (EditText) group.getChildAt(idx);
	            	if(enabled) {
	            		a.setHintTextColor(getResources().getColor(R.color.bbs_hint_color));	
	            	} else {
	            		a.setHintTextColor(getResources().getColor(R.color.bbs_disabled_element));
	            	}
	            } else {
	            	
	            	// Set underline color
	            	if (enabled) {
	            		group.getChildAt(idx).setBackgroundColor(getResources().getColor(R.color.bbs_input_bar));
	            	} else {
	            		group.getChildAt(idx).setBackgroundColor(getResources().getColor(R.color.bbs_disabled_element));
	            	}
	            	
	            }
	        }
	    }
	}

	@Override
	public void onResult(String result) {
		// TODO Auto-generated method stub
		if (pay_calc) {
			wid_result_payment.setResultValue(String.format("%.2f", Float.parseFloat(result)));
		} else {
			wid_result_time.setResultValue(String.format("%.1f", Float.parseFloat(result) / 12));
		}
	}
	
}
