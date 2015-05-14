package com.example.note2;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

public class AtySearch extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_search);
		
		handleIntent(getIntent());
		
	}
	
	private void handleIntent(Intent intent){
		if(Intent.ACTION_SEARCH.equals(intent.getAction())){
			String query = intent.getStringExtra(SearchManager.QUERY);
			//use the query to search your data somehow
		}
	}

}
