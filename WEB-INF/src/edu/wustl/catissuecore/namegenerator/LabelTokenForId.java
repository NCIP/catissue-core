package edu.wustl.catissuecore.namegenerator;



// TODO: Auto-generated Javadoc
/**
 * The Class LabelTokenForId.
 */
/**
 * @author nitesh_marwaha
 *
 */
public class LabelTokenForId implements LabelTokens
{

	/**
	 * This will return the value of the token provided.
	 * @see edu.wustl.catissuecore.namegenerator.LabelTokens#getTokenValue(java.lang.Object,
	 * java.lang.String, java.lang.Long)
	 */
	public String getTokenValue(Object object, String token, Long currVal)
	{
//		currVal = currVal+1;
		return currVal.toString();
	}


}
