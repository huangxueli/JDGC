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

/** ͳ�ƽ��� **/
public class StatisticsFragment extends Fragment {

	public static final String TAG = "StatisticsFragment";

	private MainActivity mMainActivity;
	private MainFragment mMainFragment;
	private ListView mLightListView;
	private StatisticsAdapter mLightAdapter;

	private ListView mDetialListView;
	private SearchResultAdapter mDetialAdapter;
	private ArrayList<Light> mLightList;

	private boolean mIsSecondFrom; // ��ǰ�Ƿ���ʾ�������
	private boolean mIsDetialShown; // ��ǰ�Ƿ���ʾ�����б�

	public StatisticsFragment(MainFragment fragment) {
		mMainFragment = fragment;
		mMainActivity = (MainActivity) fragment.getActivity();
		mMainActivity.setStatisticsFragment(this);
	}

	private List<Policeman> mPolicemanTList; // ���������о�Ա
	private List<Policeman> mPolicemanOList; // ���������߾�Ա
	private List<Policeman> mPolicemanFList; // ���������߾�Ա
	private List<Station> mNormalStationTList; // ����������������ͤ
	private List<Station> mNormalStationOList; // ����������������ͤ
	private List<Station> mNormalStationFList; // ����������������ͤ
	private List<Station> mPoliceStationTList; // �����������񾯸�ͤ
	private List<Station> mPoliceStationOList; // �����������񾯸�ͤ
	private List<Station> mPoliceStationFList; // �����������񾯸�ͤ
	// <�������ƣ��������Ƽ���>
	private Map<String, List<JG>> mJGMap;
	// <�������ƣ����ݼ���>
	private Map<String, List<Policeman>> mQYPoliceTMap = new HashMap<String, List<Policeman>>(); // ����-��Ա�ܼ�
	private Map<String, List<Policeman>> mQYPoliceOMap = new HashMap<String, List<Policeman>>(); // ����-��Ա����
	private Map<String, List<Policeman>> mQYPoliceFMap = new HashMap<String, List<Policeman>>(); // ����-��Ա����
	private Map<String, List<Station>> mQYNormalStationTMap = new HashMap<String, List<Station>>(); // ����-������ͤ�ܼ�
	private Map<String, List<Station>> mQYNormalStationOMap = new HashMap<String, List<Station>>(); // ����-������ͤ����
	private Map<String, List<Station>> mQYNormalStationFMap = new HashMap<String, List<Station>>(); // ����-������ͤ����
	private Map<String, List<Station>> mQYPoliceStationTMap = new HashMap<String, List<Station>>(); // ����-�񾯸�ͤ�ܼ�
	private Map<String, List<Station>> mQYPoliceStationOMap = new HashMap<String, List<Station>>(); // ����-�񾯸�ͤ����
	private Map<String, List<Station>> mQYPoliceStationFMap = new HashMap<String, List<Station>>(); // ����-�񾯸�ͤ����
	// <�������ƣ�<�������ƣ����ݼ���>>
	private Map<String, Map<String, List<Policeman>>> mQYJGPoliceTMap = new HashMap<String, Map<String, List<Policeman>>>(); // ����-��Ա�ܼ�
	private Map<String, Map<String, List<Policeman>>> mQYJGPoliceOMap = new HashMap<String, Map<String, List<Policeman>>>(); // ����-��Ա����
	private Map<String, Map<String, List<Policeman>>> mQYJGPoliceFMap = new HashMap<String, Map<String, List<Policeman>>>(); // ����-��Ա����
	private Map<String, Map<String, List<Station>>> mQYJGNormalStationTMap = new HashMap<String, Map<String, List<Station>>>(); // ����-������ͤ�ܼ�
	private Map<String, Map<String, List<Station>>> mQYJGNormalStationOMap = new HashMap<String, Map<String, List<Station>>>(); // ����-������ͤ����
	private Map<String, Map<String, List<Station>>> mQYJGNormalStationFMap = new HashMap<String, Map<String, List<Station>>>(); // ����-������ͤ����
	private Map<String, Map<String, List<Station>>> mQYJGPoliceStationTMap = new HashMap<String, Map<String, List<Station>>>(); // ����-�񾯸�ͤ�ܼ�
	private Map<String, Map<String, List<Station>>> mQYJGPoliceStationOMap = new HashMap<String, Map<String, List<Station>>>(); // ����-�񾯸�ͤ����
	private Map<String, Map<String, List<Station>>> mQYJGPoliceStationFMap = new HashMap<String, Map<String, List<Station>>>(); // ����-�񾯸�ͤ����

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
		mResultNum.setText("����" + mLightList.size() + "��");
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
		for (QY qy : mQYList) { // ��������
			String qymc = qy.getXzqh_mc().trim(); // ������������
			String qydm = qy.getXzqh_dm(); // �����������
			List<JG> jgGroup = mMainFragment.getJGListData(qymc); // ��ȡĳһ����Ļ�������
			mJGMap.put(qymc, jgGroup);
			// �������߾�Ա
			List<Policeman> qyPoliceOList = new ArrayList<Policeman>();
			for (int i = 0; i < mPolicemanOList.size(); i++) {
				Policeman policeman = mPolicemanOList.get(i);
				if(policeman.getGtqydm().equals("")){ // ���߾�Ա�͸�ͤû�й���������� ��ͤ��������Ϊ�� ����������þ�Ա�Ļ����������ж�
					if(policeman.getJgmc().contains(qymc)){
						qyPoliceOList.add(policeman);
					}
				}else{
					if (qydm.equals(policeman.getGtqydm())) { 
						qyPoliceOList.add(policeman);
					}
				}
			}
			Log.i(TAG, qymc +"  ������:" +qyPoliceOList.size());
			mQYPoliceOMap.put(qymc, qyPoliceOList);
			// �������߾�Ա
			List<Policeman> qyPoliceFList = new ArrayList<Policeman>();
			for (int i = 0; i < mPolicemanFList.size(); i++) {
				Policeman policeman = mPolicemanFList.get(i);
				if (qydm.equals(policeman.getGtqydm())) { 
					qyPoliceFList.add(policeman);
				}
			}
			Log.i(TAG, qymc +"  ������:" +qyPoliceFList.size());
			mQYPoliceFMap.put(qymc, qyPoliceFList);
			// �����ܼƾ�Ա
			List<Policeman> qyPoliceTList = new ArrayList<Policeman>(qyPoliceFList);
			qyPoliceTList.addAll(0, qyPoliceTList);
			Log.i(TAG, qymc +"  �ܼ���:" +qyPoliceTList.size());
			mQYPoliceTMap.put(qymc, qyPoliceTList);
			
			// ��������������ͤ
			List<Station> qyNormalStationOList = new ArrayList<Station>();
			for (int i = 0; i < mNormalStationOList.size(); i++) {
				Station station = mNormalStationOList.get(i);
				if (qydm.equals(station.getXzqh_dm())) { 
					qyNormalStationOList.add(station);
				}
			}
			Log.i(TAG, qymc +"  ����������ͤ:" +qyNormalStationOList.size());
			mQYNormalStationOMap.put(qymc, qyNormalStationOList);
			// ��������������ͤ
			List<Station> qyNormalStationFList = new ArrayList<Station>();
			for (int i = 0; i < mNormalStationFList.size(); i++) {
				Station station = mNormalStationFList.get(i);
				if (qydm.equals(station.getXzqh_dm())) { 
					qyNormalStationFList.add(station);
				}
			}
			Log.i(TAG, qymc +"  ����������ͤ:" +qyNormalStationFList.size());
			mQYNormalStationFMap.put(qymc, qyNormalStationFList);
			// �����ܼ�������ͤ
			List<Station> qyNormalStationTList = new ArrayList<Station>(qyNormalStationFList);
			qyNormalStationTList.addAll(0, qyNormalStationOList);
			Log.i(TAG, qymc +"  �ܼ�������ͤ:" +qyNormalStationTList.size());
			mQYNormalStationTMap.put(qymc, qyNormalStationTList);
			
			// ���������񾯸�ͤ
			List<Station> qyPoliceStationOList = new ArrayList<Station>();
			for (int i = 0; i < mPoliceStationOList.size(); i++) {
				Station station = mPoliceStationOList.get(i);
				if (qydm.equals(station.getXzqh_dm())) { 
					qyPoliceStationOList.add(station);
				}
			}
			Log.i(TAG, qymc +"  �����񾯸�ͤ:" + qyPoliceStationOList.size());
			mQYPoliceStationOMap.put(qymc, qyPoliceStationOList);
			// ���������񾯸�ͤ
			List<Station> qyPoliceStationFList = new ArrayList<Station>();
			for (int i = 0; i < mPoliceStationFList.size(); i++) {
				Station station = mPoliceStationFList.get(i);
				if (qydm.equals(station.getXzqh_dm())) { 
					qyPoliceStationFList.add(station);
				}
			}
			Log.i(TAG, qymc +"  �����񾯸�ͤ:" + qyPoliceStationFList.size());
			mQYPoliceStationFMap.put(qymc, qyPoliceStationFList);
			// �����ܼ��񾯸�ͤ
			List<Station> qyPoliceStationTList = new ArrayList<Station>(qyPoliceStationFList);
			qyNormalStationTList.addAll(0, qyPoliceStationOList);
			Log.i(TAG, qymc +"  �ܼ��񾯸�ͤ:" +qyPoliceStationTList.size());
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
			// ����һ����������л���
			for (int j = 0; j < jgGroup.size(); j++) { 
				JG jg = jgGroup.get(j);
				String jgbm = jg.getJgbm();	// ��������
				String jgmc = jg.getJgmc().trim(); // ��������
				if(jgmc.equals("")) continue;
//				if (qymc.equals(jgmc)) continue; // ��ȡ�Ļ����д��ں���������ͬ����� �������С���������
				if (jgbm == null) jgbm = ""; // �еĻ���û���ṩ�������� ������������������
				jgbm = jgbm.trim();
				
				// �������߾�Ա
				List<Policeman> jgPoliceOList = new ArrayList<Policeman>();
				for (int i = 0; i < qyPoliceOList.size(); i++) {
					Policeman policeman = qyPoliceOList.get(i);
					if (jgmc.equals(policeman.getGtjgmc())) { 
						jgPoliceOList.add(policeman);
					}
				}
				Log.i(TAG, "-------------" + jgmc + "-------------");
				Log.i(TAG, qymc + "-" + jgmc + "  ������:" + jgPoliceOList.size());
				jgPoliceOMap.put(jgmc, jgPoliceOList);
				// �������߾�Ա
				List<Policeman> jgPoliceFList = new ArrayList<Policeman>();
				for (int i = 0; i < qyPoliceFList.size(); i++) {
					Policeman policeman = qyPoliceFList.get(i);
					if (jgmc.equals(policeman.getGtjgmc())) { 
						jgPoliceFList.add(policeman);
					}
				}
				Log.i(TAG, qymc + "-" + jgmc + "  ������:" + jgPoliceFList.size());
				jgPoliceFMap.put(jgmc, jgPoliceFList);
				// �����ܼƾ�Ա
				List<Policeman> jgPoliceTList = new ArrayList<Policeman>(jgPoliceFList);
				jgPoliceTList.addAll(0, jgPoliceOList);
				Log.i(TAG, qymc + "-" + jgmc + "  �ܼ���:" + jgPoliceTList.size());
				jgPoliceTMap.put(jgmc, jgPoliceTList);
				
				// ���������񾯸�ͤ
				List<Station> jgPoliceStationOList = new ArrayList<Station>();
				for (int i = 0; i < qyPoliceStationOList.size(); i++) {
					Station station = qyPoliceStationOList.get(i);
					if (jgmc.equals(station.getJgmc())) { 
						jgPoliceStationOList.add(station);
					}
				}
				Log.i(TAG, qymc + "-" + jgmc + "  �����񾯸�ͤ:" + jgPoliceStationOList.size());
				jgPoliceStationOMap.put(jgmc, jgPoliceStationOList);
				// �������߾�Ա��ͤ
				List<Station> jgPoliceStationFList = new ArrayList<Station>();
				for (int i = 0; i < qyPoliceStationFList.size(); i++) {
					Station station = qyPoliceStationFList.get(i);
					if (jgmc.equals(station.getJgmc())) { 
						jgPoliceStationFList.add(station);
					}
				}
				Log.i(TAG, qymc + "-" + jgmc + "  �����񾯸�ͤ:" + jgPoliceStationFList.size());
				jgPoliceStationFMap.put(jgmc, jgPoliceStationFList);
				// �����ܼƾ�Ա��ͤ
				List<Station> jgPoliceStationTList = new ArrayList<Station>(jgPoliceStationFList);
				jgPoliceStationTList.addAll(0, jgPoliceStationOList);
				Log.i(TAG, qymc + "-" + jgmc + "  �ܼ��񾯸�ͤ:" + jgPoliceStationTList.size());
				jgPoliceStationTMap.put(jgmc, jgPoliceStationTList);
				
				// ��������������ͤ
				List<Station> jgNormalStationOList = new ArrayList<Station>();
				for (int i = 0; i < qyNormalStationOList.size(); i++) {
					Station station = qyNormalStationOList.get(i);
					if (jgmc.equals(station.getJgmc())) { 
						jgNormalStationOList.add(station);
					}
				}
				Log.i(TAG, qymc + "-" + jgmc + "  ����������ͤ:" + jgNormalStationOList.size());
				jgNormalStationOMap.put(jgmc, jgNormalStationOList);
				// ��������������ͤ
				List<Station> jgNormalStationFList = new ArrayList<Station>();
				for (int i = 0; i < qyNormalStationFList.size(); i++) {
					Station station = qyNormalStationFList.get(i);
					if (jgmc.equals(station.getJgmc())) { 
						jgNormalStationFList.add(station);
					}
				}
				Log.i(TAG, qymc + "-" + jgmc + "  ����������ͤ:" + jgNormalStationFList.size());
				jgNormalStationFMap.put(jgmc, jgNormalStationFList);
				// �����ܼ�������ͤ
				List<Station> jgNormalStationTList = new ArrayList<Station>(jgNormalStationFList);
				jgNormalStationTList.addAll(0, jgNormalStationOList);
				Log.i(TAG, qymc + "-" + jgmc + "  �ܼ�������ͤ:" + jgNormalStationTList.size());
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
	 * ����title����
	 * @param index 1��һ��Ŀ¼����-�����أ��� 	2������Ŀ¼�������أ�-���� 	3����Ա�����б�	4��������ͤ�����б�	5���񾯸�ͤ�����б�	6�����������б�
	 * @param mc ������������
	 */
	public void setTitleText(int levelIndex, int stateSign, int typeSign, String mc){
		switch(levelIndex){
		case 1:
			mFormTitle.setText("������	���߾���ͳ�Ʊ�");
			break;
		case 2:
			mFormTitle.setText(mc + "	���߾���ͳ�Ʊ�");
			break;
		case 3:
			//stateSign 1 ����  	2����	3����
			//typeSign  1 ��Ա  	2������ͤ���ƶ���ͤ��	3�񾯸�ͤ���ΰ���ͤ��	4����
			switch(stateSign){
			case 1:
				switch(typeSign){
				case 1:
					mListTitle.setText(mc + "	��Ա�����б�");
					break;
				case 2:
					mListTitle.setText(mc + "	������ͤ�����б�");
					break;
				case 3:
					mListTitle.setText(mc + "	�񾯸�ͤ�����б�");
					break;
				case 4:
					mListTitle.setText(mc + "	���������б�");
					break;
				}
				break;
			case 2:
				switch(typeSign){
				case 1:
					mListTitle.setText(mc + "	��Ա�����б�");
					break;
				case 2:
					mListTitle.setText(mc + "	������ͤ�����б�");
					break;
				case 3:
					mListTitle.setText(mc + "	�񾯸�ͤ�����б�");
					break;
				case 4:
					mListTitle.setText(mc + "	���������б�");
					break;
				}
				break;
			case 3:
				switch(typeSign){
				case 1:
					mListTitle.setText(mc + "	��Ա�ܼ��б�");
					break;
				case 2:
					mListTitle.setText(mc + "	������ͤ�ܼ��б�");
					break;
				case 3:
					mListTitle.setText(mc + "	�񾯸�ͤ�ܼ��б�");
					break;
				case 4:
					mListTitle.setText(mc + "	�����ܼ��б�");
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

	// ����
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

	// ����
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
				if(((Station) light).getSfmj() != null && ((Station) light).getSfmj().equals("��")){
					list_mingjin.add(((Station) light));
				}
				if(((Station) light).getSfmj() != null && ((Station) light).getSfmj().equals("��")){
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
