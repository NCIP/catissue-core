<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.common.actionForm.SimpleQueryInterfaceForm,java.util.List,edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.common.query.Operator"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>


<head>
<style>
.hideTD
{
	display:none;
}
</style>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
</head>
<%
	String[] attributeConditionArray = (String[])request.getAttribute(Constants.ATTRIBUTE_CONDITION_LIST);
	String aliasName = (String)request.getAttribute(Constants.TABLE_ALIAS_NAME);
	String noOfRows="1";
	String showCal = "";
	String dateClass = "hideTD";
	Object obj = request.getAttribute("simpleQueryInterfaceForm");
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	
	String selectMenu = (String) request.getAttribute(Constants.MENU_SELECTED);
	String objectChanged="";
	if(obj != null && obj instanceof SimpleQueryInterfaceForm)
	{
		SimpleQueryInterfaceForm form = (SimpleQueryInterfaceForm)obj;
		noOfRows = form.getCounter();		
		objectChanged = request.getParameter("objectChanged");
       	
		if(objectChanged != null && !objectChanged.equals(""))
		{
			form.setValue("SimpleConditionsNode:"+Integer.parseInt(noOfRows)+"_Condition_DataElement_field",null);		
		}		
	}
        
	String title = (String)request.getAttribute(Constants.SIMPLE_QUERY_INTERFACE_TITLE);
	
	String alias = (String)request.getParameter("aliasName");
	if(pageOf.equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
	{
		title="simpleQuery.title";
	}
	else
	{
		title=alias+".Search";
	}	
	
%>
<script>

function callAction(action)
{
	document.forms[0].action = action;
	document.forms[0].submit();
}

function onObjectChange(element,action)
{
	var index = element.name.indexOf("(");
	var lastIndex = element.name.lastIndexOf(")");
	
	var saveObject = document.getElementById("objectChanged");	
	saveObject.value = element.name.substring(index+1,lastIndex);
	
	callAction(action);
}

function setPropertyValue(propertyName, value)
{
	for (var i=0;i < document.forms[0].elements.length;i++)
    {
    	var e = document.forms[0].elements[i];
        if (e.name == propertyName)
        {
        	document.forms[0].elements[i].value = value;
        }
    }
}
function incrementCounter()
{
	document.forms[0].counter.value = parseInt(document.forms[0].counter.value) + 1;
}
function decrementCounter()
{
	document.forms[0].counter.value = parseInt(document.forms[0].counter.value) - 1;
} 

function showDateColumn(element,valueField,columnID,showCalendarID,fieldValue,overDiv)
{
	var dataStr = element.options[element.selectedIndex].value;
	var dataValue = new String(dataStr);
	var lastInd = dataValue.lastIndexOf(".");
	if(lastInd == -1)
		return;
	else
	{
		var dataType = dataValue.substr(lastInd+1);

		var txtField = document.getElementById(valueField);
		txtField.value="";

		var calendarShow = document.getElementById(showCalendarID);

		if (dataType == "<%=Constants.FIELD_TYPE_DATE%>" || dataType == "<%=Constants.FIELD_TYPE_TIMESTAMP_DATE%>")
		{
			var td = document.getElementById(columnID);
			txtField.readOnly="";
			calendarShow.value = "Show";
			var innerStr = "<div id='"+ overDiv +"' style='position:absolute; visibility:hidden; z-index:1000;'></div>";
			innerStr = innerStr + "<a href=\"javascript:show_calendar('"+fieldValue+"',null,null,'MM-DD-YYYY');\">";
			innerStr = innerStr + "<img src=\"images\\calendar.gif\" width=24 height=22 border=0>";
			innerStr = innerStr + "</a>";
			td.innerHTML = innerStr;
		}
		else
		{
			var td = document.getElementById(columnID);
			td.innerHTML = "&nbsp;";
			txtField.readOnly="";
			calendarShow.value = "";
		}	
	}	
}

function onAttributeChange(element,opComboName,txtFieldID)
{
	var columnValue = element.options[element.selectedIndex].value;
	var index = columnValue.lastIndexOf(".");
	
	var opCombo = document.getElementById(opComboName);
	opCombo.options.length=0;

	var txtField = document.getElementById(txtFieldID);
	txtField.disabled = false;
	
	if(element.value == "<%=Constants.SELECT_OPTION%>")
	{
		opCombo.options[0] = new Option("<%=Constants.SELECT_OPTION%>","-1");
	}
	else
	{
		//If the datatype of selected column "varchar" or "text"
		if(columnValue.match("varchar") == "varchar" || columnValue.match("text") == "text")
		{
			opCombo.options[0] = new Option("<%=Operator.STARTS_WITH%>","<%=Operator.STARTS_WITH%>");
			opCombo.options[1] = new Option("<%=Operator.ENDS_WITH%>","<%=Operator.ENDS_WITH%>");
			opCombo.options[2] = new Option("<%=Operator.CONTAINS%>","<%=Operator.CONTAINS%>");
			opCombo.options[3] = new Option("Equals","<%=Operator.EQUAL%>");
			opCombo.options[4] = new Option("Not Equals","<%=Operator.NOT_EQUALS%>");
		}
		else if (columnValue.match("<%=Constants.FIELD_TYPE_TINY_INT%>") == "<%=Constants.FIELD_TYPE_TINY_INT%>")
		{
			opCombo.options[0] = new Option("Equals","<%=Operator.EQUAL%>");
			opCombo.options[1] = new Option("Not Equals","<%=Operator.NOT_EQUALS%>");
		}
		else
		{
			opCombo.options[0] = new Option("Equals","<%=Operator.EQUAL%>");
			opCombo.options[1] = new Option("Not Equals","<%=Operator.NOT_EQUALS%>");
			opCombo.options[2] = new Option("<%=Operator.LESS_THAN%>","<%=Operator.LESS_THAN%>");
			opCombo.options[3] = new Option("<%=Operator.LESS_THAN_OR_EQUALS%>","<%=Operator.LESS_THAN_OR_EQUALS%>");
			opCombo.options[4] = new Option("<%=Operator.GREATER_THAN%>","<%=Operator.GREATER_THAN%>");
			opCombo.options[5] = new Option("<%=Operator.GREATER_THAN_OR_EQUALS%>","<%=Operator.GREATER_THAN_OR_EQUALS%>");
		}

		opCombo.options[opCombo.options.length] = new Option("<%=Operator.IS_NULL%>","<%=Operator.IS_NULL%>");
		opCombo.options[opCombo.options.length] = new Option("<%=Operator.IS_NOT_NULL%>","<%=Operator.IS_NOT_NULL%>");
	}
}

function showDatafield(element,txtFieldID)
{
	var dataStr = element.options[element.selectedIndex].value;
	var dataValue = new String(dataStr);
	var txtField = document.getElementById(txtFieldID);
	
	if(dataValue == "<%=Operator.IS_NULL%>" || dataValue == "<%=Operator.IS_NOT_NULL%>")
	{
		txtField.disabled = true;
	}
	else
	{
		txtField.disabled = false;
	}
}

function enableLastCheckbox()
{
	var lastRowNo = document.forms[0].counter.value;
	var chkBox = document.getElementById("chk_"+lastRowNo);
	if(lastRowNo>1)
		chkBox.disabled = false;
}

function enablePreviousCheckBox(element)
{
   	var elementName = element.name;
   	var index = elementName.indexOf('_');
   	var previousRowNo = parseInt(elementName.substring(index+1))-1;
    if (element.checked == true)
    {
      	if(previousRowNo > 1)
      	{
			var previousElement = document.getElementById("chk_"+previousRowNo);
			previousElement.disabled = false;     
		}
    }
    else if(element.checked == false)
    {
      	for(var i=previousRowNo;i>=1;i--)
      	{
			var previousElement = document.getElementById("chk_"+i);
			previousElement.checked = false;
			previousElement.disabled = true;     
		}
	}
}

</script>

<html:errors />
<body onload="enableLastCheckbox()">
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="620">
	
	<html:form action="<%=Constants.SIMPLE_SEARCH_ACTION%>">
	
		<!-- SIMPLE QUERY INTERFACE BEGINS-->
		<tr>
		  <td>
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<br/>
				<tr>
					<td>
						<html:hidden property="aliasName" value="<%=aliasName%>"/>
						<html:hidden property="<%=Constants.MENU_SELECTED%>" value="<%=selectMenu%>"/>
						<input type="hidden" name="objectChanged" id="objectChanged" value="">
					</td>
				</tr>
				<tr>
					<td>
						<html:hidden property="counter" value="<%=noOfRows%>"/>
					</td>
				</tr>
				<tr>
					<td>
						<html:hidden property="pageOf" value="<%=pageOf%>"/>
					</td>
				</tr>
				<tr>
					<td>
						<html:hidden property="andOrOperation" />
					</td>
				</tr>
				<tr>
					<td class="formTitle" height="20" colspan="7">
						<bean:message key="<%=title%>" />
					</td>
					<logic:equal name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
					<%String addAction = "setPropertyValue('andOrOperation','true');"+
									"incrementCounter();callAction('SimpleQueryInterfaceValidate.do?pageOf="+pageOf+"');"; 
					%>
					<td class="formTitle" align="Right">
						<html:button property="addKeyValue" styleClass="actionButton" onclick="<%=addAction%>">
							<bean:message key="buttons.addMore"/>
						</html:button>
					</td>
					</logic:equal>
				</tr>
				
				<tr>
						<logic:equal name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
						<td class="formLeftSubTitle" width="5">#</td>
						<td class="formLeftSubTitle">
							<label for="delete" align="center">
								<bean:message key="addMore.delete" />
							</label>
						</td>
						<td class="formLeftSubTitle" >
							<label for="object" >
								<bean:message key="query.object" />
							</label>
						</td>
						
						</logic:equal>
						<td class="formLeftSubTitle">
							<label for="attributes" >
								<bean:message key="query.attributes" />
							</label>
						</td>
						<td class="formLeftSubTitle">
							<label for="attributes" >
								<bean:message key="query.conditions" />
							</label>
						</td>
						<td class="formLeftSubTitle" colspan="2">
							<label for="value" >
								<bean:message key="query.value" />
							</label>
						</td>
						
						<logic:equal name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
						<td class="formLeftSubTitle" >
							<label for="attributes" >
								<bean:message key="query.operator" />
							</label>
				</td>
				</logic:equal>						

				</tr>

				
				<tbody id="simpleQuery">
				<%
					for (int i=1;i<=Integer.parseInt(noOfRows);i++){
						String objectName = "value(SimpleConditionsNode:"+i+"_Condition_DataElement_table)";
						String attributeName = "value(SimpleConditionsNode:"+i+"_Condition_DataElement_field)";
						
						String attributeCondition = "value(SimpleConditionsNode:"+i+"_Condition_Operator_operator)";
						String attributeValue = "value(SimpleConditionsNode:"+i+"_Condition_value)";
						String attributeValueID = "SimpleConditionsNode"+i+"_Condition_value_ID";
						String nextOperator = "value(SimpleConditionsNode:"+i+"_Operator_operator)";			
						String attributeNameList = "attributeNameList"+i;
						String attributeDisplayNameList = "attributeDisplayNameList"+i;
						String objectNameList = "objectList"+i;
						String objectDisplayNameList = "objectDisplayNameList"+i;
						SimpleQueryInterfaceForm form = (SimpleQueryInterfaceForm)obj;
						String nextOperatorValue = (String)form.getValue("SimpleConditionsNode:"+i+"_Operator_operator");
						String columnID = "calTD"+i;
						String showCalendarKey = "SimpleConditionsNode:"+i+"_showCalendar";			
						String showCalendarValue = "showCalendar(SimpleConditionsNode:"+i+"_showCalendar)";
						String fieldName = "simpleQueryInterfaceForm."+attributeValueID;
						String overDiv = "overDiv";
						String check = "chk_"+i;
						if(i>1)
						{
							overDiv = overDiv + "" + i;
						}
						String functionName = "showDateColumn(this,'"+ attributeValueID +"','" + columnID + "','" + showCalendarValue + "','"+fieldName+"','"+overDiv+"')";
						String attributeId = "attribute" + i;
						String operatorId = "operator" + i;
						String onAttributeChange = "onAttributeChange(this,'" + operatorId + "','" + attributeValueID + "'); " + functionName;
						String operatorFunction = "showDatafield(this,'" + attributeValueID +"')";
						String attributeConditionKey = "SimpleConditionsNode:"+i+"_Condition_Operator_operator";
				%>					
				<tr>
				<logic:equal name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
					<td class="formSerialNumberField" width="5"><%=i%>.</td>
					<!--td class="formRequiredNotice" width="5">&nbsp;</td-->
					<td class="formField" width="5">
							<input type=checkbox name="<%=check %>" id="<%=check %>" disabled="true" onClick="enablePreviousCheckBox(this);enableButton(document.forms[0].deleteValue,document.forms[0].counter,'chk_')">		
					</td>
					
					</logic:equal>
					<%
						String attributeAction = "javascript:onObjectChange(this,'SimpleQueryInterface.do?pageOf="+pageOf;
						if (aliasName != null)
							attributeAction = attributeAction + "&aliasName="+aliasName+"')";
						else
							attributeAction = attributeAction + "')";
					%>
<!-- Mandar : 434 : for tooltip -->
						<logic:equal name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
						<td class="onlyBottomBorder">
						<html:select property="<%=objectName%>" styleClass="formFieldSized15" styleId="<%=objectName%>" size="1" onchange="<%=attributeAction%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<logic:notPresent name="<%=objectNameList%>">			
								<html:options collection="objectNameList" labelProperty="name" property="value"/>
							</logic:notPresent>	
							<logic:present name="<%=objectNameList%>">		
								<html:options collection="<%=objectNameList%>" labelProperty="name" property="value"/>
							</logic:present>	
						</html:select>
						</td>
						</logic:equal>
						<logic:notEqual name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
							<html:hidden property="<%=objectName%>" value="<%=aliasName%>"/>
						</logic:notEqual>
					<td class="tabrightmostcell">
<!-- Mandar : 434 : for tooltip -->
						<html:select property="<%=attributeName%>" styleClass="formFieldSized15" styleId="<%=attributeId%>" size="1" onchange="<%=onAttributeChange%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<logic:notPresent name="<%=attributeNameList%>">
								<html:options name="attributeNameList" labelName="attributeNameList" />
							</logic:notPresent>	
							<logic:present name="<%=attributeNameList%>">		
								<html:options collection="<%=attributeNameList%>" labelProperty="name" property="value"/>
							</logic:present>	
						</html:select>
					</td>
					<td class="formField">
<!-- Mandar : 434 : for tooltip -->
						<html:select property="<%=attributeCondition%>" styleClass="formFieldSized10" styleId="<%=operatorId%>" size="1" onchange="<%=operatorFunction%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
						<%
							String attributeNameKey = "SimpleConditionsNode:"+i+"_Condition_DataElement_field";
							String attributeNameValue = (String)form.getValue(attributeNameKey);
							String attributeType = null;	
							List columnNameValueBeanList = (List) request.getAttribute(attributeNameList);		
							System.out.println("---------"+objectChanged);
							if(columnNameValueBeanList != null && !columnNameValueBeanList.isEmpty() && i==Integer.parseInt(noOfRows) && (objectChanged == null || !objectChanged.equals("")))
							{								
								NameValueBean nameValueBean = (NameValueBean) columnNameValueBeanList.get(0);
								attributeNameValue = nameValueBean.getValue();
								
							}	
							if(attributeNameValue != null)
							{
								StringTokenizer tokenizer = new StringTokenizer(attributeNameValue,".");
								int tokenCount = 1;
								while(tokenizer.hasMoreTokens())
								{
									attributeType = tokenizer.nextToken();
									if(tokenCount == 3) break;
									tokenCount++;
								}
								System.out.println("attributeType-----------"+attributeType);							
								if(attributeType.equals("varchar") || attributeType.equals("text"))
								{
							%>
								<html:option value="<%=Operator.STARTS_WITH%>"><%=Operator.STARTS_WITH%></html:option>
								<html:option value="<%=Operator.ENDS_WITH%>"><%=Operator.ENDS_WITH%></html:option>
								<html:option value="<%=Operator.CONTAINS%>"><%=Operator.CONTAINS%></html:option>
								<html:option value="<%=Operator.EQUAL%>">Equals</html:option>
								<html:option value="<%=Operator.NOT_EQUALS%>">Not Equals</html:option>
							<%
								}
								else if (attributeType.equals(Constants.FIELD_TYPE_TINY_INT))
								{
							%>
								<html:option value="<%=Operator.EQUAL%>">Equals</html:option>
								<html:option value="<%=Operator.NOT_EQUALS%>">Not Equals</html:option>
							<%
								}
								else if (attributeType.equals(Constants.FIELD_TYPE_BIGINT) || attributeType.equals("double"))
								{
							%>
								<html:option value="<%=Operator.EQUAL%>">Equals</html:option>
								<html:option value="<%=Operator.NOT_EQUALS%>">Not Equals</html:option>
								<html:option value="<%=Operator.LESS_THAN%>"><%=Operator.LESS_THAN%></html:option>
								<html:option value="<%=Operator.LESS_THAN_OR_EQUALS%>"><%=Operator.LESS_THAN_OR_EQUALS%></html:option>
								<html:option value="<%=Operator.GREATER_THAN%>"><%=Operator.GREATER_THAN%></html:option>
								<html:option value="<%=Operator.GREATER_THAN_OR_EQUALS%>"><%=Operator.GREATER_THAN_OR_EQUALS%></html:option>
							<%
								}
								else if (attributeType.equals(Constants.FIELD_TYPE_DATE) || attributeType.equals(Constants.FIELD_TYPE_TIMESTAMP_DATE))
								{
							%>
								<html:option value="<%=Operator.EQUAL%>">Equals</html:option>
								<html:option value="<%=Operator.NOT_EQUALS%>">Not Equals</html:option>
								<html:option value="<%=Operator.LESS_THAN%>"><%=Operator.LESS_THAN%></html:option>
								<html:option value="<%=Operator.LESS_THAN_OR_EQUALS%>"><%=Operator.LESS_THAN_OR_EQUALS%></html:option>
								<html:option value="<%=Operator.GREATER_THAN%>"><%=Operator.GREATER_THAN%></html:option>
								<html:option value="<%=Operator.GREATER_THAN_OR_EQUALS%>"><%=Operator.GREATER_THAN_OR_EQUALS%></html:option>
							<%
								}
								else{
								
							%>
								<html:option value="<%=Operator.STARTS_WITH%>"><%=Operator.STARTS_WITH%></html:option>
								<html:option value="<%=Operator.ENDS_WITH%>"><%=Operator.ENDS_WITH%></html:option>
								<html:option value="<%=Operator.CONTAINS%>"><%=Operator.CONTAINS%></html:option>
								<html:option value="<%=Operator.EQUAL%>">Equals</html:option>
								<html:option value="<%=Operator.NOT_EQUALS%>">Not Equals</html:option>
							<%
								}
							}
							else
							{
						%>
							<html:option value="<%=Operator.STARTS_WITH%>"><%=Operator.STARTS_WITH%></html:option>
							<html:option value="<%=Operator.ENDS_WITH%>"><%=Operator.ENDS_WITH%></html:option>
							<html:option value="<%=Operator.CONTAINS%>"><%=Operator.CONTAINS%></html:option>
							<html:option value="<%=Operator.EQUAL%>">Equals</html:option>
							<html:option value="<%=Operator.NOT_EQUALS%>">Not Equals</html:option>
						<%
							}
						%>
							<html:option value="<%=Operator.IS_NULL%>"><%=Operator.IS_NULL%></html:option>
							<html:option value="<%=Operator.IS_NOT_NULL%>"><%=Operator.IS_NOT_NULL%></html:option>
						</html:select>
						
				</td>
				<td class="onlyBottomBorder" id="<%=columnID%>"  size=3>
				<!--  ********************* Mandar Code ********************** -->	
				<!-- ***** Code added to check multiple rows for Calendar icon ***** -->
				<html:hidden property="<%=showCalendarValue%>" styleId="<%=showCalendarValue%>" />
				<%
					showCal = (String)form.getShowCalendar(showCalendarKey);
					if(showCal != null && showCal.trim().length()>0)
					{
				%>
						<div id="<%=overDiv%>" style="position:absolute; visibility:hidden; z-index:1000;"></div>
						<a href="javascript:show_calendar('<%=fieldName%>',null,null,'MM-DD-YYYY');">
							<img src="images\calendar.gif" width=24 height=22 border=0>
						</a>
				<%		
					}
					else
					{
				%>
						&nbsp;					
				<%						
					}

				%>
				</td>
					<td class="formField">
					<%
						String currentOperatorValue = (String)form.getValue(attributeConditionKey);
						if((currentOperatorValue != null) && (currentOperatorValue.equals(Operator.IS_NOT_NULL) || currentOperatorValue.equals(Operator.IS_NULL)))
						{
					%>
						<html:text styleClass="formFieldSized10" size="30" styleId="<%=attributeValueID%>" property="<%=attributeValue%>" disabled="true"/>
					<%
						}
						else
						{
					%>
						<html:text styleClass="formFieldSized10" size="30" styleId="<%=attributeValueID%>" property="<%=attributeValue%>" />
					<%
						}
					%>
					</td>


						<!--html:hidden property="<%=nextOperator%>"/-->
				<logic:equal name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
					<td class="formField">
					<html:select property="<%=nextOperator%>" >
						<html:option value="And"> 
							<bean:message key="simpleQuery.and" />
						</html:option>
						<html:option value="Or"> 
							<bean:message key="simpleQuery.or" />
						</html:option>
					</html:select>
					</td>
				</logic:equal>
				</tr>
				<%}%>
				</tbody>	
				<tr>
					<td align="right" colspan="7">
					<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0" align="right">
						<tr>
							<td>
								<%String searchAction = "callAction('"+Constants.SIMPLE_SEARCH_ACTION+"')";%>
								<html:button styleClass="actionButton" property="searchButton" onclick="<%=searchAction%>">
									<bean:message  key="buttons.search" />
								</html:button>
							</td>
							<%
							if(pageOf.equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
							{
							String configAction = "callAction('"+Constants.CONFIGURE_SIMPLE_QUERY_VALIDATE_ACTION+"?pageOf=pageOfSimpleQueryInterface')";%>
							<td>
								<html:button styleClass="actionButton" property="configureButton" onclick="<%=configAction%>">
									<bean:message  key="buttons.configure" />
								</html:button>
							</td>
							<!--td>
								<%configAction = "window.location.href='"+Constants.SIMPLE_QUERY_INTERFACE_URL+"'";%>
								<html:button styleClass="actionButton" property="resetButton" onclick="<%=configAction%>">
									<bean:message  key="buttons.reset" />
								</html:button>
							</td-->
							<td>
								<%String deleteAction = "decrementCounter();setPropertyValue('value(SimpleConditionsNode:"+(Integer.parseInt(noOfRows)-1)+"_Operator_operator)','');"+"callAction('SimpleQueryInterface.do?pageOf="+pageOf+"');"; %>
								<html:button property="deleteValue" styleClass="actionButton" onclick="deleteChecked('simpleQuery','SimpleQueryInterface.do?pageOf=<%=pageOf%>',document.forms[0].counter,'chk_',false,document.forms[0].deleteValue)"  disabled="true">
									<bean:message key="buttons.delete"/>
								</html:button>
							</td>
							<%}%>
							
						</tr>
					</table>
					<!-- action buttons end -->
					</td>
					
				</tr>

			</table>
		</td>
	</tr>

	<!-- SIMPLE QUERY INTERFACE ENDS-->
	</html:form>
</table>
</body>