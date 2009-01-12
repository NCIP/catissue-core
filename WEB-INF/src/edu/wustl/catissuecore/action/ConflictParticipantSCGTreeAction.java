/**
 * <p>Title: ConflictParticipantSCGTreeAction Class>
 * <p>Description:	ConflictParticipantSCGTreeAction class: it sets the tree node
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 *@author kalpana Thakur
 * Created on sep 18,2007
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ReportLoaderQueueBizLogic;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.dbManager.DAOException;

public class ConflictParticipantSCGTreeAction extends BaseAction{
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String reportQueueId = (String)request.getParameter(Constants.REPORT_ID);	
		ReportLoaderQueue reportLoaderQueue =null;
		ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic)BizLogicFactory.getInstance().getBizLogic(ReportLoaderQueue.class.getName());
		
		Vector treeData = new Vector();
		List reportQueueData = new ArrayList();
		Iterator iter=null;
		reportQueueData = getReportQueueDataList(reportQueueId);
		iter = reportQueueData.iterator();
		if(iter.hasNext())
		{
			reportLoaderQueue = (ReportLoaderQueue)iter.next();
		}
		String siteName = (String)reportLoaderQueue.getSiteName();
		Long reportId = (Long)reportLoaderQueue.getId();
		
		//To retrieve the tree data  
		treeData=reportLoaderQueueBizLogic.getTreeViewData(reportId,siteName,treeData); 
		request.setAttribute("treeData", treeData);
	
		return mapping.findForward(Constants.SUCCESS);
		
	}
	/**
	 * To retieve the Report Queue List
	 * @param reportQueueId
	 * @return
	 * @throws DAOException
	 */
	private List getReportQueueDataList(String reportQueueId) throws DAOException
	{
		ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic)BizLogicFactory.getInstance().getBizLogic(ReportLoaderQueue.class.getName());
	    List reportQueueList = (List)reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class.getName(),Constants.SYSTEM_IDENTIFIER, new Long(reportQueueId));
		return reportQueueList;		
	}
	
}
