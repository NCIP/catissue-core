package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.actionForm.DistributionReportForm;
import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.catissuecore.query.Operator;
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.QueryFactory;
import edu.wustl.catissuecore.query.SimpleConditionsNode;
import edu.wustl.catissuecore.query.SimpleQuery;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


public class DistributionReportAction extends Action
{
	public String [] columnNames;
	public Vector setViewElements(String []selectedColumnsList) 
	{
			columnNames=new String[selectedColumnsList.length];
		    Vector vector = new Vector();
		    for(int i=0;i<selectedColumnsList.length;i++)
		    {
		    	StringTokenizer st= new StringTokenizer(selectedColumnsList[i],".");
		    	DataElement dataElement = new DataElement();
		    	while (st.hasMoreTokens())
				{
		    		dataElement.setTable(st.nextToken());
		    		
		    		dataElement.setField(st.nextToken());
		    		columnNames[i]=st.nextToken();
		    		Logger.out.debug("Selected column names in configuration "+columnNames[i]);
		    		//j++;
				}
		        vector.add(dataElement);
		    }
		    //setResultView(vector);
		    return vector;
		    
	}
    /**
     * Overrides the execute method of Action class.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
    	
    	ConfigureResultViewForm configForm = (ConfigureResultViewForm)form;
    	String action = configForm.getAction();
    	Logger.out.debug("Configure/Default action "+action);
    	Long distributionId ;
    	if(!("configure").equals(action))
    	{
    		distributionId = (Long)request.getAttribute(Constants.DISTRIBUTION_ID);
    		configForm.setDistributionId(distributionId);
    		//distributionId = configForm.getSystemIdentifier();
        	Logger.out.debug("distributionId "+distributionId);
        	
    	}
    	else
    	{
    		distributionId = configForm.getDistributionId();
    		Logger.out.debug("distributionId "+distributionId);
    	}
    		
    	AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
    	List list = bizLogic.retrieve(Distribution.class.getName(),Constants.SYSTEM_IDENTIFIER,distributionId);
    	Distribution dist = (Distribution) list.get(0);
    	
    	DistributionReportForm distributionReportForm = new DistributionReportForm();
    	distributionReportForm.setAllValues(dist);
    	
    	Collection distributedItemCollection = dist.getDistributedItemCollection();		
    	
    	String []specimenIds = new String[distributedItemCollection.size()];
    	String []distributedItemIds = new String[distributedItemCollection.size()];
    	int i=0;
    	Iterator itr = distributedItemCollection.iterator();
    	while(itr.hasNext())
    	{
    		DistributedItem item = (DistributedItem)itr.next();
    		Specimen specimen = item.getSpecimen();
    		Logger.out.debug("Specimen "+specimen);
    		Logger.out.debug("Specimen "+specimen.getSystemIdentifier());
    		specimenIds[i] = specimen.getSystemIdentifier().toString();
    		distributedItemIds[i]=item.getSystemIdentifier().toString();
    		i++;
    	}
    	
    	String selectedColumns[] =getSelectedColumns(action,configForm);
    	
		Logger.out.debug("Selected columns length"+selectedColumns.length);
		
		if(selectedColumns.length!=0)
		{
			for(int k=0;k<selectedColumns.length;k++)
			{
				Logger.out.debug("Selected columns in configuration "+selectedColumns[k]);
				
			}
		}
		
		List listOfData = new ArrayList();
		for(int j=0;j<specimenIds.length;j++)
		{
			Collection simpleConditionNodeCollection = new ArrayList();
			Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, "Participant");
			Logger.out.debug("Specimen ID" +specimenIds[j]);
			SimpleConditionsNode simpleConditionsNode = new SimpleConditionsNode();
			simpleConditionsNode.getCondition().setValue(specimenIds[j]);
			simpleConditionsNode.getCondition().getDataElement().setTable("Specimen");
			simpleConditionsNode.getCondition().getDataElement().setField("Identifier");
			simpleConditionsNode.getCondition().getOperator().setOperator("=");
			
		
			SimpleConditionsNode simpleConditionsNode1 = new SimpleConditionsNode();
			simpleConditionsNode1.getCondition().getDataElement().setTable("DistributedItem");
			simpleConditionsNode1.getCondition().getDataElement().setField("Distribution_Id");
			simpleConditionsNode1.getCondition().getOperator().setOperator("=");
			simpleConditionsNode1.getCondition().setValue(distributionId.toString());
			simpleConditionsNode1.setOperator(new Operator("And"));
			
			/*SimpleConditionsNode simpleConditionsNode2 = new SimpleConditionsNode();
			simpleConditionsNode2.getCondition().getDataElement().setTable("DistributedItem");
			simpleConditionsNode2.getCondition().getDataElement().setField("Identifier");
			simpleConditionsNode2.getCondition().getOperator().setOperator("=");
			simpleConditionsNode2.getCondition().setValue(distributedItemIds[j]);
			simpleConditionsNode2.setOperator(new Operator("And"));
			
			simpleConditionNodeCollection.add(simpleConditionsNode2);*/
			simpleConditionNodeCollection.add(simpleConditionsNode1);
			simpleConditionNodeCollection.add(simpleConditionsNode);
			((SimpleQuery)query).addConditions(simpleConditionNodeCollection);
			
			Set tableSet = new HashSet();
			tableSet.add("Participant");
			tableSet.add("Specimen");
			tableSet.add("CollectionProtocolRegistration");
			tableSet.add("SpecimenCollectionGroup");
			tableSet.add("DistributedItem");
			query.setTableSet(tableSet);
			
			Vector vector = setViewElements(selectedColumns);
			query.setResultView(vector);
			
			List list1 = query.execute();
			Logger.out.debug("Size of the Data from the database" +list1.size());
			Iterator listItr = list1.iterator();
			while(listItr.hasNext())
			{
				Logger.out.debug("Data from the database according to the selected columns"+ listItr.next());
			}
			listOfData.add(list1);
		}
		
		if(("configure").equals(action))
    	{
			request.setAttribute("reportName", "configuredReport");
    	}
		else
    	{
			request.setAttribute("reportName", "defaultReport");
    	}
		
		request.setAttribute("distributionReportForm", distributionReportForm);
        request.setAttribute("columnNames", columnNames);
        request.setAttribute("listOfData", listOfData);
        
		return (mapping.findForward("Success"));
}

    public String [] getSelectedColumns(String action,ConfigureResultViewForm form)
    {
    	if(("configure").equals(action))
    	{
    		String selectedColumns[] = form.getSelectedColumnNames();
    		return selectedColumns;
    	}
    	else 
    	{
    		String selectedColumns[] ={"Specimen.IDENTIFIER.Specimen Identifier","Specimen.TYPE.Specimen Type","DistributedItem.QUANTITY.Specimen Quantity"};
    		return selectedColumns;
    	}
    	
    }
}