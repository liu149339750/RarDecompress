/**
 * Copyright (C) 2013, Romain Estievenart
 * ExtractFile class
 * @author: Romain Estievenart
 * @date: 2013-11-07
 */
package com.lw.decompress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import u.aly.ca;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.github.junrar.UnrarCallback;
import com.github.junrar.Volume;
import com.hostzi.blenderviking.zip.ManipZip;
import com.lw.rardecompress.R;
import com.snda.Andro7z.Andro7za;

/**
 * Extract file archive into a specific output directory
 * 
 * @author: Romain Estievenart
 * 
 */
public class ExtractFile {
	private Context ctx = null;
	private File archive = null, outputdir = null;
	private DecompressCallback mProgressCallback;
	private boolean isCancel;
	/**
	 * Extract file archive into a specific output directory
	 * 
	 * @param _ctx
	 *            - Context
	 * @param archivePath
	 *            - The archive file path
	 * @param outputDirPath
	 *            - The output directory path
	 * @throws IOException
	 *             - IOException
	 */
	public ExtractFile(Context _ctx, String archivePath, String outputDirPath)
			throws IOException {
		ExtractFileInit(_ctx, new File(archivePath), new File(outputDirPath));
	}

	/**
	 * Extract file archive into a specific output directory
	 * 
	 * @param _ctx
	 *            - Context
	 * @param archive
	 *            - The archive file
	 * @param outputdir
	 *            - The output directory
	 * @throws IOException
	 *             - IOException
	 */
	private void ExtractFileInit(Context _ctx, File archive, File outputdir)
			throws IOException {
		if (!archive.exists())
			throw new FileNotFoundException("Archive file not found : "
					+ archive.getCanonicalPath());
		if (archive.isDirectory())
			throw new IOException("Archive file is a directory : "
					+ archive.getCanonicalPath());
		if (outputdir.exists() && !outputdir.isDirectory())
			throw new IOException("The output directory is not a folder : "
					+ outputdir.getCanonicalPath());

		this.ctx = _ctx;
		this.archive = archive;
		this.outputdir = outputdir;
	}

	/**
	 * Start the extract process
	 * 
	 * @throws IOException
	 */
	public void exec() throws IOException {
		AsyncTask<File, FileInfo, Boolean> task = null;

		if (archive.getName().matches("(?i)(.*?(.zip|cbz))"))
			task = new ExtractZipFileTask();
		else if (archive.getName().matches("(?i)(.*?(.rar|cbr))"))
			task = new ExtractRarFileTask();
		else if (archive.getName().matches("(?i)(.*?(.7z|cb7))"))
			task = new Extract7zFileTask();
		else
			throw new IOException("loading_file_error");

		if (!outputdir.exists())
			outputdir.mkdirs();

		if (task != null)
			task.execute(archive, outputdir);
	}
	
	public void setCallback(DecompressCallback callback){
		mProgressCallback = callback;
	}
	
	public void cancel(){
		isCancel = true;
	}

	/**
	 * Extract file archive into a specific output directory
	 * 
	 * @param _ctx
	 *            - Context
	 * @param archive
	 *            - The archive file
	 * @param outputdir
	 *            - The output directory
	 * @throws IOException
	 *             - IOException
	 */
	public ExtractFile(Context _ctx, File archive, File outputdir)
			throws IOException {
		ExtractFileInit(_ctx, archive, outputdir);
	}

	/**
	 * Extract zip file with a AsyncTask
	 * 
	 * @author Romain Estievenart
	 * 
	 */
	private class ExtractZipFileTask extends AsyncTask<File, FileInfo, Boolean> {
		/**
		 * Extract in background a zip archive into a directory File params[0] -
		 * the zip archive; File params[1] - the output directory;
		 */
		@Override
		protected Boolean doInBackground(File... params) {
			boolean result = false;
			if (params.length != 2)
				return result;

			try {
				result = ManipZip.extract(params[0], params[1]);
				return result;
			} catch (IOException e) {
				return result;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(
					ctx,
					(result) ? R.string.loading_file_complete
							: R.string.loading_file_error, Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * Extract rar file with a AsyncTask
	 * 
	 * @author Romain Estievenart
	 * 
	 */
	private class ExtractRarFileTask extends AsyncTask<File, FileInfo, Boolean> implements UnrarCallback,FileCallback{
		/**
		 * Call in background the junrar library for extract a rar file File
		 * params[0] : archive file File params[1] : output directory
		 */
		@Override
		protected Boolean doInBackground(File... params) {
			File rarFile = params[0];
			File outputDir = params[1];
			try{
			if (ExtractArchive.extractArchive(rarFile, outputDir,this,this))
				return true;
			else
				return false;
			}catch(NullPointerException e){
				
			}catch (Exception e) {
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(
					ctx,
					(result) ? "解压结束"
							: "解压失败，是否为Rar文件", Toast.LENGTH_SHORT)
					.show();
			if(mProgressCallback != null)
			mProgressCallback.onDecompressEnd(result);
		}

		@Override
		public boolean isNextVolumeReady(Volume nextVolume) {
			// TODO Auto-generated method stub
			return true;
		}
		
		@Override
		protected void onProgressUpdate(FileInfo... values) {
			FileInfo fi = values[0];
			if(mProgressCallback != null){
			if(fi.type == Type.name)
				mProgressCallback.onEntryChange(fi.name);
			else
				mProgressCallback.onProgress(fi.current, fi.total);
			}
		}

		@Override
		public void volumeProgressChanged(long current, long total) {
			FileInfo fi = new FileInfo();
			fi.current = current;
			fi.total = total;
			fi.type = Type.currentpercent;
			publishProgress(fi);
		}

		@Override
		public void onFileName(String name) {
			FileInfo fi = new FileInfo();
			fi.name = name;
			fi.type = Type.name;
			publishProgress(fi);
		}
	}

	/**
	 * Extract 7z file with a AsyncTask
	 * 
	 * @author Romain Estievenart
	 * 
	 */
	private class Extract7zFileTask extends AsyncTask<File, FileInfo, Boolean> {
		/**
		 * Call in background the J7zip library for extract a 7z file File
		 * params[0] : archive file File params[1] : output directory
		 */
		@Override
		protected Boolean doInBackground(File... params) {

			try {
				File archive7z = params[0];
				File outputDir = params[1];

				Andro7za a7z = new Andro7za();

				switch (a7z.printUsage(archive7z, outputDir)) {
				case 0: // Successful operation
				case 1: // Non fatal error(s) occured
					return true;
				case 2: // fatal error occured
					System.err.println(ctx
							.getString(R.string.loading_file_error));
					return false;
				case 8: // Not enough memory for operation
					System.err
							.println(ctx
									.getString(R.string.not_enough_memory_for_operation));
				default:
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			Toast.makeText(
					ctx,
					(result) ? R.string.loading_file_complete
							: R.string.loading_file_error, Toast.LENGTH_SHORT)
					.show();
		}
	}
	
	public static class FileInfo{
		public String name;
		public Type type;
		public long current;
		public long total;
	}
	
	public enum Type{
		name,currentpercent,totalpercent
	}

	public interface FileCallback{
		public void onFileName(String name);
	}
}