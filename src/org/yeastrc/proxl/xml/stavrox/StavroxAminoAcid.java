package org.yeastrc.proxl.xml.stavrox;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A representation of an amino acid by StavroX
 * @author mriffle
 *
 */
public class StavroxAminoAcid {
	
	public StavroxAminoAcid( String letterCode, String name, String formula ) {
		this.letterCode = letterCode;
		this.name = name;
		this.formula = formula;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString( this );
	}
	
	public String getLetterCode() {
		return letterCode;
	}
	public void setLetterCode(String letterCode) {
		this.letterCode = letterCode;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	private String letterCode;
	private String name;
	private String formula;
	
}
