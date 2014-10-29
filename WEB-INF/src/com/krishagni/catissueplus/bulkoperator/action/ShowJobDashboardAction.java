/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.action;

import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.krishagni.catissueplus.bulkoperator.util.AppUtility;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationUtility;
import com.krishagni.catissueplus.bulkoperator.util.GridUtil;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;

// TODO: Auto-generated Javadoc
/**
 * The Class ShowBulkOperationDashboardAction.
 * @author nitesh_marwaha
 */
public class ShowJobDashboardAction extends SecureAction
{
	/**
	 * logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(ShowJobDashboardAction.class);

	/* (non-Javadoc)
	 * @see com.krishagni.catissueplus.core.common.action.SecureAction#executeSecureAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeSecureAction(final ActionMapping mapping, final ActionForm form,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{
		final String gridXml = getGridXML(request);
		List columnsToDisplay = new ArrayList();
		final SessionDataBean sessionDataBean = this.getSessionData(request);
		final long userId = sessionDataBean.getUserId();
		columnsToDisplay = getColumnsToDisplay();
		getDashboardDataXML(userId);
		request.setAttribute(getXmlHeaderName(), gridXml);
		request.setAttribute("columnsToDisplay", columnsToDisplay);
		String requestType=request.getParameter("requestType");
		if(requestType != null && requestType.equals("ajax"))
		{
			JSONObject resultObject = new JSONObject();
			String index = request.getParameter("index");
			String jobId = request.getParameter("jobId");
			List<ArrayList> list = null;

				String query = "Select IDENTIFIER, JOB_NAME,START_TIME, JOB_STATUS, TOTAL_RECORDS_COUNT, "
						+ "CURRENT_RECORDS_PROCESSED, FAILED_RECORDS_COUNT, TIME_TAKEN from "
						+ "JOB_DETAILS where IDENTIFIER > " + jobId +"  and JOB_STARTED_BY="+userId;
				list = AppUtility.executeSQLQuery(query);
				StringBuffer buffer = new StringBuffer();
				if(!list.isEmpty())
				{
					String latestId = null;
					for(int i=0;i<list.size();i++)
					{
						List contentList=list.get(i);

						for(int j=0;j<contentList.size();j++)
						{
							if(j != 0)
							{
								buffer.append(',');
							}
							buffer.append(contentList.get(j));
						}

						buffer.append(",<a href='javascript:showAttachment("+contentList.get(0)+")'>Download</a>");
						if(i<list.size()-1)
						{
							buffer.append("#");
						}

						latestId=contentList.get(0).toString();
					}
					resultObject.append("contentList",buffer);
					resultObject.append("latestId",latestId);


				}


			//for reading the jobGrid.refresh.time from the bulkOperation.properties file
			String filePath=CommonServiceLocator.getInstance().getPropDirPath()+ File.separator + "bulkOperation.properties";
			Properties properties = BulkOperationUtility.getPropertiesFile(filePath);
			String gridRefreshTime=properties.getProperty("jobGrid.refresh.timeInterval");

			resultObject.append("xmlGrid", gridXml);
			resultObject.append("gridRefreshTime",gridRefreshTime);
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			Writer writer = response.getWriter();
			writer.write(new JSONObject().put("resultObject", resultObject).toString());
			return null;
		}
		else
		{
			return mapping.findForward(Constants.SUCCESS);
		}
	}

	/**
	 * This will return the grid XML as String.
	 *
	 * @param request HttpServletRequest instance
	 *
	 * @return the grid XML as String.
	 *
	 * @throws CiderSystemException CiderSystemException instance
	 * @throws ApplicationException the application exception
	 */
	public String getGridXML(final HttpServletRequest request) throws ApplicationException
	{
		StringBuffer gridXML = new StringBuffer(500);
		gridXML.append(GridUtil.getXMLStringDocType());
		gridXML.append(GridUtil.getRowsStartTag());
		gridXML.append(getContentXML(request));
		gridXML.append(GridUtil.getRowsEndTag());
		return gridXML.toString();
	}

	/**
	 * This will return the content XML.
	 *
	 * @param request HttpServletRequest instance
	 *
	 * @return the content XML.
	 *
	 * @throws CiderSystemException CiderSystemException instance
	 * @throws ApplicationException the application exception
	 */
	public StringBuffer getContentXML(final HttpServletRequest request) throws ApplicationException
	{
		StringBuffer contentXML = new StringBuffer(500);
		final StringBuffer headerXML = getHeaderXML(request);
		final StringBuffer rowXML = getRowXML(request);
		contentXML.append(headerXML);
		contentXML.append(rowXML);
		return contentXML;
	}

	/**
	 * Gets the xml header name.
	 *
	 * @return the xml header name
	 */
	public String getXmlHeaderName()
	{
		return "msgBoardXml";
	}

	/**
	 * This method returns the row XML.
	 *
	 * @param request HttpServletRequest instance
	 *
	 * @return the row XML.
	 *
	 * @throws CiderSystemException instance of CiderSystemException
	 * @throws ApplicationException the application exception
	 */
	public StringBuffer getRowXML(final HttpServletRequest request) throws ApplicationException
	{
		StringBuffer rowXML = new StringBuffer();
		JDBCDAO dao = null;
		try
		{
			SessionDataBean sessionDataBean = this.getSessionData(request);
			long userId = sessionDataBean.getUserId();
			dao = AppUtility.openJDBCSession();
			List<ArrayList> msgBoardItemsList = (List) dao.executeQuery("Select IDENTIFIER, " +
				"JOB_NAME, START_TIME, JOB_STATUS, TOTAL_RECORDS_COUNT, CURRENT_RECORDS_PROCESSED, " +
				"FAILED_RECORDS_COUNT, TIME_TAKEN from JOB_DETAILS where JOB_STARTED_BY = " + userId
				+ " ORDER BY IDENTIFIER DESC ");
			int rowCount = 0;
			if (msgBoardItemsList != null)
			{
				for (List msgBoardItem : msgBoardItemsList)
				{
					if (msgBoardItem != null)
					{
						rowXML.append(GridUtil.getEachRowStartTag(++rowCount));
						addMsgBoardItemToRowXml(rowXML, msgBoardItem);
						rowXML.append(GridUtil.getEachRowEndTag());
					}
				}
			}
		}
		catch (Exception exp)
		{
			logger.debug(exp.getMessage(), exp);
		}
		finally
		{
			AppUtility.closeJDBCSession(dao);
		}
		return rowXML;
	}

	/**
	 * This will add the message board items to row XML.
	 *
	 * @param rowXML row XML
	 * @param msgBoardItem message board items array
	 */
	private void addMsgBoardItemToRowXml(StringBuffer rowXML, List msgBoardItem)
	{
		String jobId = msgBoardItem.get(0).toString();

		for (int i = 0; i < msgBoardItem.size(); i++)
		{
			Object object = msgBoardItem.get(i);
			if (object == null)
			{
				rowXML.append(GridUtil.getCellXML(""));
			}
			else
			{
				rowXML.append(GridUtil.getCellXML(object.toString()));
			}
		}
		rowXML.append(GridUtil.getCellXML("<a href='javascript:showAttachment(" + jobId + ")'>Download</a>"));
	}

	/**
	 * Returns The header XML.
	 *
	 * @param request HttpServletRequest instance
	 *
	 * @return The header XML.
	 *
	 * @see com.krishagni.catissueplus.core.cider.action.AbstractShowGridAction#getHeaderXML
	 * (javax.servlet.http.HttpServletRequest)
	 */
	public StringBuffer getHeaderXML(HttpServletRequest request)
	{
		StringBuffer headerXML = new StringBuffer(500);

		headerXML.append(GridUtil.HEAD_START_TAG);
		headerXML.append(GridUtil.getColumnXML("5", GridUtil.CELL_TYPE_READ_ONLY, "Id",
				GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_INT));
		headerXML.append(GridUtil.getColumnXML("20", GridUtil.CELL_TYPE_READ_ONLY, "Bulk Operation Name",
				GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_STR));
		headerXML.append(GridUtil.getColumnXML("15", GridUtil.CELL_TYPE_READ_ONLY, "Start Time",
				GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_STR));
		headerXML.append(GridUtil.getColumnXML("10", GridUtil.CELL_TYPE_READ_ONLY, "Status",
				GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_STR));
		headerXML.append(GridUtil.getColumnXML("10", GridUtil.CELL_TYPE_READ_ONLY, "Total Records",
				GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_INT));
		headerXML.append(GridUtil.getColumnXML("10", GridUtil.CELL_TYPE_READ_ONLY,
				"Processed Records", GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_INT));
		headerXML.append(GridUtil.getColumnXML("10", GridUtil.CELL_TYPE_READ_ONLY,
				"Failed Records", GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_INT));
		headerXML.append(GridUtil.getColumnXML("10", GridUtil.CELL_TYPE_READ_ONLY,
				"Time taken(sec)", GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_INT));
		headerXML.append(GridUtil.getColumnXML("10", GridUtil.CELL_TYPE_READ_ONLY, "Report",
				GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_STR));

		headerXML.append(GridUtil.SETTINGS_WIDTH_PERCENTAGE);
		headerXML.append(GridUtil.HEAD_END_TAG);
		request.setAttribute("identifierFieldIndex", 0);
		return headerXML;
	}

	/**
	 * Gets the dash board data xml.
	 *
	 * @param userId the user id
	 *
	 * @return the dash board data xml
	 */
	private String getDashboardDataXML(long userId)
	{
		return null;
	}

	/**
	 * Gets the columns to display.
	 *
	 * @return the columns to display
	 */
	private List getColumnsToDisplay()
	{
		return null;
	}
}