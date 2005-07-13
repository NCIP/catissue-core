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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.dao.AbstractBizLogic;
import edu.wustl.catissuecore.dao.BizLogicFactory;
import edu.wustl.catissuecore.domain.ActivityStatus;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institute;
import edu.wustl.catissuecore.domain.Role;
import edu.wustl.catissuecore.util.global.Constants;

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

        //Sets the cancerResearchGroupList attribute to be used in the Add/Edit User Page.
        request.setAttribute(Constants.CANCER_RESEARCH_GROUP_LIST, Constants.COUNTRYARRAY);

        try
        {
            AbstractBizLogic dao = BizLogicFactory.getDAO(Constants.USER_FORM_ID);
            ListIterator iterator = null;
            int i;
//            if (operation.equals(Constants.EDIT))
//            {
                //Sets the roleList attribute to be used in the Add/Edit User Page.
                List roleList = dao.retrieve(Role.class.getName());
                String[] roleArray = new String[roleList.size()];
                iterator = roleList.listIterator();
                i = 0;
                while (iterator.hasNext())
                {
                    Role role = (Role) iterator.next();
                    roleArray[i] = role.getName();
                    i++;
                }
                request.setAttribute(Constants.ROLELIST, roleArray);
//            }

            //Sets the instituteList attribute to be used in the Add/Edit User Page.
            List instituteList = dao.retrieve(Institute.class.getName());
            String[] instituteArray = new String[instituteList.size()];
            iterator = instituteList.listIterator();
            i = 0;
            while (iterator.hasNext())
            {
                Institute institute = (Institute) iterator.next();
                instituteArray[i] = institute.getName();
                i++;
            }
            request.setAttribute(Constants.INSTITUTELIST, instituteArray);

            //Sets the departmentList attribute to be used in the Add/Edit User Page.
            List departmentList = dao.retrieve(Department.class.getName());
            String[] departmentArray = new String[departmentList.size()];
            iterator = departmentList.listIterator();
            i = 0;
            while (iterator.hasNext())
            {
                Department department = (Department) iterator.next();
                departmentArray[i] = department.getName();
                i++;
            }
            request.setAttribute(Constants.DEPARTMENTLIST, departmentArray);

            departmentList = dao.retrieve(ActivityStatus.class.getName());
            String[] activityStatusArray = new String[departmentList.size()];
            iterator = departmentList.listIterator();
            i = 0;
            while (iterator.hasNext())
            {
                ActivityStatus activityStatus = (ActivityStatus) iterator
                        .next();
                activityStatusArray[i] = activityStatus.getStatus();
                i++;
            }
            request.setAttribute(Constants.ACTIVITYSTATUSLIST,
                    activityStatusArray);
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
//            Logger.out.error(exc.getMessage());
        }

        return mapping.findForward(Constants.SUCCESS);
    }
}