package com.lw.item;




import java.io.File;
import java.io.FileFilter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.lw.rardecompress.R;

public class DirectoryItem extends BaseItem{

	public DirectoryItem(Context content, String path) {
		super(content, path);
	}

	
	@Override
	protected Drawable getdefaultDrawable() {
		// TODO Auto-generated method stub
		return getDrawable(R.drawable.folder);
	}
	
	@Override
	public boolean delete() {

		deleteDirectory(path);
		return true;
	}
	
	private boolean deleteDirectory(String path) {
		File file = new File(path);
		file.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File f) {
				if(f.isDirectory())
					return deleteDirectory(f.getPath());
				return !f.delete();
			}
		});
		return file.delete();
	}


	@Override
	public boolean reName(String newName) {
		File file = new File(path);
		File nf = new File(file.getParentFile(), newName);
		if(nf.exists())
			return false;
		boolean c = file.renameTo(nf);
		if(c){
			name = nf.getName();
			path = nf.getPath();
		}
		return c;
	}
}
