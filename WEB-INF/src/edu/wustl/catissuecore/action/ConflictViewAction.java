/**
 * <p>Title: ConflictViewAction Class>
 * <p>Description:	Initialization action for conflict view
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 * @author kalpana Thakur
 * Created on sep 18,2007
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CPSearchForm;
import edu.wustl.catissuecore.actionForm.ConflictViewForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.bizlogic.ReportLoaderQueueBizLogic;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.reportloader.HL7Parser;
import edu.wustl.catissuecore.reportloader.ReportLoaderUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;

public class ConflictViewAction extends BaseAction
{

	/**
	 * Overrides the execute method of Action class.
	 * Initializes the various fields in ConflictView.jsp Page.
	 * @param mapping object
	 * @param form object
	 * @param request object
	 * @param response object
	 * @return ActionForward object
	 * @throws Exception object
	 * */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception, DAOException
	{
		ConflictViewForm conflictViewForm = (ConflictViewForm) form;
		int selectedFilter =  Integer.parseInt(conflictViewForm.getSelectedFilter());
		
		
		String[] retrieveFilterList = Constants.CONFLICT_FILTER_LIST;
		List filterList = new ArrayList();
		for(int i=0;i<retrieveFilterList.length;i++)
		{
			filterList.add(0,new NameValueBean(retrieveFilterList[i], i));
		}
		Collections.sort(filterList);
	    //Setting the list in request
		request.setAttribute(Constants.FILTER_LIST, filterList);
		
		
		
		 //Returns the page number to be shown.
        int pageNum = Integer.parseInt(request.getParameter(Constants.PAGE_NUMBER));
		
        //Gets the session of this request.
        HttpSession session = request.getSession();
        
        //The start index in the list of users to be approved/rejected.
        int startIndex = Constants.ZERO;
        
        //The end index in the list of conflicts.
        int recordsPerPage = Integer.parseInt(XMLPropertyHandler.getValue(edu.wustl.common.util.global.Constants.RECORDS_PER_PAGE_PROPERTY_NAME));
        
        if(request.getParameter(Constants.RESULTS_PER_PAGE) != null) 
    	{
        	recordsPerPage = Integer.parseInt(request.getParameter(Constants.RESULTS_PER_PAGE));       
    	}
        else if (session.getAttribute(Constants.RESULTS_PER_PAGE)!=null)
        {
        	recordsPerPage = Integer.parseInt(session.getAttribute(Constants.RESULTS_PER_PAGE).toString());
        } 
        int endIndex = recordsPerPage;
    
        
        
		
		List reportQueueDataList = null;;
		
		//making grid data to display
		if (pageNum == Constants.START_PAGE)
        {
            //If start page is to be shown retrieve the list from the database.
           //	retrieving the report queue data from db
			
			//reportQueueDataList = getReportQueueDataList();
			ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic)BizLogicFactory.getInstance().getBizLogic(ReportLoaderQueue.class.getName());
			
			// retrieving all the conflicts
			if (selectedFilter==0)
			{	
				reportQueueDataList = reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class.getName(), "status", CaTIESConstants.STATUS_PARTICIPANT_CONFLICT);
				reportQueueDataList.addAll(reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class.getName(), "status", CaTIESConstants.STATUS_SCG_CONFLICT));
				reportQueueDataList.addAll(reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class.getName(), "status", CaTIESConstants.STATUS_SCG_PARTIAL_CONFLICT));
				
			
			
			}
			else
			{	//retrieving only the participant conflicts
				if (selectedFilter==1)
			    {	
					reportQueueDataList = reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class.getName(), "status", CaTIESConstants.STATUS_PARTICIPANT_CONFLICT);
				
					
			    }
				else
				{	//retrieving all the scg conflicts both partial and exact match
					if (selectedFilter==2)
					{	
						reportQueueDataList = reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class.getName(), "status", CaTIESConstants.STATUS_SCG_CONFLICT);
						reportQueueDataList.addAll(reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class.getName(), "status", CaTIESConstants.STATUS_SCG_PARTIAL_CONFLICT));
					
						
					}	
				}
			}	
		    if (recordsPerPage > reportQueueDataList.size())
            {
                endIndex = reportQueueDataList.size();
            }
		    //Save the list of conflicts in the sesson.
            session.setAttribute(Constants.ORIGINAL_DOMAIN_OBJECT_LIST,reportQueueDataList);
        }
		else
	        {
	            //Get the list of conflicts from the session.
				reportQueueDataList = (List)session.getAttribute(Constants.ORIGINAL_DOMAIN_OBJECT_LIST);
	            if (recordsPerPage!= Integer.MAX_VALUE)
	            {
		            //Set the start index of the conflicts in the list.
		            startIndex = (pageNum-1) * recordsPerPage;
		            
		            //Set the end index of the conflicts in the list.
		            endIndex = startIndex + recordsPerPage;
		            
		            if (endIndex > reportQueueDataList.size())
		            {
		                endIndex = reportQueueDataList.size();
		            }
	            }
	            else
	            {
	            	startIndex = 0;
	            	endIndex = reportQueueDataList.size();
	            }
	        }
		
    
        
        //Saves the page number in the request.
        request.setAttribute(Constants.PAGE_NUMBER,Integer.toString(pageNum));
        
        //Saves the total number of results in the request. 
        session.setAttribute(Constants.TOTAL_RESULTS,Integer.toString(reportQueueDataList.size()));
        
        session.setAttribute(Constants.RESULTS_PER_PAGE,recordsPerPage+"");
		
		List dataList = makeGridData(reportQueueDataList,selectedFilter );
		request.getSession().setAttribute(Constants.REPORT_QUEUE_LIST, dataList);
		request.getSession().setAttribute(Constants.SELECTED_FILTER, Integer.toString(selectedFilter));
		return mapping.findForward(Constants.SUCCESS);
	}
	
	/**
	 * To prepare the grid to display on conflictView.jsp
	 * @param reportQueueDataList
	 * @param selectedFilter 
	 * @return
	 */
	private List makeGridData(List reportQueueDataList, int selectedFilter)
	{
		Iterator iter = reportQueueDataList.iterator();
		List gridData = new ArrayList();
		while(iter.hasNext())
		{
			List rowData = new ArrayList();
			ReportLoaderQueue reportLoaderQueue = (ReportLoaderQueue)iter.next();
			rowData.add(reportLoaderQueue.getParticipantName());
			rowData.add(reportLoaderQueue.getId());
			rowData.add(reportLoaderQueue.getSurgicalPathologyNumber());
			rowData.add(reportLoaderQueue.getReportLoadedDate());
			rowData.add(reportLoaderQueue.getStatus());
			rowData.add(reportLoaderQueue.getSiteName());
			gridData.add(rowData);
		}
	
		return gridData;
	}
	
	
}
