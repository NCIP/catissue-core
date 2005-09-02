package edu.wustl.catissuecore.query;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;




/**
 *<p>Title: SimpleQuery</p>
 *<p>Description:  This class provides implementation for simple query interface</p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class SimpleQuery extends Query 
{
    
    /**
     * Constructs a SimpleQuery object
     * @param queryStartObject - Start object for the query
     */
	public SimpleQuery(final String queryStartObject){
	    this.whereConditions = new SimpleConditionsImpl();
	    this.queryStartObject = queryStartObject;
	}
	
	/**
	 * Adds a SimpleConditionsNode object to the set of conditions for this query
	 * @param condition - condition to be added
	 * @return true (as per the general contract of Collection.add). 
	 */
	public boolean addCondition(SimpleConditionsNode condition){
	    return((SimpleConditionsImpl)this.whereConditions).addCondition(condition);
	}
	
	/**
	 * Adds a SimpleConditionsNode object to the set of conditions for this query
	 * @param condition - condition to be added
	 * @return true (as per the general contract of Collection.add). 
	 */
	public void addConditions(Collection simpleConditionsNodeCollection)
	{
	    Iterator iterator = simpleConditionsNodeCollection.iterator();
        
	    while (iterator.hasNext())
	    {
	        SimpleConditionsNode simpleConditionsNode = (SimpleConditionsNode) iterator.next();
	        addCondition(simpleConditionsNode);
	    }
	}
	
	/**
	 * Inserts the specified condition at the specified position in whereConditions SimpleConditionsImpl object. 
	 * Shifts the element currently at that position (if any) and any subsequent elements to the right 
	 * (adds one to their indices).
	 * @param position index at which the specified condition is to be inserted.
     * @param condition condition to be inserted
	 * @return
	 */
	public boolean addCondition(int position,SimpleConditionsNode condition){
	    return ((SimpleConditionsImpl)this.whereConditions).addCondition(position,condition);
	}
	
	
}