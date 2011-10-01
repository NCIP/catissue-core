/**
 * <p>
 * Title: SpecimenCollectionGroupAction Class>
 * <p>
 * Description: SpecimenCollectionGroupAction initializes the fields in the New
 * Specimen Collection Group page.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Ajay Sharma
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.xmi.AnnotationUtil;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.cdms.integrator.CatissueCdmsIntegrator;
import edu.wustl.catissuecore.cdms.integrator.CatissueCdmsURLInformationObject;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.CDMSIntegrationConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;

// TODO: Auto-generated Javadoc
/**
 * SpecimenCollectionGroupAction initializes the fields in the New Specimen
 * Collection Group page.
 *
 * @author ajay_sharma
 */
public class SpecimenCollectionGroupAction extends SecureAction
{

	/** logger. */
	private static final Logger LOGGER = Logger
			.getCommonLogger(SpecimenCollectionGroupAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 *
	 * @return ActionForward : ActionForward
	 *
	 * @throws Exception : Exception
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, final ActionForm form,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{
		DAO dao = null;
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		try
		{
			final SessionDataBean sessionData = (SessionDataBean) request.getSession()
					.getAttribute(Constants.SESSION_DATA);
			dao = AppUtility.openDAOSession(sessionData);
			// changes made by Baljeet
			final String treeRefresh = request.getParameter("refresh");
			request.setAttribute("refresh", treeRefresh);
			final String test = (String) request.getParameter("clinicalDiagnosis");
			final SpecimenCollectionGroupForm specimenCollectionGroupForm = (SpecimenCollectionGroupForm) form;
			request.setAttribute("clinicalDiagnosis", specimenCollectionGroupForm
					.getClinicalDiagnosis());
			specimenCollectionGroupForm.setClinicalDiagnosis(test);
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic bizLogicObj = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
			LOGGER.debug("SCGA : " + specimenCollectionGroupForm.getId());
			String nodeId = null;
			/**
			 * Bug id : 4213 Patch id : 4213_2 Description : getting parameters from
			 * request and keeping them in seesion to keep the node in tree
			 * selected.
			 */
			if (request.getParameter("clickedNodeId") != null)
			{
				nodeId = request.getParameter("clickedNodeId");
				request.getSession().setAttribute("nodeId", nodeId);
			}
			// set the menu selection
			request.setAttribute(Constants.MENU_SELECTED, "14");

			// Gets the value of the operation parameter.
			final String operation = (String) request.getParameter(Constants.OPERATION);

			// Sets the operation attribute to be used in the Edit/View Specimen
			// Collection Group Page in Advance Search Object View.
			request.setAttribute(Constants.OPERATION, operation);
			if (operation.equalsIgnoreCase(Constants.ADD))
			{
				specimenCollectionGroupForm.setId(0);
				this.LOGGER.debug("SCGA : set to 0 " + specimenCollectionGroupForm.getId());
			}

			boolean isOnChange = false;
			final String str = request.getParameter("isOnChange");
			if (str != null && "true".equals(str))
			{
				isOnChange = true;
			}
			// For Consent Tracking (Virender Mehta) - Start

			// If radioButtonSelected = 1 then selected radio button is for
			// Participant
			// If radioButtonSelected = 2 then selected radio button is for Protocol
			// Participant Identifier
			// int radioButtonSelected = 1;
			// Id of Selected Participant or Protocol Participant Identifier
			String selectedParticipantOrPPIdentifierId = null;
			// Radio button for Protocol Participant Identifier or Participant
			String typeRadioButton = null;
			String selectedCPId = String.valueOf(specimenCollectionGroupForm
					.getCollectionProtocolId());
			if (selectedCPId.equalsIgnoreCase(Constants.SELECTED_COLLECTION_PROTOCOL_ID))
			{
				final Map forwardToHashMap = (Map) request
						.getAttribute(Constants.FORWARD_TO_HASHMAP);
				if (forwardToHashMap != null)
				{
					selectedCPId = forwardToHashMap.get(Constants.COLLECTION_PROTOCOL_ID)
							.toString();
					selectedParticipantOrPPIdentifierId = forwardToHashMap.get(
							Constants.PARTICIPANT_ID).toString();
					typeRadioButton = Constants.PARTICIPANT_ID;
					if ("0".equals(selectedParticipantOrPPIdentifierId))
					{
						selectedParticipantOrPPIdentifierId = forwardToHashMap.get(
								Constants.PARTICIPANT_PROTOCOL_ID).toString();
						typeRadioButton = Constants.PARTICIPANT_PROTOCOL_ID;
					}
				}
			}
			else
			{
				// radioButtonSelected = (int)
				// specimenCollectionGroupForm.getRadioButtonForParticipant();
				// if (radioButtonSelected == 1)
				if (specimenCollectionGroupForm.getParticipantId() > 0)
				{
					selectedParticipantOrPPIdentifierId = Long.toString(specimenCollectionGroupForm
							.getParticipantId());
					typeRadioButton = Constants.PARTICIPANT_ID;
				}
				else
				{
					selectedParticipantOrPPIdentifierId = specimenCollectionGroupForm
							.getProtocolParticipantIdentifier();
					typeRadioButton = Constants.PARTICIPANT_PROTOCOL_ID;
				}
			}
			CollectionProtocolRegistration collectionProtocolRegistration = null;
			if (selectedParticipantOrPPIdentifierId != null
					&& !("0".equals(selectedParticipantOrPPIdentifierId)))
			{
				// Get CollectionprotocolRegistration Object
				collectionProtocolRegistration = this.getcollectionProtocolRegistrationObj(
						selectedParticipantOrPPIdentifierId, selectedCPId, typeRadioButton, dao);
			}
			else if (specimenCollectionGroupForm.getId() != 0)
			{
				// Get CollectionprotocolRegistration Object
				//				final SpecimenCollectionGroupBizLogic specimenCollectiongroupBizLogic = (SpecimenCollectionGroupBizLogic) factory
				//						.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
				final List cpRegnList = dao.retrieveAttribute(SpecimenCollectionGroup.class, "id",
						specimenCollectionGroupForm.getId(), "collectionProtocolRegistration");
				if ((cpRegnList != null) && (!cpRegnList.isEmpty()))
				{
					collectionProtocolRegistration = (CollectionProtocolRegistration) cpRegnList
							.get(0);
				}
			}
			User witness = null;
			if (collectionProtocolRegistration.getId() != null)
			{
				final List userList = dao.retrieveAttribute(CollectionProtocolRegistration.class,
						"id", collectionProtocolRegistration.getId(), "consentWitness");
				if ((userList != null) && (!userList.isEmpty()))
				{
					witness = (User) userList.get(0);
				}
			}
			// User witness= userObj.getConsentWitness();
			// Resolved Lazy
			if (witness == null || witness.getFirstName() == null)
			{
				final String witnessName = "";
				specimenCollectionGroupForm.setWitnessName(witnessName);
			}
			else
			{
				final String witnessFullName = witness.getLastName() + ", "
						+ witness.getFirstName();
				specimenCollectionGroupForm.setWitnessName(witnessFullName);
			}
			final String getConsentDate = Utility
					.parseDateToString(collectionProtocolRegistration.getConsentSignatureDate(),
							CommonServiceLocator.getInstance().getDatePattern());
			specimenCollectionGroupForm.setConsentDate(getConsentDate);
			final String getSignedConsentURL = Utility.toString(collectionProtocolRegistration
					.getSignedConsentDocumentURL());
			specimenCollectionGroupForm.setSignedConsentUrl(getSignedConsentURL);
			// Set witnessName,ConsentDate and SignedConsentURL
			// Resolved Lazy
			// ----collectionProtocolRegistration.getConsentTierResponseCollection()

			final List consentTierResponseList = (List) dao.retrieveAttribute(
					CollectionProtocolRegistration.class, "id", collectionProtocolRegistration
							.getId(), "elements(consentTierResponseCollection)");

			if (consentTierResponseList != null)
			{
				//Set participantResponseSet = (Set) consentTierResponseList;

				final List participantResponseList = new ArrayList(consentTierResponseList);
				specimenCollectionGroupForm
						.setCollectionProtocolRegistrationId(collectionProtocolRegistration.getId());
				if (operation.equalsIgnoreCase(Constants.ADD))
				{
					final ActionErrors errors = (ActionErrors) request
							.getAttribute(Globals.ERROR_KEY);
					if (errors == null)
					{
						final String protocolEventID = request
								.getParameter(Constants.PROTOCOL_EVENT_ID);
						if (protocolEventID == null
								|| protocolEventID.equalsIgnoreCase(Constants.FALSE))
						{
							final Map tempMap = this.prepareConsentMap(participantResponseList);
							specimenCollectionGroupForm.setConsentResponseForScgValues(tempMap);
						}
					}
					specimenCollectionGroupForm.setConsentTierCounter(participantResponseList
							.size());
				}
				else
				{
					final String scgID = String.valueOf(specimenCollectionGroupForm.getId());
					final SpecimenCollectionGroup specimenCollectionGroup = AppUtility.getSCGObj(
							scgID, dao);
					// List added for grid
					final List specimenDetails = new ArrayList();
					this.getSpecimenDetails(specimenCollectionGroup, specimenDetails, dao);
					final List columnList = ConsentUtil.columnNames();
					// Resolved Lazy
					// Collection consentResponse =
					// specimenCollectionGroup.getCollectionProtocolRegistration
					// ().getConsentTierResponseCollection();
					// Collection consentResponseStatuslevel=
					// specimenCollectionGroup.getConsentTierStatusCollection();
					List consentResponse = null, consentResponseStatuslevel = null;
					consentResponse = dao
							.retrieveAttribute(SpecimenCollectionGroup.class, "id",
									specimenCollectionGroup.getId(),
									"elements(collectionProtocolRegistration.consentTierResponseCollection)");

					consentResponseStatuslevel = dao.retrieveAttribute(
							SpecimenCollectionGroup.class, "id", specimenCollectionGroup.getId(),
							"elements(consentTierStatusCollection)");

					final String scgResponsekey = "_specimenCollectionGroupLevelResponse";
					final String scgResponseIDkey = "_specimenCollectionGroupLevelResponseID";
					final Map tempMap = ConsentUtil.prepareSCGResponseMap(
							consentResponseStatuslevel, consentResponse, scgResponsekey,
							scgResponseIDkey);
					specimenCollectionGroupForm.setConsentResponseForScgValues(tempMap);
					specimenCollectionGroupForm.setConsentTierCounter(participantResponseList
							.size());
					final HttpSession session = request.getSession();
					session.setAttribute(Constants.SPECIMEN_LIST, specimenDetails);
					session.setAttribute(Constants.COLUMNLIST, columnList);
				}
			}
			final List specimenCollectionGroupResponseList = AppUtility.responceList(operation);
			request.setAttribute(Constants.LIST_OF_SPECIMEN_COLLECTION_GROUP,
					specimenCollectionGroupResponseList);

			final String tabSelected = request.getParameter(Constants.SELECTED_TAB);
			if (tabSelected != null)
			{
				request.setAttribute(Constants.SELECTED_TAB, tabSelected);
			}

			// For Consent Tracking (Virender Mehta) - End

			// get list of Protocol title.
			final SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) factory
					.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			// populating protocolist bean.
			String sourceObjectName = CollectionProtocol.class.getName();
			final String[] displayNameFields = {"shortTitle"};
			final String valueField = Constants.SYSTEM_IDENTIFIER;
			List list = bizLogic.getList(sourceObjectName, displayNameFields, valueField, true);
			request.setAttribute(Constants.PROTOCOL_LIST, list);

			// Populating the Site Type bean
			sourceObjectName = Site.class.getName();
			final String[] siteDisplaySiteFields = {"name"};
			list = bizLogic.getList(sourceObjectName, siteDisplaySiteFields, valueField, true);
			request.setAttribute(Constants.SITELIST, list);

			// Populating the participants registered to a given protocol
			/** For Migration Start **/
			// loadPaticipants(specimenCollectionGroupForm.getCollectionProtocolId()
			// , bizLogic, request);
			/** For Migration End **/
			// Populating the protocol participants id registered to a given
			// protocol
			// By Abhishek Mehta -Performance Enhancement
			// loadPaticipantNumberList(specimenCollectionGroupForm.
			// getCollectionProtocolId(),bizLogic,request);
			final String protocolParticipantId = specimenCollectionGroupForm
					.getProtocolParticipantIdentifier();
			// Populating the participants Medical Identifier for a given
			// participant
			this.loadParticipantMedicalIdentifier(specimenCollectionGroupForm.getParticipantId(),
					bizLogic, request);

			// Load Clinical status for a given study calander event point
			final String changeOn = request.getParameter(Constants.CHANGE_ON);

			if (changeOn != null && changeOn.equals(Constants.COLLECTION_PROTOCOL_ID))
			{
				specimenCollectionGroupForm.setCollectionProtocolEventId(-1l);
			}

			// Populating the Collection Protocol Events
			this.loadCollectionProtocolEvent(specimenCollectionGroupForm.getCollectionProtocolId(),
					bizLogic, request, specimenCollectionGroupForm);
			Object cPEObject = null;
			if (!operation.equalsIgnoreCase(Constants.ADD)
					&& !(specimenCollectionGroupForm.getCollectionProtocolEventId() == 0))
			{
				cPEObject = dao.retrieveById(CollectionProtocolEvent.class.getName(),
						specimenCollectionGroupForm.getCollectionProtocolEventId());
			}

			// The values of restrict checkbox and the number of specimen must alos
			// populate in edit mode.
			if ((isOnChange || operation.equalsIgnoreCase(Constants.EDIT)))
			{
				// Added by Vijay Pande. Method is created since code was repeating
				// for SUBMITTED_FOR= "AddNew" || "Default" value.
				this.setCalendarEventPoint(cPEObject, request, specimenCollectionGroupForm);
			}

			// populating clinical Diagnosis field
			// CDE cde =
			// CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_CLINICAL_DIAGNOSIS
			// );
			// CDEBizLogic cdeBizLogic = (CDEBizLogic)
			// BizLogicFactory.getInstance().getBizLogic(Constants.CDE_FORM_ID);
			// List clinicalDiagnosisList = new ArrayList();
			// clinicalDiagnosisList.add(new NameValueBean(Constants.SELECT_OPTION,
			// "" + Constants.SELECT_OPTION_VALUE));
			// cdeBizLogic.getFilteredCDE(cde.getPermissibleValues(),
			// clinicalDiagnosisList);
			// request.setAttribute(Constants.CLINICAL_DIAGNOSIS_LIST,
			// clinicalDiagnosisList);
			// request.getSession().setAttribute(Constants.CLINICAL_DIAGNOSIS_LIST,
			// clinicalDiagnosisList);

			// populating clinical Status field
			// NameValueBean undefinedVal = new
			// NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
			final List clinicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_CLINICAL_STATUS, null);
			request.setAttribute(Constants.CLINICAL_STATUS_LIST, clinicalStatusList);

			// Sets the activityStatusList attribute to be used in the Site Add/Edit
			// Page.
			request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);
			// Sets the collectionStatusList attribute to be used in the Site
			// Add/Edit Page.
			request.setAttribute(Constants.COLLECTIONSTATUSLIST,
					Constants.SCG_COLLECTION_STATUS_VALUES);
			// fix for bug no.7390
			if (specimenCollectionGroupForm.getCollectionStatus() == null)
			{
				specimenCollectionGroupForm
						.setCollectionStatus(Constants.COLLECTION_STATUS_PENDING);
			}
			// end for fix. Bug no.7390
			LOGGER.debug("CP ID in SCG Action======>"
					+ specimenCollectionGroupForm.getCollectionProtocolId());
			LOGGER.debug("Participant ID in SCG Action=====>"
					+ specimenCollectionGroupForm.getParticipantId() + "  "
					+ specimenCollectionGroupForm.getProtocolParticipantIdentifier());

			/**
			 * Name: Vijay Pande check for SUBMITTED_FOR with "AddNew" is added
			 * since while coming from specimen->scg->AddNew link for participant->
			 * Register participant -> submit then the SUBMITTED_FOR is equal to
			 * "AddNew" If the flow is scg->AddNew link for participant-> Register
			 * participant -> submit then the SUBMITTED_FOR is equal to "Default"
			 */
			// -------called from Collection Protocol Registration
			// start-------------------------------
			if ((request.getAttribute(Constants.SUBMITTED_FOR) != null)
					&& ((request.getAttribute(Constants.SUBMITTED_FOR).equals("Default")) || (request
							.getAttribute(Constants.SUBMITTED_FOR).equals(Constants.ADDNEW_LINK))))
			{
				LOGGER.debug("Populating CP and Participant in SCG ====  AddNew operation loop");

				final Long cprId = specimenCollectionGroupForm
						.getCollectionProtocolRegistrationId();

				if (cprId != null)
				{
					final Object cPRObject = dao.retrieveById(CollectionProtocolRegistration.class
							.getName(), cprId);
					if (cPRObject != null)
					{
						final CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) cPRObject;

						final long cpID = cpr.getCollectionProtocol().getId().longValue();
						final long pID = cpr.getParticipant().getId().longValue();
						final String ppID = cpr.getProtocolParticipantIdentifier();

						this.LOGGER.debug("cpID : " + cpID + "   ||" + "  pID : " + pID
								+ "    || ppID : " + ppID);

						specimenCollectionGroupForm.setCollectionProtocolId(cpID);

						// Populating the participants registered to a given
						// protocol
						/** For Migration Start **/
						// loadPaticipants(cpID , bizLogic, request);
						/** For Migration Start **/
						// By Abhishek Mehta -Performance Enhancement
						// loadPaticipantNumberList(specimenCollectionGroupForm.
						// getCollectionProtocolId(),bizLogic,request);
						/**
						 * Name: Vijay Pande Reviewer Name: Aarti Sharma participant
						 * associated with collection protocol is explicitly
						 * retrived from DB since its lazy load property is true
						 */
						Participant cprParticipant = null;
						final List cprParticipantList = dao.retrieveAttribute(
								CollectionProtocolRegistration.class, "id", cpr.getId(),
								Constants.COLUMN_NAME_PARTICIPANT);
						if ((cprParticipantList != null) && (!cprParticipantList.isEmpty()))
						{
							cprParticipant = (Participant) cprParticipantList.get(0);
						}
						// set participant id in request. This is required only in
						// CP based View since SpecimenTreeView.jsp is retrieveing
						// participant id from request
						if (cprParticipant.getId() != null)
						{
							request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID, cprParticipant
									.getId().toString());
						}
						final String firstName = Utility.toString(cprParticipant.getFirstName());
						final String lastName = Utility.toString(cprParticipant.getLastName());
						final String birthDate = Utility.toString(cprParticipant.getBirthDate());
						final String ssn = Utility.toString(cprParticipant
								.getSocialSecurityNumber());
						if (firstName.trim().length() > 0 || lastName.trim().length() > 0
								|| birthDate.trim().length() > 0 || ssn.trim().length() > 0)
						{
							specimenCollectionGroupForm.setParticipantId(pID);
							specimenCollectionGroupForm.setRadioButtonForParticipant(1);
							specimenCollectionGroupForm.setParticipantName(lastName + ", "
									+ firstName);
						}
						// Populating the protocol participants id registered to a
						// given protocol

						else if (cpr.getProtocolParticipantIdentifier() != null)
						{
							specimenCollectionGroupForm.setProtocolParticipantIdentifier(ppID);
							specimenCollectionGroupForm.setRadioButtonForParticipant(2);
						}

						// Populating the Collection Protocol Events
						this.loadCollectionProtocolEvent(specimenCollectionGroupForm
								.getCollectionProtocolId(), bizLogic, request,
								specimenCollectionGroupForm);

						// Load Clinical status for a given study calander event
						// point
						if (!(specimenCollectionGroupForm.getCollectionProtocolEventId() == 0))
						{
							cPEObject = bizLogic.retrieve(CollectionProtocolEvent.class.getName(),
									specimenCollectionGroupForm.getCollectionProtocolEventId());
						}
						if (isOnChange && cPEObject != null)
						{
							this.setCalendarEventPoint(cPEObject, request,
									specimenCollectionGroupForm);
						}
					}
				}
				request.setAttribute(Constants.SUBMITTED_FOR, "Default");
			}

			// ************* ForwardTo implementation *************
			final HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");

			if (forwardToHashMap != null)
			{

				/**
				 * Name: Falguni Sachde Reviewer Name: Attribute
				 * collectionProtocolName added to show Collection ProtocolName in
				 * Add mode only.
				 */

				Long collectionProtocolId = (Long) forwardToHashMap.get("collectionProtocolId");
				final String collectionProtocolName = (String) request.getSession().getAttribute(
						"cpTitle");
				if (collectionProtocolId == null && request.getParameter("cpId") != null
						&& !request.getParameter("cpId").equals("null"))
				{
					collectionProtocolId = Long.valueOf(request.getParameter("cpId"));
				}

				final Long participantId = (Long) forwardToHashMap.get("participantId");
				String participantProtocolId = (String) forwardToHashMap
						.get("participantProtocolId");

				specimenCollectionGroupForm.setCollectionProtocolId(collectionProtocolId
						.longValue());
				specimenCollectionGroupForm.setCollectionProtocolName(collectionProtocolName);

				/**
				 * Name : Deepti Shelar Bug id : 4216 Patch id : 4216_1 Description
				 * : populating list of ParticipantMedicalIdentifiers for given
				 * participant id
				 */
				this.loadParticipantMedicalIdentifier(participantId, bizLogic, request);

				if (participantId != null && participantId.longValue() != 0)
				{
					// Populating the participants registered to a given protocol
					/** For Migration Start **/
					// loadPaticipants(collectionProtocolId.longValue(), bizLogic,
					// request);
					/** For Migration End **/
					// By Abhishek Mehta -Performance Enhancement
					// loadPaticipantNumberList(specimenCollectionGroupForm.
					// getCollectionProtocolId(),bizLogic,request);
					specimenCollectionGroupForm.setParticipantId(participantId.longValue());
					specimenCollectionGroupForm.setRadioButtonForParticipant(1);
					request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID, participantId
							.toString());
					/** For Migration Start **/

					final Object participantObject = dao.retrieveById(Participant.class.getName(),
							participantId);
					if (participantObject != null)
					{

						final Participant participant = (Participant) participantObject;
						String firstName = "";
						String lastName = "";
						if (participant.getFirstName() != null)
						{
							firstName = participant.getFirstName();
						}
						if (participant.getLastName() != null)
						{
							lastName = participant.getLastName();
						}
						if (!firstName.equals("") && !lastName.equals(""))
						{
							specimenCollectionGroupForm.setParticipantName(lastName + ", "
									+ firstName);
						}
						else if (lastName.equals("") && !firstName.equals(""))
						{
							specimenCollectionGroupForm.setParticipantName(participant
									.getFirstName());
						}
						else if (firstName.equals("") && !lastName.equals(""))
						{
							specimenCollectionGroupForm.setParticipantName(participant
									.getLastName());
						}

					}
					/** For Migration End **/
					/**
					 * Name : Deepti Shelar Reviewer Name : Sachin Lale Bug id :
					 * FutureSCG Patch Id : FutureSCG_1 Description : setting
					 * participantProtocolId to form
					 */
					if (participantProtocolId == null)
					{
						participantProtocolId = this.getParticipantProtocolIdForCPAndParticipantId(
								participantId.toString(), collectionProtocolId.toString(), dao);
						if (participantProtocolId != null)
						{
							specimenCollectionGroupForm
									.setProtocolParticipantIdentifier(participantProtocolId);
							specimenCollectionGroupForm.setRadioButtonForParticipant(2);
						}
					}
				}
				else if (participantProtocolId != null)
				{
					// Populating the participants registered to a given protocol
					/** For Migration Start **/
					// loadPaticipants(collectionProtocolId.longValue(), bizLogic,
					// request);
					/** For Migration End **/
					// By Abhishek Mehta -Performance Enhancement
					// loadPaticipantNumberList(specimenCollectionGroupForm.
					// getCollectionProtocolId(),bizLogic,request);
					specimenCollectionGroupForm
							.setProtocolParticipantIdentifier(participantProtocolId);
					specimenCollectionGroupForm.setRadioButtonForParticipant(2);
					final String cpParticipantId = this.getParticipantIdForProtocolId(
							participantProtocolId, dao);
					if (cpParticipantId != null)
					{
						request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID, cpParticipantId);
					}
				}
				/**
				 * Patch Id : FutureSCG_3 Description : Setting number of specimens
				 * and restricted checkbox
				 */
				/**
				 * Removing the above patch, as it no more required. Now the new CP
				 * based entry page takes care of this.
				 */
				final Long cpeId = (Long) forwardToHashMap.get("COLLECTION_PROTOCOL_EVENT_ID");
				if (cpeId != null)
				{
					specimenCollectionGroupForm.setCollectionProtocolEventId(cpeId);
					/*
					 * List cpeList =
					 * bizLogic.retrieve(CollectionProtocolEvent.class
					 * .getName(),Constants.SYSTEM_IDENTIFIER,cpeId);
					 * if(!cpeList.isEmpty()) { setNumberOfSpecimens(request,
					 * specimenCollectionGroupForm, cpeList); }
					 */
				}
				// Bug 1915:SpecimenCollectionGroup.Study Calendar Event Point not
				// populated when page is loaded through proceedTo
				// Populating the Collection Protocol Events
				this.loadCollectionProtocolEvent(specimenCollectionGroupForm
						.getCollectionProtocolId(), bizLogic, request, specimenCollectionGroupForm);

				// Load Clinical status for a given study calander event point
				if (!(specimenCollectionGroupForm.getCollectionProtocolEventId() == 0))
				{
					cPEObject = dao.retrieveById(CollectionProtocolEvent.class.getName(),
							specimenCollectionGroupForm.getCollectionProtocolEventId());
				}
				if (cPEObject != null)
				{
					this.setCalendarEventPoint(cPEObject, request, specimenCollectionGroupForm);
				}

				LOGGER.debug("CollectionProtocolID found in forwardToHashMap========>>>>>>"
						+ collectionProtocolId);
				LOGGER.debug("ParticipantID found in forwardToHashMap========>>>>>>"
						+ participantId);
				LOGGER.debug("ParticipantProtocolID found in forwardToHashMap========>>>>>>"
						+ participantProtocolId);
			}
			// ************* ForwardTo implementation *************
			// Populate the group name field with default value in the form of
			// <Collection Protocol Name>_<Participant ID>_<Group Id>
			final int groupNumber = bizLogic.getNextGroupNumber();

			// Get the collection protocol title for the collection protocol Id
			// selected
			String collectionProtocolTitle = "";
			String collectionProtocolName = "";

			final Object cPObject = dao.retrieveById(CollectionProtocol.class.getName(),
					specimenCollectionGroupForm.getCollectionProtocolId());

			if (cPObject != null)
			{
				final CollectionProtocol collectionProtocol = (CollectionProtocol) cPObject;
				collectionProtocolTitle = collectionProtocol.getTitle();
				collectionProtocolName = (String) collectionProtocol.getShortTitle();
				specimenCollectionGroupForm.setCollectionProtocolName(collectionProtocolName);
			}

			final long groupParticipantId = specimenCollectionGroupForm.getParticipantId();
			// check if the reset name link was clicked
			final String resetName = request.getParameter(Constants.RESET_NAME);

			// Set the name to default if reset name link was clicked or page is
			// loading for first time
			// through add link or forward to link
			if (forwardToHashMap != null
					|| (specimenCollectionGroupForm.getName() != null && specimenCollectionGroupForm
							.getName().equals(""))
					|| (resetName != null && resetName.equals("Yes")))
			{
				if (!collectionProtocolTitle.equals("")
						&& (groupParticipantId > 0 || (protocolParticipantId != null && !protocolParticipantId
								.equals(""))))
				{
					// Poornima:Bug 2833 - Error thrown when adding a specimen
					// collection group
					// Max length of CP is 150 and Max length of SCG is 55, in
					// Oracle the name does not truncate
					// and it is giving error. So the title is truncated in case it
					// is longer than 30 .
					String maxCollTitle = collectionProtocolName;
					if (collectionProtocolName.length() > Constants.COLLECTION_PROTOCOL_TITLE_LENGTH)
					{
						maxCollTitle = collectionProtocolName.substring(0,
								Constants.COLLECTION_PROTOCOL_TITLE_LENGTH - 1);
					}
					// During add operation the id to set in the default name is
					// generated
					if (operation.equals(Constants.ADD))
					{
						specimenCollectionGroupForm.setName(maxCollTitle + "_" + groupParticipantId
								+ "_" + groupNumber);
					}
					// During edit operation the id to set in the default name using
					// the id
					else if (operation.equals(Constants.EDIT)
							&& (resetName != null && resetName.equals("Yes")))
					{
						if (groupParticipantId > 0)
						{
							specimenCollectionGroupForm.setName(maxCollTitle + "_"
									+ groupParticipantId + "_"
									+ specimenCollectionGroupForm.getId());
						}
						else
						{
							specimenCollectionGroupForm.setName(maxCollTitle + "_"
									+ protocolParticipantId + "_"
									+ specimenCollectionGroupForm.getId());
						}
					}
				}
			}

			request.setAttribute(Constants.PAGE_OF, pageOf);
			LOGGER.debug("page of in Specimen coll grp action:"
					+ request.getParameter(Constants.PAGE_OF));
			// -------called from Collection Protocol Registration end
			// -------------------------------
			// Falguni:Performance Enhancement.
			Long scgEntityId = null;
			/*if (CatissueCoreCacheManager.getInstance().getObjectFromCache("scgEntityId") != null)
			{
				scgEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache(
						"scgEntityId");
			}
			else
			{
				scgEntityId = AnnotationUtil
						.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP);
				CatissueCoreCacheManager.getInstance().addObjectToCache("scgEntityId", scgEntityId);
			}
			request.setAttribute("scgEntityId", scgEntityId);*/
			if (CatissueCoreCacheManager.getInstance().getObjectFromCache(
					AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID) == null)
			{
				scgEntityId = AnnotationUtil
						.getEntityId(AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY);
				CatissueCoreCacheManager.getInstance().addObjectToCache(
						AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID, scgEntityId);
			}
			else
			{
				scgEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache(
						AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID);

			}
			request.setAttribute(AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID, scgEntityId);
			AppUtility.setDefaultPrinterTypeLocation(specimenCollectionGroupForm);
			/**
			 * Name : Ashish Gupta Reviewer Name : Sachin Lale Bug ID: 2741 Patch
			 * ID: 2741_11 Description: Methods to set default events on SCG page
			 */
			this.setDefaultEvents(request, specimenCollectionGroupForm, operation);

			request.setAttribute("scgForm", specimenCollectionGroupForm);
			/*
			 * Bug ID: 4135 Patch ID: 4135_2 Description: Setting the ids in
			 * collection and received events associated with this scg
			 */
			// When opening in Edit mode, to set the ids of collection event
			// parameters and received event parameters
			if (specimenCollectionGroupForm.getId() != 0)
			{
				this.setEventsId(specimenCollectionGroupForm, bizLogic);
			}

			// set associated identified report id
			Long reportId = this.getAssociatedIdentifiedReportId(specimenCollectionGroupForm
					.getId());
			if (reportId == null)
			{
				reportId = -1l;
			}
			else if (AppUtility.isQuarantined(reportId))
			{
				reportId = -2l;
			}
			final HttpSession session = request.getSession();
			session.setAttribute(Constants.IDENTIFIED_REPORT_ID, reportId);

			session.removeAttribute("asignedPositonSet");
			final String isClinPortalServiceEnabled = XMLPropertyHandler
					.getValue(CDMSIntegrationConstants.CDMS_SERVICE_ENABLED);
			if (isClinPortalServiceEnabled.equals(Constants.TRUE))
			{
				long strt = System.currentTimeMillis();
				formClinportalURL(request, specimenCollectionGroupForm, sessionData.getUserName());
				long end = System.currentTimeMillis();
			}

		}
		finally
		{
			dao.closeSession();
		}
		return mapping.findForward(pageOf);
	}

	/**
	 * 1: get scgId != null => get catissue pid,cp id ,cpe id
	 * 2: get clinportal pid, cs id ,cse id
	 * 3: if scgid == null =>.
	 *
	 * @param request the request
	 * @param specimenCollectionGroupForm the specimen collection group form
	 * @param loginName the login name
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	/*private void formClinportalURL_OLD(final HttpServletRequest request,
	        final SpecimenCollectionGroupForm specimenCollectionGroupForm,
	        final String loginName) throws BizLogicException
	{
	    final SessionDataBean sessionData = (SessionDataBean) request.getSession()
	    .getAttribute(Constants.SESSION_DATA);
	    StringBuilder url = new StringBuilder();
	    try
	    {
	    	final Long scgID = specimenCollectionGroupForm.getId();
	        final CDMSAPIService clinPortalAPIService = new CDMSAPIService();

	        String clinportalUrl = XMLPropertyHandler
	                .getValue(CDMSIntegrationConstants.CDMS_URL);
	        final Map<String, Long> map = clinPortalAPIService.getClinPortalURLIds(sessionData.getUserName(),
	                specimenCollectionGroupForm.getCollectionProtocolId(),
	                specimenCollectionGroupForm.getParticipantId(),
	                specimenCollectionGroupForm.getCollectionProtocolEventId(),specimenCollectionGroupForm.getId());
	        if(CDMSCaTissueIntegrationUtil.validateClinPortalMap(map)){
	        clinportalUrl = CDMSIntegrationConstants.CLINPORTAL_URL_CONTEXT(clinportalUrl);
	        url.append(clinportalUrl);
	        String visitId =(String) request.getSession().getAttribute(CDMSIntegrationConstants.EVENTENTRYID);
	        final Long csId = map.get(CDMSIntegrationConstants.CLINICAL_STUDY_ID);
	        if(visitId==null)
	        {
	            *//**
	                 * get corresponding visit id to scgId if present
	                 * else get near to visit Id of scgIdf
	                 */
	/*
	                if(map.get(CDMSIntegrationConstants.EVENTENTRYID)==null)
	                {
	                	//Set error message.....
	                    setErrorMessage(request);

	                 }
	                else
	                {
	                	visitId=map.get(CDMSIntegrationConstants.EVENTENTRYID).toString();
	                }
	            }
	            if (visitId != null && scgID != null && !visitId.equals("0") && scgID >0)
	            {
	                request.getSession().removeAttribute(CDMSIntegrationConstants.EVENTENTRYID);
	                url.append(CDMSIntegrationConstants.EVENTENTRYID).append(
	                        CDMSIntegrationConstants.EQUALS).append(
	                        String.valueOf(visitId));
	            }
	            else
	            {

	                 * get  CS id from CP id , get CSE id from CPE id, get PId and user login name

	                final Long cseId = map.get(CDMSIntegrationConstants.EVENT_ID);
	                final Long pId = map.get(CDMSIntegrationConstants.CP_PARTICIPANT_ID);

	                url.append(CDMSIntegrationConstants.CP_PARTICIPANT_ID)
	                        .append(Constants.EQUALS).append(pId);
	                url.append(CDMSCaTissueIntegrationUtil.formReqParameter(
	                        CDMSIntegrationConstants.CLINICAL_STUDY_ID,String.valueOf(csId)));
	                url.append(CDMSCaTissueIntegrationUtil.formReqParameter(
	                        CDMSIntegrationConstants.EVENT_ID, String.valueOf(cseId)));
	            }
	            url.append(CDMSCaTissueIntegrationUtil.formReqParameter(CDMSIntegrationConstants.SCGID,Utility.toString(scgID)));
	            url.append(CDMSCaTissueIntegrationUtil.formReqParameter(CDMSIntegrationConstants.CSM_USER_ID, sessionData.getCsmUserId()));
	            url.append(CDMSCaTissueIntegrationUtil.formReqParameter("&method", CDMSIntegrationConstants.LOGIN));
	            if(csId==null || csId<=0)
	            {
	                url=new StringBuilder();
	            }}
	        }
	        catch (Exception e)
	        {
	            LOGGER.out.error(e.getMessage());
	        }
	        request.setAttribute(CDMSIntegrationConstants.CALLBACK_URL, url.toString());

	    }*/
	/**
	 * Form Clinportal URL.
	 * @param request HttpServletRequest
	 * @param specimenCollectionGroupForm SpecimenCollectionGroupForm
	 * @param loginName String
	 * @throws BizLogicException BizLogicException
	 */
	private void formClinportalURL(final HttpServletRequest request,
			final SpecimenCollectionGroupForm specimenCollectionGroupForm, final String loginName)
			throws BizLogicException
	{
		String url = new String();
		final SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
		try
		{
			CatissueCdmsURLInformationObject informationObject = new CatissueCdmsURLInformationObject();
			informationObject.setSpecimenCollectionGroupIdentifier(String
					.valueOf(specimenCollectionGroupForm.getId()));
			informationObject.setCollectionProtocolIdentifier(String
					.valueOf(specimenCollectionGroupForm.getCollectionProtocolId()));
			informationObject.setCollectionProtocolEventIdentifier(String
					.valueOf(specimenCollectionGroupForm.getCollectionProtocolEventId()));
			informationObject.setParticipantIdentifier(String.valueOf(specimenCollectionGroupForm
					.getParticipantId()));
			informationObject.setVisitIdentifier((String) request.getSession().getAttribute(
					CDMSIntegrationConstants.EVENTENTRYID));
			informationObject.setUrl(XMLPropertyHandler
					.getValue(CDMSIntegrationConstants.CDMS_URL));
			informationObject.setUserCSMIdentifier(sessionData.getCsmUserId());

			CatissueCdmsIntegrator catissueCdmsIntegrator = getClassNameFromCatissueCdmsPropertiesFile();

			String password = AppUtility.getPassord(loginName);
			url = catissueCdmsIntegrator.getVisitInformationURL(informationObject, loginName, password);
			if (url.contains(CDMSIntegrationConstants.EVENTENTRYID
					+ CDMSIntegrationConstants.EQUALS))
			{
				request.getSession().removeAttribute(CDMSIntegrationConstants.EVENTENTRYID);
			}
			else
			{
				//Set error message....
				setErrorMessage(request);
			}
		}
		catch (gov.nih.nci.system.applicationservice.ApplicationException appExp)
		{
			LOGGER.out.error(appExp.getMessage());
			if (appExp.getMessage().contains("No Visit"))
			{
				ActionErrors actionErrors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
				if (actionErrors == null)
				{
					actionErrors = new ActionErrors();
				}
				final ActionError actionError = new ActionError("errors.item", "No Visit");
				actionErrors.add(ActionErrors.GLOBAL_ERROR, actionError);
				saveErrors(request, actionErrors);
			}
		}
		catch (Exception exp)
		{
			LOGGER.out.error(exp.getMessage());
		}
		request.setAttribute(CDMSIntegrationConstants.CALLBACK_URL, url.toString());
	}

	/**
	 *
	 * @return
	 * @throws BulkOperationException
	 */
	public static CatissueCdmsIntegrator getClassNameFromCatissueCdmsPropertiesFile()
			throws ApplicationException, Exception
	{
		String fileName = System.getProperty(Constants.CATISSUE_CDMS_INTEGRATION_PROP_FILE_NAME);
		Properties properties;
		CatissueCdmsIntegrator catissueCdmsIntegrator = null;
		try
		{
			properties = AppUtility.getPropertiesFile(fileName);
			String cdmsIntegrationClassName = properties
					.getProperty(Constants.CDMS_INTEGRATION_CLASSNAME);
			Class klass = Class.forName(cdmsIntegrationClassName);
			Constructor constructor = klass.getConstructor(null);
			catissueCdmsIntegrator = (CatissueCdmsIntegrator) constructor.newInstance();
		}
		catch (ApplicationException appExp)
		{
			throw appExp;
		}
		catch (Exception exp)
		{
			throw exp;
		}
		return catissueCdmsIntegrator;
	}

	/**
	 * Sets the error message.
	 *
	 * @param request the request
	 */
	private void setErrorMessage(final HttpServletRequest request)
	{
		ActionErrors actionErrors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		if (actionErrors == null)
		{
			actionErrors = new ActionErrors();
		}
		final ActionError actionError = new ActionError("errors.item", "No Visit");
		actionErrors.add(ActionErrors.GLOBAL_ERROR, actionError);
		saveErrors(request, actionErrors);

	}

	/**
	 * Sets the events id.
	 *
	 * @param specimenCollectionGroupForm : specimenCollectionGroupForm
	 * @param bizLogic : bizLogic
	 *
	 * @throws BizLogicException : BizLogicException
	 */
	private void setEventsId(SpecimenCollectionGroupForm specimenCollectionGroupForm,
			SpecimenCollectionGroupBizLogic bizLogic) throws BizLogicException
	{
		final Object object = bizLogic.retrieve(SpecimenCollectionGroup.class.getName(),
				specimenCollectionGroupForm.getId());
		if (object != null)
		{
//			final SpecimenCollectionGroup scg = (SpecimenCollectionGroup) object;
//			final Collection<SpecimenEventParameters> eventsColl = scg.getSpecimenEventParametersCollection();
//			CollectionEventParameters collectionEventParameters = null;
//			ReceivedEventParameters receivedEventParameters = null;
//			if (eventsColl != null && !eventsColl.isEmpty())
//			{
//				final Iterator<SpecimenEventParameters> iter = eventsColl.iterator();
//				while (iter.hasNext())
//				{
//					final Object temp = iter.next();
//					if (temp instanceof CollectionEventParameters)
//					{
//						collectionEventParameters = (CollectionEventParameters) temp;
//					}
//					else if (temp instanceof ReceivedEventParameters)
//					{
//						receivedEventParameters = (ReceivedEventParameters) temp;
//					}
//				}
//				// Setting the ids
//				specimenCollectionGroupForm.setCollectionEventId(collectionEventParameters.getId()
//						.longValue());
//				specimenCollectionGroupForm.setReceivedEventId(receivedEventParameters.getId()
//						.longValue());
//			}
		}
	}

	/**
	 * Sets the default events.
	 *
	 * @param request : request
	 * @param specimenCollectionGroupForm : specimenCollectionGroupForm
	 * @param operation : operation
	 *
	 * @throws ApplicationException : ApplicationException
	 */
	private void setDefaultEvents(HttpServletRequest request,
			SpecimenCollectionGroupForm specimenCollectionGroupForm, String operation)
			throws ApplicationException
	{
		this.setDateParameters(specimenCollectionGroupForm, request);

		if (specimenCollectionGroupForm.getCollectionEventCollectionProcedure() == null)
		{
			specimenCollectionGroupForm.setCollectionEventCollectionProcedure(Constants.CP_DEFAULT);

		}
		if (specimenCollectionGroupForm.getCollectionEventContainer() == null)
		{
			specimenCollectionGroupForm.setCollectionEventContainer(Constants.CP_DEFAULT);
		}
		if (specimenCollectionGroupForm.getReceivedEventReceivedQuality() == null)
		{
			specimenCollectionGroupForm.setReceivedEventReceivedQuality(Constants.CP_DEFAULT);
		}
		// setting the collector and receiver drop downs
		AppUtility.setUserInForm(request, operation);
		final long collectionEventUserId = AppUtility.setUserInForm(request, operation);
		setEventUserId(specimenCollectionGroupForm, collectionEventUserId);
		// Setting the List for drop downs
		this.setEventsListInRequest(request);
	}

	/**
	 * Sets the event user id.
	 *
	 * @param specimenCollectionGroupForm the specimen collection group form
	 * @param collectionEventUserId the collection event user id
	 */
	private void setEventUserId(SpecimenCollectionGroupForm specimenCollectionGroupForm,
			final long collectionEventUserId)
	{
		if (specimenCollectionGroupForm.getCollectionEventUserId() == 0)
		{
			specimenCollectionGroupForm.setCollectionEventUserId(collectionEventUserId);
		}
		if (specimenCollectionGroupForm.getReceivedEventUserId() == 0)
		{
			specimenCollectionGroupForm.setReceivedEventUserId(collectionEventUserId);
		}
	}

	/**
	 * Sets the events list in request.
	 *
	 * @param request : request
	 */
	private void setEventsListInRequest(HttpServletRequest request)
	{
		// setting the procedure
		final List procedureList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
		request.setAttribute(Constants.PROCEDURE_LIST, procedureList);
		// set the container lists
		final List containerList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_CONTAINER, null);
		request.setAttribute(Constants.CONTAINER_LIST, containerList);

		// setting the quality for received events
		final List qualityList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_RECEIVED_QUALITY, null);
		request.setAttribute(Constants.RECEIVED_QUALITY_LIST, qualityList);

		// Sets the hourList attribute to be used in the Add/Edit
		// FrozenEventParameters Page.
		request.setAttribute(Constants.HOUR_LIST, Constants.HOUR_ARRAY);
		// Sets the minutesList attribute to be used in the Add/Edit
		// FrozenEventParameters Page.
		request.setAttribute(Constants.MINUTES_LIST, Constants.MINUTES_ARRAY);
	}

	/**
	 * Sets the date parameters.
	 *
	 * @param specimenForm : specimenForm
	 * @param request : request
	 */
	private void setDateParameters(SpecimenCollectionGroupForm specimenForm,
			HttpServletRequest request)
	{
		// set the current Date and Time for the event.
		final Calendar cal = Calendar.getInstance();
		// Collection Event fields
		if (specimenForm.getCollectionEventdateOfEvent() == null)
		{
			if (request.getParameter("evtDate") != null)
			{
				specimenForm.setCollectionEventdateOfEvent(request.getParameter("evtDate"));
			}
			else
			{
				specimenForm.setCollectionEventdateOfEvent(Utility.parseDateToString(cal.getTime(),
						CommonServiceLocator.getInstance().getDatePattern()));
			}
		}
		if (specimenForm.getCollectionEventTimeInHours() == null)
		{
			specimenForm.setCollectionEventTimeInHours(Integer.toString(cal
					.get(Calendar.HOUR_OF_DAY)));
		}
		if (specimenForm.getCollectionEventTimeInMinutes() == null)
		{
			specimenForm
					.setCollectionEventTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
		}

		// ReceivedEvent Fields
		if (specimenForm.getReceivedEventDateOfEvent() == null)
		{
			if (request.getParameter("evtDate") != null)
			{
				// received date should be same as collected if its anticipated
				// date.
				specimenForm.setReceivedEventDateOfEvent(request.getParameter("evtDate"));
			}
			else
			{
				specimenForm.setReceivedEventDateOfEvent(Utility.parseDateToString(cal.getTime(),
						CommonServiceLocator.getInstance().getDatePattern()));
			}
		}
		if (specimenForm.getReceivedEventTimeInHours() == null)
		{
			specimenForm.setReceivedEventTimeInHours(Integer
					.toString(cal.get(Calendar.HOUR_OF_DAY)));
		}
		if (specimenForm.getReceivedEventTimeInMinutes() == null)
		{
			specimenForm.setReceivedEventTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
		}

	}

	/**
	 * For Migration Start *.
	 *
	 * @param protocolID the protocol id
	 * @param bizLogic the biz logic
	 * @param request the request
	 * @param form the form
	 *
	 * @throws Exception the exception
	 */
	/*
	 * private void loadPaticipants(long protocolID, IBizLogic bizLogic,
	 * HttpServletRequest request) throws Exception { //get list of
	 * Participant's names String sourceObjectName =
	 * CollectionProtocolRegistration.class.getName(); String []
	 * displayParticipantFields = {"participant.id"}; String valueField =
	 * "participant."+Constants.SYSTEM_IDENTIFIER; String whereColumnName[] =
	 * {"collectionProtocol."+Constants.SYSTEM_IDENTIFIER,"participant.id"};
	 * String whereColumnCondition[]; Object[] whereColumnValue;
	 * if(Variables.databaseName.equals(Constants.MYSQL_DATABASE)) {
	 * whereColumnCondition = new String[]{"=","is not"}; whereColumnValue=new
	 * Object[]{new Long(protocolID),null}; } else { // for ORACLE
	 * whereColumnCondition = new String[]{"=",Constants.IS_NOT_NULL};
	 * whereColumnValue=new Object[]{new Long(protocolID),""}; } String
	 * joinCondition = Constants.AND_JOIN_CONDITION; String
	 * separatorBetweenFields = ", "; List list =
	 * bizLogic.getList(sourceObjectName, displayParticipantFields, valueField,
	 * whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
	 * separatorBetweenFields, true); //get list of Participant's names
	 * valueField = Constants.SYSTEM_IDENTIFIER; sourceObjectName =
	 * Participant.class.getName(); String[] participantsFields =
	 * {"lastName","firstName","birthDate","socialSecurityNumber"}; String[]
	 * whereColumnName2 =
	 * {"lastName","firstName","birthDate","socialSecurityNumber"}; String[]
	 * whereColumnCondition2 = {"!=","!=","is not","is not"}; Object[]
	 * whereColumnValue2 = {"","",null,null};
	 * if(Variables.databaseName.equals(Constants.MYSQL_DATABASE)) {
	 * whereColumnCondition2 = new String[]{"!=","!=","is not","is not"};
	 * whereColumnValue2=new String[]{"","",null,null}; } else { // for ORACLE
	 * whereColumnCondition2 = new
	 * String[]{Constants.IS_NOT_NULL,Constants.IS_NOT_NULL
	 * ,Constants.IS_NOT_NULL,Constants.IS_NOT_NULL}; whereColumnValue2=new
	 * String[]{"","","",""}; } String joinCondition2 =
	 * Constants.OR_JOIN_CONDITION; String separatorBetweenFields2 = ", "; List
	 * listOfParticipants = bizLogic.getList(sourceObjectName,
	 * participantsFields, valueField, whereColumnName2, whereColumnCondition2,
	 * whereColumnValue2, joinCondition2, separatorBetweenFields, false); //
	 * removing blank participants from the list of Participants
	 * list=removeBlankParticipant(list, listOfParticipants); //Mandar bug
	 * id:1628 :- sort participant dropdown list Collections.sort(list );
	 * Logger.out.debug("Paticipants List"+list);
	 * request.setAttribute(Constants.PARTICIPANT_LIST, list); } private List
	 * removeBlankParticipant(List list, List listOfParticipants) { List
	 * listOfActiveParticipant=new ArrayList(); for(int i=0; i<list.size(); i++)
	 * { NameValueBean nameValueBean =(NameValueBean)list.get(i);
	 * if(Long.parseLong(nameValueBean.getValue()) == -1) {
	 * listOfActiveParticipant.add(list.get(i)); continue; } for(int j=0;
	 * j<listOfParticipants.size(); j++) {
	 * if(Long.parseLong(((NameValueBean)listOfParticipants.get(j)).getValue())
	 * == -1) continue; NameValueBean participantsBean =
	 * (NameValueBean)listOfParticipants.get(j); if(
	 * nameValueBean.getValue().equals(participantsBean.getValue()) ) {
	 * listOfActiveParticipant.add(listOfParticipants.get(j)); break; } } }
	 * Logger.out.debug(
	 * "No.Of Active Participants Registered with Protocol~~~~~~~~~~~~~~~~~~~~~~~>"
	 * +listOfActiveParticipant.size()); return listOfActiveParticipant; }
	 */

	/**
	 * Commented by Abhishek Mehta Method to load protocol participant
	 * identifier number list
	 *
	 * @param protocolID
	 * @param bizLogic
	 * @param request
	 * @throws Exception
	 */
	/*
	 * private void loadPaticipantNumberList(long protocolID, IBizLogic
	 * bizLogic, HttpServletRequest request) throws Exception { //get list of
	 * Participant's names String sourceObjectName =
	 * CollectionProtocolRegistration.class.getName(); String
	 * displayParticipantNumberFields[] = {"protocolParticipantIdentifier"};
	 * String valueField = "protocolParticipantIdentifier"; String
	 * whereColumnName[] = {"collectionProtocol."+Constants.SYSTEM_IDENTIFIER,
	 * "protocolParticipantIdentifier"}; String whereColumnCondition[];// =
	 * {"=","!="}; Object[] whereColumnValue;// = {new Long(protocolID),"null"};
	 * // if(Variables.databaseName.equals(Constants.MYSQL_DATABASE)) // {
	 * whereColumnCondition = new String[]{"=","!="}; whereColumnValue = new
	 * Object[]{new Long(protocolID),"null"}; // } // else // { //
	 * whereColumnCondition = new String[]{"=","!=null"}; // whereColumnValue =
	 * new Object[]{new Long(protocolID),""}; // } String joinCondition =
	 * Constants.AND_JOIN_CONDITION; String separatorBetweenFields = ""; List
	 * list = bizLogic.getList(sourceObjectName, displayParticipantNumberFields,
	 * valueField, whereColumnName, whereColumnCondition, whereColumnValue,
	 * joinCondition, separatorBetweenFields, true);
	 * Logger.out.debug("Paticipant Number List"+list);
	 * request.setAttribute(Constants.PROTOCOL_PARTICIPANT_NUMBER_LIST, list); }
	 */

	/**
	 * Method to load list of collection protocol event point.
	 *
	 * @param protocolID : protocolID
	 * @param bizLogic : bizLogic
	 * @param request : request
	 * @param form : form
	 * @throws Exception : Exception
	 */
	private void loadCollectionProtocolEvent(long protocolID, IBizLogic bizLogic,
			HttpServletRequest request, SpecimenCollectionGroupForm form) throws Exception
	{
		final String sourceObjectName = CollectionProtocolEvent.class.getName();
		final String[] displayEventFields = {"studyCalendarEventPoint", "collectionPointLabel"};
		final String valueField = "id";
		final String[] whereColumnName = {"collectionProtocol." + Constants.SYSTEM_IDENTIFIER};
		final String[] whereColumnCondition = {"="};
		final Object[] whereColumnValue = {protocolID};
		final String joinCondition = Constants.AND_JOIN_CONDITION;
		final String separatorBetweenFields = ",";

		final List list = bizLogic.getList(sourceObjectName, displayEventFields, valueField,
				whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
				separatorBetweenFields);

		request.setAttribute(Constants.STUDY_CALENDAR_EVENT_POINT_LIST, list);
		// Bug #8533
		// Patch: 8533_5
		if (list.size() >= 2 && form.getCollectionProtocolEventId() <= 0)
		{
			form.setCollectionProtocolEventId(Long
					.valueOf(((NameValueBean) list.get(1)).getValue()));
		}
	}

	/**
	 * Method to load list of participant medical identifier.
	 *
	 * @param participantID : participantID
	 * @param bizLogic : bizLogic
	 * @param request : request
	 *
	 * @throws Exception : Exception
	 */
	private void loadParticipantMedicalIdentifier(long participantID, IBizLogic bizLogic,
			HttpServletRequest request) throws Exception
	{
		// get list of Participant's names
		final String sourceObjectName = ParticipantMedicalIdentifier.class.getName();
		final String[] displayEventFields = {"medicalRecordNumber"};
		final String valueField = Constants.SYSTEM_IDENTIFIER;
		final String[] whereColumnName = {"participant." + Constants.SYSTEM_IDENTIFIER,
				"medicalRecordNumber"};
		final String[] whereColumnCondition = {"=", "is not null"};
		final Object[] whereColumnValue = {Long.valueOf(participantID)};
		final String joinCondition = Constants.AND_JOIN_CONDITION;
		final String separatorBetweenFields = "";

		final List list = bizLogic.getList(sourceObjectName, displayEventFields, valueField,
				whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
				separatorBetweenFields, false);

		request.setAttribute(Constants.PARTICIPANT_MEDICAL_IDNETIFIER_LIST, list);
	}

	/**
	 * Method to retrieve participant id from the protocol participant id.
	 *
	 * @param participantProtocolId : participantProtocolId
	 * @param dao the dao
	 *
	 * @return String : String
	 *
	 * @throws Exception : Exception
	 */
	private String getParticipantIdForProtocolId(String participantProtocolId, DAO dao)
			throws Exception
	{
		final String sourceObjectName = CollectionProtocolRegistration.class.getName();
		final String[] selectColumnName = {"participant.id"};

		final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause.addCondition(new EqualClause("protocolParticipantIdentifier",
				participantProtocolId));
		final List participantList = dao.retrieve(sourceObjectName, selectColumnName,
				queryWhereClause);
		if (participantList != null && !participantList.isEmpty())
		{

			final String participantId = ((Long) participantList.get(0)).toString();
			return participantId;

		}
		return null;
	}

	/**
	 * Method to retrieve participant protocol identifier for given CP and
	 * participant id.
	 *
	 * @param participantId : participantId
	 * @param cpId : cpId
	 * @param dao the dao
	 *
	 * @return String : String
	 *
	 * @throws Exception : Exception
	 */
	private String getParticipantProtocolIdForCPAndParticipantId(String participantId, String cpId,
			DAO dao) throws Exception
	{
		final String sourceObjectName = CollectionProtocolRegistration.class.getName();
		final String[] selectColumnName = {"protocolParticipantIdentifier"};
		final String[] whereColumnName = {"participant.id", "collectionProtocol.id"};
		//String[] whereColumnCondition = {"=", "="};
		final Object[] whereColumnValue = {Long.valueOf(participantId), Long.valueOf(cpId)};
		final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause.addCondition(new EqualClause(whereColumnName[0], whereColumnValue[0]));
		queryWhereClause.andOpr();
		queryWhereClause.addCondition(new EqualClause(whereColumnName[1], whereColumnValue[1]));
		final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
		if (list != null && !list.isEmpty())
		{
			final Iterator iter = list.iterator();
			while (iter.hasNext())
			{
				final Object identifier = (Object) iter.next();
				if (identifier != null)
				{
					return identifier.toString();
				}
			}
		}
		return null;
	}

	/**
	 * Method to set default values related to calendar event point list.
	 *
	 * @param object calendar event point list
	 * @param request object of HttpServletRequest
	 * @param specimenCollectionGroupForm object of specimenCollectionGroup action form
	 *
	 * @throws DAOException : DAOException
	 */
	private void setCalendarEventPoint(Object object, HttpServletRequest request,
			SpecimenCollectionGroupForm specimenCollectionGroupForm) throws DAOException
	{
		// Patch ID: Bug#3184_27
		// By Abhishek Mehta
		// int numberOfSpecimen = 1;
		if (object != null)
		{
			final CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) object;

			// Set checkbox status depending upon the days of study calendar
			// event point. If it is zero, then unset the restrict
			// checkbox, otherwise set the restrict checkbox
			//Bug 14487
			/*final Double studyCalendarEventPoint = collectionProtocolEvent
					.getStudyCalendarEventPoint();
			if (studyCalendarEventPoint.doubleValue() == 0)
			{
				specimenCollectionGroupForm.setRestrictSCGCheckbox("false");
			}
			else*/
			{
				specimenCollectionGroupForm.setRestrictSCGCheckbox("true");
			}
		}
		else if (object == null)
		{
			// Set checkbox status
			specimenCollectionGroupForm.setRestrictSCGCheckbox("false");
		}
		// Sets the value for number of specimen field on the specimen
		// collection group page.
		// Set the number of actual specimen requirements for validation
		// purpose.
		// This value is used in validate method of
		// SpecimenCollectionGroupForm.java.
		// request.setAttribute(Constants.NUMBER_OF_SPECIMEN_REQUIREMENTS,
		// numberOfSpecimen + "");
	}

	// Consent Tracking Virender Mehta
	/**
	 * Getcollection protocol registration obj.
	 *
	 * @param idOfSelectedRadioButton Id for selected radio button.
	 * @param cpId CollectionProtocolID CollectionProtocolID selected by dropdown
	 * @param indexType i.e Which Radio button is selected participantId or
	 * protocolParticipantIdentifier
	 * @param dao the dao
	 *
	 * @return collectionProtocolRegistration CollectionProtocolRegistration
	 * object
	 *
	 * @throws BizLogicException : BizLogicException
	 */
	private CollectionProtocolRegistration getcollectionProtocolRegistrationObj(
			String idOfSelectedRadioButton, String cpId, String indexType, DAO dao)
			throws BizLogicException
	{
		CollectionProtocolRegistration collectionProtocolRegistration = null;
		try
		{
			final String[] colName = new String[2];
			final Object[] val = new Object[2];
			if (indexType.equalsIgnoreCase(Constants.PARTICIPANT_ID))
			{
				colName[0] = "participant.id";
				colName[1] = "collectionProtocol.id";
				val[0] = Long.valueOf(idOfSelectedRadioButton);
			}
			else
			{
				colName[0] = "protocolParticipantIdentifier";
				colName[1] = "collectionProtocol.id";
				val[0] = idOfSelectedRadioButton;
			}

			val[1] = Long.valueOf(cpId);
			final String[] colCondition = {"=", "="};
			final QueryWhereClause queryWhereClause = new QueryWhereClause(
					CollectionProtocolRegistration.class.getName());
			queryWhereClause.addCondition(new EqualClause(colName[0], val[0]));
			queryWhereClause.andOpr();
			queryWhereClause.addCondition(new EqualClause(colName[1], val[1]));
			final List collProtRegObj = dao.retrieve(
					CollectionProtocolRegistration.class.getName(), null, queryWhereClause, true);
			if ((collProtRegObj != null) && (!collProtRegObj.isEmpty()))
			{
				collectionProtocolRegistration = (CollectionProtocolRegistration) collProtRegObj
						.get(0);
			}
		}
		catch (final DAOException daoEx)
		{
			LOGGER.error(daoEx.getMessage(), daoEx);
			throw new BizLogicException(daoEx);
		}
		return collectionProtocolRegistration;
	}

	/**
	 * Prepare Map for Consent tiers.
	 *
	 * @param participantResponseList This list will be iterated to map to populate participant
	 * Response status.
	 *
	 * @return Map : tempMap
	 */
	private Map prepareConsentMap(List participantResponseList)
	{
		final Map tempMap = new HashMap();
		if (participantResponseList != null)
		{
			int identifier = 0;
			final Iterator consentResponseCollectionIter = participantResponseList.iterator();
			while (consentResponseCollectionIter.hasNext())
			{
				final ConsentTierResponse consentTierResponse = (ConsentTierResponse) consentResponseCollectionIter
						.next();
				final ConsentTier consent = consentTierResponse.getConsentTier();
				final String idKey = "ConsentBean:" + identifier + "_consentTierID";
				final String statementKey = "ConsentBean:" + identifier + "_statement";
				final String responseKey = "ConsentBean:" + identifier + "_participantResponse";
				final String participantResponceIdKey = "ConsentBean:" + identifier
						+ "_participantResponseID";
				final String scgResponsekey = "ConsentBean:" + identifier
						+ "_specimenCollectionGroupLevelResponse";
				final String scgResponseIDkey = "ConsentBean:" + identifier
						+ "_specimenCollectionGroupLevelResponseID";

				tempMap.put(idKey, consent.getId());
				tempMap.put(statementKey, consent.getStatement());
				tempMap.put(responseKey, consentTierResponse.getResponse());
				tempMap.put(participantResponceIdKey, consentTierResponse.getId());
				tempMap.put(scgResponsekey, consentTierResponse.getResponse());
				tempMap.put(scgResponseIDkey, null);
				identifier++;
			}
		}
		return tempMap;
	}

	// Consent Tracking Virender Mehta

	/**
	 * This function is used for retriving specimen from Specimen collection
	 * group Object.
	 *
	 * @param specimenCollectionGroupObj : specimenCollectionGroupObj
	 * @param finalDataList : finalDataList
	 * @param dao the dao
	 *
	 * @throws BizLogicException : BizLogicException
	 */
	private void getSpecimenDetails(SpecimenCollectionGroup specimenCollectionGroupObj,
			List finalDataList, DAO dao) throws BizLogicException
	{
		List specimen = null;
		try
		{
			if (specimenCollectionGroupObj.getId() != null)
			{
				specimen = dao.retrieveAttribute(SpecimenCollectionGroup.class, "id",
						specimenCollectionGroupObj.getId(), "elements(specimenCollection)");

				if (specimen != null)
				{
					// Collection specimen =
					// specimenCollectionGroupObj.getSpecimenCollection();
					final Iterator specimenIterator = specimen.iterator();
					while (specimenIterator.hasNext())
					{
						final Specimen specimenObj = (Specimen) specimenIterator.next();
						this.getDetailsOfSpecimen(specimenObj, finalDataList);
					}
				}
			}
		}
		catch (final DAOException daoEx)
		{
			LOGGER.error(daoEx.getMessage(), daoEx);
			throw new BizLogicException(daoEx);
		}
	}

	/**
	 * This function is used for retriving specimen and sub specimen's
	 * attributes.
	 *
	 * @param specimenObj : specimenObj
	 * @param finalDataList : finalDataList
	 *
	 * @throws BizLogicException : BizLogicException
	 */
	private void getDetailsOfSpecimen(Specimen specimenObj, List finalDataList)
			throws BizLogicException
	{
		final List specimenDetailList = new ArrayList();

		if (specimenObj.getActivityStatus().equals(Status.ACTIVITY_STATUS_ACTIVE.toString()))
		{
			specimenDetailList.add(specimenObj.getLabel());
			specimenDetailList.add(specimenObj.getSpecimenType());
			if (specimenObj.getSpecimenPosition() == null)
			{
				specimenDetailList.add(Constants.VIRTUALLY_LOCATED);
			}
			else
			{

				final StorageContainer storageContainer = specimenObj.getSpecimenPosition()
						.getStorageContainer();

				if (specimenObj != null && specimenObj.getSpecimenPosition() != null)
				{
					final String storageLocation = storageContainer.getName() + ": X-Axis-"
							+ specimenObj.getSpecimenPosition().getPositionDimensionOne()
							+ ", Y-Axis-"
							+ specimenObj.getSpecimenPosition().getPositionDimensionTwo();
					specimenDetailList.add(storageLocation);
				}
			}
			specimenDetailList.add(specimenObj.getSpecimenClass());
			finalDataList.add(specimenDetailList);
		}

	}

	/**
	 * Gets the associated identified report id.
	 *
	 * @param scgId : scgId
	 *
	 * @return Long : long
	 *
	 * @throws BizLogicException : BizLogicException
	 */
	private Long getAssociatedIdentifiedReportId(Long scgId) throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IdentifiedSurgicalPathologyReportBizLogic bizLogic = (IdentifiedSurgicalPathologyReportBizLogic) factory
				.getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());
		final String sourceObjectName = IdentifiedSurgicalPathologyReport.class.getName();
		final String[] displayEventFields = {"id"};
		final String valueField = Constants.SYSTEM_IDENTIFIER;
		final String[] whereColumnName = {Constants.COLUMN_NAME_SCG_ID};
		final String[] whereColumnCondition = {"="};
		final Object[] whereColumnValue = {scgId};
		final String joinCondition = Constants.AND_JOIN_CONDITION;
		final String separatorBetweenFields = "";

		final List list = bizLogic.getList(sourceObjectName, displayEventFields, valueField,
				whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
				separatorBetweenFields, false);
		if (list != null && list.size() > 1)
		{
			final NameValueBean nvBean = (NameValueBean) list.get(1);
			return (Long.valueOf(nvBean.getValue()));
		}
		return null;
	}

	/**
	 * Gets the object id.
	 *
	 * @param form : form
	 *
	 * @return String : String
	 */
	protected String getObjectId(AbstractActionForm form)
	{
		final SpecimenCollectionGroupForm specimenCollectionGroupForm = (SpecimenCollectionGroupForm) form;
		if (specimenCollectionGroupForm.getCollectionProtocolId() != 0L
				&& specimenCollectionGroupForm.getCollectionProtocolId() != -1L)
		{
			return Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_"
					+ specimenCollectionGroupForm.getCollectionProtocolId();
		}
		else
		{
			return null;
		}

	}
}