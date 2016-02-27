package org.yeastrc.proxl.xml.stavrox.linker;

import org.yeastrc.proxl.xml.stavrox.AnalysisProperties;
import org.yeastrc.proxl.xml.stavrox.MassUtils;

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
	public static double calculateLinkerMass( StavroxCrosslinker linker, AnalysisProperties properties ) throws Exception {
		boolean negative = false;
		
		String formula = linker.getFormula();
		if( formula.startsWith( "-" ) ) {
			negative = true;
			formula = formula.substring( 1 );			
		}

		double mass = MassUtils.getMassFromFormula( formula, properties );
		if( negative ) { mass = mass * -1; }
		
		return mass;
	}
	
}
