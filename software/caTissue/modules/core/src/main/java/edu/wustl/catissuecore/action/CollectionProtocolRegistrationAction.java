/**
 * <p>
 * Title: DepartmentAction Class
 * </p>
 * <p>
 * Description: This class initializes the fields in the Department Add/Edit
 * webpage.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Ajay Sharma
 * @version 1.00 Created on May 23rd, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.AddNewSessionDataBean;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.daofactory.DAOConfigFactory;

/**
 * This class initializes the fields in the User Add/Edit webpage.
 *
 * @author ajay_sharma
 */

public class CollectionProtocolRegistrationAction extends SecureAction
{

	/**
	 * logger.
	 */
	private static final Logger LOGGER = Logger
			.getCommonLogger(CollectionProtocolRegistrationAction.class);
	// This will keep track of no of consents for a particular participant
	/**
	 * consentCounter.
	 */
	int consentCounter;

	private static final String NOT_NULL_STRING="is not null";
	/**
	 * Overrides the executeSecureAction method of SecureAction class.
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
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final CollectionProtocolRegistrationForm collProtRegForm = (CollectionProtocolRegistrationForm) form;
		// Gets the value of the operation parameter.
		final String operation = request.getParameter(Constants.OPERATION);
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		// CollectionProtocolId
		// Consent Tracking (Virender Mehta)
		String selectedCPId = null;
		if (request.getParameter(Constants.CP_SEARCH_CP_ID) != null)
		{
			final long collProtId = Long.valueOf(request
					.getParameter(Constants.CP_SEARCH_CP_ID));
			try
			{
				((CollectionProtocolRegistrationForm) form)
						.setCollectionProtocolID(collProtId);
			}
			catch (final ClassCastException exp)
			{
				LOGGER.debug("Class cast Exception in"
						+ " CollectionProtocolRegistrationAction :" + exp);
			}
		}
		selectedCPId = String.valueOf(collProtRegForm
				.getCollectionProtocolID());
		// Adding name,value pair in NameValueBean
		// Getting witness name list for CollectionProtocolID
		final List witnessList = ConsentUtil.witnessNameList(selectedCPId);
		// Getting ResponseList if Operation=Edit then "Withdraw" is added to
		// the List
		final List responseList = AppUtility.responceList(operation);
		final Set consentList = (Set) ConsentUtil.getConsentList(selectedCPId);
		final List reqConsentList = new ArrayList(consentList);
		if (operation.equalsIgnoreCase(Constants.ADD))
		{
			final ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
			if (errors == null)
			{
				final Map tempMap = this.prepareConsentMap(reqConsentList);
				collProtRegForm.setConsentResponseValues(tempMap);
			}
			collProtRegForm.setConsentTierCounter(reqConsentList.size());
		}
		else
		{
			final String cprID = String.valueOf(collProtRegForm.getId());
			final CollectionProtocolRegistration collProtReg = AppUtility
					.getcprObj(cprID);
			// List added for grid
			final List specimenDetails = new ArrayList();
			ConsentUtil.getSpecimenDetails(collProtReg, specimenDetails);
			final List<String> columnList = ConsentUtil.columnNames();
			// Lazy Resolved ---
			// collectionProtocolRegistration.getConsentTierResponseCollection
			// ();
			final Collection consentResponse = (Collection) bizLogic.retrieveAttribute(
					CollectionProtocolRegistration.class.getName(), collProtReg
							.getId(), "elements(consentTierResponseCollection)");
			final Map tempMap = this.prepareMap(consentResponse);
			collProtRegForm.setConsentResponseValues(tempMap);
			collProtRegForm.setConsentTierCounter(this.consentCounter);
			final HttpSession session = request.getSession();
			session.setAttribute(Constants.SPECIMEN_LIST, specimenDetails);
			session.setAttribute(Constants.COLUMNLIST, columnList);
		}
		request.setAttribute("witnessList", witnessList);
		request.setAttribute("responseList", responseList);
		// Consent Tracking Virender Mehta
		// Sets the operation attribute to be used in the Add/Edit User Page.

		request.setAttribute(Constants.OPERATION, operation);
		if (operation.equalsIgnoreCase(Constants.ADD))
		{
			final CollectionProtocolRegistrationForm cpform = (CollectionProtocolRegistrationForm) form;
			cpform.setId(0);
			if (cpform.getRegistrationDate() == null)
			{
				cpform.setRegistrationDate(CommonUtilities.parseDateToString(Calendar.getInstance()
						.getTime(), CommonServiceLocator.getInstance().getDatePattern()));
			}
		}

		// Sets the pageOf attribute
		final String pageOf = request.getParameter(Constants.PAGE_OF);

		request.setAttribute(Constants.PAGE_OF, pageOf);

		// Mandar : code for Addnew Collection Protocol data 24-Jan-06
		// String collectionProtocolID =
		// (String)request.getAttribute(Constants.ADD_NEW_COLLECTION_PROTOCOL_ID
		// );
		// if(collectionProtocolID != null &&
		// collectionProtocolID.trim().length() > 0 )
		// {
		// Logger.out.debug(
		// ">>>>>>>>>>><<<<<<<<<<<<<<<<>>>>>>>>>>>>> CP ID in CPR : "+
		// collectionProtocolID );
		//((CollectionProtocolRegistrationForm)form).setCollectionProtocolID(Long
		// .parseLong(collectionProtocolID));
		// }
		// Mandar -- 24-Jan-06 end

		// String reqPath = request.getParameter(Constants.REQ_PATH);
		// if (reqPath != null)
		// request.setAttribute(Constants.REQ_PATH, reqPath);
		//
		// Logger.out.debug("PartProtReg redirect :---------- "+ reqPath );
		//
		// ----------------add new end-----

		// get list of Protocol title.
		String sourceObjectName = CollectionProtocol.class.getName();

		// Smita changes start
		final String[] displayNameFields = {"shortTitle"};
		// Smita changes end

		final String valueField = Constants.SYSTEM_IDENTIFIER;
		List list = bizLogic.getList(sourceObjectName, displayNameFields, valueField, true);
		request.setAttribute(Constants.PROTOCOL_LIST, list);

		LOGGER.debug("SubmittedFor on CPRAction====>"
				+ request.getAttribute(Constants.SUBMITTED_FOR));
		if ((request.getAttribute(Constants.SUBMITTED_FOR) != null)
				&& (request.getAttribute(Constants.SUBMITTED_FOR).equals("AddNew")))
		{
			final HttpSession session = request.getSession();
			final Stack formBeanStack = (Stack) session.getAttribute(Constants.FORM_BEAN_STACK);

			if (formBeanStack != null)
			{
				try
				{
					final AddNewSessionDataBean newSessionBean = (AddNewSessionDataBean) formBeanStack
							.peek();

					final SpecimenCollectionGroupForm sessionFormBean = (SpecimenCollectionGroupForm) newSessionBean
							.getAbstractActionForm();

					((CollectionProtocolRegistrationForm) form)
							.setCollectionProtocolID(sessionFormBean.getCollectionProtocolId());
				}
				catch (final ClassCastException exp)
				{
					LOGGER.debug("Class cast Exception in"
							+ " CollectionProtocolRegistrationAction ~>" + exp);
				}
			}
		}

		// if(request.getParameter(Constants.CP_SEARCH_CP_ID)!=null)
		// {
		// long cpSearchCpId = new
		// Long(request.getParameter(Constants.CP_SEARCH_CP_ID)).longValue();
		// try
		// {
		// ((CollectionProtocolRegistrationForm)form).setCollectionProtocolID(
		// cpSearchCpId);
		// }
		// catch(ClassCastException exp)
		// {
		// Logger.out.debug(
		// "Class cast Exception in CollectionProtocolRegistrationAction ~~~~~~~~~~~~~~~~~~~~~~~>"
		// +exp);
		// }
		// }

		// get list of Participant's names
		sourceObjectName = Participant.class.getName();
		final String[] partsFields = {"lastName", "firstName", "birthDate",
				"socialSecurityNumber"};
		final String[] whereColumnName = {"lastName", "firstName", "birthDate",
				"socialSecurityNumber"};
		String[] whereClmCondition;
		Object[] whereColumnValue;

		// get Database name and set conditions

		final String appName = CommonServiceLocator.getInstance().getAppName();
		final String databaseType = DAOConfigFactory.getInstance().getDAOFactory(appName)
				.getDataBaseType();

		if (databaseType.equals(Constants.MYSQL_DATABASE))
		{
			whereClmCondition = new String[]{"!=", "!=", NOT_NULL_STRING, NOT_NULL_STRING};
			whereColumnValue = new String[]{"", ""};
		}
		else if (databaseType.equals(Constants.MSSQLSERVER_DATABASE))
		{
			// for MsSqlServer DB.
			whereClmCondition = new String[]{"!=", "!=", NOT_NULL_STRING, NOT_NULL_STRING};
			whereColumnValue = new String[]{"", ""};
		}
		else
		{
			// for ORACLE
			whereClmCondition = new String[]{NOT_NULL_STRING, NOT_NULL_STRING, NOT_NULL_STRING,
					NOT_NULL_STRING};
			whereColumnValue = new String[]{};
		}

		final String joinCondition = Constants.OR_JOIN_CONDITION;
		final String fieldSeparator = ", ";

		list = bizLogic.getList(sourceObjectName, partsFields, valueField, whereColumnName,
				whereClmCondition, whereColumnValue, joinCondition, fieldSeparator,
				false);

		// get list of Disabled Participants
		final String[] partFields2 = {Constants.SYSTEM_IDENTIFIER};
		final String[] whereColumnName2 = {"activityStatus"};
		final String[] whereClmCondition2 = {"="};
		final String[] whereColumnValue2 = {Status.ACTIVITY_STATUS_DISABLED.toString()};
		final String joinCondition2 = Constants.AND_JOIN_CONDITION;
		final String fieldSeparator2 = ",";

		final List disabledParts = bizLogic.getList(sourceObjectName,
				partFields2, valueField, whereColumnName2, whereClmCondition2,
				whereColumnValue2, joinCondition2, fieldSeparator2, false);

		// removing disabled participants from the list of Participants
		list = this.removeDisabledParticipant(list, disabledParts);

		// Sets the participantList attribute to be used in the Site Add/Edit
		// Page.
		request.setAttribute(Constants.PARTICIPANT_LIST, list);

		// Sets the activityStatusList attribute to be used in the Site Add/Edit
		// Page.
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);

		// ************* ForwardTo implementation *************
		final HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");

		if (forwardToHashMap != null)
		{
			final Long participantId = (Long) forwardToHashMap.get("participantId");
			Logger.out.debug("ParticipantID found in forwardToHashMap========>>>>>>"
					+ participantId);

			if ((request.getParameter("firstName").trim().length() > 0)
					|| (request.getParameter("lastName").trim().length() > 0)
					|| (request.getParameter("birthDate").trim().length() > 0)
					|| ((request.getParameter("socialSecurityNumberPartA").trim().length() > 0)
							&& (request.getParameter("socialSecurityNumberPartB").trim().length() > 0) && (request
							.getParameter("socialSecurityNumberPartC").trim().length() > 0)))
			{
				final CollectionProtocolRegistrationForm cprForm = (CollectionProtocolRegistrationForm) form;
				cprForm.setParticipantID(participantId.longValue());
				// cprForm.setCheckedButton(true);
				// Bug-2819: Performance issue due to participant drop down:
				// Jitendra
				final Object object = bizLogic.retrieve(sourceObjectName, participantId);
				if (object != null)
				{
					final Participant participant = (Participant) object;
					cprForm.setParticipantName(participant.getMessageLabel());
				}

			}
		}
		else
		{
			if (request.getParameter("participantId") != null)
			{
				try
				{
					final Long participantId = Long.valueOf(request.getParameter("participantId"));
					final CollectionProtocolRegistrationForm cprForm = (CollectionProtocolRegistrationForm) form;
					cprForm.setParticipantID(participantId.longValue());
					// cprForm.setCheckedButton(true);
					final Object object = bizLogic.retrieve(sourceObjectName, participantId);
					if (object != null)
					{
						final Participant participant = (Participant) object;
						cprForm.setParticipantName(participant.getMessageLabel());
					}
				}
				catch (final NumberFormatException e)
				{
					LOGGER.debug("NumberFormatException Occured :" + e);
				}
			}

			// Bug- 2819 : Jitendra
			if (((CollectionProtocolRegistrationForm) form).getParticipantID() != 0)
			{
				try
				{
					final Long participantId = Long.valueOf(((CollectionProtocolRegistrationForm) form)
							.getParticipantID());
					final CollectionProtocolRegistrationForm cprForm = (CollectionProtocolRegistrationForm) form;
					cprForm.setParticipantID(participantId.longValue());
					// cprForm.setCheckedButton(true);
					final Object object = bizLogic.retrieve(sourceObjectName, participantId);
					if (object != null)
					{
						final Participant participant = (Participant) object;
						cprForm.setParticipantName(participant.getMessageLabel());
					}
				}
				catch (final NumberFormatException e)
				{
					LOGGER.debug("NumberFormatException Occured :" + e);
				}
			}
		}
		// ************* ForwardTo implementation *************

		/*
		 * Logger.out.info("--------------------------------- caching ---------------"
		 * ); CollectionProtocolRegistrationBizLogic cBizLogic =
		 * (CollectionProtocolRegistrationBizLogic
		 * )BizLogicFactory.getInstance().
		 * getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID); List
		 * list12 =cBizLogic.getAllParticipantRegistrationInfo();
		 * Logger.out.info
		 * ("--------------------------------- caching end ---------------");
		 */

		if (request.getParameter(Constants.OPERATION).equals(Constants.EDIT))
		{
			final CollectionProtocolRegistrationForm cprForm = (CollectionProtocolRegistrationForm) form;
			String participantId = String.valueOf(cprForm.getParticipantID());
			if (cprForm.getParticipantID() == 0 && cprForm.getParticipantProtocolID() != null)
			{
				participantId = this.getParticipantIdForProtocolId(cprForm
						.getParticipantProtocolID(), bizLogic);
			}
			request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID, participantId);
		}

		return mapping.findForward(pageOf);
	}

	/**
	 * @param listOfParticipant
	 *            list of participant
	 * @param listOfDisabledParticipant
	 *            list of disabled participant
	 * @return list of participant those having activityStatus not as disabled
	 */
	private List removeDisabledParticipant(List listOfParticipant, List listOfDisabledParticipant)
	{
		final List listOfActiveParticipant = new ArrayList();

		Logger.out.debug("No. Of Participants ~~~~~~~~~~~~~~~~~~~~~~~>" + listOfParticipant.size());
		Logger.out.debug("No. Of Disabled Participants ~~~~~~~~~~~~~~~~~~~~~~~>"
				+ listOfDisabledParticipant.size());

		listOfActiveParticipant.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
		for (int i = 0; i < listOfParticipant.size(); i++)
		{
			final NameValueBean participantBean = (NameValueBean) listOfParticipant.get(i);
			boolean isParticipantDisable = false;

			if (Long.parseLong(participantBean.getValue()) == -1)
			{
				// listOfActiveParticipant.add(listOfParticipant.get(i));
				continue;
			}

			for (int j = 0; j < listOfDisabledParticipant.size(); j++)
			{
				if (Long.parseLong(((NameValueBean) listOfDisabledParticipant.get(j)).getValue()) == -1)
				{
					continue;
				}

				final NameValueBean disabledParticipant = (NameValueBean) listOfDisabledParticipant
						.get(j);
				if (participantBean.getValue().equals(disabledParticipant.getValue()))
				{
					isParticipantDisable = true;
					break;
				}
			}
			if (!isParticipantDisable)
			{
				listOfActiveParticipant.add(listOfParticipant.get(i));
			}
		}

		LOGGER.debug("No.Of Active Participants ~~~~~~~~~~~~~~~~~~~~~~~>"
				+ listOfActiveParticipant.size());

		return listOfActiveParticipant;
	}

	/**
	 * @param participantProtocolId
	 *            String value for participant Protocol id
	 * @param bizLogic
	 *            object of class implementing IBizLogic
	 * @return String for respective participant Id
	 * @throws Exception
	 *             generic exception
	 */
	private String getParticipantIdForProtocolId(String participantProtocolId, IBizLogic bizLogic)
			throws Exception
	{
		final String sourceObjectName = CollectionProtocolRegistration.class.getName();
		final String[] selectColumnName = {"participant.id"};
		final String[] whereColumnName = {"protocolParticipantIdentifier"};
		final String[] whereColumnCondition = {"="};
		final Object[] whereColumnValue = {participantProtocolId};
		final List participantList = bizLogic.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue,
				Constants.AND_JOIN_CONDITION);
		String partId = null;
		if (participantList != null && !participantList.isEmpty())
		{

			partId = ((Long) participantList.get(0)).toString();

		}
		return partId;
	}

	/**
	 * Prepare map for Showing Consents for a CollectionprotocolID when
	 * Operation=Add.
	 *
	 * @param partiResponseCollection : partiResponseCollection
	 * @return Map : tempMap
	 */
	private Map prepareMap(Collection partiResponseCollection)
	{
		Map tempMap = null;
		if (partiResponseCollection != null)
		{
			tempMap = new LinkedHashMap();
			int iinternalCtr = 0;
			final Iterator consentResponseCollectionIter = partiResponseCollection.iterator();
			String idKey = null;
			String statementKey = null;
			String responsekey = null;
			String participantResponceIdKey = null;
			Long consentTierID;
			Long consentID;
			while (consentResponseCollectionIter.hasNext())
			{
				final ConsentTierResponse consentTierResponse = (ConsentTierResponse) consentResponseCollectionIter
						.next();
				final ConsentTier consent = consentTierResponse.getConsentTier();
				consentTierID = consentTierResponse.getConsentTier().getId();
				consentID = consent.getId();
				if (consentTierID.longValue() == consentID.longValue())
				{
					idKey = "ConsentBean:" + iinternalCtr + "_consentTierID";
					statementKey = "ConsentBean:" + iinternalCtr + "_statement";
					responsekey = "ConsentBean:" + iinternalCtr + "_participantResponse";
					participantResponceIdKey = "ConsentBean:" + iinternalCtr + "_participantResponseID";
					tempMap.put(idKey, consent.getId());
					tempMap.put(statementKey, consent.getStatement());
					tempMap.put(responsekey, consentTierResponse.getResponse());
					tempMap.put(participantResponceIdKey, consentTierResponse.getId());
					iinternalCtr++;
				}
			}
			this.consentCounter = iinternalCtr;

		}
		return tempMap;
	}

	/**
	 * Prepare map for Showing Consents for a CollectionprotocolID when
	 * Operation=Add.
	 *
	 * @param requestConsentList
	 *            This is the List of Consents for a selected
	 *            CollectionProtocolID
	 * @return tempMap
	 */
	private Map prepareConsentMap(List requestConsentList)
	{
		final Map tempMap = new HashMap();
		if (requestConsentList != null)
		{
			int internalCtr = 0;
			final Iterator consentTierCollectionIter = requestConsentList.iterator();
			String idKey = null;
			String statementKey = null;
			while (consentTierCollectionIter.hasNext())
			{
				final ConsentTier consent = (ConsentTier) consentTierCollectionIter.next();
				idKey = "ConsentBean:" + internalCtr + "_consentTierID";
				statementKey = "ConsentBean:" + internalCtr + "_statement";

				tempMap.put(idKey, consent.getId());
				tempMap.put(statementKey, consent.getStatement());
				internalCtr++;
			}
		}
		return tempMap;
	}
}