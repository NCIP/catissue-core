package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.domain.Specimen;

/**
 * Temporary label name maker for IU specifically. This will be enhanced in the
 * future to come from the database if even necessary.
 * 
 * @author smita_kadam
 */
public class SpecimenLabelGeneratorForIU extends DefaultSpecimenLableGenerator
{

	
	private void setNextAvailableAliquotSpecimenlabel(Specimen parentObject,Specimen specimenObject) 
	{
		
		String parentSpecimenLabel = (String) parentObject.getLabel();
		long aliquotChildCount = 0;
		if(labelCountTreeMap.containsKey(parentObject))
		{
			 aliquotChildCount= Long.parseLong(labelCountTreeMap.get(parentObject).toString());	
		}
		else
		{
			// biz logic 
			aliquotChildCount = parentObject.getChildrenSpecimen().size();	
			
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
			labelCountTreeMap.put(parentObject,aliquotChildCount);	
			labelCountTreeMap.put(specimenObject,0);	
		}
		

	}
	private String determineSerumPlasma(String type, long num) 
	{
		String sp = null;
		if (type.equals("S"))
			sp = type;
		else if (num > 5)
			sp = "PNP";
		else
			sp = "P";
		return sp;
	}

}
