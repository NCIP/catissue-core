
package edu.wustl.catissuecore.action;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ProtocolEventDetailsForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.tree.QueryTreeNodeData;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
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
			HttpServletRequest request, HttpServletResponse response)
			throws DAOException
	{
		final ProtocolEventDetailsForm protocolEventDetailsForm = (ProtocolEventDetailsForm) form;
		final HttpSession session = request.getSession();
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		final String parentNodeId = request.getParameter("parentNodeId");
		final String nodeId = request.getParameter(Constants.TREE_NODE_ID);
		final Vector<QueryTreeNodeData> treeData = new Vector<QueryTreeNodeData>();
		//tree node attributes
		String operation = null;

		Map collectionProtocolEventMap = null;
		CollectionProtocolEventBean collectionProtocolEventBean = null;
		if (session
				.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP) != null)
		{
			collectionProtocolEventMap = (LinkedHashMap) session
					.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		}
		else
		{
			collectionProtocolEventMap = new LinkedHashMap();
		}
		final String isCreateDuplicateEvent = request
				.getParameter(Constants.CREATE_DUPLICATE_EVENT);
		String ollectionProtocolEventkey = protocolEventDetailsForm
				.getCollectionProtocolEventkey();
		if (Constants.TRUE.equals(isCreateDuplicateEvent))
		{
			protocolEventDetailsForm
					.setCollectionProtocolEventkey(Constants.ADD_NEW_EVENT);
			protocolEventDetailsForm.setCollectionPointLabel("");
		}
		if (protocolEventDetailsForm.getCollectionProtocolEventkey().equals(
				Constants.ADD_NEW_EVENT))
		{
			operation = Constants.ADD;
			int eventmapSize = collectionProtocolEventMap.size();
			while (collectionProtocolEventMap
					.containsKey(Constants.UNIQUE_IDENTIFIER_FOR_EVENTS
							+ (eventmapSize)))
			{
				eventmapSize = eventmapSize + 1;
			}
			collectionProtocolEventBean = new CollectionProtocolEventBean();
			if (eventmapSize == 0)
			{
				eventmapSize = eventmapSize + 1;
			}
			collectionProtocolEventBean
					.setUniqueIdentifier(Constants.UNIQUE_IDENTIFIER_FOR_EVENTS
							+ (eventmapSize));
			this.setCollectionProtocolBean(collectionProtocolEventBean,
					protocolEventDetailsForm);
			if (Constants.TRUE.equals(isCreateDuplicateEvent))
			{
				CollectionProtocolEventBean speBean = (CollectionProtocolEventBean) collectionProtocolEventMap
						.get(ollectionProtocolEventkey);
				LinkedList<GenericSpecimen> specimenList = new LinkedList<GenericSpecimen>();
				specimenList = (LinkedList<GenericSpecimen>) CollectionProtocolUtil
						.getSpecimenList(speBean
								.getSpecimenRequirementbeanMap().values());

				String uniqueIdentifier = collectionProtocolEventBean
						.getUniqueIdentifier();
				collectionProtocolEventBean
						.setSpecimenRequirementbeanMap(CollectionProtocolUtil
								.getSpecimenRequirementMap(uniqueIdentifier,
										specimenList, 0));
			}
			collectionProtocolEventMap.put(
					collectionProtocolEventBean.getUniqueIdentifier(),
					collectionProtocolEventBean);
		}
		else
		{
			operation = Constants.EDIT;
			collectionProtocolEventBean = (CollectionProtocolEventBean) collectionProtocolEventMap
					.get(protocolEventDetailsForm
							.getCollectionProtocolEventkey());
			this.setCollectionProtocolBean(collectionProtocolEventBean,
					protocolEventDetailsForm);
			collectionProtocolEventMap.put(
					protocolEventDetailsForm.getCollectionProtocolEventkey(),
					collectionProtocolEventBean);
		}

		final String objectName = collectionProtocolEventBean
				.getCollectionPointLabel() + Constants.CLASS;
		final String displayName = collectionProtocolEventBean
				.getCollectionPointLabel()
				+ " ("
				+ collectionProtocolEventBean.getStudyCalenderEventPoint()
						.toString() + " days)";
		final String identifier = collectionProtocolEventBean
				.getUniqueIdentifier();
		final CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		String parentId = collectionProtocolBean.getTitle();

		if (protocolEventDetailsForm.getCollectionProtocolEventkey().equals(
				Constants.ADD_NEW_EVENT))
		{
			AppUtility.addNode(objectName, displayName, parentId, identifier,
					Constants.OBJECTNAME_FOR_CP, treeData, "");
			if (Constants.TRUE.equals(isCreateDuplicateEvent))
			{
				addCPTreeChildNodes(request, treeData, operation,
						collectionProtocolEventBean, objectName);
			}
			request.setAttribute("nodeAdded", treeData);
		}

		if (nodeId != null && !nodeId.equals(objectName + "_" + identifier)
				&& !Constants.ADD.equals(operation))
		{
			//delete old Event Tree node with its chield's
			request.setAttribute("deleteNode", nodeId);

			//add new event node
			AppUtility.addNode(objectName, displayName, parentId, identifier,
					Constants.OBJECTNAME_FOR_CP, treeData, "");

			addCPTreeChildNodes(request, treeData, operation,
					collectionProtocolEventBean, objectName);
			request.setAttribute("nodeAdded", treeData);
		}

		session.setAttribute(Constants.TREE_NODE_ID,
				protocolEventDetailsForm.getCollectionPointLabel() + "class_"
						+ collectionProtocolEventBean.getUniqueIdentifier());
		final String listKey = collectionProtocolEventBean
				.getUniqueIdentifier();
		session.setAttribute(Constants.NEW_EVENT_KEY, listKey);
		// request.setAttribute("listKey", listKey);
		session.setAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP,
				collectionProtocolEventMap);
		request.setAttribute(Constants.OPERATION,
				request.getParameter(Constants.OPERATION));
		return mapping.findForward(pageOf);
	}

	private void addCPTreeChildNodes(HttpServletRequest request,
			final Vector<QueryTreeNodeData> treeData, String operation,
			CollectionProtocolEventBean collectionProtocolEventBean,
			final String objectName)
	{

		//add child node of event
		Map<String, GenericSpecimen> specimenRequirementMap = collectionProtocolEventBean
				.getSpecimenRequirementbeanMap();
		Set<String> eventKeySet = specimenRequirementMap.keySet();
		String parentId = collectionProtocolEventBean.getUniqueIdentifier();
		Iterator<String> itrator = eventKeySet.iterator();
		while (itrator.hasNext())
		{
			String key = (String) itrator.next();
			SpecimenRequirementBean srBean = (SpecimenRequirementBean) specimenRequirementMap
					.get(key);
			if (!Constants.DISABLE.equalsIgnoreCase(srBean.getActivityStatus()))
			{
				AppUtility.createSpecimenNode(objectName, parentId, srBean,
						treeData, operation);
			}
		}
	}

	/**
	 *
	 * @param collectionProtocolEventBean : collectionProtocolEventBean
	 * @param protocolEventDetailsForm : protocolEventDetailsForm
	 */
	private void setCollectionProtocolBean(
			CollectionProtocolEventBean collectionProtocolEventBean,
			ProtocolEventDetailsForm protocolEventDetailsForm)
	{
		collectionProtocolEventBean
				.setClinicalDiagnosis(protocolEventDetailsForm
						.getClinicalDiagnosis());
		collectionProtocolEventBean.setClinicalStatus(protocolEventDetailsForm
				.getClinicalStatus());
		collectionProtocolEventBean
				.setCollectionPointLabel(protocolEventDetailsForm
						.getCollectionPointLabel());
		collectionProtocolEventBean
				.setStudyCalenderEventPoint(protocolEventDetailsForm
						.getStudyCalendarEventPoint());

		collectionProtocolEventBean
				.setCollectedEventComments(protocolEventDetailsForm
						.getCollectionEventComments());
		collectionProtocolEventBean
				.setCollectionContainer(protocolEventDetailsForm
						.getCollectionEventContainer());
		collectionProtocolEventBean
				.setReceivedEventComments(protocolEventDetailsForm
						.getReceivedEventComments());
		collectionProtocolEventBean.setReceivedQuality(protocolEventDetailsForm
				.getReceivedEventReceivedQuality());
		collectionProtocolEventBean
				.setCollectionProcedure(protocolEventDetailsForm
						.getCollectionEventCollectionProcedure());

		collectionProtocolEventBean.setLabelFormat(protocolEventDetailsForm
				.getLabelFormat());
		collectionProtocolEventBean.setDefaultSiteId(protocolEventDetailsForm
				.getDefaultSiteId());

	}

}
