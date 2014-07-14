package com.lw.adapter;

import java.util.List;

import com.example.rardecompress.R;
import com.lw.item.BaseItem;
import com.lw.ui.ListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FileAdapter extends BaseAdapter{
	private List<BaseItem>mItems;
	private LayoutInflater mInflater;
	
	public FileAdapter(Context context,List<BaseItem> items){
		mInflater = LayoutInflater.from(context);
		mItems = items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItems.size();
	}

	@Override
	public BaseItem getItem(int position) {
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
		if(convertView == null)
			convertView = mInflater.inflate(R.layout.list_item, null);
		ListItem itemView = (ListItem) convertView;
		itemView.bind(mItems.get(position));
		return convertView;
	}

	public void changeItems(List<BaseItem> listItems) {
		mItems.clear();
		mItems.addAll(listItems);
		notifyDataSetChanged();
	}
	
	public void remove(BaseItem item){
		mItems.remove(item);
		notifyDataSetChanged();
	}

}
