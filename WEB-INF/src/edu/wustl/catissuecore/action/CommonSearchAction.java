/**
 * <p>Title: CommonSearchAction Class>
 * <p>Description:	This class is used to retrieve the information whose record
 * 					is to be modified.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 20, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.dao.AbstractBizLogic;
import edu.wustl.catissuecore.dao.BizLogicFactory;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


/**
 * This class is used to retrieve the information whose record is to be modified.
 * @author gautam_shetty
 */
public class CommonSearchAction extends Action
{
    /**
     * Overrides the execute method of Action class.
     * Retrieves and populates the information to be edited. 
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServerException
    {
        /** 
         * Represents whether the search operation was successful or not.
         */
        String target = null;
        
        AbstractActionForm abstractForm = (AbstractActionForm) form;

        /* Get the systemIdentifier whose information is to be searched */
        long identifier = abstractForm.getSystemIdentifier();

        try
        {
            AbstractBizLogic dao = BizLogicFactory.getDAO(abstractForm.getFormId());
            AbstractDomainObject abstractDomain = null;
            List list = null;
            
            //Retrieves the information to be edited.
            String objName = AbstractDomainObject.getDomainObjectName(abstractForm.getFormId());
            list= dao.retrieve(objName,Constants.IDENTIFIER, new Long(identifier).toString());

            if (list.size() != 0)
            {
                /* If the record searched is present in the database,
                 * populate the formbean with the information retrieved.  
                 */
                abstractDomain = (AbstractDomainObject)list.get(0);
                abstractForm.setAllValues(abstractDomain);
                request.setAttribute(mapping.getAttribute(),form);
                target = new String(Constants.SUCCESS);
            }
            else
            {
                /* If the searched record is not present in the database,  
                 * display an Error message.
                 */
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.unknown",ApplicationProperties.getValue("user.systemIdentifier")));
                saveErrors(request,errors);
                target = new String(Constants.FAILURE);
            }
        }
        catch (DAOException excp)
        {
            target = new String(Constants.FAILURE);
            Logger.out.error(excp.getMessage());
        }
        return (mapping.findForward(target));
    }
}
