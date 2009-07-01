
package edu.wustl.catissuecore.namegenerator;

import java.util.Iterator;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;

/**
 * This is the Specimen Label Generator for Indiana University.
 * @author falguni_sachde
 */
public class SpecimenLabelGeneratorForIU extends DefaultSpecimenLabelGenerator
{

	/**
	 * Default Constructor.
	 */
	public SpecimenLabelGeneratorForIU()
	{
		super();
	}

	/**
	 * This function is overridden as per IU requirement.
	 * @param parentObject parent obj
	 * @param specimenObject specimen obj
	 */
	synchronized void setNextAvailableAliquotSpecimenlabel(Specimen parentObject,
			Specimen specimenObject)
	{

		String parentSpecimenLabel = (String) parentObject.getLabel();
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
		StringBuffer buffy = null;
		StringBuffer prefixBuffy = new StringBuffer();
		String sp = null;

		if (parentSpecimenLabel != null)
		{
			parentSpecimenLabel = (String) parentSpecimenLabel;
			int dash = parentSpecimenLabel.lastIndexOf("-");
			prefixBuffy.append(parentSpecimenLabel.substring(0, dash + 1));
			sp = parentSpecimenLabel.substring(dash + 1, dash + 2);
			buffy = new StringBuffer();
			buffy.append(prefixBuffy);
			buffy.append(++aliquotChildCount);
			buffy.append(determineSerumPlasma(sp, aliquotChildCount));
			specimenObject.setLabel(buffy.toString());
		}
	}

	/**
	 * @param type type
	 * @param num num value
	 * @return Specific method of IU
	 */
	private String determineSerumPlasma(String type, long num)
	{
		String sp = null;
		if (type.equals("S"))
		{
			sp = type;
		}
		else if (num > 5)
		{
			sp = "PNP";
		}
		else
		{
			sp = "P";
		}
		return sp;
	}

}