package com.lw.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lw.item.BaseItem;
import com.lw.item.FileItem;
import com.lw.rardecompress.R;

public class ListItem extends LinearLayout{

	private TextView mNameTextView;
	private ImageView mIconImageView;
	private TextView mSizeTextView;
	private TextView mTimeTextView;
	
	public ListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mNameTextView = (TextView) findViewById(R.id.name);
		mIconImageView = (ImageView) findViewById(R.id.icon);
		mSizeTextView = (TextView) findViewById(R.id.size);
		mTimeTextView = (TextView) findViewById(R.id.time);
	}

	public void bind(BaseItem item){
		item.setDrawableIcon(mIconImageView);
		mNameTextView.setText(item.getName());
		mTimeTextView.setText(formatTime(item.getLastModifiedTime()));
		if(item.isFile())
			mSizeTextView.setText(((FileItem)item).getFileSize());
		else
			mSizeTextView.setText("");
	}

	private CharSequence formatTime(long lastModifiedTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new Date(lastModifiedTime));
	}
	
}
