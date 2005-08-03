/**
 * <p>Title: SpecimenCollectionGroupAction Class>
 * <p>Description:	SpecimenCollectionGroupAction initializes the fields in the 
 * New Specimen Collection Group page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * SpecimenCollectionGroupAction initializes the fields in the 
 * New Specimen Collection Group page.
 * @author ajay_sharma
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
        
        
		
		boolean prtocolTitleSelected=false;
        long protocolTitleSelectedValue = -1;    
		if(request.getParameter("typeSelected") != null)
		{
			protocolTitleSelectedValue =Long.parseLong(request.getParameter("typeSelected"));
			prtocolTitleSelected = true;
		}
		
		try
		{
			// get list of Protocol title.
			AbstractBizLogic dao = BizLogicFactory.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
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


		
           //Populating the Site Type Array
		   List siteList = dao.retrieve(Site.class.getName());
		   System.out.println("2: "+protocolList.size());
		   String[] siteArray	 = new String[siteList.size() + 1];
		   String[] siteIdArray = new String[siteList.size() + 1]; 

		   siteArray[0]	= Constants.SELECT_OPTION;
		   siteIdArray[0]	= "-1";

		   iterator = null;
		   iterator = siteList.listIterator();
		   i = 1;
        
		   while (iterator.hasNext())
		   {
			   Site site = (Site) iterator.next();
			   siteArray[i] = site.getName();
			   siteIdArray[i] = site.getSystemIdentifier().toString();
		 	   i++;
		   }

 		   request.setAttribute(Constants.SITELIST,siteArray);
 		   request.setAttribute(Constants.SITEIDLIST,siteIdArray);
		
            //get list of Participant's names
			List participantList = dao.retrieve(Participant.class.getName());
			System.out.println("3: "+protocolList.size());
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
			System.out.println("4: "+protocolList.size());
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
			
			if(prtocolTitleSelected)
			{   
			                     
				String[] calendarEventPointArray = {Constants.SELECT_OPTION};
				String[] calendarEventPointIdArray = {"-1"};
				
				String[] clinicalStatusArray = {Constants.SELECT_OPTION};
				String[] clinicalStatusIdArray = {"-1"};
				//getting Study Calendar event points. 
				List calendarEventPointList = dao.retrieve(CollectionProtocol.class.getName(),"systemIdentifier",new Long(protocolTitleSelectedValue));
				if(!calendarEventPointList.isEmpty())
				{
					CollectionProtocol collectionProtocol = (CollectionProtocol)calendarEventPointList.get(0);
					Collection collectionEventList = collectionProtocol.getCollectionProtocolEventCollection();
					
					calendarEventPointArray = new String[collectionEventList.size()+1];
					calendarEventPointIdArray = new String[collectionEventList.size()+1];
					calendarEventPointArray[0] = Constants.SELECT_OPTION;
					calendarEventPointIdArray[0]	= "-1";
				
					clinicalStatusArray = new String[collectionEventList.size()+1];
					clinicalStatusIdArray = new String[collectionEventList.size()+1];
					clinicalStatusArray[0] = Constants.SELECT_OPTION;
					clinicalStatusIdArray[0]	= "-1";
				
					
					i = 1;
					Iterator iterator1 = collectionEventList.iterator();
					while(iterator1.hasNext())
					{
					   CollectionProtocolEvent cpe = (CollectionProtocolEvent)iterator1.next();
					   	   				  
					   calendarEventPointArray[i] = cpe.getStudyCalendarEventPoint().toString();
					   calendarEventPointIdArray[i] = cpe.getSystemIdentifier().toString();
					   
					   clinicalStatusArray[i] = cpe.getClinicalStatus().toString();
					   clinicalStatusIdArray[i] = cpe.getClinicalStatus().toString();
					   i++;           				
					}
				}
	     			
				request.setAttribute(Constants.STUDY_CALENDAR_EVENT_POINT_LIST, calendarEventPointArray);
				request.setAttribute(Constants.STUDY_CALENDAR_EVENT_POINT_ID_LIST, calendarEventPointIdArray);
				
				request.setAttribute(Constants.CLINICAL_STATUS_LIST,clinicalStatusArray);
			}
			else
			{
				    String[] calendarEventPointArray = new String[1];
					String[] calendarEventPointIdArray = new String[1];
				    calendarEventPointArray[0] = Constants.SELECT_OPTION;
				    calendarEventPointIdArray[0]	= "-1"; 
				    request.setAttribute(Constants.STUDY_CALENDAR_EVENT_POINT_LIST, calendarEventPointArray);
					request.setAttribute(Constants.STUDY_CALENDAR_EVENT_POINT_ID_LIST, calendarEventPointIdArray);
				    
				    request.setAttribute(Constants.CLINICAL_STATUS_LIST,Constants.CLINICAL_STATUS_ARRAY);		
			}		
			
			
			
			
		}
		catch(Exception exc){
			exc.printStackTrace();
		}
        return mapping.findForward(Constants.SUCCESS);
    }
}
