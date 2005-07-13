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
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

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
        JTree queryTree = null;
        Vector dataList = new Vector();
        
        try
        {
            AbstractDAO dao = DAOFactory.getDAO(Constants.JDBC_DAO);
            List list = dao.retrieve(tmpResultsTableName, selectColumnName);
            ResultSet resultSet = null;
            
            if (list.size() != 0)
            {
                resultSet = (ResultSet) list.get(0);
                while (resultSet.next())
                {
                    int i = 0;
                    String[] columnData = new String[resultSet.getFetchSize()];
                    System.out.println();
                    while (i < selectColumnName.length)
                    {
                        columnData[i] = new String(resultSet.getString(selectColumnName[i]));
                        i++;
                    }
                    dataList.add(columnData);
                }
            }
        }
        catch (Exception exp)
        {
        }

        return dataList;
    }

    /**
     * Returns a tree node which is child of the “ParentNode”, 
     * if the data associated with it is same as “treeNodeData”, else returns null.
     * @param parentNode The parent node.
     * @param treeNodeData The node to be checked.
     * @return Returns the node if the data associated with it is same as treeNodeData, else returns null.
     */
    private DefaultMutableTreeNode getChildNode(
            DefaultMutableTreeNode parentNode, TreeNodeData treeNodeData)
    {
        DefaultMutableTreeNode node = null;
        try
        {
            for (int i = 0; i < parentNode.getChildCount(); i++)
            {
                node = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                String nodeData = (String) node.getUserObject();
//                TreeNodeData nodeData = (TreeNodeData)node.getUserObject();
                
                if (getId(nodeData) == treeNodeData.getId())
                {
                    return node;
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException exp)
        {
            return null;
        }
        
        return null;
    }

    /**
     * Adds the treeNodeData as the child of the parentNode.
     * @param parentNode The parent node.
     * @param treeNodeData The child node to be added.
     * @return Returns the child node which is added.
     */
    private DefaultMutableTreeNode addChildNode(
            DefaultMutableTreeNode parentNode, TreeNodeData treeNodeData)
    {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(treeNodeData.toString());
        DefaultMutableTreeNode d = (DefaultMutableTreeNode) parentNode;
        d.add(childNode);
        return childNode;
    }

    /**
     * Retuns the id of the field(Participant,Accession,Specimen,Segment,Sample) 
     * @param fieldName The field whose id is to be found out.
     * @return The id of the fields.
     */
    private long getId(String fieldName)
    {
        StringTokenizer str = new StringTokenizer(fieldName, ":");
        str.nextToken();
        String id = str.nextToken();
        return Long.parseLong(id);
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