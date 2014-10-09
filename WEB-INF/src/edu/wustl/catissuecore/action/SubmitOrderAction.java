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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.google.gson.Gson;

import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.OrderStatusDTO;
import edu.wustl.catissuecore.dto.OrderSubmissionDTO;
import edu.wustl.catissuecore.util.OrderingSystemUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dao.HibernateDAO;

/**
 * @author renuka_bajpai
 */
public class SubmitOrderAction extends BaseAction
{

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

		String dataJSON = request.getParameter("dataJSON");
		Gson gson = AppUtility.initGSONBuilder().create();
		
		OrderSubmissionDTO orderSubmissionDTO = gson.fromJson(dataJSON, OrderSubmissionDTO.class);

		JSONObject jsonObject = new JSONObject(dataJSON);
		orderSubmissionDTO.setOrderItemSubmissionDTOs(OrderingSystemUtil
				.getOrderItemDTOs(jsonObject.get("gridXML").toString()));

		OrderBizLogic orderBizLogic = new OrderBizLogic();

		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
		Long userId = sessionDataBean.getUserId();
		HibernateDAO dao = null;
		StringBuilder errors = new StringBuilder();
		try
		{

			dao = (HibernateDAO) AppUtility.openDAOSession(sessionDataBean);
			OrderStatusDTO orderStatusDTO = orderBizLogic.updateOrder(orderSubmissionDTO, userId,
					dao);

			if (orderStatusDTO.getOrderErrorDTOs().isEmpty())
			{
				Map<String, Object> csvFileData = orderBizLogic.getOrderCsv(
						orderStatusDTO.getOrderId(), getExportedName(sessionDataBean), dao,"");
				UserBizLogic userBizLogic=new UserBizLogic();
				dao.commit();
				User user = userBizLogic.getUserNameById(orderSubmissionDTO.getRequestorId(), dao);
				orderBizLogic
						.sendOrderUpdateEmail(user.getFirstName(), user.getLastName(),
								orderSubmissionDTO.getRequestorEmail(),
								sessionDataBean.getUserName(),
								orderSubmissionDTO.getOrderName(),
								sessionDataBean.getLastName(),
										 sessionDataBean.getFirstName(),
								orderStatusDTO.getStatus(), orderStatusDTO.getOrderId(), csvFileData);
				response.getWriter().write("Success");

			}
			else
			{
				dao.rollback();
				String json = gson.toJson(orderStatusDTO);
				response.getWriter().write(json);
			}

		}
		catch (Exception exception)
		{
			dao.rollback();
			response.getWriter().write(errors.toString());
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}
		return null;
	}

	private String getExportedName(SessionDataBean sessionDataBean)
	{
		String firstName = sessionDataBean.getFirstName() == null ? "" : sessionDataBean
				.getFirstName();
		String lastName = sessionDataBean.getLastName() == null ? "" : sessionDataBean
				.getLastName();
		return (lastName + " " + firstName).trim();
	}
}