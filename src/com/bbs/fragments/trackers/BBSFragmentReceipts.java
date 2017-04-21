package com.bbs.fragments.trackers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbs.adapters.BBSReceiptsArrayAdapter;
import com.bbs.api.BBSApiSettings;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.iTextpdf.BBSCreatePdf;
import com.bbs.listeners.BBSCreateDocumentListener;
import com.bbs.model.BBSReceipt;
import com.bbs.preference.BBSPreference;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.utils.UiUtil;
import com.bbs.widgets.BBSHeader.BBSHeaderButtonType;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class BBSFragmentReceipts extends BBSFragmentBase implements View.OnClickListener, BBSCreateDocumentListener, AdapterView.OnItemClickListener{
	public static String PAGEID = "Receipts"; 

	//constructor
	public BBSFragmentReceipts() {
		super();
		
		mPageID = PAGEID;
	}
	
	ListView mListView;
	BBSReceiptsArrayAdapter mArrayAdapter;
	ArrayList<BBSReceipt> mArrayList;
	JSONArray receipts;
	
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
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_receipts, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.receipts), 
				BBSHeaderButtonType.BBSButtonBack, 
				BBSHeaderButtonType.BBSButtonPlus);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		mListView = (ListView)view.findViewById(R.id.lst_receipt);
		
		getListItems();		
		
		mArrayAdapter = new BBSReceiptsArrayAdapter(mMainActivity.getApplicationContext(), mMainActivity, R.layout.cell_receipt, mArrayList);
		mListView.setAdapter(mArrayAdapter);
		mListView.setOnItemClickListener(this);
		
		btnSend = (Button) view.findViewById(R.id.btn_send);
		UiUtil.applyButtonEffect(btnSend, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				pdf_instance.createReceiptsPage(mArrayList);
			}
		});
		
		mRootLayout = view;
		return view;
	}
	
	// Add ListView data & item click listener
	private void getListItems() {
		mArrayList = new ArrayList<BBSReceipt>();
		
		String receipts_str = BBSPreference.getInstance(mMainActivity).getStore(BBSPreference.PREFERENCE_RECEIPTS);
		if (!receipts_str.equals("")) {
			try{
				receipts = new JSONArray(receipts_str); 
				for (int i = 0; i < receipts.length(); i++) {
			        JSONObject repObject = receipts.getJSONObject(i);
					BBSReceipt obj = new BBSReceipt(repObject.getString("name"), repObject.getString("category"), repObject.getString("date"), String.format("%.2f", Float.parseFloat(repObject.getString("amount"))), repObject.getString("image_url"));
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
		BBSFragmentAddReceipt newPage = new BBSFragmentAddReceipt();
		mMainActivity.addPage(newPage);
	}

	@Override
	public void onDocumentCreated(String pdf_url) {
		// TODO Auto-generated method stub
		mSettings.sendEmail(pdf_url);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		try{
			JSONObject repObject = receipts.getJSONObject(position);
			mMainActivity.setPreviewBitmap(repObject.getString("image_url"));
			BBSFragmentReceiptImage curPage = new BBSFragmentReceiptImage(receipts, position, repObject);
			mMainActivity.addPage(curPage);
		} catch (JSONException e) {
			
		}
	}
}
