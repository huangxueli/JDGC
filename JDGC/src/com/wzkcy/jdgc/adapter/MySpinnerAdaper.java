package com.wzkcy.jdgc.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wzkcy.jdgc.MyApplication;
import com.wzkcy.jdgc.R;

public class MySpinnerAdaper extends ArrayAdapter<String> {
	
	private List<String> mList;

	public MySpinnerAdaper(Context context, int resource, List<String> list) {
		super(context, resource, list);
		mList = list;
	}
	public void setData(List<String> list){
		mList = list;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public String getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		private TextView text;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = MyApplication.Inflater.inflate(R.layout.simple_spinner_item, parent, false);
			holder = new ViewHolder();
//			holder.text = (TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text.setText((String)getItem(position));
		return convertView;
	}

}
