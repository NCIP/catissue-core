
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.client.ui.query.QueryObject;
import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.common.querysuite.exceptions.CyclicException;

/**
 * Creates Query Object as per the data filled by the user on AddLimits section.
 * This will also validate the inputs and generates messages and they will be shown to user.
 * @author deepti_shelar
 *
 */
public class CreateQueryObjectBizLogic
{
	/**
	 * Adds Rules to Query Object.
	 * @param queryObject QueryObject 
	 * @param queryStr String 
	 * @param entity Entity
	 * @param searchForm CategorySearchForm 
	 * @return QueryObject QueryObject
	 * @throws DynamicExtensionsApplicationException  DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException  DynamicExtensionsSystemException
	 * @throws CyclicException CyclicException
	 */
	public QueryObject addRulesToQueryObject(QueryObject queryObject,String queryStr, Entity entity, CategorySearchForm searchForm)
	throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, CyclicException
	{
		Map conditionsMap = createConditionsMap(queryStr, entity);
		Collection attrCollection = entity.getAttributeCollection();
		if (conditionsMap != null && !conditionsMap.isEmpty() && attrCollection != null && !attrCollection.isEmpty())
		{
			List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
			List<String> attributeOperators = new ArrayList<String>();
			List<String> firstAttributeValues = new ArrayList<String>();
			List<String> secondAttributeValues = new ArrayList<String>();
			Iterator iterAttributes = (Iterator) attrCollection.iterator();

			while (iterAttributes.hasNext())
			{
				AttributeInterface attr = (AttributeInterface) iterAttributes.next();
				String[] params = (String[]) conditionsMap.get(attr.getName());
				if (params != null && params.length != 0)
				{
					attributes.add(attr);
					attributeOperators.add(params[0]);
					if (params[1] != null)
					{
						firstAttributeValues.add(params[1]);
					}
					if (params[2] != null)
					{
						secondAttributeValues.add(params[2]);
					}
					else
					{
						secondAttributeValues.add(params[2]);
					}
				}
			}

			/*	queryObject.addRule(attributes, attributeOperators, firstAttributeValues, secondAttributeValues);
			queryObject.updateLogicalOperator(entity.getName(),LogicalOperator.And.toString());	*/
		}
		return queryObject;
	}

	/**
	 * Initialises the resource bundle.
	 * @return new Resource bundle
	 *//*
	private ResourceBundle initializeResourceBundle()
	{
		ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources");
		return resourceBundle;
	}

	*//**
	 * Gets the message from properties file.
	 * @param resourceBundle : Resource bundle name
	 * @param captionKey : key for the caption in the resource bundle
	 * @return Value for caption key in the resource bundle
	 *//*
	private String getErrorMessageFromResourceBundle(ResourceBundle resourceBundle, String captionKey)
	{
		if ((captionKey != null) && (resourceBundle != null))
		{
			return resourceBundle.getString(captionKey);
		}
		else
		{
			return null;
		}
	}*/

	/**
	 * Validates the user input and populates the list of messages to be shown to the user on the screen.
	 * @param attr IAttribute
	 * @param attrvalues List<String>
	 * @return String message
	 *//*
	private String validateAttributeValues(IAttribute attr, List<String> attrvalues)
	{
		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();
		ResourceBundle resourceBundle = initializeResourceBundle();
		String errorMessages = "";
		String dataType = attr.getDataType().toString();
		Iterator valuesIter = (Iterator) attrvalues.iterator();
		while (valuesIter.hasNext())
		{
			String enteredValue = (String) valuesIter.next();
			if ((dataType.trim().equalsIgnoreCase("bigint") || dataType.trim().equalsIgnoreCase("integer"))
					|| dataType.trim().equalsIgnoreCase("Long"))
			{
				Logger.out.debug(" Check for integer");
				if (validator.convertToLong(enteredValue) == null)
				{
					errorMessages = errorMessages + getErrorMessageFromResourceBundle(resourceBundle, "simpleQuery.intvalue.required");
					Logger.out.debug(enteredValue + " is not a valid integer");
				}
				else if (!validator.isPositiveNumeric(enteredValue, 0))
				{
					errorMessages = errorMessages + getErrorMessageFromResourceBundle(resourceBundle, "simpleQuery.intvalue.poisitive.required");
					Logger.out.debug(enteredValue + " is not a positive integer");
				}

			}//integer         
			else if ((dataType.trim().equalsIgnoreCase("double")) && !validator.isDouble(enteredValue, false))
			{
				errorMessages = errorMessages + getErrorMessageFromResourceBundle(resourceBundle, "simpleQuery.decvalue.required");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.decvalue.required"));
			} // double
			else if (dataType.trim().equalsIgnoreCase("tinyint"))
			{
				if (!enteredValue.trim().equalsIgnoreCase(Constants.BOOLEAN_YES) && !enteredValue.trim().equalsIgnoreCase(Constants.BOOLEAN_NO))
				{
					errorMessages = errorMessages + getErrorMessageFromResourceBundle(resourceBundle, "simpleQuery.tinyint.format");
				}
			}
			else if (dataType.trim().equalsIgnoreCase(Constants.FIELD_TYPE_TIMESTAMP_TIME))
			{
				if (!validator.isValidTime(enteredValue, Constants.TIME_PATTERN_HH_MM_SS))
				{
					errorMessages = errorMessages + getErrorMessageFromResourceBundle(resourceBundle, "simpleQuery.time.format");
				}
			}
			else if (dataType.trim().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)
					|| dataType.trim().equalsIgnoreCase(Constants.FIELD_TYPE_TIMESTAMP_DATE))
			{
				if (!validator.checkDate(enteredValue))
				{
					errorMessages = errorMessages + getErrorMessageFromResourceBundle(resourceBundle, "simpleQuery.date.format");
				}
			}
		}

		return errorMessages;
	}
*/
	/**
	 * Craetes Map of condition Objects.
	 * @param queryString queryString
	 * @param entity entity
	 * @return Map conditions map
	 * @throws DynamicExtensionsApplicationException  DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 */
	private Map createConditionsMap(String queryString, Entity entity) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Map<String, String[]> conditionsMap = new HashMap<String, String[]>();
		String[] conditions = queryString.split("condition=");
		final int LENGTH = 3;

		for (int i = 0; i < conditions.length; i++)
		{
			String[] attrParams = new String[LENGTH];
			String condition = conditions[i];
			if (!condition.equals(""))
			{
				condition = condition.substring(0, condition.indexOf(";"));
				String attrName = "";
				StringTokenizer tokenizer = new StringTokenizer(condition, "*");
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
							if (operator.equalsIgnoreCase("Between"))
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