/**
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00 Created on Sep 19, 2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;

public class DepartmentBizLogic extends CatissueDefaultBizLogic
{
	/**
	 * Logger instance.
	 */
	private transient final Logger logger =
		Logger.getCommonLogger(DepartmentBizLogic.class);
	/**
	 * 
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		// comment by Ashwin
		final Department department = (Department) obj;
		if (department == null)
		{
			final String message = ApplicationProperties.getValue("app.department");
			throw this.getBizLogicException(null, "domain.object.null.err.msg", message);
			// throw new DAOException("domain.object.null.err.msg", new
			// String[]{"Institution"});
		}

		new Validator();
		if (Validator.isEmpty(department.getName()))
		{
			final String message = ApplicationProperties.getValue("department.name");
			throw this.getBizLogicException(null, "errors.item.required", message);

			// throw new DAOException("errors.item.required", new
			// String[]{message});
		}
		return true;
	}

	/**
	 * @author Baljeet Singh This method returns the Id of the latest department
	 *         corresponding to the name
	 * @param departmentName : departmentName
	 * @return String
	 * @throws BizLogicException  : BizLogicException
	 */
	public String getLatestDepartment(String departmentName) throws BizLogicException
	{
		try
		{
			final String sourceObjectName = Department.class.getName();
			final String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER};

			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause(Constants.NAME, departmentName));

			final List departmentList = this.retrieve(sourceObjectName, selectColumnName,
					queryWhereClause);
			Long departmentId = null;
			if ((departmentList != null) && (departmentList.size() > 0))
			{
				departmentId = (Long) departmentList.get(0);
			}
			return departmentId.toString();
		}
		catch (final DAOException daoexp)
		{
			this.logger.error(daoexp.getMessage(),daoexp);
			daoexp.printStackTrace();
			throw this
					.getBizLogicException(daoexp, daoexp.getErrorKeyName(), daoexp.getMsgValues());
		}
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @param domainObject : domainObject
	 * @param dao : dao
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
	 * @param domainObject : domainObject
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	@Override
	protected String getPrivilegeKey(Object domainObject)
	{
		return Constants.ADD_EDIT_DEPARTMENT;
	}
}
