package org.yeastrc.proxl.xml.stavrox;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Interact with the zipped StavroX analysis file to load the data
 * from the experiment into a StavroxAnalysis object
 * 
 * @author mriffle
 *
 */
public class StavroxAnalysisLoader {

	public StavroxAnalysis loadStavroxAnalysis( File dataFile ) throws Exception {
		StavroxAnalysis sa = new StavroxAnalysis();
		
		ZipFile zipFile = null;	
		InputStream is = null;
		
		try {
		
			zipFile = new ZipFile( dataFile.getAbsolutePath() );

			
			// load the properties file
			ZipEntry zipEntry = zipFile.getEntry( StavroxConstants.PROPERTIES_FILENAME );

			is = zipFile.getInputStream( zipEntry );
			
			PropertiesReader pr = new PropertiesReader();
			AnalysisProperties ap = pr.getAnalysisProperties( is );
			
			sa.setAnalysisProperties( ap );

			
			// load the PSM data
			zipEntry = zipFile.getEntry( StavroxConstants.PSM_ANNOTATIONS_FILENAME );

			is = zipFile.getInputStream( zipEntry );
			
			ResultsReader rr = new ResultsReader();
			Map<Integer, List<Result>> analysisResults = rr.getAnalysisResults( is );
			
			sa.setAnalysisResults( analysisResults );
			
			System.out.println( "Loaded: " + sa.getAnalysisResults().keySet().size() + " scans." );			


			for( Integer scan : analysisResults.keySet() ) {
				System.out.println( scan );
				for( Result r : analysisResults.get( scan ) ) {
					System.out.println( r );
				}
			}
			

			
		} finally {
			try { is.close(); }
			catch( Throwable t ) { ; }
			
			try { zipFile.close(); }
			catch( Throwable t ) { ; }
		}
		
		

		
		return sa;
		
	}
	
	
}
