/**
 * <p>Title: UpdateAvailableQuantity Class>
 * <p>Description:	Ajax Action Class for updated available quantity.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Nov 17,2006
 */
package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.action.BaseAction;


/**
 * @author ashish_gupta
 *
 */
public class UpdateAvailableQuantity extends BaseAction
{

	 /**
     * Overrides the execute method in Action class.
     * @param mapping ActionMapping object
     * @param form ActionForm object
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @return ActionForward object
     * @throws Exception object
     */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//The selected specimen Id.
		String specimenId = (String)request.getParameter("selectedSpecimen");
		//The row number.
		String finalSpecimenListId = (String)request.getParameter("finalSpecimenListId");
		//The list containing Specimen Objects.
		List specimenList = (ArrayList)request.getSession().getAttribute("finalSpecimenList");
		
		Iterator iter = specimenList.iterator();
		String quantity = "";
		while(iter.hasNext())
		{
			Specimen specimen = (Specimen)iter.next();
			if(specimen.getId().compareTo(new Long(specimenId)) == 0)
			{
				quantity = specimen.getAvailableQuantity().getValue().toString();
				break;
			}				
		}		
		//Writing to response
		PrintWriter out = response.getWriter();		
		out.print(quantity);
		
		return null;
	}

}
