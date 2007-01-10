package edu.wustl.catissuecore.action;

import edu.wustl.common.action.BaseAction;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm;
import edu.wustl.catissuecore.actionForm.OrderForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.DistributionBizLogic;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

public class OrderBiospecimenArrayAction extends BaseAction
{
	/**
	 * @param mapping ActionMapping object
	 * @param form ActionForm object
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @return ActionForward object
	 * @throws Exception object
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
			{

		OrderBiospecimenArrayForm arrayObject = (OrderBiospecimenArrayForm) form;
		HttpSession session = request.getSession();

		if (session.getAttribute("OrderForm") != null) 
		{
			OrderForm orderForm = (OrderForm) session.getAttribute("OrderForm");
			arrayObject.setOrderForm(orderForm);

			if (orderForm.getDistributionProtocol() != null) 
			{
				getProtocolName(request, arrayObject, orderForm);
			}
			try 
			{

				IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(
						Constants.NEW_SPECIMEN_FORM_ID);

				String sourceObjectName = SpecimenArray.class.getName();
				String []columnName = { "name" };
				List specimenArrayList = bizLogic.retrieve(sourceObjectName);
				request.setAttribute("SpecimenNameList", specimenArrayList);

			}
			catch (Exception e)
			{
				Logger.out.error(e.getMessage(), e);
				return null;
			}

			request.setAttribute("typeOf", "specimenArray");
			request.setAttribute("OrderBiospecimenArrayForm", arrayObject);
			return mapping.findForward("success");
		} 
		else 
		{
			return mapping.findForward("failure");

		}
	}

	/**
	 * @param request HttpServletRequest object
	 * @param arrayObject OrderBiospecimenArrayForm object
	 * @param orderForm OrderForm object
	 * @throws Exception object
	 */
	private void getProtocolName(HttpServletRequest request,
			OrderBiospecimenArrayForm arrayObject, OrderForm orderForm)
			throws Exception
			{
		// to get the distribution protocol name
		DistributionBizLogic dao = (DistributionBizLogic) BizLogicFactory
				.getInstance().getBizLogic(Constants.DISTRIBUTION_FORM_ID);

		String sourceObjectName = DistributionProtocol.class.getName();
		String[] displayName = { "title" };
		String valueField = Constants.SYSTEM_IDENTIFIER;
		List protocolList = dao.getList(sourceObjectName, displayName,
				valueField, true);

		request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, protocolList);

		for (int i = 0; i < protocolList.size(); i++) 
		{
			NameValueBean obj = (NameValueBean) protocolList.get(i);

			if (orderForm.getDistributionProtocol().equals(obj.getValue())) 
			{
				arrayObject.setDistrbutionProtocol(obj.getName());
			}
		}
	}

	/**
	 * @param request HttpServletRequest object
	 * @return List specimen array objects
	 */
	private List getDataFromDatabase(HttpServletRequest request) 
	{
		// to get data from database when specimen id is given
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(
				Constants.NEW_SPECIMEN_FORM_ID);
		HttpSession session = request.getSession(true);

		try 
		{
			String sourceObjectName = Specimen.class.getName();
			String columnName = "id";

			List valueField = (List) session.getAttribute("specimenId");
			List specimenArrayList = new ArrayList();
			for (int i = 0; i < valueField.size(); i++) 
			{
				List specimenList = bizLogic.retrieve(sourceObjectName,
						columnName, (String) valueField.get(i));
				Specimen speclist = (Specimen) specimenList.get(0);
				specimenArrayList.add(speclist);
			}
			return specimenArrayList;
		}
		catch (DAOException e)
		{
			Logger.out.error(e.getMessage(), e);
			return null;
		}
	}

}
