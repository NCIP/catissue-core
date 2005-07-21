/**
 * <p>Title: StorageContainerForm Class>
 * <p>Description:  This Class is used to encapsulate all the request parameters passed 
 * from StorageContainer.jsp page. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 15, 2005
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to encapsulate all the request parameters passed from StorageType.jsp page.
 * @author aniruddha_phadnis
 * */
public class StorageContainerForm extends AbstractActionForm
{
    /**
     * identifier is a unique id assigned to each Storage Container.
     * */
    private long systemIdentifier;

    /**
     * A String containing the operation(Add/Edit) to be performed.
     * */
    private String operation;
    
    /**
     * A string containing the type of the storage.
     */
    private String type;
    
    /**
     * Parent Container of this container.
     */
    private String parentContainer;

    /**
     * Site of the container if it is parent container.
     */
    private String site;
    
    /**
     * A default temperature of the storage container.
     */
    private double defaultTemperature;
    
    /**
     * Capacity in dimension one.
     */
    private int oneDimensionCapacity;

    /**
     * Capacity in dimension two.
     */
    private int twoDimensionCapacity;

    /**
     * Text label for dimension one.
     */
    private String oneDimensionLabel;

    /**
     * Text label for dimension two.
     */
    private String twoDimensionLabel;
    
    /**
     * Starting Number.
     */
    private int startNumber;

    /**
     * No. of containers.
     */
    private int noOfContainers;
    
    /**
     * Text label for dimension two.
     */
    private String barcode;
    
    /**
     * Key.
     */
    private String key;
    
    /**
     * No argument constructor for StorageTypeForm class 
     */
    public StorageContainerForm()
    {
        reset();
    }

    /**
     * This function Copies the data from an storage type object to a StorageTypeForm object.
     * @param storageType A StorageType object containing the information about storage type of the container.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
//        try
//        {
//        	StorageContainer container = (StorageContainer) abstractDomain;
//            this.systemIdentifier = container.getSystemIdentifier().longValue();
//            this.type = container.getStorageType().getType();
//            this.defaultTemperature = container.getTempratureInCentigrade().doubleValue();
//            this.oneDimensionCapacity = container.getStorageContainerCapacity().getOneDimensionCapacity().intValue();
//            this.twoDimensionCapacity = container.getStorageContainerCapacity().getTwoDimensionCapacity().intValue();
//            this.oneDimensionLabel = container.getStorageContainerCapacity().getOneDimensionLabel();
//            this.twoDimensionLabel = container.getStorageContainerCapacity().getTwoDimensionLabel();
//        }
//        catch (Exception excp)
//        {
//            Logger.out.error(excp.getMessage(),excp);
//        }
    }

    /**
     * Returns the identifier assigned to Storage Container.
     * @return long identifier assigned to Storage Container.
     * @see #setIdentifier(long)
     * */
    public long getSystemIdentifier()
    {
        return (this.systemIdentifier);
    }

    /**
     * Sets an the identifier for a Storage Container.
     * @param systemIdentifier identifier to be assigned to Storage Container.
     * @see #getIdentifier()
     * */
    public void setSystemIdentifier(long systemIdentifier)
    {
        this.systemIdentifier = systemIdentifier;
    }
    
    /**
     * Returns the operation(Add/Edit) to be performed.
     * @return Returns the operation.
     * @see #setOperation(String)
     */
    public String getOperation()
    {
        return operation;
    }
    
    /**
     * Sets the operation to be performed.
     * @param operation The operation to set.
     * @see #getOperation()
     */
    public void setOperation(String operation)
    {
        this.operation = operation;
    }
    
    /**
     * Returns the type of the storage.
     * @return String the type of the storage.
     * @see #setType(String)
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * Sets the type of the storage container.
     * @param type String type of the storage container to be set.
     * @see #getType()
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Returns the default temperature of the storage container.
     * @return double the default temperature of the storage container to be set.
     * @see #setDefaultTemperature(double)
     */
    public double getDefaultTemperature()
    {
        return this.defaultTemperature;
    }

    /**
     * Sets the default temperature of the storage container.
     * @param defaultTemperature the default temperature of the storage container to be set.
     * @see #getDefaultTemperature()
     */
    public void setDefaultTemperature(double defaultTemperature)
    {
        this.defaultTemperature = defaultTemperature;
    }
   
    /**
     * Returns the capacity of dimension one.
     * @return int the capacity of dimension one.
     * @see #setOneDimensionCapacity(int)
     */
    public int getOneDimensionCapacity()
    {
        return this.oneDimensionCapacity;
    }

    /**
     * Sets the capacity of dimension one.
     * @param oneDimensionCapacity the capacity of dimension one to be set.
     * @see #getOneDimensionCapacity()
     */
    public void setOneDimensionCapacity(int oneDimensionCapacity)
    {
        this.oneDimensionCapacity = oneDimensionCapacity;
    }

    /**
     * Returns the capacity of dimension two.
     * @return int the capacity of dimension two.
     * @see #setTwoDimensionCapacity(int)
     */
    public int getTwoDimensionCapacity()
    {
        return this.twoDimensionCapacity;
    }

    /**
     * Sets the capacity of dimension two.
     * @param twoDimensionCapacity the capacity of dimension two to be set.
     * @see #getTwoDimensionCapacity()
     */
    public void setTwoDimensionCapacity(int twoDimensionCapacity)
    {
        this.twoDimensionCapacity = twoDimensionCapacity;
    }

    /**
     * Returns the label of dimension one.
     * @return String the label of dimension one.
     * @see #setOneDimensionLabel(String)
     */
    public String getOneDimensionLabel()
    {
        return this.oneDimensionLabel;
    }

    /**
     * Sets the label of dimension one.
     * @param oneDimensionLabel the label of dimension one to be set.
     * @see #getOneDimensionLabel()
     */
    public void setOneDimensionLabel(String oneDimensionLabel)
    {
        this.oneDimensionLabel = oneDimensionLabel;
    }

    /**
     * Returns the label of dimension two.
     * @return String the label of dimension two.
     * @see #setTwoDimensionLabel(String)
     */
    public String getTwoDimensionLabel()
    {
        return this.twoDimensionLabel;
    }

    /**
     * Sets the label of dimension two.
     * @param twoDimensionLabel the label of dimension two to be set.
     * @see #getTwoDimensionLabel()
     */
    public void setTwoDimensionLabel(String twoDimensionLabel)
    {
        this.twoDimensionLabel = twoDimensionLabel;
    }

    /**
     * Returns the parent container.
     * @return String the parent container.
     * @see #setParentContainer(String)
     */
	public String getParentContainer()
	{
		return parentContainer;
	}
	
	/**
     * Sets the parent container.
     * @param parentContainer the parent container.
     * @see #getParentContainer()
     */
	public void setParentContainer(String parentContainer)
	{
		this.parentContainer = parentContainer;
	}
	
	/**
     * Returns the site of the container.
     * @return String the site of the container.
     * @see #setSite(String)
     */
	public String getSite()
	{
		return site;
	}
	
	/**
     * Sets the site of the container.
     * @param parentContainer the site of the container.
     * @see #getSite()
     */
	public void setSite(String site)
	{
		this.site = site;
	}   
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getActivityStatus()
     */
    public String getActivityStatus()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setActivityStatus(java.lang.String)
     */
    public void setActivityStatus(String activityStatus)
    {
        // TODO Auto-generated method stub

    }
    
    /**
     * Checks the operation to be performed is add or not.
     * @return Returns true if operation is equal to "add", else it returns false
     * */
    public boolean isAddOperation()
    {
        return(getOperation().equals(Constants.ADD));
    }
    
    /**
     * Returns the id assigned to form bean
     */
    public int getFormId()
    {
        return Constants.STORAGE_CONTAINER_FORM_ID;
    }
    
    /**
     * Resets the values of all the fields.
     * This method defined in ActionForm is overridden in this class.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        reset();
    }
    
    /**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.  
     * */
    private void reset()
    {
    }

    /**
    * Overrides the validate method of ActionForm.
    * */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        
        try
        {
            if (operation.equals(Constants.SEARCH))
            {
                checkValidNumber(new Long(systemIdentifier).toString(),"storageContainer.identifier",errors,validator);
            }
            if (operation.equals(Constants.ADD) || operation.equals(Constants.EDIT))
            {             
                if (validator.isEmpty(type))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("storageType.type")));
                }  
                
            }
        }
        catch(Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
        return errors;
     }
	/**
	 * @return Returns the noOfContainers.
	 */
	public int getNoOfContainers()
	{
		return noOfContainers;
	}
	/**
	 * @param noOfContainers The noOfContainers to set.
	 */
	public void setNoOfContainers(int noOfContainers)
	{
		this.noOfContainers = noOfContainers;
	}
	/**
	 * @return Returns the startNumber.
	 */
	public int getStartNumber()
	{
		return startNumber;
	}
	/**
	 * @param startNumber The startNumber to set.
	 */
	public void setStartNumber(int startNumber)
	{
		this.startNumber = startNumber;
	}
	/**
	 * @return Returns the barcode.
	 */
	public String getBarcode()
	{
		return barcode;
	}
	/**
	 * @param barcode The barcode to set.
	 */
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}
	/**
	 * @return Returns the key.
	 */
	public String getKey()
	{
		return key;
	}
	/**
	 * @param key The key to set.
	 */
	public void setKey(String key)
	{
		this.key = key;
	}
}