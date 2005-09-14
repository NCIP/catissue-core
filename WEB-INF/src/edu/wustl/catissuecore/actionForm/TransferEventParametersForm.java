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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
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
	protected int positionDimensionOne;

	/**
	 * Reference to dimensional position two of the specimen in new storage container after transfer.
	 */
	protected int positionDimensionTwo;

	/**
	 * Storage Container to which the transfer is made. 
	 */
	protected long storageContainer;

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
	public int getPositionDimensionOne()
	{
		return positionDimensionOne;
	}

	/**
	 * Sets the toPositionDimensionOne. 
	 * @param toPositionDimensionOne
	 * Reference to dimensional position one of the specimen in new storage container after transfer.
	 */
	public void setPositionDimensionOne(int toPositionDimensionOne)
	{
		this.positionDimensionOne = toPositionDimensionOne;
	}

	/**
	 * Returns the Reference to dimensional position two of the specimen in new storage container after transfer.
	 * @return toPositionDimensionTwo.
	 */
	public int getPositionDimensionTwo()
	{
		return positionDimensionTwo;
	}

	/**
	 * Sets the toPositionDimensionTwo. 
	 * @param toPositionDimensionTwo
	 * Reference to dimensional position two of the specimen in new storage container after transfer.
	 */
	public void setPositionDimensionTwo(int toPositionDimensionTwo)
	{
		this.positionDimensionTwo = toPositionDimensionTwo;
	}

	/**
	 * Returns the new StorageContainer.  
	 * @return the new StorageContainer. 
	 */
	public long getStorageContainer()
	{
		return storageContainer;
	}

	/**
	 * @param toStorageContainerId
	 *            The to StorageContainerId to set.
	 */
	public void setStorageContainer(long toStorageContainerId)
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
			this.positionDimensionOne = transferEventParametersObject.getToPositionDimensionOne().intValue();
			this.positionDimensionTwo = transferEventParametersObject.getToPositionDimensionTwo().intValue();
			this.fromStorageContainerId = transferEventParametersObject.getFromStorageContainer().getSystemIdentifier().longValue();
			this.storageContainer = transferEventParametersObject.getToStorageContainer().getSystemIdentifier().longValue();  
			 
			
	    }
	    catch(Exception excp)
	    {
	        Logger.out.error(excp.getMessage());
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
         	
//          check the TO position
         	if (validator.isEmpty(String.valueOf(positionInStorageContainer )))
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("transfereventparameters.toposition")));
            }

         	
//            //	 checks the fromPositionDimensionOne 
//         	if (!validator.isNumeric(String.valueOf(fromPositionDimensionOne)))
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("transfereventparameters.frompositiondimensionone")));
//            }
//
//            //	 checks the fromPositionDimensionTwo 
//         	if (!validator.isNumeric(String.valueOf(fromPositionDimensionTwo) ))
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("transfereventparameters.frompositiondimensiontwo")));
//            }
//
//         	//	 checks the toPositionDimensionOne 
//         	if (!validator.isNumeric(String.valueOf(toPositionDimensionOne)) )
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("transfereventparameters.topositiondimensionone")));
//            }
//            //	 checks the toPositionDimensionTwo 
//         	if (!validator.isNumeric(String.valueOf(toPositionDimensionTwo)))
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("transfereventparameters.topositiondimensiontwo")));
//            }
//             //	 checks the fromStorageContainerId 
//         	if (fromStorageContainerId <= 0 )
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("transfereventparameters.fromstoragecontainerid")));
//            }
//            //	 checks the toStorageContainerId
//         	if (toStorageContainerId  <= 0 )
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("transfereventparameters.tostoragecontainerid")));
//            }

         }
         catch(Exception excp)
         {
             Logger.out.error(excp.getMessage());
         }
         return errors;
      }
	

     
	protected void reset()
	{
        super.reset();
        this.fromPositionDimensionOne = 0;
        this.fromPositionDimensionTwo = 0;
        this.positionDimensionOne = 0;
        this.positionDimensionTwo = 0;
        this.fromStorageContainerId = -1;
        this.storageContainer = -1;
        this.fromPosition = null;
        this.positionInStorageContainer = null;
		
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
