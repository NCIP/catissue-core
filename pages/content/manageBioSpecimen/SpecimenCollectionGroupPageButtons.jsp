<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm"%>

<!-- action buttons begins -->
							<div id="scgPageButtons">

											<logic:equal name="<%=Constants.SUBMITTED_FOR%>" value="AddNew">
												<%
													isAddNew=true;
												%>
											</logic:equal>
											<!-- Patch ID: Bug#3184_13 and Description: Added restrict checkbox
											-->
											 <tr>
									          
											 <td colspan="3"  align="left" class="dividerline" >
											 <html:checkbox styleId="restrictSCGCheckbox" property="restrictSCGCheckbox" value="true" onclick="disableButtonsOnCheck(this)" style="display:none">
												</html:checkbox>
													&nbsp;<html:checkbox styleId="printCheckbox" property="printCheckbox" value="true" onclick="showPriterTypeLocation()">
														<span class="black_ar" style="vertical-align:3px">
															<bean:message key="print.checkboxLabel"/>
														</span>
														</html:checkbox>
											 </td> 
											</tr>
											<tr>
													
												       <td>
					   			                         <%@ include file="/pages/content/common/PrinterLocationTypeComboboxes.jsp" %>
			 				                          </td>
											</tr>
										<!--  End : Displaying   printer type and location -->
											 
											<tr>
												<td colspan="3" align="left" class="buttonbg">
												
												
													<html:button styleClass="blue_ar_b"
															property="submitPage"
															title="Submit Only"
															value="<%=Constants.SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST[0][0]%>"
															onclick="testFunction()"
															styleId = "submitOnly">
											     	</html:button>
												
												<!-- delete button added for disabling the objects -->
												<%if( clinportalUrl!=null && clinportalUrl.length() >0){%>
<html:button property="clinportal" style='width=150;' styleId="clinportal" styleClass="blue_ar_c" value="Clinical Data Entry" onclick="makeClinPortalUrl()"/>

												<%} %>
													<%
														String deleteAction="deleteObject('" + formName +"','" + Constants.CP_QUERY_BIO_SPECIMEN + "')";
													%>
													<html:button styleClass="blue_ar_c"
														property="disableRecord"
														title="Delete or Disable Record"
														value="Delete"
														onclick="<%=deleteAction%>">
													</html:button>
												</td>
											</tr>
							</div>
							<!-- action buttons end -->
<script>
function testFunction()
	{
		var collStatus = scgCombo.collectionStatusCombo.getSelectedValue();
		
		
		if(cpSelect)
		{
			if(collStatus == "Pending")
			{
				
				scgCombo.collectionStatusCombo.setComboText('Complete');
				scgCombo.collectionStatusCombo.setComboValue('Complete');
			}
			<%=normalSubmit%>;
		}
		else if(adhocSelect)
		{
			if(collStatus == "Pending")
			{
				
				scgCombo.collectionStatusCombo.setComboText('Complete');
				scgCombo.collectionStatusCombo.setComboValue('Complete');
			}
			var adhocSpecimenCnt = document.getElementById('numberOfSpecimens').value;
			if(adhocSpecimenCnt >1)
			{
				<%=forwardToSubmitForMultipleSpecimen%>;
			}
			else
			{
				<%=forwardToSubmit%>;
			}
		}
		else
		{
			<%=forwardToJustSubmit%>;
		}
	}
function isNumeric(number) {
value = number.value;
if(value==Number(value))
return true;
else
{
	alert("Invalid Number, Please enter valid number.");
	number.value="";
	setTimeout(function(){number.focus();}, 1);
}
}
document.getElementById('numberOfSpecimens').value = "";
var collectionStatus = document.getElementById('collectionStatus').value;
if(operation == "add")
{
	var radios = document.getElementsByName('specimenChild');
		for(i=0;i<radios.length; i++){
		  if(radios[i].value == 1){
			 radios[i].checked = true;
			 adhocSelect = false;
				cpSelect = true;
			 }
		}
}
else
{
	if(collectionStatus == "Pending")
	{
		var radios = document.getElementsByName('specimenChild');
		for(i=0;i<radios.length; i++){
		  if(radios[i].value == 1){
			 radios[i].checked = true;
			 adhocSelect = false;
				cpSelect = true;
			 }
		}
	}
	else if(collectionStatus == "Complete")
	{
		var radios = document.getElementsByName('specimenChild');
		for(i=0;i<radios.length; i++){
		  if(radios[i].value == 3){
			 radios[i].checked = true;
			 adhocSelect = false;
				cpSelect = false;
			 }
		}
	}
}
</script>