package com.bbs.adapters;

import java.util.ArrayList;

import com.bbs.model.BBSReceipt;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.widgets.BBSTextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class BBSReceiptsArrayAdapter extends ArrayAdapter<BBSReceipt> {
	
	LayoutInflater mInflater;
	MainActivity mMainActivity;

	private ArrayList<BBSReceipt> filteredData = null;

	public BBSReceiptsArrayAdapter(Context context, MainActivity activity, int resource, ArrayList<BBSReceipt> objects) {
		super(context, resource, objects);
		
		mMainActivity = activity;
		filteredData = objects;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return filteredData.size();
	}

	public BBSReceipt getItem(int position) {
		return filteredData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mMainActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.cell_receipt, parent,
					false);
		}
		
		BBSTextView txt_name = (BBSTextView)convertView.findViewById(R.id.txt_cell_name);
		BBSTextView txt_date = (BBSTextView)convertView.findViewById(R.id.txt_cell_date);
		BBSTextView txt_cat = (BBSTextView)convertView.findViewById(R.id.txt_cell_cat);
		BBSTextView txt_amount = (BBSTextView)convertView.findViewById(R.id.txt_cell_amount);

		BBSReceipt item_info = filteredData.get(position);
		
		txt_name.setText(item_info.getReceiptName());
		txt_cat.setText("Category: " + item_info.getReceiptCategory());
		txt_date.setText(item_info.getReceiptDate());
		txt_amount.setText("$" + item_info.getReceiptAmount());

		return convertView;
	}
}