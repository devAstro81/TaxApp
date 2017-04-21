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

public class BBSFragmentTip extends BBSFragmentBase implements BBSCalcResultListener{
	public static String PAGEID = "RestaurantTip"; 
	
	private BBSCalculatorResult wid_result;
	
	private EditText txt_check;
	private EditText txt_people;
	private EditText txt_tip;
	
	private Button btn_clear;
	private Button btn_calc;
	
	float val_b;
	float val_p;
	float val_t;
	
	private BBSCalcResultListener mListener;
	
	//constructor
	public BBSFragmentTip() {
		super();
		
		mPageID = PAGEID;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		mListener = this;
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_calc_tip, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.restaurant_tip), 
				BBSHeaderButtonType.BBSButtonBack, 
				null);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		wid_result = (BBSCalculatorResult) view.findViewById(R.id.wid_result);
		wid_result.setViewValues(R.drawable.ico_money, "0.00", R.string.total_tip_amount);
		
		txt_check = (EditText) view.findViewById(R.id.amount_text);
		txt_people = (EditText) view.findViewById(R.id.people_text);
		txt_tip = (EditText) view.findViewById(R.id.tip_text);
		
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
				if (txt_check.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_check_amount));
					return;
				} else {
					val_b = Float.parseFloat(txt_check.getText().toString());	
				}
				
				if (txt_people.getText().toString().equals("")) {
					val_p = 1;
				} else {
					val_p = Float.parseFloat(txt_people.getText().toString());	
				}
				
				if (txt_tip.getText().toString().equals("")) {
					val_t = 15;
				} else {
					val_t = Float.parseFloat(txt_tip.getText().toString());	
				}
				
				BBSApiSettings.getInstance(mMainActivity).calcTip(val_b, val_p, val_t, mListener);	
			}
		});
		
		initValues();
		
		mRootLayout = view;
		return view;
	}
	
	private void initValues() {
		wid_result.setResultValue("0.00");
		
		val_b = 0;
		val_p = 1;
		val_t = 15;
		
		txt_check.setText("");
		txt_people.setText("");
		txt_tip.setText("");
		
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
