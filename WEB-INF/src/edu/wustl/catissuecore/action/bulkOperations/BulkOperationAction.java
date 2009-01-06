package edu.wustl.catissuecore.action.bulkOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.BulkEventOperationsForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.SpecimenEventParametersBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;

public abstract class BulkOperationAction extends SecureAction
{

	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// Get Specimen Ids from request. If not there then get all from cart
		List specimenIds = (List) request.getAttribute(Constants.SPECIMEN_ID);
		if(specimenIds == null || specimenIds.size()==0)
		{
			specimenIds = getSpecimenIds(request);
		}
		
		//Set common request parameters for all events
		setCommonRequestParameters(request, specimenIds);

		BulkEventOperationsForm eventParametersForm = (BulkEventOperationsForm)form;
		//Set operation
		String operation = (String) request.getAttribute(Constants.OPERATION);
		eventParametersForm.setOperation(operation);
		
		//Set current user
		SessionDataBean sessionData = getSessionData(request);
		if(sessionData!=null && sessionData.getUserId()!=null)
		{
			long userId = sessionData.getUserId().longValue();
			eventParametersForm.setUserId(userId);
		}
		// set the current Date and Time for the event.
		Calendar cal = Calendar.getInstance();
		eventParametersForm.setDateOfEvent(Utility.parseDateToString(cal.getTime(), Variables.dateFormat));
		eventParametersForm.setTimeInHours(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
		eventParametersForm.setTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
		
		//Set event specific request params
		setEventSpecificRequestParameters(eventParametersForm,request, specimenIds);
		
		
		if(specimenIds == null || specimenIds.size()==0)
		{
			ActionErrors errors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("specimen.cart.size.zero");
			errors.add(ActionErrors.GLOBAL_ERROR,error);
	        saveErrors(request,errors);
			return mapping.findForward(Constants.FAILURE);
		}
		if(operation!=null)
		{
			return mapping.findForward(operation);
		}
		return mapping.findForward(Constants.SUCCESS);
	}
	
	/**
	 * This method sets all the common parameters for the SpecimenEventParameter pages
	 * @param request HttpServletRequest instance in which the data will be set. 
	 * @throws Exception Throws Exception. Helps in handling exceptions at one common point.
	 */
	private void setCommonRequestParameters(HttpServletRequest request, List specimenIds) throws Exception
	{
        //Set the minutesList attribute 
        request.setAttribute(Constants.MINUTES_LIST, Constants.MINUTES_ARRAY);

        //Set the hourList attribute 
        request.setAttribute(Constants.HOUR_LIST, Constants.HOUR_ARRAY);
        
        //Set User List
       	UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
    	Collection userCollection =  userBizLogic.getUsers(Constants.ADD);
    	request.setAttribute(Constants.USERLIST, userCollection);
    	
    	//Set Specimen Ids
    	request.setAttribute(Constants.SPECIMEN_ID_LIST, specimenIds);
	}
	
	private void setEventSpecificRequestParameters(BulkEventOperationsForm eventParametersForm,HttpServletRequest request, List specimenIds)throws Exception
	{
		
		if(specimenIds != null && specimenIds.size()>0)
		{
			SpecimenEventParametersBizLogic bizlogic = (SpecimenEventParametersBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.BULK_OPERATIONS_FORM_ID);
			System.out.println("");
			List specimenDataList = bizlogic.getSpecimenDataForBulkOperations(eventParametersForm.getOperation(),specimenIds, getSessionData(request));
			List specimenRow = null;
			String specimenId = null;
			for(int i=0; i<specimenDataList.size();i++)
			{
				specimenRow = (List) specimenDataList.get(i);
				specimenId = specimenRow.get(0).toString();
				fillFormData(eventParametersForm, specimenRow, specimenId, request);
			}
		}
	}
	
	/*
	 * This method returns all specimen ids in cart
	 */
	private List getSpecimenIds(HttpServletRequest request)
	{
		List specimenIds = new ArrayList();
		QueryShoppingCart cart = (QueryShoppingCart) request.getSession()
		.getAttribute(Constants.QUERY_SHOPPING_CART);
		if(cart != null)
		{
			QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
			specimenIds = new ArrayList<String>(bizLogic.getEntityIdsList(cart, Arrays.asList(Constants.specimenNameArray), null));
			if(specimenIds == null)
			{
				specimenIds = new ArrayList();
			}
		}
		return specimenIds;
	}
	
	protected abstract void fillFormData(BulkEventOperationsForm eventParametersForm, List specimenRow, String specimenId, HttpServletRequest request);

}
