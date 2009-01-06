<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.common.beans.SessionDataBean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<html>
<%
	String MODE = (String)request.getAttribute("MODE");
	String PARENT_TYPE = (String)request.getAttribute("PARENT_TYPE");
	String PARENT_NAME = (String)request.getAttribute("PARENT_NAME");
	String SP_COUNT = (String)request.getAttribute("SP_COUNT");
	String SHOW_PARENT_SELECTION = (String) request.getAttribute("SHOW_PARENT_SELECTION");
	String SHOW_LABEL = (String) request.getAttribute("SHOW_LABEL");
	String SHOW_BARCODE = (String) request.getAttribute("SHOW_BARCODE");
	String DATE_FORMAT = (String) request.getAttribute("DATE_FORMAT");
	SessionDataBean bean = (SessionDataBean) session.getAttribute("sessionData");
	String temp = (String) session.getAttribute("temp");
%>

<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<script src="jss/queryModule.js"></script>
<script type="text/javascript" src="jss/ajax.js"></script> 
<script language="JavaScript" >
		//Set last refresh time
		if(window.parent!=null)
		{
			if(window.parent.lastRefreshTime!=null)
			{
				window.parent.lastRefreshTime = new Date().getTime();
			}
		}	
</script>
<script language="JavaScript">
function callSubmitSpecimen()
{
	<%
		String formAction2 = "GenericSpecimenSummary.do";
		if(request.getAttribute(Constants.PAGEOF) != null)
		{
			formAction2 = "MultipleSpecimenView.do?pageOf="+request.getAttribute(Constants.PAGEOF)+"&mode=add";
		}
	%>
	document.forms[0].action = "<%=formAction2%>";
	document.forms[0].submit();
	
}

function callUpdateSpecimen()
{
	<%
		String formAction1 = "GenericSpecimenSummary.do";
		if(request.getAttribute(Constants.PAGEOF) != null)
		{
			formAction1 = "MultipleSpecimenView.do?pageOf="+request.getAttribute(Constants.PAGEOF)+"&mode=edit";
		}
	%>
	document.forms[0].action = "<%=formAction1%>";
	document.forms[0].submit();
	
}
if(navigator.userAgent.indexOf('Mac')>=0)
{
	window.onload = function() {document.getElementById("td1").height=700;document.getElementById("multiplespeId").height=700; }
	window.onresize = function() {document.getElementById("td1").height=700;document.getElementById("multiplespeId").height=700; }
}
</script>
</head>
<body>
	<table border="1" width="100%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" height="100%" bordercolorlight="#000000">
	<tr>
		<td>
			<%@ include file="/pages/content/common/ActionErrors.jsp" %>
		</td>
	</tr>
		<tr>
<%
      Object os = request.getHeader("user-agent");
      if(os!=null && os.toString().toLowerCase().indexOf("mac")!=-1)
      {
%>
			<td id="td1" height=700>											
				<div id="multiplespeId" style="overflow:auto;height:700;width:100%">

<%
      }else{
%>
			<td id="td1" height=100%>											
				<div id="multiplespeId" style="overflow:auto;height:100%;width:100%">
<%
}
%>
					<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
						id="MultipleSpecimen" width="100%" height="100%"
						codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
						<param name="movie" value="flexclient/multiplespecimen/MultipleSpecimen.swf?MODE=<%=MODE%>&PARENT_TYPE=<%=PARENT_TYPE%>&PARENT_NAME=<%=PARENT_NAME%>&SP_COUNT=<%=SP_COUNT%>&SHOW_PARENT_SELECTION=<%=SHOW_PARENT_SELECTION%>&SHOW_LABEL=<%=SHOW_LABEL%>&SHOW_BARCODE=<%=SHOW_BARCODE%>&DATE_FORMAT=<%=DATE_FORMAT%>"/>
						<param name="quality" value="high" />
						<param name="wmode" value="transparent" />						
						<param name="bgcolor" value="#869ca7" />
						<param name="allowScriptAccess" value="sameDomain"/>
							<embed src="flexclient/multiplespecimen/MultipleSpecimen.swf?MODE=<%=MODE%>&PARENT_TYPE=<%=PARENT_TYPE%>&PARENT_NAME=<%=PARENT_NAME%>&SP_COUNT=<%=SP_COUNT%>&SHOW_PARENT_SELECTION=<%=SHOW_PARENT_SELECTION%>&SHOW_LABEL=<%=SHOW_LABEL%>&SHOW_BARCODE=<%=SHOW_BARCODE%>&DATE_FORMAT=<%=DATE_FORMAT%>" quality="high" bgcolor="#869ca7"
								width="100%" height="100%" name="MultipleSpecimen" align="middle"
								play="true"
								loop="false"
								quality="high"
								wmode="transparent"
								allowScriptAccess="sameDomain"
								type="application/x-shockwave-flash"
								pluginspage="http://www.adobe.com/go/getflashplayer">
							</embed>
						</object>
					</div>
			</td>
		</tr>
		<tr>
			<td>
				<html:form action="GenericSpecimenSummary.do">
				</html:form>
			</td>
		</tr>
	</table>
</body>
</html> 