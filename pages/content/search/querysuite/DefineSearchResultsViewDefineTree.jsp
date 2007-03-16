<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<html>
<body>
<table border="0"  height="100%" width="100%" cellpadding="1" cellspacing="3" valign="top">
	
<tr  valign="top">
	<td  valign="top" width="20%">
		<table border="0"  height="100%" width="100%" cellpadding="1" cellspacing="3" valign="top">
			<tr  class='validationMessageCss'  >
				<td width="80%" class='validationMessageCss' style="display:none">
					&nbsp;
				</td>
			</tr>
			<tr valign="top" width="100%" height="80%" align="left">
				<td valign="top" height="100%" align="left" >
					<%@ include file="/pages/content/search/querysuite/ChooseSearchCategory.jsp" %>
				</td>
			</tr>
			<tr valign="top" width="100%" height="20%" align="left">
				<td valign="top" align="center" height="100%">
					<html:button property="Button" onclick = "addToView()"><bean:message key="query.addToView"/></html:button>
				</td>
			</tr>
		</table>
	</td>
	<td>
	    <table border="0"  height="100%" width="100%" cellpadding="1" cellspacing="3" valign="top">
			<tr  id="rowMsg" class='validationMessageCss'>
				<td id="validationMessagesSection"  width="80%" class='validationMessageCss'>
					<div id="validationMessagesRow"   style="overflow:auto; width:820; height:50;display:none"></div>
				</td>
			</tr>
			<tr>
				<td valign="top" width="80%" height="100%">
					<div id="queryTableTd" style="overflow:auto;height:100%;width:100%"" valign="top">
						<APPLET
							CODEBASE = "<%=Constants.APPLET_CODEBASE%>"
							ARCHIVE = "dagViewApplet.jar, cab2bStandAlone.jar,commonpackage.jar,swingx-2006_10_08.jar,org-netbeans-graph-vmd.jar,org-netbeans-graph.jar,org-openide-util.jar,DynamicExtensions.jar,struts.jar,hibernate2.1.7c.jar,odmg.jar,log4j-1.2.9.jar"
							CODE = "<%=Constants.QUERY_DAG_VIEW_APPLET%>"
							ALT = "Dag view Applet"
							NAME = "<%=Constants.QUERY_DAG_VIEW_APPLET_NAME%>"
							width="100%" height="100%" MAYSCRIPT>
							<PARAM name="type" value="application/x-java-applet;jpi-version=1.5.0_08">
							<PARAM name="name" value="<%=Constants.QUERY_DAG_VIEW_APPLET_NAME%>">
							<PARAM name="session_id" value="<%=session.getId()%>">					
							<PARAM name = "<%=Constants.APPLET_SERVER_URL_PARAM_NAME%>" value="<%=Constants.APPLET_SERVER_HTTP_START_STR%><%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>">
							<PARAM name="isForView" value="true">">
						</APPLET>
					</div>
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>
</body>
</html>