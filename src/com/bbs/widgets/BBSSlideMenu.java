package com.bbs.widgets;

/*
 * This class handles the slide menu logic
 */

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbs.taxapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class BBSSlideMenu extends SlidingMenu {

	
	int m_currentId = R.id.home_container;
	OnSlideMenuClickListener m_listener = null; 
	
	int[] menuItemIds = new int[] {
			R.id.home_container,
			R.id.calculators_container,
			R.id.trackers_container,
			R.id.taxpc_container,
			R.id.mydocs_container,
			R.id.contacts_container,
			R.id.aboutus_container,
			R.id.legaldisc_container,
			R.id.privacy_container
	};
	
	
	public BBSSlideMenu(Activity context) {
		super(context, SLIDING_CONTENT);
		
		setMode(SlidingMenu.LEFT);
		setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		setShadowWidthRes(R.dimen.shadow_width);
        setShadowDrawable(R.drawable.shadow);
        setBehindOffsetRes(R.dimen.slidingmenu_offset);
        setFadeDegree(0.5f);
        setMenu(R.layout.side_menu);
        
        for(int i = 0; i < menuItemIds.length; i++) {
        	addSlideItemClickHandler(menuItemIds[i]);
        }
       
        setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
				updateIcons();
			}
		});
	}
	
	
	
	/*
	 * Update icon and text states - call this function when you manually updated the central panel
	 */
	public void updateIcons() {
		//cancels item states first
		for(int i = 0; i < menuItemIds.length; i++) {
			setItemState(menuItemIds[i], menuItemIds[i] == m_currentId);
		}
	}

	
	/*
	 * Set current selection - id should be one of menuItemIds variable
	 */
	public void setCurrentId(int id) {
		m_currentId = id;
		updateIcons();
	}
	
	public int getCurrentId() {
		return m_currentId;
	}
	
	
	/*
	 * Sets menu click listener - calls when a menu item is clicked
	 */
	public void setOnMenuClickListener(OnSlideMenuClickListener listener) {
		m_listener = listener;
	}
	
	
	//////////////////////////////////////// Private Utils Function /////////////////////////////////
	private void addSlideItemClickHandler(int containerId) {
		findViewById(containerId).setOnClickListener(m_SlideMenuClickListener);
	}
	
	View.OnClickListener m_SlideMenuClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			showContent(true);
			if(m_listener != null) {
				if(m_listener.onMenuClicked(v.getId())) {
					setCurrentId(v.getId());
				}
			}
			else {
				setCurrentId(v.getId());
			}
		}
	};

	private void setItemState(int id, boolean state) {
		ViewGroup itemContainer = (ViewGroup)findViewById(id);
		for(int i = 0; i < itemContainer.getChildCount(); i++) {
			View child = itemContainer.getChildAt(i);
			if(ImageView.class.isInstance(child)) {
				child.setActivated(state);
			}
			else if(TextView.class.isInstance(child)) {
				((TextView)child).setTextColor(getContext().getResources().getColor(
						state ? R.color.slide_menu_text_color_active : R.color.slide_menu_text_color));
			}
		}
	}
	
	
	public interface OnSlideMenuClickListener {
		public abstract boolean onMenuClicked(int id);
	}
}
