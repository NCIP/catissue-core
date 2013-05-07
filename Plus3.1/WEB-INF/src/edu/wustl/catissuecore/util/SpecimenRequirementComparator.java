package edu.wustl.catissuecore.util;

import java.util.Comparator;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenRequirement;



/**
 * @author kalpana_thakur
 * Used to compare specimens ordered by specimen label
 *
 */
public class SpecimenRequirementComparator implements Comparator{
	
	public int compare(Object arg0, Object arg1)
	{
		int returnVal = 0;
		if((SpecimenRequirement)arg0 instanceof SpecimenRequirement && (SpecimenRequirement)arg1 instanceof SpecimenRequirement)
		{
			SpecimenRequirement sprOne = (SpecimenRequirement)arg0;
			SpecimenRequirement sprTwo = (SpecimenRequirement)arg1;
			
			if(sprOne !=null && sprTwo != null && sprOne.getId() != null && sprTwo.getId() != null)
			{
				returnVal = sprOne.getId().compareTo(sprTwo.getId());
			}
			else if(sprOne ==null && sprTwo == null)
			{
				returnVal = 0;
			}
			else if(sprOne ==null)
			{
				returnVal = 1;
			}
			else if(sprTwo == null)
			{
				returnVal = -1;
			}					
		}
		
		return returnVal;
	}

}
