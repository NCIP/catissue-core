/**
 * <p>Title: CreateDerivedSpecimenAction Class>
 * <p>Description:	CreateDerivedSpecimenAction populates the fields in the Create Specimen page with information extracted from the order placed.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Feb 01,2007
 */
package edu.wustl.catissuecore.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.RequestDetailsForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

public class CreateDerivedSpecimenAction extends BaseAction
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
		String  parentSpecimenLabelKey = "";
		String requestedClassKey = "";
		String requestedTypeKey = "";
		String requestedQtyKey = "";
		String parentSpecimenIdKey = "";
		//whether request is from request details page or defined array page 
		if(beanName != null && !beanName.equals(""))
		{
			parentSpecimenLabelKey = "DefinedArrayDetailsBean:"+rowNumber+"_requestedItem";
			requestedClassKey = "DefinedArrayDetailsBean:"+rowNumber+"_className";
			requestedTypeKey = "DefinedArrayDetailsBean:"+rowNumber+"_type";
			requestedQtyKey = "DefinedArrayDetailsBean:"+rowNumber+"_requestedQuantity";
			parentSpecimenIdKey = "DefinedArrayDetailsBean:"+rowNumber+"_specimenId";
		}
		else
		{
			parentSpecimenLabelKey = "RequestDetailsBean:"+rowNumber+"_requestedItem";
			requestedClassKey = "RequestDetailsBean:"+rowNumber+"_className";
			requestedTypeKey = "RequestDetailsBean:"+rowNumber+"_type";
			requestedQtyKey = "RequestDetailsBean:"+rowNumber+"_requestedQty";
			parentSpecimenIdKey = "RequestDetailsBean:"+rowNumber+"_specimenId";
		}
		
		Map valuesMap = requestDetailsForm.getValues();		
		//getting the values
		
		String parentSpecimenLabel = (String)valuesMap.get(parentSpecimenLabelKey);	
		String requestedClass = (String)valuesMap.get(requestedClassKey);
		String requestedType = (String)valuesMap.get(requestedTypeKey);
		String requestedQty = ((String)valuesMap.get(requestedQtyKey)).toString();
		//String instanceOf = (String)valuesMap.get("RequestDetailsBean:"+rowNumber+"_instanceOf");
		String parentSpecimenId = ((String)valuesMap.get(parentSpecimenIdKey)).toString();
		
		//Create Specimen Form to populate.
		CreateSpecimenForm createSpecimenForm = (CreateSpecimenForm)form;
		//Setting the values in CreateSpecimenForm
		createSpecimenForm.setParentSpecimenId(parentSpecimenId);
		createSpecimenForm.setParentSpecimenLabel(parentSpecimenLabel);
		createSpecimenForm.setClassName(requestedClass);
		createSpecimenForm.setType(requestedType);
		createSpecimenForm.setQuantity(requestedQty);
		createSpecimenForm.setOperation(Constants.EDIT);
		createSpecimenForm.setReset(false);
		createSpecimenForm.setForwardTo("orderDetails");
		
		return mapping.findForward("derivedSpecimen");
	}
}
