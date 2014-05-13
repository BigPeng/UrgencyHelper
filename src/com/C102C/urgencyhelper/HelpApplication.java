package com.C102C.urgencyhelper;

import android.app.Application;
import android.content.Intent;

import com.C102C.urgencyhelper.location.LocationService;
import com.C102C.util.SharedPreferenceUtil;

public class HelpApplication extends Application {

	private static HelpApplication mApplication;
	private SharedPreferenceUtil spUtil = null;
	private LocationService locationService;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		locationService = new LocationService(this);	

	}
	
	public LocationService getLocationService(){
		return locationService;
	}

	public static HelpApplication getInstance() {
		return mApplication;
	}

	public SharedPreferenceUtil getSpUtil() {
		if (spUtil == null)
			spUtil = new SharedPreferenceUtil(this);
		return spUtil;
	}

}
