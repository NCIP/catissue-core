
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
 * This is the Specimen Label Generator for Michigan University.
 */
public class SpecimenLabelGeneratorForMichigan extends DefaultSpecimenLabelGenerator
{
	/**
	 * Logger Object.
	 */
	private static final transient  Logger logger = Logger.getCommonLogger(SpecimenLabelGeneratorForMichigan.class);
	/**
	 * Default Constructor.
	 */
	public SpecimenLabelGeneratorForMichigan() throws ApplicationException
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
		currentLabel = new Long("0");
		String sql = "select MAX(LABEL_COUNT) from CATISSUE_SPECIMEN_LABEL_COUNT";
		currentLabel=AppUtility.getLastAvailableValue(sql);
	}

	
	/**
	 * @param input format input type
	 * @param pattern label pattern
	 * @return String
	 */
	private String format(long input, String pattern)
	{
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(input);
	}

	/**
	 * Format for specimen: site_AutoIncrementingNumber.
	 * @param obj Specimen obj
	 */

	public void setLabel(Object obj)
	{

		Specimen objSpecimen = (Specimen) obj;

		if (objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN))
		{
			String siteName = objSpecimen.getSpecimenCollectionGroup().getGroupName();
			currentLabel = currentLabel + 1;
			String nextNumber = format(currentLabel, "0000");
			String label = siteName + "_" + nextNumber;
			objSpecimen.setLabel(label);
		}

		else if (objSpecimen.getLineage().equals(Constants.ALIQUOT))
		{
			setNextAvailableAliquotSpecimenlabel(objSpecimen.getParentSpecimen(), objSpecimen);
		}

		else if (objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))
		{
			setNextAvailableDeriveSpecimenlabel(objSpecimen.getParentSpecimen(), objSpecimen);
		}

		if (objSpecimen.getChildSpecimenCollection().size() > 0)
		{
			Collection<AbstractSpecimen> specimenCollection = objSpecimen.getChildSpecimenCollection();
			Iterator<AbstractSpecimen> it = specimenCollection.iterator();
			while (it.hasNext())
			{
				Specimen objChildSpecimen = (Specimen) it.next();
				setLabel(objChildSpecimen);
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

		String parentSpecimenLabel = (String) ((Specimen) parentObject).getLabel();

		long aliquotCount = parentObject.getChildSpecimenCollection().size();
		specimenObject.setLabel(parentSpecimenLabel + "_" + (format((aliquotCount + 1), "00")));
	}

	/**
	 * This function is overridden as per Michigan requirement.
	 * @param specimenObject specimen object
	 * @param parentObject parent object
	 */
	synchronized void setNextAvailableAliquotSpecimenlabel(AbstractSpecimen parentObject,
			Specimen specimenObject)
	{

		String parentSpecimenLabel = (String) ((Specimen) parentObject).getLabel();
		long aliquotChildCount = parentObject.getChildSpecimenCollection().size();
		Iterator<AbstractSpecimen> itr = parentObject.getChildSpecimenCollection().iterator();
		while (itr.hasNext())
		{
			Specimen spec = (Specimen) itr.next();
			if (spec.getLabel() == null)
			{
				aliquotChildCount--;
			}
		}
		aliquotChildCount++;
		specimenObject.setLabel(parentSpecimenLabel + "_" + format((aliquotChildCount), "00"));
	}

}