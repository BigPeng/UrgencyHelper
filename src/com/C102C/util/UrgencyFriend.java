package com.C102C.util;

import java.util.HashMap;
import java.util.Map;

public class UrgencyFriend {
	public static class Table {
		private Table() {
		}

		public static final String TABLE = "friendTable";
		public static final String NAME = "name";
		public static final String PHONE = "phone";
		public static final String MSG = "msg";

		public static Map<String, String> friendTable() {
			Map<String, String> para = new HashMap<String, String>();
			para.put(DataBaseService.TABLE_NAME, TABLE);
			para.put(NAME, "text");
			para.put(PHONE, "varchar(15)");
			para.put(MSG, "text");
			return para;
		}
	}

	private String name;
	private String phone;
	private String msg;

	private UrgencyFriend() {

	}

	public UrgencyFriend(String name, String phone, String msg) {
		this.name = name;
		this.phone = phone;
		this.msg = msg;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append("\t");
		sb.append(phone);
		sb.append("\t");
		sb.append(msg);
		return sb.toString();
	}

	public static UrgencyFriend makeUrgencyFriend(String str) {
		str = str.trim();
		String[] data = str.split("\t");
		if (data.length != 3)
			return null;
		UrgencyFriend friend = new UrgencyFriend();
		friend.setName(data[0]);
		friend.setPhone(data[1]);
		friend.setMsg(data[2]);
		return friend;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof UrgencyFriend) {
			return this.phone.equals(((UrgencyFriend) other).phone);
		}
		return false;
	}
}
