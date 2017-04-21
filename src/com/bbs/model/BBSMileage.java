package com.bbs.model;

public class BBSMileage {
	
	public int mileageBAmount;
	public int mileageEAmount;
	public String mileageReason;
	public String mileageDate;
	public String mileageCarMake;
	public String mileageCarModel;
	public String mileageCarYear;
	
	public BBSMileage() {
		
	}
	
	public BBSMileage(String reason, String date, int b_amount, int e_amount, String carmake, String carmodel, String caryear) {
		setMileageReason(reason);
		setMileageDate(date);
		setMileageBAmount(b_amount);
		setMileageEAmount(e_amount);
		setMileageCarMake(carmake);
		setMileageCarModel(carmodel);
		setMileageCarYear(caryear);
	}
	
	public int getMileageBAmount() {
		return mileageBAmount;
	}
	public void setMileageBAmount(int mileageBAmount) {
		this.mileageBAmount = mileageBAmount;
	}
	public int getMileageEAmount() {
		return mileageEAmount;
	}
	public void setMileageEAmount(int mileageEAmount) {
		this.mileageEAmount = mileageEAmount;
	}
	public String getMileageReason() {
		return mileageReason;
	}
	public void setMileageReason(String mileageReason) {
		this.mileageReason = mileageReason;
	}
	public String getMileageDate() {
		return mileageDate;
	}
	public void setMileageDate(String mileageDate) {
		this.mileageDate = mileageDate;
	}

	public String getMileageCarMake() {
		return mileageCarMake;
	}

	public void setMileageCarMake(String mileageCarMake) {
		this.mileageCarMake = mileageCarMake;
	}

	public String getMileageCarModel() {
		return mileageCarModel;
	}

	public void setMileageCarModel(String mileageCarModel) {
		this.mileageCarModel = mileageCarModel;
	}

	public String getMileageCarYear() {
		return mileageCarYear;
	}

	public void setMileageCarYear(String mileageCarYear) {
		this.mileageCarYear = mileageCarYear;
	}
	
}