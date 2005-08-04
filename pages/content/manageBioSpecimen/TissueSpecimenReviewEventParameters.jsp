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
        String searchFormName = new String(Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_SEARCH_ACTION);

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_ADD_ACTION;
            readOnlyValue = false;
        }
		
%>	
			
<html:errors/>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action="<%=Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_ADD_ACTION%>">

	<logic:notEqual name="operation" value="<%=Constants.ADD%>"> 
	<!-- ENTER IDENTIFIER BEGINS-->	
	<br/>	
	<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td class="formTitle" height="20" colspan="3">
						<bean:message key="tissuespecimenrevieweventparameters.searchTitle"/>
					</td>
				</tr>	
		  
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel">
						<label for="systemIdentifier">
							<bean:message key="eventParameters.systemIdentifier"/>
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
	

	<!-- NEW TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS REGISTRATION BEGINS-->
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
				<bean:message key="tissuespecimenrevieweventparameters.title"/>
			 </td>
		</tr>

		<!-- Name of the tissuespecimenrevieweventparameters -->
<!-- User -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="eventParameters.user"/> 
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
					<bean:message key="eventParameters.dateofevent"/> 
				</label>
			</td>
			<td class="formField">
				 <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
					<html:text styleClass="formDateSized" size="35" styleId="dateOfEvent" property="dateOfEvent" readonly="true"/>
						<a href="javascript:show_calendar('tissueSpecimenReviewEventParametersForm.dateOfEvent','','','MM-DD-YYYY');">
							<img src="images\calendar.gif" width=24 height=22 border=0>
						</a>
			</td>
		</tr>

<!-- hours & minutes -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="type">
					<bean:message key="eventParameters.timeinhours"/>&nbsp; 
					<bean:message key="eventParameters.timeinminutes"/> 
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

<!-- neoplasticCellularityPercentage -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="tissuespecimenrevieweventparameters.neoplasticcellularitypercentage"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formDateSized" size="35" styleId="neoplasticCellularityPercentage" property="neoplasticCellularityPercentage" />
			</td>
		</tr>

<!-- necrosisPercentage -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="tissuespecimenrevieweventparameters.necrosispercentage"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formDateSized" size="35" styleId="necrosisPercentage" property="necrosisPercentage" />
			</td>
		</tr>

<!-- lymphocyticPercentage -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="tissuespecimenrevieweventparameters.lymphocyticpercentage"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formDateSized" size="35" styleId="lymphocyticPercentage" property="lymphocyticPercentage" />
			</td>
		</tr>

<!-- totalCellularityPercentage -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="tissuespecimenrevieweventparameters.totalcellularitypercentage"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formDateSized" size="35" styleId="totalCellularityPercentage" property="totalCellularityPercentage" />
			</td>
		</tr>

<!-- histologicalQuality -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="tissuespecimenrevieweventparameters.histologicalquality"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formDateSized" size="35" styleId="histologicalQuality" property="histologicalQuality" />
			</td>
		</tr>


<!-- comments -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="type">
					<bean:message key="eventParameters.comments"/> 
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

	 <!-- NEW tissuespecimenrevieweventparameters ends-->
	 
	 </html:form>
 </table>