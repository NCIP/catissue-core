/*
 * Created on Aug 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.vo;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface TreeNode
{
    
    public void initialiseRoot();
    
    public TreeNode getParentTreeNode();
    
    public boolean isChildOf(TreeNode treeNode);
    
    public boolean hasEqualParents(TreeNode treeNode);
    
    public Object getParentIdentifier();

    public Object getIdentifier();
    
    public boolean isPresentIn(DefaultMutableTreeNode parentNode);
    
}
