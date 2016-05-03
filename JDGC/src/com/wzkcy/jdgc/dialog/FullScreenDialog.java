package com.wzkcy.jdgc.dialog;

import android.app.Dialog;
import android.view.View;
import android.view.Window;

import com.wzkcy.jdgc.MainActivity;
import com.wzkcy.jdgc.MyApplication;
import com.wzkcy.jdgc.R;

public class FullScreenDialog {
	
	protected Dialog mDialog;
	protected MainActivity mMainActivity;
	protected View mView;
	
	public FullScreenDialog(MainActivity mMainActivity, int resource){
		this.mMainActivity = mMainActivity;
		mView = MyApplication.Inflater.inflate(resource, null);
		initBasicElements();
	}
	
	protected void initBasicElements(){}
	
	public void ShowDialog(){
		mDialog = new Dialog(mMainActivity, R.style.DialogFullTheme);
		mDialog.setCancelable(true);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(mView);
		mDialog.show();
	}
	
	public void Dismiss(){
		mDialog.dismiss();
	}
}
