/**
 * <p>Title: GenerateStorageTree Class>
 * <p>Description:	GenerateStorageTree generates tree for the storage structure.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 */
package edu.wustl.catissuecore.storage;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.catissuecore.vo.TreeNode;
import edu.wustl.catissuecore.vo.TreeNodeFactory;


/**
 * GenerateStorageTree generates tree for the storage structure.
 * @author gautam_shetty
 */
public class GenerateStorageTree
{
    public JTree createTree(Vector dataVector, int treeType)
    {
        TreeNode rootNode = TreeNodeFactory.getTreeNode(treeType);
        rootNode.initialiseRoot();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootNode);
        DefaultMutableTreeNode parentNode;
        
        synchronized (dataVector)
        {
            Iterator iterator = dataVector.iterator();
            while (iterator.hasNext())
            {
                parentNode = root;
                DefaultMutableTreeNode node;

                TreeNode treeNode = (TreeNode) iterator.next();
                
                if (treeNode.getParentIdentifier() == null)
                {
                    boolean found = false;
                    for (int i = 0; i < parentNode.getChildCount(); i++)
                    {
                        DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                        TreeNode childDataNode = (TreeNode) childNode.getUserObject();
                        
                        if (childDataNode.hasEqualParents(treeNode))
                        {
                            found = true;
                            DefaultMutableTreeNode containerNode = new DefaultMutableTreeNode(treeNode);
                            childNode.add(containerNode);
                            break;
                        }
                    }
                    
                    if (!found)
                    {
                        TreeNode siteNode = treeNode.getParentTreeNode();
                        
                       	DefaultMutableTreeNode siteTreeNode = new DefaultMutableTreeNode(siteNode);
                       	DefaultMutableTreeNode childContainerNode = new DefaultMutableTreeNode(treeNode);
                       	siteTreeNode.add(childContainerNode);
                       	parentNode.add(siteTreeNode);
                    }
                }
                else
                {
                    for (int i = 0; i < parentNode.getChildCount(); i++)
                    {
                        DefaultMutableTreeNode site = (DefaultMutableTreeNode)parentNode.getChildAt(i);
                        node = getChildNode(site,treeNode);
                        
                        if (node != null)
                        {
                            DefaultMutableTreeNode childContainerNode = new DefaultMutableTreeNode(treeNode);
                            node.add(childContainerNode);
                            break;
                        }
                    }
                }
            }
        }
        
        JTree tree = new JTree(root);
        return tree;
    }
        
    private DefaultMutableTreeNode getChildNode(
            DefaultMutableTreeNode parentNode, TreeNode treeNode)
    {
        DefaultMutableTreeNode returnNode = null;
        if (parentNode != null)
        {
            boolean flag = false;
            for (int i = 0; i < parentNode.getChildCount();i++)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                TreeNode nodeData = (TreeNode) node.getUserObject();
                if (treeNode.isChildOf(nodeData) )
                {
                    returnNode = node;
                    flag = true;
                    break;
                }
                returnNode = getChildNode(node,treeNode);
                if (returnNode != null)
                    break;
            }
        }
        
        return returnNode;
    }
}
