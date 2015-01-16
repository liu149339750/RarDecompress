package com.lw.decompress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.PublicKey;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.compressors.CompressorException;

import com.hostzi.blenderviking.extractarchiveandroid.ExtractFile.FileInfo;
import com.hostzi.blenderviking.extractarchiveandroid.ExtractFile.Type;
import com.lw.fragment.FragmentCallback;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

public class DecompressProxy implements DialogInterface.OnClickListener{

	private Decompress mDecompress;
	private Context mContext;
	private DecompressProgressDialog mDialog;
	private String mTitle;
	private Drawable mIcon;
	private boolean useCustomTitle;
	private FragmentCallback mFragmentCallback;
	
	private boolean mIsEnd = true;
	
	public DecompressProxy(Context context,String source){
		mDecompress = new Decompress(source);
		mContext = context;
	}
	
	public void setFragmentCallback(FragmentCallback callback){
		mFragmentCallback = callback;
	}
	
	public void decompress(File output){
		if(mDialog == null)
			mDialog = new DecompressProgressDialog(mContext);
		if(!mIsEnd) {
			mDialog.show();
			return;
		}
		mIsEnd = false;
		mDialog.setListener(this);
		mDialog.initDeafault();
		if(useCustomTitle)
			mDialog.setCustomTitle();
		if(!TextUtils.isEmpty(mTitle))
			mDialog.setTitle(mTitle);
		if(mIcon != null)
			mDialog.setIcon(mIcon);
		mDialog.show();
		mDialog.setCancelable(false);
		new MyAsycTask().execute(output);
	}
	
	public void setIcon(Drawable drawable){
		mIcon = drawable;
	}
	
	public void setTitle(String title){
		mTitle = title;
	}
	
	public void useCustomTitle(){
		useCustomTitle = true;
	}
	
	 class MyAsycTask extends AsyncTask<File, FileInfo, Boolean> implements DecompressCallback{

		@Override
		protected Boolean doInBackground(File... params) {
			System.out.println(params[0].getName());
			try {
				mDecompress.setCallback(this);
				mDecompress.decompress(params[0]);
				return true;
			} catch (ArchiveException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CompressorException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected void onProgressUpdate(FileInfo... values) {
			FileInfo fi = values[0];
			Type type= fi.type;
			switch (type) {
			case name:
				mDialog.setFileName(fi.name);
				break;
			case currentpercent:
				mDialog.setProgress((int)(fi.current*100/fi.total));
				break;
			case totalpercent:
				mDialog.setTotalProgress((int)(fi.current*100/fi.total));
			default:
				break;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			onDecompressEnd(result);
		}
		
		@Override
		public void onProgress(long current, long total) {
			FileInfo fi = new FileInfo();
			fi.current = current;
			fi.total = total;
			fi.type = Type.totalpercent;
			publishProgress(fi);
		}

		@Override
		public void onEntryProgress(long current, long total) {
			if(total <= 0)
				return;
			FileInfo fi = new FileInfo();
			fi.current = current;
			fi.total = total;
			fi.type = Type.currentpercent;
			System.out.println("current="+current + ",total="+total );
			publishProgress(fi);
		}

		@Override
		public void onEntryChange(String name) {
			FileInfo fi = new FileInfo();
			fi.name = name;
			fi.type = Type.name;
			publishProgress(fi);
		}

		@Override
		public void onDecompressEnd(boolean result) {
			Toast.makeText(mContext, result?"解压成功":"解压结束", Toast.LENGTH_SHORT).show();
			mDialog.dissMiss();
			mDialog = null;
			mIsEnd = true;
			if(mFragmentCallback != null)
				mFragmentCallback.onNotifyDataSetChanged();
		}
		 
	 }

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE: //cancel
			mDecompress.cancel();
			break;
		case DialogInterface.BUTTON_NEGATIVE: //dissmiss
			
			break;
		default:
			break;
		}
	}



}
