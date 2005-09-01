/*
 * Created on Aug 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.tissuesite;

import java.io.Serializable;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.vo.TreeNode;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TissueSiteTreeNode implements TreeNode, Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
    private String identifier;

    private String value;

    private String parentIdentifier;

    private String cdeLongName;

    private String cdePublicId;

    public TissueSiteTreeNode()
    {
        identifier = null;
        value = null;
        parentIdentifier = null;
        cdeLongName = null;
        cdePublicId = null;
    }
    
    /**
     * @return Returns the cdeLongName.
     */
    public String getCdeLongName()
    {
        return cdeLongName;
    }

    /**
     * @param cdeLongName The cdeLongName to set.
     */
    public void setCdeLongName(String cdeLongName)
    {
        this.cdeLongName = cdeLongName;
    }

    /**
     * @return Returns the cdePublicId.
     */
    public String getCdePublicId()
    {
        return cdePublicId;
    }

    /**
     * @param cdePublicId The cdePublicId to set.
     */
    public void setCdePublicId(String cdePublicId)
    {
        this.cdePublicId = cdePublicId;
    }

    /**
     * @return Returns the identifier.
     */
    public String getIdentifier()
    {
        return identifier;
    }

    /**
     * @param identifier The identifier to set.
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    /**
     * @return Returns the parentIdentifier.
     */
    public Object getParentIdentifier()
    {
        return parentIdentifier;
    }

    /**
     * @param parentIdentifier The parentIdentifier to set.
     */
    public void setParentIdentifier(String parentIdentifier)
    {
        this.parentIdentifier = parentIdentifier;
    }

    /**
     * @return Returns the permissibleValue.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param permissibleValue The permissibleValue to set.
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
    public String toString()
    {
        String nodeName = this.value;
        if ((this.cdePublicId != null) && (this.identifier == null))
            nodeName = this.cdeLongName;
        return nodeName;
    }
    
    public void initialiseRoot()
    {
        this.setValue(Constants.TISSUE_SITE);
    }
    
    public TreeNode getParentTreeNode()
    {
        TissueSiteTreeNode treeNode = new TissueSiteTreeNode();
        treeNode.setCdePublicId(this.getCdePublicId());
        treeNode.setCdeLongName(this.getCdeLongName());
        return treeNode;
    }
    
    public boolean isChildOf(TreeNode treeNode)
    {
        TissueSiteTreeNode tissueSiteTreeNode = (TissueSiteTreeNode) treeNode;
        return this.getParentIdentifier().equals(tissueSiteTreeNode.getIdentifier());
    }
    
    public boolean hasEqualParents(TreeNode treeNode)
    {
        TissueSiteTreeNode tissueSiteTreeNode = (TissueSiteTreeNode) treeNode; 
        return this.getCdePublicId().trim().equals(tissueSiteTreeNode.getCdePublicId().trim());
    }
}