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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.SecureAction;
import edu.wustl.dao.exception.DAOException;

public class SubCollectionProtocolRegistrationAction extends SecureAction
{

	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{

		CollectionProtocolRegistrationForm collectionProtocolRegistrationForm = (CollectionProtocolRegistrationForm) form;

		// Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);
		Long collectionProtocolId=null,participantId=null;
		if (request.getParameter(Constants.CP_SEARCH_CP_ID) != null && request.getParameter(Constants.CP_SEARCH_PARTICIPANT_ID) != null)
		{
			collectionProtocolId = new Long(request.getParameter(Constants.CP_SEARCH_CP_ID));
			participantId = new Long(request.getParameter(Constants.CP_SEARCH_PARTICIPANT_ID));
			
			
			setAttributesOfCPInForm(collectionProtocolId, collectionProtocolRegistrationForm);
			setAttributesOfParticipantInForm(participantId, collectionProtocolRegistrationForm);
			operation = chkOperation(participantId, collectionProtocolId, collectionProtocolRegistrationForm);
			request.setAttribute(Constants.CP_SEARCH_CP_ID, request.getParameter(Constants.CP_SEARCH_CP_ID));
			request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID, request.getParameter(Constants.CP_SEARCH_PARTICIPANT_ID));
		}

		request.setAttribute(Constants.OPERATION, operation);
		if (operation.equalsIgnoreCase(Constants.ADD))
		{
			CollectionProtocolRegistrationForm cpform = (CollectionProtocolRegistrationForm) form;
			cpform.setId(0);
			/* setting the PPI of the main COllection protocol Registration if given */
			if(request.getParameter("parentCPId")!= null && participantId != null)
			{
				Long parentCPId = new Long(request.getParameter("parentCPId"));
				setParticipantMedicalIdentifierInForm(parentCPId,participantId,cpform);
				System.out.println("ParentCPId:"+parentCPId);
			}
			if (cpform.getRegistrationDate() == null)
			{

				if (request.getParameter("regDate") != null)
				{
//					Date regDate = Utility.parseDate(request.getParameter(Constants.REG_DATE), Constants.DATE_PATTERN_YYYY_MM_DD);
//					if (regDate != null)
//						cpform.setRegistrationDate(Utility.parseDateToString(regDate, Constants.DATE_PATTERN_MM_DD_YYYY));
					
					
					//bug no:6526 Date  of format yyyy-dd-mm was parsed in format mm-dd-yyyy and then set in CPR.
					//Now the Date received is of format mm--dd-yyyy.So no need to parse it.

					cpform.setRegistrationDate(request.getParameter(Constants.REG_DATE));
				}
				if (cpform.getRegistrationDate() == null || cpform.getRegistrationDate().equals(""))
				{
					cpform.setRegistrationDate(Utility.parseDateToString(Calendar.getInstance().getTime(), Variables.dateFormat));

				}
			}
//			setOffsetInForm(collectionProtocolId,participantId,collectionProtocolRegistrationForm);
		}
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);

		// Sets the pageOf attribute
		String pageOf = request.getParameter(Constants.PAGEOF);

		request.setAttribute(Constants.PAGEOF, "pageOfCollectionProtocolRegistrationCPQuery");

		return mapping.findForward(pageOf);
	}

	private void setAttributesOfCPInForm(Long cpId, CollectionProtocolRegistrationForm form) throws DAOException
	{
		String sourceObjName = CollectionProtocol.class.getName();
		String[] selectColName = {"shortTitle", "studyCalendarEventPoint"};
		String[] whereColName = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColCond = {"="};
		Object[] whereColVal = {cpId};

		CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic) BizLogicFactory.getInstance().getBizLogic(
				Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);

		List list = bizLogic.retrieve(sourceObjName, selectColName, whereColName, whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);

		if (list != null && !list.isEmpty())
		{

			Object[] obj = (Object[]) list.get(0);
			String shortTitle = (String) obj[0];
			Double studyCalEvtPoint = (Double) obj[1];

			form.setCollectionProtocolShortTitle(shortTitle);
			if (studyCalEvtPoint != null)
				form.setStudyCalEvtPoint(studyCalEvtPoint.toString() + " Days");

			form.setCollectionProtocolID(cpId);
		}

	}

	private void setAttributesOfParticipantInForm(Long participantId, CollectionProtocolRegistrationForm form) throws DAOException
	{

		CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic) BizLogicFactory.getInstance().getBizLogic(
				Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);

		form.setParticipantID(participantId.longValue());
		// cprForm.setCheckedButton(true);
		Object object = bizLogic.retrieve(Participant.class.getName(), participantId);
		if (object != null)
		{
			Participant participant = (Participant) object;
			form.setParticipantName(participant.getMessageLabel());
		}

	}

	private String chkOperation(Long participantId, Long cpId, CollectionProtocolRegistrationForm form) throws DAOException
	{
		boolean isParticipantRegToCP = chkParticipantRegToCP(participantId, cpId, form);
		if (isParticipantRegToCP)
			return Constants.EDIT;
		else
			return Constants.ADD;

	}

	/*
	 * private boolean chkParticipantRegToCP(Long participantId, Long
	 * cpId,CollectionProtocolRegistrationForm form) throws DAOException {
	 * CollectionProtocolRegistrationBizLogic bizLogic =
	 * (CollectionProtocolRegistrationBizLogic) BizLogicFactory
	 * .getInstance().getBizLogic(
	 * Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
	 * 
	 * String sourceObjName = CollectionProtocolRegistration.class.getName();
	 * String[] selectColName = { "id","registrationDate" }; String[]
	 * whereColName = { "participant.id", "collectionProtocol.id" }; String[]
	 * whereColCond = { "=", "=" }; Object[] whereColVal = { participantId, cpId };
	 * 
	 * List regList = bizLogic.retrieve(sourceObjName, selectColName,
	 * whereColName, whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);
	 * 
	 * if (regList != null && !regList.isEmpty()) { Object[] obj = (Object[])
	 * regList.get(0); Long id = (Long) obj[0]; Date regDate = (Date) obj[1];
	 * form.setId(id); form.setRegistrationDate(regDate.toString());
	 * form.setRegistrationDate(Utility.parseDateToString(regDate,Constants.DATE_PATTERN_MM_DD_YYYY));
	 * return true; }
	 * 
	 * return false; }
	 */
	/* offset changes 27th dec */
	private boolean chkParticipantRegToCP(Long participantId, Long cpId, CollectionProtocolRegistrationForm form) throws DAOException
	{
		CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic) BizLogicFactory.getInstance().getBizLogic(
				Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);

		String sourceObjName = CollectionProtocolRegistration.class.getName();
		String[] selectColName = {"id", "registrationDate", "offset", "protocolParticipantIdentifier"};
		String[] whereColName = {"participant.id", "collectionProtocol.id"};
		String[] whereColCond = {"=", "="};
		Object[] whereColVal = {participantId, cpId};

		List regList = bizLogic.retrieve(sourceObjName, selectColName, whereColName, whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);

		if (regList != null && !regList.isEmpty())
		{
			Object[] obj = (Object[]) regList.get(0);
			Long id = (Long) obj[0];
			Date regDate = (Date) obj[1];
			if (obj[2] != null)
			{
				int offset = ((Integer) obj[2]).intValue();
				form.setOffset(offset);
			}
			if (obj[3] != null)
			{
				String protocolParticipantIdentifier = (String) obj[3];
				form.setParticipantProtocolID(protocolParticipantIdentifier);
			}
			form.setId(id);
			form.setRegistrationDate(regDate.toString());
			form.setRegistrationDate(Utility.parseDateToString(regDate, Variables.dateFormat));

			return true;
		}

		return false;
	}

	/* offset changes end */

	private void setParticipantMedicalIdentifierInForm(Long parentCPId,Long participantId,CollectionProtocolRegistrationForm form) throws DAOException
	{
		CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic) BizLogicFactory.getInstance().getBizLogic(
				Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);

		String sourceObjName = CollectionProtocolRegistration.class.getName();
		String[] selectColName = {"protocolParticipantIdentifier"};
		String[] whereColName = {"participant.id", "collectionProtocol.id"};
		String[] whereColCond = {"=", "="};
		Object[] whereColVal = {participantId, parentCPId};

		List regList = bizLogic.retrieve(sourceObjName, selectColName, whereColName, whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);

		if (regList != null && !regList.isEmpty())
		{
			String PPI = (String) regList.get(0);
			if(PPI != null)
				form.setParticipantProtocolID(PPI);
		}
	}
	
//	private void setOffsetInForm(Long collectionProtocolId,Long participantId,CollectionProtocolRegistrationForm form) throws DAOException
//	{
//		CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic) BizLogicFactory.getInstance().getBizLogic(
//				Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
//		Long parentCpId = getImmidateParentCpId(collectionProtocolId);
//			
//		if(parentCpId != null)
//		{
//			String sourceObjName = CollectionProtocolRegistration.class.getName();
//			String[] selectColName = {"offset"};
//			String[] whereColName = {"collectionProtocol.id","participant.id"};
//			String[] whereColCond = {"=","="};
//			Object[] whereColVal = {parentCpId,participantId};
//			List offsetList = bizLogic.retrieve(sourceObjName, selectColName, whereColName, whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);
//			if(offsetList != null && !offsetList.isEmpty())
//			{
//				Integer offset = (Integer) offsetList.get(0);
//				if(offset!=null)
//				{
//					form.setOffset(offset.intValue());
//				}
//			}
//		}
//	}

//	private Long getImmidateParentCpId(Long collectionProtocolId) throws DAOException
//	{
//		CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic) BizLogicFactory.getInstance().getBizLogic(
//				Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
//		Long parentCpId = null;
//
//		String sourceObjName = CollectionProtocol.class.getName();
//		String[] selectColName = {"parentCollectionProtocol.id"};
//		String[] whereColName = {"id"};
//		String[] whereColCond = {"="};
//		Object[] whereColVal = {collectionProtocolId};
//
//		List parentCPIdList = bizLogic.retrieve(sourceObjName, selectColName, whereColName, whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);
//		if(parentCPIdList != null && !parentCPIdList.isEmpty())
//			parentCpId = (Long) parentCPIdList.get(0);
//		
//		return parentCpId;
//	}
}