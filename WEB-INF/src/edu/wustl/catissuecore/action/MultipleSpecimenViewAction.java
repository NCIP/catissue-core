
package edu.wustl.catissuecore.action;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;

/**
 * @author abhijit_naik
 *
 */
public class MultipleSpecimenViewAction extends BaseAction
{
	/**
	 * logger.
	 */

	private transient Logger logger = Logger.getCommonLogger(MultipleSpecimenViewAction.class);
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

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		ViewSpecimenSummaryForm summaryForm = (ViewSpecimenSummaryForm) form;

		HttpSession session = request.getSession();
		try
		{
			LinkedHashMap < String , CollectionProtocolEventBean > cpEventMap =
				new LinkedHashMap < String , CollectionProtocolEventBean >();
			CollectionProtocolEventBean eventBean = new CollectionProtocolEventBean();
			String pageOf = request.getParameter(Constants.PAGE_OF);
			eventBean.setUniqueIdentifier("EventID-1");
			LinkedHashMap specimenCollection = (LinkedHashMap) session
					.getAttribute(Constants.SPECIMEN_LIST_SESSION_MAP);

			eventBean.setSpecimenRequirementbeanMap(specimenCollection);

			String globalSpecimenId = "E" + eventBean.getUniqueIdentifier() + "_";
			cpEventMap.put(globalSpecimenId, eventBean);
			session.removeAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
			session.setAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP, cpEventMap);
			summaryForm.setShowCheckBoxes(true);//bug 12531
			summaryForm.setShowbarCode(!Variables.isSpecimenBarcodeGeneratorAvl);
			summaryForm.setShowLabel(!Variables.isSpecimenLabelGeneratorAvl);
			if (Constants.EDIT.equals(request.getParameter("mode")))
			{
				summaryForm.setUserAction(ViewSpecimenSummaryForm.UPDATE_USER_ACTION);
				summaryForm.setShowParentStorage(false);
				summaryForm.setShowbarCode(true);
				summaryForm.setShowLabel(true);
				summaryForm.setMultipleSpEditMode(true);

			}
			request.setAttribute("RequestType", "");
			if (pageOf != null)
			{
				return mapping.findForward(pageOf);
			}
			return mapping.findForward(Constants.SUCCESS);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return null;

	}

}
