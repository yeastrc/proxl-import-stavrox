package org.yeastrc.proxl.xml.stavrox;

import java.util.Collection;

/**
 * A peptide parsed from a stavrox representation of a peptide.
 * It contains the unmodified peptide sequence, and a collection 
 * of associated variable modifications
 * @author Valued Customer
 *
 */
public class ParsedPeptide {
	
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public Collection<ParsedPeptideModification> getMods() {
		return mods;
	}
	public void setMods(Collection<ParsedPeptideModification> mods) {
		this.mods = mods;
	}
	public Integer getLinkedPosition() {
		return linkedPosition;
	}
	public void setLinkedPosition(Integer linkedPosition) {
		this.linkedPosition = linkedPosition;
	}



	private String sequence;
	private Collection<ParsedPeptideModification> mods;
	private Integer linkedPosition;
	
}
