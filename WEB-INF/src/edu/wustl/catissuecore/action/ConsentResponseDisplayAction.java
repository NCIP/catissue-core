/**
 * <p>Title: ConsentResponseDisplayAction Class>
 * <p>Description: ConsentResponseDisplayAction class is for displaying consent response on ParticipantConsentTracking.jsp </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Abhishek Mehta
 * @version 1.00
 * Created on Sept 5, 2007
 */
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConsentResponseForm;
import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.catissuecore.bean.ConsentResponseBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

public class ConsentResponseDisplayAction extends BaseAction
{
	//This will keep track of no of consents for a particular participant
	int consentCounter;
	
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		ConsentResponseForm consentForm = (ConsentResponseForm)form;
		HttpSession session =request.getSession();
		
		
		//Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);
		
		//Gets the value of collection protocol id.
		String collectionProtocolId = null;
		if(request.getParameter(Constants.CP_SEARCH_CP_ID)!=null)
		{
			collectionProtocolId = request.getParameter(Constants.CP_SEARCH_CP_ID);
		}
		String collectionProtocolRegIdValue = null;
		if(request.getParameter("collectionProtocolRegIdValue")!=null)
		{
			collectionProtocolRegIdValue = request.getParameter("collectionProtocolRegIdValue");
		}
		
		
		long cpId = new Long(collectionProtocolId).longValue();
		//Bug: 5935
		//Remove old list of specimen from Session.
		session.removeAttribute(Constants.SPECIMEN_LIST);
		//As per the collection protocol registration id of Participant set All Participant's Specimen List to Session
		if(collectionProtocolRegIdValue!=null && !(collectionProtocolRegIdValue.equals("")))
		{	
			List columnList=columnNames();		
			session.setAttribute(Constants.COLUMNLIST,columnList);		
			CollectionProtocolRegistration collectionProtocolRegistration = getcprObj(collectionProtocolRegIdValue);
			List specimenDetails= new ArrayList();
			getSpecimenDetails(collectionProtocolRegistration,specimenDetails);
			session.setAttribute(Constants.SPECIMEN_LIST,specimenDetails);
		}
		//Getting witness name list for CollectionProtocolID
		List witnessList = getWitnessNameList(collectionProtocolId);
		//Getting ResponseList if Operation=Edit then "Withdraw" is added to the List 
		List responseList= Utility.responceList(operation);
		
		//Getting consent response map.
		String consentResponseKey = Constants.CONSENT_RESPONSE_KEY+cpId;
		Hashtable consentResponseHashTable = (Hashtable)session.getAttribute(Constants.CONSENT_RESPONSE);
		
		
		Map consentResponseMap;
		if(consentResponseHashTable != null && consentResponseHashTable.containsKey(consentResponseKey)) // If Map already exist in session
		{
			ConsentResponseBean consentResponseBean = (ConsentResponseBean)consentResponseHashTable.get(consentResponseKey);
			Collection consentResponseCollection = consentResponseBean.getConsentResponse();
			consentResponseMap = getConsentResponseMap(consentResponseCollection,true);
			consentForm.setSignedConsentUrl(consentResponseBean.getSignedConsentUrl());
			consentForm.setWitnessId(consentResponseBean.getWitnessId());
			consentForm.setConsentDate(consentResponseBean.getConsentDate());
			
		}
		else 
		{
			Collection consentResponseCollection = getConsentList(collectionProtocolId);
			consentResponseMap = getConsentResponseMap(consentResponseCollection,false);
		}
		
		consentForm.setCollectionProtocolID(cpId);
		consentForm.setConsentResponseValues(consentResponseMap);
		consentForm.setConsentTierCounter(consentCounter);
		String pageOf  = request.getParameter(Constants.PAGEOF);
		
		request.setAttribute("witnessList", witnessList);			
		request.setAttribute("responseList", responseList);
		request.setAttribute("cpId", collectionProtocolId);
		request.setAttribute(Constants.PAGEOF, pageOf);
		request.setAttribute(consentResponseKey, consentResponseMap);
		
		
        return mapping.findForward(pageOf);
	}
	/**
	 * This function adds the columns to the List
	 * @return columnList 
	 */
	public List columnNames()
	{
		List columnList = new ArrayList();
		columnList.add("Label");
		columnList.add(Constants.TYPE);
		columnList.add(Constants.STORAGE_CONTAINER_LOCATION);
		columnList.add(Constants.CLASS_NAME);
		return columnList; 
	}
	/**
	 * This function will return CollectionProtocolRegistration object 
	 * @param scg_id Selected SpecimenCollectionGroup ID
	 * @return collectionProtocolRegistration
	 */
	private CollectionProtocolRegistration getcprObj(String cpr_id) throws DAOException
	{
		CollectionProtocolRegistrationBizLogic collectionProtocolRegistrationBizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		String colName = "id";			
		List getCPRIdFromDB = collectionProtocolRegistrationBizLogic.retrieve(CollectionProtocolRegistration.class.getName(), colName, cpr_id);		
		CollectionProtocolRegistration collectionProtocolRegistrationObject = (CollectionProtocolRegistration)getCPRIdFromDB.get(0);
		return collectionProtocolRegistrationObject;
	}
	
	/**
	 * @param collectionProtocolRegistration
	 * @param finalDataList
	 * @throws DAOException
	 */
	private void getSpecimenDetails(CollectionProtocolRegistration collectionProtocolRegistration, List finalDataList) throws DAOException
	{
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		Collection specimencollectionGroup = (Collection)bizLogic.retrieveAttribute(CollectionProtocolRegistration.class.getName(),collectionProtocolRegistration.getId(), "elements(specimenCollectionGroupCollection)");
		//Collection specimencollectionGroup = collectionProtocolRegistration.getSpecimenCollectionGroupCollection();
		Iterator specimenCollGroupIterator = specimencollectionGroup.iterator();
		while(specimenCollGroupIterator.hasNext())
		{
			SpecimenCollectionGroup specimenCollectionGroupObj =(SpecimenCollectionGroup)specimenCollGroupIterator.next(); 
			getDetailsOfSpecimen(specimenCollectionGroupObj, finalDataList);
		}		
	}
	/**
	 * This function is used for retriving specimen and sub specimen's attributes.
	 * @param specimenObj
	 * @param finalDataList
	 * @throws DAOException 
	 */
	private void getDetailsOfSpecimen(SpecimenCollectionGroup specimenCollGroupObj, List finalDataList) throws DAOException
	{
		// lazy Resolved specimenCollGroupObj.getSpecimenCollection();
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		Collection specimenCollection = (Collection)bizLogic.retrieveAttribute(SpecimenCollectionGroup.class.getName(), specimenCollGroupObj.getId(), "elements(specimenCollection)");
		Iterator specimenIterator = specimenCollection.iterator();
		while(specimenIterator.hasNext())
		{
			Specimen specimenObj =(Specimen)specimenIterator.next();
			List specimenDetailList=new ArrayList();
			if(specimenObj.getActivityStatus().equals(Constants.ACTIVITY_STATUS_ACTIVE))
			{
				specimenDetailList.add(specimenObj.getLabel());
				specimenDetailList.add(specimenObj.getType());
				if(specimenObj.getStorageContainer()==null)
				{
					specimenDetailList.add(Constants.VIRTUALLY_LOCATED);
				}
				else
				{
					//kalpana:
					StorageContainer storageContainer =	(StorageContainer)bizLogic.retrieveAttribute(Specimen.class.getName(), specimenObj.getId(),"storageContainer");
					String storageLocation=storageContainer.getName()+": X-Axis-"+specimenObj.getPositionDimensionOne()+", Y-Axis-"+specimenObj.getPositionDimensionTwo();
					specimenDetailList.add(storageLocation);
				}
				specimenDetailList.add(specimenObj.getClassName());
				finalDataList.add(specimenDetailList);
			}
		}
		
	}
	/**
	 * Returns the Map of consent responses for given collection protocol.
	 * @param consentResponse
	 * @param isMapExist
	 * @return
	 */
	private Map getConsentResponseMap(Collection consentResponse , boolean isMapExist){
		Map consentResponseMap = new LinkedHashMap();
		if(consentResponse != null){
			int i=0;
			Iterator consentResponseIter = consentResponse.iterator();
			String idKey=null;
			String statementKey=null;
			String responsekey =null;
			String participantResponceIdKey =null;
			while(consentResponseIter.hasNext())
			{
				idKey="ConsentBean:"+i+"_consentTierID";
				statementKey="ConsentBean:"+i+"_statement";
				responsekey = "ConsentBean:"+i+"_participantResponse";
				participantResponceIdKey="ConsentBean:"+i+"_participantResponseID";
				
				if(isMapExist){
					ConsentBean consentBean=(ConsentBean)consentResponseIter.next();
					consentResponseMap.put(idKey, consentBean.getConsentTierID());
					consentResponseMap.put(statementKey,consentBean.getStatement());
					consentResponseMap.put(responsekey, consentBean.getParticipantResponse());
					consentResponseMap.put(participantResponceIdKey, consentBean.getParticipantResponseID());
				}
				else
				{
					ConsentTier consent=(ConsentTier)consentResponseIter.next();
					consentResponseMap.put(idKey, consent.getId());
					consentResponseMap.put(statementKey,consent.getStatement());
					consentResponseMap.put(responsekey, "");
					consentResponseMap.put(participantResponceIdKey, "");
				}
				i++;
			}
			consentCounter=i;
		}
		return consentResponseMap;
	}
	
	
	/**
	 * Adding name,value pair in NameValueBean for Witness Name
	 * @param collProtId Get Witness List for this ID
	 * @return consentWitnessList
	 */ 
	public Collection getConsentList(String collectionProtocolID) throws DAOException
    {   	
    	CollectionProtocolBizLogic collectionProtocolBizLogic = (CollectionProtocolBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
		List collProtList  = collectionProtocolBizLogic.retrieve(CollectionProtocol.class.getName(), Constants.ID, collectionProtocolID);		
		CollectionProtocol collectionProtocol = (CollectionProtocol)collProtList.get(0);
		//Setting consent tiers
		//Resolved lazy --- collectionProtocol.getConsentTierCollection()
		Collection consentTierCollection = (Collection)collectionProtocolBizLogic.retrieveAttribute(CollectionProtocol.class.getName(), collectionProtocol.getId(), "elements(consentTierCollection)");
		return consentTierCollection;
    }
	
	/**
	 * Adding name,value pair in NameValueBean for Witness Name
	 * @param collProtId Get Witness List for this ID
	 * @return consentWitnessList
	 */ 
	private List getWitnessNameList(String collProtId) throws DAOException
	{		
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		
		List collProtList = bizLogic.retrieve(CollectionProtocol.class.getName(), Constants.ID, collProtId);		
		CollectionProtocol collectionProtocol = (CollectionProtocol)collProtList.get(0);
		//Setting the consent witness
		String witnessFullName="";
		List consentWitnessList = new ArrayList();
		consentWitnessList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
		Collection userCollection = null;
		if(collectionProtocol.getId()!= null)
		{ 
			userCollection = (Collection)bizLogic.retrieveAttribute(CollectionProtocol.class.getName(),collectionProtocol.getId(), "elements(coordinatorCollection)");
		}
		
		Iterator iter = userCollection.iterator();
		while(iter.hasNext())
		{
			User user = (User)iter.next();
			witnessFullName = user.getLastName()+", "+user.getFirstName();
			consentWitnessList.add(new NameValueBean(witnessFullName,user.getId()));
		}
		//Setting the PI
		User principalInvestigator = (User)bizLogic.retrieveAttribute(CollectionProtocol.class.getName(),collectionProtocol.getId(), "principalInvestigator");
		String piFullName=principalInvestigator.getLastName()+", "+principalInvestigator.getFirstName();
		consentWitnessList.add(new NameValueBean(piFullName,principalInvestigator.getId()));
		return consentWitnessList;
	}	

}
