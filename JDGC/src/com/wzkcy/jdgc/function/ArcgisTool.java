package com.wzkcy.jdgc.function;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.CompositeSymbol;
import com.esri.core.symbol.FontStyle;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleLineSymbol.STYLE;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.TextSymbol;
import com.wzkcy.jdgc.MyApplication;
import com.wzkcy.jdgc.R;
import com.wzkcy.jdgc.fragment.MainFragment.LightKind;

public class ArcgisTool {
	
	public static final String ATTRIBUTE_KEY_ID = "ID";
	public static final String ATTRIBUTE_KEY_TYPE = "Type";
	public static final String ATTRIBUTE_KEY_ONLINE = "isOnLine";
	
	public static int getGraphicId(MapView map, float screenx, float screeny, GraphicsLayer layer) {
		int graphicId = -1;
		int[] graphicIDs = layer.getGraphicIDs(screenx, screeny, 10);
		if(graphicIDs.length>0)
			graphicId = graphicIDs[0];
		return graphicId;
	}
	
	public static int addGraphic(Geometry geometry, int imageRes, GraphicsLayer layer){
		int graphicId = -1;
		if(layer !=null){
			Drawable drawable = MyApplication.Resources.getDrawable(imageRes);
			PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
//			symbol.setOffsetY(symbol.getHeight()/8); // 负数向上 正数像下
			Map<String, Object> attributes = new HashMap<String, Object>();
			Graphic graphic = new Graphic(geometry, symbol, attributes);
			graphicId = layer.addGraphic(graphic);
		}
		return graphicId;
	}
	public static void updateGraphicSymbol(int graphicId, int imageRes, GraphicsLayer layer){
		if(layer !=null){
			Drawable drawable = MyApplication.Resources.getDrawable(imageRes);
			PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
			layer.updateGraphic(graphicId, symbol);
		}
	}
	public static void updateGraphicAttributes(int graphicId, GraphicsLayer graphicsLayer, String id, LightKind type, boolean isOnLine){
		Graphic graphic = graphicsLayer.getGraphic(graphicId);
		Map<String, Object> attributes = graphic.getAttributes();
		attributes.put(ATTRIBUTE_KEY_ID, id);
		attributes.put(ATTRIBUTE_KEY_TYPE, type);
		attributes.put(ATTRIBUTE_KEY_ONLINE, isOnLine);
		graphicsLayer.updateGraphic(graphicId, attributes);
	}
	public static int addTextGraphic(Point point, String text, MapView map, GraphicsLayer textLayer, int resID){
		if(textLayer!=null){
			TextSymbol textSymbol = new TextSymbol(14, text, MyApplication.Resources.getColor(resID));
			textSymbol.setOffsetX(13);	
			textSymbol.setOffsetY(-10);	
			textSymbol.setFontStyle(FontStyle.NORMAL);
			textSymbol.setFontFamily("DroidSansFallback.ttf");
			textSymbol.setSize(15f);
			return textLayer.addGraphic(new Graphic(point, textSymbol, null));
		}
		return -1;
	}
	
	public static int addTextGraphic(Point point, String text, MapView map, GraphicsLayer textLayer, int x, int y){
		if(textLayer!=null){
			TextSymbol textSymbol = new TextSymbol(14, text, MyApplication.Resources.getColor(R.color.line_color2));
			textSymbol.setOffsetX(13+x);	
			textSymbol.setOffsetY(-10-y);	
			textSymbol.setFontStyle(FontStyle.NORMAL);
			textSymbol.setFontFamily("DroidSansFallback.ttf");
			textSymbol.setSize(15f);
			return textLayer.addGraphic(new Graphic(point, textSymbol, null));
		}
		return -1;
	}
	
	public static int addTextGraphic(Point point, String text, MapView map, GraphicsLayer textLayer){
		return addTextGraphic(point, text, map, textLayer, R.color.line_color2);
	}
	
	public static int addLineGraphic(Geometry geometry, Symbol symbol, GraphicsLayer layer){
		int graphicId = -1;
		if(layer !=null){
			Map<String, Object> attributes = new HashMap<String, Object>();
			Graphic graphic = new Graphic(geometry, symbol, attributes);
			graphicId = layer.addGraphic(graphic);
		}
		return graphicId;
	}
	
	public static void removeGraphic(float screenx, float screeny, MapView map, GraphicsLayer layer){
		if(layer !=null){
			int id = getGraphicId(map, screenx, screeny, layer);
			layer.removeGraphic(id);
		}
	}
	
	public static void removeGraphic(int graphicId, GraphicsLayer layer){
		if(layer !=null){
			Graphic graphic = layer.getGraphic(graphicId);
			if(graphic!=null){
				layer.removeGraphic(graphicId);
			}
		}
	}
	
	public static Symbol getLineFourSymbol(String type){
		STYLE style = null;
		if(type == null){
			style = SimpleLineSymbol.STYLE.SOLID;
		}else{
			switch(type){
			case "绝缘导线":
				style = SimpleLineSymbol.STYLE.SOLID;
				break;
			case "电缆":
				style = SimpleLineSymbol.STYLE.DASH;
				break;
			case "裸导线":
				style = SimpleLineSymbol.STYLE.SOLID;
				break;
			}
		}
		SimpleLineSymbol symbol1 = new SimpleLineSymbol(MyApplication.Resources.getColor(R.color.line_color1), 15, style);
		SimpleLineSymbol symbol2 = new SimpleLineSymbol(Color.WHITE, 12, SimpleLineSymbol.STYLE.SOLID);
		SimpleLineSymbol symbol3 = new SimpleLineSymbol(MyApplication.Resources.getColor(R.color.line_color1), 6, style);
		SimpleLineSymbol symbol4 = new SimpleLineSymbol(Color.WHITE, 3, SimpleLineSymbol.STYLE.SOLID);
		CompositeSymbol symbol = new CompositeSymbol();
		symbol.add(symbol1);
		symbol.add(symbol2);
		symbol.add(symbol3);
		symbol.add(symbol4);
		return symbol;
	}
	public static Symbol getLineTwoSymbol(String type){
		STYLE style = null;
		if(type == null){
			style = SimpleLineSymbol.STYLE.SOLID;
		}else{
			switch(type){
			case "绝缘导线":
				style = SimpleLineSymbol.STYLE.SOLID;
				break;
			case "电缆":
				style = SimpleLineSymbol.STYLE.DASH;
				break;
			case "裸导线":
				style = SimpleLineSymbol.STYLE.SOLID;
				break;
			}
		}
		SimpleLineSymbol symbol1 = new SimpleLineSymbol(MyApplication.Resources.getColor(R.color.line_color1), 6, style);
		SimpleLineSymbol symbol2 = new SimpleLineSymbol(Color.WHITE, 3, SimpleLineSymbol.STYLE.SOLID);
		CompositeSymbol symbol = new CompositeSymbol();
		symbol.add(symbol1);
		symbol.add(symbol2);
		return symbol;
	}
	public static Symbol getLineOneSymbol(String type, int index){
		STYLE style = null;
		if(type == null){
			style = SimpleLineSymbol.STYLE.SOLID;
		}else{
			switch(type){
			case "绝缘导线":
				style = SimpleLineSymbol.STYLE.SOLID;
				break;
			case "电缆":
				style = SimpleLineSymbol.STYLE.DASH;
				break;
			case "裸导线":
				style = SimpleLineSymbol.STYLE.SOLID;
				break;
			}
		}
		SimpleLineSymbol mSimpleLineSymbol = null;
		int color = getColorByIndex(index);
		CompositeSymbol mCompositeSymbol = new CompositeSymbol();
		if(color !=-1){
			mSimpleLineSymbol = new SimpleLineSymbol(color, 2, style);
			mCompositeSymbol.add(mSimpleLineSymbol);
		}
		return mCompositeSymbol;
	}
	public static int getColorByIndex(int index){
		int color = -1;
		switch(index){
		case 0:
			color = MyApplication.Resources.getColor(R.color.red);
			break;
		case 1:
			color = MyApplication.Resources.getColor(R.color.blue);
			break;
		case 2:
			color = MyApplication.Resources.getColor(R.color.green);
			break;
		case 3:
			color = MyApplication.Resources.getColor(R.color.yellow);
			break;
		case 4: // 找不到对应的线路名称的情况下
			color = MyApplication.Resources.getColor(R.color.gray_light);
			break;
		}
		return color;
	}
	
	
	public static int accordingNameGetDrawableResource(String name){
		int resid = -1;
		switch(name){
		
		case "g_more":
			resid = R.drawable.g_more;
			break;
		case "y_more":
			resid = R.drawable.y_more;
			break;
		case "r_more":
			resid = R.drawable.r_more;
			break;
		default:
			resid = R.drawable.b0;
			break;
		}
		return resid;
	}
}
