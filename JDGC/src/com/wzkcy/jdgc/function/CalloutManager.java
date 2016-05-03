package com.wzkcy.jdgc.function;

import java.util.List;
import java.util.Map;

import android.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.esri.android.map.Callout;
import com.esri.android.map.CalloutStyle;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.wzkcy.arcgismapapi.ArcGISRouteQuerier;
import com.wzkcy.jdgc.MainActivity;
import com.wzkcy.jdgc.MyApplication;
import com.wzkcy.jdgc.R;
import com.wzkcy.jdgc.bean.Policeman;
import com.wzkcy.jdgc.bean.Station;
import com.wzkcy.jdgc.fragment.MainFragment;
import com.wzkcy.jdgc.fragment.MainFragment.PowerLevel;
import com.wzkcy.jdgc.fragment.PolicemanFragment;
import com.wzkcy.jdgc.fragment.StationFragment;
import com.wzkcy.jdgc.function.GraphicsPointMove.OnMoveListener;
import com.wzkcy.jdgc.function.location.DeviceLocationManager;
import com.wzkcy.jdgc.setting.Util;

public class CalloutManager {
	
	private MainFragment mMainFragment;
	private MainActivity mMainActivity;
	private MapView mMap;
	private Callout mCallout;
	private PowerLevel mPowerLevel;
	private OnMoveListener mOnMoveListener;
	private List<Station> mPoliceStationList;
	private List<Station> mNormalStationList;
	private List<Policeman> mPolicemanList;
	
	public CalloutManager(MainFragment mainFragment, MapView map){
		this.mMainFragment = mainFragment;
		this.mMainActivity = (MainActivity)mMainFragment.getActivity();
		this.mMap = map;
	}
	public OnMoveListener getMoveListener(){
		return mOnMoveListener;
	}
	public static void HideLightCallout(MapView map){
		Callout callout = map.getCallout();
		callout.hide();
	}
	public void ShowLightCallout(float x, float y){
		GraphicsLayer layer = null;
		mPowerLevel = mMainFragment.getPowerLevel();
		layer = mMainFragment.getPoliceStationPointLayer();
		int gid = ArcgisTool.getGraphicId(mMap, x, y, layer);
		if(gid>0) {
			ShowPoliceStationCallout(x, y);
		}else {
			layer = mMainFragment.getNormalStationPointLayer();
			gid = ArcgisTool.getGraphicId(mMap, x, y, layer);
			if(gid>0) {
				ShowNormalStationCallout(x, y);
			}else{
				layer = mMainFragment.getPolicemanPointLayer();
				gid = ArcgisTool.getGraphicId(mMap, x, y, layer);
				if(gid>0) {
					ShowPolicemanCallout(x, y);
				}else{
					mCallout = mMap.getCallout();
					mCallout.hide();
				}
			}
		}
	}
	
	private void ShowPoliceStationCallout(float x, float y){
		final GraphicsLayer layer = mMainFragment.getPoliceStationPointLayer();
		final int gid = ArcgisTool.getGraphicId(mMap, x, y, layer);
		mCallout = null;
		mCallout = mMap.getCallout();
		mPoliceStationList = mMainFragment.getPoliceStationData();
		mNormalStationList = mMainFragment.getNormalStationData();
		mPolicemanList = mMainFragment.getPolicemanData();
		Graphic graphic = layer.getGraphic(gid);
		Map<String, Object> att = graphic.getAttributes();
		String dm = (String)att.get(ArcgisTool.ATTRIBUTE_KEY_ID);
		Station station = null;
		for(Station temp : mPoliceStationList){
			if(temp.getDm().equals(dm)){
				station = temp;
				break;
			}
		}
		if(station==null){
			Util.Toast("数据未找到！");
			return;
		}
		final Geometry geo = graphic.getGeometry();
		View view = MyApplication.Inflater.inflate(R.layout.callout_station_move, null);
		TextView gtmc = (TextView)view.findViewById(R.id.gtmc);
		gtmc.setText(station.getGtmc());
		TextView zqmj = (TextView)view.findViewById(R.id.zqmj);
		String mjxms = station.getPoliceXmStr().trim();
		if (!mjxms.equals("")) {
			mjxms = mjxms.replaceAll("&", " ");
		}
		zqmj.setText(mjxms);
		TextView sfzx = (TextView)view.findViewById(R.id.sfzx);
		if(station.isOn_guard())
			sfzx.setText("是");
		else
			sfzx.setText("否");
		TextView dqwz = (TextView)view.findViewById(R.id.dqwz);
		dqwz.setText(station.getGtdz());
		TextView lxdh = (TextView)view.findViewById(R.id.lxdh);
		lxdh.setText(station.getLxdh());
		Point point = new Point(Double.parseDouble(station.getGps_jd()),
				Double.parseDouble(station.getGps_wd()));
		mMap.centerAt(point, false);
		
		ImageButton move = (ImageButton)view.findViewById(R.id.move);
		move.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Graphic point = layer.getGraphic(gid);
				if(point!=null){
					mOnMoveListener = GraphicsPointMove.Prepare(mMainFragment, layer, gid);
					if(mCallout.isShowing())
						mCallout.hide();
				}
			}
		});
		if(mPowerLevel == PowerLevel.SEE){
			move.setVisibility(View.INVISIBLE);
		}
		ImageButton detail = (ImageButton)view.findViewById(R.id.detail);
		detail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Graphic point = layer.getGraphic(gid);
				Map<String, Object> attributes = point.getAttributes();
				String dm = (String)attributes.get(ArcgisTool.ATTRIBUTE_KEY_ID);
				Station station = null;
				for(Station data:mPoliceStationList){
					if(data.getDm().equals(dm.trim())){
						station = data;
					}
				}
				if(point!=null){
					StationFragment mStationFragment = new StationFragment(station, mMainActivity);  
					FragmentTransaction transaction = mMainFragment.getFragmentManager().beginTransaction(); 
			        transaction.hide(mMainFragment);
			        transaction.add(R.id.container, mStationFragment);
			        transaction.addToBackStack(null);  
			        transaction.commit(); 
			        mMainFragment.replaceLogoView();
				}
			}
		});
		ImageButton navigation = (ImageButton)view.findViewById(R.id.navigation);
		navigation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArcGISRouteQuerier mArcGISRouteQuerier = mMainFragment.getArcGISRouteQuerier();
				DeviceLocationManager mDeviceLocationManager = mMainFragment.getDeviceLocationManager();
				Point startPoint = mDeviceLocationManager.getCurrentLocation();
				if(startPoint!=null){
					mArcGISRouteQuerier.clearPoint();
					startPoint = mMap.toScreenPoint(startPoint);
					int startUid = mArcGISRouteQuerier.addPoint((float)startPoint.getX(), (float)startPoint.getY());
					Point endPoint = (Point)geo;
					endPoint = mMap.toScreenPoint(endPoint);
					int endUid = mArcGISRouteQuerier.addPoint((float)endPoint.getX(), (float)endPoint.getY());
					if(startUid>0 && endUid>0){
						mArcGISRouteQuerier.query();
						mCallout.hide();
					}
				} else{
					Util.Toast("未定位到当前位置，无法规划路径");
				}
			}
		});
		CalloutStyle style = new CalloutStyle();
		style.setBackgroundAlpha(R.color.transparent);
		style.setFrameColor(MyApplication.Resources.getColor(R.color.transparent));
		style.setMaxHeight(1920);
		style.setMaxWidth(1080);
		mCallout.setContent(view);
		mCallout.setStyle(style);
//		mCallout.setOffset(0, 5);
		mCallout.setCoordinates((Point)geo);
		mCallout.show();
	}
	
	private void ShowNormalStationCallout(float x, float y){
		final GraphicsLayer layer = mMainFragment.getNormalStationPointLayer();
		final int gid = ArcgisTool.getGraphicId(mMap, x, y, layer);
		mCallout = null;
		mCallout = mMap.getCallout();
		mPoliceStationList = mMainFragment.getPoliceStationData();
		mNormalStationList = mMainFragment.getNormalStationData();
		mPolicemanList = mMainFragment.getPolicemanData();
		Graphic graphic = layer.getGraphic(gid);
		Map<String, Object> att = graphic.getAttributes();
		String dm = (String)att.get(ArcgisTool.ATTRIBUTE_KEY_ID);
		Station station = null;
		for(Station temp : mNormalStationList){
			if(temp.getDm().equals(dm)){
				station = temp;
				break;
			}
		}
		if(station==null){
			Util.Toast("数据未找到！");
			return;
		}
		final Geometry geo = graphic.getGeometry();
		View view = MyApplication.Inflater.inflate(R.layout.callout_station_move, null);
		TextView gtmc = (TextView)view.findViewById(R.id.gtmc);
		gtmc.setText(station.getGtmc());
		TextView zqmj = (TextView)view.findViewById(R.id.zqmj);
		String zqmjs = station.getPoliceXmStr().trim();
		if(!zqmjs.equals("")){
			zqmjs = zqmjs.replaceAll("&", " ");
		}
		zqmj.setText(zqmjs);
		TextView sfzx = (TextView)view.findViewById(R.id.sfzx);
		if(station.isOn_guard())
			sfzx.setText("是");
		else
			sfzx.setText("否");
		TextView dqwz = (TextView)view.findViewById(R.id.dqwz);
		dqwz.setText(station.getGtdz());
		TextView lxdh = (TextView)view.findViewById(R.id.lxdh);
		lxdh.setText(station.getLxdh());
		Point point = new Point(Double.parseDouble(station.getGps_jd()),
				Double.parseDouble(station.getGps_wd()));
		mMap.centerAt(point, false);
		ImageButton move = (ImageButton)view.findViewById(R.id.move);
		move.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Graphic point = layer.getGraphic(gid);
				if(point!=null){
					mOnMoveListener = GraphicsPointMove.Prepare(mMainFragment, layer, gid);
					if(mCallout.isShowing())
						mCallout.hide();
				}
			}
		});
		if(mPowerLevel == PowerLevel.SEE){
			move.setVisibility(View.INVISIBLE);
		}
		ImageButton detail = (ImageButton)view.findViewById(R.id.detail);
		detail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Graphic point = layer.getGraphic(gid);
				Map<String, Object> attributes = point.getAttributes();
				String dm = (String)attributes.get(ArcgisTool.ATTRIBUTE_KEY_ID);
				Station station = null;
				for(Station data:mNormalStationList){
					if(data.getDm().equals(dm.trim())){
						station = data;
					}
				}
				if(point!=null){
					StationFragment mStationFragment = new StationFragment(station, mMainActivity);  
					FragmentTransaction transaction = mMainFragment.getFragmentManager().beginTransaction(); 
			        transaction.hide(mMainFragment);
			        transaction.add(R.id.container, mStationFragment);
			        transaction.addToBackStack(null);  
			        transaction.commit(); 
			        mMainFragment.replaceLogoView();
				}
			}
		});
		ImageButton navigation = (ImageButton)view.findViewById(R.id.navigation);
		navigation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArcGISRouteQuerier mArcGISRouteQuerier = mMainFragment.getArcGISRouteQuerier();
				DeviceLocationManager mDeviceLocationManager = mMainFragment.getDeviceLocationManager();
				Point startPoint = mDeviceLocationManager.getCurrentLocation();
				startPoint = mMap.toScreenPoint(startPoint);
				if(startPoint!=null){
					mArcGISRouteQuerier.clearPoint();
					int startUid = mArcGISRouteQuerier.addPoint((float)startPoint.getX(), (float)startPoint.getY());
					Point endPoint = (Point)geo;
					endPoint = mMap.toScreenPoint(endPoint);
					int endUid = mArcGISRouteQuerier.addPoint((float)endPoint.getX(), (float)endPoint.getY());
					if(startUid>0 && endUid>0){
						mArcGISRouteQuerier.query();
						mCallout.hide();
					}
				} else{
					Util.Toast("未定位到当前位置，无法规划路径");
				}
				
			}
		});
		CalloutStyle style = new CalloutStyle();
		style.setBackgroundAlpha(R.color.transparent);
		style.setFrameColor(MyApplication.Resources.getColor(R.color.transparent));
		style.setMaxHeight(1920);
		style.setMaxWidth(1080);
		mCallout.setContent(view);
		mCallout.setStyle(style);
//		mCallout.setOffset(0, 5);
		mCallout.setCoordinates((Point)geo);
		mCallout.show();
		
	}
	
	private void ShowPolicemanCallout(float x, float y){
		final GraphicsLayer layer = mMainFragment.getPolicemanPointLayer();
		final int gid = ArcgisTool.getGraphicId(mMap, x, y, layer);
		mCallout = null;
		mCallout = mMap.getCallout();
		mPoliceStationList = mMainFragment.getPoliceStationData();
		mNormalStationList = mMainFragment.getNormalStationData();
		mPolicemanList = mMainFragment.getPolicemanData();
		if (gid > 0) {
			Graphic graphic = layer.getGraphic(gid);
			Map<String, Object> att = graphic.getAttributes();
			String jh = (String)att.get(ArcgisTool.ATTRIBUTE_KEY_ID);
			Policeman policeman = null;
			for(Policeman temp : mPolicemanList){
				if(temp.getMjjh().equals(jh)){
					policeman = temp;
					break;
				}
			}
			if(policeman==null){
				Util.Toast("数据未找到！");
				return;
			}
			final Geometry geo = graphic.getGeometry();
			View view = MyApplication.Inflater.inflate(R.layout.callout_policeman, null);
			TextView mjxm = (TextView)view.findViewById(R.id.mjxm);
			mjxm.setText(policeman.getMjxm());
			TextView mjjh = (TextView)view.findViewById(R.id.mjjh);
			mjjh.setText(policeman.getMjjh());
			TextView sjhm = (TextView)view.findViewById(R.id.sjhm);
			sjhm.setText(policeman.getSjhm());
			TextView jgmc = (TextView)view.findViewById(R.id.jgmc);
			jgmc.setText(policeman.getJgmc());
			TextView sfzh = (TextView)view.findViewById(R.id.sfzh);
			sfzh.setText(policeman.getSfzh());
			Point point = new Point(Double.parseDouble(policeman.getGps_jd()),
					Double.parseDouble(policeman.getGps_wd()));
			mMap.centerAt(point, false);
			ImageButton detail = (ImageButton)view.findViewById(R.id.detail);
			detail.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Graphic point = layer.getGraphic(gid);
					Map<String, Object> attributes = point.getAttributes();
					String jh = (String)attributes.get(ArcgisTool.ATTRIBUTE_KEY_ID);
					Policeman policeman = null;
					for(Policeman data:mPolicemanList){
						if(data.getMjjh().equals(jh.trim())){
							policeman = data;
						}
					}
					if(point!=null){
						PolicemanFragment mPolicemanFragment = new PolicemanFragment(policeman, mMainActivity);  
						FragmentTransaction transaction = mMainFragment.getFragmentManager().beginTransaction(); 
				        transaction.hide(mMainFragment);
				        transaction.add(R.id.container, mPolicemanFragment);
				        transaction.addToBackStack(null);  
				        transaction.commit(); 
				        mMainFragment.replaceLogoView();
					}
				}
			});
			ImageButton navigation = (ImageButton)view.findViewById(R.id.navigation);
			navigation.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ArcGISRouteQuerier mArcGISRouteQuerier = mMainFragment.getArcGISRouteQuerier();
					DeviceLocationManager mDeviceLocationManager = mMainFragment.getDeviceLocationManager();
					Point startPoint = mDeviceLocationManager.getCurrentLocation();
					if(startPoint!=null){
						mArcGISRouteQuerier.clearPoint();
						startPoint = mMap.toScreenPoint(startPoint);
						int startUid = mArcGISRouteQuerier.addPoint((float)startPoint.getX(), (float)startPoint.getY());
						Point endPoint = (Point)geo;
						endPoint = mMap.toScreenPoint(endPoint);
						int endUid = mArcGISRouteQuerier.addPoint((float)endPoint.getX(), (float)endPoint.getY());
						if(startUid>0 && endUid>0){
							mArcGISRouteQuerier.query();
							mCallout.hide();
						}
					} else{
						Util.Toast("未定位到当前位置，无法规划路径");
					}
				}
			});
			CalloutStyle style = new CalloutStyle();
			style.setBackgroundAlpha(R.color.transparent);
			style.setFrameColor(MyApplication.Resources.getColor(R.color.transparent));
			style.setMaxHeight(1920);
			style.setMaxWidth(1080);
			mCallout.setContent(view);
			mCallout.setStyle(style);
//			mCallout.setOffset(0, 5);
			mCallout.setCoordinates((Point)geo);
			mCallout.show();
			
		}else{
			mCallout.hide();
		}
	}
}
