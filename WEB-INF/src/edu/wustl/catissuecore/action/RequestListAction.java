/**
 * <p>
 * Title: RequestListAction Class>
 * <p>
 * Description: This class initializes the fields of
 * RequestListAdministratorView.jsp Page
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Ashish Gupta
 * @version 1.00 Created on Oct 04,2006
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.wustl.catissuecore.actionForm.RequestListFilterationForm;
import edu.wustl.catissuecore.bean.RequestViewBean;
import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Validator;

/**
 * @author renuka_bajpai
 *
 */
public class RequestListAction extends SecureAction
{

	/**
	 * Overrides the execute method of Action class. Initializes the various
	 * fields in RequestListAdministratorView.jsp Page.
	 *
	 * @param mapping
	 *            object
	 * @param form
	 *            object
	 * @param request
	 *            object
	 * @param response
	 *            object
	 * @return ActionForward object
	 * @throws Exception
	 *             object
	 */
	@Override
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final RequestListFilterationForm requestListForm = (RequestListFilterationForm) form;
		String requestFor = request.getParameter("requestFor");

		if(!Validator.isEmpty(requestFor) && requestFor.equals("gridData"))
		{
				setGridData(requestListForm, request, response);
				
				return null;
				
		}
		List columnList = new ArrayList();
		columnList.add("Order Name");
		columnList.add("Distribution Protocol");
		columnList.add("PI of Distribution Protocol");
		columnList.add("Requested Date");
		columnList.add("Order Status");
		
		request.setAttribute("columns", AppUtility.getcolumns(columnList));
		setNumberOfRequests(requestListForm);
		return mapping.findForward("success");
	}


	private void setGridData(RequestListFilterationForm requestListForm,
			HttpServletRequest request, HttpServletResponse response) throws BizLogicException {
		List<RequestViewBean> requestViewBeanList;
		final SessionDataBean sessionData = this.getSessionData(request);
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
				.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
		requestViewBeanList = orderBizLogic.getRequestList(requestListForm
				.getRequestStatusSelected(), sessionData.getUserName(), sessionData
				.getUserId());
		
		try
		{
			JSONArray jsonDataRow = new JSONArray();
			JSONObject mainJsonRow = new JSONObject();
		if (!requestViewBeanList.isEmpty())
		{
			int i = 0 ;
			for (RequestViewBean bean : requestViewBeanList)
			{
				JSONObject jsonObject = new JSONObject();
				JSONArray dataArray = new JSONArray();
				dataArray.put("<a href='RequestDetails.do?id="+bean.getRequestId()+"'>"+bean.getOrderName()+"</a>");
				dataArray.put(bean.getDistributionProtocol());
				dataArray.put(bean.getRequestedBy());
				dataArray.put(bean.getRequestedDate());
				dataArray.put(bean.getStatus());
				jsonObject.put("data", dataArray);
				jsonObject.put("id", i++);
				jsonDataRow.put(jsonObject);
			}
			mainJsonRow.put("rows", jsonDataRow);
			response.setContentType("json");
			response.getWriter().write(mainJsonRow.toString());
		}
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	}


	/**
	 *
	 * @param requestListFilterationForm : requestListFilterationForm
	 * @param userName : userName
	 * @param userId : userId
	 * @throws BizLogicException : BizLogicException
	 */
	private void setNumberOfRequests(RequestListFilterationForm requestListFilterationForm
			) throws BizLogicException
	{

		int newStatus = 0, pendingStatus = 0;
		OrderBizLogic bizLogic = new OrderBizLogic();
		newStatus = bizLogic.getOrderCount("New");
		pendingStatus = bizLogic.getOrderCount("Pending");
		requestListFilterationForm.setNewRequests(newStatus);
		requestListFilterationForm.setPendingRequests(pendingStatus);

	}


}