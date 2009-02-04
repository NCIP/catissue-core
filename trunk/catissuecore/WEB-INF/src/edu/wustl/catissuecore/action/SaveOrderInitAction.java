
/**
 * <p>Title: SaveOrderInitAction Class>
 * <p>Description:This class sets the OrderForm instance into OrderSpecimenForm,OrderBioSpecimenArrayForm
 * and OrderPathologyCaseForm respectively depending on the value of 'typeOf' request parameter.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ramya Nagraj
 * @version 1.00
 * Created on Dec 12, 2006
 */

package edu.wustl.catissuecore.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm;
import edu.wustl.catissuecore.actionForm.OrderForm;
import edu.wustl.catissuecore.actionForm.OrderSpecimenForm;
import edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm;
import edu.wustl.catissuecore.util.global.Constants;

import edu.wustl.common.action.BaseAction;

public class SaveOrderInitAction extends BaseAction
{

	/**
	 * @param mapping ActionMapping object
	 * @param form ActionForm object
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @return ActionForward object
	 * @throws Exception object
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession session = request.getSession();
		String temp=request.getParameter("typeOf");
		//for specimen
        if(session.getAttribute("OrderForm")!=null)
        {
			if(temp.equals(Constants.SPECIMEN_ORDER_FORM_TYPE))
			{
				OrderSpecimenForm orderSpecimenFormObject = (OrderSpecimenForm)form;
				//Obtain OrderForm instance from the session.
				OrderForm orderFrom = (OrderForm) session.getAttribute("OrderForm");
				orderSpecimenFormObject.setOrderForm(orderFrom);
				orderSpecimenFormObject.setPageOf("specimen");
				List arrayFormObj=null;
				if(session.getAttribute("DefineArrayFormObjects")!=null)
				{
					arrayFormObj=(List)session.getAttribute("DefineArrayFormObjects");
				}
				orderSpecimenFormObject.setDefineArrayObj(arrayFormObj);
			}
			
			if(temp.equals(Constants.ARRAY_ORDER_FORM_TYPE))
			{
				OrderBiospecimenArrayForm orderArrayFormObject = (OrderBiospecimenArrayForm) form;
				//Obtain OrderForm instance from the session.
				OrderForm orderFrom = (OrderForm) session.getAttribute("OrderForm");
				orderArrayFormObject.setOrderForm(orderFrom);
				orderArrayFormObject.setPageOf("specimenArray");
				List arrayFormObj=null;
				if(session.getAttribute("DefineArrayFormObjects")!=null)
				{
					arrayFormObj=(List)session.getAttribute("DefineArrayFormObjects");
				}
				orderArrayFormObject.setDefineArrayObj(arrayFormObj);
			}
			
			if(temp.equals(Constants.PATHOLOGYCASE_ORDER_FORM_TYPE))
			{
				OrderPathologyCaseForm pathologyObject = (OrderPathologyCaseForm) form;
				//Obtain OrderForm instance from the session.
				OrderForm orderFrom = (OrderForm) session.getAttribute("OrderForm");
				pathologyObject.setOrderForm(orderFrom);
				pathologyObject.setPageOf("pathologyCase");
				List arrayFormObj=null;
				if(session.getAttribute("DefineArrayFormObjects")!=null)
				{
					arrayFormObj=(List)session.getAttribute("DefineArrayFormObjects");
				}
				pathologyObject.setDefineArrayObj(arrayFormObj);
			}
			return mapping.findForward("success");
        }
        else
        	return mapping.findForward("failure");
    }
}
