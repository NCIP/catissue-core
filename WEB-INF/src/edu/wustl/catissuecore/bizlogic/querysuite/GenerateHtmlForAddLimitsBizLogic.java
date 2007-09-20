
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.PermissibleValue;
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
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.util.AttributeInterfaceComparator;
import edu.wustl.cab2b.common.util.PermissibleValueComparator;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.cde.CommonUtilities;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.util.ParseXMLFile;
import edu.wustl.common.util.Utility;

/**
 * This class generates UI for 'Add Limits' and 'Edit Limits' section.
 * @author deepti_shelar
 */
public class GenerateHtmlForAddLimitsBizLogic
{
	/**
	 * Object which holds data operators fro attributes.
	 */
	static ParseXMLFile parseFile = null;

	private int expressionId = -1;
		
	public int getExpressionId() {
		return expressionId;
	}



	public void setExpressionId(int expressionId) {
		this.expressionId = expressionId;
	}



	


	/**
	 * Constructor for GenerateHtmlForAddLimitsBizLogic
	 * @throws CheckedException 
	 */
	public GenerateHtmlForAddLimitsBizLogic() 
	{
		if (parseFile == null)
		{
			try
			{
				parseFile = ParseXMLFile.getInstance(Constants.DYNAMIC_UI_XML);
			}
			catch (CheckedException e)
			{
				System.out.println("Exception has occured while parsing dynamicUI.xml");
				e.printStackTrace();
			}
		}
	}
	
	
	
	public StringBuffer generateSaveQueryPreHTML()
	{
		StringBuffer generatedPreHTML = new StringBuffer();
		generatedPreHTML.append("<table border=\"0\" width=\"100%\"  callspacing=\"0\" cellpadding=\"0\">");
		return generatedPreHTML;
	}
	
	
	
	public StringBuffer generateSaveQueryForEntity(int expressionID,EntityInterface entity, List<ICondition> conditions,boolean isShowAll)
	{
		setExpressionId(expressionID);
		System.out.println("Entity :---->"+entity.getName() +" contains conditions :----> "+conditions.size());
		StringBuffer generatedHTML = new StringBuffer();
		Collection attributeCollection = entity.getAttributeCollection();
		String attributesList = "";
		boolean isBGColor = false;
		Map<String, ICondition> attributeNameConditionMap = getMapOfConditions(conditions);
		System.out.println("attributeCollection Size :---->"+attributeCollection.size()+"  attributeNameConditionMap  Size----> "+attributeNameConditionMap.size() );
		if (!attributeCollection.isEmpty())
		{ 
			List attributes = new ArrayList(attributeCollection);
			String styleSheetClass = "rowBGWhiteColor";
			Collections.sort(attributes, new AttributeInterfaceComparator());
			for(int i=0;i<attributes.size();i++)
			{
				AttributeInterface attribute = (AttributeInterface) attributes.get(i);
				String attrName = attribute.getName();
				if (!attributeNameConditionMap.containsKey(attrName)&&!isShowAll)
				{
					continue;
				}
				String attrLabel =Utility.getDisplayLabel(attrName);
				String componentId = generateComponentName(attribute);
				attributesList = attributesList + ";" + componentId;
				if(isBGColor)
				{
					styleSheetClass = "rowBGGreyColor1";
				}
				else
				{
					styleSheetClass = "rowBGWhiteColor";
				}
				isBGColor = !isBGColor;
				generatedHTML.append("\n<tr class='"+styleSheetClass+"' id=\"" + componentId + "\" height=\"6%\">\n" +
						generateCheckBox(attribute,false)+
						"<td valign='top' align='right' >" +
						"<input type=\"textbox\" name='displayName"+attribute.getId()+"' value='"+expressionID+"." +attrLabel+"' disabled='true'> " +
						"</td>");
				generatedHTML.append("<td valign='top' align='right' class='standardTextQuery' nowrap='nowrap' width=\"15%\"><b>"+attrLabel+" ");
				if(attribute.getDataType().equalsIgnoreCase(Constants.DATE))
				{
					String dateFormat = Constants.DATE_FORMAT;//ApplicationProperties.getValue("query.date.format");
					generatedHTML.append("\n("+dateFormat+")");
				}
				generatedHTML.append(":&nbsp;&nbsp;&nbsp;&nbsp;</b></td>\n");
				List<String> operatorsList = getConditionsList(attribute);
				boolean isBetween = false;
				if (!operatorsList.isEmpty() && operatorsList.get(0).equalsIgnoreCase(RelationalOperator.Between.toString()))
				{
					isBetween = true;
				}
				List<PermissibleValueInterface> permissibleValues = getPermissibleValuesList(attribute);
				if (conditions != null)
				{
					if(attributeNameConditionMap.containsKey(attrName))
					{
						ICondition condition = attributeNameConditionMap.get(attrName);
						List<String> values = (List<String>) condition.getValues();
						String operator = condition.getRelationalOperator().toString();
						generatedHTML.append("\n" + generateHTMLForOperators(attribute, operatorsList, operator));
						if (operator.equalsIgnoreCase(RelationalOperator.Between.toString()))
						{
							isBetween = true;
						}
						else
						{
							isBetween = false;
						}
						if (!permissibleValues.isEmpty())
						{
							generatedHTML.append("\n" + generateHTMLForEnumeratedValues(attribute, permissibleValues, values));
						}
						else
						{
							if(attribute.getDataType().equalsIgnoreCase("boolean"))
							{
								generatedHTML.append("\n" + generateHTMLForRadioButton(attribute,values));	
							}
							else
							{
								generatedHTML.append("\n" + generateHTMLForTextBox(attribute, isBetween, values,operator));
							}
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
							if(attribute.getDataType().equalsIgnoreCase("boolean"))
							{
								generatedHTML.append("\n" +generateHTMLForRadioButton(attribute,null));	
							}
							else
							{
								generatedHTML.append("\n" + generateHTMLForTextBox(attribute, isBetween, null,null));
							}
						}
					}
						
				}
			
				generatedHTML.append("\n</tr>");
				
				
			}
			generatedHTML.append("<tr>" +
					                "<td> <input type='hidden'  name='"+
					                "/>"+
					                "</td>"+
								"</tr>");
		}
		
		return generatedHTML;
	}
	
	
	private String generateCheckBox(AttributeInterface attribute , boolean isSelected)
	{
	    String select = (isSelected?"select":"");	
	    String componentId = generateComponentName(attribute);
		String tag = "<td valign='top' align='right' width=\"10%\"><input type=\"checkbox\"  name='"+componentId+"_checkbox'"+  select+"  onClick=\"enableDisplayField(this.form,'"+attribute.getId()+"')\"></td>" ; 
		return tag;
	}
	
	
	private String generateComponentName(AttributeInterface attribute )
	{
		String componentId =""; 
		if(getExpressionId()> -1)
		{
			componentId = getExpressionId()+"_";
		}
			
		String attributeName = attribute.getName();
		componentId = componentId +attributeName + attribute.getId().toString();
		return componentId;
		
	}
	
	
	/* This method generates the html for Save Query section.
	 * This internally calls methods to generate other UI components like text, Calendar, Combobox etc.
	 * This method is same as the generateHTML except that this will generate html for selected conditions
	 * and will display only those conditions with their values set by user.
	 * @param entity entity to be presented on UI.
	 * @param conditions List of conditions , will contains atleast one element always.
	 * @return String html generated for Save Query section.
	 * */
	
	public String generateHTMLForSavedQuery(Map<IExpressionId,Map<EntityInterface, List>>expressionMap,boolean isShowAll)
	{
		StringBuffer generatedHTML = new StringBuffer("<table>");
		Map<EntityInterface, List> entityConditionMap = null;
		if(expressionMap.isEmpty())
		{
			generatedHTML.append("No record found.");
			return generatedHTML.toString();
		}
		else
		{
		   Iterator it = expressionMap.keySet().iterator();
		   while(it.hasNext())
		   {
			   IExpressionId expressionId = (IExpressionId)it.next();
			   entityConditionMap = expressionMap.get(expressionId);
			   if(entityConditionMap.isEmpty())
			   {
				   continue;
			   }
			   Iterator it2 = entityConditionMap.keySet().iterator();
			   while(it2.hasNext())
			   {
			    EntityInterface entity =(EntityInterface)it2.next();
			    List<ICondition> conditions =  entityConditionMap.get(entity);
			    generatedHTML.append( generateSaveQueryForEntity(expressionId.getInt(),entity, conditions,isShowAll));
			   }
		   }
		}
	
		generatedHTML.append("</table>");
		return generateSaveQueryPreHTML().toString()+" "+generatedHTML.toString();
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
		StringBuffer generatedPreHTML = new StringBuffer();
		StringBuffer generatedHTML = new StringBuffer();
		String nameOfTheEntity = entity.getName();
		String entityName = Utility.parseClassName(nameOfTheEntity);//nameOfTheEntity.substring(nameOfTheEntity.lastIndexOf(".")+1,nameOfTheEntity.length());
		Collection attributeCollection = entity.getAttributeCollection();
		boolean isEditLimits = false;
		String header = Constants.DEFINE_SEARCH_RULES;
		//ApplicationProperties.getValue("query.defineSearchRulesFor");//"\nDefine Search Rules For";//
		String attributesList = "";
		generatedPreHTML.append("<table border=\"0\" width=\"100%\" height=\"30%\" callspacing=\"0\" cellpadding=\"0\">");
		generatedPreHTML.append("\n<tr height=\"2%\"> ");
		generatedPreHTML.append("<td valign='top' height=\"2%\" colspan=\"8\" bgcolor=\"#EAEAEA\" ><font face=\"Arial\" size=\"2\" color=\"#000000\"><b>");
		generatedPreHTML.append(header + " '" + entityName + "'</b></font>");
		generatedPreHTML.append("\n</td>");
		
		boolean isTopButton = true;
		if(conditions != null)
		{ 
			isEditLimits = true;
		}
		generatedPreHTML.append(generateHTMLForButton(nameOfTheEntity, getAttributesString(attributeCollection), isEditLimits,isTopButton));
		generatedPreHTML.append("\n</tr></table>");
		generatedHTML.append("<table border=\"0\" width=\"100%\" height=\"100%\" callspacing=\"0\" cellpadding=\"0\">");
		//generatedHTML.append("\n<tr>");
//		generatedHTML
//		.append("<td valign='top' height=\"4%\" colspan=\"8\" bgcolor=\"#EAEAEA\" style=\"border:solid 1px\"><font face=\"Arial\" size=\"2\" color=\"#000000\"><b>");
//		generatedHTML.append(header + " '" + entityName + "'</b></font>");
//		generatedHTML.append("\n</td></tr>");
		//generatedHTML.append("\n<tr><td valign='top' height=\"3%\" colspan=\"8\" bgcolor=\"#FFFFFF\">&nbsp;</td></tr>");
//		if(conditions != null)
//		{ 
//			isEditLimits = true;
//		}
		
		boolean isBGColor = false;
		generatedHTML.append("\n<tr>");
		generatedHTML.append("\n<td valign=\"top\">");
		generatedHTML.append("\n</td>");
		generatedHTML.append("\n</tr>");
		//generatedHTML.append(generateHTMLForButton(nameOfTheEntity, getAttributesString(attributeCollection), isEditLimits,isTopButton));
		if (!attributeCollection.isEmpty())
		{ 
			List attributes = new ArrayList(attributeCollection);
			String styleSheetClass = "rowBGWhiteColor";
			Collections.sort(attributes, new AttributeInterfaceComparator());
			for(int i=0;i<attributes.size();i++)
			{
				AttributeInterface attribute = (AttributeInterface) attributes.get(i);
				String attrName = attribute.getName();
				String attrLabel = Utility.getDisplayLabel(attrName);
				String componentId = generateComponentName(attribute);
				attributesList = attributesList + ";" + componentId;
				if(isBGColor)
				{
					styleSheetClass = "rowBGGreyColor1";
				}
				else
				{
					styleSheetClass = "rowBGWhiteColor";
				}
				isBGColor = !isBGColor;
				generatedHTML.append("\n<tr class='"+styleSheetClass+"' id=\"" + componentId + "\" height=\"6%\">\n" +
						"<td valign='top' align='right' class='standardTextQuery' nowrap='nowrap' width=\"15%\"><b>");
				generatedHTML.append(attrLabel+" ");
				if(attribute.getDataType().equalsIgnoreCase(Constants.DATE))
				{
					String dateFormat = Constants.DATE_FORMAT;//ApplicationProperties.getValue("query.date.format");
					generatedHTML.append("\n("+dateFormat+")");
				}
				generatedHTML.append(":&nbsp;&nbsp;&nbsp;&nbsp;</b></td>\n");
				List<String> operatorsList = getConditionsList(attribute);
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
						List<String> values = (List<String>) condition.getValues();
						String operator = condition.getRelationalOperator().toString();
						generatedHTML.append("\n" + generateHTMLForOperators(attribute, operatorsList, operator));
						if (operator.equalsIgnoreCase(RelationalOperator.Between.toString()))
						{
							isBetween = true;
						}
						else
						{
							isBetween = false;
						}
						if (!permissibleValues.isEmpty())
						{
							generatedHTML.append("\n" + generateHTMLForEnumeratedValues(attribute, permissibleValues, values));
						}
						else
						{
							if(attribute.getDataType().equalsIgnoreCase("boolean"))
							{
								generatedHTML.append("\n" + generateHTMLForRadioButton(attribute,values));	
							}
							else
							{
								generatedHTML.append("\n" + generateHTMLForTextBox(attribute, isBetween, values,operator));
							}
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
							if(attribute.getDataType().equalsIgnoreCase("boolean"))
							{
								generatedHTML.append("\n" +generateHTMLForRadioButton(attribute,null));	
							}
							else
							{
								generatedHTML.append("\n" + generateHTMLForTextBox(attribute, isBetween, null,null));
							}
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
						if(attribute.getDataType().equalsIgnoreCase("boolean"))
						{
							generatedHTML.append("\n" +generateHTMLForRadioButton(attribute,null));	
						}
						else
						{
							generatedHTML.append("\n" + generateHTMLForTextBox(attribute, isBetween, null,null));
						}
					}
				}
				generatedHTML.append("\n</tr>");
			}
		}
		isTopButton = false;
		generatedHTML.append("\n<tr>");
		generatedHTML.append("\n<td valign=\"top\">");
		generatedHTML.append("\n</td>");
		generatedHTML.append("\n</tr>");
		//generatedHTML.append(generateHTMLForButton(nameOfTheEntity, attributesList, isEditLimits,isTopButton));
		generatedHTML.append("</table>");
		return generatedPreHTML.toString()+"####"+generatedHTML.toString();
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

				else if (permValue instanceof ShortValueInterface)
					permissibleValues.add(((ShortValueInterface) permValue));

				else if (permValue instanceof LongValueInterface)
					permissibleValues.add(((LongValueInterface) permValue));

				else if (permValue instanceof DateValueInterface)
					permissibleValues.add(((DateValueInterface) permValue));

				else if (permValue instanceof BooleanValueInterface)
					permissibleValues.add(((BooleanValueInterface) permValue));

				else if (permValue instanceof IntegerValueInterface)
					permissibleValues.add(((IntegerValueInterface) permValue));

				else if (permValue instanceof DoubleValueInterface)
					permissibleValues.add((DoubleValueInterface) permValue);

				else if (permValue instanceof FloatValueInterface)
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
	private List<String> getConditionsList(AttributeInterface attributeInterface)
	{
		List<String> operatorsList = new ArrayList<String>();
		List strObj = null;
		if (attributeInterface != null)
		{
			String dataType = attributeInterface.getDataType();
			UserDefinedDE userDefineDE = (UserDefinedDE) attributeInterface.getAttributeTypeInformation().getDataElement();
			if (userDefineDE != null)
			{
				if (dataType.equalsIgnoreCase("long") || dataType.equalsIgnoreCase("double") || dataType.equalsIgnoreCase("short") || dataType.equalsIgnoreCase("integer"))
				{
					operatorsList = parseFile.getEnumConditionList("number");
				}
				else if (dataType.equalsIgnoreCase("string"))
				{
					operatorsList = parseFile.getEnumConditionList("string");
				}
				else if (dataType.equalsIgnoreCase("boolean"))
				{
					operatorsList = parseFile.getEnumConditionList("boolean");
				}
				else if (dataType.equalsIgnoreCase("date"))
				{
					operatorsList = parseFile.getEnumConditionList("date");
				}
			}
			else
			{
				if (dataType.equalsIgnoreCase("long") || dataType.equalsIgnoreCase("double") || dataType.equalsIgnoreCase("short") || dataType.equalsIgnoreCase("integer"))
				{
					operatorsList = parseFile.getNonEnumConditionList("number");
				}
				else if (dataType.equalsIgnoreCase("string"))
				{
					operatorsList = parseFile.getNonEnumConditionList("string");
				}
				else if (dataType.equalsIgnoreCase("boolean"))
				{
					operatorsList = parseFile.getNonEnumConditionList("boolean");
				}
				else if (dataType.equalsIgnoreCase("date"))
				{
					operatorsList = parseFile.getNonEnumConditionList("date");
				}
			}
			strObj = new ArrayList(operatorsList);;
			operatorsList = new ArrayList<String>();
			Collections.sort(strObj);
			for (int i = 0; i < strObj.size(); i++)
			{
				if (strObj.get(i) != null)
					operatorsList.add((String) strObj.get(i));
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
		String componentId = generateComponentName(attribute);
		if (operatorsList != null && operatorsList.size() != 0)
		{
			html.append("\n<td width='15%' class=\"dropdownQuery\" valign='top' >");
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
	private String generateHTMLForTextBox(AttributeInterface attributeInterface, boolean isBetween, List<String> values,String op)
	{
		String componentId = generateComponentName(attributeInterface);
		String textBoxId = componentId + "_textBox";
		String textBoxId1 = componentId + "_textBox1";
		String dataType = attributeInterface.getDataType();
		StringBuffer html = new StringBuffer();
		html.append("<td width='15%' valign='top' class=\"standardTextQuery\">\n");
		if (values == null || values.isEmpty())
		{
			html.append("<input style=\"width:150px; display:block;\" type=\"text\" name=\"" + textBoxId + "\" id=\"" + textBoxId + "\">");
		}
		else
		{
			String valueStr = "";
			if(op.equalsIgnoreCase("In") || op.equalsIgnoreCase("Not In"))
			{
				valueStr = values.toString();
				valueStr = valueStr.replace("[", "");
				valueStr = valueStr.replace("]", "");
				html.append("<input style=\"width:150px; display:block;\" type=\"text\" name=\"" + textBoxId + "\" id=\"" + textBoxId + "\" value=\"" + valueStr + "\">");
			}
			else
			{
				html.append("<input style=\"width:150px; display:block;\" type=\"text\" name=\"" + textBoxId + "\" id=\"" + textBoxId + "\" value=\"" + values.get(0) + "\">");
			}
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
		html.append("<td width='15%'  valign='top' class=\"standardTextQuery\">\n");
		if (isBetween)
		{
			if (values == null || values.isEmpty())
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
		//html.append("\n<tr>");
		if(isTopButton)
		{
			buttonId = "TopAddLimitButton";
			html.append("\n<td bgcolor=\"#EAEAEA\" colspan=\"2\" height=\"2%\"valign=\"top\">");
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
		//html.append("\n</tr>");
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
		String componentId = generateComponentName(attribute);
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
		String componentId = generateComponentName(attribute);
		if (permissibleValues != null && permissibleValues.size() != 0)
		{

			html.append("\n<td width='15%' valign='centre' class=\"PermissibleValuesQuery\" >");
			html.append("\n<select style=\"width:150px; display:block;\" MULTIPLE styleId='country' size ='3' name=\"" + componentId
					+ "_enumeratedvaluescombobox\"\">");
			List values = new ArrayList(permissibleValues);
			Collections.sort(values, new PermissibleValueComparator());
			for(int i=0; i<values.size(); i++)
			{
				PermissibleValueInterface perValue = (PermissibleValueInterface)values.get(i);
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
			html.append("\n<td>");
			html.append("\n</td>");
			html.append("\n<td>");
			html.append("\n</td>");
			html.append("\n<td>");
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
			List attributes = new ArrayList(attributeCollection);
			Collections.sort(attributes, new AttributeInterfaceComparator());
			for(int i=0;i<attributes.size();i++)
			{
				AttributeInterface attribute = (AttributeInterface) attributes.get(i);
				String attrName = attribute.getName();
				String componentId = generateComponentName(attribute);
				attributesList = attributesList + ";" + componentId;
			}
		}
		return attributesList;
	}
	private String generateHTMLForRadioButton(AttributeInterface attribute,List<String> values)
	{
		StringBuffer html = new StringBuffer();
		String attributeName = attribute.getName();
		
		String componentId = generateComponentName(attribute)+"_radioButton";
		String componentName = componentId+"_booleanAttribute";
	    String radioButtonTrueId = componentId+"_true";
	    String radioButtonFalseId = componentId+"_false";
		html.append("\n<td>");
		if(values == null)
		{
			//html.append("\n<select class=\"dropdownQuery\" style=\"width:150px; display:block;\" name=\"" + componentId + "_combobox\" onChange=\"operatorChanged('" + componentId + "','true')\">");
			html.append("\n<input type='radio' id = '"+componentId+"_true' value='true' onclick=\"resetOptionButton('" + radioButtonTrueId + "',this)\" name='"+componentName+"'><font class='standardTextQuery'>True</font>");
			html.append("\n<input type='radio' id = '"+componentId+"_false' value='false' onclick=\"resetOptionButton('" + radioButtonFalseId + "',this)\" name='"+componentName+"'><font class='standardTextQuery'>False</font>");
		}
		else
		{
			if(values.get(0).equalsIgnoreCase("true"))
			{
				html.append("\n<input type='radio' id = '"+componentId+"_true' value='true' name='"+componentName+"' checked><font class='standardTextQuery'>True</font>");
				html.append("\n<input type='radio' id = '"+componentId+"_false' value='false' name='"+componentName+"'><font class='standardTextQuery'>False</font>");
			}
			else
			{
				html.append("\n<input type='radio' id = '"+componentId+"_true' value='true' name='"+componentName+"' ><font class='standardTextQuery'>True</font>");
				html.append("\n<input type='radio' id = '"+componentId+"_false' value='false' name='"+componentName+"' checked><font class='standardTextQuery'>False</font>");
			}
		}
		html.append("\n</td>");
		html.append("\n<td>");
		html.append("\n</td>");
		html.append("\n<td>");
		html.append("\n</td>");
		html.append("\n<td>");
		html.append("\n</td>");
		return html.toString();
	}
}
