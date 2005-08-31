<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<head>
	<script language="javascript">
		
	</script>
</head>
	
<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName;
        String searchFormName = new String(Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_SEARCH_ACTION);

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_ADD_ACTION;
            readOnlyValue = false;
        }
		
%>	
			
<html:errors/>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action="<%=Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_ADD_ACTION%>">

	<logic:notEqual name="operation" value="<%=Constants.ADD%>"> 
	<!-- ENTER IDENTIFIER BEGINS-->	
	<br/>	
	<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td class="formTitle" height="20" colspan="3">
						<bean:message key="checkincheckouteventparameter.searchTitle"/>
					</td>
				</tr>	
		  
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
						<label for="systemIdentifier">
							<bean:message key="eventparameters.systemIdentifier"/>
						</label>
					</td>
					<td class="formField">
						<html:text styleClass="formFieldSized" size="30" styleId="systemIdentifier" property="systemIdentifier"/>
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
	

	<!-- NEW CHECKIN_CHECKOUTEventParameter REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td><html:hidden property="operation" value="<%=operation%>"/></td>
		</tr>

		<logic:notEqual name="operation" value="<%=Constants.SEARCH%>">
		<tr>
			 <td class="formMessage" colspan="3">* indicates a required field</td>
		</tr>

		<tr>
			 <td class="formTitle" height="20" colspan="3">
				<bean:message key="checkincheckouteventparameter.title"/>
			 </td>
		</tr>

		<!-- Name of the checkincheckouteventparameter -->
<!-- User -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="eventparameters.user"/> 
				</label>
			</td>
			<td class="formField">
				<html:select property="userId" styleClass="formFieldSized" styleId="userId" size="1">
					<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
				</html:select>
			</td>
		</tr>

<!-- date -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="type">
					<bean:message key="eventparameters.dateofevent"/> 
				</label>
			</td>
			<td class="formField">
				 <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
					<html:text styleClass="formDateSized" size="35" styleId="dateOfEvent" property="dateOfEvent" readonly="true"/>
						<a href="javascript:show_calendar('checkInCheckOutEventParametersForm.dateOfEvent',null,null,'MM-DD-YYYY');">
							<img src="images\calendar.gif" width=24 height=22 border=0>
						</a>
			</td>
		</tr>


<!-- hours & minutes -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="type">
					<bean:message key="eventparameters.timeinhours"/>&nbsp; 
					<bean:message key="eventparameters.timeinminutes"/> 
				</label>
			</td>
			<td class="formField">
				<html:select property="timeInHours" styleClass="formFieldSized5" styleId="timeInHours" size="1">
					<html:options name="<%=Constants.HOURLIST%>" labelName="<%=Constants.HOURLIST%>" />
				</html:select>&nbsp;
				<html:select property="timeInMinutes" styleClass="formFieldSized5" styleId="timeInMinutes" size="1">
					<html:options name="<%=Constants.MINUTESLIST%>" labelName="<%=Constants.MINUTESLIST%>" />
				</html:select>
			</td>
		</tr>

<!-- checkincheckouteventparameter.storagestatus -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="checkincheckouteventparameter.storagestatus"/> 
				</label>
			</td>
			<td class="formField">
				<html:select property="storageStatus" styleClass="formDateSized" styleId="storageStatus" size="1">
					<html:options name="<%=Constants.STORAGESTATUSLIST%>" labelName="<%=Constants.STORAGESTATUSLIST%>" />
				</html:select>
			</td>
		</tr>

<!-- comments -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="type">
					<bean:message key="eventparameters.comments"/> 
				</label>
			</td>
			<td class="formField">
				<html:textarea styleClass="formFieldSized"  styleId="comments" property="comments" />
			</td>
		</tr>

<!-- buttons -->
		<tr>
		  <td align="right" colspan="3">
			<!-- action buttons begins -->
			<%
        		String changeAction = "setFormAction('" + formName + "');";
			%> 
			<table cellpadding="4" cellspacing="0" border="0">
				<tr>
					<td>
						<html:submit styleClass="actionButton" value="Submit" onclick="<%=changeAction%>" />
					</td>
					<td><html:reset styleClass="actionButton"/></td> 
				</tr>
			</table>
			<!-- action buttons end -->
			</td>
		</tr>

		</logic:notEqual>
		</table>
		
	  </td>
	 </tr>

	 <!-- NEW CheckInCheckOutEventParameters ends-->
	 
	 </html:form>
 </table>