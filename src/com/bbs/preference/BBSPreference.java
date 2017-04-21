package com.bbs.preference;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

public class BBSPreference {
	public final static String PREFERENCE_KEY = "BBS";
	
	public final static String PREFERENCE_DONATIONS 	= "bbs_donations";
	public final static String PREFERENCE_MILEAGE 		= "bbs_mileage";
	public final static String PREFERENCE_CHARGES 		= "bbs_charges";
	public final static String PREFERENCE_RECEIPTS 		= "bbs_receipts";
	public final static String PREFERENCE_CHECKLIST 	= "bbs_checklist";
	public final static String PREFERENCE_DOCUMENTS 	= "bbs_documents";
	public final static String PREFERENCE_FAVORITE 		= "bbs_favorites";
	
	static BBSPreference g_instance;
	
	Context mContext;
	
	public BBSPreference(Context ctx) {
		mContext = ctx;
	}
	
	public static BBSPreference getInstance(Context ctx) {
		if (g_instance == null)
			g_instance = new BBSPreference(ctx);
		
		return g_instance;
	}
	
	public void setSettings(String key, String value) {
		SharedPreferences settings = mContext.getSharedPreferences(PREFERENCE_KEY, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
	}
	
	public String getSettings(String key) {
		SharedPreferences settings = mContext.getSharedPreferences(PREFERENCE_KEY, 0);
		return settings.getString(key, "");
	}
	
	public void removeSettings(String key) {
		SharedPreferences settings = mContext.getSharedPreferences(PREFERENCE_KEY, 0);
		SharedPreferences.Editor editor = settings.edit();		
		editor.remove(key);
		editor.commit();
	}
	
	public void setStore(String key, String value) {
		SharedPreferences settings = mContext.getSharedPreferences(key, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
	}
	
	public String getStore(String key) {
		SharedPreferences settings = mContext.getSharedPreferences(key, 0);
		return settings.getString(key, "");
	}	
	
	public void setFavoriteLocation(String email, boolean value) {
		SharedPreferences settings = mContext.getSharedPreferences(PREFERENCE_FAVORITE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(email, value);
        editor.commit();
	}
	
	public ArrayList<String> getFavoriteLocation() {
		ArrayList<String> locations = new ArrayList<String>();
		
		SharedPreferences settings = mContext.getSharedPreferences(PREFERENCE_FAVORITE, 0);
		Map<String, ?> allEntries = settings.getAll();
		for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
			
			// If value is true, then add email to favorite locations return value.
			if ((Boolean)entry.getValue())
				locations.add(entry.getKey());
		}
		
		return locations;
	}
}