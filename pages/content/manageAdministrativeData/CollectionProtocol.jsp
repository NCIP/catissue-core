<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>

<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<SCRIPT LANGUAGE="JavaScript">
	var search1='`';
</script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/caTissueSuite.js"></script>
<script src="jss/calendarComponent.js" language="JavaScript" type="text/javascript"></script>
<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>

<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css" href="css/clinicalstudyext-all.css" />

<script>var imgsrc="/de/images/";</script>
<script language="JavaScript" type="text/javascript" src="de/jss/prototype.js"></script>
<script language="JavaScript" type="text/javascript" src="de/jss/scr.js"></script>
<script language="JavaScript" type="text/javascript" src="de/jss/combobox.js"></script>
<script language="JavaScript" type="text/javascript" src="de/jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript" src="de/jss/ext-all.js"></script>
<script language="JavaScript" type="text/javascript" src="de/jss/combos.js"></script>
<script language="JavaScript" type="text/javascript" src="de/jss/ajax.js"></script>


<%
	String selectText = "--Select--";
%>


<script>Ext.onReady(function(){var myUrl= 'ClinicalDiagnosisData.do?';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_coord',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'coord'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});</script>

 

<script>

Ext.onReady(function(){

      var myUrl= 'ClinicalDiagnosisData.do?';

      var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),

      reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});

      var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'principalInvestigatorId',displayField:'excerpt',valueField: 'id',typeAhead: 'true',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',lazyInit:'true',triggerAction: 'all',minChars : 3,emptyText:'<%=selectText%>',selectOnFocus:'true',applyTo: 'txtprincipalInvestigatorId' });

      var firsttimePI="true"; 

      combo.on("expand", function() {

            if(Ext.isIE || Ext.isIE7)

            {

                  combo.list.setStyle("width", "250");

                  combo.innerList.setStyle("width", "250");

            }

            else

            {

                  combo.list.setStyle("width", "auto");

                  combo.innerList.setStyle("width", "auto");

            }           

            },          {single: true}); 

            ds.on('load',function(){

                  if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase()))

                  {combo.typeAheadDelay=50;

                  } 

                  else

                  {combo.typeAheadDelay=60000}

                  });

            <%    /*if (opr.equals(Constants.EDIT) ||  (showErrMsg  && piId!=0) || (deloperation && piId!=0) )

                  {*/

 

            %>    

                  ds.load({params:{start:0, limit:9999,query:''}});

            ds.on('load',function(){

                        

                               if(firsttimePI == "true")

                              { combo.setValue('',false); firsttimePI="false";}

            });

            <%

                  //}

            %>                                  

                  });

</script>



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

                      <tr>
                        <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
                        <td width="22%" align="left" class="black_ar"><bean:message key="collectionprotocol.principalinvestigator" /> </td>
                        <td width="77%" align="left"><label><html:select property="principalInvestigatorId" styleClass="formFieldSizedNew" styleId="principalInvestigatorId" size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"><html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/></html:select>&nbsp;<html:link href="#" styleId="newUser" styleClass="view"onclick="addNewAction('CollectionProtocolAddNew.do?addNewForwardTo=principalInvestigator&forwardTo=collectionProtocol&addNewFor=principalInvestigator')"><bean:message key="buttons.addNew" /></html:link></label></td>
                     </tr>
                      <tr>
                        <td align="center" class="black_ar">&nbsp;</td>
                        <td align="left" valign="top" class="black_ar"><label for="coordinatorIds"><bean:message key="collectionprotocol.protocolcoordinator" /></label></td>
                        <td align="left"><label><html:select property="coordinatorIds" styleClass="formFieldSizedNew" styleId="coordinatorIds" size="4" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"><html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/></html:select>&nbsp;<html:link href="#" styleId="newUser" styleClass="view"onclick="addNewAction('CollectionProtocolAddNew.do?addNewForwardTo=protocolCoordinator&forwardTo=collectionProtocol&addNewFor=protocolCoordinator')"><bean:message key="buttons.addNew" /></html:link></label></td>
                      </tr>
                      <tr>
                        <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0"/></td>
                        <td align="left" class="black_ar"><bean:message key="collectionprotocol.protocoltitle" /> </td>

						<!--  Added by geeta --> 
						<% 
						if(Variables.isCPTitleChange){
						%>
							<td align="left"> <html:textarea styleClass="black_ar" cols="28" rows="3" styleId="title" property="title" readonly='${requestScope.readOnlyValue}' /></td>
						<%}else{%>
							<td align="left"> <html:text styleClass="black_ar" maxlength='${requestScope.fieldWidth}'  size="30" styleId="title" property="title" readonly='${requestScope.readOnlyValue}' /></td>
						<%}%>
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
                        <td align="left" class="black_ar"><label for="consentWaived"><bean:message key="consent.consentwaivedyes" /></label><html:radio property="consentWaived" styleId="consentWaived" value="true"/>&nbsp;&nbsp;<label for="consentWaived"><bean:message key="consent.consentwaivedno" /></label><html:radio property="consentWaived" styleId="consentWaived" value="false"/></label></td>
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
					    	 <dynamicExtensions:multiSelectUsingCombo/>
					   	  </td>
						
						</tr>

					 </table>

					  
					  <table cellpadding="0" cellspacing="0" border="0" width = "100%" id="submittable">
                      <tr>
                        <td class="black_ar"><label><html:checkbox property="aliqoutInSameContainer"><bean:message key="aliquots.storeAllAliquotes" /></html:checkbox></label><br><br></td>
                      </tr>
                      </table>
					<%@ include file="/pages/content/ConsentTracking/DefineConsent.jsp" %>
                </td>
              </tr>
            </table>
			</html:form>