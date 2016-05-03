package com.wzkcy.jdgc.http;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.alibaba.fastjson.JSON;
import com.wzkcy.jdgc.bean.Cell;
import com.wzkcy.jdgc.bean.JGS;
import com.wzkcy.jdgc.bean.Policeman;
import com.wzkcy.jdgc.bean.Powerman;
import com.wzkcy.jdgc.bean.QY;
import com.wzkcy.jdgc.bean.SearchResult;
import com.wzkcy.jdgc.bean.Station;
import com.wzkcy.jdgc.setting.Constants;
import com.wzkcy.jdgc.setting.Util;

public class HttpCommunication {
	
	public final String TAG = "HttpCommunication";
	
	private final String GETPOLICEMAN = "findVwusersAll.action"; // ��ȡ����������
	private final String GETZXPOLICEMAN = "findVwusersZX.action"; // ��ȡ����������
	private final String GETPOLICESTATION = "findFixedGT.action"; 	// ��ȡ�񾯸�ͤ��ԭ���ΰ���ͤ��
	private final String GETNORMALSTATION = "findNotFixedGT.action"; // ��ȡ������ͤ��ԭ���ƶ���ͤ��
	private final String SEARCHBYSQL = "findBySql.action"; // ����
	private final String SEARCHBYQY = "findByQyGroup.action"; // ���������ѯ
	private final String SEARCHBYJG = "findByJGGroup.action"; // ���ݻ�����ѯ
	private final String FIXLOCATION = "updateJdWd.action";   // ��ͤά��
	private final String GETPOWERMEN = "findGtManageAll.action";   // ��ȡ��Ȩ����Ա
	private final String FINDJZDM = "findJZDM.action";   // ��ȡ��Ȩ����Ա
	
	private AbHttpUtil mAbHttpUtil;
	private Handler mHandler;
	
	public HttpCommunication(Context context, Handler handler){
        mAbHttpUtil = AbHttpUtil.getInstance(context);
        mAbHttpUtil.setTimeout(60*1000);
        mAbHttpUtil.setDebug(true);
        mHandler = handler;
	}
	// ��ȡ���о�Ա��Ϣ
	public void getPoliceman(){
		String url = Constants.URL_HTTP + GETPOLICEMAN;
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {

			@Override // ��ȡ���ݳɹ����������
			public void onSuccess(int statusCode, String content) {
				Log.e(TAG, "getPoliceman : onSuccess" );
				try{
					List<Policeman> list =  JSON.parseArray(content, Policeman.class);
					Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_GET_POLICEMAN_S);
					message.obj = list;
					mHandler.sendMessage(message);
				}catch(Exception e){
					Util.Toast("��Ա���ݽ����쳣");
				}
			}

			@Override 
			public void onFailure(int statusCode, String content, Throwable error) {
				Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_GET_POLICEMAN_F);
				message.obj = "��Ա�����쳣��" + error.getMessage();
				mHandler.sendMessage(message);
				Log.e(TAG, "getPoliceman : onFailure" );
			}

		});
	}
	// ��ȡ���߾�Ա��Ϣ
	public void getOnlinePoliceman(){
		String url = Constants.URL_HTTP + GETZXPOLICEMAN;
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {

			@Override // ��ȡ���ݳɹ����������
			public void onSuccess(int statusCode, String content) {
				Log.e(TAG, "getOnlinePoliceman : onSuccess" );
				try{
					List<Policeman> list =  JSON.parseArray(content, Policeman.class);
					Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_GET_ONLINE_POLICEMAN_S);
					message.obj = list;
					mHandler.sendMessage(message);
				}catch(Exception e){
					Util.Toast("���߾�Ա���ݽ����쳣");
				}
			}

			@Override 
			public void onFailure(int statusCode, String content, Throwable error) {
				Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_GET_ONLINE_POLICEMAN_F);
				message.obj = "���߾�Ա�����쳣��" + error.getMessage();
				mHandler.sendMessage(message);
				Log.e(TAG, "getOnlinePoliceman : onFailure" );
			}

		});
	}
	// ��ȡ�񾯸�ͤ��Ϣ
	public void getPoliceStation(){
		String url = Constants.URL_HTTP + GETPOLICESTATION;
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
			
			@Override // ��ȡ���ݳɹ����������
			public void onSuccess(int statusCode, String content) {
				Log.e(TAG, "getPoliceStation : onSuccess" );
				try{
					List<Station> list =  JSON.parseArray(content, Station.class);
					Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_GET_POLICE_STATION_S);
					message.obj = list;
//					mHandler.sendMessage(message);
					mHandler.sendMessageDelayed(message, 1000);
				}catch(Exception e){
					Util.Toast("�񾯸�ͤ���ݽ����쳣");
				}
			}
			
			@Override 
			public void onFailure(int statusCode, String content, Throwable error) {
				Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_GET_POLICE_STATION_F);
				message.obj = "�񾯸�ͤ�����쳣��" + error.getMessage();
				mHandler.sendMessage(message);
				Log.e(TAG, "getPoliceStation : onFailure" );
			}
			
		});
	}
	// ��ȡ�ƶ���ͤ��Ϣ
	public void getNormalStation(){
		String url = Constants.URL_HTTP + GETNORMALSTATION;
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
			
			@Override // ��ȡ���ݳɹ����������
			public void onSuccess(int statusCode, String content) {
				Log.e(TAG, "getNormalStation : onSuccess" );
				try{
					List<Station> list =  JSON.parseArray(content, Station.class);
					Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_GET_NORMAL_STATION_S);
					message.obj = list;
//					mHandler.sendMessage(message);
					mHandler.sendMessageDelayed(message, 1000);
				}catch(Exception e){
					Util.Toast("������ͤ���ݽ����쳣");
				}
			}
			
			@Override 
			public void onFailure(int statusCode, String content, Throwable error) {
				Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_GET_NORMAL_STATION_F);
				message.obj = "������ͤ�����쳣��" + error.getMessage();
				mHandler.sendMessage(message);
				Log.e(TAG, "getNormalStation : onFailure" );
			}
			
		});
	}
	// ��ͤά��
	public void fixStation(String dm, double jd, double wd){
		if(isLegal(dm) && isLegal(jd) && isLegal(wd)){
			String url = Constants.URL_HTTP + FIXLOCATION + "?dm=" + dm + "&jd=" + jd + "&wd=" + wd;
			mAbHttpUtil.get(url, new AbStringHttpResponseListener() {

				@Override // ��ȡ���ݳɹ����������
				public void onSuccess(int statusCode, String content) {
					Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_FIX_LOCATION_S);
					if(content!=null && content.contains("true")){
						message.obj = true;
					}else if(content!=null && content.contains("false")){
						message.obj = false;
					}else{
						message.what = Constants.MESSAGE_WHAT_FIX_LOCATION_F1;
					}
					mHandler.sendMessage(message);
				}

				@Override 
				public void onFailure(int statusCode, String content, Throwable error) {
					mHandler.sendEmptyMessage(Constants.MESSAGE_WHAT_FIX_LOCATION_F2);
					Util.Toast(error.getMessage());
				}

			});
		}
	}
	// ��ȡ�����б�
	public void getQyGroup(){
		String url = Constants.URL_HTTP + SEARCHBYQY ;
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
			
			@Override // ��ȡ���ݳɹ����������
			public void onSuccess(int statusCode, String content) {
				Log.i(TAG, "getQyGroup : onSuccess" );
				try{
					List<QY> list =  JSON.parseArray(content, QY.class);
					Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_GETQYGROUP_S);
					message.obj = list;
					mHandler.sendMessage(message);
				}catch(Exception e){
					mHandler.sendEmptyMessage(Constants.MESSAGE_WHAT_GETQYGROUP_F1);
				}
			}
			
			@Override 
			public void onFailure(int statusCode, String content, Throwable error) {
				Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_GETQYGROUP_F2);
				message.obj = "���������쳣��" + error.getMessage();
				mHandler.sendMessage(message);
				Log.e(TAG, "getQyGroup : onFailure" );
			}
			
		});
	}
	// ��ȡ���������б�
	public void getJGroup(){
		String url = Constants.URL_HTTP + SEARCHBYJG ;
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
			
			@Override // ��ȡ���ݳɹ����������
			public void onSuccess(int statusCode, String content) {
				Log.e(TAG, "getJGroup : onSuccess" );
				try{
					List<JGS> list =  JSON.parseArray(content, JGS.class);
					Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_GETJGROUP_S);
					message.obj = list;
//					mHandler.sendMessage(message);
					mHandler.sendMessageDelayed(message, 1000);
				}catch(Exception e){
					mHandler.sendEmptyMessage(Constants.MESSAGE_WHAT_GETJGROUP_F1);
				}
			}
			
			@Override 
			public void onFailure(int statusCode, String content, Throwable error) {
				Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_GETJGROUP_F2);
				message.obj = "���������쳣��" + error.getMessage();
				mHandler.sendMessage(message);
				Log.e(TAG, "getJGroup : onFailure" );
			}
			
		});
	}
	// ��ȡȨ����Ա
	public void getPowermen(){
		String url = Constants.URL_HTTP + GETPOWERMEN ;
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
			
			@Override // ��ȡ���ݳɹ����������
			public void onSuccess(int statusCode, String content) {
				List<Powerman> list =  JSON.parseArray(content, Powerman.class);
				Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_GET_POWERMAN_S);
				message.obj = list;
				mHandler.sendMessage(message);
			}
			
			@Override 
			public void onFailure(int statusCode, String content, Throwable error) {
				mHandler.sendEmptyMessage(Constants.MESSAGE_WHAT_GET_POWERMAN_F);
				Util.Toast("Ȩ����Ա�����쳣��" + error.getMessage());
			}
			
		});
	}
	// ��ȡ��վ��γ��
	public void getLocationByCell(final Cell cell){
		String url = Constants.URL_HTTP + FINDJZDM + "?cid=" + cell.getCid() + "&lac=" + cell.getLac();
		
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				if(!content.equals("")){
					List<Cell> list =  JSON.parseArray(content, Cell.class);
					Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_GET_CELLLOCATION_S);
					message.obj = list;
					int cid = 0;
					int lac = 0;
					if(Util.isNum(cell.getCid())) cid = Integer.valueOf(cell.getCid());
					if(Util.isNum(cell.getLac())) lac = Integer.valueOf(cell.getLac());
					message.arg1 = cid;
					message.arg2 = lac;
					mHandler.sendMessage(message);
				} else{
					mHandler.sendEmptyMessage(Constants.MESSAGE_WHAT_GET_CELLLOCATION_EMPTY);
				}
				
			}
			
			@Override 
			public void onFailure(int statusCode, String content, Throwable error) {
				mHandler.sendEmptyMessage(Constants.MESSAGE_WHAT_GET_POWERMAN_F);
				Util.Toast("��ȡ��վ�����쳣��" + error.getMessage());
			}
			
		});
	}
	
	public void searchByKey(String xzqhmc, String jgmc, String keyWord){
		xzqhmc = xzqhmc.trim();
		jgmc = jgmc.trim();
		keyWord = keyWord.trim();
		String url = Constants.URL_HTTP + SEARCHBYSQL + "?xzqhmc=" + xzqhmc + "&jgmc=" + jgmc + "&keyWord=" + keyWord;;
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
			
			@Override // ��ȡ���ݳɹ����������
			public void onSuccess(int statusCode, String content) {
				try{
					List<SearchResult> list =  JSON.parseArray(content, SearchResult.class);
					Message message = mHandler.obtainMessage(Constants.MESSAGE_WHAT_SEARCHBYKEY_S);
					message.obj = list;
					mHandler.sendMessage(message);
				}catch(Exception e){
					mHandler.sendEmptyMessage(Constants.MESSAGE_WHAT_SEARCHBYKEY_F);
				}
			}
			
			@Override 
			public void onFailure(int statusCode, String content, Throwable error) {
				mHandler.sendEmptyMessage(Constants.MESSAGE_WHAT_SEARCHBYKEY_F);
				Util.Toast(error.getMessage());
			}
			
		});
	}

	public void doLogin(String username){
		
		String url = Constants.URL_HTTP + GETPOLICEMAN;
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
			
			@Override // ��ȡ���ݳɹ����������
			public void onSuccess(int statusCode, String content) {
				System.out.println(content);
			}
			
			@Override 
			public void onFailure(int statusCode, String content, Throwable error) {
				Util.Toast(error.getMessage());
			}
			
		});
	}
	
	private boolean isLegal(String s){
		if(s!=null && !s.trim().equals("")) return true;
		else return false;
	}
	private boolean isLegal(double d){
		if(d>0) return true;
		else return false;
	}
}
