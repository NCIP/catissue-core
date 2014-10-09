/**
 * <p>
 * Title: RequestDetailsAction Class>
 * <p>
 * Description: This class initializes the fields of RequestDetails.jsp Page
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Ashish Gupta
 * @version 1.00 Created on Oct 05,2006
 */

package edu.wustl.catissuecore.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.bizlogic.SiteBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.dto.DisplayOrderDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.security.manager.SecurityManagerFactory;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * @author renuka_bajpai
 */
public class OrderAction extends BaseAction
{

	/**
	 * logger.
	 */

	private static final Logger LOGGER = Logger.getCommonLogger(RequestDetailsAction.class);
	private StringBuilder speciemnIdValue; 

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
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
			{
		
		final List<NameValueBean> requestedItemsStatusList = new ArrayList<NameValueBean>();
		for (String status : Constants.STATUS_LIST) {
			requestedItemsStatusList.add(new NameValueBean(status,status));
		}
		
		final String[] displayNameFields = {"name"};
		final String[] activityStatusArray = {Status.ACTIVITY_STATUS_DISABLED.toString(),Status.ACTIVITY_STATUS_CLOSED.toString()};
		SiteBizLogic siteBizLogic=new SiteBizLogic();
		List sites=siteBizLogic.getAllSiteList(Site.class.getName(), displayNameFields, Constants.SYSTEM_IDENTIFIER, activityStatusArray, true);
		
		HibernateDAO dao=null;
		try
		{
			Long orderId=null;
			if(request.getParameter("id")==null || request.getParameter("id").isEmpty())
			{
				orderId=Long.parseLong(request.getAttribute("id").toString());
			}
			else
			{
				orderId=Long.parseLong(request.getParameter("id"));
			}
			SessionDataBean sessiondataBean=(SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
			dao=(HibernateDAO)AppUtility.openDAOSession(sessiondataBean);
			OrderBizLogic orderBizLogic=new OrderBizLogic();
			DisplayOrderDTO displayOrderDTO=orderBizLogic.getOrderDetails(orderId,dao);
			request.setAttribute("DisplayOrderDTO", displayOrderDTO);
			request.setAttribute("sites", sites);
			request.setAttribute(Constants.ITEM_STATUS_LIST, requestedItemsStatusList);
			request.setAttribute("id", orderId);
			request.setAttribute("status", Constants.STATUS_LIST);
			request.setAttribute("datePattern", ApplicationProperties.getValue("date.pattern"));
			request.setAttribute("distributeStatusList", orderBizLogic.DISTRIBUTE_STATUS_LIST);
			
			final long csmUserId = new Long(sessiondataBean.getCsmUserId()).longValue();
			final Role role = SecurityManagerFactory.getSecurityManager().getUserRole(csmUserId);
			final List distributionProtocolList = orderBizLogic.loadDistributionProtocol(
					sessiondataBean.getUserId(), role.getName(), sessiondataBean,"shortTitle");
			request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, distributionProtocolList);
			
			UserBizLogic userBizLogic=new UserBizLogic();
			final List<NameValueBean> users=userBizLogic.getUsersNameValueList(null);
			users.add(new NameValueBean(Constants.SELECT_OPTION, String.valueOf(Constants.SELECT_OPTION_VALUE)));
			request.setAttribute(Constants.USERLIST, users);
			request.setAttribute("distriTree", readFile());
			
		}
		finally
		{
			dao.closeSession();
		}
		return mapping.findForward("success");
	}
	
	private String readFile() throws IOException {
		BufferedReader reader = null;
		String treeXml ="";
		try{
			String server_file_path =  CommonServiceLocator.getInstance().getPropDirPath()
					+ File.separator+"distriExport.xml";
    reader = new BufferedReader( new FileReader(server_file_path));
    String         line = null;
    StringBuilder  stringBuilder = new StringBuilder();
    String         ls = System.getProperty("line.separator");
    while( ( line = reader.readLine() ) != null ) {
        stringBuilder.append( line );
        stringBuilder.append( ls );
    }
    treeXml = stringBuilder.toString();
    treeXml = treeXml.replace("\n", "").replace("\r", "");
		}finally{
			if(reader!=null){
				reader.close();
			}
		}
    return treeXml;
}
}