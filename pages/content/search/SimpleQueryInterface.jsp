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

function vieMapTabSelected(){

 
 var action= "OpenStorageContainer.do?operation=showEditAPageAndMap&pageOf=pageOfStorageContainer"; 
	document.forms[0].action=action;
	document.forms[0].submit();
}


</script>
<script language="JavaScript" type="text/javascript">

  window.onload = enableLastCheckbox;
</script>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
<html:form action="<%=Constants.SIMPLE_SEARCH_ACTION%>">
		<html:hidden property="aliasName" value="<%=aliasName%>" />
		<html:hidden property="<%=Constants.MENU_SELECTED%>"
			value="<%=selectMenu%>" />
		<input type="hidden" name="objectChanged" id="objectChanged" value="">
		<html:hidden property="counter" value="<%=noOfRows%>" />
		<html:hidden property="pageOf" value="<%=pageOf%>" />
		<html:hidden property="andOrOperation" />
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="<%=header%>" /></span></td>
        <td align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Search Page Title" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
	<logic:notEqual name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
	<tr>
	<td class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
	<!----Add tab hidden for the Specimen Search----->
		<logic:notEqual name="pageOf" value="<%=Constants.PAGEOF_NEW_SPECIMEN%>">
        <td valign="bottom"><html:link href="#" onclick="callSerachAction('CommonTab.do')">
		<img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" /></html:link></td>
		</logic:notEqual>
       
		<td valign="bottom"><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22" border="0" /></td>
		<logic:equal name="pageOf" value="<%=Constants.PAGEOF_USER_ADMIN%>">
        <td valign="bottom"><html:link page="/ApproveUserShow.do?pageNum=1"><img src="images/uIEnhancementImages/tab_approve_user.jpg" alt="Approve New Users" width="139" height="22" border="0" /></html:link></td>
		</logic:equal>
		<logic:equal name="pageOf" value="pageOfStorageContainer">
		<td  valign="bottom"><a href="#"><img src="images/uIEnhancementImages/view_map2.gif" alt="View Map"width="76" height="22" border="0" onclick="vieMapTabSelected()"/></a></td>
		</logic:equal>
<!-- These tabs are visible in case of specimen page--->
		 <logic:equal name="pageOf" value="<%=Constants.PAGEOF_NEW_SPECIMEN%>">
			<td valign="bottom"><html:link page="/CreateSpecimen.do?operation=add&amp;pageOf=&virtualLocated=true">	<img src="images/uIEnhancementImages/tab_derive2.gif" alt="Derive" width="56" height="22" border="0"/>	</html:link></td>
			<td valign="bottom"><html:link page="/Aliquots.do?pageOf=pageOfAliquot"><img src="images/uIEnhancementImages/tab_aliquot2.gif" alt="Aliquot" width="66" height="22" border="0" >		</html:link></td>
			<td valign="bottom"><html:link page="/QuickEvents.do?operation=add"><img src="images/uIEnhancementImages/tab_events2.gif" alt="Events" width="56" height="22" border="0" />		</html:link></td>
			<td valign="bottom"><html:link page="/MultipleSpecimenFlexInitAction.do?pageOf=pageOfMultipleSpWithMenu"><img src="images/uIEnhancementImages/tab_multiple2.gif" alt="Multiple" width="66" height="22" border="0" />	</html:link></td>
		</logic:equal>
		<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>

	</tr>
	</logic:notEqual>
	<logic:equal name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
      <tr>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
	  </logic:equal>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
      
      <tr>
        <td colspan="2" align="left" class="toptd"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
      </tr>
      <tr>
        <td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;<bean:message key="<%=title%>" /></span></td>

      </tr>
	 <%
 		if (pageOf.equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE)) {
 		String configAction = "callSerachAction('"
 		+ Constants.CONFIGURE_SIMPLE_QUERY_VALIDATE_ACTION
 		+ "?pageOf=pageOfSimpleQueryInterface')";
 %>
      <tr>
        <td align="right"><table width="100%" border="0" cellspacing="0" cellpadding="2">
            <tr>
              <td width="94%" align="right" nowrap><img src="images/uIEnhancementImages/viewall_icon.gif" alt="View All" /></td>
              <td width="6%" align="right" nowrap="nowrap"><span class="link"><html:link href="#" onclick="<%=configAction%>" styleClass="view">Define View</html:link></span></td>
            </tr>
        </table></td>
        <td width="1%" align="right"></td>
      </tr>
	  <%
	  }
 %>
      <tr>
        <td colspan="2" align="center" class="showhide"><table width="99%" border="0" cellspacing="0" cellpadding="3">
            <tr class="tableheading">
<logic:equal name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
              <td width="6%" align="left" class="black_ar_b">Select</td>
              <td width="26%" align="left" class="black_ar_b"><label for="object"> <bean:message
											key="query.object" /> </label> </td>
	</logic:equal>
              <td width="26%" class="black_ar_b"><label for="attributes"> <bean:message key="query.attributes" />
									</label></td>
              <td width="12%" class="black_ar_b"><label for="conditions"> <bean:message
										key="query.conditions" /> </label></td>
			  <td width="4%">&nbsp;</td>
              <td width="15%" class="black_ar_b" ><label for="value">
									<bean:message key="query.value" /> </label></td>
	<logic:equal name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
              <td width="11%" class="black_ar_b"><label for="attributes">
										<bean:message key="query.operator" /> </label></td>
	</logic:equal>
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
			<logic:equal name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
              <td align="left" class="black_ar" ><input type=checkbox
												name="<%=check %>" id="<%=check %>" disabled="true"
												onClick="enablePreviousCheckBox(this);enableButton(document.forms[0].deleteValue,document.forms[0].counter,'chk_')"></td>
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
			<logic:equal name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
              <td nowrap class="black_ar"><html:select
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
											</html:select>
				</td>
				</logic:equal>
				<logic:notEqual name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
									<html:hidden property="<%=objectName%>"
												value="<%=aliasName%>" />
				</logic:notEqual>
              <td nowrap class="black_new" ><html:select
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
										</html:select>
						</td>
              <td nowrap class="black_new">
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
											}
											else if (attributeType.equals("integer"))
											{
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
										</html:select>
			  </td>
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
              <td nowrap class="black_ar">
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
			  <logic:equal name="pageOf" value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
              <td nowrap class="black_ar"><html:select
												property="<%=nextOperator%>" styleClass="formFieldSized8">
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
			<%
									}
			%>
			</tbody>
			<logic:equal name="pageOf"
										value="<%=Constants.PAGEOF_SIMPLE_QUERY_INTERFACE%>">
										<%
													String addAction = "setPropertyValue('andOrOperation','true');"
													+ "incrementCounter();callSerachAction('SimpleQueryInterfaceValidate.do?pageOf="
													+ pageOf + "');";
										%>
										
									
            <tr>
              <td colspan="7" align="left" class="black_ar" ><table width="100%" border="0" cellspacing="0" cellpadding="1">
                  <tr>
                    <td width="9%"><html:button
											property="addKeyValue" styleClass="black_ar"
											onclick="<%=addAction%>">
											<bean:message key="buttons.addMore" />
										</html:button></td>
										<%
 		String deleteAction = "decrementCounter();setPropertyValue('value(SimpleConditionsNode:"
 		+ (Integer.parseInt(noOfRows) - 1)
 		+ "_Operator_operator)','');"
 		+ "callSerachAction('SimpleQueryInterface.do?pageOf="
 		+ pageOf + "');";
 %> 
                    <td align="left"><html:button property="deleteValue" styleClass="black_ar"
										onclick="deleteChecked('simpleQuery','SimpleQueryInterface.do?pageOf=<%=pageOf%>',document.forms[0].counter,'chk_',false,document.forms[0].deleteValue)"
										disabled="true">
										<bean:message key="buttons.delete" />
									</html:button></td>
                  </tr>
              </table></td>
            </tr>
		</logic:equal>
        </table></td>
      </tr>
      <tr>
        <td colspan="2" class="buttonbg"><%
												String searchAction = "callSerachAction('"
												+ Constants.SIMPLE_SEARCH_ACTION + "')";
									%> <html:button styleClass="blue_ar_b" property="searchButton"
										onclick="<%=searchAction%>">
										<bean:message key="buttons.search" />
									</html:button>
          &nbsp;&nbsp;|&nbsp; <html:link page="/ManageAdministrativeData.do" styleClass="cancellink">
										<bean:message key="buttons.cancel" />
									</html:link></td>
      </tr>
    </table></td>
  </tr>
 </html:form>
</table>