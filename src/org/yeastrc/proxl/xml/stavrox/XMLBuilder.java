package org.yeastrc.proxl.xml.stavrox;

import java.io.File;

import org.yeastrc.proxl.xml.stavrox.linker.LinkerMapper;
import org.yeastrc.proxl_import.api.xml_dto.DefaultVisibleAnnotations;
import org.yeastrc.proxl_import.api.xml_dto.DescriptivePsmAnnotationTypes;
import org.yeastrc.proxl_import.api.xml_dto.FilterablePsmAnnotationTypes;
import org.yeastrc.proxl_import.api.xml_dto.ProxlInput;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgram;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgram.PsmAnnotationTypes;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgramInfo;
import org.yeastrc.proxl_import.api.xml_dto.SearchPrograms;

/**
 * Take the results of a StavroX analysis and build the ProXL XML
 * @author mriffle
 *
 */
public class XMLBuilder {

	public void buildAndSaveXML( StavroxAnalysis analysis, String linker, String fastaFilename, File outputFile ) throws Exception {
		
		ProxlInput proxlInputRoot = new ProxlInput();
		proxlInputRoot.setFastaFilename( fastaFilename );
		
		SearchProgramInfo searchProgramInfo = new SearchProgramInfo();
		
		SearchPrograms searchPrograms = new SearchPrograms();
		searchProgramInfo.setSearchPrograms( searchPrograms );
		
		SearchProgram searchProgram = new SearchProgram();
		searchPrograms.getSearchProgram().add( searchProgram );
		
		searchProgram.setName( StavroxConstants.SEARCH_PROGRAM_NAME );
		searchProgram.setVersion( StavroxConstants.SEARCH_PROGRAM_VERSION );
		
		
		
		// Define the annotation types present in StavroX data
		PsmAnnotationTypes psmAnnotationTypes = new PsmAnnotationTypes();
		searchProgram.setPsmAnnotationTypes( psmAnnotationTypes );
		
		FilterablePsmAnnotationTypes filterablePsmAnnotationTypes = new FilterablePsmAnnotationTypes();
		psmAnnotationTypes.setFilterablePsmAnnotationTypes( filterablePsmAnnotationTypes );
		filterablePsmAnnotationTypes.getFilterablePsmAnnotationType().addAll( PSMAnnotationTypes.getFilterablePsmAnnotationTypes() );
		
		DescriptivePsmAnnotationTypes descriptivePsmAnnotationTypes = new DescriptivePsmAnnotationTypes();
		psmAnnotationTypes.setDescriptivePsmAnnotationTypes( descriptivePsmAnnotationTypes );
		descriptivePsmAnnotationTypes.getDescriptivePsmAnnotationType().addAll( PSMAnnotationTypes.getDescriptivePsmAnnotationTypes() );

		
		
		// Define which annotation types are visible by default
		DefaultVisibleAnnotations visibleAnnotations = new DefaultVisibleAnnotations();
		visibleAnnotations.getVisiblePsmAnnotations().getSearchAnnotation().addAll( PSMDefaultVisibleAnnotationTypes.getDefaultVisibleAnnotationTypes() );

		
		
		
		// Define the linker information
		String stavroxLinker = LinkerMapper.getStavroxCrosslinkerName( linker );
		if( !stavroxLinker.equals( analysis.getAnalysisProperties().getCrosslinker().getName() ) ) {
			String message = "Entered linker: " + linker + " does not map to linker in properties: " + analysis.getAnalysisProperties().getCrosslinker().getName();
			throw new Exception( message );
		}
		
		
		
	}
	
}
