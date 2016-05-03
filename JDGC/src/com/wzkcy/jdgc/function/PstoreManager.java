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
		mSsoHandler.authorize(new AuthListener()); // 授权
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
					/** 资源未授权 */
					Util.Toast("PSTORE错误：资源未授权 ");
					break;
				case PstoreUserException.ERROR_ACCESS_TOKEN_EXPIRED:
					/** access token已过期 */
					Util.Toast("PSTORE错误：TOKEN已过期 " + e.getErrorDesc());
					break;
				case PstoreUserException.ERROR_UNKONWN:
					/** 未知错误 */
					Util.Toast("PSTORE错误：未知错误 ");
					break;
				}
			} else {
				Util.Toast("PSTORE未知错误：" + exception.getMessage());
			}
		}
	};
	public IPstoreHandler.Response getResponse(){
		return mResponse;
	}
	/**
	 * 获取账户名
	 */
	public String getAccount(){
		String account = UserInfo.getAccount(mMainActivity);
		return account;
	}
	/**
	 * 获取用户信息
	 */
	public Map<String, String> getUserInfo(){
		/**
		* 获取用户信息 
		* key:
		* uid-用户信息 
		* account-账户名 
		* name-姓名
		* phone-电话 
		* dept_id-部门ID
		* dept_name-部门
		*/
		Map<String, String> userinfo = UserInfo.getUserInfo(mMainActivity);
		return userinfo;
	}
	/**
	 * 获取链路信息
	 */
	public Map<String, String> getLinkInfo(){
		Map<String, String> linkMap = LinkInfo.getLinkInfo(mMainActivity);
		Constants.PHost = LinkInfo.getHost(mMainActivity); // 获取host(ip:port)
		Constants.PIP = LinkInfo.getIP(mMainActivity); // 获取ip
		Constants.PPort = LinkInfo.getPort(mMainActivity);// 获取port
		return linkMap;
	}
	/**
	 * 获取AccessToken
	 */
	public Oauth2AccessToken getToken(){
		if(!mAccessToken.isSessionValid()){
			mSsoHandler.authorizeRefresh(new AuthListener());// 重新授权
		}
		return mAccessToken;
	}
	
	// 授权监听器
	class AuthListener implements PstoreAuthListener {

		@Override
		public void onCancel() {
			
		}

		@Override
		public void onComplete(Oauth2AccessToken accessToken) {
			mAccessToken = accessToken;
			// 授权后请求用户信息
			IPstoreAPI pstoreAPI = new PstoreAPIImpl();
			pstoreAPI.requestUserInfo(mMainActivity, mResponse, Constants.CLIENT_ID, mAccessToken.getToken());
		}

		@Override
		public void onPstoreException(PstoreException pstoreException) {
			if (pstoreException instanceof PstoreAuthException) {
				PstoreAuthException e = (PstoreAuthException) pstoreException;
				switch (e.getErrorCode()) {
				case PstoreAuthException.ERROR_CLIENTID_NULL:  
					/** client_id为空 */
					break;
				
				case PstoreAuthException.ERROR_CLIENTID_ILLEGAL:  
					/** client_id非法 */	
					break;
				
				case PstoreAuthException.ERROR_GRANT_CODE_ILLEGAL:  
					/** client_id非法 */	
					break;
				
				case PstoreAuthException.ERROR_PSTORE_NOT_INSTALLED:  
					/** PSTORE未安装 */
					break;
				case PstoreAuthException.ERROR_PSTORE_NOT_LOGGED:  
					/** PSTORE未登录 */
					break;
				default:
					// 其他未知错误
					break;
				}
			} else {
			
			}
		}
	}
}
