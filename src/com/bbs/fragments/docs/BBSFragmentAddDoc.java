package com.bbs.fragments.docs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.bbs.api.BBSApiSettings;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.preference.BBSPreference;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.utils.UiUtil;
import com.bbs.widgets.BBSHeader.BBSHeaderButtonType;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class BBSFragmentAddDoc extends BBSFragmentBase implements View.OnClickListener{
	public static String PAGEID = "AddDoc"; 

	private ImageView btn_take_photo;
	private EditText doc_name;
    
	private static final int ACTION_REQUEST_GALLERY = 1000;
	private static final int ACTION_REQUEST_CAMERA = 1001;
	private Uri initialURI;
	private String mCameraCapturedPhotoFileName = "";
	
	//constructor
	public BBSFragmentAddDoc() {
		super();
		
		mPageID = PAGEID;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_add_doc, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.add_document),
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
		
		doc_name = (EditText) view.findViewById(R.id.doc_name);
		
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
		
	}
	
	@Override
	public void onNavBarLeftButtonClicked() {
		mMainActivity.popLastPage();
	}
	
	@Override
	public void onNavBarRightButtonClicked() {
		addDocument();
	}
	
	private void addDocument() {
		// Add document process
		JSONObject document = new JSONObject();
		
		if (doc_name.getText().toString().equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_enter_document_name));
			return;
		}
		
		if (mCameraCapturedPhotoFileName.equals("")) {
			UiUtil.showToastMessage(mMainActivity, getString(R.string.please_select_your_document_image));
			return;
		}
		
		try {
			document.put("name", doc_name.getText().toString());
			document.put("image_url", mCameraCapturedPhotoFileName);
			
			BBSApiSettings.getInstance(mMainActivity).addTrackersValue(BBSPreference.PREFERENCE_DOCUMENTS, document.toString());
			mMainActivity.popLastPage();
		} catch(JSONException e) {
			
		}
	}
	
}
