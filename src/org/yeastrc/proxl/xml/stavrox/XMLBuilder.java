package org.yeastrc.proxl.xml.stavrox;

import java.io.File;
import java.math.BigDecimal;

import org.yeastrc.proxl.xml.stavrox.linker.LinkerMapper;
import org.yeastrc.proxl.xml.stavrox.mods.StavroxStaticModification;
import org.yeastrc.proxl_import.api.xml_dto.CrosslinkMass;
import org.yeastrc.proxl_import.api.xml_dto.CrosslinkMasses;
import org.yeastrc.proxl_import.api.xml_dto.DefaultVisibleAnnotations;
import org.yeastrc.proxl_import.api.xml_dto.DescriptivePsmAnnotationTypes;
import org.yeastrc.proxl_import.api.xml_dto.FilterablePsmAnnotationTypes;
import org.yeastrc.proxl_import.api.xml_dto.Linker;
import org.yeastrc.proxl_import.api.xml_dto.Linkers;
import org.yeastrc.proxl_import.api.xml_dto.ProxlInput;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgram;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgram.PsmAnnotationTypes;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgramInfo;
import org.yeastrc.proxl_import.api.xml_dto.SearchPrograms;
import org.yeastrc.proxl_import.api.xml_dto.StaticModification;
import org.yeastrc.proxl_import.api.xml_dto.StaticModifications;

/**
 * Take the results of a StavroX analysis and build the ProXL XML
 * @author mriffle
 *
 */
public class XMLBuilder {

	public void buildAndSaveXML( StavroxAnalysis analysis, String linkerName, String fastaFilename, File outputFile ) throws Exception {
		
		ProxlInput proxlInputRoot = new ProxlInput();
		proxlInputRoot.setFastaFilename( fastaFilename );
		
		SearchProgramInfo searchProgramInfo = new SearchProgramInfo();
		proxlInputRoot.setSearchProgramInfo( searchProgramInfo );
		
		SearchPrograms searchPrograms = new SearchPrograms();
		searchProgramInfo.setSearchPrograms( searchPrograms );
		
		SearchProgram searchProgram = new SearchProgram();
		searchPrograms.getSearchProgram().add( searchProgram );
		
		searchProgram.setName( StavroxConstants.SEARCH_PROGRAM_NAME );
		searchProgram.setVersion( StavroxConstants.SEARCH_PROGRAM_VERSION );
		
		
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
		DefaultVisibleAnnotations visibleAnnotations = new DefaultVisibleAnnotations();
		visibleAnnotations.getVisiblePsmAnnotations().getSearchAnnotation().addAll( PSMDefaultVisibleAnnotationTypes.getDefaultVisibleAnnotationTypes() );

		
		
		//
		// Define the linker information
		//
		Linkers linkers = new Linkers();
		proxlInputRoot.setLinkers( linkers );

		Linker linker = new Linker();
		linkers.getLinker().add( linker );
		
		linker.setName( linkerName );
		
		CrosslinkMasses masses = new CrosslinkMasses();
		linker.setCrosslinkMasses( masses );
		
		CrosslinkMass xlinkMass = new CrosslinkMass();
		linker.getCrosslinkMasses().getCrosslinkMass().add( xlinkMass );

		// set the mass for this crosslinker to the calculated mass for the crosslinker, as defined in the properties file
		xlinkMass.setMass( new BigDecimal( analysis.getAnalysisProperties().getCrosslinker().getMass( analysis.getAnalysisProperties() ) ) );

		
		
		//
		// Define the static mods
		//
		StaticModifications smods = new StaticModifications();
		proxlInputRoot.setStaticModifications( smods );
		
		for( String smodsTo : analysis.getAnalysisProperties().getStaticMods().keySet() ) {
			
			StavroxStaticModification stavroxSmod = analysis.getAnalysisProperties().getStaticMods().get( smodsTo );
			StaticModification xmlSmod = new StaticModification();
			
			xmlSmod.setAminoAcid( stavroxSmod.getFrom() );
			xmlSmod.setMassChange( new BigDecimal( stavroxSmod.getMassShift( analysis.getAnalysisProperties() ) ) );			
			
			smods.getStaticModification().add( xmlSmod );
		}

		
		
		//
		// Define the peptide and PSM data
		//
		
		
		
		
		
		
	}
	
}
