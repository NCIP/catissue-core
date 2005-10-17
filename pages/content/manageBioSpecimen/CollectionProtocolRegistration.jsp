<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm"%>

<script src="jss/script.js" type="text/javascript"></script>
<%
	   		Object obj = request.getAttribute("collectionProtocolRegistrationForm");
			CollectionProtocolRegistrationForm form =null;
	
			if(obj != null && obj instanceof CollectionProtocolRegistrationForm)
			{
				form = (CollectionProtocolRegistrationForm)obj;
			}	
%>
<head>
<script language="JavaScript">
		function onCheckboxButtonClick(element,dropDownList)
		{
			// changes as per bug 287
			var row = document.getElementById("row1");
			var cell1 = row.cells[0];
			var cell2 = row.cells[1];
			
		    if(element.checked==true)
		    { 
		    	cell1.innerHTML=" &nbsp; ";
			    cell2.className="formLabel";
			    
		    	document.forms[0].participantID.disabled = false;
      		}
           	else
           	{
	           	cell1.innerHTML="*";
	           	cell2.className="formRequiredLabel";
	           	
	           	document.forms[0].participantID.disabled = true;
      		}
		}
		
		// for add new SpecimenCollectionGroup
		function onAddSpecimenCollectionGroup(element)
		{
			var identifier = "";
			<%
				if(form != null)
				{
			%>
			identifier = "<%=form.getSystemIdentifier()%>";
			<%
				}
			%>
			var action = "/catissuecore/SpecimenCollectionGroup.do?operation=add&pageOf=pageOfSpecimenCollectionGroup"+"&cprId=" + identifier;
			document.forms[0].action = action;
			document.forms[0].submit();
		}
</script>		
</head>
<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
        String appendingPath = "/CollectionProtocolRegistration.do?operation=add&pageOf=pageOfCollectionProtocolRegistration";
		if (reqPath != null)
			appendingPath = reqPath + "|/CollectionProtocolRegistration.do?operation=add&pageOf=pageOfCollectionProtocolRegistration";
        
        String formName;

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
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action="<%=formName%>">
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	
		<!-- NEW Collection Protocol Registration ENTRY BEGINS-->
		<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td><html:hidden property="operation" value="<%=operation%>" /></td>
					<td><html:hidden property="systemIdentifier"/>
					<html:hidden property="redirectTo" value="<%=reqPath%>"/>
					</td>
				</tr>

				<tr>
					<td class="formMessage" colspan="3">* indicates a required field</td>
				</tr>
					
				<tr>
					<td class="formTitle" height="20" colspan="3">
						<bean:message key="collectionProtocolReg.title" />
						<%
							if(operation.equals(Constants.EDIT))
							{
						%>
						&nbsp;<bean:message key="for.identifier"/>&nbsp;<bean:write name="collectionProtocolRegistrationForm" property="systemIdentifier" />
						<%
							}
						%>
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
					<%
						String url1 = "/CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol";
						String onClickPath = "changeUrl(this,'"+appendingPath+"')";
					%>
				    	<html:link page="<%=url1%>" styleId="newSite" onclick="<%=onClickPath%>">
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
						<logic:equal name="collectionProtocolRegistrationForm" property="checkedButton" value="true">
							<html:select property="participantID" styleClass="formFieldSized" styleId="participantID" size="1">
 							    <html:options collection="<%=Constants.PARTICIPANT_LIST%>" labelProperty="name" property="value"/>							
							</html:select>
						</logic:equal>
						<logic:equal name="collectionProtocolRegistrationForm" property="checkedButton" value="false">
							<html:select property="participantID" styleClass="formFieldSized" styleId="participantID" size="1" disabled="true">
 							    <html:options collection="<%=Constants.PARTICIPANT_LIST%>" labelProperty="name" property="value"/>							
							</html:select>
						</logic:equal>
					<%
						String url = "/Participant.do?operation=add&pageOf=pageOfParticipant";
					%>
						
							<html:link page="<%=url%>" styleId="newParticipant" onclick="<%=onClickPath%>">
		 						<bean:message key="buttons.addNew" />
	 						</html:link>					   
					</td>
				</tr>

				<tr id="row1">
						<logic:equal name="collectionProtocolRegistrationForm" property="checkedButton" value="true">
		         		    <td class="formRequiredNotice" width="5">&nbsp;</td>					
							<td class="formLabel">
						</logic:equal>
						<logic:equal name="collectionProtocolRegistrationForm" property="checkedButton" value="false">
		         		    <td class="formRequiredNotice" width="5">*</td>					
							<td class="formRequiredLabel">
						</logic:equal>

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
				           <html:text styleClass="formDateSized15" size="15" styleId="registrationDate" property="registrationDate" />
				           &nbsp;<bean:message key="page.dateFormat" />&nbsp;
					       <a href="javascript:show_calendar('collectionProtocolRegistrationForm.registrationDate',null,null,'MM-DD-YYYY');">
						         <img src="images\calendar.gif" width=24 height=22 border=0></a>
				 	</td>
				</tr>
	
				<!-- activitystatus -->	
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel" >
						<label for="activityStatus">
							<bean:message key="collectionprotocolregistration.activityStatus" />
						</label>
					</td>
					<td class="formField">
						<html:select property="activityStatus" styleClass="formFieldSized10" styleId="activityStatus" size="1">
							<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
						</html:select>
					</td>
				</tr>
				</logic:equal>
				
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
						<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
							<td>
								<html:button property="addSpecimenCollectionGroup" styleClass="actionButton" onclick="onAddSpecimenCollectionGroup(this)">
									<bean:message key="buttons.addSpecimenCollectionGroup"/>
								</html:button>
							</td>
						</logic:equal>	
						</tr>
					</table>
					<!-- action buttons end --></td>
				</tr>
			</table>
		</td>
		</tr>

		<!-- NEW Collection Protocol Registration ENTRY ends-->
	</table>
</html:form>