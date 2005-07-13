/**
 * <p>Title: AccessionAction Class>
 * <p>Description:	This class initializes the fields in the Accession webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 12, 2005
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

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.dao.AbstractBizLogic;
import edu.wustl.catissuecore.dao.BizLogicFactory;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields in the Accession webpage.
 * @author gautam_shetty
 */

public class AccessionAction extends Action
{

    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in Accession webpage.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        try
        {
            //Gets the value of the operation parameter.
            String operation = request.getParameter(Constants.OPERATION);

            //Sets the operation attribute to be used in the Add/Edit Accession Page. 
            request.setAttribute(Constants.OPERATION, operation);

            //Sets the pageOf attribute (for Add,Edit pages or Query Interface)
            String pageName = request.getParameter(Constants.PAGEOF);
            request.setAttribute(Constants.PAGEOF, pageName);

            AbstractActionForm abstractActionForm = (AbstractActionForm) form;
            AbstractBizLogic dao = BizLogicFactory
                    .getDAO(Constants.ACCESSION_FORM_ID);

            //Sets the participantList attribute to be used in the Add/Edit Accession Page.
            List participantList = dao.retrieve(Participant.class.getName());
            StringBuffer[] participantArray = new StringBuffer[participantList
                    .size()];
            String[] participantId = new String[participantList.size()];
            ListIterator iterator = participantList.listIterator();
            int i = 0;
            while (iterator.hasNext())
            {
                Participant participant = (Participant) iterator.next();
                participantArray[i] = new StringBuffer().append(
                        participant.getLastName()).append(",").append(
                        participant.getFirstName());
                participantId[i] = participant.getIdentifier().toString();
                i++;
            }
            request.setAttribute(Constants.PARTICIPANTIDLIST, participantId);
            request.setAttribute(Constants.PARTICIPANTLIST, participantArray);

            request.setAttribute(Constants.PROTOCOLLIST,
                    Constants.PROTOCOLARRAY);

            request.setAttribute(Constants.RECEIVEDBYLIST,
                    Constants.RECEIVEDBYARRAY);

            request.setAttribute(Constants.RECEIVEDSITELIST,
                    Constants.RECEIVEDBYARRAY);

            request.setAttribute(Constants.COLLECTEDBYLIST,
                    Constants.COLLECTEDBYARRAY);

            request.setAttribute(Constants.COLLECTIONSITELIST,
                    Constants.RECEIVEDBYARRAY);

            request.setAttribute(Constants.TIMEHOURLIST,
                    Constants.TIME_HOUR_ARRAY);

            request.setAttribute(Constants.TIMEMINUTESLIST,
                    Constants.TIME_HOUR_ARRAY);

            request.setAttribute(Constants.TIMEAMPMLIST,
                    Constants.TIME_HOUR_AMPM_ARRAY);

            request.setAttribute(Constants.RECEIVEDMODELIST,
                    Constants.RECEIVEDMODEARRAY);

        }
        catch (DAOException excp)
        {
            Logger.out.error(excp.getMessage());
        }
        return mapping.findForward(Constants.SUCCESS);
    }
}