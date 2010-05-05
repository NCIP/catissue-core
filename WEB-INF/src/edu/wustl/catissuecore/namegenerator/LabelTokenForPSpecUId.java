package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.KeySequenceGeneratorUtil;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;


/**
 * The Class LabelTokenForPSpecUId.
 * @author nitesh_marwaha
 *
 */
public class LabelTokenForPSpecUId implements LabelTokens
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(LabelTokenForPSpecUId.class);

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelTokens#getTokenValue(java.lang.Object)
	 */
	public String getTokenValue(Object object)
	{
		return getParentSequenceNumber(object);
	}

	/**
	 * Gets the parent sequence number.
	 *
	 * @param object the object
	 *
	 * @return the parent sequence number
	 */
	private String getParentSequenceNumber(Object object)
	{
		Specimen specimen = (Specimen)object;
		Long parentSeqCtr = 0l;
		if(specimen.getParentSpecimen()== null)
		{

		}
		else
		{
			String parentSpecId = specimen.getParentSpecimen().getId().toString();
			String cprId = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getId().toString();
			String key = cprId+"_"+parentSpecId;
			String type = "ParentSequenceNumber";
			try
			{
				parentSeqCtr = KeySequenceGeneratorUtil.getNextUniqeId(key, type);
			}
			catch (DAOException e1)
			{
				LOGGER.info(e1.getMessage());
			}
		}
		return parentSeqCtr.toString();
	}

}
