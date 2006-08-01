/*
 * Created on Jul 14, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.actionForm;

import org.apache.struts.action.ActionForm;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ArrayTypeForm extends ActionForm
{

    String name;

    String specimenClass;

    String specimenType;

    String oneDimensionCapacity;

    String twoDimensionCapacity;

    String comments;

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
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
}