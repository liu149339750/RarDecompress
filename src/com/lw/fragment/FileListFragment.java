package com.lw.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.lw.activity.FileSortHelper;
import com.lw.adapter.FileAdapter;
import com.lw.adapter.TitleAdapter;
import com.lw.item.BaseItem;
import com.lw.item.FileItem;
import com.lw.item.ItemFactory;
import com.lw.rardecompress.R;

public class FileListFragment extends ListFragment implements OnClickListener,OnItemSelectedListener,FragmentCallback{
	private FileAdapter mAdapter;
	private TitleAdapter mTitleAdapter;
	private BaseItem mCurrentItem;
	private ImageButton mUpPress;
	private BaseItem mSelectItem;
	private FileSortHelper mSortHelper;
	private Spinner mSpinner;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mSortHelper = new FileSortHelper();
		mTitleAdapter = new TitleAdapter(getActivity());
		mUpPress = (ImageButton) getActivity().findViewById(R.id.up_press);
		mSpinner = (Spinner) getActivity().findViewById(R.id.tspinner);
		mSpinner.setAdapter(mTitleAdapter);
		mSpinner.setOnItemSelectedListener(this);
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
			BaseItem fitem = ItemFactory.createItem(getActivity(), file,this);
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
			mTitleAdapter.add(item);
			mSpinner.setSelection(mTitleAdapter.getCount() - 1);
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
		mTitleAdapter.removeLast();
		mSpinner.setSelection(mTitleAdapter.getCount() - 1);
		updataPressStatus();
	}

	private void updataPressStatus() {
		if(mCurrentItem.getParent() != null){
//			mUpPress.setVisibility(View.VISIBLE);
			mUpPress.setEnabled(true);
		}else{
//			mUpPress.setVisibility(View.INVISIBLE);
			mUpPress.setEnabled(false);
		}
	}
	
	public boolean back(){
		if(mCurrentItem.getParent() != null){
			showParent();
			return true;
		}
		return false;
	}
	
	public static class TreeInfo{
		public String tip;
		public BaseItem item;
		public TreeInfo last;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		TreeInfo info = mTitleAdapter.getItem(position);
		mTitleAdapter.change(info);
		mCurrentItem = info.item;
		mAdapter.changeItems(getListItems(mCurrentItem));
		updataPressStatus();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		System.out.println("onNothingSelected");
	}

	@Override
	public void onNotifyDataSetChanged() {
		mAdapter.changeItems(getListItems(mCurrentItem));
	}
}
