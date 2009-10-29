
package edu.wustl.catissuecore.printservicemodule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.printserviceclient.LabelPrinter;
import edu.wustl.catissuecore.util.IdComparator;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.User;

/**
 * This class is used to define method for Specimen label printing.
 * @author falguni_sachde
 */
public class SpecimenCollectionGroupLabelPrinterImpl implements LabelPrinter
{

	/**
	 * Generic logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(SpecimenCollectionGroupLabelPrinterImpl.class);

	/**
	 * This method prints label.
	 * @param abstractDomainObject abstract Domain Object
	 * @param ipAddress IP Address
	 * @param userObj user Object
	 * @param printerType printer Type
	 * @param printerLocation printer Location
	 * @return true or false status.
	 */
	public boolean printLabel(final AbstractDomainObject abstractDomainObject,
			final String ipAddress, final User userObj, final String printerType,
			final String printerLocation)
	{
		final List listMap = this.createObjectMap(abstractDomainObject, printerType,
				printerLocation, ipAddress);

		try
		{
			final PrintServiceInputParserInterface objParser = new PrintServiceInputXMLParser();
			return objParser.callPrintService(listMap);

		}
		catch (final Exception exp)
		{
			this.logger.error(exp.getMessage(), exp);
			exp.printStackTrace();
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
	 * @return true or false status.
	 */
	public boolean printLabel(final List<AbstractDomainObject> abstractDomainObject,
			final String ipAddress, final User userObj, final String printerType,
			final String printerLocation)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @param abstractDomainObject Specimen Collection group
	 * @param printerType printer Type
	 * @param printerLocation printer Location
	 * @param ipAddress IP Address
	 * @return List List of all Specimen including child Specimen
	 */
	List createObjectMap(AbstractDomainObject abstractDomainObject, String printerType,
			String printerLocation, String ipAddress)
	{
		final List listMap = new ArrayList();
		if (abstractDomainObject instanceof SpecimenCollectionGroup)
		{
			final SpecimenCollectionGroup objSCG = (SpecimenCollectionGroup) abstractDomainObject;
			final Collection specimenCollection = objSCG.getSpecimenCollection();
			final Iterator itr = specimenCollection.iterator();
			final ArrayList specimenList = new ArrayList();
			while (itr.hasNext())
			{
				final Specimen objSpecimen = (Specimen) itr.next();
				specimenList.add(objSpecimen);
			}
			//Bug 11509
			Collections.sort(specimenList, new IdComparator());
			for (int cnt = 0; cnt < specimenList.size(); cnt++)
			{
				final Specimen obj = (Specimen) specimenList.get(cnt);
				final Map<String, String> dataMap = new LinkedHashMap<String, String>();
				final String label = obj.getLabel();
				final String barcode = obj.getBarcode();
				dataMap.put("class", obj.getClassName());
				dataMap.put("id", obj.getId().toString());
				dataMap.put("label", label);
				dataMap.put("barcode", barcode);
				dataMap.put("printerType", printerType);
				dataMap.put("printerLocation", printerLocation);
				listMap.add(dataMap);
			}

		}
		return listMap;
	}

}
