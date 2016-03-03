package org.yeastrc.proxl.xml.stavrox.mods;

import org.yeastrc.proxl.xml.stavrox.reader.AnalysisProperties;

public interface IStavroxModification {

	/**
	 * Get the letter code representing this modification in peptide sequences. E.g.,
	 * "m" if "m" now represents an oxidized "M"
	 * 
	 * @return
	 */
	public String getTo();

	/**
	 * Set the "to" parameter
	 * @param to
	 */
	public void setTo( String to );
	
	/**
	 * Get the letter code representing the amino acid that was modified. E.g.,
	 * "M" if "m" now represents an oxidized methionine.
	 * @return
	 */
	public String getFrom();
	
	/**
	 * Set the "from" parameter
	 * @param from
	 */
	public void setFrom( String from );

	/**
	 * Get the mass shift of this modification. E.g., an oxidized
	 * methionine would have a mass shift of 15.99...
	 * 
	 * @return The mass of the "to" - The mass of the "from"
	 */
	public double getMassShift( AnalysisProperties properties ) throws Exception;
	
}
