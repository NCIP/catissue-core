/**
 * <p>Title: NewSpecimenForm Class>
 * <p>Description:  NewSpecimenForm Class is used to encapsulate all the request parameters passed 
 * from New Specimen webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
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
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * NewSpecimenForm Class is used to encapsulate all the request parameters passed 
 * from New Specimen webpage.
 * @author aniruddha_phadnis
 */
public class NewSpecimenForm extends AbstractActionForm
{
    private long specimenCollectionGroupId;
    
    /**
     * systemIdentifier is a unique id assigned to each User.
     * */
    private long systemIdentifier;
    
    /**
     * A String containing the operation(Add/Edit) to be performed.
     * */
    private String operation = null;

    /**
     * Activity Status
     */
    private String activityStatus;
    
    /**
     * Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
     */
    private String className = "";

    private String type = "";

    /**
     * Anatomic site from which the specimen was derived.
     */
    private String tissueSite = "";

    /**
     * For bilateral sites, left or right.
     */
    private String tissueSide = "";

    /**
     * Histopathological character of the specimen 
     * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
     */
    private String pathologicalStatus = "";

    /**
     * Concentration of specimen.
     */
    private String concentration;

    /**
     * Amount of Specimen.
     */
    private String quantity;

    /**
     * A physically discreet container that is used to store a specimen.
     * e.g. Box, Freezer etc
     */
    private String storageContainer = "";

    /**
     * Reference to dimensional position one of the specimen in Storage Container.
     */
    private String positionDimensionOne;

    /**
     * Reference to dimensional position two of the specimen in Storage Container.
     */
    private String positionDimensionTwo;

    /**
     * Comments on specimen.
     */
    private String comments = "";
    
    /**
     * Type of the biohazard.
     */
    private String biohazardType;
    
    /**
     * Name of the biohazard.
     */
    private String biohazardName;

    /**
     * Number of external identifier rows.
     */
    private int exIdCounter=1;
    
    /**
     * Number of biohazard rows.
     */
    private int bhCounter=1;
    
    private Map externalIdentifier = new HashMap();

    private Map biohazard = new HashMap();
    
    /**
     * Returns the systemIdentifier assigned to User.
     * @return int representing the id assigned to User.
     * @see #setIdentifier(int)
     * */
    public long getSystemIdentifier()
    {
        return (this.systemIdentifier);
    }

    /**
     * Sets an id for the User.
     * @param systemIdentifier id to be assigned to the User.
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
	 * Returns the activity status
	 * @return the activityStatus.
	 * @see #setActivityStatus(String)
	 */
    public String getActivityStatus()
	{
		return activityStatus;
	}
	/**
	 * Sets the activity status.
	 * @param activityStatus activity status.
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

//    /**
//     * @return Returns the biohazards.
//     */
//    public Object getBiohazard(String key)
//    {
//        return biohazards.get(key);
//    }
//
//    /**
//     * @param biohazards The biohazards to set.
//     */
//    public void setBiohazard(String key, Object value)
//    {
//        biohazards.put(key, value);
//    }

    /**
     * @return Returns the concentration.
     */
    public String getConcentration()
    {
        return concentration;
    }

    /**
     * @param concentration The concentration to set.
     */
    public void setConcentration(String concentration)
    {
        this.concentration = concentration;
    }
    
    /**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setExternalIdentifierValue(String key, Object value)
	{
		externalIdentifier.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getExternalIdentifierValue(String key)
	{
		return externalIdentifier.get(key);
	}

	/**
	 * @return Returns the values.
	 */
	public Collection getAllExternalIdentifiers()
	{
		return externalIdentifier.values();
	}

	/**
	 * @param values
	 * The values to set.
	 */
	public void setExternalIdentifier(Map externalIdentifier)
	{
		this.externalIdentifier = externalIdentifier;
	}

	/**
	 * @param values
	 * Returns the map.
	 */
	public Map getExternalIdentifier()
	{
		return this.externalIdentifier;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setBiohazardValue(String key, Object value)
	{
		biohazard.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getBiohazardValue(String key)
	{
		return biohazard.get(key);
	}

	/**
	 * @return Returns the values.
	 */
	public Collection getAllBiohazards()
	{
		return biohazard.values();
	}

	/**
	 * @param values
	 * The values to set.
	 */
	public void setBiohazard(Map biohazard)
	{
		this.biohazard = biohazard;
	}

	/**
	 * @param values
	 * Returns the map.
	 */
	public Map getBiohazard()
	{
		return this.biohazard;
	}	
    
    /**
     * @return Returns the comments.
     */
    public String getComments()
    {
        return comments;
    }

    /**
     * @param comments The comments to set.
     */
    public void setComments(String notes)
    {
        this.comments = notes;
    }

    /**
     * @return Returns the pathologicalStatus.
     */
    public String getPathologicalStatus()
    {
        return pathologicalStatus;
    }

    /**
     * @param pathologicalStatus The pathologicalStatus to set.
     */
    public void setPathologicalStatus(String pathologicalStatus)
    {
        this.pathologicalStatus = pathologicalStatus;
    }

    /**
     * @return Returns the positionDimensionOne.
     */
    public String getPositionDimensionOne()
    {
        return positionDimensionOne;
    }

    /**
     * @param positionDimensionOne The positionDimensionOne to set.
     */
    public void setPositionDimensionOne(String positionDimensionOne)
    {
        this.positionDimensionOne = positionDimensionOne;
    }

    /**
     * @return Returns the positionDimensionTwo.
     */
    public String getPositionDimensionTwo()
    {
        return positionDimensionTwo;
    }

    /**
     * @param positionDimensionTwo The positionDimensionTwo to set.
     */
    public void setPositionDimensionTwo(String positionDimensionTwo)
    {
        this.positionDimensionTwo = positionDimensionTwo;
    }

    /**
     * @return Returns the quantity.
     */
    public String getQuantity()
    {
        return quantity;
    }

    /**
     * @param quantity The quantity to set.
     */
    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }

    /**
     * @return Returns the specimenCollectionGroupId.
     */
    public long getSpecimenCollectionGroupId()
    {
        return specimenCollectionGroupId;
    }

    /**
     * @param specimenCollectionGroupId The specimenCollectionGroupId to set.
     */
    public void setSpecimenCollectionGroupId(long specimenCollectionGroupId)
    {
        this.specimenCollectionGroupId = specimenCollectionGroupId;
    }

    /**
     * @return Returns the storageContainer.
     */
    public String getStorageContainer()
    {
        return storageContainer;
    }

    /**
     * @param storageContainer The storageContainer to set.
     */
    public void setStorageContainer(String storageContainer)
    {
        this.storageContainer = storageContainer;
    }

    /**
     * @return Returns the subType.
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param subType The subType to set.
     */
    public void setType(String subType)
    {
        this.type = subType;
    }

    /**
     * @return Returns the tissueSide.
     */
    public String getTissueSide()
    {
        return tissueSide;
    }

    /**
     * @param tissueSide The tissueSide to set.
     */
    public void setTissueSide(String tissueSide)
    {
        this.tissueSide = tissueSide;
    }

    /**
     * @return Returns the tissueSite.
     */
    public String getTissueSite()
    {
        return tissueSite;
    }

    /**
     * @param tissueSite The tissueSite to set.
     */
    public void setTissueSite(String tissueSite)
    {
        this.tissueSite = tissueSite;
    }

    /**
     * @return Returns the type.
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * @param type The type to set.
     */
    public void setClassName(String type)
    {
        this.className = type;
    }
    
    private void reset()
    {
        this.className = null;
        this.type = null;
        this.tissueSite = null;
        this.tissueSide = null;
        this.pathologicalStatus = null;
        this.storageContainer = null;
        this.comments = null;
        this.externalIdentifier = new HashMap();
//        this.externalIdentifierNames = new HashMap();
//        this.biohazards = new HashMap();
    }
    
    /**
     * Resets all fields.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        reset();
    }
    
    /**
     * Checks the operation to be performed is add operation.
     * @return Returns true if operation is equal to "add", else returns false.
     * */
    public boolean isAddOperation()
    {
        return(getOperation().equals(Constants.ADD));
    }
    
    /**
     * Returns the id assigned to form bean.
     */
    public int getFormId()
    {
        return Constants.SITE_FORM_ID;
    }
    
    /**
     * This function Copies the data from an site object to a SiteForm object.
     * @param site An object containing the information about site.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        try
        {
            Specimen specimen = (Specimen) abstractDomain;
            
            this.systemIdentifier= specimen.getSystemIdentifier().longValue();
            this.className = specimen.getType();
            
            SpecimenCharacteristics characteristic = specimen.getSpecimenCharacteristics();
            this.pathologicalStatus = characteristic.getPathologicalStatus();
            this.tissueSide = characteristic.getTissueSide();
            this.tissueSite = characteristic.getTissueSite();
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage(),excp);
        }
    }
    
	/**
	 * @return Returns the biohazardType.
	 */
	public String getBiohazardType()
	{
		return biohazardType;
	}
	/**
	 * @param biohazardType The biohazardType to set.
	 */
	public void setBiohazardType(String biohazardType)
	{
		this.biohazardType = biohazardType;
	}
	
	/**
	 * @return Returns the biohazardName.
	 */
	public String getBiohazardName()
	{
		return biohazardName;
	}
	/**
	 * @param biohazardName The biohazardName to set.
	 */
	public void setBiohazardName(String biohazardName)
	{
		this.biohazardName = biohazardName;
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
                 checkValidNumber(new Long(systemIdentifier).toString(),"site.identifier",errors,validator);
             }
             if (operation.equals(Constants.ADD) || operation.equals(Constants.EDIT))
             {             
             	if (tissueSite.equals(Constants.SELECT_OPTION))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.tissueSite")));
                }
             	
             	if (tissueSide.equals(Constants.SELECT_OPTION))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.tissueSide")));
                }
             	
             	if (pathologicalStatus.equals(Constants.SELECT_OPTION))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.pathologicalStatus")));
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
	 * @return Returns the bhCounter.
	 */
	public int getBhCounter()
	{
		return bhCounter;
	}
	
	/**
	 * @param bhCounter The bhCounter to set.
	 */
	public void setBhCounter(int bhCounter)
	{
		this.bhCounter = bhCounter;
	}
	
	/**
	 * @return Returns the exIdCounter.
	 */
	public int getExIdCounter()
	{
		return exIdCounter;
	}
	
	/**
	 * @param exIdCounter The exIdCounter to set.
	 */
	public void setExIdCounter(int exIdCounter)
	{
		this.exIdCounter = exIdCounter;
	}
}