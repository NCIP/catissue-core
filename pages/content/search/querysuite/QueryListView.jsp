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
<%@ page import="org.apache.struts.action.ActionMessages,"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<script>
function QueryWizard()
{
	var rand_no = Math.random();
	document.forms[0].action='QueryWizard.do?random='+rand_no;
	document.forms[0].submit();
}
</script>

<head>
	
	<script language="JavaScript" type="text/javascript" src="jss/queryModule.js"></script>
	<script type="text/javascript" src="jss/wz_tooltip.js"></script>
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
	
</head>

<body onunload='closeWaitPage()'>

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
String popupMessage = (String)request.getAttribute(Constants.POPUP_MESSAGE);
int queryCount = 0;
%>
<html:messages id="messageKey" message="true" >
<%
	message = messageKey;
%>
</html:messages>
	<html:form styleId='saveQueryForm' action='<%=Constants.FETCH_QUERY_ACTION%>' style="margin:0;padding:0;">
		<table cellpadding='0' cellspacing='0' border='0' height="500"  align='center' style="width:100%;"> 
			
			<tr valign="center" class="bgImage"> 
				<td width="10" >&nbsp;</td>			
				<td  align="left" width="60%" class="bgImage">
					<img src="images/ic_saved_queries.gif" alt="Saved Queries"   width="38" height="48" hspace="5" align="absmiddle"/>
					<span class="savedQueryHeading" > <bean:message key="query.savedQueries.label"/> </span>
				</td>
				<td  width="130" align="left" class="bgImage" > 					
					<span class="savedQueryHeading" ><%=message%> </span>					
				</td>
				<td width="1" valign="middle" class="bgImage" align="right">
					<img src="images/dot.gif" width="1" height="25" />
				</td>
				<td width="130" align="right" valign="middle" class="bgImage">
					<!--a href="javascript:QueryWizard()"> <img src="images/add.gif" width="125" height="18" /> </a-->
					<img src="images/add.gif" width="125" height="18" onclick="QueryWizard()"/>
				</td>
			</tr>
			
			<tr >
				<td colspan='5' ><html:errors /></td>
			</tr>
			
			<tr style="height:100%;">
				<td colspan='5'>
					<div style="width:100%; height:100%; overflow:auto; " id="searchDiv">
						<table cellpadding='0' cellspacing='0' border='0' width='99%' class='contentPage' >
							<c:set var="parameterizedQueryCollection" value="${saveQueryForm.parameterizedQueryCollection}" />
							<jsp:useBean id="parameterizedQueryCollection" type="java.util.Collection" />
					
							<c:forEach items="${parameterizedQueryCollection}" var="parameterizedQuery" varStatus="queries">
							<jsp:useBean id="parameterizedQuery" type="edu.wustl.common.querysuite.queryobject.IParameterizedQuery" />
							
								<tr>
									<%
										String target = "executeQuery('"+parameterizedQuery.getId()+"')"; 
																  String title = parameterizedQuery.getName();
																  String newTitle = AppUtility.getQueryTitle(title);
																  
																  String tooltip = AppUtility.getTooltip(title);
																  String function = "Tip('"+tooltip+"', WIDTH, 700)";
																  queryCount++;
									%>
									
									<td valign="top" height='20'width='30' style="font-size:1.2em;">
										<!--img src="images/savedQuery.bmp"/-->
										<%=queryCount%> 
									</td>
									<td  height='20'  style="padding-left:0.7em; font-size:1.1em; font-family:arial; width:92%;">
										<html:link styleClass='formQueryLink' href='#' onclick='<%=target%>'  onmouseover="<%=function%>" >
											 <%=newTitle%>
										</html:link><br/>
																				
										<b>Description: &nbsp;</b><c:out value='${parameterizedQuery.description}' />
									</td>
									<td valign="center" height='20' align="right">
										<%target = "deleteQueryPopup('"+parameterizedQuery.getId()+"','"+popupMessage+"')"; %>
										<html:image src="images/ic_trash.gif" alt="Delete" onclick='<%=target%>' />
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
