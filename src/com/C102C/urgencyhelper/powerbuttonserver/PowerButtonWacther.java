package com.C102C.urgencyhelper.powerbuttonserver;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import com.C102C.util.L;

public class PowerButtonWacther extends Service {
	private static final String TAG = "PowerButtonWacther";

	private OnPowerButtonPressedListener mListener = null;
	private Context mContext;
	private InnerRecevier mRecevier;
	private IntentFilter mFilter;
	private UrgencyProcessor processor;

	@Override
	public void onCreate() {
		L.i(TAG, "PowerButtonWacther.onCreate()");
		super.onCreate();
		mContext = this;
		processor = new UrgencyProcessor(mContext);
		setListener(processor);
		mRecevier = new InnerRecevier();
		mFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);// ��Ļ��
		mFilter.addAction(Intent.ACTION_SCREEN_OFF);
		mFilter.addAction(InnerRecevier.SEND_SMS_ACTION);
		mFilter.addAction(InnerRecevier.DELIVERED_SMS_ACTION);
		mFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
		mFilter.addAction(Intent.ACTION_TIME_TICK);// ʱ�ӹ㲥�����ڻ���Ӧ��
		startWatch();
	}

	@Override
	public void onDestroy() {
		stopWatch();
		setListener(null);
		super.onDestroy();
		startService(new Intent(this, PowerButtonWacther.class));
	}

	/**
	 * ��ʼ���ӵ�Դ��
	 */
	private void startWatch() {
		if (mListener != null)
			mContext.registerReceiver(mRecevier, mFilter);
	}

	/***
	 * ֹͣ����
	 */
	private void stopWatch() {
		if (mListener != null)
			mContext.unregisterReceiver(mRecevier);
	}

	/***
	 * ���ü�����
	 * 
	 * @param listener
	 */
	public void setListener(OnPowerButtonPressedListener listener) {
		mListener = listener;
	}

	/***
	 * ������
	 * 
	 * @author jiqunpeng
	 * 
	 *         ����ʱ�䣺2014-3-7 ����3:44:48
	 */
	public interface OnPowerButtonPressedListener {

		public void powerButtonPressed();

		public void sendMsgOk();
	}

	/**
	 * �㲥������
	 */
	public class InnerRecevier extends BroadcastReceiver {
		// ���Ͷ��Ŷ���
		public static final String SEND_SMS_ACTION = "send_sms_action";
		// �Է����ն���
		public static final String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			L.i(action);
			if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				mListener.powerButtonPressed();
			} else if (Intent.ACTION_SCREEN_ON.equals(action)) {
				mListener.powerButtonPressed();
			} else if (SEND_SMS_ACTION.equals(action)) {
				int code = getResultCode();
				switch (code) {
				case Activity.RESULT_OK:
					L.i("�ɹ�����");
					mListener.sendMsgOk();
				default:
					L.i("" + code);
				}
			} else if (DELIVERED_SMS_ACTION.equals(action)) {
				L.i("�������Ѿ��յ�");
			} else if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
				L.i("�����������");
				Toast.makeText(context, "�����ɹ���", Toast.LENGTH_LONG).show();
				Intent startIntent = new Intent(context,
						PowerButtonWacther.class);
				startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startService(startIntent);
			} else if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
				if (!checkRuning(context)) {
					startService(new Intent(context, PowerButtonWacther.class));
				}
			}
		}

		private boolean checkRuning(Context context) {
			ActivityManager manager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			for (RunningServiceInfo service : manager
					.getRunningServices(Integer.MAX_VALUE))
				if ("com.C102C.urgencyhelper.PowerButtonWacther"
						.equals(service.service.getClassName()))
					return true;

			return false;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
