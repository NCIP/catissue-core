package edu.wustl.catissuecore.printservicemodule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.printserviceclient.LabelPrinter;
import edu.wustl.catissuecore.util.IdComparator;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.User;
/**
 * This Class is used to define method for Specimen label printing
 * @author falguni_sachde
 */
public class SpecimenLabelPrinterImpl implements LabelPrinter {


	private transient Logger logger = Logger.getCommonLogger(SpecimenLabelPrinterImpl.class);
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.printserviceclient.LabelPrinter#printLabel(edu.wustl.common.domain.AbstractDomainObject, java.lang.String, gov.nih.nci.security.authorization.domainobjects.User)
	 */
	public boolean printLabel(AbstractDomainObject abstractDomainObject, String ipAddress, User userObj,
			String printerType,String printerLocation)   
	{
		
		ArrayList listMap = new ArrayList ();
		//createObjectMap(abstractDomainObject,listMap);
		createObjectMap(abstractDomainObject,listMap,printerType,printerLocation,ipAddress);
		try
		{
			  PrintServiceInputParserInterface objParser = new PrintServiceInputXMLParser();
			  return objParser.callPrintService(listMap);
			
		}
		catch(Exception exp)
		{
			logger.info(exp.getMessage(), exp);
			return false;
			
		}
		
	}	

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.print.LabelPrinter#printLabel(java.util.List, java.lang.String, gov.nih.nci.security.authorization.domainobjects.User)
	 */
	public boolean printLabel(List<AbstractDomainObject> abstractDomainObjectList, String ipAddress, User userObj,String printerType,String printerLocation)
	{
		//Iterate through all objects in List ,crate map of each object.
		ArrayList listMap = new ArrayList ();
		createObjectMap(abstractDomainObjectList,listMap,printerType,printerLocation,ipAddress);
		try
		{
			PrintServiceInputParserInterface objParser = new PrintServiceInputXMLParser();
			return objParser.callPrintService(listMap);
		}
		catch(Exception exp)
		{
			logger.info(exp.getMessage(), exp);
			return false;	
		}
	}
	void createObjectMap(List<AbstractDomainObject> abstractDomainObjectList,ArrayList listMap,String printerType,String printerLocation,String ipAddress)
	{
		//Bug 11509
		Collections.sort(abstractDomainObjectList,new IdComparator());
		for(AbstractDomainObject abstractDomainObject : abstractDomainObjectList)
		{
			if(abstractDomainObject instanceof Specimen)
			{
				Specimen obj = (Specimen)abstractDomainObject;	
				addDataToPrint(obj,listMap,printerType,printerLocation,ipAddress);			
			}
		}
	}
	protected void addDataToPrint(Specimen specimen,ArrayList listMap,String printerType,String printerLocation,String ipAddress)
	{
		LinkedHashMap dataMap = new LinkedHashMap();
		String label= specimen.getLabel();
		//bug 13100
		//if any property is null the Nullpointerexception is thrown while creating
		//string data from Document in getStringFromDocument()
		if(label == null)
		{
			label = " ";
		}
		String barcode = specimen.getBarcode();
		if(barcode == null)
		{
			barcode = " ";
		}
		dataMap.put("class",specimen.getClassName());
		dataMap.put("id",specimen.getId().toString());
		dataMap.put("label",label);
		dataMap.put("barcode",barcode);
		dataMap.put("printerType",printerType);
		dataMap.put("printerLocation",printerLocation);
		listMap.add(dataMap);
		
	}
	/**
	 * @param abstractDomainObject Specimen Object
	 * @param listMap List of Specimen details including all child specimen.
	 */
	void createObjectMap(AbstractDomainObject abstractDomainObject,ArrayList listMap,String printerType,String printerLocation,String ipAddress)
	{
		
		if(abstractDomainObject instanceof Specimen)
		{
					
			Specimen objSpecimen = (Specimen)abstractDomainObject;			
			ArrayList specimenList = new ArrayList();
			specimenList.add(objSpecimen);
			getAllSpecimenList(objSpecimen,specimenList);
			//Bug 11509
			Collections.sort(specimenList,new IdComparator());
			for(int cnt=0;cnt < specimenList.size();cnt++)
			{
				Specimen obj = (Specimen)specimenList.get(cnt);
				addDataToPrint(obj,listMap,printerType,printerLocation,ipAddress);	
			}
		}
	
	}
	/**
	 * @param objSpecimen Specimen Object
	 * @param specimenList List of Specimen including all child specimen.
	 */
	void getAllSpecimenList(Specimen objSpecimen,List specimenList)
	{
		
		Collection childSpecimen = objSpecimen.getChildSpecimenCollection();
		if(childSpecimen!= null && childSpecimen.size() >0)
		{
			
			Iterator itr = childSpecimen.iterator();
			while(itr.hasNext())
			{
				Specimen specimen = (Specimen)itr.next();
				specimenList.add(specimen);
				getAllSpecimenList(specimen,specimenList);
			}
		}
	}
	
}
