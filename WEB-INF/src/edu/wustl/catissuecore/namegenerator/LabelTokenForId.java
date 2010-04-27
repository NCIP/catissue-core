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

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelTokens#getTokenValue(java.lang.Object, java.lang.String)
	 */
	public String getTokenValue(Object object, String token, Long currVal)
	{
//		currVal = currVal+1;
		return currVal+"";
	}


}
