
package edu.wustl.catissuecore.util.querysuite;

import java.util.Arrays;
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
import edu.wustl.catissuecore.bizlogic.querysuite.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.QueryResultObjectDataBean;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.metadata.QueryOutputTreeAttributeMetadata;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * This is an utility class to provide methods required for query interface.
 * @author deepti_shelar
 */
/**
 * @author vijay_pande
 *
 */
final public  class QueryModuleUtil
{
	private QueryModuleUtil(){}
		/**
	 * Takes data from the map and generates out put data accordingly so that spreadsheet will be updated.
	 * @param spreadSheetDatamap map which holds data for columns and records.
	 * @return this string consists of two strings seperated by '&', first part is for column names
	 * to be displayed in spreadsheet and the second part is data in the spreadsheet.
	 */
	public static String prepareOutputSpreadsheetDataString(final Map spreadSheetDtmap)
	{
		List<List<String>> dataList = (List<List<String>>) spreadSheetDtmap
				.get(Constants.SPREADSHEET_DATA_LIST);
		StringBuffer dataStr = new StringBuffer();		
		String columns = getColumnsAsString(spreadSheetDtmap);
		dataStr.append(columns).append("&");
		getGridDetails(dataList, dataStr);
		return dataStr.toString();
	}

	/**
	 * @param dataList
	 * @param dataStr
	 */
	private static void getGridDetails(List<List<String>> dataList, StringBuffer dataStr) 
	{
		StringBuffer gridStrBuff = new StringBuffer();
		Object gridObj;
		String gridStr;
		for (List<String> row : dataList)
		{
			for (Object columnData : row)
			{
				gridObj = (Object) Utility.toNewGridFormat(columnData);
				gridStr = gridObj.toString();
				gridStrBuff.append(gridStr);
				gridStrBuff.append(',');	
			}
			dataStr.append('|').append(gridStrBuff.toString());
		}
	}

	/**This will get the column's data as String format.
	 * @param spreadSheetDtmap
	 * @return String
	 */
	private static String getColumnsAsString(final Map spreadSheetDtmap) 
	{
		List columnsList = (List) spreadSheetDtmap.get(Constants.SPREADSHEET_COLUMN_LIST);
		String columns = columnsList.toString();
		columns = columns.replace("[", "");
		columns = columns.replace("]", "");
		return columns;
	}
	
	/**
	 * Forms select part of the query.
	 * @param attributes
	 * @param queryDetailsObj
	 * @param queryResultObjectDataBean
	 * @return String having all columnnames for select part.
	 */
	public static Map<String, String> getColumnNamesForSelectpart(List<QueryOutputTreeAttributeMetadata> attributes,
			QueryDetails queryDetailsObj, QueryResultObjectDataBean queryResultObjectDataBean)
	{
		String columnNames = "";
		String idColumnName = null;
		String dspNameColName = null;		
		String index = null;
		String columnName;
		int columIndex = 0;
		AttributeInterface attribute;
		Vector<Integer> objectColumnIdsVector = new Vector<Integer>();		
		Vector<Integer> idvector = new Vector<Integer>();		
		for (QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
		{
			attribute = attributeMetaData.getAttribute();
			columnName = attributeMetaData.getColumnName();
			if (idColumnName != null && dspNameColName != null)
			{
				break;
			}
			if (Constants.ID.equals(attribute.getName()))
			{
				idColumnName = columnName;
				if(queryResultObjectDataBean.isMainEntity())
				{
					   queryResultObjectDataBean.setMainEntityIdentifierColumnId(0);
				}
					else
					   {
						queryResultObjectDataBean.setEntityId(0);
					   }
				objectColumnIdsVector.add(0);
				columIndex++;				
			}
			else if (isPresentInArray(attribute.getName(), Constants.ATTRIBUTE_NAMES_FOR_TREENODE_LABEL))
			{
				index = columnName.substring(Constants.COLUMN_NAME.length(), columnName.length());
				if(attribute.getIsIdentified()!=null)
				{
				 idvector.add(1);
				}
				objectColumnIdsVector.add(1);				
				queryResultObjectDataBean.setIdentifiedDataColumnIds(idvector);
				dspNameColName = columnName;
				columIndex++;
			}
		}
		queryResultObjectDataBean.setObjectColumnIds(objectColumnIdsVector);
		if (dspNameColName != null)
		{
			columnNames = idColumnName + " , " + dspNameColName;	// + " , " + columnNames;
		}
		else
		{
			columnNames = idColumnName;
		}
	//	columnNames = columnNames.substring(0, columnNames.lastIndexOf(","));
		Map<EntityInterface, Integer> entityIdIndexMap =new HashMap<EntityInterface, Integer>();
		if(queryResultObjectDataBean.getIdentifiedDataColumnIds().size()!=0)
		{
		  queryResultObjectDataBean.setHasAssociatedIdentifiedData(true);
		}
		if (!queryResultObjectDataBean.isMainEntity())
		{
			columnNames = QueryCSMUtil.updateEntityIdIndexMap(queryResultObjectDataBean, columIndex,
					columnNames, null, entityIdIndexMap, queryDetailsObj);
		}
		Map<String,String> colNameIndexMap = new HashMap<String,String>();
		colNameIndexMap.put(Constants.COLUMN_NAMES, columnNames);
		colNameIndexMap.put(Constants.INDEX, index);
		return colNameIndexMap;
	}

	/**
	 * Returns true if the attribute name can be used to form label for tree node.
	 * @param objectName
	 * @param stringArray
	 * @return true if the attribute name can be used to form label for tree node otherwise returns false
	 */
	public static boolean isPresentInArray(String objectName, String[] stringArray)
	{	
		boolean isPresentInArray = false;
		int strLen = stringArray.length;
		for (int i = 0; i < strLen; i++)
		{
			String name = stringArray[i];
			if (name.equals(objectName))
			{
				isPresentInArray = true;
			}
		}
		return isPresentInArray ;
	}
	/**
	 * This is used to set the default selections for the UI components when 
	 * the screen is loaded for the first time.
	 * @param actionForm form bean
	 * @return CategorySearchForm formbean
	 */
	public static CategorySearchForm setDefaultSelections(CategorySearchForm actionForm)
	{
		if (actionForm.getClassChecked() == null)
		{
			actionForm.setClassChecked(Constants.ON);
		}
		if (actionForm.getAttributeChecked() == null)
		{
			actionForm.setAttributeChecked(Constants.ON);
		}
		if (actionForm.getPermissibleValuesChecked() == null)
		{
			actionForm.setPermissibleValuesChecked(Constants.OFF);
		}
		if (actionForm.getIncludeDescriptionChecked() == null)
		{
			actionForm.setIncludeDescriptionChecked(Constants.OFF);
		}
		//TODO check if null and then set the value of seleted.
		// Bug #5131: Commenting until the Concept Code search is fixed
		// actionForm.setSelected(Constants.TEXT_RADIOBUTTON);
		actionForm.setTextField("");
		actionForm.setPermissibleValuesChecked(Constants.OFF);
		return actionForm;
	}

	/**
	 * When passes treeNumber , this method returns the root node of that tree. 
	 * @param rootOutputTreeNodeList tree data
	 * @param treeNo number of tree
	 * @return root node of the tree
	 */
	public static OutputTreeDataNode getRootNodeOfTree(QueryDetails queryDetailsObj, String treeNo)
	{
		OutputTreeDataNode getRootNodeOfTree = null;
		//List<OutputTreeDataNode> rootOpTreeNodeLst =queryDetailsObj.getRootOutputTreeNodeList();
		for (OutputTreeDataNode node : queryDetailsObj.getRootOutputTreeNodeList())
		{
			if (node.getTreeNo() == Integer.valueOf(treeNo))
			{
				getRootNodeOfTree = node;
			}
		}
		return getRootNodeOfTree;
	}

	/**
	 * Returns column name of nodes id when passed a node to it
	 * @param node {@link OutputTreeDataNode}
	 * @return String id Columns name
	 */
	public static String getParentIdColumnName(OutputTreeDataNode node)
	{
		String getParenIdColName=null;
		if (node != null)
		{
			List<QueryOutputTreeAttributeMetadata> attributes = node.getAttributes();
			for (QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
			{
				AttributeInterface attribute = attributeMetaData.getAttribute();
				if (Constants.ID.equals(attribute.getName()))
				{
					String sqlColumnName = attributeMetaData.getColumnName();
					getParenIdColName = sqlColumnName;
				}
			}
		}
		return getParenIdColName;
	}

	/**
	 * Sets required data for grid.
	 * @param request HTTPRequest
	 * @param spreadSheetDtmap Map to store spreadsheet data
	 */
	public static void setGridData(HttpServletRequest request, Map spreadSheetDtmap)
	{
		int pageNum = Constants.START_PAGE;
		SelectedColumnsMetadata selColumnsMdata = (SelectedColumnsMetadata) spreadSheetDtmap
				.get(Constants.SELECTED_COLUMN_META_DATA);
		//OutputTreeDataNode object = selectedColumnsMetadata.getCurrentSelectedObject();
		HttpSession session = request.getSession();
		//session.setAttribute(Constants.CURRENT_SELECTED_OBJECT,object);
		request.setAttribute(Constants.PAGE_NUMBER, Integer.toString(pageNum));
		QuerySessionData querySessionData = (QuerySessionData) spreadSheetDtmap
				.get(Constants.QUERY_SESSION_DATA);
		int totalNoOfRecords = querySessionData.getTotalNumberOfRecords();
		List<List<String>> dataList = (List<List<String>>) spreadSheetDtmap
				.get(Constants.SPREADSHEET_DATA_LIST);
		//request.setAttribute(Constants.SPREADSHEET_DATA_LIST,dataList);
		request.setAttribute(Constants.PAGINATION_DATA_LIST, dataList);
		List columnsList = (List) spreadSheetDtmap.get(Constants.SPREADSHEET_COLUMN_LIST);
		if (columnsList != null)
		{
			session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnsList);
		}
		session.setAttribute(Constants.TOTAL_RESULTS, Integer.valueOf(totalNoOfRecords));
		session.setAttribute(Constants.QUERY_SESSION_DATA, querySessionData);
		session.setAttribute(Constants.SELECTED_COLUMN_META_DATA, selColumnsMdata);
		session.setAttribute(Constants.QUERY_REASUL_OBJECT_DATA_MAP, spreadSheetDtmap
				.get(Constants.QUERY_REASUL_OBJECT_DATA_MAP));
		session.setAttribute(Constants.DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP,spreadSheetDtmap
				.get(Constants.DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP));
		session.setAttribute(Constants.MAIN_ENTITY_MAP, spreadSheetDtmap.get(Constants.MAIN_ENTITY_MAP));
		String pageOf = (String) request.getParameter(Constants.PAGEOF);
		if (pageOf == null)
		{
			pageOf = Constants.PAGE_OF_QUERY_MODULE;
		}
		request.setAttribute(Constants.PAGEOF, pageOf);
	}

	/**
	 * 
	 * @param query
	 * @return boolean
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
	 * checks if the current view contains any orderable entity and if the 'id' attribute of the entity is 
	 * included in the view.
	 * @param selectedColumnsMetadata - gives current entity and attribute list
	 * @param cart - gives the attribute list of the entities present in the cart.
	 * @return message if if the 'id' attribute of the orderable entity is not included in view.
	 */
	public static String getMessageIfIdNotPresentForOrderableEntities(SelectedColumnsMetadata
			selectedColumnsMetadata, QueryShoppingCart cart)
	{
		String message = null;
		QueryShoppingCartBizLogic queryShoppingCartBizLogic = new QueryShoppingCartBizLogic();
		boolean areListsequal = true;
		boolean isOrderableEntityPresent = false;
		boolean isAttribIdIncludedInView = false;
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
				int indexArray[] = queryShoppingCartBizLogic
					.getNewAttributeListIndexArray(cartAttributeList, currentAttributeList);
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
			QueryOutputTreeAttributeMetadata element;
			while (iterator.hasNext())
			{
				 element = (QueryOutputTreeAttributeMetadata) iterator.next();
				if (orderableEntityNameList.contains(element.getAttribute().getEntity().getName()))
				{
					isOrderableEntityPresent = true;
					if(element.getAttribute().getName().equals(Constants.ID))
					{
						isAttribIdIncludedInView = true;
						break;
					}
				}
			}
			if ((isOrderableEntityPresent) && (!isAttribIdIncludedInView))
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
		 QueryModuleError errorCode = null;
		 QueryModuleSearchQueryUtil QMSearchQuery = new QueryModuleSearchQueryUtil(request, parameterizedQuery);
		 if (isRulePresentInDag)
		 {
			 errorCode = QMSearchQuery.searchQuery(null);
		 }
		 else
		 {
			 errorCode = QueryModuleError.EMPTY_DAG;
		 }	
		 switch (errorCode)
		 { 
			 case EMPTY_DAG :
				 errorMessage = "<li><font color='blue' family='arial,helvetica,verdana,sans-serif'>" + ApplicationProperties.getValue("query.empty.dag")+ "</font></li>";
				 break;
			 case MULTIPLE_ROOT :
				 errorMessage = "<li><font color='red'> " + ApplicationProperties.getValue("errors.executeQuery.multipleRoots") + "</font></li>";
				 break;
			 case NO_RESULT_PRESENT :
				 errorMessage = ApplicationProperties.getValue("query.zero.records.present");
				 break;
			 case SQL_EXCEPTION :
			 case CLASS_NOT_FOUND :
				 errorMessage = "<li><font color='red'> " + ApplicationProperties.getValue("errors.executeQuery.genericmessage") + "</font></li>";
				 break;
			 case DAO_EXCEPTION :
				 errorMessage = "<li><font color='red'> " + ApplicationProperties.getValue("errors.upgradequery.message") + "</font></li>";
				 break;
			 case RESULTS_MORE_THAN_LIMIT :
				 errorMessage = Constants.TREE_NODE_LIMIT_EXCEEDED_RECORDS;
				 break;
			default:
				 errorMessage= null;
		 }
		 return errorMessage;
	 }
	 
		/**
		 *  It will generate ramdom number
		 * @param session
		 * @return String
		 */
		public static String generateRandomNumber(HttpSession session){
			String randomNumber="";
			if(session.getAttribute(Constants.RANDOM_NUMBER) == null)
			{
				int number =(int) (Math.random()*100000);
				randomNumber = Constants.UNDERSCORE + Integer.toString(number);			
				session.setAttribute(Constants.RANDOM_NUMBER, randomNumber);
			}
			else
			{
				randomNumber = (String)session.getAttribute(Constants.RANDOM_NUMBER);
			}
			return randomNumber;
		}		
}		