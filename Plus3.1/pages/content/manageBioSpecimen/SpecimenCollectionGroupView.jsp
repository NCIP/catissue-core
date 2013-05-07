<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
<head>
<LINK type=text/css rel=stylesheet href="css/participantEffects.css"/>
<script>
	function showEditSCGPage()
	{
		var id1=document.getElementById("idValue").value;
		var pId=document.getElementById("participantId").value;
		var cpId=document.getElementById("cpId").value;
		var id=document.getElementById("clickedNodeId").value;
		var evtDate=document.getElementById("evtDate").value;
		var action="QuerySpecimenCollectionGroupSearch.do?pageOf=pageOfSpecimenCollectionGroupCPQueryEdit&refresh=false&operation=edit&id="+id1+"&cpSearchParticipantId="+pId+"&cpSearchCpId="+cpId+"&clickedNodeId="+id+"&evtDate="+evtDate;
		document.forms[0].action=action;
		document.forms[0].submit();
	}
	
	function inputFocus(i){
		if(i.value==i.defaultValue)
			{ 
				i.value="";
				i.style.color="#000";
			}
	}
	function inputBlur(i){
		if(i.value=="")
		{ 
			i.value=i.defaultValue;
			if(i.value == i.defaultValue)
				i.style.color="Silver";
		}
	}
	
	function onRadioButtonClick(element)
	{
		if(element.value == "asPerCP")
		{
			document.forms[0].additionalSpecimens.disabled = true;
		}
		else
		{
			document.forms[0].additionalSpecimens.disabled = false;
		}
	}
</script>
</head>
<body>
<html:form action="SpecimenCollectionGroupView.do" >
	<html:hidden property="idValue" styleId="idValue" />
	<html:hidden property="participantId" styleId="participantId" />
	<html:hidden property="cpId" styleId="cpId" />
	<html:hidden property="clickedNodeId" styleId="clickedNodeId" />
	<html:hidden property="evtDate" styleId="evtDate" />
	<table width="100%" border="0"  cellpadding="10" cellspacing="0"	class="whitetable_bg">	
		<tr class="tr_bg_blue1 blue_ar_b">
			<td class="heading_text_style">
				Specimen Collection Group Details for "<bean:write name="specimenCollectionGroupViewForm" property="specimenGroupName" />
											(<bean:write name="specimenCollectionGroupViewForm" property="barcode" />)"		
			</td>
			<td align="right">
				<html:button styleClass="blue_ar_b"	property="editSCG"	title="Edit Only" value="Edit" onclick="showEditSCGPage()">
				</html:button> &nbsp;
				
				<html:button styleClass="blue_ar_b"	property="deleteSCG"	title="Delete Only" value="Delete" >
				</html:button>
			</td>
		</tr>
		<tr class="bottomtd">
		</tr>
	</table>
	
<div id="specimenCollectionGroupDetails" class="align_left_style">
<fieldset class="field_set fieldset_New_border"> 
  <legend class="blue_ar_b legend_font_size"> <b> &nbsp;SCG Details&nbsp;</b></legend>
	<table width="100%" border="0"  cellpadding="0" cellspacing="0"	class="whitetable_bg">
	   <tr height="33px">
		 <td  align="right" class="black_ar bottomtd  padding_right_style" width="25%"> 
			<b> Study Calendar Event Point </b>
		 </td> 
		 <td class="black_ar bottomtd" width="25%">
				<bean:write name="specimenCollectionGroupViewForm" property="studyCalendarEventPoint" />&nbsp;
		</td>
		<td  align="right" class="black_ar bottomtd  padding_right_style" width="25%"> 
			<b> Collection Site </b>
		</td>
		<td class="black_ar bottomtd" width="25%"> 
			<bean:write name="specimenCollectionGroupViewForm" property="collectionSite" /> 
		</td>
	  </tr>
	
	<tr height="33px" class="tr_alternate_color_lightGrey">
		<td  width="25%" align="right" class="black_ar padding_right_style">
			<b>Clinical Diagnosis</b>
		</td>
		<td width="25%" class="black_ar">
			<bean:write name="specimenCollectionGroupViewForm" property="clinicalDignosis" />
		</td>
		<td  width="25%" align="right" class="black_ar padding_right_style">
			<b>Clinical Status </b> 
		</td>
		<td  width="25%" class="black_ar" > 
			<bean:write name="specimenCollectionGroupViewForm" property="clinicalStatus" />
		</td>
	</tr>
	
	<tr height="33px">
		<td  width="25%" align="right" class="black_ar padding_right_style">
			<b>Collection Status</b>
		</td>
		<td  width="25%" class="black_ar">
			<bean:write name="specimenCollectionGroupViewForm" property="collectionStatus" />
		</td>
		<td  width="25%" align="right" class="black_ar padding_right_style">
			<b>Activity Status </b> 
		</td>
		<td  width="25%" class="black_ar" > 
			<bean:write name="specimenCollectionGroupViewForm" property="activityStatus" />
		</td>
	</tr>
	
	<tr height="33px" class="tr_alternate_color_lightGrey">
		<td  width="25%" align="right" class="black_ar padding_right_style">
			<b>Surgical Pathology Number</b>
		</td>
		<td  width="25%" class="black_ar">
			<bean:write name="specimenCollectionGroupViewForm" property="surgicalPathologyNumber" />
		</td>
		<td colspan="2"></td>
	</tr>
	</table>
</fieldset>
</div>
<p> </p>
<div id="eventDetails" class="align_left_style">
<fieldset class="field_set fieldset_New_border"> 
  <legend class="blue_ar_b legend_font_size"> <b> &nbsp; Event Details&nbsp;</b></legend>
	<table width="100%" border="0"  cellpadding="0" cellspacing="0"	class="whitetable_bg">
		<tr height="33px">
			<td  align="right" class="black_ar  padding_right_style" width="25%">
				<b> &nbsp; &nbsp; Collected Date (Time) </b> 
			</td>
			<td class="black_ar" width="25%">
				<bean:write name="specimenCollectionGroupViewForm" property="collectedDate" />&nbsp;
			</td>
			<td align="right" class="black_ar padding_right_style" width="25%"> 
				<b> Recieved Date (Time) </b></td>
			<td class="black_ar" width="25%"> 
				<bean:write name="specimenCollectionGroupViewForm" property="receivedDate" />
			</td>
	   </tr>
	   
	   <tr height="33px" class="tr_alternate_color_lightGrey">
		 <td  align="right" class="black_ar bottomtd  padding_right_style" width="25%"> 
			<b>Collected Procedure </b>
		 </td> 
		 <td class="black_ar bottomtd" width="25%">
				<bean:write name="specimenCollectionGroupViewForm" property="collectedProcedure" />&nbsp;
		</td>
		<td  width="25%" align="right" class="black_ar padding_right_style"><b>Recieved Quality</b> </td>
		<td  width="25%" class="black_ar" > 
			<bean:write name="specimenCollectionGroupViewForm" property="receivedQuality" />
		</td>
	  </tr>
	
	<tr height="33px">
		<td  width="25%" align="right" class="black_ar padding_right_style">
			<b>Collected Container</b>
		</td>
		<td  width="25%" class="black_ar">
			<bean:write name="specimenCollectionGroupViewForm" property="collectedContainer" />
		</td>
		<td colspan="2"> </td>
	</tr>
	</table>
</fieldset>
</div>
<p></p>


<div id="specimenActions" class="align_left_style">
<fieldset class="field_set fieldset_New_border"> 
  <legend class="blue_ar_b legend_font_size"> <b> &nbsp;Specimen Action&nbsp;</b></legend>
	<table width="100%" border="0"  cellpadding="0" cellspacing="0"	class="whitetable_bg">
		<tr height="33px">
			<td width="50%" style="padding-left:20px">
				<input type="radio" name="additionalSpecimensRadio" value="asPerCP" onclick="onRadioButtonClick(this)">
				   &nbsp; <span class="black_ar"> <b>  As per Collection Protocol </b> </span>
			</td>
		</tr>
		<tr height="33px" class="tr_alternate_color_lightGrey">
			<td width="50%"  style="padding-left:20px">
				<input type="radio" name="additionalSpecimensRadio" value="additionalSpecimensRadio" onclick="onRadioButtonClick(this)">
				 &nbsp; <span class="black_ar">  <b> Additional Specimen(s) </b> </span> &nbsp;&nbsp;&nbsp;
				 
				  <input type="text" name="additionalSpecimens" id="additionalSpecimens" size ="5" value="Count" class="text_box_style" onfocus="inputFocus(this)" onblur="inputBlur(this)"/>
			</td>
		</tr>
		<tr height="33px">
			<td width="50%" style="padding-left:50px">
				<html:button styleClass="blue_ar_b"	property="Collect Specimens"	title="Collect Specimen" value="Collect Specimen">
				</html:button> &nbsp;
				<html:button styleClass="blue_ar_b"	property="Print Labels"	title="Print Labels" value="Print Labels" >
				</html:button>
			</td>
		</tr>
	</table>
</fieldset>
</div>
</html:form>
</body>
</html>
