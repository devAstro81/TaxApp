package com.bbs.fragments.trackers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbs.adapters.BBSChargesArrayAdapter;
import com.bbs.api.BBSApiSettings;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.iTextpdf.BBSCreatePdf;
import com.bbs.listeners.BBSCreateDocumentListener;
import com.bbs.model.BBSCharge;
import com.bbs.preference.BBSPreference;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.utils.UiUtil;
import com.bbs.widgets.BBSHeader.BBSHeaderButtonType;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemLongClickListener;


public class BBSFragmentCharges extends BBSFragmentBase implements View.OnClickListener, BBSCreateDocumentListener{
	public static String PAGEID = "Charges"; 

	//constructor
	public BBSFragmentCharges() {
		super();
		
		mPageID = PAGEID;
	}
	
	ListView mListView;
	BBSChargesArrayAdapter mArrayAdapter;
	ArrayList<BBSCharge> mArrayList;
	JSONArray charges;
	
	Button btnSend;
	
	BBSApiSettings mSettings;
	BBSCreatePdf pdf_instance;
	BBSCreateDocumentListener mListener;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		mListener = this;
		
		mSettings = BBSApiSettings.getInstance(mMainActivity);
		pdf_instance = BBSCreatePdf.getInstance(mMainActivity, mListener);
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_charges, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.non_reimbursed_charges), 
				BBSHeaderButtonType.BBSButtonBack, 
				BBSHeaderButtonType.BBSButtonPlus);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		mListView = (ListView)view.findViewById(R.id.lst_charge);
		
		getListItems();		
		
		mArrayAdapter = new BBSChargesArrayAdapter(mMainActivity.getApplicationContext(), mMainActivity, R.layout.cell_charge, mArrayList);
		mListView.setAdapter(mArrayAdapter);
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				deleteItemAtPosition(position);
				
				return false;
			}
		});
		
		btnSend = (Button) view.findViewById(R.id.btn_send);
		UiUtil.applyButtonEffect(btnSend, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				pdf_instance.createChargePage(mArrayList);
			}
		});
		
		mRootLayout = view;
		return view;
	}
	

	private void deleteItemAtPosition(final int pos) {
		UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(mMainActivity) * 2);
		new AlertDialog.Builder(mMainActivity)
		.setTitle(getString(R.string.app_name))
		.setMessage(getString(R.string.delete_item))
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		        // Delete Item here
		    	UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(mMainActivity) / 2);
		    	charges = mSettings.remove(pos, charges);
		    	BBSPreference.getInstance(mMainActivity).setStore(BBSPreference.PREFERENCE_CHARGES, charges.toString());
		    	mArrayList.remove(pos);
		    	mArrayAdapter.notifyDataSetChanged();
		    }})
		 .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog, int whichButton) {
			        // Delete Item here
			    	UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(mMainActivity) / 2);
			    }}
		 ).show();
	}
	
	// Add ListView data & item click listener
	private void getListItems() {
		mArrayList = new ArrayList<BBSCharge>();
		
		String charges_str = BBSPreference.getInstance(mMainActivity).getStore(BBSPreference.PREFERENCE_CHARGES);
		if (!charges_str.equals("")) {
			try{
				charges = new JSONArray(charges_str); 
				for (int i = 0; i < charges.length(); i++) {
			        JSONObject chargeObject = charges.getJSONObject(i);
			        BBSCharge obj = new BBSCharge(chargeObject.getString("name"), chargeObject.getString("date"), String.format("%.2f", Float.parseFloat(chargeObject.getString("amount"))));
			        mArrayList.add(obj);	
				}
			} catch(JSONException e) {
				
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		mMainActivity.showPageWithMenuId(v.getId());
	}
	
	@Override
	public void onNavBarLeftButtonClicked() {
		mMainActivity.popLastPage();
	}
	
	@Override
	public void onNavBarRightButtonClicked() {
		BBSFragmentAddCharge newPage = new BBSFragmentAddCharge();
		mMainActivity.addPage(newPage);
	}


	@Override
	public void onDocumentCreated(String pdf_url) {
		// TODO Auto-generated method stub
		mSettings.sendEmail(pdf_url);
	}
}
