<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="edu.wustl.labelSQLApp.form.CPDashboardForm"%>


<%@page import="java.util.*"%>
<%@page import="java.util.HashMap"%>


<link rel="stylesheet" type="text/css"
	href="css/advQuery/styleSheet.css" />
<link rel="stylesheet" type="text/css"
	href="css/advQuery/catissue_suite.css" />
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>

<script>

 function getResults(key) 
   {
	 if (key!=0)
	 {
		var request = newXMLHTTPReq();
		var url = "QueryResultAjaxAction.do";//AJAX action class
		request.onreadystatechange = getReadyStateHandler(request,updatePage,true);//AJAX handler
		request.open("POST", url, true);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		var dataToSend = "labelSQLAssocID=" + key;
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

Object formObj = request.getAttribute("cpDashboardForm");
boolean isWhite = false;
String displayStyle= "display:block";

CPDashboardForm form = (CPDashboardForm)formObj;

HashMap<String,Long> labelQueryListMap = form.getDisplayNameAndAssocMap();

HttpSession newSession = request.getSession();

if(labelQueryListMap != null)
{
	newSession.setAttribute("labelQueryResultList",labelQueryListMap);
	
}
else
{
	newSession.setAttribute("labelQueryResultList",null);
}
if(labelQueryListMap.size() == 0)
{
	displayStyle = "display:none";
}
else
{
	displayStyle = "display:block";
}

%>

<body>

<table width="100%" border="0" cellpadding="3" cellspacing="0">

	<tr valign="center" class='bgImage'>
		<td width="17%">&nbsp; <img src="images/ic_saved_queries.gif"
			id="labelSQLMenu" alt="CP Dashboard" width="38" height="44"
			hspace="5" align="absmiddle" /><span class="blue_ar_b"> <bean:message
			key="dashboard.title" /></span></td>
		<td align="left" width="20%" class='bgImage'></td>
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

</body>
