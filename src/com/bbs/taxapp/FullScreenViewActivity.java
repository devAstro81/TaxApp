package com.bbs.taxapp;

import com.bbs.api.BBSApiSettings;
import com.bbs.widgets.BBSTouchImageView;

import android.app.Activity;
import android.os.Bundle;

public class FullScreenViewActivity extends Activity{
	
	private BBSTouchImageView img_view;
	private String bmp_url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);
		
		Bundle b = getIntent().getExtras();
		bmp_url = b.getString("image_url");
		
		img_view = (BBSTouchImageView) findViewById(R.id.imgDisplay);
		img_view.setImageBitmap(BBSApiSettings.getInstance(this).getBitmapImage(bmp_url));
	}

}