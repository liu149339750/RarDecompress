package com.lw.adapter;

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lw.fragment.FileListFragment;
import com.lw.fragment.FileListFragment.TreeInfo;
import com.lw.item.BaseItem;

public class TitleAdapter extends BaseAdapter{
	
	private List<TreeInfo> mData;
	private LayoutInflater mInflater;
	
	public TitleAdapter(Context context){
		mData = new ArrayList<TreeInfo>();
		mInflater = LayoutInflater.from(context);
		reset(context);
	}

	private void reset(Context context) {
		TreeInfo tInfo = new TreeInfo();
//		tInfo.item = new BaseItem(context, "/");
//		tInfo.tip = "/¸ùÄ¿Â¼";
//		mData.add(tInfo);
		TreeInfo sd = new TreeInfo();
		sd.item = new BaseItem(context, "/mnt/sdcard/");
		sd.tip = "SD¿¨";
		mData.add(sd);
	}
	
	public String add(BaseItem item){
		TreeInfo info = new TreeInfo();
		info.item = item;
		info.tip = mData.get(mData.size() - 1).tip + "/" + item.getName();
		info.last = mData.get(mData.size() - 1);
		mData.add(info);
		return mData.get(mData.size() - 1).tip + "/" + item.getName();
	}
	
	public void removeLast(){
		mData.remove(mData.size() - 1);
	}
	
	public String change(TreeInfo info){
		int size = mData.size();
		for(int i = size - 1;i >= 0;i--){
			TreeInfo ti = mData.get(i);
			if(ti == info)
				break;
			else
				mData.remove(ti);
		}
		return info.tip;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public TreeInfo getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
		}
		TextView text = (TextView) convertView.findViewById(android.R.id.text1);
		text.setText(mData.get(position).tip);
		text.setSingleLine();
		text.setEllipsize(TruncateAt.START);
		return convertView;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.getDropDownView(position, convertView, parent);
	}

}
