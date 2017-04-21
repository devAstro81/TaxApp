package com.bbs.fragments.trackers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbs.adapters.BBSDonationsArrayAdapter;
import com.bbs.api.BBSApiSettings;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.iTextpdf.BBSCreatePdf;
import com.bbs.listeners.BBSCreateDocumentListener;
import com.bbs.model.BBSDonation;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class BBSFragmentDonations extends BBSFragmentBase implements View.OnClickListener, BBSCreateDocumentListener{
	public static String PAGEID = "Donations"; 

	//constructor
	public BBSFragmentDonations() {
		super();
		
		mPageID = PAGEID;
	}
	
	ListView mListView;
	BBSDonationsArrayAdapter mArrayAdapter;
	ArrayList<BBSDonation> mArrayList;
	JSONArray donations;
	
	Button btnSend;
	
	BBSApiSettings mSettings;
	BBSPreference mPref;
	BBSCreatePdf pdf_instance;
	BBSCreateDocumentListener mListener;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		mListener = this;
		
		mSettings = BBSApiSettings.getInstance(mMainActivity);
		mPref = BBSPreference.getInstance(mMainActivity);
		pdf_instance = BBSCreatePdf.getInstance(mMainActivity, mListener);
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_donations, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.charitable_donations), 
				BBSHeaderButtonType.BBSButtonBack, 
				BBSHeaderButtonType.BBSButtonPlus);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		mListView = (ListView)view.findViewById(R.id.lst_dons);
		
		getListItems();
		
		mArrayAdapter = new BBSDonationsArrayAdapter(mMainActivity.getApplicationContext(), mMainActivity, R.layout.cell_donation, mArrayList);
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
				pdf_instance.createDonationsPage(mArrayList);
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
		    	donations = mSettings.remove(pos, donations);
		    	mPref.setStore(BBSPreference.PREFERENCE_DONATIONS, donations.toString());
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
		mArrayList = new ArrayList<BBSDonation>();
		String donations_str = mPref.getStore(BBSPreference.PREFERENCE_DONATIONS);
		if (!donations_str.equals("")) {
			try{
				donations = new JSONArray(donations_str); 
				for (int i = 0; i < donations.length(); i++) {
			        JSONObject donObject = donations.getJSONObject(i);
					BBSDonation obj = new BBSDonation(donObject.getString("name"), donObject.getString("date"), String.format("%.2f", Float.parseFloat(donObject.getString("amount"))));
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
		BBSFragmentAddDonation newPage = new BBSFragmentAddDonation();
		mMainActivity.addPage(newPage);
	}

	@Override
	public void onDocumentCreated(String pdf_url) {
		// TODO Auto-generated method stub
		mSettings.sendEmail(pdf_url);
	}
}
