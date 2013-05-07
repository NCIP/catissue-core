/**
 * <p>
 * Title: SpreadsheetExportAction Class>
 * <p>
 * Description: This class exports the data of a spreadsheet to a file.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Aniruddha Phadnis
 * @version 1.00 Created on Oct 24, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.QuerySessionData;
import edu.wustl.common.util.logger.Logger;

/**
 * @author aniruddha_phadnis
 */
public class SpreadsheetExportAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(SpreadsheetExportAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response : response obj
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final AdvanceSearchForm searchForm = (AdvanceSearchForm) form;
		final HttpSession session = request.getSession();
		final String path = CommonServiceLocator.getInstance().getAppHome()
				+ System.getProperty("file.separator");
		final String csvfileName = path + Constants.SEARCH_RESULT;
		final String zipFileName = path + session.getId() + Constants.ZIP_FILE_EXTENTION;
		final String fileName = path + session.getId() + Constants.CSV_FILE_EXTENTION;
		final String isCheckAllAcrossAllChecked = request
				.getParameter(Constants.CHECK_ALL_ACROSS_ALL_PAGES);

		// Extracting map from formbean which gives the serial numbers of
		// selected rows
		final Map map = searchForm.getValues();
		final Object[] obj = map.keySet().toArray();

		// Getting column data & grid data from session
		List<String> columnList = (List<String>) session
				.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
		/**
		 * Name: Deepti Description: Query performance issue. Instead of saving
		 * complete query results in session, resultd will be fetched for each
		 * result page navigation. object of class QuerySessionData will be
		 * saved in session, which will contain the required information for
		 * query execution while navigating through query result pages. Here, as
		 * results are not stored in session, the sql is fired again to form the
		 * shopping cart list.
		 */
		final String pageNo = request.getParameter(Constants.PAGE_NUMBER);
		final String recordsPerPageStr = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);
		if (pageNo != null)
		{
			request.setAttribute(Constants.PAGE_NUMBER, pageNo);
		}
		int recordsPerPage = new Integer(recordsPerPageStr);
		int pageNum = new Integer(pageNo);
		if (isCheckAllAcrossAllChecked != null
				&& isCheckAllAcrossAllChecked.equalsIgnoreCase("true"))
		{
			final Integer totalRecords = (Integer) session.getAttribute(Constants.TOTAL_RESULTS);
			recordsPerPage = totalRecords;
			pageNum = 1;
		}
		final QuerySessionData querySessionData = (QuerySessionData) session
				.getAttribute(edu.wustl.common.util.global.Constants.QUERY_SESSION_DATA);
		final List dataList1 = AppUtility.getPaginationDataList(request, this
				.getSessionData(request), recordsPerPage, pageNum, querySessionData);
		List<List<String>> dataList = (List<List<String>>) session
				.getAttribute(Constants.EXPORT_DATA_LIST);
		if (dataList == null)
		{
			dataList = dataList1;
		}
		// List<String> entityIdsList = (List<String>)
		// session.getAttribute(Constants.ENTITY_IDS_LIST);
		final Map<Integer, List<String>> entityIdsMap = (Map<Integer, List<String>>) session
				.getAttribute(Constants.ENTITY_IDS_MAP);
		// Mandar 06-Apr-06 Bugid:1165 : Extra ID columns displayed. start

		this.logger.debug("-------");
		this.logger.debug("06-apr-06 : cl size :-" + columnList.size());
		this.logger.debug(columnList);
		this.logger.debug("--");
		this.logger.debug("06-apr-06 : dl size :-" + dataList.size());
		this.logger.debug(dataList);

		final List tmpColumnList = new ArrayList();
		int idColCount = 0;
		// count no. of ID columns
		for (int cnt = 0; cnt < columnList.size(); cnt++)
		{
			final String columnName = columnList.get(cnt);
			this.logger.debug(columnName + " : " + columnName.length());
			if (columnName.trim().equalsIgnoreCase("ID"))
			{
				idColCount++;
			}
		}
		// remove ID columns
		for (int cnt = 0; cnt < (columnList.size() - idColCount); cnt++)
		{
			tmpColumnList.add(columnList.get(cnt));
		}

		// datalist filtration for ID data.
		final List tmpDataList = new ArrayList();
		for (int dataListCnt = 0; dataListCnt < dataList.size(); dataListCnt++)
		{
			final List tmpList = dataList.get(dataListCnt);
			final List tmpNewList = new ArrayList();
			for (int cnt = 0; cnt < (tmpList.size() - idColCount); cnt++)
			{
				tmpNewList.add(tmpList.get(cnt));
			}
			tmpDataList.add(tmpNewList);
		}

		this.logger.debug("--");
		this.logger.debug("tmpcollist :" + tmpColumnList.size());
		this.logger.debug(tmpColumnList);
		this.logger.debug("--");
		this.logger.debug("tmpdatalist :" + tmpDataList.size());
		this.logger.debug(tmpDataList);

		this.logger.debug("---");
		columnList = tmpColumnList;
		dataList = tmpDataList;
		// Mandar 06-Apr-06 Bugid:1165 : Extra ID columns end

		final List<List<String>> exportList = new ArrayList();

		// Adding first row(column names) to exportData
		exportList.add(columnList);
		final List<String> idIndexList = new ArrayList<String>();
		final int columnsSize = columnList.size();
		if (isCheckAllAcrossAllChecked != null
				&& isCheckAllAcrossAllChecked.equalsIgnoreCase("true"))
		{
			for (int i = 0; i < dataList.size(); i++)
			{
				final List<String> list = dataList.get(i);
				final List<String> subList = list.subList(0, columnsSize);
				exportList.add(subList);
				if (entityIdsMap != null && !entityIdsMap.isEmpty())
				{
					final List<String> entityIdList = entityIdsMap.get(i);
					idIndexList.addAll(entityIdList);
				}
			}
		}
		else
		{
			for (final Object element : obj)
			{
				final int indexOf = element.toString().indexOf("_") + 1;
				final int index = Integer.parseInt(element.toString().substring(indexOf));
				final List<String> list = dataList.get(index);
				final List<String> subList = list.subList(0, columnsSize);
				if (entityIdsMap != null && !entityIdsMap.isEmpty())
				{
					final List<String> entityIdList = entityIdsMap.get(index);
					idIndexList.addAll(entityIdList);
				}
				exportList.add(subList);
			}
		}
		final String delimiter = Constants.DELIMETER;
		ExportReport report = null;
		// Exporting the data to the given file & sending it to user
		if (entityIdsMap != null && !entityIdsMap.isEmpty())
		{
			report = new ExportReport(path, csvfileName, zipFileName);
			report.writeDataToZip(exportList, delimiter, idIndexList);
			SendFile.sendFileToClient(response, zipFileName, Constants.EXPORT_ZIP_NAME,
					"application/download");
		}
		else
		{
			report = new ExportReport(fileName);
			report.writeData(exportList, delimiter);
			report.closeFile();
			SendFile.sendFileToClient(response, fileName, Constants.SEARCH_RESULT,
					"application/download");
		}

		return null;
	}
}