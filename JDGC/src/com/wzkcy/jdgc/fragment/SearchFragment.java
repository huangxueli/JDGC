package com.wzkcy.jdgc.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.wzkcy.jdgc.MainActivity;
import com.wzkcy.jdgc.R;
import com.wzkcy.jdgc.adapter.SearchResultAdapter;
import com.wzkcy.jdgc.bean.Light;
import com.wzkcy.jdgc.bean.Policeman;
import com.wzkcy.jdgc.bean.SearchResult;
import com.wzkcy.jdgc.bean.Station;
import com.wzkcy.jdgc.http.HttpCommunication;
import com.wzkcy.jdgc.setting.Constants;
import com.wzkcy.jdgc.setting.Util;

public class SearchFragment extends Fragment implements Callback {

	public static final String TAG = "SearchFragment";

	private MainActivity mMainActivity;
	private MainFragment mMainFragment;
	private Handler mHandler;
	private HttpCommunication mHttpCommunication;
	private EditText mSearchEdit;
	private TextView mResultNum;
	private ArrayAdapter<String> mAdapter2;
	private ArrayAdapter<String> mAdapter3;
	private String mKeyValue = "";
	private String mSpinner2Value = "";
	private String mSpinner3Value = "";
	private ArrayList<Light> mResultList = new ArrayList<Light>();
	private SearchResultAdapter mSearchResultAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainActivity = (MainActivity) getActivity();
		mMainFragment = mMainActivity.getMainFragment();
		mHandler = new Handler(this);
		mHttpCommunication = new HttpCommunication(mMainActivity, mHandler);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search, null);
		mSearchEdit = (EditText) view.findViewById(R.id.searchEdit);
		final Spinner mLevel1Spinner = (Spinner) view.findViewById(R.id.level1Spinner);
		final Spinner mLevel2Spinner = (Spinner) view.findViewById(R.id.level2Spinner);
		final Spinner mLevel3Spinner = (Spinner) view.findViewById(R.id.level3Spinner);
		ImageButton mSearchBtn = (ImageButton) view.findViewById(R.id.searchBtn);
		mSearchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mKeyValue = mSearchEdit.getText().toString();
				mHttpCommunication.searchByKey(mSpinner2Value, mSpinner3Value, mKeyValue);
			}
		});
		mResultNum = (TextView) view.findViewById(R.id.result_num);
		mResultNum.setText("共有" + mResultList.size() + "条");
		final ArrayAdapter<CharSequence> mEmptyAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.empty,
						R.layout.simple_spinner_item);
		mEmptyAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<CharSequence> mAdapter1 = ArrayAdapter.createFromResource(
				getActivity(), R.array.city, R.layout.simple_spinner_item);
		mAdapter1.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		mLevel1Spinner.setAdapter(mAdapter1);

		mAdapter2 = new ArrayAdapter<String>(getActivity(),
				R.layout.simple_spinner_item, mMainFragment.getQYStringListData());
		mAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		mLevel2Spinner.setAdapter(mEmptyAdapter);

		mAdapter3 = new ArrayAdapter<String>(getActivity(),
				R.layout.simple_spinner_item, new ArrayList<String>());
		mAdapter3.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		mLevel3Spinner.setAdapter(mEmptyAdapter);

		mLevel1Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				switch (arg2) {
				case 0:
					mLevel2Spinner.setAdapter(mEmptyAdapter);
					mSpinner2Value = "";
					mSpinner3Value = "";
					break;
				case 1:
					mAdapter2 = new ArrayAdapter<String>(getActivity(),
							R.layout.simple_spinner_item, mMainFragment.getQYStringListData());
					mAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
					mLevel2Spinner.setAdapter(mAdapter2);
					break;
				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Util.Toast("NothingSelected");
			}
		});
		mLevel2Spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				String qymc = arg0.getItemAtPosition(arg2).toString();
				mSpinner2Value = qymc;
				List<String> jgList = mMainFragment.getJGStringListData(qymc);
				mAdapter3 = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, jgList);
				mAdapter3.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
				mLevel3Spinner.setAdapter(mAdapter3);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		mLevel3Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mSpinner3Value = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		ListView mSearchList = (ListView) view.findViewById(R.id.searchList);
		mSearchResultAdapter = new SearchResultAdapter(mResultList);
		mSearchList.setAdapter(mSearchResultAdapter);
		mSearchList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Light light = (Light) mSearchResultAdapter.getItem(arg2);
				if (light instanceof Station) {
					StationFragment mStationFragment = new StationFragment(
							(Station) light, mMainActivity);
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.hide(SearchFragment.this);
					transaction.add(R.id.container, mStationFragment);
					transaction.addToBackStack(null);
					transaction.commit();
				} else {
					PolicemanFragment mPolicemanFragment = new PolicemanFragment(
							(Policeman) light, mMainActivity);
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.hide(SearchFragment.this);
					transaction.add(R.id.container, mPolicemanFragment);
					transaction.addToBackStack(null);
					transaction.commit();
				}
			}
		});
		return view;
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case Constants.MESSAGE_WHAT_SEARCHBYKEY_S:
			List<SearchResult> list = (List<SearchResult>) msg.obj;
			if (list.size() == 0) {
				Util.Toast("找不到相关记录");
				mResultNum.setText("共有0条");
				mResultList.clear();
				mSearchResultAdapter.notifyDataSetChanged();
			} else {
				SearchResult searchResult = list.get(0);
				List<Station> gtList = searchResult.getGt();
				List<Policeman> police1List = searchResult.getPolice();
				List<Policeman> police2List = mMainFragment.getPolicemanData();// 信息全一些
				List<Policeman> police3List = new ArrayList<Policeman>();
				for (Policeman police1 : police1List) {
					for (Policeman police2 : police2List) {
						if (police1.getMjjh().equals(police2.getMjjh())) {
							police3List.add(police2);
						}
					}
				}
				mResultList.clear();
				mResultList.addAll(gtList);
				mResultList.addAll(gtList.size(), police3List);
				mSearchResultAdapter.notifyDataSetChanged();
				mResultNum.setText("共有" + mResultList.size() + "条");
			}

			break;
		case Constants.MESSAGE_WHAT_SEARCHBYKEY_F:
			if (mKeyValue.equals("") && mSpinner2Value.equals("") && mSpinner3Value.equals("")) {
				Util.Toast("请输入关键字或者选择所属区域");
			} else {
				Util.Toast("搜索异常");
			}
			// 清空搜索结果列表
			mResultList.clear();
			mSearchResultAdapter.notifyDataSetChanged();
			mResultNum.setText("共有" + mResultList.size() + "条");
			break;
		}
		return false;
	}

}
