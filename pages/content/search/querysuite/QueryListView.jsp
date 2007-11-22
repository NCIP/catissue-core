<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
	import="edu.wustl.catissuecore.util.global.Constants, org.apache.struts.Globals"
%>
<%@ page import="org.apache.struts.action.ActionMessages, edu.wustl.catissuecore.util.global.Utility;"%>



<head>
	
	<script language="JavaScript" type="text/javascript" src="jss/queryModule.js"></script>
	<script type="text/javascript" src="jss/wz_tooltip.js"></script>
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
	
</head>

<body >

<% 
boolean mac = false;
Object os = request.getHeader("user-agent");
if(os!=null && os.toString().toLowerCase().indexOf("mac")!=-1)
{
	mac = true;
}
String height = "100%";		
if(mac)
{
  height="500";
}
String message = null; 
String popupMessage = (String)request.getAttribute("popupMessage");%>
<html:messages id="messageKey" message="true" >
<% message = messageKey;    %>
</html:messages>
	<html:form styleId='saveQueryForm' action='<%=Constants.FETCH_QUERY_ACTION%>' style="margin:0;padding:0;">
		<table cellpadding='0' cellspacing='0' border='0' height="<%=height%>"  align='center' style="width:100%;"> 
			<tr ><td>&nbsp;</td></tr>
			<tr>
				<td class="formTitle" height="20">
					<bean:message key="query.savedQueries.label"/>
				</td>
				<td class="formTitle" height="20" width="20%">
					<%= message %>
				</td>
			</tr>
			<tr >
				<td><html:errors /></td>
			</tr>
			
			<tr style="height:100%;">
				<td colspan='2'>
					<div style="width:100%; height:100%; overflow:auto; " id="searchDiv">
						<table cellpadding='0' cellspacing='0' border='0' width='99%' class='contentPage' >
							<c:set var="parameterizedQueryCollection" value="${saveQueryForm.parameterizedQueryCollection}" />
							<jsp:useBean id="parameterizedQueryCollection" type="java.util.Collection" />
					
							<c:forEach items="${parameterizedQueryCollection}" var="parameterizedQuery" varStatus="queries">
							<jsp:useBean id="parameterizedQuery" type="edu.wustl.common.querysuite.queryobject.IParameterizedQuery" />
							
								<tr>
									<%String target = "executeQuery('"+parameterizedQuery.getId()+"')"; 
									  String title = parameterizedQuery.getName();
									  String newTitle = Utility.getQueryTitle(title);
									  
									  String tooltip = Utility.getTooltip(title);
									  String function = "Tip('"+tooltip+"', WIDTH, 700)";
									%>
									
									<td valign="top" height='20'width='30'>
										<img src="images/savedQuery.bmp"/>
									</td>
									<td  height='20' valign="top" style="padding-left:0.7em;
																		 width:760px;">
										<html:link styleClass='formQueryLink' href='#' onclick='<%=target%>'  onmouseover="<%=function%>" >
											<b> <%=newTitle%> </b>
										</html:link><br/>				
																				
										<b>Description: &nbsp;</b><c:out value='${parameterizedQuery.description}' />
									</td>
									<td valign="top" height='20'>
										<%target = "deleteQueryPopup('"+parameterizedQuery.getId()+"','"+popupMessage+"')"; %>
										<html:image src="images/delete.gif" alt="Delete" onclick='<%=target%>' />
									</td>
								</tr>
								<tr><td colspan='3' class="saveQuery">&nbsp;</td></tr>
							</c:forEach>
						</table>
					</div>
				</td>
			</tr>
		</table>
		
		<html:hidden styleId="queryId" property="queryId" />
	</html:form>
</body>