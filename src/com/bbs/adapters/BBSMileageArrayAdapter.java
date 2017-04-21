package com.bbs.adapters;

import java.util.ArrayList;

import com.bbs.model.BBSMileage;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.widgets.BBSTextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class BBSMileageArrayAdapter extends ArrayAdapter<BBSMileage> {
	
	LayoutInflater mInflater;
	MainActivity mMainActivity;

	private ArrayList<BBSMileage> filteredData = null;

	public BBSMileageArrayAdapter(Context context, MainActivity activity, int resource, ArrayList<BBSMileage> objects) {
		super(context, resource, objects);
		
		mMainActivity = activity;
		filteredData = objects;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return filteredData.size();
	}

	public BBSMileage getItem(int position) {
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
			convertView = inflater.inflate(R.layout.cell_mileage, parent,
					false);
		}
		
		BBSTextView txt_name = (BBSTextView)convertView.findViewById(R.id.txt_cell_name);
		BBSTextView txt_date = (BBSTextView)convertView.findViewById(R.id.txt_cell_date);
		BBSTextView txt_amount = (BBSTextView)convertView.findViewById(R.id.txt_cell_amount);
		BBSTextView txt_carmake = (BBSTextView)convertView.findViewById(R.id.txt_cell_carmake);

		BBSMileage item_info = filteredData.get(position);
		
		txt_name.setText(item_info.getMileageReason());
		txt_date.setText(item_info.getMileageDate());
		txt_amount.setText((item_info.getMileageEAmount() - item_info.getMileageBAmount()) + "mi");
		txt_carmake.setText(item_info.getMileageCarMake());

		return convertView;
	}
}