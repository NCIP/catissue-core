/**
 * <p>
 * Title: ConflictViewAction Class>
 * <p>
 * Description: Initialization action for conflict view Copyright: Copyright (c)
 * year Company: Washington University, School of Medicine, St. Louis.
 *
 * @version 1.00
 * @author kalpana Thakur Created on sep 18,2007
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConflictViewForm;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.PagenatedResultData;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.QuerySessionData;

/**
 * @author renuka_bajpai
 */
public class ConflictViewAction extends SecureAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ConflictViewForm conflictViewForm = (ConflictViewForm) form;
		int selectedFilter = Integer.parseInt(conflictViewForm.getSelectedFilter());

		// Added by Ravindra to disallow Non Super Admin users to view
		// Conflicting Reports
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
		if (!sessionDataBean.isAdmin())
		{
			ActionErrors errors = new ActionErrors();
			ActionError error = new ActionError("access.execute.action.denied");
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			saveErrors(request, errors);

			return mapping.findForward(Constants.ACCESS_DENIED);
		}
		String[] retrieveFilterList = Constants.CONFLICT_FILTER_LIST;
		List filterList = new ArrayList();
		for (int i = 0; i < retrieveFilterList.length; i++)
		{
			filterList.add(0, new NameValueBean(retrieveFilterList[i], i));
		}
		Collections.sort(filterList);
		// Setting the list in request
		request.getSession().setAttribute(Constants.FILTER_LIST, filterList);

		// Returns the page number to be shown.
		int pageNum = Integer.parseInt(request.getParameter(Constants.PAGE_NUMBER));

		// Gets the session of this request.
		HttpSession session = request.getSession();

		// The start index in the list of users to be approved/rejected.
		// int startIndex = Constants.ZERO;
		String sqlString = "";

		if (selectedFilter == 0)
		{
			sqlString = "select PARTICIPANT_NAME ,IDENTIFIER ,SURGICAL_PATHOLOGY_NUMBER," +
					"REPORT_LOADED_DATE,STATUS ,SITE_NAME ," +
					"REPORT_COLLECTION_DATE from catissue_report_queue" +
					" where status='PARTICIPANT_CONFLICT' or status='SCG_PARTIAL_CONFLICT'" +
					" or status='SCG_CONFLICT'";

		}
		else
		{ // retrieving only the participant conflicts
			if (selectedFilter == 1)
			{
				sqlString = "select PARTICIPANT_NAME ," +
						"IDENTIFIER ,SURGICAL_PATHOLOGY_NUMBER," +
						"REPORT_LOADED_DATE,STATUS ,SITE_NAME,REPORT_COLLECTION_DATE" +
						" from catissue_report_queue where status='PARTICIPANT_CONFLICT'";
			}
			else
			{ // retrieving all the scg conflicts both partial and exact match
				if (selectedFilter == 2)
				{
					sqlString = "select PARTICIPANT_NAME ,IDENTIFIER ," +
							"SURGICAL_PATHOLOGY_NUMBER,REPORT_LOADED_DATE,STATUS ," +
							"SITE_NAME,REPORT_COLLECTION_DATE from" +
							" catissue_report_queue where" +
							" status='SCG_PARTIAL_CONFLICT' or status='SCG_CONFLICT'";
				}

			}
		}

		int recordsPerPage;
		String recordsPerPageSessionValue = (String) session
				.getAttribute(Constants.RESULTS_PER_PAGE);
		if (recordsPerPageSessionValue == null)
		{
			recordsPerPage = Integer.parseInt(XMLPropertyHandler
					.getValue(Constants.RECORDS_PER_PAGE_PROPERTY_NAME));
			session.setAttribute(Constants.RESULTS_PER_PAGE, recordsPerPage + "");
		}
		else
			recordsPerPage = new Integer(recordsPerPageSessionValue).intValue();

		PagenatedResultData pagenatedResultData = null;
		pagenatedResultData = AppUtility.executeForPagination(sqlString, getSessionData(request),
				false, null, false, 0, recordsPerPage);

		QuerySessionData querySessionData = new QuerySessionData();
		querySessionData.setSql(sqlString);
		querySessionData.setQueryResultObjectDataMap(null);
		querySessionData.setSecureExecute(false);
		querySessionData.setHasConditionOnIdentifiedField(false);
		querySessionData.setRecordsPerPage(recordsPerPage);
		querySessionData.setTotalNumberOfRecords(pagenatedResultData.getTotalRecords());
		session.setAttribute(Constants.QUERY_SESSION_DATA, querySessionData);

		String[] retrieveColumnList = Constants.CONFLICT_LIST_HEADER;
		List columnList = new ArrayList();
		for (int i = 0; i < retrieveColumnList.length; i++)
		{
			columnList.add(retrieveColumnList[i]);
		}

		// List of results the query will return on execution.
		List list = pagenatedResultData.getResult();

		// request.setAttribute(Constants.SPREADSHEET_DATA_LIST, list);
		// request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnNames);

		// Saves the page number in the request.
		request.setAttribute(Constants.PAGE_NUMBER, Integer.toString(pageNum));

		// Saves the total number of results in the request.
		session.setAttribute(Constants.TOTAL_RESULTS, Integer.toString(pagenatedResultData
				.getTotalRecords()));

		session.setAttribute(Constants.RESULTS_PER_PAGE, recordsPerPage + "");

		List dataList = makeGridData(list);
		AppUtility.setGridData(dataList, columnList, request);
		Integer identifierFieldIndex = new Integer(1);
		request.setAttribute("identifierFieldIndex", identifierFieldIndex.intValue());
		request.setAttribute("pageOf", "pageOfConflictResolver");
		request.getSession().setAttribute(Constants.SELECTED_FILTER,
				Integer.toString(selectedFilter));
		request.setAttribute(Constants.PAGINATION_DATA_LIST, dataList);
		request.getSession().setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnList);
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * To prepare the grid to display on conflictView.jsp.
	 *
	 * @param reportQueueDataList : reportQueueDataList
	 * @return List : List
	 */
	private List makeGridData(List reportQueueDataList)
	{
		Iterator iter = reportQueueDataList.iterator();
		List gridData = new ArrayList();

		while (iter.hasNext())
		{
			List rowData = new ArrayList();

			List reportDataList = new ArrayList();
			reportDataList = (ArrayList) iter.next();
			rowData.add((String) reportDataList.get(0));
			rowData.add((String) reportDataList.get(1));
			rowData.add((String) reportDataList.get(2));
			rowData.add((String) reportDataList.get(3));
			rowData.add((String) reportDataList.get(4));
			rowData.add((String) reportDataList.get(5));
			rowData.add((String) reportDataList.get(6));
			gridData.add(rowData);
		}

		return gridData;
	}
}
