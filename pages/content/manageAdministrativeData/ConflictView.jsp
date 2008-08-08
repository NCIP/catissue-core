<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<head>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
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

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td nowrap="nowrap" class="td_table_head"><span class="wh_ar_b"><bean:message key="app.reportedConflicts"/></span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
      
      <tr>
        <td colspan="2" align="left" class="toptd"></td>
      </tr>
      <tr>
        <td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;<bean:message key="caTies.conflict.resolve.title"/></span></td>
      </tr>
      <tr>
        <td align="right"><label><span class="black_ar"><bean:message key="caTies.conflict.filter.conflicts"/>&nbsp;
		<html:select property="selectedFilter" name="conflictViewForm" styleClass="black_ar" styleId="selectedFilter" size="1" onchange="onFilterChange(this)">
				<html:options collection="<%=Constants.FILTER_LIST%>" labelProperty="name" property="value" />
				</html:select></span>

        </label></td>
        <td width="1%" align="right">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="2" ><table width="100%" border="0" align="center" cellpadding="4" cellspacing="0">
            <tr>
			<td colspan="3">
              <custom:test name="New User Search Results" pageNum="<%=pageNum%>" totalResults="<%=totalResults%>" numResultsPerPage="<%=numResultsPerPage%>" pageName="<%=pageName%>" showPageSizeCombo="<%=true%>" recordPerPageList="<%=Constants.RESULT_PERPAGE_OPTIONS%>"/>
			<html:hidden property="<%=Constants.PAGEOF%>" value="<%=pageOf%>"/>
			<html:hidden property="isPaging" value="true"/>
			</td>
            </tr>
			</html:form>
            <tr><td>
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
    
    	<table  border="0">
	    	<tr>
				<td class="black_ar" align="top"><b><bean:message key="caTies.conflict.noreport.message" /></b></td>
			</tr>
			<tr />
		</table>
  <%}%>
			</td></tr><tr><td class="bottomtd"/></tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>


