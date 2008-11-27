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
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet />
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<!-- Mandar 21-Aug-06 : calendar changes end -->
</head>
<script language="JavaScript">

function toStoragePositionChange(element)
{	
	var autoDiv = document.getElementById("AutoDiv");
	var manualDiv = document.getElementById("ManualDiv");

	if(element.value == 1)
	{   
		manualDiv.style.display='none';
		autoDiv.style.display  = 'block';
	}
	else
	if(element.value == 2)
	{				
		autoDiv.style.display  = 'none';	
		manualDiv.style.display = 'block';				
	}			
}

</SCRIPT>


			
<%@ include file="/pages/content/common/ActionErrors.jsp" %>
    
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%">
<html:form action='${requestScope.formName}'>
	<html:hidden property="operation" />
	<html:hidden property="id" />
	<html:hidden property="specimenId" value='${requestScope.specimenId}'/>
	<tr>
         <td align="left" class="tr_bg_blue1" >
			<span class="blue_ar_b">&nbsp;<bean:message  key="eventparameters"/> &quot;<em><bean:message key="transfereventparameters"/></em>&quot;</span></td>
        </tr>
		<tr>
          <td  class="showhide1"></td>
        </tr>
		<!-- Name of the transfereventparameters -->
<!-- User -->		
		<tr>
          <td colspan="4" class="showhide"><table width="100%" border="0" cellpadding="1" cellspacing="0">
               <tr>
                  <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td width="15%" align="left" nowrap class="black_ar"><bean:message key="eventparameters.user"/></td>
                  <td  align="left" valign="middle" class="black_ar" width="30%"><html:select property="userId" styleClass="formFieldSized18" styleId="userId" size="1"
				 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
					<html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/>
				</html:select></td>
				<td width="1%" colspan="4"></td>
                </tr>

<!-- date -->		
		<tr>
                  <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar"><bean:message key="eventparameters.dateofevent"/></td>
                  <td colspan="" align="left" >
				  <logic:notEmpty name="currentEventParametersDate">
					<ncombo:DateTimeComponent name="dateOfEvent"
					  id="dateOfEvent"
							  formName="transferEventParametersForm"
			                  month='${requestScope.eventParametersMonth}'
							  year='${requestScope.eventParametersYear}'
							  day='${requestScope.eventParametersDay}'
							  value='${requestScope.currentEventParametersDate}'
							  styleClass="black_ar" />
				</logic:notEmpty>
				<logic:empty name="currentEventParametersDate">
					<ncombo:DateTimeComponent name="dateOfEvent"
						  id="dateOfEvent"
						  formName="transferEventParametersForm"
						  styleClass="black_ar" size="5" />
				</logic:empty>
                    <span class="grey_ar_s"><bean:message key="page.dateFormat" /></span></td>
					
                  <td align="center" class="black_ar" width="1%"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar" width="8%"><bean:message key="eventparameters.time"/></td>
                  <td colspan="0" align="left"><span class="black_ar">
					<autocomplete:AutoCompleteTag property="timeInHours"
					   optionsList = '${requestScope.hourList}'
					   initialValue='${transferEventParametersForm.timeInHours}'
					  styleClass="black_ar"
					  staticField="false"
						size = "3" /> 
					  &nbsp;<bean:message key="eventparameters.timeinhours"/>&nbsp;&nbsp;
                    <autocomplete:AutoCompleteTag property="timeInMinutes"
						  optionsList = '${requestScope.minutesList}'
						  initialValue='${transferEventParametersForm.timeInMinutes}'
						  styleClass="black_ar"
						  staticField="false"  size="3"/>	
				<label for="eventparameters.timeinminutes">
					&nbsp;<bean:message key="eventparameters.timeinminutes"/> 
				</label></span></td>
                </tr>
		
		

<!-- fromPosition -->		
		<tr>
                  <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar"><bean:message key="transfereventparameters.fromposition"/></td>
                  <td colspan="5" align="left"><span class="black_ar">
                    <html:hidden property="fromPositionDimensionOne" value='${requestScope.posOne}'/>
				<html:hidden property="fromPositionDimensionTwo" value='${requestScope.posTwo}' />
				<html:hidden property="fromStorageContainerId" value='${requestScope.storContId}' />
				<html:hidden property="containerId" styleId="containerId"/>
				<!-- Checking the fromPositionData is null -->
				<logic:empty name="transferEventParametersForm" property="fromPositionData" >
				<html:text styleClass="black_ar" size="30" styleId="fromPosition" property="fromPosition" readonly="true" />
				</logic:empty>
				
				<logic:notEmpty name="transferEventParametersForm" property="fromPositionData" >
				<html:text styleClass="black_ar" size="30" styleId="fromPositionData" property="fromPositionData" readonly="true" />
				</logic:notEmpty>
                  </span></td>
                </tr>

<!-- toPosition -->		
		<tr>
			<td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar"><bean:message key="transfereventparameters.toposition"/></td>
			<%-- n-combo-box start --%>
			${requestScope.getJSForOutermostDataTable}
			${requestScope.getJSEquivalentFor }
			
			
			<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
			
			<td class="black_ar" align = "left" colspan="4">					
				<table border="0" align="left" cellpadding="0" cellspacing="0">
							<tr >
							<logic:equal name="operation" value='${requestScope.add}'>
								<td>								
								<html:select property="stContSelection" styleClass="black_ar"
											styleId="stContSelection" size="1"	onmouseover="showTip(this.id)"
											onmouseout="hideTip(this.id)" onchange= "toStoragePositionChange(this)">
										<html:options collection="storageListForTransferEvent"
														labelProperty="name" property="value" />
								</html:select> 
										
								&nbsp;&nbsp;
								</td>
								</logic:equal>
								<td>
									<div id="AutoDiv" style="display:block" >
										<ncombo:nlevelcombo dataMap='${requestScope.dataMap}'
								attributeNames='${requestScope.attrNames}'
								initialValues='${requestScope.initValues}'
								styleClass ='${requestScope.styClass}' 
								tdStyleClass ='${requestScope.tdStyleClass}' 
								labelNames='${requestScope.labelNames}'
								rowNumber='${requestScope.rowNumber}'
								onChange ='${requestScope.onChange}' 
								
								tdStyleClassArray='${requestScope.tdStyleClassArray}'
								formLabelStyle="formLabelBorderless"							
								noOfEmptyCombos = '${requestScope.noOfEmptyCombos}'/>
											</tr>
											</table>

									</div>
								</td>
								<td class="groupElements">
									<div id="ManualDiv" style="display:none" >
									
										<html:text styleClass="black_ar"  size="15" styleId="selectedContainerName"
													property="selectedContainerName" />
												<html:text styleClass="black_ar"  size="3" styleId="pos1" 
													property="pos1" />
												<html:text styleClass="black_ar"  size="3" styleId="pos2" 
													property="pos2" />
												<html:button styleClass="black_ar" property="containerMap"												onclick="${requestScope.buttonOnClicked}" >
														<bean:message key="buttons.map"/>
												</html:button>									
									</div>								
								</td>
								
							</tr>
							</table>										
			</td>
			
			

<%--		 n-combo-box end --%>
					
		</tr>	




<!-- comments -->		
		<tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" valign="top" class="black_ar_t">
						<bean:message key="eventparameters.comments"/></td>
                  <td colspan="5" align="left">
						<html:textarea styleClass="black_ar"  styleId="comments" property="comments" cols="73" rows="4"/>
				</td>
                </tr>
				</table></td>
        </tr>

<!-- buttons -->
		<tr>
          <td class="buttonbg">
			<html:submit styleClass="blue_ar_b" value="Submit" onclick='${requestScope.changeAction}' />
				&nbsp;|&nbsp;
			<html:link	page="/QueryManageBioSpecimen.do" styleClass="cancellink">
				<bean:message key="buttons.cancel" />
			</html:link>
		</td>
        </tr>

		</table>
		
	  </td>
	 </tr>

	 <!-- NEW TRANSFER_EVENT_PARAMETERS ends-->
	 
	 </html:form>
 </table>