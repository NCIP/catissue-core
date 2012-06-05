package edu.wustl.catissuecore.action;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.tree.QueryTreeNodeData;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveSpecimenRequirementAction.
 * 
 * @author renuka_bajpai
 */
public class DuplicateSpecimenRequirementAction extends BaseAction {

	/** logger. */

	private static final Logger LOGGER = Logger
			.getCommonLogger(DuplicateSpecimenRequirementAction.class);

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
	 * 
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String target = Constants.SUCCESS;
		// request.getParameter("isPersistent")
		final HttpSession session = request.getSession();
		final String mapKey = request.getParameter(Constants.EVENT_KEY);
		final String parentNodeId = request.getParameter(Constants.PARENT_NODE_ID);
		final String nodeId = request.getParameter(Constants.TREE_NODE_ID);
		final Vector<QueryTreeNodeData> treeData = new Vector<QueryTreeNodeData>();
		String objectName = null;
		String parentId = null;
		String eventSelected = null;
		
		try
		{
			if (nodeId != null && !nodeId.startsWith(Constants.NEW_SPECIMEN))
				eventSelected = mapKey;
			else
				eventSelected = parentNodeId.substring(parentNodeId.lastIndexOf('_') + 1, parentNodeId.length());
	
			request.setAttribute("isPersistent", request.getParameter("isPersistent"));
	
			final Map collectionProtocolEventMap = (Map) session.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
			final CollectionProtocolEventBean collectionProtocolEventBean = (CollectionProtocolEventBean) collectionProtocolEventMap.get(eventSelected);
	
			LinkedList<GenericSpecimen> specimens = new LinkedList<GenericSpecimen>();
			specimens.add(collectionProtocolEventBean.getSpecimenRequirementbeanMap().get(nodeId.substring(nodeId.indexOf("_") + 1)));
			LinkedHashMap<String, GenericSpecimen> specimenRequirementBeans = CollectionProtocolUtil
					.getSpecimenRequirementMap(collectionProtocolEventBean.getUniqueIdentifier(), specimens,collectionProtocolEventBean.getSpecimenRequirementbeanMap().size());
			SpecimenRequirementBean specimenRequirementBean = (SpecimenRequirementBean) specimenRequirementBeans.values().toArray()[0];
			collectionProtocolEventBean.getSpecimenRequirementbeanMap().put(specimenRequirementBean.getUniqueIdentifier(),specimenRequirementBean);
			
			if (specimenRequirementBean != null) {
				collectionProtocolEventBean.addSpecimenRequirementBean(specimenRequirementBean);
				// to create CP Tree node
				objectName = collectionProtocolEventBean.getCollectionPointLabel()+ Constants.CLASS;
				if (parentNodeId != null && parentNodeId.startsWith("cpName")) {
					parentId = collectionProtocolEventBean.getUniqueIdentifier();
				} else {
					parentId = parentNodeId.substring(parentNodeId.lastIndexOf('_') + 1);
				}
				AppUtility.createSpecimenNode(objectName, parentId,specimenRequirementBean, treeData, request.getParameter(Constants.OPERATION));
				request.setAttribute("nodeAdded", treeData);
			}
			
			session.setAttribute(Constants.TREE_NODE_ID,	Constants.NEW_SPECIMEN + "_"+ specimenRequirementBean.getUniqueIdentifier());
			session.setAttribute("key",specimenRequirementBean.getUniqueIdentifier());
			session.setAttribute("listKey", eventSelected);
		}
		catch (final Exception e)
		{
			LOGGER.error(e.getMessage(), e);
			final ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("errors.item", e
					.getMessage()));
			this.saveErrors(request, actionErrors);
//			return mapping.findForward(Constants.FAILURE);
			target = Constants.FAILURE;
		}
		return mapping.findForward(target);
	}
}