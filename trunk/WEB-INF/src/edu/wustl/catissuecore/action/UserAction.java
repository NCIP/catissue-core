/**
 * <p>Title: UserAction Class>
 * <p>Description:	This class initializes the fields in the User Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 22, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * This class initializes the fields in the User Add/Edit webpage.
 * @author gautam_shetty
 */
public class UserAction extends SecureAction
{

    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in User Add/Edit webpage.
     * */
    protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit User Page. 
        request.setAttribute(Constants.OPERATION, operation);

        //Sets the countryList attribute to be used in the Add/Edit User Page.
        List countryList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COUNTRY_LIST,null);
        request.setAttribute(Constants.COUNTRYLIST, countryList);
        
        //Sets the stateList attribute to be used in the Add/Edit User Page.
        List stateList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_STATE_LIST,null);
        request.setAttribute(Constants.STATELIST, stateList);
        
        
        //Sets the pageOf attribute (for Add,Edit or Query Interface).
        String pageOf  = request.getParameter(Constants.PAGEOF);
        request.setAttribute(Constants.PAGEOF,pageOf);
        String target = pageOf;
        
        IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
        
        //Sets the instituteList attribute to be used in the Add/Edit User Page.
        String sourceObjectName = Institution.class.getName();
        String[] displayNameFields = {Constants.NAME};
        String valueField = Constants.SYSTEM_IDENTIFIER;
        
        List instituteList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, false);
        request.setAttribute(Constants.INSTITUTIONLIST, instituteList);
        
        //Sets the departmentList attribute to be used in the Add/Edit User Page.
        sourceObjectName = Department.class.getName();
        List departmentList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, false);
        request.setAttribute(Constants.DEPARTMENTLIST, departmentList);
        	
        //Sets the cancerResearchGroupList attribute to be used in the Add/Edit User Page.
        sourceObjectName = CancerResearchGroup.class.getName();
        List cancerResearchGroupList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, false);
        request.setAttribute(Constants.CANCER_RESEARCH_GROUP_LIST, cancerResearchGroupList);
        
        //Populate the activity status dropdown if the operation is edit 
        //and the user page is of administrative tab.
        if (operation.equals(Constants.EDIT) && pageOf.equals(Constants.PAGEOF_USER_ADMIN))
        {
            request.setAttribute(Constants.ACTIVITYSTATUSLIST,
                    			 Constants.USER_ACTIVITY_STATUS_VALUES);
        }
        
        //Populate the role dropdown if the page is of approve user or (Add/Edit) user page of adminitraive tab. 
        if (pageOf.equals(Constants.PAGEOF_APPROVE_USER) || pageOf.equals(Constants.PAGEOF_USER_ADMIN) ||
        		pageOf.equals(Constants.PAGEOF_USER_PROFILE ))
        {
            List roleNameValueBeanList = getRoles();
            
            request.setAttribute(Constants.ROLELIST, roleNameValueBeanList);
        }
        
        //Populate the status dropdown for approve user page.(Approve,Reject,Pending)
        if (pageOf.equals(Constants.PAGEOF_APPROVE_USER))
        {
            request.setAttribute(Constants.APPROVE_USER_STATUS_LIST,Constants.APPROVE_USER_STATUS_VALUES);
        }
        
        Logger.out.debug("pageOf :---------- "+ pageOf );
        
        // ------------- add new
        String reqPath = request.getParameter(Constants.REQ_PATH);
        
		request.setAttribute(Constants.REQ_PATH, reqPath);
        
        AbstractActionForm aForm = (AbstractActionForm )form;
        if(reqPath != null && aForm !=null )
        	aForm.setRedirectTo(reqPath);
        
        Logger.out.debug("USerAction redirect :---------- "+ reqPath  );
            
        return mapping.findForward(target);
    }
    
    /**
     * Returns a list of all roles that can be assigned to a user.
     * @return a list of all roles that can be assigned to a user.
     * @throws SMException
     */
    private List getRoles() throws SMException
    {
        //Sets the roleList attribute to be used in the Add/Edit User Page.
        Vector roleList = SecurityManager.getInstance(UserAction.class).getRoles();
        
        ListIterator iterator = roleList.listIterator();
        
        List roleNameValueBeanList = new ArrayList();
        NameValueBean nameValueBean = new NameValueBean();
        nameValueBean.setName(Constants.SELECT_OPTION);
        nameValueBean.setValue(String.valueOf(Constants.SELECT_OPTION_VALUE));
        roleNameValueBeanList.add(nameValueBean);
        
        while (iterator.hasNext())
        {
            Role role = (Role) iterator.next();
            nameValueBean = new NameValueBean();
            nameValueBean.setName(role.getName());
            nameValueBean.setValue(String.valueOf(role.getId()));
            roleNameValueBeanList.add(nameValueBean);
        }
        return roleNameValueBeanList;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.action.SecureAction#isAuthorizedToExecute(javax.servlet.http.HttpServletRequest)
     */
    protected boolean isAuthorizedToExecute(HttpServletRequest request)
            throws Exception
    {
        String pageOf  = request.getParameter(Constants.PAGEOF);
        if(pageOf.equals(Constants.PAGEOF_USER_ADMIN))
        {
            return super.isAuthorizedToExecute(request);
        }
        return true;
    }
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.action.BaseAction#getSessionData(javax.servlet.http.HttpServletRequest)
     */
    protected SessionDataBean getSessionData(HttpServletRequest request)
    {
        String pageOf  = request.getParameter(Constants.PAGEOF);
        if(pageOf.equals(Constants.PAGEOF_USER_ADMIN))
        {
            return super.getSessionData(request);
        }
        return new SessionDataBean();
    }
}