package com.bbs.adapters;

import java.util.ArrayList;

import com.bbs.model.BBSCharge;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.widgets.BBSTextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class BBSChargesArrayAdapter extends ArrayAdapter<BBSCharge> {
	
	LayoutInflater mInflater;
	MainActivity mMainActivity;

	private ArrayList<BBSCharge> filteredData = null;

	public BBSChargesArrayAdapter(Context context, MainActivity activity, int resource, ArrayList<BBSCharge> objects) {
		super(context, resource, objects);
		
		mMainActivity = activity;
		filteredData = objects;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return filteredData.size();
	}

	public BBSCharge getItem(int position) {
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
			convertView = inflater.inflate(R.layout.cell_donation, parent,
					false);
		}
		
		BBSTextView txt_name = (BBSTextView)convertView.findViewById(R.id.txt_cell_name);
		BBSTextView txt_date = (BBSTextView)convertView.findViewById(R.id.txt_cell_date);
		BBSTextView txt_amount = (BBSTextView)convertView.findViewById(R.id.txt_cell_amount);

		BBSCharge item_info = filteredData.get(position);
		
		txt_name.setText(item_info.getChargeName());
		txt_date.setText(item_info.getChargeDate());
		txt_amount.setText("$" + item_info.getChargeAmount());

		return convertView;
	}
}