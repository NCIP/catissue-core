
package edu.wustl.catissuecore.util.querysuite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.DefineGridViewBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputSpreadsheetBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputTreeBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.QueryResultObjectDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.querysuite.factory.SqlGeneratorFactory;
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IOutputAttribute;
import edu.wustl.common.querysuite.queryobject.IOutputTerm;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.impl.Expression;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.impl.metadata.QueryOutputTreeAttributeMetadata;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;
import edu.wustl.common.querysuite.queryobject.util.QueryObjectProcessor;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * This is an utility class to provide methods required for query interface.
 * @author deepti_shelar
 */
/**
 * @author vijay_pande
 *
 */
public  class QueryModuleUtil
{

	/**
	 * Executes the query and returns the results.
	 * @param selectSql sql to be executed
	 * @param sessionData sessiondata
	 * @param querySessionData 
	 * @return list of results 
	 * @throws ClassNotFoundException 
	 * @throws DAOException 
	 */
	public static List<List<String>> executeQuery(String selectSql, SessionDataBean sessionData, QuerySessionData querySessionData)
			throws ClassNotFoundException, DAOException
	{
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		List<List<String>> dataList = new ArrayList<List<String>>();
		try
		{
			dao.openSession(sessionData);
			dataList = dao.executeQuery(querySessionData.getSql(), sessionData, querySessionData.isSecureExecute(), querySessionData.isHasConditionOnIdentifiedField(), querySessionData.getQueryResultObjectDataMap());
			dao.commit();
		}
		finally
		{
			dao.closeSession();
		}
		return dataList;
	}

	/**
	 * Creates a new table in database. First the table is deleted if exist already.
	 * @param tableName name of the table to be deleted before creating new one. 
	 * @param createTableSql sql to create table
	 * @param sessionData session data.
	 * @throws DAOException DAOException 
	 */
	public static void executeCreateTable(String tableName, String createTableSql,
			SessionDataBean sessionData) throws DAOException
	{
		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{
			jdbcDao.openSession(sessionData);
			jdbcDao.delete(tableName);
			jdbcDao.executeUpdate(createTableSql);
			jdbcDao.commit();
		}
		catch (DAOException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			jdbcDao.closeSession();
		}
	}

	/**
	 * Takes data from the map and generates out put data accordingly so that spreadsheet will be updated.
	 * @param spreadSheetDatamap map which holds data for columns and records.
	 * @return this string consists of two strings seperated by '&', first part is for column names to be displayed in spreadsheet 
	 * and the second part is data in the spreadsheet.
	 */
	public static String prepareOutputSpreadsheetDataString(Map spreadSheetDatamap)
	{
		List<List<String>> dataList = (List<List<String>>) spreadSheetDatamap
				.get(Constants.SPREADSHEET_DATA_LIST);

		String outputSpreadsheetDataStr = "";
		String dataStr = "";
		for (List<String> row : dataList)
		{
			StringBuffer gridStrBuff = new StringBuffer();
			for (Object columnData : row)
			{
				Object gridObj = (Object) Utility.toNewGridFormat(columnData);
				String gridStr = gridObj.toString();
				gridStrBuff.append(gridStr + ",");
			}
			dataStr = dataStr + "|" + gridStrBuff.toString();
		}
		List columnsList = (List) spreadSheetDatamap.get(Constants.SPREADSHEET_COLUMN_LIST);
		String columns = columnsList.toString();
		columns = columns.replace("[", "");
		columns = columns.replace("]", "");
		outputSpreadsheetDataStr = columns + "&" + dataStr;
		return outputSpreadsheetDataStr;
	}

	public static String getSQLForRootNode(String tableName, Map<String,String> columnNameIndexMap)
	{
//		Map<String,String> columnNameIndexMap = getColumnNamesForSelectpart(root,queryResulObjectDataMap,uniqueIdNodesMap2); 
		String columnNames = columnNameIndexMap.get(Constants.COLUMN_NAMES);
		String indexStr = columnNameIndexMap.get(Constants.INDEX);
		int index = -1;
		if (indexStr!= null && !indexStr.equalsIgnoreCase("null"))
		{
			index = Integer.parseInt(indexStr);
		}
		String idColumnName = columnNames;
		if (columnNames.indexOf(",") != -1)
		{
			idColumnName = columnNames.substring(0, columnNames.indexOf(","));
		}
		String selectSql = "select distinct " + columnNames + " from " + tableName + " where "
				+ idColumnName + " is not null";
		selectSql = selectSql + Constants.NODE_SEPARATOR + index;
		return selectSql;
	}

	/**
	 * Forms select part of the query.
	 * @param node Node of Uotput tree .
	 * @param queryResulObjectDataMap 
	 * @param idNodeMap 
	 * @param columnMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @return String having all columnnames for select part.
	 */
	public static Map<String,String> getColumnNamesForSelectpart(List<QueryOutputTreeAttributeMetadata> attributes,Map<String, OutputTreeDataNode> idNodeMap, QueryResultObjectDataBean queryResultObjectDataBean)
	{  
		String columnNames = "";
		Map<String,String> columnNameIndexMap = new HashMap<String,String>();
		String idColumnName = null;
		String displayNameColumnName = null;
		String index = null;
		int columIndex = 0;
		Vector<Integer> objectColumnIdsVector = new Vector<Integer>();
		for (QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
		{
			AttributeInterface attribute = attributeMetaData.getAttribute();
			String columnName = attributeMetaData.getColumnName();
			
			if (idColumnName != null && displayNameColumnName != null)
			{
				break;
			}
			if (attribute.getName().equalsIgnoreCase(Constants.ID))
			{
				idColumnName = columnName;
				if(queryResultObjectDataBean.isMainEntity())
					   queryResultObjectDataBean.setMainEntityIdentifierColumnId(0);
					else
					   queryResultObjectDataBean.setEntityId(0);
				objectColumnIdsVector.add(0);
				columIndex++;
				
			}
			else if (isPresentInArray(attribute.getName(),Constants.ATTRIBUTE_NAMES_FOR_TREENODE_LABEL))
			{
				index = columnName.substring(Constants.COLUMN_NAME.length(), columnName.length());
				Vector<Integer> idvector = new Vector<Integer>();
				
				if(attribute.getIsIdentified()!=null)
				 idvector.add(1);
				objectColumnIdsVector.add(1);
				
				queryResultObjectDataBean.setIdentifiedDataColumnIds(idvector);
				displayNameColumnName = columnName;
				columIndex++;
			}
		}
		queryResultObjectDataBean.setObjectColumnIds(objectColumnIdsVector);
		if (displayNameColumnName != null)
		{
			columnNames = idColumnName + " , " + displayNameColumnName;// + " , " + columnNames;
		}
		else
		{
			columnNames = idColumnName;
		}
	//	columnNames = columnNames.substring(0, columnNames.lastIndexOf(","));
		Map<EntityInterface, Integer> entityIdIndexMap =new HashMap<EntityInterface, Integer>();
		if(queryResultObjectDataBean.getIdentifiedDataColumnIds().size()!=0)
		  queryResultObjectDataBean.setHasAssociatedIdentifiedData(true);
		if (!queryResultObjectDataBean.isMainEntity())
			columnNames = QueryCSMUtil.updateEntityIdIndexMap(queryResultObjectDataBean,columIndex,columnNames,null,entityIdIndexMap,idNodeMap);
		columnNameIndexMap.put(Constants.COLUMN_NAMES, columnNames);
		columnNameIndexMap.put(Constants.INDEX, index);
		return columnNameIndexMap;
	}

	/**
	 * Returns true if the attribute name can be used to form label for tree node.
	 * @param attrName String
	 * @return true if the attribute name can be used to form label for tree node otherwise returns false
	 */
	public static boolean isPresentInArray(String objectName,String[] stringArray)
	{
		for (int i = 0; i < stringArray.length; i++)
		{
			String name = stringArray[i];
			if (objectName.equalsIgnoreCase(name))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * This is used to set the default selections for the UI components when the screen is loaded for the first time.
	 * @param actionForm form bean
	 * @return CategorySearchForm formbean
	 */
	public static CategorySearchForm setDefaultSelections(CategorySearchForm actionForm)
	{
		if (actionForm.getClassChecked() == null)
		{
			actionForm.setClassChecked("on");
		}
		if (actionForm.getAttributeChecked() == null)
		{
			actionForm.setAttributeChecked("on");
		}
		if (actionForm.getPermissibleValuesChecked() == null)
		{
			actionForm.setPermissibleValuesChecked("off");
		}
		if (actionForm.getIncludeDescriptionChecked() == null)
		{
			actionForm.setIncludeDescriptionChecked("off");
		}
		//TODO check if null and then set the value of seleted.
		actionForm.setSelected("text_radioButton");
		actionForm.setTextField("");
		actionForm.setPermissibleValuesChecked("off");
		return actionForm;
	}

	/**
	 * When passes treeNumber , this method returns the root node of that tree. 
	 * @param rootOutputTreeNodeList tree data
	 * @param treeNo number of tree
	 * @return root node of the tree
	 */
	public static OutputTreeDataNode getRootNodeOfTree(
			List<OutputTreeDataNode> rootOutputTreeNodeList, String treeNo)
	{
		OutputTreeDataNode treeNode = null;
		for (OutputTreeDataNode node : rootOutputTreeNodeList)
		{
			if (node.getTreeNo() == Integer.valueOf(treeNo))
			{
				treeNode = node;
				break;
			}
		}
		return treeNode;
	}

	/**
	 * Returns column name of nodes id when passed a node to it 
	 * @param node {@link OutputTreeDataNode}
	 * @return String id Columns name
	 */
	public static String getParentIdColumnName(OutputTreeDataNode node)
	{
		if (node != null)
		{
			List<QueryOutputTreeAttributeMetadata> attributes = node.getAttributes();

			for (QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
			{
				AttributeInterface attribute = attributeMetaData.getAttribute();
				if (attribute.getName().equalsIgnoreCase(Constants.ID))
				{
					String sqlColumnName = attributeMetaData.getColumnName();
					return sqlColumnName;
				}
			}
		}
		return null;
	}

	/**
	 * Sets required data for grid.
	 * @param request HTTPRequest
	 * @param spreadSheetDatamap Map to store spreadsheet data
	 */
	public static void setGridData(HttpServletRequest request, Map spreadSheetDatamap)
	{
		int pageNum = Constants.START_PAGE;
		SelectedColumnsMetadata selectedColumnsMetadata = (SelectedColumnsMetadata) spreadSheetDatamap
				.get(Constants.SELECTED_COLUMN_META_DATA);
		//OutputTreeDataNode object = selectedColumnsMetadata.getCurrentSelectedObject();

		HttpSession session = request.getSession();
		//session.setAttribute(Constants.CURRENT_SELECTED_OBJECT,object);
		request.setAttribute(Constants.PAGE_NUMBER, Integer.toString(pageNum));
		QuerySessionData querySessionData = (QuerySessionData) spreadSheetDatamap
				.get(Constants.QUERY_SESSION_DATA);
		int totalNumberOfRecords = querySessionData.getTotalNumberOfRecords();
		List<List<String>> dataList = (List<List<String>>) spreadSheetDatamap
				.get(Constants.SPREADSHEET_DATA_LIST);
		//request.setAttribute(Constants.SPREADSHEET_DATA_LIST,dataList);
		request.setAttribute(Constants.PAGINATION_DATA_LIST, dataList);
		List columnsList = (List) spreadSheetDatamap.get(Constants.SPREADSHEET_COLUMN_LIST);
		if (columnsList != null)
			session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnsList);

		session.setAttribute(Constants.TOTAL_RESULTS, new Integer(totalNumberOfRecords));
		session.setAttribute(Constants.QUERY_SESSION_DATA, querySessionData);
		session.setAttribute(Constants.SELECTED_COLUMN_META_DATA, selectedColumnsMetadata);
		session.setAttribute(Constants.QUERY_REASUL_OBJECT_DATA_MAP, spreadSheetDatamap.get(Constants.QUERY_REASUL_OBJECT_DATA_MAP));
		session.setAttribute(Constants.DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP,spreadSheetDatamap
				.get(Constants.DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP));
		session.setAttribute(Constants.MAIN_ENTITY_MAP, spreadSheetDatamap.get(Constants.MAIN_ENTITY_MAP));
		String pageOf = (String) request.getParameter(Constants.PAGEOF);
		if (pageOf == null)
			pageOf = "pageOfQueryModule";
		request.setAttribute(Constants.PAGEOF, pageOf);
	}

	/**
	 * This method extracts query object and forms results for tree and grid.
	 * @param session session object
	 * @param query IQuery Object
	 * @param option 
	 * @return
	 */
	public static QueryModuleError searchQuery(HttpServletRequest request, IQuery query, String option)
	{      
		String isSavedQuery = (String) request.getSession().getAttribute(Constants.IS_SAVED_QUERY);
		if(isSavedQuery == null) 
			isSavedQuery = Constants.FALSE;
		HttpSession session = request.getSession();  
		boolean hasConditionOnIdentifiedField = edu.wustl.common.querysuite.security.utility.Utility.isConditionOnIdentifiedField(query);
		session.removeAttribute(Constants.HYPERLINK_COLUMN_MAP);
		QueryModuleError status = QueryModuleError.SUCCESS;
		try
		{
				session.setAttribute(AppletConstants.QUERY_OBJECT, query);
 
				SqlGenerator sqlGenerator = (SqlGenerator) SqlGeneratorFactory.getInstance();
				QueryOutputTreeBizLogic outputTreeBizLogic = new QueryOutputTreeBizLogic();
				String selectSql = (String)session.getAttribute(Constants.SAVE_GENERATED_SQL);
				List<OutputTreeDataNode> rootOutputTreeNodeList = (List<OutputTreeDataNode>)session.getAttribute(Constants.SAVE_TREE_NODE_LIST);
				Map<String, OutputTreeDataNode> uniqueIdNodesMap = (Map<String, OutputTreeDataNode>) session.getAttribute(Constants.ID_NODES_MAP);
				Map<EntityInterface, List<EntityInterface>> mainEntityMap = (Map<EntityInterface, List<EntityInterface>>)session.getAttribute(Constants.MAIN_ENTITY_MAP);
				if(isSavedQuery.equalsIgnoreCase(Constants.TRUE))
				{
					selectSql = sqlGenerator.generateSQL(query);
					session.setAttribute(Constants.SAVE_GENERATED_SQL,selectSql);
					rootOutputTreeNodeList = sqlGenerator.getRootOutputTreeNodeList();
					session.setAttribute(Constants.SAVE_TREE_NODE_LIST, rootOutputTreeNodeList);
					uniqueIdNodesMap = QueryObjectProcessor.getAllChildrenNodes(rootOutputTreeNodeList);
					session.setAttribute(Constants.ID_NODES_MAP, uniqueIdNodesMap);
					mainEntityMap = QueryCSMUtil.setMainObjectErrorMessage(query, request.getSession(), uniqueIdNodesMap);
				}
				Object obj = session.getAttribute(Constants.SESSION_DATA);
				if (obj != null)
				{
					SessionDataBean sessionData = (SessionDataBean) obj;
					String propertyValue = XMLPropertyHandler.getValue(Constants.MULTIUSER);
					String randomNumber="";
				if(propertyValue!=null)
				{
						if(session.getAttribute(Constants.RANDOM_NUMBER)==null)
						{
							int number =(int) (Math.random()*100000);
							if(propertyValue.equalsIgnoreCase(Constants.TRUE))
							{
								randomNumber =Constants.UNDERSCORE+Integer.toString(number);
							}
							session.setAttribute(Constants.RANDOM_NUMBER,randomNumber);
						}
						else
						{
							randomNumber = (String)session.getAttribute(Constants.RANDOM_NUMBER);
						}
				} 
				// auditing query execution : Deepti
				QueryBizLogic queryBizLogic = (QueryBizLogic)AbstractBizLogicFactory.getBizLogic(
				    	ApplicationProperties.getValue("app.bizLogicFactory"),
						"getBizLogic", Constants.QUERY_INTERFACE_ID);
				queryBizLogic.insertQuery(selectSql,sessionData);
	    
				outputTreeBizLogic.createOutputTreeTable(selectSql,sessionData,randomNumber);
				status = QueryModuleError.SUCCESS;
				int i = 0;
				for (OutputTreeDataNode outnode : rootOutputTreeNodeList)
				{
					Vector<QueryTreeNodeData> treeData = outputTreeBizLogic
							.createDefaultOutputTreeData(i, outnode, sessionData,randomNumber,hasConditionOnIdentifiedField,mainEntityMap,uniqueIdNodesMap);
					
					int resultsSize = treeData.size();
					if(option == null)
					{
						if (resultsSize == 0)
						{
							status = QueryModuleError.NO_RESULT_PRESENT;
							return status;
						} else if(resultsSize-1 > Variables.maximumTreeNodeLimit)
						{
							status = QueryModuleError.RESULTS_MORE_THAN_LIMIT;
							String resultSizeStr = resultsSize-1 +"";
							session.setAttribute(Constants.TREE_NODE_LIMIT_EXCEEDED_RECORDS,resultSizeStr);
							return status;
						}
						session.setAttribute(Constants.TREE_DATA + "_" + i, treeData);
						i += 1;
					}
					else if(option.equalsIgnoreCase(Constants.VIEW_ALL_RECORDS))
					{
						session.setAttribute(Constants.TREE_DATA + "_" + i, treeData);
						i += 1;
					}
					else if(option.equalsIgnoreCase(Constants.VIEW_LIMITED_RECORDS))
					{
						List<QueryTreeNodeData> limitedRecordsList = treeData.subList(0, Variables.maximumTreeNodeLimit+1);
						Vector<QueryTreeNodeData> limitedTreeData = new Vector<QueryTreeNodeData>();
						limitedTreeData.addAll(limitedRecordsList);
						session.setAttribute(Constants.TREE_DATA + "_" + i, limitedTreeData);
						i += 1;
					}
				}
				int recordsPerPage = 0;
				String recordsPerPageSessionValue = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);
				if (recordsPerPageSessionValue == null)
				{
					recordsPerPage = Integer.parseInt(XMLPropertyHandler
							.getValue(Constants.RECORDS_PER_PAGE_PROPERTY_NAME));
					session.setAttribute(Constants.RESULTS_PER_PAGE, recordsPerPage + "");
				}
				else
					recordsPerPage = Integer.valueOf(recordsPerPageSessionValue);
				session.setAttribute(Constants.TREE_ROOTS, rootOutputTreeNodeList);
				
				Long noOfTrees = Long.valueOf(rootOutputTreeNodeList.size());
				session.setAttribute(Constants.NO_OF_TREES, noOfTrees);
				
				OutputTreeDataNode node = rootOutputTreeNodeList.get(0);
				QueryResultObjectDataBean queryResulObjectDataBean = QueryCSMUtil.getQueryResulObjectDataBean(node,mainEntityMap);
				Map<Long,QueryResultObjectDataBean> queryResultObjectDataBeanMap = new HashMap<Long, QueryResultObjectDataBean>();
				queryResultObjectDataBeanMap.put(node.getId(), queryResulObjectDataBean);
				
				QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
				String parentNodeId = null;
				String treeNo = "0";
				SelectedColumnsMetadata selectedColumnsMetadata = getAppropriateSelectedColumnMetadata(query, (SelectedColumnsMetadata) session.getAttribute(Constants.SELECTED_COLUMN_META_DATA));
				selectedColumnsMetadata.setCurrentSelectedObject(node);
				List<IOutputAttribute> selectedAttributeList = null;
				if(query.getId() != null && isSavedQuery.equals(Constants.TRUE))
				{
					if (query instanceof ParameterizedQuery)
					{
						ParameterizedQuery savedQuery = (ParameterizedQuery) query;
						selectedAttributeList = savedQuery.getOutputAttributeList();
						if(!selectedAttributeList.isEmpty())
						{
							DefineGridViewBizLogic defineGridViewBizLogic = new DefineGridViewBizLogic();
							selectedColumnsMetadata.setSelectedOutputAttributeList(selectedAttributeList);
							defineGridViewBizLogic.getSelectedColumnMetadataForSavedQuery(uniqueIdNodesMap,selectedAttributeList, selectedColumnsMetadata);
							selectedColumnsMetadata.setDefinedView(true);
						}
					}
				} 
				Map<String, IOutputTerm> outputTermsColumns = sqlGenerator.getOutputTermsColumns();
				if(outputTermsColumns == null)
				{ 
					outputTermsColumns = (Map<String, IOutputTerm>)session.getAttribute(Constants.OUTPUT_TERMS_COLUMNS);
				}
				session.setAttribute(Constants.OUTPUT_TERMS_COLUMNS, outputTermsColumns);
				Map<String, List<String>> spreadSheetDatamap = outputSpreadsheetBizLogic
						.createSpreadsheetData(treeNo, node, sessionData, parentNodeId,
								recordsPerPage, selectedColumnsMetadata,randomNumber,uniqueIdNodesMap,queryResultObjectDataBeanMap,
								hasConditionOnIdentifiedField,mainEntityMap,query.getConstraints(),
								outputTermsColumns);
				
				QuerySessionData querySessionData = (QuerySessionData) spreadSheetDatamap
						.get(Constants.QUERY_SESSION_DATA);
				int totalNumberOfRecords = querySessionData.getTotalNumberOfRecords();
				session.setAttribute(Constants.QUERY_SESSION_DATA, querySessionData);
				session.setAttribute(Constants.TOTAL_RESULTS,
								new Integer(totalNumberOfRecords));
				QueryShoppingCart cart = (QueryShoppingCart)session.getAttribute(Constants.QUERY_SHOPPING_CART);
				String message = getMessageIfIdNotPresentForOrderableEntities(selectedColumnsMetadata, cart);
				session.setAttribute(Constants.VALIDATION_MESSAGE_FOR_ORDERING, message);
				
				session.setAttribute(Constants.PAGINATION_DATA_LIST, spreadSheetDatamap
						.get(Constants.SPREADSHEET_DATA_LIST));
				session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, spreadSheetDatamap
						.get(Constants.SPREADSHEET_COLUMN_LIST));
				session.setAttribute(Constants.SELECTED_COLUMN_META_DATA, spreadSheetDatamap
						.get(Constants.SELECTED_COLUMN_META_DATA));
				session.setAttribute(Constants.QUERY_REASUL_OBJECT_DATA_MAP, spreadSheetDatamap
						.get(Constants.QUERY_REASUL_OBJECT_DATA_MAP));
				session.setAttribute(Constants.DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP,spreadSheetDatamap
										.get(Constants.DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP));
			}
		}
		catch (MultipleRootsException e)
		{
			Logger.out.error(e);
			status = QueryModuleError.MULTIPLE_ROOT;
		}
		catch (SqlException e)
		{
			Logger.out.error(e);
			status = QueryModuleError.SQL_EXCEPTION;
		}
		catch (ClassNotFoundException e)
		{
			Logger.out.error(e);
			status = QueryModuleError.CLASS_NOT_FOUND;
		}
		catch (DAOException e)
		{
			Logger.out.error(e);
			status = QueryModuleError.DAO_EXCEPTION;
		}
		catch (BizLogicException e) {
			Logger.out.error(e);
			status = QueryModuleError.DAO_EXCEPTION;
		}
		return status;
	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	public static boolean checkIfRulePresentInDag(IQuery query) 
	{
		boolean isRulePresentInDag = false;

		if (query != null)
		{
			IConstraints constraints = query.getConstraints();
			for(IExpression expression : constraints) 
			{
				if (expression.containsRule())
				{
					isRulePresentInDag = true;
					break;
				}
			}
		}
		return isRulePresentInDag;
	}

	/**
	 * @param query
	 * @param session
	 * @param isQueryChanged
	 * @return
	 */
	private static SelectedColumnsMetadata getAppropriateSelectedColumnMetadata
	(IQuery query, SelectedColumnsMetadata selectedColumnsMetadata)
	{
		boolean isQueryChanged = false;
		if(query != null && selectedColumnsMetadata != null)
		{
			List<Integer> expressionIdsInQuery = new ArrayList<Integer>();
			IConstraints constraints = query.getConstraints();
			List<QueryOutputTreeAttributeMetadata> selectedAttributeMetaDataList = selectedColumnsMetadata.getSelectedAttributeMetaDataList();
			for(IExpression expression : constraints) 
			{
				if (expression.isInView())
				{
					expressionIdsInQuery.add(new Integer(expression.getExpressionId()));
				}
			}
				for(QueryOutputTreeAttributeMetadata element :selectedAttributeMetaDataList)
				{
					int expressionId = element.getTreeDataNode().getExpressionId();
					if(!expressionIdsInQuery.contains(new Integer(expressionId)))
					{
						isQueryChanged = true;
						break;
					}
				}
		}
		if(isQueryChanged || selectedColumnsMetadata == null)
		{
			selectedColumnsMetadata = new SelectedColumnsMetadata();
			selectedColumnsMetadata.setDefinedView(false);
		}
		return selectedColumnsMetadata;
	}
	/**
	 * @param query
	 * @param session
	 * @param isQueryChanged
	 * @return
	 *//*
	private static SelectedColumnsMetadata getAppropriateSelectedColumnMetadata1
	(IQuery query, SelectedColumnsMetadata selectedColumnsMetadata)
	{
	boolean isQueryChanged = false;
	if(query != null && selectedColumnsMetadata != null)
	{
		IConstraints constraints = query.getConstraints();
		Enumeration<IExpressionId> expressionsInQuery = constraints.getExpressionIds();
		List<IExpressionId> expressionIdsInQuery = new ArrayList<IExpressionId>();
		List<QueryOutputTreeAttributeMetadata> selectedAttributeMetaDataList = selectedColumnsMetadata.getSelectedAttributeMetaDataList();
		while(expressionsInQuery.hasMoreElements())
		{
			IExpressionId nextElement = expressionsInQuery.nextElement();
			if (constraints.getExpression(nextElement).isInView())
				expressionIdsInQuery.add(nextElement);
		}
		for(QueryOutputTreeAttributeMetadata element :selectedAttributeMetaDataList)
		{
			IExpressionId expressionId = element.getTreeDataNode().getExpressionId();
			if(!expressionIdsInQuery.contains(expressionId))
			{
				isQueryChanged = true;
				break;
			}
		}
		
	}
	
	if(isQueryChanged || selectedColumnsMetadata == null)
	{
		selectedColumnsMetadata = new SelectedColumnsMetadata();
		selectedColumnsMetadata.setDefinedView(false);
	}
	return selectedColumnsMetadata;
}*/
	/**
	 * checks if the current view contains any orderable entity and if the 'id' attribute of the entity is 
	 * included in the view.
	 * @param selectedColumnsMetadata - gives current entity and attribute list
	 * @param cart - gives the attribute list of the entities present in the cart.
	 * @return message if if the 'id' attribute of the orderable entity is not included in view.
	 */public static String getMessageIfIdNotPresentForOrderableEntities(SelectedColumnsMetadata selectedColumnsMetadata, QueryShoppingCart cart)
	{
		 if (selectedColumnsMetadata == null)
			 return null;
		String message = null;
		QueryShoppingCartBizLogic queryShoppingCartBizLogic = new QueryShoppingCartBizLogic();
		boolean areListsequal = true;
		boolean isOrderableEntityPresent = false;
		boolean isAttributeIdIncludedInView = false;
		List<String> orderableEntityNameList = Arrays.asList(Constants.entityNameArray);
		List<QueryOutputTreeAttributeMetadata> selectedAttributeMetaDataList = selectedColumnsMetadata
				.getSelectedAttributeMetaDataList();
		List<AttributeInterface> currentAttributeList = selectedColumnsMetadata.getAttributeList();
		// check if the cart view and the defined view are same
		if (cart != null)
		{
			List<AttributeInterface> cartAttributeList = cart.getCartAttributeList();
			if (cartAttributeList != null)
			{
				int indexArray[] = queryShoppingCartBizLogic.getNewAttributeListIndexArray(cartAttributeList,
						currentAttributeList);
				if (indexArray == null)
				{
					areListsequal = false;
				}
			}
			
		}
		
		if (areListsequal)
		{		 
			//if the two views are same checks if orderable entity is present and id attribute is present
			Iterator<QueryOutputTreeAttributeMetadata> iterator = selectedAttributeMetaDataList.iterator();
			while (iterator.hasNext())
			{
				QueryOutputTreeAttributeMetadata element = (QueryOutputTreeAttributeMetadata) iterator.next();
				if (orderableEntityNameList.contains(element.getAttribute().getEntity().getName()))
				{
					isOrderableEntityPresent = true;
					if(element.getAttribute().getName().equals(Constants.ID))
					{
						isAttributeIdIncludedInView = true;
						//String gh = null;
						break;
					}
				}
			}
			if ((isOrderableEntityPresent) && (!isAttributeIdIncludedInView))
			{			
				message = ApplicationProperties.getValue("query.defineGridResultsView.messageForPopup");							
			}
		}
		return message;
		}

	 /**
	  * Method to call searchQuery and to set appropriate error message
	  * @param request object of HttpServletRequest
	  * @param parameterizedQuery object of IParameterizedQuery
	  * @return errorMessage String value for errorMessage
	  */
	 public static String executeQuery(HttpServletRequest request, IQuery parameterizedQuery)
	 {
		 String errorMessage;
		 boolean isRulePresentInDag = checkIfRulePresentInDag(parameterizedQuery) ;
		 QueryModuleError errorCode;
		 if (isRulePresentInDag)
		 {
			 errorCode = searchQuery(request , parameterizedQuery,null);
		 }
		 else
		 {
			 errorCode = QueryModuleError.EMPTY_DAG;
		 }
		 
		 switch (errorCode)
		 {
			 case EMPTY_DAG :
				 errorMessage = ApplicationProperties.getValue("query.empty.dag");
				 break;
			 case MULTIPLE_ROOT :
				 errorMessage = ApplicationProperties.getValue("errors.executeQuery.multipleRoots");
				 break;
			 case NO_RESULT_PRESENT :
				 errorMessage = ApplicationProperties.getValue("query.zero.records.present");
				 break;
			 case SQL_EXCEPTION :
			 case DAO_EXCEPTION :
			 case CLASS_NOT_FOUND :
				 errorMessage = ApplicationProperties.getValue("errors.executeQuery.genericmessage");
				 break;
			 case RESULTS_MORE_THAN_LIMIT :
				 errorMessage = Constants.TREE_NODE_LIMIT_EXCEEDED_RECORDS;
				 break;
			default:
				 errorMessage= null;
		 }
		 return errorMessage;
	 }
}