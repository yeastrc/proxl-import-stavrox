package org.yeastrc.proxl.xml.stavrox.reader;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.yeastrc.proxl.xml.stavrox.constants.StavroxConstants;

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
			List<Result> analysisResults = rr.getAnalysisResults( is );
			
			sa.setAnalysisResults( analysisResults );
			
			// load the decoy data
			zipEntry = zipFile.getEntry( StavroxConstants.DECOY_FILENAME );
			if( zipEntry != null ) {
				is = zipFile.getInputStream( zipEntry );
				DecoyHandler handler = new DecoyHandler();
				handler.readDecoys( is );
				
				sa.setDecoyHandler( handler );
			} else {
				throw new Exception( "Decoy file was not found." );
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
