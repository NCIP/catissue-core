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
			
			// ---------------------------------------------
			String operation = request.getParameter("operation");
			if(operation.equals("add") )
			{
				String specimenId = request.getParameter(Constants.SPECIMEN_ID); 
		      	DefaultBizLogic bizLogic = new DefaultBizLogic();
		    	
		    	String identifier = (String)request.getAttribute(Constants.SPECIMEN_ID);
		    	if(identifier == null)
		    		identifier = (String)request.getParameter(Constants.SPECIMEN_ID);
		    	
		    	Logger.out.debug("\t\t*******************************SpecimenID : "+identifier );
		    	List specimenList = bizLogic.retrieve(Specimen.class.getName(),Constants.SYSTEM_IDENTIFIER,identifier);
		    	
		    	String posOne = null;
		    	String posTwo = null;
		    	String storContId = null;
		    	String fromPositionData = null;
		    	if(specimenList!=null && specimenList.size() != 0)
		    	{
		    		Specimen specimen = (Specimen)specimenList.get(0);
		    		posOne = specimen.getPositionDimensionOne().toString();
		    		posTwo = specimen.getPositionDimensionTwo().toString();
		    		
		    		StorageContainer container = specimen.getStorageContainer();
		    		storContId = container.getSystemIdentifier().toString();
		    		fromPositionData = container.getStorageType().getType() + " : " 
					+ storContId + " Pos(" + posOne + "," + posTwo + ")";
		    		
		            Logger.out.debug("\t\t************************************");
		            Logger.out.debug("\t\t*******************************SPID : "+specimenId );
		            Logger.out.debug("\t\t*******************************fromPosData : "+fromPositionData );
		            Logger.out.debug("\t\t*******************************Pos 1 : " + posOne );
		            Logger.out.debug("\t\t*******************************Pos 2 : " + posTwo );
					 //The fromPositionData(storageContainer Info) of specimen of this event.
			        request.setAttribute(Constants.FROM_POSITION_DATA, fromPositionData);
			        
			        //POSITION 1
			        request.setAttribute(Constants.POS_ONE, posOne);
			        
			        //POSITION 2
			        request.setAttribute(Constants.POS_TWO, posTwo);
	
			        //storagecontainer info
			        request.setAttribute(Constants.STORAGE_CONTAINER_ID, storContId);
		    	}
			} // operation=edit
			// ---------------------------------------------

	        
		}
		catch (Exception exc)
		{
			Logger.out.error(exc.getMessage());
		}
	}
	
}