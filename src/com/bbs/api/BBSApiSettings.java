package com.bbs.api;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbs.listeners.BBSCalcResultListener;
import com.bbs.preference.BBSPreference;
import com.bbs.taxapp.R;
import com.bbs.utils.UiUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

public class BBSApiSettings {
	
	static BBSApiSettings g_instance;
	
	BBSCalcResultListener mListener;
	
	Context mContext;
	public BBSApiSettings(Context ctx) {
		mContext = ctx;
	}
	
	public static BBSApiSettings getInstance(Context ctx) {
		if(g_instance == null) 
			g_instance = new BBSApiSettings(ctx);
		
		return g_instance;
	}
	
	public void calcSavings(float p, float pmt, float t, float n, float r, BBSCalcResultListener callback) {
		
		mListener = callback;
		
		float result = 0;
		float f_r = (float)r / 100;
		
		result = (float) (p * (Math.pow((1 + f_r / n), n * t)) + pmt * ((Math.pow((1 + f_r / n), n * t)) - 1) / (f_r / n));
		
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		
		mListener.onResult(formatter.format(result));
	}
	
	public void calcLoanAmort(float p, float r, float n, BBSCalcResultListener callback) {
		
		mListener = callback;
		
		float result = 0;
		float f_r = (float) r / 100 / 12;
		float f_n = (float) n * 12;
		
		result = (float) (p * (f_r / (1 - Math.pow((1 + f_r), (-f_n)))));
		
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		
		mListener.onResult(formatter.format(result));
	}
	
	public float calcGoalSaving(float g, float p, float y, float r) {
		float result = 0;
		float f_r = r / 100 / 12;
		
		result = (float) (f_r * ((g - p * (float)Math.pow((1 + f_r), (12 * y))) / ((float)Math.pow((1 + f_r), (12 * y)) - 1)));
		
		return result;
	}
	
	public float calcGoalTime(float g, float p, float s, float r) {
		float result = 0;
		float f_p = p;
		float f_r = r / 100 / 12;
		
		while(f_p < g) {
			f_p += s;
			f_p = (f_p * f_r) + (int)(f_p);
			result ++;
			if (result > 1200) {
				return result;
			}
		}
		
		return result;
	}
	
	public void calcTip(float b, float p, float t, BBSCalcResultListener callback) {
		
		mListener = callback;
		
		float result = 0;
		float f_t = t / 100;
		
		result = (float) (b * f_t / p);
		
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		
		mListener.onResult(formatter.format(result));
	}
	
	public void calcDebtPay(float b, float r, float n, BBSCalcResultListener callback) {
		mListener = callback;
		
		float result = 0;
		float f_r = (float) r / 100 / 12;
		
		result = (float) (b * f_r / (1 - Math.pow((1 + f_r), (-n))));
		
		mListener.onResult(Float.toString(result));
	}
	
	public void calcDebtTime(float b, float r, float p, BBSCalcResultListener callback) {
		mListener = callback;
		
		float result = 0;
		float f_r = (float) r / 100 / 12;
		
		result = (float) (- (Math.log(1 - f_r * b / p) / Math.log(1 + f_r)));
		
		mListener.onResult(Float.toString(result));
	}

	public String calcBudget(float income, float value) {
		String result = "0.00";
		if (income > 0 && value > 0) {
			result = Integer.toString(Math.round(value * 100 / income));
		}
		if (result.equals("0"))
			result = "0.00";
				
		return result;
	}
	
//	public float calcPayoffTMP(float a, float r, float m) { // Total Monthly Payment
//		float result = 0;
//		float f_r = (float) r / 100 / 12;
//		
//		result = (float) (a * f_r / (1 - Math.pow((1 + f_r), -m)));
//				
//		return result;
//	}

	public float calcPayoffTMP(float a, float r, float m) {
		float f_r = r / 100 / 12;
		float kk = 0;
	    if (m == 0) {
	        kk = 0;
	    } else if (f_r == 0) {
	        kk = (float)Math.ceil(a / m * 100) / 100;
	    } else {
	        kk = (float)Math.ceil(a * (f_r / (1.0 - Math.pow(1.0 + f_r, -m))) * 100) / 100;
	    }
	    return kk;
	}
	
	public void addTrackersValue(String key, String value) {
		try{
			String val_str = BBSPreference.getInstance(mContext).getStore(key);
			JSONArray array;
			if (val_str.equals("")) {
				array = new JSONArray();	
			} else {
				array = new JSONArray(val_str);
			}
			
			JSONObject json = new JSONObject(value);
			
			array.put(json);
			BBSPreference.getInstance(mContext).setStore(key, array.toString());
		} catch(JSONException e) {
			Log.e("Exception : ", e.toString());
		}
	}
	
	public String loadJSONFromAsset(String name) {
	    String json = null;
	    try {
	        InputStream is = mContext.getAssets().open(name);
	        int size = is.available();
	        byte[] buffer = new byte[size];
	        is.read(buffer);
	        is.close();
	        json = new String(buffer, "UTF-8");
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return null;
	    }
	    return json;
	}
	
	// App Settings
	public JSONArray remove(final int idx, final JSONArray from) {
	    final List<JSONObject> objs = asList(from);
	    objs.remove(idx);

	    final JSONArray ja = new JSONArray();
	    for (final JSONObject obj : objs) {
	        ja.put(obj);
	    }

	    return ja;
	}

	public static List<JSONObject> asList(final JSONArray ja) {
	    final int len = ja.length();
	    final ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
	    for (int i = 0; i < len; i++) {
	        final JSONObject obj = ja.optJSONObject(i);
	        if (obj != null) {
	            result.add(obj);
	        }
	    }
	    return result;
	}
	
	public Bitmap getBitmapImage(String img_url) {
		try {
			ExifInterface exif = new ExifInterface(img_url);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			
			Matrix matrix = new Matrix();
			if (orientation == 6) 
				matrix.postRotate(90);
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			Bitmap bitmap = BitmapFactory.decodeFile(img_url, options);
			
			if (bitmap != null) {
				return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);	
			} else {
				return null;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void createPDF() {
		
	}
	
	public void sendEmail(String pdf_url) {
		ArrayList<String>fav_locations = BBSPreference.getInstance(mContext).getFavoriteLocation();
		if (fav_locations.size() <= 0) {
			UiUtil.setSpUnit(mContext, UiUtil.getSpUnit(mContext) * 2);
			new AlertDialog.Builder(mContext)
			.setTitle(mContext.getResources().getString(R.string.app_name))
			.setMessage(mContext.getResources().getString(R.string.no_fav_locations))
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog, int whichButton) {
			        // Delete Item here
			    	UiUtil.setSpUnit(mContext, UiUtil.getSpUnit(mContext) / 2);
			    	
			    }})
			 .show();
			return;
		}
		
		Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.fromParts("mailto",fav_locations.get(0), null));
//		Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.fromParts("mailto","lance8887@gmail.com", null));
		emailIntent.setType("application/pdf");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, fav_locations.toArray(new String[fav_locations.size()]));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.app_name));
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
		emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + pdf_url));
		mContext.startActivity(Intent.createChooser(emailIntent, mContext.getResources().getString(R.string.choose_email_client)));
	}

}