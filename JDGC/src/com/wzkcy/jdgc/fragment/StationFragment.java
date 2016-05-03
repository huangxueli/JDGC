package com.wzkcy.jdgc.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.wzkcy.jdgc.MainActivity;
import com.wzkcy.jdgc.R;
import com.wzkcy.jdgc.bean.Station;
import com.wzkcy.jdgc.setting.Util;

public class StationFragment extends Fragment {

	public static final String TAG = "StationFragment";

	private MainActivity mMainActivity;
	private MainFragment mMainFragment;
	private Station mStation;

	public StationFragment(Station station, MainActivity mainActivity) {
		mMainActivity = mainActivity;
		mMainFragment = mMainActivity.getMainFragment();
		mStation = station;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_station, container,false);
		ImageView picture = (ImageView) view.findViewById(R.id.picture);
		TextView gtlx = (TextView) view.findViewById(R.id.gtlx);
		GraphicsLayer layer = null;
		if (mStation.getSfmj() != null && mStation.getSfmj().equals("是")) {
			picture.setImageResource(R.drawable.station);
			gtlx.setText("民警岗亭");
			layer = mMainFragment.getPoliceStationPointLayer();
		} else if (mStation.getSfmj() != null && mStation.getSfmj().equals("否")) {
			picture.setImageResource(R.drawable.lianf_station);
			gtlx.setText("联防岗亭");
			layer = mMainFragment.getNormalStationPointLayer();
		}
		TextView gtmc = (TextView) view.findViewById(R.id.gtmc);
		gtmc.setText(mStation.getGtmc());
		TextView gtbh = (TextView) view.findViewById(R.id.gtbh);
		gtbh.setText(mStation.getGtbh());
		TextView zqmj = (TextView) view.findViewById(R.id.zqmj);
		String mjxms = mStation.getPoliceXmStr().trim();
		if (!mjxms.equals("")) {
			mjxms = mjxms.replaceAll("&", " ");
		}
		zqmj.setText(mjxms);
		TextView xzqy = (TextView) view.findViewById(R.id.xzqy);
		xzqy.setText(mStation.getXzqh_mc());
		TextView jgmc = (TextView) view.findViewById(R.id.jgmc);
		jgmc.setText(mStation.getGtmc());
		TextView gtdz = (TextView) view.findViewById(R.id.gtdz);
		gtdz.setText(mStation.getGtdz());
		TextView jdbh = (TextView) view.findViewById(R.id.jdbh);
		jdbh.setText(mStation.getJdbh());
		TextView lxr = (TextView) view.findViewById(R.id.lxr);
		lxr.setText(mStation.getLxr());
		TextView lxdh = (TextView) view.findViewById(R.id.lxdh);
		lxdh.setText(mStation.getLxdh());
		TextView bz = (TextView) view.findViewById(R.id.bz);
		bz.setText(mStation.getBz());

		if (mStation.getGps_jd() != null && mStation.getGps_wd() != null) {
			Point point = new Point(Double.parseDouble(mStation.getGps_jd()),
					Double.parseDouble(mStation.getGps_wd()));
			mMainActivity.setRightBtnFunction(true, point, layer);
		} else {
			Util.Toast("该岗亭位置信息缺失，请查询数据确认！");
		}

		return view;
	}
}
