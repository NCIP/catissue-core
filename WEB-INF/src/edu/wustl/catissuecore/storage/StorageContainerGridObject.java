/*
 * Created on Jul 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.storage;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StorageContainerGridObject
{

    private long systemIdentifier;
    
    private String type;

    private Integer oneDimensionCapacity;

    private Integer twoDimensionCapacity;

    private Collection childrenContainerCollection = new HashSet();

    /**
     * @return Returns the oneDimensionCapacity.
     */
    public Integer getOneDimensionCapacity()
    {
        return oneDimensionCapacity;
    }

    /**
     * @param oneDimensionCapacity The oneDimensionCapacity to set.
     */
    public void setOneDimensionCapacity(Integer oneDimensionCapacity)
    {
        this.oneDimensionCapacity = oneDimensionCapacity;
    }

    /**
     * @return Returns the twoDimensionCapacity.
     */
    public Integer getTwoDimensionCapacity()
    {
        return twoDimensionCapacity;
    }

    /**
     * @param twoDimensionCapacity The twoDimensionCapacity to set.
     */
    public void setTwoDimensionCapacity(Integer twoDimensionCapacity)
    {
        this.twoDimensionCapacity = twoDimensionCapacity;
    }

    /**
     * @return Returns the childrenContainerCollection.
     */
    public Collection getChildrenContainerCollection()
    {
        return childrenContainerCollection;
    }

    /**
     * @param childrenContainerCollection The childrenContainerCollection to set.
     */
    public void setChildrenContainerCollection(
            Collection childrenContainerCollection)
    {
        this.childrenContainerCollection = childrenContainerCollection;
    }

    /**
     * @return Returns the systemIdentifier.
     */
    public long getSystemIdentifier()
    {
        return systemIdentifier;
    }

    /**
     * @param systemIdentifier The systemIdentifier to set.
     */
    public void setSystemIdentifier(long systemIdentifier)
    {
        this.systemIdentifier = systemIdentifier;
    }
    /**
     * @return Returns the type.
     */
    public String getType()
    {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(String type)
    {
        this.type = type;
    }
}