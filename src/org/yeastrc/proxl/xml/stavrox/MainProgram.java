package org.yeastrc.proxl.xml.stavrox;

import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.IllegalOptionValueException;
import jargs.gnu.CmdLineParser.UnknownOptionException;

import java.io.File;

import org.yeastrc.proxl.xml.stavrox.linker.LinkerConstants;
import org.yeastrc.proxl.xml.stavrox.linker.LinkerMapper;

/**
 * Convert a StavroX data file to a ProXL XML file
 * @author mriffle
 *
 */
public class MainProgram {

	
	private void convertData( String filename, String linkerName, String fastaFilename, String scanFilename, String outputFilename ) throws Exception {
		
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
		builder.buildAndSaveXML(analysis, linkerName, fastaFilename, scanFilename, new File( outputFilename ) );
		
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
		
		MainProgram mp = new MainProgram();
		mp.convertData( resultsFilename, linkerName, fastaFilename, scanFilename, outputFilename );
		
		
		
	}
	
	private static void printHelp() {
		System.out.println( "Usage: java -jar stavrox2ProxlXML.jar -r /path/to/results.file -l linker -f FASTA file name -s /path/to/scan/file.mzml -o /path/to/output.proxl.xml" );
		System.out.println( "E.g.: java -jar stavrox2ProxlXML.jar -r ./results.zhrs -l dss -f FASTA yeast2016.fa -s /data/scans/mydata.mzML -o ./output.proxl.xml" );
		System.out.println( "Valid linkers: " );
		for( String s : LinkerConstants.VALID_LINKERS ) {
			System.out.println( "\t" + s );
		}
		
	}
	
}
