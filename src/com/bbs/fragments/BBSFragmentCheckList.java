package com.bbs.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbs.api.BBSApiSettings;
import com.bbs.listeners.BBSChecklistListener;
import com.bbs.model.BBSCheckItem;
import com.bbs.preference.BBSPreference;
import com.bbs.tableviews.HeaderListView;
import com.bbs.tableviews.SectionAdapter;
import com.bbs.tableviews.TableCellBuilder;
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
import android.widget.RelativeLayout;


public class BBSFragmentCheckList extends BBSFragmentBase implements BBSChecklistListener{
	public static String PAGEID = "Checklist"; 

	//constructor
	public BBSFragmentCheckList() {
		super();
		
		mPageID = PAGEID;
	}
	
	LinkedHashMap<String, ArrayList<BBSCheckItem>> mTableData;
	
	HeaderListView mChecklistView;
	BBSChecklistListener mListener;
	BBSPreference mPref;
	JSONArray checklistJSON;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		mListener = this;
		mPref = BBSPreference.getInstance(mMainActivity);
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_checklist, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.taxpc), 
				BBSHeaderButtonType.BBSButtonMenu, 
				BBSHeaderButtonType.BBSButtonMore);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		mChecklistView = (HeaderListView) view.findViewById(R.id.lst_checklist);
		initValues();
		
		mRootLayout = view;
		return view;
	}
	
	@Override
	public void onNavBarLeftButtonClicked() {
		mMainActivity.showSlideMenu();
	}
	
	@Override
	public void onNavBarRightButtonClicked() {
		// Add action
		UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(mMainActivity) * 2);
		new AlertDialog.Builder(mMainActivity)
		.setTitle(getString(R.string.app_name))
		.setMessage(getString(R.string.uncheck_all_checklist))
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		        // Delete Item here
		    	UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(mMainActivity) / 2);

		    	try {
		    		for (int i = 0; i < checklistJSON.length(); i++) {
						JSONObject jsonObj = checklistJSON.getJSONObject(i);
						JSONArray jsonData = jsonObj.getJSONArray("f_data");
						
						for (int j = 0; j < jsonData.length(); j++) {
							JSONObject cl_Object = jsonData.getJSONObject(j);
					        if (cl_Object.getInt("f_value") == 1) {
					        	cl_Object.put("f_value", 0);
					        }
						}
					}
		    		
		    		// store to preferences with changed value
					mPref.setStore(BBSPreference.PREFERENCE_CHECKLIST, checklistJSON.toString());
					initValues();
		    	} catch (JSONException e) {
		    		e.printStackTrace();
		    	}
		    }})
		 .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		        // Delete Item here
		    	UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(mMainActivity) / 2);
		    }}
		 ).show();
		
	}
	
	public void initValues() {
		LinkedHashMap<String, ArrayList<BBSCheckItem>> map_data = new LinkedHashMap<String, ArrayList<BBSCheckItem>>();
		try{
			
			String checklist_str = mPref.getStore(BBSPreference.PREFERENCE_CHECKLIST);
			if (!checklist_str.equals("")) {
				// Get Json object from preferences
				checklistJSON = new JSONArray(checklist_str);
			} else {
				// Get Json object from local json file
				checklistJSON = new JSONArray(BBSApiSettings.getInstance(mMainActivity).loadJSONFromAsset("checklist.json"));
				mPref.setStore(BBSPreference.PREFERENCE_CHECKLIST, checklistJSON.toString());
			}
			
			for (int i = 0; i < checklistJSON.length(); i++) {
				JSONObject jsonObj = checklistJSON.getJSONObject(i);
				JSONArray jsonData = jsonObj.getJSONArray("f_data");
				
				ArrayList<BBSCheckItem> section = new ArrayList<BBSCheckItem>();
				for (int j = 0; j < jsonData.length(); j++) {
					JSONObject cl_Object = jsonData.getJSONObject(j);
			        BBSCheckItem cl_item = new BBSCheckItem(cl_Object.getInt("f_id"), cl_Object.getString("f_text"), cl_Object.getInt("f_value"));
					section.add(cl_item);
				}
				map_data.put(jsonObj.getString("f_name"), section);
			}
//			
//			// iterates keys and put it to map object according to it's section
//			Iterator<String> iter = checklistJSON.keys();
//			while (iter.hasNext()) {
//			    String key = iter.next();
//			    try {
//			    	// Create array for each section
//			    	ArrayList<BBSCheckItem> section = new ArrayList<BBSCheckItem>();
//			    	
//			    	// get json array from it's key
//			        JSONArray values = checklistJSON.getJSONArray(key);
//					for (int i = 0; i < values.length(); i++) {
//						// add objects to section array
//				        JSONObject cl_Object = values.getJSONObject(i);
//				        BBSCheckItem cl_item = new BBSCheckItem(cl_Object.getInt("f_id"), cl_Object.getString("f_text"), cl_Object.getInt("f_value"));
//						section.add(cl_item);
//					}
//					
//					map_data.put(key, section);
//			    } catch (JSONException e) {
//			        // Something went wrong!
//			    }
//			}
		} catch (JSONException e) {
			
		}
	
		mTableData = map_data;
		reloadTable();
	}
	
	public void reloadTable() {
		mChecklistView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}
	

	//////////////////////////////////////////////////////////////////////////////////////
	SectionAdapter mAdapter = new SectionAdapter() {

		@Override
		public int numberOfSections() {
			if(mTableData != null)
				return mTableData.size();
			return 0;
		}
		
		public String getSectionKey(int section) {
			int idx = 0;
			Iterator<String> iter = mTableData.keySet().iterator();
			while(iter.hasNext()) {
				String obj = iter.next();
				if(idx == section) {
					return obj;
				}
				idx++;
			}
			return null;
		}
		
		public ArrayList<BBSCheckItem> getRowsForSection(int section) {
			if(mTableData == null)
				return null;
			String key = getSectionKey(section);
			if(key == null)
				return  null;
			return mTableData.get(key);
		}
		
		
		@Override
		public int numberOfRows(int section) {
			if(mTableData != null) {
				ArrayList<BBSCheckItem> rows = getRowsForSection(section);
				if(rows != null) {
					return rows.size();
				}
			}
			return 0;
		}

		@Override
		public View getRowView(int section, int row, View convertView, ViewGroup parent) {
			BBSCheckItem booking = (BBSCheckItem)getRowItem(section, row);
			if(booking != null)
				convertView = TableCellBuilder.buildBBSCell(getActivity(), null, booking, mListener);
			return convertView;
		}

		@Override
		public Object getRowItem(int section, int row) {
			if(mTableData != null) {
				ArrayList<BBSCheckItem> rows = getRowsForSection(section);
				if(rows != null) {
					return rows.get(row);
				}
			}
			return null;
		}
		
		@Override
        public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
            super.onRowItemClick(parent, view, section, row, id);
            
        }
		
		@Override
        public boolean hasSectionHeaderView(int section) {
			if(mTableData != null) {
				return mTableData.size() != 0;
			}
			return false;
        }

		@Override
        public int getSectionHeaderViewTypeCount() {
			if(mTableData != null) {
				return mTableData.size() != 0 ? 1 : 0;
			}
            return 0;
        }
 
        @Override
        public int getSectionHeaderItemViewType(int section) {
            return 0;
        }
        
       
        
        @Override
        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            convertView = TableCellBuilder.buildHeaderCell(getActivity(), null, getSectionKey(section));
            return convertView;
        }
	};

	@Override
	public void onCheckChanged(int itemId, boolean isChecked) {
		// TODO Auto-generated method stub
		int checked_val = isChecked ? 1 : 0;

		try{
			for (int i = 0; i < checklistJSON.length(); i++) {
				JSONObject jsonObj = checklistJSON.getJSONObject(i);
				JSONArray jsonData = jsonObj.getJSONArray("f_data");
				
				for (int j = 0; j < jsonData.length(); j++) {
					JSONObject cl_Object = jsonData.getJSONObject(j);
			        if (cl_Object.getInt("f_id") == itemId) {
			        	cl_Object.put("f_value", checked_val);
			        	
			        	// store to preferences with changed value
			    		mPref.setStore(BBSPreference.PREFERENCE_CHECKLIST, checklistJSON.toString());
			    		return;
			        }
				}
			}	
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
}
