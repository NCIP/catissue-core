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

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_ADD_ACTION;
            readOnlyValue = false;
        }
		
%>	
			
<html:errors/>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action="<%=Constants.MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_ADD_ACTION%>">


	<!-- NEW MOLECULAR_SPECIMEN_REVIEW_PARAMETERS REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td><html:hidden property="operation" value="<%=operation%>"/></td>
		</tr>
		
		<tr>
			<td><html:hidden property="systemIdentifier" /></td>
		</tr>
		
		<tr>
			 <td class="formMessage" colspan="3">* indicates a required field</td>
		</tr>

		<tr>
			<td class="formTitle" height="20" colspan="3">
				<logic:equal name="operation" value="<%=Constants.ADD%>">
					<bean:message key="molecularspecimenreviewparameters.title"/>
				</logic:equal>
				<logic:equal name="operation" value="<%=Constants.EDIT%>">
					<bean:message key="molecularspecimenreviewparameters.edittitle"/>
				</logic:equal>
			</td>
		</tr>
		<!-- Name of the molecularspecimenreviewparameters -->
<!-- User -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="eventparameters.user">
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
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="eventparameters.dateofevent">
					<bean:message key="eventparameters.dateofevent"/> 
				</label>
			</td>
			<td class="formField">
				 <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
					<html:text styleClass="formDateSized" size="35" styleId="dateOfEvent" property="dateOfEvent" readonly="true"/>
						<a href="javascript:show_calendar('molecularSpecimenReviewParametersForm.dateOfEvent',null,null,'MM-DD-YYYY');">
							<img src="images\calendar.gif" width=24 height=22 border=0>
						</a>
			</td>
		</tr>

<!-- hours & minutes -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="eventparameters.timeinhours">
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

<!-- gelImageURL -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="molecularspecimenreviewparameters.gelimageurl">
					<bean:message key="molecularspecimenreviewparameters.gelimageurl"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formDateSized" size="35" styleId="gelImageURL" property="gelImageURL" />
			</td>
		</tr>

<!-- qualityIndex -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="molecularspecimenreviewparameters.qualityindex">
					<bean:message key="molecularspecimenreviewparameters.qualityindex"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formDateSized" size="35" styleId="qualityIndex" property="qualityIndex" />
			</td>
		</tr>

<!-- laneNumber -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="molecularspecimenreviewparameters.lanenumber">
					<bean:message key="molecularspecimenreviewparameters.lanenumber"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formDateSized" size="35" styleId="laneNumber" property="laneNumber" />
			</td>
		</tr>

<!-- gelNumber -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="molecularspecimenreviewparameters.gelnumber">
					<bean:message key="molecularspecimenreviewparameters.gelnumber"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formDateSized" size="35" styleId="gelNumber" property="gelNumber" />
			</td>
		</tr>

<!-- absorbanceAt260 -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="molecularspecimenreviewparameters.absorbanceat260">
					<bean:message key="molecularspecimenreviewparameters.absorbanceat260"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formDateSized" size="35" styleId="absorbanceAt260" property="absorbanceAt260" />
			</td>
		</tr>

<!-- absorbanceAt280 -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="molecularspecimenreviewparameters.absorbanceat280">
					<bean:message key="molecularspecimenreviewparameters.absorbanceat280"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formDateSized" size="35" styleId="absorbanceAt280" property="absorbanceAt280" />
			</td>
		</tr>

<!-- ratio28STo18S -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="molecularspecimenreviewparameters.ratio28STo18S">
					<bean:message key="molecularspecimenreviewparameters.ratio28STo18S"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formDateSized" size="35" styleId="ratio28STo18S" property="ratio28STo18S" />
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

		</table>
		
	  </td>
	 </tr>

	 <!-- NEW MOLECULAR specimen review parameters ends-->
	 
	 </html:form>
 </table>