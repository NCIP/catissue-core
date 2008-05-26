<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo"%>
<%@ page
	import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.common.actionForm.SimpleQueryInterfaceForm,java.util.List,edu.wustl.common.beans.NameValueBean"%>
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
<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"
	src="jss/scwcalendar.js"></script>
</head>
<%
			String[] attributeConditionArray = (String[]) request
			.getAttribute(Constants.ATTRIBUTE_CONDITION_LIST);
	String aliasName = (String) request
			.getAttribute(Constants.TABLE_ALIAS_NAME);
	String noOfRows = "1";
	String showCal = "";
	String dateClass = "hideTD";
	Object obj = request.getAttribute("simpleQueryInterfaceForm");
	String pageOf = (String) request.getAttribute(Constants.PAGEOF);

	String selectMenu = (String) request
			.getAttribute(Constants.MENU_SELECTED);
	String objectChanged = "";
	if (obj != null && obj instanceof SimpleQueryInterfaceForm) {
		SimpleQueryInterfaceForm form = (SimpleQueryInterfaceForm) obj;
		noOfRows = form.getCounter();
		objectChanged = request.getParameter("objectChanged");

		if (objectChanged != null && !objectChanged.equals("")) {
			form.setValue("SimpleConditionsNode:"
			+ Integer.parseInt(noOfRows)
			+ "_Condition_DataElement_field", null);
		}
	}

	String title = (String) request
			.getAttribute(Constants.SIMPLE_QUERY_INTERFACE_TITLE);
	String header;
	String alias = (String) request.getParameter("aliasName");
	if (pageOf.equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE)) {
		title = "simpleQuery.title";
		header = "SimpleQuery.header";
	} else {
		header = alias + ".header";
		title = alias + ".Search";
	}
%>
<script>



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
			//innerStr = innerStr + "<a href=\"javascript:show_calendar('"+fieldValue+"',null,null,'MM-DD-YYYY');\">";
			innerStr = innerStr + "<img src=\"images\\calendar.gif\" width=24 height=22 border=0 onclick='scwShow("+ fieldValue + ",event);'>";
			//innerStr = innerStr + "</a>";
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




</script>
<script language="JavaScript" type="text/javascript">

  window.onload = enableLastCheckbox;
</script>

<html:errors />

<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="newMaintable">
	<html:form action="<%=Constants.SIMPLE_SEARCH_ACTION%>">
		<html:hidden property="aliasName" value="<%=aliasName%>" />
		<html:hidden property="<%=Constants.MENU_SELECTED%>"
			value="<%=selectMenu%>" />
		<input type="hidden" name="objectChanged" id="objectChanged" value="">
		<html:hidden property="counter" value="<%=noOfRows%>" />
		<html:hidden property="pageOf" value="<%=pageOf%>" />
		<html:hidden property="andOrOperation" />
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="td_color_bfdcf3">
				<tr>
					<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="whitetable_bg">
						<tr>
							<td width="100%" colspan="2" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">

								<tr>
									<td colspan="3" valign="top" class="td_color_bfdcf3">
									<table width="22%" border="0" cellpadding="0" cellspacing="0"
										background="images/uIEnhancementImages/table_title_bg.gif">
										<tr>
											<td width="74%"><span class="wh_ar_b">&nbsp;&nbsp;&nbsp;
											<bean:message key="<%=header%>" /> </span></td>
											<td width="26%" align="right"><img
												src="images/uIEnhancementImages/table_title_corner2.gif"
												width="31" height="24" /></td>
										</tr>
									</table>
									</td>
								</tr>



								<logic:notEqual name="pageOf"
									value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
									<logic:notEqual name="pageOf"
										value="<%=Constants.PAGEOF_NEW_SPECIMEN%>">
										<tr>
											<td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;</td>
											<td width="9%" valign="top" class="td_tab_bg">&nbsp;</td>
											<td width="90%" valign="bottom" class="td_color_bfdcf3"
												style="padding-top:4px;">
											<table width="100%" border="0" cellpadding="0"
												cellspacing="0">


												<tr>
													<td width="4%" class="td_tab_bg">&nbsp;</td>
													<td width="6%" valign="bottom"
														background="images/uIEnhancementImages/tab_bg.gif"><html:link
														href="#" onclick="callSerachAction('CommonTab.do')">
														<img src="images/uIEnhancementImages/tab_add_user1.jpg"
															alt="Add" width="57" height="22" border="0" />
													</html:link></td>
													<td width="6%" valign="bottom"
														background="images/uIEnhancementImages/tab_bg.gif"><img
														src="images/uIEnhancementImages/tab_edit_user1.jpg"
														alt="Edit" width="59" height="22" border="0" /></td>
													<logic:equal name="pageOf"
														value="<%=Constants.PAGEOF_USER_ADMIN%>">
														<td width="15%" valign="bottom"
															background="images/uIEnhancementImages/tab_bg.gif"><html:link
															page="/ApproveUserShow.do?pageNum=1&menuSelected=1">
															<img
																src="images/uIEnhancementImages/tab_approve_user.jpg"
																alt="Approve New Users" width="139" height="22"
																border="0" />
														</html:link></td>
													</logic:equal>
													<logic:notEqual name="pageOf"
														value="<%=Constants.PAGEOF_USER_ADMIN%>">
														<td width="15%" valign="bottom"
															background="images/uIEnhancementImages/tab_bg.gif">&nbsp;</td>
													</logic:notEqual>

													<td width="65%" valign="bottom" class="td_tab_bg">&nbsp;</td>
													<td width="1%" align="left" valign="bottom"
														class="td_color_bfdcf3">&nbsp;</td>
												</tr>

											</table>
											</td>
										</tr>
									</logic:notEqual>
								</logic:notEqual>
							</table>
							</td>
						</tr>
						<logic:equal name="pageOf"
							value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
							<tr>
								<td class="td_color_bfdcf3">&nbsp;</td>
							</tr>
						</logic:equal>
						<logic:equal name="pageOf"
							value="<%=Constants.PAGEOF_NEW_SPECIMEN%>">
							<tr>
								<td class="td_color_bfdcf3">&nbsp;</td>
							</tr>
						</logic:equal>
						<tr>
							<td class="td_color_bfdcf3"
								style="padding-left:10px; padding-right:10px; padding-bottom:10px;">
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								bgcolor="#FFFFFF">

								<!-- SIMPLE QUERY INTERFACE BEGINS-->
								<tr>
									<td width="1%" align="left">&nbsp;</td>
									<td colspan="3" align="left">&nbsp;</td>
								</tr>
								<tr class="tr_bg_blue1">
									<!--<td align="left" class="tr_bg_blue1">&nbsp;</td>-->
									<td height="25" colspan="7" align="left" class="tr_bg_blue1"><span
										class="blue_ar_b"> <bean:message key="<%=title%>" /> </span></td>
									<logic:equal name="pageOf"
										value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
										<%
													String addAction = "setPropertyValue('andOrOperation','true');"
													+ "incrementCounter();callSerachAction('SimpleQueryInterfaceValidate.do?pageOf="
													+ pageOf + "');";
										%>
										<td class="tr_bg_blue1" align="Right"><html:button
											property="addKeyValue" styleClass="blue_ar_b"
											onclick="<%=addAction%>">
											<bean:message key="buttons.addMore" />
										</html:button></td>
									</logic:equal>
								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr bgcolor="#f8fcff">
									<logic:equal name="pageOf"
										value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
										<td class="black_arNew" width="5"><bean:message
											key="query.queryNumber" /></td>
										<td class="black_arNew"><label for="delete"
											align="center"> <bean:message key="addMore.delete" />
										</label></td>
										<td class="black_arNew"><label for="object"> <bean:message
											key="query.object" /> </label></td>

									</logic:equal>
									<td class="black_arNew" colspan="1"><label
										for="attributes"> <bean:message key="query.attributes" />
									</label></td>

									<td class="black_arNew"><label for="conditions"> <bean:message
										key="query.conditions" /> </label></td>
									<td class="black_arNew" colspan="2"><label for="value">
									<bean:message key="query.value" /> </label></td>
									<logic:equal name="pageOf"
										value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
										<td class="black_arNew"><label for="attributes">
										<bean:message key="query.operator" /> </label></td>
									</logic:equal>
								<tr>
									<td>&nbsp;</td>
								</tr>
								</tr>

								<tbody id="simpleQuery">
									<%
											for (int i = 1; i <= Integer.parseInt(noOfRows); i++) {
											String objectName = "value(SimpleConditionsNode:" + i
											+ "_Condition_DataElement_table)";
											String attributeName = "value(SimpleConditionsNode:" + i
											+ "_Condition_DataElement_field)";

											String attributeCondition = "value(SimpleConditionsNode:" + i
											+ "_Condition_Operator_operator)";
											String attributeValue = "value(SimpleConditionsNode:" + i
											+ "_Condition_value)";
											String attributeValueID = "SimpleConditionsNode" + i
											+ "_Condition_value_ID";
											String nextOperator = "value(SimpleConditionsNode:" + i
											+ "_Operator_operator)";
											String attributeNameList = "attributeNameList" + i;
											String attributeDisplayNameList = "attributeDisplayNameList"
											+ i;
											String objectNameList = "objectList" + i;
											String objectDisplayNameList = "objectDisplayNameList" + i;
											SimpleQueryInterfaceForm form = (SimpleQueryInterfaceForm) obj;
											String nextOperatorValue = (String) form
											.getValue("SimpleConditionsNode:" + i
													+ "_Operator_operator");
											String columnID = "calTD" + i;
											String showCalendarKey = "SimpleConditionsNode:" + i
											+ "_showCalendar";
											String showCalendarValue = "showCalendar(SimpleConditionsNode:"
											+ i + "_showCalendar)";
											String fieldName = "simpleQueryInterfaceForm."
											+ attributeValueID;
											String overDiv = "overDiv";
											String check = "chk_" + i;
											if (i > 1) {
												overDiv = overDiv + "" + i;
											}
											String functionName = "showDateColumn(this,'"
											+ attributeValueID + "','" + columnID + "','"
											+ showCalendarValue + "','" + fieldName + "','"
											+ overDiv + "')";
											String attributeId = "attribute" + i;
											String operatorId = "operator" + i;
											String onAttributeChange = "onAttributeChange(this,'"
											+ operatorId + "','" + attributeValueID + "'); "
											+ functionName;
											String operatorFunction = "showDatafield(this,'"
											+ attributeValueID + "')";
											String attributeConditionKey = "SimpleConditionsNode:" + i
											+ "_Condition_Operator_operator";
									%>
									<tr>
										<logic:equal name="pageOf"
											value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
											<td class="blue_ar_b1" width="5"><%=i%>.</td>
											<td class="blue_ar_b1"><input type=checkbox
												name="<%=check %>" id="<%=check %>" disabled="true"
												onClick="enablePreviousCheckBox(this);enableButton(document.forms[0].deleteValue,document.forms[0].counter,'chk_')">
											</td>

										</logic:equal>
										<%
												String attributeAction = "javascript:onObjectChange(this,'SimpleQueryInterface.do?pageOf="
												+ pageOf;
												if (aliasName != null)
													attributeAction = attributeAction + "&aliasName="
													+ aliasName + "')";
												else
													attributeAction = attributeAction + "')";
										%>

										<logic:equal name="pageOf"
											value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
											<td class="black_new"><html:select
												property="<%=objectName%>" styleClass="formFieldSized18"
												styleId="<%=objectName%>" size="1"
												onchange="<%=attributeAction%>"
												onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
												<logic:notPresent name="<%=objectNameList%>">
													<html:options collection="objectNameList"
														labelProperty="name" property="value" />
												</logic:notPresent>
												<logic:present name="<%=objectNameList%>">
													<html:options collection="<%=objectNameList%>"
														labelProperty="name" property="value" />
												</logic:present>
											</html:select></td>
										</logic:equal>
										<logic:notEqual name="pageOf"
											value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
											<html:hidden property="<%=objectName%>"
												value="<%=aliasName%>" />
										</logic:notEqual>
										<td class="black_new" width="30%"><html:select
											property="<%=attributeName%>" styleClass="formFieldSized18"
											styleId="<%=attributeId%>" size="1"
											onchange="<%=onAttributeChange%>"
											onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
											<logic:notPresent name="<%=attributeNameList%>">
												<html:options name="attributeNameList"
													labelName="attributeNameList" />
											</logic:notPresent>
											<logic:present name="<%=attributeNameList%>">
												<html:options collection="<%=attributeNameList%>"
													labelProperty="name" property="value" />
											</logic:present>
										</html:select></td>

										<td class="black_new"><!-- Mandar : 434 : for tooltip -->
										<html:select property="<%=attributeCondition%>"
											styleClass="formFieldSized8" styleId="<%=operatorId%>"
											size="1" onchange="<%=operatorFunction%>"
											onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
											<%
													String attributeNameKey = "SimpleConditionsNode:" + i
													+ "_Condition_DataElement_field";
													String attributeNameValue = (String) form
													.getValue(attributeNameKey);
													String attributeType = null;
													List columnNameValueBeanList = (List) request
													.getAttribute(attributeNameList);
													System.out.println("---------" + objectChanged);
													if (columnNameValueBeanList != null
													&& !columnNameValueBeanList.isEmpty()
													&& i == Integer.parseInt(noOfRows)
													&& attributeNameValue == null
													&& (objectChanged == null || !objectChanged.equals(""))) {
														NameValueBean nameValueBean = (NameValueBean) columnNameValueBeanList
														.get(0);
														attributeNameValue = nameValueBean.getValue();

													}
													if (attributeNameValue != null) {
														StringTokenizer tokenizer = new StringTokenizer(
														attributeNameValue, ".");
														int tokenCount = 1;
														while (tokenizer.hasMoreTokens()) {
													attributeType = tokenizer.nextToken();
													if (tokenCount == 3)
														break;
													tokenCount++;
														}
														System.out.println("attributeType-----------"
														+ attributeType);
														if (attributeType.equals("varchar")
														|| attributeType.equals("text")) {
											%>
											<html:option value="<%=Operator.STARTS_WITH%>">
												<%=Operator.STARTS_WITH%>
											</html:option>
											<html:option value="<%=Operator.ENDS_WITH%>">
												<%=Operator.ENDS_WITH%>
											</html:option>
											<html:option value="<%=Operator.CONTAINS%>">
												<%=Operator.CONTAINS%>
											</html:option>
											<html:option value="<%=Operator.EQUAL%>">Equals</html:option>
											<html:option value="<%=Operator.NOT_EQUALS%>">Not Equals</html:option>
											<%
														} else if (attributeType
														.equals(Constants.FIELD_TYPE_TINY_INT)) {
											%>
											<html:option value="<%=Operator.EQUAL%>">Equals</html:option>
											<html:option value="<%=Operator.NOT_EQUALS%>">Not Equals</html:option>
											<%
														} else if (attributeType
														.equals(Constants.FIELD_TYPE_BIGINT)
														|| attributeType.equals("double")) {
											%>
											<html:option value="<%=Operator.EQUAL%>">Equals</html:option>
											<html:option value="<%=Operator.NOT_EQUALS%>">Not Equals</html:option>
											<html:option value="<%=Operator.LESS_THAN%>">
												<%=Operator.LESS_THAN%>
											</html:option>
											<html:option value="<%=Operator.LESS_THAN_OR_EQUALS%>">
												<%=Operator.LESS_THAN_OR_EQUALS%>
											</html:option>
											<html:option value="<%=Operator.GREATER_THAN%>">
												<%=Operator.GREATER_THAN%>
											</html:option>
											<html:option value="<%=Operator.GREATER_THAN_OR_EQUALS%>">
												<%=Operator.GREATER_THAN_OR_EQUALS%>
											</html:option>
											<%
														} else if (attributeType.equals(Constants.FIELD_TYPE_DATE)
														|| attributeType
														.equals(Constants.FIELD_TYPE_TIMESTAMP_DATE)) {
											%>
											<html:option value="<%=Operator.EQUAL%>">Equals</html:option>
											<html:option value="<%=Operator.NOT_EQUALS%>">Not Equals</html:option>
											<html:option value="<%=Operator.LESS_THAN%>">
												<%=Operator.LESS_THAN%>
											</html:option>
											<html:option value="<%=Operator.LESS_THAN_OR_EQUALS%>">
												<%=Operator.LESS_THAN_OR_EQUALS%>
											</html:option>
											<html:option value="<%=Operator.GREATER_THAN%>">
												<%=Operator.GREATER_THAN%>
											</html:option>
											<html:option value="<%=Operator.GREATER_THAN_OR_EQUALS%>">
												<%=Operator.GREATER_THAN_OR_EQUALS%>
											</html:option>
											<%
											} else {
											%>
											<html:option value="<%=Operator.STARTS_WITH%>">
												<%=Operator.STARTS_WITH%>
											</html:option>
											<html:option value="<%=Operator.ENDS_WITH%>">
												<%=Operator.ENDS_WITH%>
											</html:option>
											<html:option value="<%=Operator.CONTAINS%>">
												<%=Operator.CONTAINS%>
											</html:option>
											<html:option value="<%=Operator.EQUAL%>">Equals</html:option>
											<html:option value="<%=Operator.NOT_EQUALS%>">Not Equals</html:option>
											<%
													}
													} else {
											%>
											<html:option value="<%=Operator.STARTS_WITH%>">
												<%=Operator.STARTS_WITH%>
											</html:option>
											<html:option value="<%=Operator.ENDS_WITH%>">
												<%=Operator.ENDS_WITH%>
											</html:option>
											<html:option value="<%=Operator.CONTAINS%>">
												<%=Operator.CONTAINS%>
											</html:option>
											<html:option value="<%=Operator.EQUAL%>">Equals</html:option>
											<html:option value="<%=Operator.NOT_EQUALS%>">Not Equals</html:option>
											<%
											}
											%>
											<html:option value="<%=Operator.IS_NULL%>">
												<%=Operator.IS_NULL%>
											</html:option>
											<html:option value="<%=Operator.IS_NOT_NULL%>">
												<%=Operator.IS_NOT_NULL%>
											</html:option>
										</html:select></td>
										<td class="blue_ar_b" id="<%=columnID%>" size=3><!--  ********************* Mandar Code ********************** -->
										<!-- ***** Code added to check multiple rows for Calendar icon ***** -->
										<html:hidden property="<%=showCalendarValue%>"
											styleId="<%=showCalendarValue%>" /> <%
 		showCal = (String) form.getShowCalendar(showCalendarKey);
 		if (showCal != null && showCal.trim().length() > 0) {
 %>
										<div id="<%=overDiv%>"
											style="position:absolute; visibility:hidden; z-index:1000;"></div>
										<a
											href="javascript:show_calendar('<%=fieldName%>',null,null,'MM-DD-YYYY');">
										<img src="images\calendar.gif" width=24 height=22 border=0>
										</a> <%
 } else {
 %> &nbsp; <%
 }
 %>
										</td>
										<td align="left">
										<%
												String currentOperatorValue = (String) form
												.getValue(attributeConditionKey);
												if ((currentOperatorValue != null)
												&& (currentOperatorValue.equals(Operator.IS_NOT_NULL) || currentOperatorValue
														.equals(Operator.IS_NULL))) {
										%> <html:text styleClass="black_ar" size="15"
											styleId="<%=attributeValueID%>"
											property="<%=attributeValue%>" disabled="true" /> <%
 } else {
 %> <html:text styleClass="black_ar" size="15"
											styleId="<%=attributeValueID%>"
											property="<%=attributeValue%>" /> <%
 }
 %>
										</td>
										<!--html:hidden property="<%=nextOperator%>"/-->
										<logic:equal name="pageOf"
											value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
											<td class="black_new"><html:select
												property="<%=nextOperator%>" styleClass="formFieldSized8">
												<html:option value="And">
													<bean:message key="simpleQuery.and" />
												</html:option>
												<html:option value="Or">
													<bean:message key="simpleQuery.or" />
												</html:option>
											</html:select></td>
										</logic:equal>
									</tr>
									<tr>
										<td>&nbsp;</td>
									</tr>
									<%
									}
									%>
								</tbody>

								<tr class="td_color_F7F7F7">

									<td align="right" colspan="10" class="buttonbg">
									<%
												String searchAction = "callSerachAction('"
												+ Constants.SIMPLE_SEARCH_ACTION + "')";
									%> <html:button styleClass="blue_ar_b" property="searchButton"
										onclick="<%=searchAction%>">
										<bean:message key="buttons.search" />
									</html:button> <%
 		if (pageOf.equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE)) {
 		String configAction = "callSerachAction('"
 		+ Constants.CONFIGURE_SIMPLE_QUERY_VALIDATE_ACTION
 		+ "?pageOf=pageOfSimpleQueryInterface')";
 %> &nbsp;&nbsp;|&nbsp;<span> <html:button styleClass="blue_ar_b"
										property="configureButton" onclick="<%=configAction%>">
										<bean:message key="buttons.configure" />
									</html:button></span> &nbsp;&nbsp;|&nbsp;<span> <%
 		String deleteAction = "decrementCounter();setPropertyValue('value(SimpleConditionsNode:"
 		+ (Integer.parseInt(noOfRows) - 1)
 		+ "_Operator_operator)','');"
 		+ "callSerachAction('SimpleQueryInterface.do?pageOf="
 		+ pageOf + "');";
 %> <html:button property="deleteValue" styleClass="blue_ar_b"
										onclick="deleteChecked('simpleQuery','SimpleQueryInterface.do?pageOf=<%=pageOf%>',document.forms[0].counter,'chk_',false,document.forms[0].deleteValue)"
										disabled="true">
										<bean:message key="buttons.delete" />
									</html:button></span> <%
 }
 %> &nbsp;&nbsp;|&nbsp;<span class="cancellink"><html:link
										page="/ManageAdministrativeData.do">
										<bean:message key="buttons.cancel" />
									</html:link></span>&nbsp;&nbsp;</td>
								</tr>

								<!-- action buttons end -->


							</table>
							</td>
						</tr>

						<!-- SIMPLE QUERY INTERFACE ENDS-->

					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</html:form>
</table>
