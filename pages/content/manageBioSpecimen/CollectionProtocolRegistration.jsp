<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>

<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<script src="jss/script.js" type="text/javascript"></script>
<%
	   		Object obj = request.getAttribute("collectionProtocolRegistrationForm");
			CollectionProtocolRegistrationForm form =null;
			String currentRegistrationDate = "";
			if(obj != null && obj instanceof CollectionProtocolRegistrationForm)
			{
				form = (CollectionProtocolRegistrationForm)obj;
				currentRegistrationDate = form.getRegistrationDate();  
				
				if(currentRegistrationDate == null)
					currentRegistrationDate = "";
			}

				String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
				boolean isAddNew = false;

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
<head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript">
		function onCheckboxButtonClick(element,dropDownList)
		{
			// changes as per bug 287
			var row = document.getElementById("row1");
			var cell1 = row.cells[0];
			var cell2 = row.cells[1];

			//	 Changes as per bug id 709
	    	var row0 = document.getElementById("row0");
			var cell10 = row0.cells[0];
			var cell20 = row0.cells[1];
			
		    if(element.checked==true)
		    { 
		    	cell1.innerHTML=" &nbsp; ";
			    cell2.className="formLabel";
			    
		    	document.forms[0].participantID.disabled = false;
	           	cell10.innerHTML="*";
	           	cell20.className="formRequiredLabel";
      		}
           	else
           	{
	           	cell1.innerHTML="*";
	           	cell2.className="formRequiredLabel";

		    	cell10.innerHTML=" &nbsp; ";
			    cell20.className="formLabel";
	           	
	           	document.forms[0].participantID.disabled = true;
      		}
		}
		
		// for add new SpecimenCollectionGroup
		function onAddSpecimenCollectionGroup(element)
		{
			var action ="createSpecimenCollectionGroup";
			changeSubmitTo(action );
			document.forms[0].submit();
		}
		
		
</script>		
</head>
<%
%>

<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action="<%=formName%>">
	
	<%
			String normalSubmitFunctionName = "setSubmittedFor('" + submittedFor+ "','" + Constants.PROTOCOL_REGISTRATION_FORWARD_TO_LIST[0][1]+"')";
			String forwardToSubmitFuctionName = "setSubmittedFor('ForwardTo','" + Constants.PROTOCOL_REGISTRATION_FORWARD_TO_LIST[1][1]+"')";									
			String confirmDisableFuncName = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
			String normalSubmit = normalSubmitFunctionName + ","+confirmDisableFuncName;
			String forwardToSubmit = forwardToSubmitFuctionName + ","+confirmDisableFuncName;
	%>	
	
		
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	
		<!-- NEW Collection Protocol Registration ENTRY BEGINS-->
		<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td>
						<html:hidden property="operation" value="<%=operation%>" />
						<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
						<html:hidden property="forwardTo" value=""/>
						<html:hidden property="participantID" />
					</td>
					<td><html:hidden property="id"/>
					<td><html:hidden property="onSubmit"/></td>
					<html:hidden property="redirectTo" value="<%=reqPath%>"/>
					
				</tr>

				<tr>
					<td class="formMessage" colspan="3">* indicates a required field</td>
				</tr>
					
				<tr>
					<td class="formTitle" height="20" colspan="3">
						<logic:equal name="operation" value="<%=Constants.ADD%>">
							<bean:message key="collectionProtocolReg.add.title"/>
						</logic:equal>
						<logic:equal name="operation" value="<%=Constants.EDIT%>">
							<bean:message key="collectionProtocolReg.edit.title"/>
						</logic:equal>

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
<!-- Mandar : 434 : for tooltip -->
						<html:select property="collectionProtocolID" styleClass="formFieldSized" styleId="collectionProtocolID" size="1"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
						    <html:options collection="<%=Constants.PROTOCOL_LIST%>" labelProperty="name" property="value"/>															
					    </html:select>
						&nbsp;
						<html:link href="#" styleId="newCollectionProtocol" onclick="addNewAction('ParticipantRegistrationAddNew.do?addNewForwardTo=collectionProtocol&forwardTo=participantRegistration&addNewFor=collectionProtocolId')">
							<bean:message key="buttons.addNew" />
						</html:link>					   
					</td>
				</tr>
					
				<tr id="row0">				
			    	<td class="formRequiredNotice" width="5">&nbsp;</td>					
      	 	       	<td class="formLabel" nowrap>
                   		
							<label for="participantID">
								<bean:message key="collectionProtocolReg.participantName" />
							</label>
						
					</td>
					<td class="formField">
						<html:text styleClass="formFieldSized" maxlength="10"  size="30" styleId="participantName" 
					     		property="participantName" disabled="true"/>	
						&nbsp;
						<html:link href="#" styleId="newParticipant" onclick="addNewAction('ParticipantRegistrationAddNew.do?addNewForwardTo=participant&forwardTo=participantRegistration&addNewFor=participantId')">
							<bean:message key="buttons.addNew" />
						</html:link>				   
					</td>
				</tr>

				<tr id="row1">					
						
						<td class="formRequiredNotice" width="5">&nbsp;</td>					
						<td class="formLabel">
						
						<label for="name">
							<bean:message key="collectionProtocolReg.participantProtocolID" />
						</label>
					</td>
					<td class="formField">
						<html:text styleClass="formFieldSized" maxlength="255"  size="30" styleId="participantProtocolID" property="participantProtocolID" readonly="<%=readOnlyValue%>" />
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
<!-- 				           <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
				           <html:text styleClass="formDateSized15" maxlength="10"  size="15" styleId="registrationDate" property="registrationDate" />
				           &nbsp;<bean:message key="page.dateFormat" />&nbsp;
					       <a href="javascript:show_calendar('collectionProtocolRegistrationForm.registrationDate',null,null,'MM-DD-YYYY');">
						         <img src="images\calendar.gif" width=24 height=22 border=0></a>
-->
<%
	 if(currentRegistrationDate.trim().length() > 0)
	{
			Integer registrationYear = new Integer(Utility.getYear(currentRegistrationDate ));
			Integer registrationMonth = new Integer(Utility.getMonth(currentRegistrationDate ));
			Integer registrationDay = new Integer(Utility.getDay(currentRegistrationDate ));
%>
			<ncombo:DateTimeComponent name="registrationDate"
									  id="registrationDate"
 									  formName="collectionProtocolRegistrationForm"	
									  month= "<%=registrationMonth %>"
									  year= "<%=registrationYear %>"
									  day= "<%= registrationDay %>" 
									  value="<%=currentRegistrationDate %>"
									  styleClass="formDateSized10"
											 />		
<% 
	}
	else
	{  
 %>
			<ncombo:DateTimeComponent name="registrationDate"
									  id="registrationDate"
 									  formName="collectionProtocolRegistrationForm"	
									  styleClass="formDateSized10" 
											 />		
<%
	}
%>
<bean:message key="page.dateFormat" />&nbsp;

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
<!-- Mandar : 434 : for tooltip -->
						<html:select property="activityStatus" styleClass="formFieldSized10" styleId="activityStatus" size="1" onchange="<%=strCheckStatus%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
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
				</tr>
			</table>
		</td>
		</tr>

		<!-- NEW Collection Protocol Registration ENTRY ends-->
	</table>
	<%@ include file="CollectionProtocolRegistrationPageButtons.jsp"%>
</html:form>