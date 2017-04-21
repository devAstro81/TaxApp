package com.bbs.widgets;

import java.util.ArrayList;

import com.bbs.preference.BBSPreference;
import com.bbs.taxapp.R;
import com.bbs.utils.UiUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;
 
public class BBSContact extends RelativeLayout implements OnClickListener {
	
	private LinearLayout contact_header;
	private ImageView contact_pin;
	private BBSTextView contact_title;
	private ImageView contact_arrow;
	
	private LinearLayout contact_content;
	private BBSTextView contact_name;
	private BBSTextView contact_address;
	private BBSTextView contact_phone;
	private BBSTextView contact_toll;
	private BBSTextView contact_manager;
	private BBSTextView contact_email;
	private BBSTextView contact_hours;
	private BBSTextView contact_hours_detail;
	private ImageButton btn_fav;
	private ImageButton btn_call;
	private ImageButton btn_email;
	private ImageButton btn_map;
	
	private View divider_top;
	private View divider_bottom;
	
	private Boolean is_expand = false;
	private Boolean is_contactFav = false;
	
	private String phoneNumber;
	private String emailAddress;
	private String mAddress;
	
	private Context mContext;
    
    public BBSContact(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        
        initView();
    }

    public BBSContact(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        
        initView();
    }

    public BBSContact(Context context) {
        super(context);
        mContext = context;
        
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.widget_contact, null);
        addView(view);
        
        divider_top = (View) view.findViewById(R.id.divider_top);
        divider_bottom = (View) view.findViewById(R.id.divider_bottom);
        divider_top.setVisibility(View.GONE);
        divider_bottom.setVisibility(View.GONE);
        
        contact_header = (LinearLayout) view.findViewById(R.id.contact_header);
        contact_header.setOnClickListener(this);
        
        contact_pin = (ImageView) view.findViewById(R.id.contact_pin);
        contact_title = (BBSTextView) view.findViewById(R.id.contact_title);
        contact_arrow = (ImageView) view.findViewById(R.id.contact_arrow);
        
        contact_content = (LinearLayout) view.findViewById(R.id.contact_content);
        contact_content.setVisibility(View.GONE);
        
        contact_name = (BBSTextView) view.findViewById(R.id.contact_name);
        contact_address = (BBSTextView) view.findViewById(R.id.contact_address);
        contact_phone = (BBSTextView) view.findViewById(R.id.contact_phone);
        contact_toll = (BBSTextView) view.findViewById(R.id.contact_toll);
        contact_manager = (BBSTextView) view.findViewById(R.id.contact_manager);
        contact_email = (BBSTextView) view.findViewById(R.id.contact_email);
        contact_hours = (BBSTextView) view.findViewById(R.id.contact_hours);
        contact_hours_detail = (BBSTextView) view.findViewById(R.id.contact_hours_detail);
        
        // Set Font as Roboto-light
        Typeface roboto_light = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        contact_address.setTypeface(roboto_light);
        contact_phone.setTypeface(roboto_light);
        contact_toll.setTypeface(roboto_light);
        contact_email.setTypeface(roboto_light);
        contact_hours_detail.setTypeface(roboto_light);
        
        btn_fav = (ImageButton) view.findViewById(R.id.btn_fav);
        UiUtil.applyImageButtonEffect(btn_fav, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (!is_contactFav) {
					setFavValue(true);
				} else {
					setFavValue(false);
				}
			}
		});
        
        btn_call = (ImageButton) view.findViewById(R.id.btn_call);
        UiUtil.applyImageButtonEffect(btn_call, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent callIntent = new Intent(Intent.ACTION_CALL);
			    callIntent.setData(Uri.parse("tel:" + phoneNumber));
			    mContext.startActivity(callIntent);
			}
		});
        
        btn_email = (ImageButton) view.findViewById(R.id.btn_email);
        UiUtil.applyImageButtonEffect(btn_email, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
			            "mailto",emailAddress, null));
				intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
				intent.putExtra(Intent.EXTRA_TEXT, "Hello");
				mContext.startActivity(Intent.createChooser(intent, getResources().getString(R.string.choose_an_email_client)));
			}
		});
        
        btn_map = (ImageButton) view.findViewById(R.id.btn_map);
        UiUtil.applyImageButtonEffect(btn_map, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				String uriBegin = "geo:" + latitude + "," + longitude;
//				String query = latitude + "," + longitude + "(" + mapLabel + ")";
//				String encodedQuery = Uri.encode(query);
//				String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
//				Uri uri = Uri.parse(uriString);
				String address = mAddress.replace("\n", " ");
				address = address.replace(" ", "+");
				address = address.replace("#", "%23");
				
				Uri uri = Uri.parse("geo:0,0?q=" + address);
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
				mContext.startActivity(intent);
			}
		});
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.contact_header) {
			if (!is_expand) {
				expand(contact_content);
			} else {
				collapse(contact_content);
			}
		}
	}
	
    public void setViewValues(Context context, String title, String name, 
    		String address, String phone, String toll, String manager,
    		String email, String hours, String hours_detail) {
    	
        contact_title.setText(title);
        contact_name.setText(name);
        contact_address.setText(address);
        contact_phone.setText("Phone: " + phone);
        contact_toll.setText(toll);
        contact_manager.setText(manager);
        contact_email.setText(email);
        contact_hours.setText(hours);
        contact_hours_detail.setText(hours_detail);
        
        phoneNumber = phone;
        emailAddress = email;
        mAddress = address;
        
        // Get favorite locations and set favorite button value
        ArrayList<String> fav_locations = BBSPreference.getInstance(mContext).getFavoriteLocation();
        if (fav_locations.contains(emailAddress)) {
        	setFavValue(true);
        }
    }
    
    public void expandView() {
    	is_expand = true;
    	contact_pin.setImageResource(R.drawable.contact_pin_active);
    	contact_title.setTextColor(getResources().getColor(R.color.slide_menu_text_color_active));
    	contact_arrow.setImageResource(R.drawable.nav_arrow_up);
    	divider_top.setVisibility(View.VISIBLE);
        divider_bottom.setVisibility(View.VISIBLE);
    }
    
    public void collapseView() {
    	is_expand = false;
    	contact_pin.setImageResource(R.drawable.contact_pin);
    	contact_title.setTextColor(getResources().getColor(R.color.bbs_menu_text_color));
    	contact_arrow.setImageResource(R.drawable.nav_arrow_down);
    	divider_top.setVisibility(View.GONE);
        divider_bottom.setVisibility(View.GONE);
    }
    

	public Boolean getFavValue() {
		return this.is_contactFav;
	}
	
	public void setFavValue(boolean isFav) {
		this.is_contactFav = isFav;
		BBSPreference.getInstance(mContext).setFavoriteLocation(emailAddress, isFav);
		setFavButtonImage();
	}
	
    public void setFavButtonImage() {
    	if (is_contactFav) {
			btn_fav.setImageResource(R.drawable.contact_star_on);
		} else {
			btn_fav.setImageResource(R.drawable.contact_star_off);
		}
    }
    
    /*	Expand & Collapse action  */
    
    public void expand(final View v) {
    	
    	expandView();
    	
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void collapse(final View v) {
    	
    	collapseView();
    	
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

}