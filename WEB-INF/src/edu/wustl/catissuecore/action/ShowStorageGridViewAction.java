/**
 * <p>
 * Title: ShowStorageGridViewAction Class>
 * <p>
 * Description: ShowStorageGridViewAction shows the grid view of the map
 * according to the storage container selected from the tree view.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenArrayBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageTypeBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.storage.StorageContainerGridObject;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.util.HibernateMetaData;

/**
 * ShowStorageGridViewAction shows the grid view of the map according to the
 * storage container selected from the tree view.
 *
 * @author gautam_shetty
 */
public class ShowStorageGridViewAction extends BaseAction
{

	/**
	 * logger.
	 */

	private transient final Logger logger = Logger.getCommonLogger(ShowStorageGridViewAction.class);

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
		String target = Constants.SUCCESS;
		String id = request.getParameter(Constants.SYSTEM_IDENTIFIER);
		if (id == null)
		{
			id = (String) request.getAttribute(Constants.SYSTEM_IDENTIFIER);
			if (id == null)
			{
				id = "0";
			}
		}
		request.setAttribute("storageContainerIdentifier", id);

		String contentOfContainer = null;
		// To get privilegeCache through
		// Singleton instance of PrivilegeManager, requires User LoginName
		// PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		/*
		 * PrivilegeCache privilegeCache = privilegeManager
		 * .getPrivilegeCache(getUserLoginName(request));
		 */

		// Aarti: Check whether user has use permission on the storage container
		// or not
		// if(!SecurityManager.getInstance(this.getClass()).isAuthorized(
		// getUserLoginName(request)
		// ,StorageContainer.class.getName()+"_"+id,Permissions.USE))
		// Call to SecurityManager.isAuthorized bypassed &
		// instead, call redirected to privilegeCache.hasPrivilege
		// Commented by Ravindra and vishvesh because this is not how it will
		// work in MSR
		//if(!privilegeCache.hasPrivilege(StorageContainer.class.getName()+"_"+id
		// ,Permissions.USE))
		// {
		// ActionErrors errors = new ActionErrors();
		// ActionError error = new ActionError("access.use.object.denied");
		// errors.add(ActionErrors.GLOBAL_ERROR,error);
		// saveErrors(request,errors);
		// return mapping.findForward(Constants.FAILURE);
		// }
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		if (pageOf.equals(Constants.PAGE_OF_STORAGE_CONTAINER))
		{
			target = Constants.PAGE_OF_STORAGE_CONTAINER;
		}

		// Sri: Added to get the position of the storage container map
		final String position = request.getParameter(Constants.STORAGE_CONTAINER_POSITION);
		if ((null != position) && ("" != position))
		{
			Long positionOne;
			Long positionTwo;
			try
			{
				// The two positions are separated by :
				final StringTokenizer strToken = new StringTokenizer(position, ":");
				positionOne = Long.valueOf(strToken.nextToken());
				positionTwo = Long.valueOf(strToken.nextToken());
			}
			catch (final Exception ex)
			{
				this.logger.error(ex.getMessage(), ex);
				ex.printStackTrace() ;
				// Will not select anything
				positionOne = null;
				positionTwo = null;
			}
			request.setAttribute(Constants.POS_ONE, positionOne);
			request.setAttribute(Constants.POS_TWO, positionTwo);

		}
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) factory
				.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

		final Object containerObject = bizLogic.retrieve(StorageContainer.class.getName(),
				new Long(id));
		StorageContainerGridObject storageContainerGridObject = null;
		int[][] fullStatus = null;
		int[][] childContainerIds = null;
		String[][] childContainerType = null;
		String[][] childContainerName = null;
		// Object a = request.getAttribute("tree");

		if (containerObject != null)
		{
			storageContainerGridObject = new StorageContainerGridObject();
			final StorageContainer storageContainer = (StorageContainer) containerObject;

			this.setEnablePageAttributeIfRequired(request, storageContainer);

			final Site site = (Site) bizLogic.retrieveAttribute(StorageContainer.class.getName(),
					storageContainer.getId(), "site");// container.getSite();
			request.setAttribute("siteName", site.getName());

			final StorageType storageType = (StorageType) bizLogic.retrieveAttribute(
					StorageContainer.class.getName(), storageContainer.getId(), "storageType");// storageContainer
			// .
			// getStorageType
			// (
			// )
			// ;
			request.setAttribute("storageTypeName", storageType.getName());
			// Object tree = request.getAttribute("tree");

			// Mandar : Labels for Dimensions
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

			request.setAttribute(Constants.STORAGE_CONTAINER_DIM_ONE_LABEL, oneDimLabel);
			request.setAttribute(Constants.STORAGE_CONTAINER_DIM_TWO_LABEL, twoDimLabel);

			storageContainerGridObject.setId(storageContainer.getId().longValue());
			storageContainerGridObject.setType(storageType.getName());
			storageContainerGridObject.setName(storageContainer.getName());

			final Integer oneDimensionCapacity = storageContainer.getCapacity()
					.getOneDimensionCapacity();
			final Integer twoDimensionCapacity = storageContainer.getCapacity()
					.getTwoDimensionCapacity();

			childContainerIds = new int[oneDimensionCapacity.intValue() + 1][twoDimensionCapacity
					.intValue() + 1];
			storageContainerGridObject.setOneDimensionCapacity(oneDimensionCapacity);
			storageContainerGridObject.setTwoDimensionCapacity(storageContainer.getCapacity()
					.getTwoDimensionCapacity());

			fullStatus = new int[oneDimensionCapacity.intValue() + 1][twoDimensionCapacity
					.intValue() + 1];
			childContainerType = new String[oneDimensionCapacity.intValue() + 1][twoDimensionCapacity
					.intValue() + 1];
			childContainerName = new String[oneDimensionCapacity.intValue() + 1][twoDimensionCapacity
					.intValue() + 1];
			final Collection children = StorageContainerUtil.getContainerChildren(storageContainer.getId());
			if (children != null)
			{
				final Iterator iterator = children.iterator();
				while (iterator.hasNext())
				{
					final Object object = iterator.next();
					final StorageContainer childStorageContainer = (StorageContainer) HibernateMetaData
							.getProxyObjectImpl(object);
					if (childStorageContainer != null
							&& childStorageContainer.getLocatedAtPosition() != null)
					{
						final Integer positionDimensionOne = childStorageContainer
								.getLocatedAtPosition().getPositionDimensionOne();
						final Integer positionDimensionTwo = childStorageContainer
								.getLocatedAtPosition().getPositionDimensionTwo();

						fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = 1;
						childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo
								.intValue()] = childStorageContainer.getId().intValue();
						childContainerType[positionDimensionOne.intValue()][positionDimensionTwo
								.intValue()] = Constants.CONTAINER_LABEL_CONTAINER_MAP
								+ childStorageContainer.getName();
						childContainerName[positionDimensionOne.intValue()][positionDimensionTwo
								.intValue()] = childStorageContainer.getName();
					}
				}
			}

			final IBizLogic specimenBizLogic = factory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);

			// Showing Specimens in the Container map.
			String sourceObjectName = Specimen.class.getName();
			final String[] selectColumnName = {"id", "specimenPosition.positionDimensionOne",
					"specimenPosition.positionDimensionTwo", "label"};
			final String[] whereColumnName = {"specimenPosition.storageContainer.id"};
			final String[] whereColumnCondition = {"="};
			final Object[] whereColumnValue = {Long.valueOf(id)};
			final String joinCondition = Constants.AND_JOIN_CONDITION;

			List list = null;
			list = specimenBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName,
					whereColumnCondition, whereColumnValue, joinCondition);

			if (list != null)
			{
				final Iterator iterator = list.iterator();
				while (iterator.hasNext())
				{
					// Specimen specimen = (Specimen)iterator.next();
					final Object[] obj = (Object[]) iterator.next();

					final Long specimenID = (Long) obj[0];
					final Integer positionDimensionOne = (Integer) obj[1];
					final Integer positionDimensionTwo = (Integer) obj[2];
					final String specimenLable = (String) obj[3];

					fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = 2;
					childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo
							.intValue()] = specimenID.intValue();
					childContainerType[positionDimensionOne.intValue()][positionDimensionTwo
							.intValue()] = Constants.SPECIMEN_LABEL_CONTAINER_MAP + specimenLable;
					contentOfContainer = Constants.ALIAS_SPECIMEN;

				}
			}

			// Showing Specimen Arrays in the Container map.
			sourceObjectName = SpecimenArray.class.getName();

			selectColumnName[1] = "locatedAtPosition.positionDimensionOne";
			selectColumnName[2] = "locatedAtPosition.positionDimensionTwo";
			selectColumnName[3] = "name";
			whereColumnName[0] = "locatedAtPosition.parentContainer.id";

			list = specimenBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName,
					whereColumnCondition, whereColumnValue, joinCondition);

			if (list != null)
			{
				final Iterator iterator = list.iterator();
				while (iterator.hasNext())
				{
					final Object[] obj = (Object[]) iterator.next();

					final Long specimenID = (Long) obj[0];
					final Integer positionDimensionOne = (Integer) obj[1];
					final Integer positionDimensionTwo = (Integer) obj[2];
					final String specimenArrayLable = obj[3].toString();

					fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = 2;
					childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo
							.intValue()] = specimenID.intValue();
					childContainerType[positionDimensionOne.intValue()][positionDimensionTwo
							.intValue()] = Constants.SPECIMEN_ARRAY_LABEL_CONTAINER_MAP
							+ specimenArrayLable;
					contentOfContainer = Constants.ALIAS_SPECIMEN_ARRAY;

				}
			}
		}

		request.setAttribute(Constants.CONTENT_OF_CONTAINNER, contentOfContainer);
		if (pageOf.equals(Constants.PAGE_OF_STORAGE_LOCATION))
		{
			final String storageContainerType = request
					.getParameter(Constants.STORAGE_CONTAINER_TYPE);
			this.logger.info("Id-----------------" + id);
			this.logger.info("storageContainerType:" + storageContainerType);
			final int startNumber = StorageContainerUtil.getNextContainerNumber(Long.parseLong(id), Long
					.parseLong(storageContainerType), false);
			request.setAttribute(Constants.STORAGE_CONTAINER_TYPE, storageContainerType);
			request.setAttribute(Constants.START_NUMBER, new Integer(startNumber));
		}

		request.setAttribute(Constants.PAGE_OF, pageOf);
		request.setAttribute(Constants.CHILD_CONTAINER_SYSTEM_IDENTIFIERS, childContainerIds);
		request.setAttribute(Constants.CHILD_CONTAINER_TYPE, childContainerType);
		request.setAttribute(Constants.CHILD_CONTAINER_NAME, childContainerName);
		request.setAttribute(Constants.STORAGE_CONTAINER_CHILDREN_STATUS, fullStatus);
		request.setAttribute(Constants.STORAGE_CONTAINER_GRID_OBJECT, storageContainerGridObject);

		// Mandar : 29aug06 : to set collectionprotocol titles
		
		final List collectionProtocolList = CollectionProtocolBizLogic.getCollectionProtocolList(id);
		request.setAttribute(Constants.MAP_COLLECTION_PROTOCOL_LIST, collectionProtocolList);

		// Mandar : 29aug06 : to set specimenclass
		final List specimenClassList = bizLogic.getSpecimenClassList(id);
		request.setAttribute(Constants.MAP_SPECIMEN_CLASS_LIST, specimenClassList);

		final List specimenTypeList = AppUtility.getSpecimenTypeList(id);
		request.setAttribute(Constants.MAP_SPECIMEN_TYPE_LIST, specimenTypeList);

		return mapping.findForward(target);
	}

	/**
	 * To enable or disable the Storage container links on the page depending on
	 * restriction criteria on Container.
	 *
	 * @param request
	 *            The HttpServletRequest object reference.
	 * @param storageContainer
	 *            The Storage container object reference.
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private void setEnablePageAttributeIfRequired(HttpServletRequest request,
			StorageContainer storageContainer)
			throws BizLogicException
	{
		boolean enablePage = true;
		String activityStatus = request.getParameter(Status.ACTIVITY_STATUS.toString());
		if (activityStatus == null)
		{
			activityStatus = (String) request.getAttribute(Status.ACTIVITY_STATUS.toString());
		}

		if (activityStatus != null
				&& activityStatus.equals(Status.ACTIVITY_STATUS_CLOSED.toString()))
		{
			enablePage = false;
			final ActionErrors errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.container.closed"));
			this.saveErrors(request, errors);
		}

		final HttpSession session = request.getSession();
		// checking for container type.
		final String holdContainerType = (String) session
				.getAttribute(Constants.CAN_HOLD_CONTAINER_TYPE);
		if (enablePage && holdContainerType != null && !holdContainerType.equals(""))
		{
			final int typeId = Integer.parseInt(holdContainerType);
			StorageTypeBizLogic stBiz = new StorageTypeBizLogic();
			enablePage = stBiz.canHoldContainerType(typeId, storageContainer);
		}
		CollectionProtocolBizLogic cpBiz = new CollectionProtocolBizLogic();
		final String holdCollectionProtocol = (String) session
				.getAttribute(Constants.CAN_HOLD_COLLECTION_PROTOCOL);
		if (enablePage && holdCollectionProtocol != null)
		{
			if (!holdCollectionProtocol.equals(""))
			{
				final int collectionProtocolId = Integer.parseInt(holdCollectionProtocol);
				enablePage = cpBiz.canHoldCollectionProtocol(collectionProtocolId,
						storageContainer);
			}
			else
			{
				enablePage = false;
			}
		}

		NewSpecimenBizLogic nspBiz = new NewSpecimenBizLogic();
		final String holdspecimenClass = (String) session
				.getAttribute(Constants.CAN_HOLD_SPECIMEN_CLASS);
		if (enablePage && holdspecimenClass != null)
		{
			if (!holdspecimenClass.equals(""))
			{
				enablePage = nspBiz.canHoldSpecimenClass(holdspecimenClass, storageContainer);
			}
			else
			{
				enablePage = false;
			}
		}

		SpecimenArrayBizLogic spArraybiz = new SpecimenArrayBizLogic();
		final String holdspecimenArrayType = (String) session
				.getAttribute(Constants.CAN_HOLD_SPECIMEN_ARRAY_TYPE);
		if (enablePage && holdspecimenArrayType != null)
		{
			if (!holdspecimenArrayType.equals(""))
			{
				final int specimenArrayTypeId = Integer.parseInt(holdspecimenArrayType);
				enablePage = spArraybiz.canHoldSpecimenArrayType(specimenArrayTypeId,
						storageContainer);
			}
			else
			{
				enablePage = false;
			}
		}

		if (enablePage)
		{
			request.setAttribute(Constants.ENABLE_STORAGE_CONTAINER_GRID_PAGE, Constants.TRUE);
		}
	}
}