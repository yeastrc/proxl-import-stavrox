package org.yeastrc.proxl.xml.stavrox.reader;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.yeastrc.proxl.xml.stavrox.utils.LinkerUtils;

public class StavroxCrosslinker {

	/**
	 * Get the mass of this crosslinker, using the masses for elements found
	 * in a given analysis properties file.
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	public double getMass( AnalysisProperties properties ) throws Exception {
		return LinkerUtils.calculateLinkerMass( this, properties );
	}

	@Override
	public String toString() {
		return "StavroxCrosslinker{" +
				"name='" + name + '\'' +
				", formula='" + formula + '\'' +
				", crosslinkerEnds=" + crosslinkerEnds +
				'}';
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}

	public List<StavroxCrosslinkerEnd> getCrosslinkerEnds() {
		return crosslinkerEnds;
	}

	public void setCrosslinkerEnds(List<StavroxCrosslinkerEnd> crosslinkerEnds) {
		this.crosslinkerEnds = crosslinkerEnds;
	}

	private String name;
	private String formula;
	private List<StavroxCrosslinkerEnd> crosslinkerEnds;
}
