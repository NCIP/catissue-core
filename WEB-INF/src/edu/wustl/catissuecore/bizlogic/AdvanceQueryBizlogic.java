/*
 * Created on Nov 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.querysuite.bizlogic.QueryBizLogic;
import edu.wustl.common.querysuite.queryobject.impl.Condition;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.tree.TreeDataInterface;
import edu.wustl.common.tree.TreeNode;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.simplequery.query.Operator;
import edu.wustl.simplequery.query.Table;
/**
 * @author poornima_govindrao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AdvanceQueryBizlogic extends DefaultBizLogic implements TreeDataInterface
{ 
	//Creates a temporary table containing the Advance Query Search results given the AdvanceSearchQuery.
	public int createTempTable(String searchQuery,String tempTableName,SessionDataBean sessionData,Map queryResultObjectDataMap, boolean hasConditionOnIdentifiedField) throws Exception
	{ 
 		String sql = "Create table "+tempTableName+" as "+"("+searchQuery+" AND 1!=1)";
		//String sql = "Create table "+tempTableName+" as "+"("+searchQuery+")";
 		Logger.out.debug("sql for create table"+sql);
 		JDBCDAO jdbcDao = (JDBCDAO)DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
        jdbcDao.openSession(sessionData);
        int noOfRecords = 0;
        List dataList =null;
        try
		{
	        //Delete if there is any tbale existing with same name
	       	jdbcDao.delete(tempTableName);
	       	//Create empty temporary table
	        jdbcDao.createTable(sql);
	
	        //Insert list of data into the temporary table created.
	        dataList = jdbcDao.executeQuery(searchQuery,sessionData,true,hasConditionOnIdentifiedField,queryResultObjectDataMap);	        
	        Iterator dataListItr = dataList.iterator();
	        while(dataListItr.hasNext())
	        {
	        	List rowList = (List)dataListItr.next();
//	        	Logger.out.debug("list size to be inserted"+rowList.size()+":"+rowList);
	        	jdbcDao.insert(tempTableName,rowList);
	        }
	        jdbcDao.commit();
	        noOfRecords = dataList.size();
		}
        catch(Exception e)
		{
        	Logger.out.error("Exception occured in createTempTable method of AdvanceQueryBizlogic: " + e.getMessage() , e);
        	Logger.out.error("Sql for creating temporary table : " + sql);
        	Logger.out.error("Search query being executed : " + searchQuery);  
        	Logger.out.error("Full list to be inserted : " + dataList);
        	throw e;
		}
		finally
		{
			jdbcDao.closeSession();
		}
        return noOfRecords;
	}
	
	/* Get the data for tree view from temporary table and create tree nodes.
	 */
	public Vector getTreeViewData(SessionDataBean sessionData,Map columnIdsMap,List disableSpecimenIdsList) throws DAOException,ClassNotFoundException
	{
		String tempTableName = Constants.QUERY_RESULTS_TABLE+"_"+sessionData.getUserId();
		JDBCDAO jdbcDao = (JDBCDAO)DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		List dataList=null; 
		jdbcDao.openSession(sessionData);
		Logger.out.debug("Temp table in adv bizlogic:"+tempTableName);
		try
		{
	        //Retrieve all the data from the temporary table
			dataList =  jdbcDao.retrieve(tempTableName);
		}
		catch (DAOException excp)
		{
			Logger.out.error(excp.getMessage(),excp);
			throw excp;
		}
		finally
		{
	        jdbcDao.closeSession();
		}

		Logger.out.debug("List of data for identifiers:"+dataList);
		
		//Get column idetifiers from the column ids map.
		int participantColumnId =  ((Integer)columnIdsMap.get(Constants.PARTICIPANT+"."+Constants.IDENTIFIER)).intValue()-1;
		int collectionProtocolColumnId =  ((Integer)columnIdsMap.get(Constants.COLLECTION_PROTOCOL+"."+Constants.IDENTIFIER)).intValue()-1;
		int specimenCollGrpColumnId =  ((Integer)columnIdsMap.get(Constants.SPECIMEN_COLLECTION_GROUP+"."+Constants.IDENTIFIER)).intValue()-1;
		int specimenColumnId = ((Integer)columnIdsMap.get(Constants.SPECIMEN+"."+Constants.IDENTIFIER)).intValue()-1;
        int parentSpecimenColumnId = ((Integer)columnIdsMap.get(Constants.SPECIMEN+"."+Constants.PARENT_SPECIMEN_ID_COLUMN)).intValue()-1;
        
        int participantFisrtnameColumnId =  ((Integer)columnIdsMap.get(Constants.PARTICIPANT+"."+Constants.PARTICIPANT_FIRST_NAME)).intValue()-1;
        int participantLastnameColumnId =  ((Integer)columnIdsMap.get(Constants.PARTICIPANT+"."+Constants.PARTICIPANT_LAST_NAME)).intValue()-1;
		int collectionProtocolNameColumnId =  ((Integer)columnIdsMap.get(Constants.SPECIMEN_PROTOCOL+"."+Constants.SPECIMEN_PROTOCOL_SHORT_TITLE)).intValue()-1;
		int specimenCollGrpNameColumnId =  ((Integer)columnIdsMap.get(Constants.SPECIMEN_COLLECTION_GROUP+"."+Constants.SPECIMEN_COLLECTION_GROUP_NAME)).intValue()-1;
		int specimenLableColumnId = ((Integer)columnIdsMap.get(Constants.SPECIMEN+"."+Constants.SPECIMEN_LABEL)).intValue()-1;
		
        Vector treeDataVector = new Vector();
        Iterator dataListIterator = dataList.iterator();
        List rowList = new ArrayList();
        List disabledSpecimenIds = new ArrayList();
        
        //Create tree nodes
        while (dataListIterator.hasNext())
        {
            rowList = (List)dataListIterator.next();
            
            String participantName = rowList.get(participantFisrtnameColumnId)+" " +rowList.get(participantLastnameColumnId);
            if (participantName.trim().equals(""))
            	participantName = "NA";
            setQueryTreeNode((String) rowList.get(participantColumnId),
					Constants.PARTICIPANT, participantName, null, null, null, null,treeDataVector);
        	
            //Create tree nodes for Participant & Collection Protocol
			setQueryTreeNode((String) rowList.get(collectionProtocolColumnId), 
						Constants.COLLECTION_PROTOCOL, (String) rowList.get(collectionProtocolNameColumnId),(String) rowList.get(participantColumnId),Constants.PARTICIPANT,
						null, null, treeDataVector);
			
			//Create tree nodes for Specimen Collection Group
			setQueryTreeNode((String) rowList.get(specimenCollGrpColumnId), Constants.SPECIMEN_COLLECTION_GROUP, (String) rowList.get(specimenCollGrpNameColumnId),
			                (String) rowList.get(collectionProtocolColumnId), Constants.COLLECTION_PROTOCOL,
						    (String) rowList.get(participantColumnId),Constants.PARTICIPANT,treeDataVector);
			
			String parentSpecimenId = (String) rowList.get(parentSpecimenColumnId);
			Logger.out.debug("parentSpecimenId"+parentSpecimenId);
			
			//if parent specimen not present, form tree node directly under Specimen Collection group
			if(parentSpecimenId.equals("")||parentSpecimenId.equals("0"))
			{
				Logger.out.debug("parent specimen not present");
				setQueryTreeNode((String) rowList.get(specimenColumnId), Constants.SPECIMEN,(String) rowList.get(specimenLableColumnId), 
						(String)rowList.get(specimenCollGrpColumnId),Constants.SPECIMEN_COLLECTION_GROUP,null,null,treeDataVector);
			}
			//if parent specimen id is present for the specimen hierarchy
			else
			{
				String specimenId= (String) rowList.get(specimenColumnId);
				//get the specimen heirarchy above the current specimen
				List specimenIdsHeirarchy = getSpecimenHeirarchy(specimenId);
				specimenIdsHeirarchy.add(specimenId);
				
				 /** Name : Aarti Sharma
				 * Reviewer: Sachin Lale
				 * Bug ID: 4785
				 * Desciption: Tree view of advanced query search results was showing incorrect labels for specimen 
				 * since for parent specimen wrong labels of children were getting set. Now labels for all specimen in heirarchy
				 * are being specifically obtained and set
				 */
				List specimenLabels = getSpecimenLabels(specimenIdsHeirarchy);
				
				Logger.out.debug("list of parent specimen ids:"+specimenIdsHeirarchy);
				if(specimenIdsHeirarchy.size()>0)
				{
					setQueryTreeNode((String)specimenIdsHeirarchy.get(0), Constants.SPECIMEN,(String)specimenLabels.get(0),
							(String) rowList.get(specimenCollGrpColumnId),Constants.SPECIMEN_COLLECTION_GROUP,null,null,treeDataVector);
					
					Iterator specimenIdsHeirarchyItr= specimenIdsHeirarchy.iterator();
					String parentSpecimenIdInHeirarchy = (String)specimenIdsHeirarchyItr.next();
					int j=1;
					while(specimenIdsHeirarchyItr.hasNext())
					{
						specimenId = (String)specimenIdsHeirarchyItr.next();
						setQueryTreeNode(specimenId, Constants.SPECIMEN,(String) specimenLabels.get(j++), parentSpecimenIdInHeirarchy,Constants.SPECIMEN,null,null,treeDataVector);
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
        
        return createHierarchy(treeDataVector);
	} 
	
	/**
	 * This method returns the list of lables of all the specimens in specimenIdsHeirarchy.
	 * Returned Labels are in the same order as specimens sent
	 * @author aarti_sharma
	 * @param specimenIdsHeirarchy
	 * @return List of specimen labels
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private List getSpecimenLabels(List specimenIdsHeirarchy) throws DAOException, ClassNotFoundException
	{
		List specimenLabels = new ArrayList();
		String sql; 
		StringBuffer specimenIds = new StringBuffer();
		
		/*
		 * Form a string of all specimens in the list sent
		 */
		for(int i=0; i<specimenIdsHeirarchy.size()-1;i++)
		{
			specimenIds.append(specimenIdsHeirarchy.get(i)+",");
		}
		specimenIds.append(specimenIdsHeirarchy.get(specimenIdsHeirarchy.size()-1));
		
		/*
		 * Fire a query to find labels of all specimens in the list sent
		 */
		JDBCDAO jdbcDao = (JDBCDAO)DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
        jdbcDao.openSession(null);
		sql = "Select IDENTIFIER,LABEL from CATISSUE_SPECIMEN where IDENTIFIER IN ("+specimenIds.toString()+")";
		List dataList =  jdbcDao.executeQuery(sql,null,false,null);
		jdbcDao.closeSession();
		
		/*
		 * Put the labels in a map where key is specimen identifier and label is the value
		 */
		List rowList;
		Map specimenLabelsMap = new HashMap();
		for(int i=0; i<dataList.size();i++)
		{
			rowList = (List)dataList.get(i);
			specimenLabelsMap.put((String)rowList.get(0), (String)rowList.get(1));
		}
		
		/*
		 * Extract specimen labels from  the map formed in previous step and insert them in a list
		 * in the same order as of the specimens in specimenIdsHeirarchy
		 */
		for(int i=0; i< specimenIdsHeirarchy.size();i++)
		{
			specimenLabels.add(specimenLabelsMap.get(specimenIdsHeirarchy.get(i)));
		}
		
		return specimenLabels;
	}

	/**
	 * Creates and returns the vector of AdvanceQueryTreeNode nodes from the QueryTreeNodeData nodes passed. 
	 * @param oldNodes Vector of QueryTreeNodeData nodes.
	 * @return the vector of AdvanceQueryTreeNode nodes from the QueryTreeNodeData nodes passed.
	 */
	
	private Vector createHierarchy(Vector oldNodes)
	{
	    Vector finalTreeNodes = new Vector();
	    
	    Iterator iterator = oldNodes.iterator();
	    while (iterator.hasNext())
	    {
	        QueryTreeNodeData treeNode = (QueryTreeNodeData) iterator.next();
	        AdvanceQueryTreeNode treeNodeImpl = new AdvanceQueryTreeNode(Long.valueOf((String)treeNode.getIdentifier()),
	                									 treeNode.getObjectName(), treeNode.getDisplayName());
	        
	        //If the parent is null, the node is of participant. Add it in the vector.
	        if (treeNode.getParentIdentifier() == null && treeNode.getCombinedParentIdentifier() == null)
	        {
	            if (finalTreeNodes.contains(treeNodeImpl) == false)
	            {
	                finalTreeNodes.add(treeNodeImpl);
	            }
	            continue;
	        }
	        
	        //The parent node of this node.
	        AdvanceQueryTreeNode parentTreeNode = new AdvanceQueryTreeNode();
	        if (treeNode.getParentIdentifier() != null)
	        {
	            parentTreeNode.setIdentifier(Long.valueOf((String)treeNode.getParentIdentifier()));
	            parentTreeNode.setValue(treeNode.getParentObjectName());
	            parentTreeNode.setDisplayName(treeNode.getDisplayName());
	        }
	        
	        //In case of specimen colleciton group, the participant node is also required.
	        //So creating the parent of the parent node.
	        AdvanceQueryTreeNode parentOfParentNode = new AdvanceQueryTreeNode();
	        if (treeNode.getCombinedParentIdentifier() != null)
	        {
	            parentOfParentNode.setIdentifier(Long.valueOf((String)treeNode.getCombinedParentIdentifier()));
	            parentOfParentNode.setValue(treeNode.getCombinedParentObjectName());
	            parentTreeNode.setDisplayName(treeNode.getDisplayName());
	            parentTreeNode.setParentNode(parentOfParentNode);
	        }
	        
	        //get the parent node from the final tree node vector.
	        parentTreeNode = (AdvanceQueryTreeNode) getNode(finalTreeNodes, parentTreeNode);
	        if (parentTreeNode != null)
	        {
	            treeNodeImpl.setParentNode(parentTreeNode);
	            if (parentTreeNode.getChildNodes().contains(treeNodeImpl) == false)
	            {
	                parentTreeNode.getChildNodes().add(treeNodeImpl);
	            }
	        }
	    }
	    Utility.sortTreeVector(finalTreeNodes);
	    return finalTreeNodes;
	}
	
	/**
	 * Searches and returns the given node in the vector fo nodes and its child nodes. 
	 * @param treeNodeVector the vector of tree nodes. 
	 * @param treeNode the node to be searched.
	 * @return the node equal to the given node.
	 */
	private TreeNode getNode(Vector treeNodeVector, AdvanceQueryTreeNode treeNode)
	{
	    //The node equal to the given node.
	    AdvanceQueryTreeNode returnNode = null;
	    
	    Iterator treeNodeVectorIterator = treeNodeVector.iterator();
	    while (treeNodeVectorIterator.hasNext())
	    {
	        AdvanceQueryTreeNode treeNodeImpl = (AdvanceQueryTreeNode) treeNodeVectorIterator.next();
	        
	        //If the node is not equal to collection protocol, check only the nodes and not their parents.
	        if (treeNode.getValue().equals(Constants.COLLECTION_PROTOCOL) == false)
	        {
	            if (treeNodeImpl.getIdentifier().equals(treeNode.getIdentifier()) && treeNodeImpl.getValue().equals(treeNode.getValue()))
		        {
		            return treeNodeImpl;
		        }
	        }
	        else //In case of collection protocol, check the nodes as well as their parent nodes.
	        {
                if (treeNodeImpl.getParentNode() != null)
                {
                    AdvanceQueryTreeNode parentNode = (AdvanceQueryTreeNode)treeNode.getParentNode();
		            if ((parentNode.getIdentifier().equals(((AdvanceQueryTreeNode)treeNodeImpl.getParentNode()).getIdentifier())
		                    && parentNode.getValue().equals(((AdvanceQueryTreeNode)treeNodeImpl.getParentNode()).getValue()))
		                && (treeNode.getIdentifier().equals(treeNodeImpl.getIdentifier()) 
		                    && treeNode.getValue().equals(treeNodeImpl.getValue())))
		            {
		            	return treeNodeImpl;
		            }
	            }
	        }
	        
	        returnNode = (AdvanceQueryTreeNode) getNode(treeNodeImpl.getChildNodes(), treeNode);
	        if (returnNode != null)
	            break;
	    }
	    
	    return returnNode;
	}
	
    //Create QueryTreeNode given the Tree node data.
	private void setQueryTreeNode(String identifier,String objectName, String displayName,String parentIdentifier,String parentObjectName,String combinedParentIdentifier,String combinedParentObjectName,Vector vector)
	{
		QueryTreeNodeData treeNode = new QueryTreeNodeData();
        treeNode.setIdentifier(identifier);
        treeNode.setObjectName(objectName);
        treeNode.setDisplayName(displayName);
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
		//Set eventParametersTables=new HashSet();
		/*Set all the tables selected in the condition.
		 * Set all the tables in path between parent-child in the Advance query root object.
		 * Sets all the tables in path between AdvanceCOnditionNode Object name and its related tables.    
		 */
		DefaultMutableTreeNode parent = new DefaultMutableTreeNode();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode();
		QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory
		.getInstance().getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
		
		int childCount = tree.getChildCount();
		//Logger.out.debug("childCount"+childCount);
		for(int i=0;i<childCount;i++)
		{
			parent = (DefaultMutableTreeNode)tree.getChildAt(i);
			AdvancedConditionsNode parentAdvNode = (AdvancedConditionsNode)parent.getUserObject();
			String parentTable = parentAdvNode.getObjectName();
			String parentTableId = bizLogic.getTableIdFromAliasName(parentTable);
			Logger.out.debug("parentTableId"+parentTableId);

			AdvancedConditionsNode advNode = (AdvancedConditionsNode)parent.getUserObject();
			Vector conditions = advNode.getObjectConditions();
			
			
			Table table;
			String tableId=new String();
			/*incase of Specimen conditions the specimen event paramters object names will have the all the tables in path in the object name
			 * Check if there are more than 1 table, tokenize it and set proper table in DataElement
			 * for eg: if CellSpecimenReviewParameters condition is selected for Specimen Id column then the 
			 * object name will be CellSpecimenReviewParameters.SpecimenEventParamters
			 */
			
			if(parentTable.equals(Constants.SPECIMEN))
			{
				Iterator conditionsItr = conditions.iterator();
				while(conditionsItr.hasNext())
				{
					Condition condition = (Condition)conditionsItr.next();
					table = condition.getDataElement().getTable();
					String tableName = condition.getDataElement().getTableAliasName();
					Logger.out.debug("table in specimen condition..."+tableName);
					StringTokenizer tableTokens = new StringTokenizer(tableName,".");
					if(tableTokens.countTokens()==2)
					{
						String firstTableName = tableTokens.nextToken();
						tableName = tableTokens.nextToken();
//						Logger.out.debug("table in specimen condition token1..."+objectName);
						Logger.out.debug("table in specimen condition  token2..."+firstTableName);
//						eventParametersTables.add(tableName);
						condition.getDataElement().setTableName(tableName);
						
						//Aarti: Changes to add actual event table also to the query 
						//when the attribute selected belongs to the base specimen event parameters class.
						//this linking table is used to get the actual event the query was made on while 
						//formatting tree in catissuecoreAdvancedQueryImpl
						table.setLinkingTable(new Table(firstTableName));
//						tableId = bizLogic.getTableIdFromAliasName(tableName);
//						Set tablesInPath = bizLogic.setTablesInPath(Long.valueOf(parentTableId),Long.valueOf(tableId));
//						Logger.out.debug("tablesinpath for specimen event tables:"+tablesInPath);
//						tableSet.addAll(tablesInPath);

					}
					tableId = bizLogic.getTableIdFromAliasName(tableName);
					Set tablesInPath = bizLogic.setTablesInPath(Long.valueOf(parentTableId),Long.valueOf(tableId));
					Logger.out.debug("tablesinpath for specimen event tables:"+tablesInPath);
					tableSet.addAll(tablesInPath);
					//eventParametersTables.addAll(tablesInPath);
				}
			}
			else
			{
				Iterator conditionsItr = conditions.iterator();
				while(conditionsItr.hasNext())
				{
					Condition condition = (Condition)conditionsItr.next();
					String tableName = condition.getDataElement().getTableAliasName();
					tableId = bizLogic.getTableIdFromAliasName(tableName);
					Set tablesInPath = bizLogic.setTablesInPath(Long.valueOf(parentTableId),Long.valueOf(tableId));
					tableSet.addAll(tablesInPath);
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
				String childTableId = bizLogic.getTableIdFromAliasName(childTable);
				Set tablesInPath = bizLogic.setTablesInPath(Long.valueOf(parentTableId),Long.valueOf(childTableId));
				Logger.out.debug("tablesInPath after method call:"+tablesInPath);
				tableSet.addAll(tablesInPath);
			}
			setTables(parent,tableSet);
		}
//		Logger.out.debug("event tables before returning:"+eventParametersTables);
//		return eventParametersTables;
	}
	
	//Get the specimen Ids heirarchy above a given specimen ids.
	private List getSpecimenHeirarchy(String specimenId) throws DAOException,ClassNotFoundException
	{
		List specimenIdsList = new ArrayList();
		List specimenIdsListInOrder = new ArrayList();
		JDBCDAO jdbcDao = (JDBCDAO)DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
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
		
		JDBCDAO jdbcDao = (JDBCDAO)DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
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
	//Sets the activity status conditions for the query to filter disbaled data
	public String formActivityStatusConditions(Vector tablesVector,int tableSuffix)
	{
		StringBuffer activityStatusConditions=new StringBuffer();
		Iterator tablesVectorItr = tablesVector.iterator();
		int i=0;
		while(tablesVectorItr.hasNext())
		{
			activityStatusConditions.append(" "+Operator.AND+" "+tablesVectorItr.next()+tableSuffix+"."+Constants.ACTIVITY_STATUS_COLUMN+" "+
								Operator.NOT_EQUALS+" '"+Constants.ACTIVITY_STATUS_DISABLED+"' ");
			i++;
		}
		
		return activityStatusConditions.toString();
	}
	
	/**
	 * To creaet SQL for the Advance Query navigation. This will create query on Temporary table.
	 * @param whereColumnName The whereColumnNames
	 * @param whereColumnValue The whereColumnValues
	 * @param whereColumnCondition The whereColumnConditions
	 * @param columnList The column list that will form SELECT clause of the query.
	 * @param orderByAttributes The attributes that will apprear in the order by clause of SQL.
	 * @param sessionDataBean The session databean object.
	 * @return The SQL.
	 */
	public String createSQL(String[] whereColumnName, String[] whereColumnValue, String[] whereColumnCondition,String[] columnList, String[] orderByAttributes, SessionDataBean sessionDataBean)
	{
		String tmpResultsTableName = Constants.QUERY_RESULTS_TABLE+"_"+sessionDataBean.getUserId();
		if (whereColumnName[0].equals(Constants.ROOT))
        {
        	whereColumnName = null;
            whereColumnCondition = null;
            whereColumnValue = null;
        }
		
		StringBuffer sql=new StringBuffer("Select DISTINCT ");
		
		List<String> selectColumnNames = Arrays.asList(columnList);
		// Forming SELECT clause
		sql.append(columnList[0]);
		for (int index = 1; index < columnList.length; index++)
		{
			sql.append(", ").append(columnList[index]);
		}
		
		// Forming FROM clause
		sql.append(" From ").append(tmpResultsTableName);
		
		// Forming WHERE clause
		if (whereColumnName != null && whereColumnName.length != 0)
		{
			sql.append(" WHERE ");
			for (int index = 0; index < whereColumnName.length; index++)
			{
				sql.append(whereColumnName[index]).append(whereColumnCondition[index]).append(whereColumnValue[index]);
				if (index!= whereColumnName.length-1)
					sql.append(" "+Constants.AND_JOIN_CONDITION+" ");
			}
		}
		
		// Adding ORDER BY clause to query.
		if (orderByAttributes!=null && orderByAttributes.length != 0)
		{
			List<String> orderByAttributeList = new ArrayList<String>();
			for (int index = 0; index < orderByAttributes.length; index++)
			{
				if(selectColumnNames.contains(orderByAttributes[index]))
				{
					orderByAttributeList.add(orderByAttributes[index]);
				}
			}
			// adding only attributes in order by clause which are present in select clause. 
			if (orderByAttributeList.size()!=0)
			{
				sql.append(" ORDER BY ").append(orderByAttributeList.get(0));
				
				for (int index = 1; index < orderByAttributeList.size(); index++)
				{
					sql.append(" , ").append(orderByAttributeList.get(index));
				}
			}
		}
		
		return sql.toString();
	}
}
