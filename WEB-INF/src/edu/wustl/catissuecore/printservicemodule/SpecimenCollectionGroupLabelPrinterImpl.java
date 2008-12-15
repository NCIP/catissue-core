package edu.wustl.catissuecore.printservicemodule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import edu.wustl.catissuecore.printserviceclient.LabelPrinter;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.domain.AbstractDomainObject;
import gov.nih.nci.security.authorization.domainobjects.User;
/**
 * This class is used to define method for Specimen label printing
 * @author falguni_sachde
 */
public class SpecimenCollectionGroupLabelPrinterImpl implements LabelPrinter {

	public boolean printLabel(AbstractDomainObject abstractDomainObject, String ipAddress,
			User userObj, String printerType, String printerLocation)
	{
		ArrayList listMap = (ArrayList) createObjectMap(abstractDomainObject,printerType,printerLocation);

		try
		{
			PrintServiceInputParserInterface objParser = new PrintServiceInputXMLParser();
			return objParser.callPrintService(listMap);

		}
		catch(Exception exp)
		{
			return false;

		}
	}


	public boolean printLabel(List<AbstractDomainObject> abstractDomainObject, String ipAddress,
			User userObj, String printerType, String printerLocation)
	{
		// TODO Auto-generated method stub
		return false;
	}	
	
	/**
	 * @param abstractDomainObject Specimen Collection group
	 * @return List List of all Specimen including child Specimen
	 */
	List createObjectMap(AbstractDomainObject abstractDomainObject,String printerType,String printerLocation)
	{
		ArrayList listMap = new ArrayList ();
		if(abstractDomainObject instanceof SpecimenCollectionGroup)
		{
			
			SpecimenCollectionGroup objSCG = (SpecimenCollectionGroup)abstractDomainObject;
			Collection specimenCollection = objSCG.getSpecimenCollection();
			Iterator itr = specimenCollection.iterator();
			ArrayList specimenList = new ArrayList();
			while(itr.hasNext())
			{
				Specimen objSpecimen = (Specimen)itr.next();
				specimenList.add(objSpecimen);				
				
			}
			
			for(int cnt=0;cnt < specimenList.size();cnt++)
			{
				Specimen obj = (Specimen)specimenList.get(cnt);
				LinkedHashMap dataMap = new LinkedHashMap();
				String label= obj.getLabel();
				String barcode = obj.getBarcode();
//			
				dataMap.put("class", obj.getClassName());
				dataMap.put("id",obj.getId().toString());
				dataMap.put("label", label);
				dataMap.put("barcode",barcode);
				if(printerType.equals(""))
				{
					printerType = " ";				   
				}
				dataMap.put("printerType",printerType);
				if(printerLocation.equals(""))
				{
					printerLocation = " ";				    
				}
				dataMap.put("printerLocation",printerLocation);
				listMap.add(dataMap);
			}

		}
		return listMap;
	}

	
	
}
