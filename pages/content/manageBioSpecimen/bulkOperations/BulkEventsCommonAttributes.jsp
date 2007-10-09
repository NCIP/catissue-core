<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 

<html:errors />
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="60%">
		<html:hidden property="operation" />
		<tr><td height="10"></td></tr>
		<tr>
			<td>
				<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
					<tr>
						<td class="formTitle" height="20" colspan="2">
							<logic:equal name="bulkEventOperationsForm" property="operation" value="<%=Constants.BULK_TRANSFERS%>">
								<bean:message key="bulk.events.operation"/> 
							</logic:equal>
							<logic:equal name="bulkEventOperationsForm" property="operation" value="<%=Constants.BULK_DISPOSALS%>">
								<bean:message key="bulk.events.disposals"/> 
							</logic:equal>
						</td>
						<td class="formTitle"  align="right">
							<html:submit styleClass="actionButton"/>
						</td>
					</tr>
					<!-- User -->		
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" width="25%">
							<label for="type">
								<bean:message key="eventparameters.user"/> 
							</label>
						</td>
						<td class="formField">
						<!-- For tooltip -->
							<html:select property="userId" styleClass="formFieldSized" styleId="userId" size="1">
								<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
							</html:select>
						</td>
					</tr>

					<!-- date -->		
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" width="25%">
							<label for="type">
								<bean:message key="eventparameters.dateofevent"/> 
							</label>
						</td>
						<td class="formField">
							<bean:define id="eventDate" name="bulkEventOperationsForm" type="java.lang.String" property="dateOfEvent"/>
							<ncombo:DateTimeComponent name="dateOfEvent"
							  id="dateOfEvent"
							  formName="bulkEventOperationsForm"
							  month= "<%= Utility.getMonth(eventDate) %>"
							  year= "<%= Utility.getYear(eventDate) %>"
							  day= "<%= Utility.getDay(eventDate) %>"
							  value="<%= eventDate %>"
							  styleClass="formDateSized10"
							/>
							<bean:message key="page.dateFormat" />&nbsp;	
						</td>
					<tr>
					
					<!-- Time -->
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" width="25%">
							<label for="eventparameters.time">
								<bean:message key="eventparameters.time"/>
							</label>
						</td>
						<td class="formField">
							<bean:define id="hours" name="bulkEventOperationsForm" type="java.lang.String" property="timeInHours"/>
							<bean:define id="minutes" name="bulkEventOperationsForm" type="java.lang.String" property="timeInMinutes"/>
							<autocomplete:AutoCompleteTag property="timeInHours"
								  optionsList = "<%=request.getAttribute(Constants.HOUR_LIST)%>"
								  initialValue="<%=hours%>"
								  styleClass="formFieldSized5"
								  staticField="false"
						    	/>	
							&nbsp;
							<label for="eventparameters.timeinhours">
								<bean:message key="eventparameters.timeinhours"/>&nbsp; 
							</label>
					   		<autocomplete:AutoCompleteTag property="timeInMinutes"
									  optionsList = "<%=request.getAttribute(Constants.MINUTES_LIST)%>"
									  initialValue="<%=minutes%>"
									  styleClass="formFieldSized5"
									  staticField="false"
							   />	
							<label for="eventparameters.timeinminutes">
								&nbsp;<bean:message key="eventparameters.timeinminutes"/> 
							</label>
						</td>
					</tr>
					
					<!-- comments -->		
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel" width="25%">
							<label for="type">
								<bean:message key="eventparameters.comments"/> 
							</label>
						</td>
						<td class="formField">
							<html:textarea styleClass="formFieldSized"  styleId="comments" property="comments" />
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>