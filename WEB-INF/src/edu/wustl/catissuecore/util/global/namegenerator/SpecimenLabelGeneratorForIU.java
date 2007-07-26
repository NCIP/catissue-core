package edu.wustl.catissuecore.util.global.namegenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;

/**
 * Temporary label name maker for IU specifically. This will be enhanced in the
 * future to come from the database if even necessary.
 * 
 * @author smita_kadam
 */
public class SpecimenLabelGeneratorForIU extends DefaultSpecimenLableGenerator
{

	/**
	 * This method will return no of aliquot chids present for a passed specimenID.
	 * If plasma and 6,7, 8 or 9 then it is PNP else if serum just int and S 
	 * @param  obj
	 * @return aliquotChild Count
	 */
	public synchronized List<String> getNextAvailableAliquotSpecimenlabel(Object parentValueMap, int count) 
	{
		String parentSpecimenLabel = (String)((Map)parentValueMap).get(Constants.PARENT_SPECIMEN_LABEL_KEY);
		//validate(parentSpecimenLabel);
		List<String> labels = new ArrayList<String>();
		StringBuffer buffy = null;
		StringBuffer prefixBuffy = new StringBuffer();
		String sp = null;
		
		if (parentSpecimenLabel != null) 
		{
			parentSpecimenLabel = (String) parentSpecimenLabel;
			int dash = parentSpecimenLabel.lastIndexOf("-");
			prefixBuffy.append(parentSpecimenLabel.substring(0, dash + 1));
			sp = parentSpecimenLabel.substring(dash + 1, dash + 2);
		
			for(int i=1; i<=count; i++)
			{
				buffy = new StringBuffer();
				buffy.append(prefixBuffy);
				buffy.append(i);
				buffy.append(determineSerumPlasma(sp, i));
				labels.add(buffy.toString());
			}
		}
		
		return labels;
	}
	
	private String determineSerumPlasma(String type, int num) 
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
