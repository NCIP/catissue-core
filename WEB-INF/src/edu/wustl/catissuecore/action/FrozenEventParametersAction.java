/**
 * <p>Title: FrozenEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the FrozenEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 28, 2005
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
import edu.wustl.catissuecore.dao.FrozenEventParametersBizLogic;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * This class initializes the fields in the FrozenEventParameters Add/Edit webpage.
 * @author mandar deshmukh
 */
public class FrozenEventParametersAction extends Action
{

    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in FrozenEventParameters Add/Edit webpage.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit FrozenEventParameters Page. 
        request.setAttribute(Constants.OPERATION, operation);

        //Sets the minutesList attribute to be used in the Add/Edit FrozenEventParameters Page.
        request.setAttribute("minutesList", Constants.MINUTESARRAY);

        //Sets the hourList attribute to be used in the Add/Edit FrozenEventParameters Page.
        request.setAttribute("hourList", Constants.HOURARRAY);


        try
        {
            AbstractBizLogic dao = BizLogicFactory.getBizLogic(Constants.FROZEN_EVENT_PARAMETERS_FORM_ID);
            ListIterator iterator = null;
            int i;

            FrozenEventParametersBizLogic bizLogic = (FrozenEventParametersBizLogic)BizLogicFactory.getBizLogic(Constants.SITE_FORM_ID);
            
      
        	List userList = bizLogic.retrieve(User.class.getName());
            String[] userArray = new String[userList.size() + 1];
            String[] userIdArray = new String[userList.size() + 1];
            iterator = userList.listIterator();
            
            userArray[0]	= Constants.SELECT_OPTION;
            userIdArray[0]	= "-1";
            
            i = 1;
            while (iterator.hasNext())
            {
                User user = (User) iterator.next();
                userArray[i] = user.getUser().getLastName() + ", " + user.getUser().getFirstName();
                userIdArray[i] = user.getSystemIdentifier().toString();
                i++;
            }
        	
        	request.setAttribute(Constants.USERLIST, userArray);
        	request.setAttribute(Constants.USERIDLIST, userIdArray);

        }
        catch (Exception exc)
        {
            Logger.out.error(exc.getMessage());
        }

        return mapping.findForward(Constants.SUCCESS);
    }
}