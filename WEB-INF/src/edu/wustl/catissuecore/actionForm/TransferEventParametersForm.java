/**
 * <p>Title: TransferEventParametersForm Class</p>
 * <p>Description:  This Class handles the Transfer Event Parameters.
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 28th, 2005
 */
package edu.wustl.catissuecore.actionForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


/**
 * @author mandar_deshmukh
 *
 *  This Class handles the Transfer Event Parameters.
 */
public class TransferEventParametersForm extends SpecimenEventParametersForm
{
	/**
	 * Reference to dimensional position one of the specimen in previous storage container before transfer.
	 */
	protected int fromPositionDimensionOne;

	/**
	 * Reference to dimensional position two of the specimen in previous storage container before transfer.
	 */
	protected int fromPositionDimensionTwo;

	/**
	 * Reference to dimensional position one of the specimen in new storage container after transfer.
	 */
	protected String positionDimensionOne;

	/**
	 * Reference to dimensional position two of the specimen in new storage container after transfer.
	 */
	protected String positionDimensionTwo;

	/**
	 * Storage Container to which the transfer is made. 
	 */
	protected String storageContainer;

	/**
	 * Storage Container from which the transfer is made.
	 */
	protected long fromStorageContainerId;
	
	/*
	 * Used for getting the TO and FROM Positions from the JSP 
	 */
	protected String fromPosition;
	
	protected String positionInStorageContainer;

	/**
	 * Returns the Reference to dimensional position one of the specimen in previous storage container before transfer.
	 * @return fromPositionDimensionOne.
	 */
	public int getFromPositionDimensionOne()
	{
		return fromPositionDimensionOne;
	}

	/**
	 * Sets the fromPositionDimensionOne. 
	 * @param fromPositionDimensionOne
	 * Reference to dimensional position one of the specimen in previous storage container before transfer.
	 */
	public void setFromPositionDimensionOne(int fromPositionDimensionOne)
	{
		this.fromPositionDimensionOne = fromPositionDimensionOne;
	}

	/**
	 * Returns the Reference to dimensional position two of the specimen in previous storage container before transfer.
	 * @return fromPositionDimensionTwo.
	 */
	public int getFromPositionDimensionTwo()
	{
		return fromPositionDimensionTwo;
	}

	/**
	 * Sets the fromPositionDimensionTwo. 
	 * 
	 * @param fromPositionDimensionTwo
	 * Reference to dimensional position two of the specimen in previous storage container before transfer.
	 */
	public void setFromPositionDimensionTwo(int fromPositionDimensionTwo)
	{
		this.fromPositionDimensionTwo = fromPositionDimensionTwo;
	}

	/**
	 * Returns the Reference to dimensional position one of the specimen in new storage container after transfer.
	 * @return toPositionDimensionOne.
	 */
	public String getPositionDimensionOne()
	{
		return positionDimensionOne;
	}

	/**
	 * Sets the toPositionDimensionOne. 
	 * @param toPositionDimensionOne
	 * Reference to dimensional position one of the specimen in new storage container after transfer.
	 */
	public void setPositionDimensionOne(String toPositionDimensionOne)
	{
		this.positionDimensionOne = toPositionDimensionOne;
	}

	/**
	 * Returns the Reference to dimensional position two of the specimen in new storage container after transfer.
	 * @return toPositionDimensionTwo.
	 */
	public String getPositionDimensionTwo()
	{
		return positionDimensionTwo;
	}

	/**
	 * Sets the toPositionDimensionTwo. 
	 * @param toPositionDimensionTwo
	 * Reference to dimensional position two of the specimen in new storage container after transfer.
	 */
	public void setPositionDimensionTwo(String toPositionDimensionTwo)
	{
		this.positionDimensionTwo = toPositionDimensionTwo;
	}

	/**
	 * Returns the new StorageContainer.  
	 * @return the new StorageContainer. 
	 */
	public String getStorageContainer()
	{
		return storageContainer;
	}

	/**
	 * @param toStorageContainerId
	 *            The to StorageContainerId to set.
	 */
	public void setStorageContainer(String toStorageContainerId)
	{
		this.storageContainer = toStorageContainerId;
	}

	/**
	 * Returns the old StorageContainer. 
	 * @return the old StorageContainer. 
	 */
	public long getFromStorageContainerId()
	{

		return fromStorageContainerId;
	}

	/**
	 * @param fromStorageContainer
	 *            The from StorageContainer to set.
	 */
	public void setFromStorageContainerId(long fromStorageContainerId)
	{
		this.fromStorageContainerId = fromStorageContainerId;
	}

//	 ----- SUPERCLASS METHODS
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.TRANSFER_EVENT_PARAMETERS_FORM_ID;
	}

	public void setAllValues(AbstractDomainObject abstractDomain)
	{
	    try
        {
			super.setAllValues(abstractDomain);
			TransferEventParameters transferEventParametersObject = (TransferEventParameters)abstractDomain ;
			this.fromPositionDimensionOne = transferEventParametersObject.getFromPositionDimensionOne().intValue();
			this.fromPositionDimensionTwo = transferEventParametersObject.getFromPositionDimensionTwo().intValue();
			this.positionDimensionOne = transferEventParametersObject.getToPositionDimensionOne().toString();
			this.positionDimensionTwo = transferEventParametersObject.getToPositionDimensionTwo().toString();
			this.fromStorageContainerId = transferEventParametersObject.getFromStorageContainer().getSystemIdentifier().longValue();
			this.storageContainer = transferEventParametersObject.getToStorageContainer().getSystemIdentifier().toString() ;  
			this.positionInStorageContainer = transferEventParametersObject.getToStorageContainer().getStorageType().getName() + " : " 
				+ this.storageContainer + " Pos(" + this.positionDimensionOne + ","
				+ this.positionDimensionTwo + ")";
			this.fromPosition = transferEventParametersObject.getFromStorageContainer().getStorageType().getName() + " : " 
			+ this.fromStorageContainerId + " Pos(" + this.fromPositionDimensionOne + ","
			+ this.fromPositionDimensionTwo + ")";
			
//			// ---------------------------------------------
//			String specimenId = transferEventParametersObject.getSpecimen().getSystemIdentifier().toString(); 
//			IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
//	    	
//	    	String identifier = specimenId ;
//	    	
//	    	Logger.out.debug("\t\t*****************Form**************SpecimenID : "+identifier );
//	    	List specimenList = bizLogic.retrieve(Specimen.class.getName(),Constants.SYSTEM_IDENTIFIER,identifier);
//	    	
//	    	String posOne = null;
//	    	String posTwo = null;
//	    	String storContId = null;
//	    	String fromPositionData = null;
//	    	if(specimenList!=null && specimenList.size() != 0)
//	    	{
//	    		Specimen specimen = (Specimen)specimenList.get(0);
//	    		posOne = specimen.getPositionDimensionOne().toString();
//	    		posTwo = specimen.getPositionDimensionTwo().toString();
//	    		
//	    		StorageContainer container = specimen.getStorageContainer();
//	    		storContId = container.getSystemIdentifier().toString();
//	    		fromPositionData = container.getStorageType().getName() + " : " 
//				+ storContId + " Pos(" + posOne + "," + posTwo + ")";
//	    		
//	            Logger.out.debug("\t\t************************************");
//	            Logger.out.debug("\t\t*******************************SPID : "+specimenId );
//	            Logger.out.debug("\t\t*******************************fromPosData : "+fromPositionData );
//	            Logger.out.debug("\t\t*******************************Pos 1 : " + posOne );
//	            Logger.out.debug("\t\t*******************************Pos 2 : " + posTwo );
//	    	}	
//			// ---------------------------------------------
			
	    }
	    catch(Exception excp)
	    {
	        Logger.out.error(excp.getMessage());
	    }
	}
	
	public void setAllVal(Object object)
	{
		super.setAllVal(object);
		edu.wustl.catissuecore.domainobject.TransferEventParameters parameter
				= (edu.wustl.catissuecore.domainobject.TransferEventParameters)object;
		
		//Aniruddha : Fix for bug - 1613
		if(parameter.getFromPositionDimensionOne() != null)
		{
			this.fromPositionDimensionOne = parameter.getFromPositionDimensionOne().intValue();
		}
		else
		{
			this.fromPositionDimensionOne = 0;
		}
		
		if(parameter.getFromPositionDimensionTwo() != null)
		{
			this.fromPositionDimensionTwo = parameter.getFromPositionDimensionTwo().intValue();
		}
		else
		{
			this.fromPositionDimensionTwo = 0;
		}
		
		if(parameter.getToPositionDimensionOne() != null)
		{
			this.positionDimensionOne = parameter.getToPositionDimensionOne().toString();
		}
		else
		{
			this.positionDimensionOne = "0";
		}
		
		if(parameter.getToPositionDimensionTwo() != null)
		{
			this.positionDimensionTwo = parameter.getToPositionDimensionTwo().toString();
		}
		else
		{
			this.positionDimensionTwo = "0";
		}
		
		if(parameter.getFromStorageContainer() != null 
				&& parameter.getFromStorageContainer().getId() != null)
		{
			this.fromStorageContainerId = parameter.getFromStorageContainer().getId().longValue();
		}
		else
		{
			this.fromStorageContainerId = 0L;
		}
		
		
		if(parameter.getToStorageContainer() != null 
				&& parameter.getToStorageContainer().getId() != null)
		{
			this.storageContainer = parameter.getToStorageContainer().getId().toString();
		}
		else
		{
			this.storageContainer = "0";
		}
		
		String toStorageType = "";
		
		if(parameter.getToStorageContainer().getStorageType() != null
				&& parameter.getToStorageContainer().getStorageType().getType() != null)
		{
			toStorageType = parameter.getToStorageContainer().getStorageType().getType();
		}
		
		this.positionInStorageContainer = toStorageType + " : " 
			+ this.storageContainer + " Pos(" + this.positionDimensionOne + ","
			+ this.positionDimensionTwo + ")";
		
		String fromStorageType = "";
		
		if(parameter.getFromStorageContainer().getStorageType() != null 
				&& parameter.getFromStorageContainer().getStorageType().getType() != null)
		{
			fromStorageType = parameter.getFromStorageContainer().getStorageType().getType();
		}
		
		this.fromPosition = fromStorageType + " : " 
		+ this.fromStorageContainerId + " Pos(" + this.fromPositionDimensionOne + ","
		+ this.fromPositionDimensionTwo + ")";
		
		String specimenId = "";
		
		if(parameter.getSpecimen() != null && parameter.getSpecimen().getId() != null)
		{
			specimenId = parameter.getSpecimen().getId().toString();
		}
		else
		{
			specimenId = "0";
		}
      	IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
    	
    	String identifier = specimenId ;
    	
    	try
		{
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
	    		fromPositionData = container.getStorageType().getName() + " : " 
				+ storContId + " Pos(" + posOne + "," + posTwo + ")";
	    	}
		}
    	catch(Exception e)
		{
    		Logger.out.debug(e.getMessage(),e);
		}
	}
	
	/**
     * Overrides the validate method of ActionForm.
     * */
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
     {
     	ActionErrors errors = super.validate(mapping, request);
         Validator validator = new Validator();
         
         try
         {
         	// check the FROM Position
         	if (validator.isEmpty(String.valueOf(fromPosition)))
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("transfereventparameters.fromposition")));
            }
    
            if(validator.isEmpty(positionDimensionOne) || validator.isEmpty(positionDimensionTwo) || validator.isEmpty(storageContainer ))
            {
                  errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                  "errors.item.required", ApplicationProperties
                          .getValue("transfereventparameters.toposition")));
            }
            else
            {
                if(!validator.isNumeric(positionDimensionOne,1) || !validator.isNumeric(positionDimensionTwo,1) || !validator.isNumeric(storageContainer ,1))
                {
	                  errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
	                  "errors.item.format", ApplicationProperties
	                          .getValue("transfereventparameters.toposition")));
                }
            	
            }
         }
         catch(Exception excp)
         {
             Logger.out.error(excp.getMessage());
         }
         return errors;
      }
	

     
	protected void reset()
	{
//        super.reset();
//        this.fromPositionDimensionOne = 0;
//        this.fromPositionDimensionTwo = 0;
//        this.positionDimensionOne = 0;
//        this.positionDimensionTwo = 0;
//        this.fromStorageContainerId = -1;
//        this.storageContainer = -1;
//        this.fromPosition = null;
//        this.positionInStorageContainer = null;
		
	}
	
	/**
	 * @return Returns the fromPosition.
	 */
	public String getFromPosition() {
		return fromPosition;
	}
	/**
	 * @param fromPosition The fromPosition to set.
	 */
	public void setFromPosition(String fromPosition) {
		this.fromPosition = fromPosition;
	}
	/**
	 * @return Returns the toPosition.
	 */
	public String getPositionInStorageContainer() {
		return positionInStorageContainer;
	}
	/**
	 * @param toPosition The toPosition to set.
	 */
	public void setPositionInStorageContainer(String toPosition) {
		this.positionInStorageContainer = toPosition;
	}
}
