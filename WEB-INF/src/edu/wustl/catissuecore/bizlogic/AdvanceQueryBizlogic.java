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
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.catissuecore.dao.JDBCDAO;
import edu.wustl.catissuecore.query.AdvancedConditionsNode;
import edu.wustl.catissuecore.query.Condition;
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
	public void createTempTable(String searchQuery,String tempTableName,SessionDataBean sessionData,Map queryResultObjectDataMap) throws Exception
	{
 		String sql = "Create table "+tempTableName+" as "+"("+searchQuery+" AND 1!=1)";
		//String sql = "Create table "+tempTableName+" as "+"("+searchQuery+")";
 		Logger.out.debug("sql for create table"+sql);
 		JDBCDAO jdbcDao = new JDBCDAO();
        jdbcDao.openSession(sessionData);

        //Delete if there is any tbale existing with same name
       	jdbcDao.delete(tempTableName);
       	//Create empty temporary table
        jdbcDao.createTable(sql);

        //Insert list of data into the temporary table created.
        List dataList = jdbcDao.executeQuery(searchQuery,sessionData,true,queryResultObjectDataMap);
        Logger.out.debug("full list to be inserted"+dataList);
        Iterator dataListItr = dataList.iterator();
        while(dataListItr.hasNext())
        {
        	List rowList = (List)dataListItr.next();
        	Logger.out.debug("list size to be inserted"+rowList.size()+":"+rowList);
        	jdbcDao.insert(tempTableName,rowList);
        }
        jdbcDao.commit();
        jdbcDao.closeSession();
	}
	/* Get the data for tree view from temporary table and create tree nodes.
	 */
	public Vector getTreeViewData(SessionDataBean sessionData,Map columnIdsMap,List disableSpecimenIdsList) throws DAOException,ClassNotFoundException
	{
		String tempTableName = Constants.QUERY_RESULTS_TABLE+"_"+sessionData.getUserId();
		JDBCDAO jdbcDao = new JDBCDAO();
        jdbcDao.openSession(sessionData);
        Logger.out.debug("Temp table in adv bizlogic:"+tempTableName);

        //Retrieve all the data from the temporary table 
		List dataList =  jdbcDao.retrieve(tempTableName);
		jdbcDao.closeSession();
		Logger.out.debug("List of data for identifiers:"+dataList);
		
		//Get column idetifiers from the column ids map.
		int participantColumnId =  ((Integer)columnIdsMap.get(Constants.PARTICIPANT+"."+Constants.IDENTIFIER)).intValue()-1;
		int collectionProtocolColumnId =  ((Integer)columnIdsMap.get(Constants.COLLECTION_PROTOCOL+"."+Constants.IDENTIFIER)).intValue()-1;
		int specimenCollGrpColumnId =  ((Integer)columnIdsMap.get(Constants.SPECIMEN_COLLECTION_GROUP+"."+Constants.IDENTIFIER)).intValue()-1;
		int specimenColumnId = ((Integer)columnIdsMap.get(Constants.SPECIMEN+"."+Constants.IDENTIFIER)).intValue()-1;
        int parentSpecimenColumnId = ((Integer)columnIdsMap.get(Constants.SPECIMEN+"."+Constants.PARENT_SPECIMEN_ID_COLUMN)).intValue()-1;

        Vector treeDataVector = new Vector();
        Iterator dataListIterator = dataList.iterator();
        List rowList = new ArrayList();
        List disabledSpecimenIds = new ArrayList();
        
        //Create tree nodes
        while (dataListIterator.hasNext())
        {
            rowList = (List)dataListIterator.next();
        	//setQueryTreeNode((String) rowList.get(0),Constants.PARTICIPANT,null,null,vector);
            //Create tree nodes for Participant & Collection Protocol
			setQueryTreeNode((String) rowList.get(collectionProtocolColumnId), 
						Constants.COLLECTION_PROTOCOL,null,null,(String) rowList.get(participantColumnId), Constants.PARTICIPANT,treeDataVector);
			//Create tree nodes for Specimen Collection Group
			setQueryTreeNode((String) rowList.get(specimenCollGrpColumnId), Constants.SPECIMEN_COLLECTION_GROUP,(String) 
						rowList.get(collectionProtocolColumnId), Constants.COLLECTION_PROTOCOL,(String) rowList.get(participantColumnId),Constants.PARTICIPANT,treeDataVector);
			String parentSpecimenId = (String) rowList.get(parentSpecimenColumnId);
			Logger.out.debug("parentSpecimenId"+parentSpecimenId);
			//if parent specimen not present, form tree node directly under Specimen Collection group
			if(parentSpecimenId.equals("")||parentSpecimenId.equals("0"))
			{
				Logger.out.debug("parent specimen not present");
				setQueryTreeNode((String) rowList.get(specimenColumnId), Constants.SPECIMEN,(String)  
						rowList.get(specimenCollGrpColumnId),Constants.SPECIMEN_COLLECTION_GROUP,null,null,treeDataVector);
			}
			//if parent specimen id is present for the specimen heirarchy
			else
			{
				String specimenId= (String) rowList.get(specimenColumnId);
				//get the specimen heirarchy above the current specimen
				List specimenIdsHeirarchy = getSpecimenHeirarchy(specimenId);
				specimenIdsHeirarchy.add(specimenId);
				
				Logger.out.debug("list of parent specimen ids:"+specimenIdsHeirarchy);
				if(specimenIdsHeirarchy.size()>0)
				{
					setQueryTreeNode((String)specimenIdsHeirarchy.get(0), Constants.SPECIMEN,(String)
							rowList.get(specimenCollGrpColumnId),Constants.SPECIMEN_COLLECTION_GROUP,null,null,treeDataVector);
					
					Iterator specimenIdsHeirarchyItr= specimenIdsHeirarchy.iterator();
					String parentSpecimenIdInHeirarchy = (String)specimenIdsHeirarchyItr.next();
					while(specimenIdsHeirarchyItr.hasNext())
					{
						specimenId = (String)specimenIdsHeirarchyItr.next();
						setQueryTreeNode(specimenId, Constants.SPECIMEN,parentSpecimenIdInHeirarchy,Constants.SPECIMEN,null,null,treeDataVector);
						parentSpecimenIdInHeirarchy = specimenId;
					}
				}
				//Find the specimen ids which are not satisfying the query condition to disbale in tree view.
				//get the specimen ids present in temp table
				List parentSpecimenIdsInTempTable = getParentSpecimensInTempTable(Constants.COLUMN+specimenColumnId,tempTableName); 
				//list of specimens not satisfying the query but are shown in results tree view.
				// These specimen ids have to be disabled.
				specimenIdsHeirarchy.removeAll(parentSpecimenIdsInTempTable);
				disabledSpecimenIds.addAll(specimenIdsHeirarchy);
				Logger.out.debug("Specimen ids to be disabled in tree view"+specimenIdsHeirarchy);
			}
			Logger.out.debug("Tree Data:"+rowList.get(participantColumnId)+" "+rowList.get(collectionProtocolColumnId)+" "+
					rowList.get(specimenCollGrpColumnId)+" "+rowList.get(parentSpecimenColumnId)+" "+ rowList.get(specimenColumnId));
        }
        disableSpecimenIdsList.addAll(disabledSpecimenIds);
        Logger.out.debug("vector of tree nodes"+treeDataVector);
        return treeDataVector;
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
			if(parentTable.equals(Constants.SPECIMEN))
			{
				AdvancedConditionsNode specimenAdvNode = (AdvancedConditionsNode)parent.getUserObject();
				Vector conditions = specimenAdvNode.getObjectConditions();
				Iterator conditionsItr = conditions.iterator();
				while(conditionsItr.hasNext())
				{
					Condition condition = (Condition)conditionsItr.next();
					String table = condition.getDataElement().getTableAliasName();
					StringTokenizer tableTokens = new StringTokenizer(table,".");
					if(tableTokens.countTokens()==2)
					{
						String objectName = tableTokens.nextToken();
						String tableName = tableTokens.nextToken();
						//tableSet.add(objectName);
						condition.getDataElement().setTableName(tableName);
					}
				}
			}
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
	//Get the specimen Ids heirarchy above a given specimen ids.
	private List getSpecimenHeirarchy(String specimenId) throws DAOException,ClassNotFoundException
	{
		List specimenIdsList = new ArrayList();
		List specimenIdsListInOrder = new ArrayList();
		JDBCDAO jdbcDao = new JDBCDAO();
        jdbcDao.openSession(null);
		Long whereColumnValue=new Long(specimenId);
		String sql =new String(); 
		while(whereColumnValue!=null)
		{
			sql = "Select PARENT_SPECIMEN_ID from CATISSUE_SPECIMEN where IDENTIFIER = "+whereColumnValue;
			List dataList =  jdbcDao.executeQuery(sql,null,false,null);
			Logger.out.debug("list size in speci heirarchy:"+dataList.size()+" "+dataList);
			List rowList = (List)dataList.get(0);
			if(!rowList.get(0).equals(""))
			{
				whereColumnValue = new Long((String)rowList.get(0));
				specimenIdsList.add(whereColumnValue.toString());
			}
			else
				whereColumnValue=null;
			
		}
		//Order the specimen ids in the order which the tree nodes have to be created
		Logger.out.debug("List specimenIdsList before order"+specimenIdsList);
		for(int i=specimenIdsList.size()-1;i>=0;i--)
		{
			specimenIdsListInOrder.add(specimenIdsList.get(i));
			
		}
		Logger.out.debug("List specimenIdsList after order"+specimenIdsListInOrder);
		jdbcDao.closeSession();
		return specimenIdsListInOrder;
	}
	
	//Get all the specimen ids in temp table to check the existence of all the specimens of the heirarchy 
	//in the temp table
	private List getParentSpecimensInTempTable(String specimenColumnId,String tempTable) throws DAOException,ClassNotFoundException
	{
		List parentSpecimenIdsList = new ArrayList();
		
		JDBCDAO jdbcDao = new JDBCDAO();
        jdbcDao.openSession(null);
		String sql = "Select "+specimenColumnId+" from "+tempTable;
		List dataList =  jdbcDao.executeQuery(sql,null,false,null);
		Logger.out.debug("list size in speci in temp table:"+dataList.size()+" "+dataList);
		Iterator dataListItr = dataList.iterator();
		while(dataListItr.hasNext())
		{
			List rowList = (List)dataListItr.next();
			parentSpecimenIdsList.add(rowList.get(0));
		}
		jdbcDao.closeSession();
		return parentSpecimenIdsList;
	}
}
