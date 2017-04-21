package com.bbs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * BBS Settings
 */

public class BBSSettings {
	public static String getConvertDate(String date) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, MMM dd yyyy");
		Date parsed = new Date();
		try {
			parsed = inputFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String outputText = outputFormat.format(parsed);
		return outputText;
	}
}
