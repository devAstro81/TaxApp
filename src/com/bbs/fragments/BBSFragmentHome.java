package com.bbs.fragments;

import java.util.ArrayList;

import com.bbs.adapters.BBSArrayAdapter;
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


public class BBSFragmentHome extends BBSFragmentBase implements View.OnClickListener, AdapterView.OnItemClickListener{
	public static String PAGEID = "Home"; 

	//constructor
	public BBSFragmentHome() {
		super();
		
		mPageID = PAGEID;
	}
	
	ListView mListView;
	BBSArrayAdapter mArrayAdapter;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_home, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.home), 
				BBSHeaderButtonType.BBSButtonMenu, 
				null);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		mListView = (ListView)view.findViewById(R.id.lst_home);
		
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
		
		BBSItem obj_calculators = new BBSItem(R.drawable.side_calculators, getString(R.string.calculators));
		BBSItem obj_trackers = new BBSItem(R.drawable.side_trackers, getString(R.string.trackers));
		BBSItem obj_checklist = new BBSItem(R.drawable.side_checklist, getString(R.string.taxpc));
		
		objects.add(obj_calculators);
		objects.add(obj_trackers);
		objects.add(obj_checklist);
		
		return objects;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		switch(position) {
			case 0:
				mMainActivity.showPageWithMenuId(R.id.calculators_container);
				break;
			case 1:
				mMainActivity.showPageWithMenuId(R.id.trackers_container);
				break;
			case 2:
				mMainActivity.showPageWithMenuId(R.id.taxpc_container);
				break;
			default:
				break;
		}
	}
	
}
