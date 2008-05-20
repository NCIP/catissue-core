<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.actionForm.TransferEventParametersForm"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%@ page language="java" isELIgnored="false" %>

<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/Hashtable.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<!-- Mandar 21-Aug-06 : For calendar changes -->
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<!-- Mandar 21-Aug-06 : calendar changes end -->
</head>


			
<html:errors/>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<html:form action='${requestScope.formName}'>


	<!-- NEW TRANSFER_EVENT_PARAMETERS REGISTRATION BEGINS-->
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
			 <td class="formMessage" colspan="3">* indicates a required field</td>
		</tr>
		
		<tr>
			<td>
				<html:hidden property="specimenId" value='${requestScope.specimenId}'/>
			</td>
		</tr>

		<tr>
			<td class="formTitle" height="20" colspan="3">
				<logic:equal name="operation" value='${requestScope.addForJSP}'>
					<bean:message key="transfereventparameters.title"/>
				</logic:equal>
				<logic:equal name="operation" value='${requestScope.editForJSP}'>
					<bean:message key="transfereventparameters.edittitle"/>
				</logic:equal>
			</td>
		</tr>
		
		<!-- Name of the transfereventparameters -->
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
			  formName="transferEventParametersForm"
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
			  formName="transferEventParametersForm"
			  styleClass="formDateSized10"
					/>
</logic:empty>


<bean:message key="page.dateFormat" />&nbsp;


			</td>
		</tr>
		
		<!-- hours & minutes -->		
		<!-- 
			 Bug ID:  AutocompleteBugID
			 Patch ID: AutocompleteBugID_15
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
					   initialValue='${transferEventParametersForm.timeInHours}'
					  styleClass="formFieldSized5"
					  staticField="false"
			    />	
				&nbsp;
				<label for="eventparameters.timeinhours">
					<bean:message key="eventparameters.timeinhours"/>&nbsp; 
				</label>
                   <autocomplete:AutoCompleteTag property="timeInMinutes"
						  optionsList = '${requestScope.minutesList}'
						  initialValue='${transferEventParametersForm.timeInMinutes}'
						  styleClass="formFieldSized5"
						  staticField="false"
				    />	
				<label for="eventparameters.timeinminutes">
					&nbsp;<bean:message key="eventparameters.timeinminutes"/> 
				</label>
			</td>
		</tr>
		
		

<!-- fromPosition -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="transfereventparameters.fromposition"/> 
				</label>
			</td>
			<td class="formField">
				<html:hidden property="fromPositionDimensionOne" value='${requestScope.posOne}'/>
				<html:hidden property="fromPositionDimensionTwo" value='${requestScope.posTwo}' />
				<html:hidden property="fromStorageContainerId" value='${requestScope.storContId}' />
				<html:hidden property="containerId" styleId="containerId"/>
				<!-- Checking the fromPositionData is null -->
				<logic:empty name="transferEventParametersForm" property="fromPositionData" >
				<html:text styleClass="formDateSized" size="35" styleId="fromPosition" property="fromPosition" readonly="true" />
				</logic:empty>
				
				<logic:notEmpty name="transferEventParametersForm" property="fromPositionData" >
				<html:text styleClass="formDateSized" size="35" styleId="fromPositionData" property="fromPositionData" readonly="true" />
				</logic:notEmpty>
			</td>
		</tr>

<!-- toPosition -->		
		<tr>
			<td class="formRequiredNotice" width="5">*</td>
			<td class="formRequiredLabel">
				<label for="type">
					<bean:message key="transfereventparameters.toposition"/> 
				</label>
			</td>
			<%-- n-combo-box start --%>
			${requestScope.getJSForOutermostDataTable}
			${requestScope.getJSEquivalentFor }
			
			
			<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
			
			<td class="formField" colSpan="2">							
				<table border="0">	
					<tr>
						<logic:equal name="operation" value='${requestScope.add}'>
						<td ><html:radio value="1" onclick="onRadioButtonGroupClickForTransfer(this)" styleId="stContSelection" property="stContSelection" /></td>
						</logic:equal>
						<td>
							<ncombo:nlevelcombo dataMap='${requestScope.dataMap}'
								attributeNames='${requestScope.attrNames}'
								initialValues='${requestScope.initValues}'
								styleClass ='${requestScope.styClass}' 
								tdStyleClass ='${requestScope.tdStyleClass}' 
								labelNames='${requestScope.labelNames}'
								rowNumber='${requestScope.rowNumber}'
								onChange ='${requestScope.onChange}' 
								disabled = '${requestScope.dropDownDisable}'
								tdStyleClassArray='${requestScope.tdStyleClassArray}'
								formLabelStyle="formLabelBorderless"							
								noOfEmptyCombos = '${requestScope.noOfEmptyCombos}'/>
								</tr>
								</table>
						</td>
					</tr>
					<logic:equal name="operation" value='${requestScope.add}'>
					<tr>
						<td ><html:radio value="2" onclick="onRadioButtonGroupClickForTransfer(this)" styleId="stContSelection" property="stContSelection"/></td>
						<td class="formLabelBorderlessLeft">
							<html:text styleClass="formFieldSized10"  size="30" styleId="selectedContainerName" property="selectedContainerName" disabled= '${requestScope.textBoxDisable}'/>
							<html:text styleClass="formFieldSized3"  size="5" styleId="pos1" property="pos1" disabled='${requestScope.textBoxDisable}' />
							<html:text styleClass="formFieldSized3"  size="5" styleId="pos2" property="pos2" disabled= '${requestScope.textBoxDisable}'/>
							<html:button styleClass="actionButton" property="containerMap" onclick='${requestScope.buttonOnClicked}' disabled='${requestScope.textBoxDisable}'>
								<bean:message key="buttons.map"/>
							</html:button>
						</td>
					</tr>			
					</logic:equal>
				</table>											
			</td>
			
			

<%--		 n-combo-box end --%>
					
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

	 <!-- NEW TRANSFER_EVENT_PARAMETERS ends-->
	 
	 </html:form>
 </table>