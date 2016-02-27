package org.yeastrc.proxl.xml.stavrox;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yeastrc.proxl.xml.stavrox.linker.StavroxCrosslinker;

/**
 * Read the PSM-level results from Results.csv in the zipped results file
 * @author mriffle
 *
 */
public class ResultsReader {

	/**
	 * Get the results from Results.csv. The returned data structure is a map where the keys are
	 * the scan number and the values are a list of results with the highest score for that scan.
	 * If multiple results have the highest score, they will all be returned.
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, List<Result>> getAnalysisResults( InputStream is ) throws Exception {
		
		Map<Integer, List<Result>> results = new HashMap<Integer, List<Result>>();
		
		BufferedReader br = null;
		try {
			
			br = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
			
			String currentLine;		// the line we're currently parsing
			while( ( currentLine = br.readLine() ) != null ) {
				
				String[] fields = currentLine.split( ";" );
				Result result = new Result();

				result.setScore( Integer.parseInt( fields[ 0 ] ) );
				result.setMoverz( Double.parseDouble( fields[ 1 ] ) );
				result.setCharge( Integer.parseInt( fields[ 2 ] ) );
				result.setObservedMass( Double.parseDouble( fields[ 3 ] ) );
				result.setCandidateMass( Double.parseDouble( fields [ 4 ] ) );
				result.setDeviation( Double.parseDouble( fields [ 5 ] ) );
				
				result.setPeptide1( fields [ 6 ] );
				result.setPeptide2( fields[ 10 ] );
				
				// skip dead-ends (monolinks) and nulls (looplinks) for now
				if( result.getPeptide2().equals( "0" ) || result.getPeptide2().equals( "1" ) ) continue;
				
				result.setScanNumber( Integer.parseInt( fields [ 14 ] ) );
				
				result.setPosition1String( fields[ 20 ] );
				result.setPosition2String( fields[ 21 ] );
				
				String[] xlinkerFields = fields[ 24 ].split( "#" );
				StavroxCrosslinker linker = new StavroxCrosslinker();
				linker.setName( xlinkerFields[ 0 ] );
				linker.setFormula( xlinkerFields[ 1 ] );
				result.setLinker( linker );
				
				/* to ensure consistency about the order of peptides 1 and 2 for a PSM
				 * this is required so that a calculated reported peptide string for a result
				 * always unique identifies the same pair of linked peptides and positions and
				 * because we do not know if stavrox is consistent about order of peptides in
				 * identified peptide pairs
				 * we will ensure peptide1 is alphabetically less than peptide 2 */
				if( result.getPeptide1().compareTo( result.getPeptide2() ) > 0 ) {
					
					// need to swap them
					String tmpPeptide = result.getPeptide1();
					String tmpPosition = result.getPosition1String();
					
					result.setPeptide1( result.getPeptide2() );
					result.setPosition1String( result.getPosition2String() );
					
					result.setPeptide2( tmpPeptide );
					result.setPosition2String( tmpPosition );
				} else if( result.getPeptide1().equals( result.getPeptide2() ) ) {
					
					// same peptide sequences, ensure position strings are alphabetized
					if( result.getPosition1String().compareTo( result.getPosition2String() ) > 0 ) {
						String tmpPosition = result.getPosition1String();
						
						result.setPosition1String( result.getPosition2String() );
						result.setPosition2String( tmpPosition );
					}
				}
				
				if( !results.containsKey( result.getScanNumber() ) ) {
					
					// no results yet for this scan
					results.put( result.getScanNumber(), new ArrayList<Result>() );
					results.get( result.getScanNumber() ).add( result );
				} else {
					if( results.get( result.getScanNumber() ).get( 0 ).getScore() < result.getScore() ) {

						// replace the existing array list with a new one containing only this result
						results.put( result.getScanNumber(), new ArrayList<Result>() );
						results.get( result.getScanNumber() ).add( result );
						
					} else if( results.get( result.getScanNumber() ).get( 0 ).getScore() == result.getScore() ) {
						
						// if the scores are equal, add this to the list
						results.get( result.getScanNumber() ).add( result );
					}
				}				
			}
			
		} finally {
			try { br.close(); }
			catch( Throwable t ) { ; }
		}
		
		
		return results;
		
	}
	
}
