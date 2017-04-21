package com.bbs.fragments;

import com.bbs.taxapp.MainActivity;
import com.bbs.utils.UiUtil;
import com.bbs.widgets.BBSHeader;
import com.bbs.widgets.BBSHeader.OnNavButtonClickListener;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class BBSFragmentBase extends Fragment implements OnNavButtonClickListener {
	public String mPageID;
	protected BBSHeader mHeader;
	
	protected MainActivity mMainActivity;
	protected RelativeLayout mRootLayout;
	
	public boolean mbFragmentLayoutDone = false;
	
	//view create function
	public void createHeader(ViewGroup view, String title, BBSHeader.BBSHeaderButtonType left, BBSHeader.BBSHeaderButtonType right) {
		mHeader = new BBSHeader(mMainActivity, view, title, left, right);
		mHeader.setNavBarListener(this);
	}
	
	public String getPageID() {
		return mPageID;
	}

	@Override
	public void onNavBarRightButtonClicked() {
		
	}

	@Override
	public void onNavBarLeftButtonClicked() {
		
	}
	
	public void hideCurrentKeyBoard() {
		View focus = mMainActivity.getCurrentFocus();
		if(EditText.class.isInstance(focus))
			UiUtil.hideKeyboard(mMainActivity, focus);
	}
	
	public void showKeyboard() {
		UiUtil.showKeyboard(mMainActivity);
	}
	
	/*
	public TSLoadingView showLoadingView() {
		hideCurrentKeyBoard();
		TSLoadingView res = TSLoadingView.showLoadingView(mMainActivity, mRootLayout, null, this);
		mMainActivity.setEnableSlidingMenu(false);
		return res;
	}
	
	
	public void removeLoadingView() {
		TSLoadingView.removeLoadingView(mRootLayout, null);
		mMainActivity.setEnableSlidingMenu(true);
	}
	
	@Override
	public void onCancelClicked() {
		TSApiInterface.getInstance().cancelCurrentRequest();
		removeLoadingView();
	}
	*/
	
	public void fragmentAppeared() {
		
	}
	
	public void updatePage() {
//		if(mbFragmentLayoutDone)
//			fragmentAppeared();
		
	}

}
