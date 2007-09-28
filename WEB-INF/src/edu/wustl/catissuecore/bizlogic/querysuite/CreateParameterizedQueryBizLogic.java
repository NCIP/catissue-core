package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.actionForm.querysuite.SaveQueryForm;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IParameterizedCondition;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedCondition;
import edu.wustl.common.querysuite.queryobject.util.QueryUtility;

public class CreateParameterizedQueryBizLogic 
{
	private Map<String,String> displayNamesMap = null;
	public void getParameterizedConditions(IQuery query,HttpServletRequest request,ActionForm actionForm)
	{
		SaveQueryForm saveActionForm = (SaveQueryForm) actionForm;
		String queryString =  saveActionForm.getQueryString();
		
		Set<Integer> expressionIds = new TreeSet<Integer>();  
		displayNamesMap = new HashMap<String,String>(); 
        if(queryString!=null)
        {
        	
        	StringTokenizer strtokenizer = new StringTokenizer(queryString,";");
        	while(strtokenizer.hasMoreTokens())
        	{
        		String token = strtokenizer.nextToken();
        		String subTokens[] = token.split("_");
        		String expId = subTokens[0];
        		
        		 try
        		 {
        		  expressionIds.add(Integer.parseInt(expId));
        		 }
        		 catch(NumberFormatException e)
        		 {
        			 e.printStackTrace();
        		 }
        		 catch(Exception e )
        		 {
        			 e.printStackTrace();
        		 }
        		String displayName = request.getParameter(token+"_displayName");
        		displayNamesMap.put(token, displayName);
        	}
        	if(!expressionIds.isEmpty())
        	{
        		
        	    fetchConditions(expressionIds,query);
        	    
        	}
        	String conditionString = request.getParameter("conditionList");
    		Map<String,String[]> conditionMap = null;
    		if(conditionString!=null)
    		{
    		  try
    		  {	 
    			CreateQueryObjectBizLogic bizLogic = new  CreateQueryObjectBizLogic();
    			conditionMap = bizLogic.createConditionsMap(conditionString);
    		  }
    		  catch(Exception exp )
    		  {
    			  
    		  }
    		}
        	if(conditionMap!=null&& !conditionMap.isEmpty())
        	{
        		processInputData(conditionMap, query);
        	}
        }
		
	}

	
	public void processInputData(Map<String, String[]> conditions,
			IQuery query) {
		Map<IExpressionId, Map<AttributeInterface, ICondition>> expressionConditionMap = QueryUtility.getSelectedConditions(query);

		
		Set<Map.Entry<IExpressionId, Map<AttributeInterface, ICondition>>> mapEntries = expressionConditionMap
				.entrySet();
		for (Map.Entry<IExpressionId, Map<AttributeInterface, ICondition>> entry : mapEntries) {
			IExpressionId id = entry.getKey();
			Map<AttributeInterface, ICondition> map = entry.getValue();
			Set<Map.Entry<AttributeInterface, ICondition>> attrEntries = map
					.entrySet();
			for (Map.Entry<AttributeInterface, ICondition> attrEntry : attrEntries) {
				AttributeInterface attribute = attrEntry.getKey();
				String expressionidattrid = id.getInt() + "_"
						+ attribute.getId();
				if (conditions.containsKey(expressionidattrid)) {
					ICondition condition = attrEntry.getValue();
					String[] params = conditions.get(expressionidattrid);
					ArrayList<String> conditionValueList = new ArrayList<String>();
					for (int i = 1; i < params.length; i++) {
						if (params[i] != null)
							conditionValueList.add(params[i]);
					}
					condition.setValues(conditionValueList);
					condition.setRelationalOperator(RelationalOperator
							.getOperatorForStringRepresentation(params[0]));

				}
			}
		}
	}
	
	
	private void fetchConditions(Set<Integer> expressionIdset,IQuery query)
	{

		
				replaceConditions(query);
				
				
		
	}
	
	
	private String generateComponentName(int expressionId,AttributeInterface attribute )
	{
		String componentId =""; 
		String attributeName = attribute.getName();
		componentId = expressionId+"_"+ attribute.getId().toString();
		return componentId;
		
	}
	
	
	
	public void replaceConditions(IQuery query) 
	{
		IConstraints constraints = query.getConstraints();
		Enumeration<IExpressionId> expressionIds = constraints.getExpressionIds();
		 
		while (expressionIds.hasMoreElements()) 
		{
		 IExpression expression = constraints.getExpression(expressionIds.nextElement());
		  for (int i = 0; i < expression.numberOfOperands(); i++) 
		  {
			IExpressionOperand operand = expression.getOperand(i);
			if(operand instanceof IRule)
			{
			 IRule ruleObject = (IRule) operand;
 			 List<ICondition> conditions = ruleObject.getConditions();
			 for(int j=0;j<conditions.size();j++)
			 {
				ICondition condition = conditions.get(j);
				String componentName = generateComponentName(expression.getExpressionId().getInt(),condition.getAttribute());
				if(displayNamesMap!=null && displayNamesMap.containsKey(componentName))
				{
						IParameterizedCondition iparameterizedCondition = new ParameterizedCondition(condition);
						iparameterizedCondition.setName(displayNamesMap.get(componentName));
						conditions.remove(condition);
						conditions.add(j, iparameterizedCondition);
				}
			}
			}	
		}

	 }
	}
	
}
