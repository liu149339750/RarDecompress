package com.lw.item;



import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.rardecompress.R;

public class ApkItem extends HasThumbItem{

	public ApkItem(Context content,String path) {
		super(content,path);
	}

	
	@Override
	protected Drawable getdefaultDrawable() {
		return getDrawable(R.drawable.file_icon_default);
	}
}
