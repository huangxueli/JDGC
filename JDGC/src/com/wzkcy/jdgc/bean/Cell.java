package com.wzkcy.jdgc.bean;
/**
 * 基站信息
 */
public class Cell {
	// SIM卡自带参数
	private int sim_mcc; // 移动国家代码（中国的为460）
	private int sim_mnc; // 移动网络号码（中国移动为00，中国联通为01）；
	private int sim_cid; // 位置区域码
	private int sim_lac; // 基站编号 16位的数据（范围是0到65535）
	// 基站表
	private String lac; 
	private String cid; 
	private String gps_jd;
	private String gps_wd;
	private String jzdz; // 建筑地址
	private String jzmc; // 建筑名称
	
	public Cell(){
		
	}
	public Cell(int sim_cid, int sim_lac){
		this.sim_cid = sim_cid;
		this.sim_lac = sim_lac;
	}
	
	public int getSim_mcc() {
		return sim_mcc;
	}
	public void setSim_mcc(int sim_mcc) {
		this.sim_mcc = sim_mcc;
	}
	public int getSim_mnc() {
		return sim_mnc;
	}
	public void setSim_mnc(int sim_mnc) {
		this.sim_mnc = sim_mnc;
	}
	public int getSim_cid() {
		return sim_cid;
	}
	public void setSim_cid(int sim_cid) {
		this.sim_cid = sim_cid;
	}
	public int getSim_lac() {
		return sim_lac;
	}
	public void setSim_lac(int sim_lac) {
		this.sim_lac = sim_lac;
	}
	public String getLac() {
		return lac;
	}
	public void setLac(String lac) {
		this.lac = lac;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getGps_jd() {
		return gps_jd;
	}
	public void setGps_jd(String gps_jd) {
		this.gps_jd = gps_jd;
	}
	public String getGps_wd() {
		return gps_wd;
	}
	public void setGps_wd(String gps_wd) {
		this.gps_wd = gps_wd;
	}
	public String getJzdz() {
		return jzdz;
	}
	public void setJzdz(String jzdz) {
		this.jzdz = jzdz;
	}
	public String getJzmc() {
		return jzmc;
	}
	public void setJzmc(String jzmc) {
		this.jzmc = jzmc;
	}
}
