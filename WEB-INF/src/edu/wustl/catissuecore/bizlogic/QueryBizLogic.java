/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.Set;
import java.util.TreeSet;

import edu.wustl.catissuecore.dao.DAOFactory;
import edu.wustl.catissuecore.dao.JDBCDAO;
import edu.wustl.catissuecore.query.Client;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.catissuecore.query.Operator;
import edu.wustl.catissuecore.query.Relation;
import edu.wustl.catissuecore.query.RelationCondition;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class QueryBizLogic extends DefaultBizLogic
{

    private static final String ALIAS_NAME_TABLE_NAME_MAP_QUERY = 
        "select ALIAS_NAME ,TABLE_NAME from " +
        "CATISSUE_QUERY_INTERFACE_TABLE_DATA";
    
    private static final String ALIAS_NAME_PRIVILEGE_TYPE_MAP_QUERY = 
        "select ALIAS_NAME ,PRIVILEGE_ID from " +
        "CATISSUE_QUERY_INTERFACE_TABLE_DATA";
    
    private static final String GET_RELATION_DATA=
        "select FIRST_TABLE_ID, SECOND_TABLE_ID," +
        "FIRST_TABLE_JOIN_COLUMN, SECOND_TABLE_JOIN_COLUMN " +
    		"from CATISSUE_RELATED_TABLES_MAP";
    
    private static final String GET_COLUMN_DATA=
        "select ALIAS_NAME,COLUMN_NAME from CATISSUE_QUERY_INTERFACE_COLUMN_DATA columnData, " +
        "CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData where columnData.TABLE_ID = tableData.TABLE_ID  " +
        "and columnData.IDENTIFIER=";
    
    private static final String GET_TABLE_ALIAS =
        "select ALIAS_NAME from CATISSUE_QUERY_INTERFACE_TABLE_DATA " +
        "where TABLE_ID=";
    
    private static final String GET_RELATED_TABLE_ALIAS = "SELECT table2.alias_name "+
    " from catissue_table_relation relation, catissue_query_interface_table_data table1, "+
	" catissue_query_interface_table_data table2 "+
	" where relation.parent_table_id = table1.table_id and relation.child_table_id = table2.table_id "+
	" and table1.alias_name = ";

    public static HashMap getQueryObjectNameTableNameMap()
    {
        List list = null;
        HashMap queryObjectNameTableNameMap = new HashMap();
        JDBCDAO dao =null;
        try
        {
            dao = new JDBCDAO();
            dao.openSession(null);
            list = dao.executeQuery(ALIAS_NAME_TABLE_NAME_MAP_QUERY, null, Constants.INSECURE_RETRIEVE, null,null);

            Iterator iterator = list.iterator();
            while (iterator.hasNext())
            {
                List row = (List) iterator.next();
                queryObjectNameTableNameMap.put(row.get(0), row.get(1));
                Logger.out.info("Key: "+row.get(0)+" value: "+row.get(1));
            }

            
        }
        catch (DAOException daoExp)
        {
            Logger.out.debug("Could not obtain table object relation. Exception:"
                    + daoExp.getMessage(), daoExp);
        }
        catch (ClassNotFoundException classExp)
        {
            Logger.out.debug("Could not obtain table object relation. Exception:"
                    + classExp.getMessage(), classExp);
        }
        finally
        {
            try
            {
                dao.closeSession();
            }
            catch (DAOException e)
            {
                Logger.out.debug(e.getMessage(),e);
            }
        }
        return queryObjectNameTableNameMap;
    }
    
    /**
     * This returns the map containing table alias as key
     * and type of privilege on that table as value
     * @return
     */
    public static HashMap getPivilegeTypeMap()
    {
        List list = null;
        HashMap pivilegeTypeMap = new HashMap();
        JDBCDAO dao =null;
        try
        {
            dao = new JDBCDAO();
            dao.openSession(null);
            list = dao.executeQuery(ALIAS_NAME_PRIVILEGE_TYPE_MAP_QUERY, null, Constants.INSECURE_RETRIEVE, null,null);

            Iterator iterator = list.iterator();
            while (iterator.hasNext())
            {
                List row = (List) iterator.next();
                pivilegeTypeMap.put(row.get(0), row.get(1));
                Logger.out.info("Key: "+row.get(0)+" value: "+row.get(1));
            }

            
        }
        catch (DAOException daoExp)
        {
            Logger.out.debug("Could not obtain table privilege map. Exception:"
                    + daoExp.getMessage(), daoExp);
        }
        catch (ClassNotFoundException classExp)
        {
            Logger.out.debug("Could not obtain table privilege map. Exception:"
                    + classExp.getMessage(), classExp);
        }
        finally
        {
            try
            {
                dao.closeSession();
            }
            catch (DAOException e)
            {
                Logger.out.debug(e.getMessage(),e);
            }
        }
        return pivilegeTypeMap;
    }
    
    public static HashMap getRelationData()
    {
        List list = null;
        JDBCDAO dao =null;
        String tableAlias1;
        String columnName1;
        String tableAlias2;
        String columnName2;
        List columnDataList;
        List row;
        String parentTableColumnID;
        String childTableColumnID;
        HashMap relationConditionsForRelatedTables = new HashMap();
        try
        {
            dao = new JDBCDAO();
            dao.openSession(null);
            list = dao.executeQuery(GET_RELATION_DATA, null, Constants.INSECURE_RETRIEVE, null,null);

            Iterator iterator = list.iterator();
            
            while (iterator.hasNext())
            {
                row = (List) iterator.next();
                parentTableColumnID = (String) row.get(0);
                childTableColumnID = (String) row.get(1);
                columnName1 = (String) row.get(2);
                columnName2 = (String) row.get(3);
                if(Integer.parseInt(parentTableColumnID) ==0 || Integer.parseInt(childTableColumnID) ==0)
                {
                    continue;
                }
                
                columnDataList = dao.executeQuery(GET_TABLE_ALIAS+parentTableColumnID, null, Constants.INSECURE_RETRIEVE, null,null);
                if(columnDataList.size() <=0 )
                {
                    continue;
                }
                row = (List) columnDataList.get(0);
                if(row.size() <=0 )
                {
                    continue;
                }
                tableAlias1 = (String) row.get(0);
                
                columnDataList = dao.executeQuery(GET_TABLE_ALIAS+childTableColumnID, null, Constants.INSECURE_RETRIEVE, null,null);
                if(columnDataList.size() <=0 )
                {
                    continue;
                }
                row = (List) columnDataList.get(0);
                if(row.size() <=0 )
                {
                    continue;
                }
                tableAlias2 = (String) row.get(0);
                
                relationConditionsForRelatedTables.put(new Relation(tableAlias1,
                        tableAlias2), new RelationCondition(
                        new DataElement(tableAlias1, columnName1), new Operator(
                                Operator.EQUAL), new DataElement(
                                        tableAlias2,
                                        columnName2)));
                Logger.out.info("Relation: "+tableAlias1+":"+tableAlias2+" "+columnName1+":"+columnName2);
            }

            
        }
        catch (DAOException daoExp)
        {
            Logger.out.debug("Could not obtain table object relation. Exception:"
                    + daoExp.getMessage(), daoExp);
        }
        catch (ClassNotFoundException classExp)
        {
            Logger.out.debug("Could not obtain table object relation. Exception:"
                    + classExp.getMessage(), classExp);
        }
        finally
        {
            try
            {
                dao.closeSession();
            }
            catch (DAOException e)
            {
                Logger.out.debug(e.getMessage(),e);
            }
        }
        return relationConditionsForRelatedTables;
    }
    
    public static void initializeQueryData()
    {
//        setObjectTableNames();
//        setRelationConditionsForRelatedTables();
//        setRelations();
        
        Client.objectTableNames = QueryBizLogic.getQueryObjectNameTableNameMap();
        Client.relationConditionsForRelatedTables = QueryBizLogic.getRelationData();
        Client.privilegeTypeMap = QueryBizLogic.getPivilegeTypeMap();
    }
    
    /**
     * Returns the aliasName of the table from the table id.
     * @param tableId
     * @return
     * @throws DAOException
     */
    public String getAliasNameFromTableId(Long tableId) throws DAOException
    {
        	Logger.out.debug("QueryBizLogic getAliasNameFromTableId.....................tableId......."+tableId);
            JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
            jdbcDAO.openSession(null);
            String [] selectColumnNames = {"ALIAS_NAME"};
            String [] whereColumnNames = {"TABLE_ID"};
            String [] whereColumnConditions = {"="};
            Long [] whereColumnValues = {tableId};
            List list = jdbcDAO.retrieve("CATISSUE_QUERY_INTERFACE_TABLE_DATA", selectColumnNames, 
                    	whereColumnNames, whereColumnConditions, whereColumnValues, null);
            jdbcDAO.closeSession();
            
            String aliasName = null;
            if (list.isEmpty() == false)
            {
                List row = (List) list.get(0);
                aliasName = (String) row.get(0);
            }
            
            return aliasName;
    }
    /**
     * Sets column names depending on the table name selected for that condition.
     * @param request HttpServletRequest
     * @param i number of row.
     * @param value table name.
     * @throws DAOException
     * @throws ClassNotFoundException
     */
    public List getColumnNames(String value) throws DAOException, ClassNotFoundException
    {
        String sql = 	" SELECT tableData2.ALIAS_NAME, temp.COLUMN_NAME, temp.ATTRIBUTE_TYPE, temp.TABLES_IN_PATH, temp.DISPLAY_NAME " +
				        " from CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData2 join " +
				        " ( SELECT  columnData.COLUMN_NAME, columnData.TABLE_ID, columnData.ATTRIBUTE_TYPE, " +
				        " displayData.DISPLAY_NAME, relationData.TABLES_IN_PATH " +
				        " FROM CATISSUE_QUERY_INTERFACE_COLUMN_DATA columnData, " +
				        " CATISSUE_TABLE_RELATION relationData, " +
				        " CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData, " +
				        " CATISSUE_SEARCH_DISPLAY_DATA displayData " +
				        " where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and " +
				        " relationData.PARENT_TABLE_ID = tableData.TABLE_ID and " +
				        " relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and " +
				        " columnData.IDENTIFIER = displayData.COL_ID and " +
				        " tableData.ALIAS_NAME = '"+value+"') as temp " +
				        " on temp.TABLE_ID = tableData2.TABLE_ID ";
        
        Logger.out.debug("SQL*****************************"+sql);
        
        JDBCDAO jdbcDao = new JDBCDAO();
        jdbcDao.openSession(null);
        List list = jdbcDao.executeQuery(sql, null, Constants.INSECURE_RETRIEVE, null,null);
        jdbcDao.closeSession();
        
        List columnNameValueBeanList = new ArrayList();
        
        Iterator iterator = list.iterator();
        int j = 0;
        while (iterator.hasNext())
        {
            List rowList = (List)iterator.next();
            String columnValue = (String)rowList.get(j++)+"."+(String)rowList.get(j++)
            					+"."+(String)rowList.get(j++);
            String tablesInPath = (String)rowList.get(j++);
            if ((tablesInPath != null) && ("".equals(tablesInPath) == false))
            {
                columnValue = columnValue+"."+tablesInPath;
            }
            
            String columnName = (String)rowList.get(j++);
            NameValueBean nameValueBean = new NameValueBean();
            nameValueBean.setName(columnName);
            nameValueBean.setValue(columnValue);
            columnNameValueBeanList.add(nameValueBean);
            j = 0;
        }
        
        return columnNameValueBeanList;
    }
    
    /**
     * Sets the next table names depending on the table in the previous row. 
     * @param request
     * @param i
     * @param prevValue previous table name.
     * @param nextOperatorValue
     * @param checkList
     * @throws DAOException
     * @throws ClassNotFoundException
     */
    public Set getNextTableNames(String prevValue) throws DAOException, ClassNotFoundException
    {
        Set objectList = new TreeSet();
        
        String sql =" (select temp.ALIAS_NAME, temp.DISPLAY_NAME " +
        			" from " +
			        " (select relationData.FIRST_TABLE_ID, tableData.ALIAS_NAME, tableData.DISPLAY_NAME " +
			        " from CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData join " +
			        " CATISSUE_RELATED_TABLES_MAP relationData " +
			        " on tableData.TABLE_ID = relationData.SECOND_TABLE_ID) as temp join CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData2 " +
			        " on temp.FIRST_TABLE_ID = tableData2.TABLE_ID " +
			        " where tableData2.ALIAS_NAME = '"+prevValue+"') " +
			        " union " +
			        " (select temp1.ALIAS_NAME, temp1.DISPLAY_NAME " +
			        " from " +
			        " (select relationData1.SECOND_TABLE_ID, tableData4.ALIAS_NAME, tableData4.DISPLAY_NAME " +
			        " from CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData4 join " +
			        " CATISSUE_RELATED_TABLES_MAP relationData1 " +
			        " on tableData4.TABLE_ID = relationData1.FIRST_TABLE_ID) as temp1 join CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData3 " +
			        " on temp1.SECOND_TABLE_ID = tableData3.TABLE_ID " +
			        " where tableData3.ALIAS_NAME = '"+prevValue+"')";		
            
            Logger.out.debug("TABLE SQL*****************************"+sql);
            
            JDBCDAO jdbcDao = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
            jdbcDao.openSession(null);
            List list = jdbcDao.executeQuery(sql,null,Constants.INSECURE_RETRIEVE,null,null);
            jdbcDao.closeSession();
            
            //Adding NameValueBean of select option.
            NameValueBean nameValueBean = new NameValueBean();
            nameValueBean.setName(Constants.SELECT_OPTION);
            nameValueBean.setValue("-1");
            objectList.add(nameValueBean);
            
            //Adding the NameValueBean of previous selected object.
            JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
            jdbcDAO.openSession(null);
            
            sql = "select DISPLAY_NAME from CATISSUE_QUERY_INTERFACE_TABLE_DATA where ALIAS_NAME='"+prevValue+"'";
            List prevValueDisplayNameList = jdbcDAO.executeQuery(sql,null,Constants.INSECURE_RETRIEVE,null,null);
            jdbcDAO.closeSession();
            
            if (!prevValueDisplayNameList.isEmpty())
            {
                List rowList = (List)prevValueDisplayNameList.get(0);
                nameValueBean = new NameValueBean();
                nameValueBean.setName((String)rowList.get(0));
                nameValueBean.setValue(prevValue);
                objectList.add(nameValueBean);
            }
            
            List checkList = getCheckList();
            Iterator iterator = list.iterator();
            while (iterator.hasNext())
            {
                int j=0;
                List rowList = (List)iterator.next();
                if (checkForTable(rowList, checkList))
                {
                    nameValueBean = new NameValueBean();
                    nameValueBean.setValue((String)rowList.get(j++));
                    nameValueBean.setName((String)rowList.get(j));
                    objectList.add(nameValueBean);
                }
            }
            
            return objectList;
    }
    
    private List getCheckList() throws DAOException, ClassNotFoundException
    {
        String sql = " select TABLE_A.ALIAS_NAME, TABLE_A.DISPLAY_NAME " +
		 " from catissue_table_relation TABLE_R, " +
		 " CATISSUE_QUERY_INTERFACE_TABLE_DATA TABLE_A, " +
		 " CATISSUE_QUERY_INTERFACE_TABLE_DATA TABLE_B " +
		 " where TABLE_R.PARENT_TABLE_ID = TABLE_A.TABLE_ID and " +
		 " TABLE_R.CHILD_TABLE_ID = TABLE_B.TABLE_ID ";

		JDBCDAO jdbcDao = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List checkList = jdbcDao.executeQuery(sql,null,Constants.INSECURE_RETRIEVE,null,null);
		jdbcDao.closeSession();
		
		return checkList;
    }
    
    private boolean checkForTable(List rowList, List checkList)
    {
        String aliasName = (String)rowList.get(0), displayName = (String)rowList.get(1);
        Iterator iterator = checkList.iterator();
        while (iterator.hasNext())
        {
            List row = (List) iterator.next();
            if (aliasName.equals((String) row.get(0)) && displayName.equals((String) row.get(1)))
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Returns the display name of the aliasName passed.
     * @param value
     * @return
     * @throws DAOException
     * @throws ClassNotFoundException
     */
    public String getDisplayName(String aliasName) throws DAOException, ClassNotFoundException
    {
        String prevValueDisplayName = null;
        
        JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
        jdbcDAO.openSession(null);
        String sql = "select DISPLAY_NAME from CATISSUE_QUERY_INTERFACE_TABLE_DATA where ALIAS_NAME='"+aliasName+"'";
        List list = jdbcDAO.executeQuery(sql,null,Constants.INSECURE_RETRIEVE,null,null);
        jdbcDAO.closeSession();
        
        if (!list.isEmpty())
        {
            List rowList = (List)list.get(0);
            prevValueDisplayName = (String)rowList.get(0);
        }
        return prevValueDisplayName;
    }
    
     /**
 	* Returns all the tables in the simple query interface.
 	* @param request
 	* @throws DAOException
 	* @throws ClassNotFoundException
 	*/
	public Set getAllTableNames(String aliasName)throws DAOException, ClassNotFoundException
	{
    	String sql = " select distinct tableData.DISPLAY_NAME, tableData.ALIAS_NAME " +
    				 " from CATISSUE_TABLE_RELATION tableRelation join CATISSUE_QUERY_INTERFACE_TABLE_DATA " +
    				 " tableData on tableRelation.PARENT_TABLE_ID = tableData.TABLE_ID ";

		if ((aliasName != null) && (!"".equals(aliasName)))
		{
			sql = sql + " where tableData.ALIAS_NAME = '"+ aliasName +"'";
		}

		JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);
		List tableList = jdbcDAO.executeQuery(sql,null,Constants.INSECURE_RETRIEVE,null,null);
		jdbcDAO.closeSession();
		
		Set objectNameValueBeanList = new TreeSet();
		if (aliasName == null || "".equals(aliasName))
		{
		    NameValueBean nameValueBean = new NameValueBean();
		    nameValueBean.setValue("-1");
		    nameValueBean.setName(Constants.SELECT_OPTION);
		    objectNameValueBeanList.add(nameValueBean);
		}
		
		Iterator objIterator = tableList.iterator();
		while (objIterator.hasNext())
		{
			List row = (List) objIterator.next();
			NameValueBean nameValueBean = new NameValueBean();
		    nameValueBean.setName((String)row.get(0));
		    nameValueBean.setValue((String)row.get(1));
		    objectNameValueBeanList.add(nameValueBean);
		}
		
		return objectNameValueBeanList;
	}
    
	 /**
 	* Returns all the tables in the simple query interface.
 	* @param request
 	* @throws DAOException
 	* @throws ClassNotFoundException
 	*/
	public static Vector getMainObjectsOfQuery()throws DAOException
	{
    	String sql = " select distinct tableData.ALIAS_NAME " +
    				 " from CATISSUE_TABLE_RELATION tableRelation join CATISSUE_QUERY_INTERFACE_TABLE_DATA " +
    				 " tableData on tableRelation.PARENT_TABLE_ID = tableData.TABLE_ID ";

		
		
    	List list = null;
        java.util.Vector mainObjects = new java.util.Vector();
        JDBCDAO dao =null;
        try
        {
            dao = new JDBCDAO();
            dao.openSession(null);
            list = dao.executeQuery(sql, null, Constants.INSECURE_RETRIEVE, null,null);

            Iterator iterator = list.iterator();
            while (iterator.hasNext())
            {
                List row = (List) iterator.next();
                mainObjects.add(row.get(0));
                Logger.out.info("Main Objects:" +row.get(0));
            }

            
        }
        catch (DAOException daoExp)
        {
            Logger.out.debug("Could not obtain main objects. Exception:"
                    + daoExp.getMessage(), daoExp);
        }
        catch (ClassNotFoundException classExp)
        {
            Logger.out.debug("Could not obtain main objects. Exception:"
                    + classExp.getMessage(), classExp);
        }
        finally
        {
            try
            {
                dao.closeSession();
            }
            catch (DAOException e)
            {
                Logger.out.debug(e.getMessage(),e);
            }
        }
        return mainObjects;
	}
	
	/**
	 * This method returns all tables that are related to 
	 * the aliasname passed as parameter
	 * @author aarti_sharma
	 * @param aliasName
	 * @return
	 * @throws DAOException
	 */
    public static Vector getRelatedTableAliases(String aliasName)throws DAOException
    {
    	List list = null;
        java.util.Vector relatedTableAliases = new java.util.Vector();
        JDBCDAO dao =null;
        try
        {
            dao = new JDBCDAO();
            dao.openSession(null);
            list = dao.executeQuery(GET_RELATED_TABLE_ALIAS+"'"+aliasName+"'", null, Constants.INSECURE_RETRIEVE, null,null);

            Iterator iterator = list.iterator();
            while (iterator.hasNext())
            {
                List row = (List) iterator.next();
                relatedTableAliases.add(row.get(0));
                Logger.out.info("aliasName:"+aliasName+" Related Table: "+row.get(0));
            }

            
        }
        catch (DAOException daoExp)
        {
            Logger.out.debug("Could not obtain related tables. Exception:"
                    + daoExp.getMessage(), daoExp);
        }
        catch (ClassNotFoundException classExp)
        {
            Logger.out.debug("Could not obtain related tables. Exception:"
                    + classExp.getMessage(), classExp);
        }
        finally
        {
            try
            {
                dao.closeSession();
            }
            catch (DAOException e)
            {
                Logger.out.debug(e.getMessage(),e);
            }
        }
        return relatedTableAliases;
    }
    
}