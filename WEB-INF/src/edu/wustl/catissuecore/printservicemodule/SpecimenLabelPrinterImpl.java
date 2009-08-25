
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
 * This Class is used to define method for Specimen label printing.
 * @author falguni_sachde
 */
public class SpecimenLabelPrinterImpl implements LabelPrinter
{

	/**
	 * Generic logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(SpecimenLabelPrinterImpl.class);

	/**
	 * This method prints label.
	 * @param abstractDomainObject abstract Domain Object
	 * @param ipAddress IP Address
	 * @param userObj user Object
	 * @param printerType printer Type
	 * @param printerLocation printer Location
	 * @return true or false status.
	 * @see edu.wustl.catissuecore.printserviceclient.LabelPrinter#printLabel
	 * (edu.wustl.common.domain.AbstractDomainObject,
	 * java.lang.String, gov.nih.nci.security.authorization.domainobjects.User)
	 */
	public boolean printLabel(AbstractDomainObject abstractDomainObject, String ipAddress,
			User userObj, String printerType, String printerLocation)
	{

		final ArrayList listMap = new ArrayList();
		//createObjectMap(abstractDomainObject,listMap);
		this
				.createObjectMap(abstractDomainObject, listMap, printerType, printerLocation,
						ipAddress);
		try
		{
			final PrintServiceInputParserInterface objParser = new PrintServiceInputXMLParser();
			return objParser.callPrintService(listMap);

		}
		catch (final Exception exp)
		{
			this.logger.info(exp.getMessage(), exp);
			return false;

		}

	}

	/**
	 * This method prints label.
	 * @param abstractDomainObjectList abstract Domain Object list.
	 * @param ipAddress IP Address
	 * @param userObj user Object
	 * @param printerType printer Type
	 * @param printerLocation printer Location
	 * @return true or false status.
	 * @see edu.wustl.catissuecore.print.LabelPrinter#printLabel
	 * (java.util.List, java.lang.String, gov.nih.nci.security.authorization.domainobjects.User)
	 */
	public boolean printLabel(List<AbstractDomainObject> abstractDomainObjectList,
			String ipAddress, User userObj, String printerType, String printerLocation)
	{
		//Iterate through all objects in List ,crate map of each object.
		final ArrayList listMap = new ArrayList();
		this.createObjectMap(abstractDomainObjectList, listMap, printerType, printerLocation,
				ipAddress);
		try
		{
			final PrintServiceInputParserInterface objParser = new PrintServiceInputXMLParser();
			return objParser.callPrintService(listMap);
		}
		catch (final Exception exp)
		{
			this.logger.info(exp.getMessage(), exp);
			return false;
		}
	}

	/**
	 * This method creates Object Map.
	 * @param abstractDomainObjectList abstract Domain Object List
	 * @param listMap list Map
	 * @param printerType printer Type
	 * @param printerLocation printer Location
	 * @param ipAddress IP Address
	 */
	void createObjectMap(List<AbstractDomainObject> abstractDomainObjectList, ArrayList listMap,
			String printerType, String printerLocation, String ipAddress)
	{
		//Bug 11509
		Collections.sort(abstractDomainObjectList, new IdComparator());
		for (final AbstractDomainObject abstractDomainObject : abstractDomainObjectList)
		{
			if (abstractDomainObject instanceof Specimen)
			{
				final Specimen obj = (Specimen) abstractDomainObject;
				this.addDataToPrint(obj, listMap, printerType, printerLocation, ipAddress);
			}
		}
	}

	/**
	 * This method adds Data To Print.
	 * @param specimen Specimen instance.
	 * @param listMap list Map
	 * @param printerType printer Type
	 * @param printerLocation printer Location
	 * @param ipAddress IP Address
	 */
	protected void addDataToPrint(Specimen specimen, ArrayList listMap, String printerType,
			String printerLocation, String ipAddress)
	{
		final LinkedHashMap dataMap = new LinkedHashMap();
		String label = specimen.getLabel();
		//bug 13100
		//if any property is null the Null pointer exception is thrown while creating
		//string data from Document in getStringFromDocument()
		if (label == null)
		{
			label = " ";
		}
		String barcode = specimen.getBarcode();
		if (barcode == null)
		{
			barcode = " ";
		}
		dataMap.put("class", specimen.getClassName());
		dataMap.put("id", specimen.getId().toString());
		dataMap.put("label", label);
		dataMap.put("barcode", barcode);
		dataMap.put("printerType", printerType);
		dataMap.put("printerLocation", printerLocation);
		listMap.add(dataMap);

	}

	/**
	 * @param abstractDomainObject Specimen Object
	 * @param listMap List of Specimen details including all child specimen.
	 * @param printerType printer Type
	 * @param printerLocation printer Location
	 * @param ipAddress IP Address
	 */
	void createObjectMap(AbstractDomainObject abstractDomainObject, ArrayList listMap,
			String printerType, String printerLocation, String ipAddress)
	{

		if (abstractDomainObject instanceof Specimen)
		{

			final Specimen objSpecimen = (Specimen) abstractDomainObject;
			final ArrayList specimenList = new ArrayList();
			specimenList.add(objSpecimen);
			this.getAllSpecimenList(objSpecimen, specimenList);
			//Bug 11509
			Collections.sort(specimenList, new IdComparator());
			for (int cnt = 0; cnt < specimenList.size(); cnt++)
			{
				final Specimen obj = (Specimen) specimenList.get(cnt);
				this.addDataToPrint(obj, listMap, printerType, printerLocation, ipAddress);
			}
		}

	}

	/**
	 * @param objSpecimen Specimen Object
	 * @param specimenList List of Specimen including all child specimen.
	 */
	void getAllSpecimenList(Specimen objSpecimen, List specimenList)
	{

		final Collection childSpecimen = objSpecimen.getChildSpecimenCollection();
		if (childSpecimen != null && childSpecimen.size() > 0)
		{

			final Iterator itr = childSpecimen.iterator();
			while (itr.hasNext())
			{
				final Specimen specimen = (Specimen) itr.next();
				specimenList.add(specimen);
				this.getAllSpecimenList(specimen, specimenList);
			}
		}
	}

}
