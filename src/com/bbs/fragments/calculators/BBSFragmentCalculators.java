package com.bbs.fragments.calculators;

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


public class BBSFragmentCalculators extends BBSFragmentBase implements View.OnClickListener, AdapterView.OnItemClickListener{
	public static String PAGEID = "Calculators"; 

	//constructor
	public BBSFragmentCalculators() {
		super();
		
		mPageID = PAGEID;
	}
	
	ListView mListView;
	BBSArrayAdapter mArrayAdapter;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_calculators, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.calculators), 
				BBSHeaderButtonType.BBSButtonMenu, 
				null);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		mListView = (ListView)view.findViewById(R.id.lst_calc);
		
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
		
		BBSItem obj_saving = new BBSItem(R.drawable.calc_saving, getString(R.string.saving));
		BBSItem obj_loan_amort = new BBSItem(R.drawable.calc_amortization, getString(R.string.loan_amortization));
		BBSItem obj_loan_payoff = new BBSItem(R.drawable.calc_payoff, getString(R.string.loan_payoff));
		BBSItem obj_hbudget = new BBSItem(R.drawable.calc_home, getString(R.string.home_budget));
//		BBSItem obj_ksaving = new BBSItem(R.drawable.calc_401_saving, getString(R.string.k401k_savings));
		BBSItem obj_saving_goal = new BBSItem(R.drawable.calc_saving_goal, getString(R.string.saving_goal));
		BBSItem obj_credit_card = new BBSItem(R.drawable.calc_credit_card, getString(R.string.credit_card));
		BBSItem obj_tip = new BBSItem(R.drawable.calc_tip, getString(R.string.restaurant_tip));
		
		objects.add(obj_saving);
		objects.add(obj_loan_amort);
		objects.add(obj_loan_payoff);
		objects.add(obj_hbudget);
//		objects.add(obj_ksaving);
		objects.add(obj_saving_goal);
		objects.add(obj_credit_card);
		objects.add(obj_tip);
		
		return objects;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
			
		BBSFragmentBase curPage = new BBSFragmentBase();
		switch(position) {
			case 0:
				curPage = new BBSFragmentSavings();
				break;
			case 1:
				curPage = new BBSFragmentAmortization();
				break;
			case 2:
				curPage = new BBSFragmentPayoff();
				break;
			case 3:
				curPage = new BBSFragmentBudget();
				break;
//			case 4:
//				curPage = new BBSFragmentKSavings();
//				break;
			case 4:
				curPage = new BBSFragmentSavingsGoal();
				break;
			case 5:
				curPage = new BBSFragmentCardDebt();
				break;
			case 6:
				curPage = new BBSFragmentTip();
				break;
			default:
				break;
		}
		
		mMainActivity.addPage(curPage);
	}
	
}
