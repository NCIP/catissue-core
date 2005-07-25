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

import edu.wustl.catissuecore.dao.AbstractBizLogic;
import edu.wustl.catissuecore.dao.BizLogicFactory;
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
        String pageName  = request.getParameter(Constants.PAGEOF);
        request.setAttribute(Constants.PAGEOF,pageName);

        try
        {
            AbstractBizLogic dao = BizLogicFactory.getDAO(Constants.USER_FORM_ID);
            ListIterator iterator = null;
            int i;
            
            //Sets the instituteList attribute to be used in the Add/Edit User Page.
            List instituteList = dao.retrieve(Institution.class.getName());
            String[] instituteArray = new String[instituteList.size()+1];
            iterator = instituteList.listIterator();
            i = 0;
            instituteArray[i++] = Constants.SELECT_OPTION;
            while (iterator.hasNext())
            {
                Institution institute = (Institution) iterator.next();
                instituteArray[i] = institute.getName();
                i++;
            }
            request.setAttribute(Constants.INSTITUTIONLIST, instituteArray);

            //Sets the departmentList attribute to be used in the Add/Edit User Page.
            List departmentList = dao.retrieve(Department.class.getName());
            String[] departmentArray = new String[departmentList.size()+1];
            iterator = departmentList.listIterator();
            i = 0;
            departmentArray[i++] = Constants.SELECT_OPTION;
            while (iterator.hasNext())
            {
                Department department = (Department) iterator.next();
                departmentArray[i] = department.getName();
                i++;
            }
            request.setAttribute(Constants.DEPARTMENTLIST, departmentArray);

            List cancerResearchGroupList = dao.retrieve(CancerResearchGroup.class.getName());
            String[] cancerResearchGroupArray = new String[cancerResearchGroupList.size()+1];
            iterator = cancerResearchGroupList.listIterator();
            i = 0;
            cancerResearchGroupArray[i++] = Constants.SELECT_OPTION;
            while (iterator.hasNext())
            {
                CancerResearchGroup cancerResearchGroup = 
                    				(CancerResearchGroup) iterator.next();
                cancerResearchGroupArray[i] = cancerResearchGroup.getName();
                i++;
            }
            
            //Sets the cancerResearchGroupList attribute to be used in the Add/Edit User Page.
            request.setAttribute(Constants.CANCER_RESEARCH_GROUP_LIST, 
                    cancerResearchGroupArray);
            
            request.setAttribute(Constants.ACTIVITYSTATUSLIST,
                    Constants.ACTIVITY_STATUS_VALUES);
            
            if (operation.equals(Constants.EDIT))
            {
                //Sets the roleList attribute to be used in the Add/Edit User Page.
                Vector roleList = SecurityManager.getInstance(UserAction.class).getRoles();
                
                String[] roleNameArray = new String[roleList.size()];
                String[] roleIdArray = new String[roleList.size()];
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
        }
        catch (Exception exc)
        {
            Logger.out.error(exc.getMessage());
        }

        return mapping.findForward(Constants.SUCCESS);
    }
}