<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<head>
<SCRIPT LANGUAGE="JavaScript">
	var search1='`';
</script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/caTissueSuite.js"></script>
<script src="jss/calendarComponent.js" language="JavaScript" type="text/javascript"></script>
<script src="jss/caTissueSuite.js" language="JavaScript" type="text/javascript"></script>

<SCRIPT>var imgsrc="images/";</SCRIPT>
<script>

if('${requestScope.tabSel}'=="consentTab"){
	window.onload=consentPage;
}

function defineEvents()
{
	var action="DefineEvents.do?pageOf=pageOfDefineEvents&operation=${requestScope.operation}";
	document.forms[0].action=action;
	document.forms[0].submit();
}

function viewSummary()
{	
	var action="DefineEvents.do?Event_Id=dummyId&pageOf=ViewSummary&operation=${requestScope.operation}";
	document.forms[0].action=action;
	document.forms[0].submit();
}
function updateCPTree()
{	
  window.parent.frames['CPTreeView'].location="ShowCollectionProtocol.do?operation=${requestScope.operation}";
}
function openEventPage()
{
    var action="DefineEvents.do?pageOf=pageOfDefineEvents&operation=${requestScope.operation}";
	document.forms[0].action=action;
	document.forms[0].submit();
}

</script>

<style>
	div#d1
	{
	 display:none;
	}
	div#d999
	{
	 display:none;
	}
</style>
</head>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<link href="css/styleSheet.css" rel="stylesheet" type="text/css" />
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>

        
<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action='${requestScope.formName}'>

 <table summary="" cellpadding="0" cellspacing="0" border="0"  style="padding-left:0;padding-right:0;" width="100%" >
  
	<tr>
 		
	<td><table width="100%" border="0" cellspacing="0" cellpadding="0" >
                    <tr>
                      <td width="10%" valign="bottom" id="collectionProtocolTab" onclick="collectionProtocolPage()"> <img src="images/uIEnhancementImages/cp_details.gif" alt="Collection Protocol Details" width="174" height="20" border="0" /></td>
                      <td width="10%" valign="bottom" onclick="consentPage()" id="consentTab"><img src="images/uIEnhancementImages/cp_consents1.gif" alt="Consents" width="94" height="20" border="0" /></td>
                      <td width="10%" valign="bottom" ><a href="#"><img src="images/uIEnhancementImages/cp_privileges1.gif" alt="Privileges" width="94" height="20" border="0" onclick="showAssignPrivilegePage('${requestScope.operation}')"/></a></td>
					  <td width="70%" valign="bottom" class="cp_tabbg">&nbsp;</td>
					 </tr>
                  </table></td>
				    
					<td width="70%" valign="bottom" class="cp_tabbg">&nbsp;</td>
						 <td align="right" valign="top" class="cp_tabbg">
							<span class="smalllink">
									<html:link href="#"  styleId="newUser" onclick="updateCPTree();viewSummary()">
										<bean:message key="cpbasedentry.viewsummary" /> 
									 </html:link>
							</span>
						 </td>  
   </tr>
   <tr>
   <td class="cp_tabtable" colspan="6"/>
<!-- table 1 -->
<table summary="" cellpadding="0" cellspacing="0" border="0" id="table1" class="contentPage" width="100%">
<!-- NEW COLLECTIONPROTOCOL ENTRY BEGINS-->
		
		<tr>
		<td colspan="4">
		<!-- table 4 -->
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td>
						<html:hidden property="operation"/>
						<html:hidden property="submittedFor"/>
					</td>
					<td><html:hidden property="onSubmit"/></td>
				</tr>
				<tr>
					<td><html:hidden property="id" />
					<html:hidden property="redirectTo"/></td>
				</tr>
<!-- page title -->	
					

					
<!-- principal investigator -->	
			<tr>
				<td width="1%" align="left" class="black_ar">
					<span class="blue_ar_b">
						<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
					</span>
				</td>
				<td width="22%" align="left" class="black_ar">
				  <label for="principalInvestigatorId">
					<bean:message key="collectionprotocol.principalinvestigator" />
				   </label>
			    </td>
				<td align="left" class="black_new">
					<label>
						<html:select property="principalInvestigatorId" styleClass="formFieldSizedNew" styleId="principalInvestigatorId" size="1"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/>
						</html:select>
					&nbsp; 
					<logic:notEqual name="pageOf" value='${requestScope.query}'>
						<span class="smalllink">
							<html:link href="#" styleId="newUser" onclick="addNewAction('CollectionProtocolAddNew.do?addNewForwardTo=principalInvestigator&forwardTo=collectionProtocol&addNewFor=principalInvestigator')">
								<bean:message key="buttons.addNew" />
							</html:link></span>
	 					  </logic:notEqual>
						</label>
					</td>
				</tr>
<!-- protocol coordinators -->	
				<tr>
					<td align="left" class="black_ar">&nbsp;</td>
			        <td align="left" class="black_ar">
						<label for="protocolCoordinatorIds">
							<bean:message key="collectionprotocol.protocolcoordinator" />
						</label>
				    </td>
					<td align="left">
						<label>
							<html:select property="protocolCoordinatorIds" styleClass="formFieldSizedNew" styleId="protocolCoordinatorIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/>
							 </html:select>
							&nbsp;
		<logic:notEqual name="pageOf" value='requestScope.query'>
					            <span class="smalllink">
									<html:link href="#" styleId="newUser" onclick="addNewAction('CollectionProtocolAddNew.do?addNewForwardTo=protocolCoordinator&forwardTo=collectionProtocol&addNewFor=protocolCoordinator')">
										<bean:message key="buttons.addNew" />
									</html:link>
								</span>
		</logic:notEqual>
						</label>
					</td>
				</tr>
<!-- title -->						
				<tr>
						<td align="left" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
          <td align="left" class="black_ar">
				<label for="title">
								<bean:message key="collectionprotocol.protocoltitle" />
							</label>
		  </td>
          <td align="left">
		  <html:text styleClass="black_ar" maxlength='${requestScope.fieldWidth}'  size="30" styleId="title" property="title" readonly='${requestScope.readOnlyValue}' />
           </td>
					</tr>

<!-- short title -->						
					<tr>
						<td align="left" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
          <td align="left" class="black_ar">
			<label for="shortTitle">
				<bean:message key="collectionprotocol.shorttitle" />
			</label>
		  </td>
          <td align="left">
			<html:text styleClass="black_ar" maxlength="50"  size="30" styleId="shortTitle" property="shortTitle" readonly='${requestScope.readOnlyValue}' />
		  </td>
					</tr>
					
<!-- irb id -->						
					<tr>
						<td align="left" class="black_ar">&nbsp;</td>
          <td align="left" class="black_ar"><label for="irbID">
			<bean:message key="collectionprotocol.irbid" />
		  </label></td>
          <td align="left">
			<html:text styleClass="black_ar" maxlength="50"  size="30" styleId="irbID" property="irbID" readonly='${requestScope.readOnlyValue}' />
		   </td>
					</tr>

<!-- startdate -->						
					<tr>
						<td align="left" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
          <td align="left" class="black_ar"><label for="startDate">
			<bean:message key="collectionprotocol.startdate" />
		  </label></td>
          
		<td align="left" valign="top" >
				<ncombo:DateTimeComponent name="startDate" 
							  id="startDate"
							  formName="collectionProtocolForm"
							  month= '${requestScope.collectionProtocolMonth}'
							  year= '${requestScope.collectionProtocolYear}'
							  day= '${requestScope.collectionProtocolDay}'
							  value='${requestScope.currentCollectionProtocolDate}'
							  styleClass="black_ar"
				/>


		  <span class="grey_ar_s"><bean:message key="page.dateFormat" /></span></td>
					</tr>

<!-- enddate: Should be displayed only in case of edit -->
				<!-- bug id: 1565   -->	
				<logic:equal name="operation" value='${requestScope.edit}'>					
					<tr>
						<td align="left" class="black_ar" >&nbsp;</td>
						<td align="left" class="black_ar">
							<label for="endDate">
								<b><bean:message key="collectionprotocol.enddate" /></b>
							</label>
						</td>
			
						<td align="left" valign="top" >
	<ncombo:DateTimeComponent name="endDate" 
							  id="endDate"
							  formName="collectionProtocolForm"
							  month= '${requestScope.collectionProtocolEndDateMonth}'
							  year= '${requestScope.collectionProtocolEndDateYear}'
							  day= '${requestScope.collectionProtocolEndDateDay}'
							  value='${requestScope.collectionProtocolEndDate}'
							  styleClass="black_ar"
				/>


		  <span class="grey_ar_s"><bean:message key="page.dateFormat" /></span></td>
					</tr>
				</logic:equal>
<!-- Consent waived radio button -->
				<tr>
						<td align="left" class="black_ar">&nbsp;</td>
          <td align="left" class="black_ar">
			<label for="consentWaived">
								<bean:message key="consent.consentwaived" />		
   						    </label>
		  </td>
          <td align="left" class="black_ar">
			<label for="consentWaived">
								<bean:message key="consent.consentwaivedyes" />		
							</label>
						<html:radio property="consentWaived" styleId="consentWaived" value="true"/>
            
				<label for="consentWaived">
								<bean:message key="consent.consentwaivedno" />		
							</label>
				<html:radio property="consentWaived" styleId="consentWaived" value="false"/>
			  </td>
				</tr>

<!-- no of participants -->						
					<tr>
						<td align="left" class="black_ar">&nbsp;</td>
          <td align="left" class="black_ar">
			<label for="enrollment">
								<bean:message key="collectionprotocol.participants" />
							</label>
		  </td>
          <td align="left">
			<html:text styleClass="black_ar" maxlength="10"  size="30" styleId="enrollment" property="enrollment" readonly='${requestScope.readOnlyValue}' />
		  </td>
					</tr>

<!-- descriptionurl -->						
					<tr>
						<td align="left" class="black_ar">&nbsp;</td>
          <td align="left" class="black_ar">
			<label for="descriptionURL">
								<bean:message key="collectionprotocol.descriptionurl" />
							</label>
		  </td>
          <td align="left">
			<html:text styleClass="black_ar" maxlength="200"  size="30" styleId="descriptionURL" property="descriptionURL" readonly='${requestScope.readOnlyValue}' />
		  </td>
					</tr>

<!-- activitystatus -->	
					<logic:equal name="operation" value='${requestScope.edit}'>
					<tr>
						<td width="1%" align="left" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
						<td width="22%" align="left" class="black_ar">
							<label for="activityStatus">
								<bean:message key="site.activityStatus" />
							</label>
						</td>
						<td class="formFieldNoBordersSimple">
<!-- Mandar : 434 : for tooltip -->
							<html:select property="activityStatus" styleClass="formFieldSized12" styleId="activityStatus" size="1"  onchange="checkActivityStatus(this,'/ManageAdministrativeData.do')"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options name='${requestScope.activityStatusforJSP}' labelName='${requestScope.activityStatusforJSP}' />
							</html:select>
						</td>
					</tr>
					</logic:equal>
					
				</table> 	<!-- table 4 end -->
			</td>
          <tr>
			
	</table>
<table cellpadding="0" cellspacing="0" border="0" width = "100%" id="submittable">
			
		<tr>
			<td align="left" class="black_ar">&nbsp;</td>
				<td  align="left" class="black_ar">
				  <label>
					   	<html:checkbox property="aliqoutInSameContainer">
									<bean:message key="aliquots.storeAllAliquotes" />
								</html:checkbox>
				</label>
				</td>
			 </tr>
			 </br>
		 <tr>
			<td>&nbsp;&nbsp;</td>
				  <td class="buttonbg">
					<!-- <html:button styleClass="blue_ar_b" property="submitPage">
						<bean:message key="buttons.submit" />
					</html:button>
				 &nbsp;| -->

					 <html:button styleClass="blue_ar_b" property="forwardPage" onclick="openEventPage()" >
						Add Events >>
					</html:button>
				<!-- &nbsp;|

					<html:button styleClass="blue_ar_b" property="deletePage" onclick='${requestScope.deleteAction}'>
						<bean:message key="buttons.delete" />
					</html:button> -->
			  </td>
			</tr>
			
		</table>

<!-- Define Consent Page start  Virender Mehta-->

<%@ include file="/pages/content/ConsentTracking/DefineConsent.jsp" %>

<!-- Define Consent Page end Virender Mehta-->
</td></tr></table>
    
</html:form>
</body>