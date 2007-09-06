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
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ConsentTier;
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
		long cpId = new Long(collectionProtocolId).longValue();
		
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
