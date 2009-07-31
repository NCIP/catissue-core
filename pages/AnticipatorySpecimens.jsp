<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo"%>
<%@ taglib uri="/WEB-INF/specimenDetails.tld" prefix="md"%>
<%@ taglib uri="/WEB-INF/AutoCompleteTag.tld" prefix="autocomplete"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.bean.GenericSpecimen"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp"%>
<%@ page language="java" isELIgnored="false"%>
<% ViewSpecimenSummaryForm form = (ViewSpecimenSummaryForm)request.getAttribute("viewSpecimenSummaryForm");  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/antiSpecAjax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/GenericSpecimenDetailsTag.js"></script>

<script language="JavaScript" type="text/javascript">
 var isPrintChecked = false;
	//window.parent.frames['SpecimenEvents'].location="ShowCollectionProtocol.do?pageOf=specimenEventsPage&operation=ViewSummary";
	function ApplyToAll(object,type)
		{
			MDApplyToAll(object,type);
		}

//Mandar : 15Dec08 ---
function getCountByType(type)
{
	var count=0;
	var fields = document.getElementsByTagName("select");
	for (i=0; i<fields.length;i++)
	{
		var fid = fields[i].id;
		if(fid.indexOf(type+"[")>=0)
			count = count+1;
	}

	return count;
}
function getElement(name)
{
	var fields = document.getElementsByName(name);
	if(fields.length > 0)
		return fields[0];
	else
		return "";
}
function updateField(type,i,isDis,valueToSet)
{
	elemName = type+"["+i+"]"+".selectedContainerName";
	getElement(elemName).disabled =isDis;

	elemName = type+"["+i+"]"+".positionDimensionOne";
	getElement(elemName).disabled =isDis;
	elemName = type+"["+i+"]"+".positionDimensionTwo";
	getElement(elemName).disabled =isDis;

	elemName = type+"["+i+"]"+".selectedContainerName";
	getElement(elemName).value =valueToSet;

	elemName = type+"["+i+"]"+".positionDimensionOne";
	getElement(elemName).value = "";
	elemName = type+"["+i+"]"+".positionDimensionTwo";
	getElement(elemName).value ="";

}
	function MDApplyToAll(object,type)
		{
//			if(object.checked )
			{
				var cnt = getCountByType(type);
				
				var elemId = type+"[0]"+".storageContainerForSpecimen";
				var ele0 = document.getElementById(elemId);
				if(ele0.selectedIndex > 0)	// not virtual
				{
					elemName = type+"[0]"+".selectedContainerName";
					var valueToSet = getElement(elemName).value;

					for(i=1;i<cnt;i++)	// change values for all remaining
					{
						elemId = type+"["+i+"]"+".storageContainerForSpecimen";
						document.getElementById(elemId).selectedIndex = ele0.selectedIndex;
						updateField(type,i,false,valueToSet);
					}
				}
				else	// for virtual
				{
					for(i=1;i<cnt;i++)	
					{
						elemId = type+"["+i+"]"+".storageContainerForSpecimen";
						document.getElementById(elemId).selectedIndex = ele0.selectedIndex;
						updateField(type,i,true,"");
						
					}

				}
			}
		}



		function saveCollectionProtocol()
		{
				var action ="SubmitSpecimenCollectionProtocol.do?action=collectionprotocol";
				document.forms[0].action = action;
				document.forms[0].submit();
		}

		function showMap(selectedContainerName,positionDimensionOne,positionDimensionTwo,containerId,specimenClassName,cpId)
		{
			frameUrl="ShowFramedPage.do?pageOf=pageOfSpecimen&"+
				"selectedContainerName=" + selectedContainerName +
				"&pos1=" + positionDimensionOne + 
				"&pos2=" + positionDimensionTwo +
				"&containerId=" +containerId +
				"&${requestScope.CAN_HOLD_SPECIMEN_CLASS}="+specimenClassName +
				"&${requestScope.CAN_HOLD_COLLECTION_PROTOCOL}=" + cpId;
			
			var storageContainer = document.getElementById(selectedContainerName).value;
			frameUrl+="&storageContainerName="+storageContainer;

			openPopupWindow(frameUrl,'newSpecimenPage');
			//mapButtonClickedOnSpecimen(frameUrl,'newSpecimenPage');
		}
		//bug 11169 start
		function ChangeCheckBoxStatus(type,chkInstance)
		{
             onCheckBoxClick(type,chkInstance,'checkedSpecimen');			
		}
		function ChangePrintCheckBoxStatus(type,chkInstance)
		{
             onCheckBoxClick(type,chkInstance,'printSpecimen');
		}
		function onCheckBoxClick(type,chkInstance,prefix)
		{
           var checkedSpecimen ='].'+prefix;
			var elementType = type +'[';
			var ctr=0;
			do
			{
				var elementName = elementType + ctr + checkedSpecimen;

				var chkCount= document.getElementsByName(elementName).length;
				if (chkCount >0)
				{
					var element = document.getElementsByName(elementName)[0];
					if (element.disabled == false)
					{
						element.checked = chkInstance.checked;
						//Mandar : for event propagation
						if(type == "specimen" && element.onclick)
						{
							if(document.all)
								element.fireEvent("onclick"); 
							else
								element.onclick(); 
						}
					}
					ctr++;
				}
			}while(chkCount>0);
		}
		
	function UpdateCheckBoxStatus()
	{
		var checkedSpecimen = '].checkedSpecimen';
		var aliquotType = "aliquot[";
		var ctr = 0;
		if (document.forms[0].chkAllAliquot != null) {
			document.forms[0].chkAllAliquot.disabled = true;
			document.forms[0].chkAllAliquot.checked = true;
			do {
				var elementName = aliquotType + ctr + checkedSpecimen;
				var chkCount = document.getElementsByName(elementName).length;
				if (chkCount > 0) {
					var element = document.getElementsByName(elementName)[0];
					if (element.disabled == false) {
						document.forms[0].chkAllAliquot.disabled = false;
					}
					ctr++;
				}
			} while (chkCount > 0);
		}

		
	if (document.forms[0].chkAllDerived != null) 
	{
			ctr = 0;
			var derivedType = "derived[";
			document.forms[0].chkAllDerived.disabled = true;
			document.forms[0].chkAllDerived.checked = true;
			do {
				var elementName = derivedType + ctr + checkedSpecimen;
				var chkCount = document.getElementsByName(elementName).length;
				if (chkCount > 0) {
					var element = document.getElementsByName(elementName)[0];
					if (element.disabled == false) {
						document.forms[0].chkAllDerived.disabled = false;
					}
					ctr++;
				}
			} while (chkCount > 0);
		}		
	}
	function checkPrintStatusOfAllSpecimens() {
		checkPrintStatus('specimen');
		if (!isPrintChecked) {
			checkPrintStatus('aliquot');
		}
		if (!isPrintChecked) {
			checkPrintStatus('derived');
		}

	}
	function checkPrintStatus(type) {
		var checkedSpecimen = '].printSpecimen';
		var elementType = type + '[';
		var ctr = 0;
		do {
			var elementName = elementType + ctr + checkedSpecimen;
			var chkCount = document.getElementsByName(elementName).length;
			if (chkCount > 0) {
				var element = document.getElementsByName(elementName)[0];
				if (element.checked) {
					isPrintChecked = true;
					break;
				}
				ctr++;
			}
		} while (chkCount > 0 && !isPrintChecked);
	}
	//bug 11169 end
function pageSubmit() {
		checkPrintStatusOfAllSpecimens();
		<%
		String isSCGSubmit = request.getAttribute( Constants.IS_SCG_SUBMIT ).toString();
		%> 
		var url;
		<%if(isSCGSubmit!=null)
		{%>
		  url = 'GenericSpecimenSummary.do?save=SCGSpecimens&isSCGSubmit=<%=isSCGSubmit%>';
		  <%}
		  else
		  {%>
		  url = 'GenericSpecimenSummary.do?save=SCGSpecimens';
		 <% }
		 %>
		//bug 12656 
		<% if(request.getAttribute(Constants.PAGE_OF) != null && request.getAttribute(Constants.PAGE_OF).equals(Constants.PAGE_OF_MULTIPLE_SPECIMEN_WITHOUT_MENU))
		{
		  if(isSCGSubmit!=null)
		  {%>
              url = 'GenericSpecimenSummary.do?save=SCGSpecimens&pageOf=pageOfMultipleSpWithoutMenu&isSCGSubmit=<%=request.getAttribute( Constants.IS_SCG_SUBMIT )%>';
		<%}
		  else
		  {%>
		  url = 'GenericSpecimenSummary.do?save=SCGSpecimens&pageOf=pageOfMultipleSpWithoutMenu';
		 <% }
		 }%>
		
			
<%	if(request.getAttribute(Constants.PAGE_OF) != null && request.getAttribute(Constants.PAGE_OF).equals(Constants.CP_CHILD_SUBMIT)) {
			
			 if(isSCGSubmit!=null)
			  {%>
			      url = 'GenericSpecimenSummaryForSpecimen.do?save=SCGSpecimens&isSCGSubmit=<%=request.getAttribute( Constants.IS_SCG_SUBMIT )%>';
			<%}
			  else
			  {%>
			  url = 'GenericSpecimenSummaryForSpecimen.do?save=SCGSpecimens';
			 <% }
			 }%>
			
			
			//var printFlag = document.getElementById("printCheckbox");
			if(isPrintChecked)			
			{
				//changes added to work in Mozilla
				document.getElementsByName("printCheckbox")[0].value = true;
				document.forms[0].action = url + '&printflag=1';
				document.forms[0].submit();						
			}
			else
			{
				document.getElementsByName("printCheckbox")[0].value = false;
				document.forms[0].action =url+'&printflag=0'; 
				document.forms[0].submit();
			}
			
		}
		
		
		function onAddToCart()
		{
				document.forms[0].forwardTo.value="addMltipleSpecimenToCart";
				pageSubmit();
			
		}
		function onParentRadioBtnClick()
		{
			var url = 'GenericSpecimenSummary.do';
			<%	if(request.getAttribute(Constants.PAGE_OF) != null && request.getAttribute(Constants.PAGE_OF).equals(Constants.CP_CHILD_SUBMIT)) {%>
			 url = 	'GenericSpecimenSummaryForSpecimen.do?pageOf=<%=Constants.CP_CHILD_SUBMIT%>';
			<%}%>
			document.forms[0].action =url;
			document.forms[0].submit();
		}

//Mandar : 6Aug08 ----- ajax call for storage location
var sid="";
function scForSpecimen(element,spid)
{
	sid=spid;
//	alert(element.value + " : "+element.name);
	var name= element.name;
	var prefix = name.substring(0,name.indexOf('.'));
	var cpidName = prefix+".collectionProtocolId";
	//var cpid = document.getElementsByName(cpidName)[0].value;
	var cpid = document.getElementById(cpidName).value;
	var className = prefix+".className";
//	var cName = document.getElementsByName(className)[0].value;
	var cName = document.getElementById(className).value;
	
	//alert(prefix+" : " + cpid + " : " + cName);
	if(element.value == "Auto")
	{
		var responseHandlerFn = setSCLocation;
		var bool = true;
		var reqType = "GET";
		var url = "GenericSpecimenSummaryForSpecimen.do?sid="+sid+"&cpid="+cpid+"&cName="+cName;

		ajaxCall(reqType, url, bool, responseHandlerFn);
	}
	else
	{
		setContainerType(element.value,sid);
	}
}

function setContainerType(containerType, sid)
{
		if(containerType == "Manual")
		{
			updateSCFields(sid, false);
		}
		else
		{
			updateSCFields(sid, true);
		}
}
function updateSCFields(sid, isDisabled)
{
	var scName = "selectedContainerName_"+sid;
	var scPos1 = "positionDimensionOne_"+sid;
	var scPos2 = "positionDimensionTwo_"+sid;
	var scCntr = "containerId_"+sid;
	var t1 = document.getElementById(scName);
	var t2 = document.getElementById(scPos1);
	var t3 = document.getElementById(scPos2);
	var t4 = document.getElementById(scCntr);
	t1.value="";
	t2.value="";
	t3.value="";
	t4.value="";
	t1.disabled = isDisabled;
	t2.disabled = isDisabled;
	t3.disabled = isDisabled;
	t4.disabled = isDisabled;
}



function setSCLocation()
{
	if ((xmlHttpReq.readyState == 4) && (xmlHttpReq.status == 200))
	{
		var msg = xmlHttpReq.responseText;
	//	alert(msg);
		if(msg.indexOf('#') != -1)
		{
			updateSCDetails(msg);
		}
		else
		{
			alert("Container info not available right now. Try again after some time.");
		}
	}
/*	else
	{
		alert("Could not fetch location right now");
	}
*/
}

function updateSCDetails(msg)
{
	var scName = "selectedContainerName_"+sid;
	var scPos1 = "positionDimensionOne_"+sid;
	var scPos2 = "positionDimensionTwo_"+sid;
	var scontid = "containerId_"+sid;
	var t1 = document.getElementById(scName);
	var t2 = document.getElementById(scPos1);
	var t3 = document.getElementById(scPos2);
	var t4 = document.getElementById(scontid);

	//alert(msg); 
	var data = msg.split("#");
	t1.value=data[0];
	t2.value=data[2];
	t3.value=data[3];
	t4.value=data[1];
	t1.disabled = false;
	t2.disabled = false;
	t3.disabled = false;

}
</script>
</head>
<body onload="UpdateCheckBoxStatus()">
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<script language="javascript" type="text/javascript">
	${requestScope.refreshTree}
</script>
<logic:equal name="viewSpecimenSummaryForm" property="requestType"
	value="Multiple Specimen">
	<script language="javascript" type="text/javascript">
		${requestScope.refreshTree}
	</script>
</logic:equal>
<%
String lbl = "Apply first to all";
%>
<%@ include file="/pages/content/common/ActionErrors.jsp"%>
<logic:notEmpty name="messageKey">
	<html:messages id="messageKey" message="true" header="messages.header"
		footer="messages.footer">
			${requestScope.messageKey}
		</html:messages>
</logic:notEmpty>

<html:form action="${requestScope.formAction}">
	<!-- Mandar : New Table design starts -->
	<TABLE width="100%" cellspacing="0" cellpadding="0">
		<TR>
			<TD align="left" class="tr_anti_hdrbg_blue" width="100%" colspan=3>
			<TABLE width="100%" border="0">
				<TR>
					<TD align="left" rowspan="2" valign="middle"
						class="tr_anti_hdrbg_blue" width="${requestScope.sfCol}%"><span
						class="blue_ar_b"> <bean:write
						name="viewSpecimenSummaryForm" property="title" /> </span></TD>
					<TD class="tr_anti_hdrbg_blue" width=33% rowspan="2"
						valign="middle" align="left"><A class="black_ar"
						name="parent" HREF="#parent" onClick="ApplyToAll(this,'specimen')"
						onmouseover="Tip(' Apply first location to all')"><bean:message
						key="aliquots.applyFirstToAll" /></A></TD>
					<td nowrap class="tr_anti_hdrbg_blue" scope="col" width="2%"
						valign="middle" align="left" onmouseover="Tip('Collect All')">
					<logic:equal name="viewSpecimenSummaryForm"
						property="showCheckBoxes" value="true">
						<span class="blue_ar_b"> <bean:message
							key="anticipatorySpecimen.Collected" /> </span>
					</logic:equal></td>
					<td nowrap class="tr_anti_hdrbg_blue" scope="col" width="2%"
						valign="middle" align="left" onmouseover="Tip('Print All')">
					<logic:equal name="viewSpecimenSummaryForm"
						property="showCheckBoxes" value="true">
						<span class="blue_ar_b"> <bean:message
							key="specimen.printLabel" /> </span>
					</logic:equal></td>
				</TR>
				<TR>
					<td nowrap class="tr_anti_hdrbg_blue_small" scope="col" width="2%"
						valign="middle" align="center"><logic:equal
						name="viewSpecimenSummaryForm" property="showCheckBoxes"
						value="true">
						<input type="checkbox" name="chkAllSpecimen" checked="checked"
							onclick="ChangeCheckBoxStatus('specimen',this)" />
					</logic:equal></td>
					<td nowrap class="tr_anti_hdrbg_blue_small" scope="col" width="2%"
						valign="middle" align="center"><logic:equal
						name="viewSpecimenSummaryForm" property="showCheckBoxes"
						value="true">
						<input type="checkbox" name="printAll"
							onclick="ChangePrintCheckBoxStatus('specimen',this)" />
					</logic:equal></td>
				</TR>
			</TABLE>
			</TD>
		</TR>

		<logic:empty name="viewSpecimenSummaryForm" property="specimenList">
			<tr>
				<td class="dataTableWhiteCenterHeader" colspan="3">No specimens
				to display for current action!!</td>
			</tr>
		</logic:empty>
		<TR>
			<TD colspan="3">
			<table border=0 width="100%">
				<md:specDetFormat4 columnHeaderListName="columnHeaderList"
					formName="viewSpecimenSummaryForm" dataListName="specimenList"
					dataListType="Parent" columnListName="columnListName"
					isReadOnly="false" displayColumnListName="dispColumnsList" />
			</table>
			<logic:notEmpty name="viewSpecimenSummaryForm" property="eventId">
				<html:hidden property="eventId" />
				<html:hidden property="lastSelectedSpecimenId" />
				<html:hidden property="selectedSpecimenId" />
			</logic:notEmpty> <html:hidden property="userAction" /> <html:hidden
				property="requestType" /></TD>
		</TR>
		<logic:empty name="viewSpecimenSummaryForm" property="aliquotList">
			<logic:empty name="viewSpecimenSummaryForm" property="derivedList">
				<TR>
					<TD colspan="3">
					<table>
						<tr>
							<td class="dataTablePrimaryLabel" colspan="6" height="20">
							Child specimens not defined.</td>
						</tr>
						<tr>
							<td><br>
							</td>
						</tr>
					</table>
					</TD>
				</TR>
			</logic:empty>
		</logic:empty>

		<logic:notEmpty name="viewSpecimenSummaryForm" property="derivedList">
			<TR>
				<TD align="left" valign="middle" class="tr_anti_hdrbg_blue"
					width="100%" colspan=3>
				<TABLE width="100%" border="0">
					<TR>
						<TD rowspan="2" align="left" class="tr_anti_hdrbg_blue"
							width="${requestScope.fCol}%"><span class="blue_ar_b">
						<bean:message key="anticipatorySpecimen.DerivativeDetails" /> </span></TD>
						<TD class="tr_anti_hdrbg_blue" width=40% rowspan="2"
							valign="middle" align="left"><A class="black_ar"
							name="derived" HREF="#derived"
							onClick="ApplyToAll(this,'derived')"
							onmouseover="Tip(' Apply first location to all')"><bean:message
							key="aliquots.applyFirstToAll" /></A></TD>
						<td nowrap class="tr_anti_hdrbg_blue" scope="col" width="2%"
							align="left" valign="middle" onmouseover="Tip('Create All')">
						<logic:equal name="viewSpecimenSummaryForm"
							property="showCheckBoxes" value="true">
							<span class="blue_ar_b"> <bean:message
								key="anticipatorySpecimen.Collected" /> </span>
						</logic:equal></td>
						<td nowrap class="tr_anti_hdrbg_blue" scope="col" width="2%"
							align="left" valign="middle" onmouseover="Tip('Print All')">
						<logic:equal name="viewSpecimenSummaryForm"
							property="showCheckBoxes" value="true">
							<span class="blue_ar_b"> <bean:message
								key="specimen.printLabel" /> </span>
						</logic:equal></td>
					</TR>
					<TR>
						<td nowrap class="tr_anti_hdrbg_blue_small" scope="col" width="3%"
							align="center" valign="middle"><logic:equal
							name="viewSpecimenSummaryForm" property="showCheckBoxes"
							value="true">
							<input type="checkbox" name="chkAllDerived" checked="checked"
								onclick="ChangeCheckBoxStatus('derived',this)" />
						</logic:equal></td>
						<td nowrap class="tr_anti_hdrbg_blue_small" scope="col" width="2%"
							align="center" valign="middle"><logic:equal
							name="viewSpecimenSummaryForm" property="showCheckBoxes"
							value="true">
							<input type="checkbox" name="printAllDerived"
								onclick="ChangePrintCheckBoxStatus('derived',this)" />
						</logic:equal></td>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD colspan="3">
				<table border=0 width="100%">
					<md:specDetFormat4 columnHeaderListName="subSpecimenColHeaderList"
						formName="viewSpecimenSummaryForm" dataListName="derivedList"
						dataListType="Derived" columnListName="columnListName"
						isReadOnly="false" displayColumnListName="subSpecdispColumnsList" />
				</table>
				</TD>
			</TR>
		</logic:notEmpty>

		 <logic:notEmpty name="viewSpecimenSummaryForm" property="aliquotList">
         <TR>
		 <TD align="left" valign="middle" class="tr_anti_hdrbg_blue" width="100%" colspan=3> 
		 <TABLE width="100%" border="0">
		  <TR>
		  <TD colspan="1" rowspan="2" align="left" class="tr_anti_hdrbg_blue" width="${requestScope.fCol}%">
		     <span class="blue_ar_b">
			   <bean:message key="anticipatorySpecimen.AliquotDetails"/>
			  </span>
		   </TD>

		   <TD class="tr_anti_hdrbg_blue" rowspan="2" width=47% align="left" valign="middle">
		     <A class="black_ar" name="aliquot" HREF="#aliquot" onClick="ApplyToAll(this,'aliquot')" onmouseover="Tip(' Apply first location to all')">
			 <bean:message key="aliquots.applyFirstToAll" />
			 </A>
			 </TD>

			 <td nowrap align="left" valign="middle" class="tr_anti_hdrbg_blue"
							scope="col" width="2%" onmouseover="Tip('Create All')">
				<logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
					<span class="blue_ar_b">
					<bean:message key="anticipatorySpecimen.Collected"/>
					</span>
                </logic:equal>
			</td>

			<td nowrap align="left" valign="middle" class="tr_anti_hdrbg_blue" scope="col" width="2%" onmouseover="Tip('Print  All')">
			<logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
				<span class="blue_ar_b">
				<bean:message key="specimen.printLabel"/>
				</span>
			</logic:equal>
			</td>
          </TR>

		  <tr>
		  	<td nowrap class="tr_anti_hdrbg_blue" scope="col" width="2%" align="center" valign="middle">
			  <logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
					<input type="checkbox" name="chkAllAliquot" checked="checked" onclick="ChangeCheckBoxStatus('aliquot',this)"/>
			 </logic:equal>
		    </td>

			<td nowrap class="tr_anti_hdrbg_blue" scope="col" width="2%" align="center" valign="middle">
			  <logic:equal name="viewSpecimenSummaryForm" property="showCheckBoxes" value="true">
			     <input type="checkbox" name="printAllAliquot" onclick="ChangePrintCheckBoxStatus('aliquot',this)"/>
			 </logic:equal>
			</td>
		  </tr>
         </TABLE>
          </TD>
		 </TR>
         <TR>
          <TD colspan="3">
		    <table width="100%">
			  <tr>
			    <td>
				<md:specDetFormat4 columnHeaderListName="subSpecimenColHeaderList"
						formName="viewSpecimenSummaryForm" dataListName="aliquotList"
						dataListType="Aliquot" columnListName="columnListName"
						isReadOnly="false" displayColumnListName="subSpecdispColumnsList" />
				</td>
			  </tr>					
			 </table>
		   </TD>
         </TR>
        </logic:notEmpty>

	</TABLE>

	<table width="100%">
		<tr>
			<TD align="left" valign="middle" class="tr_anti_dividerbg_blue"
				colspan=4></td>
		</tr>
	</table>

	<!-- New Table design ends -->
	<table align="bottom">
		<logic:equal name="viewSpecimenSummaryForm" property="requestType"
			value="Collection Protocol">
			<tr>
				<td><html:submit value="Save Collection Protocol" /></td>
			</tr>
		</logic:equal>
		<logic:equal name="viewSpecimenSummaryForm" property="requestType"
			value="Multiple Specimen">
			<tr>
				<td><html:submit value="Save Specimens" /></td>
			</tr>
		</logic:equal>
		<html:hidden property="targetSuccess" />
		<html:hidden property="submitAction" />
		<html:hidden property="showCheckBoxes" />
		<html:hidden property="showLabel" />
		<html:hidden property="showbarCode" />
		<html:hidden property="readOnly" />
		<html:hidden property="showParentStorage" />
		<html:hidden property="forwardTo" />
		<html:hidden property="multipleSpEditMode" />
		<tr>
				<!-- <td class="formFieldNoBorders" colspan="5"  height="20" nowrap width="16%"> -->
				<html:hidden property="printCheckbox" style="printCheckbox"
					styleId="printCheckbox" />
				<!-- </td> -->
				<!--  Added for displaying  printer type and location -->

				<td>
				<%@ include	file="/pages/content/common/PrinterLocationTypeComboboxes.jsp"%>
				</td>
				<!--  End : Displaying   printer type and location -->
			</tr>

			<tr>
				<table border="0">
					<tr>
						<td><input class="blue_ar_b" type="button" value="Submit"
							onclick="pageSubmit()" /></td>
						<td><input class="blue_ar_b" type="button"  id="addToCart"
							value="Add To My List" onclick="onAddToCart()" /></td>
					</tr>
				</table>
			</tr>
		</table>
	<div id="divForHiddenChild"></div>
</html:form>
</body>
<script language="JavaScript" type="text/javascript">
identifyDisabledCheckBox();
displayPrinterTypeLocation();
</script>
</html>
