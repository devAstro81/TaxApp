package com.bbs.fragments.docs;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbs.adapters.BBSArrayAdapter;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.model.BBSItem;
import com.bbs.preference.BBSPreference;
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


public class BBSFragmentMyDocs extends BBSFragmentBase implements View.OnClickListener, AdapterView.OnItemClickListener{
	public static String PAGEID = "MyDocs"; 

	//constructor
	public BBSFragmentMyDocs() {
		super();
		
		mPageID = PAGEID;
	}
	
	ListView mListView;
	BBSArrayAdapter mArrayAdapter;
	JSONArray doc_array;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_mydocs, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.mydocs), 
				BBSHeaderButtonType.BBSButtonMenu, 
				BBSHeaderButtonType.BBSButtonPlus);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		mListView = (ListView)view.findViewById(R.id.lst_docs);
		
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
	
	@Override
	public void onNavBarRightButtonClicked() {
		// Add action
		BBSFragmentAddDoc newPage = new BBSFragmentAddDoc();
		mMainActivity.addPage(newPage);
	}
	
	// Add ListView data & item click listener
	private ArrayList<BBSItem> getListItems() {
		doc_array = new JSONArray();
		ArrayList<BBSItem> objects = new ArrayList<BBSItem>();
		
//		BBSItem obj_security = new BBSItem(R.drawable.docs_icon, getString(R.string.security_number));
//		BBSItem obj_license = new BBSItem(R.drawable.docs_icon, getString(R.string.driver_license));
//		BBSItem obj_form = new BBSItem(R.drawable.docs_icon, getString(R.string.w_2_form));
//		BBSItem obj_back = new BBSItem(R.drawable.docs_icon, getString(R.string.bank_docs));
//		
//		objects.add(obj_security);
//		objects.add(obj_license);
//		objects.add(obj_form);
//		objects.add(obj_back);
		
		String documents_str = BBSPreference.getInstance(mMainActivity).getStore(BBSPreference.PREFERENCE_DOCUMENTS);
		if (!documents_str.equals("")) {
			try{
				doc_array = new JSONArray(documents_str); 
				for (int i = 0; i < doc_array.length(); i++) {
			        JSONObject docObject = doc_array.getJSONObject(i);
					BBSItem obj = new BBSItem(R.drawable.docs_icon, docObject.getString("name"));
					objects.add(obj);
				}
				return objects;
			} catch(JSONException e) {
				e.printStackTrace();
			}
		}
		
		return objects;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		try{
			JSONObject docObject = doc_array.getJSONObject(position);
			mMainActivity.setPreviewBitmap(docObject.getString("image_url"));
			BBSFragmentDocDetail curPage = new BBSFragmentDocDetail(doc_array, position, docObject);
			mMainActivity.addPage(curPage);
		} catch (JSONException e) {
			
		}
	}
}
