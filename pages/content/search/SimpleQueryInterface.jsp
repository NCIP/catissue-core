<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm,java.util.List,edu.wustl.common.beans.NameValueBean"%>

<%
	List objectNameArray = (List)request.getAttribute(Constants.OBJECT_NAME_LIST);
	String[] attributeConditionArray = (String[])request.getAttribute(Constants.ATTRIBUTE_CONDITION_LIST);
	Object obj = request.getAttribute("simpleQueryInterfaceForm");
	String noOfRows="1";
	if(obj != null && obj instanceof SimpleQueryInterfaceForm)
	{
		SimpleQueryInterfaceForm form = (SimpleQueryInterfaceForm)obj;
		noOfRows = form.getCounter();
	}
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
	sname="";
	
	sname="<a href='#' onclick=javascript:insertRow('simpleQuery')><bean:message key='simpleQuery.and' /></a>"
	sname=sname+" / <a href='#' onclick=javascript:insertRow('simpleQuery')><bean:message key='simpleQuery.or' /></a>"        	
	spreqqty.innerHTML="" + sname;
}

function callAction(action)
{
	document.forms[0].action = "/catissuecore/"+action;
	document.forms[0].submit();
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
						<html:hidden property="counter" value="<%=noOfRows%>"/>
					</td>
				</tr>
				<tr>
					<td class="formTitle" height="20" colspan="7">
						<bean:message key="simpleQuery.title" />
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
						String attributeNameList = "SimpleConditionsNode"+i;
				%>
				<tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formField">
						<html:select property="<%=objectName%>" styleClass="formFieldSized10" styleId="objectName" size="1" onchange="javascript:callAction('SimpleQueryInterface.do')">
							<html:options collection="objectNameList" labelProperty="name" property="value" />
						</html:select>
					</td>
					<td class="formField">
						<html:select property="<%=attributeName%>" styleClass="formFieldSized10" styleId="attributeName" size="1">
							<logic:notPresent name="<%=attributeNameList%>">			
								<html:options name="attributeNameList" labelName="attributeNameList" />
							</logic:notPresent>	
							<logic:present name="<%=attributeNameList%>">				
								<html:options collection="<%=attributeNameList%>" labelProperty="name" property="value" />
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
					<td class="formSmallField">
						<html:hidden property="<%=nextOperator%>" value="AND"/>
					</td>
					<td class="formField">
						<a href="#" onclick="javascript:insertRow('simpleQuery');">
							<bean:message key="simpleQuery.and" />
						</a>/
						<a href="#" onclick="javascript:insertRow('simpleQuery');">
							<bean:message key="simpleQuery.or" />
						</a>
					</td>
				</tr>
				<%}%>
				</tbody>	
				<tr>
					<td align="right" colspan="7">
					<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
							<td>
								<html:submit styleClass="actionButton">
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