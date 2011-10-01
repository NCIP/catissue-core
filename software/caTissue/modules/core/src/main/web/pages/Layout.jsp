<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties"%>
<%@ page import="edu.wustl.common.util.XMLPropertyHandler"%>
<%@ page import="java.text.MessageFormat"%>

<link rel="STYLESHEET" type="text/css" href="css/dhtmlxtabbar.css" />
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css" href="css/CascadeMenu.css" />
<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />

<script src="jss/script.js" type="text/javascript"></script>
<script src="jss/overlib_mini.js" type="text/javascript"></script>
<script src="jss/calender.js" type="text/javascript"></script>
<script src="jss/splitter.js" type="text/javascript"></script>
<script src="jss/ajax.js" type="text/javascript"></script>
<script src="jss/caTissueSuite.js" type="text/javascript"></script>
<script type="text/javascript" src="jss/dhtmlwindow.js"></script>
<script type="text/javascript" src="jss/modal.js"></script>
<script type="text/javascript" src="jss/ajax.js"></script>

<html>
<tiles:importAttribute />
<head>
<title><bean:message key="app.name"/> <bean:message key="app.version"/></title>

<!--Jitendra -->
<script language="JavaScript">
		var timeOut;
		var advanceTime;
		var lastRefreshTime;//timestamp in millisecond of last accessed through child page
		var pageLoadTime;
		var warnTimeout;
		var defTimeout;
		var pvwindow;
		<%
			int timeOut = -1;
			int advanceTime = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SESSION_EXPIRY_WARNING_ADVANCE_TIME));
			String tempMsg = ApplicationProperties.getValue("app.session.advanceWarning");
			Object[] args = new Object[] {"" + advanceTime};
			String advanceTimeoutMesg = MessageFormat.format(tempMsg,args);

			timeOut = -1;

			if(request.getSession().getAttribute(Constants.SESSION_DATA) != null) //if user is logged in
			{
				//timeOut = request.getSession().getMaxInactiveInterval();
				String timeOutToSet = XMLPropertyHandler
				.getValue(Constants.SESSION_TIME_OUT);
				if(timeOutToSet != null)
				{
				request.getSession().setMaxInactiveInterval(Integer.parseInt(timeOutToSet)*60);
				timeOut = request.getSession().getMaxInactiveInterval();
				}
			}
		%>


		timeOut = "<%= timeOut%>";
		advanceTime = "<%= advanceTime%>";
		pageLoadTime = new Date().getTime(); //timestamp in millisecond of last pageload
		lastRefreshTime = pageLoadTime ; // last refreshtime in millisecond
		setAdvanceSessionTimeout(timeOut);

		function warnBeforeSessionExpiry()
		{
			//check for the last refresh time,whether page is refreshed in child frame after first load.
			if(lastRefreshTime > pageLoadTime)
			{

				var newTimeout = (lastRefreshTime - pageLoadTime)*0.001;
				newTimeout = newTimeout + (advanceTime*60.0);

				pageLoadTime = lastRefreshTime ;
				setAdvanceSessionTimeout(newTimeout);

			}
			else
			{
				defTimeout = setTimeout('sendToHomePage()', advanceTime*60*1000);
				pvwindow=dhtmlmodal.open('Session Timeout', 'iframe', 'pages/SessionTimeOutWin.html','Session Timeout Warning', 'width=280px,height=115px,center=1,resize=0,scrolling=1');
			}
		}

		function setAdvanceSessionTimeout(ptimeOut)
		{

			if(ptimeOut > 0)
			{
				var time = (ptimeOut - (advanceTime*60)) * 1000;
				warnTimeout = setTimeout('warnBeforeSessionExpiry()', time); //if session timeout, then redirect to Home page
			}
		}


		function sendToHomePage()
		{
			pvwindow.hide(); // closes the message box when session expires
				<%
				   Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
				   if(obj != null)
				   {
				%>
				   var timeoutMessage = "<%= ApplicationProperties.getValue("app.session.timeout") %>";
				   window.location.href = "Logout.do";
				   alert(timeoutMessage);
				<%
				   }
				%>
		}

		function cancelMethod()
		{
			clearTimeout(defTimeout);
			sendBlankRequest();
			setAdvanceSessionTimeout(timeOut);
		}

		function getSessionWarnMessage()
		{
			return '<%=advanceTimeoutMesg%>';
		}

		function detectApplicationUsageActivity()
		{
			var currentTime = new Date().getTime();
			var activationTime = currentTime - pageLoadTime;
			var advTime = (advanceTime * 1) + 1;

			if(((timeOut*1000) - activationTime) <= (advTime*60*1000)) {
				lastRefreshTime = new Date().getTime();
				sendBlankRequest();
				clearTimeout(warnTimeout);
				clearTimeout(defTimeout);
				setAdvanceSessionTimeout(timeOut);
			}

//			if (lastRefreshTime <= pageLoadTime)
//			{
//				lastRefreshTime = new Date().getTime();
//				clearTimeout(advanceTime*60*1000);
//				sendBlankRequest();
//			}
		}

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
		
		//Added this script to avoid the script error on login page
		onerror = handleError;
		function handleError(msg, url, line) 
		{
		      return true;
		}
		
	</script>
<!--Jitendra -->

<!-- Mandar 11-Aug-06 : For calendar changes -->
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<!-- Mandar 11-Aug-06 : calendar changes end -->

<!-- For Favicon -->
<link rel="shortcut icon" href="images/favicon.ico"
	type="image/vnd.microsoft.icon" />
<link rel="icon" href="images/favicon.ico"
	type="image/vnd.microsoft.icon" />

</head>
<body onclick="detectApplicationUsageActivity()" onkeydown="detectApplicationUsageActivity()">
<table width="100%" height="99%" border="0" cellspacing="0" cellpadding="0">
	<tr height="10%">
		<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			bgcolor="#FFFFFF">
			<tr>
				<td width="20%" rowspan="2" style="border-top:4px solid #558dc0;" id="appHeader"><tiles:insert
					attribute="applicationheader">
				</tiles:insert></td>
				<td valign="top"><tiles:insert attribute="header"></tiles:insert></td>
			</tr>
			<tr>
				<td width="80%" align="right" valign="top"><tiles:insert
					attribute="mainmenu">
				</tiles:insert></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr height="90%">
		<td>
		<!--
		<table width="100%" border="0" cellspacing="0" cellpadding="0" height="475">
		-->
			<table width="100%" border="0" cellspacing="0" cellpadding="0"  height="100%">
				<tr height="100%">
					<td colspan="2" width="100%" valign="top" height="100%"><!-- target of anchor to skip menus -->
					<a name="content" style="height:100%"/> <tiles:insert attribute="content"></tiles:insert></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>
