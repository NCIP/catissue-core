/**
 * <p>Title: ConflictDetailsAction Class>
 * <p>Description:	Initialization action for conflict details view
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Feb 27,2007
 */
package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConflictDetailsForm;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.reportloader.HL7Parser;
import edu.wustl.catissuecore.reportloader.Parser;
import edu.wustl.catissuecore.reportloader.ReportLoaderUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;


/**
 * @author ashish_gupta
 *
 */
public class ConflictDetailsAction extends BaseAction
{
	/**
	 * Overrides the execute method of Action class.
	 * Initializes the various fields in ConflictView.jsp Page.
	 * @param mapping object
	 * @param form object
	 * @param request object
	 * @param response object
	 * @return ActionForward object
	 * @throws Exception object
	 * */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String reportQueueId = request.getParameter(Constants.REPORT_ID);
		ConflictDetailsForm conflictDetailsForm = (ConflictDetailsForm)form;
		conflictDetailsForm.setReportQueueId(reportQueueId);
		
		List reportLoaderQueueList = ReportLoaderUtil.getObject(ReportLoaderQueue.class.getName(),Constants.SYSTEM_IDENTIFIER, reportQueueId);
		//Report Queue Object
		ReportLoaderQueue reportLoaderQueue = (ReportLoaderQueue)reportLoaderQueueList.get(0);		
		String pidLine = ReportLoaderUtil.getLineFromReport(reportLoaderQueue.getReportText(), Parser.PID);
		//Participant Object		
		Participant participant = HL7Parser.parserParticipantInformation(pidLine);
		Site site = HL7Parser.parseSiteInformation(pidLine);
		//TODO find alternative for this method 
		//participant = HL7Parser.setSiteToParticipant(participant, site);
		
		HttpSession session = request.getSession();
		session.setAttribute(Constants.REPORT_PARTICIPANT_OBJECT, participant);		
		//Retrieving all conflicting participants
		Collection conflictingParticipantColl = reportLoaderQueue.getParticipantCollection();
		
		return mapping.findForward(Constants.SUCCESS);
	}
}
