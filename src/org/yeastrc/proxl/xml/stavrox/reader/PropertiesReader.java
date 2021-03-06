package org.yeastrc.proxl.xml.stavrox.reader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import org.yeastrc.proxl.xml.stavrox.mods.StavroxStaticModification;
import org.yeastrc.proxl.xml.stavrox.mods.StavroxVariableModification;

/**
 * Read the properties out of a properties file in the results file of a StavroX analysis
 * @author mriffle
 *
 */
public class PropertiesReader {

	private static final int NONE				 = -1;
	private static final int SETTINGS	 		 = 0;
	private static final int ELEMENTS	 		 = 1;
	private static final int IONTYPES	 		 = 2;
	private static final int AMINOACIDS 		 = 3;
	private static final int CROSSLINKER 		 = 4;
	private static final int PROTEASE			 = 5;
	private static final int VARMODIFICATION	 = 6;
	private static final int STATMODIFICATION	 = 7;	
	private static final int UNKNOWN			 = 8;
	private static final int REPORTERIONS		 = 9;
	
	public AnalysisProperties getAnalysisProperties( InputStream is ) throws Exception {

		AnalysisProperties ap = new AnalysisProperties();
		
		ap.setAnalysisSettings( new HashMap< String, String >() );
		ap.setAminoAcids( new HashMap<String, StavroxAminoAcid>() );
		ap.setElements( new HashMap<String, Double>() );
		ap.setIonTypes( new HashMap<String, String>() );
		ap.setProteaseLines( new ArrayList<StavroxProteaseLine>() );
		ap.setStaticMods( new HashMap<String, StavroxStaticModification>() );
		ap.setVariableMods( new HashMap<String, StavroxVariableModification>() );
		ap.setCrosslinkers( new ArrayList<StavroxCrosslinker>() );
		
		BufferedReader br = null;
		try {
			
			br = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
			
			String currentLine;		// the line we're currently parsing
			int mode = SETTINGS;
			while( ( currentLine = br.readLine() ) != null ) {
				
				if( currentLine.equals( "END" ) ) {
					mode = NONE;
					continue;
				}
				
				
				// find the new mode
				if( mode == NONE ) {
					if( currentLine.equals( "ELEMENTS" ) )
						mode = ELEMENTS;
					else if( currentLine.equals( "IONTYPES" ) )
						mode = IONTYPES;
					else if( currentLine.equals( "AMINOACIDS" ) )
						mode = AMINOACIDS;
					else if( currentLine.equals( "PROTEASE" ) )
						mode = PROTEASE;
					else if( currentLine.equals( "VARMODIFICATION" ) )
						mode = VARMODIFICATION;
					else if( currentLine.equals( "STATMODIFICATION" ) )
						mode = STATMODIFICATION;
					
					// special case, section header includes data
					else if( currentLine.startsWith( "CROSSLINKER=" ) ) {
						mode = CROSSLINKER;
						String fields[] = currentLine.split( "=" );
						ap.setCrosslinkerIndex( Integer.parseInt( fields[ 1 ] ) );						
					}
					
					else if( currentLine.equals( "REPORTERIONS" ) )
						mode = REPORTERIONS;
					
					else {
						
						System.err.println( "INFO: Got unknown header in properties file: " + currentLine );
						mode = UNKNOWN;						
					}
					
					continue;
				}
				
				
				// handle processing the various sections of the properties file
				
				if( mode == UNKNOWN ) {
					continue;
				}
				
				else if( mode == SETTINGS ) {
					
					// for some reason, the settings at the beginning of this file do not have a header line or an END line
					if( currentLine.equals( "ELEMENTS" ) ) {
						mode = ELEMENTS;
						continue;
					}
					
					String fields[] = currentLine.split( "=" );
					ap.getAnalysisSettings().put( fields[ 0 ],  fields[ 1 ] );
				}
				
				else if( mode == ELEMENTS ) {
					String fields[] = currentLine.split( ";" );
					ap.getElements().put( fields[ 0 ], Double.parseDouble( fields[ 1 ] ) );
				}
				
				else if( mode == IONTYPES ) {
					String fields[] = currentLine.split( ";" );
					ap.getIonTypes().put( fields[ 0 ],  fields[ 1 ] );
				}
				
				else if( mode == AMINOACIDS ) {
					String fields[] = currentLine.split( ";" );
					
					StavroxAminoAcid aa = new StavroxAminoAcid( fields[ 1 ], fields[ 0 ], fields[ 2 ] );
					
					ap.getAminoAcids().put( fields[ 1 ], aa );
				}
				
				
				else if( mode == CROSSLINKER ) {
					String fields[] = currentLine.split( ";" );
					
					StavroxCrosslinker xlinker = new StavroxCrosslinker();
					xlinker.setName( fields[ 0 ].toLowerCase() );
					xlinker.setFormula( fields[ 1 ] );

					List<StavroxCrosslinkerEnd> linkerEnds = new ArrayList<>( 2 );
					xlinker.setCrosslinkerEnds( linkerEnds );

					for( int i = 2; i < 4; i++ ) {
						linkerEnds.add( getLinkerEnd( fields[ i ] ) );
					}
					
					ap.getCrosslinkers().add( xlinker );
				}
				
				else if( mode == PROTEASE ) {
					String fields[] = currentLine.split( ";" );

					StavroxProteaseLine spl = new StavroxProteaseLine();
					spl.setSite( fields[ 0 ] );
					spl.setMissedCleavages( fields[ 1 ] );
					spl.setBlockedBy( fields [ 2 ] );
					
					ap.getProteaseLines().add( spl );					
				}
				
				else if( mode == VARMODIFICATION ) {
					String fields[] = currentLine.split( ";" );

					StavroxVariableModification mod = new StavroxVariableModification();
					mod.setFrom( fields[ 0 ] );
					mod.setTo( fields[ 1 ] );
					mod.setMaxModifications( Integer.parseInt( fields [ 2 ] ) );
					
					ap.getVariableMods().put( fields[ 1 ], mod );					
				}
				
				else if( mode == STATMODIFICATION ) {
					String fields[] = currentLine.split( ";" );

					StavroxStaticModification mod = new StavroxStaticModification();
					mod.setFrom( fields [ 0 ] );
					mod.setTo( fields [ 1 ] );
					
					ap.getStaticMods().put( fields[ 1 ], mod );					
				}
				
				else if( mode == REPORTERIONS ) {
					continue;	// nothing to do for this.				
				}
				
			}
			
		} finally {
			try { br.close(); }
			catch( Throwable t ) { ; }
		}
		
		
		return ap;
	}

	private static StavroxCrosslinkerEnd getLinkerEnd( String linkerDefinition ) {

		Collection<String> residues = new HashSet<>();
		boolean linksCTerm = false;
		boolean linksNTerm = false;

		String[] definedResidues = linkerDefinition.split( "" );
		for( String definedResidue : definedResidues ) {
			if( definedResidue.equals( "}" ) ) {
				linksCTerm = true;
			} else if( definedResidue.equals( "{" ) ) {
				linksNTerm = true;
			} else {
				residues.add( definedResidue );
			}
		}

		return new StavroxCrosslinkerEnd( residues, linksNTerm, linksCTerm );
	}
	
}
