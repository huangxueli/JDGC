package com.wzkcy.jdgc.function.location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.LocationDisplayManager.AutoPanMode;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.PictureFillSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.wzkcy.jdgc.MyApplication;
import com.wzkcy.jdgc.bean.Cell;
import com.wzkcy.jdgc.http.HttpCommunication;
import com.wzkcy.jdgc.setting.Constants;
import com.wzkcy.jdgc.setting.Util;

public class DeviceLocationManager implements Callback{
	
	public static final String TAG = "DeviceLocationManager";
	public static final String ATTRIBUTES_TAG_TIME = "Time";
	
	private MapView mMap;
	private Context mContext;
	private boolean mStartLocation; // 是否启动定位功能
	private boolean mAutoPanOnce;// 自动平移到定位点
	private boolean mAutoPanFollow; // 标识是否跟随定位点移动
	private boolean mLocateByCell  = false; // 使用基站定位
	private Handler mHandler;
	private HttpCommunication mHttpCommunication;
	
	private GraphicsLayer mDeviceLocationLayer; // 定位点图层
	
	private LocationDisplayManager mLocationDisplayManager;
	private LocationChangeListener mLocationChangeListener;
	
	private Point mCurrPoint;
	
	public DeviceLocationManager(Context context, MapView mMap, LocationChangeListener mLocationChangeListener){
		this.mContext = context;
		this.mMap = mMap;
		this.mHandler = new Handler(this);
		this.mHttpCommunication = new HttpCommunication(context, mHandler);
		this.mLocationChangeListener = mLocationChangeListener;
		this.mLocationDisplayManager = mMap.getLocationDisplayManager();
		
		this.mStartLocation = DeviceLocationSetting.StartLocation;
		this.mAutoPanOnce = DeviceLocationSetting.AutoPanOnce;
		this.mAutoPanFollow = DeviceLocationSetting.AutoPanFollow;
		// 添加图层
		mDeviceLocationLayer = new GraphicsLayer();
		mMap.addLayer(mDeviceLocationLayer);
	}
	
	// 下次定位时自动平移到定位点
	public void AutoPanToLocationNextTime() {
		if(mLocationDisplayManager.getPoint()!=null){
			mLocationDisplayManager.setAutoPanMode(AutoPanMode.LOCATION);
			mAutoPanOnce = false;
		} else{
			Util.Toast("当前设备暂时无法获取位置信息");
			Log.i(TAG, "获取当前位置失败");
			if(mLocateByCell){
				// 启动基站定位
				Cell cell = Util.GetCellInfo(mContext);
				if(cell!=null){
					if(cell.getSim_cid()>0){
						if(DeviceLocationSetting.Debug) Log.i(TAG, "CID:" + cell.getSim_cid() + "	LAC:" + cell.getSim_lac());
						mHttpCommunication.getLocationByCell(cell);
					}else{
						Util.Toast("无法获取基站编号");	
					}
				}
				return;
			}
			
		}
	}
	
	// 开启定位功能
	public void StartLocation(){
		try{
			mLocationDisplayManager.setAccuracyCircleOn(DeviceLocationSetting.DrawAccuracy);
			InitAccuracySymbolStyle(mLocationDisplayManager, DeviceLocationSetting.AccuracyResourceID, DeviceLocationSetting.AccuracyColorString);
//			InitLocationSymbolStyle(DeviceLocationSetting.PointResourceID, DeviceLocationSetting.PointColor);
			mLocationDisplayManager.setLocationListener(new CurrentLocationListener());
			mLocationDisplayManager.setAutoPanMode(AutoPanMode.LOCATION);
			mLocationDisplayManager.setUseCourseSymbolOnMovement(DeviceLocationSetting.UseCourseSymbolOnMovement);
			mLocationDisplayManager.setAllowNetworkLocation(DeviceLocationSetting.AllowNetworkLocation);
			mLocationDisplayManager.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public boolean AllowLocation(){
		return mStartLocation;
	}
	
	private class CurrentLocationListener implements LocationListener{
		
		@Override
		public void onLocationChanged(Location location) {
			
			Point point = mLocationDisplayManager.getPoint();
			try {
				if (point != null){
					if(DeviceLocationSetting.Debug){
						Log.i(TAG, "位置变化：" + point.getX() + " " + point.getY());
						Util.Toast(point.getX() + " " + point.getY());
					}
					mCurrPoint = point;
					doAfterLocationSucceed(point);
					
				} else{
					locationByHuman();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			Util.Toast("GPS Enabled");
		}

		@Override
		public void onProviderDisabled(String provider) {
			Util.Toast("GPS Disabled");
			try {
				locationByHuman();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void locationByHuman() throws Exception{
		// 人工定位到一个公安局科技大楼
		Point point = new Point(120.69485726071322, 27.983214196811428);
		doAfterLocationSucceed(point);
	}
	// 设置范围圆的样式
	private void InitAccuracySymbolStyle(LocationDisplayManager mLocationDisplayManager, 
			Integer mAccuracyResourceID, String mAccuracyColorString) throws Exception{
		FillSymbol mAccuracySymbol = null;
		if(mAccuracyResourceID != null){
			Drawable mDrawable =  MyApplication.Resources.getDrawable(mAccuracyResourceID);
			SimpleLineSymbol mSimpleLineSymbol = new SimpleLineSymbol(Color.BLACK, 1.0F, SimpleLineSymbol.STYLE.NULL);
			mAccuracySymbol = new PictureFillSymbol(mDrawable, mSimpleLineSymbol);
		} else{
//				mAccuracySymbol = new SimpleFillSymbol(Color.parseColor(mAccuracyColorString), SimpleFillSymbol.STYLE.SOLID);
		}
		mLocationDisplayManager.setAccuracySymbol(mAccuracySymbol);
	}
	// 设置定位点的样式
	@SuppressWarnings("unused")
	private void InitLocationSymbolStyle(Integer mPointResourceID, int mPointColor) throws Exception{
		MarkerSymbol mLocationSymbol = null;
		if(mPointResourceID != null && mPointResourceID != -1){
			Drawable drawable =  MyApplication.Resources.getDrawable(mPointResourceID);
			mLocationSymbol = new PictureMarkerSymbol(drawable);
		} else{
			mLocationSymbol = new SimpleMarkerSymbol(mPointColor, 16, STYLE.CIRCLE);
		}
		mLocationDisplayManager.setLocationAcquiringSymbol(mLocationSymbol);
	}
	
	private void addTimeAtrribute(Point geometry) throws Exception{
		if (mDeviceLocationLayer != null) {
            // 将定位时间作为特别的属性保存起来
            Map<String, Object> attributes = new HashMap<String, Object>();
            String time = DateFormat.format("yyyy-MM-dd kk:mm:ss", System.currentTimeMillis()).toString();
            attributes.put(ATTRIBUTES_TAG_TIME, time);
        }
	}
	
	public Point getCurrentLocation(){
		return mCurrPoint;
	}
	
	public void doAfterLocationSucceed(Point point) throws Exception{
		addTimeAtrribute(point);// 记录定位时间
		
		if(mAutoPanFollow){
			mLocationDisplayManager.setAutoPanMode(AutoPanMode.LOCATION);
		}else{
			if(mAutoPanOnce){ 
				mAutoPanOnce = false;
				mMap.zoomToScale(point, 52223.0000000);// 在这里设置初始地图 比例尺
			}else {
				mLocationDisplayManager.setAutoPanMode(AutoPanMode.OFF);
			}
		}
		
		if (mLocationChangeListener != null) {
			mLocationChangeListener.onDeviceLocationChanged(point);
		}
	}
	public static abstract class LocationChangeListener {
		public abstract void onDeviceLocationChanged(Point point);
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case Constants.MESSAGE_WHAT_GET_CELLLOCATION_S:
			@SuppressWarnings("unchecked")
			List<Cell> list = (List<Cell>) msg.obj;
			int cid = msg.arg1;
			int lac = msg.arg2;
			if(list.size()>0){
				Cell cell = list.get(0);
				String latitude = cell.getGps_wd();
				String longitude = cell.getGps_jd();
				if(DeviceLocationSetting.Debug) Log.i(TAG, "基站定位结果：" + longitude + "	" + latitude);
				Point point = new Point(Double.valueOf(longitude), Double.valueOf(latitude));
				try {
					doAfterLocationSucceed(point);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				Util.Toast("找不到相关的基站点 CID：" + cid + "	  LAC:" + lac);
			}
			break;
		case Constants.MESSAGE_WHAT_GET_CELLLOCATION_EMPTY:
			Util.Toast("定位失败：找不到相关基站");
			break;
		case Constants.MESSAGE_WHAT_GET_CELLLOCATION_F:
			
			break;

		default:
			break;
		}
		return false;
	}

}
