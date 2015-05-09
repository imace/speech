package com.example.note2;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class AtyLogin extends Activity {
	private TextView textSignin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_login);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		textSignin = (TextView) findViewById(R.id.btn_signin);
		textSignin.setText(Html.fromHtml("<u>"+"×¢²áiNotes"+"</u>"));//ÏÂ»®Ïß
		textSignin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(AtyLogin.this,AtySignin.class);
				startActivity(i);
			}
		});
		
	}

}
