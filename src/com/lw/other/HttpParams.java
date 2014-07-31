package com.lw.other;

import com.umeng.analytics.MobclickAgent;

import android.content.Context;

public class HttpParams {

	
	public static boolean showAd(Context context){
		return Boolean.parseBoolean(MobclickAgent.getConfigParams(context, "showAd"));
	}
}
