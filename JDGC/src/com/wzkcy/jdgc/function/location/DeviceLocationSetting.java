package com.wzkcy.jdgc.function.location;

import android.graphics.Color;

public class DeviceLocationSetting {
	/**
	 * ����
	 */
	public static final boolean Debug = false;
	/**
	 * �Ƿ�������λ����
	 */
	public static boolean StartLocation = true;
	/**
	 * �Ƿ�ʹ�����綨λ
	 */
	public static boolean AllowNetworkLocation = false; 
	/**
	 * �Զ�ƽ�Ƶ���λ��
	 */
	public static boolean AutoPanOnce = true;
	/**
	 * �Ƿ���涨λ���ƶ�
	 */
	public static boolean AutoPanFollow = false;
	/**
	 * �Ƿ񻭷�ΧԲ
	 */
	public static boolean DrawAccuracy = false;
	/**
	 * �϶���ͼʱ�Ƿ���ʾ��λ��
	 */
	public static boolean UseCourseSymbolOnMovement = true;
	/**
	 * ��ΧԲ��ԴͼƬ��
	 */
	public static final Integer AccuracyResourceID = null; // R.drawable.accuracy; 
	/**
	 * ��ΧԲ������ɫ
	 */
	public static final String AccuracyColorString = "#000000"; 
	/**
	 * ��λ����ԴͼƬ��
	 */
	public static final Integer PointResourceID = null; 
	/**
	 * ��λ�������ɫ
	 */
	public static final Integer PointColor = Color.GREEN; 
}
