<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.DisposalEventParametersForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<%@ page language="java" isELIgnored="false" %>
<script src="jss/script.js" type="text/javascript"></script>


<head>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<!-- Mandar 21-Aug-06 : For calendar changes -->
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<!-- Mandar 21-Aug-06 : calendar changes end -->


<script language="javascript" >

function confirmAction(form)
	{
		if(form.activityStatus.value == '${requestScope.activityStatusDisabled}')
		{
			if(confirm("Are you sure you want to disable the specimen ?"))
			{
				form.action='${requestScope.formName}?disposal=true';
				form.submit();
			}
			else
			{
				return false;
			}
		}
		else if(form.activityStatus.value == '${requestScope.activityStatusClosed}')
		{
			if(confirm("Are you sure you want to close the specimen ?"))
			{
				form.action='${requestScope.formName}';
				form.submit();
			}
			else
			{
				return false;
			}
		
		}
		
	}
	
</script>	
</head>
	
			
<html:errors/>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action='${requestScope.formName}'>


	<!-- NEW disposalEventParameter REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td><html:hidden property="operation" /></td>
		</tr>
		<tr>
			<td><html:hidden property="id" /></td>
			<td><html:hidden property="onSubmit" /></td>
		</tr>

		<tr>
			<td>
				<html:hidden property="specimenId" value='${requestScope.specimenId}'/>
			</td>
		</tr>
		
		<tr>
			 <td class="formMessage" colspan="3">* indicates a required field</td>
		</tr>

		<tr>
			<td class="formTitle" height="20" colspan="3">
				<logic:equal name="operation" value='${requestScope.addForJSP}'>
					<bean:message key="disposaleventparameters.title"/>
				</logic:equal>
				<logic:equal name="operation" value='${requestScope.editForJSP}'>
					<bean:message key="disposaleventparameters.EDITtitle"/>
				</logic:equal>
			</td>
		</tr>

		<!-- Name of the disposalEventParameters -->
<!-- User -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="eventparameters.user"/> 
				</label>
			</td>
			<td class="formField">
<!-- Mandar : 434 : for tooltip -->
				<html:select property="userId" styleClass="formFieldSized" styleId="userId" size="1"
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
					<html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/>
				</html:select>
			</td>
		</tr>

<!-- date -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="eventparameters.dateofevent"/> 
				</label>
			</td>
			<td class="formField">
				
				<logic:notEmpty name="currentEventParametersDate">
<ncombo:DateTimeComponent name="dateOfEvent"
			  id="dateOfEvent"
			  formName="disposalEventParametersForm"
			                  month='${requestScope.eventParametersMonth}'
							  year='${requestScope.eventParametersYear}'
							  day='${requestScope.eventParametersDay}'
							  value='${requestScope.currentEventParametersDate}'
			  styleClass="formDateSized10"
					/>
</logic:notEmpty>
<logic:empty name="currentEventParametersDate">
<ncombo:DateTimeComponent name="dateOfEvent"
			  id="dateOfEvent"
			  formName="disposalEventParametersForm"
			  styleClass="formDateSized10"
					/>
</logic:empty>
				
				<bean:message key="page.dateFormat" />&nbsp;
				

			</td>
		</tr>
		
		
		<!-- hours & minutes -->		
		<!-- 
			 Bug ID:  AutocompleteBugID
			 Patch ID: AutocompleteBugID_4
			 Description:html:select tag is replaced by Autocomplete
	   -->	
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="eventparameters.time">
					<bean:message key="eventparameters.time"/>
				</label>
			</td>
			<td class="formField">
			 <autocomplete:AutoCompleteTag property="timeInHours"
					  optionsList = '${requestScope.hourList}'
					   initialValue='${disposalEventParametersForm.timeInHours}'
					  styleClass="formFieldSized5"
					  staticField="false"
			    />	
				&nbsp;
				<label for="eventparameters.timeinhours">
					<bean:message key="eventparameters.timeinhours"/>&nbsp; 
				</label>
                   <autocomplete:AutoCompleteTag property="timeInMinutes"
						 optionsList = '${requestScope.minutesList}'
						  initialValue='${disposalEventParametersForm.timeInMinutes}'
						  styleClass="formFieldSized5"
						  staticField="false"
				    />	
				<label for="eventparameters.timeinminutes">
					&nbsp;<bean:message key="eventparameters.timeinminutes"/> 
				</label>
			</td>
		</tr>
		
		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="activityStatus">
				<bean:message key="participant.activityStatus" />
				</label>
			</td>
			<td class="formField">
			 <autocomplete:AutoCompleteTag property="activityStatus"
						  optionsList = '${requestScope.activityStatusList}'
						  onChange='${requestScope.strCheckStatus}'
						  initialValue='${disposalEventParametersForm.activityStatus}'
			 />	
			</td>
		</tr>				

		
<!-- Reason -->		
		<tr>
		
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="type">
					<bean:message key="disposaleventparameters.reason"/> 
				</label>
			</td>
			<td class="formField">
				<html:textarea styleClass="formFieldSized"  styleId="reason" property="reason" />
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
			
			<table cellpadding="4" cellspacing="0" border="0">
				<tr>
				<!-- delete button added for deleting the objects : Nitesh-->
					<td>
						
						<html:button styleClass="actionButton" value="Delete" property="disable" onclick='${requestScope.deleteAction}'/>
					</td>
					<td>
						<html:button styleClass="actionButton" value="Submit" property="Submit" onclick="return confirmAction(this.form)"/>
						<!--<html:submit styleClass="actionButton" value="Submit" onclick='${requestScope.changeAction}'/>-->
						
					</td>
					<%-- td><html:reset styleClass="actionButton"/></td --%> 
				</tr>
			</table>
			<!-- action buttons end -->
			</td>
		</tr>

		</table>
		
	  </td>
	 </tr>

	 <!-- NEW disposalEventParameters ends-->
	 
	 </html:form>
 </table>