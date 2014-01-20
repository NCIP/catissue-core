package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.action.SecureAction;

public abstract class CatissueBaseAction extends SecureAction 
{

	@Override
	protected ActionForward executeSecureAction(ActionMapping arg0,
			ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3)
			throws Exception 		
	{
		arg2.getSession().setAttribute("displayMsg", "false");
		saveToken(arg2);
		return executeCatissueAction(arg0, arg1, arg2, arg3);
	}
	
	protected abstract ActionForward executeCatissueAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception;

}
