package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.ComboDataBizLogic;
import edu.wustl.catissuecore.bizlogic.SiteBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerForSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Status;
import edu.wustl.dao.DAO;

public class CatissueCommonAjaxAction extends DispatchAction{
	
	/**
	 * Returns list of all the User of this application.
	 *
	 * @param mapping the mapping
	 * @param form the form
	 * @param request the request
	 * @param response the response
	 * @return the action forward
	 * @throws ApplicationException the application exception
	 * @throws IOException 
	 */
	public ActionForward allUserList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ApplicationException, IOException
	{
		StringBuffer responseString = new StringBuffer(Constants.XML_START);
		List<NameValueBean> userList = userList(request);
		responseString.append(Constants.XML_ROWS);
		for (NameValueBean nvb : userList)
		{
			responseString.append(this.addRowToResponseXML(Long.valueOf(nvb.getValue()),null, nvb.getName()));
		}
		responseString.append(Constants.XML_ROWS_END);
		response.setContentType(Constants.CONTENT_TYPE_XML);
		response.getWriter().write(responseString.toString());
		return null;
	}

	/**
	 * This function returns result in xml fromat to populate DHTMLX DropDown combo box.
	 * @param identifier if list is of (String,Long) type pass long value as row id
	 * @param stringValue if list is of (String,String) type pass String value as row id
	 * @param name - this is a display name in DropDown
	 * @return
	 */
	private String addRowToResponseXML(Long identifier,String stringValue, String name)
	{
		StringBuffer responseString = new StringBuffer(Constants.XML_ROW_ID_START);
		responseString.append((identifier==null?stringValue:identifier)).append(Constants.XML_TAG_END).append(Constants.XML_CELL_START).append(
						Constants.XML_CDATA_START).append(name).append(
						Constants.XML_CDATA_END).append(Constants.XML_CELL_END).append(Constants.XML_ROW_END);
		return responseString.toString();
	}
	
	public ActionForward getUserListAsJSon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ApplicationException, IOException
	{
		AppUtility.writeListAsJSon(userList(request), request, response);
		return null;
	}
	
	private List<NameValueBean> userList(HttpServletRequest request) throws BizLogicException
	{
		String operation = request.getParameter(Constants.OPERATION);
		if (operation == null)
		{
			operation = (String) request.getAttribute(Constants.OPERATION);
		}
		UserBizLogic userBizLogic = new UserBizLogic();
		final List<NameValueBean> userCollection = userBizLogic.getUsersNameValueList(operation);
		return userCollection;
	}
	
	/**
	 * This returns list of Clinical Diagnosis which contains specified string as an query.  
	 */
	public ActionForward getClinicalDiagnosisValues(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ApplicationException, IOException
	{
		String query = request.getParameter("query");
		ComboDataBizLogic comboDataBizObj = new ComboDataBizLogic();
		List<NameValueBean> diagnosisList = comboDataBizObj.getClinicalDiagnosisList(query,false);
		AppUtility.writeListAsJSon(diagnosisList, request, response);
		return null;
	}
	
	public ActionForward getAllSiteList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ApplicationException, IOException
	{
		final String[] siteDisplayField = {"name"};
		final String valueField = "id";

		final String[] activityStatusArray = {Status.ACTIVITY_STATUS_DISABLED.toString(),
				Status.ACTIVITY_STATUS_CLOSED.toString()};
		
		final SiteBizLogic siteBizlog = new SiteBizLogic();
		final List<NameValueBean> siteResultList = siteBizlog.getAllSiteList(Site.class.getName(),
				siteDisplayField, valueField, activityStatusArray, false);
		StringBuffer responseString = new StringBuffer(Constants.XML_START);
		NameValueBean selectBean = new NameValueBean("-- Select --",Long.valueOf(-1));
		siteResultList.remove(siteResultList.indexOf(selectBean));
		responseString.append(Constants.XML_ROWS);
		for (NameValueBean nvb : siteResultList)
		{
			responseString.append(this.addRowToResponseXML(Long.valueOf(nvb.getValue()),null, nvb.getName()));
		}
		responseString.append(Constants.XML_ROWS_END);
		response.setContentType(Constants.CONTENT_TYPE_XML);
		response.getWriter().write(responseString.toString());
		return null;
	}
	
	public ActionForward getStorageContainerList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ApplicationException, IOException
	{
		List <NameValueBean>containerList=new ArrayList();
		String contName=request.getParameter(Constants.CONTAINER_NAME);
		String stContSelection=request.getParameter("stContSelection");
		if(contName!=null || (null!=stContSelection && Integer.valueOf(2).equals(Integer.valueOf(stContSelection))))
		{
			final SessionDataBean sessionData = (SessionDataBean) request.getSession()
					.getAttribute(Constants.SESSION_DATA);
			DAO dao = AppUtility.openDAOSession(sessionData);
			long cpId=0;
			if(!"".equals(request.getParameter(Constants.CAN_HOLD_COLLECTION_PROTOCOL)))
			{
				cpId=Long.parseLong(request.getParameter(Constants.CAN_HOLD_COLLECTION_PROTOCOL));
			}
			String spType=request.getParameter("specimenType");
			String spClass=request.getParameter(Constants.CAN_HOLD_SPECIMEN_CLASS);
			
			
			StorageContainerForSpecimenBizLogic bizLogic=new StorageContainerForSpecimenBizLogic();
			TreeMap treeMap=bizLogic.getAutoAllocatedContainerListForSpecimen(AppUtility.setparameterList(cpId,spClass,0,spType), sessionData, dao, contName);
			if(treeMap!=null)
			{
				containerList=AppUtility.convertMapToList(treeMap);
			}
		}
		StringBuffer responseString = new StringBuffer(Constants.XML_START);
		NameValueBean virtualBean = new NameValueBean("Virtual",Long.valueOf(-1));
		//containerList.remove(containerList.indexOf(selectBean));
		responseString.append(Constants.XML_ROWS);
		for (NameValueBean nvb : containerList)
		{
			responseString.append(this.addRowToResponseXML(Long.valueOf(nvb.getValue()),null, nvb.getName()));
		}
		responseString.append(this.addRowToResponseXML(Long.valueOf(virtualBean.getValue()),null, virtualBean.getName()));
		responseString.append(Constants.XML_ROWS_END);
		response.setContentType(Constants.CONTENT_TYPE_XML);
		response.getWriter().write(responseString.toString());
		return null;
	}

	/**
	 * This function returns list of all active collection protocols
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ApplicationException
	 * @throws IOException 
	 */
	public ActionForward getAllCPList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ApplicationException, IOException
	{
		CollectionProtocolBizLogic cpBizLogic = new CollectionProtocolBizLogic();
		List<NameValueBean> cpList = cpBizLogic.getAllCPNameValueBeanList();
		StringBuffer responseString = new StringBuffer(Constants.XML_START);
		responseString.append(Constants.XML_ROWS);
		for (NameValueBean nvb : cpList)
		{
			responseString.append(this.addRowToResponseXML(Long.valueOf(nvb.getValue()),null, nvb.getName()));
		}
		responseString.append(Constants.XML_ROWS_END);
		response.setContentType(Constants.CONTENT_TYPE_XML);
		response.getWriter().write(responseString.toString());
		return null;
	}
	
	public ActionForward getClinicalStatusList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ApplicationException, IOException
	{
		List<NameValueBean> csNameValueBeanList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_CLINICAL_STATUS, null);
		NameValueBean selectBean = new NameValueBean("-- Select --",Long.valueOf(-1));
		csNameValueBeanList.remove(csNameValueBeanList.indexOf(selectBean));
		StringBuffer responseString = new StringBuffer(Constants.XML_START);
		responseString.append(Constants.XML_ROWS);
		for (NameValueBean nvb : csNameValueBeanList)
		{
			responseString.append(this.addRowToResponseXML(null,nvb.getValue(), nvb.getName()));
		}
		responseString.append(Constants.XML_ROWS_END);
		response.setContentType(Constants.CONTENT_TYPE_XML);
		response.getWriter().write(responseString.toString());
		return null;
	}

}