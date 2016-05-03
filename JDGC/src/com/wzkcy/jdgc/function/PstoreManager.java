package com.wzkcy.jdgc.function;

import java.util.Map;

import android.os.Bundle;
import android.os.Looper;
import cn.com.cybertech.pdk.LinkInfo;
import cn.com.cybertech.pdk.UserInfo;
import cn.com.cybertech.pdk.api.IPstoreAPI;
import cn.com.cybertech.pdk.api.IPstoreHandler;
import cn.com.cybertech.pdk.api.PstoreAPIImpl;
import cn.com.cybertech.pdk.api.UserObject;
import cn.com.cybertech.pdk.auth.Oauth2AccessToken;
import cn.com.cybertech.pdk.auth.PstoreAuth;
import cn.com.cybertech.pdk.auth.PstoreAuthListener;
import cn.com.cybertech.pdk.auth.sso.SsoHandler;
import cn.com.cybertech.pdk.exception.PstoreAuthException;
import cn.com.cybertech.pdk.exception.PstoreException;
import cn.com.cybertech.pdk.exception.PstoreUserException;

import com.wzkcy.jdgc.MainActivity;
import com.wzkcy.jdgc.setting.Constants;
import com.wzkcy.jdgc.setting.Util;

public class PstoreManager {
	
	private MainActivity mMainActivity;
	private PstoreAuth mAuth;
	private SsoHandler mSsoHandler;
	private Oauth2AccessToken mAccessToken;
	
	public PstoreManager(MainActivity mMainActivity){
		this.mMainActivity = mMainActivity;
	}
	public SsoHandler authorization(){
		mAuth = new PstoreAuth(mMainActivity, Constants.CLIENT_ID);
		mSsoHandler = new SsoHandler(mMainActivity, mAuth);
		mSsoHandler.authorize(new AuthListener()); // ��Ȩ
		return mSsoHandler;
	}
	
	private IPstoreHandler.Response mResponse = new IPstoreHandler.Response() {
		@Override
		public void onResponse(Bundle response) {
			UserObject user = UserObject.fromBundle(response);
			Constants.PAccount = user.getAccount();
			Constants.PName = user.getName();
			new Thread() {
				public void run() {
					Looper.prepare();
					mMainActivity.getMainFragment().getHttpCommunication().getPowermen();
					Looper.loop();
				};
			}.start();
		}

		@Override
		public void onPstoreException(PstoreException exception) {
			if (exception instanceof PstoreUserException) {
				PstoreUserException e = (PstoreUserException) exception;
				switch(e.getErrorCode()){
				case PstoreUserException.ERROR_RES_UNAUTHORIZED: 
					/** ��Դδ��Ȩ */
					Util.Toast("PSTORE������Դδ��Ȩ ");
					break;
				case PstoreUserException.ERROR_ACCESS_TOKEN_EXPIRED:
					/** access token�ѹ��� */
					Util.Toast("PSTORE����TOKEN�ѹ��� " + e.getErrorDesc());
					break;
				case PstoreUserException.ERROR_UNKONWN:
					/** δ֪���� */
					Util.Toast("PSTORE����δ֪���� ");
					break;
				}
			} else {
				Util.Toast("PSTOREδ֪����" + exception.getMessage());
			}
		}
	};
	public IPstoreHandler.Response getResponse(){
		return mResponse;
	}
	/**
	 * ��ȡ�˻���
	 */
	public String getAccount(){
		String account = UserInfo.getAccount(mMainActivity);
		return account;
	}
	/**
	 * ��ȡ�û���Ϣ
	 */
	public Map<String, String> getUserInfo(){
		/**
		* ��ȡ�û���Ϣ 
		* key:
		* uid-�û���Ϣ 
		* account-�˻��� 
		* name-����
		* phone-�绰 
		* dept_id-����ID
		* dept_name-����
		*/
		Map<String, String> userinfo = UserInfo.getUserInfo(mMainActivity);
		return userinfo;
	}
	/**
	 * ��ȡ��·��Ϣ
	 */
	public Map<String, String> getLinkInfo(){
		Map<String, String> linkMap = LinkInfo.getLinkInfo(mMainActivity);
		Constants.PHost = LinkInfo.getHost(mMainActivity); // ��ȡhost(ip:port)
		Constants.PIP = LinkInfo.getIP(mMainActivity); // ��ȡip
		Constants.PPort = LinkInfo.getPort(mMainActivity);// ��ȡport
		return linkMap;
	}
	/**
	 * ��ȡAccessToken
	 */
	public Oauth2AccessToken getToken(){
		if(!mAccessToken.isSessionValid()){
			mSsoHandler.authorizeRefresh(new AuthListener());// ������Ȩ
		}
		return mAccessToken;
	}
	
	// ��Ȩ������
	class AuthListener implements PstoreAuthListener {

		@Override
		public void onCancel() {
			
		}

		@Override
		public void onComplete(Oauth2AccessToken accessToken) {
			mAccessToken = accessToken;
			// ��Ȩ�������û���Ϣ
			IPstoreAPI pstoreAPI = new PstoreAPIImpl();
			pstoreAPI.requestUserInfo(mMainActivity, mResponse, Constants.CLIENT_ID, mAccessToken.getToken());
		}

		@Override
		public void onPstoreException(PstoreException pstoreException) {
			if (pstoreException instanceof PstoreAuthException) {
				PstoreAuthException e = (PstoreAuthException) pstoreException;
				switch (e.getErrorCode()) {
				case PstoreAuthException.ERROR_CLIENTID_NULL:  
					/** client_idΪ�� */
					break;
				
				case PstoreAuthException.ERROR_CLIENTID_ILLEGAL:  
					/** client_id�Ƿ� */	
					break;
				
				case PstoreAuthException.ERROR_GRANT_CODE_ILLEGAL:  
					/** client_id�Ƿ� */	
					break;
				
				case PstoreAuthException.ERROR_PSTORE_NOT_INSTALLED:  
					/** PSTOREδ��װ */
					break;
				case PstoreAuthException.ERROR_PSTORE_NOT_LOGGED:  
					/** PSTOREδ��¼ */
					break;
				default:
					// ����δ֪����
					break;
				}
			} else {
			
			}
		}
	}
}
