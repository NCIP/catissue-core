package edu.wustl.catissuecore.util;

import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;


public class XmlRulesModule extends FromXmlRulesModule{

	private String rulesFile;
	public XmlRulesModule(String rulesFile) {
		this.rulesFile = rulesFile;
	}
	@Override
	protected void loadRules() {
              loadXMLRules(getClass().getClassLoader().getResourceAsStream(rulesFile));
	}

}
