package com.example.note2;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note2.db.NotesDB;

public class MainActivity extends ListActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawList;
	private ActionBarDrawerToggle mDrawerToggle;
	private String title;
	private List<DrawerListItem> ListItem = new ArrayList<DrawerListItem>();
	
	private ListView mListView;
	
	private SearchView searchView;

	


	
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
		//获取并给listview注册上下文菜单
		mListView = getListView();
		registerForContextMenu(mListView);
		
		
		/*
         * 设置多选模式。 
         
		mListView = getListView();
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		mListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// Here you can perform updates to the CAB due to
		        // an invalidate() request
		        return false;
			}
			
			@Override
			public void onDestroyActionMode(ActionMode mode) {
				   // Here you can make any necessary updates to the activity when
		        // the CAB is removed. By default, selected items are deselected/unchecked.
				
			}
			
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				  // Inflate the menu for the CAB
		        MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.context, menu);
		        
		        *//** 
	             * 设置ActionMode的标题。 
	             *//*  
	            mode.setTitle("Select Items");
	            setActionModeTitle(mode);
		        return true;
			}
			
			

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				 // Respond to clicks on the actions in the CAB
		        switch (item.getItemId()) {
		            case R.id.menu_delete:
		                deleteSelectedItems();
		                mode.finish(); // Action picked, so close the CAB
		                return true;
		            case R.id.menu_cancel: // 全选
						if (mIsSelectAll) {
							item.setTitle("取消全选");
							mIsSelectAll = false;

						} else {
							item.setTitle("全选");
							mIsSelectAll = true;
						}
						for (int i = 0; i < mListView.getCount(); i++) {
							mListView.setItemChecked(i,
									!mIsSelectAll);
						}
						return true;
					
		            default:
		                return false;
			}
			}
			
			
			private void deleteSelectedItems() {
				Toast.makeText(MainActivity.this,"cool!!!", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position,
					long id, boolean checked) {
				 // Here you can do something when items are selected/de-selected,
		        // such as update the title in the CAB
				setActionModeTitle(mode);
				
			}
			private void setActionModeTitle(ActionMode mode) {
				final int checkedCount = getListView().getCheckedItemCount();
				
				switch (checkedCount) {
				case 0:
					mode.setSubtitle(null);
					break;
				case 1: 
                    mode.setSubtitle("1 item selected");  
                    break;  
                default:  
                    mode.setSubtitle(checkedCount + " items selected");  
                    break;
				}
			}
		});*/

		refreshNotesListView();
	
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
				getActionBar().setTitle("滴滴备忘");
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
		getActionBar().setDisplayShowHomeEnabled(false);//
		
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
		
		switch (item.getItemId()) {
		case R.id.action_add:
			startActivityForResult(new Intent(MainActivity.this,
					AtyEditNote.class), REQUEST_CODE_ADD_NOTE);
			break;
			
		default:
			break;
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
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		//加载xml中的上下文菜单
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		 AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		// 相应上下文的操作
		  switch (item.getItemId()) {  
          case R.id.edit:  
              Toast.makeText(MainActivity.this, "Edit", Toast.LENGTH_LONG).show();  
              break;  
          case R.id.share:  
              Toast.makeText(MainActivity.this, "share", Toast.LENGTH_LONG).show();  
              break;  
          case R.id.delete:  
              Toast.makeText(MainActivity.this, "delete", Toast.LENGTH_LONG).show();  
              break;  
          default:  
              break;  
      } 
		return super.onContextItemSelected(item);
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