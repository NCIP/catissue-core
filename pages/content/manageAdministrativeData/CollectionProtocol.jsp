<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CollectionProtocolForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.domain.CollectionProtocol"%>


<%@ include file="/pages/content/common/AdminCommonCode.jsp" %>
<head>

<%
	List tissueSiteList = (List) request.getAttribute(Constants.TISSUE_SITE_LIST);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	List pathologyStatusList = (List) request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST);
	List predefinedConsentsList =(List)request.getAttribute(Constants.PREDEFINED_CADSR_CONSENTS);
    String operation = (String) request.getAttribute(Constants.OPERATION);
	String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
    
    String formName, pageView=operation,editViewButton="buttons."+Constants.EDIT;
	String currentCollectionProtocolDate="";
	CollectionProtocolForm form = (CollectionProtocolForm) request.getAttribute("collectionProtocolForm");
	if(form != null)
	{	
		currentCollectionProtocolDate = form.getStartDate();
		if(currentCollectionProtocolDate == null)
			currentCollectionProtocolDate = "";				
	}
	String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
	String appendingPath = "/CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol";
	if (reqPath != null)
		appendingPath = reqPath + "|/CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol";	
	if(!operation.equals("add"))
	{
		if(form != null)
		{
			appendingPath = "/CollectionProtocolSearch.do?operation=search&pageOf=pageOfCollectionProtocol&id="+form.getId() ;
		}
	}		
		
    boolean readOnlyValue, readOnlyForAll=false;
    if (operation.equals(Constants.EDIT))
    {
    	editViewButton="buttons."+Constants.VIEW;
    	formName = Constants.COLLECTIONPROTOCOL_EDIT_ACTION;
        readOnlyValue = false;
		if(pageOf.equals(Constants.QUERY))
			formName = Constants.QUERY_COLLECTION_PROTOCOL_EDIT_ACTION + "?pageOf="+pageOf;

    }
    else
    {
        formName = Constants.COLLECTIONPROTOCOL_ADD_ACTION;
        readOnlyValue = false;
    }
    

%>

<%@ include file="/pages/content/common/CommonScripts.jsp" %>

<SCRIPT LANGUAGE="JavaScript">
	var search1='`';
</script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>

<SCRIPT LANGUAGE="JavaScript">
<!--
	// functions for add more

	

// variable to count the outer blocks
var insno=1;
var tblColor = "#123456";
function addBlock(tmpdiv,tmpd0)
{
var d0 = document.getElementById(tmpd0);
var val = parseInt(document.forms[0].outerCounter.value);
		val = val + 1;
		document.forms[0].outerCounter.value = val;
		
		if(val%2 == 0)
			tblColor = "<%=Constants.EVEN_COLOR%>";
		else
			tblColor = "<%=Constants.ODD_COLOR%>";
	var z = d0.innerHTML;
	insno =val;
	var mm = z.indexOf('`');
	for(var cnt=0;cnt<mm;cnt++)
	{
		z = z.replace('`',insno);
		mm = z.indexOf('`');
	}
// --------21 mar start
	var r = new Array(); 
	r = document.getElementById(tmpdiv).rows;
	var q = r.length;
	var newRow=document.getElementById(tmpdiv).insertRow(0);
	var newCol=newRow.insertCell(0);
	newCol.innerHTML = z;

	var tb = document.getElementById("itable_"+val);
	tb.bgColor = tblColor;
// --------21 mar end

}

function addDiv(div,adstr)
{
	
	var x = div.innerHTML;
	div.innerHTML = div.innerHTML +adstr;
}

//  function to insert a row in the inner block
function insRow(subdivtag,iCounter,blockCounter)
{
	var cnt = document.getElementById(iCounter);
	var val = parseInt(cnt.value);
	val = val + 1;
	cnt.value = val;

	var sname = "";
	
	var r = new Array(); 
	r = document.getElementById(subdivtag).rows;
	var q = r.length;
	var x=document.getElementById(subdivtag).insertRow(1);
	
	//setSubDivCount(subdivtag);
	var subdivname = ""+ subdivtag;

	// srno
	var spreqno=x.insertCell(0)
	spreqno.className="tabrightmostcellAddMore";
	var rowno=(q);
	var srIdentifier = subdivname + "_SpecimenRequirement:" + rowno + "_id)";
	var cell1 = "<input type='hidden' name='" + srIdentifier + "' value='' id='" + srIdentifier + "'>";
	spreqno.innerHTML="" + rowno+"." + cell1;
	
	//type
	var spreqtype=x.insertCell(1)
	spreqtype.className="formFieldAddMore";
	sname="";
	objname = subdivname + "_SpecimenRequirement:" + rowno + "_specimenClass)";
	
	var objunit = subdivname + "_SpecimenRequirement:"+rowno+"_unitspan)";
	var specimenClassName = objname;
	var objsubtype = subdivname + "_SpecimenRequirement:"+rowno+"_specimenType)";
//Mandar : 434 : for tooltip
	sname = "<select name='" + objname + "' size='1' onchange=changeUnit('" + objname + "','" + objunit +"','"+ objsubtype +"') class='formFieldSized10' id='" + objname + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
	<%for(int i=0;i<specimenClassList.size();i++)
	{
		String specimenClassLabel = "" + ((NameValueBean)specimenClassList.get(i)).getName();
		String specimenClassValue = "" + ((NameValueBean)specimenClassList.get(i)).getValue();
	%>
		sname = sname + "<option value='<%=specimenClassValue%>'><%=specimenClassLabel%></option>";
	<%}%>
	sname = sname + "</select>";
	 
	spreqtype.innerHTML="" + sname;
	
	//subtype
	var spreqsubtype=x.insertCell(2)
	spreqsubtype.className="formFieldAddMore";
	sname="";
	objname = subdivname + "_SpecimenRequirement:"+rowno+"_specimenType)";
	var functionName = "onSubTypeChangeUnit('" + specimenClassName + "',this,'" + objunit + "')" ;
	
	sname= "<select name='" + objname + "' size='1' class='formFieldSized10' id='" + objname + "' onChange=" + functionName + " onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
	
	sname = sname + "<option value='-1'><%=Constants.SELECT_OPTION%></option>";

	sname = sname + "</select>"
	
	spreqsubtype.innerHTML="" + sname;
	
	//tissuesite
	var spreqtissuesite=x.insertCell(3)
	spreqtissuesite.className="formFieldAddMore";
	sname="";
	objname = subdivname + "_SpecimenRequirement:"+rowno+"_tissueSite)";
	
	sname = "<select name='" + objname + "' size='1' class='formFieldSized35' id='" + objname + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
	<%for(int i=0;i<tissueSiteList.size();i++)
	{%>
		sname = sname + "<option value='<%=((NameValueBean)tissueSiteList.get(i)).getValue()%>'><%=((NameValueBean)tissueSiteList.get(i)).getName()%></option>";
	<%}%>
	sname = sname + "</select>"
	var url = "ShowFramedPage.do?pageOf=pageOfTissueSite&cdeName=Tissue%20Site&propertyName="+objname;
	
	sname = sname + "<a href='#' onclick=javascript:NewWindow('" + url + "','name','250','330','no');return false>";
	sname = sname + "<img src='images\\Tree.gif' border='0' width='26' height='22' title='Tissue Site Selector'></a>";
	spreqtissuesite.innerHTML="" + sname;
	
	//pathologystatus
	var spreqpathologystatus=x.insertCell(4)
	spreqpathologystatus.className="formFieldAddMore";
	
	sname="";
	objname = subdivname + "_SpecimenRequirement:"+rowno+"_pathologyStatus)";
	
	sname="<select name='" + objname + "' size='1' class='formFieldSized10' id='" + objname + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
	<%for(int i=0;i<pathologyStatusList.size();i++)
	{%>
		sname = sname + "<option value='<%=((NameValueBean)pathologyStatusList.get(i)).getValue()%>'><%=((NameValueBean)pathologyStatusList.get(i)).getName()%></option>";
	<%}%>
	sname = sname + "</select>";
	
	spreqpathologystatus.innerHTML="" + sname;
	
	//qty
	var spreqqty=x.insertCell(5)
	spreqqty.className="formFieldAddMore";
	sname="";
	objname = subdivname + "_SpecimenRequirement:"+rowno+"_quantity_value)";

	sname="<input type='text' name='" + objname + "' value='0'  maxlength='10' class='formFieldSized5' id='" + objname + "'>"        	
	sname = sname + "&nbsp;<span id='" + objunit + "'>&nbsp;</span>"
					
	spreqqty.innerHTML="" + sname;
	
	//Fourth Cell
	var checkb=x.insertCell(6);
	checkb.className="formFieldAddMore";
	checkb.colSpan=2;
	sname="";
	var name = "chk_spec_"+ blockCounter +"_"+rowno;
	//var func = "enableButton(document.forms[0].deleteSpecimenReq,'ivl("+ blockCounter + ")','" +"chk_spec_" + blockCounter + "_ "+ "')";
	sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C'>"; // onClick=" + func + ">";
	checkb.innerHTML=""+sname;

}


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
		
		var display2=document.getElementById('table2'); 
		display2.style.display=tabSelected;
			
		var display3=document.getElementById('table3'); 
		display3.style.display=tabSelected;
			
		var display4=document.getElementById('table4');
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
		
		//var checkboxObject=document.getElementById('consentChecked');
		//if(checkboxObject.checked)
		//{
			//document.forms[0].consentChecked.value = true;
			switchToTab("consentTab");
		//}
	//	else
		//{
			//alert("Consent checkbox is not checked");
		//}
	}
//	Consent Tracking Module Virender Mehta (End)

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
<body>
        
<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action="<%=formName%>">

<%
if(pageView.equals("add") || pageView.equals("edit"))
{
%>
 <table summary="" cellpadding="1" cellspacing="0" border="0" height="20" class="tabPage" width="700">
	<tr>
 		<td height="20" width="9%" nowrap class="tabMenuItemSelected" onclick="collectionProtocolPage()" id="collectionProtocolTab">CollectionProtocol</td>

        <td height="20" width="10%" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="consentPage()" id="consentTab">
		    <bean:message key="consents.consents" />        
        </td>								
     <td width="600" class="tabMenuSeparator" colspan="1">&nbsp;</td>
   </tr>
   <tr>
	<td class="tabField" colspan="4">
<%
}
%>

<!-- table 1 -->
<table summary="" cellpadding="0" cellspacing="0" border="0" id="table1" class="contentPage" width="100%">
<!-- NEW COLLECTIONPROTOCOL ENTRY BEGINS-->

<!-- If operation is equal to edit or search but,the page is for query the identifier field is not shown -->

	<%--logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>"--%>
		<tr>
		<td colspan="3">
		<!-- table 4 -->
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td>
						<html:hidden property="operation" value="<%=operation%>" />
						<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
					</td>
					<td><html:hidden property="onSubmit"/></td>
				</tr>
				
				<tr>
					<td><html:hidden property="id" />
					<html:hidden property="redirectTo" value="<%=reqPath%>"/></td>
				</tr>
		
				<tr>
					<td class="formMessage" colspan="4">* indicates a required field</td>
				</tr>
<!-- page title -->	
					<tr>
						<td class="formTitle" height="20" width="100%" colspan="6">
							<%String title = "collectionprotocol."+pageView+".title";%>
							<bean:message key="<%=title%>"/>							
						</td>
					</tr>

					
<!-- principal investigator -->	
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="principalInvestigatorId">
								<bean:message key="collectionprotocol.principalinvestigator" />
							</label>
						</td>
						
						<td class="formField">
<!-- Mandar : 434 : for tooltip -->
							<html:select property="principalInvestigatorId" styleClass="formFieldSized" styleId="principalInvestigatorId" size="1"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
							</html:select>
							&nbsp;
							<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
							<html:link href="#" styleId="newUser" onclick="addNewAction('CollectionProtocolAddNew.do?addNewForwardTo=principalInvestigator&forwardTo=collectionProtocol&addNewFor=principalInvestigator')">
								<bean:message key="buttons.addNew" />
							</html:link>
	 						</logic:notEqual>
						</td>
					</tr>

<!-- protocol coordinators -->	
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="protocolCoordinatorIds">
								<bean:message key="collectionprotocol.protocolcoordinator" />
							</label>
						</td>
						
						<td class="formField">
<!-- Mandar : 434 : for tooltip -->
							<html:select property="protocolCoordinatorIds" styleClass="formFieldSized" styleId="protocolCoordinatorIds" size="4" multiple="true"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
							</html:select>
							&nbsp;
							<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
							<html:link href="#" styleId="newUser" onclick="addNewAction('CollectionProtocolAddNew.do?addNewForwardTo=protocolCoordinator&forwardTo=collectionProtocol&addNewFor=protocolCoordinator')">
								<bean:message key="buttons.addNew" />
							</html:link>
	 						</logic:notEqual>
						</td>
					</tr>

<!-- title -->						
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="title">
								<bean:message key="collectionprotocol.protocoltitle" />
							</label>
						</td>
						<td class="formField" colspan=2>
						<!--Mandar 26-apr-06 Bug 874 : increase the width of title -->
						<%
							String fieldWidth = Utility.getColumnWidth(CollectionProtocol.class,"title" );
						%>
							<html:text styleClass="formFieldSized" maxlength="<%= fieldWidth %>"  size="30" styleId="title" property="title" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- short title -->						
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="shortTitle">
								<bean:message key="collectionprotocol.shorttitle" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" maxlength="50"  size="30" styleId="shortTitle" property="shortTitle" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>
					
<!-- irb id -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="irbID">
								<bean:message key="collectionprotocol.irbid" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" maxlength="50"  size="30" styleId="irbID" property="irbID" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- startdate -->						
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="startDate">
								<bean:message key="collectionprotocol.startdate" />
							</label>
						</td>
			
						<td class="formField" colspan=2>
				<%
				if(currentCollectionProtocolDate.trim().length() > 0)
				{
					Integer collectionProtocolYear = new Integer(Utility.getYear(currentCollectionProtocolDate ));
					Integer collectionProtocolMonth = new Integer(Utility.getMonth(currentCollectionProtocolDate ));
					Integer collectionProtocolDay = new Integer(Utility.getDay(currentCollectionProtocolDate ));
										
				%>
				<ncombo:DateTimeComponent name="startDate"
							  id="startDate"
							  formName="collectionProtocolForm"
							  month= "<%= collectionProtocolMonth %>"
							  year= "<%= collectionProtocolYear %>"
							  day= "<%= collectionProtocolDay %>"
							  value="<%=currentCollectionProtocolDate %>"
							  styleClass="formDateSized10"
				/>
				<% 
					}
					else
					{  
				 %>
				<ncombo:DateTimeComponent name="startDate"
							  id="startDate"
							  formName="collectionProtocolForm"
							  styleClass="formDateSized10"
									/>
				<% 
					} 
				%> 
				<bean:message key="page.dateFormat" />&nbsp;
				
						</td>
					</tr>

<!-- enddate: Should be displayed only in case of edit -->
				<!-- bug id: 1565   -->	
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="endDate">
								<bean:message key="collectionprotocol.enddate" />
							</label>
						</td>
			
						 <td class="formField" colspan=2>
  						 <div id="enddateDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
						 <html:text styleClass="formDateSized15" maxlength="10"  size="35" styleId="endDate" property="endDate" readonly="true" />
						 &nbsp;<bean:message key="page.dateFormat" />&nbsp;
							<!-- a href="javascript:show_calendar('collectionProtocolForm.endDate',null,null,'MM-DD-YYYY');">
								<img src="images\calendar.gif" width=24 height=22 border=0></a -->

						 </td>
					</tr>
				</logic:equal>
<!-- Consent waived radio button -->
				<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="consentWaived">
								<bean:message key="consent.consentwaived" />		
   						    </label>
						</td>
						<td class="formLabel">
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
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="enrollment">
								<bean:message key="collectionprotocol.participants" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" maxlength="10"  size="30" styleId="enrollment" property="enrollment" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- descriptionurl -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="descriptionURL">
								<bean:message key="collectionprotocol.descriptionurl" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" maxlength="200"  size="30" styleId="descriptionURL" property="descriptionURL" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- activitystatus -->	
					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" >
							<label for="activityStatus">
								<bean:message key="site.activityStatus" />
							</label>
						</td>
						<td class="formField">
<!-- Mandar : 434 : for tooltip -->
							<html:select property="activityStatus" styleClass="formFieldSized10" styleId="activityStatus" size="1"  onchange="<%=strCheckStatus%>"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
							</html:select>
						</td>
					</tr>
					</logic:equal>
							
				</table> 	<!-- table 4 end -->
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr> <!-- SEPARATOR -->
</table>

<!-- Define Consent Page start  Virender Mehta-->

<%@ include file="/pages/content/ConsentTracking/DefineConsent.jsp" %>

<!-- Define Consent Page end Virender Mehta-->

<table summary="" cellpadding="0" cellspacing="0" border="0"  id ="table2" class="contentPage" width="100%">
	<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td class="formTitle">
							<b><bean:message key="collectionprotocol.eventtitle" /></b>
					</td>
					<td align="right" class="formTitle">		
							<html:button property="addCollectionProtocolEvents" styleClass="actionButton" onclick="addBlock('outerdiv','d1')">Add More</html:button>
							<html:hidden property="outerCounter"/>	
					</td>
					<td class="formTitle" align="Right">
						<html:button property="deleteCollectionProtocolEvents" styleClass="actionButton" onclick="deleteChecked('outerdiv','CollectionProtocol.do?operation=<%=operation%>&pageOf=pageOfCollectionProtocol&status=true&button=deleteCollectionProtocolEvents',document.forms[0].outerCounter,'chk_proto_',true)" disabled="true">
							<bean:message key="buttons.delete"/>
						</html:button>
					</td>
			  </tr>
		   </table>
		</td>
	</tr>
</table>


<!--  outermostdiv start --><!-- outer div tag  for entire block -->

<table width="100%" id="table3">
<tbody id="outerdiv">
<tr>
<td>

<%! Map map; %>
<%
		int maxCount=1;
		int maxIntCount=1;
				
		CollectionProtocolForm colForm = null;
		
		Object obj = request.getAttribute("collectionProtocolForm");
		//Map map = null;
		
		if(obj != null && obj instanceof CollectionProtocolForm)
		{
			colForm = (CollectionProtocolForm)obj;
			maxCount = colForm.getOuterCounter();
			map = colForm.getValues();
			
		}

		for(int counter=maxCount;counter>=1;counter--)
		{
			String commonLabel = "value(CollectionProtocolEvent:" + counter;
			String commonName = "CollectionProtocolEvent:" + counter;
			String cid = "ivl(" + counter + ")";
			String functionName = "insRow('" + commonLabel + "','" + cid +"'," + counter+ ")";
			String cpeIdentifier= commonLabel + "_id)";
			String check = "chk_proto_"+ counter;
			String tableId = "table_" + counter;
		
			if(colForm!=null)
			{
				Object o = colForm.getIvl(""+counter);
				if(o!=null)
					maxIntCount = Integer.parseInt(o.toString());
			}
		String tblColor = Constants.ODD_COLOR;
		if(counter%2 == 0)
			tblColor = Constants.EVEN_COLOR;
						
%>
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%" id="<%=tableId%>">
<tr><td>
<table cellpadding=5 width="100%" class="tbBorders">
<tr><td>
<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%" bgColor="<%=tblColor%>"  id="i<%=tableId%>">
	<tr>
		<td rowspan=2 class="tabrightmostcellAddMore"><%=counter%></td>
		<td class="formFieldAddMore">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td class="formRequiredNoticeAddMore" width="5">*
						<html:hidden property="<%=cpeIdentifier%>" />
					</td>
					<td class="formRequiredLabelAddMore" width="32%">
					<%
						String fldName = commonLabel + "_clinicalStatus)";
					%>
						<label for="<%=fldName%>">
							<bean:message key="collectionprotocol.clinicalstatus" />
						</label>
					</td>
					
					<td class="formFieldAddMore" colspan=2>
<!-- Mandar : 434 : for tooltip -->
						<html:select property="<%=fldName%>" styleClass="formField" styleId="<%=fldName%>" size="1"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.CLINICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
					</td>
				</tr>
				
			    <tr>
					<td class="formRequiredNoticeAddMore" width="5">*</td>
					<%
						fldName="";
						fldName = commonLabel + "_studyCalendarEventPoint)";
						String keyStudyPoint = commonName + "_studyCalendarEventPoint";
						String valueStudyPoint = (String)colForm.getValue(keyStudyPoint);
						
					
						if(valueStudyPoint == null)
							valueStudyPoint = "1";
						
					%>

			        <td colspan="1" class="formRequiredLabelAddMore">
			        	<label for="<%=fldName%>">
							<bean:message key="collectionprotocol.studycalendartitle" />
						</label>
			        </td>
			        
			        <td colspan="2" class="formFieldAddMore">
			        	<html:text styleClass="formFieldSized5" size="30" 
			        			styleId="<%=fldName%>"  maxlength="10" 
			        			property="<%=fldName%>" 
			        			readonly="<%=readOnlyValue%>"
			        			value="<%=valueStudyPoint%>" /> 
			        	<bean:message key="collectionprotocol.studycalendarcomment"/>
					</td>
					<%
							String outerKey = "CollectionProtocolEvent:" + counter + "_id";
							boolean outerBool = Utility.isPersistedValue(map,outerKey);
							//boolean bool = false;
							String outerCondition = "";
							if(outerBool)
								outerCondition = "disabled='disabled'";

						%>
						
			    </tr>
			</TABLE>
		</td>
		<td rowspan=2 class="tabrightmostcellAddMore">
			<input type=checkbox name="<%=check%>" id="<%=check %>" <%=outerCondition%> onClick="enableButton(document.forms[0].deleteCollectionProtocolEvents,document.forms[0].outerCounter,'chk_proto_')">		
		</td>
	</tr>

	<!-- 2nd row -->
	<tr>
		<td class="formFieldAddMore">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width=100%>
			    <tr>
			        <td colspan="5" class="formSubTitle">
			        	<b><bean:message key="collectionprotocol.specimenreq"/></b>
			        </td>
			        <td class="formSubTitle">	
			     		<html:button property="addSpecimenReq" styleClass="actionButton" value="Add More" onclick="<%=functionName%>"/>
			     		
			     		<html:hidden styleId="<%=cid%>" property="<%=cid%>" value="<%=""+maxIntCount%>"/>
			        </td>
			        <td class="formSubTitle" align="Right">
			        		<% String temp = "deleteChecked('";
			        			temp = temp + commonLabel+"',";
			        			temp = temp + "'CollectionProtocol.do?operation="+operation+"&pageOf=pageOfCollectionProtocol&status=true&button=deleteSpecimenReq&blockCounter="+counter+"',";
			        			temp = temp +"'"+ cid + "'" +",";
			        			temp = temp + "'chk_spec_"+ counter +"_',false)";
			        			
			        		%> 
							<html:button property="deleteSpecimenReq" styleClass="actionButton" onclick="<%=temp%>">
								<bean:message key="buttons.delete"/>
							</html:button>
					</td>
			    </tr>
			    
			    <TBODY id="<%=commonLabel%>">
			    <TR> <!-- SUB TITLES -->
			        <td class="formLeftSubTitle">
		        		<bean:message key="collectionprotocol.specimennumber" />
			        </td>
			        <td class="formLeftSubTitle">* 
			        	<bean:message key="collectionprotocol.specimenclass" />
			        </td>
			        <td class="formLeftSubTitle">* 
			        	<bean:message key="collectionprotocol.specimetype" />
			        </td>
			        
			        <td class="formLeftSubTitle">* 
			        	<bean:message key="collectionprotocol.specimensite" />
				    </td>
			        <td  class=formLeftSubTitle>* 
			    		<bean:message key="collectionprotocol.specimenstatus" />
				    </td>
			        <td class=formLeftSubTitle>&nbsp;
			        	<bean:message key="collectionprotocol.quantity" />
			        </td>
			        <td class="formLeftSubTitle">
						<label for="delete" align="center">
							<bean:message key="addMore.delete" />
						</label>
					</td>
			    </TR><!-- SUB TITLES END -->
				
				<%
					for(int innerCounter=maxIntCount;innerCounter>=1;innerCounter--)
					{
				%>
				
				<TR>	<!-- SPECIMEN REQ DATA -->
			        <td class="tabrightmostcellAddMore"><%=innerCounter%>.</td>
			        <%
						String cName="";
						int iCnt = innerCounter;
						cName = commonLabel + "_SpecimenRequirement:" + iCnt ;
						String srCommonName = commonName + "_SpecimenRequirement:" + iCnt ;
						
						String fName = cName + "_specimenClass)";
						String srFname = srCommonName + "_specimenClass";
						String srSubTypeKeyName = srCommonName + "_specimenType";
						String sName = cName + "_unitspan)";
						String srIdentifier = cName + "_id)";
						String tmpSubTypeName = cName + "_specimenType)";
						
						String innerCheck = "chk_spec_" + counter + "_"+ iCnt;
						
						String specimenClassKey = "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + innerCounter + "_specimenClass";
						String specimenClassValue = (String)map.get(specimenClassKey);
						String identifierKey = "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + innerCounter + "_id";
						String idValue = String.valueOf(map.get(identifierKey));

						int sysId = 0;

						try
						{
							sysId = Integer.parseInt(idValue);
						}
						catch(Exception e) //Exception is handled. If NumberFormatException or NullPointerException then identfier value = 0
						{
							sysId = 0;
						}

					%>
			        
			        <td class="formFieldAddMore">
			        	<html:hidden property="<%=srIdentifier%>" />	
			        	<%
			        		String onChangeFun = "changeUnit('" + fName + "','" + sName + "','" + tmpSubTypeName + "')";
			        		String subTypeFunctionName ="onSubTypeChangeUnit('" + fName + "',this,'" + sName + "')"; 
			        		
			        	%>
			        	<html:select property="<%=fName%>" 
										styleClass="formFieldSized10" 
										styleId="<%=fName%>" size="1"
										onchange="<%=onChangeFun%>"
									 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
						<%
							if(operation.equals(Constants.EDIT) && sysId > 0)
							{
						%>
								<html:option value="<%=specimenClassValue%>"><%=specimenClassValue%></html:option>
						<%
							}else{
						%>
								<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
						<%
							}
						%>
						</html:select>
			        </td>
			        
			        <td class="formFieldAddMore">
						<%
								String classValue = (String)colForm.getValue(srFname);
								specimenTypeList = (List)specimenTypeMap.get(classValue);
								
								boolean subListEnabled = false;
								
								if(specimenTypeList == null)
								{
									specimenTypeList = new ArrayList();
									specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
								}
								pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
								fName="";
								 fName = cName + "_specimenType)";
						%>
			        	<html:select property="<%=fName%>" 
										styleClass="formFieldSized10" 
										styleId="<%=fName%>" size="1"
										   onchange="<%=subTypeFunctionName%>" 
										   disabled="<%=subListEnabled%>" 
										 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formFieldAddMore">
						<%
								fName="";
								 fName = cName + "_tissueSite)";
						%>

			        	<html:select property="<%=fName%>" 
										styleClass="formFieldSized35" 
										styleId="<%=fName%>" size="1"
									 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.TISSUE_SITE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        	<%
							String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&cdeName=Tissue%20Site&propertyName="+fName;
						%>
				        <a href="#" onclick="javascript:NewWindow('<%=url%>','name','375','330','yes');return false">
							<img src="images\Tree.gif" border="0" width="26" height="22" title='Tissue Site Selector'>
						</a>
					</td>
					
			        <td class="formFieldAddMore">
						<%
								fName="";
								 fName = cName + "_pathologyStatus)";
						%>

			        	<html:select property="<%=fName%>" 
										styleClass="formFieldSized10" 
										styleId="<%=fName%>" size="1"
									 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.PATHOLOGICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formFieldAddMore">
						<%
								fName="";
								 fName = cName + "_quantity_value)";
								 
								 String typeclassValue = (String)colForm.getValue(srSubTypeKeyName);
								 String strHiddenUnitValue = "" + changeUnit(classValue,typeclassValue);
								 String srQtyKeyName = srCommonName + "_quantity_value";
								 String qtyValue = (String)colForm.getValue(srQtyKeyName);
								if(qtyValue == null)
									qtyValue="0";
						%>

			        	<html:text styleClass="formFieldSized5" size="30" 
			        			styleId="<%=fName%>"  maxlength="10" 
			        			property="<%=fName%>" 
			        			readonly="<%=readOnlyValue%>"
								value="<%=qtyValue%>" />
		        			
			        	<span id="<%=sName%>">
			        		<%=strHiddenUnitValue%>
						</span>
					</td>
					<%
							String innerKey = "CollectionProtocolEvent:"+counter+"_SpecimenRequirement:"+iCnt+"_id";
							boolean innerBool = Utility.isPersistedValue(map,innerKey);
							String innerCondition = "";
							if(innerBool)
								innerCondition = "disabled='disabled'";

						%>
						<td class="formFieldAddMore" width="5">
						<%
							//String func = "enableButton(document.forms[0].deleteSpecimenReq,'" + cid + "','" +"chk_spec_" + counter + "_ "+ "')";
						%>
							<input type=checkbox name="<%=innerCheck%>" id="<%=innerCheck %>" <%=innerCondition%>>
						</td>
				</TR>	<!-- SPECIMEN REQ DATA END -->
				<%
					} // inner for block
				%>
				</TBODY>
			</TABLE>
		</td>
	</tr>
</table> <!-- outer table for CPE ends -->
</td></tr>
</table>
</td></tr>
</table>

<%
} // outer for
%>
</td></tr></tbody></table>
<!-- outermosttable  -->

<table cellpadding="0" cellspacing="0" border="0" width = "85%" id="submittable">
		<tr><td>&nbsp;&nbsp;</td></tr>		
			<tr>
			<td>&nbsp;&nbsp;</td>
				<td class="formLabelNoBackGround" style="font-size:12;" bgcolor="#F4F4F5" width="50%">
					<html:checkbox property="aliqoutInSameContainer">
						<bean:message key="aliquots.storeAllAliquotes" />
					</html:checkbox>
				</td>
						   
				<td align="right" bgcolor="#F4F4F5">
				<%
				   	String action = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
				%>
					<html:button styleClass="actionButton" property="submitPage" onclick="<%=action%>">
						<bean:message key="buttons.submit"/>
					</html:button>
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
			</tr>
</table>


</td></tr></table>
	
</html:form>

<html:form action="DummyCollectionProtocol.do">
<div id="d1">
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%" id="table_`">
<tr><td>
<table cellpadding=5 class="tbBorders">
<tr><td>
<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%" id="itable_`">
	<tr>
		<td rowspan=2 class="tabrightmostcellAddMore">`</td>
		<td class="formFieldAddMore">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td class="formRequiredNoticeAddMore" width="5">*
						<html:hidden property="value(CollectionProtocolEvent:`_id)" />
					</td>
					<td class="formRequiredLabelAddMore" width="32%">
						<label for="value(CollectionProtocolEvent:`_clinicalStatus)">
							<bean:message key="collectionprotocol.clinicalstatus" />
						</label>
					</td>
					
					<td class="formFieldAddMore" colspan=2>
						<html:select property="value(CollectionProtocolEvent:`_clinicalStatus)" 
										styleClass="formField" styleId="value(CollectionProtocolEvent:`_clinicalStatus)" size="1"
										 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.CLINICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
					</td>
				</tr>
				
			    <tr>
					<td class="formRequiredNoticeAddMore" width="5">*</td>
			        <td colspan="1" class="formRequiredLabelAddMore">
			        	<label for="value(CollectionProtocolEvent:`_studyCalendarEventPoint)">
							<bean:message key="collectionprotocol.studycalendartitle" />
						</label>
			        </td>
			        
			        <td colspan="2" class="formFieldAddMore">
			        	<html:text styleClass="formFieldSized5" size="30" 
			        			styleId="value(CollectionProtocolEvent:`_studyCalendarEventPoint)"  maxlength="10" 
			        			property="value(CollectionProtocolEvent:`_studyCalendarEventPoint)" 
			        			readonly="<%=readOnlyValue%>"
			        			value="1" /> 
			        	<bean:message key="collectionprotocol.studycalendarcomment"/>
					</td>
					
			    </tr>
			</TABLE>
		</td>
		
			<td rowspan=2 class="tabrightmostcellAddMore">
				<input type=checkbox name="chk_proto_`" id="chk_proto_`" onClick="enableButton(document.forms[0].deleteCollectionProtocolEvents,document.forms[0].outerCounter,'chk_proto_')">		
			</td>
	</tr>

	<!-- 2nd row -->
	<tr>
		<td class="formFieldAddMore">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width=100%>
			    <tr>
			        <td colspan="5" class="formSubTitle">
			        	<b><bean:message key="collectionprotocol.specimenreq"/></b>
			        </td>
			        <td class="formSubTitle">	
			        <%
				        String hiddenCounter = "ivl(`)";
			        %>
			     		<html:button property="addSpecimenReq" styleClass="actionButton" value="Add More" onclick="insRow('value(CollectionProtocolEvent:`','ivl(`)','`')"/>
			     		
			     		<html:hidden styleId="<%=hiddenCounter%>" property="<%=hiddenCounter%>" value="1"/>
			        </td>
			        <td class="formSubTitle" align="Right">
							<html:button property="deleteSpecimenReq" styleClass="actionButton" onclick="deleteChecked('value(CollectionProtocolEvent:`','CollectionProtocol.do?operation=<%=operation%>&pageOf=pageOfCollectionProtocol&status=true&button=deleteSpecimenReq&blockCounter=`','ivl(`)','chk_spec_`_',false)">
								<bean:message key="buttons.delete"/>
							</html:button>
					</td>
			    </tr>
			    
			    <TBODY id="value(CollectionProtocolEvent:`">
			    <TR> <!-- SUB TITLES -->
			        <td class="formLeftSubTitle">
		        		<bean:message key="collectionprotocol.specimennumber" />
			        </td>
			        <td class="formLeftSubTitle">* 
			        	<bean:message key="collectionprotocol.specimenclass" />
			        </td>
			        <td class="formLeftSubTitle">* 
			        	<bean:message key="collectionprotocol.specimetype" />
			        </td>
			        
			        <td class="formLeftSubTitle">* 
			        	<bean:message key="collectionprotocol.specimensite" />
				    </td>
			        <td  class=formLeftSubTitle>* 
			    		<bean:message key="collectionprotocol.specimenstatus" />
				    </td>
			        <td class=formLeftSubTitle>* 
			        	<bean:message key="collectionprotocol.quantity" />
			        </td>
			        <td class="formLeftSubTitle">
						<label for="delete" align="center">
							<bean:message key="addMore.delete" />
						</label>
					</td>
			    </TR><!-- SUB TITLES END -->
				
				<TR>	<!-- SPECIMEN REQ DATA -->
			        <td class="tabrightmostcellAddMore">1.
			        	<html:hidden property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_id)" />
			        </td>
			        <td class="formFieldAddMore">		
			        	<html:select property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenClass)" 
										styleClass="formFieldSized10" 
										styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenClass)" size="1"
										onchange="changeUnit('value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenClass)',
			           						'value(CollectionProtocolEvent:`_SpecimenRequirement:1_unitspan)',
			           						'value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenType)')"
										 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formFieldAddMore">
			        	<html:select property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenType)" 
										styleClass="formFieldSized10" 
										styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenType)" size="1"
										onchange="onSubTypeChangeUnit('value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenClass)',
			           						this,'value(CollectionProtocolEvent:`_SpecimenRequirement:1_unitspan)')"
									 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:option value="-1"><%=Constants.SELECT_OPTION%></html:option>
						</html:select>
			        </td>
			        
			        <td class="formFieldAddMore">
			        	<html:select property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_tissueSite)" 
										styleClass="formFieldSized35" 
										styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_tissueSite)" size="1"
										 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.TISSUE_SITE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			<!-- ****************************************  -->
				        <a href="#" onclick="javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfTissueSite&cdeName=Tissue%20Site&propertyName=value(CollectionProtocolEvent:`_SpecimenRequirement:1_tissueSite)','name','250','330','no');return false">
							<img src="images\Tree.gif" border="0" width="26" height="22" title='Tissue Site Selector'>
						</a>
				     <%--   <a href="#">
							<img src="images\Tree.gif" border="0" width="26" height="22" title='Tissue Site Selector'></a>   --%>
					</td>
					
			        <td class="formFieldAddMore">
			        	<html:select property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_pathologyStatus)" 
										styleClass="formFieldSized10" 
										styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_pathologyStatus)" size="1"
										 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.PATHOLOGICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formFieldAddMore">
			        	<html:text styleClass="formFieldSized5" size="30"  maxlength="10" 
			        			styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_quantity_value)" 
			        			property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_quantity_value)" 
			        			readonly="<%=readOnlyValue%>"
								value="0" />
			        	<span id="value(CollectionProtocolEvent:`_SpecimenRequirement:1_unitspan)">
			        		&nbsp;
						</span>
					</td>
					
						<td class="formFieldAddMore" width="5">
							<input type=checkbox name="chk_spec_`_1" id="chk_spec_`_1" ><!-- onClick="enableButton(document.forms[0].deleteSpecimenReq,'ivl(`)','chk_spec_`_ ')"-->	
								
						</td>
					
				</TR>	<!-- SPECIMEN REQ DATA END -->
				</TBODY>
			</TABLE>
		</td>
	</tr>
</table> <!-- outer table for CPE ends -->
</td></tr>
</table>
</td></tr>
</table>
</div>
</html:form>
</body>