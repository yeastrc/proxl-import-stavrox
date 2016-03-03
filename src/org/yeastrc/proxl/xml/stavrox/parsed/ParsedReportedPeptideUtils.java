package org.yeastrc.proxl.xml.stavrox.parsed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.yeastrc.proxl.xml.stavrox.reader.Result;
import org.yeastrc.proxl.xml.stavrox.reader.StavroxAnalysis;

public class ParsedReportedPeptideUtils {

	/**
	 * Convert the results of the analysis (a map of scans to top-scoring PSMs) into a collection of reported peptides
	 * each with associated PSMs
	 * 
	 * @param analysis
	 * @return
	 * @throws Exception
	 */
	public static Map<String, ParsedReportedPeptide> getParsedReportedPeptideFromResults( StavroxAnalysis analysis ) throws Exception {
		Map<String, ParsedReportedPeptide> peptides = new HashMap<String, ParsedReportedPeptide>();
		
		for( int scanNumber : analysis.getAnalysisResults().keySet() ) {
			for( Result result : analysis.getAnalysisResults().get( scanNumber ) ) {
				
				String reportedPeptideString = result.getReportedPeptideString();
				
				ParsedReportedPeptide reportedPeptide = null;
				if( peptides.containsKey( reportedPeptideString ) ) {
					reportedPeptide = peptides.get( reportedPeptideString );
				} else {
					reportedPeptide = new ParsedReportedPeptide();
					peptides.put( reportedPeptideString, reportedPeptide );
					
					reportedPeptide.setResults( new ArrayList<Result>() );
					reportedPeptide.setReportedPeptideString( reportedPeptideString );
					reportedPeptide.setType( result.getPsmType() );
					
					// associated the parsed peptides with this reported peptide
					reportedPeptide.setPeptides( ParsedPeptideUtils.getParsePeptides( result, analysis.getAnalysisProperties() ) );					
				}
				
				reportedPeptide.getResults().add( result );								
			}
		}
				
		return peptides;		
	}
	
}
