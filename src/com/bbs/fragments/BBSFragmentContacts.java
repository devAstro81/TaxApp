package com.bbs.fragments;

import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.utils.UiUtil;
import com.bbs.widgets.BBSContact;
import com.bbs.widgets.BBSHeader.BBSHeaderButtonType;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;


public class BBSFragmentContacts extends BBSFragmentBase implements View.OnClickListener{
	public static String PAGEID = "Contacts"; 

	private BBSContact contact1;
	private BBSContact contact2;
	private BBSContact contact3;
	private BBSContact contact4;
	private BBSContact contact5;
	private BBSContact contact6;
	
	private RelativeLayout share_facebook;
	private RelativeLayout share_twitter;
	
	private Button btn_website;
	private Button btn_website_logic;
	private Button btn_feedback;
	
	//constructor
	public BBSFragmentContacts() {
		super();
		
		mPageID = PAGEID;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_contacts, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.contacts), 
				BBSHeaderButtonType.BBSButtonMenu, 
				null);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		contact1 = (BBSContact) view.findViewById(R.id.contact1);
		contact1.setViewValues(mMainActivity, "San Antonio, TX", "Better Business services, TaxLogic", 
				"22588 Scenic Loop Road\nSan Antonio, TX 78255", "(210) 694-7884", 
				"Toll: (800) 729-2271", "Manager: James Francesco", 
				getString(R.string.contact_email_san), "Postal Address:",
				"P.O. Box 780637\nSan Antonio, TX 78278");
		contact2 = (BBSContact) view.findViewById(R.id.contact2);
		contact2.setViewValues(mMainActivity, "Orlando, FL", "Better Business services", 
				"1621 Hillcrest\nOrlando, FL 32803", "(407) 896-2481", 
				"Toll: (800) 729-2271", "Manager: Sandra Rutzler", 
				getString(R.string.contact_email_orlando), "Hours:",
				"Mon - Thu 8am - 5pm\nFriday   8am - 4pm\nSat - Sun Closed");
		contact3 = (BBSContact) view.findViewById(R.id.contact3);
		contact3.setViewValues(mMainActivity, "Lakeland, FL", "Better Business services", 
				"1515 E Memorial Blvd\nLakeland, FL 33801", "(863) 682-0141", 
				"Toll: (800) 729-2271", "Manager: Linda Riggs", 
				getString(R.string.contact_email_lakeland), "Hours:",
				"Mon - Fri 8am - 5pm\nSat - Sun Closed");
		contact4 = (BBSContact) view.findViewById(R.id.contact4);
		contact4.setViewValues(mMainActivity, "Pensacola, FL", "Better Business services", 
				"4300 Bayou Blvd #3\nPensacola, FL 32503", "(850) 478-9140", 
				"Toll: (800) 822-1554", "Manager: Linda Webb", 
				getString(R.string.contact_email_pensacola), "Hours:",
				"Mon - Fri 8am - 5pm\nSat - Sun Closed");
		contact5 = (BBSContact) view.findViewById(R.id.contact5);
		contact5.setViewValues(mMainActivity, "Amarillo, TX", "Better Business services", 
				"2700 South Western #1400\nAmarillo, TX 79109", "(806) 358-3458", 
				"Toll: (806) 358-7338", "Manager: Sammy Lyles", 
				getString(R.string.contact_email_amarillo), "Hours:",
				"Mon - Fri 8am - 5pm\nSat - Sun Closed");
		contact6 = (BBSContact) view.findViewById(R.id.contact6);
		contact6.setViewValues(mMainActivity, "Idabel, OK", "BBS, PSS, John M Carr CPA", 
				"8 North Central Ave\nIdabel, OK 74745", "(580) 286-3311", 
				"Toll: (800) 937-8868", "Manager: Charlotte Gurley", 
				getString(R.string.contact_email_idable), "Hours:",
				"Mon - Fri 8am - 5pm\nSat - Sun Closed");
		
		share_facebook = (RelativeLayout) view.findViewById(R.id.social_facebook);
		share_facebook.setOnClickListener(this);
		share_twitter = (RelativeLayout) view.findViewById(R.id.social_twitter);
		share_twitter.setOnClickListener(this);
		
		btn_feedback = (Button) view.findViewById(R.id.btn_feedback);
		UiUtil.applyButtonEffect(btn_feedback, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
			            "mailto", getResources().getString(R.string.feedback_email), null));
				intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
				intent.putExtra(Intent.EXTRA_TEXT, "Feedback");
				mMainActivity.startActivity(Intent.createChooser(intent, getResources().getString(R.string.choose_an_email_client)));
			}
		});
		
		btn_website = (Button) view.findViewById(R.id.btn_website);
		UiUtil.applyButtonEffect(btn_website, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				openBrowser(Uri.parse(getString(R.string.website_url)));
			}
		});
		
		btn_website_logic = (Button) view.findViewById(R.id.btn_website_logic);
		UiUtil.applyButtonEffect(btn_website_logic, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				openBrowser(Uri.parse(getString(R.string.website_logic_url)));
			}
		});
		
		mRootLayout = view;
		return view;
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.social_facebook) {
			openBrowser(Uri.parse(getString(R.string.facebook_url)));
		} else if (v.getId() == R.id.social_twitter) {
			openBrowser(Uri.parse(getString(R.string.twitter_url)));
		}
	}
	
	@Override
	public void onNavBarLeftButtonClicked() {
		mMainActivity.showSlideMenu();
	}
	
	private void openBrowser(Uri uri) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
		
		startActivity(browserIntent);
	}
}
