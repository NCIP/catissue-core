/**
 * <p>Title: CommonAddEditAction Class>
 * <p>Description:	This Class is used to Add/Edit the data in the database.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.DomainObjectFactory;
import edu.wustl.catissuecore.exception.AssignDataException;
import edu.wustl.catissuecore.exception.BizLogicException;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to Add/Edit data in the database.
 * @author gautam_shetty
 */
public class CommonAddEditAction extends Action
{

    /**
     * Overrides the execute method of Action class.
     * Adds / Updates the data in the database.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        String target = null;

        try
        {
            AbstractActionForm abstractForm = (AbstractActionForm) form;
            AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(abstractForm.getFormId());

            if (abstractForm.isAddOperation())
            {
                //If operation is add, add the data in the database.
                AbstractDomainObject abstractDomain = DomainObjectFactory.getDomainObject(
                        abstractForm.getFormId(), abstractForm);
                
                bizLogic.insert(abstractDomain, Constants.HIBERNATE_DAO);
                target = new String(Constants.SUCCESS);
            }
            else
            {
                //If operation is edit, update the data in the database.
            	String objName = AbstractDomainObject.getDomainObjectName(abstractForm.getFormId());
            	Logger.out.debug("DEBUG...................ID*********"+abstractForm.getSystemIdentifier());
                List list = bizLogic.retrieve(objName, Constants.IDENTIFIER,
										  new Long(abstractForm.getSystemIdentifier()));
                
                if (!list.isEmpty())
                {
                	AbstractDomainObject abstractDomain = (AbstractDomainObject) list.get(0);
                    abstractDomain.setAllValues(abstractForm);
                    bizLogic.update(abstractDomain,Constants.HIBERNATE_DAO);
                    target = new String(Constants.SUCCESS);
                }
                else
                {
                    target = new String(Constants.FAILURE);
                }
            }
            
            //Status message key.
            String statusMessageKey = String.valueOf(abstractForm.getFormId() +
					"."+String.valueOf(abstractForm.isAddOperation()));

            request.setAttribute(Constants.STATUS_MESSAGE_KEY,statusMessageKey);
        }
        catch (BizLogicException excp)
        {
        	ActionErrors errors = new ActionErrors();
        	
        	ActionError error = new ActionError("errors.item.required","Error "+excp.getMessage());
        	errors.add(ActionErrors.GLOBAL_ERROR,error);
        	saveErrors(request,errors);
            target = new String(Constants.FAILURE);
            
            Logger.out.debug("excp "+excp.getMessage());
            Logger.out.error(excp.getMessage(), excp);
            //Logger.out.error(excp.getMessage(), excp);
        }
        catch (DAOException excp)
        {
            target = new String(Constants.FAILURE);
            Logger.out.debug("excp "+excp.getMessage());
            Logger.out.error(excp.getMessage(), excp);
            //Logger.out.error(excp.getMessage(), excp);
        }
        catch (AssignDataException excp)
        {
            target = new String(Constants.FAILURE);
            Logger.out.debug("excp "+excp.getMessage());
            Logger.out.error(excp.getMessage(), excp);
            //Logger.out.error(excp.getMessage(), excp);
        }
        return (mapping.findForward(target));
    }
}