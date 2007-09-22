
package edu.wustl.catissuecore.util.querysuite;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.globus.util.http.HTTPRequestParser;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputSpreadsheetBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputTreeBizLogic;
import edu.wustl.catissuecore.flex.dag.DAGConstant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.querysuite.factory.SqlGeneratorFactory;
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.impl.Expression;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.metadata.QueryOutputTreeAttributeMetadata;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;
import edu.wustl.common.querysuite.queryobject.util.QueryObjectProcessor;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * This is an utility class to provide methods required for query interface.
 * @author deepti_shelar
 */
public abstract class QueryModuleUtil
{

	public static final int SUCCESS = 0;
	public static final int EMPTY_DAG = 1;
	public static final int MULTIPLE_ROOT = 2;
	public static final int NO_RESULT_PRESENT = 3;
	public static final int SQL_EXCEPTION = 4;
	public static final int DAO_EXCEPTION = 5;
	public static final int CLASS_NOT_FOUND = 6;
	public static final int RESULTS_MORE_THAN_LIMIT = 10;
	
	
	/**
	 * Executes the query and returns the results.
	 * @param selectSql sql to be executed
	 * @param sessionData sessiondata
	 * @return list of results 
	 * @throws ClassNotFoundException 
	 * @throws DAOException 
	 */
	public static List<List<String>> executeQuery(String selectSql, SessionDataBean sessionData)
			throws ClassNotFoundException, DAOException
	{
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		List<List<String>> dataList = new ArrayList<List<String>>();
		try
		{
			dao.openSession(sessionData);
			dataList = dao.executeQuery(selectSql, sessionData, false, false, null);
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

	/**
	 * Returns SQL for root node in tree.
	 * @param root root node of the tree
	 * @param tableName name of the temp table created
	 * @return String sql for root node
	 */
	public static String getSQLForRootNode(OutputTreeDataNode root, String tableName)
	{
		String columnNames = getColumnNamesForSelectpart(root);
		String indexStr = columnNames.substring(columnNames.lastIndexOf(";") + 1, columnNames
				.length());
		int index = -1;
		if (!indexStr.equalsIgnoreCase("null"))
		{
			index = new Integer(indexStr);
		}
		if (columnNames.lastIndexOf(";") != -1)
		{
			columnNames = columnNames.substring(0, columnNames.lastIndexOf(";"));
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
	 * @param columnMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @return String having all columnnames for select part.
	 */
	public static String getColumnNamesForSelectpart(OutputTreeDataNode node)
	{
		String columnNames = "";
		String idColumnName = null;
		String displayNameColumnName = null;
		String index = null;
		List<QueryOutputTreeAttributeMetadata> attributes = node.getAttributes();
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
			}
			else if (ifAttributeIsDisplayName(attribute.getName()))
			{
				index = columnName.substring(Constants.COLUMN_NAME.length(), columnName.length());
				displayNameColumnName = columnName;
			}
		}
		if (displayNameColumnName != null)
		{
			columnNames = idColumnName + " , " + displayNameColumnName + " , " + columnNames;
		}
		else
		{
			columnNames = idColumnName + " , " + columnNames;
		}
		columnNames = columnNames.substring(0, columnNames.lastIndexOf(","));
		columnNames = columnNames + ";" + index;
		return columnNames;
	}

	/**
	 * Returns true if the attribute name can be used to form label for tree node.
	 * @param attrName String
	 * @return true if the attribute name can be used to form label for tree node otherwise returns false
	 */
	public static boolean ifAttributeIsDisplayName(String attrName)
	{
		String[] attrNamesForLabel = Constants.ATTRIBUTE_NAMES_FOR_TREENODE_LABEL;
		for (int i = 0; i < attrNamesForLabel.length; i++)
		{
			String name = attrNamesForLabel[i];
			if (attrName.equalsIgnoreCase(name))
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
		return actionForm;
	}

	/**
	 * When passes treeNumber , this method returns the root node of that tree. 
	 * @param rootOutputTreeNodeList tree deta
	 * @param treeNo number of tree
	 * @return root node of the tree
	 */
	public static OutputTreeDataNode getRootNodeOfTree(
			List<OutputTreeDataNode> rootOutputTreeNodeList, String treeNo)
	{
		for (OutputTreeDataNode node : rootOutputTreeNodeList)
		{
			if (node.getTreeNo() == new Integer(treeNo).intValue())
				return node;
		}
		return null;
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
	public static int searchQuery(HttpServletRequest request, IQuery query, String option)
	{
		HttpSession session = request.getSession(); 
		int status = 0;
		try
		{
			boolean isRulePresentInDag = false;

			if (query != null)
			{
				IConstraints constraints = query.getConstraints();
				Enumeration<IExpressionId> expressionIds = constraints.getExpressionIds();
				while (expressionIds.hasMoreElements())
				{
					IExpressionId id = expressionIds.nextElement();
					if (((Expression) constraints.getExpression(id)).containsRule())
					{
						isRulePresentInDag = true;
						break;
					}
				}
			}
			if (isRulePresentInDag)
			{
				session.setAttribute(AppletConstants.QUERY_OBJECT, query);

				SqlGenerator sqlGenerator = (SqlGenerator) SqlGeneratorFactory.getInstance();
				QueryOutputTreeBizLogic outputTreeBizLogic = new QueryOutputTreeBizLogic();
				String selectSql = sqlGenerator.generateSQL(query);
				Object obj = session.getAttribute(Constants.SESSION_DATA);
				if (obj != null)
				{
					SessionDataBean sessionData = (SessionDataBean) obj;
					outputTreeBizLogic.createOutputTreeTable(selectSql, sessionData);
					List<OutputTreeDataNode> rootOutputTreeNodeList = sqlGenerator
							.getRootOutputTreeNodeList();
					
					status = DAGConstant.SUCCESS;
					int i = 0;
					for (OutputTreeDataNode outnode : rootOutputTreeNodeList)
					{
						Vector<QueryTreeNodeData> treeData = outputTreeBizLogic
								.createDefaultOutputTreeData(i, outnode, sessionData);
						int resultsSize = treeData.size();
						System.out.println("resultsSize    :"+resultsSize);
						if(option == null)
						{
							if (resultsSize == 0)
							{
								status = NO_RESULT_PRESENT;
								return status;
							} else if(resultsSize > Variables.maximumTreeNodeLimit)
							{
								System.out.println();
								status = QueryModuleUtil.RESULTS_MORE_THAN_LIMIT;
								String resultSizeStr = resultsSize+"";
								session.setAttribute(Constants.TREE_NODE_LIMIT_EXCEEDED_RECORDS,resultSizeStr);
								return status;
							}
							session.setAttribute(Constants.TREE_DATA + "_" + i, treeData);
							i += 1;
						}
						else if(option.equalsIgnoreCase("viewAllRecords"))
						{
							session.setAttribute(Constants.TREE_DATA + "_" + i, treeData);
							i += 1;
						}
						else if(option.equalsIgnoreCase("viewLimitedRecords"))
						{
							List<QueryTreeNodeData> limitedRecordsList = treeData.subList(0, Variables.maximumTreeNodeLimit+1);
							Vector limitedTreeData = new Vector<QueryTreeNodeData>();
							limitedTreeData.addAll(limitedRecordsList);
							session.setAttribute(Constants.TREE_DATA + "_" + i, limitedTreeData);
							i += 1;
						}
					}
					int recordsPerPage = 0;
					String recordsPerPageSessionValue = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);
					System.out.println(recordsPerPageSessionValue);
					if (recordsPerPageSessionValue == null)
					{
						recordsPerPage = Integer.parseInt(XMLPropertyHandler
								.getValue(Constants.RECORDS_PER_PAGE_PROPERTY_NAME));
						session.setAttribute(Constants.RESULTS_PER_PAGE, recordsPerPage + "");
					}
					else
						recordsPerPage = new Integer(recordsPerPageSessionValue).intValue();
					session.setAttribute(Constants.TREE_ROOTS, rootOutputTreeNodeList);

					Long noOfTrees = new Long(rootOutputTreeNodeList.size());
					session.setAttribute(Constants.NO_OF_TREES, noOfTrees);
					Map<String, OutputTreeDataNode> uniqueIdNodesMap = QueryObjectProcessor
							.getAllChildrenNodes(rootOutputTreeNodeList);
					session.setAttribute(Constants.ID_NODES_MAP, uniqueIdNodesMap);
					OutputTreeDataNode node = rootOutputTreeNodeList.get(0);
					
					QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
					String parentNodeId = null;
					String treeNo = "0";
					SelectedColumnsMetadata selectedColumnsMetadata = new SelectedColumnsMetadata();
					selectedColumnsMetadata.setDefinedView(false);
					Map<String, List<String>> spreadSheetDatamap = outputSpreadsheetBizLogic
							.createSpreadsheetData(treeNo, node, sessionData, parentNodeId,
									recordsPerPage, selectedColumnsMetadata);

					// Changes added by deepti for performance change
					QuerySessionData querySessionData = (QuerySessionData) spreadSheetDatamap
							.get(Constants.QUERY_SESSION_DATA);
					int totalNumberOfRecords = querySessionData.getTotalNumberOfRecords();
					session.setAttribute(Constants.QUERY_SESSION_DATA, querySessionData);
					session
							.setAttribute(Constants.TOTAL_RESULTS,
									new Integer(totalNumberOfRecords));

					request.setAttribute(Constants.PAGINATION_DATA_LIST, spreadSheetDatamap
							.get(Constants.SPREADSHEET_DATA_LIST));
					session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, spreadSheetDatamap
							.get(Constants.SPREADSHEET_COLUMN_LIST));
					session.setAttribute(Constants.SELECTED_COLUMN_META_DATA, spreadSheetDatamap
							.get(Constants.SELECTED_COLUMN_META_DATA));
					
				}
				else
				{
					status = EMPTY_DAG;
				}
			}
		}
		catch (MultipleRootsException e)
		{
			Logger.out.error(e);
			status = MULTIPLE_ROOT;
		}
		catch (SqlException e)
		{
			Logger.out.error(e);
			status = SQL_EXCEPTION;
		}
		catch (ClassNotFoundException e)
		{
			Logger.out.error(e);
			status = CLASS_NOT_FOUND;
		}
		catch (DAOException e)
		{
			Logger.out.error(e);
			status = DAO_EXCEPTION;
		}
		return status;
	}
}