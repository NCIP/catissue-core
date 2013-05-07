
package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.DefineArrayForm;
import edu.wustl.catissuecore.bizlogic.SpecimenArrayBizLogic;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class DefineArrayAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(DefineArrayAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
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
		final DefineArrayForm defineArray = (DefineArrayForm) form;
		final HttpSession session = request.getSession();
		if (session.getAttribute("OrderForm") != null)
		{
			try
			{
				final String[] arrayTypeLabelProperty = {"name"};
				final String arrayTypeProperty = "id";
				final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
				final SpecimenArrayBizLogic specimenArrayBizLogic = (SpecimenArrayBizLogic) factory
						.getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);
				List specimenArrayTypeList = new ArrayList();

				specimenArrayTypeList = specimenArrayBizLogic.getList(SpecimenArrayType.class
						.getName(), arrayTypeLabelProperty, arrayTypeProperty, true);
				for (final Iterator iter = specimenArrayTypeList.iterator(); iter.hasNext();)
				{
					final NameValueBean nameValueBean = (NameValueBean) iter.next();
					// remove ANY entry from array type list
					if (nameValueBean.getValue().equals(Constants.ARRAY_TYPE_ANY_VALUE)
							&& nameValueBean.getName().equalsIgnoreCase(
									Constants.ARRAY_TYPE_ANY_NAME))
					{
						iter.remove();
						break;
					}
				}
				request.setAttribute(Constants.SPECIMEN_ARRAY_TYPE_LIST, specimenArrayTypeList);
			}
			catch (final Exception e)
			{
				this.logger.error(e.getMessage(), e);
				return null;
			}

			if (request.getParameter("arrayType") != null)
			{

				final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
				final IBizLogic bizLogic = factory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);

				final String sourceObjectName = SpecimenArrayType.class.getName();

				final Object object = bizLogic.retrieve(sourceObjectName, new Long(request
						.getParameter("arrayType")));
				final SpecimenArrayType containerTyperow = (SpecimenArrayType) object;
				// SpecimenArrayType
				// spec=(SpecimenArrayType)containerType.get(0);

				final Capacity capacityobj = containerTyperow.getCapacity();

				defineArray.setDimenmsionX(Integer.valueOf(capacityobj.getOneDimensionCapacity())
						.toString());
				defineArray.setDimenmsionY(Integer.valueOf(capacityobj.getTwoDimensionCapacity())
						.toString());
				defineArray.setArrayClass(containerTyperow.getSpecimenClass());

				final String dimen = Integer.valueOf(capacityobj.getOneDimensionCapacity()).toString()
						+ ":" + new Integer(capacityobj.getTwoDimensionCapacity()).toString() + ":"
						+ defineArray.getArrayClass();

				final PrintWriter out = response.getWriter();
				response.setContentType("text/html");
				out.write(dimen);
				return null;

			}
			String typeOf = null;
			typeOf = request.getParameter("typeOf");

			if (typeOf == null)
			{
				typeOf = request.getAttribute("typeOf").toString();
			}

			request.setAttribute("typeOf", typeOf);

			request.setAttribute("DefineArrayForm", defineArray);
			return mapping.findForward("success");
		}
		else
		{
			return mapping.findForward("failure");
		}
	}
}
