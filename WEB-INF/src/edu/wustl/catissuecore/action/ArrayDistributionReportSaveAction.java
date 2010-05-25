
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
import edu.wustl.catissuecore.vo.ArrayDistributionReportEntry;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.global.CommonServiceLocator;

/**
 * This is the action class for saving the Distribution report.
 *
 * @author Rahul Ner
 */

public class ArrayDistributionReportSaveAction extends ArrayDistributionReportAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
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
		distributionReportForm.setDistributionType(new Integer(
				Constants.SPECIMEN_ARRAY_DISTRIBUTION_TYPE));

		// Set the columns for Distribution report
		final String action = configForm.getNextAction();
		final String[] selectedColumns = this.getSelectedColumns(action, configForm, true);
//		final String[] columnNames = this.getColumnNames(selectedColumns);

		final String[] specimenColumns = Constants.SPECIMEN_IN_ARRAY_SELECTED_COLUMNS;
		final String[] specimenColumnNames;
			List<String> listSelColumns = new ArrayList<String>(Arrays.asList(specimenColumns));
		listSelColumns.removeAll(Arrays.asList("Print : Specimen"));
		specimenColumnNames = listSelColumns.toArray(new String[0]);
		String[] columnNames = new String[specimenColumnNames.length];
		columnNames = AppUtility.getColNames(specimenColumnNames, columnNames, 0);

//			this.getColumnNames(specimenColumns);

		final List listOfData = this.getListOfArrayDataForSave(dist, configForm, sessionData,
				selectedColumns, specimenColumns);

		this.setSelectedMenuRequestAttribute(request);
		// Save the report as a CSV file at the client side
		final HttpSession session = request.getSession();
		if (session != null)
		{
			final String filePath = CommonServiceLocator.getInstance().getAppHome()
					+ System.getProperty("file.separator") + "DistributionReport_"
					+ session.getId() + ".csv";

			this.saveReport(distributionReportForm, listOfData, filePath, columnNames,
					specimenColumnNames);

			final String fileName = Constants.DISTRIBUTION_REPORT_NAME;
			final String contentType = "application/download";
			SendFile.sendFileToClient(response, filePath, fileName, contentType);
		}
		return null;
	}

	/**
	 * saves report for list of ArrayDistributionReportEntry.
	 *
	 * @param distributionReportForm : distributionReportForm
	 * @param listOfData : listOfData
	 * @param fileName : fileName
	 * @param arrayColumnNames : arrayColumnNames
	 * @param specimenColumnNames : specimenColumnNames
	 * @throws IOException : IOException
	 */
	protected void saveReport(DistributionReportForm distributionReportForm, List listOfData,
			String fileName, String[] arrayColumnNames, String[] specimenColumnNames)
			throws IOException
	{
		final ExportReport report = new ExportReport(fileName);
		final String[] gridInfoLabel = {"Array Grid"};
		final String delimiter = Constants.DELIMETER;
		final List distributionData = new ArrayList();
		this.addDistributionHeader(distributionData, distributionReportForm, report);
		report.writeData(distributionData, delimiter);
		final Iterator itr = listOfData.iterator();
		while (itr.hasNext())
		{
			final ArrayDistributionReportEntry arrayEntry = (ArrayDistributionReportEntry) itr
					.next();

			final List arrayInfo = new ArrayList();
			arrayInfo.add(arrayEntry.getArrayInfo());
			this.addSection(report, arrayColumnNames, arrayInfo, 2, 0);
			this.addSection(report, gridInfoLabel, arrayEntry.getGridInfo(), 1, 2);
			this.addSection(report, specimenColumnNames, arrayEntry.getSpecimenEntries(), 1, 2);
		}
		report.closeFile();
	}

	/**
	 * Adds a section to report.
	 *
	 * @param report : report
	 * @param columnNames : columnNames
	 * @param listOfData : listOfData
	 * @param noblankLines : noblankLines
	 * @param columnIndent : columnIndent
	 * @throws IOException : IOException
	 */
	protected void addSection(ExportReport report, String[] columnNames, List listOfData,
			int noblankLines, int columnIndent) throws IOException
	{
		if (columnNames != null)
		{
			final List headerColumns = new ArrayList();
			final List columns = new ArrayList();
			for (final String columnName : columnNames)
			{
				columns.add(columnName);
			}
			headerColumns.add(columns);
			report.writeData(headerColumns, Constants.DELIMETER, noblankLines, columnIndent);
		}
		report.writeData(listOfData, Constants.DELIMETER, 0, columnIndent);
	}

}