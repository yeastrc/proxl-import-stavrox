package org.yeastrc.proxl.xml.stavrox.builder;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.yeastrc.proxl.xml.stavrox.annotations.PSMAnnotationTypes;
import org.yeastrc.proxl.xml.stavrox.annotations.PSMDefaultVisibleAnnotationTypes;
import org.yeastrc.proxl.xml.stavrox.constants.StavroxConstants;
import org.yeastrc.proxl.xml.stavrox.mods.StavroxStaticModification;
import org.yeastrc.proxl.xml.stavrox.parsed.ParsedPeptide;
import org.yeastrc.proxl.xml.stavrox.parsed.ParsedPeptideModification;
import org.yeastrc.proxl.xml.stavrox.parsed.ParsedReportedPeptide;
import org.yeastrc.proxl.xml.stavrox.parsed.ParsedReportedPeptideUtils;
import org.yeastrc.proxl.xml.stavrox.reader.Result;
import org.yeastrc.proxl.xml.stavrox.reader.StavroxAnalysis;
import org.yeastrc.proxl.xml.stavrox.reader.StavroxCrosslinker;
import org.yeastrc.proxl.xml.stavrox.reader.StavroxCrosslinkerEnd;
import org.yeastrc.proxl.xml.stavrox.utils.NumberUtils;
import org.yeastrc.proxl_import.api.xml_dto.*;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgram.PsmAnnotationTypes;
import org.yeastrc.proxl_import.create_import_file_from_java_objects.main.CreateImportFileFromJavaObjectsMain;

/**
 * Take the results of a StavroX analysis and build the ProXL XML
 * @author mriffle
 *
 */
public class XMLBuilder {

	public void buildAndSaveXML(StavroxAnalysis analysis, StavroxCrosslinker stavroxLinker, String fastaFilePath, String scanFilename, int scanNumberAdjustment, File outputFile ) throws Exception {
		
		ProxlInput proxlInputRoot = new ProxlInput();
		proxlInputRoot.setFastaFilename( ( new File( fastaFilePath ) ).getName() );
		
		SearchProgramInfo searchProgramInfo = new SearchProgramInfo();
		proxlInputRoot.setSearchProgramInfo( searchProgramInfo );
		
		SearchPrograms searchPrograms = new SearchPrograms();
		searchProgramInfo.setSearchPrograms( searchPrograms );
		
		SearchProgram searchProgram = new SearchProgram();
		searchPrograms.getSearchProgram().add( searchProgram );
		
		searchProgram.setName( StavroxConstants.SEARCH_PROGRAM_NAME );
		searchProgram.setVersion( StavroxConstants.SEARCH_PROGRAM_VERSION );
		searchProgram.setDisplayName( StavroxConstants.SEARCH_PROGRAM_NAME );
		
		
		//
		// Define the annotation types present in StavroX data
		//
		PsmAnnotationTypes psmAnnotationTypes = new PsmAnnotationTypes();
		searchProgram.setPsmAnnotationTypes( psmAnnotationTypes );
		
		FilterablePsmAnnotationTypes filterablePsmAnnotationTypes = new FilterablePsmAnnotationTypes();
		psmAnnotationTypes.setFilterablePsmAnnotationTypes( filterablePsmAnnotationTypes );
		filterablePsmAnnotationTypes.getFilterablePsmAnnotationType().addAll( PSMAnnotationTypes.getFilterablePsmAnnotationTypes() );
		
		DescriptivePsmAnnotationTypes descriptivePsmAnnotationTypes = new DescriptivePsmAnnotationTypes();
		psmAnnotationTypes.setDescriptivePsmAnnotationTypes( descriptivePsmAnnotationTypes );
		descriptivePsmAnnotationTypes.getDescriptivePsmAnnotationType().addAll( PSMAnnotationTypes.getDescriptivePsmAnnotationTypes() );
		
		
		//
		// Define which annotation types are visible by default
		//
		DefaultVisibleAnnotations xmlDefaultVisibleAnnotations = new DefaultVisibleAnnotations();
		searchProgramInfo.setDefaultVisibleAnnotations( xmlDefaultVisibleAnnotations );
		
		VisiblePsmAnnotations xmlVisiblePsmAnnotations = new VisiblePsmAnnotations();
		xmlDefaultVisibleAnnotations.setVisiblePsmAnnotations( xmlVisiblePsmAnnotations );

		xmlVisiblePsmAnnotations.getSearchAnnotation().addAll( PSMDefaultVisibleAnnotationTypes.getDefaultVisibleAnnotationTypes() );

		
		
		//
		// Define the linker information
		//
		Linkers linkers = new Linkers();
		proxlInputRoot.setLinkers( linkers );

		Linker linker = new Linker();
		linkers.getLinker().add( linker );
		
		linker.setName( stavroxLinker.getName() );
		
		CrosslinkMasses masses = new CrosslinkMasses();
		linker.setCrosslinkMasses( masses );
		
		CrosslinkMass xlinkMass = new CrosslinkMass();
		xlinkMass.setChemicalFormula( stavroxLinker.getFormula() );
		linker.getCrosslinkMasses().getCrosslinkMass().add( xlinkMass );

		// set the mass for this crosslinker to the calculated mass for the crosslinker, as defined in the properties file
		xlinkMass.setMass( NumberUtils.getRoundedBigDecimal( analysis.getAnalysisProperties().getCrosslinker().getMass( analysis.getAnalysisProperties() ) ) );

		//
		// Add in the linkable/reactable linker ends as defined in the stavrox config file
		//
		LinkedEnds xLinkedEnds = new LinkedEnds();
		linker.setLinkedEnds( xLinkedEnds );

		for(StavroxCrosslinkerEnd stavroxLinkerEnd : stavroxLinker.getCrosslinkerEnds() ) {

			LinkedEnd xLinkedEnd = new LinkedEnd();
			xLinkedEnds.getLinkedEnd().add( xLinkedEnd );

			Residues xResidues = new Residues();
			xResidues.getResidue().addAll( stavroxLinkerEnd.getResidues() );

			xLinkedEnd.setResidues( xResidues );

			ProteinTermini xProteinTermini = null;

			if( stavroxLinkerEnd.isBindsCTerminus() || stavroxLinkerEnd.isBindsNTerminus() ) {

				if( xProteinTermini == null ) {
					xProteinTermini = new ProteinTermini();
					xLinkedEnd.setProteinTermini( xProteinTermini );
				}

				if( stavroxLinkerEnd.isBindsCTerminus() ) {
					ProteinTerminus xProteinTerminus = new ProteinTerminus();
					xProteinTermini.getProteinTerminus().add( xProteinTerminus );

					xProteinTerminus.setTerminusEnd( ProteinTerminusDesignation.C );
				}

				if( stavroxLinkerEnd.isBindsNTerminus() ) {
					ProteinTerminus xProteinTerminus = new ProteinTerminus();
					xProteinTermini.getProteinTerminus().add( xProteinTerminus );

					xProteinTerminus.setTerminusEnd( ProteinTerminusDesignation.N );
					xProteinTerminus.setDistanceFromTerminus( BigInteger.ZERO );
				}
			}
		}
		
		//
		// Define the static mods
		//
		
		if( analysis.getAnalysisProperties().getStaticMods() != null &&
				analysis.getAnalysisProperties().getStaticMods().size() > 0 ) {
			
			StaticModifications smods = new StaticModifications();
			proxlInputRoot.setStaticModifications( smods );
			
			for( String smodsTo : analysis.getAnalysisProperties().getStaticMods().keySet() ) {
				
				StavroxStaticModification stavroxSmod = analysis.getAnalysisProperties().getStaticMods().get( smodsTo );
				StaticModification xmlSmod = new StaticModification();
				
				xmlSmod.setAminoAcid( stavroxSmod.getFrom() );
				xmlSmod.setMassChange( NumberUtils.getRoundedBigDecimal( stavroxSmod.getMassShift( analysis.getAnalysisProperties() ) ) );			
				
				smods.getStaticModification().add( xmlSmod );
			}	
		}
		
		//
		// Add decoy labels (optional)
		//
		DecoyLabels xmlDecoyLabels = new DecoyLabels();
		proxlInputRoot.setDecoyLabels( xmlDecoyLabels );
		
		
		Collection<String> decoyLabels = new HashSet<>();
		decoyLabels.add( "random_seq" );
		decoyLabels.add( "random" );
		
		
		for( String decoyLabel : decoyLabels ) {
			DecoyLabel xmlDecoyLabel = new DecoyLabel();
			xmlDecoyLabels.getDecoyLabel().add( xmlDecoyLabel );
			
			xmlDecoyLabel.setPrefix( decoyLabel );
		}

		//
		// Define the peptide and PSM data
		//
		ReportedPeptides reportedPeptides = new ReportedPeptides();
		proxlInputRoot.setReportedPeptides( reportedPeptides );
		
		Map<String, ParsedReportedPeptide> reportedPeptidesAndResults = ParsedReportedPeptideUtils.getParsedReportedPeptideFromResults( analysis );

		for( String reportedPeptideString : reportedPeptidesAndResults.keySet() ) {
			
			ReportedPeptide xmlReportedPeptide = new ReportedPeptide();
			reportedPeptides.getReportedPeptide().add( xmlReportedPeptide );
			
			xmlReportedPeptide.setReportedPeptideString( reportedPeptideString );
			
			if( reportedPeptidesAndResults.get( reportedPeptideString ).getType() == StavroxConstants.PSM_TYPE_CROSSLINK )
				xmlReportedPeptide.setType( LinkType.CROSSLINK );
			else if( reportedPeptidesAndResults.get( reportedPeptideString ).getType() == StavroxConstants.PSM_TYPE_LOOPLINK )
				xmlReportedPeptide.setType( LinkType.LOOPLINK );
			else
				xmlReportedPeptide.setType( LinkType.UNLINKED );	// monolinked peptide with no cross- or loop-link are considered unlinked (monolinks are considered mods)
			
			Peptides xmlPeptides = new Peptides();
			xmlReportedPeptide.setPeptides( xmlPeptides );
			
			// add in the parsed peptides
			for( ParsedPeptide peptide : reportedPeptidesAndResults.get( reportedPeptideString ).getPeptides() ) {
				Peptide xmlPeptide = new Peptide();
				xmlPeptides.getPeptide().add( xmlPeptide );
				
				xmlPeptide.setSequence( peptide.getSequence() );
				
				if( peptide.getMods() != null && peptide.getMods().size() > 0 ) {
					
					Modifications xmlModifications = new Modifications();
					xmlPeptide.setModifications( xmlModifications );
					
					for( ParsedPeptideModification mod : peptide.getMods() ) {
						Modification xmlModification = new Modification();
						xmlModifications.getModification().add( xmlModification );
						
						xmlModification.setMass( NumberUtils.getRoundedBigDecimal( mod.getMass() ) );
						xmlModification.setPosition( new BigInteger( String.valueOf( mod.getPosition() ) ) );
						xmlModification.setIsMonolink( mod.isMonolink() );
					}
				}
				
				// add in the linked position in this peptide
				if( reportedPeptidesAndResults.get( reportedPeptideString ).getType() != StavroxConstants.PSM_TYPE_MONOLINK ) {	// monolinks have no linked positions, they're mods
					LinkedPositions xmlLinkedPositions = new LinkedPositions();
					xmlPeptide.setLinkedPositions( xmlLinkedPositions );
					
					LinkedPosition xmlLinkedPosition = new LinkedPosition();
					xmlLinkedPositions.getLinkedPosition().add( xmlLinkedPosition );
					xmlLinkedPosition.setPosition( new BigInteger( String.valueOf( peptide.getLinkedPosition1() ) ) );

					// looplinked PSMs are linked in two places in the same peptide
					if( reportedPeptidesAndResults.get( reportedPeptideString ).getType() == StavroxConstants.PSM_TYPE_LOOPLINK ) {
						xmlLinkedPosition = new LinkedPosition();
						xmlLinkedPositions.getLinkedPosition().add( xmlLinkedPosition );
						xmlLinkedPosition.setPosition( new BigInteger( String.valueOf( peptide.getLinkedPosition2() ) ) );
					}
				}
			}
			
			// add in the PSMs and annotations
			Psms xmlPsms = new Psms();
			xmlReportedPeptide.setPsms( xmlPsms );
			
			// iterate over all PSMs
			for( Result result : reportedPeptidesAndResults.get( reportedPeptideString ).getResults() ) {
				Psm xmlPsm = new Psm();
				xmlPsms.getPsm().add( xmlPsm );
				
				if( scanFilename != null && scanFilename != "" )
					xmlPsm.setScanFileName( scanFilename );
				
				xmlPsm.setScanNumber( new BigInteger( String.valueOf( result.getScanNumber() + scanNumberAdjustment ) ) );
				xmlPsm.setPrecursorCharge( new BigInteger( String.valueOf( result.getCharge() ) ) );
				
				if( result.getPsmType() != StavroxConstants.PSM_TYPE_MONOLINK )
					xmlPsm.setLinkerMass( NumberUtils.getRoundedBigDecimal( result.getLinker().getMass( analysis.getAnalysisProperties() ) ) );
				
				// add in the filterable PSM annotations (e.g., score)
				FilterablePsmAnnotations xmlFilterablePsmAnnotations = new FilterablePsmAnnotations();
				xmlPsm.setFilterablePsmAnnotations( xmlFilterablePsmAnnotations );
				
				// handle score
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );
					
					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.ANNOTATION_TYPE_SCORE );
					xmlFilterablePsmAnnotation.setSearchProgram( StavroxConstants.SEARCH_PROGRAM_NAME );
					xmlFilterablePsmAnnotation.setValue( new BigDecimal( result.getScore() ) );				
				}

				
				// handle rank
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );
					
					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.ANNOTATION_TYPE_RANK );
					xmlFilterablePsmAnnotation.setSearchProgram( StavroxConstants.SEARCH_PROGRAM_NAME );
					xmlFilterablePsmAnnotation.setValue( new BigDecimal( result.getRank() ) );				
				}
				
				// handle FDR
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );
					
					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.ANNOTATION_TYPE_FDR );
					xmlFilterablePsmAnnotation.setSearchProgram( StavroxConstants.SEARCH_PROGRAM_NAME );
					xmlFilterablePsmAnnotation.setValue( NumberUtils.getRoundedBigDecimal( analysis.getDecoyHandler().getFDR( result.getScore() ) ) );				
				}
				
				
				// add in the non-filterable descriptive annotations (e.g., calculated mass)
				DescriptivePsmAnnotations xmlDescriptivePsmAnnotations = new DescriptivePsmAnnotations();
				xmlPsm.setDescriptivePsmAnnotations( xmlDescriptivePsmAnnotations );

				{
					// handle m/z
					DescriptivePsmAnnotation xmlDescriptivePsmAnnotation = new DescriptivePsmAnnotation();
					xmlDescriptivePsmAnnotations.getDescriptivePsmAnnotation().add( xmlDescriptivePsmAnnotation );
					
					xmlDescriptivePsmAnnotation.setAnnotationName( PSMAnnotationTypes.ANNOTATION_TYPE_MOVERZ );
					xmlDescriptivePsmAnnotation.setSearchProgram( StavroxConstants.SEARCH_PROGRAM_NAME );
					
					// try to limit this value to the chosen number of decimal places
					try {
						xmlDescriptivePsmAnnotation.setValue( NumberUtils.getRoundedBigDecimal( Double.valueOf( result.getMoverz() ) ).toString() );
					} catch( Exception e ) {
						xmlDescriptivePsmAnnotation.setValue( String.valueOf( result.getMoverz() ) );
					}
				}
				
				{
					// handle observed mass
					DescriptivePsmAnnotation xmlDescriptivePsmAnnotation = new DescriptivePsmAnnotation();
					xmlDescriptivePsmAnnotations.getDescriptivePsmAnnotation().add( xmlDescriptivePsmAnnotation );
					
					xmlDescriptivePsmAnnotation.setAnnotationName( PSMAnnotationTypes.ANNOTATION_TYPE_OBSERVED_MASS );
					xmlDescriptivePsmAnnotation.setSearchProgram( StavroxConstants.SEARCH_PROGRAM_NAME );
					
					// try to limit this value to the chosen number of decimal places
					try {
						xmlDescriptivePsmAnnotation.setValue( NumberUtils.getRoundedBigDecimal( Double.valueOf( result.getObservedMass() ) ).toString() );
					} catch( Exception e ) {
						xmlDescriptivePsmAnnotation.setValue( String.valueOf( result.getObservedMass() ) );
					}
					
				}
				
				{
					// handle candidate mass
					DescriptivePsmAnnotation xmlDescriptivePsmAnnotation = new DescriptivePsmAnnotation();
					xmlDescriptivePsmAnnotations.getDescriptivePsmAnnotation().add( xmlDescriptivePsmAnnotation );
					
					xmlDescriptivePsmAnnotation.setAnnotationName( PSMAnnotationTypes.ANNOTATION_TYPE_CANDIDATE_MASS );
					xmlDescriptivePsmAnnotation.setSearchProgram( StavroxConstants.SEARCH_PROGRAM_NAME );
					
					// try to limit this value to the chosen number of decimal places
					try {
						xmlDescriptivePsmAnnotation.setValue( NumberUtils.getRoundedBigDecimal( Double.valueOf( result.getCandidateMass() ) ).toString() );
					} catch( Exception e ) {
						xmlDescriptivePsmAnnotation.setValue( String.valueOf( result.getCandidateMass() ) );
					}
									}
				
				{
					// handle mass deviation
					DescriptivePsmAnnotation xmlDescriptivePsmAnnotation = new DescriptivePsmAnnotation();
					xmlDescriptivePsmAnnotations.getDescriptivePsmAnnotation().add( xmlDescriptivePsmAnnotation );
					
					xmlDescriptivePsmAnnotation.setAnnotationName( PSMAnnotationTypes.ANNOTATION_TYPE_DEVIATION );
					xmlDescriptivePsmAnnotation.setSearchProgram( StavroxConstants.SEARCH_PROGRAM_NAME );
					
					// try to limit this value to the chosen number of decimal places
					try {
						xmlDescriptivePsmAnnotation.setValue( NumberUtils.getRoundedBigDecimal( Double.valueOf( result.getDeviation() ) ).toString() );
					} catch( Exception e ) {
						xmlDescriptivePsmAnnotation.setValue( String.valueOf( result.getDeviation() ) );
					}
					
				}

				{
					// handle scan number
					DescriptivePsmAnnotation xmlDescriptivePsmAnnotation = new DescriptivePsmAnnotation();
					xmlDescriptivePsmAnnotations.getDescriptivePsmAnnotation().add( xmlDescriptivePsmAnnotation );
					
					xmlDescriptivePsmAnnotation.setAnnotationName( PSMAnnotationTypes.ANNOTATION_TYPE_SCAN_NUMBER );
					xmlDescriptivePsmAnnotation.setSearchProgram( StavroxConstants.SEARCH_PROGRAM_NAME );
					
					// try to limit this value to the chosen number of decimal places
					xmlDescriptivePsmAnnotation.setValue( String.valueOf( result.getScanNumber() + scanNumberAdjustment ) );
				}
				
				
			}
			
		}
		
		// add in matched proteins
		MatchedProteinsBuilder.getInstance().buildMatchedProteins( proxlInputRoot, new File( fastaFilePath ), decoyLabels );
		
		
		// add in config file
		ConfigurationFiles xmlConfigurationFiles = new ConfigurationFiles();
		proxlInputRoot.setConfigurationFiles( xmlConfigurationFiles );
		
		ConfigurationFile xmlConfigurationFile = new ConfigurationFile();
		xmlConfigurationFiles.getConfigurationFile().add( xmlConfigurationFile );
		
		xmlConfigurationFile.setSearchProgram( StavroxConstants.SEARCH_PROGRAM_NAME );
		xmlConfigurationFile.setFileName( StavroxConstants.PROPERTIES_FILENAME );
		xmlConfigurationFile.setFileContent( analysis.getPropertiesFileContents() );
		
		
		CreateImportFileFromJavaObjectsMain.getInstance().createImportFileFromJavaObjectsMain(outputFile, proxlInputRoot);
		
	}
	
}
