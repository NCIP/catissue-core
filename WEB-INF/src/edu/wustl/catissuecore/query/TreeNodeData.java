/**
 * <p>Title: TreeNodeData Class>
 * <p>Description: TreeNodeData represents the node in the query result view tree.</p>
 * Copyright: Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.catissuecore.query;


/**
 * TreeNodeData represents the node in the query result view tree.
 * @author gautam_shetty
 */
public class TreeNodeData 
{
    /**
     * The level of the node.
     */
    int level;
    
    /**
     * Identifier of the data this node represents.  
     */
    long id;
    
    /**
     * Initializes an empty node.
     */
    public TreeNodeData()
    {
    }

    /**
     * Initializes the node with the level and id passed as arguments.
     */
    public TreeNodeData(int level, long id)
    {
        this.level = level;
        this.id = id;
    }
    
    /**
     * Sets the level of the node.
     * @param level the level of the node.
     * @see #getLevel() 
     */
    public void setLevel(int level)
    {
        this.level = level;
    }
    
    /**
     * Returns the level of the node.
     * @return the level of the node.
     * @see #setLevel(int)
     */
    public int getLevel()
    {
        return level;
    }
    
    /**
     * Sets the identifier of the data this node represents.
     * @param id the identifier.
     * @see #getId()
     */
    public void setId(long id)
    {
        this.id = id;
    }
    
    /**
     * Returns the identifier of the data this node represents.
     * @return the identifier of the data this node represents.
     * @see #setId(long)
     */
    public long getId()
    {
        return id;
    }
   
    /**
     * Returns the string representation of the node.
     */
    public String toString()
    {
        QueryTreeNodeMap map = new QueryTreeNodeMap();
        return (map.getNodeName(level)+":"+this.id);
    }
}
