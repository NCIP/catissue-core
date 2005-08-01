/**
 * <p>Title: ResultData Class>
 * <p>Description:	ResultData is used to generate the data required for the result view of query interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.catissuecore.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import edu.wustl.catissuecore.dao.AbstractDAO;
import edu.wustl.catissuecore.dao.DAOFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * ResultData is used to generate the data required for the result view of query interface.
 * @author gautam_shetty
 */
public class ResultData
{

    /**
     * Query results temporary table.
     */
    private String tmpResultsTableName = Constants.QUERY_RESULTS_TABLE;
    
    /**
     * Builds a JTree from the ResultSet passed.
     * @param rs The ResultSet object.
     * @return Returns the built JTree.
     * @throws SQLException
     */
    public Vector getTreeViewData() throws SQLException
    {
        String[] selectColumnName = Constants.DEFAULT_TREE_SELECT_COLUMNS;
        Vector dataList = new Vector();
        
        try
        {
            AbstractDAO dao = DAOFactory.getDAO(Constants.JDBC_DAO);
            dataList = (Vector)dao.retrieve(tmpResultsTableName, selectColumnName);
        }
        catch (Exception exp)
        {
        }

        return dataList;
    }

    public List getSpreadsheetViewData(String name, int id, String[] columnList)
    {
        
        String[] whereColumnName = {getColumnName(name)};
        String[] whereColumnCondition = {"="};
        String[] whereColumnValue = {String.valueOf(id)};
        
        if (name.equals(Constants.ROOT))
        {
            whereColumnName = null;
            whereColumnCondition = null;
            whereColumnValue = null;
        }
       
        List dataList = new ArrayList();
        
        try
        {
            AbstractDAO dao = DAOFactory.getDAO(Constants.JDBC_DAO);
            List list = dao.retrieve(tmpResultsTableName,columnList,
                    				 whereColumnName,whereColumnCondition,
                    				 whereColumnValue,null);
            
            if (list.size() != 0)
            {
                ResultSet resultSet = (ResultSet)list.get(0);
                while (resultSet.next())
                {
                    int i = 0;
                    String[] columnData = new String[resultSet.getFetchSize()];
                    
                    while (i < columnList.length)
                    {
                        columnData[i] = new String(resultSet.getString(columnList[i]));
                        i++;
                    }
                    dataList.add(columnData);
                }
            }
        }
        catch (SQLException sqlExp)
        {
            Logger.out.error(sqlExp.getMessage(),sqlExp);
        }
        catch (Exception exp)
        {
            Logger.out.error(exp.getMessage(),exp);
        }
        
        return dataList;
    }
    
    public String getColumnName(String name)
    {
        String columnName = null;
        
        if (name.equals(Constants.PARTICIPANT))
        {
            columnName = Constants.PARTICIPANT_ID_COLUMN;
        }else if (name.equals(Constants.ACCESSION))
        {
            columnName = Constants.ACCESSION_ID_COLUMN;
        }else if (name.equals(Constants.SPECIMEN))
        {
            columnName = Constants.SPECIMEN_ID_COLUMN;
        }else if (name.equals(Constants.SEGMENT))
        {
            columnName = Constants.SEGMENT_ID_COLUMN;
        }else if (name.equals(Constants.SAMPLE))
        {
            columnName = Constants.SAMPLE_ID_COLUMN;
        }
        
        return columnName;
    }
}