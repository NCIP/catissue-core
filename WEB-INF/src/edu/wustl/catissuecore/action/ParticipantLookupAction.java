/**
 * <p>Title: ParticipantLookupAction Class>
 * <p>Description:	This Action Class invokes the Participant Lookup Algorithm and gets matching participants</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author vaishali_khandelwal
 * @Created on May 19, 2006
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.DomainObjectFactory;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.factory.AbstractDomainObjectFactory;
import edu.wustl.common.factory.MasterFactory;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;

public class ParticipantLookupAction extends BaseAction
{
	/**
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 * @return value for ActionForward object
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
	{
		AbstractDomainObject abstractDomain = null;
		ActionMessages messages=null;
		String target=null;
		
		AbstractActionForm abstractForm = (AbstractActionForm) form;
		
		AbstractDomainObjectFactory abstractDomainObjectFactory = (AbstractDomainObjectFactory) MasterFactory
				.getFactory(DomainObjectFactory.class.getName());
		
		
		abstractDomain = abstractDomainObjectFactory.getDomainObject(abstractForm.getFormId(),
				abstractForm);
		Participant participant = (Participant) abstractDomain;
		
		Logger.out.debug("Participant Id :"+request.getParameter("participantId"));
		//checks weather participant is selected from the list and so forwarding to next action instead of participant lookup.
		//Abhishek Mehta
		if(request.getAttribute("continueLookup") == null)
		{
			if(request.getParameter("participantId")!=null &&!request.getParameter("participantId").equals("null")&&!request.getParameter("participantId").equals("")&&!request.getParameter("participantId").equals("0"))
			{
				Logger.out.info("inside the participant mapping");
				return mapping.findForward("participantSelect");
			}
		}
		
		boolean isCallToLookupLogicNeeded = isCallToLookupLogicNeeded(participant);
		
		if(isCallToLookupLogicNeeded)
		{
			ParticipantBizLogic bizlogic = (ParticipantBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.PARTICIPANT_FORM_ID);
			LookupLogic participantLookupLogic = (LookupLogic)Utility.getObject(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_ALGO));
			List matchingParticipantList = bizlogic.getListOfMatchingParticipants(participant,participantLookupLogic);
			if (matchingParticipantList!=null && matchingParticipantList.size() > 0)
			{
				messages=new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("participant.lookup.success","Submit was not successful because some matching participants found."));
	   			//Creating the column headings for Data Grid
				List columnList = getColumnHeadingList(bizlogic);
				request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnList);
				
				//Getitng the Participant List in Data Grid Format
				List participantDisplayList=getParticipantDisplayList(matchingParticipantList);
				request.setAttribute(Constants.SPREADSHEET_DATA_LIST, participantDisplayList);
				
				target=Constants.PARTICIPANT_LOOKUP_SUCCESS;
			}
			//	if no participant match found then add the participant in system
			else
			{
				target=Constants.PARTICIPANT_ADD_FORWARD;
			}
		}
		else
		{
			target=Constants.PARTICIPANT_ADD_FORWARD;
		}
		
		//if any matching participants are there then show the participants otherwise add the participant
		
		
		//setting the Submitted_for and Forward_to variable in request
		if(request.getParameter(Constants.SUBMITTED_FOR)!=null && !request.getParameter(Constants.SUBMITTED_FOR).equals(""))
		{
			request.setAttribute(Constants.SUBMITTED_FOR,request.getParameter(Constants.SUBMITTED_FOR));
		}
		if(request.getParameter(Constants.FORWARD_TO)!=null && !request.getParameter(Constants.FORWARD_TO).equals(""))
		{
			request.setAttribute(Constants.FORWARD_TO,request.getParameter(Constants.FORWARD_TO));
			
		}
		
		request.setAttribute("participantId","");
		if(request.getAttribute("continueLookup") == null)
		{
			if (messages != null)
	        {
	            saveMessages(request,messages);
	        }
		}
		Logger.out.debug("target:"+target);
		return (mapping.findForward(target));
	}
	
	
	private boolean isCallToLookupLogicNeeded(Participant participant)
	{
		if((participant.getFirstName() == null || participant.getFirstName().length()==0) && (participant.getMiddleName() == null || participant.getMiddleName().length() == 0) && (participant.getLastName() == null || participant.getLastName().length() == 0) && (participant.getSocialSecurityNumber()== null || participant.getSocialSecurityNumber().length() == 0) && participant.getBirthDate() == null && (participant.getParticipantMedicalIdentifierCollection() == null || participant.getParticipantMedicalIdentifierCollection().size()==0))
		{
			return false;
		}
		return true;
	}
	/**
	 * This Function creates the Column Headings for Data Grid
	 * @param bizlogic instance of ParticipantBizLogic
	 * @throws Exception generic exception
	 * @return List Column List
	 */
	private List getColumnHeadingList(ParticipantBizLogic bizlogic) throws Exception
	{
		//Creating the column list which is used in Data grid to display column headings
		String[] columnHeaderList = new String[]{Constants.PARTICIPANT_LAST_NAME,Constants.PARTICIPANT_FIRST_NAME,Constants.PARTICIPANT_MIDDLE_NAME,Constants.PARTICIPANT_BIRTH_DATE,Constants.PARTICIPANT_DEATH_DATE,Constants.PARTICIPANT_VITAL_STATUS,Constants.PARTICIPANT_GENDER,Constants.PARTICIPANT_SOCIAL_SECURITY_NUMBER,Constants.PARTICIPANT_MEDICAL_RECORD_NO};
		List columnList = new ArrayList();
		Logger.out.info("column List header size ;"+columnHeaderList.length);	
		for (int i = 0; i < columnHeaderList.length; i++)
		{
			columnList.add(columnHeaderList[i]);
		}
		Logger.out.info("column List size ;"+columnList.size());
		List displayList=bizlogic.getColumnList(columnList);
	//	displayList.add(0,Constants.PARTICIPANT_PROBABLITY_MATCH);
		return displayList;
	}
	
	/**
	 * This functions creates Particpant List with each participant informaton  with the match probablity 
	 * @param participantList list of participant
	 * @return List of Participant Information  List
	 */
	private List getParticipantDisplayList(List participantList)
	{
		List participantDisplayList=new ArrayList();
		Iterator itr=participantList.iterator();
		String medicalRecordNo="";
		String siteId = "";
		while(itr.hasNext())
		{
			DefaultLookupResult result=(DefaultLookupResult)itr.next();
			Participant participant=(Participant)result.getObject();
			
			List participantInfo=new ArrayList();
		
			participantInfo.add(Utility.toString(participant.getLastName()));
			participantInfo.add(Utility.toString(participant.getFirstName()));
			participantInfo.add(Utility.toString(participant.getMiddleName()));
			participantInfo.add(Utility.toString(participant.getBirthDate()));
			participantInfo.add(Utility.toString(participant.getDeathDate()));
			participantInfo.add(Utility.toString(participant.getVitalStatus()));
			participantInfo.add(Utility.toString(participant.getGender()));						
			participantInfo.add(Utility.toString(participant.getSocialSecurityNumber()));
			if(participant.getParticipantMedicalIdentifierCollection()!=null)
			{
				Iterator participantMedicalIdentifierItr = participant.getParticipantMedicalIdentifierCollection().iterator();
				while(participantMedicalIdentifierItr.hasNext())
				{
					ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier)participantMedicalIdentifierItr.next();
					if(participantMedicalIdentifier.getSite() != null && participantMedicalIdentifier.getSite().getId() != null)
					{
					medicalRecordNo = participantMedicalIdentifier.getMedicalRecordNumber();
					siteId = participantMedicalIdentifier.getSite().getId().toString();
					}
				}
			}
			participantInfo.add(Utility.toString(medicalRecordNo));
			participantInfo.add(participant.getId());
			participantDisplayList.add(participantInfo);
			
		}
		return participantDisplayList;
	}
}