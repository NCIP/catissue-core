package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.LabelGenUtil;
import edu.wustl.common.util.global.Validator;


// TODO: Auto-generated Javadoc
/**
 * The Class LabelTokenForSpecimenType.
 */
/**
 * @author nitesh_marwaha
 *
 */
public class LabelTokenForSpecimenType implements LabelTokens
{


	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelTokens#getTokenValue(java.lang.Object, java.lang.String)
	 */
	public String getTokenValue(Object object, String token, Long currVal)
	{
		String valToReplace="";
		Specimen objSpecimen = (Specimen) object;
		try
		{
			valToReplace = LabelGenUtil.getTypeAbbriviation(objSpecimen.getSpecimenType());
			if(Validator.isEmpty(valToReplace))
			{
				char abc = objSpecimen.getSpecimenType().charAt(1);
				valToReplace = abc+"";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return valToReplace;
	}

}
