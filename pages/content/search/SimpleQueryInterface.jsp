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
	List objectNameArray = (List)request.getAttribute(Constants.OBJECT_NAME_LIST);
	String[] attributeConditionArray = (String[])request.getAttribute(Constants.ATTRIBUTE_CONDITION_LIST);
	Object obj = request.getAttribute("simpleQueryInterfaceForm");
	String aliasName = (String)request.getAttribute(Constants.TABLE_ALIAS_NAME);
	String noOfRows="1";
	if(obj != null && obj instanceof SimpleQueryInterfaceForm)
	{
		SimpleQueryInterfaceForm form = (SimpleQueryInterfaceForm)obj;
		noOfRows = form.getCounter();
	}

	String title = (String)request.getAttribute(Constants.SIMPLE_QUERY_INTERFACE_TITLE);
%>
<script>

//  function to insert a row in the inner block
function insertRow(subdivtag)
{
	var val = parseInt(document.forms[0].counter.value);
	val = val + 1;
	document.forms[0].counter.value = val;
	var sname = "";
	
	var r = new Array();
	r = document.getElementById(subdivtag).rows;
	var q = r.length;
	var x=document.getElementById(subdivtag).insertRow(q);
	
	// srno
	var spreqno=x.insertCell(0);
	spreqno.className="formRequiredNotice";
	var rowno=(q);
	spreqno.innerHTML="&nbsp;";
	
	//Object Name List.
	var spreqtype=x.insertCell(1);
	spreqtype.className="formField";
	sname="";
	
	var objectAction = "SimpleConditionsNode"+val;
	var objname = "value(SimpleConditionsNode:"+val+"_Condition_DataElement_table)";
	sname = "<select name='" + objname + "' size='1' class='formFieldSized10' id='" + objname + "' onchange=javascript:callAction('SimpleQueryInterface.do')>";
	<%for(int i=0;i<objectNameArray.size();i++)
	{ NameValueBean nameValueBean = (NameValueBean) objectNameArray.get(i);%>
		sname = sname + "<option value='<%=nameValueBean.getValue()%>'><%=nameValueBean.getName()%></option>";
	<%}%>
	sname = sname + "</select>";
	spreqtype.innerHTML="" + sname;
	
	//Attribute Name List.
	var spreqsubtype=x.insertCell(2);
	spreqsubtype.className="formField";
	sname="";
	
	objname = "value(SimpleConditionsNode:"+val+"_Condition_DataElement_field)";
	sname= "<select name='" + objname + "' size='1' class='formFieldSized10' id='" + objname + "'>";
	sname = sname + "<option value='<%=Constants.SELECT_OPTION%>'><%=Constants.SELECT_OPTION%></option>";
	sname = sname + "</select>"
	spreqsubtype.innerHTML="" + sname;
	
	//Condition List
	var spreqtissuesite=x.insertCell(3);
	spreqtissuesite.className="formField";
	sname="";

	objname = "value(SimpleConditionsNode:"+val+"_Condition_Operator_operator)";
	sname = "<select name='" + objname + "' size='1' class='formFieldSized10' id='" + objname + "'>";
	<%for(int i=0;i<attributeConditionArray.length;i++)
	{%>
		sname = sname + "<option value='<%=attributeConditionArray[i]%>'><%=attributeConditionArray[i]%></option>";
	<%}%>
	sname = sname + "</select>"
	spreqtissuesite.innerHTML="" + sname;
	
	//Attribute Value.
	var spreqpathologystatus=x.insertCell(4);
	spreqpathologystatus.className="formField";
	
	sname="";
	objname = "value(SimpleConditionsNode:"+val+"_Condition_value)";
	sname="<input type='text' name='" + objname + "' value='' class='formFieldSized10' id='" + objname + "'>"        	
	spreqpathologystatus.innerHTML="" + sname;
	
	//Next Operator hidden field.
	var spreqqty=x.insertCell(5)
	spreqqty.className="formSmallField";
	sname="";
	
	objname = "value(SimpleConditionsNode:"+val+"_Operator_operator)";
	sname="<input type='hidden' name='" + objname + "' value='' id='" + objname + "'>"        	
	spreqqty.innerHTML="" + sname;
	
	//Next Operator Link.
	var spreqqty=x.insertCell(6)
	spreqqty.className="formField";
	spreqqty.id=val;
	sname="";
	
	sname="<a href='#' onclick=javascript:insertRow('simpleQuery');setPropertyValue('"+objname+"','AND');changeAndOrLink('"+val+"','AND')><bean:message key='simpleQuery.and'/></a>"
	sname=sname+" / <a href='#' onclick=javascript:insertRow('simpleQuery');setPropertyValue('"+objname+"','OR');changeAndOrLink('"+val+"','OR')><bean:message key='simpleQuery.or' /></a>"        	
	spreqqty.innerHTML="" + sname;
}

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

function changeAndOrLink(id, operation)
{
	var r = document.getElementById(id);
	r.innerHTML = operation;
}

function showDateColumn(element)
{
	var dataStr = element.options[element.selectedIndex].value;
	var dataValue = new String(dataStr);
//	alert(dataValue);
	var lastInd = dataValue.lastIndexOf(".");
//	alert(lastInd);
	if(lastInd == -1)
		return;
	else
	{
		var dataType = dataValue.substr(lastInd+1);

		var txtField = document.getElementById("attributeValue");
		txtField.value="";

		if (dataType == "date")
		{
			var td = document.getElementById("calTD");
			td.className="formField";
			txtField.readOnly="readOnly";
		}
		else
		{
			var td = document.getElementById("calTD");
			td.className="hideTD";
			txtField.readOnly="";
		}	
	}	
}

</script>

<html:errors />

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	
	<html:form action="/SimpleSearch.do">
	
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
						<%String pageOf = (String)request.getAttribute(Constants.PAGEOF);%>
						<html:hidden property="pageOf" value="<%=pageOf%>"/>
					</td>
				</tr>
				<tr>
					<td class="formTitle" height="20" colspan="7">
						<%--bean:message key="simpleQuery.title" /--%>
						<%=title%>
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
				%>
				<tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formField">
					<%
						String attributeAction = "javascript:callAction('SimpleQueryInterface.do?pageOf="+pageOf+
												 "&aliasName="+aliasName+"')"; %>
						<html:select property="<%=objectName%>" styleClass="formFieldSized15" styleId="objectName" size="1" onchange="<%=attributeAction%>">
							<html:options collection="objectNameList" labelProperty="name" property="value" />
						</html:select>
					</td>
					<td class="formField">
						<html:select property="<%=attributeName%>" styleClass="formFieldSized15" styleId="attributeName" size="1" onchange="showDateColumn(this)">
							<logic:notPresent name="<%=attributeNameList%>">			
								<html:options name="attributeNameList" labelName="attributeNameList" />
							</logic:notPresent>	
							<logic:present name="<%=attributeNameList%>">				
								<html:options name="<%=attributeNameList%>" labelName="<%=attributeDisplayNameList%>" />
							</logic:present>	
						</html:select>
					</td>
					<td class="formField">
						<html:select property="<%=attributeCondition%>" styleClass="formFieldSized10" styleId="attributeCondition" size="1">
							<html:options name="attributeConditionList" labelName="attributeConditionList" />
						</html:select>
					</td>
					<td class="formField">
						<html:text styleClass="formFieldSized10" size="30" styleId="attributeValue" property="<%=attributeValue%>" />

					</td>
				<!--  ********************* MD Code ********************** -->	
					<td id="calTD" class="hideTD">
					<%	
						String fieldName = "simpleQueryInterfaceForm.attributeValue";
					%>
						<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
							<a href="javascript:show_calendar('<%=fieldName%>',null,null,'MM-DD-YYYY');">
								<img src="images\calendar.gif" width=24 height=22 border=0>
							</a>
					</td>
					
					<logic:equal name="<%=pageOf%>" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
					<td class="formField">
						<html:hidden property="<%=nextOperator%>"/>
					</td>
					<td class="formField" id="<%=i%>">
						<%String andOrLink = "javascript:insertRow('simpleQuery');setPropertyValue('"+nextOperator+"','"+Constants.AND_JOIN_CONDITION+"');changeAndOrLink('"+i+"','AND')"; %>
						<a href="#" onclick="<%=andOrLink%>">
							<bean:message key="simpleQuery.and" />
						</a>/
						<%andOrLink = "javascript:insertRow('simpleQuery');setPropertyValue('"+nextOperator+"','"+Constants.OR_JOIN_CONDITION+"');changeAndOrLink('"+i+"','OR')"; %>
						<a href="#" onclick="<%=andOrLink%>">
							<bean:message key="simpleQuery.or" />
						</a>
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
								<html:submit styleClass="actionButton" >
									<bean:message  key="buttons.search" />
								</html:submit>
							</td>
							
							<td>
								<html:submit styleClass="actionButton" >
									<bean:message  key="buttons.cancel" />
								</html:submit>
							</td>
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