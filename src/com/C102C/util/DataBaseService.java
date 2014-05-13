package com.C102C.util;

import java.io.File;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.C102C.util.UrgencyFriend.Table;

public class DataBaseService extends SQLiteOpenHelper {
	private static final String DATABASENAME = "UrgencyFriend.db";
	private static final int DATABASVERSION = 1;
	private static final String TAG = "DataOpenHelper";
	public static final String SYS_ID = "sysId";
	public static final String TABLE_NAME = "TABLE_NAME";

	public DataBaseService(Context context) {
		super(context, DATABASENAME, null, DATABASVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createSql(Table.friendTable()));

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	/***
	 * 
	 * @param table
	 * @param values
	 * @return ������к�
	 */
	public long insert(String table, ContentValues values) {
		L.i(TAG, "(insert)" + table + ": " + values.toString());
		SQLiteDatabase insertDb = getWritableDatabase();
		long rawId = insertDb.insert(table, null, values);
		insertDb.close();
		L.i(TAG, "(inserted) rawId:" + rawId);
		return rawId;
	}

	/***
	 * ɾ��table����cloumn��ֵΪvalues�ļ�¼
	 * 
	 * @param table
	 * @param cloumn
	 * @param value
	 * @return
	 */
	public int delete(String table, String cloumn, String value) {
		L.i(TAG, "(delete)" + table + ": " + cloumn + ":" + value);
		SQLiteDatabase deleteDb = getWritableDatabase();
		int raws = deleteDb.delete(table, cloumn + " = ?",
				new String[] { value });
		deleteDb.close();
		L.i(TAG, "(deleted) raws:" + raws);
		return raws;
	}

	/**
	 * ��ѯ���е���������
	 * 
	 * @param table
	 * @return
	 */
	public Cursor query(String table) {
		L.i(TAG, "(query)" + table);
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(table);
		SQLiteDatabase queryDb = getReadableDatabase();
		Cursor cursor = queryDb.rawQuery(sql.toString(), new String[] {});
		return cursor;
	}

	/**
	 * ��map�н����������sql
	 * 
	 * @param tableDesc
	 * @return
	 */
	private String createSql(Map<String, String> tableDesc) {
		StringBuilder builder = new StringBuilder();
		builder.append("create table ");
		builder.append(tableDesc.get(TABLE_NAME));
		tableDesc.remove(TABLE_NAME);// ɾ������
		builder.append("(");
		builder.append(SYS_ID);
		builder.append(" integer PRIMARY KEY autoincrement,");
		for (Map.Entry<String, String> entry : tableDesc.entrySet()) {
			builder.append(entry.getKey());
			builder.append(" ");
			builder.append(entry.getValue());
			builder.append(",");
		}
		String sql = builder.toString();
		sql = sql.substring(0, sql.length() - 1);// ɾ�����һ������
		return sql + ")";

	}

	/***
	 * �h���������ݿ�
	 */
	@SuppressLint("NewApi")
	public static void deleteDb() {
		boolean state = SQLiteDatabase.deleteDatabase(new File(DATABASENAME));
		L.i(TAG, "deleteDb:" + state);

	}
}
