package com.bbs.model;

public class BBSItem {
	public int itemImage;
	public String itemName;
	
	public BBSItem() { }
	
	public BBSItem(int img, String name) {
		setItemImage(img);
		setItemName(name);
	}
	
	public int getItemImage() {
		return itemImage;
	}
	public void setItemImage(int itemImage) {
		this.itemImage = itemImage;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}