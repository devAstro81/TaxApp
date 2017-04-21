package com.bbs.utils;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Android UI Utilities
 */

@SuppressWarnings("deprecation")
public class UiUtil {

	/**
	 * determine if the screen is landscape or portrait mode
	 */
	public static boolean isLandscapeOrientation(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		if (display.getWidth() < display.getHeight()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * get the screen size
	 */
	public static int getScreenWidth(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		return display.getWidth();
	}

	/**
	 * get the screen size
	 */
	public static int getScreenHeight(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		return display.getHeight();
	}

	/**
	 * get the screen size
	 */
	public static int getScreenWidth(Window window) {
		Display display = window.getWindowManager().getDefaultDisplay();
		return display.getWidth();
	}

	/**
	 * get the screen size
	 */
	public static int getScreenHeight(Window window) {
		Display display = window.getWindowManager().getDefaultDisplay();
		return display.getHeight();
	}

	/**
	 * set the SP unit for layouts
	 */
	public static float setSpUnit(Context context, double size) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float orig = metrics.scaledDensity;
		metrics.scaledDensity = (float) size;
		return orig;
	}

	/**
	 * get the SP unit for layouts
	 */
	public static float getSpUnit(Context context) {
		return context.getResources().getDisplayMetrics().scaledDensity;
	}

	/**
	 * make a view as a button (not ImageView)
	 */
	public static void applyButtonEffect(final View view,
			final Runnable onClickListener) {
		final ColorFilter filter = new LightingColorFilter(0xA0A0A0A0, 0);
		view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Drawable background = v.getBackground();
					if (background != null) {
						background.setColorFilter(filter);
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					Drawable background = v.getBackground();
					if (background != null) {
						background.setColorFilter(null);
					}
					if (onClickListener != null) {
						float x = event.getX();
						float y = event.getY();
						if (checkLocalPointInView(view, x, y)) {
							onClickListener.run();
						}
					}

				}
				v.onTouchEvent(event);
				return true;
			}
		});
	}

	/**
	 * make an image view as a button
	 */
	public static void applyImageButtonEffect(final ImageView view,
			final Runnable onClickListener) {
		final ColorFilter filter = new LightingColorFilter(0xA0A0A0A0, 0);
		view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					view.setColorFilter(filter);
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					view.setColorFilter(null);
					if (onClickListener != null) {
						float x = event.getX();
						float y = event.getY();
						if (checkLocalPointInView(view, x, y)) {
							onClickListener.run();
						}
					}
				}
				return true;
			}
		});
	}

	/**
	 * check a point that is in the view visible area
	 */
	public static boolean checkLocalPointInView(View view, float x, float y) {
		int width = view.getWidth();
		int height = view.getHeight();
		return 0 < x && x < width && 0 < y && y < height;
	}
	
	
	//////////////////////// Custom Switch with TextView ///////////////////////
	/* public static void setSwithOnOff(TextView switcher, boolean on) {
 		if(on) {
 			switcher.setText(R.string.on);
 			switcher.setBackgroundResource(R.drawable.green_round_fill);
 		}
 		else {
 			switcher.setText(R.string.off);
 			switcher.setBackgroundResource(R.drawable.red_round_fill);
 		}
 	}	*/
	//////////////////////////////////////////////////////////////////////////////

	// ////////////////////// Various Fonts ////////////////////////
	public enum FontType {
		boldFontSmall, // 10
		boldFontLight, // 11
		boldFontNormal, // 12
		boldFontBig, // 14
		boldFontBigger, // 15
		boldFontExtraBig, // 19

		regularFontSmall, 
		regularFontLight, 
		regularFontNormal, 
		regularFontBig,
		regularFontBigger
	};

	static HashMap<String, Typeface> stcFonts = null;
	
	public static Typeface getFont(Context ctx, boolean bRegular) {
		String family = bRegular ? "Roboto-Regular.ttf" : "Roboto-Medium.ttf";
		String key = family;
		if (stcFonts == null) {
			stcFonts = new HashMap<String, Typeface>();
		}

		Typeface tf = stcFonts.get(key);
		if (tf == null) {
			tf = Typeface.createFromAsset(ctx.getAssets(), "fonts/" + family);
			stcFonts.put(key, tf);
		}
		
		return tf;
	}

	public static void setFont(Context ctx, View tv, float size,
			boolean bRegular) {
		
		if(tv.isInEditMode())
			return;
		float spToPxRatio = 3;
		String family = bRegular ? "Roboto-Regular.ttf" : "Roboto-Medium.ttf";
		String key = family;
		if (stcFonts == null) {
			stcFonts = new HashMap<String, Typeface>();
		}

		Typeface tf = stcFonts.get(key);
		if (tf == null) {
			tf = Typeface.createFromAsset(ctx.getAssets(), "fonts/" + family);
			stcFonts.put(key, tf);
		}

		if (tv.getClass() == TextView.class) {
			((TextView) tv).setTypeface(tf);
			((TextView) tv).setTextSize(size * spToPxRatio);
		} else if (tv.getClass() == Button.class) {
			((Button) tv).setTypeface(tf);
			((Button) tv).setTextSize(size * spToPxRatio);
		} else if (tv.getClass() == EditText.class) {
			((EditText) tv).setTypeface(tf);
			((EditText) tv).setTextSize(size * spToPxRatio);
		}
	}

	public static void setFont(Context ctx, View tv, FontType type) {
		boolean bRegular = false;
		int size = 12;
		switch (type) {
		case boldFontSmall:
			size = 10;
			break;
		case boldFontLight:
			size = 11;
			break;
		case boldFontNormal:
			size = 12;
			break;
		case boldFontBig:
			size = 14;
			break;
		case boldFontBigger:
			size = 15;
			break;
		case boldFontExtraBig:
			size = 19;
			break;

		case regularFontSmall:
			bRegular = true;
			size = 10;
			break;
		case regularFontLight:
			bRegular = true;
			size = 11;
			break;
		case regularFontNormal:
			bRegular = true;
			size = 12;
			break;
		case regularFontBig:
			bRegular = true;
			size = 14;
			break;
		case regularFontBigger:
			bRegular = true;
			size = 15;
			break;
		}

		setFont(ctx, tv, size, bRegular);
	}

	// ////////////////////// Hide Key Board //////////////////////
	public static void showKeyboard(Context ctx) {
		InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
	}
	
	public static void hideKeyboard(Context ctx, View view) {
		if(view == null)
			return;
		InputMethodManager imm = (InputMethodManager) ctx
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	//////////////////////////////// Show Alerts //////////////////////////////
	public static void showAlert(Activity parent, String title, String message,
			String buttonTitle) {
		
		float sp = UiUtil.setSpUnit(parent, 2);
		try {
			new AlertDialog.Builder(parent).setTitle(title).setMessage(message)
					.setNeutralButton(buttonTitle, null)
					.setIcon(android.R.drawable.ic_dialog_alert).show();
		} finally {
			UiUtil.setSpUnit(parent, sp);
		}
	}

	public static void showAlert(Activity parent, int titleId, int messageId,
			int buttonId) {
		float sp = UiUtil.setSpUnit(parent, 2);
		try {
			new AlertDialog.Builder(parent).setTitle(parent.getString(titleId))
				.setMessage(parent.getString(messageId))
				.setNeutralButton(parent.getString(buttonId), null)
				.setIcon(android.R.drawable.ic_dialog_alert).show();
		} catch(Exception e) {
			
		} finally {
			UiUtil.setSpUnit(parent, sp);
		}
	}
	
	/*
	public static void showAlertYesNo(Activity parent, int titleId, int messageId, final TSDialogButtonListener listener) {
		try {
			TSDialog dialog = new TSDialog(parent);
			dialog.setTitle(parent.getString(titleId));
			dialog.setMessageText(parent.getString(messageId));
			dialog.setListener(new TSDialogListener() {
				@Override
				public void onButtonClicked(boolean positive, View customView) {
					listener.onButtonClicked(positive ? TSDialogButtonListener.OK : TSDialogButtonListener.CANCEL, null);
				}
			});
			dialog.show();
		} catch(Exception e) {
			
		} 
	}
	
	public static void showInputDialog(Activity parent, int titleId, int messageId, final TSDialogButtonListener listener) {
		try {
			final EditText editText = new EditText(parent);
			editText.setInputType(InputType.TYPE_CLASS_TEXT);
			setFont(parent, editText, FontType.regularFontNormal);
			
			TSDialog dialog = new TSDialog(parent);
			dialog.setTitle(parent.getString(titleId));
			dialog.setMessageText(parent.getString(messageId));
			dialog.setDialogContentView(editText);
			dialog.setListener(new TSDialogListener() {
				@Override
				public void onButtonClicked(boolean positive, View customView) {
					if(positive) {
						listener.onButtonClicked(
								TSDialogButtonListener.OK,
								editText.getText().toString());
					}
					else {
						listener.onButtonClicked(
								TSDialogButtonListener.CANCEL,
								null);
					}
				}
			});
			dialog.show();
		} catch(Exception e) {
			
		}
	}
	*/
	public static interface TSDialogButtonListener {
		public final static int OK = 1;
		public final static int CANCEL = 0;
		
		public abstract void onButtonClicked(int buttonIdx, String data);
	}

	// ////////////////////// fast blur effect ////////////////////////
	@SuppressLint("NewApi")
	public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {
		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		final RenderScript rs = RenderScript.create(context);
		final Allocation input = Allocation.createFromBitmap(rs,
				sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
				Allocation.USAGE_SCRIPT);
		final Allocation output = Allocation.createTyped(rs,
				input.getType());
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
				Element.U8_4(rs));
		script.setRadius(radius /* e.g. 3.f */);
		script.setInput(input);
		script.forEach(output);
		output.copyTo(bitmap);
		return bitmap;
	}
	
	public static void showToastMessage(Context ctx, String text) {
		int duration = Toast.LENGTH_SHORT;
		
		UiUtil.setSpUnit(ctx, UiUtil.getSpUnit(ctx) * 2);
		Toast toast = Toast.makeText(ctx, text, duration);
		toast.show();
		UiUtil.setSpUnit(ctx, UiUtil.getSpUnit(ctx) / 2);
	}
}
