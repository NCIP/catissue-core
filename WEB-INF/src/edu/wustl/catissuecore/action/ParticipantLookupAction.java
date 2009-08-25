/**
 * <p>
 * Title: ParticipantLookupAction Class>
 * <p>
 * Description: This Action Class invokes the Participant Lookup Algorithm and
 * gets matching participants
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author vaishali_khandelwal
 * @Created on May 19, 2006
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IDomainObjectFactory;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;

/**
 * @author renuka_bajpai
 */
public class ParticipantLookupAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(ParticipantLookupAction.class);

	/**
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
	 * @return value for ActionForward object
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionMessages messages = null;
		String target = null;
		final StringBuffer partMRNColName = new StringBuffer("");

		final AbstractActionForm abstractForm = (AbstractActionForm) form;

		final IDomainObjectFactory factoryObj = AbstractFactoryConfig.getInstance()
				.getDomainObjectFactory();

		final AbstractDomainObject abstractDomain = factoryObj.getDomainObject(abstractForm
				.getFormId(), abstractForm);
		final Participant participant = (Participant) abstractDomain;
		// 11968 S
		if (!this.isAuthorized(mapping, request, participant))
		{
			final ActionErrors errors = new ActionErrors();
			final ActionError error = new ActionError("access.execute.action.denied");
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			this.saveErrors(request, errors);
			return mapping.findForward("failure");
		}
		// 11968 E
		this.logger.debug("Participant Id :" + request.getParameter("participantId"));
		// checks weather participant is selected from the list and so
		// forwarding to next action instead of participant lookup.
		// Abhishek Mehta
		if (request.getAttribute("continueLookup") == null)
		{
			if (request.getParameter("participantId") != null
					&& !request.getParameter("participantId").equals("null")
					&& !request.getParameter("participantId").equals("")
					&& !request.getParameter("participantId").equals("0"))
			{
				this.logger.info("inside the participant mapping");
				return mapping.findForward("participantSelect");
			}
		}

		final boolean isCallToLookupLogicNeeded = this.isCallToLookupLogicNeeded(participant);

		if (isCallToLookupLogicNeeded)
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final ParticipantBizLogic bizlogic = (ParticipantBizLogic) factory
					.getBizLogic(Constants.PARTICIPANT_FORM_ID);
			final LookupLogic participantLookupLogic = (LookupLogic) CommonUtilities
					.getObject(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_ALGO));
			final List matchingParticipantList = bizlogic.getListOfMatchingParticipants(
					participant, participantLookupLogic);
			if (matchingParticipantList != null && matchingParticipantList.size() > 0)
			{
				messages = new ActionMessages();
				messages
						.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"participant.lookup.success",
								"Submit was not successful because some matching"
										+ " participants found."));
				// Creating the column headings for Data Grid
				final List columnList = this.getColumnHeadingList(bizlogic, partMRNColName);
				request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnList);
				request.setAttribute(Constants.PARTICIPANT_MRN_COL_NAME, partMRNColName);

				// Getitng the Participant List in Data Grid Format
				final List participantDisplayList = this.getParticipantDisplayList(
						matchingParticipantList, bizlogic);
				request.setAttribute(edu.wustl.simplequery.global.Constants.SPREADSHEET_DATA_LIST,
						participantDisplayList);

				target = Constants.PARTICIPANT_LOOKUP_SUCCESS;
			}
			// if no participant match found then add the participant in system
			else
			{
				target = Constants.PARTICIPANT_ADD_FORWARD;
			}
		}
		else
		{
			target = Constants.PARTICIPANT_ADD_FORWARD;
		}

		// if any matching participants are there then show the participants
		// otherwise add the participant

		// setting the Submitted_for and Forward_to variable in request
		if (request.getParameter(Constants.SUBMITTED_FOR) != null
				&& !request.getParameter(Constants.SUBMITTED_FOR).equals(""))
		{
			request.setAttribute(Constants.SUBMITTED_FOR, request
					.getParameter(Constants.SUBMITTED_FOR));
		}
		if (request.getParameter(Constants.FORWARD_TO) != null
				&& !request.getParameter(Constants.FORWARD_TO).equals(""))
		{
			request.setAttribute(Constants.FORWARD_TO, request.getParameter(Constants.FORWARD_TO));

		}

		request.setAttribute("participantId", "");
		if (request.getAttribute("continueLookup") == null)
		{
			if (messages != null)
			{
				this.saveMessages(request, messages);
			}
		}
		this.logger.debug("target:" + target);
		return (mapping.findForward(target));
	}

	// 11968 S
	/**
	 *
	 * @param mapping : mapping
	 * @param request : request
	 * @param participant : participant
	 * @return boolean : boolean
	 */
	private boolean isAuthorized(ActionMapping mapping, HttpServletRequest request,
			Participant participant)
	{
		DAO dao = null;
		final SessionDataBean sessionDataBean = this.getSessionData(request);
		boolean authorizedFlag = false;
		try
		{
			dao = AppUtility.openDAOSession(null);
			final ParticipantBizLogic biz = new ParticipantBizLogic();
			authorizedFlag = biz.isAuthorized(dao, participant, sessionDataBean);

		}
		catch (final Exception e)
		{
			this.logger.error("Exception occured : " + e.getMessage(), e);
			authorizedFlag = false;
		}
		finally
		{
			try
			{
				AppUtility.closeDAOSession(dao);
			}
			catch (final ApplicationException e)
			{
				this.logger.error("Exception occured : " + e.getMessage(), e);
			}
		}
		return authorizedFlag;
	}

	// 11968 E
	/**
	 *
	 * @param participant : participant
	 * @return boolean : boolean
	 */

	private boolean isCallToLookupLogicNeeded(Participant participant)
	{
		if ((participant.getFirstName() == null || participant.getFirstName().length() == 0)
				&& (participant.getMiddleName() == null || participant.getMiddleName().length() == 0)
				&& (participant.getLastName() == null || participant.getLastName().length() == 0)
				&& (participant.getSocialSecurityNumber() == null || participant
						.getSocialSecurityNumber().length() == 0)
				&& participant.getBirthDate() == null
				&& (participant.getParticipantMedicalIdentifierCollection() == null || participant
						.getParticipantMedicalIdentifierCollection().size() == 0))
		{
			return false;
		}
		return true;
	}

	/**
	 * This Function creates the Column Headings for Data Grid.
	 *
	 * @param bizlogic
	 *            instance of ParticipantBizLogic
	 * @param partMRNColName : partMRNColName
	 * @throws Exception
	 *             generic exception
	 * @return List Column List
	 */
	private List getColumnHeadingList(ParticipantBizLogic bizlogic, StringBuffer partMRNColName)
			throws Exception
	{
		// Creating the column list which is used in Data grid to display column
		// headings
		final String[] columnHeaderList = new String[]{Constants.PARTICIPANT_MEDICAL_RECORD_NO,
				Constants.PARTICIPANT_GENDER, Constants.PARTICIPANT_BIRTH_DATE,
				Constants.PARTICIPANT_SOCIAL_SECURITY_NUMBER, Constants.PARTICIPANT_DEATH_DATE,
				Constants.PARTICIPANT_VITAL_STATUS};
		final List columnList = new ArrayList();
		this.logger.info("column List header size ;" + columnHeaderList.length);
		for (final String element : columnHeaderList)
		{
			columnList.add(element);
		}
		this.logger.info("column List size ;" + columnList.size());
		final List displayList = bizlogic.getColumnList(columnList, partMRNColName);

		displayList.add(0, Constants.PARTICIPANT_NAME_HEADERLABEL);
		// displayList.add(0,Constants.PARTICIPANT_PROBABLITY_MATCH);
		return displayList;
	}

	/**
	 * 	 * This functions creates Particpant List with each participant informaton
		 * with the match probablity.
	 * @param participantList : participantList
	 * @param bizLogic : bizLogic
	 * @return List : List
	 * @throws BizLogicException : BizLogicException
	 */
	private List getParticipantDisplayList(List participantList, ParticipantBizLogic bizLogic)
			throws BizLogicException
	{
		final List participantDisplayList = new ArrayList();
		final Iterator<DefaultLookupResult> itr = participantList.iterator();
		while (itr.hasNext())
		{
			final DefaultLookupResult result = itr.next();
			final Participant participant = (Participant) result.getObject();
			final List participantInfo = this.getParticipantInfo(bizLogic, participant);
			participantDisplayList.add(participantInfo);
		}
		return participantDisplayList;
	}

	/**
	 *
	 * @param bizLogic : bizLogic
	 * @param participant : participant
	 * @return List : List
	 * @throws BizLogicException : BizLogicException
	 */
	private List getParticipantInfo(ParticipantBizLogic bizLogic, Participant participant)
			throws BizLogicException
	{
		final StringBuffer participantName = new StringBuffer();
		final List participantInfo = new ArrayList();
		final String partLastName = CommonUtilities.toString(participant.getLastName());
		final String partFirstName = CommonUtilities.toString(participant.getFirstName());
		participantName.append(partLastName);
		if (partLastName != null && !(("").equals(partLastName)) && partFirstName != null
				&& !("").equals(partFirstName))
		{
			final String stringCharAppend = "~";
			participantName.append(stringCharAppend);
		}
		participantName.append(partFirstName);
		participantInfo.add(participantName.toString());
		final String mrn = this.getParticipantMrnDisplay(bizLogic, participant);
		participantInfo.add(CommonUtilities.toString(mrn));
		participantInfo.add(CommonUtilities.toString(participant.getGender()));

		// participantInfo.add(Utility.toString(participant.getBirthDate()));
		// Added by Geeta for date format change.
		participantInfo.add(CommonUtilities.parseDateToString(participant.getBirthDate(),
				CommonServiceLocator.getInstance().getDatePattern()));
		// End by geeta
		if (!Variables.isSSNRemove)
		{
			participantInfo.add(CommonUtilities.toString(participant.getSocialSecurityNumber()));
		}
		// participantInfo.add(Utility.toString(participant.getDeathDate()));
		// Added by Geeta for date format change.
		participantInfo.add(CommonUtilities.parseDateToString(participant.getDeathDate(),
				CommonServiceLocator.getInstance().getDatePattern()));
		// End by Geeta
		participantInfo.add(CommonUtilities.toString(participant.getVitalStatus()));
		participantInfo.add(participant.getId());
		return participantInfo;
	}

	/**
	 *
	 * @param bizLogic : bizLogic
	 * @param participant : participant
	 * @return String : String
	 * @throws BizLogicException : BizLogicException
	 */
	private String getParticipantMrnDisplay(ParticipantBizLogic bizLogic, Participant participant)
			throws BizLogicException
	{
		final StringBuffer mrn = new StringBuffer();
		Long siteId;
		String siteName;
		if (participant.getParticipantMedicalIdentifierCollection() != null)
		{
			final Iterator<ParticipantMedicalIdentifier> pmiItr = participant
					.getParticipantMedicalIdentifierCollection().iterator();
			while (pmiItr.hasNext())
			{
				final ParticipantMedicalIdentifier participantMedicalIdentifier = pmiItr.next();
				if (participantMedicalIdentifier.getSite() != null
						&& participantMedicalIdentifier.getSite().getId() != null)
				{
					siteId = participantMedicalIdentifier.getSite().getId();
					final Site site = (Site) bizLogic.retrieve(Site.class.getName(), siteId);
					siteName = site.getName();
					mrn.append(siteName);
					final String stringCharAppend = ":";
					mrn.append(stringCharAppend);
					mrn.append(participantMedicalIdentifier.getMedicalRecordNumber());
					mrn.append("\n" + "<br>");
				}
			}
		}
		return mrn.toString();
	}
}