<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties"%>
<%@ page import="edu.wustl.common.util.XMLPropertyHandler"%>
<%@ page import="edu.wustl.catissuecore.util.HelpXMLPropertyHandler"%>
<%@ page import="java.text.MessageFormat"%>

<!doctype html>
<tiles:importAttribute />
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title><bean:message key="display.app.name"/> <bean:message key="app.version"/></title>
	<%
	String URLKey=(String)request.getAttribute("helpURLKey");
	String pageOf=(String)request.getAttribute("pageOf");
	String view=(String)request.getAttribute("view");
	String helpURL=null;
	
	if(null==URLKey)
	{
		helpURL=HelpXMLPropertyHandler.getValue(Constants.HELP_HOME_PAGE);
	}
	else
	{
		helpURL=HelpXMLPropertyHandler.getValue(URLKey);
		if(helpURL==null || "".equals(helpURL))
		{
			helpURL=HelpXMLPropertyHandler.getValue(Constants.HELP_HOME_PAGE);
		}
	}

	%>
<!--Jitendra -->
<style>
    html, body {
      height: 100%;
    }
</style>

<script language="JavaScript">
		function getUmlModelLink()
		{
				var  frameUrl="<%=XMLPropertyHandler.getValue("umlmodel.link")%>";
				NewWindow(frameUrl,'name');
		}

		function getUserGuideLink()
		{
			var frameUrl = "<%=XMLPropertyHandler.getValue("userguide.link")%>";
			NewWindow(frameUrl,'name');
		}
		
		function getHelpURL()
		{
			var URL;
			<%
			if(null!=helpURL) 
			{
				if(null==pageOf && null!=view && "cpBasedView".equals(view))
				{%>
					URL=document.getElementById('cpFrameNew').contentWindow.updateHelpURL();
				<%}
				else if(!"".equals(helpURL))
				{%>
					URL="<%=helpURL%>";
				<%}
			}%>
			if(URL!="")
			{
				window.open(URL,'_blank');
			}			
		}
	</script>
<!--Jitendra -->

<SCRIPT>var imgsrc="images/";</SCRIPT>

<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<!-- Mandar 11-Aug-06 : calendar changes end -->

<!-- For Favicon -->
<link rel="shortcut icon" href="images/favicon.ico"
	type="image/vnd.microsoft.icon" />
<link rel="icon" href="images/favicon.ico"
	type="image/vnd.microsoft.icon" />

</head>
<body>
<table width="100%" height="99%" border="0" cellspacing="0" cellpadding="0">
	<tr height="8%" valign="top" >
		<td>
		<logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<!--bgcolor="#D0DCBD"-->
				<tr>
				<td width="30%" rowspan="2" ><tiles:insert
					attribute="applicationheader">
				</tiles:insert></td>
				<td valign="top"><tiles:insert attribute="header"></tiles:insert></td>
			</tr>
			<tr>
				<td width="70%" align="right" valign="top"><tiles:insert
					attribute="mainmenu">
				</tiles:insert></td>
			</tr>
		</table>
		</logic:empty>
			<logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					bgcolor="#FFFFFF">
					<tr>
						<td width="30%" rowspan="2" style="border-top:4px solid #558dc0;"><tiles:insert
							attribute="applicationheader">
						</tiles:insert></td>
						<td valign="top"><tiles:insert attribute="header"></tiles:insert></td>
					</tr>
					<tr>
						<td width="70%" align="right" valign="top"><tiles:insert
							attribute="mainmenu">
						</tiles:insert></td>
					</tr>
				</table>
			</logic:notEmpty>
		
		</td>
	</tr>
	<tr height="88%">
		<td height="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"  height="100%">
				<tr height="100%">
					<td colspan="2" width="100%" valign="top" height="100%"><!-- target of anchor to skip menus -->
        <tiles:useAttribute name="pageContentUrl" classname="org.apache.struts.tiles.DirectStringAttribute"/>
        <% String url = pageContentUrl.getValue().toString(); %>
					<a name="content" style="height:100%"/> <iframe src="<%= url %>" width="100%" height="100%" frameborder="0" marginheight="0" marginwidth="0"></iframe>
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>
<!-- Mandar 11-Aug-06 : For calendar changes -->
<script src="jss/calendarComponent.js"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script src="jss/overlib_mini.js" type="text/javascript"></script>
<script src="jss/calender.js" type="text/javascript"></script>
<script src="jss/splitter.js" type="text/javascript"></script>
<script src="jss/caTissueSuite.js" type="text/javascript"></script>
<script type="text/javascript" src="jss/dhtmlwindow.js"></script>
<script type="text/javascript" src="jss/modal.js"></script>
<script type="text/javascript" src="jss/ajax.js"></script>
<script type="text/javascript" src="jss/ie9-prototypes.js"></script>

</html>
