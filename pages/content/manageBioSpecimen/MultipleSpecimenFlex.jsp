<!-- saved from url=(0014)about:internet -->
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>



<%@ page import="edu.wustl.common.beans.SessionDataBean" %>



<%
	String MODE = (String)request.getAttribute("MODE");
	String PARENT_TYPE = (String)request.getAttribute("PARENT_TYPE");
	String PARENT_NAME = (String)request.getAttribute("PARENT_NAME");
	String SP_COUNT = (String)request.getAttribute("SP_COUNT");
	SessionDataBean bean = (SessionDataBean) session.getAttribute("sessionData");
	String temp = (String) session.getAttribute("temp");
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<script src="flexclient/resources/jss/AC_OETags.js" language="javascript"></script>
<style>
body { margin: 0px; overflow:hidden }
</style>
<script language="JavaScript" type="text/javascript">
<!--
// -----------------------------------------------------------------------------
// Globals
// Major version of Flash required
var requiredMajorVersion = 9;
// Minor version of Flash required
var requiredMinorVersion = 0;
// Minor version of Flash required
var requiredRevision = 0;
// -----------------------------------------------------------------------------
// -->


</script>
<script language="JavaScript">
function callSubmitSpecimen()
{
	alert("in javscript functionm");
	alert("<%=bean.getUserName()%>");
	alert("<%=temp%>");
	
	
	alert("After session attribute display");
	document.forms[0].submit();
//	alert("action:"+document.forms[0].action);
	//document.forms[0].submit();
	
}
</script>
</head>

<body scroll="no">

<script language="JavaScript" type="text/javascript" src="flexclient/resources/jss/history.js"></script>
<script language="JavaScript" type="text/javascript">
<!--
// Version check for the Flash Player that has the ability to start Player Product Install (6.0r65)
var hasProductInstall = DetectFlashVer(6, 0, 65);

// Version check based upon the values defined in globals
var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);


// Check to see if a player with Flash Product Install is available and the version does not meet the requirements for playback
if ( hasProductInstall && !hasRequestedVersion ) {
	// MMdoctitle is the stored document.title value used by the installation process to close the window that started the process
	// This is necessary in order to close browser windows that are still utilizing the older version of the player after installation has completed
	// DO NOT MODIFY THE FOLLOWING FOUR LINES
	// Location visited after installation is complete if installation is required
	var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
	var MMredirectURL = window.location;
    document.title = document.title.slice(0, 47) + " - Flash Player Installation";
    var MMdoctitle = document.title;

	AC_FL_RunContent(
		"src", "flexclient/resources/playerProductInstall",
		"FlashVars", "MMredirectURL="+MMredirectURL+'&MMplayerType='+MMPlayerType+'&MMdoctitle='+MMdoctitle+"",
		"width", "100%",
		"height", "100%",
		"align", "middle",
		"id", "MultipleSpecimen",
		"quality", "high",
		"bgcolor", "#ffffff",
		"name", "MultipleSpecimen",
		"allowScriptAccess","sameDomain",
		"type", "application/x-shockwave-flash",
		"pluginspage", "http://www.adobe.com/go/getflashplayer"
	);
} else if (hasRequestedVersion) {
	// if we've detected an acceptable version
	// embed the Flash Content SWF when all tests are passed
	AC_FL_RunContent(
			"src", "flexclient/multiplespecimen/MultipleSpecimen?MODE=<%=MODE%>&PARENT_TYPE=<%=PARENT_TYPE%>&PARENT_NAME=<%=PARENT_NAME%>&SP_COUNT=<%=SP_COUNT%>",
			"width", "100%",
			"height", "100%",
			"align", "middle",
			"id", "MultipleSpecimen",
			"quality", "high",
			"bgcolor", "#ffffff",
			"name", "MultipleSpecimen",
			"flashvars",'historyUrl=history.htm%3F&lconid=' + lc_id + '',
			"allowScriptAccess","sameDomain",
			"type", "application/x-shockwave-flash",
			"pluginspage", "http://www.adobe.com/go/getflashplayer"
	);
  } else {  // flash is too old or we can't detect the plugin
    var alternateContent = 'Alternate HTML content should be placed here. '
  	+ 'This content requires the Adobe Flash Player. '
   	+ '<a href=http://www.adobe.com/go/getflash/>Get Flash</a>';
    document.write(alternateContent);  // insert non-flash content
  }
// -->
</script>
<noscript>
  	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="MultipleSpecimen" width="100%" height="100%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="flexclient/multiplespecimen/MultipleSpecimen.swf?MODE=ADD&PARENT_TYPE=SCG&PARENT_NAME=AA&SP_COUNT=2" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="allowScriptAccess" value="sameDomain" />
			<embed src="flexclient/multiplespecimen/MultipleSpecimen.swf?MODE=ADD&PARENT_TYPE=SCG&PARENT_NAME=AA&SP_COUNT=2" quality="high" bgcolor="#ffffff"
				width="100%" height="100%" name="MultipleSpecimen" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object>
</noscript>
<iframe name="_history" src="flexclient/resources/history.htm" frameborder="0" scrolling="no" width="22" height="0"></iframe>

</body>
<html:form action="GenericSpecimenSummary.do">
</html:form>