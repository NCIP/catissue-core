/*
 * Created on Nov 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.catissuecore.dao.JDBCDAO;
import edu.wustl.catissuecore.query.AdvancedConditionsNode;
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
		int participantColumnId =  ((Integer)columnIdsMap.get(Constants.PARTICIPANT+"."+Constants.IDENTIFIER)).intValue()-1;
		int collectionProtocolColumnId =  ((Integer)columnIdsMap.get(Constants.COLLECTION_PROTOCOL+"."+Constants.IDENTIFIER)).intValue()-1;
		int specimenCollGrpColumnId =  ((Integer)columnIdsMap.get(Constants.SPECIMEN_COLLECTION_GROUP+"."+Constants.IDENTIFIER)).intValue()-1;
		int specimenColumnId = ((Integer)columnIdsMap.get(Constants.SPECIMEN+"."+Constants.IDENTIFIER)).intValue()-1;
        int parentSpecimenColumnId = ((Integer)columnIdsMap.get(Constants.SPECIMEN+"."+Constants.PARENT_SPECIMEN_ID_COLUMN)).intValue()-1;
		Vector vector = new Vector();
        Iterator iterator = dataList.iterator();
        List rowList = new ArrayList();
        String previousSpecimenId = new String();
        //int i=0;
        //Create tree nodes
        while (iterator.hasNext())
        {
        	//i++;
        	/*if(rowList.size()!=0)
        		previousSpecimenId = (String) rowList.get(specimenColumnId);*/
        	//Logger.out.debug("previousSpecimenId"+previousSpecimenId+":"+i);
            rowList = (List)iterator.next();
        	//setQueryTreeNode((String) rowList.get(0),Constants.PARTICIPANT,null,null,vector);
			setQueryTreeNode((String) rowList.get(collectionProtocolColumnId), 
						Constants.COLLECTION_PROTOCOL,null,null,(String) rowList.get(participantColumnId), Constants.PARTICIPANT,vector);
			setQueryTreeNode((String) rowList.get(specimenCollGrpColumnId), Constants.SPECIMEN_COLLECTION_GROUP,(String) 
						rowList.get(collectionProtocolColumnId), Constants.COLLECTION_PROTOCOL,null,null,vector);
			String parentSpecimenId = (String) rowList.get(parentSpecimenColumnId);
			Logger.out.debug("parentSpecimenId"+parentSpecimenId);
			if(parentSpecimenId.equals(""))
			{
				Logger.out.debug("parent specimen not present");
				setQueryTreeNode((String) rowList.get(specimenColumnId), Constants.SPECIMEN,(String)  
						rowList.get(specimenCollGrpColumnId),Constants.SPECIMEN_COLLECTION_GROUP,null,null,vector);
			}
			else
			{
				Logger.out.debug("parent specimen present");
				setQueryTreeNode((String) rowList.get(specimenColumnId), Constants.SPECIMEN,parentSpecimenId,
									Constants.SPECIMEN,null,null,vector);
			}
			
			
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
	
	//Recursive function to Traverse root and set tables in path
	public void setTables(DefaultMutableTreeNode tree,Set tableSet)throws DAOException, ClassNotFoundException
	{
		DefaultMutableTreeNode parent = new DefaultMutableTreeNode();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode();
		int childCount = tree.getChildCount();
		//Logger.out.debug("childCount"+childCount);
		for(int i=0;i<childCount;i++)
		{
			parent = (DefaultMutableTreeNode)tree.getChildAt(i);
			AdvancedConditionsNode parentAdvNode = (AdvancedConditionsNode)parent.getUserObject();
			String parentTable = parentAdvNode.getObjectName();
			tableSet.add(parentTable);
			//Set tablesInPath between parent & child if child exists.
			if(!parent.isLeaf())
			{
				child = (DefaultMutableTreeNode)parent.getFirstChild();
				AdvancedConditionsNode childAdvNode = (AdvancedConditionsNode)child.getUserObject();
				String childTable = childAdvNode.getObjectName();
				tableSet.add(childTable);
				QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory
												.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
				String parentTableId = bizLogic.getTableIdFromAliasName(parentTable);
				String childTableId = bizLogic.getTableIdFromAliasName(childTable);
				Set tablesInPath = bizLogic.setTablesInPath(Long.valueOf(parentTableId),Long.valueOf(childTableId));
				Logger.out.debug("tablesInPath after method call:"+tablesInPath);
				tableSet.addAll(tablesInPath);
			}
			setTables(parent,tableSet);
		}
	}
}
