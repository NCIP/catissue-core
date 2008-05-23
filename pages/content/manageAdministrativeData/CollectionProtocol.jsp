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

<SCRIPT LANGUAGE="JavaScript">



//Consent Tracking Module Virender Mehta(Start)
	//This Function will add more consent Tier 
	function addConsentTier()
	{		
		var val = parseInt(document.forms[0].consentTierCounter.value);
		val = val + 1;
		document.forms[0].consentTierCounter.value = val;
		var rowCount = document.getElementById('innertable').rows.length;
		var createRow = document.getElementById('innertable').insertRow(1);
		var createSerialNo=createRow.insertCell(0);
		var createCheckBox=createRow.insertCell(1);
		var createTextArea=createRow.insertCell(2);
		
		var iCount = rowCount-1;
		var consentName="consentValue(ConsentBean:"+iCount+"_statement)";
		var consentKey="consentValue(ConsentBean:"+iCount+"_consentTierID)";
			
		createSerialNo.className="tabrightmostcell";
		createSerialNo.setAttribute('align','right');
		createCheckBox.className="formField";
		createCheckBox.setAttribute('align','center');
		createTextArea.className="formField";
		var sname = "<input type='hidden' id='" + consentKey + "'>";				
		createSerialNo.innerHTML=rowCount+".";
		createCheckBox.innerHTML="<input type='checkbox' name='consentcheckBoxs'id='check"+iCount+"'>";
		createTextArea.innerHTML= sname+"<textarea rows='2'class='formFieldSized' style='width:90%;' name="+consentName+"></textarea>";
	}
	
	//On selecting Select All CheckBox all the associted check box wiil be selected
	function checkAll(chkInstance)
	{
		var chkCount= document.getElementsByName('consentcheckBoxs').length;
		for (var i=0;i<chkCount;i++)
		{
			var elements = document.getElementsByName('consentcheckBoxs');
			elements[i].checked = chkInstance.checked;
		}
	}

	//This function will delete the selected consent Tier
	function deleteSelected()
	{
		var rowIndex = 0;	
		var rowCount=document.getElementById('innertable').rows.length;
		var removeButton = document.getElementsByName('removeButton');
		
		/** creating checkbox name**/
		var chkBox = document.getElementsByName('consentcheckBoxs');
		var lengthChk=chkBox.length;
		var j = 0;
		for(var i=0;i<lengthChk;i++)
		{
			if(chkBox[j].checked==true)
			{
				var gettable = document.getElementById('innertable');
				var currentRow = chkBox[j].parentNode.parentNode;
				rowIndex = currentRow.rowIndex;
				gettable.deleteRow(rowIndex);
			}
			else
			{
				j++;
			}	
		}
		var j = chkBox.length;
		for(var i=0;i<chkBox.length;i++)
		{
			var currentRow = chkBox[i].parentNode.parentNode;
			currentRow.childNodes[0].innerHTML=(j-i)+".";
		}		
	}	
	
	//This function will the called while switching between Tabs
	function switchToTab(selectedTab)
	{
		var displayKey="block";
		
		if(!document.all)
			displayKey="table";
			
		var displayTable=displayKey;
		var tabSelected="none";
		
		if(selectedTab=="collectionProtocolTab")
		{
			tabSelected=displayKey;
			displayTable="none";
		}	
		
		var display=document.getElementById('table1');
		display.style.display=tabSelected;
		
			
		var display4=document.getElementById('consentTierTable');
		display4.style.display=displayTable;	

		var display5=document.getElementById('submittable');
		display5.style.display=tabSelected;


		var collectionTab=document.getElementById('collectionProtocolTab');
		var consentTab=document.getElementById('consentTab');
		
		if(selectedTab=="collectionProtocolTab")
		{
			updateTab(collectionTab,consentTab);
		}
		else		
		{
			updateTab(consentTab,collectionTab);
		}
		
	}
	
	//This function is for changing the behaviour of TABs
	function updateTab(tab1, tab2)
	{
		tab1.onmouseover=null;
		tab1.onmouseout=null;
		tab1.className="tabMenuItemSelected";
	
		tab2.className="tabMenuItem";
		tab2.onmouseover=function() { changeMenuStyle(this,'tabMenuItemOver'),showCursor();};
		tab2.onmouseout=function() {changeMenuStyle(this,'tabMenuItem'),hideCursor();};
	}

	//On calling this function the tab will be switched to CollectionProtocol Page
	function collectionProtocolPage()
	{
		switchToTab("collectionProtocolTab");
	}

	//On calling this function the tab will be switched to Consent Page	
	function consentPage()
	{
		switchToTab("consentTab");
	}
//	Consent Tracking Module Virender Mehta (End)


//Add Bulk Specimen Virender(Start)
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

//Add Bulk Specimen

//-->
</SCRIPT>

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

<logic:equal name="tab" value="null">
<body onload="consentPage()">
</logic:equal>
<logic:notEqual name="tab" value="null">
<body >
</logic:notEqual>

        
<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action='${requestScope.formName}'>


<logic:equal name="flagforPageView" value="true">
 <table summary="" cellpadding="1" cellspacing="0" border="0" height="20" class="tabPage" width="700">
	<tr>
 		<td height="20" width="9%" nowrap class="tabMenuItemSelected" id="collectionProtocolTab" onclick="collectionProtocolPage()">
 			<bean:message key="cpbasedentry.collectionprotocol" /> 
 		</td>

        <td height="20" width="10%" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="consentPage()" id="consentTab">
		    <bean:message key="consents.consents" />        
        </td>
		<td height="20" width="10%" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="defineEvents()" id="consentTab">
	    	<bean:message key="cpbasedentry.defineevents" /> 
        </td>
		<td height="20" width="10%" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="viewSummary()" id="summaryTab">
	    	<bean:message key="cpbasedentry.viewsummary" /> 
        </td>
     <td width="600" class="tabMenuSeparator" colspan="1">&nbsp;</td>
   </tr>
   <tr>
   <td class="tabField" colspan="6"/>
</logic:equal>
<!-- table 1 -->
<table summary="" cellpadding="0" cellspacing="0" border="0" id="table1" class="contentPage" width="100%">
<!-- NEW COLLECTIONPROTOCOL ENTRY BEGINS-->

		<tr>
		<td colspan="3">
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
					<tr>
						<td class="formTitle" height="20" width="100%" colspan="6">
							<bean:message key='${requestScope.title}'/>							
						</td>
					</tr>

					
<!-- principal investigator -->	
					<tr>
						<td class="formFieldNoBordersSimple" width="5">*</td>
						<td class="formFieldNoBordersSimple">
							<label for="principalInvestigatorId">
								<b><bean:message key="collectionprotocol.principalinvestigator" /></b>
							</label>
						</td>
						
						<td class="formFieldNoBordersSimple">
							<html:select property="principalInvestigatorId" styleClass="formFieldSized" styleId="principalInvestigatorId" size="1"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/>
							</html:select>
							&nbsp;
							<logic:notEqual name="pageOf" value='${requestScope.query}'>
							<html:link href="#" styleId="newUser" onclick="addNewAction('CollectionProtocolAddNew.do?addNewForwardTo=principalInvestigator&forwardTo=collectionProtocol&addNewFor=principalInvestigator')">
								<bean:message key="buttons.addNew" />
							</html:link>
	 						</logic:notEqual>
						</td>
					</tr>

<!-- protocol coordinators -->	
					<tr>
						<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
						<td class="formFieldNoBordersSimple">
							<label for="protocolCoordinatorIds">
								<bean:message key="collectionprotocol.protocolcoordinator" />
							</label>
						</td>
						
						<td class="formFieldNoBordersSimple">
							<html:select property="protocolCoordinatorIds" styleClass="formFieldSized" styleId="protocolCoordinatorIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection='${requestScope.userListforJSP}' labelProperty="name" property="value"/>
							</html:select>
							&nbsp;
							<logic:notEqual name="pageOf" value='requestScope.query'>
							<html:link href="#" styleId="newUser" onclick="addNewAction('CollectionProtocolAddNew.do?addNewForwardTo=protocolCoordinator&forwardTo=collectionProtocol&addNewFor=protocolCoordinator')">
								<bean:message key="buttons.addNew" />
							</html:link>
	 						</logic:notEqual>
						</td>
					</tr>

<!-- title -->						
					<tr>
						<td class="formFieldNoBordersSimple" width="5">*</td>
						<td class="formFieldNoBordersSimple">
							<label for="title">
								<b><bean:message key="collectionprotocol.protocoltitle" /></b>
							</label>
						</td>
						<td class="formFieldNoBordersSimple" colspan=2>
						<!--Mandar 26-apr-06 Bug 874 : increase the width of title -->
						<html:text styleClass="formFieldSized" maxlength='${requestScope.fieldWidth}'  size="30" styleId="title" property="title" readonly='${requestScope.readOnlyValue}' />
						</td>
					</tr>

<!-- short title -->						
					<tr>
						<td class="formFieldNoBordersSimple" width="5">*</td>
						<td class="formFieldNoBordersSimple">
							<label for="shortTitle">
							<b>	<bean:message key="collectionprotocol.shorttitle" /></b>
							</label>
						</td>
						<td class="formFieldNoBordersSimple" colspan=2>
							<html:text styleClass="formFieldSized" maxlength="50"  size="30" styleId="shortTitle" property="shortTitle" readonly='${requestScope.readOnlyValue}' />
						</td>
					</tr>
					
<!-- irb id -->						
					<tr>
						<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
						<td class="formFieldNoBordersSimple">
							<label for="irbID">
								<bean:message key="collectionprotocol.irbid" />
							</label>
						</td>
						<td class="formFieldNoBordersSimple" colspan=2>
							<html:text styleClass="formFieldSized" maxlength="50"  size="30" styleId="irbID" property="irbID" readonly='${requestScope.readOnlyValue}' />
						</td>
					</tr>

<!-- startdate -->						
					<tr>
						<td class="formFieldNoBordersSimple" width="5">*</td>
						<td class="formFieldNoBordersSimple">
							<label for="startDate">
							<b>	<bean:message key="collectionprotocol.startdate" /></b>
							</label>
						</td>
			
						<td class="formFieldNoBordersSimple" colspan=2>
				<ncombo:DateTimeComponent name="startDate" 
							  id="startDate"
							  formName="collectionProtocolForm"
							  month= '${requestScope.collectionProtocolMonth}'
							  year= '${requestScope.collectionProtocolYear}'
							  day= '${requestScope.collectionProtocolDay}'
							  value='${requestScope.currentCollectionProtocolDate}'
							  styleClass="formDateSized10"
				/>
				<bean:message key="page.dateFormat" />&nbsp;
				
						</td>
					</tr>

<!-- enddate: Should be displayed only in case of edit -->
				<!-- bug id: 1565   -->	
				<logic:equal name="operation" value='${requestScope.edit}'>					
					<tr>
						<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
						<td class="formFieldNoBordersSimple">
							<label for="endDate">
								<b><bean:message key="collectionprotocol.enddate" /></b>
							</label>
						</td>
			
						 <td class="formFieldNoBordersSimple" colspan=2>
  						 <div id="enddateDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
						 <html:text styleClass="formDateSized15" maxlength="10"  size="35" styleId="endDate" property="endDate" readonly="true" />
						 &nbsp;<bean:message key="page.dateFormat" />&nbsp;
						</td>
					</tr>
				</logic:equal>
<!-- Consent waived radio button -->
				<tr>
						<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
						<td class="formFieldNoBordersSimple">
							<label for="consentWaived">
								<bean:message key="consent.consentwaived" />		
   						    </label>
						</td>
						<td class="formFieldNoBordersSimple">
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
						<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
						<td class="formFieldNoBordersSimple">
							<label for="enrollment">
								<bean:message key="collectionprotocol.participants" />
							</label>
						</td>
						<td class="formFieldNoBordersSimple" colspan=2>
							<html:text styleClass="formFieldSized" maxlength="10"  size="30" styleId="enrollment" property="enrollment" readonly='${requestScope.readOnlyValue}' />
						</td>
					</tr>

<!-- descriptionurl -->						
					<tr>
						<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
						<td class="formFieldNoBordersSimple">
							<label for="descriptionURL">
								<bean:message key="collectionprotocol.descriptionurl" />
							</label>
						</td>
						<td class="formFieldNoBordersSimple" colspan=2>
							<html:text styleClass="formFieldSized" maxlength="200"  size="30" styleId="descriptionURL" property="descriptionURL" readonly='${requestScope.readOnlyValue}' />
						</td>
					</tr>

<!-- activitystatus -->	
					<logic:equal name="operation" value='${requestScope.edit}'>
					<tr>
						<td class="formFieldNoBordersSimple" width="5">*</td>
						<td class="formFieldNoBordersSimple" >
							<label for="activityStatus">
								<bean:message key="site.activityStatus" />
							</label>
						</td>
						<td class="formFieldNoBordersSimple">
<!-- Mandar : 434 : for tooltip -->
							<html:select property="activityStatus" styleClass="formFieldSized10" styleId="activityStatus" size="1"  onchange="checkActivityStatus(this,'/ManageAdministrativeData.do')"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options name='${requestScope.activityStatusforJSP}' labelName='${requestScope.activityStatusforJSP}' />
							</html:select>
						</td>
					</tr>
					</logic:equal>
							
				</table> 	<!-- table 4 end -->
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr> <!-- SEPARATOR -->
</table>
<table cellpadding="0" cellspacing="0" border="0" width = "95%" id="submittable">
		<tr><td>&nbsp;&nbsp;</td></tr>		
			<tr>
			<td>&nbsp;&nbsp;</td>
				<td class="formFieldNoBordersSimple" style="font-size:12;" width="50%">
					<html:checkbox property="aliqoutInSameContainer">
						<bean:message key="aliquots.storeAllAliquotes" />
					</html:checkbox>
				</td>
						
				<td align="center">
					
					<html:button styleClass="actionButton" property="deletePage" onclick='${requestScope.deleteAction}'>
						<bean:message key="buttons.delete"/>
					</html:button>
				</td>
				<td align="right">
					<html:button styleClass="actionButton" property="submitPage" onclick="consentPage()">
						<bean:message key="consent.addconsents" /> >>
					</html:button>
				</td>

			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
			</tr>
</table>


<!-- Define Consent Page start  Virender Mehta-->

<%@ include file="/pages/content/ConsentTracking/DefineConsent.jsp" %>

<!-- Define Consent Page end Virender Mehta-->

</td></tr></table>
	
</html:form>
</body>