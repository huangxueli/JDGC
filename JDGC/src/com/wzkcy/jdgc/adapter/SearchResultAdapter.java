package com.wzkcy.jdgc.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wzkcy.jdgc.MyApplication;
import com.wzkcy.jdgc.R;
import com.wzkcy.jdgc.bean.Light;

public class SearchResultAdapter extends BaseAdapter {
	
	private ArrayList<Light> mList = new ArrayList<Light>();
	
	public interface OnRouteLoadListener {
		public abstract void doAfterRouteLoaded(int id);
	}

	public SearchResultAdapter(ArrayList<Light> list) {
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public final class ViewHolder {
		TextView name;
		CheckBox symbol;
		Light light;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = MyApplication.Inflater.inflate(R.layout.listitem_searchresult, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.nametext);
			holder.symbol = (CheckBox) convertView.findViewById(R.id.online);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Light light = mList.get(position);
		holder.light = light; 

		holder.name.setText(light.getName());
		if(light.isOn_guard()){
			holder.symbol.setBackgroundResource(R.drawable.line_on);
		}else{
			holder.symbol.setBackgroundResource(R.drawable.line_off);
		}
		return convertView;
	}

}
