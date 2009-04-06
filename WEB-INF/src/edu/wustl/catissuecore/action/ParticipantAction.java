/**
 * <p>Title: ParticipantAction Class>
 * <p>Description:  This class initializes the fields in the Participant Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.catissuecore.bean.ConsentResponseBean;
import edu.wustl.catissuecore.bizlogic.AnnotationUtil;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.factory.AbstractDomainObjectFactory;
import edu.wustl.common.factory.MasterFactory;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields in the Participant Add/Edit webpage.
 * @author gautam_shetty
 */
public class ParticipantAction extends SecureAction
{

	/**
	 * Overrides the execute method of Action class.
	 * Sets the various fields in Participant Add/Edit webpage.
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 * @return value for ActionForward object
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		String refreshParticipant = (String)request.getParameter("refresh");
		if(refreshParticipant != null)
		{
			request.setAttribute("refresh",refreshParticipant);
			
		}
		
		
		ParticipantForm participantForm = (ParticipantForm) form;
		HttpSession session = request.getSession();
		//This if condition is for participant lookup. When participant is selected from the list then 
		//that participant gets stored in request as participantform1.
		//After that we have to show the slected participant in o/p
		
		ParticipantBizLogic participantBizlogic = (ParticipantBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.PARTICIPANT_FORM_ID);
		IBizLogic bizlogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		
		if (request.getAttribute("participantSelect") != null)
		{
			if(request.getAttribute("participantForm1") != null)
			{
				participantForm = (ParticipantForm) request.getAttribute("participantForm1");
				request.setAttribute("participantForm", participantForm);
			}
		}
		
		if (participantForm.getOperation().equals(Constants.ADD))
		{
			String clearConsentSession = request.getParameter("clearConsentSession");
			if(clearConsentSession != null){
				if(clearConsentSession.equals("true")){
					session.removeAttribute(Constants.CONSENT_RESPONSE);
				}
			}
		}
		/* Sachin
		 * bug id 5317
		 * Set Selcted Cp id in participant form so that it is get reflected whi;le adding SCG
		 */ 
		String cpid = (String)request.getParameter("cpSearchCpId");
		if(participantForm.getCpId()== -1 && cpid!=null)
		{
			participantForm.setCpId(new Long(cpid).longValue());
		}
		
		if (participantForm.getOperation().equals(Constants.EDIT))
		{
			request.setAttribute("participantId", new Long(participantForm.getId()).toString());			
			//Setting Consent Response Bean to Session
			//Abhishek Mehta
			Hashtable consentResponseHashTable = participantForm.getConsentResponseHashTable();
			if(consentResponseHashTable != null)
			{
				session.setAttribute(Constants.CONSENT_RESPONSE, consentResponseHashTable);
			}
		}
		/* Falguni Sachde
		 * bug id :8150
		 * Set participantId as request attribute as Its required in case of viewannotations view of Edit participant
		 */ 
		if (participantForm.getOperation().equals(Constants.VIEW_ANNOTATION))
		{
			request.setAttribute("participantId", new Long(participantForm.getId()).toString());			
			
		}
		//Bug- setting the default Gender
//		if (participantForm.getGender() == null)
//		{
//			participantForm.setGender(Constants.UNSPECIFIED);
//		}
		//Bug- setting the default Vital status
//		if (participantForm.getVitalStatus() == null)
//		{
//			participantForm.setVitalStatus(Constants.UNKNOWN);
//		}
		//List of keys used in map of ActionForm
		List key = new ArrayList();
		key.add("ParticipantMedicalIdentifier:outer_Site_id");
		key.add("ParticipantMedicalIdentifier:outer_medicalRecordNumber");
		key.add("ParticipantMedicalIdentifier:outer_id");
		
		//Gets the map from ActionForm
		Map map = participantForm.getValues();
		
		String deleteRegistration = request.getParameter("deleteRegistration");
		String status = request.getParameter("status");
		if(deleteRegistration==null&& status!=null && status.equalsIgnoreCase("true"))
		{
			//Calling DeleteRow of BaseAction class
			MapDataParser.deleteRow(key, map, request.getParameter("status"));
		}
		
		//Start Collection Protocol Registration For Participant
		//Abhishek Mehta
		List collectionProtocolRegistrationKey = new ArrayList();
		
		collectionProtocolRegistrationKey.add("CollectionProtocolRegistration:outer_CollectionProtocol_id");
		collectionProtocolRegistrationKey.add("CollectionProtocolRegistration:outer_CollectionProtocol_shortTitle");
		collectionProtocolRegistrationKey.add("CollectionProtocolRegistration:outer_protocolParticipantIdentifier");
		collectionProtocolRegistrationKey.add("CollectionProtocolRegistration:outer_id");
		collectionProtocolRegistrationKey.add("CollectionProtocolRegistration:outer_registrationDate");
		collectionProtocolRegistrationKey.add("CollectionProtocolRegistration:outer_isConsentAvailable");
		collectionProtocolRegistrationKey.add("CollectionProtocolRegistration:outer_activityStatus");
		
		Map mapCollectionProtocolRegistration = participantForm.getCollectionProtocolRegistrationValues();

		
		String fromSubmitAction=request.getParameter("fromSubmitAction"); // If submit button is pressed
		if(fromSubmitAction!=null)
		{
			/**
			 * Name: Vijay Pande
			 * Reviewer Name: Aarti Sharma
			 * Following method call is added to set ParticipantMedicalNumber id in the map after add/edit operation
			 */
			int count =  participantForm.getCollectionProtocolRegistrationValueCounter();
			setParticipantMedicalNumberId(bizlogic,participantForm.getId(),map);
			//Updating Collection Protocol Registration
			updateCollectionProtocolRegistrationCollection(bizlogic,participantForm,request,count);
			int cprCount = updateCollectionProtocolRegistrationMap(mapCollectionProtocolRegistration , count);
			participantForm.setCollectionProtocolRegistrationValueCounter(cprCount);
		}
		else
		{
			if(mapCollectionProtocolRegistration != null && !mapCollectionProtocolRegistration.isEmpty())
			{
				int count = participantForm.getCollectionProtocolRegistrationValueCounter();
				for(int i = 1 ; i <= count ; i++)
				{
					String collectionProtocolRegistrationActivityStausKey = "CollectionProtocolRegistration:"+i+"_activityStatus";
					if(mapCollectionProtocolRegistration.get(collectionProtocolRegistrationActivityStausKey) == null)
					{
						participantForm.setCollectionProtocolRegistrationValue(collectionProtocolRegistrationActivityStausKey, Constants.ACTIVITY_STATUS_ACTIVE);
					}
				}
			}
		}
		
		MapDataParser.deleteRow(collectionProtocolRegistrationKey, mapCollectionProtocolRegistration, "true");
		
		//Sets the collection Protocol if page is opened from collection protocol registration
		if (participantForm.getOperation().equals(Constants.ADD))
		{
			String pageOf = request.getParameter(Constants.PAGEOF);
			if(pageOf.equalsIgnoreCase(Constants.PAGE_OF_PARTICIPANT_CP_QUERY)) // If one is registering for given collection protocol
			{
				String collectionProtocolId = request.getParameter(Constants.CP_SEARCH_CP_ID);
				if(collectionProtocolId != null)
				{
					String collectionProtocolIdKey = "CollectionProtocolRegistration:1_CollectionProtocol_id";
					String isConsentAvailableKey = "CollectionProtocolRegistration:1_isConsentAvailable";
					String collectionProtocolRegistrationActivityStausKey = "CollectionProtocolRegistration:1_activityStatus";
					String collectionProtocolRegistrationDateKey = "CollectionProtocolRegistration:1_registrationDate";
					
					participantForm.setCollectionProtocolRegistrationValue(collectionProtocolIdKey, collectionProtocolId);
					
					Collection consentList = getConsentList(bizlogic,collectionProtocolId);
					if(consentList!=null && consentList.isEmpty())
					{
						participantForm.setCollectionProtocolRegistrationValue(isConsentAvailableKey,Constants.NO_CONSENTS_DEFINED);
					}
					else if(consentList!=null && !consentList.isEmpty())
					{
						participantForm.setCollectionProtocolRegistrationValue(isConsentAvailableKey,Constants.PARTICIPANT_CONSENT_ENTER_RESPONSE);
					}
					participantForm.setCollectionProtocolRegistrationValue(collectionProtocolRegistrationActivityStausKey, Constants.ACTIVITY_STATUS_ACTIVE);
					String collectionProtocolRegistrationDateValue = Utility.parseDateToString(Calendar.getInstance().getTime(), Variables.dateFormat);
					participantForm.setCollectionProtocolRegistrationValue(collectionProtocolRegistrationDateKey, collectionProtocolRegistrationDateValue);
					participantForm.setCollectionProtocolRegistrationValueCounter(1);
				}
			}
		}
		
		//Sets the collection Protocol if page is opened in add mode or if that participant doesnt have any registration
		if(mapCollectionProtocolRegistration != null && mapCollectionProtocolRegistration.isEmpty() || participantForm.getCollectionProtocolRegistrationValueCounter() == 0)
		{
			String collectionProtocolRegistrationDateKey = "CollectionProtocolRegistration:1_registrationDate";
			String collectionProtocolRegistrationActivityStausKey = "CollectionProtocolRegistration:1_activityStatus";
			String collectionProtocolRegistrationDateValue = Utility.parseDateToString(Calendar.getInstance().getTime(), Variables.dateFormat);
			participantForm.setDefaultCollectionProtocolRegistrationValue(collectionProtocolRegistrationDateKey, collectionProtocolRegistrationDateValue);
			participantForm.setDefaultCollectionProtocolRegistrationValue(collectionProtocolRegistrationActivityStausKey, Constants.ACTIVITY_STATUS_ACTIVE);
			participantForm.setCollectionProtocolRegistrationValueCounter(1);
		}
		
		//Abhishek Mehta
		//End Collection Protocol Registration For Participant
		
		
		//Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);

		//Sets the operation attribute to be used in the Add/Edit Participant Page. 
		request.setAttribute(Constants.OPERATION, operation);

		//Sets the pageOf attribute (for Add,Edit or Query Interface)
		String pageOf = request.getParameter(Constants.PAGEOF);

		request.setAttribute(Constants.PAGEOF, pageOf);
		
		//Sets the genderList attribute to be used in the Add/Edit Participant Page.
		List genderList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_GENDER, null);
		genderList.remove(0);
		request.setAttribute(Constants.GENDER_LIST, genderList);
		if (participantForm.getGender() == null || participantForm.getGender().equals(""))
		{
			Iterator itr = genderList.iterator();
			while (itr.hasNext())
			{
				NameValueBean nvb = (NameValueBean) itr.next();
				participantForm.setGender(nvb.getValue());
				break;
			}

		}

		//Sets the genotypeList attribute to be used in the Add/Edit Participant Page.
		//NameValueBean unknownVal = new NameValueBean(Constants.UNKNOWN,Constants.UNKNOWN);
		List genotypeList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_GENOTYPE, null);
		request.setAttribute(Constants.GENOTYPE_LIST, genotypeList);	
		
		//Bug- setting the default Genotype
//		if(participantForm.getGenotype() == null)
//		{
//			participantForm.setGenotype(Constants.UNKNOWN);
//		}

		//Sets the ethnicityList attribute to be used in the Add/Edit Participant Page.
		List ethnicityList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_ETHNICITY, null);
		request.setAttribute(Constants.ETHNICITY_LIST, ethnicityList);
		//Bug- setting the default ethnicity
//		if (participantForm.getEthnicity() == null)
//		{
//			participantForm.setEthnicity(Constants.NOTSPECIFIED);
//		}

		//Sets the raceList attribute to be used in the Add/Edit Participant Page.
		List raceList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RACE,
				null);
		request.setAttribute(Constants.RACELIST, raceList);		
		//Bug- setting the default raceTypes
//		if (participantForm.getRaceTypes() == null || participantForm.getRaceTypes().length == 0)
//		{
//			String[] raceTypes = {Constants.NOTSPECIFIED};
//			participantForm.setRaceTypes(raceTypes);
//		}		

		//Sets the vitalStatus attribute to be used in the Add/Edit Participant Page.
		List vitalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_VITAL_STATUS, null);
		vitalStatusList.remove(0);
		request.setAttribute(Constants.VITAL_STATUS_LIST, vitalStatusList);
		if (participantForm.getVitalStatus() == null || participantForm.getVitalStatus().equals(""))
		{
			Iterator itr = vitalStatusList.iterator();
			while (itr.hasNext())
			{
				NameValueBean nvb = (NameValueBean) itr.next();
				participantForm.setVitalStatus(nvb.getValue());
				break;
			}

		}
		//Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);
		//By Abhishek
		//ParticipantBizLogic bizlogic = (ParticipantBizLogic) BizLogicFactory.getInstance()
		//		.getBizLogic(Constants.PARTICIPANT_FORM_ID);

		//Sets the Site list of corresponding type.
		SessionDataBean sessionDataBean = getSessionData(request);
		List siteList = new ArrayList();
		String sourceObjectName = Site.class.getName();
		String[] displayNameFields = {"name"};
		String valueField = Constants.SYSTEM_IDENTIFIER;
		siteList = participantBizlogic.getList(sourceObjectName, displayNameFields, valueField, true);
		request.setAttribute(Constants.SITELIST, siteList);
		
		List list = new ArrayList();
		if(sessionDataBean != null && sessionDataBean.isAdmin())
		{
		//Set the collection protocol title list
		String cpSourceObjectName = CollectionProtocol.class.getName();
		String[] cpDisplayNameFields = {"shortTitle"};
		String cpValueField = Constants.SYSTEM_IDENTIFIER;
		list = participantBizlogic.getList(cpSourceObjectName, cpDisplayNameFields, cpValueField, true);
		}
		else 
		{
			CollectionProtocolBizLogic cpBizLogic = (CollectionProtocolBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
			String cpId = request.getParameter(Constants.CP_SEARCH_CP_ID);
			list = participantBizlogic.getCPForUserWithRegistrationAcess(sessionDataBean.getUserId());
			
			// This is done when participant is added in cp based view. 
			// Adding the CP selected in the cp based view to the list of CPs used in CPR section
			if(cpId != null)
			{
				//list = participantBizlogic.getCPForUserWithRegistrationAcess(sessionDataBean.getUserId());
				if(list.size()==1)
				{
					String shortTitle = cpBizLogic.getShortTitle(Long.valueOf(cpId));
					NameValueBean nvb = new NameValueBean(shortTitle,cpId);
					list.add(nvb);
				}
			}
		}
		request.setAttribute(Constants.PROTOCOL_LIST, list);
		//report id from session
		Long reportIdFormSession=(Long)session.getAttribute(Constants.IDENTIFIED_REPORT_ID);
		// set associated identified report id
		Long reportId=getAssociatedIdentifiedReportId(participantBizlogic,participantForm.getId());
		if(reportId==null)
		{
			reportId=new Long(-1);
		}
		else if(Utility.isQuarantined(reportId))
		{
			reportId=new Long(-2);
		}
		//while switching form view annotation tab to view path report tab report id is reset to -1 in session 
		//to omit this report is  retrieved from session and then compared with the new value.
		if(reportId== -1 && reportIdFormSession!=null)
		{
			reportId = reportIdFormSession;
		}
		session.setAttribute(Constants.IDENTIFIED_REPORT_ID, reportId);
		//Falguni:Performance Enhancement.
		Long participantEntityId = null;
		if (CatissueCoreCacheManager.getInstance().getObjectFromCache("participantEntityId") != null)
		{
			participantEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache("participantEntityId");
		}
		else
		{
			participantEntityId = AnnotationUtil.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT);
			CatissueCoreCacheManager.getInstance().addObjectToCache("participantEntityId",participantEntityId);		
		}
		request.setAttribute("participantEntityId",participantEntityId);

		Logger.out.debug("pageOf :---------- " + pageOf);

		return mapping.findForward(pageOf);
	}
	
	/*
	 * Update collection protocol registration info for given participant.
	 * //Abhishek Mehta
	 */
	private void updateCollectionProtocolRegistrationCollection(IBizLogic bizLogic,ParticipantForm participantForm , HttpServletRequest request , int count) throws Exception
	{
		//Gets the collection Protocol Registration map from ActionForm
		Map mapCollectionProtocolRegistration = participantForm.getCollectionProtocolRegistrationValues();
		
		if(mapCollectionProtocolRegistration != null && !mapCollectionProtocolRegistration.isEmpty())
		{
			Collection consentResponseBeanCollection = participantForm.getConsentResponseBeanCollection();
			setParticipantCollectionProtocolRegistrationId(bizLogic,participantForm.getId(),mapCollectionProtocolRegistration,consentResponseBeanCollection, count);
		}
	}
	
	
	/*
	 * Removing collection protocol registration info if it is disabled.
	 * //Abhishek Mehta
	 */
	private int updateCollectionProtocolRegistrationMap(Map mapCollectionProtocolRegistration, int count) throws Exception
	{
		int cprCount = 0;
		for(int i = 1 ; i <=count ; i++)
		{
			String isActive = "CollectionProtocolRegistration:" + i +"_activityStatus";
			String collectionProtocolTitle = "CollectionProtocolRegistration:"+i+"_CollectionProtocol_id";
			String activityStatus = (String)mapCollectionProtocolRegistration.get(isActive);
			String cpId = (String)mapCollectionProtocolRegistration.get(collectionProtocolTitle);
			if(activityStatus == null && cpId == null)
			{
				cprCount++;
				continue;
			}
			if(activityStatus == null)
    		{
				mapCollectionProtocolRegistration.put(isActive, Constants.ACTIVITY_STATUS_ACTIVE);
    		}
			
			if(activityStatus != null && activityStatus.equalsIgnoreCase(Constants.DISABLED) || (cpId != null && cpId.equalsIgnoreCase("-1")))
    		{
				
				String collectionProtocolParticipantId = "CollectionProtocolRegistration:"+i+"_protocolParticipantIdentifier";
				String collectionProtocolRegistrationDate = "CollectionProtocolRegistration:" + i +"_registrationDate";
				String collectionProtocolIdentifier = "CollectionProtocolRegistration:" + i +"_id";
				String isConsentAvailable = "CollectionProtocolRegistration:" + i +"_isConsentAvailable";
				String collectionProtocolParticipantTitle = "CollectionProtocolRegistration:"+i+"_CollectionProtocol_shortTitle";
				
				mapCollectionProtocolRegistration.remove(collectionProtocolTitle);
				mapCollectionProtocolRegistration.remove(collectionProtocolParticipantId);
				mapCollectionProtocolRegistration.remove(collectionProtocolRegistrationDate);
				mapCollectionProtocolRegistration.remove(collectionProtocolIdentifier);
				mapCollectionProtocolRegistration.remove(isActive);
				mapCollectionProtocolRegistration.remove(isConsentAvailable);
				mapCollectionProtocolRegistration.remove(collectionProtocolParticipantTitle);
				cprCount++;
			}
		}
		return (count-cprCount);
	}
	
	/*
	 * Consent List for given collection protocol
	 * //Abhishek Mehta
	 */
	private Collection getConsentList(IBizLogic bizLogic,String cpId) throws DAOException
    {   	
		Collection consentTierCollection = (Collection)bizLogic.retrieveAttribute(CollectionProtocol.class.getName(), Long.parseLong(cpId), "elements(consentTierCollection)");
		return consentTierCollection;
    }
	
	/**
	 * THis method sets the ParticipantMedicalNumber id in the map
	 * Bug_id: 4386
	 * After adding new participant medical number CommonAddEdit was unable to set id in the value map for participant medical number
	 * Therefore here expicitly id of the participant medical number are set
	 * 
	 * @param participantId id of the current participant
	 * @param map map that holds ParticipantMedicalNumber(s)
	 * @throws Exception generic exception
	 */
	private void setParticipantMedicalNumberId(IBizLogic bizLogic,Long participantId, Map  map)throws Exception
	{
		//By Abhishek
		//ParticipantBizLogic bizLogic = (ParticipantBizLogic) BizLogicFactory.getInstance().getBizLogic(Participant.class.getName());
		Collection paticipantMedicalIdentifierCollection=(Collection)bizLogic.retrieveAttribute(Participant.class.getName(), participantId, "elements(participantMedicalIdentifierCollection)");
		Iterator iter=paticipantMedicalIdentifierCollection.iterator();
		while(iter.hasNext())
		{
			ParticipantMedicalIdentifier pmi=(ParticipantMedicalIdentifier)iter.next();
			for(int i=1;i<=paticipantMedicalIdentifierCollection.size();i++)
			{
				//check for null medical record number since for participant having no PMI an empty PMI object is added
				if(pmi.getMedicalRecordNumber()!=null && pmi.getSite().getId().toString()!=null)
				{
					// check for site id and medical number, if they both matches then set id to the respective participant medical number
					if(((String)(map.get(Utility.getParticipantMedicalIdentifierKeyFor(i, Constants.PARTICIPANT_MEDICAL_IDENTIFIER_MEDICAL_NUMBER)))).equalsIgnoreCase(pmi.getMedicalRecordNumber()) 
							&& ((String)(map.get(Utility.getParticipantMedicalIdentifierKeyFor(i,Constants.PARTICIPANT_MEDICAL_IDENTIFIER_SITE_ID)))).equalsIgnoreCase(pmi.getSite().getId().toString()))
					{
						map.put(Utility.getParticipantMedicalIdentifierKeyFor(i, Constants.PARTICIPANT_MEDICAL_IDENTIFIER_ID), pmi.getId().toString());
						break;
					}
				}
			}
		}
	}
	
	
	/*
	 * This method sets the Collection Protocol Registration id in the map
	 * //Abhishek Mehta
	 */
	private void setParticipantCollectionProtocolRegistrationId(IBizLogic bizLogic,Long participantId, Map  map, Collection consentResponseBeanCollection , int cprCount)throws Exception
	{
		//By Abhishek Mehta
		//ParticipantBizLogic bizLogic = (ParticipantBizLogic) BizLogicFactory.getInstance().getBizLogic(Participant.class.getName());
		Collection collectionProtocolRegistrationCollection=(Collection)bizLogic.retrieveAttribute(Participant.class.getName(), participantId, "elements(collectionProtocolRegistrationCollection)");
		Iterator iter=collectionProtocolRegistrationCollection.iterator();
		
		while(iter.hasNext())
		{
			CollectionProtocolRegistration cpri=(CollectionProtocolRegistration)iter.next();			
			for(int i=1;i<=cprCount;i++)
			{
				if(cpri.getCollectionProtocol()!=null)
				{
					// Added by geeta 
					//DFCI requirement : barcode should be same as identifier
					List list =null;
					String barcode=null;
					IBizLogic bizLogic1 = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);		
					list = bizLogic1.retrieve(CollectionProtocolRegistration.class.getName(), new String[]{"barcode"}, new String[]{"id"}, new String[]{"="}, new Long[]{cpri.getId()}, null);
					if (list!=null && !list.isEmpty())
					{
						barcode = ((String)list.get(0));
					}
					String collectionProtocolIdKey = "CollectionProtocolRegistration:"+i+"_CollectionProtocol_id";
					String collectionProtocolRegistrationIdKey = "CollectionProtocolRegistration:"+i+"_id";
					String isActive = "CollectionProtocolRegistration:" + i +"_activityStatus";
					// barcodekey added by geeta
					String barcodeKey = "CollectionProtocolRegistration:" + i +"_barcode";
					if(map.containsKey(collectionProtocolIdKey))
					{
						if(((String)map.get(collectionProtocolIdKey)).equalsIgnoreCase(cpri.getCollectionProtocol().getId().toString()))
						{
							map.put(collectionProtocolRegistrationIdKey, cpri.getId().toString());
							map.put(isActive, cpri.getActivityStatus());
							map.put(barcodeKey, barcode);
							//poplulate the Protocol Participant Id in map of Participant Form
							map.put("CollectionProtocolRegistration:" + i + "_protocolParticipantIdentifier",
										cpri.getProtocolParticipantIdentifier());
							if(consentResponseBeanCollection != null)
							{
								setConsentResponseId(bizLogic,cpri.getId() , consentResponseBeanCollection);
							}
							break;
						}
					}
				}
			}
		}
	}
	
	/*
	 * To set the consent response id for add/edit page
	 * //Abhishek Mehta
	 */
	private void setConsentResponseId(IBizLogic bizLogic,Long collectionProtocolId, Collection consentResponseBeanCollection)throws Exception
	{
		//By Abhishek Mehta
		Collection consentTierResponseCollection=(Collection)bizLogic.retrieveAttribute(CollectionProtocolRegistration.class.getName(), collectionProtocolId, "elements(consentTierResponseCollection)");
		
		Iterator it = consentResponseBeanCollection.iterator();
		while(it.hasNext())
		{
			ConsentResponseBean consentResponseBean = (ConsentResponseBean)it.next();
			long cpId = consentResponseBean.getCollectionProtocolID();
			if(cpId == collectionProtocolId) // Searching for same collection protocol
			{
				Iterator iter=consentTierResponseCollection.iterator();
				while(iter.hasNext())
				{
					ConsentTierResponse consentTierResponse=(ConsentTierResponse)iter.next();
					if(consentTierResponse.getId()!=null)
					{
						ConsentTier consentTier = consentTierResponse.getConsentTier();
						String consentTierId = consentTier.getId().toString();
						Collection consentResponse = consentResponseBean.getConsentResponse();
						Iterator itResponse =  consentResponse.iterator();
						while(itResponse.hasNext()) // Setting participant response id for add/edit
						{
							ConsentBean consentBean = (ConsentBean)itResponse.next();
							String ctId = consentBean.getConsentTierID();
							if(ctId.equals(consentTierId))
							{
								consentBean.setParticipantResponseID(consentTierResponse.getId().toString());
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private Long getAssociatedIdentifiedReportId(ParticipantBizLogic participantBizlogic,Long participantId) throws DAOException
	{
		//By Abhishek Mehta
		List idList=participantBizlogic.getSCGList(participantId);
		if(idList!=null && idList.size()>0)
		{
			Object[] obj=(Object[])idList.get(0);
			return((Long)obj[2]);
		}
		return null;
	}
	
	public String getObjectId()
	{
		String objectId = "";
		return objectId;
	}
	
	/* (non-Javadoc)
	 * @see edu.wustl.common.action.SecureAction#getObjectId(edu.wustl.common.actionForm.AbstractActionForm)
	 */
	@Override
	protected String getObjectId(AbstractActionForm form)
	{
		ParticipantForm participantForm = (ParticipantForm)form;
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		
		if(participantForm.getCpId()!=0L && participantForm.getCpId()!= -1L) 
		   return Constants.COLLECTION_PROTOCOL_CLASS_NAME +"_"+participantForm.getCpId();
		
		else if (participantForm.getCpId() == -1L && participantForm.getId() != 0L)
		{
			try
			{
				StringBuffer sb = new StringBuffer();
				Participant participant;
				dao.openSession(null);
			
				participant = (Participant) dao.retrieve(Participant.class.getName(), participantForm.getId());
				 
				Collection<CollectionProtocolRegistration> collection = participant.getCollectionProtocolRegistrationCollection();
				
				if (collection != null && !collection.isEmpty())
				{
					sb.append(Constants.COLLECTION_PROTOCOL_CLASS_NAME);
					for (CollectionProtocolRegistration cpr : collection)
					{
						sb.append("_").append(cpr.getCollectionProtocol().getId());
					}
				}

				return sb.toString();
			}
			catch (Exception e) {
				return null;
			}
			finally
			{
				try {
					dao.closeSession();
				} catch (DAOException e) {
					e.printStackTrace();
				}
			}
			
		}
		else 
		   return null;
		 
	}
}