package com.example.note2;

import android.graphics.drawable.Drawable;

import com.example.note2.R.drawable;

public class DrawerListItem {
	private Drawable icon;
	private String title;

	public DrawerListItem() {

	}

	public DrawerListItem(Drawable icon, String title) {
		this.icon = icon;
		this.title = title;
	}
	public Drawable getIcon() {
		return icon;
	}
	public String getTitle() {
		return title;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
