/**
 * <p>
 * Title: ConflictResolverAction Class>
 * <p>
 * Description: Conflict Resolver Action class Copyright: Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 *
 * @version 1.00
 *@author kalpana Thakur Created on sep 18,2007
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
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * @author renuka_bajpai
 */
public class ConflictResolverAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(ConflictResolverAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final HttpSession session = request.getSession();
		final String participantIdToAssociate = (String) session
				.getAttribute(Constants.PARTICIPANT_ID_TO_ASSOCIATE);
		final String specimenCollGrpId = (String) session
				.getAttribute(Constants.SCG_ID_TO_ASSOCIATE);
		final String reportQueueId = request.getParameter(Constants.REPORT_ID);
		final String button = request.getParameter(Constants.CONFLICT_BUTTON);
		String errorMessage = null;

		// overwrite the existing report
		if (button.trim().equalsIgnoreCase(Constants.OVERWRITE_REPORT))
		{
			this.overwriteReport(request, reportQueueId);
		}

		// Ignore new Report
		if (button.trim().equalsIgnoreCase(Constants.IGNORE_NEW_REPORT))
		{
			this.ignoreNewReport(reportQueueId);
		}

		// Creating a new Participant
		if (button.trim().equalsIgnoreCase(Constants.CREATE_NEW_PARTICIPANT))
		{
			errorMessage = this.createNewParticipant(request, reportQueueId);
			if (errorMessage != null)
			{
				this.setActionError(request, errorMessage);
			}
		}
		else
		{
			if (button.trim().equalsIgnoreCase(Constants.USE_SELECTED_PARTICIPANT))
			{
				if (participantIdToAssociate != null && !participantIdToAssociate.equals(""))
				{
					// Associate existing participant with Report
					this.createNewSCG(request, reportQueueId, participantIdToAssociate);
				}
			}
			else if (button.trim().equalsIgnoreCase(Constants.USE_SELECTED_SCG))
			{

				if (specimenCollGrpId != null && !specimenCollGrpId.equals(""))
				{
					// Associate existing SCG with Report
					this.associateSCGWithReport(request, reportQueueId, participantIdToAssociate,
							specimenCollGrpId);
				}
			}
		}
		this.resetSessionAttributes(session);
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * To create new participant and associate it to the report.
	 *
	 * @param request
	 *            : request
	 * @param reportQueueId
	 *            : reportQueueId
	 * @return String : String
	 * @throws Exception
	 *             : Exception
	 */
	private String createNewParticipant(HttpServletRequest request, String reportQueueId)
			throws Exception
	{

		String errorMessage = null;
		ReportLoaderQueue reportLoaderQueue = null;
		reportLoaderQueue = Utility.getReportQueueObject(reportQueueId);

		final Participant participant = Utility.getParticipantFromReportLoaderQueue(reportQueueId);

		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final ParticipantBizLogic participantBizLogic = (ParticipantBizLogic) factory
				.getBizLogic(Participant.class.getName());
		try
		{
			participantBizLogic.insert(participant, this.getSessionData(request), 0);
		}
		catch (final Exception e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
			// System.out.println("Error Occurred !!!!!");
			errorMessage = ApplicationProperties.getValue("errors.caTies.conflict.genericmessage");
			// Setting the status to NEW
			// reportLoaderQueue.setParticipantCollection(null);
			// reportLoaderQueue.setStatus(CaTIESConstants.
			// PARTICIPANT_CREATION_ERROR);
			// updateReportLoaderQueue(reportLoaderQueue,request);
			return errorMessage;
		}

		final Collection participantColl = new HashSet();
		// Adding the new participant
		participantColl.add(participant);
		reportLoaderQueue.setParticipantCollection(participantColl);
		// The new SCG for this participant will be inserted by the
		// FileProcessorThread

		// Setting the status to NEW
		reportLoaderQueue.setStatus(CaTIESConstants.NEW);
		reportLoaderQueue.setSpecimenCollectionGroup(null);
		this.updateReportLoaderQueue(reportLoaderQueue, request);

		return errorMessage;
	}

	/**
	 * To associate existing participant to the report and to create new SCG.
	 *
	 * @param request
	 *            : request
	 * @param reportQueueId
	 *            : reportQueueId
	 * @param participantIdToAssociate
	 *            : participantIdToAssociate
	 * @throws NumberFormatException
	 *             : NumberFormatException
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private void createNewSCG(HttpServletRequest request, String reportQueueId,
			String participantIdToAssociate) throws NumberFormatException, BizLogicException
	{

		ReportLoaderQueue reportLoaderQueue = null;
		reportLoaderQueue = Utility.getReportQueueObject(reportQueueId);

		// Changing the status of the report in the queue to NEW
		reportLoaderQueue.setStatus(CaTIESConstants.NEW);

		// Create new SCG
		reportLoaderQueue.setSpecimenCollectionGroup(null);

		// removing all participants from CATISSUE_REPORT_PARTICIP_REL other
		// than the selected participant
		final Collection participantColl = reportLoaderQueue.getParticipantCollection();
		final Iterator iter = participantColl.iterator();
		final Set tempColl = new HashSet();
		while (iter.hasNext())
		{
			final Participant participant = (Participant) iter.next();
			if (participant.getId().toString().equals(participantIdToAssociate.trim()))
			{
				tempColl.add(participant);
			}
		}
		reportLoaderQueue.setParticipantCollection(tempColl);

		// Updating the report queue obj
		this.updateReportLoaderQueue(reportLoaderQueue, request);
	}

	/**
	 * Associate the existing SCG to the report.
	 *
	 * @param request
	 *            : request
	 * @param reportQueueId
	 *            : reportQueueId
	 * @param participantIdToAssociate
	 *            : participantIdToAssociate
	 * @param specimenCollGrpId
	 *            : specimenCollGrpId
	 * @throws DAOException
	 *             : DAOException
	 * @throws BizLogicException
	 *             : BizLogicException
	 * @throws UserNotAuthorizedException
	 *             : UserNotAuthorizedException
	 */
	private void associateSCGWithReport(HttpServletRequest request, String reportQueueId,
			String participantIdToAssociate, String specimenCollGrpId) throws DAOException,
			BizLogicException, UserNotAuthorizedException
	{
		Long cprId = null;
		ReportLoaderQueue reportLoaderQueue = null;
		reportLoaderQueue = Utility.getReportQueueObject(reportQueueId);

		// Changing the status of the report in the queue to NEW
		reportLoaderQueue.setStatus(CaTIESConstants.NEW);

		// Associating the SCG
		if (specimenCollGrpId != null && !specimenCollGrpId.equals(""))
		{
			SpecimenCollectionGroup scg = null;
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic) factory
					.getBizLogic(ReportLoaderQueue.class.getName());
			final Object object = reportLoaderQueueBizLogic.retrieve(SpecimenCollectionGroup.class
					.getName(), new Long(specimenCollGrpId));
			if (object != null)
			{
				scg = (SpecimenCollectionGroup) object;
			}
			cprId = scg.getCollectionProtocolRegistration().getId();
			reportLoaderQueue.setSpecimenCollectionGroup(scg);
		}

		// Retrieving participantID if it is null
		if (participantIdToAssociate == null || participantIdToAssociate.equals(""))
		{
			final DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
			final Long partID = (Long) defaultBizLogic.retrieveAttribute(
					CollectionProtocolRegistration.class.getName(), cprId,
					Constants.COLUMN_NAME_PARTICIPANT_ID);
			participantIdToAssociate = partID.toString();
		}

		// removing all participants from CATISSUE_REPORT_PARTICIP_REL other
		// than the selected participant
		final Collection participantColl = reportLoaderQueue.getParticipantCollection();
		final Iterator iter = participantColl.iterator();
		final Set tempColl = new HashSet();
		Participant participant = null;
		while (iter.hasNext())
		{
			participant = (Participant) iter.next();
			if (participant.getId().toString().equals(participantIdToAssociate.trim()))
			{
				tempColl.add(participant);
			}
		}
		reportLoaderQueue.setParticipantCollection(tempColl);

		// Updating the report queue obj
		this.updateReportLoaderQueue(reportLoaderQueue, request);
	}

	/**
	 * updating the reportloaderQueue obj.
	 *
	 * @param reportLoaderQueue
	 *            : reportLoaderQueue
	 * @param request
	 *            : request
	 */
	private void updateReportLoaderQueue(ReportLoaderQueue reportLoaderQueue,
			HttpServletRequest request)
	{

		try
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic) factory
					.getBizLogic(ReportLoaderQueue.class.getName());
			reportLoaderQueueBizLogic.update(reportLoaderQueue, reportLoaderQueue, 0, this
					.getSessionData(request));

		}
		catch (final Exception e)
		{
			this.logger.error("Error Updating ReportQueue" + e);
			e.printStackTrace() ;
		}
	}

	/**
	 * @param request
	 *            : request
	 * @return SessionDataBean : SessionDataBean
	 */
	@Override
	protected SessionDataBean getSessionData(HttpServletRequest request)
	{
		Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
		/**
		 * This if loop is specific to Password Security feature.
		 */
		if (obj == null)
		{
			obj = request.getSession().getAttribute(Constants.TEMP_SESSION_DATA);
		}
		if (obj != null)
		{
			final SessionDataBean sessionData = (SessionDataBean) obj;
			return sessionData;
		}
		return null;

	}

	/**
	 * To generate the errors.
	 *
	 * @param request
	 *            : request
	 * @param errorMessage
	 *            : errorMessage
	 */
	private void setActionError(HttpServletRequest request, String errorMessage)
	{
		final ActionErrors errors = new ActionErrors();
		final ActionError error = new ActionError("errors.item", errorMessage);
		errors.add(ActionErrors.GLOBAL_ERROR, error);
		this.saveErrors(request, errors);
	}

	/**
	 * @param session
	 *            : session
	 */
	protected void resetSessionAttributes(HttpSession session)
	{
		// Removing the session objects
		session.removeAttribute(Constants.PARTICIPANT_ID_TO_ASSOCIATE);
		session.removeAttribute(Constants.SCG_ID_TO_ASSOCIATE);

	}

	/**
	 * Ignore the new report and use the existing one.
	 *
	 * @param reportQueueId
	 *            : reportQueueId
	 * @throws DAOException
	 *             : DAOException
	 * @throws UserNotAuthorizedException
	 *             : UserNotAuthorizedException
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	protected void ignoreNewReport(String reportQueueId) throws DAOException,
			UserNotAuthorizedException, BizLogicException
	{
		// Long cprId = null;
		ReportLoaderQueue reportLoaderQueue = null;
		reportLoaderQueue = Utility.getReportQueueObject(reportQueueId);
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic) factory
				.getBizLogic(ReportLoaderQueue.class.getName());

		// deleting the reportloaderQueue object
		reportLoaderQueueBizLogic.delete(reportLoaderQueue, 0);

	}

	/**
	 * To overwrite the existing report.
	 *
	 * @param request
	 *            : request
	 * @param reportQueueId
	 *            : reportQueueId
	 * @throws BizLogicException
	 *             : BizLogicException
	 * @throws NumberFormatException
	 *             : NumberFormatException
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	protected void overwriteReport(HttpServletRequest request, String reportQueueId)
			throws NumberFormatException, BizLogicException
	{
		ReportLoaderQueue reportLoaderQueue = null;
		reportLoaderQueue = Utility.getReportQueueObject(reportQueueId);

		// Changing the status of the report in the queue to NEW
		reportLoaderQueue.setStatus(CaTIESConstants.OVERWRITE_REPORT);
		this.updateReportLoaderQueue(reportLoaderQueue, request);
	}
}
