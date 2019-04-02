package org.yeastrc.proxl.xml.stavrox.main;

import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.IllegalOptionValueException;
import jargs.gnu.CmdLineParser.UnknownOptionException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.yeastrc.proxl.xml.stavrox.builder.XMLBuilder;
import org.yeastrc.proxl.xml.stavrox.constants.ConverterConstants;
import org.yeastrc.proxl.xml.stavrox.constants.StavroxConstants;
import org.yeastrc.proxl.xml.stavrox.reader.StavroxAnalysis;
import org.yeastrc.proxl.xml.stavrox.reader.StavroxAnalysisLoader;
import org.yeastrc.proxl.xml.stavrox.utils.LinkerMapper;

/**
 * Convert a StavroX data file to a ProXL XML file
 * @author mriffle
 *
 */
public class MainProgram {

	/**
	 * Do the data conversion and save the XML file
	 * 
	 * @param filename The path of the results file
	 * @param fastaFilename The name of the FASTA file
	 * @param scanFilename The name of the scan filename
	 * @param scanNumberAdjustment The amount to adjust the reported scan numbers by
	 * @param outputFilename The path of the output file
	 * 
	 * @throws Exception If there is a problem
	 */
	private void convertData( String filename, String fastaFilename, String scanFilename, int scanNumberAdjustment, String outputFilename ) throws Exception {
		
		File file = new File( filename );
		StavroxAnalysisLoader loader = new StavroxAnalysisLoader();
		StavroxAnalysis analysis = loader.loadStavroxAnalysis( file );
		
		XMLBuilder builder = new XMLBuilder();
		builder.buildAndSaveXML(analysis, analysis.getAnalysisProperties().getCrosslinker(), fastaFilename, scanFilename, scanNumberAdjustment, new File( outputFilename ) );
		
	}
	
	
	public static void main(String[] args ) throws Exception {
		
		printStartup();
		
		if( args.length == 0 ) {
			printHelp();
			System.exit( 0 );
		}
		
		CmdLineParser cmdLineParser = new CmdLineParser();
        
		CmdLineParser.Option resultsFilenameOpt = cmdLineParser.addStringOption( 'r', "results" );	
		CmdLineParser.Option fastaOpt = cmdLineParser.addStringOption( 'f', "fasta_file" );
		CmdLineParser.Option scanFileWithPathCommandLineOpt = cmdLineParser.addStringOption( 's', "scan_file" );
		CmdLineParser.Option outputFilenameOpt = cmdLineParser.addStringOption( 'o', "output_file" );
		CmdLineParser.Option scanNumberAdjustmentOpt = cmdLineParser.addIntegerOption( 'a', "scan_adjust" );

        // parse command line options
        try { cmdLineParser.parse(args); }
        catch (IllegalOptionValueException | UnknownOptionException e) {
        	printHelp();
            System.exit( 1 );
        }


		String resultsFilename = (String)cmdLineParser.getOptionValue( resultsFilenameOpt );
        String fastaFilename = (String)cmdLineParser.getOptionValue( fastaOpt );
        String scanFilename = (String)cmdLineParser.getOptionValue( scanFileWithPathCommandLineOpt );
        String outputFilename = (String)cmdLineParser.getOptionValue( outputFilenameOpt );

        Integer scanNumberAdjustment = (Integer)cmdLineParser.getOptionValue( scanNumberAdjustmentOpt );
        if( scanNumberAdjustment == null ) scanNumberAdjustment = 0;
		
		MainProgram mp = new MainProgram();
		mp.convertData( resultsFilename, fastaFilename, scanFilename, scanNumberAdjustment, outputFilename );

	}
	
	public static void printStartup() {
		
		try( BufferedReader br = new BufferedReader( new InputStreamReader( MainProgram.class.getResourceAsStream( "startup.txt" ) ) ) ) {
			
			String line = null;
			while ( ( line = br.readLine() ) != null ) {
				line = line.replace( "#VERSION#", ConverterConstants._CONVERTER_VERSION );
				System.out.println( line );
			}
			
		} catch ( Exception e ) {
			System.out.println( "Error printing help." );
		}
	}
	
	public static void printHelp() {
		
		try( BufferedReader br = new BufferedReader( new InputStreamReader( MainProgram.class.getResourceAsStream( "help.txt" ) ) ) ) {
			
			String line = null;
			while ( ( line = br.readLine() ) != null )
				System.out.println( line );				
			
		} catch ( Exception e ) {
			System.out.println( "Error printing help." );
		}
	}
	
}
