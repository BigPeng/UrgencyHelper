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
	 * ��ʼ��λ
	 */
	public void startLocate() {
		setLocationOption();
		// ��ʼ��λ
		if (mLocationInit) {
			mLocationClient.start();
		} else {
			//Toast.makeText(mContext, "�����ö�λ��صĲ���", Toast.LENGTH_SHORT).show();
			return;
		}
		if (mLocationClient.isStarted()) {
			// ��������λ
			mLocationClient.requestLocation();
		}

	}

	// ����Option
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
	 * ���ü�����
	 * 
	 * @param listener
	 */
	public void setListener(LocationedListener listener) {
		mListener = listener;
	}

	/**
	 * ʵ��ʵλ�ص�����
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("γ�� : ");
			sb.append(location.getLatitude());
			sb.append("������ : ");
			sb.append(location.getLongitude());
			if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("(��վ��λ)");
			} else if (location.getLocType() == BDLocation.TypeGpsLocation)
				sb.append("(GPS��λ)");
			Log.i("BaiduLocationApiDem", sb.toString());
			mListener.processLocation(sb.toString());
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}
}
