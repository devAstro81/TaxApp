package com.bbs.model;

public class BBSReceipt {
	
	public String receiptAmount;
	public String receiptImage;
	public String receiptName;
	public String receiptCategory;
	public String receiptDate;
	
	public BBSReceipt() {
		
	}
	
	public BBSReceipt(String name, String category, String date, String amount, String image) {
		setReceiptName(name);
		setReceiptDate(date);
		setReceiptCategory(category);
		setReceiptImage(image);
		setReceiptAmount(amount);
	}
	
	public String getReceiptAmount() {
		return receiptAmount;
	}
	public void setReceiptAmount(String receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	public String getReceiptImage() {
		return receiptImage;
	}
	public void setReceiptImage(String receiptImage) {
		this.receiptImage = receiptImage;
	}
	public String getReceiptName() {
		return receiptName;
	}
	public void setReceiptName(String receiptName) {
		this.receiptName = receiptName;
	}
	public String getReceiptCategory() {
		return receiptCategory;
	}
	public void setReceiptCategory(String receiptCategory) {
		this.receiptCategory = receiptCategory;
	}
	public String getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}
}