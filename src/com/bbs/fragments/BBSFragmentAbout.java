package com.bbs.fragments;

import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.widgets.BBSHeader.BBSHeaderButtonType;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;


public class BBSFragmentAbout extends BBSFragmentBase implements View.OnClickListener{
	public static String PAGEID = "About"; 

	//constructor
	public BBSFragmentAbout() {
		super();
		
		mPageID = PAGEID;
	}
	
	WebView mContent;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_aboutus, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.about_us), 
				BBSHeaderButtonType.BBSButtonMenu, 
				null);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		mContent = (WebView)view.findViewById(R.id.wvw_content);
		mContent.loadUrl("file:///android_asset/about_us.html");
		
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
	
}
