/**
 * <p>Title: InstitutionBizLogic Class>
 * <p>Description:	InstitutionBizLogic </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Sep 19, 2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;

/**
 * @author
 * 
 */
public class InstitutionBizLogic extends CatissueDefaultBizLogic {

	private transient final Logger logger = Logger
			.getCommonLogger(InstitutionBizLogic.class);

	/**
	 * @param obj
	 *            : obj
	 * @param dao
	 *            : dao
	 * @param operation
	 *            : operation
	 * @throws BizLogicException
	 *             : BizLogicException
	 * @return boolean
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation)
			throws BizLogicException {
		// comment by Ashwin
		final Institution institution = (Institution) obj;
		if (institution == null) {
			final String message = ApplicationProperties
					.getValue("app.institution");
			this.logger.debug(message);
			throw this.getBizLogicException(null, "domain.object.null.err.msg",
					message);
			// throw new DAOException("domain.object.null.err.msg", new
			// String[]{"Institution"});
		}
		new Validator();
		if (Validator.isEmpty(institution.getName())) {
			final String message = ApplicationProperties
					.getValue("institution.name");
			this.logger.debug(message);
			throw this.getBizLogicException(null, "errors.item.required",
					message);
			// throw new DAOException("errors.item.required", new
			// String[]{message});
		}
		return true;
	}

	/**
	 * @author Baljeet Singh This method returns the id of the Institution given
	 *         the name
	 * @param institutionName
	 *            : institutionName
	 * @return String
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public String getLatestInstitution(String institutionName)
			throws BizLogicException {
		try {
			final String sourceObjectName = Institution.class.getName();
			final String[] selectColumnName = { Constants.SYSTEM_IDENTIFIER };

			final QueryWhereClause queryWhereClause = new QueryWhereClause(
					sourceObjectName);
			queryWhereClause.addCondition(new EqualClause(Constants.NAME,
					institutionName));

			final List institutionList = this.retrieve(sourceObjectName,
					selectColumnName, queryWhereClause);

			final Long institutionId = (Long) institutionList.get(0);
			return institutionId.toString();
		} catch (final DAOException daoexp) {
			this.logger.error(daoexp.getMessage(), daoexp);
			daoexp.printStackTrace();
			throw this.getBizLogicException(daoexp, daoexp.getErrorKeyName(),
					daoexp.getMsgValues());
		}
	}

	/**
	 * @param dao
	 *            : dao
	 * @param domainObject
	 *            : domainObject
	 * @return String Called from DefaultBizLogic to get ObjectId for
	 *         authorization check (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO,
	 *      java.lang.Object)
	 */
	@Override
	public String getObjectId(DAO dao, Object domainObject) {
		return Constants.ADMIN_PROTECTION_ELEMENT;
	}

	/**
	 * @param domainObject
	 *            : domainObject
	 * @return String To get PrivilegeName for authorization check from
	 *         'PermissionMapDetails.xml' (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	@Override
	protected String getPrivilegeKey(Object domainObject) {
		return Constants.ADD_EDIT_INSTITUTION;
	}

	/**
	 * * @return
	 * 
	 * @throws BizLogicException
	 *             Returns list of remote institution that need to be synced
	 *             i.e. remoteManagedFlag is true and dirtyEditFlag is false
	 */
	public List<Institution> getRemoteSyncInstitutions()
			throws BizLogicException {
		try {
			final String sourceObjectName = Institution.class.getName();

			final QueryWhereClause queryWhereClause = new QueryWhereClause(
					sourceObjectName);
			queryWhereClause.addCondition(new EqualClause(
					Constants.REMOTE_MANAGED_FLAG, true));
			queryWhereClause.andOpr();
			queryWhereClause.addCondition(new EqualClause(
					Constants.DIRTY_EDIT_FLAG, false));
			String[] selectedColumns = null;
			final List institutionList = this.retrieve(
					Institution.class.getName(), selectedColumns,
					queryWhereClause);
			return institutionList;
		} catch (final DAOException daoexp) {
			this.logger.error(daoexp.getMessage(), daoexp);
			daoexp.printStackTrace();
			throw this.getBizLogicException(daoexp, daoexp.getErrorKeyName(),
					daoexp.getMsgValues());
		}
	}
}
