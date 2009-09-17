
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.actionForm.DistributionReportForm;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

/**
 * This is the action class for displaying the Distribution report.
 *
 * @author Poornima Govindrao
 */

public class DistributionReportAction extends BaseDistributionReportAction
{

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
		String forward = "Success";

		// Retrieve the distribution ID which is set in CommonAddEdit Action
		Long distributionId = (Long) request.getAttribute(Constants.DISTRIBUTION_ID);

		// retrieve from configuration form if it is null
		if (distributionId == null)
		{
			distributionId = configForm.getDistributionId();
		}

		// this condition executes when user distributes the order
		if (request.getAttribute("forwardToHashMap") != null)
		{
			final HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");
			distributionId = (Long) forwardToHashMap.get("distributionId");
		}
		/*
		 * Retrieve from request attribute if it null.
		 */
		if (distributionId == null)
		{
			distributionId = (Long) request.getAttribute(Constants.SYSTEM_IDENTIFIER);
		}

		/*
		 * Retrieve from request parameter if it null. This request parameter is
		 * set in Distribution page incase the Report buttonis clicked from
		 * Distribution Edit page
		 */
		if (distributionId == null)
		{
			distributionId = new Long(request.getParameter(Constants.SYSTEM_IDENTIFIER));
		}

		// Set it in configuration form if it is not null
		if (distributionId != null)
		{
			configForm.setDistributionId(distributionId);
		}

		final Distribution dist = this.getDistribution(distributionId,
				this.getSessionData(request),
				edu.wustl.security.global.Constants.CLASS_LEVEL_SECURE_RETRIEVE);

		// Retrieve the distributed items data
		final DistributionReportForm distributionReportForm = this.getDistributionReportForm(dist);
		final SessionDataBean sessionData = this.getSessionData(request);
		List listOfData = null;
		if(request.getParameter("forward")!=null)
		{
			forward = request.getParameter("forward").toString();
		}
		listOfData = this.getListOfData(dist, configForm, sessionData);
		
		if (listOfData.isEmpty())
		{
			forward = Constants.PAGE_OF_DISTRIBUTION_ARRAY;
		}
		else
		{
			List tempList = new ArrayList();
			for(int i=0;i<listOfData.size();i++)
			{
				List list = (List) listOfData.get( i );
				list.add( 0, "false" );						
			}				

		}
		// Set the columns for Distribution report
		final String action = configForm.getNextAction();
		final String[] selectedColumns = this.getSelectedColumns(action, configForm, false);
		final String[] columnNames = this.getColumnNames(selectedColumns);

		// Set the request attributes for the Distribution report data
		request.setAttribute(Constants.DISTRIBUTION_REPORT_FORM, distributionReportForm);
		request.setAttribute(Constants.COLUMN_NAMES_LIST, columnNames);
		request.setAttribute(Constants.DISTRIBUTED_ITEMS_DATA, listOfData);
		final HashMap forwardToPrintMap = new HashMap();
		/**
		 * specimenIdString is hidden field in DistributionReport.jsp which contains colon separated 
		 * values of specimen ids selected for printing.
		 * bug 13605
		 */
		String specimenIdStr = request.getParameter( "specimenIdString" );
		List<String> idList = this.getSpecimenIdList( specimenIdStr );
		if(idList!=null && !idList.isEmpty())
		{
		   forwardToPrintMap.put(Constants.PRINT_SPECIMEN_DISTRIBUTION_REPORT, idList);
		   //this map is used in PrintAction.java to get specimen ids selected for printing.
		   request.setAttribute("forwardToPrintMap", forwardToPrintMap);
		   this.updatePrintStatusInData( listOfData, idList );
		}
		this.setSelectedMenuRequestAttribute(request);

		final String pageOf = request.getParameter(Constants.PAGE_OF);
		request.setAttribute(Constants.PAGE_OF, pageOf);
		
		AppUtility.setDefaultPrinterTypeLocation(configForm);

		return (mapping.findForward(forward));
	}
	//bug 13605 start
	/**
	 * This method returns List of specimen ids.
	 * @param specimenIdStr - colon separated string of specimen ids (eg 12:11)
	 * @return List of specimen ids
	 */
	private List<String> getSpecimenIdList(String specimenIdStr)
	{
		List<String> idList = new ArrayList<String>();
		if(specimenIdStr!=null && !specimenIdStr.trim().equals( "" ))
	    {
	    	final StringTokenizer st = new StringTokenizer(specimenIdStr, Constants.EXPRESSION_ID_SEPARATOR);
	    	while (st.hasMoreTokens())
	    	{
	    		String token = st.nextToken();
	    		if(token!=null && !token.trim().equals( "" ))
	    		{
	    			idList.add( token );
	    		}
	    	}
	    }
		return idList;
	}
	/**
	 * This method updates status of print checkbox in data list.
	 * If print checkbox is checked for a specimen then value of 1st item in list is set to "true"
	 * otherwise "false".
	 * @param listOfData - list of data
	 * @param idList - specimen ids of selected specimens for printing.
	 */
	private void updatePrintStatusInData(List listOfData,List<String> idList)
	{
		if(listOfData!=null)
		{
			for(int i=0;i<listOfData.size();i++)
			{
				if(listOfData.get( i ) instanceof List)
				{
					List rowElements = (List)listOfData.get( i );
					for(String id : idList)
					{
						if(rowElements.contains( id ))
						{
							rowElements.remove(0);
							rowElements.add(0,"true");
						}
					}
				}
			}
		}
	}
	//bug 13605 end
}
