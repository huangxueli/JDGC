package com.wzkcy.jdgc.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wzkcy.jdgc.MyApplication;
import com.wzkcy.jdgc.R;
import com.wzkcy.jdgc.bean.JG;
import com.wzkcy.jdgc.bean.Light;
import com.wzkcy.jdgc.bean.Policeman;
import com.wzkcy.jdgc.bean.Station;
import com.wzkcy.jdgc.bean.StatisticsNum;
import com.wzkcy.jdgc.fragment.StatisticsFragment;

public class StatisticsAdapter extends BaseAdapter {
	
	public final static String TAG = "StatisticsAdapter";
	
	private ArrayList<StatisticsNum> mList = new ArrayList<StatisticsNum>();
	private StatisticsFragment mStatisticsFragment;
	
	private List<Policeman> mPolicemanTList; // 温州市所有警员
	private List<Policeman> mPolicemanOList; // 温州市在线警员
	private List<Policeman> mPolicemanFList; // 温州市离线警员
	private List<Station> mNormalStationTList; // 温州市所有联防岗亭（移动岗亭）
	private List<Station> mNormalStationOList; // 温州市在线联防岗亭（移动岗亭）
	private List<Station> mNormalStationFList; // 温州市离线联防岗亭（移动岗亭）
	private List<Station> mPoliceStationTList; // 温州市所有民警岗亭（治安岗亭）
	private List<Station> mPoliceStationOList; // 温州市在线民警岗亭（治安岗亭）
	private List<Station> mPoliceStationFList; // 温州市离线民警岗亭（治安岗亭）
	
	private Map<String, List<JG>> mJGMap;
	
	private Map<String, List<Policeman>> mQYPoliceTMap; // 区域-警员总计
	private Map<String, List<Policeman>> mQYPoliceOMap; // 区域-警员在线
	private Map<String, List<Policeman>> mQYPoliceFMap; // 区域-警员离线
	private Map<String, List<Station>> mQYNormalStationTMap; // 区域-联防岗亭（移动岗亭）总计
	private Map<String, List<Station>> mQYNormalStationOMap; // 区域-联防岗亭（移动岗亭）在线
	private Map<String, List<Station>> mQYNormalStationFMap; // 区域-联防岗亭（移动岗亭）离线
	private Map<String, List<Station>> mQYPoliceStationTMap; // 区域-民警岗亭（治安岗亭	）总计
	private Map<String, List<Station>> mQYPoliceStationOMap; // 区域-民警岗亭（治安岗亭	）在线
	private Map<String, List<Station>> mQYPoliceStationFMap; // 区域-民警岗亭（治安岗亭	）离线
	
	private Map<String, Map<String, List<Policeman>>> mQYJGPoliceTMap; // 机构-警员总计
	private Map<String, Map<String, List<Policeman>>> mQYJGPoliceOMap; // 机构-警员在线
	private Map<String, Map<String, List<Policeman>>> mQYJGPoliceFMap; // 机构-警员离线
	private Map<String, Map<String, List<Station>>> mQYJGNormalStationTMap; // 机构-联防岗亭（移动岗亭）总计
	private Map<String, Map<String, List<Station>>> mQYJGNormalStationOMap; // 机构-联防岗亭（移动岗亭）在线
	private Map<String, Map<String, List<Station>>> mQYJGNormalStationFMap; // 机构-联防岗亭（移动岗亭）离线
	private Map<String, Map<String, List<Station>>> mQYJGPoliceStationTMap; // 机构-民警岗亭（治安岗亭）总计
	private Map<String, Map<String, List<Station>>> mQYJGPoliceStationOMap; // 机构-民警岗亭（治安岗亭）在线
	private Map<String, Map<String, List<Station>>> mQYJGPoliceStationFMap; // 机构-民警岗亭（治安岗亭）离线
	
	public StatisticsAdapter(StatisticsFragment statisticsFragment) {
		mStatisticsFragment = statisticsFragment;
		mPolicemanTList = mStatisticsFragment.getPolicemanTList();
		mPolicemanOList = mStatisticsFragment.getPolicemanOList();
		mPolicemanFList = mStatisticsFragment.getPolicemanFList();
		mNormalStationTList = mStatisticsFragment.getNormalStationTList();
		mNormalStationOList = mStatisticsFragment.getNormalStationOList();
		mNormalStationFList = mStatisticsFragment.getNormalStationFList();
		mPoliceStationTList = mStatisticsFragment.getStationTList();
		mPoliceStationOList = mStatisticsFragment.getStationOList();
		mPoliceStationFList = mStatisticsFragment.getStationFList();
		
		mQYPoliceTMap = mStatisticsFragment.getQYPoliceTMap();
		mQYPoliceOMap = mStatisticsFragment.getQYPoliceOMap();
		mQYPoliceFMap = mStatisticsFragment.getQYPoliceFMap();
		mQYNormalStationTMap = mStatisticsFragment.getQYNormalStationTMap();
		mQYNormalStationOMap = mStatisticsFragment.getQYNormalStationOMap();
		mQYNormalStationFMap = mStatisticsFragment.getQYNormalStationFMap();
		mQYPoliceStationTMap = mStatisticsFragment.getQYPoliceStationTMap();
		mQYPoliceStationOMap = mStatisticsFragment.getQYPoliceStationOMap();
		mQYPoliceStationFMap = mStatisticsFragment.getQYPoliceStationFMap();
		
		mQYJGPoliceTMap = mStatisticsFragment.getQYJGPoliceTMap();
		mQYJGPoliceOMap = mStatisticsFragment.getQYJGPoliceOMap();
		mQYJGPoliceFMap = mStatisticsFragment.getQYJGPoliceFMap();
		mQYJGNormalStationTMap = mStatisticsFragment.getQYJGNormalStationTMap();
		mQYJGNormalStationOMap = mStatisticsFragment.getQYJGNormalStationOMap();
		mQYJGNormalStationFMap = mStatisticsFragment.getQYJGNormalStationFMap();
		mQYJGPoliceStationTMap = mStatisticsFragment.getQYJGPoliceStationTMap();
		mQYJGPoliceStationOMap = mStatisticsFragment.getQYJGPoliceStationOMap();
		mQYJGPoliceStationFMap = mStatisticsFragment.getQYJGPoliceStationFMap();
		
		mJGMap = mStatisticsFragment.getJGMap();
		
		// 添加温州市下的所有区的数据统计
		showFirstForm(false);
		
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override  
    public boolean areAllItemsEnabled() {  
        return false;  
    }  
      
    @Override  
    public boolean isEnabled(int position) {  
        return false;  
    }  
    
	public final class ViewHolder {
		StatisticsNum num;
		
		TextView table1234_0;
//		TextView table1_2;
//		TextView table2_2;
//		TextView table3_2;
//		TextView table4_2;
//		TextView table1_3;
//		TextView table2_3;
//		TextView table3_3;
//		TextView table4_3;
		TextView table1_4;
		TextView table2_4;
		TextView table3_4;
		TextView table4_4;
		
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		final ViewHolder holder;
		
		if (convertView == null) {
			convertView = MyApplication.Inflater.inflate(R.layout.listitem_statistics_new, parent, false);
			holder = new ViewHolder();
			holder.table1234_0 = (TextView) convertView.findViewById(R.id.table1234_0);
//			holder.table1234_0.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//			holder.table1234_0.setTextColor(MyApplication.Resources.getColor(R.color.blue));
			
//			holder.table1_2 = (TextView) convertView.findViewById(R.id.table1_2);
//			holder.table2_2 = (TextView) convertView.findViewById(R.id.table2_2);
//			holder.table3_2 = (TextView) convertView.findViewById(R.id.table3_2);
//			holder.table4_2 = (TextView) convertView.findViewById(R.id.table4_2);
//			holder.table1_3 = (TextView) convertView.findViewById(R.id.table1_3);
//			holder.table2_3 = (TextView) convertView.findViewById(R.id.table2_3);
//			holder.table3_3 = (TextView) convertView.findViewById(R.id.table3_3);
//			holder.table4_3 = (TextView) convertView.findViewById(R.id.table4_3);
			
			holder.table1_4 = (TextView) convertView.findViewById(R.id.table1_4);
			holder.table1_4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.table1_4.setTextColor(MyApplication.Resources.getColor(R.color.black));
			holder.table2_4 = (TextView) convertView.findViewById(R.id.table2_4);
			holder.table2_4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.table2_4.setTextColor(MyApplication.Resources.getColor(R.color.black));
			holder.table3_4 = (TextView) convertView.findViewById(R.id.table3_4);
			holder.table3_4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.table3_4.setTextColor(MyApplication.Resources.getColor(R.color.black));
			holder.table4_4 = (TextView) convertView.findViewById(R.id.table4_4);
			holder.table4_4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.table4_4.setTextColor(MyApplication.Resources.getColor(R.color.black));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		StatisticsNum num = mList.get(position);
		holder.num = num; 
		holder.table1234_0.setText(num.getJgmc());
		// 第二列
//		holder.table1_2.setText(num.getPoliceTNum()+""); 
//		holder.table2_2.setText(num.getMoveStationTNum()+"");
//		holder.table3_2.setText(num.getStationTNum()+"");
//		holder.table4_2.setText(num.getLightTNum()+"");
		// 第三列
//		holder.table1_3.setText(num.getPoliceONum()+"");
//		holder.table2_3.setText(num.getMoveStationONum()+"");
//		holder.table3_3.setText(num.getStationONum()+"");
//		holder.table4_3.setText(num.getLightONum()+"");
		// 第四列
//		holder.table1_4.setText(num.getPoliceFNum()+"");
//		holder.table2_4.setText(num.getMoveStationFNum()+"");
//		holder.table3_4.setText(num.getStationFNum()+"");
//		holder.table4_4.setText(num.getLightFNum()+"");
		// 第四列修改为
		holder.table1_4.setText(num.getPoliceONum()+"");
		holder.table2_4.setText(num.getMoveStationONum()+"");
		holder.table3_4.setText(num.getStationONum()+"");
		holder.table4_4.setText((num.getMoveStationONum()+ num.getStationONum()) + "");
		// 区域
		holder.table1234_0.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String qy = holder.table1234_0.getText().toString();
				responseClick(parent, position, qy, 3, 4);
			}
		});
		// 第二列
//		holder.table1_2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String qy = holder.table1234_0.getText().toString();
//				responseClick(position, qy, 3, 1);
//			}
//		});
//		holder.table2_2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String qy = holder.table1234_0.getText().toString();
//				responseClick(position, qy, 3, 2);
//			}
//		});
//		holder.table3_2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String qy = holder.table1234_0.getText().toString();
//				responseClick(position, qy, 3, 3);
//			}
//		});
//		holder.table4_2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String qy = holder.table1234_0.getText().toString();
//				responseClick(position, qy, 3, 4);
//			}
//		});
		// 第三列
//		holder.table1_3.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String qy = holder.table1234_0.getText().toString();
//				responseClick(position, qy, 1, 1);
//			}
//		});
//		holder.table2_3.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String qy = holder.table1234_0.getText().toString();
//				responseClick(position, qy, 1, 2);
//			}
//		});
//		holder.table3_3.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String qy = holder.table1234_0.getText().toString();
//				responseClick(position, qy, 1, 3);
//			}
//		});
//		holder.table4_3.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String qy = holder.table1234_0.getText().toString();
//				responseClick(position, qy, 1, 4);
//			}
//		});
		// 第四列
		holder.table1_4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String qy = holder.table1234_0.getText().toString();
				responseClick(parent, position, qy, 1, 1);
			}
		});
		holder.table2_4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String qy = holder.table1234_0.getText().toString();
				responseClick(parent, position, qy, 1, 2);
			}
		});
		holder.table3_4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String qy = holder.table1234_0.getText().toString();
				responseClick(parent, position, qy, 1, 3);
			}
		});
		holder.table4_4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String qy = holder.table1234_0.getText().toString();
				responseClick(parent, position, qy, 1, 4);
			}
		});
		return convertView;
	}
	/**
	 * 点击事件
	 * @param qy 区域名称
	 * @param state 1 在线  	2离线	3总数
	 * @param type  1 警员  	2联防岗亭（移动岗亭）	3民警岗亭（治安岗亭）	4警灯
	 */
	private void responseClick(ViewGroup parent, int position, String qy, int state, int type){
		if(position==0){
			if(qy.equals("温州市")){
				List<Light> wzlist = getWZDataDetail(state, type);
				mStatisticsFragment.resetDetailListView(wzlist);
				mStatisticsFragment.showListLayout(state, type, "温州市");
				
			}else{
				List<Light> list = getQYDataDetail(qy, state, type);
				mStatisticsFragment.resetDetailListView(list);
				mStatisticsFragment.showListLayout(state, type, qy);
			}
		}else{
			List<JG> jglist = mJGMap.get(qy);
			if(mStatisticsFragment.isSecondFrom()){
				// 已经是二级列表， 显示详细列表
				String jg = qy;
				String belongqy = getBelongQY(jg);
				Log.e(TAG, belongqy + "-" + jg);
				List<Light> list = getJGDataDetail(belongqy, jg, state, type);
				mStatisticsFragment.resetDetailListView(list);
				mStatisticsFragment.showListLayout(state, type, jg);
			} else if(jglist!=null){
				showSecondForm(qy, jglist); // 显示第二级表格（区域-所）
				mStatisticsFragment.setIsSecondFrom(true);
			}
		}
	}
	// 获取机构对应的行政区域
	private String getBelongQY(String jg){
		String belongqy = null;
		for(String qyname : mJGMap.keySet()){
			List<JG> jglists = mJGMap.get(qyname);
			for(JG temp : jglists){
				if(jg.equals(temp.getJgmc())){
					belongqy = qyname;
					break;
				}
			}
			if(belongqy!=null){
				break;
			}
		}
		return belongqy;
	}
	/**
	 * 获取温州数据
	 * @param qy 区域名称
	 * @param state 1 在线  	2离线	3总数
	 * @param type  1 警员  	2联防岗亭（移动岗亭）	3民警岗亭（治安岗亭）	4警灯
	 */
	private List<Light> getWZDataDetail(int state, int type){
		List<Light>  list = new ArrayList<Light>();
		switch(state){
		case 1: // 在线
			switch(type){
			case 1: // 警员
				list.addAll(mPolicemanOList);
				break;
			case 2: // 联防岗亭（移动岗亭）
				list.addAll(mNormalStationOList);
				break;
			case 3: // 民警岗亭（治安岗亭）
				list.addAll(mPoliceStationOList);
				break;
			case 4: // 警灯
				list.addAll(mNormalStationOList);
				list.addAll(0, mPoliceStationOList);
//				list.addAll(0, mPolicemanOList);
				break;
			}
			break;
		case 2: // 离线
			switch(type){
			case 1: // 警员
				list.addAll(mPolicemanFList);
				break;
			case 2: // 联防岗亭（移动岗亭）
				list.addAll(mNormalStationFList);
				break;
			case 3: // 民警岗亭（治安岗亭）
				list.addAll(mPoliceStationFList);
				break;
			case 4: // 警灯
				list.addAll(mNormalStationFList);
				list.addAll(0, mPoliceStationFList);
//				list.addAll(0, mPolicemanFList);
				break;
			}
			break;
		case 3: // 总计
			switch(type){
			case 1: // 警员
				list.addAll(mPolicemanTList);
				break;
			case 2: // 联防岗亭（移动岗亭）
				list.addAll(mNormalStationTList);
				break;
			case 3: // 民警岗亭（治安岗亭）
				list.addAll(mPoliceStationTList);
				break;
			case 4: // 警灯
				list.addAll(mNormalStationFList);
				list.addAll(0, mPoliceStationFList);
//				list.addAll(0, mPolicemanFList);
				list.addAll(0, mNormalStationOList);
				list.addAll(0, mPoliceStationOList);
//				list.addAll(0, mPolicemanOList);
				break;
			}
			break;
		}
		return list;
	}
	/**
	 * 获取区域数据
	 * @param qy 区域名称
	 * @param state 1 在线  	2离线	3总数
	 * @param type  1 警员  	2联防岗亭（移动岗亭）	3民警岗亭（治安岗亭）	4警灯
	 */
	private List<Light> getQYDataDetail(String qy, int state, int type){
		List<Light>  list = new ArrayList<Light>();
		List<Policeman> qyPoliceOlist = new ArrayList<Policeman>();
		qyPoliceOlist = mQYPoliceOMap.get(qy);
		List<Station> qyNormalStationOlist = new ArrayList<Station>();
		qyNormalStationOlist = mQYNormalStationOMap.get(qy);
		List<Station> qyPoliceStationOlist = new ArrayList<Station>();
		qyPoliceStationOlist = mQYPoliceStationOMap.get(qy);
		
		List<Policeman> qyPoliceFlist = new ArrayList<Policeman>();
		qyPoliceFlist = mQYPoliceFMap.get(qy);
		List<Station> qyNormalStationFlist = new ArrayList<Station>();
		qyNormalStationFlist = mQYNormalStationFMap.get(qy);
		List<Station> qyPoliceStationFlist = new ArrayList<Station>();
		qyPoliceStationFlist = mQYPoliceStationFMap.get(qy);
		
		List<Policeman> qyPoliceTlist = new ArrayList<Policeman>();
		qyPoliceTlist = mQYPoliceTMap.get(qy);
		List<Station> qyNormalStationTlist = new ArrayList<Station>();
		qyNormalStationTlist = mQYNormalStationTMap.get(qy);
		List<Station> qyPoliceStationTlist = new ArrayList<Station>();
		qyPoliceStationTlist = mQYPoliceStationTMap.get(qy);
		switch(state){
		case 1: // 在线
			switch(type){
			case 1: // 警员
				list.addAll(qyPoliceOlist);
				break;
			case 2: // 联防岗亭（移动岗亭）
				list.addAll(qyNormalStationOlist);
				break;
			case 3: // 民警岗亭（治安岗亭）
				list.addAll(qyPoliceStationOlist);
				break;
			case 4: // 警灯
				list.addAll(qyNormalStationOlist);
				list.addAll(0, qyPoliceStationOlist);
//				list.addAll(0, qyPoliceOlist);
				break;
			}
			break;
		case 2: // 离线
			switch(type){
			case 1: // 警员
				list.addAll(qyPoliceFlist);
				break;
			case 2: // 联防岗亭（移动岗亭）
				list.addAll(qyNormalStationFlist);
				break;
			case 3: // 民警岗亭（治安岗亭）
				list.addAll(qyPoliceStationFlist);
				break;
			case 4: // 警灯
				list.addAll(qyNormalStationFlist);
				list.addAll(0, qyPoliceStationFlist);
//				list.addAll(0, qyPoliceFlist);
				break;
			}
			break;
		case 3: // 总计
			switch(type){
			case 1: // 警员
				list.addAll(qyPoliceTlist);
				break;
			case 2: // 联防岗亭（移动岗亭）
				list.addAll(qyNormalStationTlist);
				break;
			case 3: // 民警岗亭（治安岗亭）
				list.addAll(qyPoliceStationTlist);
				break;
			case 4: // 警灯
				list.addAll(qyNormalStationFlist);
				list.addAll(0, qyPoliceStationFlist);
//				list.addAll(0, qyPoliceFlist);
				list.addAll(0, qyNormalStationOlist);
				list.addAll(0, qyPoliceStationOlist);
//				list.addAll(0, qyPoliceOlist);
				break;
			}
			break;
		}
		return list;
	}
	/**
	 * 获取机构数据
	 * @param jg 机构名称
	 * @param state 1 在线  	2离线	3总数
	 * @param type  1 警员  	2联防岗亭（移动岗亭）	3民警岗亭（治安岗亭）	4警灯
	 */
	private List<Light> getJGDataDetail(String qy, String jg, int state, int type){
		List<Light>  list = new ArrayList<Light>();
		
		List<Policeman> jgPoliceOlist = new ArrayList<Policeman>();
		jgPoliceOlist = mQYJGPoliceOMap.get(qy).get(jg);
		List<Station> jgNormalStationOlist = new ArrayList<Station>();
		jgNormalStationOlist = mQYJGNormalStationOMap.get(qy).get(jg);
		List<Station> jgPoliceStationOlist = new ArrayList<Station>();
		jgPoliceStationOlist = mQYJGPoliceStationOMap.get(qy).get(jg);
		
		List<Policeman> jgPoliceFlist = new ArrayList<Policeman>();
		jgPoliceFlist = mQYJGPoliceFMap.get(qy).get(jg);
		List<Station> jgNormalStationFlist = new ArrayList<Station>();
		jgNormalStationFlist = mQYJGNormalStationFMap.get(qy).get(jg);
		List<Station> jgPoliceStationFlist = new ArrayList<Station>();
		jgPoliceStationFlist = mQYJGPoliceStationFMap.get(qy).get(jg);
		
		List<Policeman> jgptlist = new ArrayList<Policeman>();
		jgptlist = mQYJGPoliceTMap.get(qy).get(jg);
		List<Station> jgsmtlist = new ArrayList<Station>();
		jgsmtlist = mQYJGNormalStationTMap.get(qy).get(jg);
		List<Station> jgstlist = new ArrayList<Station>();
		jgstlist = mQYJGPoliceStationTMap.get(qy).get(jg);
		switch(state){
		case 1: // 在线
			switch(type){
			case 1: // 警员
				list.addAll(jgPoliceOlist);
				break;
			case 2: // 联防岗亭（移动岗亭）
				list.addAll(jgNormalStationOlist);
				break;
			case 3: // 民警岗亭（治安岗亭）
				list.addAll(jgPoliceStationOlist);
				break;
			case 4: // 警灯
				list.addAll(jgNormalStationOlist);
				list.addAll(0, jgPoliceStationOlist);
//				list.addAll(0, jgPoliceOlist);
				break;
			}
			break;
		case 2: // 离线
			switch(type){
			case 1: // 警员
				list.addAll(jgPoliceFlist);
				break;
			case 2: // 联防岗亭（移动岗亭）
				list.addAll(jgNormalStationFlist);
				break;
			case 3: // 民警岗亭（治安岗亭）
				list.addAll(jgPoliceStationFlist);
				break;
			case 4: // 警灯
				list.addAll(jgNormalStationFlist);
				list.addAll(0, jgPoliceStationFlist);
//				list.addAll(0, jgPoliceFlist); // 警灯= 联防岗亭+民警岗亭
				break;
			}
			break;
		case 3: // 总计
			switch(type){
			case 1: // 警员
				list.addAll(jgptlist);
				break;
			case 2: // 联防岗亭（移动岗亭）
				list.addAll(jgsmtlist);
				break;
			case 3: // 民警岗亭（治安岗亭）
				list.addAll(jgstlist);
				break;
			case 4: // 警灯
				list.addAll(jgNormalStationFlist);
				list.addAll(0, jgPoliceStationFlist);
//				list.addAll(0, jgPoliceFlist);
				list.addAll(0, jgNormalStationOlist);
				list.addAll(0, jgPoliceStationOlist);
//				list.addAll(0, jgPoliceOlist);
				break;
			}
			break;
		}
		return list;
	}
	
	public void showFirstForm(boolean clear){
		mStatisticsFragment.setTitleText(1, -1, -1, "");
		if(clear){
			mList.clear();
		}
		StatisticsNum wznum = new StatisticsNum("温州市", 
				mPolicemanOList, mPolicemanFList, mPolicemanTList,
				mNormalStationOList, mNormalStationFList, mNormalStationTList,
				mPoliceStationOList, mPoliceStationFList, mPoliceStationTList);
		mList.add(wznum);
		List<String> qylist = mStatisticsFragment.getMainFragment().getQYStringListData();
		for(String qy : qylist){
			if(qy.trim().equals("")) continue;
			// 警员
			List<Policeman> qyPoliceOlist = mQYPoliceOMap.get(qy);
			List<Policeman> qyPoliceFlist = mQYPoliceFMap.get(qy);
			List<Policeman> qyPoliceTlist = mQYPoliceTMap.get(qy);
			// 联防岗亭（移动岗亭）
			List<Station> qyNormalStationOlist = mQYNormalStationOMap.get(qy);
			List<Station> qyNormalStationFlist = mQYNormalStationFMap.get(qy);
			List<Station> qyNormalStationTlist = mQYNormalStationTMap.get(qy);
			// 民警岗亭（治安岗亭）
			List<Station> qyPoliceStationOlist = mQYPoliceStationOMap.get(qy);
			List<Station> qyPoliceStationFlist = mQYPoliceStationFMap.get(qy);
			List<Station> qyPoliceStationTlist = mQYPoliceStationTMap.get(qy);
			
			if(qyPoliceOlist==null||qyPoliceFlist==null||qyPoliceTlist==null||qyNormalStationOlist==null||qyNormalStationFlist==null||qyNormalStationTlist==null||qyPoliceStationOlist==null||qyPoliceStationFlist==null||qyPoliceStationTlist==null){
				Log.e(TAG, "ShowFirstForm  " + qy + ":空指针");
			}else{
				StatisticsNum num = new StatisticsNum(qy, qyPoliceOlist, qyPoliceFlist, qyPoliceTlist, 
						qyNormalStationOlist, qyNormalStationFlist, qyNormalStationTlist, qyPoliceStationOlist, qyPoliceStationFlist, qyPoliceStationTlist);
				mList.add(num);
				notifyDataSetChanged();
			}
		}
	}
	public void showSecondForm(String qy, List<JG> jglist){
		
		mList.clear();
		mStatisticsFragment.setTitleText(2, -1, -1, qy);
		// 警员
		List<Policeman> qyPoliceOlist = new ArrayList<Policeman>();
		qyPoliceOlist = mQYPoliceOMap.get(qy);
		List<Policeman> qyPoliceFlist = new ArrayList<Policeman>();
		qyPoliceFlist = mQYPoliceFMap.get(qy);
		List<Policeman> qyPoliceTlist = new ArrayList<Policeman>();
		qyPoliceTlist = mQYPoliceTMap.get(qy);
		// 联防岗亭（移动岗亭）
		List<Station> qyNormalStationOlist = new ArrayList<Station>();
		qyNormalStationOlist = mQYNormalStationOMap.get(qy);
		List<Station> qyNormalStationFlist = new ArrayList<Station>();
		qyNormalStationFlist = mQYNormalStationFMap.get(qy);
		List<Station> qyNormalStationTlist = new ArrayList<Station>();
		qyNormalStationTlist = mQYNormalStationTMap.get(qy);
		// 民警岗亭（治安岗亭）
		List<Station> qyPoliceStationOlist = new ArrayList<Station>();
		qyPoliceStationOlist = mQYPoliceStationOMap.get(qy);
		List<Station> qyPoliceStationFlist = new ArrayList<Station>();
		qyPoliceStationFlist = mQYPoliceStationFMap.get(qy);
		List<Station> qyPoliceStationTlist = new ArrayList<Station>();
		qyPoliceStationTlist = mQYPoliceStationTMap.get(qy);
		
		if(qyPoliceOlist==null||qyPoliceFlist==null||qyPoliceTlist==null||qyNormalStationOlist==null||qyNormalStationFlist==null||qyNormalStationTlist==null||qyPoliceStationOlist==null||qyPoliceStationFlist==null||qyPoliceStationTlist==null){
			Log.e(TAG, "ShowSecondForm  " + qy + ":空指针");
		}else{
			StatisticsNum num = new StatisticsNum(qy, qyPoliceOlist, qyPoliceFlist, qyPoliceTlist, qyNormalStationOlist, qyNormalStationFlist, qyNormalStationTlist, qyPoliceStationOlist, qyPoliceStationFlist, qyPoliceStationTlist);
			mList.add(num);
		}
		
		for(JG jg:jglist){
			String jgmc = jg.getJgmc().trim();
			if(jgmc.equals("")) continue;
			// 警员
			List<Policeman> jgPoliceOlist = new ArrayList<Policeman>();
			jgPoliceOlist = mQYJGPoliceOMap.get(qy).get(jgmc);
			List<Policeman> jgPoliceFlist = new ArrayList<Policeman>();
			jgPoliceFlist = mQYJGPoliceFMap.get(qy).get(jgmc);
			List<Policeman> jgptlist = new ArrayList<Policeman>();
			jgptlist = mQYJGPoliceTMap.get(qy).get(jgmc);
			// 联防岗亭（移动岗亭）
			List<Station> jgNormalStationOlist = new ArrayList<Station>();
			jgNormalStationOlist = mQYJGNormalStationOMap.get(qy).get(jgmc);
			List<Station> jgNormalStationFlist = new ArrayList<Station>();
			jgNormalStationFlist = mQYJGNormalStationFMap.get(qy).get(jgmc);
			List<Station> jgsmtlist = new ArrayList<Station>();
			jgsmtlist = mQYJGNormalStationTMap.get(qy).get(jgmc);
			// 民警岗亭（治安岗亭）
			List<Station> jgPoliceStationOlist = new ArrayList<Station>();
			jgPoliceStationOlist = mQYJGPoliceStationOMap.get(qy).get(jgmc);
			List<Station> jgPoliceStationFlist = new ArrayList<Station>();
			jgPoliceStationFlist = mQYJGPoliceStationFMap.get(qy).get(jgmc);
			List<Station> jgstlist = new ArrayList<Station>();
			jgstlist = mQYJGPoliceStationTMap.get(qy).get(jgmc);
			
			if(jgPoliceOlist==null||jgPoliceFlist==null||jgptlist==null||jgNormalStationOlist==null||jgNormalStationFlist==null||jgsmtlist==null||jgPoliceStationOlist==null||jgPoliceStationFlist==null||jgstlist==null){
				Log.e(TAG, "ShowSecondForm  " + qy +"-" + jgmc + ":空指针");
			}else{
				StatisticsNum num = new StatisticsNum(jgmc, jgPoliceOlist, jgPoliceFlist, jgptlist, 
						jgNormalStationOlist, jgNormalStationFlist, jgsmtlist, jgPoliceStationOlist, jgPoliceStationFlist, jgstlist);
				mList.add(num);
				notifyDataSetChanged();
			}
		}
		
	}
}
