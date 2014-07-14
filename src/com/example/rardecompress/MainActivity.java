package com.example.rardecompress;

import com.lw.fragment.FileListFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {

	private FileListFragment mListFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (findViewById(R.id.content) != null) {
		
        if (savedInstanceState != null) {
            return;
        }
        	mListFragment = new FileListFragment();
			getSupportFragmentManager().beginTransaction().add(R.id.content, mListFragment).commit();
		}	
	}

	@Override
	public void onBackPressed() {
		if(!mListFragment.back())
			super.onBackPressed();
	}
}
