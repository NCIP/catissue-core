
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.Arrays;
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
import edu.wustl.cab2b.client.ui.controls.AttributeInterfaceComparator;
import edu.wustl.cab2b.client.ui.controls.PermissibleValueComparator;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.util.ParseXMLFile;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;

/**
 * This class generates UI for 'Add Limits' and 'Edit Limits' section.
 * @author deepti_shelar
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
		String header = "\nDefine Search Rules For";//ApplicationProperties.getValue("query.defineSearchRulesFor");
		String attributesList = "";
		generatedHTML.append("<table border=\"0\" width=\"100%\" height=\"100%\" callspacing=\"1\" cellpadding=\"1\">");
		generatedHTML.append("\n<tr>");
		generatedHTML
		.append("<td valign='top' height=\"4%\" colspan=\"8\" bgcolor=\"#EAEAEA\" style=\"border:solid 1px\"><font face=\"Arial\" size=\"2\" color=\"#000000\"><b>");
		generatedHTML.append(header + " '" + nameOfTheEntity + "'</b></font>");
		generatedHTML.append("\n</td></tr>");
		//generatedHTML.append("\n<tr><td valign='top' height=\"3%\" colspan=\"8\" bgcolor=\"#FFFFFF\">&nbsp;</td></tr>");
		if(conditions != null)
		{
			isEditLimits = true;
		}
		boolean isTopButton = true;
		generatedHTML.append(generateHTMLForButton(nameOfTheEntity, getAttributesString(attributeCollection), isEditLimits,isTopButton));
		if (!attributeCollection.isEmpty())
		{
			Object[] attributes = attributeCollection.toArray();
			Arrays.sort(attributes, new AttributeInterfaceComparator());
			for(int i=0;i<attributes.length;i++)
			{
				AttributeInterface attribute = (AttributeInterface) attributes[i];
				String attrName = attribute.getName();
				String attrLabel = QueryModuleUtil.getAttributeLabel(attrName);
				String componentId = attrName + attribute.getId().toString();
				attributesList = attributesList + ";" + componentId;
				generatedHTML.append("\n<tr id=\"" + componentId + "\" height=\"6%\">\n<td valign='top' class=\"standardTextQuery\" nowrap='nowrap' width=\"5%\">");
				generatedHTML.append(attrLabel+" ");
				if(attribute.getDataType().equalsIgnoreCase(Constants.DATE))
				{
					String dateFormat = ApplicationProperties.getValue("query.date.format");
				//	generatedHTML.append("<td  valign='top' class=\"standardDateQuery\" nowrap='nowrap' width=\"10%\">");
					generatedHTML.append("\n("+dateFormat+")");
				//	generatedHTML.append("</td>\n");
				}
				/*else
				{
					generatedHTML.append("\n<td valign='top' class='standardTextQuery' nowrap='nowrap' width='8'>&nbsp</td>");	
				}*/
				generatedHTML.append("</td>\n");
				
				List<String> operatorsList = populateAttributeUIInformation(attribute);
				boolean isBetween = false;
				if (!operatorsList.isEmpty() && operatorsList.get(0).equalsIgnoreCase(RelationalOperator.Between.toString()))
				{
					isBetween = true;
				}
				List<PermissibleValueInterface> permissibleValues = getPermissibleValuesList(attribute);
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
		isTopButton = false;
		generatedHTML.append(generateHTMLForButton(nameOfTheEntity, attributesList, isEditLimits,isTopButton));
		generatedHTML.append("</table>");
		return generatedHTML.toString();
	}

	/**
	 * returns PermissibleValuesList' list for attribute
	 * @param attribute AttributeInterface
	 * @return List of permissible values for the passed attribute
	 */
	private List<PermissibleValueInterface> getPermissibleValuesList(AttributeInterface attribute)
	{
		UserDefinedDE userDefineDE = (UserDefinedDE) attribute.getAttributeTypeInformation().getDataElement();
		List<PermissibleValueInterface> permissibleValues = new ArrayList<PermissibleValueInterface>();
		if (userDefineDE != null && userDefineDE.getPermissibleValueCollection() != null)
		{
			Iterator<PermissibleValueInterface> permissibleValueInterface = userDefineDE.getPermissibleValueCollection().iterator();
			while (permissibleValueInterface.hasNext())
			{
				PermissibleValue permValue = (PermissibleValue) permissibleValueInterface.next();
				if (permValue instanceof StringValueInterface)
					permissibleValues.add(((StringValueInterface) permValue));

				if (permValue instanceof ShortValueInterface)
					permissibleValues.add(((ShortValueInterface) permValue));

				if (permValue instanceof LongValueInterface)
					permissibleValues.add(((LongValueInterface) permValue));

				if (permValue instanceof DateValueInterface)
					permissibleValues.add(((DateValueInterface) permValue));

				if (permValue instanceof BooleanValueInterface)
					permissibleValues.add(((BooleanValueInterface) permValue));

				if (permValue instanceof IntegerValueInterface)
					permissibleValues.add(((IntegerValueInterface) permValue));

				if (permValue instanceof DoubleValueInterface)
					permissibleValues.add((DoubleValueInterface) permValue);

				if (permValue instanceof FloatValueInterface)
					permissibleValues.add(((FloatValueInterface) permValue));

				if (permValue instanceof FloatValueInterface)
					permissibleValues.add(((FloatValueInterface) permValue));
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
			Arrays.sort(strObj);
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
			html.append("\n<td width='1%' class=\"dropdownQuery\" valign='top' >");
			AttributeTypeInformationInterface attrTypeInfo = attribute.getAttributeTypeInformation();
			if (attrTypeInfo instanceof DateAttributeTypeInformation)
			{
				html.append("\n<select class=\"dropdownQuery\" style=\"width:150px; display:block;\" name=\"" + componentId + "_combobox\" onChange=\"operatorChanged('" + componentId + "','true')\">");
			}
			else
			{
				html.append("\n<select class=\"dropdownQuery\" style=\"width:150px; display:block;\" name=\"" + componentId + "_combobox\" onChange=\"operatorChanged('" + componentId + "','false')\">");
			}
			Iterator iter = operatorsList.iterator();

			while (iter.hasNext())
			{
				String operator = iter.next().toString();
				String op1 = operator.replace(" ", "");
				if (op1.equalsIgnoreCase(op))
				{
					html.append("\n<option class=\"dropdownQuery\" value=\"" + operator + "\" SELECTED>" + operator + "</option>");
				}
				else
				{
					html.append("\n<option class=\"dropdownQuery\" value=\"" + operator + "\">" + operator + "</option>");
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
		String dateFormatLabelId1 = componentId + "_dateFormatLabel1";
		String dateFormatLabelId2 = componentId + "_dateFormatLabel2";
		String dataType = attributeInterface.getDataType();
		//String dateFormat = ApplicationProperties.getValue("query.date.format");
		StringBuffer html = new StringBuffer();
		html.append("<td width='1%' valign='top' class=\"standardTextQuery\">\n");
		if (values == null || values.isEmpty())
		{
			html.append("<input style=\"width:150px; display:block;\" type=\"text\" name=\"" + textBoxId + "\" id=\"" + textBoxId + "\">");
		}
		else
		{
			html.append("<input style=\"width:150px; display:block;\" type=\"text\" name=\"" + textBoxId + "\" id=\"" + textBoxId + "\" value=\"" + values.get(0) + "\">");
		}
		html.append("\n</td>");
		if (dataType.equalsIgnoreCase(Constants.DATE))
		{
			html.append("\n" + generateHTMLForCalendar(attributeInterface, true, false));
		//	html.append("\n<td valign='top' nowrap='nowrap' id=\"" +dateFormatLabelId1+ "\" class=\"standardTextQuery\" width=\"8%\">"+dateFormat+"</td>");
		}
		else
		{
			html.append("\n<td valign='top' />");
		//	html.append("\n<td valign='top' />");
		}
		html.append("<td width='1%'  valign='top' class=\"standardTextQuery\">\n");
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
		if (dataType.equalsIgnoreCase(Constants.DATE))
		{
			html.append("\n" + generateHTMLForCalendar(attributeInterface, false, isBetween));
		/*	if(!isBetween)
			{
				html.append("\n<td valign='top' nowrap='nowrap' id=\"" +dateFormatLabelId2+ "\" class=\"standardTextQuery\" style=\"display:none\" width=\"8%\">"+dateFormat+"</td>");
			}
			else
			{
				html.append("\n<td valign='top' nowrap='nowrap' id=\"" +dateFormatLabelId2+ "\" class=\"standardTextQuery\" style=\"display:block\" width=\"8%\">"+dateFormat+"</td>");
			}*/
		}
		else
		{
			html.append("\n<td valign='top' />");
			//html.append("\n<td valign='top' />");
		}
	return html.toString();
	}

	/**
	 * Generates html for button.
	 * @param entityName entityName
	 * @param attributesStr attributesStr
	 * @return String HTMLForButton
	 */
	private String generateHTMLForButton(String entityName, String attributesStr, boolean isEditLimits,boolean isTopButton)
	{
		String buttonName = "addLimit";
		String buttonId = "";
		StringBuffer html = new StringBuffer();
		html.append("\n<tr>");
		if(isTopButton)
		{
			buttonId = "TopAddLimitButton";
			html.append("\n<td valign=\"top\">");
		}
		else
		{
			buttonId = "BottomAddLimitButton";
			html.append("\n<td valign=\"bottom\">");
		}
		String buttonCaption = "Add Limit";
		if (isEditLimits)
		{
			buttonCaption = "Edit Limit";
		}
		html.append("\n<input id=\""+buttonId+"\" type=\"button\" name=\"" + buttonName + "\" onClick=\"produceQuery('"+buttonId+"', 'addToLimitSet.do', 'categorySearchForm', '"
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
		String divStr = "\n<div width='3%' id='overDiv' style='position:absolute; visibility:hidden; z-index:1000;'></div>";
		String imgStr = "\n<img id=\"calendarImg\" src=\"images\\calendar.gif\" width=\"24\" height=\"22\" border=\"0\">";
		if (isFirst)
		{
			String textBoxId = componentId + "_textBox";
			String calendarId = componentId + "_calendar";
			innerStr = "\n<td width='3%' valign='top' id=\"" + calendarId + "\">" + divStr + "\n<a href=\"javascript:show_calendar('categorySearchForm." + textBoxId
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
			innerStr = "\n<td width='3%' valign='top' id=\"" + calendarId1 + "\" style=\"" + style + "\">" + divStr
			+ "\n<a href=\"javascript:show_calendar('categorySearchForm." + textBoxId1 + "',null,null,'MM-DD-YYYY');\">" + imgStr + "</a>";
		}
		innerStr = innerStr + "\n</td>";
		return innerStr.toString();
	}

	/**
	 * This function generates the HTML for enumerated values.
	 * @param attribute AttributeInterface 
	 * @param enumeratedValuesList enumeratedValuesList
	 * @param list values values' list in case of edit limits
	 * @return String html for enumerated value dropdown
	 */
	private String generateHTMLForEnumeratedValues(AttributeInterface attribute, List permissibleValues, List<String> editLimitPermissibleValues)
	{
		StringBuffer html = new StringBuffer();
		String attributeName = attribute.getName();
		String componentId = attributeName + attribute.getId().toString();
		if (permissibleValues != null && permissibleValues.size() != 0)
		{

			html.append("\n<td width='1%' valign='top' class=\"PermissibleValuesQuery\" >");
			html.append("\n<select style=\"width:150px; display:block;\" MULTIPLE styleId='country' size ='2' name=\"" + componentId
					+ "_enumeratedvaluescombobox\"\">");
			Object[] values = permissibleValues.toArray();
			Arrays.sort(values, new PermissibleValueComparator());
			for(int i=0; i<values.length; i++)
			{
				PermissibleValueInterface perValue = (PermissibleValueInterface)values[i];
				String value = perValue.getValueAsObject().toString();
				if (editLimitPermissibleValues != null && editLimitPermissibleValues.contains(value))
				{
					html.append("\n<option class=\"PermissibleValuesQuery\" title=\""+value+"\" value=\"" + value + "\" SELECTED>" + value + "</option>");

				}
				else
				{
					html.append("\n<option class=\"PermissibleValuesQuery\" title=\""+value+"\" value=\"" + value + "\">" + value + "</option>");
				}
			}
			html.append("\n</select>");
			html.append("\n</td>");
		}
		return html.toString();
	}
	/**
	 * 
	 * @param attributeCollection
	 * @return
	 */
	String getAttributesString(Collection attributeCollection)
	{
		String attributesList = "";
		if (!attributeCollection.isEmpty())
		{
			Object[] attributes = attributeCollection.toArray();
			Arrays.sort(attributes, new AttributeInterfaceComparator());
			for(int i=0;i<attributes.length;i++)
			{
				AttributeInterface attribute = (AttributeInterface) attributes[i];
				String attrName = attribute.getName();
				String componentId = attrName + attribute.getId().toString();
				attributesList = attributesList + ";" + componentId;
			}
		}
		return attributesList;
	}
}
