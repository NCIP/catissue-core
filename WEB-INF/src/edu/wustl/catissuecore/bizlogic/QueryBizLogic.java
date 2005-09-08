/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.dao.JDBCDAO;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.catissuecore.query.Operator;
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.Relation;
import edu.wustl.catissuecore.query.RelationCondition;
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
    
    private static final String GET_RELATION_DATA=
        "select PARENT_TABLE_FIELD_ID, CHILD_TABLE_FIELD_ID " +
    		"from CATISSUE_TABLE_RELATION";
    
    private static final String GET_COLUMN_DATA=
        "select ALIAS_NAME,COLUMN_NAME from CATISSUE_QUERY_INTERFACE_COLUMN_DATA columnData, " +
        "CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData where columnData.TABLE_ID = tableData.TABLE_ID  " +
        "and columnData.IDENTIFIER=";

    public static HashMap getQueryObjectNameTableNameMap()
    {
        List list = null;
        HashMap queryObjectNameTableNameMap = new HashMap();
        JDBCDAO dao =null;
        try
        {
            dao = new JDBCDAO();
            dao.openSession();
            list = dao.executeQuery(ALIAS_NAME_TABLE_NAME_MAP_QUERY);

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
            dao.openSession();
            list = dao.executeQuery(GET_RELATION_DATA);

            Iterator iterator = list.iterator();
            
            while (iterator.hasNext())
            {
                row = (List) iterator.next();
                parentTableColumnID = (String) row.get(0);
                childTableColumnID = (String) row.get(1);
                if(Integer.parseInt(parentTableColumnID) ==0 || Integer.parseInt(childTableColumnID) ==0)
                {
                    continue;
                }
                
                columnDataList = dao.executeQuery(GET_COLUMN_DATA+parentTableColumnID);
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
                columnName1 = (String)row.get(1);
                
                columnDataList = dao.executeQuery(GET_COLUMN_DATA+childTableColumnID);
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
                columnName2 = (String)row.get(1);
                
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
    
   

}