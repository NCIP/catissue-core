package edu.wustl.catissuecore.util;

import java.util.List;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;


/**
 * The Class SpecimenUtil.
 * @author nitesh_marwaha
 *
 */
public final class SpecimenUtil
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(SpecimenUtil.class);

	/**
	 * Validate specimen status.
	 *
	 * @param specimen the specimen
	 * @param dao the dao
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	public static void validateSpecimenStatus(Specimen specimen, DAO dao) throws BizLogicException
	{
		final String sourceObjectName1 = Specimen.class.getName();
		final String[] selectColumnName1 = {"activityStatus"};
		final QueryWhereClause queryWhereClause1 = new QueryWhereClause(sourceObjectName1);
		try
		{
			queryWhereClause1.addCondition(new EqualClause("id", specimen.getId()));
			final List list = dao
			.retrieve(sourceObjectName1, selectColumnName1, queryWhereClause1);

			if(list.size()>0)
			{
				String previousStatus=list.get(0).toString();
				if(previousStatus !=null && previousStatus.equals(Constants.ACTIVITY_STATUS_VALUES[3]))
				{

					ErrorKey errorKey = ErrorKey.getErrorKey("errors.disabled.specimen");
					throw new BizLogicException(errorKey, null,	"");
				//	throw new BizLogicException(null, "errors.item", "errors.disabled.specimen");
				}
			}

		}
		catch (DAOException daoExp)
		{
			LOGGER.debug(daoExp.getMessage(),daoExp);
			final ErrorKey errorKey = daoExp.getErrorKey();
			throw new BizLogicException(errorKey, null, "");
		}
	}
}
