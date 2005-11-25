/**
 * <p>Title: GenerateTree Class>
 * <p>Description:	GenerateTree generates tree for the storage structure.</p>
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
import javax.swing.tree.TreePath;

import edu.wustl.catissuecore.query.TreeNodeData;
import edu.wustl.catissuecore.vo.TreeNode;
import edu.wustl.catissuecore.vo.TreeNodeFactory;


/**
 * GenerateTree generates tree for the storage structure.
 * @author gautam_shetty
 */
public class GenerateTree
{
    public JTree createTree(Vector dataVector, int treeType,
    		Long nodeToBeSelected)
    {
        TreeNode rootNode = TreeNodeFactory.getTreeNode(treeType);
        rootNode.initialiseRoot();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootNode);
        DefaultMutableTreeNode parentNode;
        TreePath pathToRoot = null;
        synchronized (dataVector)
        {
            Iterator iterator = dataVector.iterator();
            while (iterator.hasNext())
            {            	
                parentNode = root;
                DefaultMutableTreeNode node;

                TreeNode treeNode = (TreeNode) iterator.next();

                DefaultMutableTreeNode nextNode = new DefaultMutableTreeNode(treeNode);
                DefaultMutableTreeNode targetParentNode = null;
                TreeNodeData nodeData = (TreeNodeData) treeNode;
                if (treeNode.getParentIdentifier() == null)
                {
                	boolean parentNodeFound = false;
                    for (int i = 0; i < parentNode.getChildCount(); i++)
                    {
                        DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                        TreeNode childDataNode = (TreeNode) childNode.getUserObject();
                        
                        if (childDataNode.hasEqualParents(treeNode))
                        {
                        	targetParentNode = childNode;
                        	parentNodeFound = true;
                            break;
                        }
                    }
                    
                    if(false == parentNodeFound)
                    {
                       	// Add missing parent node.
                    	TreeNode siteNode = treeNode.getParentTreeNode();
                       	DefaultMutableTreeNode siteTreeNode = new DefaultMutableTreeNode(siteNode);
                       	parentNode.add(siteTreeNode);
                       	
                       	targetParentNode = siteTreeNode;
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
                        	targetParentNode = node;
                            break;
                        }
                    }
                }
                
                if(null != targetParentNode)
                {
                    TreeNode nextTreeNode = (TreeNode)nextNode.getUserObject();
                    if (nextTreeNode.isPresentIn(targetParentNode) == false)
                    {
                        targetParentNode.add(nextNode);
                    }
                	
                	//Sri: Select the path of the node to be selected
                	if(nodeToBeSelected.equals(treeNode.getIdentifier()))
                	{
                		pathToRoot  = new TreePath(nextNode.getPath());   
                	}
                }
            }
        }
        
        JTree tree = new JTree(root);
        if(null != pathToRoot) // if path to be selected, then select in the tree
        {
        	tree.setSelectionPath(pathToRoot);
        }
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
                if (treeNode.isChildOf(nodeData))
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
