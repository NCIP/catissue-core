
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.XMLPropertyHandler;

/**
 * <p>Title: ReportReviewQuarantineAction Class>
 * <p>Description:	This class fetch the list from the database where STSTUS="Pending".</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Virender Mehta
 * @version 1.00
 * Created on Feb 02,2007
 */
public class ReportReviewQuarantineAction extends BaseAction
{

	/**
	 * Overrides the execute method in Action class.
	 * @param mapping ActionMapping object
	 * @param form ActionForm object
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @return ActionForward object
	 * @throws Exception object
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
		String reportAction = request.getParameter(Constants.REPORT_ACTION);
		String pageOf = (String) request.getParameter(Constants.PAGE_OF);
		if (reportAction == null)
		{
			reportAction = (String) request.getAttribute(Constants.REPORT_ACTION);
		}
		List reportStatusList = getReportStatus(Constants.COMMENT_STATUS_RENDING, reportAction);
		if (reportStatusList == null || reportStatusList.size() == 0)
		{
			return mapping.findForward(Constants.NO_PENDING_REQUEST);
		}
		List finalDataList = null;
		if (reportStatusList.size() > 0)
		{
			finalDataList = new ArrayList();
		}
		for (int iCount = 0; iCount < reportStatusList.size(); iCount++)
		{
			String witnessFullName = null;
			List dataList = new ArrayList();
			AbstractDomainObject reportObject = null;
			SurgicalPathologyReport surgicalPathologyReport = null;
			String scgName = null;
			//Long reportId = null;
			User user = null;
			Date timestamp = new Date();
			if (reportAction.equalsIgnoreCase(Constants.REVIEW))
			{
				reportObject = (PathologyReportReviewParameter) reportStatusList.get(iCount);
				timestamp = ((PathologyReportReviewParameter) reportObject).getTimestamp();
				surgicalPathologyReport = (SurgicalPathologyReport) defaultBizLogic
						.retrieveAttribute(PathologyReportReviewParameter.class.getName(),
								reportObject.getId(), "surgicalPathologyReport");
				user = (User) defaultBizLogic.retrieveAttribute(
						PathologyReportReviewParameter.class.getName(), reportObject.getId(),
						"user");
			}
			else
			{
				reportObject = (QuarantineEventParameter) reportStatusList.get(iCount);
				timestamp = ((QuarantineEventParameter) reportObject).getTimestamp();
				surgicalPathologyReport = (SurgicalPathologyReport) ((QuarantineEventParameter) reportObject)
						.getDeIdentifiedSurgicalPathologyReport();
				user = (User) defaultBizLogic.retrieveAttribute(QuarantineEventParameter.class
						.getName(), reportObject.getId(), "user");
			}
			if (surgicalPathologyReport instanceof DeidentifiedSurgicalPathologyReport)
			{
				DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport = (DeidentifiedSurgicalPathologyReport) surgicalPathologyReport;
				scgName = (String) defaultBizLogic
						.retrieveAttribute(DeidentifiedSurgicalPathologyReport.class.getName(),
								deidentifiedSurgicalPathologyReport.getId(),
								"specimenCollectionGroup.name");

			}
			else
			{
				IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport = (IdentifiedSurgicalPathologyReport) surgicalPathologyReport;
				scgName = (String) defaultBizLogic.retrieveAttribute(
						IdentifiedSurgicalPathologyReport.class.getName(),
						identifiedSurgicalPathologyReport.getId(), "specimenCollectionGroup.name");

			}

			witnessFullName = user.getLastName() + ", " + user.getFirstName();
			dataList.add(iCount + 1);
			dataList.add(timestamp);
			dataList.add(witnessFullName);
			dataList.add(scgName);
			dataList.add(reportObject.getId());
			//dataList.add(surgicalPathologyReport.getAccessionNumber());
			Site reportSource = (Site) defaultBizLogic.retrieveAttribute(
					SurgicalPathologyReport.class.getName(), surgicalPathologyReport.getId(),
					"reportSource");
			dataList.add(reportSource.getName());
			finalDataList.add(dataList);
		}
		List columnList = columnNames();
		request.setAttribute(Constants.REPORT_STATUS_LIST, finalDataList);
		//request.setAttribute(Constants.COLUMN_LIST,columnList);
		request.setAttribute("pageOf", pageOf);
		//For Pagenation	
		//Gets the session of this request.
		List list = null, showList = null;
		//Returns the page number to be shown.
		int pageNum = Integer.parseInt(request.getParameter(Constants.PAGE_NUMBER));

		//Gets the session of this request.
		HttpSession session = request.getSession();

		//The start index in the list of users to be approved/rejected.
		int startIndex = Constants.ZERO;
		//The end index in the list of users to be approved/rejected.
		int recordsPerPage = Integer.parseInt(XMLPropertyHandler
				.getValue(Constants.NO_OF_RECORDS_PER_PAGE));
		if (request.getParameter(Constants.RESULTS_PER_PAGE) != null)
		{
			recordsPerPage = Integer.parseInt(request.getParameter(Constants.RESULTS_PER_PAGE));
		}
		else if (session.getAttribute(Constants.RESULTS_PER_PAGE) != null)
		{
			recordsPerPage = Integer.parseInt(session.getAttribute(Constants.RESULTS_PER_PAGE)
					.toString());
		}
		int endIndex = recordsPerPage;
		if (pageNum == Constants.START_PAGE)
		{
			//If start page is to be shown retrieve the list from the database.
			list = finalDataList;
			if (recordsPerPage > list.size())
			{
				endIndex = list.size();
			}

			//Save the list of users in the sesson.
			session.setAttribute(Constants.ORIGINAL_DOMAIN_OBJECT_LIST, list);
		}
		else
		{
			//Get the list of users from the session.
			list = (List) session.getAttribute(Constants.ORIGINAL_DOMAIN_OBJECT_LIST);
			if (recordsPerPage != Integer.MAX_VALUE)
			{
				//Set the start index of the users in the list.
				startIndex = (pageNum - 1) * recordsPerPage;

				//Set the end index of the users in the list.
				endIndex = startIndex + recordsPerPage;

				if (endIndex > list.size())
				{
					endIndex = list.size();
				}
			}
			else
			{
				startIndex = 0;
				endIndex = list.size();
			}
		}

		//Gets the list of users to be shown on the page.
		showList = list.subList(startIndex, endIndex);

		//Saves the list of users to be shown on the page in the request.
		//request.setAttribute(Constants.SHOW_DOMAIN_OBJECT_LIST,showList);
		AppUtility.setGridData(showList, columnList, request);
		Integer identifierFieldIndex = new Integer(4);
		request.setAttribute("identifierFieldIndex", identifierFieldIndex.intValue());
		//Saves the page number in the request.
		request.setAttribute(Constants.PAGE_NUMBER, Integer.toString(pageNum));

		//Saves the total number of results in the request. 
		session.setAttribute(Constants.TOTAL_RESULTS, Integer.toString(list.size()));

		session.setAttribute(Constants.RESULTS_PER_PAGE, recordsPerPage + "");
		//Saves the number of results per page in the request.
		//Prafull:Commented this can be retrived directly from constants on jsp, so no need to save it in request.
		//        request.setAttribute(Constants.RESULTS_PER_PAGE,Integer.toString(Constants.NUMBER_RESULTS_PER_PAGE));

		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * Adding name,value pair in NameValueBean for Witness Name
	 * @param collProtId Get Witness List for this ID
	 * @return consentWitnessList
	 * @throws BizLogicException 
	 */
	public List getReportStatus(String reportStatus, String reportAction) throws BizLogicException
	{
		IBizLogic bizLogic = null;
		List pendingStatusList = null;
		String colName = Constants.STATUS;
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		if (reportAction.equalsIgnoreCase(Constants.REVIEW))
		{
			bizLogic = factory.getBizLogic(Constants.PATHOLOGY_REPORT_REVIEW_FORM_ID);
			pendingStatusList = bizLogic.retrieve(PathologyReportReviewParameter.class.getName(),
					colName, reportStatus);
		}
		else
		{
			bizLogic = factory.getBizLogic(Constants.QUARANTINE_EVENT_PARAMETER_FORM_ID);
			pendingStatusList = bizLogic.retrieve(QuarantineEventParameter.class.getName(),
					colName, reportStatus);
		}
		return pendingStatusList;
	}

	/**
	 * This function adds the columns to the List
	 * @return columnList 
	 */
	public List columnNames()
	{
		List columnList = new ArrayList();
		columnList.add(Constants.IDENTIFIER_NO);
		columnList.add(Constants.REQUEST_DATE);
		columnList.add(Constants.USER_NAME_ADMIN_VIEW);
		columnList.add(Constants.SCG_NAME);
		columnList.add(Constants.IDENTIFIER);
		columnList.add(Constants.ACCESSION_NO);
		columnList.add(Constants.SITE);
		return columnList;
	}
}
