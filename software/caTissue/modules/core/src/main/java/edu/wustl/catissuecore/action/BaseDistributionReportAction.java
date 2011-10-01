
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.actionForm.DistributionReportForm;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.simplequery.bizlogic.QueryBizLogic;
import edu.wustl.simplequery.bizlogic.SimpleQueryBizLogic;
import edu.wustl.simplequery.query.DataElement;
import edu.wustl.simplequery.query.Operator;
import edu.wustl.simplequery.query.Query;
import edu.wustl.simplequery.query.QueryFactory;
import edu.wustl.simplequery.query.SimpleConditionsNode;
import edu.wustl.simplequery.query.SimpleQuery;

/**
 * This is the Base action class for the Distribution report actions.
 *
 * @author Poornima Govindrao
 */
public abstract class BaseDistributionReportAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(BaseDistributionReportAction.class);

	/**
	 * @param selectedColumnsList
	 *            : selectedColumnsList
	 * @return String[] : String[]
	 */
	protected String[] getColumnNames(String[] selectedColumnsList)
	{
		String[] columnNames = new String[selectedColumnsList.length];
		//bug 13605
		int counter=0;
		columnNames[counter] = selectedColumnsList[counter];	
		counter++;
		columnNames = AppUtility.getColNames(selectedColumnsList, columnNames, 1);
		return columnNames;
	}

	/**
	 * @param dist
	 *            : dist
	 * @return DistributionReportForm : DistributionReportForm
	 * @throws Exception
	 *             : Exception
	 */
	protected DistributionReportForm getDistributionReportForm(Distribution dist) throws Exception
	{
		// For a given Distribution object set the values for Distribution
		// report.
		final DistributionReportForm distributionReportForm = new DistributionReportForm();
		/**
		 * Name : Virender Reviewer: Prafull Calling retrieveForEditMode method
		 * from DefaultBizlogic, this method will call setAllvalue of the
		 * Abstract form. removed distributionReportForm.setAllValues(dist)
		 */
		final DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
		defaultBizLogic.populateUIBean(Distribution.class.getName(), dist.getId(),
				distributionReportForm);
		return distributionReportForm;
	}

	/**
	 * @param distributionId
	 *            : distributionId
	 * @param sessionDataBean
	 *            : sessionDataBean
	 * @param securityParam
	 *            : securityParam
	 * @return Distribution : Distribution
	 * @throws Exception
	 *             : Exception
	 */
	protected Distribution getDistribution(Long distributionId, SessionDataBean sessionDataBean,
			int securityParam) throws Exception
	{
		// For a given Distribution ID retrieve the distribution object
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
		final Object object = bizLogic.retrieve(Distribution.class.getName(), distributionId);
		final Distribution dist = (Distribution) object;
		return dist;
	}

	/**
	 * @param dist
	 *            : dist
	 * @param configForm
	 *            : configForm
	 * @param sessionData
	 *            : sessionData
	 * @return List : List
	 * @throws Exception
	 *             : Exception
	 */
	protected List getListOfData(Distribution dist, ConfigureResultViewForm configForm,
			SessionDataBean sessionData) throws Exception
	{
		// Get the list of data for Distributed items data for the report.
		final List listOfData = new ArrayList();
		/**
		 * Name : Virender Reviewer: Prafull Retriving collection of Distributed
		 * Items. dist.getDistributedItemCollection();
		 */
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogicObj = factory.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
		final Collection distributedItemCollection = (Collection) bizLogicObj.retrieveAttribute(
				Distribution.class.getName(), dist.getId(), "elements(distributedItemCollection)");
		// Specimen Ids which are getting distributed.
		final String[] specimenIds = new String[distributedItemCollection.size()];
		int i = 0;
		// This string contain list of all comma separated specimen Ids
		// (<Specimen Id 1>, <Specimen Id 2>)
		String listOfSpecimenId = new String();
		final Iterator itr = distributedItemCollection.iterator();
		while (itr.hasNext())
		{
			final DistributedItem item = (DistributedItem) itr.next();
			final Specimen specimen = item.getSpecimen();
			// Logger.out.debug("Specimen "+specimen);
			// Logger.out.debug("Specimen "+specimen.getId());
			if (specimen != null)
			{
				specimenIds[i] = specimen.getId().toString();
				if (listOfSpecimenId.equals(""))
				{
					listOfSpecimenId = "(" + specimenIds[i];
				}
				else
				{
					listOfSpecimenId = listOfSpecimenId + "," + specimenIds[i];
				}
				i++;
			}
		}
		if (listOfSpecimenId.length() == 0)
		{
			return listOfData;

		}
		listOfSpecimenId = listOfSpecimenId + ")";
		final String action = configForm.getNextAction();

		this.logger.debug("Configure/Default action " + action);
		final String selectedColumns[] = this.getSelectedColumns(action, configForm, false);
		this.logger.debug("Selected columns length" + selectedColumns.length);
		final Collection simpleConditionNodeCollection = new ArrayList();
		final Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY,
				Query.PARTICIPANT);

		this.logger.debug("Specimen IDs" + listOfSpecimenId);
		final SimpleConditionsNode simpleConditionsNode = new SimpleConditionsNode();
		simpleConditionsNode.getCondition().setValue(listOfSpecimenId);
		simpleConditionsNode.getCondition().getDataElement().setTableName(Query.SPECIMEN);
		simpleConditionsNode.getCondition().getDataElement().setField("Identifier");
		simpleConditionsNode.getCondition().getDataElement().setFieldType(
				Constants.FIELD_TYPE_BIGINT);
		simpleConditionsNode.getCondition().getOperator().setOperator(Operator.IN);

		final SimpleConditionsNode simpleConditionsNode1 = new SimpleConditionsNode();
		simpleConditionsNode1.getCondition().getDataElement().setTableName(
				Constants.DISTRIBUTED_ITEM);
		simpleConditionsNode1.getCondition().getDataElement().setField("Distribution_Id");
		simpleConditionsNode1.getCondition().getDataElement().setFieldType(
				Constants.FIELD_TYPE_BIGINT);
		simpleConditionsNode1.getCondition().getOperator().setOperator(Operator.EQUAL);
		simpleConditionsNode1.getCondition().setValue(dist.getId().toString());
		simpleConditionsNode1.setOperator(new Operator(Constants.AND_JOIN_CONDITION));

		simpleConditionNodeCollection.add(simpleConditionsNode1);
		simpleConditionNodeCollection.add(simpleConditionsNode);
		((SimpleQuery) query).addConditions(simpleConditionNodeCollection);
		final Set tableSet = new HashSet();
		tableSet.add(Query.PARTICIPANT);
		tableSet.add(Query.SPECIMEN);
		tableSet.add(Query.COLLECTION_PROTOCOL_REGISTRATION);
		tableSet.add(Query.SPECIMEN_COLLECTION_GROUP);
		tableSet.add(Constants.DISTRIBUTED_ITEM);
		tableSet.add(Query.SPECIMEN_CHARACTERISTICS);
		// Vector vector = setViewElements(selectedColumns);

		// Set the resultViewVector
		final Vector vector = new Vector();
		for (i = 1; i < selectedColumns.length; i++)
		{
			final StringTokenizer st = new StringTokenizer(selectedColumns[i], ".");
			final DataElement dataElement = new DataElement();
			String tableInPath = null;
			while (st.hasMoreTokens())
			{
				dataElement.setTableName(st.nextToken());
				dataElement.setField(st.nextToken());
				st.nextToken();

				if (st.hasMoreTokens())
				{
					tableInPath = st.nextToken();
				}
			}
			// Include the tables in tableSet if tableInPath is not null.
			if (tableInPath != null)
			{
				final StringTokenizer tableInPathTokenizer = new StringTokenizer(tableInPath, ":");
				String aliasName = null;
				while (tableInPathTokenizer.hasMoreTokens())
				{
					final Long tableId = Long.valueOf(tableInPathTokenizer.nextToken());
					final QueryBizLogic bizLogic = (QueryBizLogic) factory
							.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
					aliasName = bizLogic.getAliasName(Constants.TABLE_ID_COLUMN, tableId);
					this.logger.debug("aliasName for from Set :" + aliasName);
				}

				if (aliasName != null)
				{
					tableSet.add(aliasName);
				}
			}
			vector.add(dataElement);
		}
		query.setTableSet(tableSet);
		query.setResultView(vector);

		final Map queryResultObjectDataMap = new HashMap();

		final SimpleQueryBizLogic simpleQueryBizLogic = new SimpleQueryBizLogic();
		simpleQueryBizLogic.createQueryResultObjectData(tableSet, queryResultObjectDataMap, query);

		new ArrayList();
		simpleQueryBizLogic.addObjectIdentifierColumnsToQuery(queryResultObjectDataMap, query);
		simpleQueryBizLogic.setDependentIdentifiedColumnIds(queryResultObjectDataMap, query);
		// List list = query.execute(sessionData, true,queryResultObjectDataMap,
		// false);
		// listOfData.add(list);
		// return listOfData;
		return query.execute(sessionData, true, queryResultObjectDataMap, false);
	}

	/**
	 * @param action
	 *            : action
	 * @param form
	 *            : form
	 * @param specimenArrayDistribution
	 *            : specimenArrayDistribution
	 * @return String[] : String[]
	 */
	protected String[] getSelectedColumns(String action, ConfigureResultViewForm form,
			boolean specimenArrayDistribution)
	{
		// Set the columns according action(Default/Configured report)
		if (("configure").equals(action))
		{
			final String selectedColumns[] = form.getSelectedColumnNames();
			this.logger.debug("Selected columns length" + selectedColumns.length);
			return selectedColumns;
		}
		else
		{
			if (specimenArrayDistribution)
			{
				form.setSelectedColumnNames(Constants.ARRAY_SELECTED_COLUMNS);
				return Constants.ARRAY_SELECTED_COLUMNS;
			}
			else
			{
				form.setSelectedColumnNames(Constants.SELECTED_COLUMNS);
				return Constants.SELECTED_COLUMNS;

			}
		}

	}

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected abstract ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * @param request
	 *            : request
	 */
	protected void setSelectedMenuRequestAttribute(HttpServletRequest request)
	{
		request.setAttribute(Constants.MENU_SELECTED, "16");
	}

	/**
	 * @param key
	 *            : key
	 * @param value
	 *            : value
	 * @param list
	 *            : list
	 * @return List : List
	 */
	protected List createList(String key, String value, List list)
	{
		final List newList = new ArrayList();
		newList.add(ApplicationProperties.getValue(key));
		newList.add(value);
		list.add(newList);
		return list;
	}

	/**
	 * Adds Distribution header.
	 * @param distributionData : distributionData
	 * @param distributionReportForm
	 *            : distributionReportForm
	 * @param report
	 *            : report
	 * @throws IOException
	 *             : IOException
	 */
	protected void addDistributionHeader(List distributionData,
			DistributionReportForm distributionReportForm, ExportReport report) throws IOException
	{
		distributionData = this.createList("distribution.protocol", distributionReportForm
				.getDistributionProtocolTitle(), distributionData);
		distributionData = this.createList("eventparameters.user", distributionReportForm
				.getUserName(), distributionData);
		distributionData = this.createList("eventparameters.dateofevent", distributionReportForm
				.getDateOfEvent(), distributionData);
		distributionData = this.createList("eventparameters.time", distributionReportForm
				.getTimeInHours()
				+ ":" + distributionReportForm.getTimeInMinutes(), distributionData);
		distributionData = this.createList("distribution.toSite", distributionReportForm
				.getToSite(), distributionData);
		distributionData = this.createList("eventparameters.comments", distributionReportForm
				.getComments(), distributionData);
	}
}