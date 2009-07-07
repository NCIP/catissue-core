
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.DefineArrayForm;
import edu.wustl.catissuecore.actionForm.OrderForm;
import edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm;
import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;

/**
 * Action for ordering specimen.
 *
 * @author deepti_phadnis
 */
public class OrderPathologyCaseAction extends BaseAction
{

	/**
	 * @param mapping
	 *            ActionMapping object
	 * @param form
	 *            ActionForm object
	 * @param request
	 *            HttpServletRequest object
	 * @param response
	 *            HttpServletResponse object
	 * @return ActionForward object
	 * @throws Exception
	 *             object
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final OrderPathologyCaseForm pathology = (OrderPathologyCaseForm) form;
		final HttpSession session = request.getSession();
		pathology.setTypeOfCase("derivative");
		String target = null;

		if (session.getAttribute("OrderForm") != null)
		{
			final OrderForm orderForm = (OrderForm) session.getAttribute("OrderForm");
			pathology.setOrderForm(orderForm);
			if (orderForm.getDistributionProtocol() != null)
			{
				this.getProtocolName(request, pathology, orderForm);
			}
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
					.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
			final Collection pathologyCase = orderBizLogic.getPathologyDataFromDatabase(request);

			request.setAttribute("pathologyCase", pathologyCase);
			this.setClassAndTypeInList(request);
			this.setSiteAndStatus(request, pathology);

			final List orderToListArrayCollection = new ArrayList();
			orderToListArrayCollection.add(new NameValueBean("None", "None"));

			if (session.getAttribute("DefineArrayFormObjects") != null)
			{
				final List arrayList = (ArrayList) session.getAttribute("DefineArrayFormObjects");
				final Iterator arrayListItr = arrayList.iterator();
				while (arrayListItr.hasNext())
				{
					final DefineArrayForm defineArrayFormObj = (DefineArrayForm) arrayListItr
							.next();
					orderToListArrayCollection.add(new NameValueBean(defineArrayFormObj
							.getArrayName(), defineArrayFormObj.getArrayName()));
				}
			}
			// Add the collection in request scope to be used in the
			// OrderItem.jsp
			request.setAttribute(Constants.ORDERTO_LIST_ARRAY, orderToListArrayCollection);

			request.setAttribute("typeOf", "pathologyCase");
			request.setAttribute("OrderPathologyCaseForm", pathology);
			target = Constants.SUCCESS;
		}
		else
		{
			target = Constants.FAILURE;
		}
		return mapping.findForward(target);
	}

	/**
	 * function for setting the class and type.
	 *
	 * @param request
	 *            HttpServletRequest object
	 */
	private void setClassAndTypeInList(HttpServletRequest request)
	{
		// Setting specimen class list
		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_CLASS, null);

		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		// Setting the specimen type list
		final List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_TYPE, null);
		request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);

		// Get the Specimen class and type from the cde
		final CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(
				Constants.CDE_NAME_SPECIMEN_CLASS);
		final Set setPV = specimenClassCDE.getPermissibleValues();
		final Iterator itr = setPV.iterator();

		specimenClassList = new ArrayList();
		final Map subTypeMap = new HashMap();
		specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));

		while (itr.hasNext())
		{
			final List innerList = new ArrayList();
			final Object obj = itr.next();
			final PermissibleValue pv = (PermissibleValue) obj;
			final String tmpStr = pv.getValue();
			specimenClassList.add(new NameValueBean(tmpStr, tmpStr));

			final Set list1 = pv.getSubPermissibleValues();
			final Iterator itr1 = list1.iterator();
			innerList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
			while (itr1.hasNext())
			{
				final Object obj1 = itr1.next();
				final PermissibleValue pv1 = (PermissibleValue) obj1;
				// set specimen type
				final String tmpInnerStr = pv1.getValue();
				innerList.add(new NameValueBean(tmpInnerStr, tmpInnerStr));
			}
			subTypeMap.put(pv.getValue(), innerList);
		} // class and values set

		// specimenClassList.remove(1);

		// sets the Class list
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		// set the map to subtype
		request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);

	}

	/**
	 * @param request
	 *            HttpServletRequest object
	 * @param pathology
	 *            OrderPathologyCaseForm object
	 */
	private void setSiteAndStatus(HttpServletRequest request, OrderPathologyCaseForm pathology)
	{

		// NameValueBean bean = null;
		// Setting tissue site list
		final List tissueSiteList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_TISSUE_SITE, null);
		request.setAttribute(Constants.TISSUE_SITE_LIST, tissueSiteList);

		// Setting pathological status list
		final List pathologicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_PATHOLOGICAL_STATUS, null);

		request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, pathologicalStatusList);
	}

	/**
	 * @param request
	 *            HttpServletRequest object
	 * @param pathology
	 *            OrderPathologyCaseForm object
	 * @param orderForm
	 *            OrderForm object
	 * @throws Exception
	 *             object
	 */
	private void getProtocolName(HttpServletRequest request, OrderPathologyCaseForm pathology,
			OrderForm orderForm) throws Exception
	{
		// to get the distribution protocol name
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
				.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
		final List protocolList = orderBizLogic.getDistributionProtocol(request);

		for (int i = 0; i < protocolList.size(); i++)
		{
			final NameValueBean obj = (NameValueBean) protocolList.get(i);

			if (orderForm.getDistributionProtocol().equals(obj.getValue()))
			{
				pathology.setDistrbutionProtocol(obj.getName());
			}
		}
	}

}
