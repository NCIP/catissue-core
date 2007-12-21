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
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
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
<html:form method="GET" action="<%=defineSearchResultsViewAction%>" style="margin:0;padding:0;">
<html:hidden property="currentPage" value="prevToAddLimits"/>
<input type="hidden" name="isQuery" value="true">
<table bordercolor="#000000" border="0" width="100%" cellspacing="0" cellpadding="0"  height="100%" >

	<tr>
		<td valign="top">
			<table border="0" width="100%" cellspacing="0" cellpadding="0" height="100%" id="table1" >
				<tr>
					<td valign="top">
						<table border="0" width="100%" cellspacing="0" cellpadding="0" height="100%" bordercolor="#000000" id="table2" >
							<tr  height="10" >
								
								<td width="33%" align="center" class="bgWizardImage">
									<img src="images/1_inactive.gif" /> <!-- width="118" height="25" /-->
								</td>
								<td width="33%" align="center" class="bgWizardImage">
									<img src="images/2_active.gif" /> <!-- width="199" height="38" /-->
								</td>
								<td width="33%" align="center" class="bgWizardImage">
									<img src="images/3_inactive.gif" /> <!--  width="139" height="38" /-->
								</td>
							</tr>
							<tr height="1">
								<td></td>
							</tr>
							<tr valign="top"  height="100%" width="100%">
								<td colspan="4" valign="top" height="100%" width="100%">
									<table border="0" cellspacing="0" cellpadding="0" valign="top"  height="100%" width="100%">
									<tr valign="top">
										<td valign="top" height="600px" colspan="4" width="100%">
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
					<td colspan="4" valign="top">
						<table border="0" width="100%" cellspacing="0" cellpadding="0"  height="100%" background="images/bot_bg_wiz.gif">
							<tr height="35" valign="center">
								<td width="2%" valign="center" >&nbsp;</td>
								<td valign="top" width="6%" >
									<%--<html:button property="saveButton" onclick="validateQuery('save');">
										<bean:message key="query.saveButton"/>
									</html:button>--%>
									<img src="images/b_save.gif" width="51" height="25" hspace="3" vspace="3" onclick="validateQuery('save');" />
								</td>								
								<td  valign="top" align="left" width="70%" >
									<%--<html:button property="Button"  onclick="validateQuery('search');">
										<bean:message key="query.searchButton"/>
									</html:button>--%>
									<img src="images/b_search.gif" width="56" height="25" hspace="3" vspace="3" onclick="validateQuery('search');" />
								</td>
								<td  align="right" valign="top" >
									<%--<html:button property="Button" onclick="previousFromDefineResults()">
										<bean:message key="query.previousButton"/>
									</html:button>--%>
									<img src="images/b_prev.gif" width="72" height="25" hspace="3" vspace="3" onclick="previousFromDefineResults()"/>
								</td>
								<td align="right" valign="top" >
									<%--<html:button property="Button" onclick="validateQuery('search');">
										<bean:message key="query.nextButton"/>
									</html:button>--%>
									<img src="images/b_next.gif" width="51" height="25" hspace="3" vspace="3" onclick="validateQuery('search');" />
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