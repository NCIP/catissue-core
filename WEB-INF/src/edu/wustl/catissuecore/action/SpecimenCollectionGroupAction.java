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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.bizlogic.AnnotationUtil;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.CDEBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * SpecimenCollectionGroupAction initializes the fields in the
 * New Specimen Collection Group page.
 * @author ajay_sharma
 */
public class SpecimenCollectionGroupAction extends SecureAction
{
	/**
	 * Overrides the execute method of Action class.
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 */
	public ActionForward executeSecureAction(ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response)
			throws Exception
	{

		//changes made by Baljeet
		String treeRefresh = request.getParameter("refresh");
		request.setAttribute("refresh", treeRefresh);
		
		SpecimenCollectionGroupForm specimenCollectionGroupForm = (SpecimenCollectionGroupForm) form;
		IBizLogic bizLogicObj = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		Logger.out.debug("SCGA : " + specimenCollectionGroupForm.getId());
		String nodeId = null;
		/**
		 * Bug id : 4213
		 * Patch id  : 4213_2
		 * Description : getting parameters from request and keeping them in seesion to keep the node in tree selected.
		 */
		if (request.getParameter("clickedNodeId") != null)
		{
			nodeId = request.getParameter("clickedNodeId");
			request.getSession().setAttribute("nodeId", nodeId);
		}
		//	set the menu selection 
		request.setAttribute(Constants.MENU_SELECTED, "14");

		//pageOf and operation attributes required for Advance Query Object view.
		String pageOf = request.getParameter(Constants.PAGEOF);

		//Gets the value of the operation parameter.
		String operation = (String) request.getParameter(Constants.OPERATION);

		//Sets the operation attribute to be used in the Edit/View Specimen Collection Group Page in Advance Search Object View. 
		request.setAttribute(Constants.OPERATION, operation);
		if (operation.equalsIgnoreCase(Constants.ADD))
		{
			specimenCollectionGroupForm.setId(0);
			Logger.out.debug("SCGA : set to 0 " + specimenCollectionGroupForm.getId());
		}

		boolean isOnChange = false;
		String str = request.getParameter("isOnChange");
		if (str != null)
		{
			if (str.equals("true"))
			{
				isOnChange = true;
			}
		}
		//For Consent Tracking (Virender Mehta)   -  Start

		//If radioButtonSelected = 1 then selected radio button is for Participant
		//If radioButtonSelected = 2 then selected radio button is for Protocol Participant Identifier
		int radioButtonSelected = 1;
		//Id of Selected Participant or Protocol Participant Identifier
		String selectedParticipantOrPPIdentifier_id = null;
		// Radio button for Protocol Participant Identifier or Participant
		String radioButtonSelectedForType = null;
		String selectedCollectionProtocol_id = String.valueOf(specimenCollectionGroupForm.getCollectionProtocolId());
		if (selectedCollectionProtocol_id.equalsIgnoreCase(Constants.SELECTED_COLLECTION_PROTOCOL_ID))
		{
			Map forwardToHashMap = (Map) request.getAttribute(Constants.FORWARD_TO_HASHMAP);
			if (forwardToHashMap != null)
			{
				selectedCollectionProtocol_id = forwardToHashMap.get(Constants.COLLECTION_PROTOCOL_ID).toString();
				selectedParticipantOrPPIdentifier_id = forwardToHashMap.get(Constants.PARTICIPANT_ID).toString();
				radioButtonSelectedForType = Constants.PARTICIPANT_ID;
				if (selectedParticipantOrPPIdentifier_id.equals("0"))
				{
					selectedParticipantOrPPIdentifier_id = forwardToHashMap.get(Constants.PARTICIPANT_PROTOCOL_ID).toString();
					radioButtonSelectedForType = Constants.PARTICIPANT_PROTOCOL_ID;
				}
			}
		}
		else
		{
			radioButtonSelected = (int) specimenCollectionGroupForm.getRadioButtonForParticipant();
			if (radioButtonSelected == 1)
			{
				selectedParticipantOrPPIdentifier_id = Long.toString(specimenCollectionGroupForm.getParticipantId());
				radioButtonSelectedForType = Constants.PARTICIPANT_ID;
			}
			else
			{
				selectedParticipantOrPPIdentifier_id = specimenCollectionGroupForm.getProtocolParticipantIdentifier();
				radioButtonSelectedForType = Constants.PARTICIPANT_PROTOCOL_ID;
			}
		}
		CollectionProtocolRegistration collectionProtocolRegistration = null;
		if (selectedParticipantOrPPIdentifier_id != null && !(selectedParticipantOrPPIdentifier_id.equalsIgnoreCase("0")))
		{
			//Get CollectionprotocolRegistration Object
			collectionProtocolRegistration = getcollectionProtocolRegistrationObj(selectedParticipantOrPPIdentifier_id,
					selectedCollectionProtocol_id, radioButtonSelectedForType);
		}
		else if (specimenCollectionGroupForm.getId() != 0)
		{
			//Get CollectionprotocolRegistration Object
			SpecimenCollectionGroupBizLogic specimenCollectiongroupBizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance()
					.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			collectionProtocolRegistration = (CollectionProtocolRegistration) specimenCollectiongroupBizLogic.retrieveAttribute(
					SpecimenCollectionGroup.class.getName(), specimenCollectionGroupForm.getId(), "collectionProtocolRegistration");
		}
		User witness = null;
		if (collectionProtocolRegistration.getId() != null)
		{
			witness = (User) bizLogicObj.retrieveAttribute(CollectionProtocolRegistration.class.getName(), collectionProtocolRegistration.getId(),
					"consentWitness");
		}
		//User witness= userObj.getConsentWitness();
		//Resolved Lazy 
		if (witness == null || witness.getFirstName() == null)
		{
			String witnessName = "";
			specimenCollectionGroupForm.setWitnessName(witnessName);
		}
		else
		{
			String witnessFullName = witness.getLastName() + ", " + witness.getFirstName();
			specimenCollectionGroupForm.setWitnessName(witnessFullName);
		}
		String getConsentDate = Utility
				.parseDateToString(collectionProtocolRegistration.getConsentSignatureDate(), Constants.DATE_PATTERN_MM_DD_YYYY);
		specimenCollectionGroupForm.setConsentDate(getConsentDate);
		String getSignedConsentURL = Utility.toString(collectionProtocolRegistration.getSignedConsentDocumentURL());
		specimenCollectionGroupForm.setSignedConsentUrl(getSignedConsentURL);
		//Set witnessName,ConsentDate and SignedConsentURL			
		//Resolved Lazy ----collectionProtocolRegistration.getConsentTierResponseCollection()
		Collection consentTierResponseCollection = (Collection) bizLogicObj.retrieveAttribute(CollectionProtocolRegistration.class.getName(),
				collectionProtocolRegistration.getId(), "elements(consentTierResponseCollection)");
		Set participantResponseSet = (Set) consentTierResponseCollection;
		List participantResponseList = new ArrayList(participantResponseSet);
		specimenCollectionGroupForm.setCollectionProtocolRegistrationId(collectionProtocolRegistration.getId());
		if (operation.equalsIgnoreCase(Constants.ADD))
		{
			ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
			if (errors == null)
			{
				String protocolEventID = request.getParameter(Constants.PROTOCOL_EVENT_ID);
				if (protocolEventID == null || protocolEventID.equalsIgnoreCase(Constants.FALSE))
				{
					Map tempMap = prepareConsentMap(participantResponseList);
					specimenCollectionGroupForm.setConsentResponseForScgValues(tempMap);
				}
			}
			specimenCollectionGroupForm.setConsentTierCounter(participantResponseList.size());
		}
		else
		{
			String scgID = String.valueOf(specimenCollectionGroupForm.getId());
			SpecimenCollectionGroup specimenCollectionGroup= Utility.getSCGObj(scgID);
			//List added for grid
			List specimenDetails = new ArrayList();
			getSpecimenDetails(specimenCollectionGroup, specimenDetails);
			List columnList = ConsentUtil.columnNames();
			//Resolved Lazy
			//Collection consentResponse = specimenCollectionGroup.getCollectionProtocolRegistration().getConsentTierResponseCollection();
			//Collection consentResponseStatuslevel= specimenCollectionGroup.getConsentTierStatusCollection();
			Collection consentResponse = (Collection) bizLogicObj.retrieveAttribute(SpecimenCollectionGroup.class.getName(), specimenCollectionGroup
					.getId(), "elements(collectionProtocolRegistration.consentTierResponseCollection)");
			Collection consentResponseStatuslevel = (Collection) bizLogicObj.retrieveAttribute(SpecimenCollectionGroup.class.getName(),
					specimenCollectionGroup.getId(), "elements(consentTierStatusCollection)");
			String scgResponsekey =  "_specimenCollectionGroupLevelResponse";
			String scgResponseIDkey = "_specimenCollectionGroupLevelResponseID";
			Map tempMap=ConsentUtil.prepareSCGResponseMap(consentResponseStatuslevel, consentResponse,scgResponsekey,scgResponseIDkey);
			specimenCollectionGroupForm.setConsentResponseForScgValues(tempMap);
			specimenCollectionGroupForm.setConsentTierCounter(participantResponseList.size());
			HttpSession session = request.getSession();
			session.setAttribute(Constants.SPECIMEN_LIST, specimenDetails);
			session.setAttribute(Constants.COLUMNLIST, columnList);
		}
		List specimenCollectionGroupResponseList = Utility.responceList(operation);
		request.setAttribute(Constants.LIST_OF_SPECIMEN_COLLECTION_GROUP, specimenCollectionGroupResponseList);

		String tabSelected = request.getParameter(Constants.SELECTED_TAB);
		if (tabSelected != null)
		{
			request.setAttribute(Constants.SELECTED_TAB, tabSelected);
		}

		//	For Consent Tracking (Virender Mehta)	    -  End

		// get list of Protocol title.
		SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
				Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
		//populating protocolist bean.
		String sourceObjectName = CollectionProtocol.class.getName();
		String[] displayNameFields = {"shortTitle"};
		String valueField = Constants.SYSTEM_IDENTIFIER;
		List list = bizLogic.getList(sourceObjectName, displayNameFields, valueField, true);
		request.setAttribute(Constants.PROTOCOL_LIST, list);
		
		//Populating the Site Type bean
		sourceObjectName = Site.class.getName();
		String[] siteDisplaySiteFields = {"name"};
		list = bizLogic.getList(sourceObjectName, siteDisplaySiteFields, valueField, true);
		request.setAttribute(Constants.SITELIST, list);

		//Populating the participants registered to a given protocol
		/**For Migration Start**/
		//		loadPaticipants(specimenCollectionGroupForm.getCollectionProtocolId() , bizLogic, request);
		/**For Migration End**/
		//Populating the protocol participants id registered to a given protocol
		//By Abhishek Mehta -Performance Enhancement
		//loadPaticipantNumberList(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
		String protocolParticipantId = specimenCollectionGroupForm.getProtocolParticipantIdentifier();
		//Populating the participants Medical Identifier for a given participant
		loadParticipantMedicalIdentifier(specimenCollectionGroupForm.getParticipantId(), bizLogic, request);

		//Load Clinical status for a given study calander event point
		String changeOn = request.getParameter(Constants.CHANGE_ON);
		
		if (changeOn != null && changeOn.equals(Constants.COLLECTION_PROTOCOL_ID))
		{
			specimenCollectionGroupForm.setCollectionProtocolEventId(new Long(-1));
		}

		//Populating the Collection Protocol Events
		loadCollectionProtocolEvent(specimenCollectionGroupForm.getCollectionProtocolId(), bizLogic, request, specimenCollectionGroupForm);

		Object CPEObject = bizLogic.retrieve(CollectionProtocolEvent.class.getName(), new Long(
				specimenCollectionGroupForm.getCollectionProtocolEventId()));

		// The values of restrict checkbox and the number of specimen must alos populate in edit mode.
		if ((isOnChange || operation.equalsIgnoreCase(Constants.EDIT)))
		{
			// Added by Vijay Pande. Method is created since code was repeating for SUBMITTED_FOR= "AddNew" || "Default" value.
			setCalendarEventPoint(CPEObject, request, specimenCollectionGroupForm);
		}

		// populating clinical Diagnosis field 
		CDE cde = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_CLINICAL_DIAGNOSIS);
		CDEBizLogic cdeBizLogic = (CDEBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.CDE_FORM_ID);
		List clinicalDiagnosisList = new ArrayList();
		clinicalDiagnosisList.add(new NameValueBean(Constants.SELECT_OPTION, "" + Constants.SELECT_OPTION_VALUE));
		cdeBizLogic.getFilteredCDE(cde.getPermissibleValues(), clinicalDiagnosisList);
		request.setAttribute(Constants.CLINICAL_DIAGNOSIS_LIST, clinicalDiagnosisList);

		// populating clinical Status field
		//		NameValueBean undefinedVal = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
		List clinicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CLINICAL_STATUS, null);
		request.setAttribute(Constants.CLINICAL_STATUS_LIST, clinicalStatusList);

		//Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);
		//Sets the collectionStatusList attribute to be used in the Site Add/Edit Page.
		request.setAttribute(Constants.COLLECTIONSTATUSLIST, Constants.SCG_COLLECTION_STATUS_VALUES);
		//fix for bug no.7390
		if(specimenCollectionGroupForm.getCollectionStatus()==null)
		{
			specimenCollectionGroupForm.setCollectionStatus(Constants.COLLECTION_STATUS_PENDING);
		}
		//end for fix. Bug no.7390
		Logger.out.debug("CP ID in SCG Action======>" + specimenCollectionGroupForm.getCollectionProtocolId());
		Logger.out.debug("Participant ID in SCG Action=====>" + specimenCollectionGroupForm.getParticipantId() + "  "
				+ specimenCollectionGroupForm.getProtocolParticipantIdentifier());

		/**
		 * Name: Vijay Pande
		 * check for SUBMITTED_FOR with "AddNew" is added since while coming from specimen->scg->AddNew link for participant-> Register participant -> submit then  the SUBMITTED_FOR is equal to "AddNew"
		 * If the flow is  scg->AddNew link for participant-> Register participant -> submit then  the SUBMITTED_FOR is equal to "Default"
		 */
		// -------called from Collection Protocol Registration start-------------------------------
		if ((request.getAttribute(Constants.SUBMITTED_FOR) != null)
				&& ((request.getAttribute(Constants.SUBMITTED_FOR).equals("Default")) || (request.getAttribute(Constants.SUBMITTED_FOR)
						.equals(Constants.ADDNEW_LINK))))
		{
			Logger.out.debug("Populating CP and Participant in SCG ====  AddNew operation loop");

			Long cprId = new Long(specimenCollectionGroupForm.getCollectionProtocolRegistrationId());

			if (cprId != null)
			{
				Object CPRObject = bizLogic.retrieve(CollectionProtocolRegistration.class.getName(), cprId);
				if (CPRObject != null)
				{
					CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) CPRObject;

					long cpID = cpr.getCollectionProtocol().getId().longValue();
					long pID = cpr.getParticipant().getId().longValue();
					String ppID = cpr.getProtocolParticipantIdentifier();

					Logger.out.debug("cpID : " + cpID + "   ||  pID : " + pID + "    || ppID : " + ppID);

					specimenCollectionGroupForm.setCollectionProtocolId(cpID);

					//Populating the participants registered to a given protocol
					/**For Migration Start**/
					//					loadPaticipants(cpID , bizLogic, request);
					/**For Migration Start**/
					//By Abhishek Mehta -Performance Enhancement
					//loadPaticipantNumberList(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
					/**
					 * Name: Vijay Pande
					 * Reviewer Name: Aarti Sharma
					 * participant associated with collection protocol is explicitly retrived from DB since its lazy load property is true
					 */
					Participant cprParticipant = (Participant) bizLogic.retrieveAttribute(CollectionProtocolRegistration.class.getName(),
							cpr.getId(), Constants.COLUMN_NAME_PARTICIPANT);
					// set participant id in request. This is required only in CP based View since SpecimenTreeView.jsp is retrieveing participant id from request
					if (cprParticipant.getId() != null)
					{
						request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID, cprParticipant.getId().toString());
					}
					String firstName = Utility.toString(cprParticipant.getFirstName());
					String lastName = Utility.toString(cprParticipant.getLastName());
					String birthDate = Utility.toString(cprParticipant.getBirthDate());
					String ssn = Utility.toString(cprParticipant.getSocialSecurityNumber());
					if (firstName.trim().length() > 0 || lastName.trim().length() > 0 || birthDate.trim().length() > 0 || ssn.trim().length() > 0)
					{
						specimenCollectionGroupForm.setParticipantId(pID);
						specimenCollectionGroupForm.setRadioButtonForParticipant(1);
						specimenCollectionGroupForm.setParticipantName(lastName + ", " + firstName);
					}
					//Populating the protocol participants id registered to a given protocol

					else if (cpr.getProtocolParticipantIdentifier() != null)
					{
						specimenCollectionGroupForm.setProtocolParticipantIdentifier(ppID);
						specimenCollectionGroupForm.setRadioButtonForParticipant(2);
					}

					//Populating the Collection Protocol Events
					loadCollectionProtocolEvent(specimenCollectionGroupForm.getCollectionProtocolId(), bizLogic, request, specimenCollectionGroupForm);

					//Load Clinical status for a given study calander event point
					CPEObject = bizLogic.retrieve(CollectionProtocolEvent.class.getName(), new Long(
							specimenCollectionGroupForm.getCollectionProtocolEventId()));
					if (isOnChange && CPEObject != null)
					{
						setCalendarEventPoint(CPEObject, request, specimenCollectionGroupForm);
					}
				}
			}
			request.setAttribute(Constants.SUBMITTED_FOR, "Default");
		}

		//*************  ForwardTo implementation *************
		HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");

		if (forwardToHashMap != null)
		{

			/**
			 * Name: Falguni Sachde
			 * Reviewer Name: 
			 * Attribute  collectionProtocolName added to show Collection ProtocolName in Add mode only.
			 */

			Long collectionProtocolId = (Long) forwardToHashMap.get("collectionProtocolId");
			String collectionProtocolName = (String) request.getSession().getAttribute("cpTitle");
			if (collectionProtocolId == null && request.getParameter("cpId") != null && !request.getParameter("cpId").equals("null"))
			{
				collectionProtocolId = new Long(request.getParameter("cpId"));
			}

			Long participantId = (Long) forwardToHashMap.get("participantId");
			String participantProtocolId = (String) forwardToHashMap.get("participantProtocolId");

			specimenCollectionGroupForm.setCollectionProtocolId(collectionProtocolId.longValue());
			specimenCollectionGroupForm.setCollectionProtocolName(collectionProtocolName);

			/**
			 * Name : Deepti Shelar
			 * Bug id : 4216
			 * Patch id  : 4216_1
			 * Description : populating list of ParticipantMedicalIdentifiers for given participant id 
			 */
			loadParticipantMedicalIdentifier(participantId, bizLogic, request);

			if (participantId != null && participantId.longValue() != 0)
			{
				//Populating the participants registered to a given protocol
				/**For Migration Start**/
				//				loadPaticipants(collectionProtocolId.longValue(), bizLogic, request);
				/**For Migration End**/
				//By Abhishek Mehta -Performance Enhancement
				//loadPaticipantNumberList(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
				specimenCollectionGroupForm.setParticipantId(participantId.longValue());
				specimenCollectionGroupForm.setRadioButtonForParticipant(1);
				request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID, participantId.toString());
				/**For Migration Start**/

				Object participantObject = bizLogic.retrieve(Participant.class.getName(), participantId);
				if (participantObject != null)
				{

					Participant participant = (Participant) participantObject;
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
						specimenCollectionGroupForm.setParticipantName(lastName + ", " + firstName);
					}
					else if (lastName.equals("") && !firstName.equals(""))
					{
						specimenCollectionGroupForm.setParticipantName(participant.getFirstName());
					}
					else if (firstName.equals("") && !lastName.equals(""))
					{
						specimenCollectionGroupForm.setParticipantName(participant.getLastName());
					}

				}
				/**For Migration End**/
				/**
				 * Name : Deepti Shelar
				 * Reviewer Name : Sachin Lale
				 * Bug id : FutureSCG
				 * Patch Id : FutureSCG_1
				 * Description : setting participantProtocolId to form
				 */
				if (participantProtocolId == null)
				{
					participantProtocolId = getParticipantProtocolIdForCPAndParticipantId(participantId.toString(), collectionProtocolId.toString(),
							bizLogic);
					if (participantProtocolId != null)
					{
						specimenCollectionGroupForm.setProtocolParticipantIdentifier(participantProtocolId);
						specimenCollectionGroupForm.setRadioButtonForParticipant(2);
					}
				}
			}
			else if (participantProtocolId != null)
			{
				//Populating the participants registered to a given protocol
				/**For Migration Start**/
				//				loadPaticipants(collectionProtocolId.longValue(), bizLogic, request);
				/**For Migration End**/
				//By Abhishek Mehta -Performance Enhancement
				//loadPaticipantNumberList(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
				specimenCollectionGroupForm.setProtocolParticipantIdentifier(participantProtocolId);
				specimenCollectionGroupForm.setRadioButtonForParticipant(2);
				String cpParticipantId = getParticipantIdForProtocolId(participantProtocolId, bizLogic);
				if (cpParticipantId != null)
				{
					request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID, cpParticipantId);
				}
			}
			/**
			 * Patch Id : FutureSCG_3
			 * Description : Setting number of specimens and restricted checkbox
			 */
			/**
			 * Removing the above patch, as it no more required. Now the new CP based entry page takes care of this. 
			 */
			Long cpeId = (Long) forwardToHashMap.get("COLLECTION_PROTOCOL_EVENT_ID");
			if (cpeId != null)
			{
				specimenCollectionGroupForm.setCollectionProtocolEventId(cpeId);
				/*List cpeList = bizLogic.retrieve(CollectionProtocolEvent.class.getName(),Constants.SYSTEM_IDENTIFIER,cpeId);
				 if(!cpeList.isEmpty())
				 {
				 setNumberOfSpecimens(request, specimenCollectionGroupForm, cpeList);
				 }*/
			}
			//Bug 1915:SpecimenCollectionGroup.Study Calendar Event Point not populated when page is loaded through proceedTo
			//Populating the Collection Protocol Events
			loadCollectionProtocolEvent(specimenCollectionGroupForm.getCollectionProtocolId(), bizLogic, request, specimenCollectionGroupForm);

			//Load Clinical status for a given study calander event point
			CPEObject = bizLogic.retrieve(CollectionProtocolEvent.class.getName(), new Long(
					specimenCollectionGroupForm.getCollectionProtocolEventId()));
			if (CPEObject != null)
			{
				setCalendarEventPoint(CPEObject, request, specimenCollectionGroupForm);
			}

			Logger.out.debug("CollectionProtocolID found in forwardToHashMap========>>>>>>" + collectionProtocolId);
			Logger.out.debug("ParticipantID found in forwardToHashMap========>>>>>>" + participantId);
			Logger.out.debug("ParticipantProtocolID found in forwardToHashMap========>>>>>>" + participantProtocolId);
		}
		//*************  ForwardTo implementation *************
		//Populate the group name field with default value in the form of 
		//<Collection Protocol Name>_<Participant ID>_<Group Id>
		int groupNumber = bizLogic.getNextGroupNumber();

		//Get the collection protocol title for the collection protocol Id selected
		String collectionProtocolTitle = "";
		String collectionProtocolName = "";
		
		Object CPObject  = bizLogic.retrieve(CollectionProtocol.class.getName(), specimenCollectionGroupForm.getCollectionProtocolId());

		if (CPObject != null)
		{
			CollectionProtocol collectionProtocol = (CollectionProtocol) CPObject;
			collectionProtocolTitle = collectionProtocol.getTitle();
			collectionProtocolName = (String) collectionProtocol.getShortTitle();
			specimenCollectionGroupForm.setCollectionProtocolName(collectionProtocolName);
		}

		long groupParticipantId = specimenCollectionGroupForm.getParticipantId();
		//check if the reset name link was clicked
		String resetName = request.getParameter(Constants.RESET_NAME);

		//Set the name to default if reset name link was clicked or page is loading for first time 
		//through add link or forward to link 
		if (forwardToHashMap != null || (specimenCollectionGroupForm.getName() != null && specimenCollectionGroupForm.getName().equals(""))
				|| (resetName != null && resetName.equals("Yes")))
		{
			if (!collectionProtocolTitle.equals("")
					&& (groupParticipantId > 0 || (protocolParticipantId != null && !protocolParticipantId.equals(""))))
			{
				//Poornima:Bug 2833 - Error thrown when adding a specimen collection group
				//Max length of CP is 150 and Max length of SCG is 55, in Oracle the name does not truncate 
				//and it is giving error. So the title is truncated in case it is longer than 30 .
				String maxCollTitle = collectionProtocolName;
				if (collectionProtocolName.length() > Constants.COLLECTION_PROTOCOL_TITLE_LENGTH)
				{
					maxCollTitle = collectionProtocolName.substring(0, Constants.COLLECTION_PROTOCOL_TITLE_LENGTH - 1);
				}
				//During add operation the id to set in the default name is generated
				if (operation.equals(Constants.ADD))
				{
					specimenCollectionGroupForm.setName(maxCollTitle + "_" + groupParticipantId + "_" + groupNumber);
				}
				//During edit operation the id to set in the default name using the id
				else if (operation.equals(Constants.EDIT) && (resetName != null && resetName.equals("Yes")))
				{
					if (groupParticipantId > 0)
					{
						specimenCollectionGroupForm.setName(maxCollTitle + "_" + groupParticipantId + "_" + specimenCollectionGroupForm.getId());
					}
					else
					{
						specimenCollectionGroupForm.setName(maxCollTitle + "_" + protocolParticipantId + "_" + specimenCollectionGroupForm.getId());
					}
				}
			}
		}

		request.setAttribute(Constants.PAGEOF, pageOf);
		Logger.out.debug("page of in Specimen coll grp action:" + request.getParameter(Constants.PAGEOF));
		// -------called from Collection Protocol Registration end -------------------------------
		//Falguni:Performance Enhancement.
		Long scgEntityId = null;
		if (CatissueCoreCacheManager.getInstance().getObjectFromCache("scgEntityId") != null)
		{
			scgEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache("scgEntityId");
		}
		else
		{
			scgEntityId = AnnotationUtil.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP);
			CatissueCoreCacheManager.getInstance().addObjectToCache("scgEntityId", scgEntityId);
		}
		request.setAttribute("scgEntityId", scgEntityId);
		/**
		 * Name : Ashish Gupta
		 * Reviewer Name : Sachin Lale 
		 * Bug ID: 2741
		 * Patch ID: 2741_11	 
		 * Description: Methods to set default events on SCG page
		 */
		setDefaultEvents(request, specimenCollectionGroupForm, operation);

		request.setAttribute("scgForm", specimenCollectionGroupForm);
		/* Bug ID: 4135
		 * Patch ID: 4135_2	 
		 * Description: Setting the ids in collection and received events associated with this scg
		 */
		//When opening in Edit mode, to set the ids of collection event parameters and received event parameters
		if (specimenCollectionGroupForm.getId() != 0)
		{
			setEventsId(specimenCollectionGroupForm, bizLogic);
		}

		// set associated identified report id
		Long reportId = getAssociatedIdentifiedReportId(specimenCollectionGroupForm.getId());
		if (reportId == null)
		{
			reportId = new Long(-1);
		}
		else if (Utility.isQuarantined(reportId))
		{
			reportId = new Long(-2);
		}
		HttpSession session = request.getSession();
		session.setAttribute(Constants.IDENTIFIED_REPORT_ID, reportId);

		session.removeAttribute("asignedPositonSet");
		return mapping.findForward(pageOf);
	}

	/**
	 * @param specimenCollectionGroupForm
	 * @param bizLogic
	 * @throws DAOException
	 */
	private void setEventsId(SpecimenCollectionGroupForm specimenCollectionGroupForm, SpecimenCollectionGroupBizLogic bizLogic) throws DAOException
	{
		Object object = bizLogic.retrieve(SpecimenCollectionGroup.class.getName(), specimenCollectionGroupForm.getId());
		if (object != null)
		{
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) object;
			Collection eventsColl = scg.getSpecimenEventParametersCollection();
			CollectionEventParameters collectionEventParameters = null;
			ReceivedEventParameters receivedEventParameters = null;
			if (eventsColl != null && !eventsColl.isEmpty())
			{
				Iterator iter = eventsColl.iterator();
				while (iter.hasNext())
				{
					Object temp = iter.next();
					if (temp instanceof CollectionEventParameters)
					{
						collectionEventParameters = (CollectionEventParameters) temp;
					}
					else if (temp instanceof ReceivedEventParameters)
					{
						receivedEventParameters = (ReceivedEventParameters) temp;
					}
				}
				//				Setting the ids
				specimenCollectionGroupForm.setCollectionEventId(collectionEventParameters.getId().longValue());
				specimenCollectionGroupForm.setReceivedEventId(receivedEventParameters.getId().longValue());
			}
		}
	}

	/**
	 * @param request
	 * @param specimenCollectionGroupForm
	 */
	private void setDefaultEvents(HttpServletRequest request, SpecimenCollectionGroupForm specimenCollectionGroupForm, String operation)
			throws DAOException
	{
		setDateParameters(specimenCollectionGroupForm, request);
		
		/*if (specimenCollectionGroupForm.getCollectionEventCollectionProcedure() == null)
		{
			specimenCollectionGroupForm.setCollectionEventCollectionProcedure((String) DefaultValueManager
					.getDefaultValue(Constants.DEFAULT_COLLECTION_PROCEDURE));
		}
		if (specimenCollectionGroupForm.getCollectionEventContainer() == null)
		{
			specimenCollectionGroupForm.setCollectionEventContainer((String) DefaultValueManager.getDefaultValue(Constants.DEFAULT_CONTAINER));
		}
		if (specimenCollectionGroupForm.getReceivedEventReceivedQuality() == null)
		{
			specimenCollectionGroupForm.setReceivedEventReceivedQuality((String) DefaultValueManager
					.getDefaultValue(Constants.DEFAULT_RECEIVED_QUALITY));
		}*/
		//setting the collector and receiver drop downs
		Utility.setUserInForm(request, operation);
		/*long collectionEventUserId = Utility.setUserInForm(request,operation);
		if(specimenCollectionGroupForm.getCollectionEventUserId() == 0)
		{
			specimenCollectionGroupForm.setCollectionEventUserId(collectionEventUserId);
		}
		if(specimenCollectionGroupForm.getReceivedEventUserId() == 0)
		{
			specimenCollectionGroupForm.setReceivedEventUserId(collectionEventUserId);
		}*/
		//Setting the List for drop downs
		setEventsListInRequest(request);
	}

	/**
	 * @param request
	 */
	private void setEventsListInRequest(HttpServletRequest request)
	{
		//setting the procedure
		List procedureList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
		request.setAttribute(Constants.PROCEDURE_LIST, procedureList);
		//		set the container lists
		List containerList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CONTAINER, null);
		request.setAttribute(Constants.CONTAINER_LIST, containerList);

		//setting the quality for received events
		List qualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RECEIVED_QUALITY, null);
		request.setAttribute(Constants.RECEIVED_QUALITY_LIST, qualityList);

		//		Sets the hourList attribute to be used in the Add/Edit FrozenEventParameters Page.
		request.setAttribute(Constants.HOUR_LIST, Constants.HOUR_ARRAY);
		//Sets the minutesList attribute to be used in the Add/Edit FrozenEventParameters Page.
		request.setAttribute(Constants.MINUTES_LIST, Constants.MINUTES_ARRAY);

	}
	/**
	 * @param specimenForm
	 */
	private void setDateParameters(SpecimenCollectionGroupForm specimenForm, HttpServletRequest request)
	{
		// set the current Date and Time for the event.
		Calendar cal = Calendar.getInstance();
		//Collection Event fields
		if (specimenForm.getCollectionEventdateOfEvent() == null)
		{
			if (request.getParameter("evtDate") != null)
			{
				specimenForm.setCollectionEventdateOfEvent(request.getParameter("evtDate"));
			}
			else
			{
				specimenForm.setCollectionEventdateOfEvent(Utility.parseDateToString(cal.getTime(), Constants.DATE_PATTERN_MM_DD_YYYY));
			}
		}
		if (specimenForm.getCollectionEventTimeInHours() == null)
		{
			specimenForm.setCollectionEventTimeInHours(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
		}
		if (specimenForm.getCollectionEventTimeInMinutes() == null)
		{
			specimenForm.setCollectionEventTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
		}

		//ReceivedEvent Fields
		if (specimenForm.getReceivedEventDateOfEvent() == null)
		{
			if (request.getParameter("evtDate") != null)
			{
				//received date should be same as collected if its anticipated date.
				specimenForm.setReceivedEventDateOfEvent(request.getParameter("evtDate"));
			}
			else
			{
			specimenForm.setReceivedEventDateOfEvent(Utility.parseDateToString(cal.getTime(), Constants.DATE_PATTERN_MM_DD_YYYY));
			}
		}
		if (specimenForm.getReceivedEventTimeInHours() == null)
		{
			specimenForm.setReceivedEventTimeInHours(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
		}
		if (specimenForm.getReceivedEventTimeInMinutes() == null)
		{
			specimenForm.setReceivedEventTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
		}

	}

	/**For Migration Start**/
	/*	private void loadPaticipants(long protocolID, IBizLogic bizLogic, HttpServletRequest request) throws Exception
	 {
	 //get list of Participant's names
	 String sourceObjectName = CollectionProtocolRegistration.class.getName();
	 String [] displayParticipantFields = {"participant.id"};
	 String valueField = "participant."+Constants.SYSTEM_IDENTIFIER;
	 String whereColumnName[] = {"collectionProtocol."+Constants.SYSTEM_IDENTIFIER,"participant.id"};
	 String whereColumnCondition[];
	 Object[] whereColumnValue; 
	 if(Variables.databaseName.equals(Constants.MYSQL_DATABASE))
	 {
	 whereColumnCondition = new String[]{"=","is not"};
	 whereColumnValue=new Object[]{new Long(protocolID),null};
	 }
	 else
	 {
	 // for ORACLE
	 whereColumnCondition = new String[]{"=",Constants.IS_NOT_NULL};
	 whereColumnValue=new Object[]{new Long(protocolID),""};
	 }


	 String joinCondition = Constants.AND_JOIN_CONDITION;
	 String separatorBetweenFields = ", ";

	 List list = bizLogic.getList(sourceObjectName, displayParticipantFields, valueField, whereColumnName,
	 whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields, true);


	 //get list of Participant's names
	 valueField = Constants.SYSTEM_IDENTIFIER;
	 sourceObjectName = Participant.class.getName();
	 String[] participantsFields = {"lastName","firstName","birthDate","socialSecurityNumber"};
	 String[] whereColumnName2 = {"lastName","firstName","birthDate","socialSecurityNumber"};
	 String[] whereColumnCondition2 = {"!=","!=","is not","is not"};
	 Object[] whereColumnValue2 = {"","",null,null};
	 if(Variables.databaseName.equals(Constants.MYSQL_DATABASE))
	 {
	 whereColumnCondition2 = new String[]{"!=","!=","is not","is not"};
	 whereColumnValue2=new String[]{"","",null,null};
	 }
	 else
	 {
	 // for ORACLE
	 whereColumnCondition2 = new String[]{Constants.IS_NOT_NULL,Constants.IS_NOT_NULL,Constants.IS_NOT_NULL,Constants.IS_NOT_NULL};
	 whereColumnValue2=new String[]{"","","",""};
	 }

	 String joinCondition2 = Constants.OR_JOIN_CONDITION;
	 String separatorBetweenFields2 = ", ";

	 List listOfParticipants = bizLogic.getList(sourceObjectName, participantsFields, valueField, whereColumnName2,
	 whereColumnCondition2, whereColumnValue2, joinCondition2, separatorBetweenFields, false);

	 // removing blank participants from the list of Participants
	 list=removeBlankParticipant(list, listOfParticipants);
	 //Mandar bug id:1628 :- sort participant dropdown list
	 Collections.sort(list );  
	 Logger.out.debug("Paticipants List"+list);
	 request.setAttribute(Constants.PARTICIPANT_LIST, list);
	 }

	 private List removeBlankParticipant(List list, List listOfParticipants)
	 {
	 List listOfActiveParticipant=new ArrayList();

	 for(int i=0; i<list.size(); i++)
	 {
	 NameValueBean nameValueBean =(NameValueBean)list.get(i);

	 if(Long.parseLong(nameValueBean.getValue()) == -1)
	 {
	 listOfActiveParticipant.add(list.get(i));
	 continue;
	 }

	 for(int j=0; j<listOfParticipants.size(); j++)
	 {
	 if(Long.parseLong(((NameValueBean)listOfParticipants.get(j)).getValue()) == -1)
	 continue;

	 NameValueBean participantsBean = (NameValueBean)listOfParticipants.get(j);
	 if( nameValueBean.getValue().equals(participantsBean.getValue()) )
	 {
	 listOfActiveParticipant.add(listOfParticipants.get(j));
	 break;
	 }
	 }
	 }

	 Logger.out.debug("No.Of Active Participants Registered with Protocol~~~~~~~~~~~~~~~~~~~~~~~>"+listOfActiveParticipant.size());

	 return listOfActiveParticipant;
	 }
	 */

	/**Commented by Abhishek Mehta	
	 * Method to load protocol participant identifier number list
	 * @param protocolID
	 * @param bizLogic
	 * @param request
	 * @throws Exception
	 */
	/*private void loadPaticipantNumberList(long protocolID, IBizLogic bizLogic, HttpServletRequest request) throws Exception
	 {
	 //get list of Participant's names
	 String sourceObjectName = CollectionProtocolRegistration.class.getName();
	 String displayParticipantNumberFields[] = {"protocolParticipantIdentifier"};
	 String valueField = "protocolParticipantIdentifier";
	 String whereColumnName[] = {"collectionProtocol."+Constants.SYSTEM_IDENTIFIER, "protocolParticipantIdentifier"};
	 String whereColumnCondition[];// = {"=","!="};
	 Object[] whereColumnValue;// = {new Long(protocolID),"null"};
	 //		if(Variables.databaseName.equals(Constants.MYSQL_DATABASE))
	 //		{
	 whereColumnCondition = new String[]{"=","!="};
	 whereColumnValue = new Object[]{new Long(protocolID),"null"};
	 //		}
	 //		else
	 //		{
	 //			whereColumnCondition = new String[]{"=","!=null"};
	 //			whereColumnValue = new Object[]{new Long(protocolID),""};
	 //		}

	 String joinCondition = Constants.AND_JOIN_CONDITION;
	 String separatorBetweenFields = "";

	 List list = bizLogic.getList(sourceObjectName, displayParticipantNumberFields, valueField, whereColumnName,
	 whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields, true);



	 Logger.out.debug("Paticipant Number List"+list);
	 request.setAttribute(Constants.PROTOCOL_PARTICIPANT_NUMBER_LIST, list);
	 }*/

	/**
	 * Method to load list of collection protocol event point
	 * @param protocolID
	 * @param bizLogic
	 * @param request
	 * @param form
	 * @throws Exception
	 */
	private void loadCollectionProtocolEvent(long protocolID, IBizLogic bizLogic, HttpServletRequest request, SpecimenCollectionGroupForm form)
			throws Exception
	{
		String sourceObjectName = CollectionProtocolEvent.class.getName();
		String displayEventFields[] = {"studyCalendarEventPoint", "collectionPointLabel"};
		String valueField = "id";
		String whereColumnName[] = {"collectionProtocol." + Constants.SYSTEM_IDENTIFIER};
		String whereColumnCondition[] = {"="};
		Object[] whereColumnValue = {new Long(protocolID)};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		String separatorBetweenFields = ",";

		List list = bizLogic.getList(sourceObjectName, displayEventFields, valueField, whereColumnName, whereColumnCondition, whereColumnValue,
				joinCondition, separatorBetweenFields, false);

		request.setAttribute(Constants.STUDY_CALENDAR_EVENT_POINT_LIST, list);
		if (list.size() > 1 && form.getCollectionProtocolEventId() <= 0)
		{
			form.setCollectionProtocolEventId(new Long(((NameValueBean) list.get(1)).getValue()));
		}
	}

	/**
	 * Method to load list of participant medical identifier
	 * @param participantID
	 * @param bizLogic
	 * @param request
	 * @throws Exception
	 */
	private void loadParticipantMedicalIdentifier(long participantID, IBizLogic bizLogic, HttpServletRequest request) throws Exception
	{
		//get list of Participant's names
		String sourceObjectName = ParticipantMedicalIdentifier.class.getName();
		String displayEventFields[] = {"medicalRecordNumber"};
		String valueField = Constants.SYSTEM_IDENTIFIER;
		String whereColumnName[] = {"participant." + Constants.SYSTEM_IDENTIFIER, "medicalRecordNumber"};
		String whereColumnCondition[] = {"=", "!="};
		Object[] whereColumnValue = {new Long(participantID), "null"};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		String separatorBetweenFields = "";

		List list = bizLogic.getList(sourceObjectName, displayEventFields, valueField, whereColumnName, whereColumnCondition, whereColumnValue,
				joinCondition, separatorBetweenFields, false);

		request.setAttribute(Constants.PARTICIPANT_MEDICAL_IDNETIFIER_LIST, list);
	}

	/**
	 * Method to retrieve participant id from the protocol participant id
	 * @param participantProtocolId
	 * @param bizLogic
	 * @return
	 * @throws Exception
	 */
	private String getParticipantIdForProtocolId(String participantProtocolId, IBizLogic bizLogic) throws Exception
	{
		String sourceObjectName = CollectionProtocolRegistration.class.getName();
		String selectColumnName[] = {"participant.id"};
		String whereColumnName[] = {"protocolParticipantIdentifier"};
		String whereColumnCondition[] = {"="};
		Object[] whereColumnValue = {participantProtocolId};
		List participantList = bizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue,
				Constants.AND_JOIN_CONDITION);
		if (participantList != null && !participantList.isEmpty())
		{

			String participantId = ((Long) participantList.get(0)).toString();
			return participantId;

		}
		return null;
	}

	/**
	 * Method to retrieve participant protocol identifier for given CP and participant id
	 * @param participantId
	 * @param cpId
	 * @param bizLogic
	 * @return
	 * @throws Exception
	 */
	private String getParticipantProtocolIdForCPAndParticipantId(String participantId, String cpId, IBizLogic bizLogic) throws Exception
	{
		String sourceObjectName = CollectionProtocolRegistration.class.getName();
		String selectColumnName[] = {"protocolParticipantIdentifier"};
		String whereColumnName[] = {"participant.id", "collectionProtocol.id"};
		String whereColumnCondition[] = {"=", "="};
		Object[] whereColumnValue = {new Long(participantId), new Long(cpId)};
		List list = bizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue,
				Constants.AND_JOIN_CONDITION);
		if (list != null && !list.isEmpty())
		{
			Iterator iter = list.iterator();
			while (iter.hasNext())
			{
				Object id = (Object) iter.next();
				if (id != null)
				{
					return id.toString();
				}
			}
		}
		return null;
	}

	/**
	 * Method to set default values related to calendar event point list
	 * @param calendarEventPointList calendar event point list
	 * @param request object of HttpServletRequest 
	 * @param specimenCollectionGroupForm object of specimenCollectionGroup action form
	 * @throws DAOException 
	 */
	private void setCalendarEventPoint(Object object, HttpServletRequest request,
			SpecimenCollectionGroupForm specimenCollectionGroupForm) throws DAOException
	{
		//		Patch ID: Bug#3184_27
		//By Abhishek Mehta 
		int numberOfSpecimen = 1;
		if (object != null)
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) object;

			//Set checkbox status depending upon the days of study calendar event point. If it is zero, then unset the restrict
			//checkbox, otherwise set the restrict checkbox
			Double studyCalendarEventPoint = collectionProtocolEvent.getStudyCalendarEventPoint();
			if (studyCalendarEventPoint.doubleValue() == 0)
			{
				specimenCollectionGroupForm.setRestrictSCGCheckbox("false");
			}
			else
			{
				specimenCollectionGroupForm.setRestrictSCGCheckbox("true");
			}
		}
		else if (object == null)
		{
			//Set checkbox status
			specimenCollectionGroupForm.setRestrictSCGCheckbox("false");
		}
		//Sets the value for number of specimen field on the specimen collection group page. 
		//Set the number of actual specimen requirements for validation purpose.
		//This value is used in validate method of SpecimenCollectionGroupForm.java.
		//		request.setAttribute(Constants.NUMBER_OF_SPECIMEN_REQUIREMENTS, numberOfSpecimen + "");
	}

	//Consent Tracking Virender Mehta	
	/**
	 * @param idOfSelectedRadioButton Id for selected radio button.
	 * @param cp_id CollectionProtocolID CollectionProtocolID selected by dropdown 
	 * @param indexType i.e Which Radio button is selected participantId or protocolParticipantIdentifier 
	 * @return collectionProtocolRegistration CollectionProtocolRegistration object
	 */
	private CollectionProtocolRegistration getcollectionProtocolRegistrationObj(String idOfSelectedRadioButton, String cp_id, String indexType)
			throws DAOException
	{

		CollectionProtocolRegistrationBizLogic collectionProtocolRegistrationBizLogic = (CollectionProtocolRegistrationBizLogic) BizLogicFactory
				.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		String[] colName = new String[2];
		Object[] val = new Object[2];
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

		val[1]=Long.valueOf(cp_id);
		
		String[] colCondition = {"=", "="};
		List collProtRegObj = collectionProtocolRegistrationBizLogic
		.retrieve( CollectionProtocolRegistration.class.getName(), colName, colCondition,
				val, null);
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) collProtRegObj.get(0);
		return collectionProtocolRegistration;
	}
	/**
	 * Prepare Map for Consent tiers
	 * @param participantResponseList This list will be iterated to map to populate participant Response status.
	 * @return tempMap
	 */
	private Map prepareConsentMap(List participantResponseList)
	{
		Map tempMap = new HashMap();
		if (participantResponseList != null)
		{
			int i = 0;
			Iterator consentResponseCollectionIter = participantResponseList.iterator();
			while (consentResponseCollectionIter.hasNext())
			{
				ConsentTierResponse consentTierResponse = (ConsentTierResponse) consentResponseCollectionIter.next();
				ConsentTier consent = consentTierResponse.getConsentTier();
				String idKey = "ConsentBean:" + i + "_consentTierID";
				String statementKey = "ConsentBean:" + i + "_statement";
				String responseKey = "ConsentBean:" + i + "_participantResponse";
				String participantResponceIdKey = "ConsentBean:" + i + "_participantResponseID";
				String scgResponsekey = "ConsentBean:" + i + "_specimenCollectionGroupLevelResponse";
				String scgResponseIDkey = "ConsentBean:" + i + "_specimenCollectionGroupLevelResponseID";

				tempMap.put(idKey, consent.getId());
				tempMap.put(statementKey, consent.getStatement());
				tempMap.put(responseKey, consentTierResponse.getResponse());
				tempMap.put(participantResponceIdKey, consentTierResponse.getId());
				tempMap.put(scgResponsekey, consentTierResponse.getResponse());
				tempMap.put(scgResponseIDkey, null);
				i++;
			}
		}
		return tempMap;
	}
	//Consent Tracking Virender Mehta

	/**
	 * This function is used for retriving specimen from Specimen collection group Object
	 * @param specimenObj
	 * @param finalDataList
	 * @throws DAOException 
	 */
	private void getSpecimenDetails(SpecimenCollectionGroup specimenCollectionGroupObj, List finalDataList) throws DAOException
	{
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		Collection specimen = null;
		if (specimenCollectionGroupObj.getId() != null)
		{
			specimen = (Collection) bizLogic.retrieveAttribute(SpecimenCollectionGroup.class.getName(), specimenCollectionGroupObj.getId(),
					"elements(specimenCollection)");
		}
		//Collection specimen = specimenCollectionGroupObj.getSpecimenCollection();
		Iterator specimenIterator = specimen.iterator();
		while (specimenIterator.hasNext())
		{
			Specimen specimenObj = (Specimen) specimenIterator.next();
			getDetailsOfSpecimen(specimenObj, finalDataList);
		}
	}

	/**
	 * This function is used for retriving specimen and sub specimen's attributes.
	 * @param specimenObj
	 * @param finalDataList
	 * @throws DAOException 
	 */
	private void getDetailsOfSpecimen(Specimen specimenObj, List finalDataList) throws DAOException
	{
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		List specimenDetailList = new ArrayList();

		if (specimenObj.getActivityStatus().equals(Constants.ACTIVITY_STATUS_ACTIVE))
		{
			specimenDetailList.add(specimenObj.getLabel());
			specimenDetailList.add(specimenObj.getSpecimenType());
			if (specimenObj.getSpecimenPosition() == null)
			{
				specimenDetailList.add(Constants.VIRTUALLY_LOCATED);
			}
			else
			{
				
//				StorageContainer storageContainer = (StorageContainer) bizLogic.retrieveAttribute(Specimen.class.getName(), specimenObj.getId(),
//						"storageContainer");
				StorageContainer storageContainer = specimenObj.getSpecimenPosition().getStorageContainer();
				
				//specimenObj.getStorageContainer().getName()+": X-Axis-"+specimenObj.getPositionDimensionOne()+", Y-Axis-"+specimenObj.getPositionDimensionTwo();
				if(specimenObj != null && specimenObj.getSpecimenPosition() != null)
				{
					String storageLocation = storageContainer.getName() + ": X-Axis-" + specimenObj.getSpecimenPosition().getPositionDimensionOne() + ", Y-Axis-"
						+ specimenObj.getSpecimenPosition().getPositionDimensionTwo();
					specimenDetailList.add(storageLocation);
				}
			}
			specimenDetailList.add(specimenObj.getClassName());
			finalDataList.add(specimenDetailList);
		}

	}
	private Long getAssociatedIdentifiedReportId(Long scgId) throws DAOException
	{
		IdentifiedSurgicalPathologyReportBizLogic bizLogic = (IdentifiedSurgicalPathologyReportBizLogic) BizLogicFactory.getInstance().getBizLogic(
				IdentifiedSurgicalPathologyReport.class.getName());
		String sourceObjectName = IdentifiedSurgicalPathologyReport.class.getName();
		String displayEventFields[] = {"id"};
		String valueField = Constants.SYSTEM_IDENTIFIER;
		String whereColumnName[] = {Constants.COLUMN_NAME_SCG_ID};
		String whereColumnCondition[] = {"="};
		Object[] whereColumnValue = {scgId};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		String separatorBetweenFields = "";

		List list = bizLogic.getList(sourceObjectName, displayEventFields, valueField, whereColumnName, whereColumnCondition, whereColumnValue,
				joinCondition, separatorBetweenFields, false);
		if (list != null && list.size() > 1)
		{
			NameValueBean nvBean = (NameValueBean) list.get(1);
			return (new Long(nvBean.getValue()));
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.wustl.common.action.SecureAction#getObjectId(edu.wustl.common.actionForm.AbstractActionForm)
	 */
	@Override
	protected String getObjectId(AbstractActionForm form)
	{
		SpecimenCollectionGroupForm specimenCollectionGroupForm = (SpecimenCollectionGroupForm)form;
		if(specimenCollectionGroupForm.getCollectionProtocolId()!=0L && specimenCollectionGroupForm.getCollectionProtocolId()!= -1L) 
		   return Constants.COLLECTION_PROTOCOL_CLASS_NAME +"_"+specimenCollectionGroupForm.getCollectionProtocolId();
		else
		   return null;
		 
	}
}