package com.bbs.model;

public class BBSDonation {
	
	public String donationAmount;
	public String donationName;
	public String donationDate;
	
	public BBSDonation() {
		
	}
	
	public BBSDonation(String name, String date, String amount) {
		setDonationName(name);
		setDonationDate(date);
		setDonationAmount(amount);
	}
	
	public String getDonationAmount() {
		return donationAmount;
	}
	public void setDonationAmount(String donationAmount) {
		this.donationAmount = donationAmount;
	}
	public String getDonationName() {
		return donationName;
	}
	public void setDonationName(String donationName) {
		this.donationName = donationName;
	}
	public String getDonationDate() {
		return donationDate;
	}
	public void setDonationDate(String donationDate) {
		this.donationDate = donationDate;
	}
}