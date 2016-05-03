package com.wzkcy.jdgc;

import java.util.HashMap;

import jsqlite.Database;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.com.cybertech.pdk.api.IPstoreAPI;
import cn.com.cybertech.pdk.api.PstoreAPIImpl;
import cn.com.cybertech.pdk.auth.sso.SsoHandler;
import cn.com.cybertech.pdk.widget.AccountMappingDialogFragment.AccountMappding;

import com.ab.activity.AbActivity;
import com.ab.view.slidingmenu.SlidingMenu;
import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.wzkcy.jdgc.database.DBSetting;
import com.wzkcy.jdgc.database.DataBaseTool;
import com.wzkcy.jdgc.fragment.MainFragment;
import com.wzkcy.jdgc.fragment.MenuFragment;
import com.wzkcy.jdgc.fragment.PolicemanFragment;
import com.wzkcy.jdgc.fragment.SearchFragment;
import com.wzkcy.jdgc.fragment.StationFragment;
import com.wzkcy.jdgc.fragment.StatisticsFragment;
import com.wzkcy.jdgc.function.Cache;
import com.wzkcy.jdgc.function.PstoreManager;
import com.wzkcy.jdgc.setting.Constants;
import com.wzkcy.jdgc.setting.Util;

public class MainActivity extends AbActivity implements AccountMappding {

	public static final String TAG = "MainActivity";
	public static final int REQUESTCODE = 2;

	private Intent mServiceIntent;
	private Database mDatabase;
	private Cache mCache;
	private PstoreManager mPstoreManager;
	private SsoHandler mSsoHandler;
	
	private Fragment mCurrentFragment; // 右边菜单当前视图
	private StatisticsFragment mStatisticsFragment;
	private MenuFragment mMenuFragment;
	private long mExitTime;
	
	public abstract class OnCameraCapturedListener {
		public abstract HashMap<String, Object> onCameraCaptured();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		Util.FullScreen(this);
		MyApplication.Context = getApplicationContext();
		MyApplication.Resources = getResources();
		MyApplication.Inflater = mInflater;

		View view = mInflater.inflate(this.getResources().getLayout(R.layout.activity_main), null);
		this.setAbContentView(view);

		mDatabase = DataBaseTool.OpenDatabase(this, DBSetting.DB_NAME_DLJ);
		SharedPreferences sp = getSharedPreferences(Cache.SharedPreferencesFile, 0);
		mCache = new Cache(sp);
		
		mPstoreManager = new PstoreManager(this);
		mSsoHandler = mPstoreManager.authorization();
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		MainFragment mFragment = new MainFragment();
		transaction.add(R.id.container, mFragment, MainFragment.TAG);
		transaction.commit();
		
		mCurrentFragment = mFragment;

		Util.GetScreenArgument(this);// 获取屏幕尺寸、分辨率等相关参数
		mServiceIntent = new Intent(this, LocalService.class);
		startService(mServiceIntent);// 启动本地服务
		initView();
		
//		Util.getImportDirRootPath();
//		Util.getExportDirRootPath();
//		Util.getPictureDirRootPath();
//		Util.getLocalMapDirRootPath();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		
	}
	
	public void popBackStack(String name) {
		getFragmentManager().popBackStack(name, 0);
	}

	private SlidingMenu mSlidingMenu;
	private ImageView mRightBtn;
	private void initView() {
		// 侧滑组件
		mSlidingMenu = new SlidingMenu(this);
		mSlidingMenu.setMode(SlidingMenu.LEFT_OF);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setShadowWidth(R.dimen.shadow_width);
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT); // 添加到指定Activity

		// 侧滑菜单添加左边视图
		mSlidingMenu.setMenu(R.layout.sliding_menu);
		mMenuFragment = new MenuFragment();
		getFragmentManager().beginTransaction().replace(R.id.menu_frame, mMenuFragment, MenuFragment.TAG).commit();
//		// 侧滑菜单添加右边视图
//		mSlidingMenu.setSecondaryMenu(R.layout.sliding_menu_second);
//		SettingFragment mFragment = new SettingFragment();
//		getFragmentManager().beginTransaction().add(R.id.menu_frame_second, mFragment, SettingFragment.TAG).commit();
//		mCurrentFragment = mFragment;

		// 上标题栏
		mAbTitleBar.setVisibility(View.VISIBLE);
		mAbTitleBar.setLogo(R.drawable.button_selector_titlebar_menu);
		mAbTitleBar.setLogo2(R.drawable.button_selector_titlebar_back);
		mAbTitleBar.getLogoView2().setVisibility(View.GONE);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(Util.Dip2Px(this, 10), 0, 0, 0);
		lp.gravity = Gravity.CENTER_VERTICAL;
		mAbTitleBar.getLogoView().setLayoutParams(lp);
		mAbTitleBar.getLogoView2().setLayoutParams(lp);
//		mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.setTitleBarBackground(R.drawable.bg_titlebar_no_text);
//		mAbTitleBar.setTitleText(R.string.app_name);
//		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		LinearLayout mTitleTextLayout = mAbTitleBar.getTitleTextLayout();
		View view = mInflater.inflate(R.layout.titlebar_mid_view, null);
		mTitleTextLayout.removeAllViews();
		mTitleTextLayout.addView(view);
		View rightview = mInflater.inflate(R.layout.titlebar_right_view, null);
		mAbTitleBar.clearRightView();
		mAbTitleBar.addRightView(rightview);
		mRightBtn = (ImageView) rightview.findViewById(R.id.rightBtn);
		mRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if((getFragmentManager().findFragmentById(R.id.container) instanceof MainFragment)){
					
					SearchFragment mSettingFragment = new SearchFragment();  
			        FragmentTransaction transaction = getFragmentManager().beginTransaction(); 
			        transaction.hide(getMainFragment());
			        transaction.add(R.id.container, mSettingFragment);
			        transaction.addToBackStack(SearchFragment.TAG);  
			        transaction.commit();  
			        replaceLogoView(false);
				}
			}
		});
		mAbTitleBar.getLogoView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mSlidingMenu.isMenuShowing()) {
					mSlidingMenu.showContent();
				} else {
					mSlidingMenu.showMenu();
				}
			}
		});
		mAbTitleBar.getLogoView2().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Log.i(TAG, ""+getFragmentManager().getBackStackEntryCount());
				if((getFragmentManager().findFragmentById(R.id.container) instanceof StatisticsFragment)){
					if(mStatisticsFragment!=null){
						if(mStatisticsFragment.isDetialShown()){
							mStatisticsFragment.showFormLayout();
						}else{
							if(mStatisticsFragment.isSecondFrom()){
								mStatisticsFragment.initForm();
							}else{
								if(getFragmentManager().getBackStackEntryCount()<2){
							        replaceLogoView(true);
							        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
								}else{
									getFragmentManager().popBackStack();
								}
								
							}
						}
					}
				}else{
					if(getFragmentManager().getBackStackEntryCount()==1){
						replaceLogoView(true);
					}
					if((getFragmentManager().findFragmentById(R.id.container) instanceof StationFragment) ||
							getFragmentManager().findFragmentById(R.id.container) instanceof PolicemanFragment){
						setRightBtnFunction(false, null, null);
					}
					getFragmentManager().popBackStack();
				}
				
			}
		});
		// 下标题栏
		mAbBottomBar.setVisibility(View.GONE);
	}
	// 切换右边按钮图片
	public void replaceRightBtn(int resid){
		mRightBtn.setBackground(MyApplication.Resources.getDrawable(resid));
	}
	/**
	 * 设置右边按钮功能
	 * @param setToLocation true 为兴趣点定位功能 	flase 弹出搜索视图功能 
	 * @param point 设为true是需要赋值  false时可写null
	 * @param layer 中选高亮图层 true并且point不为空时有用
	 */
	public void setRightBtnFunction(final boolean setToLocation, final Point point, final GraphicsLayer layer){
		if(setToLocation){
			replaceRightBtn(R.drawable.button_selector_tolocation);
		}else{
			replaceRightBtn(R.drawable.button_selector_titlebar_search);
		}
		mRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(setToLocation){
					if(point==null){
						setRightBtnDoNothing();
					}else{
						getMainFragment().toLocation(point, layer);
						setRightBtnFunction(false, null, null);
						replaceLogoView(true);
					}
				}else{
					if((getFragmentManager().findFragmentById(R.id.container) instanceof MainFragment)){
						SearchFragment mSettingFragment = new SearchFragment();  
				        FragmentTransaction transaction = getFragmentManager().beginTransaction(); 
				        transaction.hide(getMainFragment());
				        transaction.add(R.id.container, mSettingFragment);
				        transaction.addToBackStack(SearchFragment.TAG);  
				        transaction.commit();  
				        replaceLogoView(false);
					}
				}
			}
		});
	}
	public void setRightBtnDoNothing(){}
	public void replaceLogoView(boolean showLogoView){
		if(showLogoView){
			mAbTitleBar.getLogoView().setVisibility(View.VISIBLE);
	        mAbTitleBar.getLogoView2().setVisibility(View.GONE);
		}else{
			mAbTitleBar.getLogoView().setVisibility(View.GONE);
	        mAbTitleBar.getLogoView2().setVisibility(View.VISIBLE);
		}
		
	}
	
	/**
	 * 替换主视图
	 */
	public void replaceMainMenu(Fragment fragment, String Tag) {
		if (!mCurrentFragment.getTag().equals(Tag)) {
			Fragment pastfragment = getFragmentManager().findFragmentByTag(Tag);
			if (pastfragment == null) {
				pastfragment = fragment;
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.add(R.id.container, pastfragment, Tag);
				transaction.addToBackStack(null);
				transaction.commit();
				MyApplication.FragmentMap.put(Tag, fragment);
			}else{
				getFragmentManager().beginTransaction().show(pastfragment).commit();
			}
//			getFragmentManager().beginTransaction().hide(mCurrentFragment).commit();
			mCurrentFragment = pastfragment;
		}
//		mSlidingMenu.showSecondaryMenu();
	}

	public void showContent() {
		mSlidingMenu.showContent();
	}

	public MainFragment getMainFragment() {
		return (MainFragment) getFragmentManager().findFragmentByTag(MainFragment.TAG);
	}
	public void setStatisticsFragment(StatisticsFragment mStatisticsFragment) {
		this.mStatisticsFragment = mStatisticsFragment;
	}
	public MenuFragment getMenuFragment() {
		return mMenuFragment;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		if(resultCode != RESULT_OK){
			return;
		} 
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		AbDialogUtil.removeDialog(this); // 这个方法会影响popBackStack的正常使用
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(!getMainFragment().getArcGISRouteQuerier().isClean()){
				getMainFragment().getArcGISRouteQuerier().clean();
			} else if(!getMainFragment().getArcGISPlaceNameQuerier().isClean()){
				getMainFragment().getArcGISPlaceNameQuerier().clean();
			} else if (mSlidingMenu.isMenuShowing() || mSlidingMenu.isSecondaryMenuShowing()) {
				mSlidingMenu.showContent();
				return false;
			} else if (!(getFragmentManager().findFragmentById(R.id.container) instanceof MainFragment)) {
				getFragmentManager().popBackStack();
				if(getFragmentManager().getBackStackEntryCount()==1){
					mAbTitleBar.getLogoView().setVisibility(View.VISIBLE);
			        mAbTitleBar.getLogoView2().setVisibility(View.GONE);
				}
			} else {
				if (System.currentTimeMillis() - mExitTime > 1000) {
					Util.Toast("再按一次退出程序");
					mExitTime = System.currentTimeMillis();
				} else {
					finish();
				}
			}
		}
		return false;
	}

	public void setTitleBarVisible(boolean visible) {
		if (visible) {
			mAbTitleBar.setVisibility(View.VISIBLE);
		} else {
			mAbTitleBar.setVisibility(View.GONE);
		}
	}
	public void keepBehindoffsetHide(boolean hide) {
		if (hide) {
			mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset_zero);
		} else {
			mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
		DataBaseTool.CloseDatabase(mDatabase);
		stopService(mServiceIntent);
		System.exit(0);
	}

	public Database getDatabase() {
		return mDatabase;
	}
	
	public Cache getCache() {
		return mCache;
	}

	@Override
	public void doMapping(String accessToken, CharSequence account, CharSequence pwd) {
		Log.i(TAG, "绑定完成");
		IPstoreAPI pstoreAPI = new PstoreAPIImpl();
		pstoreAPI.requestUserInfo(MainActivity.this, mPstoreManager.getResponse(), Constants.CLIENT_ID, accessToken);
	}
	
}
