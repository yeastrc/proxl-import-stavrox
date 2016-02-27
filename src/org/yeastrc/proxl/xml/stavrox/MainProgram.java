package org.yeastrc.proxl.xml.stavrox;

import java.io.File;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.yeastrc.proxl.xml.stavrox.linker.LinkerMapper;
import org.yeastrc.proxl.xml.stavrox.linker.LinkerUtils;
import org.yeastrc.proxl.xml.stavrox.linker.StavroxCrosslinker;

/**
 * Convert a StavroX data file to a ProXL XML file
 * @author mriffle
 *
 */
public class MainProgram {

	
	private void convertData( String filename, String linkerName ) throws Exception {
		
		File file = new File( filename );
		StavroxAnalysisLoader loader = new StavroxAnalysisLoader();
		
		StavroxAnalysis analysis = loader.loadStavroxAnalysis( file );
		
		// ensure the linker they entered maps to a linker in the properties file
		String stavroxLinker = LinkerMapper.getStavroxCrosslinkerName( linkerName );
		if( !stavroxLinker.equals( analysis.getAnalysisProperties().getCrosslinker().getName() ) ) {
			String message = "Entered linker: " + linkerName + " does not map to linker in properties: " + analysis.getAnalysisProperties().getCrosslinker().getName();
			throw new Exception( message );
		}
		
		// test print out the all linkers and calculated masses from properties file
		for( StavroxCrosslinker linker : analysis.getAnalysisProperties().getCrosslinkers() ) {
			System.out.println( linker.getName() + " : " + linker.getFormula() + " : calculated mass: " + LinkerUtils.calculateLinkerMass( linker, analysis.getAnalysisProperties() ) );
		}
	}
	
	
	public static void main(String[] args ) throws Exception {

		String resultsFilename = args[ 0 ];
		String linkerName = args[ 1 ];
		
		
		MainProgram mp = new MainProgram();
		mp.convertData( resultsFilename, linkerName );
		
		
		
	}
	
}
