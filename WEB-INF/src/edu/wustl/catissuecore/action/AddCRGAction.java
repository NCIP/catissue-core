package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CancerResearchGroupForm;
import edu.wustl.catissuecore.bizlogic.CancerResearchBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;


public class AddCRGAction extends CommonAddEditAction
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		String crgName =(String)request.getParameter(Constants.CRG_NAME);
		CancerResearchBizLogic bizlogic = new CancerResearchBizLogic();
		String crgId = null;
		String responseString = null;
		
		CancerResearchGroupForm crgForm = (CancerResearchGroupForm)form;
		crgForm.setOperation(Constants.ADD);
		crgForm.setName(crgName);
		
		ActionForward forward = super.execute(mapping,crgForm,request,response);
		
		if((forward != null) && (forward.getName().equals(Constants.FAILURE)))
		{
			responseString = Utility.getResponseString(request, responseString);
		}
		else
		{
			try
			{
				crgId = bizlogic.getLatestCRG(crgName);
				responseString = crgId + Constants.RESPONSE_SEPARATOR + crgName;
			}
			catch(DAOException e)
			{
				Logger.out.error("Exception occurred in retrieving Cancer Research Group");
			e.printStackTrace();
			}
		}
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		/**
		 * Sending the response as crgId @ crgName
		 */
		out.write(responseString);
		return null;
	}

}
