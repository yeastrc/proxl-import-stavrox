StavroX 3 to Proxl XML Converter
=============================

Use this program to convert the results of a StavroX 3 analysis to Proxl XML suitable for import into the proxl web application.

MeroX 2 to Proxl XML Converter
==============================
MeroX has replaced StavroX by its developers, and StavroX is no longer maintained. We have developed a MeroX Proxl XML converter, it is available here: https://github.com/yeastrc/proxl-import-merox

How To Run
-------------
1. Download the [latest release](https://github.com/yeastrc/proxl-import-stavrox/releases).
2. Run the program ``java -jar stavrox2ProxlXML.jar`` with no arguments to see the possible parameters.
3. Run the program, e.g., ``java -jar stavrox2ProxlXML.jar -r ./results.zhrs -l dss -f FASTA yeast2016.fa -a 1 -o ./output.proxl.xml``

In the above example, ``output.proxl.xml`` will be created and be suitable for import into ProXL.

For more information on importing data into Proxl, please see the [Proxl Import Documentation](http://proxl-web-app.readthedocs.io/en/latest/using/upload_data.html).

More Information About Proxl
-----------------------------
For more information about Proxl, visit http://proxl-ms.org/.

Command line documentation
---------------------------

Usage: ``java -jar stavrox2ProxlXML.jar -r /path/to/results.file -f FASTA file full path [-s scan file name] -o /path/to/output.proxl.xml``

E.g.:

 ``java -jar stavrox2ProxlXML.jar -r ./results.zhrs -f /path/to/yeast2016.fa -s mydata.mzML -o ./output.proxl.xml``

 ``java -jar stavrox2ProxlXML.jar -r ./results.zhrs -f /path/to/yeast2016.fa -o ./output.proxl.xml``

 ``java -jar stavrox2ProxlXML.jar -r ./results.zhrs -f FASTA /path/to/yeast2016.fa -a 1 -s mydata.mzML -o ./output.proxl.xml``


Required parameters:

    -r </path/to/results.zhrs> -- The full path to the results file from the
                                  StavroX analysis.
    
    -f </path/to/fasta.fa> -- The full path to the FASTA file used in the
                              StavroX analysis.
    
    -o </path/to/output.xml> -- The full path to the desired output proxl
                                XML file.


Optional parameters:

    -s <scan file name> -- The name of the scan file (e.g. mydata.mzML). Used to
                           annotate PSMs with the name of the scan file in which
                           the scan can be found.

	-a <scan number adjustment> -- Adjust the reported scan number by this amount.

	As of this writing, StavroX 3 has a bug in the parsing of mzML files that
	causes incorrect scan numbers to be reported. It uses the spectrum index
	instead of the scan number of the spectrum at that index. This causes
	a mismatch when the ProXL XML importer attempts to find the referenced
	scans. In testing, the true scan number appears to always be 1 higher
	than the reported scan number when using mzML files. "-a 1" will adjust
	the scan numbers appropriately.
