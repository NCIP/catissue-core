package edu.wustl.catissuecore.action;

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
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.bizlogic.SimpleQueryBizLogic;
import edu.wustl.common.query.DataElement;
import edu.wustl.common.query.Operator;
import edu.wustl.common.query.Query;
import edu.wustl.common.query.QueryFactory;
import edu.wustl.common.query.SimpleConditionsNode;
import edu.wustl.common.query.SimpleQuery;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * This is the Base action class for the Distribution report actions
 * @author Poornima Govindrao
 *  
 */
public abstract class BaseDistributionReportAction extends BaseAction
{
	protected String []getColumnNames(String []selectedColumnsList)
	{
		String [] columnNames=new String[selectedColumnsList.length];
		for(int i=0;i<selectedColumnsList.length;i++)
	    {
	    	/*Split the string which is in the form TableAlias.columnNames.columnDisplayNames
	    	 * to get the column Names 
	    	 */
			StringTokenizer st= new StringTokenizer(selectedColumnsList[i],".");
	    	while (st.hasMoreTokens())
	    	{
	    		st.nextToken();
	    		st.nextToken();
	    		columnNames[i]=st.nextToken();
	    		Logger.out.debug("Selected column names in configuration "+columnNames[i]);
	    		if(st.hasMoreTokens())
	    			st.nextToken();
	    	}
	    }
		return columnNames;
	}
	
	protected DistributionReportForm getDistributionReportForm(Distribution dist) throws Exception
	{
		//For a given Distribution object set the values for Distribution report.
		DistributionReportForm distributionReportForm = new DistributionReportForm();
		distributionReportForm.setAllValues(dist);
		return distributionReportForm;
	}
	
	
    protected Distribution getDistribution(Long distributionId, SessionDataBean sessionDataBean, int securityParam)throws Exception
    {
    	//For a given Distribution ID retrieve the distribution object
    	IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DISTRIBUTION_FORM_ID);
    	List list = bizLogic.retrieve(Distribution.class.getName(),Constants.SYSTEM_IDENTIFIER,distributionId);
    	Distribution dist = (Distribution) list.get(0);
    	return dist;
    }
    	
    protected List getListOfData(Distribution dist, ConfigureResultViewForm configForm,SessionDataBean sessionData) throws Exception
	{
    	//Get the list of data for Distributed items data for the report.
    	List listOfData = new ArrayList();
    	Collection distributedItemCollection = dist.getDistributedItemCollection();		
    	//Specimen Ids which are getting distributed.
    	String []specimenIds = new String[distributedItemCollection.size()];
    	int i=0;
    	Iterator itr = distributedItemCollection.iterator();
    	
    	while(itr.hasNext())
    	{
    		DistributedItem item = (DistributedItem)itr.next();
    		Specimen specimen = item.getSpecimen();
    		Logger.out.debug("Specimen "+specimen);
    		Logger.out.debug("Specimen "+specimen.getId());
    		specimenIds[i] = specimen.getId().toString();
    		i++;
    	}
    	String action = configForm.getNextAction();
    	
    	Logger.out.debug("Configure/Default action "+action);
    	String selectedColumns[] = getSelectedColumns(action,configForm,false);
    	Logger.out.debug("Selected columns length"+selectedColumns.length);
    	for(int j=0;j<specimenIds.length;j++)
    	{
    		Collection simpleConditionNodeCollection = new ArrayList();
    		Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, Query.PARTICIPANT);
    		Logger.out.debug("Specimen ID" +specimenIds[j]);
    		SimpleConditionsNode simpleConditionsNode = new SimpleConditionsNode();
    		simpleConditionsNode.getCondition().setValue(specimenIds[j]);
    		simpleConditionsNode.getCondition().getDataElement().setTableName(Query.SPECIMEN);
    		simpleConditionsNode.getCondition().getDataElement().setField("Identifier");
    		simpleConditionsNode.getCondition().getDataElement().setFieldType(Constants.FIELD_TYPE_BIGINT);
    		simpleConditionsNode.getCondition().getOperator().setOperator(Operator.EQUAL);
    		
    		
    		SimpleConditionsNode simpleConditionsNode1 = new SimpleConditionsNode();
    		simpleConditionsNode1.getCondition().getDataElement().setTableName(Constants.DISTRIBUTED_ITEM);
    		simpleConditionsNode1.getCondition().getDataElement().setField("Distribution_Id");
    		simpleConditionsNode1.getCondition().getDataElement().setFieldType(Constants.FIELD_TYPE_BIGINT);
    		simpleConditionsNode1.getCondition().getOperator().setOperator(Operator.EQUAL);
    		simpleConditionsNode1.getCondition().setValue(dist.getId().toString());
    		simpleConditionsNode1.setOperator(new Operator(Constants.AND_JOIN_CONDITION));
    		
    		simpleConditionNodeCollection.add(simpleConditionsNode1);
    		simpleConditionNodeCollection.add(simpleConditionsNode);
    		((SimpleQuery)query).addConditions(simpleConditionNodeCollection);
    		Set tableSet = new HashSet();
    		tableSet.add(Query.PARTICIPANT);
    		tableSet.add(Query.SPECIMEN);
    		tableSet.add(Query.COLLECTION_PROTOCOL_REGISTRATION);
    		tableSet.add(Query.SPECIMEN_COLLECTION_GROUP);
    		tableSet.add(Constants.DISTRIBUTED_ITEM);
			tableSet.add(Query.SPECIMEN_CHARACTERISTICS);
			//Vector vector = setViewElements(selectedColumns);

			//Set the resultViewVector
    	    Vector vector = new Vector();
    	    for(i=0;i<selectedColumns.length;i++)
    	    {
    	    	StringTokenizer st= new StringTokenizer(selectedColumns[i],".");
    	    	DataElement dataElement = new DataElement();
    	    	String tableInPath = null;
    	    	while (st.hasMoreTokens())
    			{
    	    		dataElement.setTableName(st.nextToken());
    	    		dataElement.setField(st.nextToken());
    	    		st.nextToken();
    	    		
    	    		if(st.hasMoreTokens())
    	    		{
    	    			tableInPath=st.nextToken();
    	    		}
    	    	}
    	    	//Include the tables in tableSet if tableInPath is not null.
    	    	if (tableInPath != null)
    			{
    			    StringTokenizer tableInPathTokenizer = new StringTokenizer(tableInPath, ":");
    			    String aliasName = null;
    				while (tableInPathTokenizer.hasMoreTokens())
    				{
    				    Long tableId = Long.valueOf(tableInPathTokenizer.nextToken());
    				    QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory.getInstance()
    				    							.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
    				    aliasName = bizLogic.getAliasName(Constants.TABLE_ID_COLUMN, tableId);
    				    Logger.out.debug("aliasName for from Set**************************"+aliasName);
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
    		
    		Map queryResultObjectDataMap = new HashMap();

    		SimpleQueryBizLogic simpleQueryBizLogic = new SimpleQueryBizLogic();
			simpleQueryBizLogic.createQueryResultObjectData(tableSet,
												queryResultObjectDataMap,query);
		
			List identifierColumnNames = new ArrayList();
			identifierColumnNames = simpleQueryBizLogic
						.addObjectIdentifierColumnsToQuery(queryResultObjectDataMap, query);
			simpleQueryBizLogic.setDependentIdentifiedColumnIds(queryResultObjectDataMap, query);

			List list = query.execute(sessionData, true,queryResultObjectDataMap, false);
    		listOfData.add(list);
    	}
    	return listOfData;
    }
    
    protected String [] getSelectedColumns(String action,ConfigureResultViewForm form,boolean specimenArrayDistribution)
    {
    	//Set the columns according action(Default/Configured report)
    	if(("configure").equals(action))
    	{
    		String selectedColumns[] = form.getSelectedColumnNames();
    		Logger.out.debug("Selected columns length"+selectedColumns.length);
    		return selectedColumns;
    	}
    	else {
			if (specimenArrayDistribution) {
				form.setSelectedColumnNames(Constants.ARRAY_SELECTED_COLUMNS);
				return Constants.ARRAY_SELECTED_COLUMNS;
			} else {
				form.setSelectedColumnNames(Constants.SELECTED_COLUMNS);
				return Constants.SELECTED_COLUMNS;

			}
		}
    	
    }
    protected abstract ActionForward executeAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

    protected void setSelectedMenuRequestAttribute(HttpServletRequest request)
    {
    	request.setAttribute(Constants.MENU_SELECTED,new String("16") );
    }
    
	protected List createList(String key,String value,List list)
	{
		List newList = new ArrayList();
		newList.add(ApplicationProperties.getValue(key));
		newList.add(value);
		list.add(newList);
		return list;
	}
}