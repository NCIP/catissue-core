/**
 * <p>Title: ConflictResolverAction Class>
 * <p>Description:	Conflict Resolver Action class
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 *@author kalpana Thakur
 * Created on sep 18,2007
 * 
 */
package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.bizlogic.ReportLoaderQueueBizLogic;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.Utility;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.UserNotAuthorizedException;



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
		HttpSession session = request.getSession();
		String participantIdToAssociate = (String) session.getAttribute(Constants.PARTICIPANT_ID_TO_ASSOCIATE);
		String specimenCollGrpId = (String) session.getAttribute(Constants.SCG_ID_TO_ASSOCIATE);
		String reportQueueId = (String) request.getParameter(Constants.REPORT_ID);
		String button = request.getParameter(Constants.CONFLICT_BUTTON);
		String errorMessage =null;
		
				
//		overwrite the existing report
		if(button.trim().equalsIgnoreCase(Constants.OVERWRITE_REPORT))
		{
			overwriteReport(request,reportQueueId);
		}
		
//		Ignore new Report
		if(button.trim().equalsIgnoreCase(Constants.IGNORE_NEW_REPORT))
		{
			ignoreNewReport(reportQueueId);
		}

	
		
//		Creating a new Participant		
		if(button.trim().equalsIgnoreCase(Constants.CREATE_NEW_PARTICIPANT))
		{
			errorMessage = createNewParticipant(request,reportQueueId);
			if(errorMessage!=null)
			{
				setActionError(request, errorMessage);
			}
		}
		else
		{	
			if(button.trim().equalsIgnoreCase(Constants.USE_SELECTED_PARTICIPANT))
			{
				if(participantIdToAssociate != null && !participantIdToAssociate.equals(""))
				{
//				Associate existing participant with Report						
				  createNewSCG(request,reportQueueId,participantIdToAssociate);
				}  
			}
			else if(button.trim().equalsIgnoreCase(Constants.USE_SELECTED_SCG)){
				
				if(specimenCollGrpId != null && !specimenCollGrpId.equals(""))
				{
//					Associate existing SCG with Report	
					associateSCGWithReport(request,reportQueueId,participantIdToAssociate,specimenCollGrpId);
				}		
			}
		}	
		resetSessionAttributes(session);
		return mapping.findForward(Constants.SUCCESS);
	}	
	
	/**
	 * To create new participant and associate it to the report:
	 * @param request
	 * @param reportQueueId
	 * @throws Exception 
	 * @throws Exception
	 */
	private String createNewParticipant(HttpServletRequest request, String reportQueueId) throws Exception 
	{
		
		String errorMessage = null;
		ReportLoaderQueue reportLoaderQueue =null;
		reportLoaderQueue = Utility.getReportQueueObject(reportQueueId);
		
		Participant participant = Utility.getParticipantFromReportLoaderQueue(reportQueueId);
	
		ParticipantBizLogic participantBizLogic = (ParticipantBizLogic)BizLogicFactory.getInstance().getBizLogic(Participant.class.getName());
		try
		{
			participantBizLogic.insert(participant,getSessionData(request),0);
		}
		catch(Exception e)
		{
			//System.out.println("Error Occurred !!!!!");
			errorMessage = ApplicationProperties.getValue("errors.caTies.conflict.genericmessage");
			//Setting the status to NEW
//			reportLoaderQueue.setParticipantCollection(null);
//			reportLoaderQueue.setStatus(CaTIESConstants.PARTICIPANT_CREATION_ERROR);
//			updateReportLoaderQueue(reportLoaderQueue,request);
			return errorMessage;
		}
		
		Collection participantColl = new HashSet();
		// Adding the new participant
		participantColl.add(participant); 
		reportLoaderQueue.setParticipantCollection(participantColl);
		//The new SCG for this participant will be inserted by the FileProcessorThread
	
		//Setting the status to NEW
		reportLoaderQueue.setStatus(CaTIESConstants.NEW);
		reportLoaderQueue.setSpecimenCollectionGroup(null);
		updateReportLoaderQueue(reportLoaderQueue,request);
		
		return errorMessage;
	}
	
	
	/**
	 * To associate existing participant to the report and to create new SCG:
	 * @param request
	 * @param reportQueueId
	 * @param participantIdToAssociate
	 * @throws DAOException
	 */
	private void createNewSCG(HttpServletRequest request,String reportQueueId,String participantIdToAssociate) throws DAOException
	{
		
		ReportLoaderQueue reportLoaderQueue =null;
		reportLoaderQueue = Utility.getReportQueueObject(reportQueueId);
		
		//Changing the status of the report in the queue to NEW
		reportLoaderQueue.setStatus(CaTIESConstants.NEW);
		
		//Create new SCG
		reportLoaderQueue.setSpecimenCollectionGroup(null);
		
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
		updateReportLoaderQueue(reportLoaderQueue ,request);
	}
	
	
	/**
	 * Associate the existing SCG to the report
	 * @param request
	 * @param reportQueueId
	 * @param participantIdToAssociate
	 * @param specimenCollGrpId
	 * @throws DAOException
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 */
	private void associateSCGWithReport(HttpServletRequest request,String reportQueueId,String participantIdToAssociate,String specimenCollGrpId) throws DAOException, BizLogicException, UserNotAuthorizedException
	{
		Long cprId =null;
		ReportLoaderQueue reportLoaderQueue =null;
		reportLoaderQueue = Utility.getReportQueueObject(reportQueueId);
		
		//Changing the status of the report in the queue to NEW
		reportLoaderQueue.setStatus(CaTIESConstants.NEW);
		
		//Associating the SCG
		if(specimenCollGrpId != null && !specimenCollGrpId.equals(""))
		{		
			SpecimenCollectionGroup scg=null;
			ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic)BizLogicFactory.getInstance().getBizLogic(ReportLoaderQueue.class.getName());
			Object object = reportLoaderQueueBizLogic.retrieve(SpecimenCollectionGroup.class.getName(), new Long(specimenCollGrpId));
			if(object != null)
			{
				scg = (SpecimenCollectionGroup) object;
			}
			cprId = (Long) scg.getCollectionProtocolRegistration().getId();
			reportLoaderQueue.setSpecimenCollectionGroup(scg);
		}
		
		//Retrieving participantID if it is null
		if(participantIdToAssociate == null || participantIdToAssociate.equals(""))
		{
			DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
			Long partID=(Long)defaultBizLogic.retrieveAttribute(CollectionProtocolRegistration.class.getName(), cprId,Constants.COLUMN_NAME_PARTICIPANT_ID );
			participantIdToAssociate = (String)partID.toString();
		}
		
		
		
		//removing all participants from CATISSUE_REPORT_PARTICIP_REL other than the selected participant
		Collection participantColl = reportLoaderQueue.getParticipantCollection();
		Iterator iter = participantColl.iterator();
		Set tempColl = new HashSet();
		Participant participant = null;
		while(iter.hasNext())
		{
			participant = (Participant)iter.next();
			if(participant.getId().toString().equals(participantIdToAssociate.trim()))
			{
				tempColl.add(participant);				
			}
		}
		reportLoaderQueue.setParticipantCollection(tempColl);
		
		//Updating the report queue obj
		updateReportLoaderQueue(reportLoaderQueue ,request);
	}
	
	
	
	/**
	 * updating the reportloaderQueue obj
	 * @param reportLoaderQueue
	 * @param request
	 */
	private void updateReportLoaderQueue(ReportLoaderQueue reportLoaderQueue , HttpServletRequest request)
	{
					
		try
		{
			ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic)BizLogicFactory.getInstance().getBizLogic(ReportLoaderQueue.class.getName());
			reportLoaderQueueBizLogic.update(reportLoaderQueue, reportLoaderQueue, 0, getSessionData(request));
			
		}
		catch(Exception e)
		{
			Logger.out.error("Error Updating ReportQueue" + e);
		}
	}
	
	protected SessionDataBean getSessionData(HttpServletRequest request) 
	{
			Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
			 /**
			  *  This if loop is specific to Password Security feature.
			  */
			if(obj == null)
			{
				obj = request.getSession().getAttribute(Constants.TEMP_SESSION_DATA);
			}
			if(obj!=null)
			{
				SessionDataBean sessionData = (SessionDataBean) obj;
				return  sessionData;
			} 
			return null;
		
	}
	
	/**
	 * To generate the errors
	 * @param request
	 * @param errorMessage
	 */
	private void setActionError(HttpServletRequest request, String errorMessage)
	{
		ActionErrors errors = new ActionErrors();
		ActionError error = new ActionError("errors.item", errorMessage);
		errors.add(ActionErrors.GLOBAL_ERROR, error);
		saveErrors(request, errors);
	}
	
	 protected void resetSessionAttributes(HttpSession session)
	 {
		 //Removing the session objects
		  session.removeAttribute(Constants.PARTICIPANT_ID_TO_ASSOCIATE);
		  session.removeAttribute(Constants.SCG_ID_TO_ASSOCIATE);
		 
	  }
	 
	 
	 
	 /**
	  * Ignore the new report and use the existing one. 
	 * @param reportQueueId
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 * @throws BizLogicException
	 */
	protected void ignoreNewReport(String reportQueueId) throws DAOException, UserNotAuthorizedException, BizLogicException
	  {
		   	Long cprId =null;
			ReportLoaderQueue reportLoaderQueue =null;
			reportLoaderQueue = Utility.getReportQueueObject(reportQueueId);
			ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic)BizLogicFactory.getInstance().getBizLogic(ReportLoaderQueue.class.getName());
			
			//deleting the reportloaderQueue object
			reportLoaderQueueBizLogic.delete(reportLoaderQueue, 0);
				  
	  }
	  
	  
	 /**To overwrite the existing report
	 * @param request
	 * @param reportQueueId
	 * @throws DAOException
	 */
	protected void overwriteReport(HttpServletRequest request,String reportQueueId) throws DAOException
	  {
		   ReportLoaderQueue reportLoaderQueue =null;
		   reportLoaderQueue = Utility.getReportQueueObject(reportQueueId);
			
			//Changing the status of the report in the queue to NEW
			reportLoaderQueue.setStatus(CaTIESConstants.OVERWRITE_REPORT);
			updateReportLoaderQueue(reportLoaderQueue,request);
		  
	  }
}
