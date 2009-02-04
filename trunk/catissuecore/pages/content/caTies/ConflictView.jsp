
<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/PagenationTag.tld" prefix="custom" %>
<%@ page import="java.util.List,edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>

<%
	String[] columnList1 = Constants.CONFLICT_LIST_HEADER;
	List columnList = new ArrayList();
	for(int i=0;i<columnList1.length;i++)
	{
		columnList.add(columnList1[i]);
	}
	List dataList = (List) session.getAttribute(Constants.REPORT_QUEUE_LIST);	
	Integer identifierFieldIndex = 0;
	String pageOf = "Add";

%>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="4%">
	<tr>
		 <td class="formTitle">
			<bean:message key="caTies.conflict.title"/>
		 </td>
	</tr>	
</table>
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

		var cl = mygrid.cells(id,colid);
		var searchId = cl.getValue();
		var url = "ConflictDetails.do?reportQueueId="+searchId;
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