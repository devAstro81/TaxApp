package com.bbs.adapters;

import java.util.ArrayList;

import com.bbs.model.BBSItem;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.widgets.BBSTextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class BBSArrayAdapter extends ArrayAdapter<BBSItem> {
	
	LayoutInflater mInflater;
	MainActivity mMainActivity;

	private ArrayList<BBSItem> filteredData = null;

	public BBSArrayAdapter(Context context, MainActivity activity, int resource, ArrayList<BBSItem> objects) {
		super(context, resource, objects);
		
		mMainActivity = activity;
		filteredData = objects;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return filteredData.size();
	}

	public BBSItem getItem(int position) {
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
			convertView = inflater.inflate(R.layout.cell_bbs_menu, parent,
					false);
		}
		
		ImageView img_item = (ImageView)convertView.findViewById(R.id.img_cell_item);
		BBSTextView img_text = (BBSTextView)convertView.findViewById(R.id.txt_cell_item);

		BBSItem item_info = filteredData.get(position);

		img_item.setImageResource(item_info.itemImage);
		img_text.setText(item_info.itemName);

		return convertView;
	}
}