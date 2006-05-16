/**
 * <p>Title: SpecimenForm Class>
 * <p>Description:  SpecimenForm Class is used to encapsulate all the request parameters passed 
 * from New/Create Specimen webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * SpecimenForm Class is used to encapsulate all the request parameters passed 
 * from New/Create Specimen webpage.
 * @author aniruddha_phadnis
 */
public class SpecimenForm extends AbstractActionForm
{

    /**
     * Type of specimen. e.g. Tissue, Molecular, Cell, Fluid
     */
    protected String className = "";

    /**
     * Sub Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
     */
    protected String type;

    /**
     * Concentration of specimen.
     */
    protected String concentration;

    /**
     * Amount of Specimen.
     */
    protected String quantity;
    
    /**
     * Available Amount of Specimen.
     */
    protected String availableQuantity;
    
    /**
     * Is quantity available?
     */
    protected boolean available;

    /**
     * Unit of specimen.
     */
    protected String unit;

    /**
     * A physically discreet container that is used to store a specimen.
     * e.g. Box, Freezer etc
     */
    protected String storageContainer = "";

    /**
     * Reference to dimensional position one of the specimen in Storage Container.
     */
    protected String positionDimensionOne;

    /**
     * Reference to dimensional position two of the specimen in Storage Container.
     */
    protected String positionDimensionTwo;
    
    /**
     * Barcode assigned to the specimen.
     */
    protected String barcode;

    /**
     * Comments on specimen.
     */
    protected String comments = "";

    /**
     * Number of external identifier rows.
     */
    protected int exIdCounter = 1;
    
    /**
     * Parent Container Identifier.
     */
    private String parentContainerId;

    protected String positionInStorageContainer;

    /**
	 * Map to handle all the data of external identifiers.
	 */
    protected Map externalIdentifier = new HashMap();

    /**
     * Returns the concentration. 
     * @return String the concentration.
     * @see #setConcentration(String)
     */
    public String getConcentration()
    {
        return concentration;
    }

    /**
     * Sets the concentration.
     * @param concentration The concentration.
     * @see #getConcentration()
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
    	if (isMutable())
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
     * Returns all the values in the map.
     * @return Collection all the values in the map.
     */
    public Collection getAllExternalIdentifiers()
    {
        return externalIdentifier.values();
    }

    /**
     * Sets the map.
     * @param externalIdentifier the map to be set.
     * @see #getExternalIdentifier()
     */
    public void setExternalIdentifier(Map externalIdentifier)
    {
        this.externalIdentifier = externalIdentifier;
    }

    /**
     * Returns the map of external identifiers. 
     * @return Map the map of external identifiers.
     * @see #setExternalIdentifier(Map)
     */
    public Map getExternalIdentifier()
    {
        return this.externalIdentifier;
    }

    /**
     * Returns the comments. 
     * @return String the comments.
     * @see #setComments(String)
     */
    public String getComments()
    {
        return comments;
    }

    /**
     * Sets the comments.
     * @param comments The comments.
     * @see #getComments()
     */
    public void setComments(String comments)
    {
        this.comments = comments;
    }

    /**
     * Returns the position dimension one. 
     * @return String the position dimension one.
     * @see #setPositionDimensionOne(String)
     */
    public String getPositionDimensionOne()
    {
        return positionDimensionOne;
    }

    /**
     * Sets the position dimension one.
     * @param positionDimensionOne The position dimension one.
     * @see #getPositionDimensionOne()
     */
    public void setPositionDimensionOne(String positionDimensionOne)
    {
        this.positionDimensionOne = positionDimensionOne;
    }

    /**
     * Returns the position dimension two. 
     * @return String the position dimension two.
     * @see #setPositionDimensionTwo(String)
     */
    public String getPositionDimensionTwo()
    {
        return positionDimensionTwo;
    }

    /**
     * Sets the position dimension two.
     * @param positionDimensionTwo The position dimension two.
     * @see #getPositionDimensionTwo()
     */
    public void setPositionDimensionTwo(String positionDimensionTwo)
    {
        this.positionDimensionTwo = positionDimensionTwo;
    }

    /**
     * Returns the barcode of this specimen. 
     * @return String the barcode of this specimen.
     * @see #setBarcode(String)
     */
    public String getBarcode()
    {
        return barcode;
    }
    
    /**
     * Sets the barcode of this specimen.
     * @param barcode The barcode of this specimen.
     * @see #getBarcode()
     */
    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }
    
    /**
     * Returns the quantity. 
     * @return String the quantity.
     * @see #setQuantity(String)
     */
    public String getQuantity()
    {
        return quantity;
    }

    /**
     * Sets the quantity.
     * @param quantity The quantity.
     * @see #getQuantity()
     */
    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }
    
    /**
     * Returns the available quantity. 
     * @return String the available quantity.
     * @see #setAvailableQuantity(String)
     */
    public String getAvailableQuantity()
    {
        return availableQuantity;
    }

    /**
     * Sets the available quantity.
     * @param availableQuantity The available quantity.
     * @see #getAvailableQuantity()
     */
    public void setAvailableQuantity(String availableQuantity)
    {
    	if (isMutable())
    	{
    		this.availableQuantity = availableQuantity;
    	}
        
    }

    /**
     * Returns the unit of this specimen. 
     * @return String the unit of this specimen.
     * @see #setUnit(String)
     */
    public String getUnit()
    {
        return unit;
    }

    /**
     * Sets the unit of this specimen.
     * @param unit The unit of this specimen.
     * @see #getUnit()
     */
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    /**
     * Returns the storage container of this specimen. 
     * @return String the storage container of this specimen.
     * @see #setStorageContainer(String)
     */
    public String getStorageContainer()
    {
        return storageContainer;
    }

    /**
     * Sets the storage container of this specimen.
     * @param storageContainer The storage container of this specimen.
     * @see #getStorageContainer()
     */
    public void setStorageContainer(String storageContainer)
    {
        this.storageContainer = storageContainer;
    }

    /**
     * Returns the subtype of this specimen. 
     * @return String the subtype of this specimen.
     * @see #setType(String)
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the subtype of this specimen.
     * @param subType The subtype of this specimen.
     * @see #getType()
     */
    public void setType(String subType)
    {
        this.type = subType;
    }

    /**
     * Returns the className of this specimen. 
     * @return String the className of this specimen.
     * @see #setClassName(String)
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * Sets the className of this specimen.
     * @param className The className of this specimen.
     * @see #getClassName()
     */
    public void setClassName(String className)
    {
        this.className = className;
    }

    protected void reset()
    {
        this.className = null;
        this.type = null;
        this.storageContainer = null;
        this.comments = null;
        this.externalIdentifier = new HashMap();
    }

    /**
     * Returns the id assigned to form bean.
     */
    public int getFormId()
    {
        return -1;
    }

    /**
     * This function Copies the data from an Specimen object to a SpecimenForm object.
     * @param site An object containing the information about site.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        Specimen specimen = (Specimen) abstractDomain;

        this.systemIdentifier = specimen.getId().longValue();
        this.type = specimen.getType();
        this.concentration = "";
        this.comments = specimen.getComments();
        this.activityStatus = specimen.getActivityStatus();
        
        if(specimen.getAvailable()!=null)
        	this.available = specimen.getAvailable().booleanValue();

        StorageContainer container = specimen.getStorageContainer();

        if (container != null)
        {
            this.storageContainer = String.valueOf(container
                    .getSystemIdentifier());
            this.positionDimensionOne = String.valueOf(specimen
                    .getPositionDimensionOne());
            this.positionDimensionTwo = String.valueOf(specimen
                    .getPositionDimensionTwo());
            
            this.positionInStorageContainer = container.getStorageType().getType() + " : " 
			+ container.getSystemIdentifier() + " Pos(" + this.positionDimensionOne + ","
			+ this.positionDimensionTwo + ")";
            
        }
        
        this.barcode = specimen.getBarcode();
        
        if (specimen instanceof CellSpecimen)
        {
            this.className = "Cell";
            this.quantity = String.valueOf(((CellSpecimen) specimen)
                    .getQuantityInCellCount());
            this.availableQuantity = String.valueOf(((CellSpecimen) specimen).getAvailableQuantityInCellCount());
        }
        else if (specimen instanceof FluidSpecimen)
        {
            this.className = "Fluid";
            this.quantity = String.valueOf(((FluidSpecimen) specimen)
                    .getQuantityInMilliliter());
            this.availableQuantity = String.valueOf(((FluidSpecimen) specimen).getAvailableQuantityInMilliliter());
        }
        else if (specimen instanceof MolecularSpecimen)
        {
            this.className = "Molecular";
            this.quantity = String.valueOf(((MolecularSpecimen) specimen)
                    .getQuantityInMicrogram());
            this.availableQuantity = String.valueOf(((MolecularSpecimen) specimen).getAvailableQuantityInMicrogram());
            if (((MolecularSpecimen) specimen)
                    .getConcentrationInMicrogramPerMicroliter() != null)
                this.concentration = String
                        .valueOf(((MolecularSpecimen) specimen)
                                .getConcentrationInMicrogramPerMicroliter());
        }
        else if (specimen instanceof TissueSpecimen)
        {
            this.className = "Tissue";
            this.quantity = String.valueOf(((TissueSpecimen) specimen)
                    .getQuantityInGram());
            this.availableQuantity = String.valueOf(((TissueSpecimen) specimen).getAvailableQuantityInGram());
        }
                    
        SpecimenCharacteristics characteristic = specimen
                .getSpecimenCharacteristics();

        Collection externalIdentifierCollection = specimen
                .getExternalIdentifierCollection();
        exIdCounter = 1;

        if (externalIdentifierCollection != null && externalIdentifierCollection.size() != 0)
        {
            externalIdentifier = new HashMap();

            int i = 1;

            Iterator it = externalIdentifierCollection.iterator();

            while (it.hasNext())
            {
                String key1 = "ExternalIdentifier:" + i + "_name";
                String key2 = "ExternalIdentifier:" + i + "_value";
                String key3 = "ExternalIdentifier:" + i
                        + "_systemIdentifier";

                ExternalIdentifier externalId = (ExternalIdentifier) it
                        .next();

                externalIdentifier.put(key1, externalId.getName());
                externalIdentifier.put(key2, externalId.getValue());
                externalIdentifier.put(key3, String.valueOf(externalId
                        .getId()));

                i++;
            }

            exIdCounter = externalIdentifierCollection.size();
        }
    }
    
    public void setAllVal(Object obj)
    {
        edu.wustl.catissuecore.domainobject.Specimen specimen = (edu.wustl.catissuecore.domainobject.Specimen) obj;

        this.systemIdentifier = specimen.getId().longValue();
        this.type = specimen.getType();
        this.concentration = "";
        this.comments = specimen.getComments();
        this.activityStatus = specimen.getActivityStatus();
        
        if(specimen.getAvailable()!=null)
        	this.available = specimen.getAvailable().booleanValue();

        edu.wustl.catissuecore.domainobject.StorageContainer container = specimen.getStorageContainer();

        if (container != null)
        {
            this.storageContainer = String.valueOf(container
                    .getId());
            this.positionDimensionOne = String.valueOf(specimen
                    .getPositionDimensionOne());
            this.positionDimensionTwo = String.valueOf(specimen
                    .getPositionDimensionTwo());
            
            this.positionInStorageContainer = container.getStorageType().getType() + " : " 
			+ container.getId() + " Pos(" + this.positionDimensionOne + ","
			+ this.positionDimensionTwo + ")";
            
        }
        
        this.barcode = specimen.getBarcode();
        
        if (specimen instanceof edu.wustl.catissuecore.domainobject.CellSpecimen)
        {
            this.className = "Cell";
            this.quantity = String.valueOf(((edu.wustl.catissuecore.domainobject.CellSpecimen) specimen)
                    .getQuantityInCellCount());
            this.availableQuantity = String.valueOf(((edu.wustl.catissuecore.domainobject.CellSpecimen) specimen).getAvailableQuantityInCellCount());
        }
        else if (specimen instanceof edu.wustl.catissuecore.domainobject.FluidSpecimen)
        {
            this.className = "Fluid";
            this.quantity = String.valueOf(((edu.wustl.catissuecore.domainobject.FluidSpecimen) specimen)
                    .getQuantityInMilliliter());
            this.availableQuantity = String.valueOf(((edu.wustl.catissuecore.domainobject.FluidSpecimen) specimen).getAvailableQuantityInMilliliter());
        }
        else if (specimen instanceof edu.wustl.catissuecore.domainobject.MolecularSpecimen)
        {
            this.className = "Molecular";
            this.quantity = String.valueOf(((edu.wustl.catissuecore.domainobject.MolecularSpecimen) specimen)
                    .getQuantityInMicrogram());
            this.availableQuantity = String.valueOf(((edu.wustl.catissuecore.domainobject.MolecularSpecimen) specimen).getAvailableQuantityInMicrogram());
            if (((edu.wustl.catissuecore.domainobject.MolecularSpecimen) specimen)
                    .getConcentrationInMicrogramPerMicroliter() != null)
                this.concentration = String
                        .valueOf(((edu.wustl.catissuecore.domainobject.MolecularSpecimen) specimen)
                                .getConcentrationInMicrogramPerMicroliter());
        }
        else if (specimen instanceof edu.wustl.catissuecore.domainobject.TissueSpecimen)
        {
            this.className = "Tissue";
            this.quantity = String.valueOf(((edu.wustl.catissuecore.domainobject.TissueSpecimen) specimen)
                    .getQuantityInGram());
            this.availableQuantity = String.valueOf(((edu.wustl.catissuecore.domainobject.TissueSpecimen) specimen).getAvailableQuantityInGram());
        }
                    
        edu.wustl.catissuecore.domainobject.SpecimenCharacteristics characteristic = specimen
                .getSpecimenCharacteristics();

        Collection externalIdentifierCollection = specimen
                .getExternalIdentifierCollection();
        exIdCounter = 1;

        if (externalIdentifierCollection != null && externalIdentifierCollection.size() != 0)
        {
            externalIdentifier = new HashMap();

            int i = 1;

            Iterator it = externalIdentifierCollection.iterator();

            while (it.hasNext())
            {
                String key1 = "ExternalIdentifier:" + i + "_name";
                String key2 = "ExternalIdentifier:" + i + "_value";
                String key3 = "ExternalIdentifier:" + i
                        + "_systemIdentifier";

                edu.wustl.catissuecore.domainobject.ExternalIdentifier externalId = 
                		(edu.wustl.catissuecore.domainobject.ExternalIdentifier) it.next();

                if(externalId != null)
                {
	                externalIdentifier.put(key1, Utility.toString(externalId.getName()));
	                externalIdentifier.put(key2, Utility.toString(externalId.getValue()));
	                externalIdentifier.put(key3, Utility.toString(externalId.getId()));
                }

                i++;
            }
            
            exIdCounter = externalIdentifierCollection.size();
        }

    }

    /**
     * Overrides the validate method of ActionForm.
     * */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();

        try
        {
            if (operation.equals(Constants.ADD)
                    || operation.equals(Constants.EDIT))
            {
                if (!validator.isValidOption(className))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.required", ApplicationProperties
                                    .getValue("specimen.type")));
                }

                if (!className.equals("Cell") && !validator.isValidOption(type))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.required", ApplicationProperties
                                    .getValue("specimen.subType")));
                }

                if (validator.isEmpty(quantity))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.required", ApplicationProperties
                                    .getValue("specimen.quantity")));
                }
                else if (!validator.isDouble(quantity))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.format", ApplicationProperties
                                    .getValue("specimen.quantity")));
                }

                if(validator.isEmpty(positionDimensionOne) || validator.isEmpty(positionDimensionTwo) || validator.isEmpty(storageContainer ))
                {
	                  errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
	                  "errors.item.required", ApplicationProperties
	                          .getValue("specimen.positionInStorageContainer")));
                }
                else
                {
                    if(!validator.isNumeric(positionDimensionOne,1) || !validator.isNumeric(positionDimensionTwo,1) || !validator.isNumeric(storageContainer,1))
                    {
    	                  errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
    	                  "errors.item.format", ApplicationProperties
    	                          .getValue("specimen.positionInStorageContainer")));
                    }
                	
                }

                //Validations for External Identifier Add-More Block
                String className = "ExternalIdentifier:";
                String key1 = "_name";
                String key2 = "_value";
                String key3 = "_" + Constants.SYSTEM_IDENTIFIER;
                
                int index = 1;

                while (true)
                {
                    String keyOne = className + index + key1;
                    String keyTwo = className + index + key2;
                    String keyThree = className + index + key3;
                    
                    String value1 = (String) externalIdentifier.get(keyOne);
                    String value2 = (String) externalIdentifier.get(keyTwo);

                    if (value1 == null || value2 == null)
                    {
                        break;
                    }
                    else if (value1.trim().equals("") && value2.trim().equals(""))
                    {
                        externalIdentifier.remove(keyOne);
                        externalIdentifier.remove(keyTwo);
                        externalIdentifier.remove(keyThree);
                    }
                    else if ((!value1.trim().equals("") && value2.trim().equals(""))
                            || (value1.trim().equals("") && !value2.trim().equals("")))
                    {
                    	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.specimen.externalIdentifier.missing",
                                ApplicationProperties.getValue("specimen.msg")));
                        break;
                    }
                    index++;
                }
            }
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
        return errors;
    }

    /**
     * Returns the counter that holds no. of external identifier rows. 
     * @return The counter that holds no. of external identifier rows.
     * @see #setExIdCounter(int)
     */
    public int getExIdCounter()
    {
        return exIdCounter;
    }

    /**
     * Sets the counter that holds no. of external identifier rows.
     * @param exIdCounter The counter that holds no. of external identifier rows.
     * @see #getExIdCounter()
     */
    public void setExIdCounter(int exIdCounter)
    {
        this.exIdCounter = exIdCounter;
    }

    /**
     * Returns the position in storage container. 
     * @return The position in storage container.
     * @see #setPositionInStorageContainer(String)
     */
    public String getPositionInStorageContainer()
    {
        return positionInStorageContainer;
    }

    /**
     * Sets the position in storage container.
     * @param positionInStorageContainer The position in storage container.
     * @see #getPositionInStorageContainer()
     */
    public void setPositionInStorageContainer(String positionInStorageContainer)
    {
        this.positionInStorageContainer = positionInStorageContainer;
    }
    
    /**
     * Returns True/False, whether the quatity is available or not. 
     * @return True/False, whether the quatity is available or not.
     * @see #setAvailable(boolean)
     */
	public boolean isAvailable()
	{
		return available;
	}
	
	/**
     * Sets True/False depending upon the availability of the quantity.
     * @param available True/False depending upon the availability of the quantity.
     * @see #getAvailable()
     */
	public void setAvailable(boolean available)
	{
		this.available = available;
	}
	
	/**
     * Returns the parent container id. 
     * @return The parent container id.
     * @see #setParentContainerId(String)
     */
	public String getParentContainerId()
	{
		return parentContainerId;
	}
	
	/**
     * Sets the parent container id.
     * @param parentContainerId The parent container id.
     * @see #getParentContainerId()
     */
	public void setParentContainerId(String parentContainerId)
	{
		this.parentContainerId = parentContainerId;
	}
	
}