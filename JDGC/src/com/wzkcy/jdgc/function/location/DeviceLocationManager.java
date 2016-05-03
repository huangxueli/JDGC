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
	private boolean mStartLocation; // �Ƿ�������λ����
	private boolean mAutoPanOnce;// �Զ�ƽ�Ƶ���λ��
	private boolean mAutoPanFollow; // ��ʶ�Ƿ���涨λ���ƶ�
	private boolean mLocateByCell  = false; // ʹ�û�վ��λ
	private Handler mHandler;
	private HttpCommunication mHttpCommunication;
	
	private GraphicsLayer mDeviceLocationLayer; // ��λ��ͼ��
	
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
		// ���ͼ��
		mDeviceLocationLayer = new GraphicsLayer();
		mMap.addLayer(mDeviceLocationLayer);
	}
	
	// �´ζ�λʱ�Զ�ƽ�Ƶ���λ��
	public void AutoPanToLocationNextTime() {
		if(mLocationDisplayManager.getPoint()!=null){
			mLocationDisplayManager.setAutoPanMode(AutoPanMode.LOCATION);
			mAutoPanOnce = false;
		} else{
			Util.Toast("��ǰ�豸��ʱ�޷���ȡλ����Ϣ");
			Log.i(TAG, "��ȡ��ǰλ��ʧ��");
			if(mLocateByCell){
				// ������վ��λ
				Cell cell = Util.GetCellInfo(mContext);
				if(cell!=null){
					if(cell.getSim_cid()>0){
						if(DeviceLocationSetting.Debug) Log.i(TAG, "CID:" + cell.getSim_cid() + "	LAC:" + cell.getSim_lac());
						mHttpCommunication.getLocationByCell(cell);
					}else{
						Util.Toast("�޷���ȡ��վ���");	
					}
				}
				return;
			}
			
		}
	}
	
	// ������λ����
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
						Log.i(TAG, "λ�ñ仯��" + point.getX() + " " + point.getY());
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
		// �˹���λ��һ�������ֿƼ���¥
		Point point = new Point(120.69485726071322, 27.983214196811428);
		doAfterLocationSucceed(point);
	}
	// ���÷�ΧԲ����ʽ
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
	// ���ö�λ�����ʽ
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
            // ����λʱ����Ϊ�ر�����Ա�������
            Map<String, Object> attributes = new HashMap<String, Object>();
            String time = DateFormat.format("yyyy-MM-dd kk:mm:ss", System.currentTimeMillis()).toString();
            attributes.put(ATTRIBUTES_TAG_TIME, time);
        }
	}
	
	public Point getCurrentLocation(){
		return mCurrPoint;
	}
	
	public void doAfterLocationSucceed(Point point) throws Exception{
		addTimeAtrribute(point);// ��¼��λʱ��
		
		if(mAutoPanFollow){
			mLocationDisplayManager.setAutoPanMode(AutoPanMode.LOCATION);
		}else{
			if(mAutoPanOnce){ 
				mAutoPanOnce = false;
				mMap.zoomToScale(point, 52223.0000000);// ���������ó�ʼ��ͼ ������
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
				if(DeviceLocationSetting.Debug) Log.i(TAG, "��վ��λ�����" + longitude + "	" + latitude);
				Point point = new Point(Double.valueOf(longitude), Double.valueOf(latitude));
				try {
					doAfterLocationSucceed(point);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				Util.Toast("�Ҳ�����صĻ�վ�� CID��" + cid + "	  LAC:" + lac);
			}
			break;
		case Constants.MESSAGE_WHAT_GET_CELLLOCATION_EMPTY:
			Util.Toast("��λʧ�ܣ��Ҳ�����ػ�վ");
			break;
		case Constants.MESSAGE_WHAT_GET_CELLLOCATION_F:
			
			break;

		default:
			break;
		}
		return false;
	}

}
