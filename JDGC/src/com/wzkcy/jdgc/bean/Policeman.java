package com.wzkcy.jdgc.bean;

public class Policeman extends Light{
	
	private String mjxm; // 民警姓名
	private String mjjh; // 民警警号
	private String sfzh; // 身份证号
	private String sjhm; // 手机号码
	private String jgbm; // 机构编码
	private String jgmc; // 机构名称
	private String gps_jd; 	// 经度
	private String gps_wd; 	// 经度
	private String gtdm;	// 民警执勤的岗亭代码
	private String gtjgbm;	// 执勤岗亭机构编码
	private String gtjgmc;	// 执勤岗亭机构名称
	private String gtqydm;  // 执勤岗亭区域代码
	private String gtqymc;	// 执勤岗亭区域名称（现在通过该字段判断该民警所属区域）
	
	public String getMjxm() {
		return mjxm;
	}
	public void setMjxm(String mjxm) {
		this.mjxm = mjxm;
	}
	public String getMjjh() {
		return mjjh;
	}
	public void setMjjh(String mjjh) {
		this.mjjh = mjjh;
	}
	public String getSfzh() {
		return sfzh;
	}
	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}
	public String getSjhm() {
		return sjhm;
	}
	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}
	public String getJgbm() {
		return jgbm;
	}
	public void setJgbm(String jgbm) {
		this.jgbm = jgbm;
	}
	public String getJgmc() {
		return jgmc;
	}
	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
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
	public String getGtdm() {
		return gtdm;
	}
	public void setGtdm(String gtdm) {
		this.gtdm = gtdm;
	}
	public String getGtjgbm() {
		return gtjgbm;
	}
	public void setGtjgbm(String gtjgbm) {
		this.gtjgbm = gtjgbm;
	}
	public String getGtjgmc() {
		return gtjgmc;
	}
	public void setGtjgmc(String gtjgmc) {
		this.gtjgmc = gtjgmc;
	}
	public String getGtqydm() {
		return gtqydm;
	}
	public void setGtqydm(String gtqydm) {
		this.gtqydm = gtqydm;
	}
	public String getGtqymc() {
		return gtqymc;
	}
	public void setGtqymc(String gtqymc) {
		this.gtqymc = gtqymc;
	}
	@Override
	public String getName() {
		return getMjxm();
	}
}
