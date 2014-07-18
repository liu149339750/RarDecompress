package com.lw.item;

import java.io.File;

import android.content.Context;

import com.lw.activity.MediaFile;
import com.lw.activity.MediaFile.MediaFileType;
import com.lw.fragment.FileListFragment;
import com.lw.fragment.FragmentCallback;

public class ItemFactory {

	
	public static BaseItem createItem(Context context,File file, FragmentCallback callback){
		BaseItem item = null;
		String path = file.getPath();
		if(file.isDirectory())
			item = new DirectoryItem(context, path);
		else {
			MediaFileType fileType = MediaFile.getFileType(path);
			if(fileType == null)
				return new FileItem(context, path);
			if(MediaFile.isAudioFileType(fileType.fileType))
				item = new AudioItem(context, path);
			else if(MediaFile.isImageFileType(fileType.fileType))
				item = new ImageItem(context, path);
			else if(MediaFile.isVideoFileType(fileType.fileType))
				item = new VideoItem(context, path);
			else if(fileType.fileType == MediaFile.FILE_TYPE_APK)
				item = new ApkItem(context,path);
			else if(fileType.fileType >= MediaFile.FILE_TYPE_ZIP)
				item = getCompressItem(context,path,fileType.fileType);
			else 
				item = new FileItem(context, path);
			item.setMimeTypeCode(fileType.fileType);
		}
		item.setCallback(callback);
		return item;
	}

	private static BaseItem getCompressItem(Context context, String path,
			int fileType) {
		BaseItem item = null;
		switch (fileType) {
		case MediaFile.FILE_TYPE_ZIP:
			item = new ZipItem(context, path);
			break;
		case MediaFile.FILE_TYPE_RAR:
			item = new RarItem(context, path);
			break;
		case MediaFile.FILE_TYPE_BZIP2:
			item = new ApacheCompressItem(context, path);
			break;
		case MediaFile.FILE_TYPE_GZ:
			item = new ApacheCompressItem(context, path);
			break;
		case MediaFile.FILE_TYPE_JAR:
			item = new ApacheCompressItem(context, path);
			break;
		case MediaFile.FILE_TYPE_7Z:
			item = new ApacheCompressItem(context, path);
			break;
		case MediaFile.FILE_TYPE_TAR:
			item = new ApacheCompressItem(context, path);
			break;
		case MediaFile.FILE_TYPE_XZ:
			item = new ApacheCompressItem(context, path);
			break;
		case MediaFile.FILE_TYPE_DUMP:
			item = new ApacheCompressItem(context, path);
			break;
		case MediaFile.FILE_TYPE_AR:
			item = new ApacheCompressItem(context, path);
			break;
		case MediaFile.FILE_TYPE_ARJ:
			item = new ApacheCompressItem(context, path);
			break;
		case MediaFile.FILE_TYPE_CPIO:
			item = new ApacheCompressItem(context, path);
			break;
		case MediaFile.FILE_TYPE_Z:
			item = new ApacheCompressItem(context, path);
			break;
		case MediaFile.FILE_TYPE_LZMA:
			item = new ApacheCompressItem(context, path);
			break;
		case MediaFile.FILE_TYPE_PACK200:
			item = new ApacheCompressItem(context, path);
			break;
		case MediaFile.FILE_TYPE_SNAPPY_FRAMED:
			item = new ApacheCompressItem(context, path);
			break;
		default:
			item = new CompressItem(context, path);
			break;
		}
		return item;
	}
}
