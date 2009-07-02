
package edu.wustl.catissuecore.action;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.CDEBizLogic;
import edu.wustl.common.tree.StorageContainerTreeNode;
import edu.wustl.common.tree.TreeDataInterface;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class TreeNodeDataAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient Logger logger = Logger.getCommonLogger(TreeNodeDataAction.class);
	/**
	 * finalDataListVector.
	 */
	Vector finalDataListVector = null;

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

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		// Map columnIdsMap = new HashMap();
		String pageOf = request.getParameter(Constants.PAGE_OF);
		logger.debug("pageOf in treeview........" + pageOf);
		request.setAttribute(Constants.PAGE_OF, pageOf);
		String operation = request.getParameter(Constants.OPERATION);
		request.setAttribute(Constants.OPERATION, operation);
		String reload = null;
		String target = Constants.SUCCESS;
		if (pageOf.equals(Constants.PAGE_OF_STORAGE_LOCATION))
		{
			String storageContainerType = request.getParameter(Constants.STORAGE_CONTAINER_TYPE);
			request.setAttribute(Constants.STORAGE_CONTAINER_TYPE, storageContainerType);
			String storageContainerID = request
					.getParameter(Constants.STORAGE_CONTAINER_TO_BE_SELECTED);
			request.setAttribute(Constants.STORAGE_CONTAINER_TO_BE_SELECTED, storageContainerID);
			String position = request.getParameter(Constants.STORAGE_CONTAINER_POSITION);
			request.setAttribute(Constants.STORAGE_CONTAINER_POSITION, position);
		}
		else if (pageOf.equals(Constants.PAGE_OF_TISSUE_SITE))
		{
			HttpSession session = request.getSession();
			String cdeName = (String) session.getAttribute(Constants.CDE_NAME);
			session.removeAttribute(Constants.CDE_NAME);
			request.setAttribute(Constants.CDE_NAME, cdeName);
		}

		try
		{
			reload = request.getParameter(Constants.RELOAD);
			SessionDataBean sessionData = getSessionData(request);
			if (reload != null && reload.equals("true"))
			{
				String treeNodeIDToBeReloaded = request.getParameter(Constants.TREE_NODE_ID);
				request.setAttribute(Constants.TREE_NODE_ID, treeNodeIDToBeReloaded);
				request.setAttribute(Constants.RELOAD, reload);
			}
			TreeDataInterface bizLogic = new StorageContainerBizLogic();
			List dataList = new Vector();
			// List disableSpecimenIdsList = new ArrayList();
			if (pageOf.equals(Constants.PAGE_OF_TISSUE_SITE))
			{
				bizLogic = new CDEBizLogic();
				CDEBizLogic cdeBizLogic = (CDEBizLogic) bizLogic;
				String cdeName = request.getParameter(Constants.CDE_NAME);
				dataList = cdeBizLogic.getTreeViewData(cdeName);
			}
			else if (pageOf.equals(Constants.PAGE_OF_STORAGE_LOCATION)
					|| pageOf.equals(Constants.PAGE_OF_MULTIPLE_SPECIMEN)
					|| pageOf.equals(Constants.PAGE_OF_SPECIMEN)
					|| pageOf.equals(Constants.PAGE_OF_ALIQUOT))
			{
				StorageContainerBizLogic scBizLogic = new StorageContainerBizLogic();
				dataList = scBizLogic.getSiteWithDummyContainer(sessionData.getUserId());
			}
			else if (pageOf.equals(Constants.PAGE_OF_STORAGE_CONTAINER))
			{
				StorageContainerBizLogic scBizLogic = new StorageContainerBizLogic();
				dataList = scBizLogic.getSiteWithDummyContainer(sessionData.getUserId());
				target = Constants.PAGE_OF_STORAGE_CONTAINER;
			}
			if (dataList != null)
			{
				finalDataListVector = new Vector();
			}
			createTreeNodeVector(dataList, finalDataListVector);
			request.setAttribute(Constants.TREE_DATA, finalDataListVector);

		}
		catch (Exception exp)
		{
			logger.error(exp.getMessage(), exp);
		}
		return mapping.findForward(target);
	}

	/**
	 * 	 * This is a recursive method to make the final-vector for DHTML tree of
		 * storage containers.
	 * @param datalist : datalist
	 * @param finalDataListVector : finalDataListVector
	 */
	void createTreeNodeVector(List datalist, Vector finalDataListVector)
	{
		if (datalist != null && datalist.size() != 0)
		{
			Iterator itr = datalist.iterator();
			while (itr.hasNext())
			{
				StorageContainerTreeNode node = (StorageContainerTreeNode) itr.next();
				boolean contains = finalDataListVector.contains(node.getValue());
				if (contains == false)
				{
					finalDataListVector.add(node);
				}
				List childNodeVector = node.getChildNodes();
				createTreeNodeVector(childNodeVector, finalDataListVector);
			}
			return;
		}
		else
		{
			return;
		}
	}

}