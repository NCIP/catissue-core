/**
 * <p>Title: DepartmentAction Class</p>
 * <p>Description:	This class initializes the fields in the Department Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00
 * Created on May 23rd, 2005
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

import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * This class initializes the fields in the User Add/Edit webpage.
 * @author ajay_sharma
 */

public class CollectionProtocolRegistrationAction extends Action
{
    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in Participant Registration Add/Edit webpage.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit User Page. 
        request.setAttribute(Constants.OPERATION,operation);
		        
		try
		{
			
			// get list of Protocol title.
			AbstractBizLogic dao = BizLogicFactory.getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
			ListIterator iterator = null;
			int i;
            
			//Sets the instituteList attribute to be used in the Add/Edit User Page.
			List protocolList = dao.retrieve(CollectionProtocol.class.getName());
			System.out.println("1: "+protocolList.size());
			String[] protocolArray = new String[protocolList.size()+1];
			String[] protocolIdArray = new String[protocolList.size() + 1];
			iterator = protocolList.listIterator();
			
			protocolArray[0] = Constants.SELECT_OPTION;
			protocolIdArray[0]	= "-1";
			i = 1;
			while (iterator.hasNext())
			{
				CollectionProtocol collectionProtocol = (CollectionProtocol) iterator.next();
				protocolArray[i] = collectionProtocol.getTitle();
				protocolIdArray[i] = collectionProtocol.getSystemIdentifier().toString();
				i++;
			}
			request.setAttribute(Constants.PROTOCOLLIST, protocolArray);
			request.setAttribute(Constants.PROTOCOLIDLIST, protocolIdArray);
			
			//get list of Participant's names
			List participantList = dao.retrieve(Participant.class.getName());
			System.out.println("2: "+protocolList.size());		
			String[] participantArray = new String[participantList.size()+1];
			String[] participantIdArray = new String[participantList.size()+1];
			iterator = participantList.listIterator();
			participantArray[0] = Constants.SELECT_OPTION;
			participantIdArray[0] = "-1";
			i = 1;
			while (iterator.hasNext())
			{
				Participant participant = (Participant) iterator.next();
				participantArray[i] = participant.getLastName()+", "+participant.getFirstName();
				participantIdArray[i] = participant.getSystemIdentifier().toString();
				i++;
			}
			request.setAttribute(Constants.PARTICIPANTLIST, participantArray);
			request.setAttribute(Constants.PARTICIPANTIDLIST, participantIdArray);
		} 
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
        
        return mapping.findForward(Constants.SUCCESS);
    }
}