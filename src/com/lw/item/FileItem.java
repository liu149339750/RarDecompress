package com.lw.item;

import java.io.File;

import com.lw.activity.MediaFile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.Formatter;


public class FileItem extends BaseItem{
	
	public long size;
	
	public long date_modified;
	
	protected String mimeType;
	
	protected int mimeTypeCode;
	
	public FileItem(Context context,String path) {
		super(context, path);
		size = new File(path).length();
	}
	
	public String getFileSize(){
		return Formatter.formatFileSize(mContext, size);
	}
	
	public long getSize(){
		return size;
	}
	
	public String getMimeType(){
		if(TextUtils.isEmpty(mimeType)){
			mimeType = MediaFile.getMimeTypeForFile(path);
		}
		return mimeType;
	}
	
	@Override
	public void setMimeTypeCode(int code) {
		this.mimeTypeCode = code;
	}
	
	@Override
	public int getMimeTypeCode() {
		if(mimeTypeCode == 0)
			mimeTypeCode = MediaFile.getFileTypeForMimeType(mimeType);
		return mimeTypeCode;
	}
	
	@Override
	public boolean isFile() {
		return true;
	}
	
	public void open(){
		try{
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://"+path), getMimeType());
			mContext.startActivity(intent);
		}catch(Exception e){
			
		}
	}

	@Override
	public boolean delete() {
		System.out.println("delet path = " + path);
		return new File(path).delete();
	}

	@Override
	public boolean reName(String newName) {
		File file = new File(path);
		File nf = new File(file.getParentFile(), newName);
		if(nf.exists())
			return false;
		return file.renameTo(nf);
	}
}
