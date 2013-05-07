
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<LINK type=text/css rel=stylesheet href="css/participantEffects.css">
<script>
function showEditSpecimenPage()
{
	var id1=document.getElementById("idValue").value;
	var pId=document.getElementById("participantId").value;
	var cpId=document.getElementById("cpId").value;
	var action="QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&operation=edit&id="+id1+"&refresh=false&cpSearchParticipantId="+pId+"&cpSearchCpId="+cpId;
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

</script>
</head>
<body>
<html:form action="NewSpecimenView.do" >
	<html:hidden property="idValue" styleId="idValue" />
	<html:hidden property="participantId" styleId="participantId" />
	<html:hidden property="cpId" styleId="cpId" />
	<table width="100%" border="0"  cellpadding="10" cellspacing="0"	class="whitetable_bg">	
		<tr class="tr_bg_blue1 blue_ar_b">
			<td class="heading_text_style" >
				Specimen Details for 
				<bean:write name="newSpecimenViewForm" property="label" />
				(<bean:write name="newSpecimenViewForm" property="barcode" />)
			</td>
			<td align="right">
				<html:button styleClass="blue_ar_b"	property="editSpecimen"	title="Edit Only" value="Edit" onclick="showEditSpecimenPage()">
				</html:button> &nbsp;
				
				<html:button styleClass="blue_ar_b"	property="deleteSpecimen"	title="Delete Only" value="Delete" >
				</html:button>
			</td>
		</tr>
		<tr>
			<td class="bottomtd"></td>
		</tr>
	</table>
	
<div id="specimenDetails" class="align_left_style">
<fieldset class="field_set fieldset_New_border"> 
  <legend class="blue_ar_b legend_font_size"> <b> &nbsp;Label & Status&nbsp;</b></legend>
	<table width="100%" border="0"  cellpadding="0" cellspacing="0"	class="whitetable_bg">
	   <tr height="33px">
		 <td  align="right" class="black_ar bottomtd  padding_right_style" width="25%"> 
			<b> Specimen Collection Group </b>
		 </td> 
		 <td class="black_ar bottomtd" width="25%">
				<bean:write name="newSpecimenViewForm" property="specimenCollectionGroup" />&nbsp;
		 </td>
		 <td  align="right" class="black_ar bottomtd  padding_right_style" width="25%"> 
			<b> Lineage </b>
		 </td>
	 	 <td class="black_ar bottomtd" width="25%"> 
			<bean:write name="newSpecimenViewForm" property="lineage" /> 
		 </td>
	  </tr>
	
	 <tr height="33px" class="tr_alternate_color_lightGrey">
		<td  width="25%" align="right" class="black_ar padding_right_style">
			<b>Collection Status</b>
		</td>
		<td width="25%" class="black_ar">
			<bean:write name="newSpecimenViewForm" property="collectionStatus" />
		</td>
		<td  width="25%" align="right" class="black_ar padding_right_style">
			<b>Created On</b> 
		</td>
		<td width="25%" class="black_ar" > 
			<bean:write name="newSpecimenViewForm" property="createdDate" />
		</td>
	</tr>
	</table>
</fieldset>
</div>
<p></p>
<div id="specimenCharacteristic" class="align_left_style">
<fieldset class="field_set fieldset_New_border"> 
  <legend class="blue_ar_b legend_font_size"> <b> &nbsp;Specimen Characteristic &nbsp;</b></legend>
	<table width="100%" border="0"  cellpadding="0" cellspacing="0"	class="whitetable_bg">
	   <tr height="33px">
		 <td  align="right" class="black_ar bottomtd  padding_right_style" width="25%"> 
			<b> Class (Type) </b>
		 </td> 
		 <td class="black_ar bottomtd" width="25%">
				<bean:write name="newSpecimenViewForm" property="classValue" />
				(<bean:write name="newSpecimenViewForm" property="typeValue" />)
		</td>
		<td  align="right" class="black_ar bottomtd  padding_right_style" width="25%"> 
			<b>Tissue Site(Tissue Side) </b>
		</td>
		<td class="black_ar bottomtd" width="25%"> 
			 <bean:write name="newSpecimenViewForm" property="tissueSite" />
			 (<bean:write name="newSpecimenViewForm" property="tissueSide" />) 
		</td>
	  </tr>
	
		<tr height="33px" class="tr_alternate_color_lightGrey">
			<td  width="25%" align="right" class="black_ar padding_right_style">
				<b>Pathological Status</b>
			</td>
			<td width="25%" class="black_ar">
				<bean:write name="newSpecimenViewForm" property="pathologicalStatus"/>
			</td>
			<td  width="25%" >
				 
			</td>
			<td  width="25%"> 
				
			</td>
		</tr>
	</table>
</fieldset>
</div>
<p></p>

<div id="availability" class="align_left_style">
<fieldset class="field_set fieldset_New_border"> 
  <legend class="blue_ar_b legend_font_size"> <b> &nbsp;Availability &nbsp;</b></legend>
	<table width="100%" border="0"  cellpadding="0" cellspacing="0"	class="whitetable_bg">
	   <tr height="33px">
		 <td  align="right" class="black_ar bottomtd  padding_right_style" width="25%"> 
			<b> Initial Quantity(Available Quantity) </b>
		 </td> 
		 <td class="black_ar bottomtd" width="25%">
				<bean:write name="newSpecimenViewForm" property="initialQuantity" />
				(<bean:write name="newSpecimenViewForm" property="availableQuantity" />)
		</td>
		<td  align="right" class="black_ar bottomtd  padding_right_style" width="25%"> 
			<b>Concentration </b>
		</td>
		<td class="black_ar bottomtd" width="25%"> 
				<bean:write name="newSpecimenViewForm" property="concentrationValue" /> 
		</td>
	 </tr>
	 
	 <tr height="33px" class="tr_alternate_color_lightGrey">
		 <td  align="right" class="black_ar bottomtd  padding_right_style" width="25%"> 
			<b> Storage Position  </b>
		 </td> 
		 <td class="black_ar bottomtd" width="25%">
				<bean:write name="newSpecimenViewForm" property="storagePosition"/>
				&nbsp;
				<html:button styleClass="blue_ar_b"	property="editStoragePosition"	title="Edit Only" value="Edit">
				</html:button>
		</td>
		<td  align="right" class="black_ar bottomtd  padding_right_style" width="25%"> 
			<b>Is Available? </b>
		</td>
		<td class="black_ar bottomtd" width="25%"> 
				Yes 
		</td>
	 </tr>
	
	</table>
</fieldset>
</div>
<p></p>

<div id="specimenActions" class="align_left_style">
<fieldset class="field_set fieldset_New_border"> 
 <legend class="blue_ar_b legend_font_size"> <b> &nbsp;Specimen Actions &nbsp;</b></legend>
<table width="100%" border="0"  cellpadding="0" cellspacing="0"	class="whitetable_bg">
	<tr height="5px"></tr>
	<tr height="33px">  
		<td width="25%">
			<input type="radio" name="aliquotDerivedSpecimen" value="createAliquotSpecimen">
				 &nbsp; <span class="black_ar">  <b> Create Derivative  </b> </span> &nbsp;
				 
				 <input type="text" name="countDerivative" id="count" size ="5" value="Count" class="text_box_style" onfocus="inputFocus(this)" onblur="inputBlur(this)"/>
		</td>
		<td width="25%">
			<input type="radio" name="aliquotDerivedSpecimen" value="createAliquotSpecimen">
			&nbsp; <span class="black_ar">  <b> Add New Events  </b> </span> &nbsp;
			<html:select property="specimenEvents" styleClass="black_ar  combo_style_height">
					<html:option value="scgvalue1">Fixed</html:option>
					<html:option value="scgvalue2">Check in Check Out</html:option>
					<html:option value="scgvalue1">Fluid Specimen Review</html:option>
					<html:option value="scgvalue2">Cell Specimen Review</html:option>
			</html:select> &nbsp;
			
						<html:link href="#" styleId="existingEvents" styleClass="view">
									Existing Events
						</html:link>
		</td>
	</tr>
	
	<tr height="33px" class="tr_alternate_color_lightGrey">  
		<td width="25%">
			<input type="radio" name="aliquotDerivedSpecimen" value="createAliquotSpecimen"/>
				 &nbsp; <span class="black_ar">  <b>  Create Aliquot </b></span>
			<span style="padding-left:28px">
				<input type="text" name="countAliquot" id="count" size ="5" value="Count"  class="text_box_style" onfocus="inputFocus(this)" onblur="inputBlur(this)"/>
				&nbsp;	
				<input type="text" name="Aquantity" id="Aquantity" size ="5" placeholder="Qty" class="text_box_style" onfocus="inputFocus(this)" onblur="inputBlur(this)"/>
			</span>		
		</td>
		
		<td width="25%">
			<input type="radio" name="aliquotDerivedSpecimen" value="createAliquotSpecimen">
			&nbsp; <span class="black_ar">  <b>  Create Aliquot/Derived Specimen as per CP </b></span>
		</td>
	</tr>
</table>
</fieldset>
</div>
<p></p>
<div class="align_left_style">
	<table width="50%" border="0"  cellpadding="0" cellspacing="0"	class="whitetable_bg">
		<tr height="2px"></tr>
	   <tr>
			<td>
				<html:button styleClass="blue_ar_b"	property="submitPage"	title="Submit Only" value="Submit">
				</html:button> &nbsp;&nbsp;
				
				<html:button styleClass="blue_ar_b"	property="AddToSpecimenList"	title="Add To Specimen List" value="Add To Specimen List" >
				</html:button> &nbsp;&nbsp;
				
				<html:button styleClass="blue_ar_b"	property="printLabels"	title="Print Labels" value="Print Labels" >
				</html:button>
			</td>
	  </tr>
  </table>
</div>
</html:form>
</body>
</html>
