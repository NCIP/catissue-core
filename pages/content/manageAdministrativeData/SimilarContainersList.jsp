<!-- 
	This JSP page is to create/display similar containers from/of Parent Storage Container.
	Author : Chetan B H
	Date   : 
-->
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility" %>
<%@ page import="edu.wustl.catissuecore.actionForm.StorageContainerForm" %>
<%@ page import="edu.wustl.catissuecore.domain.StorageContainer" %>
<%@ page import="edu.wustl.common.beans.NameValueBean" %>
<head>
</head>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

<html:form action="<%=Constants.SIMILAR_CONTAINERS_ADD_ACTION%>">

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
<tr>
<td>
	<% int cnt=0;%>
	<table summary="" cellpadding="2" cellspacing="0" border="1" width="600">
		<tr>
			 <td class="formTitle" height="20" colspan="3">
		 	LIST OF ADDED CONTAINERS
			 </td>
		 </tr>	
		 <tr>
			<logic:iterate id="nvb" name="similarContainerList">
			<%	NameValueBean nameValueBean=(NameValueBean)nvb;
			String hrefString="StorageContainerSearch.do?pageOf=pageOfStorageContainer&id="+nameValueBean.getValue();
			%>
	
			<td class="formFieldNoBorders">
				<html:link href="<%=hrefString%>"><bean:write name="nvb" property="name"/></html:link>			
			</td>	
				<%cnt=cnt+1;%>
				<%
					if(cnt == 3)
					{	
						cnt=0;
				%>	
				</tr>
				<tr>
				<%}%>
				</logic:iterate>
			
				<%if(cnt%3==2)
				{%>
				<td class="formFieldNoBorders">&nbsp;</td>
			<%}%>
			<%if(cnt%3==1)
			{%>
				<td class="formFieldNoBorders">&nbsp;</td><td class="formFieldNoBorders">&nbsp;</td>
			<%}%>
	
		</tr>
	</table>
</td>
</tr>
</table>
</html:form>