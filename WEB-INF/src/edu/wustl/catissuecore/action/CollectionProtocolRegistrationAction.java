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
import java.util.HashMap;
import java.util.List;
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
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.AddNewSessionDataBean;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields in the User Add/Edit webpage.
 * @author ajay_sharma
 */

public class CollectionProtocolRegistrationAction extends SecureAction
{
	/**
	 * Overrides the execute method of Action class.
	 * Sets the various fields in Participant Registration Add/Edit webpage.
	 * */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);

		//Sets the operation attribute to be used in the Add/Edit User Page. 
		request.setAttribute(Constants.OPERATION, operation);
        if(operation.equalsIgnoreCase(Constants.ADD ) )
        {
        	((CollectionProtocolRegistrationForm)form).setId(0);
        }

		//Sets the pageOf attribute
        String pageOf  = request.getParameter(Constants.PAGEOF);
        
        request.setAttribute(Constants.PAGEOF,pageOf);
        
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
}