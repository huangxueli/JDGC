package com.wzkcy.jdgc.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ab.view.sliding.AbSlidingButton;
import com.wzkcy.jdgc.R;
import com.wzkcy.jdgc.fragment.MenuItem;

public class MenuAdapter extends BaseExpandableListAdapter {

	private Context mContext = null;
	private ArrayList<MenuItem> mGroups;
	private ArrayList<ArrayList<MenuItem>> mChilds;
	private LayoutInflater mInflater = null;

	public MenuAdapter(Context context, ArrayList<MenuItem> groups, ArrayList<ArrayList<MenuItem>> childs ) {
		mContext = context;
		mGroups = groups;
		mChilds = childs;
		mInflater = LayoutInflater.from(mContext);
	}

	public Object getChild(int groupPosition, int childPosition) {
		return mChilds.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	private class ViewHolder {
		private TextView mGroupName;
		private Button mChildItem;
		private AbSlidingButton mCheckBox;
	}
	
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, 
			View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		MenuItem m = mChilds.get(groupPosition).get(childPosition);
		if (convertView == null) {
			holder = new ViewHolder();
			if(m.mButtonType == MenuItem.ButtonType.NormalButton){
				convertView = mInflater.inflate(R.layout.listitem_leftmenu_normal_temp, null);
			}
			holder.mChildItem = (Button) convertView.findViewById(R.id.menu_list_child);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		convertView.setBackgroundResource(R.drawable.button_selector_bg_child_item);
		Drawable drawable = mContext.getResources().getDrawable(m.mImgResID);
//		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//		holder.mChildItem.setCompoundDrawables(drawable, null, null, null);
		holder.mChildItem.setBackground(drawable);
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return mChilds.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		return mGroups.get(groupPosition);
	}

	public int getGroupCount() {
		return mGroups.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		MenuItem item = mGroups.get(groupPosition);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.expandlist_group_menu, null);
			holder = new ViewHolder();
			holder.mGroupName = (TextView) convertView.findViewById(R.id.desktop_list_group_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setBackgroundResource(R.drawable.button_selector_bg_group_item);
		if(item.mImgResID!=-1){
			Drawable drawable = mContext.getResources().getDrawable(item.mImgResID);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			holder.mGroupName.setCompoundDrawables(drawable, null, null, null);
		}
		holder.mGroupName.setText(item.mText);
		return convertView;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
