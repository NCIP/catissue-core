package edu.wustl.catissuecore.action;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ProtocolEventDetailsForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;


public class SaveProtocolEventDetailsAction extends BaseAction
{

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		ProtocolEventDetailsForm protocolEventDetailsForm = (ProtocolEventDetailsForm)form;
		HttpSession session = request.getSession();
		Map collectionProtocolEventMap = null;
		CollectionProtocolEventBean collectionProtocolEventBean =null;
		if(session.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP)!=null)
		{
			collectionProtocolEventMap = (LinkedHashMap)session.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		}
		else
		{
			collectionProtocolEventMap = new LinkedHashMap();
		}
		String pageOf = request.getParameter(Constants.PAGE_OF);
				
		if(protocolEventDetailsForm.getCollectionProtocolEventkey().equals("-1"))
		{
			collectionProtocolEventBean = new CollectionProtocolEventBean();
			collectionProtocolEventBean.setUniqueIdentifier("E"+(collectionProtocolEventMap.size()+1));
			setCollectionProtocolBean(collectionProtocolEventBean,protocolEventDetailsForm);
			collectionProtocolEventMap.put(collectionProtocolEventBean.getUniqueIdentifier(),collectionProtocolEventBean);
		}
		else
		{
			collectionProtocolEventBean = (CollectionProtocolEventBean)collectionProtocolEventMap.get(protocolEventDetailsForm.getCollectionProtocolEventkey());
			setCollectionProtocolBean(collectionProtocolEventBean,protocolEventDetailsForm);
			collectionProtocolEventMap.put(protocolEventDetailsForm.getCollectionProtocolEventkey(),collectionProtocolEventBean);
		}
		String listKey = collectionProtocolEventBean.getUniqueIdentifier();
		session.setAttribute("listKey", listKey);
		//request.setAttribute("listKey", listKey);
		session.setAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP, collectionProtocolEventMap);
		return (mapping.findForward(pageOf));
	}
	

	private void setCollectionProtocolBean(CollectionProtocolEventBean collectionProtocolEventBean, ProtocolEventDetailsForm protocolEventDetailsForm)
	{
		collectionProtocolEventBean.setClinicalDiagnosis(protocolEventDetailsForm.getClinicalDiagnosis());
		collectionProtocolEventBean.setClinicalStatus(protocolEventDetailsForm.getClinicalStatus());
		collectionProtocolEventBean.setCollectionPointLabel(protocolEventDetailsForm.getCollectionPointLabel());
		collectionProtocolEventBean.setStudyCalenderEventPoint(protocolEventDetailsForm.getStudyCalendarEventPoint());
		
		collectionProtocolEventBean.setCollectedEventComments(protocolEventDetailsForm.getCollectionEventComments());
		collectionProtocolEventBean.setCollectionContainer(protocolEventDetailsForm.getCollectionEventContainer());
		collectionProtocolEventBean.setReceivedEventComments(protocolEventDetailsForm.getReceivedEventComments());
		collectionProtocolEventBean.setReceivedQuality(protocolEventDetailsForm.getReceivedEventReceivedQuality());
		collectionProtocolEventBean.setCollectionProcedure(protocolEventDetailsForm.getCollectionEventCollectionProcedure());
	}

}


