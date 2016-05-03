package com.wzkcy.jdgc.listener;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.wzkcy.arcgismapapi.ArcGISRouteQuerier;
import com.wzkcy.jdgc.fragment.MainFragment;
import com.wzkcy.jdgc.function.CalloutManager;
import com.wzkcy.jdgc.function.route.RouteTakenManager;

public class MyOnSingleTapListener implements OnSingleTapListener{

	private static final long serialVersionUID = 1L;
	
	private MainFragment mMainFragment;
	private MapView mMap;
	private CalloutManager mCalloutManager;
	
	private RouteTakenManager mRouteTakenManager;
	private ArcGISRouteQuerier mArcGISRouteQuerier;
	
	public enum OperaterKind{
		DONOTHING, ADDPOINT, SELECT, MOVE, DRAWLINE, DELETE, ADDROUTEPOINT;
	}
	
	public MyOnSingleTapListener(MainFragment mMainFragment){
		this.mMainFragment = mMainFragment;
		this.mMap = mMainFragment.getMap();
		this.mRouteTakenManager = mMainFragment.getRouteTakenManager();
		this.mArcGISRouteQuerier = mMainFragment.getArcGISRouteQuerier();
		
		mCalloutManager = new CalloutManager(mMainFragment, mMap);
	}
	
	@Override
	public void onSingleTap(float x, float y) {
		if (!mMap.isLoaded()) {
            return;
        }
		doSingleTapOperate(x, y);
		mMainFragment.CheckMessureLine(x, y);
		mRouteTakenManager.CheckPointSingleTapListener(x, y);
		if(mMainFragment.isLJQueryInLongPress()){
			mArcGISRouteQuerier.doClick(x, y);
		}
	}
	
	private void doSingleTapOperate(float x, float y){
		mMainFragment.removeHighlight();
		switch(mMainFragment.getOperaterKind()){
		case SELECT:
			mCalloutManager.ShowLightCallout(x, y);
			break;
		case MOVE:
			mCalloutManager.getMoveListener().doSomethingAfterMove(x, y);
			break;
		case DONOTHING:
			break;
		case ADDROUTEPOINT:
			mArcGISRouteQuerier.addPoint(x, y);
			break;
		default:
			break;
		}
	}
	
	
	
	
}
