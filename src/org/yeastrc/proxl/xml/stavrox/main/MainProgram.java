package org.yeastrc.proxl.xml.stavrox.main;

import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.IllegalOptionValueException;
import jargs.gnu.CmdLineParser.UnknownOptionException;

import java.io.File;

import org.yeastrc.proxl.xml.stavrox.builder.XMLBuilder;
import org.yeastrc.proxl.xml.stavrox.constants.LinkerConstants;
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
	 * @param linkerName The name of the cross-linker
	 * @param fastaFilename The name of the FASTA file
	 * @param scanFilename The name of the scan filename
	 * @param scanNumberAdjustment The amount to adjust the reported scan numbers by
	 * @param outputFilename The path of the output file
	 * 
	 * @throws Exception If there is a problem
	 */
	private void convertData( String filename, String linkerName, String fastaFilename, String scanFilename, int scanNumberAdjustment, String outputFilename ) throws Exception {
		
		File file = new File( filename );
		StavroxAnalysisLoader loader = new StavroxAnalysisLoader();
		
		StavroxAnalysis analysis = loader.loadStavroxAnalysis( file );
		
		// ensure the linker they entered maps to a linker in the properties file
		String stavroxLinker = LinkerMapper.getStavroxCrosslinkerName( linkerName );
		if( !stavroxLinker.equals( analysis.getAnalysisProperties().getCrosslinker().getName() ) ) {
			String message = "Entered linker: " + linkerName + " does not map to linker in properties: " + analysis.getAnalysisProperties().getCrosslinker().getName();
			throw new Exception( message );
		}
		
		/*
		// test print out the all linkers and calculated masses from properties file
		for( StavroxCrosslinker linker : analysis.getAnalysisProperties().getCrosslinkers() ) {
			System.out.println( linker.getName() + " : " + linker.getFormula() + " : calculated mass: " + LinkerUtils.calculateLinkerMass( linker, analysis.getAnalysisProperties() ) );
		}
		*/
		
		XMLBuilder builder = new XMLBuilder();
		builder.buildAndSaveXML(analysis, linkerName, fastaFilename, scanFilename, scanNumberAdjustment, new File( outputFilename ) );
		
	}
	
	
	public static void main(String[] args ) throws Exception {
		
		if( args.length == 0 ) {
			printHelp();
			System.exit( 0 );
		}
		
		CmdLineParser cmdLineParser = new CmdLineParser();
        
		CmdLineParser.Option resultsFilenameOpt = cmdLineParser.addStringOption( 'r', "results" );	
		CmdLineParser.Option linkerOpt = cmdLineParser.addStringOption( 'l', "linker" );	
		CmdLineParser.Option fastaOpt = cmdLineParser.addStringOption( 'f', "fasta_file" );	
		CmdLineParser.Option scanFileWithPathCommandLineOpt = cmdLineParser.addStringOption( 's', "scan_file" );
		CmdLineParser.Option outputFilenameOpt = cmdLineParser.addStringOption( 'o', "output_file" );
		CmdLineParser.Option scanNumberAdjustmentOpt = cmdLineParser.addIntegerOption( 'a', "scan_adjust" );

        // parse command line options
        try { cmdLineParser.parse(args); }
        catch (IllegalOptionValueException e) {
        	printHelp();
            System.exit( 1 );
        }
        catch (UnknownOptionException e) {
           printHelp();
           System.exit( 1 );
        }
		
        
        String linkerName = (String)cmdLineParser.getOptionValue( linkerOpt );
        String resultsFilename = (String)cmdLineParser.getOptionValue( resultsFilenameOpt );
        String fastaFilename = (String)cmdLineParser.getOptionValue( fastaOpt );
        String scanFilename = (String)cmdLineParser.getOptionValue( scanFileWithPathCommandLineOpt );
        String outputFilename = (String)cmdLineParser.getOptionValue( outputFilenameOpt );

        Integer scanNumberAdjustment = (Integer)cmdLineParser.getOptionValue( scanNumberAdjustmentOpt );
        if( scanNumberAdjustment == null ) scanNumberAdjustment = 0;
		
		MainProgram mp = new MainProgram();
		mp.convertData( resultsFilename, linkerName, fastaFilename, scanFilename, scanNumberAdjustment, outputFilename );
		
		
		
	}
	
	private static void printHelp() {
		System.out.println( "Usage: java -jar stavrox2ProxlXML.jar -r /path/to/results.file -l linker -f FASTA file name [-s scan file name] -o /path/to/output.proxl.xml" );
		System.out.println( "E.g.: java -jar stavrox2ProxlXML.jar -r ./results.zhrs -l dss -f FASTA yeast2016.fa -s mydata.mzML -o ./output.proxl.xml" );
		System.out.println( "      java -jar stavrox2ProxlXML.jar -r ./results.zhrs -l dss -f FASTA yeast2016.fa -o ./output.proxl.xml" );
		System.out.println( "      java -jar stavrox2ProxlXML.jar -r ./results.zhrs -l dss -f FASTA yeast2016.fa -a 1 -s mydata.mzML -o ./output.proxl.xml" );

		System.out.println( "\nOptional parameter: -a <scan number adjustment> -- Adjust the reported scan number by this amount." );
		System.out.println( "\tAs of this writing, StavroX 3 has a bug in the parsing of mzML files that" );
		System.out.println( "\tcauses incorrect scan numbers to be reported. It uses the spectrum index" );
		System.out.println( "\tinstead of the scan number of the spectrum at that index. This causes" );
		System.out.println( "\ta mismatch when the ProXL XML importer attempts to find the referenced" );
		System.out.println( "\tscans. In testing, the true scan number appears to always be 1 higher" );
		System.out.println( "\tthan the reported scan number when using mzML files. \"-a 1\" will adjust" );
		System.out.println( "\tthe scan numbers appropriately." );
		
		System.out.println( "\nValid linkers: " );
		for( String s : LinkerConstants.VALID_LINKERS ) {
			System.out.println( "\t" + s );
		}
		
	}
	
}
