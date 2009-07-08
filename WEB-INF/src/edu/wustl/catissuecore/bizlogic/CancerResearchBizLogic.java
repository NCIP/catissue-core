/**
 * <p>
 * Title: CancerResearchBizLogic Class>
 * <p>
 * Description: CancerResearchBizLogic
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 * 
 * @author Ashish Gupta
 * @version 1.00
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;

public class CancerResearchBizLogic extends CatissueDefaultBizLogic
{

	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		final CancerResearchGroup cancerResearchGroup = (CancerResearchGroup) obj;
		if (cancerResearchGroup == null)
		{
			final String message = ApplicationProperties.getValue("app.cancerResearchGroup");
			throw this.getBizLogicException(null, "domain.object.null.err.msg", message);

		}

		new Validator();
		if (Validator.isEmpty(cancerResearchGroup.getName()))
		{
			final String message = ApplicationProperties.getValue("cancerResearchGroup.name");
			throw this.getBizLogicException(null, "errors.item.required", message);
			// throw new DAOException("errors.item.required", new
			// String[]{message});
		}
		return true;
	}

	/**
	 * This method returns the Id of the CRG corresponding to the CRG name
	 * 
	 * @param crgName
	 *            CancerResearchGroup name
	 * @return the id of the CancerResearchGroup
	 * @throws DAOException
	 */
	public String getLatestCRG(String crgName) throws BizLogicException
	{
		try
		{
			final String sourceObjectName = CancerResearchGroup.class.getName();
			final String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER};

			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause(Constants.NAME, crgName));

			final List crgList = this
					.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
			Long crgId = null;
			if ((crgList != null) && (crgList.size() > 0))
			{
				crgId = (Long) crgList.get(0);
			}
			return crgId.toString();
		}
		catch (final DAOException daoexp)
		{
			throw this
					.getBizLogicException(daoexp, daoexp.getErrorKeyName(), daoexp.getMsgValues());
		}
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO,
	 *      java.lang.Object)
	 */
	@Override
	public String getObjectId(DAO dao, Object domainObject)
	{
		return Constants.ADMIN_PROTECTION_ELEMENT;
	}

	/**
	 * To get PrivilegeName for authorization check from
	 * 'PermissionMapDetails.xml' (non-Javadoc)
	 * 
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	@Override
	protected String getPrivilegeKey(Object domainObject)
	{

		return Constants.ADD_EDIT_CRG;
	}
}
