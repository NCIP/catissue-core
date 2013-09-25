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
import edu.wustl.common.exception.ApplicationException;
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
	public String getTokenValue(Object object) throws ApplicationException
	{
		return getParentSequenceNumber(object);
	}

	/**
	 * Gets the parent sequence number.
	 *
	 * @param object the object
	 *
	 * @return the parent sequence number
	 * @throws ApplicationException
	 */
	private String getParentSequenceNumber(Object object) throws ApplicationException
	{
		Specimen specimen = (Specimen)object;
		Long parentSeqCtr = 0l;
		if(specimen.getParentSpecimen()== null)
		{
			throw new ApplicationException(null, null, "Parent Specimen is not available");
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
