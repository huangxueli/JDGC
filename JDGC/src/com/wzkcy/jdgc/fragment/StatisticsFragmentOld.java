/*package com.wzkcy.jdgc.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
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

*//** 统计界面 **//*
public class StatisticsFragmentOld extends Fragment {

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

	public StatisticsFragmentOld(MainFragment fragment) {
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

	private Map<String, List<JG>> mJGMap;
	
	private Map<String, List<Policeman>> mQYPoliceTMap = new HashMap<String, List<Policeman>>(); // 区域-警员总计
	private Map<String, List<Policeman>> mQYPoliceOMap = new HashMap<String, List<Policeman>>(); // 区域-警员在线
	private Map<String, List<Policeman>> mQYPoliceFMap = new HashMap<String, List<Policeman>>(); // 区域-警员离线
	private Map<String, List<Station>> mQYNormalStationTMap = new HashMap<String, List<Station>>(); // 区域-联防岗亭总计
	private Map<String, List<Station>> mQYNormalStationOMap = new HashMap<String, List<Station>>(); // 区域-联防岗亭在线
	private Map<String, List<Station>> mQYNormalStationFMap = new HashMap<String, List<Station>>(); // 区域-联防岗亭离线
	private Map<String, List<Station>> mQYPoliceStationTMap = new HashMap<String, List<Station>>(); // 区域-民警岗亭总计
	private Map<String, List<Station>> mQYPoliceStationOMap = new HashMap<String, List<Station>>(); // 区域-民警岗亭在线
	private Map<String, List<Station>> mQYPoliceStationFMap = new HashMap<String, List<Station>>(); // 区域-民警岗亭离线
	
	private Map<String, List<Policeman>> mJGPoliceTMap = new HashMap<String, List<Policeman>>(); // 机构-警员总计
	private Map<String, List<Policeman>> mJGPoliceOMap = new HashMap<String, List<Policeman>>(); // 机构-警员在线
	private Map<String, List<Policeman>> mJGPoliceFMap = new HashMap<String, List<Policeman>>(); // 机构-警员离线
	private Map<String, List<Station>> mJGNormalStationTMap = new HashMap<String, List<Station>>(); // 机构-联防岗亭总计
	private Map<String, List<Station>> mJGNormalStationOMap = new HashMap<String, List<Station>>(); // 机构-联防岗亭在线
	private Map<String, List<Station>> mJGNormalStationFMap = new HashMap<String, List<Station>>(); // 机构-联防岗亭离线
	private Map<String, List<Station>> mJGPoliceStationTMap = new HashMap<String, List<Station>>(); // 机构-民警岗亭总计
	private Map<String, List<Station>> mJGPoliceStationOMap = new HashMap<String, List<Station>>(); // 机构-民警岗亭在线
	private Map<String, List<Station>> mJGPoliceStationFMap = new HashMap<String, List<Station>>(); // 机构-民警岗亭离线

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
		mPolicemanTList = mMainFragment.getPolicemanData();
		mPolicemanOList = new ArrayList<Policeman>();
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
			String qymc = qy.getXzqh_mc().trim();
			List<JG> jgGroup = mMainFragment.getJGListData(qymc); // 获取某一区域的机构集合
			mJGMap.put(qymc, jgGroup);

			List<Policeman> qyPoliceOList = new ArrayList<Policeman>();
			List<Policeman> qyPoliceFList = new ArrayList<Policeman>();
			List<Policeman> qyPoliceTList = new ArrayList<Policeman>();

			List<Station> qyStationMoveOList = new ArrayList<Station>();
			List<Station> qyStationMoveFList = new ArrayList<Station>();
			List<Station> qyStationMoveTList = new ArrayList<Station>();

			List<Station> qyStationOList = new ArrayList<Station>();
			List<Station> qyStationFList = new ArrayList<Station>();
			List<Station> qyStationTList = new ArrayList<Station>();
			for (int j = 0; j < jgGroup.size(); j++) { // 遍历一个区域的所有机构
				JG jg = jgGroup.get(j);
				String jgbm = jg.getJgbm();
				if (qymc.equals(jg.getJgmc())) { // 获取的机构中存在和区名称相同的情况 （乐清市、龙湾区）
					continue;
				}
				if (jgbm == null) { // 有的机构没有提供机构编码 （藤桥所、三垟所）
					jgbm = "";
				}
				jgbm = jgbm.trim();
				// 按机构过滤警员的总计、在线、离线
				List<Policeman> jgPoliceOList = new ArrayList<Policeman>();
				for (int i = 0; i < mPolicemanOList.size(); i++) {
					Policeman policeman = mPolicemanOList.get(i);
					if (!jgbm.equals("") && jgbm.equals(policeman.getJgbm())) { // 找到对应机构的在线警员
						jgPoliceOList.add(policeman);
					}
					if (i == mPolicemanOList.size() - 1) {
						qyPoliceOList.addAll(jgPoliceOList);
					}
				}
				mJGPoliceOMap.put(jg.getJgmc(), jgPoliceOList);
				
				List<Policeman> jgPoliceFList = new ArrayList<Policeman>();
				for (int i = 0; i < mPolicemanFList.size(); i++) {
					Policeman policeman = mPolicemanFList.get(i);
					if (!jgbm.equals("") && jgbm.equals(policeman.getJgbm())) { // 找到对应机构的离线警员
						jgPoliceFList.add(policeman);
					}
					if (i == mPolicemanFList.size() - 1) {
						qyPoliceFList.addAll(jgPoliceFList);
					}
				}
				mJGPoliceFMap.put(jg.getJgmc(), jgPoliceFList);
				List<Policeman> jgPoliceTList = new ArrayList<Policeman>(jgPoliceFList);
				jgPoliceTList.addAll(0, jgPoliceOList);
				qyPoliceTList.addAll(jgPoliceTList);
				mJGPoliceTMap.put(jg.getJgmc(), jgPoliceTList);
				
				// 按机构过滤联防岗亭的总计、在线、离线
				List<Station> jgStationMoveOList = new ArrayList<Station>();
				for (int i = 0; i < mNormalStationOList.size(); i++) {
					Station station = mNormalStationOList.get(i);
					if (!jgbm.equals("") && jgbm.equals(station.getJgbm())) { // 找到对应机构的在线联防岗亭
						jgStationMoveOList.add(station);
					}
					if (i == mNormalStationOList.size() - 1) {
						qyStationMoveOList.addAll(jgStationMoveOList);
					}
				}
				mJGNormalStationOMap.put(jg.getJgmc(), jgStationMoveOList);
				
				List<Station> jgStationMoveFList = new ArrayList<Station>();
				for (int i = 0; i < mNormalStationFList.size(); i++) {
					Station station = mNormalStationFList.get(i);
					if (!jgbm.equals("") && jgbm.equals(station.getJgbm())) { // 找到对应机构的离线联防岗亭
						jgStationMoveFList.add(station);
					}
					if (i == mNormalStationFList.size() - 1) {
						qyStationMoveFList.addAll(jgStationMoveFList);
					}
				}
				mJGNormalStationFMap.put(jg.getJgmc(), jgStationMoveFList);
				List<Station> jgStationMoveTList = new ArrayList<Station>(jgStationMoveFList);
				jgStationMoveTList.addAll(0, jgStationMoveOList);
				qyStationMoveTList.addAll(jgStationMoveTList);
				mJGNormalStationTMap.put(jg.getJgmc(), jgStationMoveTList);
				// 按机构过滤联防岗亭的总计、在线、离线
				List<Station> jgStationOList = new ArrayList<Station>();
				for (int i = 0; i < mPoliceStationOList.size(); i++) {
					Station station = mPoliceStationOList.get(i);
					if (!jgbm.equals("") && jgbm.equals(station.getJgbm())) { // 找到对应机构的在线民警岗亭
						jgStationOList.add(station);
					}
					if (i == mPoliceStationOList.size() - 1) {
						qyStationOList.addAll(jgStationOList);

					}
				}
				mJGPoliceStationOMap.put(jg.getJgmc(), jgStationOList);

				List<Station> jgStationFList = new ArrayList<Station>();
				for (int i = 0; i < mPoliceStationFList.size(); i++) {
					Station station = mPoliceStationFList.get(i);
					if (!jgbm.equals("") && jgbm.equals(station.getJgbm())) { // 找到对应机构的离线民警岗亭
						jgStationFList.add(station);
					}
					if (i == mPoliceStationFList.size() - 1) {
						qyStationFList.addAll(jgStationFList);
					}
				}
				mJGPoliceStationFMap.put(jg.getJgmc(), jgStationFList);
				List<Station> jgStationTList = new ArrayList<Station>(jgStationFList);
				jgStationTList.addAll(0, jgStationOList);
				qyStationTList.addAll(jgStationTList);
				mJGPoliceStationTMap.put(jg.getJgmc(), jgStationTList);
				
				if (j == jgGroup.size() - 1) {
					mQYPoliceOMap.put(qymc, qyPoliceOList);
					mQYPoliceFMap.put(qymc, qyPoliceFList);
					mQYPoliceTMap.put(qymc, qyPoliceTList);

					mQYNormalStationOMap.put(qymc, qyStationMoveOList);
					mQYNormalStationFMap.put(qymc, qyStationMoveFList);
					mQYNormalStationTMap.put(qymc, qyStationMoveTList);

					mQYPoliceStationOMap.put(qymc, qyStationOList);
					mQYPoliceStationFMap.put(qymc, qyStationFList);
					mQYPoliceStationTMap.put(qymc, qyStationTList);
				}
			}

		}
		mLightAdapter = new StatisticsAdapter(StatisticsFragmentOld.this);
		mLightListView.setAdapter(mLightAdapter);

		mLightList = new ArrayList<>();
		mDetialAdapter = new SearchResultAdapter(mLightList);
		mDetialListView.setAdapter(mDetialAdapter);
		mDetialListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Light light = (Light) mDetialAdapter.getItem(arg2);
				if (light instanceof Station) {
					StationFragment mPoliceStationFragment = new StationFragment((Station) light, mMainActivity);
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.hide(StatisticsFragmentOld.this);
					transaction.add(R.id.container, mPoliceStationFragment);
					transaction.addToBackStack(null);
					transaction.commit();
				} else {
					PolicemanFragment mPolicemanFragment = new PolicemanFragment((Policeman) light, mMainActivity);
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.hide(StatisticsFragmentOld.this);
					transaction.add(R.id.container, mPolicemanFragment);
					transaction.addToBackStack(null);
					transaction.commit();
				}
			}
		});
		return view;
	}
	
	*//**
	 * 设置title文字
	 * @param index 1：一级目录（市-区（县）） 	2：二级目录（区（县）-所） 	3：警员在线列表	4：联防岗亭在线列表	5：民警岗亭在线列表	6：警灯在线列表
	 * @param mc 区域或机构名称
	 *//*
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
	public Map<String, List<Policeman>> getJGPoliceTMap() {
		return mJGPoliceTMap;
	}

	public Map<String, List<Policeman>> getJGPoliceOMap() {
		return mJGPoliceOMap;
	}

	public Map<String, List<Policeman>> getJGPoliceFMap() {
		return mJGPoliceFMap;
	}

	public Map<String, List<Station>> getJGNormalStationTMap() {
		return mJGNormalStationTMap;
	}

	public Map<String, List<Station>> getJGNormalStationOMap() {
		return mJGNormalStationOMap;
	}

	public Map<String, List<Station>> getJGNormalStationFMap() {
		return mJGNormalStationFMap;
	}

	public Map<String, List<Station>> getJGPoliceStationTMap() {
		return mJGPoliceStationTMap;
	}

	public Map<String, List<Station>> getJGPoliceStationOMap() {
		return mJGPoliceStationOMap;
	}

	public Map<String, List<Station>> getJGPoliceStationFMap() {
		return mJGPoliceStationFMap;
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

}
*/