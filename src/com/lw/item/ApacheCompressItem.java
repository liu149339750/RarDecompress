package com.lw.item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;

import com.lw.adapter.DialogAdapter;
import com.lw.adapter.DialogAdapter.DialogObject;
import com.lw.adapter.DialogAdapter.DialogType;
import com.lw.decompress.DecompressProxy;
import com.lw.other.MediaFile;
import com.lw.other.Util;
import com.lw.rardecompress.R;

public class ApacheCompressItem extends CompressItem implements DialogInterface.OnClickListener{
	private BaseAdapter mAdapter;
	private DecompressProxy mDecompressProxy;
	private EditText mEditText;
	public ApacheCompressItem(Context context, String path) {
		super(context, path);
		mDecompressProxy = new DecompressProxy(mContext, path);
	}
	
	
	@Override
	protected ListAdapter getAdapter() {
		if(mAdapter != null)
			return mAdapter;
		List<DialogObject> data = new ArrayList<DialogObject>();
		data.add(new DialogObject(DialogType.decompressCurrent, "解压到当前文件夹", R.drawable.decompress));
		data.add(new DialogObject(DialogType.decompressNewFolder, "解压到新文件夹", R.drawable.decompress));
		data.add(new DialogObject(DialogType.delete, "删除", R.drawable.decompress));
		mAdapter = new DialogAdapter(mContext, data);
		return mAdapter;
	}
	
	@Override
	protected OnClickListener getListener() {
		return this;
	}
	
	@Override
	protected Drawable getdefaultDrawable() {
		switch (getMimeTypeCode()) {
		case MediaFile.FILE_TYPE_BZIP2:
			return getDrawable(R.drawable.bz2);
		case MediaFile.FILE_TYPE_TAR:
			return getDrawable(R.drawable.tar);
		case MediaFile.FILE_TYPE_JAR:
			return getDrawable(R.drawable.jar);
		case MediaFile.FILE_TYPE_CPIO:
			return getDrawable(R.drawable.cpio);
		case MediaFile.FILE_TYPE_ARJ:
			return getDrawable(R.drawable.arj);
		case MediaFile.FILE_TYPE_XZ:
			return getDrawable(R.drawable.xz);
		case MediaFile.FILE_TYPE_Z:
			return getDrawable(R.drawable.z);
		case MediaFile.FILE_TYPE_7Z:
			return getDrawable(R.drawable.file_7z);
		default:
			break;
		}
		return super.getdefaultDrawable();
	}


	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(which == DialogInterface.BUTTON_POSITIVE){
			if(mEditText != null){
//				String name = mEditText.getText().toString();
//				File parent = new File(getParent().path);
//				decompress(new File(parent, name));
				String path = mEditText.getText().toString();
				decompress(new File(path));
			}
		}else if(which == DialogInterface.BUTTON_NEGATIVE){
			
		}else{
		DialogObject object = (DialogObject) getAdapter().getItem(which);
		switch (object.getType()) {
		case decompressCurrent:
			decompress(new File(getParent().path + "/" + getNameWithoutMimeType()));
			break;
		case decompressNewFolder:
			mEditText = new EditText(mContext);
//			mEditText.setText(name.subSequence(0, name.lastIndexOf(".")));
			mEditText.setText(Util.getStringWithoutMimeType(path));
			new AlertDialog.Builder(mContext).setView(mEditText).setTitle("输入新文件夹名").setPositiveButton("确定", this).setNegativeButton("取消", this).show();
			break;
		case delete:
			new AlertDialog.Builder(mContext).setTitle(getName()).setMessage("你确定要删除吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					delete();
					if(mCallback != null)
						mCallback.onNotifyDataSetChanged();
				}
			}).setNegativeButton("取消", null).show();
			break;
		default:
			break;
		}
		}
	}


	private void decompress(File outFile) {
		mDecompressProxy.setFragmentCallback(mCallback);
		mDecompressProxy.useCustomTitle();
		mDecompressProxy.setIcon(getdefaultDrawable());
		mDecompressProxy.setTitle(getName());
		mDecompressProxy.decompress(outFile);
	}
}
