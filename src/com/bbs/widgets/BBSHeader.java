package com.bbs.widgets;

import com.bbs.taxapp.R;
import com.bbs.utils.UiUtil;
import com.bbs.utils.UiUtil.FontType;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BBSHeader extends RelativeLayout {
	
	public enum BBSHeaderButtonType {
		BBSButtonMenu,
	    BBSButtonBack,
	    
	    
	    BBSButtonDone,
	    BBSButtonPlus,
	    BBSButtonMore
	}
	
	BBSHeaderButtonType	mLeftButtonType;
	BBSHeaderButtonType	mRightButtonType;
	
	View mHeader;
	View mLeftButtonContainer;
	View mRightButtonContainer;
	ImageView mLeftButton;
	ImageView mRightButton;
	TextView mTextTitle;
	
	OnNavButtonClickListener mListener;
	
	public BBSHeader(Context context, 
			ViewGroup header,
			String title,
			BBSHeaderButtonType leftType, BBSHeaderButtonType rightType) {
		super(context);
		
		mHeader = header;
		
		mLeftButtonContainer = mHeader.findViewById(R.id.leftbtn_container);
		mRightButtonContainer = mHeader.findViewById(R.id.rightbtn_container);
		
		mLeftButton = (ImageView)mHeader.findViewById(R.id.img_leftBtn);
		mRightButton = (ImageView)mHeader.findViewById(R.id.img_rightBtn);
		mTextTitle = (TextView)mHeader.findViewById(R.id.txt_title);
		
		mTextTitle.setText(title);
		UiUtil.setFont(getContext(), mTextTitle, FontType.regularFontBig);
		
		mLeftButtonType = leftType;
		mRightButtonType = rightType;
		
		updateHeaderView();
	}
	
	public void updateHeaderView() {
		if(mLeftButtonType == null) {
			mLeftButton.setVisibility(View.INVISIBLE);
		}
		else 
			mLeftButton.setImageResource(getImageIdForButton(mLeftButtonType));
		
		if(mRightButtonType == null) {
			mRightButton.setVisibility(View.INVISIBLE);
		}
		else 
			mRightButton.setImageResource(getImageIdForButton(mRightButtonType));
		
		mRightButtonContainer.setOnClickListener(mOnClickListener);
		mLeftButtonContainer.setOnClickListener(mOnClickListener);
	}
	
	public void setTitle(String title) {
		mTextTitle.setText(title);
	}
	
	public void setNavBarListener(OnNavButtonClickListener listener) {
		mListener = listener;
	}
	
	public void setRightButton(BBSHeaderButtonType type) {
		mRightButtonType = type;
		updateHeaderView();
	}
	
	public void setLeftButton(BBSHeaderButtonType type) {
		mLeftButtonType = type;
		updateHeaderView();
	}
	
	public interface OnNavButtonClickListener{
		public abstract void onNavBarRightButtonClicked();
		public abstract void onNavBarLeftButtonClicked();
	}
	
	
	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mListener != null) {
				if(v == mLeftButtonContainer) {
					mListener.onNavBarLeftButtonClicked();
				}
				else if(v == mRightButtonContainer) {
					mListener.onNavBarRightButtonClicked();
				}
			}
		}
	};
	
	private int getImageIdForButton(BBSHeaderButtonType type) {
		int resId = R.drawable.nav_menu;
		switch(type) {
			case BBSButtonMenu:
				resId = R.drawable.nav_menu;
				break;
			case BBSButtonBack:
				resId = R.drawable.nav_back;
				break;
			case BBSButtonDone:
				resId = R.drawable.nav_done;
				break;
			case BBSButtonPlus:
				resId = R.drawable.nav_plus;
				break;
			case BBSButtonMore:
				resId = R.drawable.nav_more;
				break;
		}
		return resId;
	}

}
