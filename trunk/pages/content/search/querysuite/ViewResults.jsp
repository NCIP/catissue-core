<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<script src="jss/queryModule.js"></script>
<%
String formAction = Constants.DefineSearchResultsViewJSPAction;
           boolean mac = false;
	        Object os = request.getHeader("user-agent");
			if(os!=null && os.toString().toLowerCase().indexOf("mac")!=-1)
			{
			    mac = true;
			}
	String height = "100%";		
	if(mac)
	{
	  height="300";
    }
%>
<html:form method="GET" action="<%=formAction%>">
<table border="2" width="100%" cellspacing="0" cellpadding="0" height="100%" bordercolor="#000000" id="table1" >
		<tr  class="trStyle">
			<td width="25%" height="5%" class="queryModuleTabMenuItem" >
				<bean:message key="query.addLimits"/>
			</td>

			<td width="25%" height="5%" class="queryModuleTabMenuItem" >
				<bean:message key="query.defineSearchResultsViews"/>
			</td>

			<td width="25%" height="5%" class="queryTabMenuItemSelected">
				<bean:message key="query.viewSearchResults"/>
			</td>

			<td width="25%" height="5%" class="queryModuleTabMenuItem">
				<bean:message key="query.dataList"/>
			</td>			
		</tr>
		<tr height="100%">
		<td colspan="4" height="100%">
<table border="2" height="100%" width="100%">
	<tr height="100%">
		<td width="30%" colspan="1">
			<iframe id="<%=Constants.TREE_VIEW_FRAME%>" src="<%=Constants.QUERY_TREE_VIEW_ACTION%>?pageOf=pageOfQueryResults" scrolling="yes" frameborder="1" width="100%" height="<%=height%>">
				Your Browser doesn't support IFrames.
			</iframe>
		</td>
		<td width="100%" height="500" colspan="3">
			<iframe name="<%=Constants.GRID_DATA_VIEW_FRAME%>" src="<%=Constants.QUERY_GRID_VIEW_ACTION%>?pageOf=pageOfQueryModule" <%=Constants.VIEW_TYPE%>=<%=Constants.SPREADSHEET_VIEW%>" scrolling="no" frameborder="1" width="100%" height="<%=height%>">
				Your Browser doesn't support IFrames.
			</iframe>
		</td>
	</tr>
</table>
</td>
</tr>
<tr bgcolor="#DFE9F3">
		<td colspan="4" valign="bottom" height="30">
		<table border="0" width="100%" cellspacing="0" cellpadding="0" bgcolor="#EAEAEA" height="100%" bordercolorlight="#000000" >
		<tr height="35" valign="center">
		 <td width="2%" valign="center">&nbsp;</td>
		 <td valign="center" width="75%"><html:button property="Button"><bean:message key="query.saveButton"/></html:button></td>
	   	 <td align="right" valign="center"><html:button property="Button" onclick="defineSearchResultsView()"><bean:message key="query.previousButton" /></html:button></td>
		 <td align="right" valign="center"><html:button property="Button" onclick=""><bean:message key="query.nextButton"/></html:button>
		 </td>
		 <td width="2%">&nbsp;</td>
</tr>
</table>	
</td>
</tr>
</table>			
</html:form>