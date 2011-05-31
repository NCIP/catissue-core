
<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	List columnList = (List) request.getAttribute(edu.wustl.common.util.global.Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) request.getAttribute(Constants.PAGINATION_DATA_LIST);
	String pageOf = org.apache.commons.lang.StringEscapeUtils.escapeHtml((String)request.getAttribute(Constants.PAGEOF));
	Integer identifierFieldIndex = (Integer)request.getAttribute(Constants.IDENTIFIER_FIELD_INDEX);
	if(identifierFieldIndex==null){
		identifierFieldIndex=0;
	}
	String title = pageOf + ".searchResultTitle";
	int pageNum = Integer.parseInt((String)request.getAttribute(Constants.PAGE_NUMBER));
	int totalResults = ((Integer)session.getAttribute(Constants.TOTAL_RESULTS)).intValue();
	int numResultsPerPage = Integer.parseInt((String)session.getAttribute(Constants.RESULTS_PER_PAGE));
	String pageName = "SpreadsheetView.do";
    String formName ="ProcessMatchedParticipants.do?pageOf=pageOfMatchedParticipant&identifierFieldIndex=0";
%>
<script>
function callAction(action)
{
	document.forms[0].action = action;
	document.forms[0].submit();
}

function refreshProcesedParticipant(){
	   // var pid=document.forms[0].selectedPID.value;

		document.forms[0].submit();
}
</script>
<form action="<%=formName%>">


<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="maintable">
	<tr>
		<td class="td_color_bfdcf3">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="td_table_head"><span class="wh_ar_b"> <bean:message
					key="<%=title%>" /> </span></td>
				<td><img
					src="images/uIEnhancementImages/table_title_corner2.gif"
					alt="Page Title - Search Results" width="31" height="24" hspace="0"
					vspace="0" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
	</tr>

</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="whitetable_bg">
	<tr>
		<td align="left" class="toptd"></td>
	</tr>


	<% if (request.getAttribute(Constants.PAGINATION_DATA_LIST) != null
							&& dataList.size() > 0)
{
%>
	<tr>
		<td><html:messages id="messageKey" message="true"
			header="messages.header" footer="messages.footer">
			<table border="0" cellpadding="3" cellspacing="3">
				<tr>
					<td><img src="images/uIEnhancementImages/error-green.gif"
						alt="successful messages" width="16" height="16"></td>
					<td class="messagetextsuccess"><%=messageKey%></td>
				</tr>
			</table>
		</html:messages></td>
	</tr>

	<tr>
		<td class="black_ar_new"><!--
					<custom:test name="Search Results" pageNum="<%=pageNum%>" totalResults="<%=totalResults%>" numResultsPerPage="<%=numResultsPerPage%>" pageName="<%=pageName%>" showPageSizeCombo="<%=true%>" recordPerPageList="<%=Constants.RESULT_PERPAGE_OPTIONS%>"/>
				--> <html:hidden property="<%=Constants.PAGEOF%>"
			value="<%=pageOf%>" /> <html:hidden property="isPaging" value="false" />
		<input type="hidden" name="selectedPID" value="" /> <input
			type="hidden" name="participantId" value="" /> <input type="hidden"
			name="isDelete" value="" /> <html:hidden
			property="identifierFieldIndex"
			value="<%=identifierFieldIndex.toString()%>" /></td>
	</tr>


	<script>
	/*
		to be used when you want to specify another javascript function for row selection.
		useDefaultRowClickHandler =1 | any value other than 1 indicates you want to use another row click handler.
		useFunction = "";  Function to be used.
	*/
	var useDefaultRowClickHandler =1;
	var gridFor="<%=Constants.GRID_FOR_EDIT_SEARCH%>";
	var useFunction = "";
</script>
	<tr>
		<td width="100%"><%@ include
			file="/pages/content/search/ProcessedMatchedGridPage.jsp"%>
		</td>
	</tr>

	<tr>
		<td>
		<table>
			<tr>
				<td><html:button styleClass="blue_ar_b"
					property="registratioPage" title="Submit Only" value="Delete"
					onclick="deleteProcesedParticipant()">
				</html:button></td>
				<td><html:button styleClass="blue_ar_b"
					property="registratioPage" title="Submit Only" value="Refresh"
					onclick="refreshProcesedParticipant()">
				</html:button></td>
			</tr>
		</table>
		</td>
	</tr>
	<%}else{%>
	<tr>
		<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">
		&nbsp; No processed participants found .... </span></td>
	</tr>
	<tr>
		<td>
		<table>
			<tr>
				<td><html:button styleClass="blue_ar_b"
					property="registratioPage" title="Submit Only" value="Refresh"
					onclick="refreshProcesedParticipant()">
				</html:button></td>
			</tr>
		</table>
		</td>
	</tr>
	<%}%>

</table>

</td>
</tr>
</table>
</form>
