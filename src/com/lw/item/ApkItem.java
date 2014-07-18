package com.lw.item;



import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.lw.rardecompress.R;


public class ApkItem extends HasThumbItem{

	public ApkItem(Context content,String path) {
		super(content,path);
	}

	
	@Override
	protected Drawable getdefaultDrawable() {
		return getDrawable(R.drawable.file_icon_default);
	}
	
	@Override
	public void open() {
		  Intent install= new Intent();
		  install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		  install.setAction(Intent.ACTION_VIEW);
		  install.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
		  mContext.startActivity(install);
	}
}
