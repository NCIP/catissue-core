package edu.wustl.catissuecore.action;
import java.io.IOException;
import java.util.ArrayList;
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
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.catissuecore.vo.ArrayDistributionReportEntry;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.global.CommonServiceLocator;

/**
 * This is the action class for saving the Distribution report
 * @author Rahul Ner
 *  
 */

public class ArrayDistributionReportSaveAction extends ArrayDistributionReportAction
{
	/**
	 * @see edu.wustl.catissuecore.action.ArrayDistributionReportAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,HttpServletRequest request,
									HttpServletResponse response)throws Exception 
	{
		ConfigureResultViewForm configForm = (ConfigureResultViewForm)form;
		//Retrieve the distribution ID
		Long distributionId =configForm.getDistributionId();;
		
		Distribution dist =  getDistribution(distributionId, getSessionData(request), edu.wustl.security.global.Constants.CLASS_LEVEL_SECURE_RETRIEVE);
    	SessionDataBean sessionData = getSessionData(request);
    	DistributionReportForm distributionReportForm = getDistributionReportForm(dist);
    	distributionReportForm.setDistributionType(new Integer(Constants.SPECIMEN_ARRAY_DISTRIBUTION_TYPE));
    	
    	//Set the columns for Distribution report
    	String action = configForm.getNextAction();
		String selectedColumns[] = getSelectedColumns(action,configForm,true);
		String []columnNames = getColumnNames(selectedColumns);
		
		String[] specimenColumns = Constants.SPECIMEN_IN_ARRAY_SELECTED_COLUMNS;
		String []specimenColumnNames = getColumnNames(specimenColumns);
		
    	List listOfData = getListOfArrayDataForSave(dist,configForm,sessionData,selectedColumns,specimenColumns) ;
		
		setSelectedMenuRequestAttribute(request);
		//Save the report as a CSV file at the client side
		HttpSession session=request.getSession();
		if(session!=null)
		{
			String filePath = CommonServiceLocator.getInstance().getAppHome()+System.getProperty("file.separator")+"DistributionReport_"+session.getId()+".csv";

			saveReport(distributionReportForm,listOfData,filePath,columnNames,specimenColumnNames);
			
			String fileName = Constants.DISTRIBUTION_REPORT_NAME;
			String contentType= "application/download";
			SendFile.sendFileToClient(response,filePath,fileName,contentType);
		}
		return null;
	}
	
	/**
	 * saves report for list of ArrayDistributionReportEntry
	 * @param distributionReportForm
	 * @param listOfData
	 * @param fileName
	 * @param arrayColumnNames
	 * @param specimenColumnNames
	 * @throws IOException
	 */
	protected void saveReport(DistributionReportForm distributionReportForm,List listOfData,String fileName,
			String []arrayColumnNames,String[] specimenColumnNames) throws IOException {
		ExportReport report = new ExportReport(fileName);
		String[] gridInfoLabel = {"Array Grid"};
		String delimiter = Constants.DELIMETER;
		List distributionData = new ArrayList();
		addDistributionHeader(distributionData,distributionReportForm,report);
		report.writeData(distributionData, delimiter);
		Iterator itr = listOfData.iterator();
		while(itr.hasNext())
		{
			ArrayDistributionReportEntry arrayEntry = (ArrayDistributionReportEntry) itr.next();
			
			List arrayInfo = new ArrayList();
			arrayInfo.add(arrayEntry.getArrayInfo());
			addSection(report,arrayColumnNames,arrayInfo,2,0);
			addSection(report,gridInfoLabel,arrayEntry.getGridInfo(),1,2);
			addSection(report,specimenColumnNames,arrayEntry.getSpecimenEntries(),1,2);
		}
		report.closeFile();
	}
	/**
	 * Adds a section to report
	 * @param report
	 * @param columnNames
	 * @param listOfData
	 * @param noblankLines
	 * @param columnIndent
	 * @throws IOException
	 */
	protected void addSection(ExportReport report,String[] columnNames,List listOfData,int noblankLines,int columnIndent)
			throws IOException {
		if (columnNames != null) {
			List headerColumns = new ArrayList();
			List columns = new ArrayList();
			for (int k = 0; k < columnNames.length; k++) {
				columns.add(columnNames[k]);
			}
			headerColumns.add(columns);
			report.writeData(headerColumns, Constants.DELIMETER,noblankLines,columnIndent);
		}
		report.writeData(listOfData, Constants.DELIMETER,0,columnIndent);
	}

}