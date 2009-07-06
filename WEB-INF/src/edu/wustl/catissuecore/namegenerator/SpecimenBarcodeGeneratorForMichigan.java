
package edu.wustl.catissuecore.namegenerator;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;

/**
 * This is the Specimen Barcode Generator for Michigan University.
 * @author falguni_sachde
 */
public class SpecimenBarcodeGeneratorForMichigan extends DefaultSpecimenBarcodeGenerator
{
	/**
	 * Logger object.
	 */
	private transient Logger logger = Logger.getCommonLogger(SpecimenBarcodeGeneratorForMichigan.class);
	/**
	 * Default Constructor.
	 */
	public SpecimenBarcodeGeneratorForMichigan() throws ApplicationException
	{
		super();
	}
	/**
	 * This is a init() function it is called from the default constructor of
	 * Base class. When getInstance of base class called then this init function
	 * will be called. This method will first check the Datatbase Name and then
	 * set function name that will convert lable from int to String
	 */
	protected void init() throws ApplicationException
	{
			currentBarcode = Long.valueOf(0);
			String sql = "select MAX(LABEL_COUNT) from CATISSUE_SPECIMEN_LABEL_COUNT";
			currentBarcode=AppUtility.getLastAvailableValue(sql);			
	}
	
	/**
	 * @param input input type
	 * @param pattern pattern
	 * @return String
	 */
	private String format(long input, String pattern)
	{
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(input);
	}

	/**
	 * Set barcode.
	 * @param obj Specimen obj
	 */
	public void setBarcode(Object obj)
	{
		Specimen objSpecimen = (Specimen) obj;
		if (objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN))
		{
			String siteName = objSpecimen.getSpecimenCollectionGroup().getGroupName();
			currentBarcode = currentBarcode + 1;
			String nextNumber = format(currentBarcode, "0000");
			String barcode = siteName + "_" + nextNumber;
			objSpecimen.setBarcode(barcode);
		}

		else if (objSpecimen.getLineage().equals(Constants.ALIQUOT))
		{
			setNextAvailableAliquotSpecimenBarcode(objSpecimen.getParentSpecimen(), objSpecimen);
		}

		else if (objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))
		{
			setNextAvailableDeriveSpecimenBarcode(objSpecimen.getParentSpecimen(), objSpecimen);
		}

		if (objSpecimen.getChildSpecimenCollection().size() > 0)
		{
			Collection<AbstractSpecimen> specimenCollection = objSpecimen.getChildSpecimenCollection();
			Iterator<AbstractSpecimen> it = specimenCollection.iterator();
			while (it.hasNext())
			{
				Specimen objChildSpecimen = (Specimen) it.next();
				setBarcode(objChildSpecimen);
			}
		}

	}

	/**
	 * @param parentObject parent obj
	 * @param specimenObject sp obj
	 */
	synchronized void setNextAvailableDeriveSpecimenBarcode(AbstractSpecimen parentObject,
			Specimen specimenObject)
	{
		String parentSpecimenBarcode = (String) ((Specimen) parentObject).getBarcode();
		long aliquotCount = parentObject.getChildSpecimenCollection().size();
		specimenObject.setBarcode(parentSpecimenBarcode + "_" + (format((aliquotCount + 1), "00")));
	}

	/**
	 * This function is overridden as per Michigan requirement.
	 * @param parentObject parent obj
	 * @param specimenObject sp obj
	 */
	synchronized void setNextAvailableAliquotSpecimenBarcode(AbstractSpecimen parentObject,
			Specimen specimenObject)
	{
		String parentSpecimenBarcode = (String) ((Specimen) parentObject).getBarcode();
		long aliquotCount = 0;
		aliquotCount = parentObject.getChildSpecimenCollection().size();
		Iterator<AbstractSpecimen> itr = parentObject.getChildSpecimenCollection().iterator();
		while (itr.hasNext())
		{
			Specimen spec = (Specimen) itr.next();
			if (spec.getLabel() == null)
			{
				aliquotCount--;
			}
		}
		aliquotCount++;
		specimenObject.setBarcode(parentSpecimenBarcode + "_" + format((aliquotCount), "00"));
	}

}
