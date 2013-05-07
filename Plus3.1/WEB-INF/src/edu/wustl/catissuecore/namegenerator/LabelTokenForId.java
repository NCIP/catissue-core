package edu.wustl.catissuecore.namegenerator;

import edu.wustl.common.exception.ApplicationException;



// TODO: Auto-generated Javadoc
/**
 * The Class LabelTokenForId.
 */
/**
 * @author nitesh_marwaha
 *
 */
public class LabelTokenForId  extends DefaultSpecimenLabelGenerator implements LabelTokens
{

	public LabelTokenForId() throws ApplicationException
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This will return the value of the token provided.
	 * @see edu.wustl.catissuecore.namegenerator.LabelTokens#getTokenValue(java.lang.Object,
	 * java.lang.String, java.lang.Long)
	 */
	public String getTokenValue(Object object)
	{
//		currVal = currVal+1;
		currentLabel = currentLabel+1;
		return String.valueOf(currentLabel);
	}


}
