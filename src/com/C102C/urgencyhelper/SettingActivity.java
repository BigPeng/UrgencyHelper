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
 * ���ý���
 * 
 * @author jiqunpeng
 * 
 *         ����ʱ�䣺2014-5-7 ����6:51:24
 */
public class SettingActivity extends Activity {
	// ��������
	private EditText triggerTimesEt;
	// ������Чʱ��
	private EditText triggerTimeEt;
	private EditText passwordEt;
	private CheckBox cb;
	private SharedPreferenceUtil sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ActionBar ab = getActionBar();
		if (ab != null) {// ���ñ���
			ab.setTitle("����");
		}
		sp = new SharedPreferenceUtil(this);
		triggerTimesEt = (EditText) findViewById(R.id.triggerTimesEt);
		triggerTimeEt = (EditText) findViewById(R.id.triggerTimeEt);
		passwordEt = (EditText) findViewById(R.id.passwordEt);
		cb = (CheckBox) findViewById(R.id.use_password_cb);
		if (sp.getPassWordState()) {// ����������
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

	// ���������Ƿ񱻵��
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
	 * ����������Ϣ
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
			Toast.makeText(this, "����������ʱ�����ò���ȷ!", Toast.LENGTH_LONG).show();
			return;
		}
		String passwd;
		if (cb.isChecked()) {
			passwd = passwordEt.getText().toString();
			if (passwd == null || passwd.length() < 2) {
				Toast.makeText(this, "����̫��!", Toast.LENGTH_LONG).show();
				return;
			}
			// ��������
			sp.setPassWordState(true);
			// ��������
			sp.savePasswd(passwd);
		} else {
			if (isCbClicked)// ����������
				Toast.makeText(this, "�����Ѿ�ȡ��!", Toast.LENGTH_LONG).show();
			// ����������
			sp.setPassWordState(false);
		}
		sp.saveTriggerTimes(times);
		sp.saveTriggerTime(time);
		PressCounter.getInstance().reset(times, time);
		finish();
	}

}
