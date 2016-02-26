package org.yeastrc.proxl.xml.stavrox;

import java.util.List;

public class StavroxCrosslinker {

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
