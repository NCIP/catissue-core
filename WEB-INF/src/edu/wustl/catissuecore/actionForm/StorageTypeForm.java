/**
 * <p>Title: StorageTypeForm Class>
 * <p>Description:  This Class is used to encapsulate all the request parameters passed 
 * from StorageType.jsp page. </p>
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
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to encapsulate all the request parameters passed from StorageType.jsp page.
 * @author aniruddha_phadnis
 * */
public class StorageTypeForm extends AbstractActionForm
{
    
    /**
     * A string containing the type of the storage.
     */
    private String type = null;

    /**
     * A default temperature of the storage container.
     */
    private String defaultTemperature;
    
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
    private String oneDimensionLabel = null;

    /**
     * Text label for dimension two.
     */
    private String twoDimensionLabel = null;
    
    /**
     * No argument constructor for StorageTypeForm class 
     */
    public StorageTypeForm()
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
            StorageType storageType = (StorageType) abstractDomain;
            this.systemIdentifier = storageType.getSystemIdentifier().longValue();
            this.type = storageType.getType();
            this.defaultTemperature = Utility.toString( storageType.getDefaultTempratureInCentigrade());
            this.oneDimensionCapacity = storageType.getDefaultStorageCapacity().getOneDimensionCapacity().intValue();
            this.twoDimensionCapacity = storageType.getDefaultStorageCapacity().getTwoDimensionCapacity().intValue();
            this.oneDimensionLabel = storageType.getOneDimensionLabel();
            this.twoDimensionLabel = storageType.getTwoDimensionLabel();
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage(),excp);
        }
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
     * Sets the type of the storage.
     * @param type String type of the storage to be set.
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
    public String getDefaultTemperature()
    {
        return this.defaultTemperature;
    }

    /**
     * Sets the default temperature of the storage container
     * @param defaultTemperature the default temperature of the storage container to be set.
     * @see #getDefaultTemperature()
     */
    public void setDefaultTemperature(String defaultTemperature)
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
     * Returns the id assigned to form bean
     */
    public int getFormId()
    {
        return Constants.STORAGE_TYPE_FORM_ID;
    }
    
  
    
    /**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.  
     * */
    protected void reset()
    {
        this.oneDimensionLabel		= null;
        this.twoDimensionLabel		= null;
        this.type					= null;
        this.defaultTemperature = null; 
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
        	setRedirectValue(validator);
            if (validator.isEmpty(type))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("storageType.type")));
            }  
            else
            {
                String s= new String("- _");
                String delimitedString = validator.delimiterExcludingGiven(s );
                if (validator.containsSpecialCharacters(type,delimitedString  ))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("storageType.type")));
                }  
            	
            }
            if (validator.isEmpty(String.valueOf(oneDimensionCapacity)))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("storageType.oneDimensionCapacity")));
            }
            else
            {
            	if(!validator.isNumeric(String.valueOf(oneDimensionCapacity)))
            	{
            		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("storageType.oneDimensionCapacity")));
            	}
            }
            if (validator.isEmpty(String.valueOf(twoDimensionCapacity)))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("storageType.twoDimensionCapacity")));
            }
            else
            {
            	if(!validator.isNumeric(String.valueOf(twoDimensionCapacity)))
            	{
            		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("storageType.twoDimensionCapacity")));
            	}
            }
            
            if (validator.isEmpty(oneDimensionLabel))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("storageType.oneDimensionLabel")));
            }
            if (validator.isEmpty(twoDimensionLabel))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("storageType.twoDimensionLabel")));
            }
            // code as per bug id 233 
            // checking for double value if present
            if (!validator.isEmpty(defaultTemperature) && !validator.isDouble(defaultTemperature,0  )  )
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("storageType.defaultTemperature")));
            }
        }
        catch(Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
        return errors;
     }
}