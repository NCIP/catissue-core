<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<script src="jss/queryModule.js"></script>
</head>
<script>
/*resultSetDivObj = document.getElementById('resultSetDiv');
	if(navigator.appName.indexOf("Microsoft")!=-1)
	{

		resultSetDivObj.height = '100%';
	}else
	{
		resultSetDivObj.height = '440';
	}*/
</script>

		<!-- Make the Ajax javascript available -->
		<script type="text/javascript" src="jss/ajax.js"></script> 
<html:errors />
<%
	String formAction = Constants.ViewSearchResultsAction;
	String defineSearchResultsViewAction = Constants.DefineSearchResultsViewAction;
%>
<html:form method="GET" action="<%=formAction%>">
<html:hidden property="stringToCreateQueryObject" value="" />
<table bordercolor="#000000" border="0" width="100%" cellspacing="2" cellpadding="2"  height="100%">
	<tr>
	<td>
	<table border="1" width="100%" cellspacing="0" cellpadding="0" height="100%" id="table1">
	<tr>
		<td>
		<table border="0" width="100%" cellspacing="0" cellpadding="0" height="100%" bordercolor="#000000" id="table2" >
		<tr  height="2%">
			<td height="2%" colspan="5" bgcolor="#000000"><font color="#FFFFFF">&nbsp;&nbsp;Search Data</font></td>
		</tr>
		<tr  height="5%" class="trStyle">
			<td width="20%" height="5%" class="queryModuleTabMenuItem">
				<bean:message key="query.chooseSearchCategory"/>
			</td>

			<td width="20%" height="5%" class="queryTabMenuItemSelected" >
				<bean:message key="query.addLimits"/>
			</td>

			<td width="20%" height="5%" class="queryModuleTabMenuItem" >
				<bean:message key="query.defineSearchResultsViews"/>
			</td>

			<td width="20%" height="5%" class="queryModuleTabMenuItem">
				<bean:message key="query.viewSearchResults"/>
			</td>

			<td width="20%" height="5%" class="queryModuleTabMenuItem">
				<bean:message key="query.dataList"/>
			</td>			
		</tr>
		<tr>
			<td height="60%" valign="top" width="100%" colspan="5">
				<table border="0"  height="100%" width="100%" cellpadding="1" cellspacing="3">
					<tr>
						<td valign="top" width="10%">
						<%@ include file="/pages/content/search/querysuite/ChooseSearchCategory.jsp" %>
						</td>
					
					

					<td valign="top" height="60%">
							<table border="0" width="100%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" height="100%" bordercolorlight="#000000" >
							<tr>
							<td height="80%">
								<table border="1" width="100%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" height="100%" bordercolorlight="#000000">
							
								<tr>
									<td><div id="addLimits" style="overflow:auto; height:100%;width:100%"></div></td>
								</tr>
								</table>
							</td>
							</tr>
							<tr >
								<td height="5px">
								</td>
								</tr>
							<tr>
							<td  height="20%">
							<table border="1" width="100%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" height="100%" bordercolorlight="#000000">
							<tr>																						
												<td height="4%" bgcolor="#EAEAEA" style="border:solid 1px"><font face="Arial" size="2" color="#000000"><b><bean:message key="query.limitSetHeader"/></b></font></td>
											</tr>
											<tr>
											<td height="20%">													
												<div id="queryTableTd" style="overflow:auto; height:100px;width:100%">
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
					<td colspan="5">
					<table border="0" width="100%" cellspacing="0" cellpadding="0" bgcolor="#EAEAEA" height="100%" bordercolorlight="#000000" >
					<tr height="35" valign="center">
					 <td width="2%" valign="center">&nbsp;</td>
						<td valign="center" width="75%"><html:button property="Button"><bean:message key="query.saveButton"/></html:button></td>
						<td  valign="center" align="right"><html:button property="Button" onclick="viewSearchResults()"><bean:message key="query.searchButton"/></html:button></td>
						<td  align="right" valign="center"><html:button property="Button"><bean:message key="query.perviousButton"/></html:button></td>
						<td align="right" valign="center"><html:button property="Button" onclick="defineSearchResultsView()"><bean:message key="query.nextButton"/></html:button>
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