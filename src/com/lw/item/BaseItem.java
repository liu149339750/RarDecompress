package com.lw.item;

import java.io.File;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.lw.fragment.FragmentCallback;
import com.lw.rardecompress.R;

public  class BaseItem {
	
//	public static final String TYPE_DIRECTORY = "directory";
//	public static final String TYPE_FILE = "file";
//	public static final String TYPE_ZIP = "zip";
//	public static final String TYPE_RAR = "rar";
//	public static final String TYPE_IMAGE = "image";
//	public static final String TYPE_VIDEO = "video";
//	public static final String TYPE_AUDIO = "audio";
//	public static final String TYPE_APK = "apk";
	
	public String name;
	
	protected Context mContext;
	
	private BaseItem parent;
	
//	protected String mType;
	
	private Resources mResources;
	
	protected String path;
	protected FragmentCallback mCallback;
	
	
	public BaseItem(Context context,String path){
		mContext = context;
		this.path = path;
		mResources = mContext.getResources();
		name = path.substring(path.lastIndexOf("/")+1, path.length());
	}
	
	protected Drawable getdefaultDrawable(){
		return mContext.getResources().getDrawable(R.drawable.file_icon_default);
	}
	
	public void setDrawableIcon(ImageView image){
		image.setImageDrawable(getdefaultDrawable());
	}
	
	public void setParent(BaseItem item){
		parent = item;
	}
	
	public BaseItem getParent(){
		return parent;
	}
	
	public boolean hasThumb(){
		return false;
	}
	
	protected Drawable getDrawable(int id){
		return mResources.getDrawable(id);
	}
	
	public void setPath(String path){
		this.path = path;
		name = path.substring(path.lastIndexOf("/")+1, path.length());
	}
	
	public long getSize(){
		return 0;
	}
	
	public String getPath(){
		return path;
	}
	
	public void setMimeTypeCode(int code){
	}
	
	public int getMimeTypeCode(){
		return 0;
	}
	
	public String getName(){
		return name;
		
	}
	
	public long getLastModifiedTime(){
		File file = new File(path);
		return file.lastModified();
	}
	
	public boolean isFile(){
		return false;
	}
	
	public boolean delete() {
		return false;
	}
	
	public boolean reName(String newName) {
		return false;
	}
	
	public void setCallback(FragmentCallback callback){
		mCallback = callback;
	}
	
}
