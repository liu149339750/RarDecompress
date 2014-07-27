package com.lw.decompress;

public interface DecompressCallback {

	
	public void onProgress(long current,long total);
	public void onEntryProgress(long current,long total);
	public void onEntryChange(String name);
	public void onDecompressEnd(boolean result);
}
