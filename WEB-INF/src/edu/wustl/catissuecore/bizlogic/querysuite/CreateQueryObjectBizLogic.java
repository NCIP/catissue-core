
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.catissuecore.util.querysuite.QueryModuleConstants;
import edu.wustl.catissuecore.util.querysuite.TemporalQueryUtility;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.DSInterval;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.IDateLiteral;
import edu.wustl.common.querysuite.queryobject.IDateOffsetLiteral;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.INumericLiteral;
import edu.wustl.common.querysuite.queryobject.IParameter;
import edu.wustl.common.querysuite.queryobject.IParameterizable;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;
import edu.wustl.common.querysuite.queryobject.impl.DateOffsetLiteral;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
import edu.wustl.common.querysuite.utils.QueryUtility;
import edu.wustl.common.util.Collections;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * Creates Query Object as per the data filled by the user on AddLimits section.
 * This will also validate the inputs and generate messages and they will be
 * shown to user.
 * 
 * @author deepti_shelar
 * 
 */
public class CreateQueryObjectBizLogic
{
	/**
	 * Gets the map which holds the data to create the rule object and add it to query.
	 * 
	 * @param strToCreateQueryObject
	 *            str to create query obj
	 * @param attrCollection            
	 * @return Map rules details
	 * @throws DynamicExtensionsSystemException
	 *             DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 *             DynamicExtensionsApplicationException
	 */
	public Map getRuleDetailsMap(String strToCreateQueryObject, Collection<AttributeInterface> attrCollection)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Map ruleDetailsMap = new HashMap();
		if (attrCollection != null)
		{
			Map conditionsMap = createConditionsMap(strToCreateQueryObject);
			ruleDetailsMap = getEntityDetails(attrCollection, conditionsMap);
		}
		return ruleDetailsMap;
	}
	
	/** 
	 * This method get the entity details and populates the rule details map.
	 * @param attrCollection
	 * @param conditionsMap
	 * @return Map rules details
	 */
	private Map getEntityDetails(Collection<AttributeInterface> attrCollection, Map conditionsMap) 
	{
		String errorMessage = "";
		Map ruleDetailsMap = new HashMap();
		if (conditionsMap != null && !conditionsMap.isEmpty() && attrCollection != null
				&& !attrCollection.isEmpty())
		{
			List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
			List<String> attributeOperators = new ArrayList<String>();
			List<String> secondAttributeValues = new ArrayList<String>();
			ArrayList<ArrayList<String>> conditionValues = new ArrayList<ArrayList<String>>();
			String[] params;
			for(AttributeInterface attr : attrCollection)
			{
				params = paramsValue(conditionsMap, attr);
				if (params != null)
				{
					attributes.add(attr);
					attributeOperators.add(params[QueryModuleConstants.INDEX_PARAM_ZERO]);
					//firstAttributeValues.add(params[Constants.INDEX_PARAM_ONE]);
					secondAttributeValues.add(params[QueryModuleConstants.INDEX_PARAM_TWO]);
					ArrayList<String> attributeValues = getConditionValuesList(params);
					errorMessage = errorMessage
					+ validateAttributeValues(attr.getDataType().trim(), attributeValues);
					if("".equals(errorMessage))
					{
						if (QueryModuleConstants.Between.equals(params[QueryModuleConstants
						                                               .INDEX_PARAM_ZERO]))
						{
							attributeValues = Utility.getAttributeValuesInProperOrder(attr
							.getDataType(),attributeValues.get(QueryModuleConstants
							.ARGUMENT_ZERO), attributeValues.get(1));
						}
						conditionValues.add(attributeValues);
					}
				}
			}
			if("".equals(errorMessage))
			{
				ruleDetailsMap.put(AppletConstants.ATTRIBUTES, attributes);
				ruleDetailsMap.put(AppletConstants.ATTRIBUTE_OPERATORS, attributeOperators);
				//ruleDetailsMap.put(AppletConstants.FIRST_ATTR_VALUES, firstAttributeValues);
				ruleDetailsMap.put(AppletConstants.SECOND_ATTR_VALUES, secondAttributeValues);
				ruleDetailsMap.put(AppletConstants.ATTR_VALUES, conditionValues);
			}
			ruleDetailsMap.put(AppletConstants.ERROR_MESSAGE, errorMessage);
		}
	       return ruleDetailsMap;
	}

	/** 
	 * This method get the name and Id of the component.
	 * @param conditionsMap
	 * @param attr
	 * @return String
	 */
	private String[] paramsValue(Map conditionsMap, AttributeInterface attr)
	{
		String componentId = attr.getName() + attr.getId().toString();
		String[] params = (String[]) conditionsMap.get(componentId);
		return params;
	}

	/**
	 * @param params
	 * @return ArrayList attributeValues
	 */
	private ArrayList<String> getConditionValuesList(String[] params)
	{
		ArrayList<String> attributeValues = new ArrayList<String>();
		if (params[1] != null)
		{
				String[] values = params[1].split(QueryModuleConstants.QUERY_VALUES_DELIMITER);
				int len = values.length;
				for (int i = 0; i < len; i++)
				{
					if(!"".equals(values[i]))
						attributeValues.add(values[i].trim());
				}
		}
		if (params[2] != null)
		{
			attributeValues.add(params[2].trim());
		}
		return attributeValues;
	}

	/**
	 * Validates the user input and populates the list of messages to be shown
	 * to the user on the screen.
	 * 
	 * @param dataType
	 *            String
	 * @param attrvalues
	 *            List<String>
	 * @return String message
	 */
	private String validateAttributeValues(String dataType, List<String> attrvalues)
	{
		Validator validator = new Validator();
		String errorMessages = "";
		for(String enteredValue : attrvalues)
		{
			if (Constants.MISSING_TWO_VALUES.equalsIgnoreCase(enteredValue))
			{
				errorMessages = getErrorMessageForBetweenOperator(errorMessages, enteredValue);
			}
			else if ((QueryModuleConstants.BIG_INT.equalsIgnoreCase(dataType) 
					|| QueryModuleConstants.INTEGER.equalsIgnoreCase(dataType))
					|| QueryModuleConstants.LONG.equalsIgnoreCase(dataType))
			{
				Logger.out.debug(" Check for integer");
				
				if (validator.convertToLong(enteredValue) == null)
				{
					errorMessages = errorMessages + ApplicationProperties
					.getValue("simpleQuery.intvalue.required");
					Logger.out.debug(enteredValue + " is not a valid integer");
				}
				else if (!validator.isPositiveNumeric(enteredValue, QueryModuleConstants.ARGUMENT_ZERO))
				{
					errorMessages = getErrorMessageForPositiveNum(errorMessages, enteredValue);
				}

			}// integer
			else if ((QueryModuleConstants.DOUBLE.equalsIgnoreCase(dataType)) && !validator
					.isDouble(enteredValue, false))
			{
				errorMessages = errorMessages + ApplicationProperties
				.getValue("simpleQuery.decvalue.required");
			} // double
			else if (QueryModuleConstants.TINY_INT.equalsIgnoreCase(dataType))
			{
				if (!QueryModuleConstants.BOOLEAN_YES.equalsIgnoreCase(enteredValue.trim())
						&& !QueryModuleConstants.BOOLEAN_NO.equalsIgnoreCase(enteredValue.trim()))
				{
					errorMessages = errorMessages + ApplicationProperties
					.getValue("simpleQuery.tinyint.format");
				}
			}
			else if (Constants.FIELD_TYPE_TIMESTAMP_TIME.equalsIgnoreCase(dataType))
			{
				errorMessages = getErrorMessageForTimeFormat(validator, errorMessages, enteredValue);
			}
			else if (Constants.FIELD_TYPE_DATE.equalsIgnoreCase(dataType)
					|| Constants.FIELD_TYPE_TIMESTAMP_DATE.equalsIgnoreCase(dataType))
			{
				errorMessages = getErrorMessageForDateFormat(validator, errorMessages, enteredValue);
			}
		}
		return errorMessages;
	}

	/**
	 * This methods returns error message for Positive Number.
	 * @param errorMessages
	 * @param enteredValue
	 * @return string Message
	 */
	private String getErrorMessageForPositiveNum(String errorMessages, String enteredValue)
	{
		errorMessages = errorMessages + "<li><font color\\='red'>" + ApplicationProperties
		.getValue("simpleQuery.intvalue.poisitive.required")+"</font></li>";
		Logger.out.debug(enteredValue + " is not a positive integer");
		return errorMessages;
	}

	/** 
	 * This methods returns error message for between operator.
	 * @param errorMessages
	 * @param enteredValue
	 * @return String Message
	 */
	private String getErrorMessageForBetweenOperator(String errorMessages, String enteredValue)
	{
		errorMessages = errorMessages + "<li><font color\\='red'>" + ApplicationProperties.getValue("simpleQuery.twovalues.required")+"</font></li>";
		Logger.out.debug(enteredValue + " two values required for 'Between' operator ");
		return errorMessages;
	}

	/**
	 * This methods returns error message for Time Format.
	 * @param validator
	 * @param errorMessages
	 * @param enteredValue
	 * @return String Message
	 */
	private String getErrorMessageForTimeFormat(Validator validator, String errorMessages, String enteredValue)
	{
		if (!validator.isValidTime(enteredValue, Constants.TIME_PATTERN_HH_MM_SS))
		{
			errorMessages = errorMessages + "<li><font color\\='red'>" +ApplicationProperties.getValue("simpleQuery.time.format")+"</font></li>";
		}
		return errorMessages;
	}

	/**
	 * This methods returns error message for date Format.
	 * @param validator
	 * @param errorMessages
	 * @param enteredValue
	 * @return String Message
	 */
	private String getErrorMessageForDateFormat(Validator validator, String errorMessages, String enteredValue)
	{
		if (!validator.checkDate(enteredValue))
		{
			errorMessages = errorMessages + "<li><font color\\='red'>" + ApplicationProperties.getValue("simpleQuery.date.format")+"</font></li>";
		}
		return errorMessages;
	}

	/**
	 * Craetes Map of condition Objects.
	 * 
	 * @param queryString
	 *            queryString
	 * @return Map conditions map
	 * @throws DynamicExtensionsApplicationException
	 *             DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 *             DynamicExtensionsSystemException
	 */
	public Map<String, String[]> createConditionsMap(String queryString)
	{
		Map<String, String[]> conditionsMap = new HashMap<String, String[]>();
		String[] conditions = queryString.split(QueryModuleConstants.QUERY_CONDITION_DELIMITER);
		String[] attrParams;
		String condition;
		int len= conditions.length;
		for (int i = 0; i < len; i++)
		{
			attrParams = new String[QueryModuleConstants.INDEX_LENGTH];
			condition = conditions[i];
			if (!condition.equals(""))
			{
				condition = condition.substring(QueryModuleConstants.ARGUMENT_ZERO,
						condition.indexOf(QueryModuleConstants.ENTITY_SEPARATOR));
				String attrName = null;
				StringTokenizer tokenizer = new StringTokenizer(condition,
						QueryModuleConstants.QUERY_OPERATOR_DELIMITER);
				while (tokenizer.hasMoreTokens())
				{
					attrName = tokenizer.nextToken();
					if (tokenizer.hasMoreTokens())
					{
						String operator = tokenizer.nextToken();
						attrParams[QueryModuleConstants.INDEX_PARAM_ZERO] = operator;
						if (tokenizer.hasMoreTokens())
						{
							attrParams[1] = tokenizer.nextToken();
							if (RelationalOperator.Between.toString().equals(operator))
							{
								attrParams[QueryModuleConstants.INDEX_PARAM_TWO]
								= tokenizer.nextToken();
							}
						}
					}
				}
				conditionsMap.put(attrName, attrParams);
			}
		}
		return conditionsMap;
	}


	/**
	 * This method process the input values for the conditions and set it to the conditions in the query
	 * also replaces the conditions with the parameterized conditions
	 * @param queryInputString
	 * @param constraints
	 * @param displayNamesMap
	 * @return String Message
	 */
	public String setInputDataToQuery(String queryInputString, IConstraints constraints,
			Map<String, String> displayNamesMap,IQuery query )
	{   
		String errorMessage = "";
		Map<String, String[]> newConditions = null;
		if (queryInputString != null)
		{
			newConditions = createConditionsMap(queryInputString);
		}
		for(IExpression expression : constraints) {
			int no_of_oprds = expression.numberOfOperands();
			IExpressionOperand operand;
			for (int i = 0; i < no_of_oprds; i++)
			{
				operand = expression.getOperand(i);
					if(operand instanceof IRule)
					{
						int expId = expression.getExpressionId();
						IRule rule = (IRule) operand;
						errorMessage = componentValues(displayNamesMap, errorMessage,
							newConditions, expId,  rule,query);
							if(displayNamesMap == null && (Collections.list((IRule) operand)).size()==0)
						{
							IExpression expression1 = rule.getContainingExpression();
					        AttributeInterface attributeObj = getIdNotNullAttribute(expression1.getQueryEntity()
					                .getDynamicExtensionsEntity());

					        if (attributeObj != null) 
					        {
					            ICondition condition = createIdNotNullCondition(attributeObj);
					            rule.addCondition(condition);
					        }
						}
					}
			}
		}
		return errorMessage;
	}
	 
	public String setInputDataToTQ(IQuery query,String pageOf,String rhsList,Map<Integer,ICustomFormula> customFormulaIndexMap)
	{ 
		String errorMsg = "";
		if(customFormulaIndexMap!=null && !customFormulaIndexMap.isEmpty())
		{   
		
		Map<String, String[]> newRHSMap = getNewRHSMap(rhsList);
	    ParameterizedQuery pQuery = null;
		if (query instanceof ParameterizedQuery)
		{
			pQuery = (ParameterizedQuery) query;
			 
		}
		for (String key : newRHSMap.keySet())
		{
			String[] newRHSValues = newRHSMap.get(key);
			ICustomFormula customFormula = customFormulaIndexMap.get(Integer.parseInt(key));
			String value = "";
			if(customFormula!=null)
			{
			ITerm rhsTerm = QueryObjectFactory.createTerm();
			if((customFormula.getLhs().getTermType()).equals(TermType.DSInterval))
			 {
				value = setDateOffsetRHS(newRHSValues, rhsTerm);
			 }
			else if((customFormula.getLhs().getTermType()).equals(TermType.Timestamp))
			{
				Date date;
				try
				{
					IDateLiteral rhsDateLiteral = null;
					if(newRHSValues.length!=QueryModuleConstants.INDEX_PARAM_TWO)
					{
								date = Utility.parseDate(newRHSValues[QueryModuleConstants.INDEX_PARAM_TWO], Variables.dateFormat);
								rhsDateLiteral = QueryObjectFactory
										.createDateLiteral(new java.sql.Date(date.getTime()));
								value = newRHSValues[QueryModuleConstants.INDEX_PARAM_TWO];
					}
					else
					{
						rhsDateLiteral = QueryObjectFactory.createDateLiteral();
						value = "";
					}
					 rhsTerm.addOperand(rhsDateLiteral);
					
				}
				catch (ParseException e)
				{
					e.printStackTrace();
					errorMsg = e.getMessage();
				}
				
			} else if(customFormula.getLhs().getTermType().equals(TermType.Numeric))
			{
				value = "";
				if(newRHSValues.length == 3)
				{
					value = newRHSValues[QueryModuleConstants.INDEX_PARAM_TWO];
				}
				INumericLiteral numericLiteral = QueryObjectFactory.createNumericLiteral(value);
				rhsTerm.addOperand(numericLiteral);
			}
			customFormula.setOperator(TemporalQueryUtility.getRelationalOperator(newRHSValues[QueryModuleConstants.INDEX_PARAM_ONE]));
			customFormula.getAllRhs().clear();
			customFormula.addRhs(rhsTerm);
			if(pageOf.equalsIgnoreCase(Constants.EXECUTE_QUERY_PAGE))
			{
			updateCFForExecuteQuery(pQuery, customFormula, value);
			}
			
			if(pageOf.equalsIgnoreCase(Constants.SAVE_QUERY_PAGE))
			{
				pQuery = insertParamaters(query, pQuery, customFormula);
			}
		  }
		}
		}
		return errorMsg;
	}

	/**
	 * @param newRHSValues
	 * @param rhsTerm
	 * @return
	 */
	private String setDateOffsetRHS(String[] newRHSValues, ITerm rhsTerm)
	{
		String value;
		IDateOffsetLiteral rhsDateOffsetLiteral = QueryObjectFactory.createDateOffsetLiteral(newRHSValues[QueryModuleConstants.INDEX_PARAM_TWO],TemporalQueryUtility.getTimeInterval(newRHSValues[QueryModuleConstants.INDEX_PARAM_THREE]));
		rhsTerm.addOperand(rhsDateOffsetLiteral);
		value = newRHSValues[QueryModuleConstants.INDEX_PARAM_TWO];
		return value;
	}

	/**
	 * @param query
	 * @param pQuery
	 * @param customFormula
	 * @return
	 */
	private ParameterizedQuery insertParamaters(IQuery query, ParameterizedQuery pQuery,
			ICustomFormula customFormula)
	{
		if(query instanceof ParameterizedQuery)
		{
			pQuery = (ParameterizedQuery)query;
		}
		IParameter<ICustomFormula> parameter = QueryObjectFactory.createParameter(customFormula, null);
		pQuery.getParameters().add(parameter);
		return pQuery;
	}

	/**
	 * @param pQuery
	 * @param customFormula
	 * @param value
	 */
	private void updateCFForExecuteQuery(ParameterizedQuery pQuery, ICustomFormula customFormula,
			String value)
	{
		Collection<ICustomFormula> allParameterizedCustomFormulas = QueryUtility.getAllParameterizedCustomFormulas(pQuery);
		for(ICustomFormula cf :allParameterizedCustomFormulas)
		{
			if(cf.getId().equals(customFormula.getId()))
			{
				Set<IExpression> expressionsInFormula = QueryUtility.getExpressionsInFormula(cf);
				for(IExpression exp : expressionsInFormula)
				{ 
					boolean removeOperand = exp.removeOperand(cf);
					if(removeOperand && !(value.trim().equals("")))
						exp.addOperand(QueryObjectFactory.createLogicalConnector(LogicalOperator.And),customFormula);
				}
			}
		}
	}
	
	private Map<String,String[]> getNewRHSMap(String rhsList)
	{ 
		Map<String,String[]> newRHSMap = new HashMap<String, String[]>();
		String[] rhsArray = rhsList.split(QueryModuleConstants.QUERY_CONDITION_DELIMITER);
		for (int i = 1; i <rhsArray.length; i++)
		{
			String[] split = rhsArray[i].split(QueryModuleConstants.QUERY_PARAMETER_DELIMITER);
			newRHSMap.put(split[0], split);
		}
		return newRHSMap;
	}
	
	/**
     * Check if identifier present in entity.
     * 
     * @param entityInterfaceObj The Entity for which it is required to check if
     *            identifier present.
     * @return Reference to the AttributeInterface if identifier attribute is
     *         present in the entity, else null.
     */
	private AttributeInterface getIdNotNullAttribute(EntityInterface entityInterfaceObj) 
	{	
		Collection<AttributeInterface> attributes = entityInterfaceObj.getEntityAttributesForQuery();
        for (AttributeInterface attribute : attributes) {
            if (attribute.getName().equals(Constants.ID)) {
                return attribute;
            }
        }
        return null;
	}

	/**
     * Creates condition identifier != null'
     * @param attributeObj The attribute on which the condition has to be created(In this case its 'identifier')
     * @return condition that is created on identifier
     */
	private ICondition createIdNotNullCondition(AttributeInterface attributeObj) 
	{
        ICondition condition = QueryObjectFactory.createCondition(attributeObj, RelationalOperator.IsNotNull, null);
        return condition;
    }

	/**
	 * @param displayNamesMap
	 * @param errorMessage
	 * @param newConditions
	 * @param expId
	 * @param conditions
	 * @return String Message
	 */
	private String componentValues(Map<String, String> displayNamesMap, String errorMessage,
			Map<String, String[]> newConditions, int expId, IRule rule ,IQuery query)
	{ 
		ICondition condition;
		String componentName;
		ArrayList<ICondition> removalList = new ArrayList<ICondition>();
		List<ICondition> deafultConditions = new ArrayList<ICondition>();
		int size = rule.size(); 
		ParameterizedQuery pQuery = null;
		if(query instanceof ParameterizedQuery)
		{
			pQuery = (ParameterizedQuery)query;
		}
		for (int j = 0; j < size; j++)
		{
			condition = rule.getCondition(j);
			componentName = generateComponentName(expId, condition.getAttribute());
			if (newConditions != null && newConditions.containsKey(componentName))
			{
				String[] params = newConditions.get(componentName);
				ArrayList<String> attributeValues = getConditionValuesList(params);
				errorMessage = errorMessage + validateAttributeValues(condition
						.getAttribute().getDataType().toString(),attributeValues);
				
				if(displayNamesMap!=null && !(displayNamesMap.containsKey(componentName)))
				{}
				else
				{
					condition.setValues(attributeValues);
					condition.setRelationalOperator(RelationalOperator.getOperatorForStringRepresentation(
							params[QueryModuleConstants.INDEX_PARAM_ZERO]));
				}
			}
			if((!newConditions.containsKey(componentName)) && (displayNamesMap == null))
			{
				removalList.add(condition);
				if(query instanceof ParameterizedQuery)
				{
					pQuery = (ParameterizedQuery)query;
					List<IParameter<?>> parameterList = pQuery.getParameters(); 
					boolean isparameter = false;
					if(parameterList !=null)
					{
					 for (IParameter<?> parameter : parameterList) 
					 {
				            if (parameter.getParameterizedObject() instanceof ICondition) 
				            {
				            	ICondition paramCondition = (ICondition) parameter.getParameterizedObject();
				                if(paramCondition.getId().equals(condition.getId()))
				                	isparameter = true;
				             }
				        }
					}
					if(!isparameter)
							{
								deafultConditions.add(condition);
							}
				}
			}
			if (displayNamesMap != null && displayNamesMap.containsKey(componentName))
			{
				
				IParameter<ICondition> parameter = QueryObjectFactory.createParameter(condition, displayNamesMap.get(componentName));
				pQuery.getParameters().add(parameter);
			}
		}
		for(ICondition removalEntity : removalList)
		{
			if(!deafultConditions.contains(removalEntity))
				rule.removeCondition(removalEntity);
		}
		return errorMessage;
	}
	
	
//	private IParameter<?> isParameterized(ICondition condition, List<IParameter<?>> parameterList)
//	{
//		if(parameterList !=null)
//		{
//		 for (IParameter<?> parameter : parameterList) 
//		 {
//	            if (parameter.getParameterizedObject() instanceof ICondition) 
//	            {
//	            	ICondition paramCondition = (ICondition) parameter.getParameterizedObject();
//	                if(paramCondition.getId()==condition.getId())
//	                    return parameter;
//	             }
//	        }
//		}
//		return null;
//	}

	/**
	 * This Method generates component name as expressionId_attributeId.
	 */
	private String generateComponentName(int expressionId, AttributeInterface attribute) {
		String componentId = expressionId + QueryModuleConstants.UNDERSCORE + attribute.getId().toString();
		return componentId;
	}
	
}