<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties" %>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="edu.wustl.bulkoperator.jobmanager.Constants"%>
<LINK href="css/styleSheet.css" type="text/css" rel="stylesheet">
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.css"/>

<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.css"/>
  <link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/skins/dhtmlxgrid_dhx_skyblue.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn_bricks.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCalendar/codebase/dhtmlxcalendar.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid_skins.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/skins/dhtmlxgrid_dhx_blue.css"/>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/skins/dhtmlxgrid_dhx_black.css"/>



<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>
<script src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_srnd.js"></script>
<script  src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_pgn.js"></script>
<script  src="dhtmlxSuite_v35/dhtmlxDataView/codebase/connector/connector.js"></script>
<script  src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_splt.js"></script>
<script  src="dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_filter.js"></script>
<script  src="dhtmlxSuite_v35/dhtmlxCalendar/codebase/dhtmlxcalendar.js"></script>



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
		//window.frames['bulkOperationDashoard'].getGridXml();
		window.frames['bulkOperationDashoard'].initializeAjaxCall();
		callStartTime();
	}
}
function callStartTime()
{
	startProcess();
}

window.onload=starttime;

function onUploadClick()
{
	
	var uploadFileName = document.getElementById('file').value;
	var fileNameArray = uploadFileName.split(".");
	var arraySize = fileNameArray.length;
	var threshHold = document.getElementById('threshHold').value
	var jobType="";
	var type = document.forms[0].jobType;
	for( i =0 ;i<type.length;i++)
	{
		if(type[i].checked)
		{
			jobType = type[i].value;
		}
	}
	
	
	if(fileNameArray[arraySize -1] != 'csv')
	{
		alert("Please upload a comma seperated file (.csv).");
	}
	else
	{
		if(jobType=='BULK MATCHING')
		{
			if(threshHold=='' || isNaN(threshHold) || parseInt(threshHold)<=0)
			{
				alert("Please enter valid numeric value for threshhold");
			}
			else
			{
				document.forms[0].submit();
				document.getElementById('file').value="";
			}
		}
		else
		{
			document.forms[0].submit();
			document.getElementById('file').value="";
		}
	}
	
}
function showThreshHold(status)
{
	if(status)
	{
		document.getElementById('threshHoldBlock').style.display="table-row";
	}
	else
	{
		document.getElementById('threshHoldBlock').style.display="none";
	}
}

</script>


<head>
	<LINK href="css/styleSheet.css" type="text/css" rel="stylesheet">
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
						<td align="left" height="2%" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="bulk.empi.matching" /> </span></td>
					</tr>
					<tr >
						<td align="left" valign="top" class="showhide">
							

<table width="100%" border="0" cellpadding="3" cellspacing="0">
	<tr>
		<td colspan="4" align="left" height="10"><%@ include file="/pages/content/common/ActionErrors.jsp" %>
        </td>
	</tr>
	<html:form action="/BulkMatchingFileUpload.do" method="post" enctype="multipart/form-data" >
		<tr>
			<td width="1%" align="center" class="black_ar"><span class="blue_ar_b"></span></td>
            <td width="10%" align="left" class="black_ar"><b></b></td>
            <td width="35%" align="left" valign="middle" class="black_new">
		

			</td>
			<td colspan="2" width="44%" class="black_ar"><span class="blue_ar_b" valign="baseline"></span>
				
			</td>
		</tr>
		<tr>
			<td colspan="5" width="100%" height="15px" align="center" class="black_ar"><span class="blue_ar_b"></span>
			</td>
		</tr>
		<tr>
			<td align="center" class="black_ar"><span class="blue_ar_b"></span>
			</td>
			<td align="left" class="black_ar"><b><bean:message key="bulk.empi.select.file" /></b>
			</td>
			<td align="left" valign="middle" width="20%">
				<input id="file" type="file" name="csvFile" value="Browse" size="34">
				</input>
			</td>
			<td align="left" valign="left" colspan="2">
				<html:button styleClass="blue_ar_b" onclick="onUploadClick()" accesskey="enter" property="">
						<bean:message key="bulk.empi.start.job" />
				</html:button>
			</td>
		</tr>
		<tr>
			<td colspan="5" width="100%" height="15px" align="center" class="black_ar"><span class="blue_ar_b"></span>
			</td>
		</tr>
		<tr id="threshHoldBlock" style="display: true;">
			<td align="center" class="black_ar"><span class="blue_ar_b"></span>
			</td>
			<td align="left" class="black_ar"><b><bean:message key="bulk.empi.threshhold" /> </b>
			</td>
			<td align="left" valign="middle" width="20%">
				<input id="threshHold" type="text" name="threshHold" value="" size="34">
				</input>
			</td>
			<td align="left" valign="left" colspan="2">
				
			</td>
		</tr>
		<tr>
			<td colspan="5" width="100%" height="15px" align="center" class="black_ar"><span class="blue_ar_b"></span>
			</td>
		</tr>
		<tr>
			<td align="center" class="black_ar"><span class="blue_ar_b"></span>
			</td>
			<td align="left" class="black_ar"><b><bean:message key="bulk.empi.job.type" /></b>
			</td>
			<td align="left" colspan="2" valign="middle" width="20%" class="black_ar">
				
				<input  type="radio" name="jobType" id="jobType" value="<%=Constants.BULK_OPERATION_TYPE_MATCH%>"  onclick="showThreshHold(true)" checked="checked"><%=Constants.BULK_OPERATION_TYPE_MATCH%></input>
				<input  type="radio" name="jobType" id="jobType" value="<%=Constants.BULK_OPERATION_TYPE_LOAD%>" onclick="showThreshHold(false)"><%=Constants.BULK_OPERATION_TYPE_LOAD%></input>
				<input  type="radio" name="jobType" id="jobType"  value="<%=Constants.BULK_OPERATION_TYPE_GENERATE%>"  onclick="showThreshHold(false)"><%=Constants.BULK_OPERATION_TYPE_GENERATE%></input>
				
			</td>
			
				
			
		</tr>
	</html:form>
</table>
						</td>
					</tr>
					<tr height="68%" >
						<td height="100%" valign="top">
							<table width="100%" valign="top" height="100%"  cellspacing="0" class="whitetable_bg" border='0'>
								<tr>
									<td align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="bulk.empi.dashboard" /></span></td>
								</tr>
								<tr>
									<td class="black_ar"></td>
								</tr>
								<tr height="100%">
									<td width="100%" height="100%" align="center" class="black_ar">
										
										<iframe id="bulkOperationDashoard" height="100%"name="bulkOperationDashoard" src="BulkMatchingGridDashboardAction.do?" scrolling="auto" frameborder="0" style="width:100%;height:100%;" marginheight='0' marginwidth='0'>
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

<script>

	
</script>