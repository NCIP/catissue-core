<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.cab2b.client.ui.query.ClientQueryBuilder"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<script src="jss/queryModule.js"></script>
<script type="text/javascript" src="jss/ajax.js"></script> 
</head>
<body>
<html:errors />
<%

	String formAction = Constants.SearchCategory;
	String defineSearchResultsViewAction = Constants.DefineSearchResultsViewAction;
%>
<html:form method="GET" action="<%=formAction%>">
<html:hidden property="stringToCreateQueryObject" value="" />
<html:hidden property="nextOperation" value="" />
<table bordercolor="#000000" border="0" width="100%" cellspacing="2" cellpadding="2"  height="100%">
	<tr>
	<td>
	<table border="1" width="100%" cellspacing="0" cellpadding="0" height="100%" id="table1">
	<tr>
		<td>
		<table border="0" width="100%" cellspacing="0" cellpadding="0" height="100%" bordercolor="#000000" id="table2" >		
		<tr  height="5%" class="trStyle">
			<td width="25%" height="5%" class="queryTabMenuItemSelected" >
				<bean:message key="query.addLimits"/>
			</td>

			<td width="25%" height="5%" class="queryModuleTabMenuItem" >
				<bean:message key="query.defineSearchResultsViews"/>
			</td>

			<td width="25%" height="5%" class="queryModuleTabMenuItem">
				<bean:message key="query.viewSearchResults"/>
			</td>

			<td width="25%" height="5%" class="queryModuleTabMenuItem">
				<bean:message key="query.dataList"/>
			</td>			
		</tr>
		<tr>
			<td height="60%" valign="top" width="100%" colspan="4">
				<table border="0"  height="100%" width="100%" cellpadding="1" cellspacing="3">
					<tr>
						<td valign="top" width="10%">
						<%@ include file="/pages/content/search/querysuite/ChooseSearchCategory.jsp" %>
						</td>
					
					

					<td valign="top" height="60%">
							<table border="0" width="100%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" height="100%" bordercolorlight="#000000" >
								
							<tr>
							<td height="60%">
								<table border="1" width="100%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" height="100%" bordercolorlight="#000000">
							
												<tr  id="rowMsg">
													<td id="validationMessagesSection"  class='validationMessageCss'>
														<div id="validationMessagesRow"   class='validationMessageCss' style="overflow:auto; width:820; height:50;display:none"></div>
													</td>
												</tr>
									
								<tr>
									<td height="350" width="100%" id="addLimitsSection"><div id="addLimits" style="overflow:auto; height:100%;width:100%"></div></td>
								</tr>
								</table>
							</td>
							</tr>
							<tr >
								<td height="5px">
								</td>
								</tr>
							<tr>
							<td  height="40%" valign="top">
								<table border="1" width="100%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" height="100%" bordercolorlight="#000000">
									<tr>																						
										<td height="4%" bgcolor="#EAEAEA" style="border:solid 1px"><font face="Arial" size="2" color="#000000"><b><bean:message 	key="query.limitSetHeader"/></b></font></td>
									</tr>
									<tr>
										<td height="36%">											
											<div id="queryTableTd" style="overflow:auto;height:100%;width:100%"">
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
													<PARAM name="isForView" value="false">">
													

													
													<PARAM name = "<%=Constants.APPLET_SERVER_URL_PARAM_NAME%>" value="<%=Constants.APPLET_SERVER_HTTP_START_STR%><%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>">
												</APPLET>
											</div>
										</td>
									</tr>
								</table>
							</td>
							</tr>
							</table>
						</td>
						
					</tr>
					
				</table>
				
			</td>
		</tr>
	
			</table>
			</td>
			</tr>
			<tr bgcolor="#DFE9F3">
					<td colspan="4">
					<table border="0" width="100%" cellspacing="0" cellpadding="0" bgcolor="#EAEAEA" height="100%" bordercolorlight="#000000" >
					<tr height="35" valign="center">
					 <td width="2%" valign="center">&nbsp;</td>
						<td valign="center" width="75%"><html:button property="Button"><bean:message key="query.saveButton"/></html:button></td>
						<td  valign="center" align="right"><html:button property="Button" onclick="viewSearchResults()"><bean:message key="query.searchButton"/></html:button></td>
						<td  align="right" valign="center"><html:button property="Button"><bean:message key="query.previousButton"/></html:button></td>
						<td align="right" valign="center"><html:button property="Button" onclick="saveClientQueryToServer()"><bean:message key="query.nextButton"/></html:button>
						</td>
						<td width="2%">&nbsp;</td>
					</tr>
				</table>
				</td>
					</tr>
			</table>
			</td></tr>

</table>
</html:form>
</body>
</html> 