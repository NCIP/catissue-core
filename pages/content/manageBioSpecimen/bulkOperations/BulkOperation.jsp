<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.bulkoperator.util.BulkOperationConstants"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties" %>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<script language="JavaScript" type="text/javascript" src="jss/bulkOperatorScripts.js"></script>
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
var interval=${requestScope.gridRefreshTime};
var refreshinterval=5;
if(interval != null && interval != 0)
{
	refreshinterval=interval;
}

var displaycountdown="yes";
var starttime;
var nowtime;
var reloadseconds=0;
var secondssinceloaded=0;

function starttime()
{
	starttime=new Date();
	starttime=starttime.getTime();
	countdown();
}

function startProcess()
{
	starttime=new Date();
	starttime=starttime.getTime();
	countdown();
}

function countdown()
{
	nowtime= new Date();
	nowtime=nowtime.getTime();
	secondssinceloaded=(nowtime-starttime)/1000;
	reloadseconds=Math.round(refreshinterval-secondssinceloaded);
	if (refreshinterval>=secondssinceloaded)
	{
		var timer=setTimeout("countdown()",1000);
		if (displaycountdown=="yes")
		{
			window.status="Page refreshing in "+reloadseconds+ " seconds";
		}
	}
	else
	{
		clearTimeout(timer);
		window.frames['bulkOperationDashoard'].getGridXml();
		window.frames['bulkOperationDashoard'].initializeAjaxCall();
		callStartTime();
	}
}
function callStartTime()
{
	startProcess();
}

window.onload=starttime;

</SCRIPT>

<head>
	<LINK href="css/styleSheet.css" type="text/css" rel="stylesheet">
	<link href="css/bulkOperator.css" rel="stylesheet" type="text/css" />
</head>

<body>
	<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
		<tr height="100%">
			<td class="tablepadding" height="100%" valign="top">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
					</tr>
				</table>
				<table width="100%" height="94%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
					<tr height="2%">
						<td align="left" height="2%" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="bulk.bulkoperations" /></span></td>
					</tr>
					<tr height="30%">
						<td align="left" valign="top" class="showhide">
							<logic:empty name="noTemplates">
								<iframe id="bulkTemplate" name="bulkTemplate" src="BulkTemplate.do?pageOf=pageOfBulkOperation" scrolling="no" frameborder="0" style="width:100%;height:100%;" marginheight='0' marginwidth='0' height="100%">
									Your Browser doesn't support IFrames.
								</iframe>
							</logic:empty>
							<logic:notEmpty name = "noTemplates">
							<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
								<tr height="40">
									<td align="left" class="black_ar">
										<b><bean:message key="bulk.no.templates.loaded.message" /></b>
									</td>
								</tr>
							</table>
							</logic:notEmpty>
						</td>
					</tr>
					<tr height="*">
						<td height="*" valign="top">
							<table width="100%" valign="top" border="0" height="100%" cellpadding="3" cellspacing="0" class="whitetable_bg">
								<tr>
									<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="bulk.bulkoperation.dashboard" /></span></td>
								</tr>
								<tr>
									<td class="black_ar"></td>
								</tr>
								<tr height="100%">
									<td width="100%" height="100%" align="center" class="black_ar">

										<iframe id="bulkOperationDashoard" height="100%"name="bulkOperationDashoard" src="JobDashboard.do" scrolling="auto" frameborder="0" style="width:100%;height:100%;" marginheight='0' marginwidth='0'>
											Your Browser doesn't support IFrames.
										</iframe>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>