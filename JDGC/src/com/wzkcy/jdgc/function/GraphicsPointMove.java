package com.wzkcy.jdgc.function;

import java.util.Map;

import android.content.DialogInterface;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.wzkcy.jdgc.fragment.MainFragment;
import com.wzkcy.jdgc.listener.MyOnSingleTapListener.OperaterKind;
import com.wzkcy.jdgc.setting.Util;

public class GraphicsPointMove {
	
	public interface OnMoveListener {
		public abstract void doSomethingAfterMove(float screenx, float screeny);
	}
	
	public static OnMoveListener Prepare(final MainFragment fragment, final GraphicsLayer layer, final int gid){
		final MapView mMap = fragment.getMap();
		layer.clearSelection();
		layer.setSelectedGraphics(new int[]{gid}, true);
		Graphic graphic = layer.getGraphic(gid);
		final Geometry geo = graphic.getGeometry();
		Map<String, Object> attributes = graphic.getAttributes();
		final String dm = (String)attributes.get(ArcgisTool.ATTRIBUTE_KEY_ID);
		fragment.setOperaterKind(OperaterKind.MOVE);
		OnMoveListener mOnAfterMoveListener = new OnMoveListener() {
			
			@Override
			public void doSomethingAfterMove(float x, float y) {
				
				final Point point = mMap.toMapPoint(new Point(x, y));
				DialogInterface.OnClickListener mPositiveListener = new DialogInterface.OnClickListener() { // 确定
					@Override
					public void onClick(DialogInterface dialog, int which) {
						layer.updateGraphic(gid, point);
						layer.clearSelection();
						fragment.setOperaterKind(OperaterKind.SELECT);
						fragment.getHttpCommunication().fixStation(dm, point.getX(), point.getY());
					}
				};
				DialogInterface.OnClickListener mNegativeListener = new DialogInterface.OnClickListener() { // 取消
					@Override
					public void onClick(DialogInterface dialog, int which) {
						layer.updateGraphic(gid, geo);
						layer.clearSelection();
						fragment.setOperaterKind(OperaterKind.SELECT);
					}
				};
				Util.ShowDialog(fragment.getActivity(), "确定将目标点移动到该位置吗?", mPositiveListener, mNegativeListener);
			}
		};
		return mOnAfterMoveListener;
	}
}
