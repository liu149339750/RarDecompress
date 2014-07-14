package com.lw.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.example.rardecompress.FileSortHelper;
import com.example.rardecompress.R;
import com.lw.adapter.FileAdapter;
import com.lw.item.BaseItem;
import com.lw.item.FileItem;
import com.lw.item.ItemFactory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class FileListFragment extends ListFragment implements OnClickListener{
	private FileAdapter mAdapter;
	private BaseItem mCurrentItem;
	private ImageButton mUpPress;
	private BaseItem mSelectItem;
	private FileSortHelper mSortHelper;
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mSortHelper = new FileSortHelper();
		mUpPress = (ImageButton) getActivity().findViewById(R.id.up_press);
		mUpPress.setOnClickListener(this);
		mCurrentItem = new BaseItem(getActivity(), Environment.getExternalStorageDirectory().getAbsolutePath());
		mAdapter = new FileAdapter(getActivity(), getListItems(mCurrentItem));
		setListAdapter(mAdapter);
		updataPressStatus();
		getListView().setOnCreateContextMenuListener(this);
	}
	
	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onInflate(activity, attrs, savedInstanceState);
	}
	
	private List<BaseItem> getListItems(BaseItem item) {
		String path = item.getPath();
		File f = new File(path);
		List<BaseItem> results = new ArrayList<BaseItem>();
		File files[] = f.listFiles();
		if(files == null)
			return new ArrayList<BaseItem>();
		for(File file : files){
			BaseItem fitem = ItemFactory.createItem(getActivity(), file);
			fitem.setParent(item);
			results.add(fitem);
		}
		Collections.sort(results, mSortHelper.getComparator());
		return results;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		BaseItem item = (BaseItem) mAdapter.getItem(position);
		if(!item.isFile()){
			mCurrentItem = item;
			mAdapter.changeItems(getListItems(item));
			updataPressStatus();
		}else{
			FileItem fitem = (FileItem) item;
			fitem.open();
		}
	}

	@Override
	public void onClick(View v) {
		if(v == mUpPress){
			showParent();
		}
	}

	private void showParent() {
		mCurrentItem = mCurrentItem.getParent();
		mAdapter.changeItems(getListItems(mCurrentItem));
		updataPressStatus();
	}

	private void updataPressStatus() {
		if(mCurrentItem.getParent() != null)
			mUpPress.setVisibility(View.VISIBLE);
		else
			mUpPress.setVisibility(View.INVISIBLE);
	}
	
	public boolean back(){
		if(mCurrentItem.getParent() != null){
			showParent();
			return true;
		}
		return false;
	}
	

}
