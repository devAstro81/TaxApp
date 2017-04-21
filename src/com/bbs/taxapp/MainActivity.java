package com.bbs.taxapp;

import java.util.List;

import com.bbs.api.BBSApiSettings;
import com.bbs.fragments.BBSFragmentAbout;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.fragments.BBSFragmentCheckList;
import com.bbs.fragments.BBSFragmentContacts;
import com.bbs.fragments.BBSFragmentHome;
import com.bbs.fragments.BBSFragmentLegal;
import com.bbs.fragments.BBSFragmentPrivacy;
import com.bbs.fragments.calculators.BBSFragmentCalculators;
import com.bbs.fragments.docs.BBSFragmentMyDocs;
import com.bbs.fragments.trackers.BBSFragmentTrackers;
import com.bbs.utils.UiUtil;
import com.bbs.widgets.BBSSlideMenu;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;

public class MainActivity extends FragmentActivity {
	
	public String mCurrentPageID;
	public BBSFragmentBase mCurrentPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_main);
		
		initUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.home, menu);
		
		return true;
	}


	@Override
	public void onResume() {
		super.onResume();
		UiUtil.setSpUnit(this, UiUtil.getScreenWidth(this) / 800.0);
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		if(mCurrentPage != null) {
			mCurrentPage.onCreateContextMenu(menu, v, menuInfo);
		}
	}
	
	public boolean onContextItemSelected(MenuItem item) {
		if(mCurrentPage != null) {
			return mCurrentPage.onContextItemSelected(item);
		}
		return false;
	}
	
	BBSFragmentBase[] mFragments;
	BBSFragmentHome homeFragment;
	BBSFragmentCheckList testFragment;
	
	private void initUI() {
		//we use sp for all units 
		UiUtil.setSpUnit(this, UiUtil.getScreenWidth(this) / 800.0);
		
		//initialize sliding menu
		initSlidingMenu();
		
		//first disable slidemenu
//		mSlideMenu.setSlidingEnabled(false);
		mSlideMenu.setOnMenuClickListener(mMenuListener);
		
		//home fragment
		homeFragment = new BBSFragmentHome();
		setFragment(homeFragment, false);
		
//		testFragment = new BBSFragmentCheckList();
//		setFragment(testFragment, false);
		
		//initialize various fragments
		mFragments = new BBSFragmentBase[] {
			new BBSFragmentHome(),
			new BBSFragmentCalculators(),
			new BBSFragmentTrackers(),
			new BBSFragmentCheckList(),
			new BBSFragmentMyDocs(),
			new BBSFragmentContacts(),
			new BBSFragmentAbout(),
			new BBSFragmentLegal(),
			new BBSFragmentPrivacy(),
		};
		
		getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
			@Override
			public void onBackStackChanged() {
				BBSFragmentBase fr = (BBSFragmentBase)getSupportFragmentManager().findFragmentById(R.id.layout_content);
				if(fr != null) {
					mCurrentPage = fr;
					mCurrentPageID = fr.getPageID();
				}
			}
		});
	}
	
	private void setFragment(BBSFragmentBase fragment, boolean bAnim) {
		FragmentManager fm;
		FragmentTransaction ft;
		fm = getSupportFragmentManager();
		
		fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		//fm.popBackStack("start", FragmentManager.POP_BACK_STACK_INCLUSIVE);
		
       	ft = fm.beginTransaction();
       	if(bAnim == true)
       		ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
       	
        ft.replace(R.id.layout_content, fragment);
        //ft.addToBackStack("start");
        ft.commit();
        
        mCurrentPageID = fragment.getPageID();
        mCurrentPage = fragment;
	}
	
	private void switchPage(final String pageId, boolean bAnim) {
		for(int i = 0; i < mFragments.length; i++) {
			BBSFragmentBase fragment = mFragments[i];
			if(fragment.getPageID().equals(pageId)) {
				setFragment(fragment, bAnim);
				fragment.updatePage();
				break;
			}
		}
	}
	
	public void addPage(BBSFragmentBase fragment) {
		addPage(fragment, true);
	}
	
	public void addPage(BBSFragmentBase fragment, boolean bAnim) {
		
		UiUtil.hideKeyboard(this, this.getCurrentFocus());
		
		FragmentManager fm;
		FragmentTransaction ft;
		
		fm = getSupportFragmentManager();
       	ft = fm.beginTransaction();
       	if(bAnim)
       		ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
       	
       	ft.replace(R.id.layout_content, fragment);
        ft.addToBackStack(fragment.mPageID);
        ft.commit();
        
        mCurrentPage = fragment;
        mCurrentPageID = fragment.getPageID();
        
	}
	
	public void popLastPage() {
		FragmentManager fm = getSupportFragmentManager();
       	fm.popBackStack();
       	
       	UiUtil.hideKeyboard(this, this.getCurrentFocus());
	}
	

	public void popToPage(BBSFragmentBase page) {
		FragmentManager fm = getSupportFragmentManager();
		fm.popBackStack(page.mPageID, 0);
		
		UiUtil.hideKeyboard(this, this.getCurrentFocus());
	}
	
	public BBSFragmentBase getPageInStack(String pageId) {
		FragmentManager fm = getSupportFragmentManager();
       	List<Fragment> list = fm.getFragments();
       	for(int i = list.size() - 1; i >= 0 ; i--) {
       		BBSFragmentBase frag = (BBSFragmentBase)list.get(i);
       		if(frag != null && frag.mPageID != null && frag.mPageID.equals(pageId))
       			return frag;
       	}
       	return null;
	}
	
	public void showHome() {
		switchPage(BBSFragmentHome.PAGEID, true);
		mSlideMenu.setCurrentId(R.id.home_container);
	}
	
	public void showCalculators() {
		switchPage(BBSFragmentCalculators.PAGEID, true);
		mSlideMenu.setCurrentId(R.id.calculators_container);
	}
	
	public void showTrackers() {
		switchPage(BBSFragmentTrackers.PAGEID, true);
		mSlideMenu.setCurrentId(R.id.trackers_container);
	}
	
	public void showTaxPC() {
		switchPage(BBSFragmentCheckList.PAGEID, true);
		mSlideMenu.setCurrentId(R.id.taxpc_container);
	}
	
	public void showMyDocs() {
		switchPage(BBSFragmentMyDocs.PAGEID, true);
		mSlideMenu.setCurrentId(R.id.mydocs_container);
	}
	
	public void showContacts() {
		switchPage(BBSFragmentContacts.PAGEID, true);
		mSlideMenu.setCurrentId(R.id.contacts_container);
	}
	
	public void showAbout() {
		switchPage(BBSFragmentAbout.PAGEID, true);
		mSlideMenu.setCurrentId(R.id.aboutus_container);
	}
	
	public void showLegal() {
		switchPage(BBSFragmentLegal.PAGEID, true);
		mSlideMenu.setCurrentId(R.id.legaldisc_container);
	}
	
	public void showPrivacy() {
		switchPage(BBSFragmentPrivacy.PAGEID, true);
		mSlideMenu.setCurrentId(R.id.privacy_container);
	}
	
	public void showMenu(int menuId) {
		
	}
	
	/////////////////////////// Slide Menu //////////////////////////////
	BBSSlideMenu mSlideMenu;
	
	private void initSlidingMenu() {
		mSlideMenu = new BBSSlideMenu(this);
	}
	
	BBSSlideMenu.OnSlideMenuClickListener mMenuListener = new BBSSlideMenu.OnSlideMenuClickListener() {
		@Override
		public boolean onMenuClicked(int id) {
			if(id == R.id.home_container) {
				switchPage(BBSFragmentHome.PAGEID, false);
				return true;
			}
			else if(id == R.id.calculators_container) {
				switchPage(BBSFragmentCalculators.PAGEID, false);
				return true;
			}
			else if(id == R.id.trackers_container) {
				switchPage(BBSFragmentTrackers.PAGEID, false);
				return true;
			}
			else if(id == R.id.taxpc_container) {
				switchPage(BBSFragmentCheckList.PAGEID, false);
				return true;
			}
			else if(id == R.id.mydocs_container) {
				switchPage(BBSFragmentMyDocs.PAGEID, false);
				return true;
			}
			else if(id == R.id.contacts_container) {
				switchPage(BBSFragmentContacts.PAGEID, false);
				return true;
			}
			else if(id == R.id.aboutus_container) {
				switchPage(BBSFragmentAbout.PAGEID, false);
				return true;
			}
			else if(id == R.id.legaldisc_container) {
				switchPage(BBSFragmentLegal.PAGEID, false);
				return true;
			}
			else if(id == R.id.privacy_container) {
				switchPage(BBSFragmentPrivacy.PAGEID, false);
				return true;
			}
//			else if(id == R.id.logout_container) {
//				TSApiInterface.getInstance().logoutMerchant();
//				mCurrentPage.showLoadingView();
//				return false;
//			}
			return false;
		}
	};
	
	public void showSlideMenu() {
		mSlideMenu.toggle(true);
	}
	
	public boolean isSlidingMenuEnabled() {
		return mSlideMenu.isSlidingEnabled();
	}
	public void setEnableSlidingMenu(boolean enable) {
		mSlideMenu.setSlidingEnabled(enable);
	}
	
	public void showPageWithMenuId(int menuId) {
		mMenuListener.onMenuClicked(menuId);
		mSlideMenu.setCurrentId(menuId);
	}
	
	@Override
	public void onBackPressed() {
		
	}
	
	public Bitmap mCurrentBitmap;
	public void setPreviewBitmap(String img_url) {
		mCurrentBitmap = BBSApiSettings.getInstance(this).getBitmapImage(img_url);
	}
}
