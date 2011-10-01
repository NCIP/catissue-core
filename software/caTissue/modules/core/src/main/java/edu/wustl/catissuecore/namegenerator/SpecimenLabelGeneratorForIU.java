
package edu.wustl.catissuecore.namegenerator;

import java.util.Collection;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.exception.ApplicationException;

/**
 * This is the Specimen Label Generator for Indiana University.
 * @author falguni_sachde
 */
public class SpecimenLabelGeneratorForIU extends DefaultSpecimenLabelGenerator
{

	/**
	 * Default Constructor.
	 * @throws ApplicationException Application Exception
	 */
	public SpecimenLabelGeneratorForIU() throws ApplicationException
	{
		super();
	}

	/**
	 * This function is overridden as per IU requirement.
	 * @param parentObject parent obj
	 * @param specimenObject specimen obj
	 */
	@Override
	synchronized void setNextAvailableAliquotSpecimenlabel(Specimen parentObject,
			Specimen specimenObject)
	{

		String parentSpecimenLabel = parentObject.getLabel();
		Collection childCollection=parentObject.getChildSpecimenCollection();
		if(childCollection!=null)
		{
			long aliquotChildCount = childCollection.size();
			final Iterator<AbstractSpecimen> itr = childCollection.iterator();
			while (itr.hasNext())
			{
				final Specimen spec = (Specimen) itr.next();
				if (spec.getLabel() == null)
				{
					aliquotChildCount--;
				}
			}

			if (parentSpecimenLabel != null)
			{
				final StringBuffer prefixBuffy = new StringBuffer();
				final int dash = parentSpecimenLabel.lastIndexOf('-');
				prefixBuffy.append(parentSpecimenLabel.substring(0, dash + 1));
				String srumPlasma = parentSpecimenLabel.substring(dash + 1, dash + 2);
				StringBuffer buffy = new StringBuffer();
				buffy.append(prefixBuffy);
				buffy.append(++aliquotChildCount);
				buffy.append(this.determineSerumPlasma(srumPlasma, aliquotChildCount));
				specimenObject.setLabel(buffy.toString());
			}
		}
	}

	/**
	 * @param type type
	 * @param num num value
	 * @return Specific method of IU
	 */
	private String determineSerumPlasma(String type, long num)
	{
		String srumPlasma = null;
		if (("S").equals(type))
		{
			srumPlasma = type;
		}
		else if (num > 5)
		{
			srumPlasma = "PNP";
		}
		else
		{
			srumPlasma = "P";
		}
		return srumPlasma;
	}

}