package edu.wustl.catissuecore.util.querysuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.DefineGridViewBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputSpreadsheetBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputTreeBizLogic;
import edu.wustl.catissuecore.querysuite.CatissueSqlGenerator;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.QueryResultObjectDataBean;
import edu.wustl.common.bizlogic.QueryBizLogic;
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
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.impl.metadata.QueryOutputTreeAttributeMetadata;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;
import edu.wustl.common.querysuite.queryobject.util.QueryObjectProcessor;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author santhoshkumar_c
 *
 */
public class QueryModuleSearchQueryUtil
{
	private HttpServletRequest request;
	private HttpSession session;
	private IQuery query;
	boolean isSavedQuery;

	QueryDetails queryDetailsObj;
	
	/**
	 * @param request
	 * @param query
	 */
	public QueryModuleSearchQueryUtil(HttpServletRequest request, IQuery query)
	{
		this.request = request;
		this.session = request.getSession();
		this.query = query;
		
		isSavedQuery = Boolean.valueOf( (String) session.getAttribute(Constants.IS_SAVED_QUERY));
		queryDetailsObj = new QueryDetails(session);
	}
	
	/**
	 * This method extracts query object and forms results for tree and grid.
	 * @param option
	 * @return status
	 */
	public QueryModuleError searchQuery(String option)
	{
		session.removeAttribute(Constants.HYPERLINK_COLUMN_MAP);
		QueryModuleError status = QueryModuleError.SUCCESS;
		try
		{
				session.setAttribute(AppletConstants.QUERY_OBJECT, query);			
				if(isSavedQuery)
				{
					processSaveQuery();
				}
				if (queryDetailsObj.getSessionData() != null)
				{
				QueryOutputTreeBizLogic outputTreeBizLogic = auditQuery();		
				boolean hasCondOnIdentifiedField = edu.wustl.common.querysuite
					.security.utility.Utility.isConditionOnIdentifiedField(query);
				setDataInSession(option, outputTreeBizLogic, hasCondOnIdentifiedField);
			    }
		}
		catch (QueryModuleException e) 
		{
			status= e.getKey();
		}
		return status;
	}
	
	/**
	 * @param option
	 * @param outputTreeBizLogic
	 * @param hasCondOnIdentifiedField
	 * @throws QueryModuleException
	 */
	private void setDataInSession(String option, QueryOutputTreeBizLogic outputTreeBizLogic,
			boolean hasCondOnIdentifiedField) throws  QueryModuleException 
   {
		int initialValue = 0;
		QueryModuleException queryModExp;
		try
		{ 
			for (OutputTreeDataNode outnode : queryDetailsObj.getRootOutputTreeNodeList())
			{ 
				Vector<QueryTreeNodeData> treeData= null;			
				treeData = outputTreeBizLogic.createDefaultOutputTreeData(initialValue, outnode, 
						hasCondOnIdentifiedField, queryDetailsObj);
				initialValue = setTreeData(option, initialValue, treeData);
			}
		} 
		catch (DAOException e)
		{
				queryModExp = new QueryModuleException(e.getMessage(), QueryModuleError.DAO_EXCEPTION);
				throw queryModExp;
		}
		catch (ClassNotFoundException e)
		{
			queryModExp = new QueryModuleException(e.getMessage(),QueryModuleError.CLASS_NOT_FOUND);
			throw queryModExp;
		}
		
		session.setAttribute(Constants.TREE_ROOTS, queryDetailsObj.getRootOutputTreeNodeList());
		Long noOfTrees = Long.valueOf(queryDetailsObj.getRootOutputTreeNodeList().size());
		session.setAttribute(Constants.NO_OF_TREES, noOfTrees);
		OutputTreeDataNode node = queryDetailsObj.getRootOutputTreeNodeList().get(0);
		processRecords(queryDetailsObj, node, hasCondOnIdentifiedField);
	}
	
	/**
	 * @param option
	 * @param initialValue
	 * @param treeData
	 * @return int
	 * @throws QueryModuleException 
	 */
	private int setTreeData(String option, int initialValue,
			Vector<QueryTreeNodeData> treeData) throws QueryModuleException
	{		
		int resultsSize = treeData.size();
		if(option == null)
		{
			if (resultsSize == 0)
			{
				throw new QueryModuleException("Query Returns Zero Results",
						QueryModuleError.NO_RESULT_PRESENT);
			}
			else if(resultsSize-1 > Variables.maximumTreeNodeLimit)
			{
				String resultSizeStr = String.valueOf(resultsSize-1);
				session.setAttribute(Constants.TREE_NODE_LIMIT_EXCEEDED_RECORDS,
				resultSizeStr);
				throw new QueryModuleException("Query Results Exceeded The Limit",
						QueryModuleError.RESULTS_MORE_THAN_LIMIT);
			}
		}
		else if(Constants.VIEW_LIMITED_RECORDS.equals(option))
		{
			List<QueryTreeNodeData> limitedRecordsList = treeData.subList(0, Variables.maximumTreeNodeLimit+1);
			Vector<QueryTreeNodeData> limitedTreeData = new Vector<QueryTreeNodeData>();
			limitedTreeData.addAll(limitedRecordsList);
			treeData = limitedTreeData;
		}
		session.setAttribute(Constants.TREE_DATA + Constants.UNDERSCORE + initialValue, treeData);
				initialValue += 1;
		return initialValue;
	}
	
	/**
	 * @return
	 * @throws QueryModuleException
	 */
	private QueryOutputTreeBizLogic auditQuery() throws QueryModuleException 
	{
		QueryBizLogic queryBizLogic = null;
		QueryModuleException queryModExp;
		QueryOutputTreeBizLogic outputTreeBizLogic = new QueryOutputTreeBizLogic();		
		try
		{
			queryBizLogic = (QueryBizLogic)AbstractBizLogicFactory.getBizLogic(
			    	ApplicationProperties.getValue("app.bizLogicFactory"),
					"getBizLogic", Constants.QUERY_INTERFACE_ID);
			String selectSql = (String)session.getAttribute(Constants.SAVE_GENERATED_SQL);		
			queryBizLogic.insertQuery(selectSql, queryDetailsObj.getSessionData());
			outputTreeBizLogic.createOutputTreeTable(selectSql, queryDetailsObj);
		}
		catch (BizLogicException e)
		{
			queryModExp = new QueryModuleException(e.getMessage(), QueryModuleError.DAO_EXCEPTION);
			throw queryModExp;
		}
		catch (ClassNotFoundException e) 
		{
			queryModExp = new QueryModuleException(e.getMessage(), QueryModuleError.CLASS_NOT_FOUND);
			throw queryModExp;
		}
		catch (DAOException e)
		{
			queryModExp = new QueryModuleException(e.getMessage(), QueryModuleError.DAO_EXCEPTION);
			throw queryModExp;
		}
			return outputTreeBizLogic;
	}
	
	/**
	 * @throws QueryModuleException
	 */
	private void processSaveQuery() throws QueryModuleException
	{	
		SqlGenerator sqlGenerator = new CatissueSqlGenerator();
		QueryModuleException queryModExp;
		try
		{ 
			session.setAttribute(Constants.SAVE_GENERATED_SQL, sqlGenerator.generateSQL(query));
			Map<AttributeInterface, String> attributeColumnNameMap = sqlGenerator.getAttributeColumnNameMap();
			session.setAttribute(Constants.ATTRIBUTE_COLUMN_NAME_MAP, attributeColumnNameMap);
			session.setAttribute(Constants.OUTPUT_TERMS_COLUMNS,sqlGenerator.getOutputTermsColumns());
		}
		catch(MultipleRootsException e)
		{
			queryModExp = new QueryModuleException(e.getMessage(), QueryModuleError.MULTIPLE_ROOT);
			throw queryModExp;
			
		}
		catch(SqlException e)
		{
			queryModExp = new QueryModuleException(e.getMessage(), QueryModuleError.SQL_EXCEPTION);
			throw queryModExp;
		}
		List<OutputTreeDataNode> rootOutputTreeNodeList = sqlGenerator.getRootOutputTreeNodeList();
		session.setAttribute(Constants.SAVE_TREE_NODE_LIST, rootOutputTreeNodeList);
		queryDetailsObj.setRootOutputTreeNodeList(rootOutputTreeNodeList);
		Map<String, OutputTreeDataNode> uniqueIdNodesMap = QueryObjectProcessor
			.getAllChildrenNodes(rootOutputTreeNodeList);
		queryDetailsObj.setUniqueIdNodesMap(uniqueIdNodesMap);
		session.setAttribute(Constants.ID_NODES_MAP, uniqueIdNodesMap);
		Map<EntityInterface, List<EntityInterface>> mainEntityMap = QueryCSMUtil
			.setMainObjectErrorMessage(query, request.getSession(), queryDetailsObj);
		queryDetailsObj.setMainEntityMap(mainEntityMap);
	}
	
	/**
	 * @param QueryDetailsObj
	 * @param node
	 * @param hasCondOnIdentifiedField
	 * @throws QueryModuleException
	 */
	public void processRecords(QueryDetails QueryDetailsObj, OutputTreeDataNode node,
			boolean hasCondOnIdentifiedField) throws  QueryModuleException
	{
		SelectedColumnsMetadata selectedColumnsMetadata = getAppropriateSelectedColumnMetadata(query,
				(SelectedColumnsMetadata) session.getAttribute(Constants.SELECTED_COLUMN_META_DATA));
		selectedColumnsMetadata.setCurrentSelectedObject(node);
		QueryModuleException queryModExp;
		int recordsPerPage = setRecordsPerPage();
		if(query.getId() != null && isSavedQuery )
		{
			getSelectedColumnsMetadata(QueryDetailsObj, selectedColumnsMetadata);
		}
		
		QueryResultObjectDataBean queryResulObjectDataBean = QueryCSMUtil
		.getQueryResulObjectDataBean(node, QueryDetailsObj);
	    Map<Long,QueryResultObjectDataBean> queryResultObjDataBeanMap = new HashMap<Long,
		QueryResultObjectDataBean>();
	    queryResultObjDataBeanMap.put(node.getId(), queryResulObjectDataBean);
	    QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic  = new QueryOutputSpreadsheetBizLogic();
	    try
	    { 	// deepti change
	    	SqlGenerator sqlGenerator = (SqlGenerator) SqlGeneratorFactory.getInstance();
	    	Map<String, IOutputTerm> outputTermsColumns = sqlGenerator.getOutputTermsColumns();
			if(outputTermsColumns == null)
			{
				outputTermsColumns = (Map<String, IOutputTerm>)session.getAttribute(Constants
						.OUTPUT_TERMS_COLUMNS);
			}
			session.setAttribute(Constants.OUTPUT_TERMS_COLUMNS, outputTermsColumns);
	    	Map<String, List<String>> spreadSheetDatamap = outputSpreadsheetBizLogic
				.createSpreadsheetData(Constants.TREENO_ZERO, node, QueryDetailsObj,
				null, recordsPerPage, selectedColumnsMetadata, queryResultObjDataBeanMap,
				hasCondOnIdentifiedField, query.getConstraints(), outputTermsColumns);
	    	setQuerySessionData(selectedColumnsMetadata, spreadSheetDatamap);
	    }
	    catch(DAOException e)
	    {
	    	queryModExp = new QueryModuleException(e.getMessage(), QueryModuleError.DAO_EXCEPTION);
	    	throw queryModExp;
	     }
	    catch (ClassNotFoundException e)
	    {
			queryModExp = new QueryModuleException(e.getMessage(), QueryModuleError.CLASS_NOT_FOUND);
			throw queryModExp;
		}
	}
	
	/**
	 * @param query
	 * @param selectedColumnsMetadata
	 * @return
	 */
	private static SelectedColumnsMetadata getAppropriateSelectedColumnMetadata(IQuery query, 
			SelectedColumnsMetadata selectedColumnsMetadata)
	{
		boolean isQueryChanged = false;
		if(query != null && selectedColumnsMetadata != null)
		{
			List<Integer> expressionIdsInQuery = new ArrayList<Integer>();
			IConstraints constraints = query.getConstraints();
			List<QueryOutputTreeAttributeMetadata> selAttributeMetaDataList = selectedColumnsMetadata
				.getSelectedAttributeMetaDataList();
			for(IExpression expression : constraints)
			{
				if (expression.isInView())
				{
					expressionIdsInQuery.add(Integer.valueOf(expression.getExpressionId()));
				}
			}
			int expressionId;
			for(QueryOutputTreeAttributeMetadata element :selAttributeMetaDataList)
			{
					expressionId = element.getTreeDataNode().getExpressionId();
					if(!expressionIdsInQuery.contains(Integer.valueOf(expressionId)))
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
	
	/** It will set the results per page.
	 * @return int
	 */
	private int setRecordsPerPage() 
	{
		int recordsPerPage;
		String recordsPerPgSessionValue = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);
		if (recordsPerPgSessionValue == null)
		{
			recordsPerPgSessionValue = XMLPropertyHandler
					.getValue(Constants.RECORDS_PER_PAGE_PROPERTY_NAME);
			session.setAttribute(Constants.RESULTS_PER_PAGE, recordsPerPgSessionValue);
		}
		recordsPerPage = Integer.valueOf(recordsPerPgSessionValue);
		return recordsPerPage;
	}
	
	/**
	 * Set the data in session.
	 * @param selectedColumnsMetadata
	 * @param spreadSheetDatamap
	 */
	public void setQuerySessionData(SelectedColumnsMetadata selectedColumnsMetadata,
			Map<String, List<String>> spreadSheetDatamap)
	{
		QuerySessionData querySessionData = (QuerySessionData) spreadSheetDatamap
				.get(Constants.QUERY_SESSION_DATA);
		int totalNoOfRecords = querySessionData.getTotalNumberOfRecords();
		session.setAttribute(Constants.QUERY_SESSION_DATA, querySessionData);
		session.setAttribute(Constants.TOTAL_RESULTS, Integer.valueOf(totalNoOfRecords));
		QueryShoppingCart cart = (QueryShoppingCart)session.getAttribute(Constants.QUERY_SHOPPING_CART);
		String message = QueryModuleUtil.getMessageIfIdNotPresentForOrderableEntities(
				selectedColumnsMetadata, cart);
		session.setAttribute(Constants.VALIDATION_MESSAGE_FOR_ORDERING, message);
		session.setAttribute(Constants.PAGINATION_DATA_LIST, spreadSheetDatamap
				.get(Constants.SPREADSHEET_DATA_LIST));
		session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, spreadSheetDatamap
				.get(Constants.SPREADSHEET_COLUMN_LIST));
		session.setAttribute(Constants.SELECTED_COLUMN_META_DATA, spreadSheetDatamap
				.get(Constants.SELECTED_COLUMN_META_DATA));
		session.setAttribute(Constants.QUERY_REASUL_OBJECT_DATA_MAP, spreadSheetDatamap
				.get(Constants.QUERY_REASUL_OBJECT_DATA_MAP));
		session.setAttribute(Constants.DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP, spreadSheetDatamap
				.get(Constants.DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP));
	}
	
	/**
	 * @param QueryDetailsObj
	 * @param selectedColumnsMetadata
	 */
	public void getSelectedColumnsMetadata(QueryDetails QueryDetailsObj,
			SelectedColumnsMetadata selectedColumnsMetadata)
	{
		List<IOutputAttribute> selAttributeList;
		if (query instanceof ParameterizedQuery)
		{
			ParameterizedQuery savedQuery = (ParameterizedQuery) query;
			selAttributeList = savedQuery.getOutputAttributeList();
			if(!selAttributeList.isEmpty())
			{
				DefineGridViewBizLogic gridViewBizLogic = new DefineGridViewBizLogic();
				selectedColumnsMetadata.setSelectedOutputAttributeList(selAttributeList);
				gridViewBizLogic.getSelectedColumnMetadataForSavedQuery(QueryDetailsObj
				.getUniqueIdNodesMap().values(), selAttributeList, selectedColumnsMetadata);
				selectedColumnsMetadata.setDefinedView(true);
			}
		}
	}
}
