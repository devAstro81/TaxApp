package com.bbs.fragments.trackers;

import java.util.ArrayList;

import com.bbs.adapters.BBSArrayAdapter;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.model.BBSItem;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.widgets.BBSHeader.BBSHeaderButtonType;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class BBSFragmentTrackers extends BBSFragmentBase implements View.OnClickListener, AdapterView.OnItemClickListener{
	public static String PAGEID = "Trackers"; 

	//constructor
	public BBSFragmentTrackers() {
		super();
		
		mPageID = PAGEID;
	}
	
	ListView mListView;
	BBSArrayAdapter mArrayAdapter;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_trackers, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.trackers), 
				BBSHeaderButtonType.BBSButtonMenu, 
				null);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		mListView = (ListView)view.findViewById(R.id.lst_trackers);
		
		ArrayList<BBSItem> objects = new ArrayList<BBSItem>();
		objects = getListItems();		
		
		mArrayAdapter = new BBSArrayAdapter(mMainActivity.getApplicationContext(), mMainActivity, R.layout.cell_bbs_menu, objects);
		mListView.setAdapter(mArrayAdapter);
		mListView.setOnItemClickListener(this);
		
		mRootLayout = view;
		return view;
	}
	
	@Override
	public void onClick(View v) {
		mMainActivity.showPageWithMenuId(v.getId());
	}
	
	@Override
	public void onNavBarLeftButtonClicked() {
		mMainActivity.showSlideMenu();
	}
	
	// Add ListView data & item click listener
	private ArrayList<BBSItem> getListItems() {
		ArrayList<BBSItem> objects = new ArrayList<BBSItem>();
		
		BBSItem obj_donation = new BBSItem(R.drawable.tracker_donation, getString(R.string.charitable_donations));
		BBSItem obj_mileage = new BBSItem(R.drawable.tracker_mileage, getString(R.string.mileage));
		BBSItem obj_charge = new BBSItem(R.drawable.tracker_charge, getString(R.string.non_reimbursed_charges));
		BBSItem obj_receipt = new BBSItem(R.drawable.tracker_receipt, getString(R.string.receipts));
		
		objects.add(obj_donation);
		objects.add(obj_mileage);
		objects.add(obj_charge);
		objects.add(obj_receipt);
		
		return objects;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		BBSFragmentBase curPage = new BBSFragmentBase();
		switch(position) {
			case 0:
				curPage = new BBSFragmentDonations();
				break;
			case 1:
				curPage = new BBSFragmentMileage();
				break;
			case 2:
				curPage = new BBSFragmentCharges();
				break;
			case 3:
				curPage = new BBSFragmentReceipts();
				break;
			default:
				break;
		}
		
		mMainActivity.addPage(curPage);
	}
	
}
