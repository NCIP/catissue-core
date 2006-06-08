/**
 * <p>Title: ParticipantLookupAction Class>
 * <p>Description:	This Class is used to search the matching participant.</p>
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

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.factory.AbstractDomainObjectFactory;
import edu.wustl.common.factory.MasterFactory;
import edu.wustl.common.lookup.DefaultLookupParameters;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.common.lookup.LookupResult;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;

public class ParticipantLookupAction extends BaseAction
{
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
	{
		AbstractDomainObject abstractDomain = null;
		String target=null;
		
		AbstractActionForm abstractForm = (AbstractActionForm) form;
		
		AbstractDomainObjectFactory abstractDomainObjectFactory = (AbstractDomainObjectFactory) MasterFactory
				.getFactory("edu.wustl.catissuecore.domain.DomainObjectFactory");
		
		
		abstractDomain = abstractDomainObjectFactory.getDomainObject(abstractForm.getFormId(),
				abstractForm);
		Participant participant = (Participant) abstractDomain;
		
		//getting the instance of ParticipantLookupLogic class
		LookupLogic participantLookupLogic = (LookupLogic) Utility
				.getObject(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_ALGO));

		//getting the cutoff value for participant lookup matching algo
		Double cutoff = new Double(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_CUTOFF));
		
		//Creating the DefaultLookupParameters object to pass as argument to lookup function
		//This object contains the Participant with which matching participant are to be found and the cutoff value.
		DefaultLookupParameters params=new DefaultLookupParameters();
		params.setObject(participant);
		params.setCutoff(cutoff);
		//calling thr lookup function which returns the List of ParticipantResuld objects.
		//ParticipantResult object contains the matching participant and the probablity.
		List participantList = participantLookupLogic.lookup(params);
				
		//if any matching participants are there then show the participants otherwise add the participant
		if (participantList.size() > 0)
		{
			//Creating the column headings for Data Grid
			List columnList = getColumnHeadingList();
			request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnList);
			
			//Getitng the Participant List in Data Grid Format
			List participantDisplayList=getParticipantDisplayList(participantList);
			request.setAttribute(Constants.SPREADSHEET_DATA_LIST, participantDisplayList);
			target=Constants.PARTICIPANT_LOOKUP_SUCCESS;
		}
		//	if no participant match found then add the participant in system
		else
		{
			target=Constants.PARTICIPANT_ADD_FORWARD;
		}
		
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
		return (mapping.findForward(target));
	}
	/**
	 * This Function creates the Column Headings for Data Grid
	 * @return List Column List
	 */
	private List getColumnHeadingList()
	{
		//Creating the column list which is used in Data grid to display column headings
		String[] columnHeaderList = new String[]{Constants.PARTICIPANT_SYSTEM_IDENTIFIER, Constants.PARTICIPANT_LAST_NAME,Constants.PARTICIPANT_FIRST_NAME,Constants.PARTICIPANT_MIDDLE_NAME,Constants.PARTICIPANT_BIRTH_DATE,Constants.PARTICIPANT_DEATH_DATE,Constants.PARTICIPANT_SOCIAL_SECURITY_NUMBER,Constants.PARTICIPANT_PROBABLITY_MATCH};
		List columnList = new ArrayList();
			
		for (int i = 0; i < columnHeaderList.length; i++)
		{
			columnList.add(columnHeaderList[i]);
		}
		return columnList;
	}
	
	/**
	 * This functions creates Particpant List with each participant informaton  with the match probablity 
	 * @param ParticipantList
	 * @return List of Participant Information  List
	 */
	private List getParticipantDisplayList(List ParticipantList)
	{
		List participantDisplayList=new ArrayList();
		Iterator itr=ParticipantList.iterator();
		while(itr.hasNext())
		{
			LookupResult result=(LookupResult)itr.next();
			Participant participant=(Participant)result.getObject();
			
			List participantInfo=new ArrayList();
			participantInfo.add(participant.getSystemIdentifier());
			participantInfo.add(participant.getLastName());
			participantInfo.add(participant.getFirstName());
			participantInfo.add(participant.getMiddleName());
			participantInfo.add(participant.getBirthDate());
			participantInfo.add(participant.getDeathDate());
			participantInfo.add(participant.getSocialSecurityNumber());
			participantInfo.add(result.getProbablity().toString()+" %");
			participantDisplayList.add(participantInfo);
			
		}
		return participantDisplayList;
	}
}