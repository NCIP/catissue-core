/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryDetails;
import edu.wustl.common.beans.QueryResultObjectDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;


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
	public List executeCSMQuery(String selectSql, QueryDetails queryDetailsObj,
			Map<Long, QueryResultObjectDataBean> queryResulObjectDataMap,
			OutputTreeDataNode root, boolean hasConditionOnIdentifiedField)
	throws DAOException, ClassNotFoundException
	{  
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		List<List<String>> dataList = new ArrayList<List<String>>();
		try
		{ 
			dao.openSession(queryDetailsObj.getSessionData());
			dataList = dao.executeQuery(selectSql, queryDetailsObj.getSessionData(),
					queryDetailsObj.getSessionData().isSecurityRequired(),
					hasConditionOnIdentifiedField, queryResulObjectDataMap);
			dao.commit();
		}
		finally
		{
			dao.closeSession();
		}
		return dataList;
	}
	
	/**
	 * Retrieves the main entity list if the entity is Abstract
	 * @param firstEntity Abstract Entity
	 * @param lastEntity The entity on which the query has been fired
	 * @return List of main entities
	 */
	public static List<EntityInterface> getMainEntityList(
			EntityInterface firstEntity, EntityInterface lastEntity) 
	{
		
		Long id1 = firstEntity.getId();
		Long id2 = lastEntity.getId();
		
		Connection conn = DBUtil.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		List<Long> firstEntityIdList = new ArrayList<Long>();
		List<EntityInterface> mainEntityList = new ArrayList<EntityInterface>();
		try 
		{		
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Select FIRST_ENTITY_ID from PATH where INTERMEDIATE_PATH in (Select INTERMEDIATE_PATH from PATH where FIRST_ENTITY_ID = "+id1+" and LAST_ENTITY_ID = "+id2+") and LAST_ENTITY_ID = "+id2);
			while(rs.next())
			{
				if(rs.getInt(1) != id1)
					firstEntityIdList.add(rs.getLong(1));
			}
			Collection<EntityInterface> allEntities = (Collection<EntityInterface>)firstEntity.getEntityGroup().getEntityCollection();
			for(Long firstEntityId : firstEntityIdList)
			{
				for(EntityInterface tempEntity : allEntities)
				{
					if(Integer.parseInt(tempEntity.getId().toString()) == Integer.parseInt(firstEntityId.toString()))
					{
						mainEntityList.add(tempEntity);
						break;
					}
				}
			}
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		} 
		finally
		{
			try
			{
				rs.close();
				stmt.close();
				DBUtil.closeConnection();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return mainEntityList;
	}
}
