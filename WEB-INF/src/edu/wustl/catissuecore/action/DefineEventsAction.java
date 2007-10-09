package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;


public class DefineEventsAction extends BaseAction
{
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		CollectionProtocolForm collectionProtocolForm = (CollectionProtocolForm)form;
		CollectionProtocolBean collectionProtocolBean=null;
		HttpSession session = request.getSession();
		if(session.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN)!=null)
		{
			collectionProtocolBean = (CollectionProtocolBean)session.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
			
		}
		else
		{	
			collectionProtocolBean = new CollectionProtocolBean();
		}
		
		if(collectionProtocolForm.getShortTitle()!=null)
		{
			collectionProtocolBean.setPrincipalInvestigatorId(collectionProtocolForm.getPrincipalInvestigatorId());
			collectionProtocolBean.setProtocolCoordinatorIds(collectionProtocolForm.getProtocolCoordinatorIds());
			collectionProtocolBean.setTitle(collectionProtocolForm.getTitle());
			collectionProtocolBean.setShortTitle(collectionProtocolForm.getShortTitle());
			collectionProtocolBean.setStartDate(collectionProtocolForm.getStartDate());
			collectionProtocolBean.setConsentWaived(collectionProtocolForm.isConsentWaived());
			collectionProtocolBean.setEnrollment(collectionProtocolForm.getEnrollment());
			collectionProtocolBean.setDescriptionURL(collectionProtocolForm.getDescriptionURL());
			collectionProtocolBean.setIrbID(collectionProtocolForm.getIrbID());
			collectionProtocolBean.setActivityStatus(collectionProtocolForm.getActivityStatus());
			collectionProtocolBean.setAliqoutInSameContainer(collectionProtocolForm.isAliqoutInSameContainer());
			//For Consent Tab
			collectionProtocolBean.setConsentTierCounter(collectionProtocolForm.getConsentTierCounter());
			collectionProtocolBean.setConsentValues(collectionProtocolForm.getConsentValues());
			collectionProtocolBean.setUnsignedConsentURLName(collectionProtocolForm.getUnsignedConsentURLName());
		}
		session.setAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN, collectionProtocolBean);
		return (mapping.findForward(Constants.SUCCESS));
	}
}
