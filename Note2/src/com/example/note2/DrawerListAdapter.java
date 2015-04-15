package com.example.note2;

import java.util.List;

import com.example.note2.DrawerListItem;
import com.example.note2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerListAdapter extends BaseAdapter {
	private List<DrawerListItem> mItems;
	private LayoutInflater mInflater;

	public DrawerListAdapter(Context context, List<DrawerListItem> data) {
		this.mInflater = LayoutInflater.from(context);
		this.mItems = data;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return mItems.size();
	}

	@Override
	public DrawerListItem getItem(int position) {
		// TODO Auto-generated method stub
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DrawerListItem item = getItem(position);
		TextView itemTitle = null;
		ImageView itemIcon = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}
		itemTitle = (TextView) convertView.findViewById(R.id.item_title); 
		itemIcon = (ImageView) convertView.findViewById(R.id.item_icon);
		itemTitle.setText(item.getTitle());
		itemIcon.setBackground(item.getIcon());
		return convertView;
	}

}
