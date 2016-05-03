package com.wzkcy.jdgc.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jsqlite.Database;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.wzkcy.arcgismapapi.ArcGISMapManager;
import com.wzkcy.arcgismapapi.ArcGISMapManager.MAPTYPE;
import com.wzkcy.arcgismapapi.ArcGISPlaceNameQuerier;
import com.wzkcy.arcgismapapi.ArcGISRouteQuerier;
import com.wzkcy.arcgismapapi.ArcGISSetting;
import com.wzkcy.jdgc.MainActivity;
import com.wzkcy.jdgc.MyApplication;
import com.wzkcy.jdgc.R;
import com.wzkcy.jdgc.bean.JG;
import com.wzkcy.jdgc.bean.JGS;
import com.wzkcy.jdgc.bean.Policeman;
import com.wzkcy.jdgc.bean.Powerman;
import com.wzkcy.jdgc.bean.QY;
import com.wzkcy.jdgc.bean.Station;
import com.wzkcy.jdgc.database.RoutePointTable;
import com.wzkcy.jdgc.database.RouteTable;
import com.wzkcy.jdgc.function.ArcgisTool;
import com.wzkcy.jdgc.function.Cache;
import com.wzkcy.jdgc.function.CalloutManager;
import com.wzkcy.jdgc.function.DrawLineManager;
import com.wzkcy.jdgc.function.location.DeviceLocationManager;
import com.wzkcy.jdgc.function.location.DeviceLocationManager.LocationChangeListener;
import com.wzkcy.jdgc.function.messure.MessureDistanceManager;
import com.wzkcy.jdgc.function.route.RouteTakenManager;
import com.wzkcy.jdgc.http.HttpCommunication;
import com.wzkcy.jdgc.listener.MyOnSingleTapListener;
import com.wzkcy.jdgc.listener.MyOnSingleTapListener.OperaterKind;
import com.wzkcy.jdgc.setting.Constants;
import com.wzkcy.jdgc.setting.TempData;
import com.wzkcy.jdgc.setting.Util;

public class MainFragment extends Fragment implements Callback {

	public final static String TAG = "MainFragment";

	private MainActivity mMainActivity;
	private MapView mMap;
	private Handler mHandler;
	private Database mDatabase;
	private HttpCommunication mHttpCommunication;
	private Cache mCache;

	private OperaterKind mCurrOperaterKind = OperaterKind.SELECT;
	private PowerLevel mPowerLevel = PowerLevel.SEE; 
	private int mDisplayLevel = 3; // 1：最小 	2：普通	 3： 最大 
	
	private ArcGISMapManager mArcGISMapManager;
	private MessureDistanceManager mMessureDistanceManager;
	private RouteTakenManager mRouteTakenManager;
	private DeviceLocationManager mDeviceLocationManager;
	private DrawLineManager mDrawLineManager;
	private ArcGISRouteQuerier mArcGISRouteQuerier;
	private ArcGISPlaceNameQuerier mArcGISPlaceNameQuerier;

	private GraphicsLayer mCurLocationLayer; // 定位点图层
	private GraphicsLayer mPoliceStationLayer; // 治安岗亭图层
	private GraphicsLayer mNormalStationLayer; // 移动岗亭图层
	private GraphicsLayer mPolicemanLayer; // 警员图层
	private GraphicsLayer mLineLayer; // 兴趣线图层
	private GraphicsLayer mDeviceTextLayer; // 项目名称图层
	
	private boolean mDMQueryInLongPress = false;
	private boolean mLJQueryInLongPress = false;

	public enum LightKind {
		PoliceStation, NormalStation, Policeman
	}
	
	public enum PowerLevel{
		SEE, MOVE, EDIT;
	}
	private View mMarkView;
	private boolean mShowTextGraphic = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		mMainActivity = (MainActivity) getActivity();
		mHandler = new Handler(this);
		mDatabase = mMainActivity.getDatabase();
		mCache = mMainActivity.getCache();
	}

	@Override
	public void onResume() {
		Log.e(TAG, "onResume");
		CalloutManager.HideLightCallout(mMap);
		super.onResume();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Log.e(TAG, "onHiddenChanged");
//		getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // 清空Fragment 栈底
		int count = getFragmentManager().getBackStackEntryCount();
		Log.i(TAG, "BackStackEntryCount:" + count);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "onCreateView");
		View view = inflater.inflate(R.layout.fragment_main, null);
		mMarkView = view.findViewById(R.id.mark);

		// 初始化地图
		initArcgisMap(view);
		// 初始化缩进、缩出、定位按钮
		initAreaButtonGroup(view);
		// 初始化图层切换按钮
		initAreaMapSwitch(view);
		// 初始化底层栏
		initBottomBar(view);
		// 开始发送网络请求
		startLoadData();
		// 使用样例数据
//		useTempData();

		return view;
	}
	
	// 开始加载数据 
	public void startLoadData(){
		new Thread() {
			public void run() {
				Looper.prepare();
				getHttpCommunication().getQyGroup();
				Looper.loop();
			};
		}.start();
	}
	
	// 清空地图
	public void clearMap() {
		GraphicsLayer stationLayer = getPoliceStationPointLayer();
		stationLayer.removeAll();
		GraphicsLayer stationMoveLayer = getNormalStationPointLayer();
		stationMoveLayer.removeAll();
		GraphicsLayer policemanLayer = getPolicemanPointLayer();
		policemanLayer.removeAll();
		GraphicsLayer linelayer = getInterestLineLayer();
		linelayer.removeAll();
		GraphicsLayer textlayer = getDeviceTextLayer();
		textlayer.removeAll();
	}

	// 消除高亮
	public void removeHighlight() {
		GraphicsLayer stationLayer = getPoliceStationPointLayer();
		stationLayer.clearSelection();
		GraphicsLayer stationMoveLayer = getNormalStationPointLayer();
		stationMoveLayer.clearSelection();
		GraphicsLayer policemanLayer = getPolicemanPointLayer();
		policemanLayer.clearSelection();
	}

	private void initAreaButtonGroup(View view) {
		ImageButton mLocationBtn = (ImageButton) view.findViewById(R.id.LocationBtn);
		mLocationBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDeviceLocationManager.AutoPanToLocationNextTime();
				mRouteTakenManager.drawHistoryRoute(2);
			}
		});

		ImageButton mZoomInBtn = (ImageButton) view.findViewById(R.id.ZoomInBtn);
		mZoomInBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMap.zoomin();
				Log.i(TAG, "当前比例尺：" + mMap.getScale());
//				Util.Toast("最大比例尺：" + mMap.getMaxScale());
//				Util.Toast("当前比例尺：" + mMap.getScale());
//				Util.Toast("最小比例尺：" + mMap.getMinScale());
			}
		});
		ImageButton mZoomOutBtn = (ImageButton) view.findViewById(R.id.ZoomOutBtn);
		mZoomOutBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMap.zoomout();
			}
		});
	}

	private void initAreaMapSwitch(View view) {
		RadioButton mTurnVector = (RadioButton) view.findViewById(R.id.turnVector);
		mTurnVector.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mArcGISMapManager.switchMap(MAPTYPE.SL);
				}
			}
		});
		RadioButton mTurnTwofive = (RadioButton) view.findViewById(R.id.turnTwofive);
		mTurnTwofive.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mArcGISMapManager.switchMap(MAPTYPE.M25D);
				}
			}
		});
		RadioButton mTurnImage = (RadioButton) view.findViewById(R.id.turnImage);
		mTurnImage.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mArcGISMapManager.switchMap(MAPTYPE.YX);
				}
			}
		});
	}

	private View mMessageArea;
	private TextView mBottomBarPrompt;
	private CheckBox mPoliceStationBtn;
	private CheckBox mNormalStationBtn;
	private CheckBox mMeasureDistanceBtn;
	private CheckBox mPolicemanBtn;

	private View initBottomBar(View view) {

		mMessageArea = view.findViewById(R.id.MessageArea);
		mBottomBarPrompt = (TextView) view.findViewById(R.id.BottomBarPrompt);
		mPoliceStationBtn = (CheckBox) view.findViewById(R.id.stationBtn);
		mNormalStationBtn = (CheckBox) view.findViewById(R.id.stationmoveableBtn);
		mPolicemanBtn = (CheckBox) view.findViewById(R.id.policemanBtn);
		mMeasureDistanceBtn = (CheckBox) view.findViewById(R.id.measureDistanceBtn);
		mPoliceStationBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					getPoliceStationPointLayer().setVisible(true);
				} else {
					getPoliceStationPointLayer().setVisible(false);
					CalloutManager.HideLightCallout(mMap);
				}
			}
		});
		mNormalStationBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					getNormalStationPointLayer().setVisible(true);
				} else {
					getNormalStationPointLayer().setVisible(false);
					CalloutManager.HideLightCallout(mMap);
				}
			}
		});
		mPolicemanBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					getPolicemanPointLayer().setVisible(true);
				} else {
					getPolicemanPointLayer().setVisible(false);
					CalloutManager.HideLightCallout(mMap);
				}
			}
		});
		return view;
	}

	public void SetMessageAreaText(String text) {
		mMessageArea.setVisibility(View.VISIBLE);
		mBottomBarPrompt.setText(text);
	}

	private void initArcgisMap(View view) {
		mMap = (MapView) view.findViewById(R.id.mapView);
		mMap.setEsriLogoVisible(false);
		mMap.setMapBackground(0xffffff, 0xffffff, 0, 0);// 设置地图背景全白无网格
//		Geometry geometry = new Envelope(120.50078347988259, 27.84615906356171, 121.06339000144553, 28.240359954088245);
//		Geometry geometry = new Envelope(119.620115, 27.045428, 121.268482, 28.615260);
//		mMap.setExtent(geometry);// 设置地图范围
		Point initpoint = new Point(120.69485726071322, 27.983214196811428);
//		initpoint = mMap.toScreenPoint(initpoint);
		mMap.zoomTo(initpoint, (float)mMap.getResolution());
		ArcGISRuntime.setClientId("1eFHW78avlnRUPHm");// 去除水印

//		 mMap.setMaxResolution(3.437082168079019E-4); // 设置最大分辨率
//		 mMap.setMinResolution(2.6852217450794713E-6);// 设置最小分辨率
//		 mMap.setResolution(3.437082168079019E-4);

		String mLocalMapDirRootPath = Util.getLocalMapDirRootPath(); // 离线地图根目录
		
		ArcGISSetting mArcGISSetting = new ArcGISSetting(mMainActivity, false, true, Constants.CACHEPATH);
		mArcGISSetting.MAP_SERVICE_PATH_SL = Constants.MAP_SERVICE_PATH1;
		mArcGISSetting.MAP_SERVICE_PATH_YX = Constants.MAP_SERVICE_PATH2;
		mArcGISMapManager = new ArcGISMapManager(mMainActivity, mMap, mArcGISSetting);
		mArcGISMapManager.setMapWhite(true);
		mArcGISMapManager.setDebug(true);
		mArcGISMapManager.init();
		mArcGISMapManager.switchMap(MAPTYPE.SL);
		// 测距功能管理器
		mMessureDistanceManager = new MessureDistanceManager(mMap, MyApplication.Resources);
		// 路线记录管理器
		mRouteTakenManager = new RouteTakenManager(mMap, new RouteTable(mDatabase), new RoutePointTable(mDatabase));
		// 设备定位功能管理器
		mDeviceLocationManager = new DeviceLocationManager(mMainActivity, mMap,
				new LocationChangeListener() {
					@Override
					public void onDeviceLocationChanged(Point point) {
						MenuFragment mMenuFragment = mMainActivity.getMenuFragment();
						mMenuFragment.setLongitudeLatitude(point.getX(), point.getY());
						mRouteTakenManager.recordPostion(point);
					}
				});
		mDrawLineManager = new DrawLineManager(this, getInterestLineLayer());
		mArcGISRouteQuerier = new ArcGISRouteQuerier(mMap, mMainActivity);
		mArcGISRouteQuerier.setUrl(Constants.MAP_SERVICE_ROUTE_WZ);
		mArcGISRouteQuerier.setSegmentSelectable(true);
		
		mArcGISPlaceNameQuerier = new ArcGISPlaceNameQuerier(mMainActivity, mMap);
		mArcGISPlaceNameQuerier.setQueryExtent(0.001);
		mArcGISPlaceNameQuerier.setUrl(Constants.MAP_SERVICE_DMDZ_WZ);
		mArcGISPlaceNameQuerier.setAttributeKey("JZWQC");
		mArcGISPlaceNameQuerier.setShowResultTextPoint(true);
		mArcGISPlaceNameQuerier.setDebug(true);
		mArcGISPlaceNameQuerier.setTextPointImageRes(R.drawable.icon_mark_pt);
		
		mMap.setOnSingleTapListener(new MyOnSingleTapListener(this));
		mMap.setOnStatusChangedListener(new OnStatusChangedListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onStatusChanged(Object source, STATUS status) {
				if (status == STATUS.INITIALIZED) {
					if (mDeviceLocationManager.AllowLocation()) {
						mDeviceLocationManager.StartLocation();
					}
				}
			}
		});
		mMap.setOnLongPressListener(new OnLongPressListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public boolean onLongPress(float x, float y) {
				if(mDMQueryInLongPress){
					mArcGISPlaceNameQuerier.find(x, y, mHandler);
				}
				if(mLJQueryInLongPress){
					mArcGISRouteQuerier.query();
				}
				
				return false;
			}
		});
		mMap.setOnZoomListener(new OnZoomListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void preAction(float pivotX, float pivotY, double factor) {
				
			}

			@Override
			public void postAction(float pivotX, float pivotY, double factor) {
				int currentScale = (int)mMap.getScale();
				Log.i(TAG, "当前比例尺：" + currentScale);
				/*if ((int) currentScale < 4000) {
					if (mShowTextGraphic != true) {
						mShowTextGraphic = true;
						mDeviceTextLayer.setVisible(true);
					}
				} else {
					if (mShowTextGraphic != false) {
						mShowTextGraphic = false;
						mDeviceTextLayer.setVisible(false);
					}
				}
				diffIconByScale(currentScale, 20000, 10000);
				if(currentScale>=20000){
					if(mDisplayLevel != 1){
						mDisplayLevel = 1;
						showNormalStationGraphic(mNormalStationList, mDisplayLevel, false);
						showPoliceStationGraphic(mPoliceStationList, mDisplayLevel, false);
						showPolicemanGraphic(mPolicemanList, mDisplayLevel, false);
					} 
					
				}else if(currentScale>=10000){
					if(mDisplayLevel != 2){
						mDisplayLevel = 2;
						showNormalStationGraphic(mNormalStationList, mDisplayLevel, false);
						showPoliceStationGraphic(mPoliceStationList, mDisplayLevel, false);
						showPolicemanGraphic(mPolicemanList, mDisplayLevel, false);
					}
				}else{
					if(mDisplayLevel != 3){
						mDisplayLevel = 3;
						showNormalStationGraphic(mNormalStationList, mDisplayLevel, false);
						showPoliceStationGraphic(mPoliceStationList, mDisplayLevel, false);
						showPolicemanGraphic(mPolicemanList, mDisplayLevel, false);
					}
				}*/
			}
		});
	}
	
	public int getDisplayLevel(){
		return mDisplayLevel;
	}
	
	public MapView getMap() {
		return mMap;
	}

	public Handler getHandler() {
		return mHandler;
	}

	public HttpCommunication getHttpCommunication() {
		if (mHttpCommunication == null) {
			mHttpCommunication = new HttpCommunication(mMainActivity, mHandler);
		}
		return mHttpCommunication;
	}

	public GraphicsLayer getPoliceStationPointLayer() {
		if (mPoliceStationLayer == null) {
			mPoliceStationLayer = new GraphicsLayer();
			mMap.addLayer(mPoliceStationLayer);
		}
		return mPoliceStationLayer;
	}

	public GraphicsLayer getNormalStationPointLayer() {
		if (mNormalStationLayer == null) {
			mNormalStationLayer = new GraphicsLayer();
			mMap.addLayer(mNormalStationLayer);
		}
		return mNormalStationLayer;
	}

	public GraphicsLayer getPolicemanPointLayer() {
		if (mPolicemanLayer == null) {
			mPolicemanLayer = new GraphicsLayer();
			mMap.addLayer(mPolicemanLayer);
		}
		return mPolicemanLayer;
	}

	public GraphicsLayer getInterestLineLayer() {
		if (mLineLayer == null) {
			mLineLayer = new GraphicsLayer();
			mMap.addLayer(mLineLayer);
		}
		return mLineLayer;
	}

	public GraphicsLayer getDeviceTextLayer() {
		if (mDeviceTextLayer == null) {
			mDeviceTextLayer = new GraphicsLayer();
			mMap.addLayer(mDeviceTextLayer);
		}
		return mDeviceTextLayer;
	}

	public GraphicsLayer getLocationLayer() {
		if (mCurLocationLayer == null) {
			mCurLocationLayer = new GraphicsLayer();
			mMap.addLayer(mCurLocationLayer);
		}
		return mCurLocationLayer;
	}

	private List<Station> mPoliceStationList;
	private List<Station> mNormalStationList;
	private List<Policeman> mPolicemanList;
	private List<QY> mQYList;
	private List<JGS> mJGList;
	private List<Powerman> mPowermenList;
	private int count = 0;
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case Constants.MESSAGE_WHAT_GETQYGROUP_S:
			mQYList = (List<QY>) msg.obj;
			sortQY(mQYList);
			count = 0;
			new Thread() {
				public void run() {
					Looper.prepare();
					getHttpCommunication().getJGroup();
					Looper.loop();
				};
			}.start();
			break;
		case Constants.MESSAGE_WHAT_GETQYGROUP_F1:
//			Util.ShowDialog(mMainActivity, "获取区域列表失败： JSON解析失败");
			break;
		case Constants.MESSAGE_WHAT_GETQYGROUP_F2:
			if(count<4){
				count++;
				Log.e(TAG, "count:" + count);
				startLoadData();
			} else {
				String errormsg = (String)msg.obj;
				Util.Toast(errormsg);
			}
			break;
		case Constants.MESSAGE_WHAT_GETJGROUP_S:
			mJGList = (List<JGS>) msg.obj;
			count = 0;
			for(int i=0; i<mJGList.size();i++){
				JGS jgs = mJGList.get(i);
				List<JG> newjglist = jgs.getJgList();
				JG jg = new JG();
				jg.setJgmc(" ");
				jg.setJgbm("-1");
				newjglist.add(0, jg);
				jgs.setJgList(newjglist);
			}
			new Thread() {
				public void run() {
					Looper.prepare();
					getHttpCommunication().getPoliceStation();
					Looper.loop();
				};
			}.start();
			break;
		case Constants.MESSAGE_WHAT_GETJGROUP_F1:
//			Util.ShowDialog(mMainActivity, "获取机构列表失败： JSON解析失败");
			break;
		case Constants.MESSAGE_WHAT_GETJGROUP_F2:
			if(count<4){
				count++;
				Log.e(TAG, "count:" + count);
				new Thread() {
					public void run() {
						Looper.prepare();
						getHttpCommunication().getJGroup();
						Looper.loop();
					};
				}.start();
			} else {
				String errormsg = (String)msg.obj;
				Util.Toast(errormsg);
			}
			break;
		case Constants.MESSAGE_WHAT_GET_POLICE_STATION_S:
			mPoliceStationList = (List<Station>) msg.obj;
			count = 0;
			showPoliceStationGraphic(mPoliceStationList, mDisplayLevel, true, mPoliceStationBtn.isChecked()); 
			new Thread() {
				public void run() {
					Looper.prepare();
					getHttpCommunication().getNormalStation();
					Looper.loop();
				};
			}.start();
			break;
		case Constants.MESSAGE_WHAT_GET_POLICE_STATION_F:
			if(count<4){
				count++;
				Log.e(TAG, "count:" + count);
				new Thread() {
					public void run() {
						Looper.prepare();
						getHttpCommunication().getPoliceStation();
						Looper.loop();
					};
				}.start();
			} else {
				String errormsg = (String)msg.obj;
				Util.Toast(errormsg);
			}
			break;
		case Constants.MESSAGE_WHAT_GET_NORMAL_STATION_S:
			mNormalStationList = (List<Station>) msg.obj;
			showNormalStationGraphic(mNormalStationList, mDisplayLevel, true, mNormalStationBtn.isChecked());
			count = 0;
			new Thread(){
				public void run() {
					Looper.prepare();
					getHttpCommunication().getOnlinePoliceman();
					Looper.loop();
				};
			}.start();
			break;
		case Constants.MESSAGE_WHAT_GET_NORMAL_STATION_F:
			if(count<4){
				count++;
				Log.e(TAG, "count:" + count);
				new Thread() {
					public void run() {
						Looper.prepare();
						getHttpCommunication().getNormalStation();
						Looper.loop();
					};
				}.start();
			} else {
				String errormsg = (String)msg.obj;
				Util.Toast(errormsg);
			}
			break;
		case Constants.MESSAGE_WHAT_GET_POLICEMAN_S:
			mPolicemanList = (List<Policeman>) msg.obj;
			showPolicemanGraphic(mPolicemanList, mDisplayLevel, true, mPolicemanBtn.isChecked());
			break;
		case Constants.MESSAGE_WHAT_GET_POLICEMAN_F:

			break;
		case Constants.MESSAGE_WHAT_GET_ONLINE_POLICEMAN_S:
			mPolicemanList = (List<Policeman>) msg.obj;
			count = 0;
			showPolicemanGraphic(mPolicemanList, mDisplayLevel, true, mPolicemanBtn.isChecked());
			break;
		case Constants.MESSAGE_WHAT_GET_ONLINE_POLICEMAN_F:
			if(count<4){
				count++;
				Log.e(TAG, "count:" + count);
				new Thread() {
					public void run() {
						Looper.prepare();
						getHttpCommunication().getOnlinePoliceman();
						Looper.loop();
					};
				}.start();
			} else {
				String errormsg = (String)msg.obj;
				Util.Toast(errormsg);
			}
			break;
		case Constants.MESSAGE_WHAT_FIX_LOCATION_S:
			boolean isSucceed = (boolean) msg.obj;
			if (!isSucceed) {
				Util.ShowDialog(mMainActivity, "修改岗亭位置失败：数据库操作失败");
			} else {
				Util.Toast("操作成功");
			}
			break;
		case Constants.MESSAGE_WHAT_FIX_LOCATION_F1:
//			Util.ShowDialog(mMainActivity, "修改岗亭位置失败： 返回未知符号");
			break;
		case Constants.MESSAGE_WHAT_FIX_LOCATION_F2:
//			Util.ShowDialog(mMainActivity, "修改岗亭位置失败： Http访问失败");
			break;
		case Constants.MESSAGE_WHAT_GET_POWERMAN_S:
			mPowermenList = (List<Powerman>) msg.obj;
			for(Powerman man : mPowermenList){
				if(man.getMjjh().trim().equals(Constants.PAccount)){
					mPowerLevel = PowerLevel.MOVE;
				}
			}
			break;
		case Constants.MESSAGE_WHAT_GET_POWERMAN_F:
			
			break;
		case ArcGISPlaceNameQuerier.MESSAGE_WHAT_FIND:
			if(mArcGISPlaceNameQuerier.isGetAllAttributes()){
				Map<String, Object> attributes = (Map<String, Object>)msg.obj;
				String dm = (String)attributes.get("JZWQC");
				String xgr = (String)attributes.get("XGR");
				Util.Toast(dm + "	" + xgr);
				
			}else{
				String diming = (String)msg.obj;
				Util.Toast(diming);
			}
			break;	
		case ArcGISPlaceNameQuerier.MESSAGE_WHAT_NOT_FIND:
			Util.Toast("未找到结果");
			break;	
		default:
			break;
		}
		return false;
	}

	private void sortQY(List<QY> QYList) {
		List<QY> temp = new ArrayList<QY>(QYList); 
		while(QYList.size()>0){
			if(QYList!=null){
				for(QY qy:QYList){
					if(qy.getXzqh_mc().contains("鹿城")){
						temp.remove(0);
						temp.add(0, qy);
						QYList.remove(qy);
						break;
					}else if(qy.getXzqh_mc().contains("龙湾")){
						temp.remove(1);
						temp.add(1, qy);
						QYList.remove(qy);
						break;
					}else if(qy.getXzqh_mc().contains("瓯海")){
						temp.remove(2);
						temp.add(2, qy);
						QYList.remove(qy);
						break;
					}else if(qy.getXzqh_mc().contains("洞头")){
						temp.remove(3);
						temp.add(3, qy);
						QYList.remove(qy);
						break;
					}else if(qy.getXzqh_mc().contains("开发")){
						temp.remove(4);
						temp.add(4, qy);
						QYList.remove(qy);
						break;
					}else if(qy.getXzqh_mc().contains("永嘉")){
						temp.remove(5);
						temp.add(5, qy);
						QYList.remove(qy);
						break;
					}else if(qy.getXzqh_mc().contains("乐清")){
						temp.remove(6);
						temp.add(6, qy);
						QYList.remove(qy);
						break;
					}else if(qy.getXzqh_mc().contains("瑞安")){
						temp.remove(7);
						temp.add(7, qy);
						QYList.remove(qy);
						break;
					}else if(qy.getXzqh_mc().contains("平阳")){
						temp.remove(8);
						temp.add(8, qy);
						QYList.remove(qy);
						break;
					}else if(qy.getXzqh_mc().contains("苍南")){
						temp.remove(9);
						temp.add(9, qy);
						QYList.remove(qy);
						break;
					}else if(qy.getXzqh_mc().contains("文成")){
						temp.remove(10);
						temp.add(10, qy);
						QYList.remove(qy);
						break;
					}else if(qy.getXzqh_mc().contains("泰顺")){
						temp.remove(11);
						temp.add(11, qy);
						QYList.remove(qy);
						break;
					}
				}
			}
			
		}
		mQYList = temp;
	}

	// 使用示例数据
	public void useTempData() {
		mPoliceStationList = JSON.parseArray(TempData.StationData, Station.class);
		mNormalStationList = JSON.parseArray(TempData.StationMoveableData, Station.class);
		mPolicemanList = JSON.parseArray(TempData.PolicemanData, Policeman.class);
		showPoliceStationGraphic(mPoliceStationList, mDisplayLevel, true, mPoliceStationBtn.isChecked());
		showNormalStationGraphic(mNormalStationList, mDisplayLevel, true, mNormalStationBtn.isChecked());
	}

	public List<Station> getPoliceStationData() {
		if (mPoliceStationList == null) {
			mPoliceStationList = new ArrayList<Station>();
		}
		return mPoliceStationList;
	}

	public List<Station> getNormalStationData() {
		if (mNormalStationList == null) {
			mNormalStationList = new ArrayList<Station>();
		}
		return mNormalStationList;
	}

	public List<Policeman> getPolicemanData() {
		if (mPolicemanList == null) {
			mPolicemanList = new ArrayList<Policeman>();
		}
		return mPolicemanList;
	}

	public List<String> getQYStringListData() {
		List<String> list = new ArrayList<String>();
		if (mQYList != null) {
			for (QY qy : mQYList) {
				list.add(qy.getXzqh_mc());
			}
			list.add(0, " ");
		}
		return list;
	}

	public List<QY> getQYListData() {
		if (mQYList == null) {
			mQYList = new ArrayList<QY>();
		}
		return mQYList;
	}

	public List<JG> getJGListData(String qymc) {
		List<JG> list = new ArrayList<JG>();
		if (mJGList == null) {
			mJGList = new ArrayList<JGS>();
		}
		if (qymc != null && !qymc.equals("")) {
			for (JGS jgs : mJGList) {
				if (jgs.getXzqh_mc().equals(qymc)) {
					List<JG> jgList = jgs.getJgList();
					if (jgList != null) {
						list = jgList;
					}
					break;
				}
			}
		}
		return list;
	}

	public List<String> getJGStringListData(String qymc) {
		List<String> list = new ArrayList<String>();
		if (mJGList == null) {
			mJGList = new ArrayList<JGS>();
		}
		if (qymc != null && !qymc.equals("")) {
			for (JGS jgs : mJGList) {
				if (jgs.getXzqh_mc().equals(qymc)) {
					List<JG> jgList = jgs.getJgList();
					if (jgList != null) {
						for (JG jg : jgList)
							list.add(jg.getJgmc());
					}
					break;
				}
			}
		}
		return list;
	}
	// 添加联防岗亭点
	public void showNormalStationGraphic(final List<Station> list, final int mDisplayLevel, final boolean add, final boolean visible) {
		if(list==null) return;
		new Thread() {
			public void run() {
				int[] resid = new int[2];
				switch(mDisplayLevel){
				case 1:
					resid = new int[]{R.drawable.normal_station_small_f, R.drawable.normal_station_small_n};
					break;
				case 2:
					resid = new int[]{R.drawable.normal_station_mid_f, R.drawable.normal_station_mid_n};
					break;
				case 3:
					resid = new int[]{R.drawable.normal_station_f, R.drawable.normal_station_n};
					break;
				}
				GraphicsLayer layer = getNormalStationPointLayer();
				layer.setVisible(visible);
				if(add){
					for (Station station : list) {
						if (station.getGps_jd() == null || station.getGps_wd() == null 
								|| station.getGps_jd().equals("") || station.getGps_wd().equals("")) {
							continue;
						}
						double jd = Double.parseDouble(station.getGps_jd());
						double wd = Double.parseDouble(station.getGps_wd());
						Point point = new Point(jd, wd);
						int graphicid = -1;
						if (station.isOn_guard()) {
							graphicid = ArcgisTool.addGraphic(point, resid[0], layer);
						} else {
							graphicid = ArcgisTool.addGraphic(point, resid[1], layer);
						}
						ArcgisTool.updateGraphicAttributes(graphicid, layer, station.getDm().trim(),
								LightKind.NormalStation, station.isOn_guard());
					}
				}else{
					int[] graphicIDs = layer.getGraphicIDs();
					if(graphicIDs!=null){
						for(int id:graphicIDs){
							Graphic graphic = layer.getGraphic(id);
							if(graphic!=null){
								boolean isOn_guard = (boolean)graphic.getAttributeValue(ArcgisTool.ATTRIBUTE_KEY_ONLINE);
								if(isOn_guard){
									ArcgisTool.updateGraphicSymbol(id, resid[0], layer);
								}else{
									ArcgisTool.updateGraphicSymbol(id, resid[1], layer);
								}
							}
						}
					}
				}
				
			}
		}.start();
	}
	// 添加民警岗亭点
	public void showPoliceStationGraphic(final List<Station> list, final int mDisplayLevel, final boolean add, final boolean visible) {
		if(list==null) return;
		new Thread() {
			public void run() {
				int[] resid = new int[2];
				switch(mDisplayLevel){
				case 1:
					resid = new int[]{R.drawable.police_station_small_f, R.drawable.police_station_small_n};
					break;
				case 2:
					resid = new int[]{R.drawable.police_station_mid_f, R.drawable.police_station_mid_n};
					break;
				case 3:
					resid = new int[]{R.drawable.police_station_f, R.drawable.police_station_n};
					break;
				}
				GraphicsLayer layer = getPoliceStationPointLayer();
				layer.setVisible(visible);
				if(add){
					for (Station station : list) {
						if (station.getGps_jd() == null|| station.getGps_wd() == null 
								|| station.getGps_jd().equals("") || station.getGps_wd().equals("")) {
							continue;
						}
						double jd = Double.parseDouble(station.getGps_jd());
						double wd = Double.parseDouble(station.getGps_wd());
						Point point = new Point(jd, wd);
						int graphicid = -1;
						if (station.isOn_guard()) {
							graphicid = ArcgisTool.addGraphic(point, resid[0], layer);
						} else {
							graphicid = ArcgisTool.addGraphic(point, resid[1], layer);
						}
						ArcgisTool.updateGraphicAttributes(graphicid, layer,
								station.getDm().trim(), LightKind.PoliceStation, station.isOn_guard());
					}
				} else{
					int[] graphicIDs = layer.getGraphicIDs();
					
					if(graphicIDs!=null){
						for(int id:graphicIDs){
							Graphic graphic = layer.getGraphic(id);
							if(graphic!=null){
								boolean isOn_guard = (boolean)graphic.getAttributeValue(ArcgisTool.ATTRIBUTE_KEY_ONLINE);
								if(isOn_guard){
									ArcgisTool.updateGraphicSymbol(id, resid[0], layer);
								} else {
									ArcgisTool.updateGraphicSymbol(id, resid[1], layer);
								}
							}
						}
					}
				}
			};
		}.start();
	}
	// 添加民警点
	public void showPolicemanGraphic(final List<Policeman> list, final int mDisplayLevel, final boolean add, final boolean visible) {
		if(list == null) return;
		new Thread() {
			public void run() {
				int[] resid = new int[1];
				switch(mDisplayLevel){
				case 1:
					resid = new int[]{R.drawable.policeman_small};
					break;
				case 2:
					resid = new int[]{R.drawable.policeman_mid};
					break;
				case 3:
					resid = new int[]{R.drawable.policeman};
					break;
				}
				GraphicsLayer layer = getPolicemanPointLayer();
				layer.setVisible(visible);
				if(add){
					for (Policeman policeman : list) {
						if (policeman.getGps_jd() == null || policeman.getGps_wd() == null 
								|| policeman.getGps_jd().equals("") || policeman.getGps_wd().equals("")) {
							continue;
						}
						double jd = Double.parseDouble(policeman.getGps_jd());
						double wd = Double.parseDouble(policeman.getGps_wd());
						Point point = new Point(jd, wd);
						int graphicid = -1;
						if (policeman.isOn_guard()) {
							graphicid = ArcgisTool.addGraphic(point, resid[0], layer);
						}
						ArcgisTool.updateGraphicAttributes(graphicid, layer, 
								policeman.getMjjh().trim(), LightKind.Policeman, policeman.isOn_guard());
					}
					
				} else{
					int[] graphicIDs = layer.getGraphicIDs();
					if(graphicIDs!=null){
						for(int id:graphicIDs){
							Graphic graphic = layer.getGraphic(id);
							if(graphic!=null){
								boolean isOn_guard = (boolean)graphic.getAttributeValue(ArcgisTool.ATTRIBUTE_KEY_ONLINE);
								if(isOn_guard){
									ArcgisTool.updateGraphicSymbol(id, resid[0], layer);
								}else{
									ArcgisTool.updateGraphicSymbol(id, resid[1], layer);
								}
							}
						}
					}
				}
			};
		}.start();
	}

	public void replaceLogoView() {
		mMainActivity.replaceLogoView(false);
	}

	public OperaterKind getOperaterKind() {
		return mCurrOperaterKind;
	}

	public void setOperaterKind(OperaterKind kind) {
		mCurrOperaterKind = kind;
	}
	public PowerLevel getPowerLevel() {
		return mPowerLevel;
	}

	public View getMarkView() {
		return mMarkView;
	}

	public String CheckMessureLine(float x, float y) {
		String length = mMessureDistanceManager.CheckMessureLine(x, y);
		if (mMeasureDistanceBtn.isChecked() && length != null) {
			SetMessageAreaText(length);
		}
		return length;
	}

	public RouteTakenManager getRouteTakenManager() {
		return mRouteTakenManager;
	}
	public ArcGISRouteQuerier getArcGISRouteQuerier() {
		return mArcGISRouteQuerier;
	}
	public ArcGISPlaceNameQuerier getArcGISPlaceNameQuerier() {
		return mArcGISPlaceNameQuerier;
	}
	public DeviceLocationManager getDeviceLocationManager() {
		return mDeviceLocationManager;
	}
	public DrawLineManager getDrawLineManager() {
		return mDrawLineManager;
	}

	private long mDelayMillis = 1000L;

	public void toLocation(final Point point, final GraphicsLayer layer) {
		getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		mMap.centerAt(point, false);
		mMap.zoomToScale(point, 4513.997733376551);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Point mScreenPoint = mMap.toScreenPoint(point);
				int graphicId = ArcgisTool.getGraphicId(mMap,
						(float) mScreenPoint.getX(),
						(float) mScreenPoint.getY(), layer);
				if (graphicId > -1) {
					layer.clearSelection();
					layer.setSelectedGraphics(new int[] { graphicId }, true);
				}

			}
		}, mDelayMillis);
		mDelayMillis = 0;
	}

	public boolean isDMQueryInLongPress() {
		return mDMQueryInLongPress;
	}

	public void setDMQueryInLongPress(boolean b) {
		this.mDMQueryInLongPress = b;
	}

	public boolean isLJQueryInLongPress() {
		return mLJQueryInLongPress;
	}

	public void setLJQueryInLongPress(boolean b) {
		this.mLJQueryInLongPress = b;
	}

}
