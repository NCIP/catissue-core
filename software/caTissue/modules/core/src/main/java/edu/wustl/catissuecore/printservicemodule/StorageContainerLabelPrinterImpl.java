
package edu.wustl.catissuecore.printservicemodule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.printserviceclient.LabelPrinter;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.User;

/**
 * Storage Container Label PrinterImpl.
 *
 */
public class StorageContainerLabelPrinterImpl implements LabelPrinter
{

	/**
	 * Generic logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(StorageContainerLabelPrinterImpl.class);
	/**
	 * Specify default Empty String.
	 */
	private static String defaultEmptyString = " ";

	/**
	 * Print single storage container.
	 * Called from PrintAction
	 * @param abstractDomainObject abstract Domain Object
	 * @param ipAddress IP Address
	 * @param userObj user Object
	 * @param printerType printer Type
	 * @param printerLocation printer Location
	 * @return true or false.
	 */
	public boolean printLabel(AbstractDomainObject abstractDomainObject, String ipAddress,
			User userObj, String printerType, String printerLocation)
	{
		final ArrayList listMap = new ArrayList();
		this.createObjectMap(abstractDomainObject, listMap, printerType, printerLocation);
		try
		{
			final PrintServiceInputParserInterface objParser = new PrintServiceInputXMLParser();
			return objParser.callPrintService(listMap);

		}
		catch (final Exception exp)
		{
			this.logger.error(exp.getMessage(), exp);
			return false;
		}
	}

	/**
	 * Print multiple storage container.
	 * Called from PrintAction
	 * @param abstractDomainObjectList abstract Domain Object list
	 * @param ipAddress IP Address
	 * @param userObj user Object
	 * @param printerType printer Type
	 * @param printerLocation printer Location
	 * @return true or false.
	 */
	public boolean printLabel(List<AbstractDomainObject> abstractDomainObjectList,
			String ipAddress, User userObj, String printerType, String printerLocation)
	{
		final ArrayList listMap = new ArrayList();

		for (int cnt = 0; cnt < abstractDomainObjectList.size(); cnt++)
		{
			final AbstractDomainObject abstractDomainObject = abstractDomainObjectList.get(cnt);
			this.createObjectMap(abstractDomainObject, listMap, printerType, printerLocation);
		}
		try
		{
			final PrintServiceInputParserInterface objParser = new PrintServiceInputXMLParser();
			return objParser.callPrintService(listMap);
		}
		catch (final Exception exp)
		{
			this.logger.error(exp.getMessage(), exp);
			return false;
		}
	}

	/**
	 * This method prints label.
	 * @param abstractDomainObject abstract Domain Object list.
	 * @param ipAddress IP Address
	 * @param userObj user Object
	 * @param printerType printer Type
	 * @param printerLocation printer Location
	 * @param printSpecimanlabel specifies the page from request is coming	 
	 * @return true or false status.
	 */
	public boolean printLabel(List<AbstractDomainObject> abstractDomainObjectList,
			String ipAddress, User userObj, String printerType,
			String printerLocation, String printSpecimanlabel)
	{
		return false;
	}


	/**
	 * created map for printing.
	 * XML created using this map:
	 *  <?xml version="1.0" encoding="UTF-8"?><Properties>
	 *  <!--This is a simple File that contain specimens object data-->
	 *  <object class="StorageContainer" id="93">
	 *  <property><name>label</name><value>site_2_Rack_93</value></property>
	 *  <property><name>barcode</name><value> </value></property>
	 *  <property><name>printerType</name><value>Box</value></property>
	 *  <property><name>printerLocation</name><value>Printer1</value></property></object></Properties>
	 * @param abstractDomainObject abstract Domain Object
	 * @param listMap list Map
	 * @param printerType printer Type
	 * @param printerLocation printer Location
	 */
	void createObjectMap(AbstractDomainObject abstractDomainObject, List listMap,
			String printerType, String printerLocation)
	{

		if (abstractDomainObject instanceof StorageContainer)
		{

			final StorageContainer objStorageContainer = (StorageContainer) abstractDomainObject;
			final LinkedHashMap dataMap = new LinkedHashMap();
			String label = objStorageContainer.getName();
			if (label == null || label.equals(""))
			{
				label = defaultEmptyString;
			}
			String barcode = objStorageContainer.getBarcode();
			dataMap.put("class", objStorageContainer.getClass().getSimpleName());
			dataMap.put("id", objStorageContainer.getId().toString());
			dataMap.put("label", label);
			if (barcode == null)
			{
				barcode = defaultEmptyString;
			}
			dataMap.put("barcode", barcode);
			dataMap.put("printerType", printerType);
			dataMap.put("printerLocation", printerLocation);
			listMap.add(dataMap);

		}

	}

}
