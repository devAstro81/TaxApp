package com.bbs.fragments.calculators;

import com.bbs.api.BBSApiSettings;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.listeners.BBSCalcResultListener;
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

public class BBSFragmentAmortization extends BBSFragmentBase implements BBSCalcResultListener{
	public static String PAGEID = "Amortization"; 
	
	private BBSCalculatorResult wid_result;
	
	private EditText txt_loan;
	private EditText txt_years;
	private EditText txt_rate;
	
	float val_p;
	float val_r;
	float val_n;
	
	private Button btn_clear;
	private Button btn_calc;
	
	private BBSCalcResultListener mListener;
	
	//constructor
	public BBSFragmentAmortization() {
		super();
		
		mPageID = PAGEID;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		mListener = this;
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_calc_amort, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.loan_amortization), 
				BBSHeaderButtonType.BBSButtonBack, 
				null);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		wid_result = (BBSCalculatorResult) view.findViewById(R.id.wid_result);
		wid_result.setViewValues(R.drawable.ico_money, "0.00", R.string.monthly_loan_payment_amount);
		
		txt_loan = (EditText) view.findViewById(R.id.amount_text);
		txt_years = (EditText) view.findViewById(R.id.term_text);
		txt_rate = (EditText) view.findViewById(R.id.rate_text);
		
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
				if (txt_loan.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_loan_amount));
					return;
				} else {
					val_p = Float.parseFloat(txt_loan.getText().toString());	
				}
				
				if (txt_years.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_years_amount));
					return;
				} else {
					val_n = Float.parseFloat(txt_years.getText().toString());	
				}
				
				if (txt_rate.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_interest_rate));
					return;
				} else {
					val_r = Float.parseFloat(txt_rate.getText().toString());	
				}
				
				BBSApiSettings.getInstance(mMainActivity).calcLoanAmort(val_p, val_r, val_n, mListener);	
			}
		});
		
		initValues();
		
		mRootLayout = view;
		return view;
	}
	
	private void initValues() {
		wid_result.setResultValue("0.00");
		
		val_p = 0;
		val_n = 0;
		val_r = 0;
		
		txt_loan.setText("");
		txt_years.setText("");
		txt_rate.setText("");
	}
	
	@Override
	public void onNavBarLeftButtonClicked() {
		mMainActivity.popLastPage();
	}

	@Override
	public void onResult(String result) {
		// TODO Auto-generated method stub
		wid_result.setResultValue(result);
	}
}
