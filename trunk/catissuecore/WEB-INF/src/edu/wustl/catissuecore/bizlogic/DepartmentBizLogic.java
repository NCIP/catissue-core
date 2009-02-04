/**
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Sep 19, 2006
 */

package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.domain.Department;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;

public class DepartmentBizLogic extends DefaultBizLogic
{

	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{		
		//comment by Ashwin
		Department department = (Department) obj;
		if (department == null)
		{
			 String message = ApplicationProperties.getValue("app.department");
			 throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg",message));   			
			//throw new DAOException("domain.object.null.err.msg", new String[]{"Institution"});
		}
		
		Validator validate = new Validator();
		if (validate.isEmpty(department.getName()))
		{
			String message = ApplicationProperties.getValue("department.name");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required",message));   			
			//throw new DAOException("errors.item.required", new String[]{message});
		}
		return true;
	}

}
