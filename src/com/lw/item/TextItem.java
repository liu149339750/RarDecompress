package com.lw.item;

import com.lw.rardecompress.R;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class TextItem extends FileItem{

	public TextItem(Context context, String path) {
		super(context, path);
	}
	
	@Override
	protected Drawable getdefaultDrawable() {
		return getDrawable(R.drawable.file_icon_txt);
	}

}
