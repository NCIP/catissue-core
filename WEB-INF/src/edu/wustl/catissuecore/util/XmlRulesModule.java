package edu.wustl.catissuecore.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;


public class XmlRulesModule extends FromXmlRulesModule{

	private String rulesFile;
	public XmlRulesModule(String rulesFile) {
		this.rulesFile = rulesFile;
	}
	@Override
	protected void loadRules() {
		  try {
              InputStream inputStream = new FileInputStream(rulesFile);
              loadXMLRules(inputStream);
          }catch (FileNotFoundException e) {
        	  throw new RuntimeException("Could not find "+rulesFile);
			}
		
	}

}
