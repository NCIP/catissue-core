/**
 * <p>Title: SiteAction Class>
 * <p>Description:	This class initializes the fields of the Site Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SiteForm;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields of the Site Add/Edit webpage.
 * @author aniruddha_phadnis
 */
public class SiteAction  extends SecureAction
{

	private transient Logger logger = Logger.getCommonLogger(SiteAction.class);
    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in Site Add/Edit webpage.
     * */
    protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	SiteForm siteForm = (SiteForm )form;

        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit User Page.
           
        String pageOf = (String) request.getParameter(Constants.PAGE_OF);
        String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
        
        siteForm.setSubmittedFor(submittedFor);
        siteForm.setOperation(operation);
        String formName;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.SITE_EDIT_ACTION;
        }
        else
        {
            formName = Constants.SITE_ADD_ACTION;
        }
        request.setAttribute("formName", formName);
        request.setAttribute("operationAdd", Constants.ADD);
    	request.setAttribute("operationEdit", Constants.EDIT);
    	request.setAttribute("operationForActivityStatus",Constants.OPERATION);
        
        //Sets the countryList attribute to be used in the Add/Edit User Page.
        List countryList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COUNTRY_LIST,null);
        request.setAttribute(Constants.COUNTRYLIST, countryList);
        
        //Sets the stateList attribute to be used in the Add/Edit User Page.
        List stateList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_STATE_LIST,null);
        request.setAttribute(Constants.STATELIST, stateList);
        

        //Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
        request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.SITE_ACTIVITY_STATUS_VALUES);
        
        //Sets the siteTypeList attribute to be used in the Site Add/Edit Page.
        List siteList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SITE_TYPE,null);
        request.setAttribute(Constants.SITETYPELIST, siteList);
        IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
    	UserBizLogic userBizLogic = (UserBizLogic)factory.getBizLogic(Constants.USER_FORM_ID);
    	Collection coll =  userBizLogic.getUsers(operation);
    	request.setAttribute(Constants.USERLIST, coll);
    	
    	// ------------------------------------------------------------------
    	
    	
    	
    	boolean isOnChange = getIsOnChange(request);  
 		Long coordinatorId = getCoordinatorId(request);
	
 		if(siteForm != null && isOnChange && coordinatorId!=null)
    	{
    	    String emailAddress ="";
    	    String street="";
    	    String city="";
    	    String state="";
    	    String country="";
    	    String zipCode="";
    	    String phoneNo="";
    	    
    	    
    	    List userList = userBizLogic.retrieve(User.class.getName(),Constants.SYSTEM_IDENTIFIER , coordinatorId);
    	    
    	    if(userList.size()>0)
    	    {
    	    	User user = (User)userList.get(0); 	
    	    	if (user != null)
        		{
        		    emailAddress = user.getEmailAddress() ; 
        		    logger.debug("Email Id of Coordinator of Site : " + emailAddress );
    
        		    siteForm.setEmailAddress(emailAddress);
           		    if(user.getAddress()!=null)
        		    {	
        		  
        		    	street=(String)user.getAddress().getStreet();
        		      	siteForm.setStreet(street);
        		    	
        		    	city=(String)user.getAddress().getCity();
        		     	siteForm.setCity(city);
        		     	
        		     	state=(String)user.getAddress().getState();
        		     	siteForm.setState(state);
        		     	
        		     	country=(String)user.getAddress().getCountry();
        		     	siteForm.setCountry(country);
        		     
        		     	zipCode=(String)user.getAddress().getZipCode();
        		     	siteForm.setZipCode(zipCode);
        		     	
        		     	phoneNo=(String)user.getAddress().getPhoneNumber();
        		     	siteForm.setPhoneNumber(phoneNo);
        		     
        		    }
        		    
        		}
    	    }
		}
 		
 		return mapping.findForward(pageOf);
    }


    
    
    /**
	 * method for getting coordinatorId from request
	 * @param request :object of HttpServletResponse
	 * @return coordinatorId
	 */
	private Long getCoordinatorId(HttpServletRequest request)
	{
		Long coordinatorId = null;
		String coordinatorIdStr = request.getParameter("coordinatorId");
		Validator validator = new Validator(); 
		try
		{
		if(!validator.isEmpty(coordinatorIdStr))
		{
			coordinatorId = new Long(coordinatorIdStr);
		}
		}
		catch(Exception e)
		{
			logger.debug(e.getMessage(), e);
			coordinatorId = null;
		}
		return coordinatorId;
	}
	
	/**
	 * method for getting isOnChange from request
	 * @param request:object of HttpServletResponse
	 * @return isOnChange :boolean 
	 */
	private boolean getIsOnChange(HttpServletRequest request)
	{
    	boolean isOnChange=false;
		String str = request.getParameter("isOnChange");
		if(str!=null && str.equals("true"))
		{
			isOnChange = true;
		}
		return isOnChange;
	}

	/**** code for ajax *****/

}