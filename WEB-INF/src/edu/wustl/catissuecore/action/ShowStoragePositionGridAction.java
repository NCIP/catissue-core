/**
 * <p>
 * Title: ShowStoragePositionGridAction Class>
 * <p>
 * Description: ShowStorageGridViewAction shows the grid view of the map
 * according to the storage container selected from the tree view.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Atul Kaushal
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.storage.StorageContainerGridObject;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;

/**
 * ShowStorageGridViewAction shows the grid view of the map according to the
 * storage container selected from the tree view.
 *
 * @author gautam_shetty
 */
public class ShowStoragePositionGridAction extends BaseAction
{

	/**
	 * logger.
	 */

	private transient static final Logger logger = Logger.getCommonLogger(ShowStoragePositionGridAction.class);

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
	 * @throws Exception : generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
			{
		String target= Constants.SUCCESS;
//		String containerName = request.getParameter(Constants.CONTAINER_NAME);
//		StorageContainerGridObject storageContainerGridObject = null;
//		storageContainerGridObject = new StorageContainerGridObject();
//		storageContainerGridObject=StorageContainerUtil.getContainerDetails(containerName);
		ActionErrors errors = new ActionErrors();
//		if(null==storageContainerGridObject)
//		{
//			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.specimen.storageContainerEditBox"));
//		}
		this.saveErrors(request, errors);
//		request.setAttribute(Constants.STORAGE_CONTAINER_GRID_OBJECT, storageContainerGridObject);
		request.getSession().removeAttribute(Constants.POS1);
		request.getSession().removeAttribute(Constants.POS2);
		request.getSession().removeAttribute("controlName");
		request.getSession().removeAttribute(Constants.PAGEOF);
		request.getSession().setAttribute(Constants.POS1,request.getParameter(Constants.POS1));
		request.getSession().setAttribute(Constants.POS2,request.getParameter(Constants.POS2));
		request.getSession().setAttribute("controlName",request.getParameter("controlName"));
		request.getSession().setAttribute(Constants.PAGEOF,request.getParameter(Constants.PAGEOF));
		request.setAttribute("collStatus", request.getParameter("collStatus"));
		request.setAttribute(Constants.COLLECTION_PROTOCOL_ID, request.getParameter(Constants.COLLECTION_PROTOCOL_ID));
		request.setAttribute(Constants.CAN_HOLD_SPECIMEN_CLASS, request.getParameter(Constants.CAN_HOLD_SPECIMEN_CLASS));
		request.setAttribute(Constants.CAN_HOLD_SPECIMEN_TYPE, request.getParameter(Constants.CAN_HOLD_SPECIMEN_TYPE));
		request.setAttribute(Constants.CONTAINER_NAME, request.getParameter(Constants.CONTAINER_NAME));
		request.setAttribute("controlName", request.getParameter("controlName"));
		request.setAttribute("isVirtual", request.getParameter("isVirtual"));
		request.setAttribute(Constants.POS1,request.getParameter(Constants.POS1));
		request.setAttribute(Constants.POS2,request.getParameter(Constants.POS2));
        request.setAttribute(Constants.PAGEOF,request.getParameter(Constants.PAGEOF));
		return mapping.findForward(target);
			}

}
