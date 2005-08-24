package edu.wustl.catissuecore.query;




/**
 *<p>Title: AdvancedQuery</p>
 *<p>Description:  This class provides implementation for advanced query interface</p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class AdvancedQuery extends Query {
    
    /**
     * Constructs a AdvancedQuery object
     * @param queryStartObject - Start object for the query
     */
	public AdvancedQuery(final String queryStartObject){
	    this.whereConditions = new AdvancedConditionsImpl();
	    this.queryStartObject = queryStartObject;
	}

	/**
	 * Add child conditions node to parent conditions node
	 * @param parent parent node
	 * @param child child node
	 * @return true if child is added to parent else false
	 */
	public boolean addCondition(AdvancedConditionsNode parent , AdvancedConditionsNode child)
	{
		return ((AdvancedConditionsImpl)this.whereConditions).addCondition(parent,child);
	}
	
	/**
	 * Add condition to root
	 * @param condition - object to be added
	 * @return
	 */
	public boolean addCondition(AdvancedConditionsNode condition)
	{
		return ((AdvancedConditionsImpl)this.whereConditions).addCondition(condition);
	}

}