/**
 * <p>Title: SpecimenEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the SpecimenEventParametersAction Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 28, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.Calendar;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.actionForm.SpecimenEventParametersForm;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields in the SpecimenEventParameters Add/Edit webpage.
 * It is a parent class for some Event Parameter Classes.
 * @author mandar deshmukh
 */

public class SpecimenEventParametersAction  extends BaseAction
{
	/**
	 * This method sets all the common parameters for the SpecimenEventParameter pages
	 * @param request HttpServletRequest instance in which the data will be set. 
	 * @throws Exception Throws Exception. Helps in handling exceptions at one common point.
	 */
	private void setCommonRequestParameters(HttpServletRequest request) throws Exception
	{
        //Gets the value of the operation parameter.
       
		String operation = request.getParameter(Constants.OPERATION);
        //Sets the operation attribute to be used in the Add/Edit FrozenEventParameters Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        //Sets the minutesList attribute to be used in the Add/Edit FrozenEventParameters Page.
        request.setAttribute("minutesList", Constants.MINUTES_ARRAY);

        //Sets the hourList attribute to be used in the Add/Edit FrozenEventParameters Page.
        request.setAttribute("hourList", Constants.HOUR_ARRAY);
         
        //The id of specimen of this event.
        String specimenId = request.getParameter(Constants.SPECIMEN_ID); 
        request.setAttribute(Constants.SPECIMEN_ID, specimenId);
        Logger.out.debug("\t\t SpecimenEventParametersAction************************************ : "+specimenId );
        IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
       	UserBizLogic userBizLogic = (UserBizLogic)factory.getBizLogic(Constants.USER_FORM_ID);
    	Collection userCollection =  userBizLogic.getUsers(operation);
    	
    	request.setAttribute(Constants.USERLIST, userCollection);
    	
    	
    	
        	
	}
    
    protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
    	setCommonRequestParameters(request);

    	EventParametersForm eventParametersForm = (EventParametersForm)form;
    	
    	//	 if operation is add
    	if(eventParametersForm.isAddOperation())
    	{
    		if(eventParametersForm.getUserId()==0)
    		{
    			SessionDataBean sessionData = getSessionData(request);
    			if(sessionData!=null && sessionData.getUserId()!=null)
    			{
    				long userId = sessionData.getUserId().longValue();
    				eventParametersForm.setUserId(userId);
    			}
    		}
    		// set the current Date and Time for the event.
			Calendar cal = Calendar.getInstance();
			if(eventParametersForm.getDateOfEvent()==null)
			{
				eventParametersForm.setDateOfEvent(Utility.parseDateToString(cal.getTime(), CommonServiceLocator.getInstance().getDatePattern()));
			}
			if(eventParametersForm.getTimeInHours()==null)
			{
				eventParametersForm.setTimeInHours(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
			}
			if(eventParametersForm.getTimeInMinutes()==null)
			{
				eventParametersForm.setTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
			}
	
    	}
    	else
    	{
    		String specimenId = (String)request.getAttribute(Constants.SPECIMEN_ID);
            if(specimenId == null)
            {
            	SpecimenEventParametersForm sepF =((SpecimenEventParametersForm)eventParametersForm); 
            	specimenId = ""+sepF.getSpecimenId();
            	request.setAttribute(Constants.SPECIMEN_ID, specimenId);
            }
    	}
    	//Changes by Anup
    	
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String pageOf=(String)request.getAttribute(Constants.PAGE_OF);
        eventParametersForm.setPageOf(pageOf);
        eventParametersForm.setOperation(operation);
        
        
		String currentEventParametersDate = "";
		currentEventParametersDate = eventParametersForm.getDateOfEvent();
		if(currentEventParametersDate == null)
		{
			currentEventParametersDate = "";
		}
		
		Integer eventParametersYear = new Integer(AppUtility.getYear(currentEventParametersDate ));
		Integer eventParametersMonth = new Integer(AppUtility.getMonth(currentEventParametersDate ));
		Integer eventParametersDay = new Integer(AppUtility.getDay(currentEventParametersDate ));
		request.setAttribute("eventParametersYear", eventParametersYear);
		request.setAttribute("eventParametersDay", eventParametersDay);
		request.setAttribute("eventParametersMonth", eventParametersMonth);
		request.setAttribute("addForJSP", Constants.ADD);
		request.setAttribute("editForJSP", Constants.EDIT);
		request.setAttribute("currentEventParametersDate", currentEventParametersDate);
		request.setAttribute("userListforJSP", Constants.USERLIST);
		request.setAttribute("pageOf", pageOf);
		// This method will be overridden by the sub classes
		setRequestParameters(request, eventParametersForm);
    	return mapping.findForward((String)request.getParameter(Constants.PAGE_OF));
    }
    
    /*  This method will be overridden by the sub classes
     * It is called from setCommonRequestParameters().
     * It will be used to set the SubClass specific parameters.
     */  
    protected void setRequestParameters(HttpServletRequest request, EventParametersForm eventParametersForm) throws Exception
	{
	}
    
	
}