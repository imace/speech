package com.example.note2;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.note2.db.NotesDB;

public class MainActivity extends ListActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawList;
	private ActionBarDrawerToggle mDrawerToggle;
	private String title;
	private List<DrawerListItem> ListItem = new ArrayList<DrawerListItem>();

	private OnClickListener btnAddNote_clickHandler = new OnClickListener() {

		@Override
		public void onClick(View v) {
			startActivityForResult(new Intent(MainActivity.this,
					AtyEditNote.class), REQUEST_CODE_ADD_NOTE);
		}
	};
	private OnItemClickListener btnMenuList_clickHandler = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			/*
			 * 将fragment插入到frameLayout中
			 */

			Fragment contentFragment = new ContentFragment();
			Bundle args = new Bundle();
			//args.putString("text", ListItem.get(position));
			args.putString("text", "aaaaaaaaaaaaaa");
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

		adapter = new SimpleCursorAdapter(this, R.layout.notes_list_cell, null,
				new String[] { NotesDB.COLUMN_NAME_NOTE_NAME,
						NotesDB.COLUMN_NAME_NOTE_DATE }, new int[] {
						R.id.tvName, R.id.tvDate });
		setListAdapter(adapter);

		refreshNotesListView();

		findViewById(R.id.btnAddNote).setOnClickListener(
				btnAddNote_clickHandler);

		/*
		 * 
		 */
		
		title = (String) getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawList = (ListView) findViewById(R.id.left_drawer);
		
		String[] mTitle = getResources().getStringArray(R.array.item_title);
		int[] itemIconRes={
				R.drawable.ic_drawer_home_normal,
				R.drawable.ic_drawer_explore_normal,
				R.drawable.ic_drawer_follow_normal,
				R.drawable.ic_drawer_collect_normal,
				R.drawable.ic_drawer_draft_normal,
				R.drawable.ic_drawer_search_normal,
				R.drawable.ic_drawer_question_normal,
				R.drawable.ic_drawer_setting_normal
				
		};
		for (int i=0;i<itemIconRes.length;i++){
			DrawerListItem item = new DrawerListItem(getResources().getDrawable(itemIconRes[i]), mTitle[i]);
			ListItem.add(item);
			
		}
		
		DrawerListAdapter adapter = new DrawerListAdapter(this,ListItem);
		mDrawList.setAdapter(adapter);
		
		

		/*menulist = new ArrayList<String>();
		menulist.add("Login");
		menulist.add("Upload");
		menulist.add("Dowload");
		menulist.add("Share");
		menulist.add("Feedback");

		menuAdapter = new ArrayAdapter<>(this,
				android.R.layout.simple_list_item_1, menulist);
		mDrawList.setAdapter(menuAdapter);*/
		mDrawList.setOnItemClickListener(btnMenuList_clickHandler);
/*
 * 监听侧边栏是否打开，并设置标题,图标
 */
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				getActionBar().setTitle("智能备忘录");
				invalidateOptionsMenu();  //call onPrepareOptionMenu(),并重写方法
				
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
				getActionBar().setTitle("首页");
				invalidateOptionsMenu();
				super.onDrawerClosed(drawerView);
				
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		//开启ActionBar上的APP ICON的功能
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);

	}

	/*
	 * 根据侧边栏是否打开变换右上角的功能图标
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		boolean isDrawerOpen = mDrawerLayout.isDrawerOpen(mDrawList);
		menu.findItem(R.id.action_search).setVisible(!isDrawerOpen);

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 将actionBar上的图标与drawer结合起来
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// 需要将actionDrawerToggle与drawerLayout同步
		// 将actionBarDrawerToggle的drawer图标，设置为actionBar中的Home-Button
		mDrawerToggle.syncState();

		super.onPostCreate(savedInstanceState);
	}

	// 屏幕旋转时的处理
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		mDrawerToggle.onConfigurationChanged(newConfig);
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Cursor c = adapter.getCursor();
		c.moveToPosition(position);

		Intent i = new Intent(MainActivity.this, AtyEditNote.class);
		i.putExtra(AtyEditNote.EXTRA_NOTE_ID,
				c.getInt(c.getColumnIndex(NotesDB.COLUMN_NAME_ID)));
		i.putExtra(AtyEditNote.EXTRA_NOTE_NAME,
				c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_NAME)));
		i.putExtra(AtyEditNote.EXTRA_NOTE_CONTENT,
				c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_CONTENT)));
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

	public void refreshNotesListView() {

		adapter.changeCursor(dbRead.query(NotesDB.TABLE_NAME_NOTES, null, null,
				null, null, null, null));

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
