/**
 *
 */

package edu.wustl.catissuecore.action;

import java.sql.SQLException;
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
import edu.wustl.query.util.global.AQConstants;
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
 * show the records from the selected entity.
 *
 * @author Juber Patel
 */
public class TitliFetchAction extends Action
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(TitliFetchAction.class);
	/**
	 * alias.
	 */
	private String alias;
	/**
	 * dataList.
	 */
	private List dataList;
	/**
	 * columnNames.
	 */
	private List columnNames;
	/**
	 * id.
	 */
	private int id;
	/**
	 * identifierIndex.
	 */
	private int identifierIndex;

	/**
	 * @param mapping
	 *            the mapping
	 * @param form
	 *            the action form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return action forward
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		// set the request and session attributes required by DataView.jsp and
		// forward
		// for that we need to fetch the selected group of records

		final TitliSearchForm titliSearchForm = (TitliSearchForm) form;

		titliSearchForm.setSortedResultMap((SortedResultMapInterface) (request.getSession()
				.getAttribute(Constants.TITLI_SORTED_RESULT_MAP)));

		final TitliResultGroup resultGroup = titliSearchForm.getSelectedGroup();
		final Collection<SimpleConditionsNode> simpleConditionsNodeCollection = this
				.getSimpleConditionsNodeCollecton(resultGroup, request);
		this.setDataAndColumnLists(simpleConditionsNodeCollection, request);

		try
		{
			request.getSession().setAttribute(AQConstants.IS_SIMPLE_SEARCH, Constants.TRUE);

			// if there is only one record in the selected group, go directly to
			// the edit page
			if (this.dataList.size() == 1)
			{
				final String pageOf = resultGroup.getPageOf();

				final List row = (List) (this.dataList.get(0));

				final String path = Constants.SEARCH_OBJECT_ACTION + "?" + Constants.PAGE_OF + "="
						+ pageOf + "&" + Constants.OPERATION + "=" + Constants.SEARCH + "&"
						+ Constants.SYSTEM_IDENTIFIER + "=" + (String) (row.get(this.id));

				return this.getActionForward(Constants.TITLI_SINGLE_RESULT, path);
			}

			request.setAttribute(Constants.PAGE_OF, resultGroup.getPageOf());
			request.setAttribute(AQConstants.SPREADSHEET_DATA_LIST, this.dataList);
			request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, this.columnNames);
			request.setAttribute(AQConstants.IDENTIFIER_FIELD_INDEX, this.identifierIndex);
			request.setAttribute(Constants.PAGE_NUMBER, 1);
			request.getSession().setAttribute(Constants.TOTAL_RESULTS, this.dataList.size());
			// request.getSession().setAttribute(Constants.RESULTS_PER_PAGE,
			// 10);

		}
		catch (final TitliFetchException e)
		{
			this.logger.error("Exception in TitliFetchAction : " + e.getMessage(), e);
		}
		catch (final Exception e)
		{
			this.logger.error("Exception in TitliFetchAction : " + e.getMessage(), e);
		}

		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 *
	 * @param name : name
	 * @param path : path
	 * @return ActionForward : ActionForward
	 */
	private ActionForward getActionForward(String name, String path)
	{
		final ActionForward actionForward = new ActionForward();
		actionForward.setName(name);
		actionForward.setPath(path);

		return actionForward;
	}

	/**
	 * Create a collection of SimpleConditionsNode as needed to create
	 * SimpleQuery.
	 *
	 * @param resultGroup
	 *            the result group for the selected table
	 * @param request
	 *            the servlet request
	 * @return a collection of SimpleConditionsNode as needed to create
	 *         SimpleQuery
	 */
	Collection<SimpleConditionsNode> getSimpleConditionsNodeCollecton(TitliResultGroup resultGroup,
			HttpServletRequest request)
	{
		final Collection<SimpleConditionsNode> simpleConditionsNodeCollection = new ArrayList<SimpleConditionsNode>();
		final MatchListInterface matchList = resultGroup.getNativeGroup().getMatchList();
		try
		{
			final String maxNoOfRecords = XMLPropertyHandler
					.getValue(Constants.MAXIMUM_RECORDS_FOR_TITLI_RESULTS);
			final int maxLimit = Integer.parseInt(maxNoOfRecords);

			final Name dbName = Titli.getInstance().getDatabases().keySet().toArray(new Name[0])[0];
			final Name tableName = matchList.get(0).getTableName();
			this.setAliasFor(tableName.toString(), request);
			final TableInterface table = Titli.getInstance().getDatabase(dbName)
					.getTable(tableName);

			int count = 1;

			// for each match form a SimpleConditionsNode and add it to the
			// collection
			for (final MatchInterface match : matchList)
			{
				final Name identifier = new Name(Constants.IDENTIFIER);
				final ColumnInterface column = table.getColumn(identifier);
				final String value = match.getUniqueKeys().get(identifier);
				final DataElement dataElement = new DataElement(this.alias, Constants.IDENTIFIER,
						column.getType());
				final Condition condition = new Condition(dataElement,
						new Operator(Operator.EQUAL), value);
				final SimpleConditionsNode simpleConditionsNode = new SimpleConditionsNode(
						condition, new Operator(Operator.OR));

				simpleConditionsNodeCollection.add(simpleConditionsNode);

				if (count >= maxLimit)
				{
					break;
				}
				count++;
			}
		}
		catch (final TitliException e)
		{
			this.logger.error("Exception in TitliFetchAction : " + e.getMessage(), e);
		}
		catch (final DAOException e)
		{
			this.logger.error("DAOException in TitliFetchAction : " + e.getMessage(), e);
		}
		catch (final ClassNotFoundException e)
		{
			this.logger.error("ClassNotFoundException in TitliFetchAction : " + e.getMessage(), e);
		}
		return simpleConditionsNodeCollection;
	}

	/**
	 * Create SimpleQuery from the given collection of SimpleConditionsNode,
	 * execute it and get the resultant data list.
	 *
	 * @param simpleConditionsNodeCollection : simpleConditionsNodeCollection
	 * @param request
	 *            the servlet request
	 */
	private void setDataAndColumnLists(
			Collection<SimpleConditionsNode> simpleConditionsNodeCollection,
			HttpServletRequest request)
	{
		this.dataList = new ArrayList();
		this.columnNames = new ArrayList();
		final HttpSession session = request.getSession();
		final Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, this.alias);
		// Sets the condition objects from user in the query object.
		((SimpleQuery) query).addConditions(simpleConditionsNodeCollection);

		final Map queryResultObjectDataMap = new HashMap();
		final SimpleQueryBizLogic simpleQueryBizLogic = new SimpleQueryBizLogic();

		try
		{
			final List fieldList = new ArrayList();
			final List aliasList = new ArrayList();
			aliasList.add(this.alias);

			if (simpleConditionsNodeCollection != null && !simpleConditionsNodeCollection.isEmpty())
			{
				final Iterator itr = simpleConditionsNodeCollection.iterator();
				while (itr.hasNext())
				{
					final SimpleConditionsNode simpleConditionsNode = (SimpleConditionsNode) itr
							.next();
					final Table table = simpleConditionsNode.getCondition().getDataElement()
							.getTable();
					final DataElement dataElement = simpleConditionsNode.getCondition()
							.getDataElement();
					final String field = table.getTableName() + "." + table.getTableName() + "."
							+ dataElement.getField() + "." + dataElement.getFieldType();
					fieldList.add(field);
				}
			}

			Vector selectDataElements = null;
			selectDataElements = simpleQueryBizLogic.getSelectDataElements(null, aliasList,
					this.columnNames, true, fieldList);
			query.setResultView(selectDataElements);

			final Set fromTables = query.getTableNamesSet();
			query.setTableSet(fromTables);
			simpleQueryBizLogic.createQueryResultObjectData(fromTables, queryResultObjectDataMap,
					query);
			List identifierColumnNames = new ArrayList();
			identifierColumnNames = simpleQueryBizLogic.addObjectIdentifierColumnsToQuery(
					queryResultObjectDataMap, query);
			simpleQueryBizLogic.setDependentIdentifiedColumnIds(queryResultObjectDataMap, query);

			for (int i = 0; i < identifierColumnNames.size(); i++)
			{
				this.columnNames.add(identifierColumnNames.get(i));
			}

			// Get the index of Identifier field of main object.
			final Vector tableAliasNames = new Vector();
			tableAliasNames.add(this.alias);
			final Map tableMap = query.getIdentifierColumnIds(tableAliasNames);
			if (tableMap != null)
			{
				this.identifierIndex = Integer.parseInt(tableMap.get(this.alias).toString()) - 1;
			}

			final boolean isSecureExecute = true;
			final boolean hasConditionOnIdentifiedField = query.hasConditionOnIdentifiedField();
			PagenatedResultData pagenatedResultData = null;

			/*
			 * recordsPerPage = number of results to be displayed on a single
			 * page
			 */
			int recordsPerPage;

			System.out.println(session.getAttribute(Constants.RESULTS_PER_PAGE));
			final String recordsPerPageSessionValue = (String) session
					.getAttribute(Constants.RESULTS_PER_PAGE);
			if (recordsPerPageSessionValue == null)
			{
				recordsPerPage = Integer.parseInt(XMLPropertyHandler
						.getValue(Constants.RECORDS_PER_PAGE_PROPERTY_NAME));
				session.setAttribute(Constants.RESULTS_PER_PAGE, recordsPerPage + "");
			}
			else
			{
				recordsPerPage = new Integer(recordsPerPageSessionValue).intValue();
			}

			pagenatedResultData = query.execute(this.getSessionData(request), isSecureExecute,
					queryResultObjectDataMap, query.hasConditionOnIdentifiedField(), 0,
					recordsPerPage);

			final QuerySessionData querySessionData = new QuerySessionData();
			querySessionData.setSql(query.getString());
			querySessionData.setQueryResultObjectDataMap(queryResultObjectDataMap);
			querySessionData.setSecureExecute(isSecureExecute);
			querySessionData.setHasConditionOnIdentifiedField(hasConditionOnIdentifiedField);
			querySessionData.setRecordsPerPage(recordsPerPage);
			querySessionData.setTotalNumberOfRecords(pagenatedResultData.getTotalRecords());

			session.setAttribute(Constants.QUERY_SESSION_DATA, querySessionData);

			this.dataList = pagenatedResultData.getResult();

			this.id = (Integer) (query.getColumnIdsMap().get(this.alias + "."
					+ Constants.IDENTIFIER)) - 1;

		}
		catch (final DAOException e)
		{
			this.logger.error("DAOException in TitliFetchAction : " + e.getMessage(), e);
		}
		catch (final SQLException e)
		{
			this.logger.error("SQLException in TitliFetchAction : " + e.getMessage(), e);
		}

	}

	/**
	 *
	 * @param tableName : tableName
	 * @param request : request
	 * @throws DAOException : DAOException
	 * @throws ClassNotFoundException : ClassNotFoundException
	 */
	private void setAliasFor(String tableName, HttpServletRequest request) throws DAOException,
			ClassNotFoundException
	{
		// Statement statement;

		// String query=null;

		final String query = "select " + Constants.TABLE_ALIAS_NAME_COLUMN + " from "
				+ Constants.TABLE_DATA_TABLE_NAME + " where " + Constants.TABLE_TABLE_NAME_COLUMN
				+ "='" + tableName + "'";
		final JDBCDAO dao = DAOConfigFactory.getInstance()
				.getDAOFactory(Constants.APPLICATION_NAME).getJDBCDAO();
		dao.openSession(this.getSessionData(request));

		final List list = dao.executeQuery(query);
		final List subList = (List) (list.get(0));
		this.alias = (String) (subList.get(0));
		dao.closeSession();

	}

	/**
	 *
	 * @param request : request
	 * @return SessionDataBean : SessionDataBean
	 */
	protected SessionDataBean getSessionData(HttpServletRequest request)
	{
		final Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
		if (obj != null)
		{
			final SessionDataBean sessionData = (SessionDataBean) obj;
			return sessionData;
		}
		return null;
	}

}
