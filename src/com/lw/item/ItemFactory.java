package com.lw.item;

import java.io.File;

import android.content.Context;

import com.example.rardecompress.MediaFile;
import com.example.rardecompress.MediaFile.MediaFileType;

public class ItemFactory {

	
	public static BaseItem createItem(Context context,File file){
		BaseItem item = null;
		String path = file.getPath();
		if(file.isDirectory())
			item = new DirectoryItem(context, path);
		else {
			MediaFileType fileType = MediaFile.getFileType(path);
			if(fileType == null)
				return new FileItem(context, path);
			System.out.println(fileType.mimeType+"<<"+fileType.fileType+">>"+path);
			if(MediaFile.isAudioFileType(fileType.fileType))
				item = new AudioItem(context, path);
			else if(MediaFile.isImageFileType(fileType.fileType))
				item = new ImageItem(context, path);
			else if(MediaFile.isVideoFileType(fileType.fileType))
				item = new VideoItem(context, path);
			else if(fileType.fileType == MediaFile.FILE_TYPE_APK)
				item = new ApkItem(context,path);
			else 
				item = new FileItem(context, path);
			item.setMimeTypeCode(fileType.fileType);
		}
		return item;
	}
}
