<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
	
	<%@ page isELIgnored ="false" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="edu.wustl.common.labelSQLApp.form.CPDashboardForm"%>
<%@ taglib uri="/WEB-INF/scheduler.tld" prefix="scheduler"%>


<%@page import="java.util.*"%>
<%@page import="java.util.HashMap"%>


<head>
<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />
<link rel="STYLESHEET" type="text/css" href="css/alretmessages.css">
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<script src="jss/caTissueSuite.js"></script>
<script src="jss/common.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<script src="jss/calendarComponent.js"></script>
<link rel="stylesheet" type="text/css"
	href="css/advQuery/styleSheet.css" />
<link rel="stylesheet" type="text/css"
	href="css/advQuery/catissue_suite.css" />
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>

<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxtabbar.css">
<script  src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script  src="dhtmlx_suite/js/dhtmlxtabbar.js"></script>

<script language="JavaScript" type="text/javascript" src="jss/keyBoardShortCutPlugin.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/KeyBoardMenuCode.js"></script>
	</head>

<script>
var noReports = false;

function getResults(key) 
{
	 if (key!=0) 
	 {
		var cpId = "${param.cpSearchCpId}";
		var request = newXMLHTTPReq();
		var url = "QueryResultAjaxAction.do";//AJAX action class
		request.onreadystatechange = getReadyStateHandler(request,updatePage,true);//AJAX handler
		request.open("POST", url, true);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		var dataToSend = "labelSQLId=" + key+"&cpId="+cpId;
		request.send(dataToSend);
	 }
}

   function updatePage(responseString)
{	
	//call back method which gets the response string containing association id and query result. 
	var responseArray = responseString.split(",");
	document.getElementById('img'+responseArray[0]).style.display ="block";
	document.getElementById(responseArray[0]).innerHTML = responseArray[1];//ajax call results
}

</script>


<%
boolean isWhite = false;

%>
<script>
	var currentCsId=0;
	var isSysDashboard = false;
	function initSchedulerProp()
	{
		currentCsId="${param.cpSearchCpId}";
		if(currentCsId==null || currentCsId=='null' ||  currentCsId=="undefined" || currentCsId==0)
		{
		currentCsId=0;
		isSysDashboard=true;
		}
	}
	function updateHelpURL()
	{
		var URL="<%=HelpXMLPropertyHandler.getValue("edu.wustl.catissuecore.actionForm.CPSearchForm")%>";
		return URL;
	}	
	initSchedulerProp();
</script>


<body onLoad="initCalendarUI()";>
<div id="a_tabbar" style="width:100% ;height: 100%;background-color: white;">
	<div id="html_1" style="width:100% ;height: 100%;overflow: auto;background-color: white; padding: 5px;">
<table width="100%" border="0" cellpadding="3" cellspacing="0">
	
	<tr>
	<td width="100%" colspan="2">
	<c:if test="${requestScope.isSyncOn == 'true'}">
	<div>
				   <table width="100%" border="0" cellpadding="0" cellspacing="0">
					
					<td align="right"  style="border:1px solid #ea6c15;vertical-align:text-top;" bgcolor="#FFF0F0" >
					   <table width="100%" cellpadding="0" cellspacing="0">
						<td width="1%"></td>					   	
						<td width="2%"> <img src="images/uIEnhancementImages/Info.png" width="24" height="24"/></td>
						<td width="1%"></td>
					  <td>	
					  	<div>
						  <span style="font-family:Verdana;font-size:11px;color: red;font-weight:bold;vertical-align:tax-top;margin:0px;">
							Registration is disabled  since syncronization has started.
						  </span>
						</div>
					 </td>
				     </table>
				  </table>
				  
				</div>
	</c:if>
	</td>
	</tr>
	
	<table width="100%" cellpadding='0' cellspacing='0' border='0'>

		<tr style="height: 100%;">
			<td>

			<table cellpadding='2' cellspacing='0' border='0' width='100%'
				class='savedqueries'>

				<c:forEach items="${labelQueryResultList}" var="labelQueryResult"
					varStatus="labels">

					<%
									String cssClass = new String();
									if(isWhite == false)
									{
										cssClass = "white";
										isWhite = true;
									}
									else
									{
										cssClass="bgImageForColumns";
										isWhite=false;
									}
								%>
					<tr class="<%=cssClass%>">

						<c:choose>

							<c:when test="${labelQueryResult.value eq 0}">
								
					</tr>
					<tr>
						<td><br>
						</td>
					</tr>
					<!-- Condition for dashboard items group heading -->
					<tr class="<%=cssClass%>">
						<td align="left" colspan="3" bgcolor="#d5e8ff"
							style="padding-left: 0.2em; * padding-left: 5px; font-size: 1em; font-family: verdana; font-weight: bold;">
						<span class="blue_ar_b">${labelQueryResult.key}</span></td>

						</c:when>

						<c:otherwise>
							<!-- Condition for dashboard items and result -->
							<td align="left"
								style="padding-left: 0.2em; * padding-left: 5px; font-size: 1em; font-family: verdana;">
							${labelQueryResult.key}</td>

							<td valign="center" height='20' width="40%" align="left"
								style="padding-left: 0.2em; * padding-left: 5px; font-size: 1em; font-family: verdana;">
							<label id='${labelQueryResult.value}'><img
								src="images/bluespinner.gif" id='img${labelQueryResult.value}'
								alt="Loading" width="18" height="15" align="absmiddle" /> <script>
										getResults(${labelQueryResult.value})
									</script></td>
						</c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
			</table>

			</td>
		</tr>
	</table>

</table>
</div>

<div id="html_2">
		<div style="width: 100%; height: 100%">
<html:form
	action="/GenerateCPReports.do" name="hsReports"
	type="edu.wustl.common.actionForm.ReportForm"
	styleId="reportForm">
	<table style="width:100%; padding: 5px">
		<tr class="tr_bg_blue1">
			<td><span class="blue_ar_b">Reports</span></td>
		</tr>
		<tr>
			<td>
				<c:if test="${empty reportNameList}" >
					<div class="alert alert-info" id="success" style="display:block"><b><bean:message key="report.notification"
                                                                                                           arg0="<a href='https://catissueplus.atlassian.net/wiki/x/DgA7' target='_NEW'>Click here</a>"/></b></div>
                 </c:if>
                                        
										
			</td>
		</tr>
		<tr>
		<td width="100%">
			<table style="padding: 5px; width:100%;">
				<tr>
					<td class="black_new" style="padding-right: 50px">Name:</td>
					<td>
						<html:select property="reportName"
								styleClass="formFieldSizedNew" styleId="reportName" size="1">
								<html:options collection="reportNameList"
									labelProperty="name" property="value" />
						</html:select>
					</td>
				</tr>
				<tr><td></td></tr>
				<tr>
					<td class="black_new">Duration:</td>
					<td>
						<html:text property="fromDate" styleId="fromDate1" styleClass="black_new" style="width: 100px"/>
						<span class="grey_ar_s capitalized"> [<bean:message key="date.pattern" />]</span>
					</td>
					<td align="center"  class="black_new" width="10%">
							To:
					</td>
					<td width="65%">
						<html:text property="toDate" styleId="toDate1" styleClass="black_new" style="width: 100px"/>
						<span class="grey_ar_s capitalized"> [<bean:message key="date.pattern" />]</span>
					</td>
				</tr>
				<tr><td></td></tr>				
				<tr>
					<td></td>
					<td>
						<input type="button" class="blue_ar_b" id="downloadBtn" value="Download" onClick="downloadReport();">
						<c:if test="${empty reportNameList}" >
							<script>
							document.getElementById("downloadBtn").disabled=true;
							</script>
						</c:if>
						
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	</html:form>
</div>
	</div>
	<div id="html_3">
	<div style="width: 100%;height: 510px; overflow: auto">
	
 <scheduler:scheduler 
	scheduleType="edu.wustl.common.scheduler.domain.ReportSchedule"
 	dropDownURL="CommonScheduleAction.do?type=populateSavedReportsDropDown"
  	userDropDownURL="CommonScheduleAction.do?type=populateUsers"
  	captionName="Report"
  	/>
		</div>
	</div>
	</div> 
<script>
var isSchedulerLoaded = false;
var toDate;
var fromDate;

function initCalendarUI()
{
	toDate= doInitCalendar("toDate1",false,'${uiDatePattern}');
	fromDate= doInitCalendar("fromDate1",false,'${uiDatePattern}');	
}

function downloadReport()
{
	var dateMessage="MM-DD-YYYY";
	if(document.getElementById("toDate1").value==dateMessage || document.getElementById("fromDate1").value==dateMessage)
	{
		document.getElementById("toDate1").value="";
		document.getElementById("fromDate1").value="";
	}
	var demoForm=document.getElementById("reportForm");
	demoForm.submit();
}

function loadUI()
{
	tabbar = new dhtmlXTabBar("a_tabbar", "top",25);
	tabbar.setSkin('default');
	tabbar.setImagePath("dhtmlx_suite/imgs/");
	tabbar.setSkinColors("#FFFFFF", "#FFFFFF");
	
	tabbar.addTab("a1",'<span style="position:relative;right:60px; font-size:14px"> Dashboard </span>', "200px");
	tabbar.addTab("a2",'<span style="position:relative;right:68px; font-size:14px"> Reports </span>', "200px");
	tabbar.addTab("a3",'<span style="position:relative;right:40px; font-size:14px"> Report Scheduler </span>', "200px");
	tabbar.setContent("a1", "html_1");
	tabbar.setContent("a2", "html_2");
	tabbar.setContent("a3", "html_3");
	tabbar.setTabActive("a1");
	tabbar.attachEvent("onSelect", function(id,last_id){
        if(id=="a3" && isSchedulerLoaded==false)
        	{
	        	loadFormUI();
	        	isSchedulerLoaded=true;
        	}
        return true;
    });
}
loadUI();
</script>

</body>
