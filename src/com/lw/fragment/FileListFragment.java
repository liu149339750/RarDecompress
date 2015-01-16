package com.lw.fragment;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.lw.adapter.FileAdapter;
import com.lw.adapter.TitleAdapter;
import com.lw.item.BaseItem;
import com.lw.item.FileItem;
import com.lw.item.ItemFactory;
import com.lw.other.FileSortHelper;
import com.lw.rardecompress.R;

public class FileListFragment extends ListFragment implements OnClickListener,OnItemSelectedListener,FragmentCallback{
	private FileAdapter mAdapter;
	private TitleAdapter mTitleAdapter;
	private BaseItem mCurrentItem;
	private ImageButton mUpPress;
	private BaseItem mSelectItem;
	private FileSortHelper mSortHelper;
	private Spinner mSpinner;
	
	private boolean isShowHide ;
	
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
		
		setHasOptionsMenu(true);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.showhide){
			isShowHide = !isShowHide;
			mAdapter.changeItems(getListItems(mCurrentItem));
		}
		return super.onOptionsItemSelected(item);
	}
	
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		System.out.println("onCreateOptionsMenu");
//		String title = "显示隐藏文件夹";
//		if (isShowHide) {
//			title = "不显示影藏文件夹";
//		}
//		menu.add(0, R.id.showhide, 0, title);
//	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		String title = "显示隐藏文件夹";
		if (isShowHide) {
			title = "不显示影藏文件夹";
		}
		menu.add(0, R.id.showhide, 0, title);
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
		File files[] = f.listFiles(mFileFilter);
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
	public boolean onContextItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.delete:
			String name = mSelectItem.getName();
			new AlertDialog.Builder(getActivity()).setMessage("请确认是否删除 " + name).setTitle("删除" + name).setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mSelectItem.delete();
					mAdapter.remove(mSelectItem);
				}
			}).setNegativeButton("取消", null).show();
			break;
		case R.id.rename:
			final EditText editText = new EditText(getActivity());
			name = mSelectItem.getName();
			editText.setText(name);
			new AlertDialog.Builder(getActivity()).setView(editText).setTitle("输入新文件夹名").setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(!TextUtils.isEmpty(editText.getText().toString())){
						mSelectItem.reName(editText.getText().toString());
						mAdapter.notifyDataSetChanged();
					}else
						Toast.makeText(getActivity(), "不能为空", Toast.LENGTH_SHORT).show();
				}
			}).setNegativeButton("取消", null).show();
			break;
		default:
			break;
		}
		
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo ai = (AdapterContextMenuInfo) menuInfo;
		mSelectItem = mAdapter.getItem(ai.position);
		getActivity().getMenuInflater().inflate(R.menu.context, menu);
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
		if(getActivity() != null)
			mAdapter.changeItems(getListItems(mCurrentItem));
	}
	
	private FileFilter mFileFilter = new FileFilter() {
		
		@Override
		public boolean accept(File file) {
			if(file.isFile())
				return true;
			if(!isShowHide && file.getName().startsWith("."))
				return false;
			return true;
		}
	};
}
