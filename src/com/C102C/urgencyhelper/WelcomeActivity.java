package com.C102C.urgencyhelper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.C102C.util.SharedPreferenceUtil;

public class WelcomeActivity extends Activity {
	private CheckBox cb;
	private SharedPreferenceUtil sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		ActionBar ab = getActionBar();
		if (ab != null) {// ���ñ���
			ab.setTitle("��ӭ");
		}
		cb = (CheckBox) findViewById(R.id.remenber_cb);
		sp = new SharedPreferenceUtil(this);
		findViewById(R.id.declaration_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.pwsswd_layout).setVisibility(View.GONE);
		if (sp.isRemenbered())
			jump();

	}

	private void jump() {
		if (!sp.getPassWordState())// û����������
			jump2main();
		else {
			findViewById(R.id.declaration_layout).setVisibility(View.GONE);
			findViewById(R.id.pwsswd_layout).setVisibility(View.VISIBLE);
		}
	}

	private void jump2main() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private int count = 0;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.agreeBtn:
			if (cb.isChecked())
				sp.saveRemenbed(true);
			else
				sp.saveRemenbed(false);
			jump();
			break;
		case R.id.disagreeBtn:
			finish();
			break;
		case R.id.ok_passwd_btn:
			String passwd = ((EditText) findViewById(R.id.input_passwd_et))
					.getText().toString();
			if (passwd != null && passwd.equals(sp.getPasswd())) {
				jump2main();
				count = 0;
			} else {
				if (count > 5)
					Toast.makeText(this, "�������룬����ѡ�����ֻ�ϵͳ�������������!",
							Toast.LENGTH_LONG).show();
				else
					Toast.makeText(this, "���벻��ȷ!", Toast.LENGTH_LONG).show();
				count++;

			}
		}

	}
}
