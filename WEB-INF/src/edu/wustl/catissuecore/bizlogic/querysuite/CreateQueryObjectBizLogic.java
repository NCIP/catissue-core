
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
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
	 * Gets the map which holds the data to create the rule object and add it to
	 * query.
	 * 
	 * @param strToCreateQueryObject
	 *            str to create query obj
	 * @param entity
	 *            name of entity
	 * @return Map rules details
	 * @throws DynamicExtensionsSystemException
	 *             DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 *             DynamicExtensionsApplicationException
	 */
	public Map getRuleDetailsMap(String strToCreateQueryObject, EntityInterface entity)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		String errorMessage = "";
		Map ruleDetailsMap = new HashMap();
		Map conditionsMap = createConditionsMap(strToCreateQueryObject);
		if (entity != null)
		{
			Collection<AttributeInterface> attrCollection = entity.getEntityAttributesForQuery();
			if (conditionsMap != null && !conditionsMap.isEmpty() && attrCollection != null
					&& !attrCollection.isEmpty())
			{
				List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
				List<String> attributeOperators = new ArrayList<String>();
				List<String> secondAttributeValues = new ArrayList<String>();
				ArrayList<ArrayList<String>> conditionValues = new ArrayList<ArrayList<String>>();

				Iterator iterAttributes = (Iterator) attrCollection.iterator();
				while (iterAttributes.hasNext())
				{
					AttributeInterface attr = (AttributeInterface) iterAttributes.next();
					String componentId = attr.getName() + attr.getId().toString();
					String[] params = (String[]) conditionsMap.get(componentId);
					if (params != null)
					{
						attributes.add(attr);
						attributeOperators.add(params[0]);
						//firstAttributeValues.add(params[1]);
						secondAttributeValues.add(params[2]); 
						ArrayList<String> attributeValues = getConditionValuesList(params);
						errorMessage = errorMessage
						+ validateAttributeValues(attr, attributeValues);
						if(errorMessage.equalsIgnoreCase(""))
						{
							if (params[0].equalsIgnoreCase(Constants.Between))
							{
								attributeValues = Utility.getAttributeValuesInProperOrder(attr.getDataType(),
										attributeValues.get(0), attributeValues.get(1));
							}
							conditionValues.add(attributeValues);
						}
					}
				}
				if(errorMessage.equalsIgnoreCase(""))
				{
					ruleDetailsMap.put(AppletConstants.ATTRIBUTES, attributes);
					ruleDetailsMap.put(AppletConstants.ATTRIBUTE_OPERATORS, attributeOperators);
					//ruleDetailsMap.put(AppletConstants.FIRST_ATTR_VALUES, firstAttributeValues);
					ruleDetailsMap.put(AppletConstants.SECOND_ATTR_VALUES, secondAttributeValues);
					ruleDetailsMap.put(AppletConstants.ATTR_VALUES, conditionValues);
				}
				ruleDetailsMap.put(AppletConstants.ERROR_MESSAGE, errorMessage);
			}
		}
		return ruleDetailsMap;
	}

	private ArrayList<String> getConditionValuesList(String[] params)
	{
		ArrayList<String> attributeValues = new ArrayList<String>();
		if (params[1] != null)
		{
			if (params[1].contains(Constants.QUERY_VALUES_DELIMITER))
			{
				String[] values = params[1].split(Constants.QUERY_VALUES_DELIMITER);
				for (int i = 0; i < values.length; i++)
				{
					if(!values[i].equalsIgnoreCase(""))
						attributeValues.add(values[i].trim());
				}
			}
			else
			{
				attributeValues.add(params[1]);
			}
		}
		if (params[2] != null)
		{
			attributeValues.add(params[2]);
		}
		return attributeValues;
	}

	/**
	 * Validates the user input and populates the list of messages to be shown
	 * to the user on the screen.
	 * 
	 * @param attr
	 *            IAttribute
	 * @param attrvalues
	 *            List<String>
	 * @return String message
	 */
	private String validateAttributeValues(AttributeInterface attr, List<String> attrvalues)
	{
		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();
		String errorMessages = "";
		String dataType = attr.getDataType().toString();
		Iterator valuesIter = (Iterator) attrvalues.iterator();
		while (valuesIter.hasNext())
		{
			String enteredValue = (String) valuesIter.next();
			enteredValue = enteredValue.trim();
			if (enteredValue.equalsIgnoreCase(Constants.MISSING_TWO_VALUES))
			{
				errorMessages = errorMessages
				+ ApplicationProperties.getValue("simpleQuery.twovalues.required");
				Logger.out.debug(enteredValue + " two values required for 'Between' operator ");
			}
			else if ((dataType.trim().equalsIgnoreCase("bigint") || dataType.trim()
					.equalsIgnoreCase("integer"))
					|| dataType.trim().equalsIgnoreCase("Long"))
			{
				Logger.out.debug(" Check for integer");

				if (validator.convertToLong(enteredValue) == null)
				{
					errorMessages = errorMessages
					+ ApplicationProperties.getValue("simpleQuery.intvalue.required");
					Logger.out.debug(enteredValue + " is not a valid integer");
				}
				else if (!validator.isPositiveNumeric(enteredValue, 0))
				{
					errorMessages = errorMessages
					+ ApplicationProperties
					.getValue("simpleQuery.intvalue.poisitive.required");
					Logger.out.debug(enteredValue + " is not a positive integer");
				}

			}// integer
			else if ((dataType.trim().equalsIgnoreCase("double"))
					&& !validator.isDouble(enteredValue, false))
			{
				errorMessages = errorMessages
				+ ApplicationProperties.getValue("simpleQuery.decvalue.required");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
				"simpleQuery.decvalue.required"));
			} // double
			else if (dataType.trim().equalsIgnoreCase("tinyint"))
			{
				if (!enteredValue.trim().equalsIgnoreCase(Constants.BOOLEAN_YES)
						&& !enteredValue.trim().equalsIgnoreCase(Constants.BOOLEAN_NO))
				{
					errorMessages = errorMessages
					+ ApplicationProperties.getValue("simpleQuery.tinyint.format");
				}
			}
			else if (dataType.trim().equalsIgnoreCase(Constants.FIELD_TYPE_TIMESTAMP_TIME))
			{
				if (!validator.isValidTime(enteredValue, Constants.TIME_PATTERN_HH_MM_SS))
				{
					errorMessages = errorMessages
					+ ApplicationProperties.getValue("simpleQuery.time.format");
				}
			}
			else if (dataType.trim().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)
					|| dataType.trim().equalsIgnoreCase(Constants.FIELD_TYPE_TIMESTAMP_DATE))
			{
				if (!validator.checkDate(enteredValue))
				{
					errorMessages = errorMessages
					+ ApplicationProperties.getValue("simpleQuery.date.format");
				}
			}
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
		String[] conditions = queryString.split(Constants.QUERY_CONDITION_DELIMITER);
		final int LENGTH = 3;

		for (int i = 0; i < conditions.length; i++)
		{
			String[] attrParams = new String[LENGTH];
			String condition = conditions[i];
			if (!condition.equals(""))
			{
				condition = condition.substring(0, condition.indexOf(";"));
				String attrName = "";
				StringTokenizer tokenizer = new StringTokenizer(condition,
						Constants.QUERY_OPERATOR_DELIMITER);
				while (tokenizer.hasMoreTokens())
				{
					attrName = tokenizer.nextToken();
					if (tokenizer.hasMoreTokens())
					{
						String operator = tokenizer.nextToken();
						attrParams[0] = operator;
						if (tokenizer.hasMoreTokens())
						{
							attrParams[1] = tokenizer.nextToken();
							if (operator.equalsIgnoreCase(RelationalOperator.Between.toString()))
							{
								attrParams[2] = tokenizer.nextToken();
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
	 * @param query
	 * @return
	 */
	public String setInputDataToQuery(String queryInputString, IQuery query,
			Map<String, String> displayNamesMap)
	{
		String errorMessage = "";
		Map<String, String[]> newConditions = null;
		if (queryInputString != null)
			newConditions = createConditionsMap(queryInputString);

		IConstraints constraints = query.getConstraints();
		Enumeration<IExpressionId> expressionIds = constraints.getExpressionIds();
		while (expressionIds.hasMoreElements())
		{
			IExpression expression = constraints.getExpression(expressionIds.nextElement());
			for (int i = 0; i < expression.numberOfOperands(); i++)
			{
				IExpressionOperand operand = expression.getOperand(i);
				if (!operand.isSubExpressionOperand())
				{
					if(operand instanceof IRule)
					{
					IRule ruleObject = (IRule) operand;
					List<ICondition> conditions = ruleObject.getConditions();
					for (int j = 0; j < conditions.size(); j++)
					{
						ICondition condition = conditions.get(j);
						String componentName = generateComponentName(expression.getExpressionId()
								.getInt(), condition.getAttribute());
						if (newConditions != null && newConditions.containsKey(componentName))
						{
							String[] params = newConditions.get(componentName);
							ArrayList<String> attributeValues = getConditionValuesList(params);
							errorMessage = errorMessage
							+ validateAttributeValues(condition.getAttribute(),
									attributeValues);
							condition.setValues(attributeValues);
							condition.setRelationalOperator(RelationalOperator
									.getOperatorForStringRepresentation(params[0]));
						}
						if (displayNamesMap != null && displayNamesMap.containsKey(componentName))
						{
							IParameterizedCondition iparameterizedCondition = new ParameterizedCondition(
									condition);
							iparameterizedCondition.setName(displayNamesMap.get(componentName));
							conditions.set(j, iparameterizedCondition);
						}
					}
					}else
					{
						errorMessage = "Could not save Temporal Query, as this feature is not yet Implemented";
					}
				}
			}
		}

		return errorMessage;
	}

	/**
	 * This Method generates component name as expressionId_attributeId
	 */
	private String generateComponentName(int expressionId, AttributeInterface attribute)
	{
		String componentId = expressionId + "_" + attribute.getId().toString();
		return componentId;

	}

}