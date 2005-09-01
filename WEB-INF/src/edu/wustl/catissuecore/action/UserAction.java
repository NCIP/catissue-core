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

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * This class initializes the fields in the User Add/Edit webpage.
 * @author gautam_shetty
 */
public class UserAction extends Action
{

    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in User Add/Edit webpage.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit User Page. 
        request.setAttribute(Constants.OPERATION, operation);

        //Sets the stateList attribute to be used in the Add/Edit User Page.
        request.setAttribute(Constants.STATELIST, Constants.STATEARRAY);

        //Sets the countryList attribute to be used in the Add/Edit User Page.
        request.setAttribute(Constants.COUNTRYLIST, Constants.COUNTRYARRAY);

        //Sets the pageOf attribute (for Add,Edit or Query Interface)
        String pageOf  = request.getParameter(Constants.PAGEOF);
        request.setAttribute(Constants.PAGEOF,pageOf);

        try
        {
            AbstractBizLogic dao = BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
            ListIterator iterator = null;
            int i;
            
            //Sets the instituteList attribute to be used in the Add/Edit User Page.
            String sourceObjectName = Institution.class.getName();
            String[] displayNameFields = {"name"};
            String valueField = "systemIdentifier";
            
            List instituteList = dao.getList(sourceObjectName, displayNameFields, valueField);
            request.setAttribute(Constants.INSTITUTIONLIST, instituteList);
            
            //Sets the departmentList attribute to be used in the Add/Edit User Page.
            sourceObjectName = Department.class.getName();
            List departmentList = dao.getList(sourceObjectName, displayNameFields, valueField);
            request.setAttribute(Constants.DEPARTMENTLIST, departmentList);
            	
            //Sets the cancerResearchGroupList attribute to be used in the Add/Edit User Page.
            sourceObjectName = CancerResearchGroup.class.getName();
            List cancerResearchGroupList = dao.getList(sourceObjectName, displayNameFields, valueField);
            request.setAttribute(Constants.CANCER_RESEARCH_GROUP_LIST, cancerResearchGroupList);

            if (operation.equals(Constants.EDIT))
            {
                request.setAttribute(Constants.ACTIVITYSTATUSLIST,
                        Constants.ACTIVITY_STATUS_VALUES);
            }
            
            if (!pageOf.equals(Constants.PAGEOF_SIGNUP))
            {
                //Sets the roleList attribute to be used in the Add/Edit User Page.
                Vector roleList = SecurityManager.getInstance(UserAction.class).getRoles();
                
                String[] roleNameArray = new String[roleList.size()+1];
                String[] roleIdArray = new String[roleList.size()+1];
                iterator = roleList.listIterator();
                i = 0;
                roleNameArray[i] = Constants.SELECT_OPTION;
                roleIdArray[i] = String.valueOf(i);
                i++;
                while (iterator.hasNext())
                {
                    Role role = (Role) iterator.next();
                    roleNameArray[i] = role.getName();
                    roleIdArray[i] = String.valueOf(role.getId());
                    i++;
                }
                
                request.setAttribute(Constants.ROLEIDLIST, roleIdArray);
                request.setAttribute(Constants.ROLELIST, roleNameArray);
            }
            
            if (pageOf.equals(Constants.PAGEOF_APPROVE_USER))
            {
                request.setAttribute(Constants.APPROVE_USER_STATUS_LIST,Constants.APPROVE_USER_STATUS_VALUES);
            }
        }
        catch (Exception exc)
        {
            Logger.out.error(exc.getMessage());
        }

        return mapping.findForward(Constants.SUCCESS);
    }
}