package com.wzkcy.jdgc.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esri.core.geometry.Point;
import com.wzkcy.jdgc.MainActivity;
import com.wzkcy.jdgc.R;
import com.wzkcy.jdgc.bean.Policeman;

public class PolicemanFragment extends Fragment {

	public static final String TAG = "PolicemanFragment";

	private MainActivity mMainActivity;
	private MainFragment mMainFragment;
	private Policeman mPoliceman;

	public PolicemanFragment(Policeman policeman, MainActivity mainActivity) {
		mMainActivity = mainActivity;
		mMainFragment = mMainActivity.getMainFragment();
		mPoliceman = policeman;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_policeman, container,
				false);
		ImageView picture = (ImageView) view.findViewById(R.id.picture);
		TextView mjxm = (TextView) view.findViewById(R.id.mjxm);
		mjxm.setText(mPoliceman.getMjxm());
		TextView mjjh = (TextView) view.findViewById(R.id.mjjh);
		mjjh.setText(mPoliceman.getMjjh());
		TextView sfzh = (TextView) view.findViewById(R.id.sfzh);
		sfzh.setText(mPoliceman.getSfzh());
		TextView sjhm = (TextView) view.findViewById(R.id.sjhm);
		sjhm.setText(mPoliceman.getSjhm());
		TextView jgbm = (TextView) view.findViewById(R.id.jgbm);
		jgbm.setText(mPoliceman.getJgbm());
		TextView jgmc = (TextView) view.findViewById(R.id.jgmc);
		jgmc.setText(mPoliceman.getJgmc());

		if (mPoliceman.getGps_jd() != null && mPoliceman.getGps_wd() != null && !mPoliceman.getGps_jd().equals("") && !mPoliceman.getGps_wd().equals("")) {
			Point point = new Point(Double.parseDouble(mPoliceman.getGps_jd()),
					Double.parseDouble(mPoliceman.getGps_wd()));
			mMainActivity.setRightBtnFunction(true, point,
					mMainFragment.getPolicemanPointLayer());
		} else {
			mMainActivity.setRightBtnFunction(true, null,
					mMainFragment.getPolicemanPointLayer());
		}
		return view;
	}
}
