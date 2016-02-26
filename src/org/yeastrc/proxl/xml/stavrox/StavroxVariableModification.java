package org.yeastrc.proxl.xml.stavrox;

public class StavroxVariableModification {
	
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
