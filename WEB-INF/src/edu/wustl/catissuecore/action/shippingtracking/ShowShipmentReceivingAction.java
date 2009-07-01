
package edu.wustl.catissuecore.action.shippingtracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentReceivingForm;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.shippingtracking.ShipmentBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;

/**
 * this class implements the action for shipment receiving view.
 */
public class ShowShipmentReceivingAction extends SecureAction
{

	/**
	 * action method for shipment add/edit.
	 * @param mapping object of ActionMapping class.
	 * @param form object of ActionForm class.
	 * @param request object of HttpServletRequest class.
	 * @param response object of HttpServletResponse class.
	 * @return forward mapping.
	 * @throws Exception if some problem occurs.
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		Long shipmentId = null;
		if (request.getParameter(edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER) != null)
		{
			shipmentId = Long.parseLong(request
					.getParameter(edu.wustl.
							catissuecore.util.global.Constants.SYSTEM_IDENTIFIER));
		}
		ShipmentReceivingForm shipmentReceivingForm = (ShipmentReceivingForm) form;
		Shipment shipment = null;
		// Get the Shipment details.
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		ShipmentBizLogic shipmentBizLogic = (ShipmentBizLogic) factory
				.getBizLogic(Constants.SHIPMENT_FORM_ID);
		String requestFor = request.getParameter("requestFor");
		if (requestFor == null || (requestFor != null && !requestFor.equals("storageLocation")))
		{
			shipment = shipmentBizLogic.getShipmentObject(shipmentId);
			shipmentReceivingForm.setAllValues(shipment);
		}
		TreeMap containerMap = new TreeMap();
		//String exceedingMaxLimit = "false";

		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) factory
				.getBizLogic(edu.wustl.catissuecore.util.
						global.Constants.STORAGE_CONTAINER_FORM_ID);
		// Get storageType for a "Shipment container".
		/*StorageType st = ((List < StorageType >) bizLogic.retrieve(StorageType.class.getName(),
				Constants.NAME, Constants.SHIPMENT_CONTAINER_TYPE_NAME)).get(0);*/
		//long shipmentContainerStorageTypeId = st.getId();

		SessionDataBean sessionDataBean = getSessionData(request);

		// Commented due to removing 'auto' functionality.
		/*
		if(sessionDataBean!=null)
		{
			long start = System.currentTimeMillis();
			ShipmentReceivingBizLogic shipmentReceivingBizLogic = new ShipmentReceivingBizLogic();
			containerMap = (TreeMap)shipmentReceivingBizLogic.getAvailableContainerMap(shipmentBizLogic
				.getPermittedSiteIdsForUser(sessionDataBean.getUserId(),
					sessionDataBean.isAdmin()), shipmentContainerStorageTypeId);
			long end = System.currentTimeMillis();
		}
		*/

		setContainerStorageInformation(containerMap, bizLogic, request, shipmentReceivingForm);

		setSpecimenStorageInformation(bizLogic, containerMap, request, shipmentReceivingForm,
				shipment);

		// For ParentStorageContainer Site
		String[] siteDisplayField = {"name"};
		String valueField = "id";
		String[] activityStatusArray = {Status.ACTIVITY_STATUS_DISABLED.toString(),
				Status.ACTIVITY_STATUS_CLOSED.toString()};
		List list = bizLogic.getSiteList(siteDisplayField, valueField, activityStatusArray,
				sessionDataBean.getUserId());
		NameValueBean nvbForSelect = (NameValueBean) list.get(0);
		if (!"-1".equals(nvbForSelect.getValue()))
		{
			NameValueBean nvb = new NameValueBean("--Select--", "-1");
			list.add(0, nvb);
		}
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.SITELIST, list);
		return mapping.findForward(edu.wustl.catissuecore.util.global.Constants.SUCCESS);
	}

	/**
	 * sets the storage container information.
	 * @param containerMap the treemap.
	 * @param bizLogic object of StorageContainerBizLogic class.
	 * @param request the request to be processed.
	 * @param shipmentReceivingForm form containing all values.
	 */
	private void setContainerStorageInformation(TreeMap containerMap,
			StorageContainerBizLogic bizLogic, HttpServletRequest request,
			ShipmentReceivingForm shipmentReceivingForm)
	{

		// For ParentStorageContainer Container
		String parentContainerSelected = request.getParameter("parentContainerSelected");
		request.setAttribute("parentContainerSelected", parentContainerSelected);

		String containerId = request.getParameter("containerObjectId");

		int containerCount = 0;
		if (shipmentReceivingForm != null && shipmentReceivingForm.getContainerCollection() != null)
		{
			containerCount = shipmentReceivingForm.getContainerCollection().size();
		}
		checkForSufficientAvailablePositions(request, containerMap, containerCount,
				ApplicationProperties.getValue("shipment.contentsContainers"));

		populateContainerStorageLocations(shipmentReceivingForm, containerMap);
		if (containerId != null && !containerId.trim().equals(""))
		{
			request.setAttribute("containerObjectId", containerId);
		}
	}

	/**
	 * sets the storage container information.
	 * @param containerMap the treemap.
	 * @param bizLogic object of StorageContainerBizLogic class.
	 * @param shipment in which specimen storage info is to be set.
	 * @param request the request to be processed.
	 * @param shipmentReceivingForm form containing all values.
	 */
	private void setSpecimenStorageInformation(StorageContainerBizLogic bizLogic,
			TreeMap containerMap, HttpServletRequest request,
			ShipmentReceivingForm shipmentReceivingForm, Shipment shipment)
	{
		//		For Storage Location Allocation
		Logger logger = Logger.getCommonLogger(ShowShipmentReceivingAction.class);
		String requestFor = request.getParameter("requestFor");
		// Removing 'auto' in shipment receiving.
		//List<NameValueBean> storagePositionList =  Utility.getStoragePositionTypeList();
		List < NameValueBean > storagePositionList = getStoragePositionTypeList();
		request.setAttribute("storageList", storagePositionList);
		String exceedingMaxLimit = "";
		String operation = (String) request
				.getParameter(edu.wustl.catissuecore.util.global.Constants.OPERATION);
		if (operation == null || operation.trim().equals(""))
		{
			operation = edu.wustl.catissuecore.util.global.Constants.ADD;
		}
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.OPERATION, operation);
		//String virtuallyLocated = request.getParameter("virtualLocated");
		//
		//		if (virtuallyLocated != null && virtuallyLocated.equals("false"))
		//		{
		//			//specimenForm.setVirtuallyLocated(false);
		//		}
		String specimenId = request.getParameter("specimenId");
		List initialValues = null;
		String stContSelection = request.getParameter("stContSelection");
		request.setAttribute("stContSelection", stContSelection);
		int specimenCount = 0;
		if (shipmentReceivingForm != null && shipmentReceivingForm.getSpecimenCollection() != null)
		{
			specimenCount = shipmentReceivingForm.getSpecimenCollection().size();
		}
		checkForSufficientAvailablePositions(request, containerMap, specimenCount,
				ApplicationProperties.getValue("shipment.contentsSpecimens"));
		populateSpecimenStorageLocations(shipmentReceivingForm, containerMap);
		if (operation.equals(edu.wustl.catissuecore.util.global.Constants.ADD)
				&& requestFor != null && !requestFor.trim().equals("") && stContSelection != null)
		{
			if (stContSelection.equals("3"))
			{
				String collectionProtocolId = "";
				if (specimenId != null && !specimenId.trim().equals(""))
				{
					Specimen specimen = new Specimen();
					specimen.setId(Long.parseLong(specimenId));
					DAO dao = null;
					try
					{
						//Create DAO
						dao = AppUtility.openDAOSession(null);
						IFactory factory = AbstractFactoryConfig.
						getInstance().getBizLogicFactory();
						NewSpecimenBizLogic newSpecimenBizLogic =
							(NewSpecimenBizLogic) factory
								.getBizLogic(edu.wustl.catissuecore.util.global.
										Constants.NEW_SPECIMEN_FORM_ID);

						collectionProtocolId =
							newSpecimenBizLogic.getObjectId(dao, specimen);
						if (collectionProtocolId != null 
								&& !collectionProtocolId.trim().equals(""))
						{
							collectionProtocolId = collectionProtocolId.split("_")[1];
						}
					}
					catch (ApplicationException e)
					{
						collectionProtocolId = "";
						logger.debug(e.getMessage(), e);
					}
					finally
					{
						try
						{
							AppUtility.closeDAOSession(dao);
						}
						catch (ApplicationException e)
						{
							logger.debug(e.getMessage(), e);
							e.printStackTrace();
						}
					}
				}
				request.setAttribute(
						edu.wustl.catissuecore.util.
						global.Constants.COLLECTION_PROTOCOL_ID,
						collectionProtocolId);
			}
		}
		//		else
		//		{
		//		}
		if (specimenId != null && !specimenId.trim().equals(""))
		{
			request.setAttribute(edu.wustl.catissuecore.util.global.Constants.SPECIMEN_ID,
					specimenId);
		}
		request.setAttribute("initValues", initialValues);
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.EXCEEDS_MAX_LIMIT,
				exceedingMaxLimit);
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.AVAILABLE_CONTAINER_MAP,
				containerMap);
	}

	/**
	 * This function populates the availability map with available storage locations.
	 * @param form object of AliquotForm.
	 * @param containerMap Map containing data for container
	 */
	private void populateSpecimenStorageLocations(ShipmentReceivingForm form, Map containerMap)
	{
		Map specimenMap = form.getSpecimenDetailsMap();
		int counter = 1;
		int numSpecimens = 0;
		if (form.getSpecimenCollection() != null)
		{
			numSpecimens = form.getSpecimenCollection().size();
		}
		if (!containerMap.isEmpty())
		{
			Object[] containerId = containerMap.keySet().toArray();
			for (int i = 0; i < containerId.length; i++)
			{
				Map xDimMap = (Map) containerMap.get(containerId[i]);
				if (!xDimMap.isEmpty())
				{
					Object[] xDim = xDimMap.keySet().toArray();
					for (int j = 0; j < xDim.length; j++)
					{
						List yDimList = (List) xDimMap.get(xDim[j]);
						for (int k = 0; k < yDimList.size(); k++)
						{
							if (counter <= numSpecimens)
							{
								String initailValuesKey = "Specimen:"
									+ counter + "_initialValues";
								String[] initialValues = new String[3];
								initialValues[0] =
									((NameValueBean) containerId[i]).getValue();
								initialValues[1] =
									((NameValueBean) xDim[j]).getValue();
								initialValues[2] =
									((NameValueBean) yDimList.get(k)).getValue();
								specimenMap.put(initailValuesKey, initialValues);
								counter++;
							}
							else
							{
								j = xDim.length;
								i = containerId.length;
								break;
							}
						}
					}
				}
			}
		}
		else
		{
			for (int specimenCounter = 0; specimenCounter < numSpecimens; specimenCounter++)
			{
				String initailValuesKey = "Specimen:" + (specimenCounter + 1) + "_initialValues";
				String[] initialValues = new String[3];
				initialValues[0] = "";
				initialValues[1] = "";
				initialValues[2] = "";
				specimenMap.put(initailValuesKey, initialValues);
			}
		}
		form.setSpecimenDetailsMap(specimenMap);
	}

	/**
	 * This function populates the availability map with available storage locations.
	 * @param form object of AliquotForm
	 * @param containerMap Map containing data for container
	 */
	private void populateContainerStorageLocations(ShipmentReceivingForm form, Map containerMap)
	{
		Map shipmentContainerMap = form.getContainerDetailsMap();
		int counter = 1;
		int numContainers = 0;
		if (form.getContainerCollection() != null)
		{
			numContainers = form.getContainerCollection().size();
		}
		if (!containerMap.isEmpty())
		{
			Object[] containerId = containerMap.keySet().toArray();
			for (int i = 0; i < containerId.length; i++)
			{
				Map xDimMap = (Map) containerMap.get(containerId[i]);
				if (!xDimMap.isEmpty())
				{
					Object[] xDim = xDimMap.keySet().toArray();
					for (int j = 0; j < xDim.length; j++)
					{
						List yDimList = (List) xDimMap.get(xDim[j]);
						for (int k = 0; k < yDimList.size(); k++)
						{
							if (counter <= numContainers)
							{
								String initailValuesKey = "Container:"
									+ counter + "_initialValues";

								String[] initialValues = new String[3];
								initialValues[0] = ((NameValueBean) containerId[i]).getValue();
								initialValues[1] = ((NameValueBean) xDim[j]).getValue();
								initialValues[2] = ((NameValueBean) yDimList.get(k)).getValue();
								shipmentContainerMap.put
								(initailValuesKey, initialValues);

								counter++;
							}
							else
							{
								j = xDim.length;
								i = containerId.length;
								break;
							}
						}
					}
				}
			}
		}
		else
		{
			for (int containerCounter = 0; containerCounter < numContainers; containerCounter++)
			{
				String initailValuesKey = "Container:" + (containerCounter + 1) + "_initialValues";
				String[] initialValues = new String[3];
				initialValues[0] = "";
				initialValues[1] = "";
				initialValues[2] = "";
				shipmentContainerMap.put(initailValuesKey, initialValues);
			}
		}
		form.setContainerDetailsMap(shipmentContainerMap);
	}

	/**
	 * This method checks whether there are sufficient storage locations are available or not.
	 * @param request object of HttpServletRequest.
	 * @param storableCount integer containing count.
	 * @param containerMap Map containing data for contaner.
	 * @param objectName string containing the name of the object.
	 */
	private void checkForSufficientAvailablePositions(HttpServletRequest request, Map containerMap,
			int storableCount, String objectName)
	{
		// Commented due to removing 'auto' functionality.
		/*
		int counter = 0;
		if(!containerMap.isEmpty())
		{
			counter = StorageContainerUtil.checkForLocation(containerMap, storableCount, counter);
		}
		// Check whether to store more contents than available positions.
		//if (containerMap.isEmpty() || counter<storableCount)
		if(counter < storableCount) {
			ActionErrors errors = getActionErrors(request);

			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
				"errors.locations.notSufficient.shipmentReceive",objectName));
			saveErrors(request, errors);
		}
		*/
	}

	/**
	 * returns the errors.
	 * @param request gives the error key.
	 * @return errors.
	 */
	/*private ActionErrors getActionErrors(HttpServletRequest request)
	{
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);

		if (errors == null)
		{
			errors = new ActionErrors();
		}

		return errors;
	}*/

	/* Method returns the storage position list - without 'Auto'*/
	/**
	 * gets the list of storage position type.
	 * @return list.
	 */
	private List getStoragePositionTypeList()
	{
		List < NameValueBean > storagePositionTypeList = new ArrayList < NameValueBean >();
		storagePositionTypeList.add(new NameValueBean(
				edu.wustl.catissuecore.util.global.Constants.STORAGE_TYPE_POSITION_VIRTUAL,
				edu.wustl.catissuecore.util.global.Constants.STORAGE_TYPE_POSITION_VIRTUAL_VALUE));
		storagePositionTypeList.add(new NameValueBean(
				edu.wustl.catissuecore.util.global.Constants.STORAGE_TYPE_POSITION_MANUAL,
				edu.wustl.catissuecore.util.global.Constants.STORAGE_TYPE_POSITION_MANUAL_VALUE));
		return storagePositionTypeList;
	}
}
