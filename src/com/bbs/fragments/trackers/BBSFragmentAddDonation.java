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

public class BBSFragmentAddDonation extends BBSFragmentBase implements View.OnClickListener, OnDateSetListener{
	public static String PAGEID = "AddDonation"; 

	private EditText don_amount;
	private EditText don_name;
	private EditText don_date;
	private int cur_year, cur_month, cur_day;
	private Button calendarButton;
	
	public static final String DATEPICKER_TAG = "datepicker";
	final Calendar calendar = Calendar.getInstance();
    DatePickerDialog datePickerDialog;
	
	//constructor
	public BBSFragmentAddDonation() {
		super();
		
		mPageID = PAGEID;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_add_donation, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.add_donation), 
				BBSHeaderButtonType.BBSButtonBack, 
				BBSHeaderButtonType.BBSButtonDone);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		don_amount = (EditText) view.findViewById(R.id.damount_text);
		don_name = (EditText) view.findViewById(R.id.dname_text);
		don_date = (EditText) view.findViewById(R.id.ddate_text);
		
		// Date Picker Controller
		
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd yyyy");
		String date = formatter.format(calendar.getTime());
		cur_year = calendar.get(Calendar.YEAR); 
		cur_month = calendar.get(Calendar.MONTH); 
		cur_day = calendar.get(Calendar.DAY_OF_MONTH);
		don_date.setText(date);
		
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
		addDonation();
	}
	
	private void addDonation() {
		// Add donation process
		JSONObject donation = new JSONObject();
		
		if (don_amount.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_donation_amount));
			return;
		}
		
		if (don_name.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_donation_name));
			return;
		}
		
		if (don_date.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_date));
			return;
		}
		
		try {
			donation.put("amount", don_amount.getText().toString());
			donation.put("name", don_name.getText().toString());
			donation.put("date", don_date.getText().toString());
			
			BBSApiSettings.getInstance(mMainActivity).addTrackersValue(BBSPreference.PREFERENCE_DONATIONS, donation.toString());
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
		String date = BBSSettings.getConvertDate(cur_day + "/" + (cur_month + 1 ) + "/" + cur_year);
		don_date.setText(date);
	}
	
	@Override
	public void onDialogDismiss() {
		// TODO Auto-generated method stub
		// Set SpUnit as default value
		UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(getActivity()) / 2);
	}
	

}
