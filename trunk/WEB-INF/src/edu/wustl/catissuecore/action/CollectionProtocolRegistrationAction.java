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
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;


import java.util.ArrayList;
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.AddNewSessionDataBean;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields in the User Add/Edit webpage.
 * @author ajay_sharma
 */

public class CollectionProtocolRegistrationAction extends SecureAction
{
	//This will keep track of no of consents for a particular participant
	int consentCounter;	
	/**
	 * Overrides the execute method of Action class.
	 * Sets the various fields in Participant Registration Add/Edit webpage.
	 * */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		CollectionProtocolRegistrationForm collectionProtocolRegistrationForm = (CollectionProtocolRegistrationForm)form;
		//Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);
		
		//CollectionProtocolId
		String cp_id=String.valueOf(collectionProtocolRegistrationForm.getCollectionProtocolID());

		//Consent Tracking (Virender Mehta)
					
		String showConsents = request.getParameter(Constants.SHOW_CONSENTS);	
		if(!cp_id.equals("-1"))
		{
			if(showConsents!=null && showConsents.equalsIgnoreCase(Constants.YES))
			{
				//Adding name,value pair in NameValueBean
				//Getting witness name list for CollectionProtocolID
				List witnessList = witnessNameList(cp_id);
				//Getting ResponseList if Operation=Edit then "Withdraw" is added to the List 
				List responseList= Utility.responceList(operation);
				List requestConsentList = getConsentList(cp_id);
				if(operation.equalsIgnoreCase(Constants.ADD))
				{ 

					ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
					if(errors == null)
					{ 
						Map tempMap=prepareConsentMap(requestConsentList);
						collectionProtocolRegistrationForm.setConsentResponseValues(tempMap);
					}
						collectionProtocolRegistrationForm.setConsentTierCounter(requestConsentList.size()) ;		
					
					
				}
				else
				{
					String cprID = String.valueOf(collectionProtocolRegistrationForm.getId());
					//getcprObj
					CollectionProtocolRegistration collectionProtocolRegistration = getcprObj(cprID);
//					List added for grid
					List specimenDetails= new ArrayList();
					getSpecimenDetails(collectionProtocolRegistration,specimenDetails);
					List columnList=columnNames();
					Collection consentResponse = collectionProtocolRegistration.getConsentTierResponseCollection();
					Map tempMap=prepareMap(consentResponse);
					collectionProtocolRegistrationForm.setConsentResponseValues(tempMap);
					collectionProtocolRegistrationForm.setConsentTierCounter(consentCounter) ;
					HttpSession session =request.getSession();
					session.setAttribute(Constants.SPECIMEN_LIST,specimenDetails);
					session.setAttribute(Constants.COLUMNLIST,columnList);
				}
				request.setAttribute("witnessList", witnessList);			
				request.setAttribute("responseList", responseList);
			}		
		}
		//Consent Tracking Virender Mehta		
		
		//Sets the operation attribute to be used in the Add/Edit User Page. 
		request.setAttribute(Constants.OPERATION, operation);
        if(operation.equalsIgnoreCase(Constants.ADD ) )
        {
        	((CollectionProtocolRegistrationForm)form).setId(0);
        }

		//Gets the value of the pageOf parameter.
		String pageOf = request.getParameter(Constants.PAGEOF);
		request.setAttribute(Constants.PAGEOF,pageOf);
        
        IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);

		//get list of Protocol title.
		String sourceObjectName = CollectionProtocol.class.getName();
		String[] displayNameFields = {"title"};
		String valueField = Constants.SYSTEM_IDENTIFIER;
		List list = bizLogic.getList(sourceObjectName, displayNameFields, valueField, true);
		request.setAttribute(Constants.PROTOCOL_LIST, list);
		
		Logger.out.debug("SubmittedFor on CPRAction====>"+request.getAttribute(Constants.SUBMITTED_FOR));
		if( (request.getAttribute(Constants.SUBMITTED_FOR) != null) && (request.getAttribute(Constants.SUBMITTED_FOR).equals("AddNew")))
		{
		    HttpSession session = request.getSession();
            Stack formBeanStack = (Stack)session.getAttribute(Constants.FORM_BEAN_STACK);
            
	        if(formBeanStack !=null)
	        {
	        	try
				{
	            AddNewSessionDataBean addNewSessionDataBean = (AddNewSessionDataBean)formBeanStack.peek();
	            
	            SpecimenCollectionGroupForm sessionFormBean =(SpecimenCollectionGroupForm)addNewSessionDataBean.getAbstractActionForm();
	            
	            ((CollectionProtocolRegistrationForm)form).setCollectionProtocolID(sessionFormBean.getCollectionProtocolId());
				}
	        	catch(ClassCastException exp)
				{
	        		Logger.out.debug("Class cast Exception in CollectionProtocolRegistrationAction ~~~~~~~~~~~~~~~~~~~~~~~>"+exp);
				}
	        }
		}
		
		if(request.getParameter(Constants.CP_SEARCH_CP_ID)!=null)
		{
			long cpSearchCpId = new Long(request.getParameter(Constants.CP_SEARCH_CP_ID)).longValue();
			try
			{
		    ((CollectionProtocolRegistrationForm)form).setCollectionProtocolID(cpSearchCpId);
			}
        	catch(ClassCastException exp)
			{
        		Logger.out.debug("Class cast Exception in CollectionProtocolRegistrationAction ~~~~~~~~~~~~~~~~~~~~~~~>"+exp);
			}
		}
		
		//get list of Participant's names
		sourceObjectName = Participant.class.getName();
		String[] participantsFields = {"lastName","firstName","birthDate","socialSecurityNumber"};
		String[] whereColumnName = {"lastName","firstName","birthDate","socialSecurityNumber"};
		String[] whereColumnCondition;
		Object[] whereColumnValue;
		
		// get Database name and set conditions 
		if(Variables.databaseName.equals(Constants.MYSQL_DATABASE))
		{
			whereColumnCondition = new String[]{"!=","!=","is not","is not"};
			whereColumnValue = new String[]{"","",null,null};
		}
		else
		{
			// for ORACLE
			whereColumnCondition = new String[]{"is not null","is not null","is not null","is not null"};
			whereColumnValue = new String[]{};
		}
		
		String joinCondition = Constants.OR_JOIN_CONDITION;
		String separatorBetweenFields = ", ";
		
		list = bizLogic.getList(sourceObjectName, participantsFields, valueField, whereColumnName,
	            whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields, false);
		
		
		//get list of Disabled Participants
		String[] participantsFields2 = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnName2 = {"activityStatus"};
		String[] whereColumnCondition2 = {"="};
		String[] whereColumnValue2 = {Constants.ACTIVITY_STATUS_DISABLED};
		String joinCondition2 = Constants.AND_JOIN_CONDITION;
		String separatorBetweenFields2 = ",";
		
		List listOfDisabledParticipant = bizLogic.getList(sourceObjectName, participantsFields2, valueField, whereColumnName2,
	            whereColumnCondition2, whereColumnValue2, joinCondition2, separatorBetweenFields2, false);
		
		//removing disabled participants from the list of Participants
		list=removeDisabledParticipant(list, listOfDisabledParticipant);
		
		// Sets the participantList attribute to be used in the Site Add/Edit Page.
		request.setAttribute(Constants.PARTICIPANT_LIST, list);
		
		//Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
        request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);
	    
        //*************  ForwardTo implementation *************
        HashMap forwardToHashMap=(HashMap)request.getAttribute("forwardToHashMap");
        
        if(forwardToHashMap !=null)
        {
            Long participantId=(Long)forwardToHashMap.get("participantId");
            Logger.out.debug("ParticipantID found in forwardToHashMap========>>>>>>"+participantId);
        
            if((request.getParameter("firstName").trim().length()>0) || (request.getParameter("lastName").trim().length()>0) || (request.getParameter("birthDate").trim().length()>0) ||( (request.getParameter("socialSecurityNumberPartA").trim().length()>0) && (request.getParameter("socialSecurityNumberPartB").trim().length()>0) && (request.getParameter("socialSecurityNumberPartC").trim().length()>0))) 
            {    
                CollectionProtocolRegistrationForm cprForm=(CollectionProtocolRegistrationForm)form;
                cprForm.setParticipantID(participantId.longValue());
                //cprForm.setCheckedButton(true);
                //Bug-2819: Performance issue due to participant drop down: Jitendra
                List participantList = bizLogic.retrieve(sourceObjectName, Constants.SYSTEM_IDENTIFIER, participantId);               
                if(participantList != null && !participantList.isEmpty())
                {
                	Participant participant = (Participant) participantList.get(0);
                	cprForm.setParticipantName(participant.getMessageLabel());
                }           
                
            }
        }
        else
        {
        	if(request.getParameter("participantId")!=null)
        	{
        		try
        		{
        		Long participantId=new Long(request.getParameter("participantId"));
        		CollectionProtocolRegistrationForm cprForm=(CollectionProtocolRegistrationForm)form;
                cprForm.setParticipantID(participantId.longValue());
                //cprForm.setCheckedButton(true);
                 List participantList = bizLogic.retrieve(sourceObjectName, Constants.SYSTEM_IDENTIFIER, participantId);               
                if(participantList != null && !participantList.isEmpty())
                {
                	Participant participant = (Participant) participantList.get(0);
                	cprForm.setParticipantName(participant.getMessageLabel());
                }
        		}
        		catch(NumberFormatException e)
        		{
        			Logger.out.debug("NumberFormatException Occured :"+e);
        		}
        	}
        	       	
        	 //Bug- 2819 :  Jitendra
            if(((CollectionProtocolRegistrationForm)form).getParticipantID() != 0) 
        	{        		
        		try
        		{
        		Long participantId=new Long(((CollectionProtocolRegistrationForm)form).getParticipantID());
        		CollectionProtocolRegistrationForm cprForm=(CollectionProtocolRegistrationForm)form;
                cprForm.setParticipantID(participantId.longValue());
                //cprForm.setCheckedButton(true);                
                List participantList = bizLogic.retrieve(sourceObjectName, Constants.SYSTEM_IDENTIFIER, participantId);               
                if(participantList != null && !participantList.isEmpty())
                {
                	Participant participant = (Participant) participantList.get(0);
                	cprForm.setParticipantName(participant.getMessageLabel());
                }
        		}
        		catch(NumberFormatException e)
        		{
        			Logger.out.debug("NumberFormatException Occured :"+e);
        		}
        	}
        }
        //*************  ForwardTo implementation *************
        
     /*   Logger.out.info("--------------------------------- caching ---------------");
        CollectionProtocolRegistrationBizLogic cBizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
        List list12 =cBizLogic.getAllParticipantRegistrationInfo();
        Logger.out.info("--------------------------------- caching end ---------------");*/
        
        if(request.getParameter(Constants.OPERATION).equals(Constants.EDIT))
        {
        	CollectionProtocolRegistrationForm cprForm = (CollectionProtocolRegistrationForm)form;
        	String participantId = new Long(cprForm.getParticipantID()).toString();
        	if(cprForm.getParticipantID() == 0 && cprForm.getParticipantProtocolID() != null)
        	{
        		participantId = getParticipantIdForProtocolId(cprForm.getParticipantProtocolID(),bizLogic);
        	}
            request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID,participantId);
        }
        
		return mapping.findForward(pageOf);
	}

	/**
	 * 
	 * @param listOfParticipant List of Participants
	 * @param listOfDisabledParticipant List of Disabled Participants
	 * @return listOfActiveParticipant
	 */
	private List removeDisabledParticipant(List listOfParticipant, List listOfDisabledParticipant)
	{
	   List listOfActiveParticipant=new ArrayList();
	   
	   Logger.out.debug("No. Of Participants ~~~~~~~~~~~~~~~~~~~~~~~>"+listOfParticipant.size());
	   Logger.out.debug("No. Of Disabled Participants ~~~~~~~~~~~~~~~~~~~~~~~>"+listOfDisabledParticipant.size());
	   
	   listOfActiveParticipant.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
	   for(int i=0; i<listOfParticipant.size(); i++)
	   {
	       NameValueBean participantBean =(NameValueBean)listOfParticipant.get(i);
	       boolean isParticipantDisable=false;
	       
	       if(Long.parseLong(participantBean.getValue()) == -1)
	       {
	           //listOfActiveParticipant.add(listOfParticipant.get(i));
	           continue;
	       }
	       
	       for(int j=0; j<listOfDisabledParticipant.size(); j++)
	       {
	           if(Long.parseLong(((NameValueBean)listOfDisabledParticipant.get(j)).getValue()) == -1)
	               continue;
	          
	           NameValueBean disabledParticipant = (NameValueBean)listOfDisabledParticipant.get(j);
	           if(participantBean.getValue().equals(disabledParticipant.getValue()))
	           {
	               isParticipantDisable=true;
	               break;
	           }
	       }
	       if(isParticipantDisable==false)
	           listOfActiveParticipant.add(listOfParticipant.get(i));
	   }
	   
	   Logger.out.debug("No.Of Active Participants ~~~~~~~~~~~~~~~~~~~~~~~>"+listOfActiveParticipant.size());
	   
	   return listOfActiveParticipant;
	}
	private String getParticipantIdForProtocolId(String participantProtocolId,IBizLogic bizLogic) throws Exception
	{
		String sourceObjectName = CollectionProtocolRegistration.class.getName();
		String selectColumnName[] = {"participant.id"};
		String whereColumnName[] = {"protocolParticipantIdentifier"};
		String whereColumnCondition[] = {"="};
		Object[] whereColumnValue = {participantProtocolId};
		List participantList = bizLogic.retrieve(sourceObjectName,selectColumnName,whereColumnName,whereColumnCondition,whereColumnValue,Constants.AND_JOIN_CONDITION);
		if(participantList != null && !participantList.isEmpty())
		{
			
			String participantId = ((Long) participantList.get(0)).toString();
			return participantId;
			
		}
		return null;
	}

	//Consent Tracking Virender Mehta		
	/**
	 * Adding name,value pair in NameValueBean for Witness Name
	 * @param collProtId Get Witness List for this ID
	 * @return consentWitnessList
	 */ 
	public List getConsentList(String collectionProtocolID) throws DAOException
    {   	
    	CollectionProtocolBizLogic collectionProtocolBizLogic = (CollectionProtocolBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
		String colName = "id";		
		List collProtList  = collectionProtocolBizLogic.retrieve(CollectionProtocol.class.getName(), colName, collectionProtocolID);		
		CollectionProtocol collectionProtocol = (CollectionProtocol)collProtList.get(0);
		//Setting consent tiers
		Set consentList = (Set)collectionProtocol.getConsentTierCollection();
		List finalConsentList = new ArrayList(consentList);
    	return finalConsentList;
    }
	
	/**
	 * Prepare map for Showing Consents for a CollectionprotocolID when Operation=Add
	 * @param requestConsentList This is the List of Consents for a selected  CollectionProtocolID
	 * @return tempMap
	 */
	private Map prepareMap(Collection partiResponseCollection)
	{
		Map tempMap = new LinkedHashMap(); 
		if(partiResponseCollection!=null)
		{
			int i=0;
			Iterator consentResponseCollectionIter = partiResponseCollection.iterator();
			String idKey=null;
			String statementKey=null;
			String responsekey=null;
			String participantResponceIdKey=null;
			Long consentTierID;
			Long consentID;
			while(consentResponseCollectionIter.hasNext())
			{
				ConsentTierResponse consentTierResponse=(ConsentTierResponse)consentResponseCollectionIter.next();
				ConsentTier consent = (ConsentTier)consentTierResponse.getConsentTier();
				consentTierID = consentTierResponse.getConsentTier().getId();
				consentID= consent.getId();
				if(consentTierID.longValue()==consentID.longValue())						
				{
					idKey="ConsentBean:"+i+"_consentTierID";
					statementKey="ConsentBean:"+i+"_statement";
					responsekey = "ConsentBean:"+i+"_participantResponse";
					participantResponceIdKey="ConsentBean:"+i+"_participantResponseID";
					tempMap.put(idKey, consent.getId());
					tempMap.put(statementKey,consent.getStatement());
					tempMap.put(responsekey, consentTierResponse.getResponse());
					tempMap.put(participantResponceIdKey, consentTierResponse.getId());
					i++;
				}
			}
			consentCounter=i;
			return tempMap;
		}
		else
		{
			return null;
		}
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
	 * Prepare map for Showing Consents for a CollectionprotocolID when Operation=Add
	 * @param requestConsentList This is the List of Consents for a selected  CollectionProtocolID
	 * @return tempMap
	 */
	private Map prepareConsentMap(List requestConsentList)
	{
		Map tempMap = new HashMap(); 
		if(requestConsentList!=null)
		{
			int i=0;
			Iterator consentTierCollectionIter = requestConsentList.iterator();
			String idKey=null;
			String statementKey=null;
			while(consentTierCollectionIter.hasNext())
			{
				ConsentTier consent=(ConsentTier)consentTierCollectionIter.next();
				idKey="ConsentBean:"+i+"_consentTierID";
				statementKey="ConsentBean:"+i+"_statement";
										
				tempMap.put(idKey, consent.getId());
				tempMap.put(statementKey,consent.getStatement());
				i++;
			}
		}
		return tempMap;
	}
	/**
	 * Adding name,value pair in NameValueBean for Witness Name
	 * @param collProtId Get Witness List for this ID
	 * @return consentWitnessList
	 */ 
	private List witnessNameList(String collProtId) throws DAOException
	{		
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String colName = Constants.ID;
		List collProtList = bizLogic.retrieve(CollectionProtocol.class.getName(), colName, collProtId);		
		CollectionProtocol collectionProtocol = (CollectionProtocol)collProtList.get(0);
		//Setting the consent witness
		String witnessFullName="";
		List consentWitnessList = new ArrayList();
		consentWitnessList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
		Collection userColl = collectionProtocol.getCoordinatorCollection();
		Iterator iter = userColl.iterator();
		while(iter.hasNext())
		{
			User user = (User)iter.next();
			witnessFullName = user.getLastName()+", "+user.getFirstName();
			consentWitnessList.add(new NameValueBean(witnessFullName,user.getId()));
		}
		//Setting the PI
		User principalInvestigator = collectionProtocol.getPrincipalInvestigator();
		String piFullName=principalInvestigator.getLastName()+", "+principalInvestigator.getFirstName();
		consentWitnessList.add(new NameValueBean(piFullName,principalInvestigator.getId()));
		return consentWitnessList;
	}	
	//Consent Tracking Virender Mehta
	/**
	 * This function is used for retriving Specimen collection group  from Collection protocol registration Object
	 * @param specimenObj
	 * @param finalDataList
	 */
	private void getSpecimenDetails(CollectionProtocolRegistration collectionProtocolRegistration, List finalDataList)
	{
		Collection specimencollectionGroup = collectionProtocolRegistration.getSpecimenCollectionGroupCollection();
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
	 */
	private void getDetailsOfSpecimen(SpecimenCollectionGroup specimenCollGroupObj, List finalDataList)
	{
		Collection specimenCollection = specimenCollGroupObj.getSpecimenCollection();
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
					String storageLocation=specimenObj.getStorageContainer().getName()+": X-Axis-"+specimenObj.getPositionDimensionOne()+", Y-Axis-"+specimenObj.getPositionDimensionTwo();
					specimenDetailList.add(storageLocation);
				}
				specimenDetailList.add(specimenObj.getClassName());
				finalDataList.add(specimenDetailList);
			}
		}
		
	}
	/**
	 * This function adds the columns to the List
	 * @return columnList 
	 */
	public List columnNames()
	{
		List columnList = new ArrayList();
		columnList.add(Constants.LABLE);
		columnList.add(Constants.TYPE);
		columnList.add(Constants.STORAGE_CONTAINER_LOCATION);
		columnList.add(Constants.CLASS_NAME);
		return columnList; 
	}

}