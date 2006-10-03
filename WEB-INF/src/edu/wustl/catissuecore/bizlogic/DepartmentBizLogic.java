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
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.dao.DAO;

public class DepartmentBizLogic extends DefaultBizLogic
{

	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		Department department = (Department) obj;
		if (department == null)
			throw new DAOException("domain.object.null.err.msg", new String[]{"Department"});
		Validator validate = new Validator();
		if (validate.isEmpty(department.getName()))
		{
			String message = ApplicationProperties.getValue("department.name");
			throw new DAOException("errors.item.required", new String[]{message});
		}
		return true;
	}

}
