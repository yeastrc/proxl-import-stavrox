package org.yeastrc.proxl.xml.stavrox;

/**
 * Represents a position and a mass modification, to be associated as
 * part of a collection of parsed mods with a parsed peptide
 * @author Valued Customer
 *
 */
public class ParsedPeptideModification {
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public double getMass() {
		return mass;
	}
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	private int position;
	private double mass;
}
