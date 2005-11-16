
package edu.wustl.catissuecore.action;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.actionForm.DistributionReportForm;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * This is the action class for displaying the Distribution report
 * @author Poornima Govindrao
 *  
 */

public class DistributionReportAction extends BaseDistributionReportAction
{
	protected ActionForward executeSecureAction(ActionMapping mapping,ActionForm form, HttpServletRequest request,
															HttpServletResponse response) throws Exception
	{
		ConfigureResultViewForm configForm = (ConfigureResultViewForm)form;
		//Retrieve the distribution ID
		Long distributionId = (Long)request.getAttribute(Constants.DISTRIBUTION_ID);
		Logger.out.debug("distributionId "+distributionId);
		if(distributionId!=null)
    		configForm.setDistributionId(distributionId);
    	else
    		distributionId = configForm.getDistributionId();
    	
    	Distribution dist =  getDistribution(distributionId, getSessionData(request), Constants.CLASS_LEVEL_SECURE_RETRIEVE);
    	
    	//Retrieve the distributed items data
    	DistributionReportForm distributionReportForm = getDistributionReportForm(dist);
    	List listOfData = getListOfData(dist, configForm) ;
    	
    	//Set the columns for Distribution report
		String action = configForm.getNextAction();
		String selectedColumns[] = getSelectedColumns(action,configForm);
		String []columnNames = getColumnNames(selectedColumns);
    	
		//Set the request attributes for the Distribution report data
		request.setAttribute(Constants.DISTRIBUTION_REPORT_FORM, distributionReportForm);
    	request.setAttribute(Constants.COLUMN_NAMES_LIST, columnNames);
    	request.setAttribute(Constants.DISTRIBUTED_ITEMS_DATA, listOfData);
    	setSelectedMenuRequestAttribute(request);
		return (mapping.findForward("Success"));
	}
}