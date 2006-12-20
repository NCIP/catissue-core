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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.CDEBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.logger.Logger;


/**
 * SpecimenCollectionGroupAction initializes the fields in the 
 * New Specimen Collection Group page.
 * @author ajay_sharma
 */
public class SpecimenCollectionGroupAction  extends SecureAction
{   
	/**
	 * Overrides the execute method of Action class.
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
			{
		SpecimenCollectionGroupForm  specimenCollectionGroupForm = (SpecimenCollectionGroupForm)form;
		Logger.out.debug("SCGA : " + specimenCollectionGroupForm.getId() );
		
		//	set the menu selection 
		request.setAttribute(Constants.MENU_SELECTED, "14"  ); 
		
		//pageOf and operation attributes required for Advance Query Object view.
		String pageOf = request.getParameter(Constants.PAGEOF);
		
		//Gets the value of the operation parameter.
		String operation = (String)request.getParameter(Constants.OPERATION);
		
		//Sets the operation attribute to be used in the Edit/View Specimen Collection Group Page in Advance Search Object View. 
		request.setAttribute(Constants.OPERATION,operation);
		if(operation.equalsIgnoreCase(Constants.ADD ) )
		{
			specimenCollectionGroupForm.setId(0);
			Logger.out.debug("SCGA : set to 0 "+ specimenCollectionGroupForm.getId() );
		}
		
		boolean isOnChange = false; 
		String str = request.getParameter("isOnChange");
		if(str!=null)
		{
			if(str.equals("true"))
				isOnChange = true; 
		}
		
		// get list of Protocol title.
		SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
		//populating protocolist bean.
		String sourceObjectName = CollectionProtocol.class.getName();
		String [] displayNameFields = {"title"};
		String valueField = Constants.SYSTEM_IDENTIFIER;
		List list = bizLogic.getList(sourceObjectName,displayNameFields,valueField, true);
		request.setAttribute(Constants.PROTOCOL_LIST, list);
		
		//Populating the Site Type bean
		sourceObjectName = Site.class.getName();
		String siteDisplaySiteFields[] = {"name"};
		list = bizLogic.getList(sourceObjectName,siteDisplaySiteFields,valueField, true);
		request.setAttribute(Constants.SITELIST, list);
		
		//Populating the participants registered to a given protocol
		loadPaticipants(specimenCollectionGroupForm.getCollectionProtocolId() , bizLogic, request);
		
		//Populating the protocol participants id registered to a given protocol
		loadPaticipantNumberList(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
		
		//Populating the Collection Protocol Events
		loadCollectionProtocolEvent(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
		

		String protocolParticipantId = specimenCollectionGroupForm.getProtocolParticipantIdentifier();
		//Populating the participants Medical Identifier for a given participant
		loadParticipantMedicalIdentifier(specimenCollectionGroupForm.getParticipantId(),bizLogic, request);
		
		//Load Clinical status for a given study calander event point
		List calendarEventPointList = bizLogic.retrieve(CollectionProtocolEvent.class.getName(),
				Constants.SYSTEM_IDENTIFIER,
				new Long(specimenCollectionGroupForm.getCollectionProtocolEventId()));
		if(isOnChange && !calendarEventPointList.isEmpty())
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)calendarEventPointList.get(0);
			specimenCollectionGroupForm.setClinicalStatus(collectionProtocolEvent.getClinicalStatus());
		}
		
		
		// populating clinical Diagnosis field 
    	CDE cde = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_CLINICAL_DIAGNOSIS);
    	CDEBizLogic cdeBizLogic = (CDEBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.CDE_FORM_ID);
    	List clinicalDiagnosisList = new ArrayList();
    	clinicalDiagnosisList.add(new NameValueBean(Constants.SELECT_OPTION,""+Constants.SELECT_OPTION_VALUE));
    	cdeBizLogic.getFilteredCDE(cde.getPermissibleValues(),clinicalDiagnosisList);
    	request.setAttribute(Constants.CLINICAL_DIAGNOSIS_LIST, clinicalDiagnosisList);

		
		// populating clinical Status field
		//		NameValueBean undefinedVal = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
		List clinicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CLINICAL_STATUS,null);
		request.setAttribute(Constants.CLINICAL_STATUS_LIST, clinicalStatusList);
		
		//Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);
		
		
		Logger.out.debug("CP ID in SCG Action======>"+specimenCollectionGroupForm.getCollectionProtocolId());
		Logger.out.debug("Participant ID in SCG Action=====>"+specimenCollectionGroupForm.getParticipantId()+"  "+specimenCollectionGroupForm.getProtocolParticipantIdentifier());
		
		// -------called from Collection Protocol Registration start-------------------------------
		if( (request.getAttribute(Constants.SUBMITTED_FOR) !=null) &&(request.getAttribute(Constants.SUBMITTED_FOR).equals("Default")))
		{
			Logger.out.debug("Populating CP and Participant in SCG ====  AddNew operation loop");
			
			Long cprId =new Long(specimenCollectionGroupForm.getCollectionProtocolRegistrationId());
			
			if(cprId != null)
			{
				List collectionProtocolRegistrationList = bizLogic.retrieve(CollectionProtocolRegistration.class.getName(),
						Constants.SYSTEM_IDENTIFIER,cprId);
				if(!collectionProtocolRegistrationList.isEmpty())
				{
					Object  obj = collectionProtocolRegistrationList.get(0 ); 
					CollectionProtocolRegistration cpr = (CollectionProtocolRegistration)obj;
					
					long cpID = cpr.getCollectionProtocol().getId().longValue();
					long pID = cpr.getParticipant().getId().longValue();
					String ppID = cpr.getProtocolParticipantIdentifier();
					
					Logger.out.debug("cpID : "+ cpID + "   ||  pID : " + pID + "    || ppID : " + ppID );
					
					specimenCollectionGroupForm.setCollectionProtocolId(cpID);
					
					//Populating the participants registered to a given protocol
					loadPaticipants(cpID , bizLogic, request);
					loadPaticipantNumberList(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
					
					String firstName = Utility.toString(cpr.getParticipant().getFirstName());;
					String lastName = Utility.toString(cpr.getParticipant().getLastName());
					String birthDate = Utility.toString(cpr.getParticipant().getBirthDate());
					String ssn = Utility.toString(cpr.getParticipant().getSocialSecurityNumber());
					if(firstName.trim().length()>0 || lastName.trim().length()>0 || birthDate.trim().length()>0 || ssn.trim().length()>0)
					{
						specimenCollectionGroupForm.setParticipantId(pID );
						specimenCollectionGroupForm.setCheckedButton(1); 
					}	
					//Populating the protocol participants id registered to a given protocol
					
					else if(cpr.getProtocolParticipantIdentifier() != null)
					{
						specimenCollectionGroupForm.setProtocolParticipantIdentifier(ppID );
						specimenCollectionGroupForm.setCheckedButton(2); 
					}
					
					//Populating the Collection Protocol Events
					loadCollectionProtocolEvent(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
					
					//Load Clinical status for a given study calander event point
					calendarEventPointList = bizLogic.retrieve(CollectionProtocolEvent.class.getName(),
							Constants.SYSTEM_IDENTIFIER,
							new Long(specimenCollectionGroupForm.getCollectionProtocolEventId()));
					if(isOnChange && !calendarEventPointList.isEmpty())
					{
						CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)calendarEventPointList.get(0);
						specimenCollectionGroupForm.setClinicalStatus(collectionProtocolEvent.getClinicalStatus());
					}
				}
			}
			request.setAttribute(Constants.SUBMITTED_FOR, "Default");
		}
		
		
		//*************  ForwardTo implementation *************
		HashMap forwardToHashMap=(HashMap)request.getAttribute("forwardToHashMap");
		
		if(forwardToHashMap !=null)
		{
			Long collectionProtocolId = (Long)forwardToHashMap.get("collectionProtocolId");
			if(collectionProtocolId == null && request.getParameter("cpId") != null && !request.getParameter("cpId").equals("null"))
			{
				collectionProtocolId = new Long(request.getParameter("cpId"));
			}
			
			Long participantId=(Long)forwardToHashMap.get("participantId");
			String participantProtocolId = (String) forwardToHashMap.get("participantProtocolId");
			
			specimenCollectionGroupForm.setCollectionProtocolId(collectionProtocolId.longValue());
			
			if(participantId != null && participantId.longValue() != 0)
			{    
				//Populating the participants registered to a given protocol
				loadPaticipants(collectionProtocolId.longValue(), bizLogic, request);
				
				loadPaticipantNumberList(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
				
				specimenCollectionGroupForm.setParticipantId(participantId.longValue());
				specimenCollectionGroupForm.setCheckedButton(1);
			}
			else if(participantProtocolId != null)
			{
				//Populating the participants registered to a given protocol
				loadPaticipants(collectionProtocolId.longValue(), bizLogic, request);
				
				loadPaticipantNumberList(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
				specimenCollectionGroupForm.setProtocolParticipantIdentifier(participantProtocolId);
				specimenCollectionGroupForm.setCheckedButton(2);
			}
			//Bug 1915:SpecimenCollectionGroup.Study Calendar Event Point not populated when page is loaded through proceedTo
			//Populating the Collection Protocol Events
			loadCollectionProtocolEvent(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
			
			//Load Clinical status for a given study calander event point
			calendarEventPointList = bizLogic.retrieve(CollectionProtocolEvent.class.getName(),
					Constants.SYSTEM_IDENTIFIER,
					new Long(specimenCollectionGroupForm.getCollectionProtocolEventId()));
			if(!calendarEventPointList.isEmpty())
			{
				CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)calendarEventPointList.get(0);
				specimenCollectionGroupForm.setClinicalStatus(collectionProtocolEvent.getClinicalStatus());
			}
			
			Logger.out.debug("CollectionProtocolID found in forwardToHashMap========>>>>>>"+collectionProtocolId);
			Logger.out.debug("ParticipantID found in forwardToHashMap========>>>>>>"+participantId);
			Logger.out.debug("ParticipantProtocolID found in forwardToHashMap========>>>>>>"+participantProtocolId);
		}
		//*************  ForwardTo implementation *************
		//Populate the group name field with default value in the form of 
		//<Collection Protocol Name>_<Participant ID>_<Group Id>
		int groupNumber=bizLogic.getNextGroupNumber();
		
		//Get the collection protocol title for the collection protocol Id selected
		String collectionProtocolTitle = "";
		list = bizLogic.retrieve(CollectionProtocol.class.getName(),valueField,new Long(specimenCollectionGroupForm.getCollectionProtocolId()));
		
		if(!list.isEmpty())
		{
			CollectionProtocol collectionProtocol = (CollectionProtocol)list.get(0);
			collectionProtocolTitle=collectionProtocol.getTitle();
		}
		
		long groupParticipantId = specimenCollectionGroupForm.getParticipantId();
		//check if the reset name link was clicked
		String resetName = request.getParameter(Constants.RESET_NAME);
		
		//Set the name to default if reset name link was clicked or page is loading for first time 
		//through add link or forward to link 
		if(forwardToHashMap !=null || (specimenCollectionGroupForm.getName()!=null && specimenCollectionGroupForm.getName().equals(""))
				|| (resetName!=null && resetName.equals("Yes")))
		{
			if(!collectionProtocolTitle.equals("")&& (groupParticipantId>0 ||
					(protocolParticipantId!=null && !protocolParticipantId.equals(""))))
			{
				//Poornima:Bug 2833 - Error thrown when adding a specimen collection group
				//Max length of CP is 150 and Max length of SCG is 55, in Oracle the name does not truncate 
				//and it is giving error. So the title is truncated in case it is longer than 30 .
				String maxCollTitle = collectionProtocolTitle;
				if(collectionProtocolTitle.length()>30)
				{
					maxCollTitle = collectionProtocolTitle.substring(0,29);
				}
				//During add operation the id to set in the default name is generated
				if(operation.equals(Constants.ADD))
				{
					//if participant is selected from the list
					if(groupParticipantId>0) 
					{
						specimenCollectionGroupForm.setName(maxCollTitle+"_"+groupParticipantId+"_"+groupNumber);
					}
					//else if participant protocol Id is selected 
					else
					{
						specimenCollectionGroupForm.setName(maxCollTitle+"_"+groupParticipantId+"_"+groupNumber);
					}
				}
				//During edit operation the id to set in the default name using the id
				else if(operation.equals(Constants.EDIT) && (resetName!=null && resetName.equals("Yes")))
				{
					if(groupParticipantId>0) 
					{
						specimenCollectionGroupForm.setName(maxCollTitle+"_"+groupParticipantId+"_"+
											specimenCollectionGroupForm.getId());
						
					}
					else
					{
						specimenCollectionGroupForm.setName(maxCollTitle+"_"+protocolParticipantId+"_"+
								specimenCollectionGroupForm.getId()); 
					}
				}
			}
		}
		
		request.setAttribute(Constants.PAGEOF,pageOf);
		Logger.out.debug("page of in Specimen coll grp action:"+request.getParameter(Constants.PAGEOF));
		// -------called from Collection Protocol Registration end -------------------------------
		return mapping.findForward(pageOf);
			}
	
	private void loadPaticipants(long protocolID, IBizLogic bizLogic, HttpServletRequest request) throws Exception
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
	
	private void loadPaticipantNumberList(long protocolID, IBizLogic bizLogic, HttpServletRequest request) throws Exception
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
	}
	
	private void loadCollectionProtocolEvent(long protocolID, IBizLogic bizLogic, HttpServletRequest request) throws Exception
	{
		String sourceObjectName = CollectionProtocolEvent.class.getName();
		String displayEventFields[] = {"studyCalendarEventPoint"};
		String valueField = "id";
		String whereColumnName[] = {"collectionProtocol."+Constants.SYSTEM_IDENTIFIER};
		String whereColumnCondition[] = {"="};
		Object[] whereColumnValue = {new Long(protocolID)};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		String separatorBetweenFields = "";
		
		List list = bizLogic.getList(sourceObjectName, displayEventFields, valueField, whereColumnName,
				whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields, false);
		
		request.setAttribute(Constants.STUDY_CALENDAR_EVENT_POINT_LIST, list);
	}
	
	private void loadParticipantMedicalIdentifier(long participantID, IBizLogic bizLogic, HttpServletRequest request) throws Exception
	{
		//get list of Participant's names
		String sourceObjectName = ParticipantMedicalIdentifier.class.getName();
		String displayEventFields[] = {"medicalRecordNumber"};
		String valueField = Constants.SYSTEM_IDENTIFIER;
		String whereColumnName[] = {"participant."+Constants.SYSTEM_IDENTIFIER, "medicalRecordNumber"};
		String whereColumnCondition[] = {"=","!="};
		Object[] whereColumnValue = {new Long(participantID),"null"};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		String separatorBetweenFields = "";
		
		List list = bizLogic.getList(sourceObjectName, displayEventFields, valueField, whereColumnName,
				whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields, false);
		
		request.setAttribute(Constants.PARTICIPANT_MEDICAL_IDNETIFIER_LIST, list);
	}
}