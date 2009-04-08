/**
 * 
 */
package edu.wustl.catissuecore.action;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import titli.controller.Name;
import titli.controller.interfaces.ColumnInterface;
import titli.controller.interfaces.MatchInterface;
import titli.controller.interfaces.MatchListInterface;
import titli.controller.interfaces.SortedResultMapInterface;
import titli.controller.interfaces.TableInterface;
import titli.model.Titli;
import titli.model.TitliException;
import titli.model.fetch.TitliFetchException;
import titli.model.util.TitliResultGroup;
import edu.wustl.catissuecore.actionForm.TitliSearchForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.PagenatedResultData;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.QuerySessionData;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.simplequery.bizlogic.SimpleQueryBizLogic;
import edu.wustl.simplequery.query.Condition;
import edu.wustl.simplequery.query.DataElement;
import edu.wustl.simplequery.query.Operator;
import edu.wustl.simplequery.query.Query;
import edu.wustl.simplequery.query.QueryFactory;
import edu.wustl.simplequery.query.SimpleConditionsNode;
import edu.wustl.simplequery.query.SimpleQuery;
import edu.wustl.simplequery.query.Table;
/**
 * show the records from the selected entity
 * @author Juber Patel
 * 
 */
public class TitliFetchAction extends Action
{
	private String alias;
	private List dataList;
	private List columnNames;
	private int id;
	private int identifierIndex;

	/**
	 * @param mapping the mapping
	 * @param form the action form
	 * @param request the request
	 * @param response the response
	 * @return action forward
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response)
	{  
		// set the request and session attributes required by DataView.jsp and forward
		// for that we need to fetch the selected group of records
  
		TitliSearchForm titliSearchForm = (TitliSearchForm) form;
		
		
		titliSearchForm.setSortedResultMap((SortedResultMapInterface) (request	.getSession().getAttribute(Constants.TITLI_SORTED_RESULT_MAP)));
		
		TitliResultGroup resultGroup = titliSearchForm.getSelectedGroup();
		Collection<SimpleConditionsNode> simpleConditionsNodeCollection = getSimpleConditionsNodeCollecton(resultGroup, request);
		setDataAndColumnLists(simpleConditionsNodeCollection, request);
		
		try
		{
			request.getSession().setAttribute(edu.wustl.common.util.global.Constants.IS_SIMPLE_SEARCH, Constants.TRUE);
			
			//if there is only one record in the selected group, go directly to the edit page
			if(dataList.size()==1)
			{
				String pageOf=resultGroup.getPageOf(); 
				
				List row = (List)(dataList.get(0));
																		
				String path = Constants.SEARCH_OBJECT_ACTION + "?" + Constants.PAGE_OFF + "="
									+ pageOf + "&" + Constants.OPERATION + "="
									+ Constants.SEARCH + "&" + Constants.SYSTEM_IDENTIFIER + "="
									+ (String)(row.get(id));
	
				return getActionForward(Constants.TITLI_SINGLE_RESULT, path);
			}
			 
			
			request.setAttribute(Constants.PAGE_OF, resultGroup.getPageOf());
			request.setAttribute(Constants.SPREADSHEET_DATA_LIST, dataList);
			request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnNames);
			request.setAttribute(Constants.IDENTIFIER_FIELD_INDEX, identifierIndex);
			request.setAttribute(Constants.PAGE_NUMBER, 1);
			request.getSession().setAttribute(Constants.TOTAL_RESULTS,	dataList.size());
			//request.getSession().setAttribute(Constants.RESULTS_PER_PAGE, 10);

		}
		catch (TitliFetchException e)
		{
			Logger.out.error("Exception in TitliFetchAction : "	+ e.getMessage(), e);
		}
		catch(Exception e)
		{
			Logger.out.error("Exception in TitliFetchAction : "	+ e.getMessage(), e);
		}
		
		
		return mapping.findForward(Constants.SUCCESS);
	}
	
	
	private ActionForward getActionForward(String name, String path)
	{
		ActionForward actionForward = new ActionForward();
		actionForward.setName(name);
		actionForward.setPath(path);

		return actionForward;
	}
	
	
	/**
	 * Create a collection of SimpleConditionsNode as needed to create SimpleQuery 
	 * @param resultGroup the result group for the selected table
	 * @param request the servlet request 
	 * @return a collection of SimpleConditionsNode as needed to create SimpleQuery
	 */
	Collection<SimpleConditionsNode> getSimpleConditionsNodeCollecton(TitliResultGroup resultGroup, HttpServletRequest request)
	{  
		Collection<SimpleConditionsNode> simpleConditionsNodeCollection = new ArrayList<SimpleConditionsNode>();
		MatchListInterface matchList = resultGroup.getNativeGroup().getMatchList();
		try
		{ 
		
			Name dbName = Titli.getInstance().getDatabases().keySet().toArray(new Name[0])[0];
			Name tableName = matchList.get(0).getTableName();
			setAliasFor(tableName.toString(), request);
			TableInterface table = Titli.getInstance().getDatabase(dbName).getTable(tableName);
			
			
			//for each match form a SimpleConditionsNode and add it to the collection
			for(MatchInterface match : matchList)
			{
				Name identifier = new Name(Constants.IDENTIFIER);
				ColumnInterface column = table.getColumn(identifier);
				String value = match.getUniqueKeys().get(identifier);
			    DataElement dataElement = new DataElement(alias, Constants.IDENTIFIER, column.getType());
				Condition condition = new Condition(dataElement, new Operator(Operator.EQUAL), value);
				SimpleConditionsNode simpleConditionsNode  = new SimpleConditionsNode(condition, new Operator(Operator.OR));
				
				simpleConditionsNodeCollection.add(simpleConditionsNode);
			}
		}
		catch(TitliException e)
		{
			Logger.out.error("Exception in TitliFetchAction : "	+ e.getMessage(), e);
		}
		catch(DAOException e)
		{
			Logger.out.error("DAOException in TitliFetchAction : "	+ e.getMessage(), e);
		}
		catch(ClassNotFoundException e)
		{
			Logger.out.error("ClassNotFoundException in TitliFetchAction : "	+ e.getMessage(), e);
		}
		
		return simpleConditionsNodeCollection;
			
	}
	
	
	/**
	 * Create SimpleQuery from the given collection of SimpleConditionsNode, execute it and get the resultant data list 
	 * @param simpleConditionsNodeCollection
	 * @param request the servlet request
	 * 
	 */
	private  void setDataAndColumnLists(Collection<SimpleConditionsNode> simpleConditionsNodeCollection, HttpServletRequest request)
	{       
		dataList = new ArrayList();
		columnNames = new ArrayList();
		HttpSession session = request.getSession();
		Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, alias);     
		//Sets the condition objects from user in the query object.
		((SimpleQuery) query).addConditions(simpleConditionsNodeCollection);
		 
		Map queryResultObjectDataMap = new HashMap();
		SimpleQueryBizLogic simpleQueryBizLogic = new SimpleQueryBizLogic();
		
				
		try
		{
			List fieldList = new ArrayList();
			List aliasList = new ArrayList();
			aliasList.add(alias); 
						
			if(simpleConditionsNodeCollection != null && !simpleConditionsNodeCollection.isEmpty() ) 
			{
				Iterator itr = simpleConditionsNodeCollection.iterator();
				while(itr.hasNext())
				{
					SimpleConditionsNode simpleConditionsNode = (SimpleConditionsNode) itr.next();
					Table table = simpleConditionsNode.getCondition().getDataElement().getTable();
					DataElement dataElement = simpleConditionsNode.getCondition().getDataElement();
					String field =table.getTableName() + "." + table.getTableName() + "." + dataElement.getField() + "." + dataElement.getFieldType() ;
					fieldList.add(field);
				}
			}		
			
			Vector selectDataElements =null;
			selectDataElements = simpleQueryBizLogic.getSelectDataElements(null, aliasList, columnNames, true, fieldList);
			query.setResultView(selectDataElements);
			
			Set fromTables = query.getTableNamesSet();
			query.setTableSet(fromTables);
			simpleQueryBizLogic.createQueryResultObjectData(fromTables, queryResultObjectDataMap, query);
			List identifierColumnNames = new ArrayList();
			identifierColumnNames = simpleQueryBizLogic.addObjectIdentifierColumnsToQuery(queryResultObjectDataMap, query);
			simpleQueryBizLogic.setDependentIdentifiedColumnIds(queryResultObjectDataMap, query);
			
			for (int i = 0; i < identifierColumnNames.size(); i++)
			{
				columnNames.add((String) identifierColumnNames.get(i));
			}
						
			//Get the index of Identifier field of main object.
			Vector tableAliasNames = new Vector();
			tableAliasNames.add(alias);
			Map tableMap = query.getIdentifierColumnIds(tableAliasNames);
			if (tableMap != null)
			{
				identifierIndex = Integer.parseInt(tableMap.get(alias).toString()) - 1;
			}
			
			boolean isSecureExecute =true ;
			boolean hasConditionOnIdentifiedField = query.hasConditionOnIdentifiedField();
			PagenatedResultData pagenatedResultData=null;
			
			/*
			 * recordsPerPage = number of results to be displayed on a single page
			 */
			int recordsPerPage; 
			   
			System.out.println(session.getAttribute(Constants.RESULTS_PER_PAGE));
			String recordsPerPageSessionValue = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);
			if (recordsPerPageSessionValue==null)
			{
					recordsPerPage = Integer.parseInt(XMLPropertyHandler.getValue(Constants.RECORDS_PER_PAGE_PROPERTY_NAME));
					session.setAttribute(Constants.RESULTS_PER_PAGE, recordsPerPage+"");
			}
			else
				recordsPerPage = new Integer(recordsPerPageSessionValue).intValue();
			
			pagenatedResultData = query.execute(getSessionData(request), isSecureExecute, queryResultObjectDataMap, query.hasConditionOnIdentifiedField(),0 ,recordsPerPage);
			
			QuerySessionData querySessionData = new QuerySessionData();
			querySessionData.setSql(query.getString());
			querySessionData.setQueryResultObjectDataMap(queryResultObjectDataMap);
			querySessionData.setSecureExecute(isSecureExecute);
			querySessionData.setHasConditionOnIdentifiedField(hasConditionOnIdentifiedField);
			querySessionData.setRecordsPerPage(recordsPerPage);
			querySessionData.setTotalNumberOfRecords(pagenatedResultData.getTotalRecords());
			
			session.setAttribute(Constants.QUERY_SESSION_DATA, querySessionData);
			
			dataList = pagenatedResultData.getResult();
	
			id = (Integer)(query.getColumnIdsMap().get(alias+"."+Constants.IDENTIFIER))-1;
							
		}
		catch(DAOException e)
		{
			Logger.out.error("DAOException in TitliFetchAction : "	+ e.getMessage(), e);
		}
		catch(SQLException e)
		{
			Logger.out.error("SQLException in TitliFetchAction : "	+ e.getMessage(), e);
		}
	
	}
	
	
	/**
	 * get the alias for the give table name from appropriate database table
	 * @param tableName the table name for which to get the alias
	 * @return alias for the give table name from appropriate database table
	 */
	private void setAliasFor(String tableName, HttpServletRequest request) throws DAOException,ClassNotFoundException
	{
		Statement statement;
		
		//String query=null;
		
		String query = "select "+Constants.TABLE_ALIAS_NAME_COLUMN+" from "+Constants.TABLE_DATA_TABLE_NAME+" where "+Constants.TABLE_TABLE_NAME_COLUMN+"='"+tableName+"'";
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		dao.openSession(getSessionData(request));
		
		List list  = dao.executeQuery(query, getSessionData(request), false, null);
		
		List subList = (List) (list.get(0)); 
		alias = (String)(subList.get(0));
		dao.closeSession();
		
	}
	
	
	protected SessionDataBean getSessionData(HttpServletRequest request)
	{
		Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
		if (obj != null)
		{
			SessionDataBean sessionData = (SessionDataBean) obj;
			return sessionData;
		}
		return null;
	}


}
