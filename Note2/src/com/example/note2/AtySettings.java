package com.example.note2;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class AtySettings extends Activity{
	private ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.aty_settings);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		super.onCreate(savedInstanceState);
		
		imageView = (ImageView) findViewById(R.id.ic_signin_back);
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
	}
}
