<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ taglib uri="/WEB-INF/specimenDetails.tld" prefix="md" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.bean.GenericSpecimen"%>
<%@ page language="java" isELIgnored="false" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/GenericSpecimenDetailsTag.js"></script>
<script language="JavaScript" type="text/javascript">
	window.parent.frames['SpecimenEvents'].location="ShowCollectionProtocol.do?pageOf=specimenEventsPage&operation=ViewSummary";
	function ApplyToAll(object,type)
		{
			if(object.checked )
			{
				var fields = document.getElementsByTagName("input");
				var i =0;
				var text="";
				var valueToSet = "";
				var isFirstField = true;
				
				for (i=0; i<fields.length;i++)
				{
					text = fields[i].name;
					if(text.indexOf(type)>=0 && text.indexOf(".selectedContainerName")>=0)
					{
						if(isFirstField)
						{
							valueToSet = fields[i].value;
							isFirstField = false;
						}
							fields[i].value = valueToSet;
					}
					else if(text.indexOf(type)>=0 && text.indexOf("[0]")<0 &&(text.indexOf(".positionDimensionOne")>=0 || text.indexOf(".positionDimensionTwo")>=0))
					{
					 fields[i].value = "";
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
		
		function ChangeCheckBoxStatus(type,chkInstance)
		{
			var checkedSpecimen ='].checkedSpecimen';
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
					}
					ctr++;
				}
			}while(chkCount>0);
		}
		
		function UpdateCheckBoxStatus()
		{
			var checkedSpecimen ='].checkedSpecimen';
			var aliquotType = "aliquot[";
			var ctr = 0;
			if (document.forms[0].chkAllAliquot != null)
			{
				document.forms[0].chkAllAliquot.disabled = true;
				document.forms[0].chkAllAliquot.checked = true;
				do
				{
					var elementName = aliquotType + ctr + checkedSpecimen;
					var chkCount= document.getElementsByName(elementName).length;
					if (chkCount >0)
					{
						var element = document.getElementsByName(elementName)[0];
						if (element.disabled == false)
						{
							document.forms[0].chkAllAliquot.disabled = false;							
						}
						ctr++;
					}
				}while(chkCount>0);
			}
			
			if (document.forms[0].chkAllAliquot != null)
			{
				ctr = 0;
				var derivedType = "derived[";
				document.forms[0].chkAllDerived.disabled = true;
				document.forms[0].chkAllDerived.checked = true;
				do
				{
					var elementName = derivedType + ctr + checkedSpecimen;
					var chkCount= document.getElementsByName(elementName).length;
					if (chkCount >0)
					{
						var element = document.getElementsByName(elementName)[0];
						if (element.disabled == false)
						{
							document.forms[0].chkAllDerived.disabled = false;							
						}
						ctr++;
					}
				}while(chkCount>0);
			}		
		}
		function pageSubmit()
		{
			var url = 'GenericSpecimenSummary.do?save=SCGSpecimens';
			<%	if(request.getAttribute(Constants.PAGEOF) != null && request.getAttribute(Constants.PAGEOF).equals(Constants.CP_CHILD_SUBMIT)) {%>
			 url = 	'GenericSpecimenSummaryForSpecimen.do?save=SCGSpecimens';
			<%}%>
			var printFlag = document.getElementById("printCheckbox");
			if(printFlag.checked)
			{
				document.forms[0].action = url + '&printflag=1';
				document.forms[0].submit();			
			}
			else
			{
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
			<%	if(request.getAttribute(Constants.PAGEOF) != null && request.getAttribute(Constants.PAGEOF).equals(Constants.CP_CHILD_SUBMIT)) {%>
			 url = 	'GenericSpecimenSummaryForSpecimen.do?pageOf=<%=Constants.CP_CHILD_SUBMIT%>';
			<%}%>
			document.forms[0].action =url;
			document.forms[0].submit();
		}
	</script>
</head>
<body onload="UpdateCheckBoxStatus()">
<script language="javascript" type="text/javascript">
	${requestScope.refreshTree}
</script>
<logic:equal name="viewSpecimenSummaryForm" property="requestType" value="Multiple Specimen">
	<script language="javascript" type="text/javascript">
		${requestScope.refreshTree}
	</script>
</logic:equal>

		<html:errors />
		<logic:notEmpty name="messageKey">
		<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
			${requestScope.messageKey}
		</html:messages>
		</logic:notEmpty>
		<html:form action="${requestScope.formAction}">		
		<!-- Mandar : New Table design starts -->
		<TABLE width="100%">
		<TR>
			<TD colspan="2" align="left" class="tr_bg_blue1">
			<span class="blue_ar_b">
				<bean:write name="viewSpecimenSummaryForm" property="title" /></span>
			</TD>
		</TR>
		<logic:empty name="viewSpecimenSummaryForm" property="specimenList" >
			<tr>
				<td class="dataTableWhiteCenterHeader">  
					No specimens to display for current action!!
				</td>
			</tr>		
		</logic:empty>
		<TR>
			<TD colspan="2" class="showhide">
				<table border="0" cellpadding="3" cellspacing="0" width="100%">
				<logic:notEmpty name="viewSpecimenSummaryForm" property="specimenList" >	
                  <tr class="tableheading">
                    <td width="4%" class="black_ar_b"><label for="delete" align="center"></label></td>
                    <logic:equal name="labelShow" value="true">
                    <td width="11%" class="black_ar_b"><bean:message key="specimen.label"/></td>
                    </logic:equal>
                    <logic:equal name="barcodeShow" value="true">
                    <td width="11%" class="black_ar_b"><bean:message key="specimen.barcode"/></td>
                    </logic:equal>
                    <td width="19%" class="black_ar_b"><bean:message key="specimen.subType"/></td>
                    <td width="7%" class="black_ar_b"><bean:message key="anticipatorySpecimen.Quantity"/></td>
                    <td width="7%" class="black_ar_b"><bean:message key="anticipatorySpecimen.Concentration"/></td>
                    <td width="30%" class="black_ar_b" nowrap><bean:message key="anticipatorySpecimen.Location"/><input id="chkSpecimen" type="checkbox" onClick="ApplyToAll(this,'specimen')"/><span class="black_ar_s">Apply First to All</span></td>
                    <td width="11%" class="black_ar_b"><bean:message key="anticipatorySpecimen.Collected"/></td>
                  </tr>
					<md:genericSpecimenDetails columnHeaderListName="columnHeaderList" formName="viewSpecimenSummaryForm" dataListName="specimenList" dataListType="Parent" columnListName="columnListName" isReadOnly="false" displayColumnListName="dispColumnsList" />
				<%-- custom tag for specimen list by mandar ---  --%>	</logic:notEmpty>
				</table>
				<logic:notEmpty name="viewSpecimenSummaryForm" property="eventId">
					<html:hidden property="eventId"  />
					<html:hidden property="lastSelectedSpecimenId"  />
					<html:hidden property="selectedSpecimenId"  />
				</logic:notEmpty>
					<html:hidden property="userAction" />
					<html:hidden property="requestType" />
			</TD>
		</TR>
		<logic:empty name="viewSpecimenSummaryForm" property="aliquotList" >
		<logic:empty name="viewSpecimenSummaryForm" property="derivedList" >
		<TR>
			<TD>
				<table>
					<tr>
						<td class="dataTablePrimaryLabel" colspan="6" height="20">  
							Child specimens not defined.
						</td>						
					</tr>
					<tr> <td> <br> </td> </tr>
				</table>	
			</TD>
		</TR>
		</logic:empty>				
		</logic:empty>
		<logic:notEmpty name="viewSpecimenSummaryForm" property="aliquotList" >
		<TR>
			<TD colspan="2" align="left" class="tr_bg_blue1">
				<span class="blue_ar_b">	
				<bean:message key="anticipatorySpecimen.AliquotDetails"/> 
				</span>
			</TD>
		</TR>
		<TR>
			<TD colspan="2" class="showhide">
				<table border="0" cellpadding="3" cellspacing="0" width="100%">
                  <tr class="tableheading">
                    <td width="11%" class="black_ar_b"><label for="delete" align="center"><bean:message key="anticipatorySpecimen.Parent"/></label></td>
                    <logic:equal name="labelShow" value="true">
                    <td width="11%" class="black_ar_b"><bean:message key="specimen.label"/></td>
                    </logic:equal>
                    <logic:equal name="barcodeShow" value="true">
                    <td width="11%" class="black_ar_b"><bean:message key="specimen.barcode"/></td>
                    </logic:equal>
                    <td width="14%" class="black_ar_b"><bean:message key="specimen.subType"/></td>
                    <td width="7%" class="black_ar_b"><bean:message key="anticipatorySpecimen.Quantity"/></td>
                    <td width="7%" class="black_ar_b"><bean:message key="anticipatorySpecimen.Concentration"/></td>
                    <td width="30%" class="black_ar_b" nowrap><bean:message key="anticipatorySpecimen.Location"/><input id="chkAliquot" type="checkbox" onClick="ApplyToAll(this,'aliquot')"/><span class="black_ar_s">Apply First to All</span> </td>
					<td><input type="checkbox" value="check" id="aliquotCheckBox" checked="true" onclick="applyToAlquots()"/></td>
                    <td width="10%" class="black_ar_b"><bean:message key="anticipatorySpecimen.Collected"/></td>
                  </tr>
					<md:genericSpecimenDetails columnHeaderListName="subSpecimenColHeaderList" formName="viewSpecimenSummaryForm" dataListName="aliquotList" dataListType="Aliquot" columnListName="columnListName" isReadOnly="false" displayColumnListName="dispColumnsList" />
				<%-- custom tag for specimen list by mandar --- Aliquot  --%>						
				</table>				
			</TD>
		</TR>
		</logic:notEmpty>
		<logic:notEmpty name="viewSpecimenSummaryForm" property="derivedList" >
		<TR>
			<TD colspan="2" align="left" class="tr_bg_blue1">
				<span class="blue_ar_b">	
				<bean:message key="anticipatorySpecimen.DerivativeDetails"/>
				</span>
			</TD>
		</TR>
		<TR>
			<TD colspan="2" class="showhide">
				<table border="0" cellpadding="3" cellspacing="0" width="100%">
					<tr class="tableheading">
						<td width="11%" class="black_ar_b"><label for="delete" align="center"><bean:message key="anticipatorySpecimen.Parent"/></label></td>
	                    <logic:equal name="labelShow" value="true">
						<td width="11%" class="black_ar_b"><bean:message key="specimen.label"/></td>
						</logic:equal>
	                    <logic:equal name="barcodeShow" value="true">
						<td width="11%" class="black_ar_b"><bean:message key="specimen.barcode"/></td>
						</logic:equal>
						<td width="14%" class="black_ar_b"><bean:message key="specimen.subType"/></td>
						<td width="7%" class="black_ar_b"><bean:message key="anticipatorySpecimen.Quantity"/></td>
						<td width="7%" class="black_ar_b"><bean:message key="anticipatorySpecimen.Concentration"/></td>
						<td width="30%" class="black_ar_b" nowrap><bean:message key="anticipatorySpecimen.Location"/><input id="chkDrived" type="checkbox" onClick="ApplyToAll(this,'derived')"/><span class="black_ar_s">Apply First to All</span></td>
						<td><input type="checkbox" value="check" id="derivedCheckBox" checked="true" onclick="applyToDerived()"/></td>
						<td width="10%" class="black_ar_b"><bean:message key="anticipatorySpecimen.Collected"/></td>
					</tr>
					<md:genericSpecimenDetails columnHeaderListName="subSpecimenColHeaderList" formName="viewSpecimenSummaryForm" dataListName="derivedList" dataListType="Derived" columnListName="columnListName" isReadOnly="false" displayColumnListName="dispColumnsList" />
					<%-- custom tag for specimen list by mandar --- Derived --%>	
				</table>
			</TD>
		</TR>
		</logic:notEmpty>
		</TABLE>
		<!-- New Table design ends -->
		<table align="bottom">
			<logic:equal name="viewSpecimenSummaryForm" property="requestType" value="Collection Protocol">
			<tr>
			   <td>
				<html:submit  value="Save Collection Protocol" />
				</td>
				</tr>
			</logic:equal>
			<logic:equal name="viewSpecimenSummaryForm" property="requestType" value="Multiple Specimen">		
			<tr>
				<td>
				<html:submit value="Save Specimens" />
				</td>
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
		<logic:equal name="viewSpecimenSummaryForm" property="readOnly" value="false">
		<tr>					
			<td class="formFieldNoBorders" colspan="5"  height="20" >
				<html:checkbox styleId="printCheckbox" property="printCheckbox" value="true" onclick="">
				<bean:message key="print.checkboxLabel"/>
				</html:checkbox>
			</td>
			<td>
			<input class="blue_ar_b" type="button" value="Submit" onclick="pageSubmit()" />
			<input class="blue_ar_b" type="button" value="Add To My List" onclick="onAddToCart()" />
			</td>
		 </tr>
		</logic:equal>
		</table>
		<div id="divForHiddenChild"></div>
		</html:form>
</body>
<script language="JavaScript" type="text/javascript">
identifyDisabledCheckBox();
</script>
</html>
