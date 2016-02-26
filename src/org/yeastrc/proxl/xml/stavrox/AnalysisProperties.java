package org.yeastrc.proxl.xml.stavrox;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The properties associated with a stavrox analysis, as parsed from the
 * properties.ssf file in the stavrox results zip file.
 * 
 * @author mriffle
 *
 */
public class AnalysisProperties {
	
	public StavroxCrosslinker getCrosslinker() {
		return this.crosslinkers.get( this.getCrosslinkerIndex() );
	}
	
	public Map<String, String> getAnalysisSettings() {
		return analysisSettings;
	}
	public void setAnalysisSettings(Map<String, String> analysisSettings) {
		this.analysisSettings = analysisSettings;
	}
	public Map<String, Double> getElements() {
		return elements;
	}
	public void setElements(Map<String, Double> elements) {
		this.elements = elements;
	}
	public Map<String, String> getIonTypes() {
		return ionTypes;
	}
	public void setIonTypes(Map<String, String> ionTypes) {
		this.ionTypes = ionTypes;
	}
	public Map<String, StavroxAminoAcid> getAminoAcids() {
		return aminoAcids;
	}
	public void setAminoAcids(Map<String, StavroxAminoAcid> aminoAcids) {
		this.aminoAcids = aminoAcids;
	}
	public Collection<StavroxProteaseLine> getProteaseLines() {
		return proteaseLines;
	}
	public void setProteaseLines(Collection<StavroxProteaseLine> proteaseLines) {
		this.proteaseLines = proteaseLines;
	}
	public Map<String, StavroxVariableModification> getVariableMods() {
		return variableMods;
	}
	public void setVariableMods(
			Map<String, StavroxVariableModification> variableMods) {
		this.variableMods = variableMods;
	}
	public Map<String, StavroxStaticModification> getStaticMods() {
		return staticMods;
	}
	public void setStaticMods(Map<String, StavroxStaticModification> staticMods) {
		this.staticMods = staticMods;
	}
	public int getCrosslinkerIndex() {
		return crosslinkerIndex;
	}

	public void setCrosslinkerIndex(int crosslinkerIndex) {
		this.crosslinkerIndex = crosslinkerIndex;
	}

	public List<StavroxCrosslinker> getCrosslinkers() {
		return crosslinkers;
	}

	public void setCrosslinkers(List<StavroxCrosslinker> crosslinkers) {
		this.crosslinkers = crosslinkers;
	}


	private Map<String, String> analysisSettings;					// keyed by the setting name
	private Map<String, Double> elements;							// keyed by the letter code of the element
	private Map<String, String> ionTypes;							// keyed by the first field of the ion type line
	private Map<String, StavroxAminoAcid> aminoAcids;				// keyed by the letter code of the amino acid
	private int crosslinkerIndex;
	private List<StavroxCrosslinker> crosslinkers;
	private Collection<StavroxProteaseLine> proteaseLines;
	private Map<String, StavroxVariableModification> variableMods;	// keyed by the "to" letter for the modification
	private Map<String, StavroxStaticModification> staticMods;		// keyed by the "to" letter for the modification
	
}
