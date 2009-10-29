
package edu.wustl.catissuecore.action.shippingtracking;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentReceivingForm;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.shippingtracking.ShipmentBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
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
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		Long shipmentId = null;
		if (request.getParameter(edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER) != null)
		{
			shipmentId = Long.parseLong(request
					.getParameter(edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER));
		}
		final ShipmentReceivingForm shipmentReceivingForm = (ShipmentReceivingForm) form;
		Shipment shipment = null;
		// Get the Shipment details.
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final ShipmentBizLogic shipmentBizLogic = (ShipmentBizLogic) factory
		.getBizLogic(Constants.SHIPMENT_FORM_ID);
		final String requestFor = request.getParameter("requestFor");
		if (requestFor == null || (requestFor != null && !requestFor.equals("storageLocation")))
		{
			shipment = shipmentBizLogic.getShipmentObject(shipmentId);
			shipmentReceivingForm.setAllValues(shipment);
		}
		Map containerMap = new TreeMap();
		final StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) factory
		.getBizLogic(edu.wustl.catissuecore.util.global.Constants.STORAGE_CONTAINER_FORM_ID);
		final SessionDataBean sessionDataBean = this.getSessionData(request);
		// Uncommented due to addition of 'auto' functionality.
        // Bug 14263 
		if(sessionDataBean!=null)
		{
			DAO dao = null;
			try 
			{
				dao = AppUtility.openDAOSession(sessionDataBean);
				String operation = request.getParameter(edu.wustl.catissuecore.util.global.Constants.OPERATION);
				if (operation == null || operation.trim().equals(""))
				{
					operation = edu.wustl.catissuecore.util.global.Constants.ADD;
				}
				request.setAttribute(edu.wustl.catissuecore.util.global.Constants.OPERATION, operation);
				String stContSelection = request.getParameter("stContSelection");
				request.setAttribute("stContSelection", stContSelection);
				String specimenId = request.getParameter("specimenId");
				if(specimenId!=null)
				{
					String specimenClass = (String)shipmentBizLogic.retrieveAttribute(Specimen.class.getName(), Long.valueOf( specimenId ), "specimenClass");
					if (operation.equals(edu.wustl.catissuecore.util.global.Constants.ADD)
							&& requestFor != null && !requestFor.trim().equals("") && stContSelection != null)
					{
						String collectionProtocolId = getCPIdFromSpecimen(specimenId,dao);
						request.setAttribute(edu.wustl.catissuecore.util.global.Constants.COLLECTION_PROTOCOL_ID,
								collectionProtocolId);
						if(!collectionProtocolId.trim().equals( "" ))
						{
							containerMap = bizLogic.getAllocatedContainerMapForSpecimen( Long.valueOf( collectionProtocolId ).longValue(), specimenClass, 0, "false", sessionDataBean, dao );
						}
					}
					this.setContainerStorageInformation(containerMap, bizLogic, request, shipmentReceivingForm);
				}
				this.setSpecimenStorageInformation(bizLogic, containerMap, request, shipmentReceivingForm,
						shipment);				
			}
			finally 
			{
				dao.closeSession();
			}
		}
		// For ParentStorageContainer Site
		final String[] siteDisplayField = {"name"};
		final String valueField = "id";
		final String[] activityStatusArray = {Status.ACTIVITY_STATUS_DISABLED.toString(),
				Status.ACTIVITY_STATUS_CLOSED.toString()};
		final List list = bizLogic.getSiteList(siteDisplayField, valueField, activityStatusArray,
				sessionDataBean.getUserId());
		final NameValueBean nvbForSelect = (NameValueBean) list.get(0);
		if (!"-1".equals(nvbForSelect.getValue()))
		{
			final NameValueBean nvb = new NameValueBean("--Select--", "-1");
			list.add(0, nvb);
		}
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.SITELIST, list);
		return mapping.findForward(edu.wustl.catissuecore.util.global.Constants.SUCCESS);
	}
    /**
     * This method will return collectionProtocolId. 
     * @param specimenId - specimen id
     * @param dao - DAO obj
     * @return collectionProtocolId - collectionProtocolId
     * @throws BizLogicException - BizLogicException
     */
    private String getCPIdFromSpecimen(String specimenId,DAO dao) throws BizLogicException
    {
    	String collectionProtocolId = "";
		if (specimenId != null && !specimenId.trim().equals(""))
		{
			final Specimen specimen = new Specimen();
			specimen.setId(Long.parseLong(specimenId));
			try
			{
				final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
				final NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) factory
				.getBizLogic(edu.wustl.catissuecore.util.global.Constants.NEW_SPECIMEN_FORM_ID);
				collectionProtocolId = newSpecimenBizLogic.getObjectId(dao, specimen);
				if (collectionProtocolId != null && !collectionProtocolId.trim().equals(""))
				{
					collectionProtocolId = collectionProtocolId.split("_")[1];
				}
			}
			catch (final ApplicationException appEx)
			{
				collectionProtocolId = "";
				throw new BizLogicException(appEx.getErrorKey(), appEx, appEx.getMsgValues());
			}			
		}
		return collectionProtocolId;
    }
	/**
	 * sets the storage container information.
	 * @param containerMap the treemap.
	 * @param bizLogic object of StorageContainerBizLogic class.
	 * @param request the request to be processed.
	 * @param shipmentReceivingForm form containing all values.
	 */
	private void setContainerStorageInformation(Map containerMap,
			StorageContainerBizLogic bizLogic, HttpServletRequest request,
			ShipmentReceivingForm shipmentReceivingForm)
	{

		// For ParentStorageContainer Container
		final String parentContainerSelected = request.getParameter("parentContainerSelected");
		request.setAttribute("parentContainerSelected", parentContainerSelected);

		final String containerId = request.getParameter("containerObjectId");

		int containerCount = 0;
		if (shipmentReceivingForm != null && shipmentReceivingForm.getContainerCollection() != null)
		{
			containerCount = shipmentReceivingForm.getContainerCollection().size();
		}
		this.checkForSufficientAvailablePositions(request, containerMap, containerCount,
				ApplicationProperties.getValue("shipment.contentsContainers"));

		this.populateContainerStorageLocations(shipmentReceivingForm, containerMap);
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
			Map containerMap, HttpServletRequest request,
			ShipmentReceivingForm shipmentReceivingForm, Shipment shipment)
	{
		//		For Storage Location Allocation
		/*final Logger logger = Logger.getCommonLogger(ShowShipmentReceivingAction.class);
		final String requestFor = request.getParameter("requestFor");*/
		// Removing 'auto' in shipment receiving. Bug 14263
		List<NameValueBean> storagePositionList =  AppUtility.getStoragePositionTypeList();
		request.setAttribute("storageList", storagePositionList);
		final String exceedingMaxLimit = "";
		final List initialValues = null;
		this.populateSpecimenStorageLocations(shipmentReceivingForm, containerMap);
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
		final Map map = form.getSpecimenDetailsMap();
		final String keyName = "Specimen:";
		int number = 0;
		if (form.getSpecimenCollection() != null)
		{
			number = form.getSpecimenCollection().size();
		}

		this.populateStorageLocations(containerMap, map, keyName, number);
		form.setSpecimenDetailsMap(map);
	}

	/**
	 * @param containerMap Map containing data for container
	 * @param map Map
	 * @param keyName key Name Specimen or Container
	 * @param number Collection size.
	 */
	private void populateStorageLocations(Map containerMap, Map map, String keyName, int number)
	{
		if (!containerMap.isEmpty())
		{
			int counter = 1;
			final Object[] containerId = containerMap.keySet().toArray();
			for (int i = 0; i < containerId.length; i++)
			{
				final Map xDimMap = (Map) containerMap.get(containerId[i]);
				if (!xDimMap.isEmpty())
				{
					final Object[] xDim = xDimMap.keySet().toArray();
					for (int j = 0; j < xDim.length; j++)
					{
						final List yDimList = (List) xDimMap.get(xDim[j]);
						for (int k = 0; k < yDimList.size(); k++)
						{
							if (counter <= number)
							{
								final String initailValuesKey = keyName + counter
										+ "_initialValues";
								final String[] initialValues = new String[3];
								initialValues[0] = ((NameValueBean) containerId[i]).getValue();
								initialValues[1] = ((NameValueBean) xDim[j]).getValue();
								initialValues[2] = ((NameValueBean) yDimList.get(k)).getValue();
								map.put(initailValuesKey, initialValues);
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
			for (int commonCounter = 0; commonCounter < number; commonCounter++)
			{
				final String initailValueKey = keyName + (commonCounter + 1) + "_initialValues";
				final String[] initialValues = new String[3];
				initialValues[0] = "";
				initialValues[1] = "";
				initialValues[2] = "";
				map.put(initailValueKey, initialValues);
			}
		}
	}

	/**
	 * This function populates the availability map with available storage locations.
	 * @param form object of AliquotForm
	 * @param containerMap Map containing data for container
	 */
	private void populateContainerStorageLocations(ShipmentReceivingForm form, Map containerMap)
	{
		final Map map = form.getContainerDetailsMap();
		final String keyName = "Container:";
		int number = 0;
		if (form.getContainerCollection() != null)
		{
			number = form.getContainerCollection().size();
		}
		this.populateStorageLocations(containerMap, map, keyName, number);
		form.setContainerDetailsMap(map);
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
		// Uncommented due to addition of 'auto' functionality.  Bug 14263
		int counter = 0;
		if(!containerMap.isEmpty())
		{
			counter = StorageContainerUtil.checkForLocation(containerMap, storableCount, counter);
		}
		// Check whether to store more contents than available positions.
		//if (containerMap.isEmpty() || counter<storableCount)
		if(counter < storableCount) 
		{
			ActionErrors errors = getActionErrors(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
				"errors.locations.notSufficient.shipmentReceive",objectName));
			saveErrors(request, errors);
		}
		
	}

	/**
	 * returns the errors.
	 * @param request gives the error key.
	 * @return errors.
	 */
	private ActionErrors getActionErrors(HttpServletRequest request)
	{
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		if (errors == null)
		{
			errors = new ActionErrors();
		}
		return errors;
	}

	/* Method returns the storage position list - without 'Auto'*/
	/**
	 * gets the list of storage position type.
	 * @return list.
	 */
	/*private List getStoragePositionTypeList()
	{
		final List<NameValueBean> storagePositionTypeList = new ArrayList<NameValueBean>();
		storagePositionTypeList.add(new NameValueBean(
				edu.wustl.catissuecore.util.global.Constants.STORAGE_TYPE_POSITION_VIRTUAL,
				edu.wustl.catissuecore.util.global.Constants.STORAGE_TYPE_POSITION_VIRTUAL_VALUE));
		storagePositionTypeList.add(new NameValueBean(
				edu.wustl.catissuecore.util.global.Constants.STORAGE_TYPE_POSITION_MANUAL,
				edu.wustl.catissuecore.util.global.Constants.STORAGE_TYPE_POSITION_MANUAL_VALUE));
		return storagePositionTypeList;
	}*/
}
