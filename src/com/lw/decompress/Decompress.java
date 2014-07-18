package com.lw.decompress;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import com.hostzi.blenderviking.extractarchiveandroid.DecompressCallback;


public class Decompress {
	private File mSource;
	private DecompressCallback mCallback;
	private long mTotal;
	private String mCharset;
	private boolean isCancel;
	
	public Decompress(String source){
		mSource = new File(source);
		mTotal = mSource.length();
	}
	public Decompress(File file){
		mSource = file;
		mTotal = file.length();
	}
	
	public void setCharset(String charset){
		mCharset = charset;
	}
	
	public void decompress(String outDirectory) throws Exception{
		decompress(new File(outDirectory));
	}
	
	public void setCallback(DecompressCallback callback){
		mCallback = callback;
	}
	
	public void cancel(){
		isCancel = true;
	}
	
	public void decompress(File outDirectory) throws Exception {
		if(!outDirectory.exists())
			outDirectory.mkdirs();
		if(outDirectory.isFile()){
			outDirectory = new File(outDirectory.getAbsolutePath()+"_");
		}
		String ext = getExtFromFileName(mSource.getName()).toLowerCase();
		if(ext.equals(ArchiveStreamFactory.AR) || ext.equals(ArchiveStreamFactory.ARJ) 
				|| ext.equals(ArchiveStreamFactory.CPIO) 
				|| ext.equals(ArchiveStreamFactory.DUMP) 
				|| ext.equals(ArchiveStreamFactory.JAR) 
				|| ext.equals(ArchiveStreamFactory.TAR) ){
			uncompressArchive(outDirectory,ext);
		}else if(ext.equals(ArchiveStreamFactory.ZIP)){
			umcompressZipArchive(outDirectory);
		}else if(ext.equals(ArchiveStreamFactory.SEVEN_Z)){
			unCompressSevenZip(outDirectory);
		}else if(ext.equals(CompressorStreamFactory.BZIP2)  || ext.equals("bz2")
				|| ext.equals(CompressorStreamFactory.GZIP) 
				|| ext.equals(CompressorStreamFactory.LZMA)
				|| ext.equals(CompressorStreamFactory.PACK200) || ext.equals("pack") 
				|| ext.equals(CompressorStreamFactory.SNAPPY_FRAMED) || ext.equals("sz")
				|| ext.equals(CompressorStreamFactory.SNAPPY_RAW) 
				|| ext.equals(CompressorStreamFactory.XZ) 
				|| ext.equals(CompressorStreamFactory.Z)){
			
			 unCompress(ext,outDirectory);
		}
 	}
	private void unCompress(String ext,File outDirectory) throws Exception {
		final InputStream is = new FileInputStream(mSource); 
		 CompressorInputStream in = null;
		 if(!outDirectory.exists())
			 outDirectory.mkdirs();
		 try{
		      in = new CompressorStreamFactory().createCompressorInputStream(ext, is);
		 }catch(Exception e){
			 BufferedInputStream bi = new BufferedInputStream(is);
			 in = new CompressorStreamFactory().createCompressorInputStream(bi);
		 }
		 String name =  mSource.getName();
		 int start = name.indexOf(".");
		 int end = name.lastIndexOf(".");
		 String archiverName = "";
		 if(end > start && start >= 0) {
			 archiverName = name.substring(start + 1, end);
		 }
		 if(IsSupportExt(archiverName)) {
			 uncompressArchive(in, outDirectory, archiverName);
		 } else{
			 if(end < 0)
				end = name.length(); 
			 if(mCallback != null)
				 mCallback.onEntryChange(name);
			 File file = new File(outDirectory, name.substring(0, end));
			 if(!file.getParentFile().exists())
				 file.getParentFile().mkdirs();
			 FileOutputStream fos = new FileOutputStream(file);
			 byte buf[] = new byte[8012];
			 int len = 0;
			 while((len = in.read(buf, 0, buf.length)) != -1){
				 fos.write(buf, 0, len);
				 if(mCallback != null) {
					 long current = in.getBytesRead();
					 mCallback.onEntryProgress(current, mTotal);
					 mCallback.onProgress(current, mTotal);
				 }
				 if(isCancel){
					 if(mTotal - in.getBytesRead() > 8*1024){
						 isCancel = false;
						 fos.close();
						 in.close();
						 throw new Exception("cancel");
					 }
				 }
			 }
			 fos.close();
		 }
			 
		 in.close();
	}
	private boolean IsSupportExt(String ext) {
		if(ext.equals(ArchiveStreamFactory.AR) || ext.equals(ArchiveStreamFactory.ARJ) 
				|| ext.equals(ArchiveStreamFactory.CPIO) 
				|| ext.equals(ArchiveStreamFactory.DUMP) 
				|| ext.equals(ArchiveStreamFactory.JAR) 
				|| ext.equals(ArchiveStreamFactory.TAR)
				|| ext.equals(ArchiveStreamFactory.ZIP))
			return true;
		return false;
	}
	
	private void unCompressSevenZip(File outDirectory) throws Exception {
		SevenZFile sevenZFile = new SevenZFile(mSource);
		SevenZArchiveEntry entry = sevenZFile.getNextEntry();
	
		long totalRead = 0;
		while(entry != null){
			String name = entry.getName();
			File file = new File(outDirectory, name);
			if(!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			if(mCallback != null){
				mCallback.onEntryChange(name);
			}
			if(entry.isDirectory())
				file.mkdir();
			else{
				long size = entry.getSize();
				byte buffer[] = new byte[8024];
				FileOutputStream fout = new FileOutputStream(file);
				long read = 0;
				int temp = 0;
				int bsize = 8012;
				while((temp = sevenZFile.read(buffer, 0, bsize)) != -1){
					fout.write(buffer, 0, temp);
					read += temp;
					totalRead += temp;
					if(isCancel){
						if(size - read > 8*1024){
							isCancel = false;
							throw new Exception("cancel");
						}
					}
					if(mCallback != null){
						mCallback.onEntryProgress(read, size);
						mCallback.onProgress(totalRead, mTotal);
					}
					if(size - read < bsize){
						bsize = (int) (size - read);
						if(bsize == 0)
							break;
					}
				}
				fout.close();
			}
			entry = sevenZFile.getNextEntry();
		}
		sevenZFile.close();
	}
	
	private void uncompressArchive(File outDirectory) throws Exception {
		uncompressArchive(outDirectory,null);
		
	}
	private void umcompressZipArchive(File outDirectory) throws Exception {
		FileInputStream fin = new FileInputStream(mSource);
//		BufferedInputStream bi = new BufferedInputStream(fin);
		ArchiveStreamFactory factory = new ArchiveStreamFactory();
		ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, fin);
		ZipArchiveEntry entry = (ZipArchiveEntry) in.getNextEntry();
		while(entry != null){
			byte data[] = entry.getRawName();
			String charset = "utf-8";
			if(!IsUTF8Bytes(data))
				charset = "gbk";
			File file = new File(outDirectory, new String(data,charset));
			if(mCallback != null)
				mCallback.onEntryChange(entry.getName());
			if(entry.isDirectory()) {
				file.mkdirs();
			}else{
				if(!file.getParentFile().exists())
					file.getParentFile().mkdirs();
				OutputStream out = new FileOutputStream(file);
				copy(in, out,entry.getSize());
				out.close();
			}
			entry = (ZipArchiveEntry) in.getNextEntry();
		}
		
		in.close();
		fin.close();
	}
	public void uncompressArchive(File outDirectory,String archiverName)
			throws Exception {
		FileInputStream fin = new FileInputStream(mSource);
		BufferedInputStream bi = new BufferedInputStream(fin);
		ArchiveStreamFactory factory = new ArchiveStreamFactory();
		if(mCharset != null)
			factory.setEntryEncoding(mCharset);
		ArchiveInputStream in = null;
		if(archiverName == null)
			in = factory.createArchiveInputStream(bi);
		else
			in = factory.createArchiveInputStream(archiverName, bi);
		ArchiveEntry entry = in.getNextEntry();
		while(entry != null){
			File file = new File(outDirectory, entry.getName());
			if(mCallback != null)
				mCallback.onEntryChange(entry.getName());
			if(entry.isDirectory()) {
				file.mkdirs();
			}else{
				if(!file.getParentFile().exists())
					file.getParentFile().mkdirs();
				OutputStream out = new FileOutputStream(file);
				copy(in, out,entry.getSize());
				out.close();
			}
			entry = in.getNextEntry();
		}
		
		in.close();
		bi.close();
		fin.close();
	}
	
	public void uncompressArchive(InputStream fin,File outDirectory,String archiverName)
			throws Exception {
		BufferedInputStream bi = new BufferedInputStream(fin);
		ArchiveStreamFactory factory = new ArchiveStreamFactory();
		if(mCharset != null)
			factory.setEntryEncoding(mCharset);
		ArchiveInputStream in = null;
		if(archiverName == null)
			in = factory.createArchiveInputStream(bi);
		else
			in = factory.createArchiveInputStream(archiverName, bi);
		ArchiveEntry entry = in.getNextEntry();
		while(entry != null){
			File file = new File(outDirectory, entry.getName());
			if(mCallback != null)
				mCallback.onEntryChange(entry.getName());
			if(entry.isDirectory()) {
				file.mkdirs();
			}else{
				if(!file.getParentFile().exists())
					file.getParentFile().mkdirs();
				OutputStream out = new FileOutputStream(file);
				copy(in, out,entry.getSize());
				out.close();
			}
			entry = in.getNextEntry();
		}
		in.close();
		bi.close();
	}
	
    private long copy(final ArchiveInputStream input, final OutputStream output,long size) throws Exception {
        final byte[] buffer = new byte[8024];
        int n = 0;
        long count=0;
        int i = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
            i++;
            if(isCancel){
            	if(size - count > 8 * 1024){
            		isCancel = false;
            		throw new Exception("cancel the progress");
            	}
            }
            if(mCallback != null && i%3 == 0){
            	mCallback.onEntryProgress(count, size);
            	mCallback.onProgress(input.getBytesRead(), mTotal);
            }
        }
        return count;
    }
    
    public static String getExtFromFileName(String name){
        int dotPosition = name.lastIndexOf('.');
        if (dotPosition != -1) {
            return name.substring(dotPosition + 1, name.length());
        }
        return "";
    }
    
    public static boolean IsUTF8Bytes(byte[] data) {

        int i = 0;
        int size = data.length;

        while(i < size)
        {
            int step = 0;
            if((data[i] & 0x80) == 0x00)
            {
                step = 1;
            }
            else if((data[i] & 0xe0) == 0xc0)
            {
                if(i + 1 >= size) return false;
                if((data[i + 1] & 0xc0) != 0x80) return false;

                step = 2;
            }
            else if((data[i] & 0xf0) == 0xe0)
            {
                if(i + 2 >= size) return false;
                if((data[i + 1] & 0xc0) != 0x80) return false;
                if((data[i + 2] & 0xc0) != 0x80) return false;

                step = 3;
            }
            else
            {
                return false;
            }

            i += step;
        }

        if(i == size) return true;

        return false;
    }
}
