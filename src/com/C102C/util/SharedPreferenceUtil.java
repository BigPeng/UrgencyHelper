package com.C102C.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceUtil {
	private SharedPreferences sp;	

	public SharedPreferenceUtil(Context context) {
		sp = context.getSharedPreferences("UrgencyFriends.log",
				Context.MODE_PRIVATE);
		
	}

	/**
	 * ��ס��¼
	 */
	public void saveRemenbed(Boolean value) {
		Editor editor = sp.edit();
		editor.putBoolean("saveRemenbed", value);
		editor.commit();
	}

	/**
	 * ��ס��¼
	 */
	public boolean isRemenbered() {
		return sp.getBoolean("saveRemenbed", false);
	}

	/**
	 * �����Ƿ�ʹ������
	 * 
	 * @return
	 */
	public void setPassWordState(Boolean value) {
		Editor editor = sp.edit();
		editor.putBoolean("PassWordState", value);
		editor.commit();
	}

	/**
	 * �Ƿ�ʹ������
	 * 
	 * @return
	 */
	public boolean getPassWordState() {
		return sp.getBoolean("PassWordState", false);
	}

	/**
	 * ����ٷ�ʱ��
	 * 
	 * @param time
	 */
	public void saveTriggerTime(int time) {
		Editor editor = sp.edit();
		editor.putInt("TriggerTime", time);
		editor.commit();
	}

	/**
	 * ��ȡ������Чʱ��
	 * 
	 * @return
	 */
	public int getTriggerTime() {
		return sp.getInt("TriggerTime", 10);
	}

	/**
	 * ���津���������Ļ���ش���
	 * 
	 * @param time
	 */
	public void saveTriggerTimes(int times) {
		Editor editor = sp.edit();
		editor.putInt("TriggerTimes", times);
		editor.commit();
	}

	/**
	 * ��ȡ�����������Ļ���ش���
	 * 
	 * @return
	 */
	public int getTriggerTimes() {
		return sp.getInt("TriggerTimes", 8);

	}

	/**
	 * ���濪������,����Ϊnullʱ��ʾ���
	 * 
	 * @param passwd
	 */
	public void savePasswd(String passwd) {
		Editor editor = sp.edit();
		if (passwd == null)
			editor.remove("passwd");
		else
			editor.putString("passwd", passwd);
		editor.commit();
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public String getPasswd() {
		return sp.getString("passwd", null);

	}
}
