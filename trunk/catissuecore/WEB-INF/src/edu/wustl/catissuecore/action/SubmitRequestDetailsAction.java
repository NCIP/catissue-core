/**
 * <p>Title: SubmitRequestDetailsAction Class>
 * <p>Description:	This class submits the fields of RequestDetails.jsp Page in the db</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Oct 06,2006
 */

package edu.wustl.catissuecore.action;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.RequestDetailsForm;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;

public class SubmitRequestDetailsAction extends BaseAction
{

	/**
	 * Overrides the execute method of Action class.
	 * Submits the various fields in RequestDetails.jsp Page.
	 * @param mapping object
	 * @param form object
	 * @param request object
	 * @param response object
	 * @return ActionForward object
	 * @throws Exception object
	 * */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		RequestDetailsForm requestDetailsForm = (RequestDetailsForm) form;
		
		Map keysMap = requestDetailsForm.getValues();
		Iterator iterator = keysMap.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = (String) iterator.next();
			Logger.out.debug("Key is: " + key + " Value is: " + keysMap.get(key));			
		}
		Logger.out.debug("Update Status Selected is: "+requestDetailsForm.getStatus());	
		
	
		
		
		
		
		return mapping.findForward("success");
	}

	
}