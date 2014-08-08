package com.lw.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lw.rardecompress.R;


public class DialogAdapter extends BaseAdapter{
	private List<DialogObject> mData;
	private LayoutInflater mInflater;
	public DialogAdapter(Context context,List<DialogObject> data){
		mInflater = LayoutInflater.from(context);
		mData = data;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public DialogObject getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.dialog_item, null);
		}
		DialogObject object = getItem(position);
		TextView text = (TextView) convertView.findViewById(R.id.title);
		ImageView image = (ImageView) convertView.findViewById(R.id.icon);
		text.setText(object.title + "");
		image.setImageResource(object.drawable);
		return convertView;
	}

	public static class DialogObject{
		public int drawable;
		public String title;
		private DialogType mType;
		
		public DialogObject(DialogType type){
			mType = type;
		}
		
		public DialogObject(DialogType type,String title, int drawable){
			mType = type;
			this.drawable = drawable;
			this.title = title;
		}
		
		public DialogType getType(){
			return mType;
		}
	}
	
	public enum DialogType{
		open,decompress,decompressCurrent,decompressNewFolder,delete
	}
	
}
