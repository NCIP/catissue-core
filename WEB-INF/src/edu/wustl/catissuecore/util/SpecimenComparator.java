package edu.wustl.catissuecore.util;

import java.util.Comparator;
import edu.wustl.catissuecore.domain.Specimen;



/**
 * @author kalpana_thakur
 * Used to compare specimens ordered by specimen label
 *
 */
public class SpecimenComparator implements Comparator{
	
	public int compare(Object arg0, Object arg1) {
		
		if((Specimen)arg0 instanceof Specimen && (Specimen)arg1 instanceof Specimen)
		{
			Specimen specimenFirst = (Specimen)arg0;
			Specimen specimenSec = (Specimen)arg1;
			
			if(specimenFirst !=null && specimenSec != null && specimenFirst.getLabel() != null && specimenSec.getLabel() != null)
			{
				return specimenFirst.getLabel().compareTo(specimenSec.getLabel());
			}
			
			if(specimenFirst ==null && specimenSec == null)
			{
				return 0;
			}
			
			
			if(specimenFirst ==null)
				return 1;
			if(specimenSec == null)
				return -1;
								
		}
		
		
		return 0;
	}
	
}
