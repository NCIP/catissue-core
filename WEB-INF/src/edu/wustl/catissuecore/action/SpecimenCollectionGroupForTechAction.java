/**
 * <p>Title: SpecimenCollectionGroupAction Class>
 * <p>Description:	SpecimenCollectionGroupAction initializes the fields in the 
 * New Specimen Collection Group page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

/**
 * @author vaishali_khandelwal
 */
public class SpecimenCollectionGroupForTechAction extends BaseAction
{
	/**
	 * Overrides the execute method of Action class.
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		//Accessing specimen collection group id and name 
		String id = request.getParameter("id");
		String name = request.getParameter("name"); 
		if(id!= null )
		{
			//setting id in request to show SCG selected in specimen page
			request.setAttribute(Constants.SPECIMEN_COLLECTION_GROUP_ID,id);
			//setting name in session to show SCG selected in multiple specimen page
			request.getSession().setAttribute(Constants.SPECIMEN_COLL_GP_NAME,name);
			request.getSession().setAttribute("specimenCollectionGroupId" , id);
			return mapping.findForward(Constants.SUCCESS);
		}
		else
		{
			return mapping.findForward(Constants.FAILURE);
		}

	}
}