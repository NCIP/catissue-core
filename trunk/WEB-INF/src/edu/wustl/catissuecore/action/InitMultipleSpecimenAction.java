package edu.wustl.catissuecore.action;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;


/**
 * @author Rahul Ner.
 *
 */
public class InitMultipleSpecimenAction extends SecureAction
{

	/**
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
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
			String specimenCollectionGroupId = (String) forwardToHashMap
			.get("specimenCollectionGroupId");
			if(specimenCollectionGroupId != null)
			{
				request.getSession().setAttribute("specimenCollectionGroupId" , specimenCollectionGroupId);
			}
			
		}
		//*************  ForwardTo implementation *************

		
    	if (pageOf == null) {
    		pageOf = Constants.SUCCESS;
    		
    	}
    	request.setAttribute("pageOf",pageOf);
    	return mapping.findForward(pageOf);
	}
}
