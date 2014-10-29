/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.templateImport;

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
