package com.bbs.tableviews;

import com.bbs.listeners.BBSChecklistListener;
import com.bbs.model.BBSCheckItem;
import com.bbs.taxapp.R;
import com.bbs.widgets.BBSTextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class TableCellBuilder {
	
	private static BBSChecklistListener mListener;
	
	public static void setText(View view, int id, String text) {
		TextView txtLabel = (TextView)view.findViewById(id);
 		txtLabel.setText(text);
	}
 	
 	public static View buildHeaderCell(Context ctx, View view, String headerText) {
 		if(view == null) {
 			LayoutInflater flater = LayoutInflater.from(ctx);
 			view = flater.inflate(R.layout.cell_header, null);
 		}
 		
 		BBSTextView header = (BBSTextView)view.findViewById(R.id.txt_header);
 		header.setText(headerText);
 		
 		return view;
 	}
 	
 	public static View buildBBSCell(Context ctx, View view, BBSCheckItem item, BBSChecklistListener listener) {
 		mListener = listener;
 		
 		if(view == null) {
 			LayoutInflater flater = LayoutInflater.from(ctx);
 			view = flater.inflate(R.layout.cell_row, null);
 		}
 		
 		boolean itemChecked = item.getItemValue() == 1 ? true : false;
 		final int item_id = item.getItemIndex();
 		
 		BBSTextView rowText = (BBSTextView)view.findViewById(R.id.txt_cell_item);
 		CheckBox cellCheck = (CheckBox) view.findViewById(R.id.cell_check);
 		
 		rowText.setText(String.valueOf(item.getItemName()));
 		cellCheck.setChecked(itemChecked);
 		cellCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				mListener.onCheckChanged(item_id, isChecked);
			}
		});
 			
 		return view;
 	}
 	
}
