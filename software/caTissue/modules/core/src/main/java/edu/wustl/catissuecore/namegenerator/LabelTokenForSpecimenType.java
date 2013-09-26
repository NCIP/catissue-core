/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.LabelGenUtil;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


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

	private static final Logger LOGGER = Logger.getCommonLogger(LabelTokenForSpecimenType.class);

	/**
	 * This will return the value of the token provided.
	 * @see edu.wustl.catissuecore.namegenerator.LabelTokens#getTokenValue(java.lang.Object, java.lang.String, java.lang.Long)
	 */
	public String getTokenValue(Object object)
	{
		String valToReplace="";
		Specimen objSpecimen = (Specimen) object;
		try
		{
			valToReplace = LabelGenUtil.getTypeAbbriviation(objSpecimen.getSpecimenType());
			if(Validator.isEmpty(valToReplace))
			{
				char abc = objSpecimen.getSpecimenType().charAt(0);
				valToReplace = Character.toString(abc);
			}
		}
		catch (Exception e)
		{
			LOGGER.info(e.getMessage());
		}
		return valToReplace;
	}

}
