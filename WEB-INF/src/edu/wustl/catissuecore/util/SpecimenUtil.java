
package edu.wustl.catissuecore.util;

import java.util.List;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
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
	public static void validateSpecimenStatus(long specimenId, DAO dao) throws BizLogicException
	{
		final String sourceObjectName1 = Specimen.class.getName();
		final String[] selectColumnName1 = {"activityStatus"};
		final QueryWhereClause queryWhereClause1 = new QueryWhereClause(sourceObjectName1);
		try
		{
			queryWhereClause1.addCondition(new EqualClause("id", specimenId));
			final List list = dao.retrieve(sourceObjectName1, selectColumnName1, queryWhereClause1);

			if (list.size() > 0)
			{
				String previousStatus = list.get(0).toString();
				if (previousStatus != null
						&& previousStatus.equals(Constants.ACTIVITY_STATUS_VALUES[3]))
				{

					ErrorKey errorKey = ErrorKey.getErrorKey("errors.disabled.specimen");
					throw new BizLogicException(errorKey, null, "");
					//	throw new BizLogicException(null, "errors.item", "errors.disabled.specimen");
				}
			}

		}
		catch (DAOException daoExp)
		{
			LOGGER.debug(daoExp.getMessage(), daoExp);
			final ErrorKey errorKey = daoExp.getErrorKey();
			throw new BizLogicException(errorKey, null, "");
		}
	}

	/**
	 * Retrieve specimenId by label name.
	 * @param dao DAO
	 * @param specimenEventParametersObject SpecimenEventParameters
	 * @return Object
	 * @throws DAOException DAOException
	 * @throws BizLogicException BizLogicException
	 */
	public static long retrieveSpecimenIdByLabelName(DAO dao,
			SpecimenEventParameters specimenEventParametersObject) throws DAOException,
			BizLogicException
	{
		Object specimenObject = null;
		List list = null;
		final String sourceObjectName1 = Specimen.class.getName();
		final String[] selectColumnName1 = {"id"};
		final QueryWhereClause queryWhereClause1 = new QueryWhereClause(sourceObjectName1);
		long specimenId = 0l;
		try
		{
			if (specimenEventParametersObject.getSpecimen().getId() != null)
			{
				queryWhereClause1.addCondition(new EqualClause("id", specimenEventParametersObject
						.getSpecimen().getId()));
				list = dao.retrieve(sourceObjectName1, selectColumnName1, queryWhereClause1);

			}

			else if (specimenEventParametersObject.getSpecimen().getLabel() != null
					|| specimenEventParametersObject.getSpecimen().getLabel().length() > 0)
			{
				String column = "label";

				queryWhereClause1.addCondition(new EqualClause(column,
						specimenEventParametersObject.getSpecimen().getLabel()));
				list = dao.retrieve(Specimen.class.getName(), selectColumnName1, queryWhereClause1);
				if (list.isEmpty())
				{

					final ErrorKey errorKey = ErrorKey.getErrorKey("invalid.label");

					throw new BizLogicException(errorKey, null, "");

				}
				else
				{
					specimenObject = list.get(0);

				}
			}

			if (list != null && list.size() > 0)
			{
				specimenId = Long.valueOf(list.get(0).toString());
			}
		}
		catch (DAOException daoExp)
		{
			LOGGER.debug(daoExp.getMessage(), daoExp);
			final ErrorKey errorKey = daoExp.getErrorKey();
			throw new BizLogicException(errorKey, null, "");
		}
		return specimenId;
	}
}
