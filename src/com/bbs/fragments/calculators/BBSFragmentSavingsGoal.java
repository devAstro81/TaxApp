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

public class BBSFragmentSavingsGoal extends BBSFragmentBase{
	public static String PAGEID = "SavingsGoal"; 
	
	private BBSCalculatorResult wid_result_time;
	private BBSCalculatorResult wid_result_saving;
	
	EditText txt_goal;
	EditText txt_saved;
	EditText txt_years;
	EditText txt_msavings;
	EditText txt_rate;
	
	float val_g;
	float val_p;
	float val_y;
	float val_r;
	float val_s;
	
	private Button btn_clear;
	private Button btn_calc;
	
	BBSApiSettings mSettings;
	
	//constructor
	public BBSFragmentSavingsGoal() {
		super();
		
		mPageID = PAGEID;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		mSettings = BBSApiSettings.getInstance(mMainActivity);
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_calc_goal, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.savings_goal), 
				BBSHeaderButtonType.BBSButtonBack, 
				null);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		wid_result_time = (BBSCalculatorResult) view.findViewById(R.id.wid_result_time);
		wid_result_time.setViewValues(R.drawable.ico_clock, "0.0", R.string.years_required_to_meet_goal);
		wid_result_saving = (BBSCalculatorResult) view.findViewById(R.id.wid_result_saving);
		wid_result_saving.setViewValues(R.drawable.ico_money, "0.00", R.string.monthly_savings_required_to_meet_goal);
		
		txt_goal = (EditText) view.findViewById(R.id.goal_text);
		txt_saved = (EditText) view.findViewById(R.id.saved_text);
		txt_years = (EditText) view.findViewById(R.id.years_text);
		txt_msavings = (EditText) view.findViewById(R.id.monthly_text);
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
				if (txt_goal.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_savings_goal));
					return;
				} else {
					val_g = Float.parseFloat(txt_goal.getText().toString());	
				}
				
				if (txt_saved.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_currently_saved));
					return;
				} else {
					val_p = Float.parseFloat(txt_saved.getText().toString());	
				}
				
				if (txt_years.getText().toString().equals("") && txt_msavings.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_years_to_save));
					return;
				}
				
				if (txt_years.getText().toString().equals("")) {
					val_y = 0;
				} else {
					val_y = Float.parseFloat(txt_years.getText().toString());
				}
				
				if (txt_msavings.getText().toString().equals("")) {
					val_s = 0;
				} else {
					val_s = Float.parseFloat(txt_msavings.getText().toString());	
				}
				
				if (txt_rate.getText().toString().equals("")) {
					UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_rate_of_return));
					return;
				} else {
					val_r = Float.parseFloat(txt_rate.getText().toString());	
				}
				
				if (val_s != 0) {
					float val_time = mSettings.calcGoalTime(val_g, val_p, val_s, val_r);
					float val_year = val_time / 12;
					
					if (val_year > 100) {
						wid_result_time.setResultValue("100+");	
					} else {
						wid_result_time.setResultValue(String.format("%.1f", val_year));	
					}
				}
				
				if (val_y != 0) {
					float val_saving = mSettings.calcGoalSaving(val_g, val_p, val_y, val_r);
					
					DecimalFormat formatter = new DecimalFormat("#,###.00");
					
					wid_result_saving.setResultValue(formatter.format(val_saving));	
				}
			}
		});
		
		initValues();
		
		mRootLayout = view;
		return view;
	}
	
	private void initValues() {
		wid_result_saving.setResultValue("0.00");
		wid_result_time.setResultValue("0.0");
		
		val_g = 0;
		val_p = 0;
		val_y = 0;
		val_r = 0;
		val_s = 0;
		
		txt_goal.setText("");
		txt_saved.setText("");
		txt_years.setText("");
		txt_msavings.setText("");
		txt_rate.setText("");
	}
	
	@Override
	public void onNavBarLeftButtonClicked() {
		mMainActivity.popLastPage();
	}
}
