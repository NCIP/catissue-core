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

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
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
		String operation = request.getParameter(Constants.OPERATION);
		if(operation.equals(Constants.ADD) )
		{
			IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
	    	String identifier = (String)request.getAttribute(Constants.SPECIMEN_ID);
	    	if(identifier == null)
	    		identifier = (String)request.getParameter(Constants.SPECIMEN_ID);
	    	
	    	Logger.out.debug("\t\t*******************************SpecimenID : "+identifier );
	    	List specimenList = bizLogic.retrieve(Specimen.class.getName(),Constants.SYSTEM_IDENTIFIER,identifier);
	    	
	    	/*
//	    	 ---- chetan 15-06-06 ----
	        StorageContainerBizLogic scbizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
	        Map containerMap = scbizLogic.getAllocatedContainerMap();
	        request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,containerMap);
	        // -------------------------
	    	*/
	    	if(specimenList!=null && specimenList.size() != 0)
	    	{
	    		Specimen specimen = (Specimen)specimenList.get(0);
	    		String positionOne = specimen.getPositionDimensionOne().toString();
	    		String positionTwo = specimen.getPositionDimensionTwo().toString();
	    		
	    		StorageContainer container = specimen.getStorageContainer();
	    		String storageContainerID = container.getSystemIdentifier().toString();
	    		String fromPositionData = container.getStorageType().getType() + " : " 
				+ storageContainerID + " Pos(" + positionOne + "," + positionTwo + ")";
	    		
				 //The fromPositionData(storageContainer Info) of specimen of this event.
		        request.setAttribute(Constants.FROM_POSITION_DATA, fromPositionData);
		        
		        //POSITION 1
		        request.setAttribute(Constants.POS_ONE, positionOne);
		        
		        //POSITION 2
		        request.setAttribute(Constants.POS_TWO, positionTwo);

		        //storagecontainer info
		        request.setAttribute(Constants.STORAGE_CONTAINER_ID, storageContainerID);
	    	}
		} // operation=add
	}
	
}