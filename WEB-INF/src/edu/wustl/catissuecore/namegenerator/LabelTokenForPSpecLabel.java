package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.Validator;


public class LabelTokenForPSpecLabel implements LabelTokens
{

	public String getTokenValue(Object object) throws ApplicationException
	{
		Specimen specimen = (Specimen)object;
		if(specimen.getParentSpecimen() == null || Validator.isEmpty(specimen.getParentSpecimen().getLabel()))
		{
			throw new ApplicationException(null, null, "Parent Label should not be empty.");
		}
		else
		{
			specimen.getParentSpecimen().getLabel();
		}
		return specimen.getParentSpecimen().getLabel();
	}

}
