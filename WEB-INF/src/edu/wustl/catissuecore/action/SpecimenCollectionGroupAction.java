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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;


/**
 * SpecimenCollectionGroupAction initializes the fields in the 
 * New Specimen Collection Group page.
 * @author ajay_sharma
 */
public class SpecimenCollectionGroupAction extends Action
{
    
    /**
     * Overrides the execute method of Action class.
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	SpecimenCollectionGroupForm  specimenCollectionGroupForm = (SpecimenCollectionGroupForm)form;
    	
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit SpecimenCollectionGroup Page. 
        request.setAttribute(Constants.OPERATION, operation);

		// populating clinical Status field from constant array
		//request.setAttribute(Constants.CLINICAL_STATUS_LIST,Constants.CLINICAL_STATUS_ARRAY);
        List clinicalStatusList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_CLINICAL_STATUS);
    	request.setAttribute(Constants.CLINICAL_STATUS_LIST, clinicalStatusList);

		try
		{
			// get list of Protocol title.
			AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);

		    //populating protocolist bean.
			String sourceObjectName = CollectionProtocol.class.getName();
			String [] displayNameFields = {"title"};
			String valueField = "systemIdentifier";
		  	List list = bizLogic.getList(sourceObjectName,displayNameFields,valueField);
			request.setAttribute(Constants.PROTOCOL_LIST, list);
		
           	//Populating the Site Type bean
		   	sourceObjectName = Site.class.getName();
		   	String siteDisplaySiteFields[] = {"name"};
		   	list = bizLogic.getList(sourceObjectName,siteDisplaySiteFields,valueField);
		   	request.setAttribute(Constants.SITELIST, list);

		   	//Populating the participants registered to a given protocol
			loadPaticipants(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
			
			//Populating the protocol participants id registered to a given protocol
			loadPaticipantNumberList(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
			
			//Populating the Collection Protocol Events
			loadCollectionProtocolEvent(specimenCollectionGroupForm.getCollectionProtocolId(),bizLogic,request);
			
			//Populating the participants Medical Identifier for a given participant
			loadParticipantMedicalIdentifier(specimenCollectionGroupForm.getParticipantId(),bizLogic, request);
			
			//Load Clinical status for a given study calander event point
			List calendarEventPointList = bizLogic.retrieve(CollectionProtocolEvent.class.getName(),
											"systemIdentifier",
											new Long(specimenCollectionGroupForm.getCollectionProtocolEventId()));
			if(!calendarEventPointList.isEmpty())
			{
				CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)calendarEventPointList.get(0);
				specimenCollectionGroupForm.setClinicalStatus(collectionProtocolEvent.getClinicalStatus());
			}
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
        return mapping.findForward(Constants.SUCCESS);
    }
    
    
	private void loadPaticipants(long protocolID, AbstractBizLogic bizLogic, HttpServletRequest request) throws Exception
	{
		//get list of Participant's names
		String sourceObjectName = CollectionProtocolRegistration.class.getName();
	  	String [] displayParticipantFields = {"participant.lastName","participant.firstName"};
	  	String valueField = "participant.systemIdentifier";
	  	String whereColumnName[] = {"collectionProtocol.systemIdentifier"};
	  	String whereColumnCondition[] = {"="};
	  	Object[] whereColumnValue = {new Long(protocolID)};
	  	String joinCondition = Constants.AND_JOIN_CONDITION;
	  	String separatorBetweenFields = ", ";
	  	
	  	List list = bizLogic.getList(sourceObjectName, displayParticipantFields, valueField, whereColumnName,
				  whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields);
	
	  	request.setAttribute(Constants.PARTICIPANT_LIST, list);
	}
    
    
	private void loadPaticipantNumberList(long protocolID, AbstractBizLogic bizLogic, HttpServletRequest request) throws Exception
	{
		//get list of Participant's names
		String sourceObjectName = CollectionProtocolRegistration.class.getName();
		String displayParticipantNumberFields[] = {"protocolParticipantIdentifier"};
		String valueField = "protocolParticipantIdentifier";
		String whereColumnName[] = {"collectionProtocol.systemIdentifier"};
		String whereColumnCondition[] = {"="};
		Object[] whereColumnValue = {new Long(protocolID)};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		String separatorBetweenFields = "";
			
		List list = bizLogic.getList(sourceObjectName, displayParticipantNumberFields, valueField, whereColumnName,
					whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields);

		request.setAttribute(Constants.PROTOCOL_PARTICIPANT_NUMBER_LIST, list);
	}    
    
	private void loadCollectionProtocolEvent(long protocolID, AbstractBizLogic bizLogic, HttpServletRequest request) throws Exception
	{
		String sourceObjectName = CollectionProtocolEvent.class.getName();
		String displayEventFields[] = {"studyCalendarEventPoint"};
		String valueField = "systemIdentifier";
		String whereColumnName[] = {"collectionProtocol.systemIdentifier"};
		String whereColumnCondition[] = {"="};
		Object[] whereColumnValue = {new Long(protocolID)};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		String separatorBetweenFields = "";
					
		List list = bizLogic.getList(sourceObjectName, displayEventFields, valueField, whereColumnName,
					whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields);
		
		request.setAttribute(Constants.STUDY_CALENDAR_EVENT_POINT_LIST, list);
	}

	private void loadParticipantMedicalIdentifier(long participantID, AbstractBizLogic bizLogic, HttpServletRequest request) throws Exception
	{
		//get list of Participant's names
		String sourceObjectName = ParticipantMedicalIdentifier.class.getName();
		String displayEventFields[] = {"medicalRecordNumber"};
		String valueField = "systemIdentifier";
		String whereColumnName[] = {"participant.systemIdentifier"};
		String whereColumnCondition[] = {"="};
		Object[] whereColumnValue = {new Long(participantID)};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		String separatorBetweenFields = "";
						
		List list = bizLogic.getList(sourceObjectName, displayEventFields, valueField, whereColumnName,
					whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields);
						
		request.setAttribute(Constants.PARTICIPANT_MEDICAL_IDNETIFIER_LIST, list);
	}
}
