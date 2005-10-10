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
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.logger.Logger;

/**
 * This is the action class for saving the Distribution report
 * @author Poornima Govindrao
 *  
 */

public class DistributionReportSaveAction extends BaseDistributionReportAction
{
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,HttpServletRequest request,
									HttpServletResponse response)throws Exception 
	{
		ConfigureResultViewForm configForm = (ConfigureResultViewForm)form;
		//Retrieve the distribution ID
		Long distributionId =configForm.getDistributionId();;
		//Retrieve the distribution object for the distribution ID
		Distribution dist =  getDistribution(distributionId);
		//Retrieve the distributed items data
    	DistributionReportForm distributionReportForm = getDistributionReportForm(dist);
    	List listOfData = getListOfData(dist, configForm) ;
    	
    	//Set the columns for Distribution report
    	String action = configForm.getNextAction();
		String selectedColumns[] = getSelectedColumns(action,configForm);
		String []columnNames = getColumnNames(selectedColumns);
		
		//Save the report as a CSV file at the client side
		try
		{
			HttpSession session=request.getSession();
			if(session!=null)
			{
				String filePath = Variables.catissueHome+System.getProperty("file.separator")+"DistributionReport_"+session.getId()+".csv";
				saveReport(distributionReportForm,listOfData,filePath,columnNames);
				String fileName = Constants.DISTRIBUTION_REPORT_NAME;
				String contentType= "application/csv";
				SendFile.sendFileToClient(response,filePath,fileName,contentType);
			}
		}
		catch (IOException e)
		{
			Logger.out.error(e.getMessage(),e);
		}  
		return null;
	}
	
	/**
	 * @param distributionReportForm
	 * @param listOfData
	 * @param fileName
	 * @param columnNames
	 * @throws IOException
	 */
	private void saveReport(DistributionReportForm distributionReportForm,List listOfData,String fileName,
																	String []columnNames) throws IOException
	{
		//Save the report data in a CSV file at server side
		Logger.out.debug("Save action");
		List distributionData = new ArrayList();
		distributionData = createList("distribution.protocol",distributionReportForm.getDistributionProtocolTitle(),distributionData);
		distributionData = createList("eventparameters.user",distributionReportForm.getUserName(),distributionData);
		distributionData = createList("eventparameters.dateofevent",distributionReportForm.getDateOfEvent(),distributionData);
		distributionData = createList("eventparameters.time",distributionReportForm.getTimeInHours()
				+":"+distributionReportForm.getTimeInMinutes(),distributionData);
		distributionData = createList("distribution.fromSite",distributionReportForm.getFromSite(),distributionData);
		distributionData = createList("distribution.toSite",distributionReportForm.getToSite(),distributionData);
		
		distributionData = createList("eventparameters.comments",distributionReportForm.getComments(),distributionData);
		
		ExportReport report = new ExportReport(fileName);
		report.writeData(distributionData);
		List distributedItemsColumns = new ArrayList();
		List columns = new ArrayList();
		for(int k=0;k<columnNames.length;k++)
		{
			columns.add(columnNames[k]);
			Logger.out.debug("Selected columns in save action "+columnNames[k]);
		}
		distributedItemsColumns.add(columns);
		report.writeData(distributedItemsColumns);
		Iterator listItr = listOfData.iterator();
		Logger.out.debug("List of Data in save action "+listOfData);
		while(listItr.hasNext())
		{
			report.writeData((List)listItr.next());
		}
		report.closeFile();
	}
	private List createList(String key,String value,List list)
	{
		List newList = new ArrayList();
		newList.add(ApplicationProperties.getValue(key));
		newList.add(value);
		list.add(newList);
		return list;
	}
}