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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
	 * An id which refers to the type of the storage.
	 */
	private long typeId;

	/**
	 * An id which refers to Parent Container of this container.
	 */
	private long parentContainerId;
	
	/**
	 * Position label shown after selecting from storage container map viewer.
	 */
	private String positionInParentContainer;

	/**
	 * An id which refers to the site of the container if it is parent container.
	 */
	private long siteId;

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
	private String startNumber = "";

	/**
	 * No. of containers.
	 */
	private int noOfContainers = 1;

	/**
	 * Text label for dimension two.
	 */
	private String barcode;

	/**
	 * Key.
	 */
	private String key;

	/**
	 * Radio button to choose site/parentContainer.
	 */
	private int checkedButton = 1;

	/**
	 * Tells whether this container is full or not.
	 */
	private String isFull;

	/**
	 * Defines whether this Storage Container record can be queried (ACTIVE) or not queried (INACTIVE) by any actor.
	 */
	private String activityStatus;

	/**
	 * Map to handle values of all the CollectionProtocol Events
	 */
	protected Map values = new HashMap();
	
	private int positionDimensionOne;

    private int positionDimensionTwo;
    

	/**
	 * Counter that contains number of rows in the 'Add More' functionality.
	 */
	private int counter=1;
	
	
	
	/**
	 * @return Returns the counter.
	 */
	public int getCounter()
	{
		return counter;
	}
	
	/**
	 * @param counter The counter to set.
	 */
	public void setCounter(int counter)
	{
		this.counter = counter;
	}
	
    

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
		try
		{
			StorageContainer container = (StorageContainer) abstractDomain;

			this.systemIdentifier = container.getSystemIdentifier().longValue();
			this.typeId = container.getStorageType().getSystemIdentifier().longValue();
			this.parentContainerId = container.getParentContainer().getSystemIdentifier()
					.longValue();
			this.siteId = container.getSite().getSystemIdentifier().longValue();
			this.defaultTemperature = container.getTempratureInCentigrade().doubleValue();
			this.oneDimensionCapacity = container.getStorageContainerCapacity()
					.getOneDimensionCapacity().intValue();
			this.twoDimensionCapacity = container.getStorageContainerCapacity()
					.getTwoDimensionCapacity().intValue();
			this.oneDimensionLabel = container.getStorageType().getOneDimensionLabel();
			this.twoDimensionLabel = container.getStorageType().getTwoDimensionLabel();
			this.noOfContainers = container.getNoOfContainers().intValue();
			this.startNumber = String.valueOf(container.getStartNo().intValue());
		}
		catch (Exception excp)
		{
			Logger.out.error(excp.getMessage(), excp);
		}
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
	 * Returns an id which refers to the type of the storage.
	 * @return An id which refers to the type of the storage.
	 * @see #setTypeId(long)
	 */
	public long getTypeId()
	{
		return this.typeId;
	}

	/**
	 * Sets an id which refers to the type of the storage.
	 * @param typeId An id which refers to the type of the storage.
	 * @see #getTypeId()
	 */
	public void setTypeId(long typeId)
	{
		this.typeId = typeId;
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
	 * Returns an id which refers to Parent Container of this container.
	 * @return long An id which refers to Parent Container of this container.
	 * @see #setParentContainerId(long)
	 */
	public long getParentContainerId()
	{
		return parentContainerId;
	}

	/**
	 * Sets an id which refers to Parent Container of this container.
	 * @param parentContainerId An id which refers to Parent Container of this container.
	 * @see #getParentContainerId()
	 */
	public void setParentContainerId(long parentContainerId)
	{
		this.parentContainerId = parentContainerId;
	}

    /**
     * @return Returns the positionInParentContainer.
     */
    public String getPositionInParentContainer()
    {
        return positionInParentContainer;
    }
    
    /**
     * @param positionInParentContainer The positionInParentContainer to set.
     */
    public void setPositionInParentContainer(String positionInParentContainer)
    {
        this.positionInParentContainer = positionInParentContainer;
    }
    
	/**
	 * Returns an id which refers to the site of the container if it is parent container.
	 * @return long An id which refers to the site of the container if it is parent container.
	 * @see #setSiteId(long)
	 */
	public long getSiteId()
	{
		return siteId;
	}

	/**
	 * Sets an id which refers to the site of the container if it is parent container.
	 * @param siteId An id which refers to the site of the container if it is parent container.
	 * @see #getSiteId()
	 */
	public void setSiteId(long siteId)
	{
		this.siteId = siteId;
	}

	/**
	 * Returns the activity status of the storage container. 
	 * @return The activity status of storage container.
	 * @see #setActivityStatus(String)
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * Sets the activity status.
	 * @param activityStatus the activity status of the storagecontainer to be set.
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * Checks the operation to be performed is add or not.
	 * @return Returns true if operation is equal to "add", else it returns false
	 * */
	public boolean isAddOperation()
	{
		return (getOperation().equals(Constants.ADD));
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
	protected void reset()
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
				checkValidNumber(new Long(systemIdentifier).toString(),
						"storageContainer.identifier", errors, validator);
			}
			if (operation.equals(Constants.ADD) || operation.equals(Constants.EDIT))
			{
				if (this.typeId == -1)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
							ApplicationProperties.getValue("storageContainer.type")));
				}
				if (checkedButton == 1 && siteId == -1)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
							ApplicationProperties.getValue("storageContainer.site")));
				}
				else if (checkedButton == 2 && parentContainerId == 0)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
							ApplicationProperties.getValue("storageContainer.parentContainer")));
				}

				checkValidNumber(String.valueOf(noOfContainers), "storageContainer.noOfContainers",
						errors, validator);
				
				//Validations for External Identifier Add-More Block
                String className = "StorageContainerDetails:";
                String key1 = "_parameterName";
                String key2 = "_parameterValue";
                int index = 1;
                boolean isError = false;
                
                while(true)
                {
                	String keyOne = className + index + key1;
					String keyTwo = className + index + key2;
                	String value1 = (String)values.get(keyOne);
                	String value2 = (String)values.get(keyTwo);
                	
                	value1 = (value1==null ? null : value1.trim());
                	value2 = (value2==null ? null : value2.trim());
                	
                	if(value1 == null || value2 == null)
                	{
                		break;
                	}
                	else if(value1.equals("") && value2.equals(""))
                	{
                		values.remove(keyOne);
                		values.remove(keyTwo);
                	}
                	else if((!value1.equals("") && value2.equals("")) || (value1.equals("") && !value2.equals("")))
                	{
                		isError = true;
                		break;
                	}
                	index++;
                }
                
                if(isError)
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.storageContainer.containerDetails.missing",ApplicationProperties.getValue("storageContainer.msg")));
                }
                

				//            	if (validator.isEmpty(String.valueOf(noOfContainers)))
				//                {
				//                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("storageContainer.noOfContainers")));
				//                }
				//                
				//                if (validator.isEmpty(String.valueOf(startNumber)))
				//                {
				//                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("storageContainer.startNumber")));
				//                }
			}
		}
		catch (Exception excp)
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
	public String getStartNumber()
	{
		return startNumber;
	}

	/**
	 * @param startNumber The startNumber to set.
	 */
	public void setStartNumber(String startNumber)
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
		this.barcode = null;
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

	/**
	 * @return Returns the checkedButton.
	 */
	public int getCheckedButton()
	{
		return checkedButton;
	}

	/**
	 * @param checkedButton The checkedButton to set.
	 */
	public void setCheckedButton(int checkedButton)
	{
		this.checkedButton = checkedButton;
	}

	/**
	 * @return Returns the isFull.
	 */
	public String getIsFull()
	{
		return isFull;
	}

	/**
	 * @param isFull The isFull to set.
	 */
	public void setIsFull(String isFull)
	{
		this.isFull = isFull;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setValue(String key, Object value)
	{
		values.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getValue(String key)
	{
		return values.get(key);
	}

	/**
	 * @return Returns the values.
	 */
	public Collection getAllValues()
	{
		return values.values();
	}

	/**
	 * @param values
	 *            The values to set.
	 */
	public void setValues(Map values)
	{
		this.values = values;
	}

	/**
	 * @param values
	 *            The values to set.
	 */
	public Map getValues()
	{
		return this.values;
	}
    /**
     * @return Returns the positionDimensionOne.
     */
    public int getPositionDimensionOne()
    {
        return positionDimensionOne;
    }
    /**
     * @param positionDimensionOne The positionDimensionOne to set.
     */
    public void setPositionDimensionOne(int positionDimensionOne)
    {
        this.positionDimensionOne = positionDimensionOne;
    }
    /**
     * @return Returns the positionDimensionTwo.
     */
    public int getPositionDimensionTwo()
    {
        return positionDimensionTwo;
    }
    /**
     * @param positionDimensionTwo The positionDimensionTwo to set.
     */
    public void setPositionDimensionTwo(int positionDimensionTwo)
    {
        this.positionDimensionTwo = positionDimensionTwo;
    }
}