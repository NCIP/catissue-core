/*
 * Created on Aug 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.util.logger.Logger;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SpreadsheetViewAction extends BaseAction
{

	/**
	 * This method get call for simple search as well as advanced search.
	 * This method also get call when user clicks on page no of Pagination tag 
	 * from simple search result page as well as advanced search result page.
	 * Each time it calculates the paginationList to be displayed by grid control
	 * by getting pageNum from request object.
	 * @Override   
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{ 
		/**
		 * Name: Deepti
		 * Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation.
		 * object of class QuerySessionData will be saved session, which will contain the required information for query execution while navigating through query result pages.
		 * 
		 *  Here, extending this class from BaseAction  
		 */
		HttpSession session = request.getSession();
    	//changes are done for check all 
    	String checkAllPages ="";
    	String ch = (String)request.getParameter(Constants.CHECK_ALL_PAGES);
     	if(ch == null || ch.equals(""))
    	{
    		checkAllPages = (String)session.getAttribute(Constants.CHECK_ALL_PAGES);
    	}
    	else
    	{
    		checkAllPages = ch;
    	}
    	session.setAttribute(Constants.CHECK_ALL_PAGES, checkAllPages);
     	String isAjax = (String)request.getParameter("isAjax");
     	if(isAjax != null && isAjax.equals("true"))
     	{
     		response.setContentType("text/html");
			response.getWriter().write(checkAllPages);
			return null;
     	}
		QuerySessionData querySessionData = (QuerySessionData) session
				.getAttribute(edu.wustl.common.util.global.Constants.QUERY_SESSION_DATA);

		String pageOf = (String) request.getAttribute(Constants.PAGEOF);
		if (pageOf == null)
		{
			pageOf = (String) request.getParameter(Constants.PAGEOF);
		}
		if (request.getAttribute(Constants.IDENTIFIER_FIELD_INDEX) == null)
		{
			String identifierFieldIndex = request.getParameter(Constants.IDENTIFIER_FIELD_INDEX);
			if (identifierFieldIndex != null && !identifierFieldIndex.equals(""))
			{
				request.setAttribute(Constants.IDENTIFIER_FIELD_INDEX, new Integer(
						identifierFieldIndex));
			}
		}
		Logger.out.debug("Pageof in spreadsheetviewaction.........:" + pageOf);
		Object defaultViewAttribute = request.getAttribute(Constants.SPECIMENT_VIEW_ATTRIBUTE);
		if (defaultViewAttribute != null)// When the Default specimen view Check box is checked or unchecked, this will be evaluated.
		{
			List list = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
			List columnNames = (List) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
//			edu.wustl.catissuecore.util.global.AppUtility.setGridData( list,columnNames, request);
			session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnNames);
			request.setAttribute(Constants.SPREADSHEET_DATA_LIST, list);
		}
		List list = null;
		String pagination = request.getParameter("isPaging");
		if (pagination == null || pagination.equals("false"))
		{

			list = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
			List columnNames = (List) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);

			//Set the SPREADSHEET_DATA_LIST and SPREADSHEET_COLUMN_LIST in the session.
			//Next time when user clicks on pages of pagination Tag, it get the same list from the session
			//and based on current page no, it will calculate paginationDataList to be displayed by grid control.
			session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnNames);
			request.setAttribute(Constants.PAGINATION_DATA_LIST, list);
			AppUtility.setGridData( list,columnNames, request);
			session.setAttribute(Constants.TOTAL_RESULTS, querySessionData
					.getTotalNumberOfRecords());
			AdvanceSearchForm advanceSearchForm = (AdvanceSearchForm) request
					.getAttribute("advanceSearchForm");
			if (advanceSearchForm != null)
			{
				session.setAttribute("advanceSearchForm", advanceSearchForm);
			}
		}

		int pageNum = Constants.START_PAGE;
		System.out.println(pageNum);
		String recordsPerPageStr = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);//Integer.parseInt(XMLPropertyHandler.getValue(Constants.NO_OF_RECORDS_PER_PAGE));
		List paginationDataList = null, columnList = null;

		//Get the SPREADSHEET_DATA_LIST and SPREADSHEET_COLUMN_LIST from the session.
		columnList = (List) session.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);

		if (request.getParameter(Constants.PAGE_NUMBER) != null)
		{
			pageNum = Integer.parseInt(request.getParameter(Constants.PAGE_NUMBER));
		}
		else if (session.getAttribute(Constants.PAGE_NUMBER) != null)
		{
			pageNum = Integer.parseInt(session.getAttribute(Constants.PAGE_NUMBER).toString());
		}
		if (request.getParameter(Constants.RESULTS_PER_PAGE) != null)
		{
			recordsPerPageStr = request.getParameter(Constants.RESULTS_PER_PAGE);
		}
		else if (request.getAttribute(Constants.RESULTS_PER_PAGE) != null)
		{
			recordsPerPageStr = request.getAttribute(Constants.RESULTS_PER_PAGE).toString();
		}
		int recordsPerPage = new Integer(recordsPerPageStr);
		if (pagination != null && pagination.equalsIgnoreCase("true"))
		{
			paginationDataList = AppUtility.getPaginationDataList(request, getSessionData(request), recordsPerPage, pageNum, querySessionData);
			request.setAttribute(Constants.PAGINATION_DATA_LIST,paginationDataList);
			AppUtility.setGridData(paginationDataList,columnList, request);
		}

		/* if(pagination != null && pagination.equalsIgnoreCase("true"))
		 {
		 request.setAttribute(Constants.PAGINATION_DATA_LIST,paginationDataList);
		 }*/
		
		request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnList);
		request.setAttribute(Constants.PAGE_NUMBER, Integer.toString(pageNum));

		session.setAttribute(Constants.RESULTS_PER_PAGE, recordsPerPage + "");
		//session.setAttribute(Constants.RESULTS_PER_PAGE,recordsPerPage);
		//Set the result per page attribute in the request to be uesd by pagination Tag.
		//      Prafull:Commented this can be retrived directly from constants on jsp, so no need to save it in request.
		//        request.setAttribute(Constants.RESULTS_PER_PAGE,Integer.toString(Constants.NUMBER_RESULTS_PER_PAGE_SEARCH));
		request.setAttribute(Constants.PAGEOF, pageOf);
		return mapping.findForward(pageOf);
	}

}
