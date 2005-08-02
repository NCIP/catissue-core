/*
 * Created on Jul 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.storage;

import java.io.Serializable;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated storageContainerType comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeNode implements Serializable
{

    private Long storageContainerIdentifier;

    private String storageContainerName;

    private String storageContainerType;
    
    private Long parentStorageContainerIdentifier;
    
    private Long siteSystemIdentifier;
    
    private String siteName;
    
    private String siteType;
    
    /**
     * 
     */
    public TreeNode()
    {
        storageContainerIdentifier = null;
        storageContainerName = null;
        storageContainerType = null;
        parentStorageContainerIdentifier = null;
        siteSystemIdentifier = null;
        siteName = null;
        siteType = null;
    }

    public TreeNode(Long identifier, String name, String type, Long parentIdentifier)
    {
        this.storageContainerIdentifier = identifier;
        this.storageContainerName = name;
        this.storageContainerType = type;
        this.parentStorageContainerIdentifier = parentIdentifier;
    }

    /**
     * @return Returns the storageContainerIdentifier.
     */
    public Long getStorageContainerIdentifier()
    {
        return storageContainerIdentifier;
    }

    /**
     * @param storageContainerIdentifier The storageContainerIdentifier to set.
     */
    public void setStorageContainerIdentifier(Long identifier)
    {
        this.storageContainerIdentifier = identifier;
    }

    /**
     * @return Returns the storageContainerName.
     */
    public String getStorageContainerName()
    {
        return storageContainerName;
    }

    /**
     * @param storageContainerName The storageContainerName to set.
     */
    public void setStorageContainerName(String name)
    {
        this.storageContainerName = name;
    }

    /**
     * @return Returns the storageContainerType.
     */
    public String getStorageContainerType()
    {
        return storageContainerType;
    }

    /**
     * @param storageContainerType The storageContainerType to set.
     */
    public void setStorageContainerType(String type)
    {
        this.storageContainerType = type;
    }

    /**
     * @return Returns the parentStorageContainerIdentifier.
     */
    public Long getParentStorageContainerIdentifier()
    {
        return parentStorageContainerIdentifier;
    }
    /**
     * @param parentStorageContainerIdentifier The parentStorageContainerIdentifier to set.
     */
    public void setParentStorageContainerIdentifier(Long parentIdentifier)
    {
        this.parentStorageContainerIdentifier = parentIdentifier;
    }
    
    public String toString()
    {
        String nodeName = this.storageContainerType + ":" + this.storageContainerIdentifier; 
        if (storageContainerIdentifier == null)
            nodeName = this.siteType + ":" + this.siteName;
        else if (storageContainerName.equals(Constants.CATISSUE_CORE))
            nodeName = Constants.CATISSUE_CORE;
        return nodeName; 
    }
    /**
     * @return Returns the siteName.
     */
    public String getSiteName()
    {
        return siteName;
    }
    /**
     * @param siteName The siteName to set.
     */
    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }
    /**
     * @return Returns the siteSystemIdentifier.
     */
    public Long getSiteSystemIdentifier()
    {
        return siteSystemIdentifier;
    }
    /**
     * @param siteSystemIdentifier The siteSystemIdentifier to set.
     */
    public void setSiteSystemIdentifier(Long siteSystemIdentifier)
    {
        this.siteSystemIdentifier = siteSystemIdentifier;
    }
    /**
     * @return Returns the siteType.
     */
    public String getSiteType()
    {
        return siteType;
    }
    /**
     * @param siteType The siteType to set.
     */
    public void setSiteType(String siteType)
    {
        this.siteType = siteType;
    }
}