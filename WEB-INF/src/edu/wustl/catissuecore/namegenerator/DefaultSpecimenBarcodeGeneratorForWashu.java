
package edu.wustl.catissuecore.namegenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;

/**
 * This class  contains the default  implementation for Specimen Barcode generation.
 * @author falguni_sachde
 *
 */
public class DefaultSpecimenBarcodeGeneratorForWashu extends DefaultSpecimenBarcodeGenerator
{

	/**
	 * Logger object
	 */
	//private transient Logger logger = Logger.getCommonLogger(DefaultSpecimenBarcodeGeneratorForWashu.class);
	/**
	 * Current Barcode.
	 */
	protected Long currentBarcode;
	/**
	 * Datasource Name.
	 */
	//String DATASOURCE_JNDI_NAME = "java:/catissuecore";

	/**
	 * Default Constructor.
	 */
	public DefaultSpecimenBarcodeGeneratorForWashu() throws ApplicationException
	{
		super();
		init();
	}

	/**
	 * Map of objects.
	 */
	Map barcodeCountTreeMap = new HashMap();

	/**
	 * @param parentObject Parent Specimen Object
	 * @param specimenObject specimen object
	 */
	synchronized void setNextAvailableAliquotSpecimenBarcode(Specimen parentObject,
			Specimen specimenObject)
	{

		String parentSpecimenBarcode = (String) parentObject.getBarcode();
		long aliquotChildCount = 0;

		if (barcodeCountTreeMap.containsKey(parentSpecimenBarcode))
		{
			aliquotChildCount = Long.parseLong(barcodeCountTreeMap.get(parentSpecimenBarcode)
					.toString());
		}
		else
		{
			// biz logic
			aliquotChildCount = parentObject.getChildSpecimenCollection().size();

		}

		specimenObject.setBarcode(parentSpecimenBarcode + "_" + (++aliquotChildCount));
		barcodeCountTreeMap.put(parentSpecimenBarcode, aliquotChildCount);

	}

	/**
	 * @param parentObject Parent Specimen Object
	 * @param specimenObject Specimen object
	 */
	synchronized void setNextAvailableDeriveSpecimenBarcode(Specimen parentObject,
			Specimen specimenObject)
	{

		currentBarcode = currentBarcode + 1;
		specimenObject.setBarcode(currentBarcode.toString());
		barcodeCountTreeMap.put(specimenObject, 0);
	}

	/**
	 * @param obj Specimen object
	 */
	synchronized public void setBarcode(Object obj)
	{
		Specimen objSpecimen = (Specimen) obj;
		Specimen parentSpecimen = (Specimen) objSpecimen.getParentSpecimen();
		if (!Constants.COLLECTION_STATUS_COLLECTED.equals(objSpecimen.getCollectionStatus()))
		{
			return;
		}
		if (!barcodeCountTreeMap.containsKey(objSpecimen)
				&& objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN))
		{
			currentBarcode = currentBarcode + 1;
			objSpecimen.setBarcode(currentBarcode.toString());
			barcodeCountTreeMap.put(objSpecimen.getBarcode(), 0);
		}
		else if (!barcodeCountTreeMap.containsKey(objSpecimen)
				&& objSpecimen.getLineage().equals(Constants.ALIQUOT))
		{

			setNextAvailableAliquotSpecimenBarcode(parentSpecimen, objSpecimen);
		}

		else if (!barcodeCountTreeMap.containsKey(objSpecimen)
				&& objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))
		{

			setNextAvailableDeriveSpecimenBarcode(parentSpecimen, objSpecimen);
		}

		if (objSpecimen.getChildSpecimenCollection().size() > 0)
		{
			Collection specimenCollection = objSpecimen.getChildSpecimenCollection();
			Iterator it = specimenCollection.iterator();
			while (it.hasNext())
			{
				Specimen objChildSpecimen = (Specimen) it.next();
				setBarcode(objChildSpecimen);
			}
		}

	}

	/**
	 * @param objSpecimenList Specimen object list.
	 */
	
// Commented to remove the CPD used the setBarcode mothod in DefaultSpecimenBarcodeGenerator
	/*
	synchronized public void setBarcode(List objSpecimenList)
	{

		List specimenList = objSpecimenList;
		for (int index = 0; index < specimenList.size(); index++)
		{
			Specimen objSpecimen = (Specimen) specimenList.get(index);
			setBarcode(objSpecimen);
		}

	}
*/
}