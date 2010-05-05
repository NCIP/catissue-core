package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.global.Validator;


public class LabelTokenForPSpecLabel implements LabelTokens
{

	public String getTokenValue(Object object)
	{
		Specimen specimen = (Specimen)object;
		if(specimen.getParentSpecimen() == null || Validator.isEmpty(specimen.getParentSpecimen().getLabel()))
		{

		}
		else
		{
			specimen.getParentSpecimen().getLabel();
		}
		return specimen.getParentSpecimen().getLabel();
	}

}
