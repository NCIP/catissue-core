
package edu.wustl.catissuecore.namegenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;

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
	 * @throws ApplicationException Application Exception
	 */
	public DefaultSpecimenBarcodeGeneratorForWashu() throws ApplicationException
	{
		super();
		this.init();
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

		final String parentSpecimenBarcode = parentObject.getBarcode();
		long aliquotChildCount = 0;

		if (this.barcodeCountTreeMap.containsKey(parentSpecimenBarcode))
		{
			aliquotChildCount = Long.parseLong(this.barcodeCountTreeMap.get(parentSpecimenBarcode)
					.toString());
		}
		else
		{
			// biz logic
			aliquotChildCount = parentObject.getChildSpecimenCollection().size();

		}

		specimenObject.setBarcode(parentSpecimenBarcode + "_" + (++aliquotChildCount));
		this.barcodeCountTreeMap.put(parentSpecimenBarcode, aliquotChildCount);

	}

	/**
	 * @param parentObject Parent Specimen Object
	 * @param specimenObject Specimen object
	 */
	synchronized void setNextAvailableDeriveSpecimenBarcode(Specimen parentObject,
			Specimen specimenObject)
	{

		this.currentBarcode = this.currentBarcode + 1;
		specimenObject.setBarcode(this.currentBarcode.toString());
		this.barcodeCountTreeMap.put(specimenObject, 0);
	}

	/**
	 * @param obj Specimen object
	 */
	@Override
	synchronized public void setBarcode(Object obj)
	{
		final Specimen objSpecimen = (Specimen) obj;
		final Specimen parentSpecimen = (Specimen) objSpecimen.getParentSpecimen();
		if (!Constants.COLLECTION_STATUS_COLLECTED.equals(objSpecimen.getCollectionStatus()))
		{
			return;
		}
		if (!this.barcodeCountTreeMap.containsKey(objSpecimen)
				&& objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN))
		{
			this.currentBarcode = this.currentBarcode + 1;
			objSpecimen.setBarcode(this.currentBarcode.toString());
			this.barcodeCountTreeMap.put(objSpecimen.getBarcode(), 0);
		}
		else if (!this.barcodeCountTreeMap.containsKey(objSpecimen)
				&& objSpecimen.getLineage().equals(Constants.ALIQUOT))
		{

			this.setNextAvailableAliquotSpecimenBarcode(parentSpecimen, objSpecimen);
		}

		else if (!this.barcodeCountTreeMap.containsKey(objSpecimen)
				&& objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))
		{

			this.setNextAvailableDeriveSpecimenBarcode(parentSpecimen, objSpecimen);
		}

		if (objSpecimen.getChildSpecimenCollection().size() > 0)
		{
			final Collection specimenCollection = objSpecimen.getChildSpecimenCollection();
			final Iterator<Specimen> specColItr = specimenCollection.iterator();
			while (specColItr.hasNext())
			{
				final Specimen objChildSpecimen = (Specimen) specColItr.next();
				this.setBarcode(objChildSpecimen);
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