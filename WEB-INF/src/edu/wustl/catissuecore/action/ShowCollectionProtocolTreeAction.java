
package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.tree.QueryTreeNodeData;

/**
 * This action is for getting the Tree For Events and Specimens collected Under
 * that Event for a Collection Protocol.
 *
 * @author Virender Mehta
 */
public class ShowCollectionProtocolTreeAction extends BaseAction
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
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession session = request.getSession();
		CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		Map collectionProtocolEventMap = (Map) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		String operation = request.getParameter(Constants.OPERATION);

		Vector < QueryTreeNodeData > treeData = new Vector < QueryTreeNodeData >();
		String cpName = null;
		String displayName = null;
		String parentIdentifier = null;
		String identifier = null;
		String nodeId = null;
		String specimenDisplayName = null;
		if (collectionProtocolBean != null)
		{

			cpName = Constants.OBJECTNAME_FOR_CP;
			displayName = collectionProtocolBean.getShortTitle();
			parentIdentifier = Constants.ZERO_ID;
			identifier = collectionProtocolBean.getTitle();
			addNode(cpName, displayName, parentIdentifier, identifier, "", treeData);

		}

		if (collectionProtocolEventMap != null && collectionProtocolBean != null)
		{
			Integer iEventCount = new Integer(1);
			Collection collectionProtocolEventBeanCollection = (Collection) collectionProtocolEventMap
					.values();
			Iterator collectionProtocolEventBeanCollectionItr = collectionProtocolEventBeanCollection
					.iterator();
			while (collectionProtocolEventBeanCollectionItr.hasNext())
			{
				CollectionProtocolEventBean collectionProtocolEventBean =
					(CollectionProtocolEventBean) collectionProtocolEventBeanCollectionItr
						.next();
				String objectName = collectionProtocolEventBean.getCollectionPointLabel()
						+ Constants.CLASS;
				// if(operation!=null &&
				// operation.equals(Constants.VIEW_SUMMARY))
				// {
				// objectName=Constants.VIEW_SUMMARY
				// }
				displayName = collectionProtocolEventBean.getStudyCalenderEventPoint().toString()
						+ " " + collectionProtocolEventBean.getCollectionPointLabel();
				parentIdentifier = collectionProtocolBean.getTitle();
				identifier = collectionProtocolEventBean.getUniqueIdentifier();
				addNode(objectName, displayName, parentIdentifier, identifier, cpName, treeData);
				nodeId = objectName + "_" + identifier;
				Map SpecimenRequirementMap = collectionProtocolEventBean
						.getSpecimenRequirementbeanMap();

				if (SpecimenRequirementMap != null)
				{
					Collection specimenRequirementBeanCollection =
						(Collection) SpecimenRequirementMap
							.values();
					Iterator specimenRequirementBeanCollectionItr =
						specimenRequirementBeanCollection
							.iterator();
					while (specimenRequirementBeanCollectionItr.hasNext())
					{
						SpecimenRequirementBean specimenRequirementBean =
							(SpecimenRequirementBean) specimenRequirementBeanCollectionItr
								.next();
						specimenDisplayName = createSpecimenNode(objectName, identifier,
								specimenRequirementBean, treeData, operation);
					}
				}
				iEventCount++;
			}
		}

		String clickedNode = (String) session.getAttribute(Constants.TREE_NODE_ID);
		request.setAttribute(Constants.OPERATION, operation);
		request.setAttribute(Constants.TREE_DATA, treeData);
		request.getSession().setAttribute(Constants.TREE_NODE_ID, nodeId);
		request.getSession().setAttribute(Constants.CLICKED_NODE, clickedNode);

		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * This is a recursive method for getting node data.
	 *
	 * @param parentObjectname : parentObjectname
	 * @param parentIdentifier : parentIdentifier
	 * @param specimenRequirementBean : specimenRequirementBean
	 * @param treeData : treeData
	 * @param operation : operation
	 * @return String : String
	 */
	private String createSpecimenNode(String parentObjectname, String parentIdentifier,
			SpecimenRequirementBean specimenRequirementBean, Vector treeData, String operation)
	{
		String objectName = Constants.NEW_SPECIMEN;
		// if(operation!=null && operation.equals(Constants.VIEW_SUMMARY))
		// {
		// objectName=Constants.VIEW_SUMMARY;
		// }
		String identifier = specimenRequirementBean.getUniqueIdentifier();

		String displayName = Constants.SPECIMEN + "_"
				+ specimenRequirementBean.getUniqueIdentifier();

		addNode(objectName, displayName, parentIdentifier, identifier, parentObjectname, treeData);

		if (specimenRequirementBean.getAliquotSpecimenCollection() != null
				&& !specimenRequirementBean.getAliquotSpecimenCollection().isEmpty())
		{
			Map aliquotsCollection = specimenRequirementBean.getAliquotSpecimenCollection();
			Iterator aliquotsCollectionItr = aliquotsCollection.values().iterator();
			parentIdentifier = identifier;
			parentObjectname = objectName;
			while (aliquotsCollectionItr.hasNext())
			{
				SpecimenRequirementBean specimenRequirementBean1 =
					(SpecimenRequirementBean) aliquotsCollectionItr
						.next();

				displayName = Constants.ALIQUOT + specimenRequirementBean1.getUniqueIdentifier();
				createSpecimenNode(parentObjectname, parentIdentifier, specimenRequirementBean1,
						treeData, operation);
			}
		}
		if (specimenRequirementBean.getDeriveSpecimenCollection() != null
				&& !specimenRequirementBean.getDeriveSpecimenCollection().isEmpty())
		{
			Map deriveSpecimenMap = specimenRequirementBean.getDeriveSpecimenCollection();
			Iterator deriveSpecimenCollectionItr = deriveSpecimenMap.values().iterator();
			parentIdentifier = identifier;
			parentObjectname = objectName;
			while (deriveSpecimenCollectionItr.hasNext())
			{
				SpecimenRequirementBean specimenRequirementBean1 =
					(SpecimenRequirementBean) deriveSpecimenCollectionItr
						.next();

				displayName = Constants.DERIVED_SPECIMEN
						+ specimenRequirementBean1.getUniqueIdentifier();
				createSpecimenNode(parentObjectname, parentIdentifier, specimenRequirementBean1,
						treeData, operation);

			}
		}
		return "New_" + identifier;
	}

	/**
	 * Description : This is a common method is for adding node.
	 *
	 * @param objectName : objectName
	 * @param displayName : displayName
	 * @param parentIdentifier : parentIdentifier
	 * @param identifier : identifier
	 * @param parentObjectname : parentObjectname
	 * @param treeData : treeData
	 */
	private void addNode(String objectName, String displayName, String parentIdentifier,
			String identifier, String parentObjectname, Vector < QueryTreeNodeData > treeData)
	{
		QueryTreeNodeData treeNode = new QueryTreeNodeData();
		treeNode.setParentIdentifier(parentIdentifier);
		treeNode.setIdentifier(identifier);
		treeNode.setObjectName(objectName);
		treeNode.setDisplayName(displayName);
		treeNode.setParentObjectName(parentObjectname);
		treeNode.setToolTipText(displayName);
		treeData.add(treeNode);
	}

}
