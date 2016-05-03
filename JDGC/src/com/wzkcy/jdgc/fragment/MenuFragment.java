package com.wzkcy.jdgc.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wzkcy.jdgc.MainActivity;
import com.wzkcy.jdgc.R;
import com.wzkcy.jdgc.adapter.MenuAdapter;
import com.wzkcy.jdgc.setting.Constants;
import com.wzkcy.jdgc.setting.Util;

public class MenuFragment extends Fragment {

	public final static String TAG = "MenuFragment";

	private MainActivity mMainActivity;
	private MainFragment mMainFragment;

	private ArrayList<MenuItem> mGroups;
	private ArrayList<ArrayList<MenuItem>> mChilds;
	private MenuAdapter mAdapter;

	private ImageButton mPhotoBtn;
	private TextView mUsername;
	private TextView mLonTextView;
	private TextView mLatTextView;
	private ExpandableListView mMenuListView;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainActivity = (MainActivity) getActivity();
		mMainFragment = mMainActivity.getMainFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu, null);

		mUsername = (TextView) view.findViewById(R.id.user_name);
		mUsername.setText(Constants.UserName);
		mPhotoBtn = (ImageButton) view.findViewById(R.id.user_photo);
		mPhotoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		mLonTextView = (TextView) view.findViewById(R.id.longitude);
		mLatTextView = (TextView) view.findViewById(R.id.latitude);

		mMenuListView = (ExpandableListView) view.findViewById(R.id.menu_list);

		mGroups = new ArrayList<MenuItem>();
		mChilds = new ArrayList<ArrayList<MenuItem>>();
		ArrayList<MenuItem> mChild1 = new ArrayList<MenuItem>();
		ArrayList<MenuItem> mChild2 = new ArrayList<MenuItem>();
		ArrayList<MenuItem> mChild3 = new ArrayList<MenuItem>();
		mChilds.add(mChild1);
		mChilds.add(mChild2);
		mChilds.add(mChild3);

		mAdapter = new MenuAdapter(getActivity(), mGroups, mChilds);
		mMenuListView.setAdapter(mAdapter);
		mMenuListView.setOnGroupClickListener(new OnGroupClickListener() {
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
		mMenuListView.setOnChildClickListener(new OnChildClickListener() {

			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				switch (groupPosition) {
				case 0:
					switch (childPosition) {
					case 0: // 刷新兴趣点
						mMainFragment.clearMap();
						mMainFragment.startLoadData();
						break;
					case 1: // 数据统计
						StatisticsFragment mStatisticsFragment = new StatisticsFragment(mMainFragment);
						FragmentTransaction transaction = getFragmentManager().beginTransaction();
						transaction.hide(mMainFragment);
						transaction.add(R.id.container, mStatisticsFragment);
						transaction.addToBackStack(StatisticsFragment.TAG);
						transaction.commit();
						mMainActivity.replaceLogoView(false);
						mMainActivity.showContent();
						break;
					case 2: // 路径分析
						mMainFragment.setLJQueryInLongPress(true);
						mMainFragment.setDMQueryInLongPress(false);
						Util.Toast("长按查询路径");
						mMainActivity.showContent();
						break;
					case 3: // 地名查询
						mMainFragment.setLJQueryInLongPress(false);
						mMainFragment.setDMQueryInLongPress(true);
						Util.Toast("长按查询地名");
						mMainActivity.showContent();
						break;
					case 4: // 空间查询
						
						break;
					}
					break;
				case 1:
					switch (childPosition) {
					case 2:
						break;
					}
					break;
				}
				return true;
			}
		});

		initMenuItem(mChild1, mChild2, mChild3); // 初始化菜单列表数据

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// disconnect();
	}

	public void initMenuItem(ArrayList<MenuItem> mChild1,
			ArrayList<MenuItem> mChild2, ArrayList<MenuItem> mChild3) {
		mGroups.clear();
		mChild1.clear();
		mChild2.clear();

		mGroups.add(new MenuItem("", -1));
		MenuItem item = null;

		item = new MenuItem("", R.drawable.button_selector_feature_refresh);
		mChild1.add(item);
		item = new MenuItem("", R.drawable.button_selector_feature_data);
		mChild1.add(item);
		item = new MenuItem("", R.drawable.button_selector_feature_lj);
		mChild1.add(item);
		item = new MenuItem("", R.drawable.button_selector_feature_dm);
		mChild1.add(item);
//		item = new MenuItem("", R.drawable.button_selector_feature_geosearch);
//		mChild1.add(item);
		

//		item = new MenuItem("使用离线地图", R.drawable.feature_localmap,
//		ButtonType.SliderButton, mUseLocalMap, new OnCheckedChangeListener(){
//		
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean
//					isChecked) {
//				mUseLocalMap = isChecked;
//				mTiledLayerManager.switchMap(mTiledLayerManager.getMode());
//				mCache.write(Cache.KEY_CODE_LOCALMAP, mUseLocalMap);
//			}
//		});
//		mChild2.add(item);

		mAdapter.notifyDataSetChanged();
		for (int i = 0; i < mGroups.size(); i++) {
			mMenuListView.expandGroup(i);
		}
	}

	// 刷新当前经纬度
	public void setLongitudeLatitude(double x, double y) {
		String lon = String.valueOf(x);
		String lat = String.valueOf(y);
		mLonTextView.setText(lon);
		mLatTextView.setText(lat);
	}

}
