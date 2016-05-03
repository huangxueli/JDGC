package com.wzkcy.jdgc.function.location;

import android.graphics.Color;

public class DeviceLocationSetting {
	/**
	 * 调试
	 */
	public static final boolean Debug = false;
	/**
	 * 是否启动定位功能
	 */
	public static boolean StartLocation = true;
	/**
	 * 是否使用网络定位
	 */
	public static boolean AllowNetworkLocation = false; 
	/**
	 * 自动平移到定位点
	 */
	public static boolean AutoPanOnce = true;
	/**
	 * 是否跟随定位点移动
	 */
	public static boolean AutoPanFollow = false;
	/**
	 * 是否画范围圆
	 */
	public static boolean DrawAccuracy = false;
	/**
	 * 拖动地图时是否显示定位点
	 */
	public static boolean UseCourseSymbolOnMovement = true;
	/**
	 * 范围圆资源图片号
	 */
	public static final Integer AccuracyResourceID = null; // R.drawable.accuracy; 
	/**
	 * 范围圆区域颜色
	 */
	public static final String AccuracyColorString = "#000000"; 
	/**
	 * 定位点资源图片号
	 */
	public static final Integer PointResourceID = null; 
	/**
	 * 定位点填充颜色
	 */
	public static final Integer PointColor = Color.GREEN; 
}
