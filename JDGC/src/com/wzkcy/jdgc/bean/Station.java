package com.wzkcy.jdgc.bean;

public class Station extends Light{
	
	private String dm; 		// ���루ID��
	private String sfgd; 	// �Ƿ�̶�����ͤ���ͣ�
	private String gtmc; 	// ��ͤ����
	private String gtbh; 	// ��ͤ���
	private String xzqh_mc; // ������������
	private String jgmc; 	// ��������
	private String gtdz; 	// ��ͤ��ַ
	private String jdbh; 	// ���Ʊ��
	private String lxr; 	// ��ϵ��
	private String lxdh; 	// ��ϵ�绰
	private String bz; 		// ��ע
	private String gps_jd; 	// ����
	private String gps_wd; 	// ����
	private String jgbm; 	// ��������
	private String sfmj; 	// �ܷ����񾯸�ͤ
	private String xzqh_dm; // �����������
	private String policeXmStr; // ִ����
	
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
