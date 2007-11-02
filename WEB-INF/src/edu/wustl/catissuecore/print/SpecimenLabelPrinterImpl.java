package edu.wustl.catissuecore.print;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.domain.AbstractDomainObject;
import gov.nih.nci.security.authorization.domainobjects.User;
/**
 * This Class is used to define method for Specimen label printing
 * @author falguni_sachde
 */
public class SpecimenLabelPrinterImpl implements LabelPrinter {


	public boolean printLabel(AbstractDomainObject abstractDomainObject, String ipAddress, User userObj) {
		
		LinkedHashMap dataMap = createObjectMap(abstractDomainObject);
		ArrayList listMap = new ArrayList ();
		listMap.add(dataMap);
		try
		{
			  PrintServiceInputParserInterface objParser = new PrintServiceInputXMLParser();
			  return objParser.callPrintWebService(listMap);
			
		}
		catch(Exception exp)
		{
			return false;
			
		}
		
	}
	

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.print.LabelPrinter#printLabel(java.util.List, java.lang.String, gov.nih.nci.security.authorization.domainobjects.User)
	 */
	public boolean printLabel(List<AbstractDomainObject> abstractDomainObjectList, String ipAddress, User userObj) {
		//Iterate through all objects in List ,crate map of each object.
		ArrayList listMap = new ArrayList ();
		for(int cnt=0;cnt < abstractDomainObjectList.size();cnt++)
		{
			AbstractDomainObject abstractDomainObject = abstractDomainObjectList.get(cnt); 
			LinkedHashMap dataMap = createObjectMap(abstractDomainObject);
			listMap.add(dataMap);
		}
		try
		{
			 PrintServiceInputParserInterface objParser = new PrintServiceInputXMLParser();
			 return objParser.callPrintWebService(listMap);
			
		}
		catch(Exception exp)
		{
			return false;
			
		}
		
		
	}
	
	
	/**
	 * @param abstractDomainObject
	 * @return
	 */
	LinkedHashMap createObjectMap(AbstractDomainObject abstractDomainObject)
	{
		LinkedHashMap dataMap = new LinkedHashMap();
		if(abstractDomainObject instanceof Specimen)
		{
			
			Specimen objSpecimen = (Specimen)abstractDomainObject;
			String label= objSpecimen.getLabel();
			String barcode = objSpecimen.getBarcode();
		
			dataMap.put("class", objSpecimen.getClassName());
			dataMap.put("id",objSpecimen.getId().toString());
			dataMap.put("label", label);
			dataMap.put("barcode",barcode);
		}
		return dataMap;
	}
}
