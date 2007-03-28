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
import java.util.Collection;
import java.util.Collections;
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

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.SecureAction;
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

		//For Consent Tracking Virender Mehta
		int index=1;
		String id=null;
		String indexType=null;
		String cp_id=String.valueOf(specimenCollectionGroupForm.getCollectionProtocolId());
		if(cp_id.equalsIgnoreCase("0"))
		{
				Map forwardToHashMap = (Map)request.getAttribute("forwardToHashMap");
				if(forwardToHashMap!=null)
				{
					cp_id=forwardToHashMap.get("collectionProtocolId").toString();
	
					id=forwardToHashMap.get("participantId").toString();
					indexType="participantId";
					if(id==null||id.equalsIgnoreCase("0"))
					{
						id=forwardToHashMap.get("participantProtocolId").toString();
						indexType="protocolParticipantIdentifier";
					}
				}
		}
		if(!cp_id.equals("-1"))
		{ 
			String showConsents = request.getParameter(Constants.SHOW_CONSENTS);  
			if(showConsents!=null && showConsents.equalsIgnoreCase(Constants.YES))
			{
				
				if(id==null)
				{
					index=(int)specimenCollectionGroupForm.getCheckedButton();
					if(index==1)
					{
						
						id=Long.toString(specimenCollectionGroupForm.getParticipantId());
						indexType="participantId";
					}
					else
					{
						id=specimenCollectionGroupForm.getProtocolParticipantIdentifier();
						indexType="protocolParticipantIdentifier";
					}
				}
				//If participant and Participant ProtocolIdentifier is not selected
				if(!id.equalsIgnoreCase("-1"))
				{
					//Get CollectionprotocolRegistration Object
					CollectionProtocolRegistration collectionProtocolRegistration=getcollectionProtocolRegistrationObj(id,cp_id,indexType);
					User witness= collectionProtocolRegistration.getConsentWitness();
					if(witness==null||witness.getFirstName()==null)
					{
						String witnessName="";
						specimenCollectionGroupForm.setWitnessName(witnessName);
					}
					else
					{
						String witnessFullName = witness.getLastName()+", "+witness.getFirstName();
						specimenCollectionGroupForm.setWitnessName(witnessFullName);
					}
					String getConsentDate=Utility.parseDateToString(collectionProtocolRegistration.getConsentSignatureDate(), Constants.DATE_PATTERN_MM_DD_YYYY);
					specimenCollectionGroupForm.setConsentDate(getConsentDate);
					String getSignedConsentURL=Utility.toString(collectionProtocolRegistration.getSignedConsentDocumentURL());
					specimenCollectionGroupForm.setSignedConsentUrl(getSignedConsentURL);
					//Set witnessName,ConsentDate and SignedConsentURL				
					Set participantResponseSet = (Set)collectionProtocolRegistration.getConsentTierResponseCollection();
					List participantResponseList= new ArrayList(participantResponseSet);
					if(operation.equalsIgnoreCase(Constants.ADD))
					{
						ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
						if(errors == null)
						{ 
							String protocolEventID = request.getParameter("protocolEventId");
							if(protocolEventID==null||protocolEventID.equalsIgnoreCase(Constants.FALSE))
							{
								Map tempMap=prepareConsentMap(participantResponseList);
								specimenCollectionGroupForm.setConsentResponseForScgValues(tempMap);
							}
						}
						specimenCollectionGroupForm.setConsentTierCounter(participantResponseList.size());
					}
					else
					{
						String scgID = String.valueOf(specimenCollectionGroupForm.getId());
						SpecimenCollectionGroup specimenCollectionGroup = getSCGObj(scgID);
						//List added for grid
						List specimenDetails= new ArrayList();
						getSpecimenDetails(specimenCollectionGroup,specimenDetails);
						List columnList=columnNames();
						Collection consentResponse = specimenCollectionGroup.getCollectionProtocolRegistration().getConsentTierResponseCollection();
						Collection consentResponseStatuslevel= specimenCollectionGroup.getConsentTierStatusCollection();
						Map tempMap=prepareSCGResponseMap(consentResponseStatuslevel, consentResponse);
						specimenCollectionGroupForm.setConsentResponseForScgValues(tempMap);
						specimenCollectionGroupForm.setConsentTierCounter(participantResponseList.size());
						HttpSession session =request.getSession();
						session.setAttribute(Constants.SPECIMEN_LIST,specimenDetails);
						session.setAttribute(Constants.COLUMNLIST,columnList);
					}
				}
				List specimenCollectionGroupResponseList =Utility.responceList(operation);
				request.setAttribute("specimenCollectionGroupResponseList", specimenCollectionGroupResponseList);
			}
		}
		//For Consent Tracking Virender Mehta		
		
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
				request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID,participantId.toString());
				
			}
			else if(participantProtocolId != null)
			{
				//Populating the participants registered to a given protocol
				loadPaticipants(collectionProtocolId.longValue(), bizLogic, request);
				
				loadPaticipantNumberList(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
				specimenCollectionGroupForm.setProtocolParticipantIdentifier(participantProtocolId);
				specimenCollectionGroupForm.setCheckedButton(2);
				String cpParticipantId = getParticipantIdForProtocolId(participantProtocolId,bizLogic);
				if(cpParticipantId != null)
				{
					request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID,cpParticipantId);
				}
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
	 * @param idOfSelectedRadioButton Id for selected radio button.
	 * @param cp_id CollectionProtocolID CollectionProtocolID selected by dropdown 
	 * @param indexType i.e Which Radio button is selected participantId or protocolParticipantIdentifier 
	 * @return collectionProtocolRegistration CollectionProtocolRegistration object
	 */
	private CollectionProtocolRegistration getcollectionProtocolRegistrationObj(String idOfSelectedRadioButton,String cp_id,String indexType) throws DAOException
	{
		
		CollectionProtocolRegistrationBizLogic collectionProtocolRegistrationBizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		String[] colName= new String[2];
		if(indexType.equalsIgnoreCase("participantId"))
		{
			colName[0] = "participant.id";
			colName[1] = "collectionProtocol.id";	
		}	
		else
		{
			colName[0] = "protocolParticipantIdentifier";
			colName[1] = "collectionProtocol.id";	
		}
		
		String[] colCondition = {"=","="};
		String[] val = new String[2];
		val[0]= idOfSelectedRadioButton;
		val[1]= cp_id;
		List collProtRegObj=collectionProtocolRegistrationBizLogic.retrieve(CollectionProtocolRegistration.class.getName(), colName, colCondition,val,null); 
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration)collProtRegObj.get(0);
		return collectionProtocolRegistration;
	}
	
	/**
	* For ConsentTracking Preparing consentResponseForScgValues for populating Dynamic contents on the UI  
	* @param partiResponseCollection This Containes the collection of ConsentTier Response at CPR level
	* @param statusResponseCollection This Containes the collection of ConsentTier Response at Specimen level 
	* @return tempMap
	*/
    private Map prepareSCGResponseMap(Collection statusResponseCollection, Collection partiResponseCollection)
	   {
	    	Map tempMap = new HashMap();
	    	Long consentTierID;
			Long consentID;
			if(partiResponseCollection!=null ||statusResponseCollection!=null)
			{
				int i = 0;
				Iterator statusResponsIter = statusResponseCollection.iterator();			
				while(statusResponsIter.hasNext())
				{
					ConsentTierStatus consentTierstatus=(ConsentTierStatus)statusResponsIter.next();
					consentTierID=consentTierstatus.getConsentTier().getId();				
					Iterator participantResponseIter = partiResponseCollection.iterator();
					
					while(participantResponseIter.hasNext())
					{
						ConsentTierResponse consentTierResponse=(ConsentTierResponse)participantResponseIter.next();
						consentID=consentTierResponse.getConsentTier().getId();
						if(consentTierID.longValue()==consentID.longValue())						
						{
							ConsentTier consent = consentTierResponse.getConsentTier();
							String idKey="ConsentBean:"+i+"_consentTierID";
							String statementKey="ConsentBean:"+i+"_statement";
							String participantResponsekey = "ConsentBean:"+i+"_participantResponse";
							String participantResponceIdKey="ConsentBean:"+i+"_participantResponseID";
							String scgResponsekey  = "ConsentBean:"+i+"_specimenCollectionGroupLevelResponse";
							String scgResponseIDkey ="ConsentBean:"+i+"_specimenCollectionGroupLevelResponseID";
							
							tempMap.put(idKey, consent.getId());
							tempMap.put(statementKey,consent.getStatement());
							tempMap.put(participantResponsekey, consentTierResponse.getResponse());
							tempMap.put(participantResponceIdKey, consentTierResponse.getId());
							tempMap.put(scgResponsekey, consentTierstatus.getStatus());
							tempMap.put(scgResponseIDkey, consentTierstatus.getId());
							i++;
							break;
						}
					}
				}
				return tempMap;
			}		
			else
			{
				return null;
			}
	   }
	
	/**
	 * Prepare Map for Consent tiers
	 * @param participantResponseList This list will be iterated to map to populate participant Response status.
	 * @return tempMap
	 */
	private Map prepareConsentMap(List participantResponseList)
	{
		Map tempMap = new HashMap();
		if(participantResponseList!=null)
		{
			int i = 0;
			Iterator consentResponseCollectionIter = participantResponseList.iterator();
			while(consentResponseCollectionIter.hasNext())
			{
				ConsentTierResponse consentTierResponse = (ConsentTierResponse)consentResponseCollectionIter.next();
				ConsentTier consent = consentTierResponse.getConsentTier();
				String idKey="ConsentBean:"+i+"_consentTierID";
				String statementKey="ConsentBean:"+i+"_statement";
				String responseKey="ConsentBean:"+i+"_participantResponse";
				String participantResponceIdKey="ConsentBean:"+i+"_participantResponseID";
				String scgResponsekey  = "ConsentBean:"+i+"_specimenCollectionGroupLevelResponse";
				String scgResponseIDkey ="ConsentBean:"+i+"_specimenCollectionGroupLevelResponseID";
				
				tempMap.put(idKey, consent.getId());
				tempMap.put(statementKey,consent.getStatement());
				tempMap.put(responseKey,consentTierResponse.getResponse());
				tempMap.put(participantResponceIdKey, consentTierResponse.getId());
				tempMap.put(scgResponsekey, consentTierResponse.getResponse());
				tempMap.put(scgResponseIDkey,null);
				i++;
			}
		}
		return tempMap;
	}	

	/**
	 * This function will return CollectionProtocolRegistration object 
	 * @param scg_id Selected SpecimenCollectionGroup ID
	 * @return collectionProtocolRegistration
	 */
	private SpecimenCollectionGroup getSCGObj(String scg_id) throws DAOException
	{
		SpecimenCollectionGroupBizLogic specimenCollectionBizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
		String colName = "id";			
		List getSCGIdFromDB = specimenCollectionBizLogic.retrieve(SpecimenCollectionGroup.class.getName(), colName, scg_id);		
		SpecimenCollectionGroup specimenCollectionGroupObject = (SpecimenCollectionGroup)getSCGIdFromDB.get(0);
		return specimenCollectionGroupObject;
	}
	//Consent Tracking Virender Mehta
	
	/**
	 * This function is used for retriving specimen from Specimen collection group Object
	 * @param specimenObj
	 * @param finalDataList
	 */
	private void getSpecimenDetails(SpecimenCollectionGroup specimenCollectionGroupObj, List finalDataList)
	{
		Collection specimen = specimenCollectionGroupObj.getSpecimenCollection();
		Iterator specimenIterator = specimen.iterator();
		while(specimenIterator.hasNext())
		{
			Specimen specimenObj =(Specimen)specimenIterator.next(); 
			getDetailsOfSpecimen(specimenObj, finalDataList);
		}		
	}
	/**
	 * This function is used for retriving specimen and sub specimen's attributes.
	 * @param specimenObj
	 * @param finalDataList
	 */
	private void getDetailsOfSpecimen(Specimen specimenObj, List finalDataList)
	{
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
