<%@page import="edu.wustl.catissuecore.ctrp.COPPAUtil"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ taglib uri="/WEB-INF/multiSelectUsingCombo.tld" prefix="mCombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ page import="java.util.List"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CollectionProtocolForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<head>
<SCRIPT LANGUAGE="JavaScript">
	var search1='`';
</script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/caTissueSuite.js"></script>
<script type="text/javascript" src="jss/dhtmlwindow.js"></script>
<script type="text/javascript" src="jss/modal.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/ctrp/ctrpCollectionProtocol.js"></script>
<script src="jss/calendarComponent.js" language="JavaScript" type="text/javascript"></script>


<script>var imgsrc="images/de/";</script>
<script language="JavaScript" type="text/javascript" src="javascripts/de/prototype.js"></script>
<script language="JavaScript" type="text/javascript" src="javascripts/de/scr.js"></script>
<script language="JavaScript" type="text/javascript" src="javascripts/de/combobox.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/ext-all.js"></script>
<script language="JavaScript" type="text/javascript" src="javascripts/de/ajax.js"></script>
<script language="JavaScript" type="text/javascript" src="/jss/multiselectUsingCombo.js"></script>
<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css" href="css/clinicalstudyext-all.css" />

<%
	String selectText = "--Select--";
	//CollectionProtocolForm newformName = (CollectionProtocolForm)request.getAttribute("formName");
	//System.out.println("formName   &&****#$#$#$#$$##$#$   :  "+ newformName.isGenerateLabel());
%>


<script>
Ext.onReady(function(){var myUrl= 'ClinicalDiagnosisData.do?';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_coord',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'coord'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});
</script>

<script>
var ctrpDirtyEditConfMessage = '<bean:message key="ctrp.dirtyedit.conf.message"/>';
var ctrpSyncConfMessage = '<bean:message key="ctrp.sync.conf.message"/>';
var ctrpUnlinkConfMessage = '<bean:message key="ctrp.unlink.conf.message"/>';

if('${requestScope.tabSel}'=="consentTab"){
	window.onload=consentPage;
}

function defineEvents()
{
	var action="DefineEvents.do?pageOf=pageOfDefineEvents&operation=${requestScope.operation}";
	document.forms[0].action=action;
	document.forms[0].submit();
}

function updateCPTree()
{
  window.parent.frames['CPTreeView'].location="ShowCollectionProtocol.do?operation=${requestScope.operation}";
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
<LINK href="css/calanderComponent.css" type="text/css" rel="stylesheet">
<html:form action='${requestScope.formName}' styleId="CollectionProtocolForm">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                   <td>
				   <%@ include file="/pages/content/common/ActionErrors.jsp" %>
				   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td valign="bottom" id="collectionProtocolTab" onclick="collectionProtocolPage()"><img src="images/uIEnhancementImages/cp_details.gif" alt="Collection Protocol Details" width="174" height="20" /><a href="#"></a></td>
                      <td valign="bottom" onclick="consentPage()" id="consentTab"><a href="#"><img src="images/uIEnhancementImages/cp_consents1.gif" alt="Consents" width="94" height="20" border="0" /></a></td>
                      <td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/cp_privileges1.gif" alt="Privileges" width="94" height="20" border="0" onclick="showAssignPrivilegePage('${requestScope.operation}')"></a></td>
                      <td width="85%" valign="bottom" class="cp_tabbg">&nbsp;</td>
                    </tr>
                </table>
                </td>
              </tr>
              <tr>
                <td class="cp_tabtable" colspan="6" >
					<br>
					<table width="100%" border="0" cellpadding="3" cellspacing="0" id="table1">
					    <html:hidden property="operation"/>
						<html:hidden property="submittedFor"/>
						<html:hidden property="onSubmit"/>
						<html:hidden property="id" />
					    <html:hidden property="redirectTo"/>

						<html:hidden property="sequenceNumber" />
						<html:hidden property="type" />
						<html:hidden property="studyCalendarEventPoint" />
						<html:hidden property="parentCollectionProtocolId" />
						<html:hidden property="remoteId" styleId="remoteId"/>
						<html:hidden property="remoteManagedFlag" styleId="remoteManagedFlag" />
						<html:hidden property="dirtyEditFlag" styleId="dirtyEditFlag" />
						<html:hidden property="operation" styleId="operation" />
					<script>
						//alert(document.getElementById('generateLabel').value);
					</script>
                      <tr>
                        <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
                        <td width="30%" align="left" class="black_ar"><bean:message key="collectionprotocol.principalinvestigator" /> </td>
                 		<td width="69%" align="left">
                     		<label>
							<c:choose>
									<c:when test="${(isCOPPAEnabled == true)}">
	                    				<html:select property="principalInvestigatorId" styleClass="formFieldSizedNew" styleId="principalInvestigatorId" size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                       						<html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/>
                       					</html:select>
										<!--
											<input property="displayPrincipalInvestigatorId" type="text" id="displayPrincipalInvestigatorId" name="displayPrincipalInvestigatorId" size="1" onmouseover="showTip(this.id)"/>
											<input property="principalInvestigatorId" type="hidden" id="principalInvestigatorId" name="principalInvestigatorId"/>
											<input property="remoteUserId" type="hidden" id="remoteUserId" name="remoteUserId"/>
										  -->
									</c:when>
									<c:otherwise>
                       					<html:select property="principalInvestigatorId" styleClass="formFieldSizedNew" styleId="principalInvestigatorId" size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
                       						<html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/>
                       					</html:select>
									</c:otherwise>
							</c:choose>
                 					&nbsp;
                 					<html:link href="#" styleId="newUser" styleClass="view"onclick="addNewAction('CollectionProtocolAddNew.do?addNewForwardTo=principalInvestigator&forwardTo=collectionProtocol&addNewFor=principalInvestigator')">
                 						<bean:message key="buttons.addNew" />
                 					</html:link>
                			</label>
                 		</td>
					  </tr>
                      <tr>
                        <td align="center" class="black_ar">&nbsp;</td>
                        <td align="left" valign="top" class="black_ar"><label for="coordinatorIds"><bean:message key="collectionprotocol.protocolcoordinator" /></label></td>
                        <td align="left"><label><html:select property="coordinatorIds" styleClass="formFieldSizedNew" styleId="coordinatorIds" size="4" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"><html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/></html:select>&nbsp;<html:link href="#" styleId="newUser" styleClass="view"onclick="addNewAction('CollectionProtocolAddNew.do?addNewForwardTo=protocolCoordinator&forwardTo=collectionProtocol&addNewFor=protocolCoordinator')"><bean:message key="buttons.addNew" /></html:link></label></td>
                      </tr>
                      <tr>
                        <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0"/></td>
                        <td align="left" class="black_ar"><bean:message key="collectionprotocol.protocoltitle" /></td>
						<td align="left">
						<table><tr>
							<td align="left">
						<!--  Added by geeta -->
							<%
							if(Variables.isCPTitleChange){
							%>
								<html:textarea styleClass="black_ar" cols="28" rows="3" styleId="title" property="title" readonly='${requestScope.readOnlyValue}' />
							<%}else{%>
								<html:text styleClass="black_ar" maxlength='${requestScope.fieldWidth}'  size="30" styleId="title" property="title" readonly='${requestScope.readOnlyValue}' />
							<%}%>
                     	     </td>
                     	     <td>
	                        <c:if test="${(isCOPPAEnabled == true)}">
								<div id="findUserDiv">
									<html:link href="#" styleClass="view" styleId="lookupNCI" titleKey="ctrp.protocol.check.coppa.message"
										onclick="openLinkProtocolWindow('${operation}','${pageOf}');">
										<bean:message key="ctrp.protocol.check.coppa.text" />
									</html:link>
								</div>
								<div id="editLocalDiv" style="display:none">
									<img hspace="0" height="16" width="16" vspace="0" title="NCI enterprise entity managed remotely" src="images/uIEnhancementImages/nci_remote.png">
									<a id="enableLocalChanges" class="viewprom" onclick="enableLocalChangesFunc();" href="#"><bean:message key="ctrp.edit.link.text" /></a>
									&nbsp;
									<a id="revmoeRemoteLink" class="viewprom" onclick="removeRemoteLinkFunc();" href="#"><bean:message key="ctrp.edit.unlink.text" /></a>
								</div>
								<div id="syncRemoteDiv" style="display:none">
									<img hspace="0" height="15" width="15" vspace="0" title="NCI enterprise entity managed locally" src="images/uIEnhancementImages/nci_remote_dirty.png">
									<a id="syncRemoteChangesLink" class="viewprom" onclick="syncRemoteChangesFunc();" href="#"><bean:message key="ctrp.edit.sync.text" /></a>
									&nbsp;
									<a id="removeRemoteLink" class="viewprom" onclick="removeRemoteLinkFunc();" href="#"><bean:message key="ctrp.edit.unlink.text" /></a>
								</div>
								<div id="storeLoadingDiv" style="display:none">
									<img hspace="0" height="15" width="15" vspace="0" title="Loading remote data" src="images/loading.gif">
									<span class="black_ar"><bean:message key="ctrp.load.message.text" /></span>
								</div>
							</c:if>
	                    	</td>
	                    	</tr></table>
                     	</td>
                      </tr>
                      <tr>
                        <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
                        <td align="left" class="black_ar"><bean:message key="collectionprotocol.shorttitle" /> </td>
                        <td align="left"><html:text styleClass="black_ar" maxlength="50"  size="30" styleId="shortTitle" property="shortTitle" readonly='${requestScope.readOnlyValue}' /></td>
                      </tr>
                       <tr>
                        <td align="center" class="black_ar">&nbsp;</td>
						 <td align="left" class="black_ar"><label for="irbID"><bean:message key="collectionprotocol.irbid"/></label></td>
						<td align="left"><html:text styleClass="black_ar" maxlength="50"  size="30" styleId="irbID" property="irbID" readonly='${requestScope.readOnlyValue}' />
                        </td>
                      </tr>
                      <tr>
                        <td width="1%" align="center" class="black_ar">&nbsp;</td>
                        <td width="30%" align="left" class="black_ar"><bean:message key="collectionprotocol.irbsite" /> </td>
                        <td width="69%" align="left"><label><html:select property="irbSiteId" styleClass="formFieldSizedNew" styleId="irbSiteId" size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"><html:options collection='${requestScope.siteListForJSP}' labelProperty="name" property="value"/></html:select></label></td>
                     </tr>
                      <tr>
                        <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
                        <td align="left" class="black_ar"><label for="startDate"><bean:message key="collectionprotocol.startdate" /> </label></td>
                        <td align="left" valign="top"><ncombo:DateTimeComponent name="startDate"
							  id="startDate"
							  formName="collectionProtocolForm"
							  month= '${requestScope.collectionProtocolMonth}'
							  year= '${requestScope.collectionProtocolYear}'
							  day= '${requestScope.collectionProtocolDay}'
							  pattern="<%=CommonServiceLocator.getInstance().getDatePattern() %>"
							  value='${requestScope.currentCollectionProtocolDate}'
							  styleClass="black_ar"
						/>
                          <span class="grey_ar_s"><bean:message key="page.dateFormat" /></span></td>
                      </tr>
                      <tr>
                        <td align="center" class="black_ar">&nbsp;</td>
                        <td align="left" class="black_ar"><label for="consentWaived"><bean:message key="consent.consentwaived" /></label></td>
                        <td align="left" class="black_ar"><html:radio property="consentWaived" styleId="consentWaived" value="true"/>&nbsp;<label for="consentWaived"><bean:message key="consent.consentwaivedyes" /></label>&nbsp;&nbsp;<html:radio property="consentWaived" styleId="consentWaived" value="false"/><label for="consentWaived">&nbsp;<bean:message key="consent.consentwaivedno" /></label></td>
                      </tr>
                      <tr>
                        <td align="center" class="black_ar">&nbsp;</td>
                        <td align="left" class="black_ar"><bean:message key="collectionprotocol.participants" /> </td>
                        <td align="left"><html:text styleClass="black_ar" maxlength="10"  size="30" styleId="enrollment" property="enrollment" readonly='${requestScope.readOnlyValue}' /></td>
                      </tr>
                      <tr>
                        <td align="center" class="black_ar">&nbsp;</td>
                        <td align="left" class="black_ar"><label for="departmentId"><bean:message key="collectionprotocol.descriptionurl" /> </label></td>
                        <td align="left"><html:text styleClass="black_ar" maxlength="200"  size="30" styleId="descriptionURL" property="descriptionURL" readonly='${requestScope.readOnlyValue}' />
                        </td>
                      </tr>
                      <logic:equal name='${requestScope.operation}' value="edit">
						<tr>
							<td align="center" class="black_ar">&nbsp;</td>
							<td align="left" class="black_ar">
								<bean:message key="site.activityStatus" />
							</td>
							<td align="left" class="black_ar">
								<autocomplete:AutoCompleteTag
								property="activityStatus"
								optionsList='${activityStatusList}'
								initialValue='${collectionProtocolForm.activityStatus}'
								styleClass="black_ar" size="27"
								onChange='${strCheckStatus}'/>
							</td>
						</tr>
					</logic:equal>

						<tr>

					 	  <td align="center" class="black_ar">&nbsp;</td>

						  <td align="left" class="black_ar">
					  		<bean:message key="specimenCollectionGroup.clinicalDiagnosis" />
					   	  </td>

						  <td align="left" class="black_ar">
					    	 <mCombo:multiSelectUsingCombo
								identifier="coord"
								size="20"
								styleClass="black_ar_new"
								addNewActionStyleClass="black_ar_new"
								addButtonOnClick="moveOptions('coord','protocolCoordinatorIds', 'add')"
								removeButtonOnClick="moveOptions('protocolCoordinatorIds','coord', 'edit')"
								selectIdentifier="protocolCoordinatorIds"
								collection='<%=(List)request.getAttribute("selectedCoordinators")%>'
								numRows="4" />
					   	  </td>

						</tr>
<!------------------------------------------------------------------------>

<%
	if(Variables.isTemplateBasedLblGeneratorAvl)
	{
%>
						<tr>
							<td align="center" class="black_ar">&nbsp;</td>
							<td align="left" class="black_ar">Parent Specimen Label Format</td>
							<td>

						<html:text styleClass="black_ar" maxlength="255"  size="108" styleId="specimenLabelFormat" property="specimenLabelFormat" />

							</td>
						</tr>
						<tr>
							<td align="center" class="black_ar">&nbsp;</td>
							<td align="left" class="black_ar">Derivative Specimen Label Format</td>
							<td>

						<html:text styleClass="black_ar" maxlength="255"  size="108" styleId="derivativeLabelFormat" property="derivativeLabelFormat" />

							</td>
						</tr>
						<tr>
							<td align="center" class="black_ar">&nbsp;</td>
							<td align="left" class="black_ar">Aliquot Specimen Label Format</td>
							<td>

						<html:text styleClass="black_ar" maxlength="255"  size="108" styleId="aliquotLabelFormat" property="aliquotLabelFormat" />

							</td>
						</tr>
<%}%>
							<tr height="8">
						<td colspan="3"/>
					  </tr>
						<tr>
							<td align="center" class="black_ar">&nbsp;</td>
							<td align="left" class="black_ar"><label>Store all aliquot(s) in same container?</label>&nbsp;&nbsp;&nbsp;&nbsp;</td>
							<td align="left" class="black_ar">
							<html:radio property="aliqoutInSameContainer" styleId="aliqoutInSameContainer" value="true"/>&nbsp;<bean:message key="consent.consentwaivedyes" />&nbsp;&nbsp;<html:radio property="aliqoutInSameContainer" styleId="aliqoutInSameContainer" value="false"/>&nbsp;<label for="consentWaived"><bean:message key="consent.consentwaivedno" /></label>
							</td>

						</tr>
<!------------------------------------------------------------------------->

					  <tr height="10">
						<td colspan="3"/>
					  </tr>
					 </table>



					<%@ include file="/pages/content/ConsentTracking/DefineConsent.jsp" %>
                </td>
              </tr>
            </table>
			</html:form>

<script>
if(document.getElementById("errorRow")!=null && document.getElementById("errorRow").innerHTML.trim()!="")
{
	  window.top.document.getElementById("errorRow").innerHTML = "";
}
</script>
<script>
//call after page loaded
window.onload=function (){
		markupRemoteFields();
		var isCOPPAEnabled = <%=COPPAUtil.isCOPPAEnabled()%>;
		if (isCOPPAEnabled){
//  			Ext.onReady(populateUserExtCombobox);
		}
	}

</script>