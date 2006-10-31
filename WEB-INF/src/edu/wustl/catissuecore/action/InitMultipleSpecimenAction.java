package edu.wustl.catissuecore.action;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


/**
 * @author Rahul Ner.
 *
 */
public class InitMultipleSpecimenAction extends Action
{

	/**
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String pageOf = request.getParameter(Constants.PAGEOF);
		
//		*************  ForwardTo implementation *************
		HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");

		if (forwardToHashMap != null)
		{
			String specimenCollectionGroupName = (String) forwardToHashMap
					.get("specimenCollectionGroupName");
			
			if (specimenCollectionGroupName != null)
			{
				//Setting the specimenCollectionGroup 
				request.getSession().setAttribute("specimenCollectionGroupName" , specimenCollectionGroupName);
			}
		}
		//*************  ForwardTo implementation *************

		
    	if (pageOf == null) {
    		pageOf = Constants.SUCCESS;
    	}
    	return mapping.findForward(pageOf);
	}
}
