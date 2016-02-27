package org.yeastrc.proxl.xml.stavrox.linker;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class StavroxCrosslinker {

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString( this );
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
	public List<String> getBindingRules() {
		return bindingRules;
	}
	public void setBindingRules(List<String> bindingRules) {
		this.bindingRules = bindingRules;
	}

	private String name;
	private String formula;
	private List<String> bindingRules;	
}
