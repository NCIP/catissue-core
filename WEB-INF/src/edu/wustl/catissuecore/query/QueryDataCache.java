/**
 * <p>Title: QueryDataCache Class>
 * <p>Description:  This Class is used to cache all the data (table names, table display names,
 *  column names, column display name etc.) required for query modules.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Nov 22, 2005
 */

package edu.wustl.catissuecore.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.dao.DAOFactory;
import edu.wustl.catissuecore.dao.HibernateDAO;
import edu.wustl.catissuecore.domain.QueryTableData;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This Class is used to cache all the data (table names, table display names,
 * column names, column display name etc.) required for query modules.
 * @author aniruddha_phadnis
 */
public class QueryDataCache
{
	/**
     * Returns a map of all query data.
     * @return a map of all query data.
     */
	public static Map getQueryData() throws DAOException
	{
		HibernateDAO dao = (HibernateDAO)DAOFactory.getDAO(Constants.HIBERNATE_DAO);
		
		dao.openSession(null);
		List list = dao.retrieve(QueryTableData.class.getName());
		dao.closeSession();
		
		HashMap tableMap = new HashMap();
		
		for(int i=0;i<list.size();i++)
		{
			QueryTableData tableData = (QueryTableData)list.get(i);
			tableMap.put(tableData.getAliasName(),tableData);
		}
		return tableMap;
	}
}