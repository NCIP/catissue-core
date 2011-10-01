
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
import edu.wustl.dao.exception.DAOException;

/**
 * @author renuka_bajpai
 */
public class SaveProtocolEventDetailsAction extends BaseAction
{

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
	 * @throws DAOException
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws DAOException
	{
		final ProtocolEventDetailsForm protocolEventDetailsForm = (ProtocolEventDetailsForm) form;
		if("0".equals(request.getParameter("sppLength")))
		{
			protocolEventDetailsForm.setSpecimenProcessingProcedure(null);
		}
		final HttpSession session = request.getSession();
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		Map collectionProtocolEventMap = null;
		CollectionProtocolEventBean collectionProtocolEventBean = null;
		if (session.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP) != null)
		{
			collectionProtocolEventMap = (LinkedHashMap) session
					.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		}
		else
		{
			collectionProtocolEventMap = new LinkedHashMap();
		}
		if (protocolEventDetailsForm.getCollectionProtocolEventkey()
				.equals(Constants.ADD_NEW_EVENT))
		{
			int eventmapSize = collectionProtocolEventMap.size();
			while (collectionProtocolEventMap.containsKey(Constants.UNIQUE_IDENTIFIER_FOR_EVENTS
					+ (eventmapSize)))
			{
				eventmapSize = eventmapSize + 1;
			}
			collectionProtocolEventBean = new CollectionProtocolEventBean();
			if (eventmapSize == 0)
			{
				eventmapSize = eventmapSize + 1;
			}
			collectionProtocolEventBean.setUniqueIdentifier(Constants.UNIQUE_IDENTIFIER_FOR_EVENTS
					+ (eventmapSize));
			this.setCollectionProtocolBean(collectionProtocolEventBean, protocolEventDetailsForm);
			collectionProtocolEventMap.put(collectionProtocolEventBean.getUniqueIdentifier(),
					collectionProtocolEventBean);
		}
		else
		{
			collectionProtocolEventBean = (CollectionProtocolEventBean) collectionProtocolEventMap
					.get(protocolEventDetailsForm.getCollectionProtocolEventkey());
			this.setCollectionProtocolBean(collectionProtocolEventBean, protocolEventDetailsForm);
			collectionProtocolEventMap.put(
					protocolEventDetailsForm.getCollectionProtocolEventkey(),
					collectionProtocolEventBean);
		}
		session.setAttribute(Constants.TREE_NODE_ID, protocolEventDetailsForm
				.getCollectionPointLabel()
				+ "class_" + collectionProtocolEventBean.getUniqueIdentifier());
		final String listKey = collectionProtocolEventBean.getUniqueIdentifier();
		session.setAttribute(Constants.NEW_EVENT_KEY, listKey);
		// request.setAttribute("listKey", listKey);
		session.setAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP,
				collectionProtocolEventMap);
		return (mapping.findForward(pageOf));
	}

	/**
	 *
	 * @param collectionProtocolEventBean : collectionProtocolEventBean
	 * @param protocolEventDetailsForm : protocolEventDetailsForm
	 */
	private void setCollectionProtocolBean(CollectionProtocolEventBean collectionProtocolEventBean,
			ProtocolEventDetailsForm protocolEventDetailsForm)
	{
		collectionProtocolEventBean.setClinicalDiagnosis(protocolEventDetailsForm
				.getClinicalDiagnosis());
		collectionProtocolEventBean.setClinicalStatus(protocolEventDetailsForm.getClinicalStatus());
		collectionProtocolEventBean.setCollectionPointLabel(protocolEventDetailsForm
				.getCollectionPointLabel());
		collectionProtocolEventBean.setStudyCalenderEventPoint(protocolEventDetailsForm
				.getStudyCalendarEventPoint());

		collectionProtocolEventBean.setCollectedEventComments(protocolEventDetailsForm
				.getCollectionEventComments());
		collectionProtocolEventBean.setCollectionContainer(protocolEventDetailsForm
				.getCollectionEventContainer());
		collectionProtocolEventBean.setReceivedEventComments(protocolEventDetailsForm
				.getReceivedEventComments());
		collectionProtocolEventBean.setReceivedQuality(protocolEventDetailsForm
				.getReceivedEventReceivedQuality());
		collectionProtocolEventBean.setCollectionProcedure(protocolEventDetailsForm
				.getCollectionEventCollectionProcedure());
		collectionProtocolEventBean.setSpecimenProcessingProcedure(protocolEventDetailsForm.getSpecimenProcessingProcedure());
		collectionProtocolEventBean.setLabelFormat(protocolEventDetailsForm.getLabelFormat());

	}

}
