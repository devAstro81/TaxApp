package com.bbs.fragments.calculators;

import java.text.DecimalFormat;

import com.bbs.api.BBSApiSettings;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.utils.UiUtil;
import com.bbs.widgets.BBSCalculatorResult;
import com.bbs.widgets.BBSHeader.BBSHeaderButtonType;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class BBSFragmentPayoff extends BBSFragmentBase{
	public static String PAGEID = "Payoff"; 
	
	private BBSCalculatorResult wid_result_addition;
	private BBSCalculatorResult wid_result_payment;
	private BBSCalculatorResult wid_result_interest;
	private BBSCalculatorResult wid_result_paid;
	
	private EditText txt_amount;
	private EditText txt_time;
	private EditText txt_payment;
	private EditText txt_interest;
	
	private Button btn_clear;
	private Button btn_calc;
	
	BBSApiSettings mSettings;
	
	float val_a = 0;	// loan balance
	float val_m = 0;	// loan time
	float val_p = 0;	// loan payment
	float val_r = 0;	// loan interest rate
	float val_tmp = 0;	// total monthly payment
	float val_aem = 0;	// additional each month
	float val_ti = 0;	// total interest
	float val_tp = 0;	// total paid
	
	//constructor
	public BBSFragmentPayoff() {
		super();
		
		mPageID = PAGEID;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		
		mSettings = BBSApiSettings.getInstance(mMainActivity);
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_calc_payoff, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.loan_payoff), 
				BBSHeaderButtonType.BBSButtonBack, 
				null);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		wid_result_addition = (BBSCalculatorResult) view.findViewById(R.id.wid_result_addition);
		wid_result_addition.setViewValues(R.drawable.ico_money, "0.00", R.string.additional_each_month);
		wid_result_payment = (BBSCalculatorResult) view.findViewById(R.id.wid_result_payment);
		wid_result_payment.setViewValues(R.drawable.ico_money, "0.00", R.string.total_monthly_payment);
		wid_result_interest = (BBSCalculatorResult) view.findViewById(R.id.wid_result_interest);
		wid_result_interest.setViewValues(R.drawable.ico_money, "0.00", R.string.total_interest);
		wid_result_paid = (BBSCalculatorResult) view.findViewById(R.id.wid_result_paid);
		wid_result_paid.setViewValues(R.drawable.ico_money, "0.00", R.string.total_paid);
		
		txt_amount = (EditText) view.findViewById(R.id.amount_text);
		txt_time = (EditText) view.findViewById(R.id.term_text);
		txt_payment = (EditText) view.findViewById(R.id.payment_text);
		txt_interest = (EditText) view.findViewById(R.id.rate_text);
		
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
				if (txt_amount.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_loan_amount));
					return;
				} else {
					val_a = Float.parseFloat(txt_amount.getText().toString());
				}
				
				if (txt_time.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_loan_term));
					return;
				} else {
					val_m = Float.parseFloat(txt_time.getText().toString());
				}
				
				if (txt_payment.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_additional_payment));
					return;
				} else {
					val_p = Float.parseFloat(txt_payment.getText().toString());
				}
				
				if (txt_interest.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_annual_interest_rate));
					return;
				} else {
					val_r = Float.parseFloat(txt_interest.getText().toString());
				}
				
				val_tmp = mSettings.calcPayoffTMP(val_a, val_r, val_m);
				val_aem = val_tmp - val_p;
				val_tp = val_tmp * val_m;
				val_ti = val_tp - val_a; 
				
				
				DecimalFormat formatter = new DecimalFormat("#,###.00");
				
				wid_result_payment.setResultValue(formatter.format(val_tmp));
				wid_result_addition.setResultValue(formatter.format(val_aem));
				wid_result_interest.setResultValue(formatter.format(val_ti));
				wid_result_paid.setResultValue(formatter.format(val_tp));
				
			}
		});
		
		initValues();
		
		mRootLayout = view;
		return view;
	}
	
	private void initValues() {
		wid_result_addition.setResultValue("0.00");
		wid_result_payment.setResultValue("0.00");
		wid_result_interest.setResultValue("0.00");
		wid_result_paid.setResultValue("0.00");
		
		val_a = 0;
		val_m = 0;
		val_p = 0;
		val_r = 0;
		
		txt_amount.setText("");
		txt_time.setText("");
		txt_payment.setText("");
		txt_interest.setText("");
	}
	
	@Override
	public void onNavBarLeftButtonClicked() {
		mMainActivity.popLastPage();
	}
}
