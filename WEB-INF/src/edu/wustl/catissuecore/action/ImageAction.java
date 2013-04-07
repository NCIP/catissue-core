package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;

public class ImageAction extends CatissueBaseAction 
{

	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		request.setAttribute("isImagingConfigurred", Variables.isImagingConfigurred);
		return mapping.findForward(Constants.SUCCESS);
	}


}
