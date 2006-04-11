/**
 * <p>Title: CommonSearchAction Class>
 * <p>Description:	This class is used to retrieve the information whose record
 * is to be modified.</p>
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

import edu.wustl.common.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.DomainObjectFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
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
        Logger.out.debug("*******In here************");
        /** 
         * Represents whether the search operation was successful or not.
         */
        String target = null;
        
        AbstractActionForm abstractForm = (AbstractActionForm) form;
        /* Get the systemIdentifier whose information is to be searched */
        Long identifier = 	Long.valueOf(request.getParameter(Constants.SYSTEM_IDENTIFIER)); 
        if(identifier == null || identifier.longValue() == 0  )
        	identifier = ((Long)request.getAttribute(Constants.SYSTEM_IDENTIFIER)); 
        	
        try
        {
            //Retrieves the information to be edited.
            AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(abstractForm.getFormId());
            AbstractDomainObject abstractDomain = null;
            List list = null;
            
            String objName = DomainObjectFactory.getDomainObjectName(abstractForm.getFormId());
            Logger.out.debug("object name to be retrieved:"+objName+" identifier:"+identifier);
            list= bizLogic.retrieve(objName,Constants.SYSTEM_IDENTIFIER, identifier.toString());
            
            if (list.size() != 0)
            {
                /* If the record searched is present in the database,
                 * populate the formbean with the information retrieved.  
                 */
                abstractDomain = (AbstractDomainObject)list.get(0);
                abstractForm.setAllValues(abstractDomain);
                String pageOf = (String)request.getAttribute(Constants.PAGEOF);
                if (pageOf == null)
                	pageOf = (String)request.getParameter(Constants.PAGEOF);
                target = pageOf;
                abstractForm.setMutable(false);
                Logger.out.debug("Search Action ...................pageOf........."+pageOf);
                
                String operation = (String)request.getAttribute(Constants.OPERATION);
                Logger.out.debug("Common Search Action ...................operation........."+operation);
                request.setAttribute(Constants.OPERATION,operation);
                
            }
            else
            {
            	String objectName = DomainObjectFactory.getDomainObjectName(abstractForm.getFormId());
                /* If the searched record is not present in the database,  
                 * display an Error message.
                 */Logger.out.debug("Mandar --------___________------------- ");
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.unknown",
                		AbstractDomainObject.parseClassName(objectName)));
                saveErrors(request,errors);
                target = new String(Constants.FAILURE);
            }
        }
        catch (DAOException excp)
        {
            target = Constants.FAILURE;
            Logger.out.error(excp.getMessage());
        }
        return (mapping.findForward(target));
    }
}
