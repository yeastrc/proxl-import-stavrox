package org.yeastrc.proxl.xml.stavrox;

import java.util.ArrayList;
import java.util.Collection;

public class ParsedPeptideUtils {

	/**
	 * Take a stavrox result and return the two parsed peptides from the described crosslink.
	 * 
	 * @param result
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	public static Collection<ParsedPeptide> getParsePeptides( Result result, AnalysisProperties properties ) throws Exception {

		System.out.println( "Parsing: " + result );
		
		Collection<ParsedPeptide> parsedPeptides = new ArrayList<ParsedPeptide>();
		
		parsedPeptides.add( getParsedPeptide( result.getPeptide1(), result.getPosition1String(), properties ) );
		parsedPeptides.add( getParsedPeptide( result.getPeptide2(), result.getPosition2String(), properties ) );		
		
		return parsedPeptides;
	}
	
	private static ParsedPeptide getParsedPeptide( String stavroxPeptide, String stavroxPosition, AnalysisProperties properties ) throws Exception {
		String nakedPeptide = "";
		Collection<ParsedPeptideModification> mods = new ArrayList<ParsedPeptideModification>();
		
		// stavrox peptide all being with either { or [ and end with } or ]. Remove the first and last characters
		stavroxPeptide = stavroxPeptide.substring(1, stavroxPeptide.length() - 1);
		
		
		for (int i = 0; i < stavroxPeptide.length(); i++){
		    String stavroxResidue = String.valueOf( stavroxPeptide.charAt(i) );
		    int peptidePosition = i + 1;

		    // is this a static mod?
		    if( properties.getStaticMods().containsKey( stavroxResidue ) ) {
		    	nakedPeptide += properties.getStaticMods().get( stavroxResidue ).getFrom();		// use the unmodded code for this residue
		    }
		    
		    // is this a variable mod?
		    else if( properties.getVariableMods().containsKey( stavroxResidue ) ) {
		    	nakedPeptide += properties.getVariableMods().get( stavroxResidue ).getFrom();		// use the unmodded code for this residue
		    	
		    	// add this mod to the collection of mods for this peptide
		    	ParsedPeptideModification mod = new ParsedPeptideModification();
		    	mod.setPosition( peptidePosition );
		    	mod.setMass( properties.getVariableMods().get( stavroxResidue ).getMassShift( properties ) );
		    	
		    	mods.add( mod );
		    }
		    
		    // neither a variable or a static mod
		    else {
		    	nakedPeptide += stavroxResidue;
		    }
		    
		}
		
		// handle the position
		stavroxPosition = stavroxPosition.substring( 1 );
		int position = Integer.parseInt( stavroxPosition );
		if( position == 0 ) { position = 1; }
		
				
		ParsedPeptide peptide = new ParsedPeptide();
		peptide.setSequence( nakedPeptide );
		peptide.setMods( mods );
		peptide.setLinkedPosition( position );
		
		return peptide;
	}
	
	
	
}
