/**
 * <p>Title: TransferEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the TransferEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 05, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.catissuecore.actionForm.TransferEventParametersForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh
 * This class initializes the fields in the TransferEventParameters Add/Edit webpage.
 */
public class TransferEventParametersAction extends SpecimenEventParametersAction
{

	protected void setRequestParameters(HttpServletRequest request) throws Exception
	{
		TransferEventParametersForm transferEventParametersForm = (TransferEventParametersForm) request
				.getAttribute("transferEventParametersForm");
		String operation = request.getParameter(Constants.OPERATION);
		StorageContainerBizLogic scbizLogic = (StorageContainerBizLogic) BizLogicFactory
				.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		TreeMap containerMap = new TreeMap();
		//boolean to indicate whether the suitable containers to be shown in dropdown 
		//is exceeding the max limit.
		String exceedingMaxLimit = "false";
		Vector initialValues = null;
		//    	
		if (operation.equals(Constants.ADD))
		{
			IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(
					Constants.DEFAULT_BIZ_LOGIC);
			String identifier = (String) request.getAttribute(Constants.SPECIMEN_ID);
			if (identifier == null)
				identifier = (String) request.getParameter(Constants.SPECIMEN_ID);

			Logger.out.debug("\t\t*******************************SpecimenID : " + identifier);
			List specimenList = bizLogic.retrieve(Specimen.class.getName(),
					Constants.SYSTEM_IDENTIFIER, identifier);

			//	 ---- chetan 15-06-06 ----

			// -------------------------

			if (specimenList != null && specimenList.size() != 0)
			{
				Specimen specimen = (Specimen) specimenList.get(0);

				String positionOne = null;
				String positionTwo = null;
				String storageContainerID = null;
				String fromPositionData = "virtual Location";
				if (specimen.getStorageContainer() != null)
				{
					positionOne = specimen.getPositionDimensionOne().toString();
					positionTwo = specimen.getPositionDimensionTwo().toString();
					StorageContainer container = specimen.getStorageContainer();
					storageContainerID = container.getId().toString();
					fromPositionData = container.getName()+":" + " Pos(" + positionOne + "," + positionTwo + ")";
				}
				//The fromPositionData(storageContainer Info) of specimen of this event.
				request.setAttribute(Constants.FROM_POSITION_DATA, fromPositionData);

				//POSITION 1
				request.setAttribute(Constants.POS_ONE, positionOne);

				//POSITION 2
				request.setAttribute(Constants.POS_TWO, positionTwo);

				//storagecontainer info
				request.setAttribute(Constants.STORAGE_CONTAINER_ID, storageContainerID);
				
				long cpId = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
				.getCollectionProtocol().getId().longValue();
				String className = specimen.getClassName();
				
				Logger.out.info("COllection Protocol Id :"+ cpId);
				request.setAttribute(Constants.COLLECTION_PROTOCOL_ID,cpId+"");
				request.setAttribute(Constants.SPECIMEN_CLASS_NAME,className);
				Logger.out.info("Spcimen Class:" + className);
				
				SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
				containerMap = scbizLogic.getAllocatedContaienrMapForSpecimen(cpId, className, 0,exceedingMaxLimit,sessionData,true);
				ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
				if (containerMap.isEmpty())
				{
					
					if (errors == null || errors.size() == 0)
					{
						errors = new ActionErrors();
					}
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"storageposition.not.available"));
					saveErrors(request, errors);
				}
				if(errors == null || errors.size() == 0)
				{
				      initialValues = checkForInitialValues(containerMap);
				} else
				{
					String[] startingPoints = new String[3];
					startingPoints[0] = transferEventParametersForm.getStorageContainer();
					startingPoints[1] = transferEventParametersForm.getPositionDimensionOne();
					startingPoints[2] = transferEventParametersForm.getPositionDimensionTwo() ;
					initialValues = new Vector();
					initialValues.add(startingPoints);
					
				}
				
			}
		} // operation=add
		else
		{

			Integer id = new Integer(transferEventParametersForm.getStorageContainer());
			String parentContainerName = "";
			String valueField1 = "id";
			List list = scbizLogic.retrieve(StorageContainer.class.getName(), valueField1,
					new Long(transferEventParametersForm.getStorageContainer()));
			if (!list.isEmpty())
			{
				StorageContainer container = (StorageContainer) list.get(0);
				parentContainerName = container.getName();

			}
			Integer pos1 = new Integer(transferEventParametersForm.getPositionDimensionOne());
			Integer pos2 = new Integer(transferEventParametersForm.getPositionDimensionTwo());

			List pos2List = new ArrayList();
			pos2List.add(new NameValueBean(pos2, pos2));

			Map pos1Map = new TreeMap();
			pos1Map.put(new NameValueBean(pos1, pos1), pos2List);
			containerMap.put(new NameValueBean(parentContainerName, id), pos1Map);

			String[] startingPoints = new String[]{"-1", "-1", "-1"};
			if (transferEventParametersForm.getStorageContainer() != null && !transferEventParametersForm.getStorageContainer().equals("-1"))
			{
				startingPoints[0] = transferEventParametersForm.getStorageContainer();

			}
			if (transferEventParametersForm.getPositionDimensionOne() != null
					&& !transferEventParametersForm.getPositionDimensionOne().equals("-1"))
			{
				startingPoints[1] = transferEventParametersForm.getPositionDimensionOne();
			}
			if (transferEventParametersForm.getPositionDimensionTwo() != null
					&& !transferEventParametersForm.getPositionDimensionTwo().equals("-1"))
			{
				startingPoints[2] = transferEventParametersForm.getPositionDimensionTwo();
			}
			initialValues = new Vector();
			Logger.out.info("Starting points[0]" + startingPoints[0]);
			Logger.out.info("Starting points[1]" + startingPoints[1]);
			Logger.out.info("Starting points[2]" + startingPoints[2]);
			initialValues.add(startingPoints);

		}
		request.setAttribute("initValues", initialValues);
		request.setAttribute(Constants.EXCEEDS_MAX_LIMIT,exceedingMaxLimit);
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
	}

	Vector checkForInitialValues(Map containerMap)
	{
		Vector initialValues = null;

		if (containerMap.size() > 0)
		{
			String[] startingPoints = new String[3];

			Set keySet = containerMap.keySet();
			Iterator itr = keySet.iterator();
			NameValueBean nvb = (NameValueBean) itr.next();
			startingPoints[0] = nvb.getValue();

			Map map1 = (Map) containerMap.get(nvb);
			keySet = map1.keySet();
			itr = keySet.iterator();
			nvb = (NameValueBean) itr.next();
			startingPoints[1] = nvb.getValue();

			List list = (List) map1.get(nvb);
			nvb = (NameValueBean) list.get(0);
			startingPoints[2] = nvb.getValue();

			Logger.out.info("Starting points[0]" + startingPoints[0]);
			Logger.out.info("Starting points[1]" + startingPoints[1]);
			Logger.out.info("Starting points[2]" + startingPoints[2]);
			initialValues = new Vector();
			initialValues.add(startingPoints);

		}
		return initialValues;

		//request.setAttribute("initValues", initialValues);
	}

}