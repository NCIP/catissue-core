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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh
 * This class initializes the fields in the TransferEventParameters Add/Edit webpage.
 */
public class TransferEventParametersAction extends SpecimenEventParametersAction
{

	protected void setRequestParameters(HttpServletRequest request)
	{
		super.setRequestParameters(request);

		try
		{
			String[] displayNameFields = {"number"};
			String valueField = "systemIdentifier";
			AbstractBizLogic dao = BizLogicFactory.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

			//Sets the TO and FROM StorageContainerList attribute to be used in the Add/Edit User Page.
			String sourceObjectName = StorageContainer.class.getName();
			List fromContainerList = dao.getList(sourceObjectName, displayNameFields, valueField);
			request.setAttribute(Constants.FROMCONTAINERLIST, fromContainerList);

			List toContainerList = dao.getList(sourceObjectName, displayNameFields, valueField);
			request.setAttribute(Constants.TOCONTAINERLIST, toContainerList);
			
			 //The fromPositionData(storageContainer Info) of specimen of this event.
	        String fromPositionData = request.getParameter(Constants.FROM_POSITION_DATA); 
	        request.setAttribute(Constants.FROM_POSITION_DATA, fromPositionData);
	        
	        //POSITION 1
	        String posOne = request.getParameter(Constants.POS_ONE); 
	        request.setAttribute(Constants.POS_ONE, posOne);
	        
	        //POSITION 2
	        String posTwo = request.getParameter(Constants.POS_TWO); 
	        request.setAttribute(Constants.POS_TWO, posTwo);

	        //storagecontainer info
	        String storContId = request.getParameter(Constants.STORAGE_CONTAINER_ID); 
	        request.setAttribute(Constants.STORAGE_CONTAINER_ID, storContId);
	        
		}
		catch (Exception exc)
		{
			Logger.out.error(exc.getMessage());
		}
	}
}