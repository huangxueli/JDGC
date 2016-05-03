package com.wzkcy.jdgc.bean;

import java.util.List;

public class StatisticsNum {
	
	private String jgmc;
	
	private List<Policeman> mPolicemanTList; 
	private List<Policeman> mPolicemanOList;
	private List<Policeman> mPolicemanFList; 
	private List<Station> mNormalStationTList; 
	private List<Station> mNormalStationOList; 
	private List<Station> mNormalStationFList; 
	private List<Station> mPoliceStationTList; 
	private List<Station> mPoliceStationOList; 
	private List<Station> mPoliceStationFList; 
	
	private int policeTNum; // 警员总计人数
	private int policeONum; // 警员在线人数
	private int policeFNum; // 警员离线人数
	
	private int normalStationTNum; // 联防岗亭总计人数
	private int normalStationONum; // 联防岗亭在线人数
	private int normalStationFNum; // 联防岗亭离线人数
	
	private int policeStationTNum; // 民警岗亭总计人数
	private int policeStationONum; // 民警岗亭在线人数
	private int policeStationFNum; // 民警岗亭离线人数
	
	private int lightTNum; // 所有警灯总计人数
	private int lightONum; // 所有警灯在线人数
	private int lightFNum; // 所有警灯离线人数
	
	public StatisticsNum(String jgmc,
			List<Policeman> mPolicemanOList, List<Policeman> mPolicemanFList, List<Policeman> mPolicemanTList, 
			List<Station> mNormalStationOList, List<Station> mNormalStationFList, List<Station> mNormalStationTList,
			List<Station> mPoliceStationOList, List<Station> mPoliceStationFList, List<Station> mPoliceStationTList){
		this.jgmc = jgmc;
		this.mPolicemanOList = mPolicemanOList;
		this.mPolicemanFList = mPolicemanFList;
		this.mPolicemanTList = mPolicemanTList;
		this.mNormalStationOList = mNormalStationOList;
		this.mNormalStationFList = mNormalStationFList;
		this.mNormalStationTList = mNormalStationTList;
		this.mPoliceStationOList = mPoliceStationOList;
		this.mPoliceStationFList = mPoliceStationFList;
		this.mPoliceStationTList = mPoliceStationTList;
		
		this.policeONum = mPolicemanOList.size();
		this.policeFNum = mPolicemanFList.size();
		this.policeTNum = mPolicemanTList.size();
		this.normalStationONum = mNormalStationOList.size();
		this.normalStationFNum = mNormalStationFList.size();
		this.normalStationTNum = mNormalStationTList.size();
		this.policeStationONum = mPoliceStationOList.size();
		this.policeStationFNum = mPoliceStationFList.size();
		this.policeStationTNum = mPoliceStationTList.size();
		this.lightONum = policeONum + normalStationONum + policeStationONum;
		this.lightFNum = policeFNum + normalStationFNum + policeStationFNum;
		this.lightTNum = policeTNum + normalStationTNum + policeStationTNum;
	}
	
	public String getJgmc() {
		return jgmc;
	}
	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	public int getPoliceTNum() {
		return policeTNum ;
	}
	public void setPoliceTNum(int policeTNum) {
		this.policeTNum = policeTNum;
	}
	public int getPoliceONum() {
		return policeONum;
	}
	public void setPoliceONum(int policeONum) {
		this.policeONum = policeONum;
	}
	public int getPoliceFNum() {
		return policeFNum;
	}
	public void setPoliceFNum(int policeFNum) {
		this.policeFNum = policeFNum;
	}
	public int getMoveStationTNum() {
		return normalStationTNum;
	}
	public void setMoveStationTNum(int normalStationTNum) {
		this.normalStationTNum = normalStationTNum;
	}
	public int getMoveStationONum() {
		return normalStationONum;
	}
	public void setMoveStationONum(int normalStationONum) {
		this.normalStationONum = normalStationONum;
	}
	public int getMoveStationFNum() {
		return normalStationFNum;
	}
	public void setMoveStationFNum(int normalStationFNum) {
		this.normalStationFNum = normalStationFNum;
	}
	public int getStationTNum() {
		return policeStationTNum;
	}
	public void setStationTNum(int policeStationTNum) {
		this.policeStationTNum = policeStationTNum;
	}
	public int getStationONum() {
		return policeStationONum;
	}
	public void setStationONum(int policeStationONum) {
		this.policeStationONum = policeStationONum;
	}
	public int getStationFNum() {
		return policeStationFNum;
	}
	public void setStationFNum(int policeStationFNum) {
		this.policeStationFNum = policeStationFNum;
	}
	public int getLightTNum() {
		return lightTNum;
	}
	public void setLightTNum(int lightTNum) {
		this.lightTNum = lightTNum;
	}
	public int getLightONum() {
		return lightONum;
	}
	public void setLightONum(int lightONum) {
		this.lightONum = lightONum;
	}
	public int getLightFNum() {
		return lightFNum;
	}
	public void setLightFNum(int lightFNum) {
		this.lightFNum = lightFNum;
	}

	public List<Policeman> getPolicemanTList() {
		return mPolicemanTList;
	}

	public void setPolicemanTList(List<Policeman> mPolicemanTList) {
		this.mPolicemanTList = mPolicemanTList;
	}

	public List<Policeman> getPolicemanOList() {
		return mPolicemanOList;
	}

	public void setPolicemanOList(List<Policeman> mPolicemanOList) {
		this.mPolicemanOList = mPolicemanOList;
	}

	public List<Policeman> getPolicemanFList() {
		return mPolicemanFList;
	}

	public void setPolicemanFList(List<Policeman> mPolicemanFList) {
		this.mPolicemanFList = mPolicemanFList;
	}

	public List<Station> getNormalStationTList() {
		return mNormalStationTList;
	}

	public void setNormalStationTList(List<Station> mNormalStationTList) {
		this.mNormalStationTList = mNormalStationTList;
	}

	public List<Station> getNormalStationOList() {
		return mNormalStationOList;
	}

	public void setNormalStationOList(List<Station> mNormalStationOList) {
		this.mNormalStationOList = mNormalStationOList;
	}

	public List<Station> getNormalStationFList() {
		return mNormalStationFList;
	}

	public void setNormalStationFList(List<Station> mNormalStationFList) {
		this.mNormalStationFList = mNormalStationFList;
	}

	public List<Station> getStationTList() {
		return mPoliceStationTList;
	}

	public void setStationTList(List<Station> mPoliceStationTList) {
		this.mPoliceStationTList = mPoliceStationTList;
	}

	public List<Station> getStationOList() {
		return mPoliceStationOList;
	}

	public void setStationOList(List<Station> mPoliceStationOList) {
		this.mPoliceStationOList = mPoliceStationOList;
	}

	public List<Station> getStationFList() {
		return mPoliceStationFList;
	}

	public void setStationFList(List<Station> mPoliceStationFList) {
		this.mPoliceStationFList = mPoliceStationFList;
	}
}
