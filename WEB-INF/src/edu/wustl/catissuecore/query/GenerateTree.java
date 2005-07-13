/**
 * <p>Title: GenerateTree Class>
 * <p>Description:	GenerateTree the tree representation of query result view.</p>
 * Copyright: Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.catissuecore.query;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.catissuecore.util.global.Constants;


/**
 * GenerateTree the tree representation of query result view.
 * @author gautam_shetty
 */
public class GenerateTree
{
    
    /**
     * Builds a JTree from the ResultSet passed.
     * @param rs The ResultSet object. 
     * @return Returns the built JTree.
     * @throws SQLException
     */
    public JTree createTreeView(Vector dataList)
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(Constants.ROOT);
        DefaultMutableTreeNode parentNode;

        synchronized (dataList)
        {
            Iterator iterator = dataList.iterator();

            while (iterator.hasNext())
            {
                parentNode = root;
                DefaultMutableTreeNode node;
                TreeNodeData treeNodeData;

                String[] data = (String[]) iterator.next();
                for (int level = Constants.MIN_LEVEL; level < Constants.MAX_LEVEL; level++)
                {
                    treeNodeData = new TreeNodeData(level, Long
                            .parseLong(data[level - 1]));
                    if ((node = getChildNode(parentNode, treeNodeData)) == null)
                    {
                        parentNode = addChildNode(parentNode, treeNodeData);
                    }
                    else
                    {
                        parentNode = node;
                    }
                }
            }
        }

        JTree tree = new JTree(root);
        return tree;
    }

    /**
     * Returns a tree node which is child of the “ParentNode”, 
     * if the data associated with it is same as “treeNodeData”, else returns null.
     * @param parentNode The parent node.
     * @param treeNodeData The node to be checked.
     * @return Returns the node if the data associated with it is same as treeNodeData, else returns null.
     */
    private DefaultMutableTreeNode getChildNode(
            DefaultMutableTreeNode parentNode, TreeNodeData treeNodeData)
    {
        DefaultMutableTreeNode node = null;
        
        try
        {
            for (int i = 0; i < parentNode.getChildCount(); i++)
            {
                node = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                String nodeData = (String) node.getUserObject();
                //TreeNodeData nodeData = (TreeNodeData)node.getUserObject();

                if (getId(nodeData) == treeNodeData.getId())
                {
                    return node;
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException exp)
        {
            return null;
        }

        return null;
    }

    /**
     * Adds the treeNodeData as the child of the parentNode.
     * @param parentNode The parent node.
     * @param treeNodeData The child node to be added.
     * @return Returns the child node which is added.
     */
    private DefaultMutableTreeNode addChildNode(
            DefaultMutableTreeNode parentNode, TreeNodeData treeNodeData)
    {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
                treeNodeData.toString());
        DefaultMutableTreeNode d = (DefaultMutableTreeNode) parentNode;
        d.add(childNode);
        return childNode;
    }

    /**
     * Retuns the id of the field(Participant,Accession,Specimen,Segment,Sample) 
     * @param fieldName The field whose id is to be found out.
     * @return The id of the fields.
     */
    private long getId(String fieldName)
    {
        StringTokenizer str = new StringTokenizer(fieldName, ":");
        str.nextToken();
        String id = str.nextToken();
        return Long.parseLong(id);
    }
}
