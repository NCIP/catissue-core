<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<head>
<script language="JavaScript">
		function onCheckboxButtonClick(element,dropDownList)
		{
		    if(element.checked==true)
		    { 
		    	document.forms[0].participantID.disabled = false;
      		}
           	else
           	{
	           	document.forms[0].participantID.disabled = true;
      		}
          
		}
</script>		
</head>
<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName;
        String searchFormName = new String(Constants.COLLECTION_PROTOCOL_REGISTRATION_SEARCH_ACTION);

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {						
            formName = Constants.COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION;
            readOnlyValue = false;
        }
        else
        {
            formName = Constants.COLLECTIONP_ROTOCOL_REGISTRATION_ADD_ACTION;
            readOnlyValue = false;
        }
        %>

<html:errors />

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	
	<html:form action="<%=Constants.COLLECTIONP_ROTOCOL_REGISTRATION_ADD_ACTION%>">
		<logic:notEqual name="operation" value="<%=Constants.ADD%>">
			<!-- ENTER IDENTIFIER BEGINS-->
			<br />
			<tr>
				<td>
				<table summary="" cellpadding="3" cellspacing="0" border="0">
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="collectionProtocolReg.searchTitle" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="identifier">
								<bean:message key="collectionProtocolReg.identifier" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="identifier" property="identifier" />
						</td>
					</tr>
					<%
        				String changeAction = "setFormAction('" + searchFormName
                							  + "');setOperation('" + Constants.SEARCH + "');";
			        %>
					<tr>
						<td align="right" colspan="3">
						<table cellpadding="4" cellspacing="0" border="0">
							<tr>
								<td>
									<html:submit styleClass="actionButton" value="Search" onclick="<%=changeAction%>" />
								</td>
							</tr>
						</table>
						</td>
					</tr>

				</table>
				</td>
			</tr>
			<!-- ENTER IDENTIFIER ENDS-->
		</logic:notEqual>


		<!-- NEW Collection Protocol Registration ENTRY BEGINS-->
		<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td><html:hidden property="operation" value="<%=operation%>" /></td>
				</tr>

				<logic:notEqual name="operation" value="<%=Constants.SEARCH%>">
					<tr>
						<td class="formMessage" colspan="3">* indicates a required field</td>
					</tr>
					
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="collectionProtocolReg.title" />
						</td>
					</tr>
					
					<tr>
  			     	   <td class="formRequiredNotice" width="5">*</td>					
					   <td class="formRequiredLabel">
							<label for="name">
								<bean:message key="collectionProtocolReg.protocolTitle" />
							</label>
					   </td>
					   <td class="formField">
							<html:select property="collectionProtocolID" styleClass="formFieldSized" styleId="collectionProtocolID" size="1">
 							    <html:options collection="<%=Constants.PROTOCOL_LIST%>" labelProperty="name" property="value"/>															
						    </html:select>
						    
					    	<html:link page="CollectionProtocol.do?operation=add" styleId="newSite">
		 						<bean:message key="buttons.addNew" />
	 						</html:link>					   
					   </td>
					
					</tr>
					
					<tr>
					   <td class="formRequiredNotice" width="5">&nbsp;</td>					
      	 	           <td class="formRequiredLabel" nowrap>
                    		<html:checkbox property="checkedButton" onclick="onCheckboxButtonClick(this)">
								<label for="participantID">
									<bean:message key="collectionProtocolReg.participantName" />
								</label>
							</html:checkbox>
						</td>
						<td class="formField">
							<html:select property="participantID" styleClass="formFieldSized" styleId="participantID" size="1" disabled="true">
 							    <html:options collection="<%=Constants.PARTICIPANT_LIST%>" labelProperty="name" property="value"/>							
							</html:select>
							<html:link page="Participant.do?operation=add" styleId="newParticipant" >
		 						<bean:message key="buttons.addNew" />
	 						</html:link>					   
							
						</td>
					</tr>

					<tr>
             		    <td class="formRequiredNotice" width="5">*</td>					
						<td class="formRequiredLabel">
							<label for="name">
								<bean:message key="collectionProtocolReg.participantProtocolID" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="participantProtocolID" property="participantProtocolID" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>
	
     				<tr>
                		<td class="formRequiredNotice" width="5">*</td>					
						<td class="formRequiredLabel">
						    
							<label for="name">
								<bean:message key="collectionProtocolReg.participantRegistrationDate" />
							</label>
						</td>
						<td class="formField">
					           <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
					           <html:text styleClass="formDateSized" size="25" styleId="registrationDate" property="registrationDate" readonly="true"/>
						       <a href="javascript:show_calendar('collectionProtocolRegistrationForm.registrationDate');">
							         <img src="images\calendar.gif" width=24 height=22 border=0>
						      </a>
					 </td>
					</tr>
	
					<tr>
						<td align="right" colspan="3">
						<%
        					String changeAction = "setFormAction('" + formName + "');";
				        %> 
						
						<!-- action buttons begins -->
						<table cellpadding="4" cellspacing="0" border="0">
							<tr>
								<td>
									<html:submit styleClass="actionButton" value="Submit" onclick="<%=changeAction%>" />
								</td>
								<td>
									<html:reset styleClass="actionButton" />
								</td>
							</tr>
						</table>
						<!-- action buttons end --></td>
					</tr>
					
				</logic:notEqual>
			</table>

			</td>
		</tr>

		<!-- NEW Collection Protocol Registration ENTRY ends-->
	</html:form>
</table>