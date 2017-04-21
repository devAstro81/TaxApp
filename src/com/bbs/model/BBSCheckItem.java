package com.bbs.model;

public class BBSCheckItem {
	public int itemIndex;
	public String itemName;
	public int itemValue;
	
	public BBSCheckItem() { }
	
	public BBSCheckItem(int index, String name, int value) {
		setItemIndex(index);
		setItemName(name);
		setItemValue(value);
	}

	public int getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getItemValue() {
		return itemValue;
	}

	public void setItemValue(int itemValue) {
		this.itemValue = itemValue;
	}
	
}