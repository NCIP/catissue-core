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

import java.util.ArrayList;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;



import edu.wustl.catissuecore.actionForm.ConflictCommonForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ReportLoaderQueueBizLogic;
import edu.wustl.catissuecore.bizlogic.SiteBizLogic;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;

import edu.wustl.catissuecore.reportloader.HL7ParserUtil;
import edu.wustl.catissuecore.reportloader.ReportLoaderUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;

public class ConflictCommonAction extends BaseAction{
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
	
	HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		ConflictCommonForm conflictCommonForm = (ConflictCommonForm)form;
		String reportQueueId = (String) request.getParameter(Constants.REPORT_ID);
		
		conflictCommonForm.setSurgicalPathologyNumber((String)request.getParameter(Constants.SURGICAL_PATHOLOGY_NUMBER));
		conflictCommonForm.setReportDate((String)request.getParameter(Constants.REPORT_DATE));
		conflictCommonForm.setSiteName((String)request.getParameter(Constants.SITE_NAME));
	
		Date reportCreationDate=(Date)retrieveReportCreationDate(reportQueueId);
		String dateOfCreation=Utility.parseDateToString(reportCreationDate, Constants.DATE_PATTERN_MM_DD_YYYY);
		conflictCommonForm.setReportcreationDate(dateOfCreation);
	
		Participant participant = (Participant) retrieveReportParticipant(reportQueueId);
		String participantName = (String)participant.getLastName()+","+ (String)participant.getFirstName();
		String birthDate = Utility.parseDateToString(participant.getBirthDate(), Constants.DATE_PATTERN_MM_DD_YYYY);
		conflictCommonForm.setParticipantName(participantName);
		conflictCommonForm.setBirthDate(birthDate);
		conflictCommonForm.setSocialSecurityNumber(participant.getSocialSecurityNumber());
		
		return mapping.findForward(Constants.SUCCESS);
	}
	
	/**
	 * To retrieve the reportQueueList
	 * @param reportQueueId
	 * @return
	 * @throws DAOException
	 */
	private List getReportQueueDataList(String reportQueueId) throws DAOException
	{
		
		ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic)BizLogicFactory.getInstance().getBizLogic(ReportLoaderQueue.class.getName());
	    List reportQueueList = (List)reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class.getName(),Constants.SYSTEM_IDENTIFIER, reportQueueId);
		return reportQueueList;		
	}
	
	/**
	 * To retrieve the participant present in the report
	 * @param reportQueueId
	 * @return
	 * @throws Exception
	 * 
	 */
	private Participant retrieveReportParticipant(String reportQueueId) throws Exception
	{
		Participant participant = null;
		
		List reportQueueDataList =  new ArrayList();
		Site site =null;
		ReportLoaderQueue reportLoaderQueue =null;
		reportQueueDataList = getReportQueueDataList(reportQueueId);
		if((reportQueueDataList!=null) && (reportQueueDataList).size()>0)
		{
			reportLoaderQueue = (ReportLoaderQueue)reportQueueDataList.get(0);
		}
		//retrieve site
		String siteName = reportLoaderQueue.getSiteName();
		SiteBizLogic siteBizLogic = (SiteBizLogic)BizLogicFactory.getInstance().getBizLogic(Site.class.getName());
		List siteList = (List)siteBizLogic.retrieve(Site.class.getName(),Constants.SYSTEM_NAME, siteName);
		
		
		if((siteList!=null) && siteList.size()>0)
		{
			site = (Site)siteList.get(0);
		}
		
		
		//retrive the PID		
		String pidLine = ReportLoaderUtil.getLineFromReport(reportLoaderQueue.getReportText(), CaTIESConstants.PID);
		
		//Participant Object		
		 participant = HL7ParserUtil.parserParticipantInformation(pidLine,site);
			 
		return participant;
	}
	
	private Date retrieveReportCreationDate(String reportQueueId) throws DAOException
	{
		Date reportCreationDate=null;
		List reportQueueDataList =  new ArrayList();
		ReportLoaderQueue reportLoaderQueue =null;
		reportQueueDataList = getReportQueueDataList(reportQueueId);
		if((reportQueueDataList!=null) && (reportQueueDataList).size()>0)
		{
			reportLoaderQueue = (ReportLoaderQueue)reportQueueDataList.get(0);
		}
		
//		retrive the OBR	
		String OBRLine = ReportLoaderUtil.getLineFromReport(reportLoaderQueue.getReportText(), CaTIESConstants.OBR);
		
		//retrieve IdentifiedSurgicalPathologyReportObject
		IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReportObject = null;
//		identifiedSurgicalPathologyReportObject = HL7ParserUtil.extractOBRSegment(OBRLine);
		reportCreationDate =(Date)identifiedSurgicalPathologyReportObject.getCollectionDateTime();
		
		return reportCreationDate;
	}

}
