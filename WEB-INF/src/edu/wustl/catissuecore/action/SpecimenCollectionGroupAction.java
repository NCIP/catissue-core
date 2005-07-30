/**
 * <p>Title: SpecimenCollectionGroupAction Class>
 * <p>Description:	SpecimenCollectionGroupAction initializes the fields in the 
 * New Specimen Collection Group page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.dao.AbstractBizLogic;
import edu.wustl.catissuecore.dao.BizLogicFactory;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * SpecimenCollectionGroupAction initializes the fields in the 
 * New Specimen Collection Group page.
 * @author gautam_shetty
 */
public class SpecimenCollectionGroupAction extends Action
{
    
    /**
     * Overrides the execute method of Action class.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit SpecimenCollectionGroup Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        request.setAttribute(Constants.PROTOCOL_TITLE_LIST, Constants.PROTOCOL_TITLE_ARRAY);
        
        request.setAttribute(Constants.PARTICIPANT_NAME_LIST,Constants.PARTICIPANT_NAME_ARRAY);
        
        //request.setAttribute(Constants.PROTOCOL_PARTICIPANT_NUMBER_LIST,Constants.PROTOCOL_PARTICIPANT_NUMBER_ARRAY);
        
        request.setAttribute(Constants.STUDY_CALENDAR_EVENT_POINT_LIST,Constants.STUDY_CALENDAR_EVENT_POINT_ARRAY);
        
        request.setAttribute(Constants.CLINICAL_STATUS_LIST,Constants.CLINICAL_STATUS_ARRAY);
        
		try
		{
	
			// get list of Protocol title.
			AbstractBizLogic dao = BizLogicFactory.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			ListIterator iterator = null;
			int i;
    
			//Sets the instituteList attribute to be used in the Add/Edit User Page.
			List protocolList = dao.retrieve(CollectionProtocol.class.getName());
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

            //getting participant number list. 
            

			List particpantNumberList = dao.retrieve(CollectionProtocolRegistration.class.getName());
            System.out.println("before setting values of participant number..:"+particpantNumberList.size());
			String[] particpantNumberArray = new String[particpantNumberList.size()+1];
	    	String[] particpantIdArray = new String[particpantNumberList.size() + 1];
			particpantNumberArray[0] = Constants.SELECT_OPTION;
			particpantIdArray[0]	= "-1";
			i = 1;
			iterator = particpantNumberList.listIterator();
			while (iterator.hasNext())
			{
				CollectionProtocolRegistration collectionProtocolregistration = (CollectionProtocolRegistration) iterator.next();
				particpantNumberArray[i] = collectionProtocolregistration.getProtocolParticipantIdentifier();
				particpantIdArray[i] = collectionProtocolregistration.getSystemIdentifier().toString();
				i++;
			}
			request.setAttribute(Constants.PROTOCOL_PARTICIPANT_NUMBER_LIST, particpantNumberArray);
			request.setAttribute(Constants.PROTOCOL_PARTICIPANT_NUMBER_ID_LIST, particpantIdArray);
            System.out.println("after setting values of participant number..");
		}
		catch(Exception exc){
			exc.printStackTrace();
		}
        return mapping.findForward(Constants.SUCCESS);
    }
}
