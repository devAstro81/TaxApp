package com.bbs.fragments.trackers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.bbs.api.BBSApiSettings;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.preference.BBSPreference;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.utils.BBSSettings;
import com.bbs.utils.UiUtil;
import com.bbs.widgets.BBSHeader.BBSHeaderButtonType;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class BBSFragmentAddReceipt extends BBSFragmentBase implements View.OnClickListener, OnDateSetListener{
	public static String PAGEID = "AddReceipt"; 

	private ImageView btn_take_photo;
	private EditText rep_amount;
	private EditText rep_name;
	private EditText rep_cat;
	private EditText rep_date;
	private int cur_year, cur_month, cur_day;
	private Button calendarButton;
	
	private static final String DATEPICKER_TAG = "datepicker";
	private static final int ACTION_REQUEST_GALLERY = 1000;
	private static final int ACTION_REQUEST_CAMERA = 1001;
	private Uri initialURI;
	private String mCameraCapturedPhotoFileName = "";
	
	final Calendar calendar = Calendar.getInstance();
    DatePickerDialog datePickerDialog;
    
	//constructor
	public BBSFragmentAddReceipt() {
		super();
		
		mPageID = PAGEID;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_add_receipt, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.add_receipt), 
				BBSHeaderButtonType.BBSButtonBack, 
				BBSHeaderButtonType.BBSButtonDone);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		btn_take_photo = (ImageView) view.findViewById(R.id.btn_take_photo);
		UiUtil.applyImageButtonEffect(btn_take_photo, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Choose Image Source");
				builder.setItems(new CharSequence[] {"Gallery", "Camera"}, 
				        new DialogInterface.OnClickListener() {

					@Override
				    public void onClick(DialogInterface dialog, int which) {
				        switch (which) {
				        case 0:

				            // GET IMAGE FROM THE GALLERY
				            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				            intent.setType("image/*");

				            Intent chooser = Intent.createChooser(intent, "Choose a Picture");
				            startActivityForResult(chooser, ACTION_REQUEST_GALLERY );

				            break;

				        case 1:
				            Intent getCameraImage = new Intent("android.media.action.IMAGE_CAPTURE");

				            File cameraFolder;

				            if (android.os.Environment.getExternalStorageState().equals
				                    (android.os.Environment.MEDIA_MOUNTED))
				                cameraFolder = new File(android.os.Environment.getExternalStorageDirectory(),
				                        "/BBSData/Image/");
				            else
				                cameraFolder= getActivity().getCacheDir();
				            if(!cameraFolder.exists())
				                cameraFolder.mkdirs();

				            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
				            String timeStamp = dateFormat.format(new Date());
				            String imageFileName = "picture_" + timeStamp + ".jpg";
				            mCameraCapturedPhotoFileName = Environment.getExternalStorageDirectory() +  
				                    "/BBSData/Image/" + imageFileName;
//				            Logger.warning(mCameraCapturedPhotoFileName);
				            File photo = new File(mCameraCapturedPhotoFileName);
				            getCameraImage.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
				            initialURI = Uri.fromFile(photo);

				            startActivityForResult(getCameraImage, ACTION_REQUEST_CAMERA );

				            break;

				        default:
				            break;
				        }
				    }
				});

				builder.show();
			}
		});
		
		rep_amount = (EditText) view.findViewById(R.id.ramount_text);
		rep_name = (EditText) view.findViewById(R.id.rname_text);
		rep_cat = (EditText) view.findViewById(R.id.rcat_text);
		rep_date = (EditText) view.findViewById(R.id.rdate_text);
		
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd yyyy");
		String date = formatter.format(calendar.getTime());
		cur_year = calendar.get(Calendar.YEAR); 
		cur_month = calendar.get(Calendar.MONTH); 
		cur_day = calendar.get(Calendar.DAY_OF_MONTH);
		rep_date.setText(date);
		
		calendarButton = (Button) view.findViewById(R.id.btn_calendar);
		calendarButton.setOnClickListener(this);
		
		mRootLayout = view;
		return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	    if (resultCode == getActivity().RESULT_OK)    {

	        switch (requestCode) {
	        case ACTION_REQUEST_GALLERY:{
	        	Uri contentUri = data.getData();
	        	String[] proj = { MediaStore.Images.Media.DATA };
        	    Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        	    cursor.moveToFirst();
        	    mCameraCapturedPhotoFileName = cursor.getString(column_index);
	        	    
	        }
//	        	sendImageToServer(mCameraCapturedPhotoFileName);
	            break;

	        case ACTION_REQUEST_CAMERA:
//	        	sendImageToServer(mCameraCapturedPhotoFileName);
	            break;          
	        }

	    }
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_calendar) {
			// Set SpUnit double to show dialog original
			UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(getActivity()) * 2);
			datePickerDialog = DatePickerDialog.newInstance(this, cur_year, cur_month, cur_day, true);
			
            datePickerDialog.setYearRange(1985, 2028);
            datePickerDialog.setCloseOnSingleTapDay(false);
            datePickerDialog.show(mMainActivity.getSupportFragmentManager(), DATEPICKER_TAG);
            
		}
	}
	
	@Override
	public void onNavBarLeftButtonClicked() {
		mMainActivity.popLastPage();
	}
	
	@Override
	public void onNavBarRightButtonClicked() {
		addReceipt();
	}
	
	private void addReceipt() {
		// Add receipt process
		JSONObject receipt = new JSONObject();
		
		if (rep_amount.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_receipt_amount));
			return;
		}
		
		if (rep_name.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_receipt_name));
			return;
		}
		
		if (rep_cat.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_receipt_category));
			return;
		}
		
		if (rep_date.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_date));
			return;
		}
		
		if (mCameraCapturedPhotoFileName.equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_select_your_receipt_image));
			return;
		}
		
		try {
			receipt.put("amount", rep_amount.getText().toString());
			receipt.put("name", rep_name.getText().toString());
			receipt.put("category", rep_cat.getText().toString());
			receipt.put("date", rep_date.getText().toString());
			receipt.put("image_url", mCameraCapturedPhotoFileName);
			
			BBSApiSettings.getInstance(mMainActivity).addTrackersValue(BBSPreference.PREFERENCE_RECEIPTS, receipt.toString());
			mMainActivity.popLastPage();
		} catch(JSONException e) {
			
		}
	}

	/****************************/
	/**	Date Picker Controller **/
	/****************************/
	
	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		// TODO Auto-generated method stub
		cur_year = year;
		cur_month = month;
		cur_day = day;
		String date = BBSSettings.getConvertDate(cur_day + "/" + (cur_month + 1 ) + "/" + cur_year);
		rep_date.setText(date);
	}

	@Override
	public void onDialogDismiss() {
		// TODO Auto-generated method stub
		// Set SpUnit as default value
		UiUtil.setSpUnit(mMainActivity, UiUtil.getSpUnit(getActivity()) / 2);
	}
}
