/**
 * <p>Title: ParticipantRegistrationSelectAction Class>
 * <p>Description:	This Class is used when participant is selected from the list.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author abhishek_mehta
 * @Created on June 06, 2006
 */
package edu.wustl.catissuecore.action;

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

import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.bean.ConsentResponseBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantEmpi;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.dao.HibernateDAOImpl;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.factory.AbstractDomainObjectFactory;
import edu.wustl.common.factory.MasterFactory;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

public class ParticipantRegistrationSelectAction extends CommonAddEditAction{
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
    {
		ActionForward forward = null;
		try
		{
			AbstractDomainObject abstractDomain = null;
	
			ParticipantForm participantForm=(ParticipantForm) form;
			AbstractDomainObjectFactory abstractDomainObjectFactory = (AbstractDomainObjectFactory) MasterFactory
					.getFactory("edu.wustl.catissuecore.domain.DomainObjectFactory");
	
			IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(participantForm.getFormId());
	
			String objectName = abstractDomainObjectFactory.getDomainObjectName(participantForm.getFormId());
		  	
			Logger.out.info("Participant Id-------------------"+objectName);
			Logger.out.info("Participant Id-------------------"+participantForm.getFormId());
			Logger.out.info("Participant Id-------------------"+request.getParameter("participantId"));
			
			String selected = request.getParameter("participantId")== null ? "DANA":request.getParameter("participantId");
			
			long participant_identifier = 0l;
			try{
				 participant_identifier = Long.parseLong(selected);
			}catch(Exception e)
			{
				Logger.out.info("Error in participant_identifier");
				//e.printStackTrace();
			}
			
			
			//Object object = bizLogic.retrieve(objectName, new Long(request.getParameter("participantId")));
			//abstractDomain = (AbstractDomainObject) object;
			Participant participant=null;
			
			//Participant participant = null;
			Map mapCollectionProtocolRegistration = null;
			int cprCount = 0;
			Map mapParticipantMedicalIdentifier = null;
			Collection consentResponseBeanCollection = null;
			Hashtable consentResponseHashTable = null;
			Logger.out.info("Participant Id#######--"+selected);
			
			
			
			if(participant_identifier == 0l)
			{
				String parti_ID = request.getParameter("clicked_Row_selected");
		        Logger.out.info("#########Participant Id"+parti_ID);
	        
	        
				
	        	Logger.out.info("$$$$$$$$$List");
				HttpSession session = request.getSession();
				List part = (List)session.getAttribute("MatchedParticpant");
				
				Logger.out.info("$$$$$$$$$List"+part.size());
				//DefaultLookupResult result = new DefaultLookupResult();
				//Participant participant = new Participant();
				
				DefaultLookupResult df = (DefaultLookupResult)part.get(Integer.parseInt(parti_ID)-1);
				participant = (Participant)df.getObject();
				//participant.setId(0l);
				abstractDomain = (AbstractDomainObject)participant;
				
				participantForm.setAllValues(abstractDomain);
				participantForm.setOperation(Constants.ADD);
			
			}else
			{
				//put it in Else loop for dana farber by satish
				HttpSession session = request.getSession();
				List empiList = (List)session.getAttribute("MatchedEmpiList");
				
				if(empiList != null )
				{
					boolean isUpdate = false;
					for (int i = 0; i < empiList.size(); i++) 
					{
						ParticipantEmpi empi = (ParticipantEmpi)empiList.get(i);
						if(empi.getId() != null)
						{
							if(participant_identifier == empi.getId().longValue());
							{
								isUpdate = true;
								break;
							}
						}
					}
					
					if(isUpdate)
					{
						String parti_ID = request.getParameter("clicked_Row_selected");
				        Logger.out.info("#########Participant Id"+parti_ID);
			        
			        
						
			        	Logger.out.info("$$$$$$$$$List");
						HttpSession session1 = request.getSession();
						List part = (List)session1.getAttribute("MatchedParticpant");
						
						Logger.out.info("$$$$$$$$$List"+part.size());
						//DefaultLookupResult result = new DefaultLookupResult();
						//Participant participant = new Participant();
						
						DefaultLookupResult df = (DefaultLookupResult)part.get(Integer.parseInt(parti_ID)-1);
						participant = (Participant)df.getObject();
						//participant.setId(0l);
						abstractDomain = (AbstractDomainObject)participant;
						
						participantForm.setAllValues(abstractDomain);
						participantForm.setOperation(Constants.EDIT);
					}else
					{
						Logger.out.info("$$$$$Came in else part");
						
						Object object = bizLogic.retrieve(objectName, new Long(request.getParameter("participantId")));
						abstractDomain = (AbstractDomainObject) object;
						//List participants = bizLogic.retrieve(objectName,Constants.SYSTEM_IDENTIFIER,new Long(request.getParameter("participantId")));
						//abstractDomain = (AbstractDomainObject)participants.get(0);
						participant=(Participant)abstractDomain;
					
					
						Logger.out.info("Last name in ParticipantSelectAction:"+participant.getLastName());
						
						// To append the cpr to already existing cprs
						//Gets the collection Protocol Registration map from ActionForm
						/*satish*/mapCollectionProtocolRegistration = participantForm.getCollectionProtocolRegistrationValues();
						/*satish*/cprCount = participantForm.getCollectionProtocolRegistrationValueCounter();
						/*satish*/consentResponseBeanCollection = participantForm.getConsentResponseBeanCollection();
						/*satish*/consentResponseHashTable = participantForm.getConsentResponseHashTable();
						/*satish*/mapParticipantMedicalIdentifier = participantMedicalIdentifierMap(participantForm.getValues());
						
						//Gets the collection Protocol Registration map from Database
						DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
						defaultBizLogic.populateUIBean(Participant.class.getName(),participant.getId(), participantForm);
						 
						Map mapCollectionProtocolRegistrationOld = participantForm.getCollectionProtocolRegistrationValues();
						int cprCountOld = participantForm.getCollectionProtocolRegistrationValueCounter();
						Collection consentResponseBeanCollectionOld = participantForm.getConsentResponseBeanCollection();
						Hashtable consentResponseHashTableOld = participantForm.getConsentResponseHashTable();
						
						Map mapCollectionProtocolRegistrationAppended = appendCollectionProtocolRegistrations(mapCollectionProtocolRegistration ,cprCount ,mapCollectionProtocolRegistrationOld ,cprCountOld);
						Map mapParticipantMedicalIdentifierOld = participantMedicalIdentifierMap(participantForm.getValues());
						
						if(consentResponseBeanCollection != null)
						{
							updateConsentResponse(consentResponseBeanCollection,consentResponseBeanCollectionOld,consentResponseHashTableOld);
						}
						
						participantForm.setCollectionProtocolRegistrationValues(mapCollectionProtocolRegistrationAppended);
						participantForm.setCollectionProtocolRegistrationValueCounter((cprCountOld+cprCount));
						participantForm.setValues(mapParticipantMedicalIdentifierOld);
						participantForm.setConsentResponseBeanCollection(consentResponseBeanCollectionOld);
						participantForm.setConsentResponseHashTable(consentResponseHashTableOld);
						//added by satish to end if loop
					}
				}
				
				
			}
			
			
			forward = super.execute(mapping, participantForm, request, response);
			
			//added by satish to get participant id if participant is added from web services ,
			//It is new participant added into participant table
			
			/*
			if(participant_identifier == 0l)
			{
				System.out.println("EMPI ID");
				System.out.println("EMPI ID-"+participantForm.getId());
				System.out.println("EMPI ID 1-"+request.getAttribute(Constants.SYSTEM_IDENTIFIER));
			}
			*/
			//end of if by satish
			
			if(!forward.getName().equals("failure"))
			{
				
				//added by satish to get participant id if participant is added from web services ,
				//It is new participant added into participant table
				
				if(participant_identifier == 0l)
				{
					String parti_ID = request.getParameter("clicked_Row_selected");
					HttpSession session = request.getSession();
					List empiList = (List)session.getAttribute("MatchedEmpiList");
					System.out.println(empiList.size()+"+++++++++"+parti_ID);
					
					for (int i = 0; i < empiList.size(); i++) 
					{
						ParticipantEmpi empi = (ParticipantEmpi)empiList.get(i);
						
							System.out.println(empi.getId()+"**********"+empi.getEmpi_id());
						
					}
					ParticipantEmpi empi = (ParticipantEmpi)empiList.get(Integer.parseInt(parti_ID)-1);
					
					
					
					System.out.println("EMPI ID");
					System.out.println("EMPI ID-"+participantForm.getId());
					System.out.println("EMPI ID 1-"+request.getAttribute(Constants.SYSTEM_IDENTIFIER));
					HibernateDAOImpl impl = new HibernateDAOImpl();
					//ParticipantEmpi empi = new ParticipantEmpi();
					empi.setId(participantForm.getId());
					//empi.setEmpi_id(2l);
					impl.openSession(getSessionData(request));
					impl.insert(empi, getSessionData(request), true, true);
					impl.commit();
					impl.closeSession();
				}
				
				 
				//end of if by satish
				request.removeAttribute("participantForm");
				request.setAttribute("participantForm1",participantForm);
				request.setAttribute("participantSelect","yes");
			}
			else
			{
	
				participantForm.setCollectionProtocolRegistrationValues(mapCollectionProtocolRegistration);
				participantForm.setCollectionProtocolRegistrationValueCounter(cprCount);
				participantForm.setValues(mapParticipantMedicalIdentifier);
				participantForm.setConsentResponseBeanCollection(consentResponseBeanCollection);
				participantForm.setConsentResponseHashTable(consentResponseHashTable);
				request.setAttribute("continueLookup","yes");
			}
		}
		catch(Exception e)
		{
			Logger.out.info("Error--------");
			e.printStackTrace();
			Logger.out.info(e.getMessage());
		}
		
		return forward;
	}
	
	/*
	 * This method is for updating consent response 
	 */
	private void updateConsentResponse(Collection consentResponseBeanCollection, Collection consentResponseBeanCollectionOld, Hashtable consentResponseHashTableOld)
	{
		Iterator it = consentResponseBeanCollection.iterator();
		while(it.hasNext())
		{
			ConsentResponseBean consentResponseBean = (ConsentResponseBean)it.next();
			long collectionProtocolId = consentResponseBean.getCollectionProtocolID();
			if(collectionProtocolId > 0)
			{
				if(!isAlreadyExist(consentResponseBeanCollectionOld,collectionProtocolId))
				{
					consentResponseBeanCollectionOld.add(consentResponseBean);
					String key = Constants.CONSENT_RESPONSE_KEY+collectionProtocolId;
					consentResponseHashTableOld.put(key, consentResponseBean);
				}
			}
		}
	}
	/*
	 * Checking that given collection protocol is aleady exist in consentResponseBeanCollection
	 */
	private boolean isAlreadyExist(Collection consentResponseBeanCollection, long collectionProtocolId)
	{
	
		Iterator it = consentResponseBeanCollection.iterator();
		while(it.hasNext())
		{
			ConsentResponseBean consentResponseBean = (ConsentResponseBean)it.next();
			long cpId = consentResponseBean.getCollectionProtocolID();
			if(cpId == collectionProtocolId)
			{
				return true;
			}
		}
		return false;
	}
	/*
	 * This method will remove invalid entries from the Map
	 * //Abhishek mehta
	 */
	private Map participantMedicalIdentifierMap(Map participantMedicalIdentifier) 
	{
		Validator validator = new Validator();
		String className = "ParticipantMedicalIdentifier:";
		String key1 = "_Site_" + Constants.SYSTEM_IDENTIFIER;
		String key2 = "_medicalRecordNumber";
		String key3 = "_" + Constants.SYSTEM_IDENTIFIER;
		int index = 1;

		while(true)
		{
			String keyOne = className + index + key1;
			String keyTwo = className + index + key2;
			String keyThree = className + index + key3;
			
			String value1 = (String)participantMedicalIdentifier.get(keyOne);
			String value2 = (String)participantMedicalIdentifier.get(keyTwo);
			
			if(value1 == null || value2 == null)
			{
				break;
			}
			else if(!validator.isValidOption(value1) && value2.trim().equals(""))
			{
				participantMedicalIdentifier.remove(keyOne);
				participantMedicalIdentifier.remove(keyTwo);
				participantMedicalIdentifier.remove(keyThree);
			}
			index++;
		}
		return participantMedicalIdentifier;
	}
	
	/*
	 * This method is for appending collection protocol registration for given participant.
	 * //Abhishek mehta
	 */
	private Map appendCollectionProtocolRegistrations(Map mapCollectionProtocolRegistration , int cprCount , Map mapCollectionProtocolRegistrationOld , int cprCountOld) throws Exception
    {
    	int cprCountNew = cprCount+cprCountOld;
    	for(int i = cprCountOld+1 ; i<= cprCountNew ; i++)
    	{
    		String collectionProtocolId = "CollectionProtocolRegistration:"+(i-cprCountOld)+"_CollectionProtocol_id";
    		String collectionProtocolTitle = "CollectionProtocolRegistration:"+(i-cprCountOld)+"_CollectionProtocol_shortTitle";
			String collectionProtocolParticipantId = "CollectionProtocolRegistration:"+(i-cprCountOld)+"_protocolParticipantIdentifier";
			String collectionProtocolRegistrationDate = "CollectionProtocolRegistration:" +(i-cprCountOld) +"_registrationDate";
			String collectionProtocolIdentifier = "CollectionProtocolRegistration:" + (i-cprCountOld) +"_id";
			String isConsentAvailable = "CollectionProtocolRegistration:" + (i-cprCountOld) +"_isConsentAvailable";
			String isActive = "CollectionProtocolRegistration:" + (i-cprCountOld) +"_activityStatus";
			
			String collectionProtocolIdNew = "CollectionProtocolRegistration:"+i+"_CollectionProtocol_id";
			String collectionProtocolTitleNew = "CollectionProtocolRegistration:"+i+"_CollectionProtocol_shortTitle";
			String collectionProtocolParticipantIdNew = "CollectionProtocolRegistration:"+i+"_protocolParticipantIdentifier";
			String collectionProtocolRegistrationDateNew = "CollectionProtocolRegistration:" + i +"_registrationDate";
			String collectionProtocolIdentifierNew = "CollectionProtocolRegistration:" + i +"_id";
			String isConsentAvailableNew = "CollectionProtocolRegistration:" + i +"_isConsentAvailable";
			String isActiveNew = "CollectionProtocolRegistration:" + i +"_activityStatus";
			
			mapCollectionProtocolRegistrationOld.put(collectionProtocolIdNew,mapCollectionProtocolRegistration.get(collectionProtocolId));
			mapCollectionProtocolRegistrationOld.put(collectionProtocolTitleNew, mapCollectionProtocolRegistration.get(collectionProtocolTitle));
			mapCollectionProtocolRegistrationOld.put(collectionProtocolParticipantIdNew,mapCollectionProtocolRegistration.get(collectionProtocolParticipantId));
			mapCollectionProtocolRegistrationOld.put(collectionProtocolRegistrationDateNew,mapCollectionProtocolRegistration.get(collectionProtocolRegistrationDate));
			mapCollectionProtocolRegistrationOld.put(collectionProtocolIdentifierNew,mapCollectionProtocolRegistration.get(collectionProtocolIdentifier));
			mapCollectionProtocolRegistrationOld.put(isConsentAvailableNew,mapCollectionProtocolRegistration.get(isConsentAvailable));
			String status = Constants.ACTIVITY_STATUS_ACTIVE;
			if(mapCollectionProtocolRegistration.get(isActive)!=null)
			{
				status = (String)mapCollectionProtocolRegistration.get(isActive);
			}
			
			mapCollectionProtocolRegistrationOld.put(isActiveNew,status);
    	}
    	
    	mapCollectionProtocolRegistrationOld = participantCollectionProtocolRegistration(mapCollectionProtocolRegistrationOld);
    	
    	return mapCollectionProtocolRegistrationOld;
    }
	
	/*
	 * This method will remove invalid entries from the Map
	 * //Abhishek mehta
	 */
	private Map participantCollectionProtocolRegistration(Map collectionProtocolRegistrationValues)
	{
		Validator validator = new Validator();
		String collectionProtocolClassName = "CollectionProtocolRegistration:";
		String collectionProtocolId = "_CollectionProtocol_id";
		String collectionProtocolParticipantId = "_protocolParticipantIdentifier";
		String collectionProtocolRegistrationDate = "_registrationDate";
		String collectionProtocolIdentifier = "_id";
		String isConsentAvailable = "_isConsentAvailable";
		String isActive = "_activityStatus";
		String collectionProtocolTitle = "_CollectionProtocol_shortTitle";
		
		int index = 1;

		while(true)
		{
			String keyOne = collectionProtocolClassName + index + collectionProtocolId;
			String keyTwo = collectionProtocolClassName + index + collectionProtocolParticipantId;
			String keyThree = collectionProtocolClassName + index + collectionProtocolRegistrationDate;
			String keyFour = collectionProtocolClassName + index + collectionProtocolIdentifier;
			String keyFive = collectionProtocolClassName + index + isConsentAvailable;
			String keySix = collectionProtocolClassName + index + isActive;
			String KeySeven = collectionProtocolClassName + index + collectionProtocolTitle;
			
			String value1 = (String)collectionProtocolRegistrationValues.get(keyOne);
			String value2 = (String)collectionProtocolRegistrationValues.get(keyTwo);
			String value3 = (String)collectionProtocolRegistrationValues.get(keyThree);
			
			if(value1 == null || value2 == null || value3 == null)
			{
				break;
			}
			else if(!validator.isValidOption(value1))
			{
				collectionProtocolRegistrationValues.remove(keyOne);
				collectionProtocolRegistrationValues.remove(keyTwo);
				collectionProtocolRegistrationValues.remove(keyThree);
				collectionProtocolRegistrationValues.remove(keyFour);
				collectionProtocolRegistrationValues.remove(keyFive);
				collectionProtocolRegistrationValues.remove(keySix);
				collectionProtocolRegistrationValues.remove(KeySeven);
			}
			index++;
		}
		
    	return collectionProtocolRegistrationValues;
	}

}
