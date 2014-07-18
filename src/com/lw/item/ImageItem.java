package com.lw.item;



import android.content.Context;
import android.graphics.drawable.Drawable;

import com.lw.rardecompress.R;


public class ImageItem extends HasThumbItem{

	public ImageItem(Context content,String path) {
		super(content,path);
	}

	@Override
	protected Drawable getdefaultDrawable() {
		return getDrawable(R.drawable.file_icon_picture);
	}
}
