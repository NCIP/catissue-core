<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<!--
	Patch ID: Bug#4129_2
	Description: Changes to support the restriction functionality for technician as well.
-->
<%
	String scgId = (String)request.getAttribute(Constants.SPECIMEN_COLLECTION_GROUP_ID);
	Integer numberOfSpecimen = (Integer)request.getAttribute(Constants.NUMBER_OF_SPECIMEN);
	String restrictSCGCheckbox = (String)request.getAttribute(Constants.RESTRICT_SCG_CHECKBOX);
%>
<head>
	<script src="jss/script.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="JavaScript">
	function AddSpecimen()
	{
		document.forms[0].action = "CPQueryNewSpecimen.do?operation=add&pageOf=pageOfNewSpecimenCPQuery&menuSelected=15&virtualLocated=true&<%=Constants.SPECIMEN_COLLECTION_GROUP_ID%>=<%=scgId%>";
		document.forms[0].submit();
	}
	function AddMultipleSpecimen()
	{
		var enteredValue = document.getElementById("numberOfSpecimen").value;	
		if(isNaN(enteredValue) || enteredValue < 1)
		{
			alert("Please enter valid number of Specimens");			
			return false;
		}
		
		document.forms[0].action = "CPQueryNewMultipleSpecimenAction.do?method=showMultipleSpecimen&menuSelected=15&button=multipleSpecimen&operation=add&pageOf=pageOfMultipleSpecimenCPQuery&<%=Constants.SPECIMEN_COLLECTION_GROUP_ID%>=<%=scgId%>&numberOfSpecimens="+enteredValue;
		document.forms[0].submit();
	}
	function disablebuttons()
	{
		var enteredValue = document.getElementById("numberOfSpecimen").value;
		if(isNumeric(enteredValue))
		{
			var submitAndAddButton = document.getElementById("submitAndAdd");
			var submitAndAddMultipleButton =  document.getElementById("submitAndAddMultiple");
			
			if(enteredValue > 1)
			{			
				submitAndAddButton.disabled = true;
				submitAndAddMultipleButton.disabled = false;
			}
			else
			{			
				submitAndAddButton.disabled = false;
				submitAndAddMultipleButton.disabled = false;
			}
		}
		else
		{
			alert('Enter a valid numeric value');
			return false;
		}
	}
	function initializeCPTechForm()
	{
		var restrictCheckbox = document.getElementById("restrictSCGCheckbox");
		restrictCheckbox.checked = <%=restrictSCGCheckbox%>;
		
		var numberOfSpecimen = document.getElementById("numberOfSpecimen");
		numberOfSpecimen.value = <%=numberOfSpecimen%>;

		disablebuttons();
	}
	</script>
</head>
<body onload="initializeCPTechForm();disablebuttons()">
	<%@ include file="/pages/content/common/ActionErrors.jsp" %>
	<html:form action="CPQueryNewSpecimen.do?operation=add&pageOf=pageOfNewSpecimenCPQuery&menuSelected=15&virtualLocated=true<%=Constants.SPECIMEN_COLLECTION_GROUP_ID%>=<%=scgId%>">
		<table cellpadding="4" cellspacing="0" border="0">
			<tr>
				<td>
					<table>
						<tr>
							<td colspan="2">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="2">
								<!-- For Multiple Specimen-----Ashish -->
								<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
									<tr>
										<td>
											<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
												<tr>
													<td class="formTitle" " colspan="6" height="20">
														<bean:message key="multipleSpecimen.mainTitle" />
													</td>
												</tr>
												<tr>
													<td class="formLabel" colspan="2" style="border-left:1px solid #5C5C5C;">
														<bean:message key="multipleSpecimen.numberOfSpecimen" />
													</td>
													<td class="formField" colspan="3">
														<html:text styleClass="formFieldSized5" maxlength="50" size="30" styleId="numberOfSpecimen" property="numberOfSpecimen" onkeyup="disablebuttons()"/>
													</td>
												</tr>
												<tr>
													<td class="formFieldNoBorders" colspan="3" valign="center">
														&nbsp;<html:checkbox styleId="restrictSCGCheckbox" property="restrictSCGCheckbox" value="true">
															<bean:message key="specimen.checkboxLabel"/>
														</html:checkbox>
													</td>
												</tr>
												<tr>
													<td>&nbsp;</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td nowrap class="formFieldNoBorders">
								<html:button styleClass="actionButton" property="submitPage" title="Add Specimen" 
									value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[1][0]%>" 
									styleId = "submitAndAdd" onclick="AddSpecimen();">
					     		</html:button>
							</td>
							<td nowrap class="formFieldNoBorders">
								<html:button styleClass="actionButton" styleId="submitAndAddMultiple" property="submitPage" title="Add Multiple Specimen" 
									value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[2][0]%>" onclick="AddMultipleSpecimen();">
					     		</html:button>
							</td>
						</tr>			
					</table>
				</td>					
			</tr>
		</table>
	</html:form>
</body>