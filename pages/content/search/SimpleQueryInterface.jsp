<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm,java.util.List,edu.wustl.common.beans.NameValueBean"%>

<head>
<style>
.hideTD
{
	display:none;
}
</style>
</head>
<%
	String[] attributeConditionArray = (String[])request.getAttribute(Constants.ATTRIBUTE_CONDITION_LIST);
	String aliasName = (String)request.getAttribute(Constants.TABLE_ALIAS_NAME);
	String noOfRows="1";
	String showCal = "";
	String dateClass = "hideTD";
	Object obj = request.getAttribute("simpleQueryInterfaceForm");
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	if(obj != null && obj instanceof SimpleQueryInterfaceForm)
	{
		SimpleQueryInterfaceForm form = (SimpleQueryInterfaceForm)obj;
		noOfRows = form.getCounter();
		showCal = form.getShowCalendar();
	}
	
	if(showCal != null && showCal.trim().length()>0)
		dateClass = "formField";
        
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
	document.forms[0].action = "/catissuecore/"+action;
	document.forms[0].submit();
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

function showDateColumn(element,valueField,colID)
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

		if (dataType == "date")
		{
			var td = document.getElementById(colID);
			td.className="formField";
			txtField.readOnly="readOnly";
			document.forms[0].showCalendar.value = "Show";
		}
		else
		{
			var td = document.getElementById(colID);
			td.className="hideTD";
			txtField.readOnly="";
			document.forms[0].showCalendar.value = "";
		}	
	}	
}

</script>

<html:errors />

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="620">
	
	<html:form action="<%=Constants.SIMPLE_SEARCH_ACTION%>">
	
		<!-- SIMPLE QUERY INTERFACE BEGINS-->
		<tr>
		  <td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<br/>
				<tr>
					<td>
						<html:hidden property="aliasName" value="<%=aliasName%>"/>
					</td>
				</tr>
				<tr>
					<td>
						<html:hidden property="counter" value="<%=noOfRows%>"/>
					</td>
				</tr>
				<tr>
					<td>
						<%%>
						<html:hidden property="pageOf" value="<%=pageOf%>"/>
					</td>
				</tr>
				<tr>
					<td class="formTitle" height="20" colspan="7">
						<bean:message key="<%=title%>" />
						
					</td>
				</tr>
				<tbody id="simpleQuery">
				<%
					for (int i=1;i<=Integer.parseInt(noOfRows);i++){
						String objectName = "value(SimpleConditionsNode:"+i+"_Condition_DataElement_table)";
						String attributeName = "value(SimpleConditionsNode:"+i+"_Condition_DataElement_field)";
						String attributeCondition = "value(SimpleConditionsNode:"+i+"_Condition_Operator_operator)";
						String attributeValue = "value(SimpleConditionsNode:"+i+"_Condition_value)";
						String nextOperator = "value(SimpleConditionsNode:"+i+"_Operator_operator)";			
						String attributeNameList = "attributeNameList"+i;
						String attributeDisplayNameList = "attributeDisplayNameList"+i;
						String objectNameList = "objectList"+i;
						String objectDisplayNameList = "objectDisplayNameList"+i;
						SimpleQueryInterfaceForm form = (SimpleQueryInterfaceForm)obj;
						String nextOperatorValue = (String)form.getValue("SimpleConditionsNode:"+i+"_Operator_operator");
						String colID = "calTD"+i;
						String functionName = "showDateColumn(this,'"+ attributeValue +"','" + colID + "')";
				%>					
				<tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formField">
					<%
						String attributeAction = "javascript:callAction('SimpleQueryInterface.do?pageOf="+pageOf;
						if (aliasName != null)
							attributeAction = attributeAction + "&aliasName="+aliasName+"')";
						else
							attributeAction = attributeAction + "')";
					%>
						<html:select property="<%=objectName%>" styleClass="formFieldSized15" styleId="<%=objectName%>" size="1" onchange="<%=attributeAction%>">
							<logic:notPresent name="<%=objectNameList%>">			
								<html:options name="objectAliasNameList" labelName="objectDisplayNameList" />
							</logic:notPresent>	
							<logic:present name="<%=objectNameList%>">		
								<html:options collection="<%=objectNameList%>" labelProperty="name" property="value"/>
							</logic:present>	
						</html:select>
					</td>
					<td class="formField">
						<html:select property="<%=attributeName%>" styleClass="formFieldSized15" styleId="<%=attributeName%>" size="1" onchange="<%=functionName%>">
							<logic:notPresent name="<%=attributeNameList%>">			
								<html:options name="attributeNameList" labelName="attributeNameList" />
							</logic:notPresent>	
							<logic:present name="<%=attributeNameList%>">		
								<html:options name="<%=attributeNameList%>" labelName="<%=attributeDisplayNameList%>" />
							</logic:present>	
						</html:select>
					</td>
					<td class="formField">
						<html:select property="<%=attributeCondition%>" styleClass="formFieldSized10" styleId="<%=attributeCondition%>" size="1">
							<html:options name="attributeConditionList" labelName="attributeConditionList" />
						</html:select>
					</td>
					<td class="formField">
						<html:text styleClass="formFieldSized10" size="30" styleId="<%=attributeValue%>" property="<%=attributeValue%>" />
						<html:hidden property="showCalendar" />
					</td>
				<!--  ********************* MD Code ********************** -->	
					<td id="<%=colID%>" class="<%=dateClass%>">
					<%	
						String fieldName = "simpleQueryInterfaceForm."+attributeValue;
					%>
						<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
						<a href="javascript:show_calendar('<%=fieldName%>',null,null,'MM-DD-YYYY');">
							<img src="images\calendar.gif" width=24 height=22 border=0>
						</a>
					</td>
					<logic:equal name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
					<td class="formSmallField">
						<html:hidden property="<%=nextOperator%>"/>
					</td>
					<td class="formField">
					<div>	
					  <%if (nextOperatorValue != null && (false == nextOperatorValue.equals(""))){
							if (nextOperatorValue.equals(Constants.AND_JOIN_CONDITION))
							{%><bean:message key="simpleQuery.and" />	
							<%}else{%>
							<bean:message key="simpleQuery.or" />
							<%}%>
						<%}else{ %>					
							<%String andLink = "setPropertyValue('"+nextOperator+"','"+Constants.AND_JOIN_CONDITION+"');incrementCounter();callAction('SimpleQueryInterface.do?pageOf="+pageOf+"')"; %>
							<a href="#" onclick="<%=andLink%>">
								<bean:message key="simpleQuery.and" />
							</a>|
							<%String orLink = "setPropertyValue('"+nextOperator+"','"+Constants.OR_JOIN_CONDITION+"');incrementCounter();callAction('SimpleQueryInterface.do?pageOf="+pageOf+"')"; %>
							<a href="#" onclick="<%=orLink%>">
								<bean:message key="simpleQuery.or" />
							</a>
						<%}%>	
					</div>
					</td>	
					</logic:equal>
				</tr>
				<%}%>
				</tbody>	
				<tr>
					<td align="right" colspan="7">
					<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
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
							String configAction = "callAction('"+Constants.CONFIGURE_SIMPLE_QUERY_ACTION+"')";%>
							<td>
								<html:button styleClass="actionButton" property="configureButton" onclick="<%=configAction%>">
									<bean:message  key="buttons.configure" />
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