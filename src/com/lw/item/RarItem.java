package com.lw.item;

import java.io.File;
import java.io.IOException;
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

import com.hostzi.blenderviking.extractarchiveandroid.DecompressCallback;
import com.hostzi.blenderviking.extractarchiveandroid.ExtractFile;
import com.lw.adapter.DialogAdapter;
import com.lw.adapter.DialogAdapter.DialogObject;
import com.lw.adapter.DialogAdapter.DialogType;
import com.lw.decompress.DecompressProgressDialog;
import com.lw.rardecompress.R;

public class RarItem extends CompressItem implements
		DialogInterface.OnClickListener, DecompressCallback {

	private BaseAdapter mAdapter;
	private ExtractFile extractFile;
	private DecompressProgressDialog mDialog;
	private boolean isEnd = true;
	private EditText mEditText;
	public RarItem(Context context, String path) {
		super(context, path);
	}

	@Override
	protected ListAdapter getAdapter() {
		if (mAdapter != null)
			return mAdapter;
		List<DialogObject> data = new ArrayList<DialogObject>();
		System.out.println("add item");
		data.add(new DialogObject(DialogType.decompressCurrent, "解压到当前文件夹",
				R.drawable.decompress));
		data.add(new DialogObject(DialogType.decompressNewFolder, "解压到新文件夹",
				R.drawable.decompress));
		mAdapter = new DialogAdapter(mContext, data);
		System.out.println("new AAdapter");
		return mAdapter;
	} 
 
	@Override
	protected Drawable getdefaultDrawable() {
		// TODO Auto-generated method stub
		return getDrawable(R.drawable.rar);
	}

	@Override
	protected OnClickListener getListener() {
		return this;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			extractFile.cancel();
		} else if (which == DialogInterface.BUTTON_NEGATIVE) {

		} else {
			DialogObject object = (DialogObject) getAdapter().getItem(which);
			switch (object.getType()) {
			case decompressCurrent:
				decompress(new File(getParent().getPath()));
				break;
			case decompressNewFolder:
				mEditText = new EditText(mContext);
				mEditText.setText(name.subSequence(0, name.lastIndexOf(".")));
				new AlertDialog.Builder(mContext).setView(mEditText).setTitle("输入新文件夹名").setPositiveButton("确定", listener).setNegativeButton("取消", listener).show();
				break;
			default:
				break;
			}
		}
	}

	private void decompress(File outFile) {
		try {
			if (mDialog == null) {
				mDialog = new DecompressProgressDialog(mContext);
				mDialog.setListener(this);
				mDialog.setCustomTitle();
				mDialog.initDeafault();
				mDialog.setEntryPerInvisable();
				mDialog.setTitle(new File(path).getName());
				mDialog.setIcon(getdefaultDrawable());
				mDialog.show();
			}
			if (!isEnd) {
				mDialog.show();
				return;
			}
			extractFile = new ExtractFile(mContext, path, outFile.getPath());
			extractFile.setCallback(this);
			extractFile.exec();
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			isEnd = false;
		}
	}
	
	private DialogInterface.OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == DialogInterface.BUTTON_POSITIVE) {
				if(mEditText != null){
					String name = mEditText.getText().toString();
					File parent = new File(getParent().path);
					decompress(new File(parent, name));
				}
			} else if (which == DialogInterface.BUTTON_NEGATIVE) {
			} 
		};
	};

	@Override
	public void onProgress(long current, long total) {
		if (mDialog.isShow()) {
			mDialog.setTotalProgress((int) (current * 100 / total));
		}
	}

	@Override
	public void onEntryProgress(long current, long total) {

	}

	@Override
	public void onEntryChange(String name) {
		mDialog.setFileName(name);
	}
 
	@Override
	public void onDecompressEnd(boolean result) {
		isEnd = true;
		mDialog.dissMiss();
		mDialog = null;
		if(mCallback != null)
			mCallback.onNotifyDataSetChanged();
	}
}
