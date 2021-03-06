package com.wzkcy.jdgc.dialog;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.wzkcy.jdgc.MyApplication;

public class DialogFragmentBase extends DialogFragment {
	
	public void shown(FragmentManager fragmentManager, String tag){
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if(!MyApplication.FragmentMap.containsKey(this.getClass().getName())){
			MyApplication.FragmentMap.put(this.getClass().getName(), this);
		}else{
			fragmentTransaction.remove(this);
		}
		this.show(fragmentTransaction, tag);
	};
	
}
