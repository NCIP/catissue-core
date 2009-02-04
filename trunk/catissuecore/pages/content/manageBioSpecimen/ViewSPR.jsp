<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%@ page import="java.util.ArrayList"%>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<script src="jss/script.js" type="text/javascript"></script>

<% 
	String id = (String)request.getAttribute(Constants.SYSTEM_IDENTIFIER);

	String surgicalPathologyReport = (String)request.getAttribute(Constants.LINKED_DATA);

	String editTabLink = (String)request.getAttribute(Constants.EDIT_TAB_LINK);

%>
<head>
</head>

<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
	
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
							 	<td class="formMessage" colspan="4">* indicates a required field</td>
							 </tr>
			
							<tr>
							 	<td class="formTitle" height="20" colspan="4"> SURGICAL PATHOLOGY REPORT for SPECIMEN COLLECTION GROUP: <%=id%></td>
							</tr>
			
							<tr>
								<td class="tabField" colspan="4">
									<html:textarea cols="111" rows="20" styleClass="tabFieldSized"  styleId="comments" property="" value="<%=surgicalPathologyReport%>" readonly="true" />
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