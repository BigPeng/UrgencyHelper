package com.C102C.urgencyhelper.powerbuttonserver;

import java.util.List;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;

import com.C102C.urgencyhelper.HelpApplication;
import com.C102C.urgencyhelper.location.LocationService;
import com.C102C.urgencyhelper.location.LocationService.LocationedListener;
import com.C102C.urgencyhelper.powerbuttonserver.PowerButtonWacther.InnerRecevier;
import com.C102C.urgencyhelper.powerbuttonserver.PowerButtonWacther.OnPowerButtonPressedListener;
import com.C102C.util.DataBaseService;
import com.C102C.util.L;
import com.C102C.util.UrgencyFriend.Table;
/**
 * 处理和发送紧急短信
 * @author jiqunpeng
 *
 * 创建时间：2014-5-8 下午10:00:56
 */
public class UrgencyProcessor implements OnPowerButtonPressedListener,
		LocationedListener {
	private PressCounter mCounter;
	private Context mContext;
	private static final long[] pattern = { 1000, 500 };
	private Vibrator mVibrator;
	private LocationService locationService;
	private DataBaseService dbService;

	public UrgencyProcessor(Context context) {
		L.i(context.toString());
		locationService = HelpApplication.getInstance().getLocationService();
		mCounter = PressCounter.getInstance();
		mContext = context;
		mVibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		dbService = new DataBaseService(mContext);
	}

	/***
	 * 发送短信msg到phoneNumber
	 * 
	 * @param phoneNumber
	 * @param msg
	 */
	private void sendMsg(String phoneNumber, String msg) {
		Log.i("sendMsg", phoneNumber+":"+msg);
		SmsManager smsManager = SmsManager.getDefault();
		List<String> divideContents = smsManager.divideMessage(msg);
		Intent sentIntent = new Intent(InnerRecevier.SEND_SMS_ACTION);
		PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0,
				sentIntent, 0);
		Intent deliverIntent = new Intent(InnerRecevier.DELIVERED_SMS_ACTION);
		PendingIntent deliverPI = PendingIntent.getBroadcast(mContext, 0,
				deliverIntent, 0);
		for (String text : divideContents) {
			smsManager.sendTextMessage(phoneNumber, null, text, sentPI,
					deliverPI);
		}
	}

	@Override
	public void powerButtonPressed() {
		mCounter.add(System.currentTimeMillis());
		if (mCounter.check()) {
			mVibrator.vibrate(pattern, -1);// 不重复震动
			// sendMsgs();
			locationService.setListener(this);
			locationService.startLocate();
		}
	}

	/**
	 * 发送短信
	 */
	private void sendMsgs() {
		Cursor cursor = dbService.query(Table.TABLE);
		if (cursor.getCount() == 0)
			return;
		while (cursor.moveToNext()) {
			String phone = getCursorString(cursor, Table.PHONE);
			String msg = getCursorString(cursor, Table.MSG);
			sendMsg(phone.trim(), msg);
		}
		cursor.close();
	}

	@Override
	public void sendMsgOk() {
		long[] pattern = { 3000, 500 };
		mVibrator.vibrate(pattern, -1);
	}

	/**
	 * 发送短信和位置信息
	 */
	@Override
	public void processLocation(String location) {
		Cursor cursor = dbService.query(Table.TABLE);
		if (cursor.getCount() == 0)
			return;
		while (cursor.moveToNext()) {
			String phone = getCursorString(cursor, Table.PHONE);
			String msg = getCursorString(cursor, Table.MSG);
			sendMsg(phone.trim(), msg + " #我的位置信息：" + location);
		}
		cursor.close();
		if (locationService != null)// 停止定位
			locationService.stopLocate();
	}

	/***
	 * 封装游标的奇葩方法
	 * 
	 * @param cursor
	 * @param cloumn
	 * @return
	 */
	protected String getCursorString(Cursor cursor, String cloumn) {
		return cursor.getString(cursor.getColumnIndex(cloumn));
	}

}
