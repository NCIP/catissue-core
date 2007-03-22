/**
 * <p>Title: ConflictResolverAction Class>
 * <p>Description:	Conflict Resolver Action class
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Feb 28,2007
 */
package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConflictDetailsForm;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.reportloader.Parser;
import edu.wustl.catissuecore.reportloader.ReportLoaderUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


/**
 * @author ashish_gupta
 *
 */
public class ConflictResolverAction extends BaseAction
{
	/**
	 * Overrides the execute method of Action class.
	 * Initializes the various fields in ConflictView.jsp Page.
	 * @param mapping object
	 * @param form object
	 * @param request object
	 * @param response object
	 * @return ActionForward object
	 * @throws Exception object
	 * */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ConflictDetailsForm conflictDetailsForm = (ConflictDetailsForm)form;
//		String participantIdToAssociate = request.getParameter(Constants.PARTICIPANT_ID_TO_ASSOCIATE);
//		String specimenCollGrpId = request.getParameter(Constants.SCG_ID_TO_ASSOCIATE);
		
		String participantIdToAssociate = conflictDetailsForm.getParticipantId();
		String specimenCollGrpId = conflictDetailsForm.getScgId();
		
		String button = request.getParameter(Constants.BUTTON_NAME);
		HttpSession session = request.getSession();
		if(button.trim().equalsIgnoreCase(Constants.CREATE_PARTICIPANT_BUTTON))
		{//Creating a new Participant
			createNewParticipant(session,conflictDetailsForm.getReportQueueId());
		}
		else if(button.trim().equalsIgnoreCase(Constants.ASSOCIATE_PARTICIPANT_BUTTON))
		{
			associateParticipantWithReport(conflictDetailsForm.getReportQueueId(),participantIdToAssociate,specimenCollGrpId);
		}
		return mapping.findForward(Constants.SUCCESS);
	}	
	/**
	 * @param session object
	 * @throws BizLogicException object
	 * @throws UserNotAuthorizedException object
	 */
	private void createNewParticipant(HttpSession session, String reportQueueId) throws BizLogicException,UserNotAuthorizedException,DAOException
	{
		Participant participant = (Participant)session.getAttribute(Constants.REPORT_PARTICIPANT_OBJECT);
		
		ReportLoaderQueue reportLoaderQueue = getReportLoaderQueueObject(reportQueueId);
		
		Collection participantColl = new HashSet();
		//Adding the new participant
		participantColl.add(participant);		
		reportLoaderQueue.setParticipantCollection(participantColl);
		
		//The new SCG for this participant will be inserted by the FileProcessorThread
		
		//Setting the status to NEW
		reportLoaderQueue.setStatus(Parser.NEW);
		updateReportLoaderQueue(reportLoaderQueue);
	}
	/**
	 * @param reportQueueId object
	 * @param participantIdToAssociate
	 * @throws DAOException object
	 */
	private void associateParticipantWithReport(String reportQueueId,String participantIdToAssociate,String specimenCollGrpId) throws DAOException
	{
		ReportLoaderQueue reportLoaderQueue = getReportLoaderQueueObject(reportQueueId);
		//Changing the status of the report in the queue to NEW
		reportLoaderQueue.setStatus(Parser.NEW);
		
		//Associating the SCG
		if(specimenCollGrpId != null && !specimenCollGrpId.equals(""))
		{			
			List scgList = ReportLoaderUtil.getObject(SpecimenCollectionGroup.class.getName(),Constants.SYSTEM_IDENTIFIER, specimenCollGrpId);
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup)scgList.get(0);
			reportLoaderQueue.setSpecimenCollectionGroup(scg);
		}
		
		//removing all participants from CATISSUE_REPORT_PARTICIP_REL other than the selected participant
		Collection participantColl = reportLoaderQueue.getParticipantCollection();
		Iterator iter = participantColl.iterator();
		Set tempColl = new HashSet();
		while(iter.hasNext())
		{
			Participant participant = (Participant)iter.next();
			if(participant.getId().toString().equals(participantIdToAssociate.trim()))
			{
				tempColl.add(participant);				
			}
		}
		reportLoaderQueue.setParticipantCollection(tempColl);
		
		//Updating the report queue obj
		updateReportLoaderQueue(reportLoaderQueue);
	}
	/**
	 * @param reportQueueId object
	 * @return ReportLoaderQueue object
	 * @throws DAOException object
	 */
	private ReportLoaderQueue getReportLoaderQueueObject(String reportQueueId) throws DAOException
	{
		//retrieving the report queue object from db
		List reportLoaderQueueList = ReportLoaderUtil.getObject(ReportLoaderQueue.class.getName(),Constants.SYSTEM_IDENTIFIER, reportQueueId);
		ReportLoaderQueue reportLoaderQueue = (ReportLoaderQueue)reportLoaderQueueList.get(0);
		return reportLoaderQueue;
	}
	/**
	 * @param reportLoaderQueue object
	 */
	private void updateReportLoaderQueue(ReportLoaderQueue reportLoaderQueue)
	{
		//updating the reportloaderQueue obj
		try
		{
			ReportLoaderUtil.updateObject(reportLoaderQueue);
		}
		catch(Exception e)
		{
			Logger.out.error("Error Updating ReportQueue" + e);
		}
	}
	
}
