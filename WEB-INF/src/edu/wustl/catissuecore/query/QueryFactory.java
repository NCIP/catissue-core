/**
 *<p>Title: QueryFactory</p>
 *<p>Description:  This is the Factory class for generating query objects</p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
package edu.wustl.catissuecore.query;



public class QueryFactory {
    
    private static QueryFactory queryFactory;
    
    /**
     * Method that returns an instance of this class
     * @return QueryFactory instance
     */
    public static synchronized QueryFactory getInstance()
    {
        if(queryFactory == null)
        {
            queryFactory =new QueryFactory();
        }
        return queryFactory;
    }

    /**
     * Returns a Query instance based on query type and start object
     * @param queryType - type of the query i.e. Simple/Advanced
     * @param queryStartObject - Highest object of the query. The query can be on this object and all the ones that
     * are directly or transitively associated with it.
     * @return Simple/Advanced Query Object
     */
	public synchronized Query newQuery(final String queryType, final String queryStartObject){
		if(queryType.equals(Query.SIMPLE_QUERY))
		{
		    return new SimpleQuery(queryStartObject);
		}
		else if(queryType.equals(Query.ADVANCED_QUERY))
		{
		    return new AdvancedQuery(queryStartObject);
		}
		else
		{
		    return null;
		}
	}

	public synchronized Query retrieveSavedQuery(){
		return null;
	}

	public synchronized boolean saveQuery(){
		return false;
	}

}