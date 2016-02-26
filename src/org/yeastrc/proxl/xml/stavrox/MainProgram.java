package org.yeastrc.proxl.xml.stavrox;

import java.io.File;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Convert a StavroX data file to a ProXL XML file
 * @author mriffle
 *
 */
public class MainProgram {

	
	private void convertData( String filename ) throws Exception {
		
		File file = new File( filename );
		StavroxAnalysisLoader loader = new StavroxAnalysisLoader();
		
		StavroxAnalysis analysis = loader.loadStavroxAnalysis( file );
		
	}
	
	
	public static void main(String[] args ) throws Exception {

		MainProgram mp = new MainProgram();
		mp.convertData( args[ 0 ] );
		
	}
	
}
