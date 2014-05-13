package com.C102C.urgencyhelper;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class About extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		ActionBar ab = getActionBar();
		if (ab != null) {// 设置标题
			ab.setTitle("关于");
		}
	}

	public void returnBack(View v) {
		finish();
	}
}
