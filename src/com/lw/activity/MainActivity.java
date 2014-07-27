package com.lw.activity;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lw.fragment.FileListFragment;
import com.lw.rardecompress.R;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends FragmentActivity {

	private FileListFragment mListFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		if (findViewById(R.id.content) != null) {

        	mListFragment = new FileListFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.content, mListFragment).commit();
		}
		MobclickAgent.updateOnlineConfig( this );
		AdManager.getInstance(this).init("49c6a07f582acbbe", "946b4bec08ff814a", false);
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		ViewGroup adLayout=(ViewGroup)findViewById(R.id.ad);
		adLayout.addView(adView); 
		if(Util.isConected(this))
			findViewById(R.id.title_content).setVisibility(View.GONE);
	}

	@Override
	public void onBackPressed() {
		if(!mListFragment.back())
			super.onBackPressed();
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
