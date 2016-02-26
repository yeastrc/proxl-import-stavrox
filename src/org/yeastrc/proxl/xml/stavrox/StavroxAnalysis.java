package org.yeastrc.proxl.xml.stavrox;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The data associated with a stavrox analysis, namely the
 * properties file and the PSMs and associated annotations
 * 
 * @author mriffle
 *
 */
public class StavroxAnalysis {

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString( this );
	}
	
	public AnalysisProperties getAnalysisProperties() {
		return analysisProperties;
	}

	public void setAnalysisProperties(AnalysisProperties analysisProperties) {
		this.analysisProperties = analysisProperties;
	}

	
	
	public Map<Integer, List<Result>> getAnalysisResults() {
		return analysisResults;
	}

	/**
	 * The returned data structure is a map where the keys are
	 * the scan number and the values are a list of results with the highest score for that scan.
	 * If multiple results have the highest score, they will all be returned.
	 * @param analysisResults
	 */
	public void setAnalysisResults(Map<Integer, List<Result>> analysisResults) {
		this.analysisResults = analysisResults;
	}



	private AnalysisProperties analysisProperties;
	private Map<Integer, List<Result>> analysisResults;
}
