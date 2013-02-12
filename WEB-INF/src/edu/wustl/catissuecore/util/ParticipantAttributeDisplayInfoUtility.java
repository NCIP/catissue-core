package edu.wustl.catissuecore.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;

import edu.wustl.bulkoperator.templateImport.XmlRulesModule;
import edu.wustl.catissuecore.dto.ParticipantAttributeDisplayInfoDTO;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.global.CommonServiceLocator;

public class ParticipantAttributeDisplayInfoUtility
{
	
	public static void initializeParticipantConfigObject() throws Exception
	{
		StringBuffer errorsBuffer = new StringBuffer();
			
		String configFile = CommonServiceLocator.getInstance().getPropDirPath()
				+ File.separatorChar + "Participant_Config.xml";
		File pageConfigFile=new File(configFile);
		if(pageConfigFile.exists())
		{
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			final String rulesFileLocation =CommonServiceLocator.getInstance().getAppHome()+File.separator+"ParticipantConfigXMLTemplateRules.xml";
			File schemaLocation = new File(CommonServiceLocator.getInstance().getPropDirPath()
					+ File.separatorChar + "ParticipantConfig.xsd");
			Schema schema = factory.newSchema(schemaLocation);
			DigesterLoader digesterLoader = DigesterLoader.newLoader(new XmlRulesModule(rulesFileLocation));
			Digester digester = digesterLoader.newDigester();
			digester.setValidating(true);
			digester.setXMLSchema(schema);
			Validator validator = schema.newValidator();
			Source xmlFileForValidation = new StreamSource(pageConfigFile);
			validator.validate(xmlFileForValidation);
			
			ParticipantConfig participantConfig = (ParticipantConfig) digester.parse(new File(configFile));
			//validateXMLData(participantConfig,errorsBuffer);
			
			if("".equals(errorsBuffer.toString()))
			{
				addAttributeToDisplayList(participantConfig.getAttributes());
			}
		}	
	}
	public static void addAttributeToDisplayList(Set<ParticipantAttributeDisplayInfoDTO> participantAttributeDisplayInfoDTOs)
	{
		Variables.attributesTodisplay=new ArrayList<String>(); 
		for (ParticipantAttributeDisplayInfoDTO participantAttributeDisplayInfoDTO : participantAttributeDisplayInfoDTOs) {
			Variables.attributesTodisplay.add(participantAttributeDisplayInfoDTO.getAttributeName());
		}
	}
}
