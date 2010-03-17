
package edu.wustl.catissuecore.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

/**
 * @author renuka_bajpai
 *
 */
public class DefineEventsAction extends BaseAction
{

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
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		final CollectionProtocolForm cpForm = (CollectionProtocolForm) form;

		final String pageOf = request.getParameter(Constants.PAGE_OF);
		final String operation = request.getParameter("operation");
		request.setAttribute("operation", operation);
		final String invokeFunction = request.getParameter("invokeFunction");
		request.setAttribute("invokeFunction", invokeFunction);
		final HttpSession session = request.getSession();

		CollectionProtocolBean cpBean = (CollectionProtocolBean) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		if (cpBean == null)
		{
			cpBean = new CollectionProtocolBean();
		}
		if (cpForm.getShortTitle() != null)
		{
			this.populateCollectionProtocolBean(cpForm, cpBean);
			CollectionProtocolUtil.updateClinicalDiagnosis(request, cpBean);
		}
		final Long cpIdentifier = cpBean.getIdentifier();
		session.setAttribute("CP_IDENTIFIER", cpIdentifier);
		session.setAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN, cpBean);
		return mapping.findForward(pageOf);
	}

	/**
	 *
	 * @param cpForm : cpForm
	 * @param cpBean : cpBean
	 */
	private void populateCollectionProtocolBean(final CollectionProtocolForm cpForm,
			CollectionProtocolBean cpBean)
	{
		cpBean.setPrincipalInvestigatorId(cpForm.getPrincipalInvestigatorId());
		long[] coordinatorsId = (long[])cpForm.getCoordinatorIds();
		if(coordinatorsId[0] != 0)
		{
		  cpBean.setCoordinatorIds(cpForm.getCoordinatorIds());
		}
		/**For Clinical Diagnosis Subset  **/
		cpBean.setClinicalDiagnosis(cpForm.getProtocolCoordinatorIds());
		cpBean.setTitle(cpForm.getTitle());
		cpBean.setShortTitle(cpForm.getShortTitle());
		cpBean.setStartDate(cpForm.getStartDate());
		cpBean.setConsentWaived(cpForm.isConsentWaived());
		cpBean.setEnrollment(cpForm.getEnrollment());
		cpBean.setDescriptionURL(cpForm.getDescriptionURL());
		cpBean.setIrbID(cpForm.getIrbID());
		cpBean.setActivityStatus(cpForm.getActivityStatus());
		cpBean.setAliqoutInSameContainer(cpForm.isAliqoutInSameContainer());
		//For Consent Tab
		cpBean.setConsentTierCounter(cpForm.getConsentTierCounter());
		final Map consentMap = CollectionProtocolUtil.sortConsentMap(cpForm.getConsentValues());//bug 8905
		//cpBean.setConsentValues(cpForm.getConsentValues());
		cpBean.setConsentValues(consentMap);//bug 8905
		cpBean.setUnsignedConsentURLName(cpForm.getUnsignedConsentURLName());

		cpBean.setStudyCalendarEventPoint(cpForm.getStudyCalendarEventPoint());
		cpBean.setType(cpForm.getType());
		cpBean.setSequenceNumber(cpForm.getSequenceNumber());
		cpBean.setParentCollectionProtocolId(cpForm.getParentCollectionProtocolId());

	}

}
