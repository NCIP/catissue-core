
package edu.wustl.catissuecore.action;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.actionForm.DistributionReportForm;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

/**
 * This is the action class for displaying the Distribution report
 * @author Rahul Ner
 *  
 */

public class ArrayDistributionReportAction extends BaseDistributionReportAction
{
	protected ActionForward executeAction(ActionMapping mapping,ActionForm form, HttpServletRequest request,
															HttpServletResponse response) throws Exception
	{
		ConfigureResultViewForm configForm = (ConfigureResultViewForm)form;
		
		//Retrieve the distribution ID which is set in CommonAddEdit Action 
		Long distributionId = (Long)request.getAttribute(Constants.DISTRIBUTION_ID);
		
		//retrieve from configuration form if it is null
		if(distributionId==null)
    		distributionId = configForm.getDistributionId();
		
		/*Retrieve from request parameter if it null. This request parameter is set in Distribution page incase the Report button 
		 *is clicked from Distribution Edit page
		 */ 
		if(distributionId==null)
		{
			distributionId = new Long(request.getParameter(Constants.SYSTEM_IDENTIFIER));
		}
		
		//Set it in configuration form if it is not null 
		if(distributionId!=null)
    		configForm.setDistributionId(distributionId);
			
    	Distribution dist =  getDistribution(distributionId, getSessionData(request), Constants.CLASS_LEVEL_SECURE_RETRIEVE);
    	
    	//Retrieve the distributed items data
    	DistributionReportForm distributionReportForm = getDistributionReportForm(dist);
    	SessionDataBean sessionData = getSessionData(request);
    	List listOfData = getListOfArrayData(dist, configForm,sessionData) ;
    	
    	//Set the columns for Distribution report
		String action = configForm.getNextAction();
		String selectedColumns[] = getSelectedColumns(action,configForm,true);
		String []columnNames = getColumnNames(selectedColumns);
    	
		//Set the request attributes for the Distribution report data
		request.setAttribute(Constants.DISTRIBUTION_REPORT_FORM, distributionReportForm);
    	request.setAttribute(Constants.COLUMN_NAMES_LIST, columnNames);
    	request.setAttribute(Constants.DISTRIBUTED_ITEMS_DATA, listOfData);
    	setSelectedMenuRequestAttribute(request);
		return (mapping.findForward("Success"));
	}

	private List getListOfArrayData(Distribution dist, ConfigureResultViewForm configForm, SessionDataBean sessionData) {
    	//Get the list of data for Distributed items data for the report.
    	List listOfData = new ArrayList();
    	Collection specimenArrayCollection = dist.getSpecimenArrayCollection();		
    	//Specimen Ids which are getting distributed.
    	String []specimenArrayIds = new String[specimenArrayCollection.size()];
    	int i=0;
    	Iterator itr = specimenArrayCollection.iterator();
    	
    	while(itr.hasNext())
    	{
    		SpecimenArray array = (SpecimenArray) itr.next();
    		List tempList = new ArrayList();
    		tempList.add(array.getId().toString());
    		tempList.add(array.getBarcode().toString());
    		i++;
    		List tempList1 = new ArrayList();
    		tempList1.add(tempList);
    		listOfData.add(tempList1);
    	}
    	return listOfData;		
	}
}