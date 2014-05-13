package com.C102C.urgencyhelper;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.C102C.urgencyhelper.powerbuttonserver.PressCounter;
import com.C102C.util.SharedPreferenceUtil;

/**
 * 设置界面
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-5-7 下午6:51:24
 */
public class SettingActivity extends Activity {
	// 触发次数
	private EditText triggerTimesEt;
	// 触发有效时间
	private EditText triggerTimeEt;
	private EditText passwordEt;
	private CheckBox cb;
	private SharedPreferenceUtil sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ActionBar ab = getActionBar();
		if (ab != null) {// 设置标题
			ab.setTitle("设置");
		}
		sp = new SharedPreferenceUtil(this);
		triggerTimesEt = (EditText) findViewById(R.id.triggerTimesEt);
		triggerTimeEt = (EditText) findViewById(R.id.triggerTimeEt);
		passwordEt = (EditText) findViewById(R.id.passwordEt);
		cb = (CheckBox) findViewById(R.id.use_password_cb);
		if (sp.getPassWordState()) {// 启用了密码
			cb.setChecked(true);
			passwordEt.setVisibility(View.VISIBLE);
			passwordEt.setText(sp.getPasswd());
		} else {
			cb.setChecked(false);
			passwordEt.setVisibility(View.INVISIBLE);
		}
		triggerTimesEt.setText(sp.getTriggerTimes() + "");
		triggerTimeEt.setText(sp.getTriggerTime() + "");

	}

	// 启用密码是否被点击
	private boolean isCbClicked = false;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.saveSettingBtn:
			saveSetting();
			break;
		case R.id.returnBtn:
			finish();
			break;
		case R.id.use_password_cb:
			isCbClicked = true;
			if (cb.isChecked()) {
				passwordEt.setVisibility(View.VISIBLE);
				String passwd = sp.getPasswd();
				if (passwd != null)
					passwordEt.setText(passwd);
			} else
				passwordEt.setVisibility(View.INVISIBLE);
			break;
		}
	}

	/**
	 * 保存设置信息
	 */
	private void saveSetting() {
		String triggerTimes = triggerTimesEt.getText().toString();
		String triggerTime = triggerTimeEt.getText().toString();
		int times = 8;
		int time = 10;
		try {
			times = Integer.parseInt(triggerTimes);
			time = Integer.parseInt(triggerTime);
		} catch (Exception e) {
			Toast.makeText(this, "触发次数或时间设置不正确!", Toast.LENGTH_LONG).show();
			return;
		}
		String passwd;
		if (cb.isChecked()) {
			passwd = passwordEt.getText().toString();
			if (passwd == null || passwd.length() < 2) {
				Toast.makeText(this, "密码太短!", Toast.LENGTH_LONG).show();
				return;
			}
			// 启用密码
			sp.setPassWordState(true);
			// 保存密码
			sp.savePasswd(passwd);
		} else {
			if (isCbClicked)// 不启用密码
				Toast.makeText(this, "密码已经取消!", Toast.LENGTH_LONG).show();
			// 不启用密码
			sp.setPassWordState(false);
		}
		sp.saveTriggerTimes(times);
		sp.saveTriggerTime(time);
		PressCounter.getInstance().reset(times, time);
		finish();
	}

}
