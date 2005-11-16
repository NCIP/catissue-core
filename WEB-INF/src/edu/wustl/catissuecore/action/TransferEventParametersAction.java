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
import edu.wustl.catissuecore.bizlogic.DefaultBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh
 * This class initializes the fields in the TransferEventParameters Add/Edit webpage.
 */
public class TransferEventParametersAction extends SpecimenEventParametersAction
{

	protected void setRequestParameters(HttpServletRequest request) throws Exception
	{
		String[] displayNameFields = {"number"};
		String valueField = "systemIdentifier";
		AbstractBizLogic dao = BizLogicFactory.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

		//Sets the TO and FROM StorageContainerList attribute to be used in the Add/Edit User Page.
		String sourceObjectName = StorageContainer.class.getName();
		List fromContainerList = dao.getList(sourceObjectName, displayNameFields, valueField, true);
		request.setAttribute(Constants.FROMCONTAINERLIST, fromContainerList);

		List toContainerList = dao.getList(sourceObjectName, displayNameFields, valueField, true);
		request.setAttribute(Constants.TOCONTAINERLIST, toContainerList);
		  
		// ---------------------------------------------
		String operation = request.getParameter(Constants.OPERATION);
		if(operation.equals(Constants.ADD) )
		{
			String specimenId = request.getParameter(Constants.SPECIMEN_ID); 
	      	DefaultBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
	    	
	    	String identifier = (String)request.getAttribute(Constants.SPECIMEN_ID);
	    	if(identifier == null)
	    		identifier = (String)request.getParameter(Constants.SPECIMEN_ID);
	    	
	    	Logger.out.debug("\t\t*******************************SpecimenID : "+identifier );
	    	List specimenList = bizLogic.retrieve(Specimen.class.getName(),Constants.SYSTEM_IDENTIFIER,identifier);
	    	
	    	String positionOne = null;
	    	String positionTwo = null;
	    	String storageContainerID = null;
	    	String fromPositionData = null;
	    	if(specimenList!=null && specimenList.size() != 0)
	    	{
	    		Specimen specimen = (Specimen)specimenList.get(0);
	    		positionOne = specimen.getPositionDimensionOne().toString();
	    		positionTwo = specimen.getPositionDimensionTwo().toString();
	    		
	    		StorageContainer container = specimen.getStorageContainer();
	    		storageContainerID = container.getSystemIdentifier().toString();
	    		fromPositionData = container.getStorageType().getType() + " : " 
				+ storageContainerID + " Pos(" + positionOne + "," + positionTwo + ")";
	    		
	            Logger.out.debug("\t\t************************************");
	            Logger.out.debug("\t\t*******************************SPID : "+specimenId );
	            Logger.out.debug("\t\t*******************************fromPosData : "+fromPositionData );
	            Logger.out.debug("\t\t*******************************Pos 1 : " + positionOne );
	            Logger.out.debug("\t\t*******************************Pos 2 : " + positionTwo );
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