<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.MolecularSpecimenReviewParametersForm"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%@ page language="java" isELIgnored="false" %>
<head>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="javascript">
		
	</script>
<!-- Mandar 21-Aug-06 : For calendar changes -->
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<!-- Mandar 21-Aug-06 : calendar changes end -->

</head>
	
		
<html:errors/>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action='${requestScope.formName}'>


	<!-- NEW MOLECULAR_SPECIMEN_REVIEW_PARAMETERS REGISTRATION BEGINS-->
	<tr>
	<td>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td><html:hidden property="operation" /></td>
		</tr>
		
		<tr>
			<td><html:hidden property="id" /></td>
		</tr>

		<tr>
			<td>
				<html:hidden property="specimenId" value='${requestScope.specimenId}'/>
				<html:hidden property="isRNA" value='${requestScope.isRNA}'/>
			</td>
		</tr>
		
		<tr>
			 <td class="formMessage" colspan="3">* indicates a required field</td>
		</tr>

		<tr>
			<td class="formTitle" height="20" colspan="3">
				<logic:equal name="operation" value='${requestScope.addForJSP}'>
					<bean:message key="molecularspecimenreviewparameters.title"/>
				</logic:equal>
				<logic:equal name="operation" value='${requestScope.editForJSP}'>
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
				<label for="eventparameters.dateofevent">
					<bean:message key="eventparameters.dateofevent"/> 
				</label>
			</td>
			<td class="formField">

<logic:notEmpty name="currentEventParametersDate">
<ncombo:DateTimeComponent name="dateOfEvent"
			  id="dateOfEvent"
			  formName="molecularSpecimenReviewParametersForm"
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
			  formName="molecularSpecimenReviewParametersForm"
			  styleClass="formDateSized10"
					/>
</logic:empty>
<bean:message key="page.dateFormat" />&nbsp;


			</td>
		</tr>		
		
		<!-- hours & minutes -->	
		<!-- 
			 Bug ID:  AutocompleteBugID
			 Patch ID: AutocompleteBugID_9
			 Description: html:select tag is replaced by Autocomplete
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
					   initialValue='${molecularSpecimenReviewParametersForm.timeInHours}'
					  styleClass="formFieldSized5"
					  staticField="false"
			    />	
				&nbsp;
				<label for="eventparameters.timeinhours">
					<bean:message key="eventparameters.timeinhours"/>&nbsp; 
				</label>
                   <autocomplete:AutoCompleteTag property="timeInMinutes"
						  optionsList = '${requestScope.minutesList}'
						  initialValue='${molecularSpecimenReviewParametersForm.timeInMinutes}'
						  styleClass="formFieldSized5"
						  staticField="false"
				    />	
				<label for="eventparameters.timeinminutes">
					&nbsp;<bean:message key="eventparameters.timeinminutes"/> 
				</label>
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
				<html:text styleClass="formDateSized" maxlength="255"  size="35" styleId="gelImageURL" property="gelImageURL" />
			</td>
		</tr>

<!-- qualityIndex -->		
		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formRequiredLabel">
				<label for="molecularspecimenreviewparameters.qualityindex">
					<bean:message key="molecularspecimenreviewparameters.qualityindex"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formDateSized" maxlength="50"  size="35" styleId="qualityIndex" property="qualityIndex" />
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
				<html:text styleClass="formDateSized" maxlength="50"  size="35" styleId="laneNumber" property="laneNumber" />
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
				<html:text styleClass="formDateSized" maxlength="10"  size="35" styleId="gelNumber" property="gelNumber" />
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
				<html:text styleClass="formDateSized" maxlength="10"  size="35" styleId="absorbanceAt260" property="absorbanceAt260" />
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
				<html:text styleClass="formDateSized" maxlength="10"  size="35" styleId="absorbanceAt280" property="absorbanceAt280" />
			</td>
		</tr>

<!-- ratio28STo18S -->		
<logic:equal name="molecularSpecimenReviewParametersForm" property="checkRNA" value="true">

		<tr>
			<td class="formRequiredNotice" width="5">&nbsp;</td>
			<td class="formLabel">
				<label for="molecularspecimenreviewparameters.ratio28STo18S">
					<bean:message key="molecularspecimenreviewparameters.ratio28STo18S"/> 
				</label>
			</td>
			<td class="formField">
				<html:text styleClass="formDateSized" maxlength="10"  size="35" styleId="ratio28STo18S" property="ratio28STo18S" />
			</td>
		</tr>
		</logic:equal>>
	
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
					<td>
						<html:submit styleClass="actionButton" value="Submit" onclick='${requestScope.changeAction}' />
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

	 <!-- NEW MOLECULAR specimen review parameters ends-->
	 
	 </html:form>
 </table>