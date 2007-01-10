
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import edu.common.dynamicextensions.domain.BooleanAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.IntegerAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domain.ShortAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.util.ParseXMLFile;

/**
 * This is used to generate UI for 'Add limits' section for categorySearch.
 * This is called from CategorySearchAction.
 * 
 * @author deepti_shelar
 *
 */
public class GenerateHtmlForAddLimitsBizLogic
{
	/**
	 * This method generates the html for Add Limits section.
	 * This internally calls methods to generate other UI components like text, Calendar, Combobox etc.
	 * @param entity entity to be presented on UI.
	 * @return String html generated for Add Limits section.
	 */
	public String generateHTML(EntityInterface entity)
	{
		StringBuffer generatedHTML = new StringBuffer();
		if (entity != null)
		{
			int lastIndex = entity.getName().lastIndexOf(".");
			String entityName = entity.getName().substring(lastIndex + 1);
			Collection attributeCollection = entity.getAttributeCollection();
			ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources");
			String header = resourceBundle.getString("query.defineSearchRulesFor");
			String attributesList = "";
			generatedHTML.append("<table border=\"0\" width=\"100%\" height=\"100%\" callspacing=\"0\" cellpadding=\"0\">");
			generatedHTML.append("\n<tr>");
			generatedHTML
			.append("<td height=\"4%\" colspan=\"6\" bgcolor=\"#EAEAEA\" style=\"border:solid 1px\"><font face=\"Arial\" size=\"2\" color=\"#000000\"><b>");
			generatedHTML.append(header + " '" + entityName + "'</b></font>");
			generatedHTML.append("\n</td></tr>");
			generatedHTML.append("\n<tr><td height=\"3%\" colspan=\"4\" bgcolor=\"#FFFFFF\">&nbsp;</td></tr>");
			if (attributeCollection != null && !attributeCollection.isEmpty())
			{
				Iterator iter = attributeCollection.iterator();
				while (iter.hasNext())
				{
					AttributeInterface attribute = (AttributeInterface) iter.next();
					if(attribute != null)
					{
						attributesList = attributesList + ";" + attribute.getName();
						generatedHTML.append("\n<tr id=\"" + attribute.getName() + "\" height=\"3%\">\n<td width=\"10%\">");
						generatedHTML.append(attribute.getName() + "</td>\n");
						List operatorsList = populateAttributeUIInformation(attribute);
						generatedHTML.append("\n" + generateHTMLForOperators(attribute, operatorsList));
						boolean isBetween = false;
						if (operatorsList != null && operatorsList.size() != 0 && operatorsList.get(0).toString().equalsIgnoreCase("Between"))
						{
							isBetween = true;
						}
						generatedHTML.append("\n" + generateHTMLForTextBox(attribute, isBetween));
						generatedHTML.append("\n</tr>");
					}
				}
			}
			generatedHTML.append(generateHTMLForButton(entityName, attributesList));
			generatedHTML.append("</table>");
		}
		return generatedHTML.toString();
	}

	/**
	 * This calls XMLParser of commonPackage , to parse dynamicUI.xml and returns a list of operators for the attribute passed to it. 
	 * @param attributeInterface attributeInterface
	 * @return List listOf operators.
	 */
	private List populateAttributeUIInformation(AttributeInterface attributeInterface)
	{
		List<String> operatorsList = new ArrayList<String>();
		AttributeTypeInformationInterface attrTypeInfo = attributeInterface.getAttributeTypeInformation();
		String path = System.getProperty("app.dynamiUIXml");
		if (path != null)
		{
			ParseXMLFile parseFile = new ParseXMLFile(path);
			Object[] strObj = null;
			if (attributeInterface != null)
			{
				if (attrTypeInfo instanceof StringAttributeTypeInformation)
				{
					strObj = parseFile.getNonEnumStr();
					for (int i = 0; i < strObj.length; i++)
					{
						operatorsList.add((String) strObj[i]);
					}
				}
				else if (attrTypeInfo instanceof BooleanAttributeTypeInformation)
				{
					strObj = parseFile.getNonEnumStr();
					for (int i = 0; i < strObj.length; i++)
					{
						operatorsList.add((String) strObj[i]);
					}
				}
				else if (attrTypeInfo instanceof DateAttributeTypeInformation)
				{
					strObj = parseFile.getNonEnumDate();
					for (int i = 0; i < strObj.length; i++)
					{
						operatorsList.add((String) strObj[i]);
					}
				}
				else if (attrTypeInfo instanceof DoubleAttributeTypeInformation || attrTypeInfo instanceof LongAttributeTypeInformation
						|| attrTypeInfo instanceof ShortAttributeTypeInformation || attrTypeInfo instanceof IntegerAttributeTypeInformation)
				{
					strObj = parseFile.getNonEnumNum();
					for (int i = 0; i < strObj.length; i++)
					{
						operatorsList.add((String) strObj[i]);
					}
				}
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
	private String generateHTMLForOperators(AttributeInterface attribute, List operatorsList)
	{
		StringBuffer html = new StringBuffer();
		String attributeName = attribute.getName();
		if (operatorsList != null && operatorsList.size() != 0)
		{
			html.append("\n<td width=\"20%\">");
			AttributeTypeInformationInterface attrTypeInfo = attribute.getAttributeTypeInformation();
			if(attrTypeInfo instanceof DateAttributeTypeInformation)
			{
				html.append("\n<select name=\"" + attributeName + "_combobox\" onChange=\"operatorChanged('" + attributeName + "','true')\">");
			} else
			{
				html.append("\n<select name=\"" + attributeName + "_combobox\" onChange=\"operatorChanged('" + attributeName + "','false')\">");
			}
			Iterator iter = operatorsList.iterator();

			while (iter.hasNext())
			{
				String operator = iter.next().toString();
				html.append("\n<option value=\"" + operator + "\">" + operator + "</option>");
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
	private String generateHTMLForTextBox(AttributeInterface attributeInterface, boolean isBetween)
	{
		String textBoxId = attributeInterface.getName() + "_textBox";
		String textBoxId1 = attributeInterface.getName() + "_textBox1";
		StringBuffer html = new StringBuffer();
		html.append("<td width=\"20%\" class=\"\">\n");
		html.append("<input type=\"text\" name=\"" + textBoxId + "\" id=\"" + textBoxId + "\">");

		html.append("\n</td>");
		if (attributeInterface.getAttributeTypeInformation() instanceof DateAttributeTypeInformation)
		{
			html.append("\n" + generateHTMLForCalendar(attributeInterface.getName(), true, false));
		}
		else
		{
			html.append("\n<td width=\"5%\">&nbsp</td>");
		}
		html.append("\n<td width=\"20%\">");
		if (isBetween)
		{
			html.append("<input type=\"text\" name=\"" + textBoxId1 + "\" id=\"" + textBoxId1 + "\" style=\"display:block\">");
		}
		else
		{
			html.append("<input type=\"text\" name=\"" + textBoxId1 + "\" id=\"" + textBoxId1 + "\" style=\"display:none\">");
		}
		html.append("\n</td>");
		if (attributeInterface.getAttributeTypeInformation() instanceof DateAttributeTypeInformation)
		{
			html.append("\n" + generateHTMLForCalendar(attributeInterface.getName(), false, isBetween));
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
	private String generateHTMLForButton(String entityName, String attributesStr)
	{
		String buttonId = "addLimit";
		StringBuffer html = new StringBuffer();
		html.append("\n<tr>");
		html.append("\n<td valign=\"bottom\">");
		html.append("\n<input type=\"button\" name=\"" + buttonId + "\" onClick=\"produceQuery('addToLimitSet.do', 'categorySearchForm', '"
				+ entityName + "','" + attributesStr + "')\" value=\"Add Limit\"></input>");
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
	private String generateHTMLForCalendar(String attributeName, boolean isFirst, boolean isBetween)
	{
		String innerStr = "";
		//String divId = "overDiv" + (i + 1);
		if (isFirst)
		{
			String textBoxId = attributeName + "_textBox";
			String calendarId = attributeName + "_calendar";
			innerStr = "<td width=\"3%\" id=\"" + calendarId + "\">";
			innerStr = innerStr + "<div id='overDiv' style='position:absolute; visibility:hidden; z-index:1000;'></div>";
			innerStr = innerStr + "<a href=\"javascript:show_calendar('categorySearchForm." + textBoxId + "',null,null,'MM-DD-YYYY');\">";
			innerStr = innerStr + "<img src=\"images\\calendar.gif\" width=\"24\" height=\"22\" border=\"0\">";
			innerStr = innerStr + "</a>";
		}
		else
		{
			String textBoxId1 = attributeName + "_textBox1";
			String calendarId1 = attributeName + "_calendar1";
			String style = "";
			if (isBetween)
			{
				style = "display:block";
			}
			else
			{
				style = "display:none";
			}
			innerStr = "<td width=\"3%\" id=\"" + calendarId1 + "\" style=\"" + style + "\">";
			innerStr = innerStr + "<div id='overDiv' style='position:absolute; visibility:hidden; z-index:1000;'></div>";
			innerStr = innerStr + "<a href=\"javascript:show_calendar('categorySearchForm." + textBoxId1 + "',null,null,'MM-DD-YYYY');\">";
			innerStr = innerStr + "<img src=\"images\\calendar.gif\" width=\"24\" height=\"22\" border=\"0\">";
			innerStr = innerStr + "</a>";
		}
		innerStr = innerStr + "</td>";
		return innerStr.toString();
	}

}
