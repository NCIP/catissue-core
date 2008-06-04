/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.QueryResultObjectDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * @author supriya_dankh
 *
 */
public class QueryCsmBizLogic
{
 
	/**
	 * @param selectSql
	 * @param sessionData
	 * @param queryResulObjectDataMap
	 * @param root 
	 * @param hasConditionOnIdentifiedField 
	 * @return
	 * @throws DAOException 
	 * @throws ClassNotFoundException 
	 */
	public List executeCSMQuery(String selectSql, SessionDataBean sessionData,
			Map<Long, QueryResultObjectDataBean> queryResulObjectDataMap, OutputTreeDataNode root, boolean hasConditionOnIdentifiedField) throws DAOException, ClassNotFoundException
	{  
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		List<List<String>> dataList = new ArrayList<List<String>>();
		try
		{ 
			dao.openSession(sessionData);
			dataList = dao.executeQuery(selectSql, sessionData, sessionData.isSecurityRequired(), hasConditionOnIdentifiedField, queryResulObjectDataMap);
			dao.commit();
		}
		finally
		{
			dao.closeSession();
		}
		return dataList;
	}
	
}
