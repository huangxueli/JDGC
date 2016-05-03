package com.wzkcy.jdgc.bean;
/**
 * ��վ��Ϣ
 */
public class Cell {
	// SIM���Դ�����
	private int sim_mcc; // �ƶ����Ҵ��루�й���Ϊ460��
	private int sim_mnc; // �ƶ�������루�й��ƶ�Ϊ00���й���ͨΪ01����
	private int sim_cid; // λ��������
	private int sim_lac; // ��վ��� 16λ�����ݣ���Χ��0��65535��
	// ��վ��
	private String lac; 
	private String cid; 
	private String gps_jd;
	private String gps_wd;
	private String jzdz; // ������ַ
	private String jzmc; // ��������
	
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
