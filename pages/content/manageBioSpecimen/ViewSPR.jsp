<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<script src="jss/script.js" type="text/javascript"></script>

<% 
	String systemIdentifier = (String)request.getAttribute(Constants.SYSTEM_IDENTIFIER);

	String surgicalPathologyReport = (String)request.getAttribute("integrationData");

	request.setAttribute("actionForm", request.getAttribute("actionForm"));

%>
<head>
</head>

<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
	
<html:form action="ViewSurgicalPathologyReport.do">

	<table summary="" cellpadding="0" cellspacing="0" border="0" height="20" class="tabPage" width="600">
		<tr>
			<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" >
				Edit
			</td>
			
			<td height="20" class="tabMenuItemSelected" onclick="document.location.href='ViewSPR.do'">
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
							<html:hidden property="systemIdentifier" value="<%=systemIdentifier%>" />
						</td>

					 </tr>
					<!-- VIEW SURGICAL PATHOLOGY REPORT BEGINS-->
				    <tr><td>
						<table summary="" cellpadding="3" cellspacing="0" border="0"  width="100%">
							<tr>
							 	<td class="formMessage" colspan="4">* indicates a required field</td>
							 </tr>
			
							<tr>
							 	<td class="formTitle" height="20" colspan="4"> SURGICAL PATHOLOGY REPORT for SPECIMEN COLLECTION GROUP: <%=systemIdentifier%></td>
							</tr>
			
							<tr>
								<td class="tabField" colspan="4">
									<html:textarea cols="111" rows="20" styleClass="tabFieldSized"  styleId="comments" property="" value="<%=surgicalPathologyReport%>" readonly="true" />
								</td>
							</tr>
							<tr>
								<td align="right" colspan="3">
									<!-- action buttons begins -->
									<table cellpadding="4" cellspacing="0" border="0">
										<tr>
											<td>
												<html:submit styleClass="actionButton">
													<bean:message  key="buttons.submit" />
												</html:submit>
											</td>
											<%-- td>
												<html:reset styleClass="actionButton" >
													<bean:message  key="buttons.reset" />
												</html:reset>
											</td --%>
										</tr>
									</table>
									<!-- action buttons end -->
								</td>
							</tr>
						</table>
					</td></tr>
					<!-- VIEW SURGICAL PATHOLOGY REPORT ENDS-->
				</table>
			</td>
		</tr>
	</table>
</html:form>