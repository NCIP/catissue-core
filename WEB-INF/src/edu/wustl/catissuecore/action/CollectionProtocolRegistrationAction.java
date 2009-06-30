/**
 * <p>Title: DepartmentAction Class</p>
 * <p>Description:	This class initializes the fields in the Department Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00
 * Created on May 23rd, 2005
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
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.daofactory.DAOConfigFactory;

/**
 * This class initializes the fields in the User Add/Edit webpage.
 * @author ajay_sharma
 */

public class CollectionProtocolRegistrationAction extends SecureAction
{

	private transient Logger logger = Logger
			.getCommonLogger(CollectionProtocolRegistrationAction.class);
	//This will keep track of no of consents for a particular participant
	int consentCounter;

	/**
	 * Overrides the execute method of Action class.
	 * Sets the various fields in Participant Registration Add/Edit webpage.
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 * */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		CollectionProtocolRegistrationForm collectionProtocolRegistrationForm = (CollectionProtocolRegistrationForm) form;
		//Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		//CollectionProtocolId
		//Consent Tracking (Virender Mehta)
		String selectedCollectionProtocolId = null;
		if (request.getParameter(Constants.CP_SEARCH_CP_ID) != null)
		{
			long collectionProtocolId = new Long(request.getParameter(Constants.CP_SEARCH_CP_ID))
					.longValue();
			try
			{
				((CollectionProtocolRegistrationForm) form)
						.setCollectionProtocolID(collectionProtocolId);
			}
			catch (ClassCastException exp)
			{
				logger
						.debug("Class cast Exception in CollectionProtocolRegistrationAction ~~~~~~~~~~~~~~~~~~~~~~~>"
								+ exp);
			}
		}
		selectedCollectionProtocolId = String.valueOf(collectionProtocolRegistrationForm
				.getCollectionProtocolID());
		//Adding name,value pair in NameValueBean
		//Getting witness name list for CollectionProtocolID
		List witnessList = ConsentUtil.witnessNameList(selectedCollectionProtocolId);
		//Getting ResponseList if Operation=Edit then "Withdraw" is added to the List 
		List responseList = AppUtility.responceList(operation);
		Set consentList = (Set) ConsentUtil.getConsentList(selectedCollectionProtocolId);
		List requestConsentList = new ArrayList(consentList);
		if (operation.equalsIgnoreCase(Constants.ADD))
		{
			ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
			if (errors == null)
			{
				Map tempMap = prepareConsentMap(requestConsentList);
				collectionProtocolRegistrationForm.setConsentResponseValues(tempMap);
			}
			collectionProtocolRegistrationForm.setConsentTierCounter(requestConsentList.size());
		}
		else
		{
			String cprID = String.valueOf(collectionProtocolRegistrationForm.getId());
			CollectionProtocolRegistration collectionProtocolRegistration = AppUtility
					.getcprObj(cprID);
			//List added for grid
			List specimenDetails = new ArrayList();
			ConsentUtil.getSpecimenDetails(collectionProtocolRegistration, specimenDetails);
			List < String > columnList = ConsentUtil.columnNames();
			//Lazy Resolved ---  collectionProtocolRegistration.getConsentTierResponseCollection();
			Collection consentResponse = (Collection) bizLogic.retrieveAttribute(
					CollectionProtocolRegistration.class.getName(), collectionProtocolRegistration
							.getId(), "elements(consentTierResponseCollection)");
			Map tempMap = prepareMap(consentResponse);
			collectionProtocolRegistrationForm.setConsentResponseValues(tempMap);
			collectionProtocolRegistrationForm.setConsentTierCounter(consentCounter);
			HttpSession session = request.getSession();
			session.setAttribute(Constants.SPECIMEN_LIST, specimenDetails);
			session.setAttribute(Constants.COLUMNLIST, columnList);
		}
		request.setAttribute("witnessList", witnessList);
		request.setAttribute("responseList", responseList);
		//Consent Tracking Virender Mehta		
		//Sets the operation attribute to be used in the Add/Edit User Page. 

		request.setAttribute(Constants.OPERATION, operation);
		if (operation.equalsIgnoreCase(Constants.ADD))
		{
			CollectionProtocolRegistrationForm cpform = (CollectionProtocolRegistrationForm) form;
			cpform.setId(0);
			if (cpform.getRegistrationDate() == null)
			{
				cpform.setRegistrationDate(edu.wustl.common.util.Utility.parseDateToString(Calendar
						.getInstance().getTime(), CommonServiceLocator.getInstance()
						.getDatePattern()));
			}
		}

		//Sets the pageOf attribute
		String pageOf = request.getParameter(Constants.PAGE_OF);

		request.setAttribute(Constants.PAGE_OF, pageOf);

		// Mandar : code for Addnew Collection Protocol data 24-Jan-06
		//		String collectionProtocolID = (String)request.getAttribute(Constants.ADD_NEW_COLLECTION_PROTOCOL_ID);
		//		if(collectionProtocolID != null && collectionProtocolID.trim().length() > 0 )
		//		{
		//			Logger.out.debug(">>>>>>>>>>><<<<<<<<<<<<<<<<>>>>>>>>>>>>> CP ID in CPR : "+ collectionProtocolID  );
		//			((CollectionProtocolRegistrationForm)form).setCollectionProtocolID(Long.parseLong(collectionProtocolID));
		//		}
		// Mandar -- 24-Jan-06 end

		//        String reqPath = request.getParameter(Constants.REQ_PATH);
		//		if (reqPath != null)
		//			request.setAttribute(Constants.REQ_PATH, reqPath);
		//		
		//		Logger.out.debug("PartProtReg redirect :---------- "+ reqPath  );
		//        
		// ----------------add new end-----

		//get list of Protocol title.
		String sourceObjectName = CollectionProtocol.class.getName();

		//Smita changes start
		String[] displayNameFields = {"shortTitle"};
		//Smita changes end

		String valueField = Constants.SYSTEM_IDENTIFIER;
		List list = bizLogic.getList(sourceObjectName, displayNameFields, valueField, true);
		request.setAttribute(Constants.PROTOCOL_LIST, list);

		logger.debug("SubmittedFor on CPRAction====>"
				+ request.getAttribute(Constants.SUBMITTED_FOR));
		if ((request.getAttribute(Constants.SUBMITTED_FOR) != null)
				&& (request.getAttribute(Constants.SUBMITTED_FOR).equals("AddNew")))
		{
			HttpSession session = request.getSession();
			Stack formBeanStack = (Stack) session.getAttribute(Constants.FORM_BEAN_STACK);

			if (formBeanStack != null)
			{
				try
				{
					AddNewSessionDataBean addNewSessionDataBean = (AddNewSessionDataBean) formBeanStack
							.peek();

					SpecimenCollectionGroupForm sessionFormBean = (SpecimenCollectionGroupForm) addNewSessionDataBean
							.getAbstractActionForm();

					((CollectionProtocolRegistrationForm) form)
							.setCollectionProtocolID(sessionFormBean.getCollectionProtocolId());
				}
				catch (ClassCastException exp)
				{
					logger
							.debug("Class cast Exception in CollectionProtocolRegistrationAction ~~~~~~~~~~~~~~~~~~~~~~~>"
									+ exp);
				}
			}
		}

		//		if(request.getParameter(Constants.CP_SEARCH_CP_ID)!=null)
		//		{
		//			long cpSearchCpId = new Long(request.getParameter(Constants.CP_SEARCH_CP_ID)).longValue();
		//			try
		//			{
		//		    ((CollectionProtocolRegistrationForm)form).setCollectionProtocolID(cpSearchCpId);
		//			}
		//        	catch(ClassCastException exp)
		//			{
		//        		Logger.out.debug("Class cast Exception in CollectionProtocolRegistrationAction ~~~~~~~~~~~~~~~~~~~~~~~>"+exp);
		//			}
		//		}

		//get list of Participant's names
		sourceObjectName = Participant.class.getName();
		String[] participantsFields = {"lastName", "firstName", "birthDate", "socialSecurityNumber"};
		String[] whereColumnName = {"lastName", "firstName", "birthDate", "socialSecurityNumber"};
		String[] whereColumnCondition;
		Object[] whereColumnValue;

		// get Database name and set conditions

		String appName = CommonServiceLocator.getInstance().getAppName();
		String databaseType = DAOConfigFactory.getInstance().getDAOFactory(appName)
				.getDataBaseType();

		if (databaseType.equals(Constants.MYSQL_DATABASE))
		{
			whereColumnCondition = new String[]{"!=", "!=", "is not null", "is not null"};
			whereColumnValue = new String[]{"", ""};
		}
		else if (databaseType.equals(Constants.MSSQLSERVER_DATABASE))
		{
			// for MsSqlServer DB.
			whereColumnCondition = new String[]{"!=", "!=", "is not null", "is not null"};
			whereColumnValue = new String[]{"", ""};
		}
		else
		{
			// for ORACLE
			whereColumnCondition = new String[]{"is not null", "is not null", "is not null",
					"is not null"};
			whereColumnValue = new String[]{};
		}

		String joinCondition = Constants.OR_JOIN_CONDITION;
		String separatorBetweenFields = ", ";

		list = bizLogic.getList(sourceObjectName, participantsFields, valueField, whereColumnName,
				whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields,
				false);

		//get list of Disabled Participants
		String[] participantsFields2 = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnName2 = {"activityStatus"};
		String[] whereColumnCondition2 = {"="};
		String[] whereColumnValue2 = {Status.ACTIVITY_STATUS_DISABLED.toString()};
		String joinCondition2 = Constants.AND_JOIN_CONDITION;
		String separatorBetweenFields2 = ",";

		List listOfDisabledParticipant = bizLogic.getList(sourceObjectName, participantsFields2,
				valueField, whereColumnName2, whereColumnCondition2, whereColumnValue2,
				joinCondition2, separatorBetweenFields2, false);

		//removing disabled participants from the list of Participants
		list = removeDisabledParticipant(list, listOfDisabledParticipant);

		// Sets the participantList attribute to be used in the Site Add/Edit Page.
		request.setAttribute(Constants.PARTICIPANT_LIST, list);

		//Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);

		//*************  ForwardTo implementation *************
		HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");

		if (forwardToHashMap != null)
		{
			Long participantId = (Long) forwardToHashMap.get("participantId");
			Logger.out.debug("ParticipantID found in forwardToHashMap========>>>>>>"
					+ participantId);

			if ((request.getParameter("firstName").trim().length() > 0)
					|| (request.getParameter("lastName").trim().length() > 0)
					|| (request.getParameter("birthDate").trim().length() > 0)
					|| ((request.getParameter("socialSecurityNumberPartA").trim().length() > 0)
							&& (request.getParameter("socialSecurityNumberPartB").trim().length() > 0) && (request
							.getParameter("socialSecurityNumberPartC").trim().length() > 0)))
			{
				CollectionProtocolRegistrationForm cprForm = (CollectionProtocolRegistrationForm) form;
				cprForm.setParticipantID(participantId.longValue());
				//cprForm.setCheckedButton(true);
				//Bug-2819: Performance issue due to participant drop down: Jitendra
				Object object = bizLogic.retrieve(sourceObjectName, participantId);
				if (object != null)
				{
					Participant participant = (Participant) object;
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
					Long participantId = new Long(request.getParameter("participantId"));
					CollectionProtocolRegistrationForm cprForm = (CollectionProtocolRegistrationForm) form;
					cprForm.setParticipantID(participantId.longValue());
					//cprForm.setCheckedButton(true);
					Object object = bizLogic.retrieve(sourceObjectName, participantId);
					if (object != null)
					{
						Participant participant = (Participant) object;
						cprForm.setParticipantName(participant.getMessageLabel());
					}
				}
				catch (NumberFormatException e)
				{
					logger.debug("NumberFormatException Occured :" + e);
				}
			}

			//Bug- 2819 :  Jitendra
			if (((CollectionProtocolRegistrationForm) form).getParticipantID() != 0)
			{
				try
				{
					Long participantId = new Long(((CollectionProtocolRegistrationForm) form)
							.getParticipantID());
					CollectionProtocolRegistrationForm cprForm = (CollectionProtocolRegistrationForm) form;
					cprForm.setParticipantID(participantId.longValue());
					//cprForm.setCheckedButton(true);                
					Object object = bizLogic.retrieve(sourceObjectName, participantId);
					if (object != null)
					{
						Participant participant = (Participant) object;
						cprForm.setParticipantName(participant.getMessageLabel());
					}
				}
				catch (NumberFormatException e)
				{
					logger.debug("NumberFormatException Occured :" + e);
				}
			}
		}
		//*************  ForwardTo implementation *************

		/*   Logger.out.info("--------------------------------- caching ---------------");
		   CollectionProtocolRegistrationBizLogic cBizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		   List list12 =cBizLogic.getAllParticipantRegistrationInfo();
		   Logger.out.info("--------------------------------- caching end ---------------");*/

		if (request.getParameter(Constants.OPERATION).equals(Constants.EDIT))
		{
			CollectionProtocolRegistrationForm cprForm = (CollectionProtocolRegistrationForm) form;
			String participantId = new Long(cprForm.getParticipantID()).toString();
			if (cprForm.getParticipantID() == 0 && cprForm.getParticipantProtocolID() != null)
			{
				participantId = getParticipantIdForProtocolId(cprForm.getParticipantProtocolID(),
						bizLogic);
			}
			request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID, participantId);
		}

		return mapping.findForward(pageOf);
	}

	/**
	 * @param listOfParticipant list of participant
	 * @param listOfDisabledParticipant list of disabled participant
	 * @return list of participant those having activityStatus not as disabled
	 */
	private List removeDisabledParticipant(List listOfParticipant, List listOfDisabledParticipant)
	{
		List listOfActiveParticipant = new ArrayList();

		Logger.out.debug("No. Of Participants ~~~~~~~~~~~~~~~~~~~~~~~>" + listOfParticipant.size());
		Logger.out.debug("No. Of Disabled Participants ~~~~~~~~~~~~~~~~~~~~~~~>"
				+ listOfDisabledParticipant.size());

		listOfActiveParticipant.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
		for (int i = 0; i < listOfParticipant.size(); i++)
		{
			NameValueBean participantBean = (NameValueBean) listOfParticipant.get(i);
			boolean isParticipantDisable = false;

			if (Long.parseLong(participantBean.getValue()) == -1)
			{
				//listOfActiveParticipant.add(listOfParticipant.get(i));
				continue;
			}

			for (int j = 0; j < listOfDisabledParticipant.size(); j++)
			{
				if (Long.parseLong(((NameValueBean) listOfDisabledParticipant.get(j)).getValue()) == -1)
				{
					continue;
				}

				NameValueBean disabledParticipant = (NameValueBean) listOfDisabledParticipant
						.get(j);
				if (participantBean.getValue().equals(disabledParticipant.getValue()))
				{
					isParticipantDisable = true;
					break;
				}
			}
			if (isParticipantDisable == false)
			{
				listOfActiveParticipant.add(listOfParticipant.get(i));
			}
		}

		logger.debug("No.Of Active Participants ~~~~~~~~~~~~~~~~~~~~~~~>"
				+ listOfActiveParticipant.size());

		return listOfActiveParticipant;
	}

	/**
	 * @param participantProtocolId String value for participant Protocol id
	 * @param bizLogic object of class implementing IBizLogic
	 * @return String for respective participant Id
	 * @throws Exception generic exception
	 */
	private String getParticipantIdForProtocolId(String participantProtocolId, IBizLogic bizLogic)
			throws Exception
	{
		String sourceObjectName = CollectionProtocolRegistration.class.getName();
		String[] selectColumnName = {"participant.id"};
		String[] whereColumnName = {"protocolParticipantIdentifier"};
		String[] whereColumnCondition = {"="};
		Object[] whereColumnValue = {participantProtocolId};
		List participantList = bizLogic.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue,
				Constants.AND_JOIN_CONDITION);
		if (participantList != null && !participantList.isEmpty())
		{

			String participantId = ((Long) participantList.get(0)).toString();
			return participantId;

		}
		return null;
	}

	/**
	 * Prepare map for Showing Consents for a CollectionprotocolID when Operation=Add
	 * @param requestConsentList This is the List of Consents for a selected  CollectionProtocolID
	 * @return tempMap
	 */
	private Map prepareMap(Collection partiResponseCollection)
	{
		Map tempMap = new LinkedHashMap();
		if (partiResponseCollection != null)
		{
			int i = 0;
			Iterator consentResponseCollectionIter = partiResponseCollection.iterator();
			String idKey = null;
			String statementKey = null;
			String responsekey = null;
			String participantResponceIdKey = null;
			Long consentTierID;
			Long consentID;
			while (consentResponseCollectionIter.hasNext())
			{
				ConsentTierResponse consentTierResponse = (ConsentTierResponse) consentResponseCollectionIter
						.next();
				ConsentTier consent = (ConsentTier) consentTierResponse.getConsentTier();
				consentTierID = consentTierResponse.getConsentTier().getId();
				consentID = consent.getId();
				if (consentTierID.longValue() == consentID.longValue())
				{
					idKey = "ConsentBean:" + i + "_consentTierID";
					statementKey = "ConsentBean:" + i + "_statement";
					responsekey = "ConsentBean:" + i + "_participantResponse";
					participantResponceIdKey = "ConsentBean:" + i + "_participantResponseID";
					tempMap.put(idKey, consent.getId());
					tempMap.put(statementKey, consent.getStatement());
					tempMap.put(responsekey, consentTierResponse.getResponse());
					tempMap.put(participantResponceIdKey, consentTierResponse.getId());
					i++;
				}
			}
			consentCounter = i;
			return tempMap;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Prepare map for Showing Consents for a CollectionprotocolID when Operation=Add
	 * @param requestConsentList This is the List of Consents for a selected  CollectionProtocolID
	 * @return tempMap
	 */
	private Map prepareConsentMap(List requestConsentList)
	{
		Map tempMap = new HashMap();
		if (requestConsentList != null)
		{
			int i = 0;
			Iterator consentTierCollectionIter = requestConsentList.iterator();
			String idKey = null;
			String statementKey = null;
			while (consentTierCollectionIter.hasNext())
			{
				ConsentTier consent = (ConsentTier) consentTierCollectionIter.next();
				idKey = "ConsentBean:" + i + "_consentTierID";
				statementKey = "ConsentBean:" + i + "_statement";

				tempMap.put(idKey, consent.getId());
				tempMap.put(statementKey, consent.getStatement());
				i++;
			}
		}
		return tempMap;
	}
}