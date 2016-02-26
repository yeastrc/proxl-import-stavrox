package org.yeastrc.proxl.xml.stavrox;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class StavroxStaticModification {
	
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
	
	private String from;
	private String to;
	
}
