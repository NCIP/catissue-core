<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXTree.css">
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<script src="jss/queryModule.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtml_comp/jss/dhtmXTreeCommon.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtml_comp/jss/dhtmlXTree.js"></script>

</head>
<body>
<!-- Make the Ajax javascript available -->
<script type="text/javascript" src="jss/ajax.js"></script> 
<html:errors />
<%
	String formAction = Constants.ViewSearchResultsAction;
	String defineSearchResultsViewAction = Constants.DefineSearchResultsViewAction;
	Map treesMap = (Map) request.getAttribute("treesMap");
%>
<html:form method="GET" action="<%=defineSearchResultsViewAction%>">
<html:hidden property="currentPage" value="prevToAddLimits"/>
<table bordercolor="#000000" border="0" width="100%" cellspacing="2" cellpadding="2"  height="100%">
	<tr>
		<td>
			<table border="1" width="100%" cellspacing="0" cellpadding="0" height="100%" id="table1">
				<tr>
					<td>
						<table border="0" width="100%" cellspacing="0" cellpadding="4" height="100%" bordercolor="#000000" id="table2" >
							<tr  height="10" class="trStyle">
								
								<td width="20%" class="queryModuleTabMenuItem" >
									<bean:message key="query.addLimits"/>
								</td>

								<td width="20%" class="queryTabMenuItemSelected" >
									<bean:message key="query.defineSearchResultsViews"/>
								</td>

								<td width="20%" class="queryModuleTabMenuItem">
									<bean:message key="query.viewSearchResults"/>
								</td>

								<td width="20%" class="queryModuleTabMenuItem">
									<bean:message key="query.dataList"/>
								</td>			
							</tr>
							<tr height="1">
								<td></td>
							</tr>
							<tr valign="top"  height="100%" width="100%">
								<td colspan="4" valign="top" height="100%" width="100%">
									<table border="1" cellspacing="0" cellpadding="0" valign="top"  height="100%" width="100%">
									<tr valign="top">
										<td valign="top" height="500" colspan="4" width="100%">
											<!--		tiles insert -->
											<tiles:insert attribute="content"></tiles:insert>
										</td>
									</tr>
									</table>
								</td>
							</tr>
						</table>						
					</td>
				</tr>
		
				<tr bgcolor="#EAEAEA">
					<td colspan="4">
						<table border="0" width="100%" cellspacing="0" cellpadding="0" bgcolor="#EAEAEA" height="100%" bordercolorlight="#000000" >
							<tr height="35" valign="center">
								<td width="2%" valign="center">&nbsp;</td>
								<td valign="center" width="75%"><html:button property="Button"><bean:message key="query.saveButton"/></html:button></td>
								<td  valign="center" align="right"><html:button property="Button"  onclick="viewSearchResults()"><bean:message key="query.searchButton"/></html:button></td>
								<td  align="right" valign="center"><html:button property="Button" onclick="previousFromDefineResults()"><bean:message key="query.previousButton"/></html:button></td>
								<td align="right" valign="center"><html:button property="Button" onclick="viewSearchResults()"><bean:message key="query.nextButton"/></html:button>
								</td>
								<td width="2%">&nbsp;</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</html:form>
</body>
</html> 