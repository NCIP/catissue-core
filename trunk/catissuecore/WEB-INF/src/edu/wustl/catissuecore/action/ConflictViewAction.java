/**
 * <p>Title: ConflictViewAction Class>
 * <p>Description:	Initialization action for conflict view
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Feb 27,2007
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.reportloader.HL7Parser;
import edu.wustl.catissuecore.reportloader.Parser;
import edu.wustl.catissuecore.reportloader.ReportLoaderUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.action.SecureAction;
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
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//retrieving the report queue data from db
		List reportQueueDataList = getReportQueueDataList();
		//making grid data to display
		List dataList = makeGridData(reportQueueDataList);
		request.getSession().setAttribute(Constants.REPORT_QUEUE_LIST, dataList);
		return mapping.findForward(Constants.SUCCESS);
	}
	/**
	 * @return
	 * @throws DAOException
	 */
	private List getReportQueueDataList() throws DAOException
	{
		List reportQueueList = ReportLoaderUtil.getObject(ReportLoaderQueue.class.getName(), "status", Parser.CONFLICT);
		return reportQueueList;		
	}
	/**
	 * @param reportQueueDataList
	 * @return
	 */
	private List makeGridData(List reportQueueDataList)
	{
		Iterator iter = reportQueueDataList.iterator();
		List gridData = new ArrayList();
		while(iter.hasNext())
		{
			List rowData = new ArrayList();
			ReportLoaderQueue reportLoaderQueue = (ReportLoaderQueue)iter.next();
			
			rowData.add(reportLoaderQueue.getId().toString());
			String accessionNo = getAccessionNo(reportLoaderQueue.getReportText());
			rowData.add(accessionNo);			
			rowData.add(reportLoaderQueue.getStatus());
			
			gridData.add(rowData);
		}
		return gridData;
	}
	private String getAccessionNo(String reportText)
	{
		String obrLine = ReportLoaderUtil.getLineFromReport(reportText, Parser.OBR);
		IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport = HL7Parser.extractOBRSegment(obrLine);
		String accessionNo = identifiedSurgicalPathologyReport.getSpecimenCollectionGroup().getSurgicalPathologyNumber(); //identifiedSurgicalPathologyReport.getAccessionNumber();
		return accessionNo;
	}

}
