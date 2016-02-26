package org.yeastrc.proxl.xml.stavrox;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class StavroxProteaseLine {
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString( this );
	}
	
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getMissedCleavages() {
		return missedCleavages;
	}
	public void setMissedCleavages(String missedCleavages) {
		this.missedCleavages = missedCleavages;
	}
	public String getBlockedBy() {
		return blockedBy;
	}
	public void setBlockedBy(String blockedBy) {
		this.blockedBy = blockedBy;
	}
	
	private String site;
	private String missedCleavages;
	private String blockedBy;
	
}
