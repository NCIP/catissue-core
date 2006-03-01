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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.vo.TreeNode;
import edu.wustl.catissuecore.vo.TreeNodeFactory;

/**
 * GenerateTree generates tree for the storage structure.
 * @author gautam_shetty
 */
public class GenerateTree
{
    /**
     * Create a tree based on the relation map between nodes sent.
     * @param containerRelationMap the relation map. (container id, parent container id)
     * @param containerMap the container map.
     * @param treeType the tree type.
     * @param nodeToBeSelected node be shown as selected.
     * @return tree to be formed.
     */
    public JTree createTree(Map containerRelationMap, Map containerMap, int treeType, Long nodeToBeSelected)
    {
        TreeNode rootNode = TreeNodeFactory.getTreeNode(treeType);
        rootNode.initialiseRoot();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootNode);
        TreePath pathToRoot = null;
        
        //get the set of container ids.
        Set keySet = containerRelationMap.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext())
        {
            Object childIdNode = iterator.next();
            TreeNode childTreeNode = (TreeNode)containerMap.get(childIdNode);
            
            //check if the node is already present in the tree.
            if (isPresent(root, childTreeNode))
            {
                continue;
            }
            
            DefaultMutableTreeNode childDefNode = new DefaultMutableTreeNode(childTreeNode);
            buildTree(containerRelationMap, containerMap, childDefNode, root);
        }
        
        JTree tree = new JTree(root);
        
        return tree;
    }
    
    /**
     * Checks whether the childNode is present in the tree represented by the root.
     * @param root the root node of the tree.
     * @param childNode the node to be checked.
     * @return true if the childNode is present in the tree else returns false.
     */
    private boolean isPresent(DefaultMutableTreeNode root, TreeNode childNode)
    {
        boolean flag = false;
        DefaultMutableTreeNode node = getNode(root, childNode);
        if (node != null)
        {
            flag = true; 
        }
        
        return flag;
    }
    
    /**
     * Returns the node corresponding to the childNode in the tree.
     * @param root the root node in the tree.
     * @param childNode the node which is required.
     * @return Returns the required node if present in the tree, else returns null.
     */
    private DefaultMutableTreeNode getNode(DefaultMutableTreeNode root, TreeNode childNode)
    {
        DefaultMutableTreeNode returnNode = null;
        Enumeration treeEnumeration = root.breadthFirstEnumeration();
        while (treeEnumeration.hasMoreElements())
        {
            DefaultMutableTreeNode defTreeNode = (DefaultMutableTreeNode)treeEnumeration.nextElement();
            TreeNode treeNode = (TreeNode) defTreeNode.getUserObject();
            
            if (childNode.getIdentifier() == null)
            {
                if (childNode.getParentIdentifier().equals(treeNode.getParentIdentifier()))
                {
                    returnNode = defTreeNode;
                    break;
                }
            }
            else if (treeNode.getIdentifier() != null && childNode.getIdentifier() != null)
            {
                if (treeNode.getIdentifier().equals(childNode.getIdentifier()))
                {
                    returnNode = defTreeNode;
                    break;
                }
            }
        }
        
        return returnNode;
    }
    
    private void buildTree(Map containerRelationMap, Map containerMap,
            DefaultMutableTreeNode subTree, DefaultMutableTreeNode root)
    {
        System.out.println("2");
        TreeNode childNode = (TreeNode)subTree.getUserObject();
        System.out.println("Child Node..............."+childNode);
        Object parentId = containerRelationMap.get(childNode.getIdentifier());
        System.out.println("parent Id before null......................."+parentId);
        
        if (parentId == null)
        {
            TreeNode firstParent = childNode.getParentTreeNode();
            
            if (firstParent.isPresentIn(root) == false)
            {
                DefaultMutableTreeNode firstParentDefNode = new DefaultMutableTreeNode(firstParent);
                firstParentDefNode.add(subTree);
                root.add(firstParentDefNode);
            }
            else
            {
                DefaultMutableTreeNode firstParentDefNode = getNode(root, firstParent);
                firstParentDefNode.add(subTree);
            }
            
            return;
        }
        
        DefaultMutableTreeNode parentDefNode = getParent(root, childNode);
        if (parentDefNode != null)
        {
            parentDefNode.add(subTree);
            return;
        }
        
        TreeNode parentNode = (TreeNode)containerMap.get(parentId);
        parentDefNode = new DefaultMutableTreeNode(parentNode);
        parentDefNode.add(subTree);
        
        System.out.println("3");
        buildTree(containerRelationMap, containerMap, parentDefNode, root);
    }
    
    private DefaultMutableTreeNode getParent(DefaultMutableTreeNode parentNode, TreeNode childNode)
    {
        TreeNode parentTreeNode = (TreeNode) parentNode.getUserObject();
        if (childNode.isChildOf(parentTreeNode))
        {
            return parentNode;
        }
        
        DefaultMutableTreeNode returnNode = null;
        for (int i=0;i<parentNode.getChildCount();i++)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) parentNode.getChildAt(i);
            returnNode = getParent(node, childNode);
            if (returnNode != null)
                break;
        }
        
        return returnNode;
    }
    
    public JTree createTree(Vector dataVector, int treeType, Long nodeToBeSelected, String rootName)
    {
        TreeNode rootNode = TreeNodeFactory.getTreeNode(treeType);
        rootNode.initialiseRoot(rootName);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootNode);
        DefaultMutableTreeNode parentNode, tempNode = new DefaultMutableTreeNode();
        TreePath pathToRoot = null;
        synchronized (dataVector)
        {
            Iterator iterator = dataVector.iterator();
            while (iterator.hasNext())
            {            	
                parentNode = root;
                DefaultMutableTreeNode node;
                
                TreeNode treeNode = (TreeNode) iterator.next();
                System.out.println("TreeNode....................."+treeNode);
                DefaultMutableTreeNode nextNode = new DefaultMutableTreeNode(treeNode);
                DefaultMutableTreeNode targetParentNode = null;
                System.out.println("storage container id : "+treeNode.getIdentifier());
                System.out.println("storage container Parent id : "+treeNode.getParentIdentifier());
//                System.out.println("Site id : "+treeNode.getParentTreeNode().getIdentifier());
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
                   	    
                       	tempNode = siteTreeNode;
                       	targetParentNode = siteTreeNode;
//                       	if (treeType == Constants.TISSUE_SITE_TREE_ID)
//                       	{
//                       	    targetParentNode = root;
//                       	}
                    }
                }
                else
                {
                    for (int i = 0; i < parentNode.getChildCount(); i++)
                    {
                        DefaultMutableTreeNode site = (DefaultMutableTreeNode)parentNode.getChildAt(i);
                        node = getChildNode(parentNode,treeNode);
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
        
        if (treeType == Constants.TISSUE_SITE_TREE_ID)
        {
            root = tempNode;
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
