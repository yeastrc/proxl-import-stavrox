package org.yeastrc.proxl.xml.stavrox.utils;

import java.util.HashMap;
import java.util.Map;

public class LinkerMapper {

	/**
	 * Get the name of the stavrox crosslinker (found in the properties file) for the
	 * proxl-based crosslinker abbreviation.
	 * 
	 * @param proxlAbbr
	 * @return
	 * @throws Exception
	 */
	public static String getStavroxCrosslinkerName( String proxlAbbr ) throws Exception {

		// map of proxl linker names to stavrox linker names
		Map<String, String> linkerMap = new HashMap<String, String>();

		linkerMap.put( "dss", "DSS/BS3" );
		linkerMap.put( "bs3", "DSS/BS3" );
		linkerMap.put( "edc", "EDC" );
		linkerMap.put( "bs2", "BS2G" );		

		
		if( linkerMap.containsKey( proxlAbbr ) )
			return linkerMap.get( proxlAbbr );
		
		throw new Exception( "Unsupported linker name: " + proxlAbbr );
		
	}
}
