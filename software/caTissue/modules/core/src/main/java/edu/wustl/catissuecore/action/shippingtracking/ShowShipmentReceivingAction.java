
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
import edu.wustl.catissuecore.bizlogic.ISiteBizLogic;
import edu.wustl.catissuecore.bizlogic.SiteBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerForSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.shippingtracking.ShipmentBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Status;
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
		final StorageContainerForSpecimenBizLogic  bizLogic = new StorageContainerForSpecimenBizLogic();
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
				String specimenId = null;
				String spClass = null;
				String spType = null;
				List<Specimen> specimenList=shipmentReceivingForm.getSpecimenCollection();
				Map map = shipmentReceivingForm.getSpecimenDetailsMap();
				int specimenCounter=0;
				for (Specimen specimen : specimenList)
				{
					specimenCounter++;
					specimenId=specimen.getId().toString();

					if(specimenId!=null)
					{
						spClass = specimen.getSpecimenClass();
						spType = specimen.getSpecimenType();
						if(spType == null)
						{
							spType = (String)shipmentBizLogic.retrieveAttribute(Specimen.class.getName(), Long.valueOf( specimenId ), "specimenType");
						}
						if (operation.equals(edu.wustl.catissuecore.util.global.Constants.ADD)
								&& requestFor != null && !requestFor.trim().equals("") )
						{

							String cpId = CollectionProtocolUtil.getCPIdFromSpecimen(specimenId,dao);
							request.setAttribute(edu.wustl.catissuecore.util.global.Constants.COLLECTION_PROTOCOL_ID+"_"+specimenCounter,
									cpId);

							if(!cpId.trim().equals( "" ))
							{
								/**
								 * If some error occurs then initial values should be retained on the page
								 * thus added initial values in map.
								 */
								if(request.getAttribute( "org.apache.struts.action.ERROR" )!= null) 
								{
									List<Object> parameterList = AppUtility.setparameterList(Long.valueOf(cpId).longValue(),spClass,0,spType);
									containerMap = bizLogic.getAllocatedContainerMapForSpecimen( parameterList,sessionDataBean, dao );
									String containerId = map.get( "containerId_"+specimenId ).toString();
									String pos1 = map.get( "pos1_"+specimenId ).toString();
									String pos2 = map.get( "pos2_"+specimenId ).toString();
									final String initailValuesKey = "Specimen:" + specimenCounter
									+ "_initialValues";
									String[] initialValues = new String[3];
									initialValues[0] = containerId;
									initialValues[1] = pos1;
									initialValues[2] = pos2;
									map.put(initailValuesKey, initialValues);								
								}
							}
						}
						this.setContainerStorageInformation(containerMap, request, shipmentReceivingForm);
					}
					this.setSpecimenStorageInformation(containerMap, request, shipmentReceivingForm);
				}
				shipmentReceivingForm.setSpecimenDetailsMap(map);
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
		ISiteBizLogic sBiz = new SiteBizLogic();
		final List list = sBiz.getSiteList(siteDisplayField, valueField, activityStatusArray,
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
	 * sets the storage container information.
	 * @param containerMap the treemap.
	 * @param bizLogic object of StorageContainerBizLogic class.
	 * @param request the request to be processed.
	 * @param shipmentReceivingForm form containing all values.
	 */
	private void setContainerStorageInformation(Map containerMap,
			HttpServletRequest request,
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
		if(request.getAttribute( "org.apache.struts.action.ERROR" )== null) 
		{
			this.checkForSufficientAvailablePositions(request, containerMap, containerCount,
					ApplicationProperties.getValue("shipment.contentsContainers"));			
		}
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
	private void setSpecimenStorageInformation(
			Map containerMap, HttpServletRequest request,
			ShipmentReceivingForm shipmentReceivingForm)
	{
		List<NameValueBean> storagePositionList =  AppUtility.getStoragePositionTypeList();
		request.setAttribute("storageList", storagePositionList);
		final String exceedingMaxLimit = "";
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.EXCEEDS_MAX_LIMIT,
				exceedingMaxLimit);
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.AVAILABLE_CONTAINER_MAP,
				containerMap);

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
		if(request.getParameter("showRequest")==null)
		{
			int counter = 0;
			if(!containerMap.isEmpty())
			{
				counter = StorageContainerUtil.checkForLocation(containerMap, storableCount, counter);
			}
			if(counter < storableCount)
			{
				ActionErrors errors = getActionErrors(request);
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"errors.locations.notSufficient.shipmentReceive",objectName));
				saveErrors(request, errors);
			}
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
	
}
