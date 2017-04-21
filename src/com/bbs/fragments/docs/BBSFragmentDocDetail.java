package com.bbs.fragments.docs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbs.api.BBSApiSettings;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.iTextpdf.BBSCreatePdf;
import com.bbs.listeners.BBSCreateDocumentListener;
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


public class BBSFragmentDocDetail extends BBSFragmentBase implements View.OnClickListener, BBSCreateDocumentListener{
	public static String PAGEID = "DocDetail"; 
	
	private ImageView doc_img;
	
	private Button btn_preview;
	private Button btn_send;
	
	private String doc_name;
	private String doc_imgUrl;
	
	BBSApiSettings mSettings;
	BBSCreatePdf pdf_instance;
	BBSCreateDocumentListener mListener;
	
	//constructor
	public BBSFragmentDocDetail() {
		super();
		
		mPageID = PAGEID;
	}
	
	JSONArray docs;
	int item_pos;
	
	public BBSFragmentDocDetail(JSONArray doc_array, int position, JSONObject doc) {
		super();
		
		docs = doc_array;
		item_pos = position;
		
		try{
			doc_name = doc.getString("name");
			doc_imgUrl = doc.getString("image_url");	
		} catch(JSONException e) {
			
		}
		
		mPageID = PAGEID;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		mListener = this;
		
		mSettings = BBSApiSettings.getInstance(mMainActivity);
		pdf_instance = BBSCreatePdf.getInstance(mMainActivity, mListener);
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_doc_detail, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.add_document), 
				BBSHeaderButtonType.BBSButtonBack, 
				BBSHeaderButtonType.BBSButtonMore);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		doc_img = (ImageView) view.findViewById(R.id.img_doc);
		doc_img.setImageBitmap(mMainActivity.mCurrentBitmap);
		
		btn_preview = (Button) view.findViewById(R.id.btn_preview);
		UiUtil.applyButtonEffect(btn_preview, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent i = new Intent(mMainActivity, FullScreenViewActivity.class);
				Bundle b = new Bundle();
				b.putString("image_url", doc_imgUrl); //Your id
				i.putExtras(b);
	            startActivity(i);
			}
			
		});
		
		btn_send = (Button) view.findViewById(R.id.btn_send);
		UiUtil.applyButtonEffect(btn_send, new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				pdf_instance.createDocumentPage(doc_name, doc_imgUrl);
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
		    	docs = mSettings.remove(item_pos, docs);
		    	BBSPreference.getInstance(mMainActivity).setStore(BBSPreference.PREFERENCE_DOCUMENTS, docs.toString());
		    	mMainActivity.popLastPage();
		    }})
		 .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog, int whichButton) {
			        // Delete Item here
			    	UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(mMainActivity) / 2);
			    }}
		 ).show();
	}

	@Override
	public void onDocumentCreated(String pdf_url) {
		// TODO Auto-generated method stub
		mSettings.sendEmail(pdf_url);
	}
	
}
