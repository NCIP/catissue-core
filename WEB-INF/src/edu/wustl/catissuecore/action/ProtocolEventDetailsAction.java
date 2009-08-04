
package edu.wustl.catissuecore.action;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ProtocolEventDetailsForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.cde.CDEManager;

/**
 * @author renuka_bajpai
 */
public class ProtocolEventDetailsAction extends BaseAction
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
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final ProtocolEventDetailsForm protocolEventDetailsForm = (ProtocolEventDetailsForm) form;
		final String operation = request.getParameter(Constants.OPERATION);
		final HttpSession session = request.getSession();

		// Event Key when flow is form Specimen Requirement Page
		final String key = request.getParameter(Constants.EVENT_KEY);
		String eventKey = null;
		if (key == null)
		{
			eventKey = (String) session.getAttribute(Constants.NEW_EVENT_KEY);
		}
		else
		{
			eventKey = key;
		}
		String selectedNode = (String) session.getAttribute(Constants.TREE_NODE_ID);
		String checkForSpecimen = null;
		if (selectedNode != null)
		{
			checkForSpecimen = selectedNode.substring(0, 3);
		}
		if (key != null && selectedNode != null && !selectedNode.contains(eventKey))
		{
			final String nodeId = request.getParameter(Constants.TREE_NODE_ID);
			session.setAttribute(Constants.TREE_NODE_ID, nodeId);
		}
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		long collectionEventUserId = 0;
		collectionEventUserId = AppUtility.setUserInForm(request, operation);
		selectedNode = protocolEventDetailsForm.getCollectionPointLabel() + "class_" + eventKey;
		if (key == null || "New".equals(checkForSpecimen))
		{
			session.setAttribute(Constants.TREE_NODE_ID, selectedNode);
		}
		if (protocolEventDetailsForm.getCollectionEventUserId() == 0)
		{
			protocolEventDetailsForm.setCollectionEventUserId(collectionEventUserId);
		}
		if (protocolEventDetailsForm.getReceivedEventUserId() == 0)
		{
			protocolEventDetailsForm.setReceivedEventUserId(collectionEventUserId);
		}
		if (pageOf != null && pageOf.equals(Constants.PAGE_OF_DEFINE_EVENTS))
		{
			this.initSpecimenrequirementForm(mapping, protocolEventDetailsForm, request);
		}
		else if (operation.equals(Constants.ADD) && pageOf.equals(Constants.PAGE_OF_ADD_NEW_EVENT))
		{
			protocolEventDetailsForm.setCollectionProtocolEventkey(Constants.ADD_NEW_EVENT);
			protocolEventDetailsForm.setStudyCalendarEventPoint(1D);
			protocolEventDetailsForm.setCollectionPointLabel("");
			protocolEventDetailsForm.setClinicalStatus((String) DefaultValueManager
					.getDefaultValue(Constants.DEFAULT_CLINICAL_STATUS));
			// removed as value not in cache and chnaged default to empty
			protocolEventDetailsForm.setClinicalDiagnosis((String) DefaultValueManager
					.getDefaultValue(Constants.DEFAULT_CLINICAL_DIAGNOSIS));
			// request.setAttribute("clinicalDiagnosis","");
			protocolEventDetailsForm
					.setCollectionEventCollectionProcedure((String) DefaultValueManager
							.getDefaultValue(Constants.DEFAULT_COLLECTION_PROCEDURE));
			protocolEventDetailsForm.setCollectionEventContainer((String) DefaultValueManager
					.getDefaultValue(Constants.DEFAULT_CONTAINER));
			protocolEventDetailsForm.setReceivedEventReceivedQuality((String) DefaultValueManager
					.getDefaultValue(Constants.DEFAULT_RECEIVED_QUALITY));
			final CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
					.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
			final String treeNode = "cpName_" + collectionProtocolBean.getTitle();
			session.setAttribute(Constants.TREE_NODE_ID, treeNode);
		}
		else if (pageOf != null && pageOf.equals(Constants.PAGE_OF_SPECIMEN_REQUIREMENT))
		{
			protocolEventDetailsForm.setCollectionProtocolEventkey(key);
		}
		request.setAttribute("clinicalDiagnosis", protocolEventDetailsForm.getClinicalDiagnosis());
		final List clinicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_CLINICAL_STATUS, null);
		// removed as value not in cache
		// List clinicalDiagnosisList =
		// CDEManager.getCDEManager().getPermissibleValueList
		// (Constants.CDE_NAME_CLINICAL_DIAGNOSIS,null);
		request.setAttribute(Constants.CLINICAL_STATUS_LIST, clinicalStatusList);
		// removed as value not in cache and chnaged default to empty
		// request.setAttribute(Constants.CLINICAL_DIAGNOSIS_LIST,
		// clinicalDiagnosisList);

		// setting the procedure
		final List procedureList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
		request.setAttribute(Constants.PROCEDURE_LIST, procedureList);
		// set the container lists
		final List containerList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_CONTAINER, null);
		request.setAttribute(Constants.CONTAINER_LIST, containerList);

		// setting the quality for received events
		final List qualityList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_RECEIVED_QUALITY, null);
		request.setAttribute(Constants.RECEIVED_QUALITY_LIST, qualityList);
		request.setAttribute(Constants.OPERATION, operation);
		request.setAttribute("protocolEventDetailsForm", protocolEventDetailsForm);

		final CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		request.setAttribute("isParticipantReg", collectionProtocolBean.isParticiapantReg());
		request.setAttribute("opr", collectionProtocolBean.getOperation());
		return (mapping.findForward(Constants.SUCCESS));
	}

	/**
	 * @param mapping
	 *            ActionMapping
	 * @param protocolEventDetailsForm
	 *            protocolEventDetails Form
	 * @param request
	 *            HttpServletRequest
	 */
	private void initSpecimenrequirementForm(ActionMapping mapping,
			ProtocolEventDetailsForm protocolEventDetailsForm, HttpServletRequest request)
	{
		final HttpSession session = request.getSession();
		final Map collectionProtocolEventMap = (Map) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);

		// If flow is from Specimen Requirement page save button.
		String collectionProtocolEventKey = (String) request.getAttribute(Constants.EVENT_KEY);

		if (collectionProtocolEventKey == null)
		{
			// If flow is from Specimen Tree View if Event Node is clicked to
			// open Event page in Edit mode.
			collectionProtocolEventKey = request.getParameter(Constants.EVENT_KEY);
		}
		if (collectionProtocolEventKey == null)
		{
			// If flow is when user Clicks Define Event button.
			collectionProtocolEventKey = (String) session.getAttribute(Constants.NEW_EVENT_KEY);
		}
		final StringTokenizer st = new StringTokenizer(collectionProtocolEventKey, "_");
		if (st.hasMoreTokens())
		{
			collectionProtocolEventKey = st.nextToken();
		}
		final CollectionProtocolEventBean collectionProtocolEventBean
		= (CollectionProtocolEventBean) collectionProtocolEventMap
				.get(collectionProtocolEventKey);
		if (new Long(collectionProtocolEventBean.getId()) != null
				&& collectionProtocolEventBean.getId() > 0)
		{
			request.setAttribute("isPersistent", true);
		}
		protocolEventDetailsForm.setClinicalDiagnosis(collectionProtocolEventBean
				.getClinicalDiagnosis());
		protocolEventDetailsForm.setClinicalStatus(collectionProtocolEventBean.getClinicalStatus());
		protocolEventDetailsForm.setCollectionPointLabel(collectionProtocolEventBean
				.getCollectionPointLabel());
		protocolEventDetailsForm.setStudyCalendarEventPoint(collectionProtocolEventBean
				.getStudyCalenderEventPoint());

		protocolEventDetailsForm.setCollectionEventComments(collectionProtocolEventBean
				.getCollectedEventComments());
		protocolEventDetailsForm.setCollectionEventContainer(collectionProtocolEventBean
				.getCollectionContainer());
		protocolEventDetailsForm.setReceivedEventComments(collectionProtocolEventBean
				.getReceivedEventComments());
		protocolEventDetailsForm.setReceivedEventReceivedQuality(collectionProtocolEventBean
				.getReceivedQuality());
		protocolEventDetailsForm.setCollectionEventCollectionProcedure(collectionProtocolEventBean
				.getCollectionProcedure());
		protocolEventDetailsForm.setCollectionProtocolEventkey(collectionProtocolEventKey);
		
		protocolEventDetailsForm.setLabelFormat(collectionProtocolEventBean.getLabelFormat());

	}

}
