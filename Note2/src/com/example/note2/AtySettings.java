package com.example.note2;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class AtySettings extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.aty_settings);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		super.onCreate(savedInstanceState);
	}
}
