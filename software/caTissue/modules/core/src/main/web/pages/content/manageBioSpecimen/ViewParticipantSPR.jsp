<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.Iterator"%>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<script src="jss/script.js" type="text/javascript"></script>

<% 
	String id = (String)request.getAttribute(Constants.SYSTEM_IDENTIFIER);

	String editTabLink = (String)request.getAttribute(Constants.EDIT_TAB_LINK);

	ArrayList integrationDataList=(ArrayList)request.getAttribute(Constants.LINKED_DATA);

	String surgicalPathologyReport=(String)request.getAttribute("firstReport");

	List reportIdList=(List)request.getAttribute("reportIdList");

	List reportList = (List)request.getAttribute("reportList");

	int noOfReports = reportList.size();

	String firstReportId=(String)request.getAttribute("firstReportId");

%>
<head>
<script language="JavaScript">

	var reportArray = new Array(<%=noOfReports%>);

	<% int reportNumber;
		for(reportNumber=0; reportNumber<reportList.size(); reportNumber++) 
		{
	%>
		reportArray[<%=reportNumber%>]="<%=reportList.get(reportNumber)%>";
	<%  }
	%>

	function showReport(reportId)
	{
		var reportNumber = reportId;

		document.forms[0].reportBox.value = reportArray[reportNumber];
	}

</script>
</head>

<html:form action="ViewSpecimenCollectionGroupSPR.do">

	<table summary="" cellpadding="0" cellspacing="0" border="0" height="20" class="tabPage" width="600">
		<tr>
			<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="addNewAction('<%=editTabLink%>')" >
				Edit
			</td>
			
			<td height="20" class="tabMenuItemSelected">
				View Surgical Pathology Report
			</td>
			
			<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()">
				View Clinical Annotations
			</td>

			<td width="450" class="tabMenuSeparator" colspan="3">&nbsp; </td>
		</tr>
		<tr>
			<td class="tabField" colspan="6">
				<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
					<tr>
						<td>
							<html:hidden property="<%=Constants.OPERATION%>" value="edit"/>
							<html:hidden property="id" value="<%=id%>" />
						</td>
					 </tr>
					<!-- VIEW SURGICAL PATHOLOGY REPORT BEGINS-->
				    <tr><td>
						<table summary="" cellpadding="3" cellspacing="0" border="0"  width="100%">
							<tr>
							 	<td class="formMessage" colspan="2"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
							 </tr>
			
							<tr>
							 	<td class="formTitle" height="20" colspan="4"> Surgical Pathology Report(s) for Participant: <%=id%></td>
							</tr>

							<tr>
								<td class="columnHeading">Number</td>
								<td class="columnHeading" colspan="3">Report</td>
							</tr>

							<tr>
								<td class="tabFieldSized5">
									<html:select property="<%=Constants.SYSTEM_IDENTIFIER%>" styleClass="tabFieldSized5" styleId="collectionProtocolId" 
									size="25" disabled="" value="0" onchange="showReport(this.value)" >
										<html:options collection="reportIdList" labelProperty="name" property="value"/>
									</html:select>
								</td>
								<td class="tabField"  colspan="3">
									<html:textarea cols="100" rows="25" styleClass="tabFieldSized"  styleId="comments" property="reportBox" value="<%=surgicalPathologyReport%>" readonly="true" />
								</td>
							</tr>
							<tr><td>&nbsp;</td></tr>
						</table>
					</td></tr>
					<!-- VIEW SURGICAL PATHOLOGY REPORT ENDS-->
				</table>
			</td>
		</tr>
	</table>
</html:form>