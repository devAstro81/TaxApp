package com.bbs.fragments.trackers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.bbs.api.BBSApiSettings;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.preference.BBSPreference;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.utils.BBSSettings;
import com.bbs.utils.UiUtil;
import com.bbs.widgets.BBSHeader.BBSHeaderButtonType;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


public class BBSFragmentAddMileage extends BBSFragmentBase implements View.OnClickListener, OnDateSetListener{
	public static String PAGEID = "AddMileage"; 

	private EditText mil_bamount;
	private EditText mil_eamount;
	private EditText mil_reason;
	private EditText mil_date;
	private EditText mil_carMake;
	private EditText mil_carModel;
	private EditText mil_carYear;
	
	private int cur_year, cur_month, cur_day;
	private Button calendarButton;
	
	public static final String DATEPICKER_TAG = "datepicker";
	final Calendar calendar = Calendar.getInstance();
    DatePickerDialog datePickerDialog;
    
	//constructor
	public BBSFragmentAddMileage() {
		super();
		
		mPageID = PAGEID;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_add_mileage, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.add_mileage), 
				BBSHeaderButtonType.BBSButtonBack, 
				BBSHeaderButtonType.BBSButtonDone);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		mil_bamount = (EditText) view.findViewById(R.id.bamount_text);
		mil_eamount = (EditText) view.findViewById(R.id.eamount_text);
		mil_reason = (EditText) view.findViewById(R.id.reason_text);
		mil_date = (EditText) view.findViewById(R.id.mdate_text);
		mil_carMake = (EditText) view.findViewById(R.id.carmake_text);
		mil_carModel = (EditText) view.findViewById(R.id.carmodel_text);
		mil_carYear = (EditText) view.findViewById(R.id.caryear_text);
		
		// Date Picker Controller
		
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd yyyy");
		String date = formatter.format(calendar.getTime());
		cur_year = calendar.get(Calendar.YEAR); 
		cur_month = calendar.get(Calendar.MONTH); 
		cur_day = calendar.get(Calendar.DAY_OF_MONTH);
		mil_date.setText(date);
		
		calendarButton = (Button) view.findViewById(R.id.btn_calendar);
		calendarButton.setOnClickListener(this);
		
		mRootLayout = view;
		return view;
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_calendar) {
			// Set SpUnit double to show dialog original
			UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(getActivity()) * 2);
			datePickerDialog = DatePickerDialog.newInstance(this, cur_year, cur_month, cur_day, true);
			
            datePickerDialog.setYearRange(1985, 2028);
            datePickerDialog.setCloseOnSingleTapDay(false);
            datePickerDialog.show(mMainActivity.getSupportFragmentManager(), DATEPICKER_TAG);
		}
	}
	
	@Override
	public void onNavBarLeftButtonClicked() {
		mMainActivity.popLastPage();
	}
	
	@Override
	public void onNavBarRightButtonClicked() {
		addMileage();
	}

	private void addMileage() {
		// Add mileage process
		JSONObject mileage = new JSONObject();
		
		if (mil_bamount.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_beginning_mileage));
			return;
		}
		
		if (mil_eamount.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_ending_mileage));
			return;
		}
		
		if (mil_reason.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_reason));
			return;
		}
		
		if (mil_date.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_date));
			return;
		}
		
		/*
		if (mil_carMake.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_car_make));
			return;
		}
		
		if (mil_carModel.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_car_model));
			return;
		}
		
		if (mil_carYear.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_car_year));
			return;
		}
		*/
		
		try {
			mileage.put("bamount", mil_bamount.getText().toString());
			mileage.put("eamount", mil_eamount.getText().toString());
			mileage.put("reason", mil_reason.getText().toString());
			mileage.put("date", mil_date.getText().toString());
			mileage.put("car_make", mil_carMake.getText().toString());
			mileage.put("car_model", mil_carModel.getText().toString());
			mileage.put("car_year", mil_carYear.getText().toString());
			
			BBSApiSettings.getInstance(mMainActivity).addTrackersValue(BBSPreference.PREFERENCE_MILEAGE, mileage.toString());
			mMainActivity.popLastPage();
		} catch(JSONException e) {
			
		}
	}
	
	/****************************/
	/**	Date Picker Controller **/
	/****************************/
	
	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		// TODO Auto-generated method stub
		cur_year = year;
		cur_month = month;
		cur_day = day;
		String date = BBSSettings.getConvertDate(day + "/" + (month + 1 ) + "/" + year);
		mil_date.setText(date);
	}

	@Override
	public void onDialogDismiss() {
		// TODO Auto-generated method stub
		// Set SpUnit as default value
		UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(getActivity()) / 2);
	}
}
