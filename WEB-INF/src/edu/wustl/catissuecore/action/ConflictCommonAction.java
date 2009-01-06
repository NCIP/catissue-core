/**
 * <p>Title: ConflictCommonAction Class>
 * <p>Description:	Conflict Common Action class instantiate the bean for ConflictCommonView.jsp
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 *@author kalpana Thakur
 * Created on sep 18,2007
 * 
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConflictCommonForm;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.Utility;

public class ConflictCommonAction extends BaseAction{
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
	
	HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		ConflictCommonForm conflictCommonForm = (ConflictCommonForm)form;
		String reportQueueId = (String) request.getParameter(Constants.REPORT_ID);
		
		conflictCommonForm.setSurgicalPathologyNumber((String)request.getParameter(Constants.SURGICAL_PATHOLOGY_NUMBER));
		conflictCommonForm.setReportDate((String)request.getParameter(Constants.REPORT_DATE));
		conflictCommonForm.setSiteName((String)request.getParameter(Constants.SITE_NAME));
		conflictCommonForm.setReportCollectionDate((String)request.getParameter(Constants.REPORT_COLLECTION_DATE));
	
		Participant participant = (Participant) edu.wustl.catissuecore.caties.util.Utility.getParticipantFromReportLoaderQueue(reportQueueId);
		String participantName = (String)participant.getLastName()+","+ (String)participant.getFirstName();
		String birthDate = Utility.parseDateToString(participant.getBirthDate(), edu.wustl.common.util.global.Variables.dateFormat);
		conflictCommonForm.setParticipantName(participantName);
		conflictCommonForm.setBirthDate(birthDate);
		conflictCommonForm.setSocialSecurityNumber(participant.getSocialSecurityNumber());
		
		return mapping.findForward(Constants.SUCCESS);
	}
}
