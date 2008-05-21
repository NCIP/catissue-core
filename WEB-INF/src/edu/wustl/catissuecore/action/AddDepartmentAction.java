package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.DepartmentForm;
import edu.wustl.catissuecore.bizlogic.DepartmentBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


public class AddDepartmentAction extends CommonAddEditAction
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		String departmentName =(String)request.getParameter(Constants.DEPARTMENT_NAME);
		DepartmentBizLogic bizlogic = new DepartmentBizLogic();
		String departmentId =  null;
		String responseString = null;
		
		/**
		 * Setting the department name to form
		 */
		DepartmentForm departmentForm = (DepartmentForm)form;
		departmentForm.setOperation(Constants.ADD);
		departmentForm.setName(departmentName);
		
		//Saving the department to the Database using COmmonAddEditAction
		ActionForward forward = super.execute(mapping,departmentForm,request,response);
		if((forward != null) && (forward.getName().equals(Constants.FAILURE)))
		{
			ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
			Logger.out.info("Errors:" + errors);
			if (errors != null || errors.size() != 0)
			{
				Iterator iterator = errors.get();
				while (iterator.hasNext())
				{
					ActionError next = (ActionError)iterator.next();
					Object[] values = next.getValues();
					for(int j=0;j<values.length;j++)
					{
						responseString = (String)values[j];
					}
				}
			}
		}
		else
		{
			try
			{
				departmentId = bizlogic.getLatestDepartment(departmentName);
				responseString = departmentId + Constants.RESPONSE_SEPARATOR + departmentName;
			}
			catch(DAOException e)
			{
				Logger.out.error("Exception occurred in retrieving Department");
			e.printStackTrace();
			}
		}
		
	    PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
	    /**
		 * sending the response as  departmentId @ departmentName
		 */
	    out.write(responseString);
		
		return null; 
	}

}
