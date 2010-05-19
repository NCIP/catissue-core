
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.actionForm.DistributionReportForm;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;

/**
 * This is the action class for saving the Distribution report.
 *
 * @author Poornima Govindrao
 */

public class DistributionReportSaveAction extends BaseDistributionReportAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(DistributionReportSaveAction.class);

	/**
	 * Overrides the execute method of Action class. Sets the various fields in
	 * DistributionProtocol Add/Edit webpage.
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
	 * @return value for ActionForward object
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final ConfigureResultViewForm configForm = (ConfigureResultViewForm) form;
		// Retrieve the distribution ID
		final Long distributionId = configForm.getDistributionId();

		final Distribution dist = this.getDistribution(distributionId,
				this.getSessionData(request),
				edu.wustl.security.global.Constants.CLASS_LEVEL_SECURE_RETRIEVE);
		final SessionDataBean sessionData = this.getSessionData(request);
		final DistributionReportForm distributionReportForm = this.getDistributionReportForm(dist);
		final List listOfData = this.getListOfData(dist, configForm, sessionData);

		// Set the columns for Distribution report
		final String action = configForm.getNextAction();
		final String[] selectedColumns = this.getSelectedColumns(action, configForm, false);
		final String[] columnNames = this.getColumnNames(selectedColumns);

		this.setSelectedMenuRequestAttribute(request);
		// Save the report as a CSV file at the client side
		final HttpSession session = request.getSession();
		if (session != null)
		{
			final String filePath = CommonServiceLocator.getInstance().getAppHome()
					+ System.getProperty("file.separator") + "DistributionReport_"
					+ session.getId() + ".csv";
			this.saveReport(distributionReportForm, listOfData, filePath, columnNames);
			final String fileName = Constants.DISTRIBUTION_REPORT_NAME;
			final String contentType = "application/download";
			SendFile.sendFileToClient(response, filePath, fileName, contentType);
		}
		return null;
	}

	/**
	 * Method to fetch the column names selected by user to export and
	 * removing the "Print" column from the list.
	 */
	protected String[] getColumnNames(String[] selectedColumnsList)
	{
		List<String> listSelColumns = new ArrayList<String>(Arrays.asList(selectedColumnsList));
		listSelColumns.removeAll(Arrays.asList("Print : Specimen"));
		selectedColumnsList = listSelColumns.toArray(new String[0]);
		String[] columnNames = new String[selectedColumnsList.length];
		return AppUtility.getColNames(selectedColumnsList, columnNames, 0);
	}

	/**
	 * @param distributionReportForm
	 *            object of DistributionReportForm
	 * @param listOfData
	 *            list of data
	 * @param fileName
	 *            String for fileName
	 * @param columnNames
	 *            String array of column names
	 * @throws IOException
	 *             I/O exception
	 */
	private void saveReport(DistributionReportForm distributionReportForm, List listOfData,
			String fileName, String[] columnNames) throws IOException
	{
		// Save the report data in a CSV file at server side
		this.logger.debug("Save action");
		final ExportReport report = new ExportReport(fileName);
		final List distributionData = new ArrayList();
		this.addDistributionHeader(distributionData, distributionReportForm, report);
		final String delimiter = Constants.DELIMETER;
		final List distributedItemsColumns = new ArrayList();
		final List columns = new ArrayList();
		for (final String columnName : columnNames)
		{
			columns.add(columnName);
			this.logger.debug("Selected columns in save action " + columnName);
		}
		distributedItemsColumns.add(columns);
		distributionData.addAll(distributedItemsColumns);
		final List newDataList = new ArrayList();
		newDataList.add(listOfData);
		final Iterator dataListItr = newDataList.iterator();
		while (dataListItr.hasNext())
		{
			final List rowList = (List) dataListItr.next();
			// Remove extra ID columns from all rows.-Bug 5590
			for (int cntRow = 0; cntRow < rowList.size(); cntRow++)
			{
				final List data = (List) rowList.get(cntRow);
				final int extraColumns = data.size()
						- ((List) distributedItemsColumns.get(0)).size();
				// Remove extra ID columns
				if (extraColumns > 0)
				{
					int size = data.size() - 1;
					for (int j = 1; j <= extraColumns; j++)
					{
						data.remove(size);
						size--;
					}
				}
			}
			distributionData.addAll(rowList);
		}
		report.writeData(distributionData, delimiter);
		report.closeFile();
	}
}