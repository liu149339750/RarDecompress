package com.lw.decompress;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lw.rardecompress.R;
import com.lw.ui.NumberProgressBar;

public class DecompressProgressDialog {
	private AlertDialog.Builder mDialog;
	private AlertDialog mAlertDialog;
	private Context mContext;
	private NumberProgressBar mProgressBar;
	private TextView mCurrentFileName;
	private TextView mCurrentPercent;;
	
	private ImageView mTitleIcon;
	private TextView mTitle;
	private DialogInterface.OnClickListener mClickListener;
	
	public DecompressProgressDialog(Context context) {
		mContext = context;
		mDialog = new AlertDialog.Builder(context);
	}
	
	public void setListener(DialogInterface.OnClickListener listener){
		mClickListener = listener;
	}

	public void initDeafault(){
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_progress, null);
		mProgressBar = (NumberProgressBar) view.findViewById(R.id.progress);
		mProgressBar.setMax(100);
		mCurrentFileName = (TextView) view.findViewById(R.id.name);
		mCurrentPercent = (TextView) view.findViewById(R.id.percent);
		mDialog.setView(view);
		mDialog.setPositiveButton("ШЁЯћ", mClickListener);
		mDialog.setNegativeButton("вўВи", mClickListener);
	}
	
	public void setEntryPerInvisable(){
		mCurrentPercent.setVisibility(View.INVISIBLE);
	}
	
	public void setCancelable(boolean cancel){
		if(mAlertDialog != null)
			mAlertDialog.setCancelable(cancel);
	}
	
	public void setProgress(int p){
		mCurrentPercent.setText(p + "%");
	}
	
	public void setTotalProgress(int p){
		mProgressBar.setProgress(p);
	}
	
	public void setCustomTitle(){
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_custom_title, null);
		mDialog.setCustomTitle(view);
		mTitleIcon = (ImageView) view.findViewById(R.id.icon);
		mTitle = (TextView) view.findViewById(R.id.title);
	}
	
	public void setFileName(String name){
		mCurrentFileName.setText(name);
	}
	
	public void show(){
		if(mAlertDialog == null)
			mAlertDialog = mDialog.show();
		else
			mAlertDialog.show();
	}
	
	public void setTitle(String title){
		if(mTitle != null)
			mTitle.setText(title);
		else
			mDialog.setTitle(title);
	}
	
	public void setIcon(Drawable drawable){
		if(mTitleIcon != null)
			mTitleIcon.setImageDrawable(drawable);
		else
			mDialog.setIcon(drawable);
	}
	
	public void dissMiss(){
		if(mAlertDialog.isShowing())
			mAlertDialog.dismiss();
	}
	
	public void cancel(){
		mAlertDialog.cancel();
	}
	
	public boolean isShow(){
		return mAlertDialog.isShowing();
	}

}
