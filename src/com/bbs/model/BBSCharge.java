package com.bbs.model;

public class BBSCharge {
	
	public String chargeAmount;
	public String chargeName;
	public String chargeDate;
	
	public BBSCharge() {
		
	}
	
	public BBSCharge(String name, String date, String amount) {
		setChargeName(name);
		setChargeDate(date);
		setChargeAmount(amount);
	}
	
	public String getChargeAmount() {
		return chargeAmount;
	}
	public void setChargeAmount(String chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	public String getChargeName() {
		return chargeName;
	}
	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}
	public String getChargeDate() {
		return chargeDate;
	}
	public void setChargeDate(String chargeDate) {
		this.chargeDate = chargeDate;
	}
}