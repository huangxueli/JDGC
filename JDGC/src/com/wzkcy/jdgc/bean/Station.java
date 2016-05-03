package com.wzkcy.jdgc.bean;

public class Station extends Light{
	
	private String dm; 		// 代码（ID）
	private String sfgd; 	// 是否固定（岗亭类型）
	private String gtmc; 	// 岗亭名称
	private String gtbh; 	// 岗亭编号
	private String xzqh_mc; // 行政区域名称
	private String jgmc; 	// 机构名称
	private String gtdz; 	// 岗亭地址
	private String jdbh; 	// 警灯编号
	private String lxr; 	// 联系人
	private String lxdh; 	// 联系电话
	private String bz; 		// 备注
	private String gps_jd; 	// 经度
	private String gps_wd; 	// 经度
	private String jgbm; 	// 机构编码
	private String sfmj; 	// 受否是民警岗亭
	private String xzqh_dm; // 行政区域代码
	private String policeXmStr; // 执勤民警
	
	public String getPoliceXmStr() {
		if(policeXmStr==null)
			policeXmStr = "";
		return policeXmStr;
	}
	public void setPoliceXmStr(String policeXmStr) {
		this.policeXmStr = policeXmStr;
	}
	public String getDm() {
		return dm;
	}
	public void setDm(String dm) {
		this.dm = dm;
	}
	public String getSfgd() {
		return sfgd;
	}
	public void setSfgd(String sfgd) {
		this.sfgd = sfgd;
	}
	public String getGtmc() {
		return gtmc;
	}
	public void setGtmc(String gtmc) {
		this.gtmc = gtmc;
	}
	public String getGtbh() {
		return gtbh;
	}
	public void setGtbh(String gtbh) {
		this.gtbh = gtbh;
	}
	public String getXzqh_mc() {
		return xzqh_mc;
	}
	public void setXzqh_mc(String xzqh_mc) {
		this.xzqh_mc = xzqh_mc;
	}
	public String getJgmc() {
		return jgmc;
	}
	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}
	public String getGtdz() {
		return gtdz;
	}
	public void setGtdz(String gtdz) {
		this.gtdz = gtdz;
	}
	public String getJdbh() {
		return jdbh;
	}
	public void setJdbh(String jdbh) {
		this.jdbh = jdbh;
	}
	public String getLxr() {
		return lxr;
	}
	public void setLxr(String lxr) {
		this.lxr = lxr;
	}
	public String getLxdh() {
		return lxdh;
	}
	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
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
	public String getJgbm() {
		return jgbm;
	}
	public void setJgbm(String jgbm) {
		this.jgbm = jgbm;
	}
	public String getSfmj() {
		return sfmj;
	}
	public void setSfmj(String sfmj) {
		this.sfmj = sfmj;
	}
	public String getXzqh_dm() {
		return xzqh_dm;
	}
	public void setXzqh_dm(String xzqh_dm) {
		this.xzqh_dm = xzqh_dm;
	}
	@Override
	public String getName() {
		return getGtmc();
	}
}
