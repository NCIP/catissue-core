package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.ComboDataBizLogic;
import edu.wustl.catissuecore.bizlogic.SiteBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Status;

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
			responseString.append(this.addRowToResponseXML(Long.valueOf(nvb.getValue()), nvb.getName()));
		}
		responseString.append(Constants.XML_ROWS_END);
		response.setContentType(Constants.CONTENT_TYPE_XML);
		response.getWriter().write(responseString.toString());
		return null;
	}

	private String addRowToResponseXML(Long identifier, String name)
	{
		StringBuffer responseString = new StringBuffer(Constants.XML_ROW_ID_START);
		responseString.append(identifier).append(Constants.XML_TAG_END).append(Constants.XML_CELL_START).append(
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
		List diagnosisList = comboDataBizObj.getClinicalDiagnosisList(query,false);
		AppUtility.writeListAsJSon(diagnosisList, request, response);
		return null;
	}
	
	public ActionForward getAllSiteList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ApplicationException
	{
		final String[] siteDisplayField = {"name"};
		final String valueField = "id";

		final String[] activityStatusArray = {Status.ACTIVITY_STATUS_DISABLED.toString(),
				Status.ACTIVITY_STATUS_CLOSED.toString()};
		
		final SiteBizLogic siteBizlog = new SiteBizLogic();
		final List siteResultList = siteBizlog.getRepositorySiteList(Site.class.getName(),
				siteDisplayField, valueField, activityStatusArray, false);
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
			responseString.append(this.addRowToResponseXML(Long.valueOf(nvb.getValue()), nvb.getName()));
		}
		responseString.append(Constants.XML_ROWS_END);
		response.setContentType(Constants.CONTENT_TYPE_XML);
		response.getWriter().write(responseString.toString());
		return null;
	}

}