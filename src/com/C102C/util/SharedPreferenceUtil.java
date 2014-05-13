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
	 * 记住登录
	 */
	public void saveRemenbed(Boolean value) {
		Editor editor = sp.edit();
		editor.putBoolean("saveRemenbed", value);
		editor.commit();
	}

	/**
	 * 记住登录
	 */
	public boolean isRemenbered() {
		return sp.getBoolean("saveRemenbed", false);
	}

	/**
	 * 设置是否使用密码
	 * 
	 * @return
	 */
	public void setPassWordState(Boolean value) {
		Editor editor = sp.edit();
		editor.putBoolean("PassWordState", value);
		editor.commit();
	}

	/**
	 * 是否使用密码
	 * 
	 * @return
	 */
	public boolean getPassWordState() {
		return sp.getBoolean("PassWordState", false);
	}

	/**
	 * 保存促发时间
	 * 
	 * @param time
	 */
	public void saveTriggerTime(int time) {
		Editor editor = sp.edit();
		editor.putInt("TriggerTime", time);
		editor.commit();
	}

	/**
	 * 获取触发有效时间
	 * 
	 * @return
	 */
	public int getTriggerTime() {
		return sp.getInt("TriggerTime", 10);
	}

	/**
	 * 保存触发所需的屏幕开关次数
	 * 
	 * @param time
	 */
	public void saveTriggerTimes(int times) {
		Editor editor = sp.edit();
		editor.putInt("TriggerTimes", times);
		editor.commit();
	}

	/**
	 * 获取触发所需的屏幕开关次数
	 * 
	 * @return
	 */
	public int getTriggerTimes() {
		return sp.getInt("TriggerTimes", 8);

	}

	/**
	 * 保存开启密码,设置为null时表示清空
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
	 * 获取开启密码
	 * 
	 * @return
	 */
	public String getPasswd() {
		return sp.getString("passwd", null);

	}
}
