package edu.wustl.catissuecore.printservicemodule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.printserviceclient.LabelPrinter;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.User;


public class StorageContainerLabelPrinterImpl implements LabelPrinter
{

	private transient Logger logger = Logger.getCommonLogger(StorageContainerLabelPrinterImpl.class);
	public static String defaultEmptyString = " ";
 
	/**
	 * Print single storage container
	 * Called from PrintAction
	 */
	public boolean printLabel(AbstractDomainObject abstractDomainObject, String ipAddress,
			User userObj, String printerType, String printerLocation)
	{
		ArrayList listMap = new ArrayList ();
		createObjectMap(abstractDomainObject,listMap,printerType,printerLocation);
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
    /**
     * Print multiple storage container
     * Called from PrintAction
     */
	public boolean printLabel(List<AbstractDomainObject> abstractDomainObjectList, String ipAddress,
			User userObj, String printerType, String printerLocation)
	{
		ArrayList listMap = new ArrayList ();

		for(int cnt=0;cnt < abstractDomainObjectList.size();cnt++)
		{
			AbstractDomainObject abstractDomainObject = abstractDomainObjectList.get(cnt); 
			createObjectMap(abstractDomainObject,listMap,printerType,printerLocation);
		}
		try
		{
			PrintServiceInputParserInterface objParser = new PrintServiceInputXMLParser();
			return objParser.callPrintService(listMap);
		}
		catch(Exception exp)
		{
			logger.info(exp.getMessage(), exp);
			exp.printStackTrace();
			return false;	
		}
	}
	/**
	 * created map for printing 
	 * XML created using this map:
	 *  <?xml version="1.0" encoding="UTF-8"?><Properties><!--This is a simple File that contain specimens object data-->
	 *  <object class="StorageContainer" id="93">
	 *  <property><name>label</name><value>site_2_Rack_93</value></property>
	 *  <property><name>barcode</name><value> </value></property>
	 *  <property><name>printerType</name><value>Box</value></property>
	 *  <property><name>printerLocation</name><value>Printer1</value></property></object></Properties>
	 * @param abstractDomainObject
	 * @param listMap
	 * @param printerType
	 * @param printerLocation
	 */
	void createObjectMap(AbstractDomainObject abstractDomainObject,ArrayList listMap,String printerType,String printerLocation)
	{
		
		if(abstractDomainObject instanceof StorageContainer)
		{

			StorageContainer objStorageContainer = (StorageContainer)abstractDomainObject;			
			LinkedHashMap dataMap = new LinkedHashMap();
			String label= objStorageContainer.getName();
			if(label==null || label.equals(""))
			{
				label = defaultEmptyString;
			}
			String barcode = objStorageContainer.getBarcode();
			dataMap.put("class",objStorageContainer.getClass().getSimpleName());
			dataMap.put("id",objStorageContainer.getId().toString());
			dataMap.put("label",label);
			if(barcode==null)
			{
				barcode = defaultEmptyString;
			}
			dataMap.put("barcode",barcode);
			dataMap.put("printerType",printerType);
			dataMap.put("printerLocation",printerLocation);
			listMap.add(dataMap);

		}

	}

}
