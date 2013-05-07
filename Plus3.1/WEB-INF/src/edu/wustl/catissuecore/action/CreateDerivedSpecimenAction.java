/**
 * <p>Title: CreateDerivedSpecimenAction Class>
 * <p>Description:	CreateDerivedSpecimenAction populates the fields
 *  in the Create Specimen page with information extracted from the order placed.</p>
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
import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;

/**
 * @author renuka_bajpai
 *
 */
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
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final HttpSession session = request.getSession();
		final RequestDetailsForm requestDetailsForm = (RequestDetailsForm) session
				.getAttribute("REQUEST_DETAILS_FORM");
		final String rowNumber = request.getParameter("rowNumber");
		final String specimenId = request.getParameter("specimenId");
		final String beanName = request.getParameter("bean");

		String parentSpecimenLabelKey = "";
		String requestedClassKey = "";
		String requestedTypeKey = "";
		String requestedQtyKey = "";
		String parentSpecimenIdKey = "";
		String parentSpecimenLabel = "";
		String requestedClass = "";
		String requestedType = "";
		String requestedQty = "";
		String parentSpecimenId = "";

		//whether request is from request details page or defined array page
		if (specimenId != null)
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
					.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
			final Specimen specimen = orderBizLogic.getSpecimenObject(Long.parseLong(specimenId));
			parentSpecimenLabel = specimen.getLabel();
			requestedClass = specimen.getClassName();
			requestedType = specimen.getSpecimenType();
			requestedQty = specimen.getAvailableQuantity().toString();
			parentSpecimenId = specimen.getId().toString();

		}
		else
		{
			if (beanName != null && !beanName.equals(""))
			{
				parentSpecimenLabelKey = "DefinedArrayDetailsBean:" + rowNumber + "_requestedItem";
				requestedClassKey = "DefinedArrayDetailsBean:" + rowNumber + "_className";
				requestedTypeKey = "DefinedArrayDetailsBean:" + rowNumber + "_type";
				requestedQtyKey = "DefinedArrayDetailsBean:" + rowNumber + "_requestedQuantity";
				parentSpecimenIdKey = "DefinedArrayDetailsBean:" + rowNumber + "_specimenId";
			}
			else
			{
				parentSpecimenLabelKey = "RequestDetailsBean:" + rowNumber + "_requestedItem";
				requestedClassKey = "RequestDetailsBean:" + rowNumber + "_className";
				requestedTypeKey = "RequestDetailsBean:" + rowNumber + "_type";
				requestedQtyKey = "RequestDetailsBean:" + rowNumber + "_requestedQty";
				parentSpecimenIdKey = "RequestDetailsBean:" + rowNumber + "_specimenId";
			}
			final Map valuesMap = requestDetailsForm.getValues();
			parentSpecimenLabel = (String) valuesMap.get(parentSpecimenLabelKey);
			requestedClass = (String) valuesMap.get(requestedClassKey);
			requestedType = (String) valuesMap.get(requestedTypeKey);
			requestedQty = (String) valuesMap.get(requestedQtyKey);
			parentSpecimenId = ((String) valuesMap.get(parentSpecimenIdKey)).toString();

		}

		//Create Specimen Form to populate.
		final CreateSpecimenForm createSpecimenForm = (CreateSpecimenForm) form;
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
