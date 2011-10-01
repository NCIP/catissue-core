package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.SpecimenUtil;


// TODO: Auto-generated Javadoc
/**
 * The Class LabelTokenForYearOfcollection.
 */
/**
 * @author nitesh_marwaha
 *
 */
public class LabelTokenForYearOfcollection implements LabelTokens
{

	/**
	 * This will return the value of the token provided.
	 * @see edu.wustl.catissuecore.namegenerator.LabelTokens#getTokenValue(java.lang.Object, java.lang.String, java.lang.Long)
	 */
	public String getTokenValue(Object object)
	{
		Specimen objSpecimen = (Specimen) object;
		return SpecimenUtil.getCollectionYear(objSpecimen);
	}


}
