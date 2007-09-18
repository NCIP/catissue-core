package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.impl.ExpressionId;

public class CreateUIFromQueryObjectBizLogic {
	
	private Map<IExpressionId,Map<EntityInterface, List>> expressionMap = null;
	

	public CreateUIFromQueryObjectBizLogic() {
		expressionMap = new HashMap<IExpressionId,Map<EntityInterface, List>>();
	}

	public String getHTMLForSavedQuery(IQuery queryObject,boolean isShowAll) 
	{
		String htmlString = "";
		expressionMap.clear();
		IConstraints constraints = queryObject.getConstraints();
		Enumeration<IExpressionId> expressionIds = constraints
				.getExpressionIds();
		while (expressionIds.hasMoreElements()) 
		{
			IExpression expression = constraints.getExpression(expressionIds
					.nextElement());
			getEntityConditionMap(expression, constraints);

		}

		
		GenerateHtmlForAddLimitsBizLogic generateHtml =	new GenerateHtmlForAddLimitsBizLogic();
		htmlString = generateHtml.generateHTMLForSavedQuery(expressionMap,isShowAll);

		return htmlString;
	}

	public void getEntityConditionMap(IExpression expression,
			IConstraints constraints) {

		for (int i = 0; i < expression.numberOfOperands(); i++) {
			IExpressionOperand operand = expression.getOperand(i);

			if (operand.isSubExpressionOperand()) 
			{
				IExpression requiredExpression = constraints.getExpression((IExpressionId) operand);
				getEntityConditionMap(requiredExpression,constraints);
				

			} 
			else 
			{
				Map<EntityInterface, List> entityConditionMap = new HashMap<EntityInterface, List>();
				IRule ruleObject = (IRule) operand;
				List<ICondition> conditions = ruleObject.getConditions();
				EntityInterface entity = expression.getQueryEntity().getDynamicExtensionsEntity();
				entityConditionMap.put(entity, conditions);
				expressionMap.put(expression.getExpressionId(), entityConditionMap);

			}

		}

	}

}
