<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.util.global.Variables"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties"%>
<%@ page import="edu.wustl.common.util.XMLPropertyHandler"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%@ page import="java.text.MessageFormat"%>

<tiles:importAttribute />

<html>
<head>
<title><tiles:getAsString name="title" ignore="true" /></title>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script src="jss/script.js" type="text/javascript"></script>
<script src="jss/overlib_mini.js" type="text/javascript"></script>
<script src="jss/calender.js" type="text/javascript"></script>
<script src="jss/splitter.js" type="text/javascript" ></script>
<script src="jss/ajax.js" type="text/javascript"></script>

<%
	/*
     * Name : Kapil Kaveeshwar
     * Reviewer: Sachin Lale
     * Bug ID: Menu_Splitter
     * Patch ID: Menu_Splitter_1
     * See also: All Menu_Splitter
     * Description: Last state of menu splitter is recorded in the session of the user, sot that next time user
     * navigates to the page the state can be preserved. This part of the code the reads the menu status from 
     * session. 
     */
	Object splitterOpenStatusObj = request.getSession().getAttribute(Constants.SPLITTER_STATUS_REQ_PARAM);
	String splitterOpenStatus = "true";
	if(splitterOpenStatusObj!=null)
	{
		splitterOpenStatus = (String)splitterOpenStatusObj;		
	}
%>
		
<!--Jitendra -->
<script language="JavaScript">
	var timeOut;
	var advanceTime;
	<%
		int timeOut = -1;
		int advanceTime = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SESSION_EXPIRY_WARNING_ADVANCE_TIME));
		String tempMsg = ApplicationProperties.getValue("app.session.advanceWarning");
		Object[] args = new Object[] {"" + advanceTime};
		String advanceTimeoutMesg = MessageFormat.format(tempMsg,args);
		
		timeOut = -1;
			
		if(request.getSession().getAttribute(Constants.SESSION_DATA) != null) //if user is logged in
		{
			timeOut = request.getSession().getMaxInactiveInterval();
		}
	%>


	timeOut = "<%= timeOut%>";	
	advanceTime = "<%= advanceTime%>";

	setAdvanceSessionTimeout(timeOut);
	
	function warnBeforeSessionExpiry()
	{			
		var defTimeout = setTimeout('sendToHomePage()', advanceTime*60*1000);
		var choice = confirm("<%= advanceTimeoutMesg %>");
		
		if(choice == 0) //cancel pressed, extend session
		{
			clearTimeout(defTimeout);
			sendBlankRequest();
			setAdvanceSessionTimeout();
    	}
	}
	
	function setAdvanceSessionTimeout() 
	{
		
		if(timeOut > 0)
		{
			var time = (timeOut - (advanceTime*60)) * 1000;
			setTimeout('warnBeforeSessionExpiry()', time); //if session timeout, then redirect to Home page
		}
	}
	
	function sendToHomePage()
	{			
			<% 
			   Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);			  			
			   if(obj != null) 
			   {
			%>			
			   var timeoutMessage = "<%= ApplicationProperties.getValue("app.session.timeout") %>";
			   alert(timeoutMessage);			  
		   
			   window.location.href = "Logout.do";
			<%
			   }
			%>		  
	}	
	
</script>
<!--Jitendra -->

<!-- Mandar 11-Aug-06 : For calendar changes -->
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<!-- Mandar 11-Aug-06 : calendar changes end -->

<!-- For Favicon -->
<link rel="shortcut icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>
<link rel="icon" href="images/favicon.ico" type="image/vnd.microsoft.icon"/>


</head>
<body>
<table summary="" cellpadding="0" cellspacing="0" border="0"
	width="100%" height="99%">

	<!-- caBIG hdr begins -->
	<tr>
		<td><tiles:insert attribute="header"></tiles:insert></td>
	</tr>
	<!-- caBIG hdr ends -->

	<tr>
		<td height="100%" valign="top">
		<table summary="" cellpadding="0" cellspacing="0" border="0"
			height="100%" >
			<!-- 
				 Name : Virender Mehta
		       	 Reviewer: Sachin Lale
				 Bug ID: MoveLogoBugId
				 Patch ID: MoveLogoBugId_2
				 See also: MoveLogoBugId_1
				 Description: Commented code because catissueLogo is moved to upper left position of the page
			 -->	
			<!-- application hdr begins -->
			<!--tr>
				<td colspan="2" height="50"><tiles:insert
					attribute="applicationheader"></tiles:insert></td>
			</tr-->
			<!-- application hdr ends -->
			<tr>
				<td width="180px" valign="top" class="subMenu" id="sideMenuTd">
				
					<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="100%">
						<tr>
							<td class="subMenuPrimaryTitle" height="40">
							<img src="images/caTISSUE_logo.gif" alt="application logo" width="170" border="0" height="40px"/>
							</td>
						</tr>
						<tr>
							<td>
								<!-- submenu begins -->				
								<tiles:insert attribute="commonmenu">
									<tiles:put name="submenu" beanName="submenu" />
								</tiles:insert> 
								<!-- submenu ends -->
							</td>
						</tr>
					</table>
				</td>
				
				<!--
		         * Name : Kapil Kaveeshwar
		         * Reviewer: Sachin Lale
		         * Bug ID: Menu_Splitter
		         * Patch ID: Menu_Splitter_3
		         * See also: All Menu_Splitter
		         * Description: A TD tag which holds the splitter. On click on this, it calls the JS method
		         * toggleMenuStatus() which toggles the state of menu status
		         -->
				<logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
					<TD id=menucontainer width="2" class="subMenuPrimaryTitle" valign="center" align="center" 	
						onmouseover="changeMenuStyle(this,'subMenuSecondaryTitleOver'),showCursor()" 
						onmouseout="changeMenuStyle(this,'subMenuPrimaryTitle'),hideCursor()" onclick="toggleSplitterStatus()">
						<SPAN id="splitter"><img src="leftPane_collapseButton.gif"/></SPAN>
					</TD>
					
					<!-- initialize the menu state -->
					<script>
						initSplitterOpenStatus(<%=Boolean.valueOf(splitterOpenStatus)%>);
					</script>
				</logic:notEmpty>
				
				<td valign="top" width="100%">
				<table summary="" cellpadding="0" cellspacing="0" border="0"
					width="100%" height="100%">
					<tr>
						<td height="20" width="90%" class="mainMenu"><!-- main menu begins -->
						<tiles:insert attribute="mainmenu"></tiles:insert> <!-- main menu ends -->

						</td>
						<td height="20" class="mainMenu" align="right">
						<table summary="" cellpadding="0" cellspacing="0" border="0"
							height="20">
							<tr>
							
								<td height="20" class="mainMenuItem"
									onclick="document.location.href='Home.do'">
									<logic:empty scope="session" name="<%=Constants.SESSION_DATA%>">
									<html:link styleClass="mainMenuLink" page="/Home.do">
										<bean:message key="app.loginMessage" />
									</html:link>
									</logic:empty>
									<logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
									<html:link styleClass="mainMenuLink" page="/Logout.do">
										<bean:message key="app.logoutMessage" />
									</html:link>
									</logic:notEmpty>
								</td>
							</tr>
						</table>
								
						</td>
					</tr>

					<!--_____ main content begins _____-->
					<tr height="90%">
						<td colspan="2" width="100%" valign="top"><!-- target of anchor to skip menus --><a
							name="content" /> <tiles:insert attribute="content"></tiles:insert></td>
					</tr>
					<!--_____ main content ends _____-->

					<tr>
						<td colspan="2" height="20" width="100%" class="footerMenu"><!-- application ftr begins -->
						<tiles:insert attribute="applicationfooter"></tiles:insert> <!-- application ftr ends -->

						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td><!-- footer begins --> <tiles:insert attribute="mainfooter"></tiles:insert>
		<!-- footer ends --></td>
	</tr>
</table>
</body>
</html>