
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
import edu.wustl.catissuecore.actionForm.OrderSpecimenForm;
import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 *
 */
public class OrderSpecimenInitAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(OrderSpecimenInitAction.class);

	/**
	 * @param mapping ActionMapping object
	 * @param form ActionForm object
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @return ActionForward object
	 * @throws Exception object
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final OrderSpecimenForm spec = (OrderSpecimenForm) form;
		final HttpSession session = request.getSession();
		spec.setTypeOfSpecimen("existingSpecimen");
		String target = null;

		if (session.getAttribute("OrderForm") != null)
		{
			final OrderForm orderForm = (OrderForm) session.getAttribute("OrderForm");
			spec.setOrderForm(orderForm);
			if (orderForm.getDistributionProtocol() != null)
			{
				this.getProtocolName(request, spec, orderForm);
			}

			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
					.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
			final Collection specimen = orderBizLogic.getSpecimenDataFromDatabase(request);

			try
			{
				request.setAttribute("specimen", specimen);

				//Setting specimen class list
				List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(
						Constants.CDE_NAME_SPECIMEN_CLASS, null);
				request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

				//Setting the specimen type list
				final List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(
						Constants.CDE_NAME_SPECIMEN_TYPE, null);
				request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);

				// Get the Specimen class and type from the cde
				final CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(
						Constants.CDE_NAME_SPECIMEN_CLASS);
				final Set setPV = specimenClassCDE.getPermissibleValues();

				specimenClassList = new ArrayList();
				final Map subTypeMap = this.getSubTypeMap(setPV, specimenClassList);

				// sets the Class list
				request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

				// set the map to subtype
				request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);

				//default checked on existing specimen
				spec.setTypeOfSpecimen("existingSpecimen");

			}

			catch (final Exception excp)
			{
				this.logger.error(excp.getMessage(), excp);
			}

			request.setAttribute("typeOf", "specimen");
			request.setAttribute("OrderSpecimenForm", spec);

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
			//Add the collection in request scope to be used in the OrderItem.jsp
			request.setAttribute(Constants.ORDERTO_LIST_ARRAY, orderToListArrayCollection);

			target = Constants.SUCCESS;
		}
		else
		{
			target = Constants.FAILURE;
		}
		return mapping.findForward(target);
	}

	/**
	 *
	 * @param setPV : setPV
	 * @param specimenClassList : specimenClassList
	 * @return Map : Map
	 */
	private Map getSubTypeMap(Set setPV, List specimenClassList)
	{
		final Iterator itr = setPV.iterator();

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
		}
		return subTypeMap;
	}

	/** function for getting protocol name.
	 * @param request HttpServletRequest object
	 * @param spec OrderSpecimenForm object
	 * @param orderForm OrderForm object
	 * @throws Exception object
	 */
	private void getProtocolName(HttpServletRequest request, OrderSpecimenForm spec,
			OrderForm orderForm) throws Exception
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
				.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
		final List protocolList = orderBizLogic.getDistributionProtocol(request);

		for (int i = 0; i < protocolList.size(); i++)
		{
			final NameValueBean obj = (NameValueBean) protocolList.get(i);

			if (orderForm.getDistributionProtocol().equals(obj.getValue()))
			{
				spec.setDistrbutionProtocol(obj.getName());
			}
		}
	}

	/** function for getting data from database
	 * @param request HttpServletRequest object
	 * @return List of specimen objects
	 * @throws BizLogicException
	 */

}
