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

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * CreateSpecimenForm Class is used to encapsulate all the request parameters passed 
 * from New/Create Specimen webpage.
 * @author aniruddha_phadnis
 */
public class SpecimenForm extends AbstractActionForm
{

    /**
     * systemIdentifier is a unique id assigned to each User.
     * */
    protected long systemIdentifier;

    /**
     * A String containing the operation(Add/Edit) to be performed.
     * */
    protected String operation = null;

    /**
     * Activity Status
     */
    protected String activityStatus = Constants.ACTIVITY_STATUS_ACTIVE;

    /**
     * Type of specimen. e.g. Tissue, Molecular, Cell, Fluid
     */
    protected String className = "";

    /**
     * Sub Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
     */
    protected String type = "";

    /**
     * Concentration of specimen.
     */
    protected String concentration;

    /**
     * Amount of Specimen.
     */
    protected String quantity;

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
     * Comments on specimen.
     */
    protected String comments = "";

    /**
     * Number of external identifier rows.
     */
    protected int exIdCounter = 1;

    protected String positionInStorageContainer;

    protected Map externalIdentifier = new HashMap();

    /**
     * Returns the systemIdentifier assigned to User.
     * @return int representing the id assigned to User.
     * @see #setIdentifier(long)
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
     * @return Returns the quantity.
     */
    public String getUnit()
    {
        return unit;
    }

    /**
     * @param unit The quantity to set.
     */
    public void setUnit(String unit)
    {
        this.unit = unit;
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

    protected void reset()
    {
        this.className = null;
        this.type = null;
        this.storageContainer = null;
        this.comments = null;
        this.externalIdentifier = new HashMap();
    }

    /**
     * Checks the operation to be performed is add operation.
     * @return Returns true if operation is equal to "add", else returns false.
     * */
    public boolean isAddOperation()
    {
        return (getOperation().equals(Constants.ADD));
    }

    /**
     * Returns the id assigned to form bean.
     */
    public int getFormId()
    {
        return -1;
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

            this.systemIdentifier = specimen.getSystemIdentifier().longValue();
            this.type = specimen.getType();
            this.concentration = "";
            this.comments = specimen.getComments();

            StorageContainer container = specimen.getStorageContainer();

            if (container != null)
            {
                this.storageContainer = String.valueOf(container
                        .getSystemIdentifier());
                this.positionDimensionOne = String.valueOf(specimen
                        .getPositionDimensionOne());
                this.positionDimensionTwo = String.valueOf(specimen
                        .getPositionDimensionTwo());
            }

            if (specimen instanceof CellSpecimen)
            {
                this.className = "Cell";
                this.quantity = String.valueOf(((CellSpecimen) specimen)
                        .getQuantityInCellCount());
            }
            else if (specimen instanceof FluidSpecimen)
            {
                this.className = "Fluid";
                this.quantity = String.valueOf(((FluidSpecimen) specimen)
                        .getQuantityInMilliliter());
            }
            else if (specimen instanceof MolecularSpecimen)
            {
                this.className = "Molecular";
                this.quantity = String.valueOf(((MolecularSpecimen) specimen)
                        .getQuantityInMicrogram());
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
            }

            SpecimenCharacteristics characteristic = specimen
                    .getSpecimenCharacteristics();

            Collection externalIdentifierCollection = specimen
                    .getExternalIdentifierCollection();
            exIdCounter = 1;

            if (externalIdentifierCollection != null)
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
                            .getSystemIdentifier()));

                    i++;
                }

                exIdCounter = externalIdentifierCollection.size();
            }
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage(), excp);
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
                if (className.equals(Constants.SELECT_OPTION))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.required", ApplicationProperties
                                    .getValue("specimen.type")));
                }

                if (type.equals(Constants.SELECT_OPTION))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.required", ApplicationProperties
                                    .getValue("specimen.subType")));
                }

                if (className.equals("Molecular"))
                {
                    if (!validator.isDouble(concentration))
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.format", ApplicationProperties
                                        .getValue("specimen.concentration")));
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

                if (storageContainer.equals(""))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.required", ApplicationProperties
                                    .getValue("specimen.storageContainer")));
                }

                if (positionDimensionOne.equals(""))
                {
                    errors
                            .add(
                                    ActionErrors.GLOBAL_ERROR,
                                    new ActionError(
                                            "errors.item.required",
                                            ApplicationProperties
                                                    .getValue("specimen.positionDimensionOne")));
                }
                
                if (validator.isEmpty(positionInStorageContainer))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.required", ApplicationProperties
                                    .getValue("specimen.positionInStorageContainer")));
                }

                if (positionDimensionTwo.equals(""))
                {
                    errors
                            .add(
                                    ActionErrors.GLOBAL_ERROR,
                                    new ActionError(
                                            "errors.item.required",
                                            ApplicationProperties
                                                    .getValue("specimen.positionDimensionTwo")));
                }

                //Validations for External Identifier Add-More Block
                String className = "ExternalIdentifier:";
                String key1 = "_name";
                String key2 = "_value";
                int index = 1;
                boolean isError = false;

                while (true)
                {
                    String keyOne = className + index + key1;
                    String keyTwo = className + index + key2;
                    String value1 = (String) externalIdentifier.get(keyOne);
                    String value2 = (String) externalIdentifier.get(keyTwo);

                    if (value1 == null || value2 == null)
                    {
                        break;
                    }
                    else if (value1.equals("") && value2.equals(""))
                    {
                        externalIdentifier.remove(keyOne);
                        externalIdentifier.remove(keyTwo);
                    }
                    else if ((!value1.equals("") && value2.equals(""))
                            || (value1.equals("") && !value2.equals("")))
                    {
                        isError = true;
                        break;
                    }
                    index++;
                }

                if (isError)
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.specimen.externalIdentifier.missing",
                            ApplicationProperties.getValue("specimen.msg")));
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

    /**
     * @return Returns the positionInStorageContainer.
     */
    public String getPositionInStorageContainer()
    {
        return positionInStorageContainer;
    }

    /**
     * @param positionInStorageContainer The positionInStorageContainer to set.
     */
    public void setPositionInStorageContainer(String positionInStorageContainer)
    {
        this.positionInStorageContainer = positionInStorageContainer;
    }
}