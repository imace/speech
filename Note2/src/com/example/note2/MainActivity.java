package com.example.note2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.example.note2.db.NotesDB;

public class MainActivity extends ListActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawList;
	private ActionBarDrawerToggle mDrawerToggle;
	private String title;
	private List<DrawerListItem> ListItem = new ArrayList<DrawerListItem>();
	
	private SwipeMenuListView mListView;
	private int itemId;
	



	
	private OnItemClickListener btnMenuList_clickHandler = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			/*
			 * 将fragment插入到frameLayout中
			 */

			switch (position) {
			case 4:
				Intent i = new Intent(MainActivity.this,AtySettings.class);
				startActivity(i);
				break;
			case 3:
				Intent loginIntent = new Intent(MainActivity.this,AtyLogin.class);
				startActivity(loginIntent);
				break;
				
			default:
				break;
			}
			Fragment drawerFragment = new DrawerFragment();
			FragmentManager fm = getFragmentManager();
			fm.beginTransaction().replace(R.id.content_frame, drawerFragment)
					.commit();

			//mDrawerLayout.closeDrawer(mDrawList);

		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new NotesDB(this);
		dbRead = db.getReadableDatabase();
		dbWrite = db.getWritableDatabase();

		adapter = new SimpleCursorAdapter(this, R.layout.notes_list_cell, null,
				new String[] { NotesDB.COLUMN_NAME_NOTE_NAME,
						NotesDB.COLUMN_NAME_NOTE_DATE,NotesDB.COLUMN_NAME_NOTE_CONTENT }, new int[] {
						R.id.tvName, R.id.tvDate,R.id.tvContent });
		setListAdapter(adapter);
		
		
		//获取并给listview注册上下文菜单(长按)
		mListView = (SwipeMenuListView) getListView();
		registerForContextMenu(mListView);

		refreshNotesListView();
		
	
		/*
		 * 滑动侧边栏
		 */
		
		title = (String) getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawList = (ListView) findViewById(R.id.left_drawer);
		
		
		String[] mTitle = getResources().getStringArray(R.array.item_title);
		int[] itemIconRes={
				R.drawable.ic_drawer_home_normal,
				R.drawable.ic_drawer_collect_normal,
				R.drawable.ic_drawer_feedback_normal,
				R.drawable.ic_drawer_count_normal,
				R.drawable.ic_drawer_setting_normal
				
		};
		for (int i=0;i<itemIconRes.length;i++){
			DrawerListItem item = new DrawerListItem(getResources().getDrawable(itemIconRes[i]), mTitle[i]);
			ListItem.add(item);
			
		}
		
		DrawerListAdapter drawerListAdapter = new DrawerListAdapter(this,ListItem);
		mDrawList.setAdapter(drawerListAdapter);
		
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
		
		
		/*
		 * 滑动删除
		 */
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			
			@Override
			public void create(SwipeMenu menu) {
				
				 // create "open" item
		        SwipeMenuItem favorItem = new SwipeMenuItem(
		                getApplicationContext());
		        // set item background
		        favorItem.setBackground(new ColorDrawable(Color.rgb(0xE5, 0xE0,
						0x3F)));
		        // set item width
		        favorItem.setWidth(dp2px(100));
		        favorItem.setIcon(R.drawable.actionbar_favor_icon);
		        // add to menu
		        menu.addMenuItem(favorItem);
		        
		        
			    // create "delete" item
		        SwipeMenuItem deleteItem = new SwipeMenuItem(
		                getApplicationContext());
		        // set item background
		        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
		                0x3F, 0x25)));
		        // set item width
		        deleteItem.setWidth(dp2px(100));
		        // set a icon
		        deleteItem.setIcon(R.drawable.actionbar_delete_icon);
		        // add to menu
		        menu.addMenuItem(deleteItem);
		    }	
				
			
		};
		// set creator
				mListView.setMenuCreator(creator);
		
		
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		    @Override
		    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
		    	Cursor cursor = adapter.getCursor();
		    	itemId = cursor.getInt(cursor.getColumnIndex(NotesDB.COLUMN_NAME_ID));
		        switch (index) {
		        case 0:
		        dbWrite.delete(NotesDB.TABLE_NAME_NOTES, NotesDB.COLUMN_NAME_ID +"=?", new String[]{itemId +""});
		        refreshNotesListView();
		            break;
		        case 1:
		            Toast.makeText(MainActivity.this, "you did it!!", Toast.LENGTH_LONG).show();
		            break;
		        }
		        // false : close the menu; true : not close the menu
		        return false;
		    }
		});
		
		
	}
	/*
	 * 根据侧边栏是否打开变换右上角的功能图标??
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
	
/*	//上下文菜单
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
	}*/
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
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private SimpleCursorAdapter adapter = null;
	private NotesDB db;
	private SQLiteDatabase dbRead,dbWrite;

	public static final int REQUEST_CODE_ADD_NOTE = 1;
	public static final int REQUEST_CODE_EDIT_NOTE = 2;
	
	

	

}

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