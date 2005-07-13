/**
 * <p>Title: QueryTreeNodeMap Class>
 * <p>Description:  QueryTreeNodeMap Class is used map the name of the node according 
 * to its level in the query tree.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.query;
import java.util.HashMap;
import java.util.Map;

/**
 * QueryTreeNodeMap Class is used map the name of the node according 
 * to its level in the query tree.
 * @author gautam_shetty
 */
public class QueryTreeNodeMap
{
    /**
     * Map of name of node to the level.
     */
    private Map nodeMap= new HashMap();
    
    /**
     * Initializes a QueryTreeNodeMap. 
     */
    public QueryTreeNodeMap()
    {
        nodeMap.put(new Integer(0),"Root");
        nodeMap.put(new Integer(1),"Participant");
        nodeMap.put(new Integer(2),"Accession");
        nodeMap.put(new Integer(3),"Specimen");
        nodeMap.put(new Integer(4),"Segment");
        nodeMap.put(new Integer(5),"Sample");
    }
    
    /**
     * Returns the name of the node according to the level.
     * @param level The level of the node.
     * @return the name of the node according to the level.
     */
    public String getNodeName(int level)
    {
        return (String)nodeMap.get(new Integer(level));
    }
}
