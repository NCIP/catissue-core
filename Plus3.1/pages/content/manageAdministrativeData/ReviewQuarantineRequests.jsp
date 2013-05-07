<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%
	
	List dataList = (List) request.getAttribute(Constants.SHOW_DOMAIN_OBJECT_LIST);
	List columnList = (List)request.getAttribute(Constants.COLUMN_LIST);
	String pageOf = (String)request.getParameter(Constants.PAGE_OF);
	String reportAction = (String)request.getParameter(Constants.REPORT_ACTION);
	String title = pageOf + ".report.request";
	if(reportAction.equals(Constants.REVIEW))
	{
		title =  "reviewSPR" + ".report.request";
	}
	else
	{
		title = "quarantineSPR" + ".report.request";
	}
	
	Integer identifierFieldIndex = 4;
	int pageNum = Integer.parseInt((String)request.getAttribute(Constants.PAGE_NUMBER));
	int totalResults = Integer.parseInt((String)session.getAttribute(Constants.TOTAL_RESULTS));
	int numResultsPerPage = Integer.parseInt((String)session.getAttribute(Constants.RESULTS_PER_PAGE));	
	String pageName="ReviewRequests.do";
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
			<html:form action="/ReviewRequests">				
				<custom:test name="Search Results" pageNum="<%=pageNum%>" totalResults="<%=totalResults%>" numResultsPerPage="<%=numResultsPerPage%>" pageName="<%=pageName%>" showPageSizeCombo="<%=true%>"/>
				<html:hidden property="reportAction" value="<%=reportAction%>"/>
				<html:hidden property="<%=Constants.PAGE_OF%>" value="<%=pageOf%>"/>
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
	var useDefaultRowClickHandler=2;
	var useFunction = "viewSPR";	
	
	function viewSPR(id)
	{
		var colid = <%=identifierFieldIndex.intValue()%>;
		var cl = mygrid.cells(id,colid);
		var searchId = cl.getValue();
		var action = "ViewSurgicalPathologyReport.do?pageOf=<%=pageOf%>&IDENTIFIER="+searchId;
		window.location.href = action;
	} 			
</script>
<%
if(dataList!=null)
{
%>
	
	<%@ include file="/pages/content/search/GridPage.jsp" %>

<%
}
%>