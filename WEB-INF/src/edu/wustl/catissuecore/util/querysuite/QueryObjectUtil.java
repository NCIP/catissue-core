package edu.wustl.catissuecore.util.querysuite;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.IQuery;

/**
 * 
 * @author deepti_shelar
 *
 */
public class QueryObjectUtil
{
	/**
	 * private instance of IQuery.
	 */
	private static IQuery queryObjectInstance = null;
	/**
	 * returns the same instance if not null.
	 * @return IQuery queryObject
	 */
	public static IQuery getQueryObjectInstance()
	{
		if (queryObjectInstance == null) 
		{
			queryObjectInstance = QueryObjectFactory.createQuery();	
		}
		return queryObjectInstance;
	}

}
