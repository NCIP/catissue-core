
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
import edu.wustl.catissuecore.tree.QueryTreeNodeData;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

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
	 * @param request : object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		final HttpSession session = request.getSession();
		final CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		final Map collectionProtocolEventMap = (Map) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		final String operation = request.getParameter(Constants.OPERATION);
		request.setAttribute(Constants.ERROR_PAGE_FOR_CP,
				request.getParameter(Constants.ERROR_PAGE_FOR_CP));

		final Vector<QueryTreeNodeData> treeData = new Vector<QueryTreeNodeData>();

		String cpName = null;
		String displayName = null;
		String parentIdentifier = null;
		String identifier = null;
		String nodeId = null;
		if (collectionProtocolBean != null)
		{

			cpName = Constants.OBJECTNAME_FOR_CP;
			displayName = collectionProtocolBean.getTitle();
			parentIdentifier = Constants.ZERO_ID;
			identifier = collectionProtocolBean.getTitle();
			AppUtility.addNode(cpName, displayName, parentIdentifier,
					identifier, "", treeData, "");

		}

		if (collectionProtocolEventMap != null
				&& collectionProtocolBean != null)
		{
			Integer iEventCount = new Integer(1);
			final Collection collectionProtocolEventBeanCollection = collectionProtocolEventMap
					.values();
			final Iterator collectionProtocolEventBeanCollectionItr = collectionProtocolEventBeanCollection
					.iterator();
			while (collectionProtocolEventBeanCollectionItr.hasNext())
			{
				final CollectionProtocolEventBean collectionProtocolEventBean = (CollectionProtocolEventBean) collectionProtocolEventBeanCollectionItr
						.next();
				if (!Constants.DISABLE.equals(collectionProtocolEventBean
						.getActivityStatus()))
				{

					final String objectName = collectionProtocolEventBean
							.getCollectionPointLabel() + Constants.CLASS;
					// if(operation!=null &&
					// operation.equals(Constants.VIEW_SUMMARY))
					// {
					// objectName=Constants.VIEW_SUMMARY
					// }
					displayName = collectionProtocolEventBean
							.getCollectionPointLabel()
							+ " ("
							+ collectionProtocolEventBean
									.getStudyCalenderEventPoint().toString()
							+ " days)";
					parentIdentifier = collectionProtocolBean.getTitle();
					identifier = collectionProtocolEventBean
							.getUniqueIdentifier();
					AppUtility.addNode(objectName, displayName,
							parentIdentifier, identifier, cpName, treeData, "");
					nodeId = objectName + "_" + identifier;
					final Map SpecimenRequirementMap = collectionProtocolEventBean
							.getSpecimenRequirementbeanMap();

					if (SpecimenRequirementMap != null)
					{
						final Collection specimenRequirementBeanCollection = SpecimenRequirementMap
								.values();
						final Iterator specimenRequirementBeanCollectionItr = specimenRequirementBeanCollection
								.iterator();
						while (specimenRequirementBeanCollectionItr.hasNext())
						{
							final SpecimenRequirementBean specimenRequirementBean = (SpecimenRequirementBean) specimenRequirementBeanCollectionItr
									.next();
							if (!Constants.DISABLE
									.equals(specimenRequirementBean
											.getActivityStatus()))
							{
								AppUtility.createSpecimenNode(objectName,
										identifier, specimenRequirementBean,
										treeData, operation);
							}
						}
					}
				}
				iEventCount++;
			}
		}

		final String clickedNode = (String) session
				.getAttribute(Constants.TREE_NODE_ID);
		request.setAttribute(Constants.OPERATION, operation);
		request.setAttribute(Constants.TREE_DATA, treeData);
		//request.getSession().setAttribute(Constants.TREE_NODE_ID, nodeId);
		request.getSession().setAttribute(Constants.CLICKED_NODE, clickedNode);

		return mapping.findForward(Constants.SUCCESS);
	}

}
