package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class LoadParticipantPage extends Action
{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception
	{
		return mapping.findForward("success");
	}


}
