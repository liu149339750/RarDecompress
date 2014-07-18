package com.lw.item;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.BaseAdapter;

import com.lw.rardecompress.R;

public class ZipItem extends ApacheCompressItem {

	private BaseAdapter mAdapter;
	public ZipItem(Context context, String path) {
		super(context, path);
	}
	
	
	
	@Override
	protected Drawable getdefaultDrawable() {
		return getDrawable(R.drawable.zip);
	}




}
