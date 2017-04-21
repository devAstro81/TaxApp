package com.bbs.widgets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.bbs.tableviews.TableCellBuilder;
import com.bbs.taxapp.R;
import com.bbs.utils.UiUtil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BBSChoiceDialog extends Dialog{
	
	Context mContext;
	
	public BBSChoiceDialog(Context context) {
		super(context);
		mContext = context;
	}

	protected BBSChoiceDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		mContext = context;
	}

	public BBSChoiceDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
	}
	
	TextView mTitle;
	TextView mQuestion;
	
	String mTitleString;
	
	FrameLayout mContentView;
	ListView mCustomView;
	
	BBSChoiceListener mListener;
	
	
	public void setListener(BBSChoiceListener listener) {
		mListener = listener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		WindowManager.LayoutParams params = this.getWindow().getAttributes();
		params.gravity = Gravity.BOTTOM;
		params.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		this.getWindow().setAttributes(params);
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.action_picker_dialog);
		
		mTitle = (TextView)findViewById(R.id.txt_title);
		if(mTitleString != null) {
			mTitle.setText(mTitleString);
		}
		
		
		mCustomView = (ListView)findViewById(R.id.lst_actions);
		mCustomView.setOnScrollListener(new OnScrollListener() {
			boolean scrolling = false;
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			    switch (scrollState) {
			    case OnScrollListener.SCROLL_STATE_IDLE:
			        if (scrolling){
			            // get first visible item
			            View itemView = view.getChildAt(0);
			            int top = Math.abs(itemView.getTop()); // top is a negative value
			            int bottom = Math.abs(itemView.getBottom());
			            int newIdx = 0; 
			            if (top >= bottom){
			                newIdx = view.getFirstVisiblePosition() + 1;
			            } else {
			                newIdx = view.getFirstVisiblePosition();
			            }
			            scrollToIdx(newIdx + 2, true);
			        }
			        scrolling = false;
			        break;
			    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			    case OnScrollListener.SCROLL_STATE_FLING:
			        scrolling = true;
			        break;
			    }
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
		
		mCustomView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				if(mBActionMode) {
					if(mListener != null) {
						mListener.onItemSelected(mObjectsCalib.get(position));
					}
					dismiss();
				}
				else
					scrollToIdx(position, true);
			}
		});
		
		mCustomView.setAdapter(new MyArrayAdapter(mContext, R.layout.cell_action));
		mCustomView.setDividerHeight(0);
		
		if(mBActionMode) {
			findViewById(R.id.hider1).setVisibility(View.GONE);
			findViewById(R.id.hider2).setVisibility(View.GONE);
			mCustomView.setDividerHeight(1);
			mCustomView.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
			findViewById(R.id.dialog_content_container).getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
			((LinearLayout.LayoutParams)findViewById(R.id.dialog_content_container).getLayoutParams()).leftMargin = 0;
			((LinearLayout.LayoutParams)findViewById(R.id.dialog_content_container).getLayoutParams()).topMargin = 0;
			((LinearLayout.LayoutParams)findViewById(R.id.dialog_content_container).getLayoutParams()).rightMargin = 0;
			((LinearLayout.LayoutParams)findViewById(R.id.dialog_content_container).getLayoutParams()).bottomMargin = 0;
		}
		else {
			Handler postHandler = new Handler();
			postHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if(mObjects != null && mObjectsCalib != null) {
						for(int i = 0; i < mObjectsCalib.size(); i++) {
							Object obj = mObjectsCalib.get(i);
							if(obj == mSelection) {
								scrollToIdx(i, false);
								break;
							}
						}
					}
				}
			}, 300);
			
			mCustomView.setSelector(android.R.color.transparent);
		}
	}
	
	boolean mBActionMode = false;
	int nCurrrentIdx = 2;
	public void scrollToIdx(int index, boolean animation) {
//		if(animation)
//			mCustomView.smoothScrollToPositionFromTop(index - 2, 0);
//		else 
			mCustomView.setSelectionFromTop(index - 2, 0);
		
		nCurrrentIdx = index;
	}
	
	public void setTitleString(String title) {
		mTitleString = title;
	}
	
	ArrayList<?> mObjects;
	ArrayList<Object> mObjectsCalib;
	Object mSelection;
	
	public void setArrayList(boolean actionMode, final ArrayList<?> objects, Object selection) {
		mObjects = objects;
		mSelection = selection; 
		
		mBActionMode = actionMode;
		
		mObjectsCalib = new ArrayList<Object>();
		
		if(mBActionMode == false) {
			mObjectsCalib.add(0, new String("")); //first one
			mObjectsCalib.add(0, new String("")); //first one
		}
		
		for(int i = 0; i < mObjects.size(); i++) {
			mObjectsCalib.add(mObjects.get(i));
		}
		
		if(mBActionMode == false) {
			mObjectsCalib.add(new String(""));    //last one
			mObjectsCalib.add(new String(""));    //last one
		}
		
		if(mSelection != null) {
			
		}
		else {
			mSelection = mObjects.get(0);
		}
	}
	
	public class MyArrayAdapter extends ArrayAdapter<Object> {
		Context mContext;
		
		public MyArrayAdapter(Context context, int resource) {
			super(context, resource);
			mContext = context;
		}
		
		@Override
		public int getCount() {
			return mObjectsCalib.size();
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.cell_action, parent, false);
			}
			TableCellBuilder.setText(convertView, R.id.txt_item_title, mObjectsCalib.get(position).toString() );
			return convertView;
		}
		
	}
	
	
	
	public static void showChooser(Context ctx, ArrayList<?> objects, Object selection, int titleStringId, BBSChoiceListener listener) {
		BBSChoiceDialog dialog = new BBSChoiceDialog(ctx, R.style.DialogSlideAnim);
		dialog.setArrayList(false, objects, selection);
		dialog.setListener(listener);
		dialog.setTitleString(ctx.getString(titleStringId));
		dialog.show();
	}
	
	public static void showActionChooser(Context ctx, ArrayList<?> objects, int titleStringId, BBSChoiceListener listener) {
		BBSChoiceDialog dialog = new BBSChoiceDialog(ctx, R.style.DialogSlideAnim);
		dialog.setArrayList(true, objects, null);
		dialog.setListener(listener);
		dialog.setTitleString(ctx.getString(titleStringId));
		dialog.show();
	}
	
	
	public static void showChooserEx(Context ctx, final ArrayList<?> objects, Object selection, final BBSChoiceListener listener) {
		float sp = UiUtil.setSpUnit(ctx, 2);
		
		try {
			final AlertDialog.Builder singlechoicedialog = new AlertDialog.Builder(ctx);
			final String[] items = new String[objects.size()];
			int pos = 0;
			for(int i = 0; i < objects.size(); i++) {
				items[i] = objects.get(i).toString();
				if(selection != null && objects.get(i) == selection) {
					pos = i;
				}
			}
			
			singlechoicedialog.setTitle("Please select...");
			singlechoicedialog.setSingleChoiceItems(items, -1,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							
							if(listener != null) {
								listener.onItemSelected(objects.get(item));
							}
							
							dialog.cancel();
						}
					});
			AlertDialog alert_dialog = singlechoicedialog.create();
			alert_dialog.show();
	
			// set defult select value
			alert_dialog.getListView().setItemChecked(pos, true);
		} finally {
			UiUtil.setSpUnit(ctx, sp);
		}
	}
	
	
	public static void showDateChooser(Context ctx, Date curDate, final BBSChoiceListener listener) {
		float sp = UiUtil.setSpUnit(ctx, 2);
		
		try {
			final Calendar myCalendar = Calendar.getInstance();
			
			DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
			    @Override
			    public void onDateSet(DatePicker view, int year, int monthOfYear,
			            int dayOfMonth) {
			    	myCalendar.set(Calendar.YEAR, year);
			        myCalendar.set(Calendar.MONTH, monthOfYear);
			        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			        
			        if(listener != null) {
			        	listener.onItemSelected(myCalendar.getTime());
			        }
			    }
			};
			
			if(curDate != null) {
				myCalendar.setTime(curDate);
			}
			new DatePickerDialog(ctx, dateListener, 
					myCalendar.get(Calendar.YEAR), 
					myCalendar.get(Calendar.MONTH) , 
					myCalendar.get(Calendar.DAY_OF_MONTH)).show(); 
		} finally {
			UiUtil.setSpUnit(ctx, sp);
		}
	}
	
	public interface BBSChoiceListener {
		public abstract void onItemSelected(Object item);
	}


}
