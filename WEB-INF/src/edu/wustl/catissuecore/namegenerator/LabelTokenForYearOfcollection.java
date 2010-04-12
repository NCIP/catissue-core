package edu.wustl.catissuecore.namegenerator;

import java.util.Calendar;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
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


	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelTokens#getTokenValue(java.lang.Object, java.lang.String)
	 */
	public String getTokenValue(Object object, String token)
	{
		Specimen objSpecimen = (Specimen) object;
		String valToReplace = SpecimenUtil.getCollectionYear(objSpecimen);
		return valToReplace;
	}


}
