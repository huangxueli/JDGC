package com.wzkcy.jdgc.bean;

import java.util.List;
/** ËÑË÷½á¹û **/
public class SearchResult {
	
	protected List<Policeman> police;
	protected List<Station> gt;
	
	public List<Policeman> getPolice() {
		return police;
	}
	public void setPolice(List<Policeman> police) {
		this.police = police;
	}
	public List<Station> getGt() {
		return gt;
	}
	public void setGt(List<Station> gt) {
		this.gt = gt;
	}
	
}
