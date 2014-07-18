package com.lw.item;


import android.content.Context;
import android.graphics.drawable.Drawable;

import com.lw.activity.MediaFile;
import com.lw.rardecompress.R;

public class AudioItem extends FileItem{

	public AudioItem(Context content,String path) {
		super(content,path);
	}
	
	@Override
	protected Drawable getdefaultDrawable() {
		int res = R.drawable.file_icon_mp3;
		int type = getMimeTypeCode();
		if(type == MediaFile.FILE_TYPE_WAV)
			res = R.drawable.file_icon_wav;
		else if(type == MediaFile.FILE_TYPE_WMA)
			res = R.drawable.file_icon_wma;
		else if(type == MediaFile.FILE_TYPE_MID)
			res = R.drawable.file_icon_mid;
		return getDrawable(res);
	}

}
