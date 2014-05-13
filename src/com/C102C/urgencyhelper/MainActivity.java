package com.C102C.urgencyhelper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.C102C.urgencyhelper.powerbuttonserver.PowerButtonWacther;
import com.C102C.util.DataBaseService;
import com.C102C.util.ThreeCloumAdapter;
import com.C102C.util.UrgencyFriend.Table;

public class MainActivity extends Activity implements OnClickListener {
	private static final int SAVE_CONTACT = 12;
	private static final int GET_FROM_DB = 13;
	private LinearLayout addContactLayout;
	private EditText nameEt, phoneEt, msgEt;
	private TextView hintTv;
	private DataBaseService dbService;
	private ThreeCloumAdapter adapter;
	private ListView listview;
	// 联系人数据
	private List<String[]> contacts;
	private static String[] title = { "姓名", "电话", "预设信息" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dbService = new DataBaseService(this);
		findIds();
		contacts = new ArrayList<String[]>();
		adapter = new ThreeCloumAdapter(this, contacts);
		listview.setAdapter(adapter);
		setListViewLisener();
		handler.obtainMessage(GET_FROM_DB).sendToTarget();
		startService(new Intent(this, PowerButtonWacther.class));
	}

	private void setListViewLisener() {
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0)// 标题不响应
					return false;
				String[] line = (String[]) parent.getItemAtPosition(position);
				dialog(line);
				return true;
			}
		});
	}

	protected void dialog(final String[] line) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确认删除" + line[0] + "？");
		builder.setTitle("删除提示");

		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					long index = dbService.delete(Table.TABLE,
							DataBaseService.SYS_ID, line[3]);
					if (index != 0) {
						dialog.cancel();
						Toast.makeText(MainActivity.this, "刪除成功",
								Toast.LENGTH_LONG).show();
						handler.obtainMessage(GET_FROM_DB).sendToTarget();

					}
				} catch (Exception e) {
					e.printStackTrace();

				}

			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	private void initFromDb() {
		Cursor cursor = dbService.query(Table.TABLE);
		contacts.clear();
		if (cursor.getCount() == 0) {// 没有数据
			hintTv.setVisibility(View.VISIBLE);
		} else {
			contacts.add(title);
			hintTv.setVisibility(View.GONE);
			while (cursor.moveToNext()) {
				String[] line = { getCursorString(cursor, Table.NAME),
						getCursorString(cursor, Table.PHONE),
						getCursorString(cursor, Table.MSG),
						getCursorString(cursor, DataBaseService.SYS_ID) };
				contacts.add(line);
			}
		}
		adapter.notifyDataSetChanged();
		// 关闭连接
		cursor.close();
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

	private void findIds() {
		addContactLayout = (LinearLayout) findViewById(R.id.add_contact_layout);
		nameEt = (EditText) findViewById(R.id.contact_name_et);
		phoneEt = (EditText) findViewById(R.id.contact_phone_et);
		msgEt = (EditText) findViewById(R.id.contact_msg_et);
		hintTv = (TextView) findViewById(R.id.add_contact_hint);
		listview = (ListView) findViewById(R.id.listview);
		Button button = (Button) findViewById(R.id.saveBtn);
		button.setOnClickListener(this);
	}

	/**
	 * 保存联系信息
	 */
	private void saveContact() {
		String name = nameEt.getText().toString();
		String phone = phoneEt.getText().toString();
		if (phone == null || phone.length() < 3) {
			Toast.makeText(this, "电话号码不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		String msg = msgEt.getText().toString();
		ContentValues cv = new ContentValues();
		cv.put(Table.NAME, name);
		cv.put(Table.PHONE, phone);
		cv.put(Table.MSG, msg);
		long index = dbService.insert(Table.TABLE, cv);
		if (index != -1) {
			Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
			View focusView = addContactLayout.getFocusedChild();
			if (focusView != null) {// 有聚焦时,即输入法打开时
				// 影藏输入法
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
			}
			addContactLayout.setVisibility(View.GONE);

			handler.obtainMessage(GET_FROM_DB).sendToTarget();
		} else
			Toast.makeText(this, "保存失败", Toast.LENGTH_LONG).show();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SAVE_CONTACT:
				saveContact();
				break;
			case GET_FROM_DB:
				initFromDb();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 响应点击按钮
	 */
	public void responseClick(View view) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.saveBtn) {
			handler.obtainMessage(SAVE_CONTACT).sendToTarget();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		// menu.add(0, 1, 1, R.string.action_settings);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.action_about:
			intent = new Intent(this, About.class);
			startActivity(intent);
			break;
		case R.id.action_add_contact:
			// 点击显示和隐藏新建按钮
			if (addContactLayout.getVisibility() == View.GONE)
				addContactLayout.setVisibility(View.VISIBLE);
			else
				addContactLayout.setVisibility(View.GONE);
			break;
		case R.id.action_setting:
			intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		startService(new Intent(this, PowerButtonWacther.class));
	}

}
