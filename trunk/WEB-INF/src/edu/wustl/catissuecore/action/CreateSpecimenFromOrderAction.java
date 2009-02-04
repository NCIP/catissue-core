/**
 * <p>Title: CreateSpecimenFromOrderAction Class>
 * <p>Description:	CreateSpecimenFromOrderAction populates the fields in the New Specimen page with information extracted from the order placed.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Feb 05,2007
 */
package edu.wustl.catissuecore.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.RequestDetailsForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

public class CreateSpecimenFromOrderAction extends BaseAction
{
	/**
	 * @param mapping object
	 * @param form object
	 * @param request object
	 * @param response object
	 * @return ActionForward object
	 * @throws Exception object
	 */	
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession session = request.getSession();
		RequestDetailsForm requestDetailsForm = (RequestDetailsForm)session.getAttribute("REQUEST_DETAILS_FORM");		
		String rowNumber = request.getParameter("rowNumber");
		
		String beanName = request.getParameter("bean");		
		//Keys
		
		String requestedClassKey = "";
		String requestedTypeKey = "";
		String requestedQtyKey = "";
		String specimenCollGrpIdKey = "";
		//whether request is from request details page or defined array page 
		if(beanName != null && !beanName.equals(""))
		{
			requestedClassKey = "DefinedArrayDetailsBean:"+rowNumber+"_className";
			requestedTypeKey = "DefinedArrayDetailsBean:"+rowNumber+"_type";
			requestedQtyKey = "DefinedArrayDetailsBean:"+rowNumber+"_requestedQuantity";
			specimenCollGrpIdKey = "DefinedArrayDetailsBean:"+rowNumber+"_specimenCollGroupId";
		}
		else
		{
			requestedClassKey = "RequestDetailsBean:"+rowNumber+"_className";
			requestedTypeKey = "RequestDetailsBean:"+rowNumber+"_type";
			requestedQtyKey = "RequestDetailsBean:"+rowNumber+"_requestedQty";
			specimenCollGrpIdKey = "RequestDetailsBean:"+rowNumber+"_specimenCollGroupId";
		}
		
		Map valuesMap = requestDetailsForm.getValues();	
		//getting the values
		String requestedClass = (String)valuesMap.get(requestedClassKey);
		String requestedType = (String)valuesMap.get(requestedTypeKey);
		String requestedQty = ((String)valuesMap.get(requestedQtyKey)).toString();
		String specimenCollGrpId = ((String)valuesMap.get(specimenCollGrpIdKey)).toString();
		
//		New Specimen Form to populate.
		NewSpecimenForm newSpecimenForm = (NewSpecimenForm)form;
		//Setting the values in CreateSpecimenForm
		newSpecimenForm.setSpecimenCollectionGroupId(specimenCollGrpId);
		
		newSpecimenForm.setClassName(requestedClass);
		newSpecimenForm.setType(requestedType);
		newSpecimenForm.setQuantity(requestedQty);
		
		newSpecimenForm.setOperation(Constants.EDIT);		
		newSpecimenForm.setForwardTo("orderDetails");
		
		return mapping.findForward("pathological");
	}
}
