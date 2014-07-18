package com.lw.item;

import java.io.File;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.widget.ListAdapter;

import com.lw.decompress.DecompressProxy;
import com.lw.rardecompress.R;

public class CompressItem extends FileItem{
	protected DecompressProxy mDecompress;
	public CompressItem(Context context, String path) {
		super(context, path);
		mDecompress = new DecompressProxy(context,path);
		mDecompress.useCustomTitle();
		mDecompress.setTitle(new File(path).getName());
	}

	@Override
	protected Drawable getdefaultDrawable() {
		return getDrawable(R.drawable.gzip);
	}
	
	@Override
	public void open() {
		ListAdapter adpater = getAdapter();
		if(adpater == null){
			super.open();
			return;
		}
		Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(getName());
		builder.setAdapter(adpater, getListener());
		builder.show();
	}

	protected OnClickListener getListener() {
		return null;
	}

	protected ListAdapter getAdapter() {
		// TODO Auto-generated method stub
		return null;
	}
}
