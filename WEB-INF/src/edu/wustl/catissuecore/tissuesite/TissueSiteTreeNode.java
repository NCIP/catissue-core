/*
 * Created on Aug 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.tissuesite;

import java.io.Serializable;

import javax.swing.tree.DefaultMutableTreeNode;

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

    public TissueSiteTreeNode(String identifier,String value,String parentIdentifier)
    {
        this.identifier = identifier;
        this.value = value;
        this.parentIdentifier = parentIdentifier;
    }
    public TissueSiteTreeNode()
    {
        this(null,Constants.TISSUE_SITE,null);
    }

    /**
     * @return Returns the identifier.
     */
    public Object getIdentifier()
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
        return this.value;
    }
    
    public void initialiseRoot()
    {
        this.setValue(Constants.TISSUE_SITE);
    }
    
    public TreeNode getParentTreeNode()
    {
        TissueSiteTreeNode treeNode = new TissueSiteTreeNode();
        treeNode.setParentIdentifier(parentIdentifier); 
        return treeNode;
    }
    
    public boolean isChildOf(TreeNode treeNode)
    {
        TissueSiteTreeNode tissueSiteTreeNode = (TissueSiteTreeNode) treeNode;
        return this.parentIdentifier.equals(tissueSiteTreeNode.getIdentifier());
    }
    
    public boolean hasEqualParents(TreeNode treeNode)
    {
        TissueSiteTreeNode tissueSiteTreeNode = (TissueSiteTreeNode) treeNode; 
        if(parentIdentifier==null)
            return true;
        return (parentIdentifier.equals(tissueSiteTreeNode.parentIdentifier));
    }
    
    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.vo.TreeNode#isPresentIn(javax.swing.tree.DefaultMutableTreeNode)
	 */
	public boolean isPresentIn(DefaultMutableTreeNode parentNode) 
	{
		return false;
	}
}