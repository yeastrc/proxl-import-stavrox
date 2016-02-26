package org.yeastrc.proxl.xml.stavrox;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class StavroxVariableModification {
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString( this );
	}
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public int getMaxModifications() {
		return maxModifications;
	}
	public void setMaxModifications(int maxModifications) {
		this.maxModifications = maxModifications;
	}
	
	private String from;
	private String to;
	private int maxModifications;
	
}
