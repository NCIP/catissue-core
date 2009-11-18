
package edu.wustl.catissuecore.namegenerator;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;

/**
 * This is the Specimen Label Generator for Michigan University.
 */
public class SpecimenLabelGeneratorForMichigan extends DefaultSpecimenLabelGenerator
{

	/**
	 * Default Constructor.
	 * @throws ApplicationException Application Exception
	 */
	public SpecimenLabelGeneratorForMichigan() throws ApplicationException
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
		this.currentLabel = new Long("0");
		final String sql = "select MAX(LABEL_COUNT) from CATISSUE_SPECIMEN_LABEL_COUNT";
		this.currentLabel = AppUtility.getLastAvailableValue(sql);
	}

	/**
	 * @param input format input type
	 * @param pattern label pattern
	 * @return String
	 */
	private String format(long input, String pattern)
	{
		final DecimalFormat decFormat = new DecimalFormat(pattern);
		return decFormat.format(input);
	}

	/**
	 * Format for specimen: site_AutoIncrementingNumber.
	 * @param obj Specimen obj
	 */

	@Override
	public void setLabel(Object obj)
	{

		final Specimen objSpecimen = (Specimen) obj;

		if (objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN))
		{
			final String siteName = objSpecimen.getSpecimenCollectionGroup().getGroupName();
			this.currentLabel = this.currentLabel + 1;
			final String nextNumber = this.format(this.currentLabel, "0000");
			final String label = siteName + "_" + nextNumber;
			objSpecimen.setLabel(label);
		}

		else if (objSpecimen.getLineage().equals(Constants.ALIQUOT))
		{
			this.setNextAvailableAliquotSpecimenlabel(objSpecimen.getParentSpecimen(), objSpecimen);
		}

		else if (objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))
		{
			this.setNextAvailableDeriveSpecimenlabel(objSpecimen.getParentSpecimen(), objSpecimen);
		}

		if (objSpecimen.getChildSpecimenCollection().size() > 0)
		{
			final Collection<AbstractSpecimen> specimenCollection = objSpecimen
					.getChildSpecimenCollection();
			final Iterator<AbstractSpecimen> specCollItr = specimenCollection.iterator();
			while (specCollItr.hasNext())
			{
				final Specimen objChildSpecimen = (Specimen) specCollItr.next();
				this.setLabel(objChildSpecimen);
			}

		}

	}

	/**
	 * This function is overridden as per Michgam requirement.
	 * Format for derived specimen: parentSpecimenLabel_childCount+1
	 * @param specimenObject specimen object
	 * @param parentObject parent object
	 */

	synchronized void setNextAvailableDeriveSpecimenlabel(AbstractSpecimen parentObject,
			Specimen specimenObject)
	{

		final String parentSpecimenLabel = ((Specimen) parentObject).getLabel();

		final long aliquotCount = parentObject.getChildSpecimenCollection().size();
		specimenObject
				.setLabel(parentSpecimenLabel + "_" + (this.format((aliquotCount + 1), "00")));
	}

	/**
	 * This function is overridden as per Michigan requirement.
	 * @param specimenObject specimen object
	 * @param parentObject parent object
	 */
	synchronized void setNextAvailableAliquotSpecimenlabel(AbstractSpecimen parentObject,
			Specimen specimenObject)
	{

		final String parentSpecimenLabel = ((Specimen) parentObject).getLabel();
		long aliquotChildCount = parentObject.getChildSpecimenCollection().size();
		final Iterator<AbstractSpecimen> itr = parentObject.getChildSpecimenCollection().iterator();
		while (itr.hasNext())
		{
			final Specimen spec = (Specimen) itr.next();
			if (spec.getLabel() == null)
			{
				aliquotChildCount--;
			}
		}
		aliquotChildCount++;
		specimenObject.setLabel(parentSpecimenLabel + "_" + this.format((aliquotChildCount), "00"));
	}

}