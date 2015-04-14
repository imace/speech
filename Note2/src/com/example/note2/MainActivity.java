package com.example.note2;


import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.note2.db.NotesDB;
import com.example.note2.ContentFragment;
import com.example.note2.R;

public class MainActivity extends ListActivity {
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawList;
	private ArrayList<String> menulist;
	private ArrayAdapter<String> menuAdapter;

	
	

	private OnClickListener btnAddNote_clickHandler=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startActivityForResult(new Intent(MainActivity.this, AtyEditNote.class), REQUEST_CODE_ADD_NOTE);
		}
	};
	private OnItemClickListener btnMenuList_clickHandler = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			/*
			 * ¶¯Ì¬²åÈëÒ»¸öfragementµ½frameLayoutÖÐ
			 */
			
			Fragment contentFragment = new ContentFragment();
			Bundle args = new Bundle();
			args.putString("text", menulist.get(position));
			contentFragment.setArguments(args);

			FragmentManager fm = getFragmentManager();
			fm.beginTransaction().replace(R.id.content_frame, contentFragment)
					.commit();

			mDrawerLayout.closeDrawer(mDrawList);
			
		}
	};
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		db = new NotesDB(this);
		dbRead = db.getReadableDatabase();
		
		adapter = new SimpleCursorAdapter(this, R.layout.notes_list_cell, null, new String[]{NotesDB.COLUMN_NAME_NOTE_NAME,NotesDB.COLUMN_NAME_NOTE_DATE}, new int[]{R.id.tvName,R.id.tvDate});
		setListAdapter(adapter);
		
		refreshNotesListView();
		
		findViewById(R.id.btnAddNote).setOnClickListener(btnAddNote_clickHandler);
		
/*
 * ²à±ßÀ¸²¿·Ö		
 */
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawList = (ListView) findViewById(R.id.left_drawer);
		
		menulist = new ArrayList<String>();
		menulist.add("登陆");
		menulist.add("上传");
		menulist.add("下载");
		menulist.add("共享");
		menulist.add("反馈");
		
		menuAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,menulist);
		mDrawList.setAdapter(menuAdapter);
		mDrawList.setOnItemClickListener(btnMenuList_clickHandler );
		
		
		
		
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Cursor c = adapter.getCursor();
		c.moveToPosition(position);
		
		Intent i = new Intent(MainActivity.this,AtyEditNote.class);
		i.putExtra(AtyEditNote.EXTRA_NOTE_ID, c.getInt(c.getColumnIndex(NotesDB.COLUMN_NAME_ID)));
		i.putExtra(AtyEditNote.EXTRA_NOTE_NAME, c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_NAME)));
		i.putExtra(AtyEditNote.EXTRA_NOTE_CONTENT, c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_CONTENT)));
		startActivityForResult(i, REQUEST_CODE_EDIT_NOTE);
		
		super.onListItemClick(l, v, position, id);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
		case REQUEST_CODE_ADD_NOTE:
		case REQUEST_CODE_EDIT_NOTE:
			if (resultCode == Activity.RESULT_OK) {
				refreshNotesListView();
			}
			break;

		default:
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void refreshNotesListView(){
		
		adapter.changeCursor(dbRead.query(NotesDB.TABLE_NAME_NOTES, null, null, null, null, null, null));
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	private SimpleCursorAdapter adapter = null;
	private NotesDB db;
	private SQLiteDatabase dbRead;
	
	public static final int REQUEST_CODE_ADD_NOTE = 1;
	public static final int REQUEST_CODE_EDIT_NOTE = 2;
	

}
