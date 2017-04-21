package com.bbs.fragments.calculators;

import java.util.ArrayList;

import com.bbs.api.BBSApiSettings;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.listeners.BBSCalcResultListener;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.utils.UiUtil;
import com.bbs.widgets.BBSCalculatorResult;
import com.bbs.widgets.BBSChoiceDialog;
import com.bbs.widgets.BBSHeader.BBSHeaderButtonType;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class BBSFragmentSavings extends BBSFragmentBase implements View.OnClickListener, BBSCalcResultListener{
	public static String PAGEID = "Savings"; 
	
	private BBSCalculatorResult wid_result;
	private BBSCalcResultListener mListener;
	
	private EditText txt_initial;
	private EditText txt_monthly;
	private EditText txt_years;
	private EditText txt_compounded;
	private EditText txt_rate;
	
	private Button btn_compounded;
	private Button btn_clear;
	private Button btn_calc;
	
	float val_p;		// investment amount
	float val_pmt; 	// monthly payment
	float val_r;		// interest rate
	float val_n;		// compounded value
	float val_t;		// number of years
	
	//constructor
	public BBSFragmentSavings() {
		super();
		
		mPageID = PAGEID;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		mListener = this;
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_calc_saving, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.savings), 
				BBSHeaderButtonType.BBSButtonBack, 
				null);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		wid_result = (BBSCalculatorResult) view.findViewById(R.id.wid_result);
		wid_result.setViewValues(R.drawable.ico_money, "0.00", R.string.total_savings);
		
		txt_initial = (EditText) view.findViewById(R.id.initial_invest_text);
		txt_monthly = (EditText) view.findViewById(R.id.month_invest_text);
		txt_years = (EditText) view.findViewById(R.id.years_text);
		txt_compounded = (EditText) view.findViewById(R.id.compounded_text);
		txt_rate = (EditText) view.findViewById(R.id.rate_text);
		
		btn_compounded = (Button) view.findViewById(R.id.btn_compounded);
		btn_compounded.setOnClickListener(this);
		
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
				if (txt_initial.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_initial_amount));
					return;
				} else {
					val_p = Float.parseFloat(txt_initial.getText().toString());	
				}
				
				if (txt_monthly.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_monthly_amount));
					return;
				} else {
					val_pmt = Float.parseFloat(txt_monthly.getText().toString());	
				}
				
				if (txt_years.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_years_amount));
					return;
				} else {
					val_t = Float.parseFloat(txt_years.getText().toString());	
				}
				
				if (txt_rate.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_interest_rate));
					return;
				} else {
					val_r = Float.parseFloat(txt_rate.getText().toString());	
				}
				
				BBSApiSettings.getInstance(mMainActivity).calcSavings(val_p, val_pmt, val_t, val_n, val_r, mListener);	
				
			}
		});
		
		initValues();
		
		mRootLayout = view;
		return view;
	}
	
	private void initValues() {
		
		wid_result.setResultValue("0.00");
		
		val_p = 0;
		val_pmt = 0;
		val_r = 0;
		val_n = 12;
		val_t = 0;
		
		txt_initial.setText("");
		txt_monthly.setText("");
		txt_years.setText("");
		txt_compounded.setText(getString(R.string.monthly));
		txt_rate.setText("");
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_compounded) {
			openMenu(btn_compounded);
		}
	}
	
	@Override
	public void onNavBarLeftButtonClicked() {
		mMainActivity.popLastPage();
	}
	
	public void openMenu(View v) {
		ArrayList<String> options = new ArrayList<String>();
		
		options.add(getString(R.string.monthly));
		options.add(getString(R.string.quarterly));
		options.add(getString(R.string.semiannually));
		options.add(getString(R.string.annually));
		
		BBSChoiceDialog.showActionChooser(getActivity(), options, R.string.compounded, new BBSChoiceDialog.BBSChoiceListener(){
			@Override
			public void onItemSelected(Object item) {
				String sel = item.toString();
				txt_compounded.setText(sel);
				
				if(sel.equals(getString(R.string.monthly))) {
					val_n = 12;
				} else if (sel.equals(getString(R.string.quarterly))) {
					val_n = 4;
				} else if (sel.equals(getString(R.string.semiannually))) {
					val_n = 2;
				} else if (sel.equals(getString(R.string.annually))) {
					val_n = 1;
				}
			}
		});
	}

	@Override
	public void onResult(String result) {
		// TODO Auto-generated method stub
		wid_result.setResultValue(result);
	}
}


