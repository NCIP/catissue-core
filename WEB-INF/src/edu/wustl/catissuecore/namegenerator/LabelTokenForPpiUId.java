package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.SpecimenUtil;
import edu.wustl.common.util.KeySequenceGeneratorUtil;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;


/**
 * The Class LabelTokenForPpiUId.
 */
public class LabelTokenForPpiUId implements LabelTokens
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(LabelTokenForPpiUId.class);

	/**
	 * This will return the value of the token provided.
	 * @see edu.wustl.catissuecore.namegenerator.LabelTokens#getTokenValue(java.lang.Object,
	 * java.lang.String, java.lang.Long)
	 */
	public String getTokenValue(Object object, String token, Long currVal)
	{
		return getSpecimenCount((Specimen)object);
	}

	/**
	 * Gets the specimen count.
	 *
	 * @param specimen the specimen
	 *
	 * @return the specimen count
	 */
	private String getSpecimenCount(Specimen  specimen)
	{
		long cprId = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getId();
		long cpId = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getId();
		String yearOfColl = SpecimenUtil.getCollectionYear(specimen);
		String key = cpId+"_"+cprId+"_"+yearOfColl;
		String type = "SpecimenCount";
		long ctr = 0;
		try
		{
			ctr = KeySequenceGeneratorUtil.getNextUniqeId(key, type);
		}
		catch (DAOException e1)
		{
			LOGGER.info(e1.getMessage());
		}
		return String.valueOf(ctr);
	}

}
