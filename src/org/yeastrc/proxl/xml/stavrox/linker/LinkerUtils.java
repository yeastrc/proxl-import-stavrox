package org.yeastrc.proxl.xml.stavrox.linker;

import org.yeastrc.proxl.xml.stavrox.AnalysisProperties;

public class LinkerUtils {

	/**
	 * Calculate the mass of the supplied crosslinker using its atomic formula, using the masses
	 * provided for those atoms in the stavrox properties file.
	 * 
	 * @param linker
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	public double calculateLinkerMass( StavroxCrosslinker linker, AnalysisProperties properties ) throws Exception {
		double mass = 0;
		boolean negative = false;
		
		String formula = properties.getCrosslinker().getFormula();
		if( formula.startsWith( "-" ) ) {
			negative = true;
			formula = formula.substring( 1 );			
		}

		
		
		
		return mass;
	}
	
}
