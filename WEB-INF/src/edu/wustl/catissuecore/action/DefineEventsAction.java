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


public class DefineEventsAction extends BaseAction
{
	/** (non-Javadoc)
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		CollectionProtocolForm cpForm = (CollectionProtocolForm)form;

		String pageOf=request.getParameter(Constants.PAGEOF);
		String operation=request.getParameter("operation");
		request.setAttribute("operation", operation);
		String invokeFunction=request.getParameter("invokeFunction");
		request.setAttribute("invokeFunction", invokeFunction);
		HttpSession session = request.getSession();
		
		CollectionProtocolBean cpBean = (CollectionProtocolBean)
				session.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		if(cpBean == null)
		{
			cpBean = new CollectionProtocolBean();
		}
		if(cpForm.getShortTitle()!=null)
		{
			populateCollectionProtocolBean(cpForm, cpBean);
		}
		Long cpIdentifier = cpBean.getIdentifier();
		session.setAttribute("CP_IDENTIFIER", cpIdentifier);
		session.setAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN, cpBean);
		return mapping.findForward(pageOf);
	}

	/**
	 * @param cpForm a collection protocol form to be used to
	 * populate collection protocol bean.
	 * @param cpBean collection protocol bean to be populated.
	 */
	private void populateCollectionProtocolBean(final CollectionProtocolForm cpForm,
			CollectionProtocolBean cpBean)
	{
		cpBean.setPrincipalInvestigatorId(cpForm.getPrincipalInvestigatorId());
		cpBean.setProtocolCoordinatorIds(cpForm.getProtocolCoordinatorIds());
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
		Map consentMap = CollectionProtocolUtil.sortConsentMap(cpForm.getConsentValues());//bug 8905
		//cpBean.setConsentValues(cpForm.getConsentValues());
		cpBean.setConsentValues(consentMap);//bug 8905
		cpBean.setUnsignedConsentURLName(cpForm.getUnsignedConsentURLName());
	}
	
	
}
