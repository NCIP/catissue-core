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
		/*DAO dao=null;

		final SessionDataBean sessionData = (SessionDataBean) request.getSession()
				.getAttribute(Constants.SESSION_DATA);
		dao = AppUtility.openDAOSession(sessionData);

		String target= Constants.SUCCESS;
		StorageContainer sc= null;*/
		String containerName = request.getParameter(Constants.CONTAINER_NAME);
		/*ColumnValueBean cvb=new ColumnValueBean(Constants.NAME, name);
		DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
		List contList= defaultBizLogic.retrieve(StorageContainer.class.getName(), cvb);
		if(contList!=null && !contList.isEmpty())
		{
			sc=(StorageContainer)contList.get(0);
		}

		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) factory
				.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

		StorageContainerGridObject storageContainerGridObject = null;

		storageContainerGridObject = new StorageContainerGridObject();
		final StorageContainer storageContainer = sc;

		final StorageType storageType = (StorageType) bizLogic.retrieveAttribute(
				StorageContainer.class.getName(), storageContainer.getId(), "storageType");// storageContainer
		request.setAttribute("storageTypeName", storageType.getName());
		String oneDimLabel = storageType.getOneDimensionLabel();
		String twoDimLabel = storageType.getTwoDimensionLabel();
		if (oneDimLabel == null)
		{
			oneDimLabel = " ";
		}
		if (twoDimLabel == null)
		{
			twoDimLabel = " ";
		}
		String oneDimLabellingScheme=storageContainer.getOneDimensionLabellingScheme();
		String twoDimLabellingScheme=storageContainer.getTwoDimensionLabellingScheme();
		request.setAttribute(Constants.STORAGE_CONTAINER_DIM_ONE_LABEL, oneDimLabel);
		request.setAttribute(Constants.STORAGE_CONTAINER_DIM_TWO_LABEL, twoDimLabel);

		request.setAttribute("oneDimLabellingScheme", oneDimLabellingScheme);
		request.setAttribute("twoDimLabellingScheme", twoDimLabellingScheme);

		storageContainerGridObject.setId(storageContainer.getId().longValue());
		storageContainerGridObject.setType(storageType.getName());
		storageContainerGridObject.setName(storageContainer.getName());
		final Integer oneDimensionCapacity = storageContainer.getCapacity()
				.getOneDimensionCapacity();
		final Integer twoDimensionCapacity = storageContainer.getCapacity()
				.getTwoDimensionCapacity();
		storageContainerGridObject.setOneDimensionCapacity(oneDimensionCapacity);
		storageContainerGridObject.setTwoDimensionCapacity(twoDimensionCapacity);
		storageContainerGridObject.setOneDimensionLabellingScheme(storageContainer.getOneDimensionLabellingScheme());
		storageContainerGridObject.setTwoDimensionLabellingScheme(storageContainer.getTwoDimensionLabellingScheme());

		boolean[][] avaiablePositions=StorageContainerUtil.getAvailablePositionsForContainer(storageContainer.getId().toString(), oneDimensionCapacity, twoDimensionCapacity, dao);
		request.setAttribute("avaiablePositions", avaiablePositions);*/
		StorageContainerGridObject storageContainerGridObject = null;

		storageContainerGridObject = new StorageContainerGridObject();
		storageContainerGridObject=StorageContainerUtil.getContainerDetails(containerName);
		ActionErrors errors = new ActionErrors();
		if(null==storageContainerGridObject)
		{
			
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.specimen.storageContainerEditBox"));
		}
		this.saveErrors(request, errors);
		request.setAttribute(Constants.STORAGE_CONTAINER_GRID_OBJECT, storageContainerGridObject);
		request.setAttribute(Constants.POS1,request.getParameter(Constants.POS1));
		request.setAttribute(Constants.POS2,request.getParameter(Constants.POS2));
		return mapping.findForward(target);
			}

}
