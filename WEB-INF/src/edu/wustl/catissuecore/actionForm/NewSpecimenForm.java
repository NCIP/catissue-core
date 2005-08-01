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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * NewSpecimenForm Class is used to encapsulate all the request parameters passed 
 * from New Specimen webpage.
 * @author gautam_shetty
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

    private Map externalIdentifierTypes = new HashMap();
    
    private Map externalIdentifierNames = new HashMap();

    private Map biohazards = new HashMap();
    
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

    /**
     * @return Returns the biohazards.
     */
    public Object getBiohazard(String key)
    {
        return biohazards.get(key);
    }

    /**
     * @param biohazards The biohazards to set.
     */
    public void setBiohazard(String key, Object value)
    {
        biohazards.put(key, value);
    }

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
     * @return Returns the externalIdentifierTypes.
     */
    public Object getExternalIdentifierType(String key)
    {
        return externalIdentifierTypes.get(key);
    }

    /**
     * @param externalIdentifierTypes The externalIdentifierTypes to set.
     */
    public void setExternalIdentifierType(String key, Object value)
    {
        externalIdentifierTypes.put(key, value);
    }

    /**
     * @return Returns the externalIdentifierTypes.
     */
    public Object getExternalIdentifierName(String key)
    {
        return externalIdentifierNames.get(key);
    }

    /**
     * @param externalIdentifierTypes The externalIdentifierTypes to set.
     */
    public void setExternalIdentifierName(String key, Object value)
    {
        externalIdentifierNames.put(key, value);
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
        this.externalIdentifierTypes = new HashMap();
        this.externalIdentifierNames = new HashMap();
        this.biohazards = new HashMap();
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
            this.className			= specimen.getType();
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage(),excp);
        }
    }
}