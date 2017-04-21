package com.bbs.fragments.trackers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbs.api.BBSApiSettings;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.preference.BBSPreference;
import com.bbs.taxapp.FullScreenViewActivity;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.utils.UiUtil;
import com.bbs.widgets.BBSHeader.BBSHeaderButtonType;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class BBSFragmentReceiptImage extends BBSFragmentBase implements View.OnClickListener{
	public static String PAGEID = "ReceiptImage"; 
	
	private ImageView rep_img;
	
	private Button btn_preview;
	
	private String rep_imgUrl;
	
	BBSApiSettings mSettings;
	
	//constructor
	public BBSFragmentReceiptImage() {
		super();
		
		mPageID = PAGEID;
	}
	
	JSONArray receipts;
	int item_pos;
	
	public BBSFragmentReceiptImage(JSONArray receipts_array, int position, JSONObject receipt) {
		super();
		
		receipts = receipts_array;
		item_pos = position;
		
		try{
			rep_imgUrl = receipt.getString("image_url");	
		} catch(JSONException e) {
			
		}
		
		mPageID = PAGEID;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		
		mSettings = BBSApiSettings.getInstance(mMainActivity);
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_receipt_image, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.receipt_image), 
				BBSHeaderButtonType.BBSButtonBack, 
				BBSHeaderButtonType.BBSButtonMore);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		rep_img = (ImageView) view.findViewById(R.id.img_rep);
		rep_img.setImageBitmap(mMainActivity.mCurrentBitmap);
		
		btn_preview = (Button) view.findViewById(R.id.btn_preview);
		UiUtil.applyButtonEffect(btn_preview, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent i = new Intent(mMainActivity, FullScreenViewActivity.class);
				Bundle b = new Bundle();
				b.putString("image_url", rep_imgUrl); //Your id
				i.putExtras(b);
	            startActivity(i);
			}
			
		});
		
		mRootLayout = view;
		return view;
	}
	
	@Override
	public void onClick(View v) {
		
	}
	
	@Override
	public void onNavBarLeftButtonClicked() {
		mMainActivity.popLastPage();
	}
	
	@Override
	public void onNavBarRightButtonClicked() {
		UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(mMainActivity) * 2);
		new AlertDialog.Builder(mMainActivity)
		.setTitle(getString(R.string.app_name))
		.setMessage(getString(R.string.delete_item))
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		        // Delete Item here
		    	UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(mMainActivity) / 2);
		    	receipts = mSettings.remove(item_pos, receipts);
		    	BBSPreference.getInstance(mMainActivity).setStore(BBSPreference.PREFERENCE_RECEIPTS, receipts.toString());
		    	mMainActivity.popLastPage();
		    }})
		 .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog, int whichButton) {
			        // Delete Item here
			    	UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(mMainActivity) / 2);
			    }}
		 ).show();
	}
	
}
