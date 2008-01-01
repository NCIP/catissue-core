<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>


<head>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript">
		function onFilterChange(element)
		{
			var selectedFilter = document.getElementById("selectedFilter");
			var action = "ConflictView.do?pageNum=1";
			document.forms[0].action = action;
			document.forms[0].submit();
		}

</script>
</head>

<html:errors />

<%
	int selectedFilter =  Integer.parseInt((String)request.getSession().getAttribute(Constants.SELECTED_FILTER));
	
	int pageNum = Integer.parseInt((String)request.getAttribute(Constants.PAGE_NUMBER));
  	int totalResults = Integer.parseInt((String)session.getAttribute(Constants.TOTAL_RESULTS));
  	int numResultsPerPage = Integer.parseInt((String)session.getAttribute(Constants.RESULTS_PER_PAGE));

	List columnList = (List) request.getSession().getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
	List dataList = (List) request.getAttribute(Constants.PAGINATION_DATA_LIST);
		
	String pageOf = "pageOfConflictResolver";
	String pageName = "SpreadsheetView.do";		
	
	
	Integer identifierFieldIndex = new Integer(1);
	Integer spnFieldIndex =  new Integer(2);
	Integer reportDateFieldIndex =  new Integer(3);
	Integer statusFieldIndex =  new Integer(4);
	Integer siteNameFieldIndex =  new Integer(5);
	Integer reportColDateFieldIndex =  new Integer(6);
	

%>

<html:form action="ConflictView.do">

<table summary="" cellpadding="3" cellspacing="0" border="0" style="table-layout:fixed" width="100%" styleClass="formFieldSized">
		<tr valign='top' align="left"  >
				
			<td class="formFieldNoBordersBold">
				<bean:message key="caTies.conflict.filter.conflicts"/> :
				<html:select property="selectedFilter" name="conflictViewForm" styleClass="formFieldSized" styleId="selectedFilter" size="1" onchange="onFilterChange(this)">
				<html:options collection="<%=Constants.FILTER_LIST%>" labelProperty="name" property="value" />
				</html:select>
				
			</td>
		</tr>
		<tr height = "10">
			&nbsp;
		</tr>

</table>					

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="4%">
					
	<tr>
		 <td class="formTitle">
			<bean:message key="caTies.conflict.resolve.title"/>
		 </td>
	</tr>	
	<!-- paging begins -->
	<tr>
		<td colspan = "8" class="dataPagingSection">
			<custom:test name="New User Search Results" pageNum="<%=pageNum%>" totalResults="<%=totalResults%>" numResultsPerPage="<%=numResultsPerPage%>" pageName="<%=pageName%>" showPageSizeCombo="<%=true%>" recordPerPageList="<%=Constants.RESULT_PERPAGE_OPTIONS%>"/>
			<html:hidden property="<%=Constants.PAGEOF%>" value="<%=pageOf%>"/>
			<html:hidden property="isPaging" value="true"/>
		
		</td>
	</tr>
	<!-- paging ends -->				
</table>

</html:form>

<script>
	/* 
		to be used when you want to specify another javascript function for row selection.
		useDefaultRowClickHandler =1 | any value other than 1 indicates you want to use another row click handler.
		useFunction = "";  Function to be used. 	
	*/
	var useDefaultRowClickHandler =2;
	var gridFor="<%=Constants.GRID_FOR_EDIT_SEARCH%>";
	var useFunction = "goToConflictDetails";	
	
	function goToConflictDetails(id)
	{
		var colid = <%=identifierFieldIndex.intValue()%>;
		var statId = <%=statusFieldIndex.intValue()%>;
		var surgicalPathologyNumberId = <%=spnFieldIndex.intValue()%>
		var reportDateId = <%=reportDateFieldIndex.intValue()%>
		var siteNameId = <%=siteNameFieldIndex.intValue()%>
		var reportCollDateId = <%=reportColDateFieldIndex%>
		
		var surgicalPathologyNumberField =mygrid.cells(id,surgicalPathologyNumberId); 
		var reportDateField =mygrid.cells(id,reportDateId); 
		var siteNameField =mygrid.cells(id,siteNameId); 
		var reportIdField = mygrid.cells(id,colid);
		var statusField = mygrid.cells(id,statId);
		var reportCollDateField = mygrid.cells(id,reportCollDateId);

		var reportId = reportIdField.getValue();
		var conflictStatus = statusField.getValue();
		var surgicalPathologyNumber =surgicalPathologyNumberField.getValue();
		var reportDate = reportDateField.getValue();
		var siteName = siteNameField.getValue();
		var reportCollDate = reportCollDateField.getValue();
		var url = "ConflictDetails.do?reportQueueId="+reportId +"&conflictStatus="+conflictStatus +"&surgicalPathologyNumber="+ surgicalPathologyNumber +"&reportDate="+reportDate +"&siteName="+siteName +"&reportCollectionDate="+reportCollDate;
		window.location.href = url;
		
		
	}
</script>
<%if(dataList.size() > 0)
	{ %>
		<%@ include file="/pages/content/search/GridPage.jsp" %>
  <%}
    else
    { %>
    
    	<table>
	    	<tr height="10%">
				<td><b>There are no conflicting enteries.</b></td>
			</tr>
		</table>
  <%}%>
