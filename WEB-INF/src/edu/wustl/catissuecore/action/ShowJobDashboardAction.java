
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.GridUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
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
	private static final Logger LOGGER = Logger.getCommonLogger(ShowJobDashboardAction.class);

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.SecureAction#executeSecureAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
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
		return mapping.findForward(Constants.SUCCESS);
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
			LOGGER.error(exp.getMessage(), exp);
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
	 * @see edu.wustl.cider.action.AbstractShowGridAction#getHeaderXML
	 * (javax.servlet.http.HttpServletRequest)
	 */
	public StringBuffer getHeaderXML(HttpServletRequest request)
	{
		StringBuffer headerXML = new StringBuffer(500);

		headerXML.append(GridUtil.HEAD_START_TAG);
		headerXML.append(GridUtil.getColumnXML("5", GridUtil.CELL_TYPE_READ_ONLY, "Id",
				GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_INT));
		headerXML.append(GridUtil.getColumnXML("20", GridUtil.CELL_TYPE_READ_ONLY, "Bulk operation Name",
				GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_STR));
		headerXML.append(GridUtil.getColumnXML("10", GridUtil.CELL_TYPE_READ_ONLY, "Start Time",
				GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_STR));
		headerXML.append(GridUtil.getColumnXML("15", GridUtil.CELL_TYPE_READ_ONLY, "Status",
				GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_STR));
		headerXML.append(GridUtil.getColumnXML("10", GridUtil.CELL_TYPE_READ_ONLY, "Total Records",
				GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_STR));
		headerXML.append(GridUtil.getColumnXML("10", GridUtil.CELL_TYPE_READ_ONLY,
				"Processed Records", GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_STR));
		headerXML.append(GridUtil.getColumnXML("10", GridUtil.CELL_TYPE_READ_ONLY,
				"Failed Records", GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_STR));
		headerXML.append(GridUtil.getColumnXML("10", GridUtil.CELL_TYPE_READ_ONLY,
				"Time taken(sec)", GridUtil.ALIGN_LEFT, GridUtil.CELL_SORT_STR));
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