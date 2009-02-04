
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * Creates Query Object as per the data filled by the user on AddLimits section.
 * This will also validate the inputs and generate messages and they will be shown to user.
 * @author deepti_shelar
 *
 */
public class CreateQueryObjectBizLogic
{
	/**
	 * Gets the map which holds the data to create the rule object and add it to query.
	 * @param strToCreateQueryObject str to create query obj
	 * @param entity name of entity
	 * @return Map rules details
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException DynamicExtensionsApplicationException
	 */
	public Map getRuleDetailsMap(String strToCreateQueryObject, EntityInterface entity) throws DynamicExtensionsSystemException,
	DynamicExtensionsApplicationException
	{
		String errorMessage = "";
		Map ruleDetailsMap = new HashMap();
		Map conditionsMap = createConditionsMap(strToCreateQueryObject);
		if (entity != null)
		{
			Collection<AttributeInterface> attrCollection = entity.getAttributeCollection();
			if (conditionsMap != null && !conditionsMap.isEmpty() && attrCollection != null && !attrCollection.isEmpty())
			{
				List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
				List<String> attributeOperators = new ArrayList<String>();
				List<String> firstAttributeValues = new ArrayList<String>();
				List<String> secondAttributeValues = new ArrayList<String>();
				ArrayList<ArrayList<String>>  conditionValues = new ArrayList<ArrayList<String>>();

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
						firstAttributeValues.add(params[1]);
						secondAttributeValues.add(params[2]);						
						ArrayList<String> attributeValues = new ArrayList<String>();
						if(params[1] != null)
						{
							if(params[1].contains(","))
							{
								String[] values = params[1].split(",");
								for(int i=0;i<values.length;i++)
								{
									attributeValues.add(values[i]);
								}
							}
							else
							{
								attributeValues.add(params[1]);
							}
						}
						if(params[2] != null)
						{
							attributeValues.add(params[2]);
						}
						errorMessage = errorMessage + validateAttributeValues(attr,attributeValues);
						conditionValues.add(attributeValues);
					}
					System.out.println(errorMessage);
				}
				ruleDetailsMap.put(AppletConstants.ATTRIBUTES, attributes);
				ruleDetailsMap.put(AppletConstants.ATTRIBUTE_OPERATORS, attributeOperators);
				ruleDetailsMap.put(AppletConstants.FIRST_ATTR_VALUES, firstAttributeValues);
				ruleDetailsMap.put(AppletConstants.SECOND_ATTR_VALUES, secondAttributeValues);
				ruleDetailsMap.put(AppletConstants.ATTR_VALUES, conditionValues);
				ruleDetailsMap.put(AppletConstants.ERROR_MESSAGE, errorMessage);
			}
		}
		return ruleDetailsMap;
	}

	/**
	 * Validates the user input and populates the list of messages to be shown to the user on the screen.
	 * @param attr IAttribute
	 * @param attrvalues List<String>
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
				errorMessages = errorMessages + ApplicationProperties.getValue("simpleQuery.twovalues.required");
				Logger.out.debug(enteredValue + " two values required for 'Between' operator ");
			}
			else if ((dataType.trim().equalsIgnoreCase("bigint") || dataType.trim().equalsIgnoreCase("integer"))
					|| dataType.trim().equalsIgnoreCase("Long"))
			{
				Logger.out.debug(" Check for integer");

				if (validator.convertToLong(enteredValue) == null)
				{
					errorMessages = errorMessages + ApplicationProperties.getValue("simpleQuery.intvalue.required");
					Logger.out.debug(enteredValue + " is not a valid integer");
				}
				else if (!validator.isPositiveNumeric(enteredValue, 0))
				{
					errorMessages = errorMessages + ApplicationProperties.getValue("simpleQuery.intvalue.poisitive.required");
					Logger.out.debug(enteredValue + " is not a positive integer");
				}

			}//integer         
			else if ((dataType.trim().equalsIgnoreCase("double")) && !validator.isDouble(enteredValue, false))
			{
				errorMessages = errorMessages + ApplicationProperties.getValue("simpleQuery.decvalue.required");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.decvalue.required"));
			} // double
			else if (dataType.trim().equalsIgnoreCase("tinyint"))
			{
				if (!enteredValue.trim().equalsIgnoreCase(Constants.BOOLEAN_YES) && !enteredValue.trim().equalsIgnoreCase(Constants.BOOLEAN_NO))
				{
					errorMessages = errorMessages + ApplicationProperties.getValue("simpleQuery.tinyint.format");
				}
			}
			else if (dataType.trim().equalsIgnoreCase(Constants.FIELD_TYPE_TIMESTAMP_TIME))
			{
				if (!validator.isValidTime(enteredValue, Constants.TIME_PATTERN_HH_MM_SS))
				{
					errorMessages = errorMessages + ApplicationProperties.getValue("simpleQuery.time.format");
				}
			}
			else if (dataType.trim().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)
					|| dataType.trim().equalsIgnoreCase(Constants.FIELD_TYPE_TIMESTAMP_DATE))
			{
				if (!validator.checkDate(enteredValue))
				{
					errorMessages = errorMessages + ApplicationProperties.getValue("simpleQuery.date.format");
				}
			}
		}
		return errorMessages;
	}

	/**
	 * Craetes Map of condition Objects.
	 * @param queryString queryString
	 * @return Map conditions map
	 * @throws DynamicExtensionsApplicationException  DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 */
	private Map<String, String[]> createConditionsMap(String queryString) throws DynamicExtensionsSystemException,
	DynamicExtensionsApplicationException
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
				StringTokenizer tokenizer = new StringTokenizer(condition, Constants.QUERY_OPERATOR_DELIMITER);
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
}