package com.lw.item;

import java.io.IOException;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.lw.rardecompress.R;

import edu.mit.mobile.android.imagecache.ImageCache;
import edu.mit.mobile.android.imagecache.ImageCache.OnImageLoadListener;

public class HasThumbItem extends FileItem implements OnImageLoadListener{

//	private static Executor mExecutor;
	private ImageCache mCache;
	private static final int WIDTH = 64;
	private static final int HEIGHT = 64;
	private int id = -1;
	private ImageView mImageView;

	public HasThumbItem(Context content, String path) {
		super(content, path);
//		if(mExecutor == null)
//			mExecutor = Executors.newCachedThreadPool();
		mCache = ImageCache.getInstance(mContext);
		
	}
	
	public void setDrawableIcon(ImageView image){
		mImageView = image;
		Drawable drawable = null;
		try {
			if(id == -1)
				id = mCache.getNewID();
			mCache.registerOnImageLoadListener(id,this);
			drawable = mCache.loadImage(id, Uri.parse("file://"+path), mimeTypeCode, WIDTH, HEIGHT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(drawable == null){
			drawable = getdefaultDrawable();
		}else
			mCache.unregisterOnImageLoadListener(id);
		mImageView.setImageDrawable(drawable);
	}
	
	protected Drawable getdefaultDrawable(){
		return mContext.getResources().getDrawable(R.drawable.file_icon_default);
	}

	@Override
	public void onImageLoaded(int id, Uri imageUri, Drawable image) {
		if(this.id == id && mImageView != null)
			mImageView.setImageDrawable(image);
		mCache.unregisterOnImageLoadListener(id);
	}

}
