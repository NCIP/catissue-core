
package edu.wustl.catissuecore.namegenerator;

import java.text.DecimalFormat;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.AppUtility;
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
	private transient final Logger logger = Logger
			.getCommonLogger(SpecimenBarcodeGeneratorForMichigan.class);

	/**
	 * Default Constructor.
	 * @throws ApplicationException Application Exception
	 */
	public SpecimenBarcodeGeneratorForMichigan() throws ApplicationException
	{
		super();
	}

	/**
	 * This is a init() function it is called from the default constructor of
	 * Base class. When getInstance of base class called then this init function
	 * will be called. This method will first check the Database Name and then
	 * set function name that will convert label from int to String.
	 * @throws ApplicationException Application Exception
	 */
	@Override
	protected void init() throws ApplicationException
	{
		this.currentBarcode = Long.valueOf(0);
		final String sql = "select MAX(LABEL_COUNT) from CATISSUE_SPECIMEN_LABEL_COUNT";
		this.currentBarcode = AppUtility.getLastAvailableValue(sql);
	}

	/**
	 * @param input input type
	 * @param pattern pattern
	 * @return String
	 */
	private String format(long input, String pattern)
	{
		final DecimalFormat df = new DecimalFormat(pattern);
		return df.format(input);
	}

	/**
	 * Set barcode.
	 * @param obj Specimen obj
	 */
	@Override
	public void setBarcode(Object obj)
	{
		super.setBarcode(obj);
	}

	/**
	 * This method will be called to get the barcode.
	 * @return
	 */
	@Override
	protected String getBarcode(Specimen objSpecimen)
	{
		final String siteName = objSpecimen.getSpecimenCollectionGroup().getGroupName();
		this.currentBarcode = this.currentBarcode + 1;
		final String nextNumber = this.format(this.currentBarcode, "0000");
		final String barcode = siteName + "_" + nextNumber;
		return barcode;
	}

	/**
	 * @param parentObject parent obj
	 * @param specimenObject sp obj
	 */
	@Override
	synchronized void setNextAvailableDeriveSpecimenBarcode(AbstractSpecimen parentObject,
			Specimen specimenObject)
	{
		final String parentSpecimenBarcode = ((Specimen) parentObject).getBarcode();
		final long aliquotCount = parentObject.getChildSpecimenCollection().size();
		specimenObject.setBarcode(parentSpecimenBarcode + "_"
				+ (this.format((aliquotCount + 1), "00")));
	}

	/**
	 * This function is overridden as per Michigan requirement.
	 * @param parentObject parent obj
	 * @param specimenObject sp obj
	 */
	@Override
	synchronized void setNextAvailableAliquotSpecimenBarcode(AbstractSpecimen parentObject,
			Specimen specimenObject)
	{
		final String parentSpecimenBarcode = ((Specimen) parentObject).getBarcode();
		long aliquotCount = 0;
		aliquotCount = parentObject.getChildSpecimenCollection().size();
		final Iterator<AbstractSpecimen> itr = parentObject.getChildSpecimenCollection().iterator();
		while (itr.hasNext())
		{
			final Specimen spec = (Specimen) itr.next();
			if (spec.getLabel() == null)
			{
				aliquotCount--;
			}
		}
		aliquotCount++;
		specimenObject.setBarcode(parentSpecimenBarcode + "_" + this.format((aliquotCount), "00"));
	}

}
