
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.BooleanAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.IntegerAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domain.PermissibleValue;
import edu.common.dynamicextensions.domain.ShortAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domain.UserDefinedDE;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.BooleanValueInterface;
import edu.common.dynamicextensions.domaininterface.DateValueInterface;
import edu.common.dynamicextensions.domaininterface.DoubleValueInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.FloatValueInterface;
import edu.common.dynamicextensions.domaininterface.IntegerValueInterface;
import edu.common.dynamicextensions.domaininterface.LongValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.ShortValueInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.util.ParseXMLFile;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;

/**
 * This generates UI for 'Add limits' and 'Edit Limits' section.
 * 
 * @author deepti_shelar
 *
 */
public class GenerateHtmlForAddLimitsBizLogic
{
	/**
	 * Object which holds data operators fro attributes.
	 */
	ParseXMLFile parseFile = null;

	/**
	 * Constructor for GenerateHtmlForAddLimitsBizLogic
	 */
	public GenerateHtmlForAddLimitsBizLogic()
	{
		if (parseFile == null)
		{
			parseFile = new ParseXMLFile(Constants.DYNAMIC_UI_XML);
		}
	}
	/**
	 * This method generates the html for Add Limits and Edit Limits section.
	 * This internally calls methods to generate other UI components like text, Calendar, Combobox etc.
	 * @param entity entity to be presented on UI.
	 * @param conditions List of conditions , These are required in case of edit limits, For adding linits this parameter is null
	 * @return String html generated for Add Limits section.
	 */
	public String generateHTML(EntityInterface entity, List<ICondition> conditions)
	{
		StringBuffer generatedHTML = new StringBuffer();
		String nameOfTheEntity = entity.getName();
		Collection attributeCollection = entity.getAttributeCollection();
		boolean isEditLimits = false;
		String header = "Define Search Rules For";//ApplicationProperties.getValue("query.defineSearchRulesFor");
		String attributesList = "";
		generatedHTML.append("<table border=\"0\" width=\"100%\" height=\"100%\" callspacing=\"0\" cellpadding=\"0\">");
		generatedHTML.append("\n<tr>");
		generatedHTML
				.append("<td height=\"4%\" colspan=\"6\" bgcolor=\"#EAEAEA\" style=\"border:solid 1px\"><font face=\"Arial\" size=\"2\" color=\"#000000\"><b>");
		generatedHTML.append(header + " '" + nameOfTheEntity + "'</b></font>");
		generatedHTML.append("\n</td></tr>");
		generatedHTML.append("\n<tr><td height=\"3%\" colspan=\"4\" bgcolor=\"#FFFFFF\">&nbsp;</td></tr>");
		if (!attributeCollection.isEmpty())
		{
			Iterator iter = attributeCollection.iterator();
			while (iter.hasNext())
			{
				AttributeInterface attribute = (AttributeInterface) iter.next();
				String attrName = attribute.getName();
				String attrLabel = QueryModuleUtil.getAttributeLabel(attrName);
				String componentId = attrName + attribute.getId().toString();
				attributesList = attributesList + ";" + componentId;
				generatedHTML.append("\n<tr id=\"" + componentId + "\" height=\"3%\">\n<td class=\"standardTextQuery\" width=\"15%\">");
				generatedHTML.append(attrLabel + "</td>\n");
				List<String> operatorsList = populateAttributeUIInformation(attribute);
				boolean isBetween = false;
				if (!operatorsList.isEmpty() && operatorsList.get(0).equalsIgnoreCase(RelationalOperator.Between.toString()))
				{
					isBetween = true;
				}
				List<String> permissibleValues = getPermissibleValuesList(attribute);
				if (conditions != null)
				{
					Map<String, ICondition> attributeNameConditionMap = getMapOfConditions(conditions);
					isEditLimits = true;
					if (attributeNameConditionMap.containsKey(attrName))
					{
						ICondition condition = attributeNameConditionMap.get(attrName);
						ArrayList<String> values = (ArrayList<String>) condition.getValues();
						String operator = condition.getRelationalOperator().toString();
						generatedHTML.append("\n" + generateHTMLForOperators(attribute, operatorsList, operator));
						if (operator.equalsIgnoreCase(RelationalOperator.Between.toString()))
						{
							isBetween = true;
						}
						if (!permissibleValues.isEmpty())
						{
							generatedHTML.append("\n" + generateHTMLForEnumeratedValues(attribute, permissibleValues, values));
						}
						else
						{
							generatedHTML.append("\n" + generateHTMLForTextBox(attribute, isBetween, values));
						}
					}
					else
					{
						generatedHTML.append("\n" + generateHTMLForOperators(attribute, operatorsList, null));
						if (!permissibleValues.isEmpty())
						{
							generatedHTML.append("\n" + generateHTMLForEnumeratedValues(attribute, permissibleValues, null));
						}
						else
						{
							generatedHTML.append("\n" + generateHTMLForTextBox(attribute, isBetween, null));
						}
					}
				}
				if (conditions == null)
				{
					generatedHTML.append("\n" + generateHTMLForOperators(attribute, operatorsList, null));
					if (!permissibleValues.isEmpty())
					{
						generatedHTML.append("\n" + generateHTMLForEnumeratedValues(attribute, permissibleValues, null));
					}
					else
					{
						generatedHTML.append("\n" + generateHTMLForTextBox(attribute, isBetween, null));
					}
				}
				generatedHTML.append("\n</tr>");
			}
		}
		generatedHTML.append(generateHTMLForButton(nameOfTheEntity, attributesList, isEditLimits));
		generatedHTML.append("</table>");
		return generatedHTML.toString();
	}

	/**
	 * returns PermissibleValuesList' list for attribute
	 * @param attribute AttributeInterface
	 * @return List of permissible values for the passed attribute
	 */
	private List<String> getPermissibleValuesList(AttributeInterface attribute)
	{
		UserDefinedDE userDefineDE = (UserDefinedDE) attribute.getAttributeTypeInformation().getDataElement();
		List<String> permissibleValues = new ArrayList<String>();
		if (userDefineDE != null && userDefineDE.getPermissibleValueCollection() != null)
		{
			Iterator<PermissibleValueInterface> permissibleValueInterface = userDefineDE.getPermissibleValueCollection().iterator();
			while (permissibleValueInterface.hasNext())
			{
				PermissibleValue permValue = (PermissibleValue) permissibleValueInterface.next();
				if (permValue instanceof StringValueInterface)
					permissibleValues.add(((StringValueInterface) permValue).getValue());

				if (permValue instanceof ShortValueInterface)
					permissibleValues.add(((ShortValueInterface) permValue).getValue().toString());

				if (permValue instanceof LongValueInterface)
					permissibleValues.add(((LongValueInterface) permValue).getValue().toString());

				if (permValue instanceof DateValueInterface)
					permissibleValues.add(((DateValueInterface) permValue).getValue().toString());

				if (permValue instanceof BooleanValueInterface)
					permissibleValues.add(((BooleanValueInterface) permValue).getValue().toString());

				if (permValue instanceof IntegerValueInterface)
					permissibleValues.add(((IntegerValueInterface) permValue).getValue().toString());

				if (permValue instanceof DoubleValueInterface)
					permissibleValues.add((String) ((DoubleValueInterface) permValue).getValue().toString());

				if (permValue instanceof FloatValueInterface)
					permissibleValues.add(((FloatValueInterface) permValue).getValue().toString());

				if (permValue instanceof FloatValueInterface)
					permissibleValues.add(((FloatValueInterface) permValue).getValue().toString());
			}
		}
		return permissibleValues;
	}

	/**
	 * Returns the map of name of the attribute and condition obj as its value.
	 * @param conditions list of conditions user had applied in case of edit limits
	 * @return Map name of the attribute and condition obj 
	 */
	private Map<String, ICondition> getMapOfConditions(List<ICondition> conditions)
	{
		Map<String, ICondition> attributeNameConditionMap = new HashMap<String, ICondition>();
		for (int i = 0; i < conditions.size(); i++)
		{
			attributeNameConditionMap.put(conditions.get(i).getAttribute().getName(), conditions.get(i));
		}
		return attributeNameConditionMap;
	}
	/**
	 * Returns list of possible numerated/enumerated operators for attribute. 
	 * @param attributeInterface attributeInterface
	 * @return List listOf operators.
	 */
	private List<String> populateAttributeUIInformation(AttributeInterface attributeInterface)
	{
		List<String> operatorsList = new ArrayList<String>();
		AttributeTypeInformationInterface attrTypeInfo = attributeInterface.getAttributeTypeInformation();
		Object[] strObj = null;
		if (attributeInterface != null)
		{
			UserDefinedDE userDefineDE = (UserDefinedDE) attributeInterface.getAttributeTypeInformation().getDataElement();
			if (userDefineDE == null)
			{
				if (attrTypeInfo instanceof StringAttributeTypeInformation)
				{
					strObj = parseFile.getNonEnumStr();
				}
				else if (attrTypeInfo instanceof BooleanAttributeTypeInformation)
				{
					strObj = parseFile.getNonEnumStr();
				}
				else if (attrTypeInfo instanceof DateAttributeTypeInformation)
				{
					strObj = parseFile.getNonEnumDate();
				}
				else if (attrTypeInfo instanceof DoubleAttributeTypeInformation || attrTypeInfo instanceof LongAttributeTypeInformation
						|| attrTypeInfo instanceof ShortAttributeTypeInformation || attrTypeInfo instanceof IntegerAttributeTypeInformation)
				{
					strObj = parseFile.getNonEnumNum();
				}
			}
			else
			{
				if (attrTypeInfo instanceof StringAttributeTypeInformation)
				{
					strObj = parseFile.getEnumStr();
				}
				else if (attrTypeInfo instanceof BooleanAttributeTypeInformation)
				{
					strObj = parseFile.getEnumStr();
				}
				else if (attrTypeInfo instanceof DoubleAttributeTypeInformation || attrTypeInfo instanceof LongAttributeTypeInformation
						|| attrTypeInfo instanceof ShortAttributeTypeInformation || attrTypeInfo instanceof IntegerAttributeTypeInformation)
				{
					strObj = parseFile.getEnumNum();
				}
			}
			for (int i = 0; i < strObj.length; i++)
			{
				if (strObj[i] != null)
					operatorsList.add((String) strObj[i]);
			}
		}
		return operatorsList;
	}
	/**
	 * This method generates the combobox's html to show the operators valid for the attribute passed to it.
	 * @param attribute AttributeInterface 
	 * @param operatorsList list of operators for each attribute
	 * @return String HTMLForOperators
	 */
	private String generateHTMLForOperators(AttributeInterface attribute, List operatorsList, String op)
	{
		StringBuffer html = new StringBuffer();
		String attributeName = attribute.getName();
		String componentId = attributeName + attribute.getId().toString();
		if (operatorsList != null && operatorsList.size() != 0)
		{
			html.append("\n<td width=\"22%\">");
			AttributeTypeInformationInterface attrTypeInfo = attribute.getAttributeTypeInformation();
			if (attrTypeInfo instanceof DateAttributeTypeInformation)
			{
				html.append("\n<select name=\"" + componentId + "_combobox\" onChange=\"operatorChanged('" + componentId + "','true')\">");
			}
			else
			{
				html.append("\n<select name=\"" + componentId + "_combobox\" onChange=\"operatorChanged('" + componentId + "','false')\">");
			}
			Iterator iter = operatorsList.iterator();

			while (iter.hasNext())
			{
				String operator = iter.next().toString();
				String op1 = operator.replace(" ", "");
				if (op1.equalsIgnoreCase(op))
				{
					html.append("\n<option value=\"" + operator + "\" SELECTED>" + operator + "</option>");
				}
				else
				{
					html.append("\n<option value=\"" + operator + "\">" + operator + "</option>");
				}
			}
			html.append("\n</select>");
			html.append("\n</td>");
		}
		return html.toString();
	}

	/**
	 * Generates html for textBox to hold the input for operator selected. 
	 * @param attributeInterface attribute
	 * @param isBetween boolean 
	 * @return String HTMLForTextBox
	 */
	private String generateHTMLForTextBox(AttributeInterface attributeInterface, boolean isBetween, ArrayList<String> values)
	{
		String componentId = attributeInterface.getName() + attributeInterface.getId().toString();
		String textBoxId = componentId + "_textBox";
		String textBoxId1 = componentId + "_textBox1";
		StringBuffer html = new StringBuffer();
		html.append("<td width=\"20%\" class=\"\">\n");
		if (values == null || values.isEmpty())
		{
			html.append("<input type=\"text\" name=\"" + textBoxId + "\" id=\"" + textBoxId + "\">");
		}
		else
		{
			html.append("<input type=\"text\" name=\"" + textBoxId + "\" id=\"" + textBoxId + "\" value=\"" + values.get(0) + "\">");
		}
		html.append("\n</td>");
		if (attributeInterface.getAttributeTypeInformation() instanceof DateAttributeTypeInformation)
		{
			html.append("\n" + generateHTMLForCalendar(attributeInterface, true, false));
		}
		else
		{
			html.append("\n<td width=\"5%\">&nbsp</td>");
		}
		html.append("\n<td width=\"20%\">");
		if (isBetween)
		{
			if (values == null)
			{
				html.append("<input type=\"text\" name=\"" + textBoxId1 + "\" id=\"" + textBoxId1 + "\" style=\"display:block\">");
			}
			else
			{
				html.append("<input type=\"text\" name=\"" + textBoxId1 + "\" id=\"" + textBoxId1 + "\" value=\"" + values.get(1)
						+ "\" style=\"display:block\">");
			}
		}
		else
		{
			html.append("<input type=\"text\" name=\"" + textBoxId1 + "\" id=\"" + textBoxId1 + "\" style=\"display:none\">");
		}
		html.append("\n</td>");
		if (attributeInterface.getAttributeTypeInformation() instanceof DateAttributeTypeInformation)
		{
			html.append("\n" + generateHTMLForCalendar(attributeInterface, false, isBetween));
		}
		else
		{
			html.append("\n<td width=\"5%\">&nbsp</td>");
		}

		return html.toString();
	}

	/**
	 * Generates html for button.
	 * @param entityName entityName
	 * @param attributesStr attributesStr
	 * @return String HTMLForButton
	 */
	private String generateHTMLForButton(String entityName, String attributesStr, boolean isEditLimits)
	{
		String buttonId = "addLimit";
		StringBuffer html = new StringBuffer();
		String buttonCaption = "Add Limit";
		html.append("\n<tr>");
		html.append("\n<td valign=\"bottom\">");
		if (isEditLimits)
		{
			buttonCaption = "Edit Limit";
		}

		html.append("\n<input type=\"button\" name=\"" + buttonId + "\" onClick=\"produceQuery('addToLimitSet.do', 'categorySearchForm', '"
				+ entityName + "','" + attributesStr + "')\" value=\"" + buttonCaption + "\"></input>");
		html.append("\n</td>");
		html.append("\n</tr>");
		return html.toString();
	}

	/**
	 * Generators html for Calendar.Depending upon the value of operator the calendar is displayed(hidden/visible).
	 * @param attributeName attributeName
	 * @param isFirst boolean 
	 * @param isBetween boolean 
	 * @return String HTMLForCalendar
	 */
	private String generateHTMLForCalendar(AttributeInterface attribute, boolean isFirst, boolean isBetween)
	{
		String componentId = attribute.getName() + attribute.getId().toString();
		String innerStr = "";
		//String divId = "overDiv" + (i + 1);
		String divStr = "<div id='overDiv' style='position:absolute; visibility:hidden; z-index:1000;'></div>";
		String imgStr = "<img id=\"calendarImg\" src=\"images\\calendar.gif\" width=\"24\" height=\"22\" border=\"0\">";
		if (isFirst)
		{
			String textBoxId = componentId + "_textBox";
			String calendarId = componentId + "_calendar";
			innerStr = "<td width=\"3%\" id=\"" + calendarId + "\">" + divStr + "<a href=\"javascript:show_calendar('categorySearchForm." + textBoxId
					+ "',null,null,'MM-DD-YYYY');\">" + imgStr + "</a>";
		}
		else
		{
			String textBoxId1 = componentId + "_textBox1";
			String calendarId1 = componentId + "_calendar1";
			String style = "";
			if (isBetween)
			{
				style = "display:block";
			}
			else
			{
				style = "display:none";
			}
			innerStr = "<td width=\"3%\" id=\"" + calendarId1 + "\" style=\"" + style + "\">" + divStr
					+ "<a href=\"javascript:show_calendar('categorySearchForm." + textBoxId1 + "',null,null,'MM-DD-YYYY');\">" + imgStr + "</a>";
		}
		innerStr = innerStr + "</td>";
		return innerStr.toString();
	}

	/**
	 * This function generates the HTML for enumerated values.
	 * @param attribute AttributeInterface 
	 * @param enumeratedValuesList enumeratedValuesList
	 * @param list values values' list in case of edit limits
	 * @return String html for enumerated value dropdown
	 */
	private String generateHTMLForEnumeratedValues(AttributeInterface attribute, List enumeratedValuesList, List<String> values)
	{
		StringBuffer html = new StringBuffer();
		String attributeName = attribute.getName();
		String componentId = attributeName + attribute.getId().toString();
		if (enumeratedValuesList != null && enumeratedValuesList.size() != 0)
		{
			html.append("\n<td width=\"20%\">");
			html.append("\n<select MULTIPLE class='enumeratedListBox' styleId='country' size ='4' name=\"" + componentId
					+ "_enumeratedvaluescombobox\"\">");
			Iterator iter = enumeratedValuesList.iterator();
			while (iter.hasNext())
			{
				String value = (String) iter.next();
				if (values != null && values.contains(value))
				{
					html.append("\n<option value=\"" + value + "\" SELECTED>" + value + "</option>");
				}
				else
				{
					html.append("\n<option value=\"" + value + "\">" + value + "</option>");
				}
			}
			html.append("\n</select>");
			html.append("\n</td>");
		}
		return html.toString();
	}
}
