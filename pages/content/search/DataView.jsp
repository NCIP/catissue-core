<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>

<%
	List columnList = (List) request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) session.getAttribute(Constants.PAGINATION_DATA_LIST);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	Integer identifierFieldIndex = (Integer)request.getAttribute(Constants.IDENTIFIER_FIELD_INDEX);
	String title = pageOf + ".searchResultTitle";
	int pageNum = Integer.parseInt((String)request.getAttribute(Constants.PAGE_NUMBER));
	int totalResults = Integer.parseInt((String)session.getAttribute(Constants.TOTAL_RESULTS));
	int numResultsPerPage = Integer.parseInt((String)session.getAttribute(Constants.RESULTS_PER_PAGE));
	String pageName = "SpreadsheetView.do";		
%>


<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="4%">
	<tr>
		 <td class="formTitle">
			<bean:message key="<%=title%>"/>
		 </td>
	</tr>	
</table>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="6%">
	<tr>
		<td class="dataPagingSection">
			<html:form action="/SpreadsheetExport">				
				<custom:test name="Search Results" pageNum="<%=pageNum%>" totalResults="<%=totalResults%>" numResultsPerPage="<%=numResultsPerPage%>" pageName="<%=pageName%>" showPageSizeCombo="<%=true%>" recordPerPageList="<%=Constants.RESULT_PERPAGE_OPTIONS%>"/>
				<html:hidden property="<%=Constants.PAGEOF%>" value="<%=pageOf%>"/>
				<html:hidden property="isPaging" value="true"/>
				<html:hidden property="identifierFieldIndex" value="<%=identifierFieldIndex.toString()%>"/>
			</html:form>
		</td>
	</tr>
</table>
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
<%@ include file="/pages/content/search/GridPage.jsp" %>
