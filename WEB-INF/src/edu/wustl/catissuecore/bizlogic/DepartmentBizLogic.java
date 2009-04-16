/**
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Sep 19, 2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;

public class DepartmentBizLogic extends CatissueDefaultBizLogic
{

	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{		
		//comment by Ashwin
		Department department = (Department) obj;
		if (department == null)
		{
			 String message = ApplicationProperties.getValue("app.department");
			 throw getBizLogicException(null, "domain.object.null.err.msg", message);
			//throw new DAOException("domain.object.null.err.msg", new String[]{"Institution"});
		}
		
		Validator validate = new Validator();
		if (validate.isEmpty(department.getName()))
		{
			String message = ApplicationProperties.getValue("department.name");
			throw getBizLogicException(null, "errors.item.required", message);
			
			//throw new DAOException("errors.item.required", new String[]{message});
		}
		return true;
	}
	
	/**
	 * @author Baljeet Singh
	 * This method returns the Id of the latest department corresponding to the name
	 * @param departmentName
	 * @return
	 * @throws BizLogicException
	 */
	public String getLatestDepartment(String departmentName)throws BizLogicException
	{
		String sourceObjectName = Department.class.getName();
    	String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER};
        	
    	QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
    	queryWhereClause.addCondition(new EqualClause(Constants.NAME,departmentName));
		
    	List departmentList = retrieve(sourceObjectName, selectColumnName,queryWhereClause);
    	Long departmentId = null;
    	if((departmentList != null) && (departmentList.size()>0))
    	{
    		departmentId =(Long)departmentList.get(0);
    	}
    	return departmentId.toString();
	}
	
	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 */
	public String getObjectId(DAO dao, Object domainObject) 
	{
		return Constants.ADMIN_PROTECTION_ELEMENT;
	}
	
	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeKey(Object domainObject)
    {
    	return Constants.ADD_EDIT_DEPARTMENT;
    }
 }
