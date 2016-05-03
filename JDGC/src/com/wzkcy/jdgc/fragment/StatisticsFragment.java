package com.wzkcy.jdgc.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wzkcy.jdgc.MainActivity;
import com.wzkcy.jdgc.R;
import com.wzkcy.jdgc.adapter.SearchResultAdapter;
import com.wzkcy.jdgc.adapter.StatisticsAdapter;
import com.wzkcy.jdgc.bean.JG;
import com.wzkcy.jdgc.bean.Light;
import com.wzkcy.jdgc.bean.Policeman;
import com.wzkcy.jdgc.bean.QY;
import com.wzkcy.jdgc.bean.Station;

/** 统计界面 **/
public class StatisticsFragment extends Fragment {

	public static final String TAG = "StatisticsFragment";

	private MainActivity mMainActivity;
	private MainFragment mMainFragment;
	private ListView mLightListView;
	private StatisticsAdapter mLightAdapter;

	private ListView mDetialListView;
	private SearchResultAdapter mDetialAdapter;
	private ArrayList<Light> mLightList;

	private boolean mIsSecondFrom; // 当前是否显示二级表格
	private boolean mIsDetialShown; // 当前是否显示具体列表

	public StatisticsFragment(MainFragment fragment) {
		mMainFragment = fragment;
		mMainActivity = (MainActivity) fragment.getActivity();
		mMainActivity.setStatisticsFragment(this);
	}

	private List<Policeman> mPolicemanTList; // 温州市所有警员
	private List<Policeman> mPolicemanOList; // 温州市在线警员
	private List<Policeman> mPolicemanFList; // 温州市离线警员
	private List<Station> mNormalStationTList; // 温州市所有联防岗亭
	private List<Station> mNormalStationOList; // 温州市在线联防岗亭
	private List<Station> mNormalStationFList; // 温州市离线联防岗亭
	private List<Station> mPoliceStationTList; // 温州市所有民警岗亭
	private List<Station> mPoliceStationOList; // 温州市在线民警岗亭
	private List<Station> mPoliceStationFList; // 温州市离线民警岗亭
	// <区域名称，机构名称集合>
	private Map<String, List<JG>> mJGMap;
	// <区域名称，数据集合>
	private Map<String, List<Policeman>> mQYPoliceTMap = new HashMap<String, List<Policeman>>(); // 区域-警员总计
	private Map<String, List<Policeman>> mQYPoliceOMap = new HashMap<String, List<Policeman>>(); // 区域-警员在线
	private Map<String, List<Policeman>> mQYPoliceFMap = new HashMap<String, List<Policeman>>(); // 区域-警员离线
	private Map<String, List<Station>> mQYNormalStationTMap = new HashMap<String, List<Station>>(); // 区域-联防岗亭总计
	private Map<String, List<Station>> mQYNormalStationOMap = new HashMap<String, List<Station>>(); // 区域-联防岗亭在线
	private Map<String, List<Station>> mQYNormalStationFMap = new HashMap<String, List<Station>>(); // 区域-联防岗亭离线
	private Map<String, List<Station>> mQYPoliceStationTMap = new HashMap<String, List<Station>>(); // 区域-民警岗亭总计
	private Map<String, List<Station>> mQYPoliceStationOMap = new HashMap<String, List<Station>>(); // 区域-民警岗亭在线
	private Map<String, List<Station>> mQYPoliceStationFMap = new HashMap<String, List<Station>>(); // 区域-民警岗亭离线
	// <区域名称，<机构名称，数据集合>>
	private Map<String, Map<String, List<Policeman>>> mQYJGPoliceTMap = new HashMap<String, Map<String, List<Policeman>>>(); // 机构-警员总计
	private Map<String, Map<String, List<Policeman>>> mQYJGPoliceOMap = new HashMap<String, Map<String, List<Policeman>>>(); // 机构-警员在线
	private Map<String, Map<String, List<Policeman>>> mQYJGPoliceFMap = new HashMap<String, Map<String, List<Policeman>>>(); // 机构-警员离线
	private Map<String, Map<String, List<Station>>> mQYJGNormalStationTMap = new HashMap<String, Map<String, List<Station>>>(); // 机构-联防岗亭总计
	private Map<String, Map<String, List<Station>>> mQYJGNormalStationOMap = new HashMap<String, Map<String, List<Station>>>(); // 机构-联防岗亭在线
	private Map<String, Map<String, List<Station>>> mQYJGNormalStationFMap = new HashMap<String, Map<String, List<Station>>>(); // 机构-联防岗亭离线
	private Map<String, Map<String, List<Station>>> mQYJGPoliceStationTMap = new HashMap<String, Map<String, List<Station>>>(); // 机构-民警岗亭总计
	private Map<String, Map<String, List<Station>>> mQYJGPoliceStationOMap = new HashMap<String, Map<String, List<Station>>>(); // 机构-民警岗亭在线
	private Map<String, Map<String, List<Station>>> mQYJGPoliceStationFMap = new HashMap<String, Map<String, List<Station>>>(); // 机构-民警岗亭离线

	private LinearLayout mListLayout;
	private LinearLayout mFormLayout;
	private TextView mResultNum;
	private TextView mShowInMap;
	private TextView mFormTitle;
	private TextView mListTitle;

	public void showListLayout(int stateSign, int typeSign, String mc) {
		mListLayout.setVisibility(View.VISIBLE);
		mFormLayout.setVisibility(View.GONE);
		mIsDetialShown = true;
		setTitleText(3, stateSign, typeSign, mc);
	}

	public void showFormLayout() {
		mListLayout.setVisibility(View.GONE);
		mFormLayout.setVisibility(View.VISIBLE);
		mIsDetialShown = false;
	}

	public TextView getResultNum() {
		return mResultNum;
	}

	public void resetDetailListView(Collection<? extends Light> collection) {
		mLightList.clear();
		mLightList.addAll(collection);
		mResultNum.setText("共有" + mLightList.size() + "条");
		mDetialAdapter.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_statistics_new, container, false);
		mFormLayout = (LinearLayout) view.findViewById(R.id.formLayout);
		mListLayout = (LinearLayout) view.findViewById(R.id.listLayout);
		mFormTitle = (TextView) view.findViewById(R.id.form_title);
		mListTitle = (TextView) view.findViewById(R.id.list_title);
		mResultNum = (TextView) view.findViewById(R.id.result_num);
		mLightListView = (ListView) view.findViewById(R.id.lightList);
		mDetialListView = (ListView) view.findViewById(R.id.detialList);
		mShowInMap = (TextView) view.findViewById(R.id.showInMap);
		mShowInMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showInMap();
			}
		});
		mPolicemanTList = new ArrayList<Policeman>();
		mPolicemanOList = mMainFragment.getPolicemanData();
		mPolicemanFList = new ArrayList<Policeman>();
		for (Policeman data : mPolicemanTList) {
			if (data.isOn_guard())
				mPolicemanOList.add(data);
			else
				mPolicemanFList.add(data);
		}
		mNormalStationTList = mMainFragment.getNormalStationData();
		mNormalStationOList = new ArrayList<Station>();
		mNormalStationFList = new ArrayList<Station>();
		for (Station data : mNormalStationTList) {
			if (data.isOn_guard())
				mNormalStationOList.add(data);
			else
				mNormalStationFList.add(data);
		}
		mPoliceStationTList = mMainFragment.getPoliceStationData();
		mPoliceStationOList = new ArrayList<Station>();
		mPoliceStationFList = new ArrayList<Station>();
		for (Station data : mPoliceStationTList) {
			if (data.isOn_guard())
				mPoliceStationOList.add(data);
			else
				mPoliceStationFList.add(data);
		}
		List<QY> mQYList = mMainFragment.getQYListData();
		mJGMap = new HashMap<String, List<JG>>();
		for (QY qy : mQYList) { // 遍历区域
			String qymc = qy.getXzqh_mc().trim(); // 行政区域名称
			String qydm = qy.getXzqh_dm(); // 行政区域代码
			List<JG> jgGroup = mMainFragment.getJGListData(qymc); // 获取某一区域的机构集合
			mJGMap.put(qymc, jgGroup);
			// 区域在线警员
			List<Policeman> qyPoliceOList = new ArrayList<Policeman>();
			for (int i = 0; i < mPolicemanOList.size(); i++) {
				Policeman policeman = mPolicemanOList.get(i);
				if(policeman.getGtqydm().equals("")){ // 在线警员和岗亭没有关联的情况下 岗亭区域代码会为空 这种情况下用警员的机构名称来判断
					if(policeman.getJgmc().contains(qymc)){
						qyPoliceOList.add(policeman);
					}
				}else{
					if (qydm.equals(policeman.getGtqydm())) { 
						qyPoliceOList.add(policeman);
					}
				}
			}
			Log.i(TAG, qymc +"  在线民警:" +qyPoliceOList.size());
			mQYPoliceOMap.put(qymc, qyPoliceOList);
			// 区域离线警员
			List<Policeman> qyPoliceFList = new ArrayList<Policeman>();
			for (int i = 0; i < mPolicemanFList.size(); i++) {
				Policeman policeman = mPolicemanFList.get(i);
				if (qydm.equals(policeman.getGtqydm())) { 
					qyPoliceFList.add(policeman);
				}
			}
			Log.i(TAG, qymc +"  离线民警:" +qyPoliceFList.size());
			mQYPoliceFMap.put(qymc, qyPoliceFList);
			// 区域总计警员
			List<Policeman> qyPoliceTList = new ArrayList<Policeman>(qyPoliceFList);
			qyPoliceTList.addAll(0, qyPoliceTList);
			Log.i(TAG, qymc +"  总计民警:" +qyPoliceTList.size());
			mQYPoliceTMap.put(qymc, qyPoliceTList);
			
			// 区域在线联防岗亭
			List<Station> qyNormalStationOList = new ArrayList<Station>();
			for (int i = 0; i < mNormalStationOList.size(); i++) {
				Station station = mNormalStationOList.get(i);
				if (qydm.equals(station.getXzqh_dm())) { 
					qyNormalStationOList.add(station);
				}
			}
			Log.i(TAG, qymc +"  在线联防岗亭:" +qyNormalStationOList.size());
			mQYNormalStationOMap.put(qymc, qyNormalStationOList);
			// 区域离线联防岗亭
			List<Station> qyNormalStationFList = new ArrayList<Station>();
			for (int i = 0; i < mNormalStationFList.size(); i++) {
				Station station = mNormalStationFList.get(i);
				if (qydm.equals(station.getXzqh_dm())) { 
					qyNormalStationFList.add(station);
				}
			}
			Log.i(TAG, qymc +"  离线联防岗亭:" +qyNormalStationFList.size());
			mQYNormalStationFMap.put(qymc, qyNormalStationFList);
			// 区域总计联防岗亭
			List<Station> qyNormalStationTList = new ArrayList<Station>(qyNormalStationFList);
			qyNormalStationTList.addAll(0, qyNormalStationOList);
			Log.i(TAG, qymc +"  总计联防岗亭:" +qyNormalStationTList.size());
			mQYNormalStationTMap.put(qymc, qyNormalStationTList);
			
			// 区域在线民警岗亭
			List<Station> qyPoliceStationOList = new ArrayList<Station>();
			for (int i = 0; i < mPoliceStationOList.size(); i++) {
				Station station = mPoliceStationOList.get(i);
				if (qydm.equals(station.getXzqh_dm())) { 
					qyPoliceStationOList.add(station);
				}
			}
			Log.i(TAG, qymc +"  在线民警岗亭:" + qyPoliceStationOList.size());
			mQYPoliceStationOMap.put(qymc, qyPoliceStationOList);
			// 区域离线民警岗亭
			List<Station> qyPoliceStationFList = new ArrayList<Station>();
			for (int i = 0; i < mPoliceStationFList.size(); i++) {
				Station station = mPoliceStationFList.get(i);
				if (qydm.equals(station.getXzqh_dm())) { 
					qyPoliceStationFList.add(station);
				}
			}
			Log.i(TAG, qymc +"  离线民警岗亭:" + qyPoliceStationFList.size());
			mQYPoliceStationFMap.put(qymc, qyPoliceStationFList);
			// 区域总计民警岗亭
			List<Station> qyPoliceStationTList = new ArrayList<Station>(qyPoliceStationFList);
			qyNormalStationTList.addAll(0, qyPoliceStationOList);
			Log.i(TAG, qymc +"  总计民警岗亭:" +qyPoliceStationTList.size());
			mQYPoliceStationTMap.put(qymc, qyPoliceStationTList);
			
			HashMap<String, List<Policeman>> jgPoliceOMap = new HashMap<String, List<Policeman>>();
			HashMap<String, List<Policeman>> jgPoliceFMap = new HashMap<String, List<Policeman>>();
			HashMap<String, List<Policeman>> jgPoliceTMap = new HashMap<String, List<Policeman>>();
			HashMap<String, List<Station>> jgPoliceStationOMap = new HashMap<String, List<Station>>();
			HashMap<String, List<Station>> jgPoliceStationFMap = new HashMap<String, List<Station>>();
			HashMap<String, List<Station>> jgPoliceStationTMap = new HashMap<String, List<Station>>();
			HashMap<String, List<Station>> jgNormalStationOMap = new HashMap<String, List<Station>>();
			HashMap<String, List<Station>> jgNormalStationFMap = new HashMap<String, List<Station>>();
			HashMap<String, List<Station>> jgNormalStationTMap = new HashMap<String, List<Station>>();
			// 遍历一个区域的所有机构
			for (int j = 0; j < jgGroup.size(); j++) { 
				JG jg = jgGroup.get(j);
				String jgbm = jg.getJgbm();	// 机构代码
				String jgmc = jg.getJgmc().trim(); // 机构名称
				if(jgmc.equals("")) continue;
//				if (qymc.equals(jgmc)) continue; // 获取的机构中存在和区名称相同的情况 （乐清市、龙湾区）
				if (jgbm == null) jgbm = ""; // 有的机构没有提供机构编码 （藤桥所、三垟所）
				jgbm = jgbm.trim();
				
				// 机构在线警员
				List<Policeman> jgPoliceOList = new ArrayList<Policeman>();
				for (int i = 0; i < qyPoliceOList.size(); i++) {
					Policeman policeman = qyPoliceOList.get(i);
					if (jgmc.equals(policeman.getGtjgmc())) { 
						jgPoliceOList.add(policeman);
					}
				}
				Log.i(TAG, "-------------" + jgmc + "-------------");
				Log.i(TAG, qymc + "-" + jgmc + "  在线民警:" + jgPoliceOList.size());
				jgPoliceOMap.put(jgmc, jgPoliceOList);
				// 机构离线警员
				List<Policeman> jgPoliceFList = new ArrayList<Policeman>();
				for (int i = 0; i < qyPoliceFList.size(); i++) {
					Policeman policeman = qyPoliceFList.get(i);
					if (jgmc.equals(policeman.getGtjgmc())) { 
						jgPoliceFList.add(policeman);
					}
				}
				Log.i(TAG, qymc + "-" + jgmc + "  离线民警:" + jgPoliceFList.size());
				jgPoliceFMap.put(jgmc, jgPoliceFList);
				// 机构总计警员
				List<Policeman> jgPoliceTList = new ArrayList<Policeman>(jgPoliceFList);
				jgPoliceTList.addAll(0, jgPoliceOList);
				Log.i(TAG, qymc + "-" + jgmc + "  总计民警:" + jgPoliceTList.size());
				jgPoliceTMap.put(jgmc, jgPoliceTList);
				
				// 机构在线民警岗亭
				List<Station> jgPoliceStationOList = new ArrayList<Station>();
				for (int i = 0; i < qyPoliceStationOList.size(); i++) {
					Station station = qyPoliceStationOList.get(i);
					if (jgmc.equals(station.getJgmc())) { 
						jgPoliceStationOList.add(station);
					}
				}
				Log.i(TAG, qymc + "-" + jgmc + "  在线民警岗亭:" + jgPoliceStationOList.size());
				jgPoliceStationOMap.put(jgmc, jgPoliceStationOList);
				// 机构离线警员岗亭
				List<Station> jgPoliceStationFList = new ArrayList<Station>();
				for (int i = 0; i < qyPoliceStationFList.size(); i++) {
					Station station = qyPoliceStationFList.get(i);
					if (jgmc.equals(station.getJgmc())) { 
						jgPoliceStationFList.add(station);
					}
				}
				Log.i(TAG, qymc + "-" + jgmc + "  离线民警岗亭:" + jgPoliceStationFList.size());
				jgPoliceStationFMap.put(jgmc, jgPoliceStationFList);
				// 机构总计警员岗亭
				List<Station> jgPoliceStationTList = new ArrayList<Station>(jgPoliceStationFList);
				jgPoliceStationTList.addAll(0, jgPoliceStationOList);
				Log.i(TAG, qymc + "-" + jgmc + "  总计民警岗亭:" + jgPoliceStationTList.size());
				jgPoliceStationTMap.put(jgmc, jgPoliceStationTList);
				
				// 机构在线联防岗亭
				List<Station> jgNormalStationOList = new ArrayList<Station>();
				for (int i = 0; i < qyNormalStationOList.size(); i++) {
					Station station = qyNormalStationOList.get(i);
					if (jgmc.equals(station.getJgmc())) { 
						jgNormalStationOList.add(station);
					}
				}
				Log.i(TAG, qymc + "-" + jgmc + "  在线联防岗亭:" + jgNormalStationOList.size());
				jgNormalStationOMap.put(jgmc, jgNormalStationOList);
				// 机构离线联防岗亭
				List<Station> jgNormalStationFList = new ArrayList<Station>();
				for (int i = 0; i < qyNormalStationFList.size(); i++) {
					Station station = qyNormalStationFList.get(i);
					if (jgmc.equals(station.getJgmc())) { 
						jgNormalStationFList.add(station);
					}
				}
				Log.i(TAG, qymc + "-" + jgmc + "  离线联防岗亭:" + jgNormalStationFList.size());
				jgNormalStationFMap.put(jgmc, jgNormalStationFList);
				// 机构总计联防岗亭
				List<Station> jgNormalStationTList = new ArrayList<Station>(jgNormalStationFList);
				jgNormalStationTList.addAll(0, jgNormalStationOList);
				Log.i(TAG, qymc + "-" + jgmc + "  总计联防岗亭:" + jgNormalStationTList.size());
				jgNormalStationTMap.put(jgmc, jgNormalStationTList);
				Log.i(TAG, "-------------" + jgmc + "-------------");
				if (j == jgGroup.size() - 1) {
					mQYJGPoliceOMap.put(qymc, jgPoliceOMap);
					mQYJGPoliceFMap.put(qymc, jgPoliceFMap);
					mQYJGPoliceTMap.put(qymc, jgPoliceTMap);
					
					mQYJGNormalStationOMap.put(qymc, jgNormalStationOMap);
					mQYJGNormalStationFMap.put(qymc, jgNormalStationFMap);
					mQYJGNormalStationTMap.put(qymc, jgNormalStationTMap);
					
					mQYJGPoliceStationOMap.put(qymc, jgPoliceStationOMap);
					mQYJGPoliceStationFMap.put(qymc, jgPoliceStationFMap);
					mQYJGPoliceStationTMap.put(qymc, jgPoliceStationTMap);
				}
			}

		}
		mLightAdapter = new StatisticsAdapter(StatisticsFragment.this);
		mLightListView.setAdapter(mLightAdapter);

		mLightList = new ArrayList<>();
		mDetialAdapter = new SearchResultAdapter(mLightList);
		mDetialListView.setAdapter(mDetialAdapter);
		mDetialListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Light light = (Light) mDetialAdapter.getItem(arg2);
				if (light instanceof Station) {
					StationFragment mPoliceStationFragment = new StationFragment((Station) light, mMainActivity);
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.hide(StatisticsFragment.this);
					transaction.add(R.id.container, mPoliceStationFragment);
					transaction.addToBackStack(null);
					transaction.commit();
				} else {
					PolicemanFragment mPolicemanFragment = new PolicemanFragment((Policeman) light, mMainActivity);
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.hide(StatisticsFragment.this);
					transaction.add(R.id.container, mPolicemanFragment);
					transaction.addToBackStack(null);
					transaction.commit();
				}
			}
		});
		return view;
	}
	
	/**
	 * 设置title文字
	 * @param index 1：一级目录（市-区（县）） 	2：二级目录（区（县）-所） 	3：警员在线列表	4：联防岗亭在线列表	5：民警岗亭在线列表	6：警灯在线列表
	 * @param mc 区域或机构名称
	 */
	public void setTitleText(int levelIndex, int stateSign, int typeSign, String mc){
		switch(levelIndex){
		case 1:
			mFormTitle.setText("温州市	在线警灯统计表");
			break;
		case 2:
			mFormTitle.setText(mc + "	在线警灯统计表");
			break;
		case 3:
			//stateSign 1 在线  	2离线	3总数
			//typeSign  1 警员  	2联防岗亭（移动岗亭）	3民警岗亭（治安岗亭）	4警灯
			switch(stateSign){
			case 1:
				switch(typeSign){
				case 1:
					mListTitle.setText(mc + "	警员在线列表");
					break;
				case 2:
					mListTitle.setText(mc + "	联防岗亭在线列表");
					break;
				case 3:
					mListTitle.setText(mc + "	民警岗亭在线列表");
					break;
				case 4:
					mListTitle.setText(mc + "	警灯在线列表");
					break;
				}
				break;
			case 2:
				switch(typeSign){
				case 1:
					mListTitle.setText(mc + "	警员离线列表");
					break;
				case 2:
					mListTitle.setText(mc + "	联防岗亭离线列表");
					break;
				case 3:
					mListTitle.setText(mc + "	民警岗亭离线列表");
					break;
				case 4:
					mListTitle.setText(mc + "	警灯离线列表");
					break;
				}
				break;
			case 3:
				switch(typeSign){
				case 1:
					mListTitle.setText(mc + "	警员总计列表");
					break;
				case 2:
					mListTitle.setText(mc + "	联防岗亭总计列表");
					break;
				case 3:
					mListTitle.setText(mc + "	民警岗亭总计列表");
					break;
				case 4:
					mListTitle.setText(mc + "	警灯总计列表");
					break;
				}
				break;
			}
			break;
		}
		
	}
	
	public List<Policeman> getPolicemanTList() {
		return mPolicemanTList;
	}

	public List<Policeman> getPolicemanOList() {
		return mPolicemanOList;
	}

	public List<Policeman> getPolicemanFList() {
		return mPolicemanFList;
	}

	public List<Station> getNormalStationTList() {
		return mNormalStationTList;
	}

	public List<Station> getNormalStationOList() {
		return mNormalStationOList;
	}

	public List<Station> getNormalStationFList() {
		return mNormalStationFList;
	}

	public List<Station> getStationTList() {
		return mPoliceStationTList;
	}

	public List<Station> getStationOList() {
		return mPoliceStationOList;
	}

	public List<Station> getStationFList() {
		return mPoliceStationFList;
	}

	// 区域
	public Map<String, List<Policeman>> getQYPoliceTMap() {
		return mQYPoliceTMap;
	}
	public Map<String, List<Policeman>> getQYPoliceOMap() {
		return mQYPoliceOMap;
	}
	public Map<String, List<Policeman>> getQYPoliceFMap() {
		return mQYPoliceFMap;
	}
	public Map<String, List<Station>> getQYNormalStationTMap() {
		return mQYNormalStationTMap;
	}
	public Map<String, List<Station>> getQYNormalStationOMap() {
		return mQYNormalStationOMap;
	}
	public Map<String, List<Station>> getQYNormalStationFMap() {
		return mQYNormalStationFMap;
	}
	public Map<String, List<Station>> getQYPoliceStationTMap() {
		return mQYPoliceStationTMap;
	}
	public Map<String, List<Station>> getQYPoliceStationOMap() {
		return mQYPoliceStationOMap;
	}
	public Map<String, List<Station>> getQYPoliceStationFMap() {
		return mQYPoliceStationFMap;
	}

	// 机构
	public Map<String, Map<String, List<Policeman>>> getQYJGPoliceTMap() {
		return mQYJGPoliceTMap;
	}
	public Map<String, Map<String, List<Policeman>>> getQYJGPoliceOMap() {
		return mQYJGPoliceOMap;
	}
	public Map<String, Map<String, List<Policeman>>> getQYJGPoliceFMap() {
		return mQYJGPoliceFMap;
	}
	public Map<String, Map<String, List<Station>>> getQYJGNormalStationTMap() {
		return mQYJGNormalStationTMap;
	}
	public Map<String, Map<String, List<Station>>> getQYJGNormalStationOMap() {
		return mQYJGNormalStationOMap;
	}
	public Map<String, Map<String, List<Station>>> getQYJGNormalStationFMap() {
		return mQYJGNormalStationFMap;
	}
	public Map<String, Map<String, List<Station>>> getQYJGPoliceStationTMap() {
		return mQYJGPoliceStationTMap;
	}
	public Map<String, Map<String, List<Station>>> getQYJGPoliceStationOMap() {
		return mQYJGPoliceStationOMap;
	}
	public Map<String, Map<String, List<Station>>> getQYJGPoliceStationFMap() {
		return mQYJGPoliceStationFMap;
	}
	
	public Map<String, List<JG>> getJGMap() {
		return mJGMap;
	}

	public boolean isSecondFrom() {
		return mIsSecondFrom;
	}

	public void setIsSecondFrom(boolean b) {
		mIsSecondFrom = b;
	}

	public boolean isDetialShown() {
		return mIsDetialShown;
	}

	public void initForm() {
		mLightAdapter.showFirstForm(true);
		mIsSecondFrom = false;
	}
	
	public void showInMap(){
		mMainActivity.getMainFragment().clearMap();
		List<Station> list_lianfang = new ArrayList<Station>();
		List<Station> list_mingjin = new ArrayList<Station>();
		List<Policeman> list_jingyuan = new ArrayList<Policeman>();
		for(int i=0;i<mLightList.size();i++){
			Light light = mLightList.get(i);
			if(light.getClass()==(Station.class)){
				if(((Station) light).getSfmj() != null && ((Station) light).getSfmj().equals("是")){
					list_mingjin.add(((Station) light));
				}
				if(((Station) light).getSfmj() != null && ((Station) light).getSfmj().equals("否")){
					list_lianfang.add(((Station) light));
				}
			}else if(light.getClass()==(Policeman.class)){
				list_jingyuan.add(((Policeman) light));
			}
		}
		mMainFragment.showNormalStationGraphic(list_lianfang, mMainFragment.getDisplayLevel(), true, true);
		mMainFragment.showPoliceStationGraphic(list_mingjin, mMainFragment.getDisplayLevel(), true, true);
		mMainFragment.showPolicemanGraphic(list_jingyuan, mMainFragment.getDisplayLevel(), true, true);
		
		mMainActivity.replaceLogoView(true);
	    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	
	public MainFragment getMainFragment(){
		return mMainFragment;
	}
	
}
