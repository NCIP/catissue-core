/*
 * Created on Jul 13, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.actionForm;

import org.apache.struts.action.ActionForm;

/**
 * @author gautam_shetty
 * @author ashwin_gupta 
 */
public class ArrayForm extends ActionForm
{
	private static final long serialVersionUID = 1L;

	String createdBy;

    String specimenClass;

    String specimenType;

    String concentration;

    String quantity;

    String barcode;

    String oneDimensionCapacity;

    String twoDimensionCapacity;

    String storageContainer;

    String positionDimensionOne;

    String positionDimensionTwo;

    String comments;

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
     * @return Returns the comments.
     */
    public String getComments()
    {
        return comments;
    }

    /**
     * @param comments The comments to set.
     */
    public void setComments(String comments)
    {
        this.comments = comments;
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
     * @return Returns the createdBy.
     */
    public String getCreatedBy()
    {
        return createdBy;
    }

    /**
     * @param createdBy The createdBy to set.
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    /**
     * @return Returns the oneDimensionCapacity.
     */
    public String getOneDimensionCapacity()
    {
        return oneDimensionCapacity;
    }

    /**
     * @param oneDimensionCapacity The oneDimensionCapacity to set.
     */
    public void setOneDimensionCapacity(String oneDimensionCapacity)
    {
        this.oneDimensionCapacity = oneDimensionCapacity;
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
     * @return Returns the specimenClass.
     */
    public String getSpecimenClass()
    {
        return specimenClass;
    }

    /**
     * @param specimenClass The specimenClass to set.
     */
    public void setSpecimenClass(String specimenClass)
    {
        this.specimenClass = specimenClass;
    }

    /**
     * @return Returns the specimenType.
     */
    public String getSpecimenType()
    {
        return specimenType;
    }

    /**
     * @param specimenType The specimenType to set.
     */
    public void setSpecimenType(String specimenType)
    {
        this.specimenType = specimenType;
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
     * @return Returns the twoDimensionCapacity.
     */
    public String getTwoDimensionCapacity()
    {
        return twoDimensionCapacity;
    }

    /**
     * @param twoDimensionCapacity The twoDimensionCapacity to set.
     */
    public void setTwoDimensionCapacity(String twoDimensionCapacity)
    {
        this.twoDimensionCapacity = twoDimensionCapacity;
    }
}