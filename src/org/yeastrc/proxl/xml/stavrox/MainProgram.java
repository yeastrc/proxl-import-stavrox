package org.yeastrc.proxl.xml.stavrox;

import java.io.File;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.yeastrc.proxl.xml.stavrox.linker.LinkerUtils;
import org.yeastrc.proxl.xml.stavrox.linker.StavroxCrosslinker;

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
		
		// test print out the all linkers and calculated masses from properties file
		for( StavroxCrosslinker linker : analysis.getAnalysisProperties().getCrosslinkers() ) {
			System.out.println( linker.getName() + " : " + linker.getFormula() + " : calculated mass: " + LinkerUtils.calculateLinkerMass( linker, analysis.getAnalysisProperties() ) );
		}
	}
	
	
	public static void main(String[] args ) throws Exception {

		MainProgram mp = new MainProgram();
		mp.convertData( args[ 0 ] );
		
	}
	
}
