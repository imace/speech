package com.example.note2;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class AtySignin extends Activity  {
	private ImageView imageView;
	private Button button;
	private OnClickListener btn_onclick_handler  = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>");
		}
	};
	private OnClickListener btn_image_handler = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_signin);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		imageView = (ImageView) findViewById(R.id.ic_signin_back);
		button = (Button) findViewById(R.id.aty_login_btnlogin);
		button.setOnClickListener(btn_onclick_handler  );
		imageView.setOnClickListener(btn_image_handler);


	}
	

}
