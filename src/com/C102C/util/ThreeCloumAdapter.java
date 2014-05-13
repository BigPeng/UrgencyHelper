package com.C102C.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.C102C.urgencyhelper.R;

/**
 * 每行三列文本的是适配器
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2014-5-7 下午7:56:55
 */
public class ThreeCloumAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<String[]> datas;

	public ThreeCloumAdapter(Context context, List<String[]> datas) {
		layoutInflater = LayoutInflater.from(context);
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String[] line = datas.get(position);
		Holder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.adapter_item, null);
			TextView tv1 = (TextView) convertView.findViewById(R.id.item_tv1);
			TextView tv2 = (TextView) convertView.findViewById(R.id.item_tv2);
			TextView tv3 = (TextView) convertView.findViewById(R.id.item_tv3);
			holder = new Holder(tv1, tv2, tv3);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.tv1.setText(line[0]);
		holder.tv2.setText(line[1]);
		holder.tv3.setText(line[2]);
		return convertView;
	}

	class Holder {
		public final TextView tv1;
		public final TextView tv2;
		public final TextView tv3;

		public Holder(TextView tv1, TextView tv2, TextView tv3) {
			this.tv1 = tv1;
			this.tv2 = tv2;
			this.tv3 = tv3;

		}
	}
}
