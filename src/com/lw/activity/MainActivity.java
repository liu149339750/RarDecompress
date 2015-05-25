package com.lw.activity;

import org.json.JSONObject;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.waps.AppConnect;

import com.lw.fragment.FileListFragment;
import com.lw.rardecompress.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class MainActivity extends FragmentActivity {

	private FileListFragment mListFragment;
	private Handler handler;
	private boolean isHaseShowAd = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		handler = new Handler();
		if (findViewById(R.id.content) != null) {

        	mListFragment = new FileListFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.content, mListFragment).commit();
		}
		MobclickAgent.updateOnlineConfig( this );
		if(showMyApp())
			findViewById(R.id.ad).setVisibility(View.VISIBLE);
//		else
//			findViewById(R.id.ad).setVisibility(View.INVISIBLE);
		MobclickAgent.setOnlineConfigureListener(new UmengOnlineConfigureListener(){
			  @Override
			  public void onDataReceived(JSONObject data) {
				  System.out.println(data);
					if(showMyApp())
						findViewById(R.id.ad).setVisibility(View.VISIBLE);
					if(showAd()&&!isHaseShowAd && forceShowAd()){
						SpotManager.getInstance(MainActivity.this).showSpotAds(MainActivity.this, new SpotDialogListener() {
						    @Override
						    public void onShowSuccess() {
						        Log.i("Youmi", "onShowSuccess");
						        MobclickAgent.onEvent(MainActivity.this, "showAd");
						    }

						    @Override
						    public void onShowFailed() {
						        Log.i("Youmi", "onShowFailed");
						    }
						});
						isHaseShowAd = true;
					}
			  }


			});
//		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
//		ViewGroup adLayout=(ViewGroup)findViewById(R.id.ad);
//		adLayout.addView(adView); 
//		if(Util.isConected(this))
//			findViewById(R.id.title_content).setVisibility(View.GONE);
		findViewById(R.id.ad).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				AppConnect.getInstance(MainActivity.this).showMore(MainActivity.this,"8ae46d4021c89d8fa6016b3c1ead5f05");
//				UmengUpdateAgent.forceUpdate(MainActivity.this);
				UmengUpdateAgent.forceUpdate(MainActivity.this);
			}
		});
//		AppConnect.getInstance("5e120e9c0d4b56d697dcafef7783d897","default",this);
		AppConnect.getInstance(this);
		AppConnect.getInstance(this).initUninstallAd(this);
//		AppConnect.getInstance(this).initPopAd(this);
		
		AdManager.getInstance(this).init("49c6a07f582acbbe", "946b4bec08ff814a", false);
		SpotManager.getInstance(this).loadSpotAds();
		UmengUpdateAgent.setDeltaUpdate(false);
//		UmengUpdateAgent.update(this); 
		
		if(showAd() && !isHaseShowAd){
			SpotManager.getInstance(MainActivity.this).showSpotAds(MainActivity.this, new SpotDialogListener() {
			    @Override
			    public void onShowSuccess() {
			        Log.i("Youmi", "onShowSuccess");
			        MobclickAgent.onEvent(MainActivity.this, "showAd");
			       
			    }

			    @Override
			    public void onShowFailed() {
			        Log.i("Youmi", "onShowFailed");
			    }
			});
			isHaseShowAd = true;
		}
		if(!showAd())
			UmengUpdateAgent.silentUpdate(this);
		
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			
			@Override
			public void onUpdateReturned(int arg0, UpdateResponse arg1) {
				switch (arg0) {
		        case UpdateStatus.Yes: // has update
		            UmengUpdateAgent.showUpdateDialog(MainActivity.this, arg1);
		            break;
		        case UpdateStatus.No: // has no update
		        		Toast.makeText(MainActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
		            break;
		        case UpdateStatus.NoneWifi: // none wifi
		            break;
		        case UpdateStatus.Timeout: // time out
		            break;
		        }
			}
		});

	}
	
	public boolean showMyApp(){
		return Boolean.parseBoolean(MobclickAgent.getConfigParams(this, "showMyApp1"));
	}
	public boolean showAd(){
		return Boolean.parseBoolean(MobclickAgent.getConfigParams(this, "showAd1"));
	}
	private boolean forceShowAd() {
		 return Boolean.parseBoolean(MobclickAgent.getConfigParams(this, "forceshowAd"));
	}

	@Override
	public void onBackPressed() {
		if(!mListFragment.back()){
			if(!isHaseShowAd)
				SpotManager.getInstance(MainActivity.this).showSpotAds(MainActivity.this);
			super.onBackPressed();
//			moveTaskToBack(true);
		}
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
	public boolean onCreateOptionsMenu(Menu menu) {
		System.out.println("Activit onCreateOp");
		return false;
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppConnect.getInstance(this).close();
		SpotManager.getInstance(this)
        .unregisterSceenReceiver();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
