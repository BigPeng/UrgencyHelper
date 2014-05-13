package com.C102C.urgencyhelper.location;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class LocationService {
	private LocationClient mLocationClient;
	private GeofenceClient mGeofenceClient;
	private MyLocationListener mMyLocationListener;
	private Context mContext;
	private boolean mLocationInit;
	private LocationedListener mListener;

	public LocationService(Context context) {
		mContext = context;
		mLocationClient = new LocationClient(mContext);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(mContext);
	}

	public void stopLocate() {
		mLocationClient.stop();
	}

	/**
	 * 开始定位
	 */
	public void startLocate() {
		setLocationOption();
		// 开始定位
		if (mLocationInit) {
			mLocationClient.start();
		} else {
			//Toast.makeText(mContext, "请设置定位相关的参数", Toast.LENGTH_SHORT).show();
			return;
		}
		if (mLocationClient.isStarted()) {
			// 单次请求定位
			mLocationClient.requestLocation();
		}

	}

	// 设置Option
	private void setLocationOption() {
		try {
			LocationClientOption option = new LocationClientOption();
			option.setLocationMode(LocationMode.Hight_Accuracy);
			option.setCoorType("bd09ll");
			option.setScanSpan(100);
			option.setNeedDeviceDirect(false);
			option.setIsNeedAddress(true);
			mLocationClient.setLocOption(option);
			mLocationInit = true;
		} catch (Exception e) {
			e.printStackTrace();
			mLocationInit = false;
		}
	}

	public interface LocationedListener {
		public void processLocation(String location);
	}

	/***
	 * 设置监听器
	 * 
	 * @param listener
	 */
	public void setListener(LocationedListener listener) {
		mListener = listener;
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("纬度 : ");
			sb.append(location.getLatitude());
			sb.append("，经度 : ");
			sb.append(location.getLongitude());
			if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("(基站定位)");
			} else if (location.getLocType() == BDLocation.TypeGpsLocation)
				sb.append("(GPS定位)");
			Log.i("BaiduLocationApiDem", sb.toString());
			mListener.processLocation(sb.toString());
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}
}
