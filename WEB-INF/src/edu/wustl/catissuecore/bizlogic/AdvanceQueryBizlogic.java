/*
 * Created on Nov 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.bizlogic;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.wustl.catissuecore.dao.JDBCDAO;
import edu.wustl.catissuecore.query.TreeNodeData;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author poornima_govindrao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AdvanceQueryBizlogic extends DefaultBizLogic implements TreeDataInterface
{
	//Creates a temporary table containing the Advance Query Search results given the AdvanceSearchQuery.
	public void createTempTable(String searchQuery,String tempTableName,SessionDataBean sessionData) throws Exception
	{
 		String sql = "Create table "+tempTableName+" as "+"("+searchQuery+")";
 		Logger.out.debug("sql for create table"+sql);
 		JDBCDAO jdbcDao = new JDBCDAO();
        jdbcDao.openSession(sessionData);
 		//jdbcDao.openSession(null);
       	jdbcDao.delete(tempTableName);
        jdbcDao.createTable(sql);
        jdbcDao.commit();
        jdbcDao.closeSession();
	}
	/* Get the data for tree view from temporary table and create tree nodes.
	 */
	public Vector getTreeViewData(SessionDataBean sessionData,Map columnIdsMap) throws DAOException
	{
		String tempTableName = Constants.QUERY_RESULTS_TABLE+"_"+sessionData.getUserId();
		JDBCDAO jdbcDao = new JDBCDAO();
        jdbcDao.openSession(sessionData);
        
        //Retrieve all the data from the temporary table 
		List dataList =  jdbcDao.retrieve(tempTableName);
		Logger.out.debug("List of data for identifiers:"+dataList);
		
		//Get column idetifiers from the map.
		int participantColumnId =  ((Integer)columnIdsMap.get(Constants.PARTICIPANT)).intValue()-1;
		int collectionProtocolColumnId =  ((Integer)columnIdsMap.get(Constants.COLLECTION_PROTOCOL)).intValue()-1;
		int specimenCollGrpColumnId =  ((Integer)columnIdsMap.get(Constants.SPECIMEN_COLLECTION_GROUP)).intValue()-1;
		int specimenColumnId = ((Integer)columnIdsMap.get(Constants.SPECIMEN)).intValue()-1;
        
		Vector vector = new Vector();
        Iterator iterator = dataList.iterator();
        
        //Create tree nodes
        while (iterator.hasNext())
        {
        	List rowList = (List)iterator.next();
        	//setQueryTreeNode((String) rowList.get(0),Constants.PARTICIPANT,null,null,vector);
			setQueryTreeNode((String) rowList.get(collectionProtocolColumnId), 
						Constants.COLLECTION_PROTOCOL,null,null,(String) rowList.get(participantColumnId), Constants.PARTICIPANT,vector);
			setQueryTreeNode((String) rowList.get(specimenCollGrpColumnId), Constants.SPECIMEN_COLLECTION_GROUP,(String) 
						rowList.get(collectionProtocolColumnId), Constants.COLLECTION_PROTOCOL,null,null,vector);
			setQueryTreeNode((String) rowList.get(specimenColumnId), Constants.SPECIMEN,(String)  
						rowList.get(specimenCollGrpColumnId),Constants.SPECIMEN_COLLECTION_GROUP,null,null,vector);
        }
        jdbcDao.closeSession();
        return vector;
	}   
	//Create TreeNode given the Tree node data.
	private void setQueryTreeNode(String identifier,String objectName,String parentIdentifier,String parentObjectName,String combinedParentIdentifier,String combinedParentObjectName,Vector vector)
	{
		TreeNodeData treeNode = new TreeNodeData();
        treeNode.setIdentifier(identifier);
        treeNode.setObjectName(objectName);
        treeNode.setParentIdentifier(parentIdentifier);
        treeNode.setParentObjectName(parentObjectName);
        treeNode.setCombinedParentIdentifier(combinedParentIdentifier);
        treeNode.setCombinedParentObjectName(combinedParentObjectName);

        vector.add(treeNode);
	}
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.TreeDataInterface#getTreeViewData()
	 */
	public Vector getTreeViewData() throws DAOException {

		return null;
	}
}
