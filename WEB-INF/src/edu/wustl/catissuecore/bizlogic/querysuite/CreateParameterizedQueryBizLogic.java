package edu.wustl.catissuecore.bizlogic.querysuite;

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
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedCondition;

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
        }
		
	}

	
	private void fetchConditions(Set<Integer> expressionIdset,IQuery query)
	{

		IConstraints constraints = query.getConstraints();
		Enumeration<IExpressionId> expressionIds = constraints
				.getExpressionIds();
		 
		while (expressionIds.hasMoreElements()) 
		{
			IExpression expression = constraints.getExpression(expressionIds.nextElement());
			int expId = expression.getExpressionId().getInt();
			if(expressionIdset!=null  && expressionIdset.contains(expId))
			{
				replaceConditions(expression,constraints);
				
			}
			

		}
		
		
	}
	
	
	private String generateComponentName(int expressionId,AttributeInterface attribute )
	{
		String componentId =""; 
		String attributeName = attribute.getName();
		componentId = expressionId+"_"+ attribute.getId().toString();
		return componentId;
		
	}
	
	
	
	public void replaceConditions(IExpression expression,IConstraints constraints) 
	{
      
		for (int i = 0; i < expression.numberOfOperands(); i++) 
		{
			IExpressionOperand operand = expression.getOperand(i);

			if (operand.isSubExpressionOperand()) 
			{
				IExpression requiredExpression = constraints.getExpression((IExpressionId) operand);
				replaceConditions(requiredExpression,constraints);
			} 
			else 
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
