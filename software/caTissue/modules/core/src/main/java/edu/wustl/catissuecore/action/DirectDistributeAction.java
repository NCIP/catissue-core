
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm;
import edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm;
import edu.wustl.catissuecore.actionForm.OrderSpecimenForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class DirectDistributeAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(DirectDistributeAction.class);

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

		this.logger.debug("Inside DirectDistributeSpecAction ");
		final String typeOf = request.getParameter("typeOf");
		Long orderId = null;
		if (typeOf.equals(Constants.SPECIMEN_ORDER_FORM_TYPE))
		{
			final OrderSpecimenForm orderSpecimenForm = (OrderSpecimenForm) form;
			orderId = orderSpecimenForm.getId();
		}
		else if (typeOf.equals(Constants.ARRAY_ORDER_FORM_TYPE))
		{
			final OrderBiospecimenArrayForm orderArrayForm = (OrderBiospecimenArrayForm) form;
			orderId = orderArrayForm.getId();
		}
		else
		{
			final OrderPathologyCaseForm pathologyForm = (OrderPathologyCaseForm) form;
			orderId = pathologyForm.getId();
		}
		this.logger.debug("order Id ::" + orderId);
		request.setAttribute("id", orderId.toString());

		return mapping.findForward("success");
	}
}
