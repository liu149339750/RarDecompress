package com.lw.item;



import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.rardecompress.R;

public class VideoItem extends HasThumbItem{

	public VideoItem(Context context,String path) {
		super(context,path);
	}
	
	@Override
	protected Drawable getdefaultDrawable() {
		// TODO Auto-generated method stub
		return getDrawable(R.drawable.file_icon_video);
	}
}
