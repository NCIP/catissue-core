<script>
function giveCall(url,msg,msg1,id)
{
	document.getElementsByName('objCheckbox').value=id;
	document.getElementsByName('objCheckbox').checked = true;
	ajaxAssignTagFunctionCall(url,msg,msg1);
}
</script>
<!-- action buttons begins -->
<table cellpadding="4" cellspacing="0" border="0" id="specimenPageButton" width="100%"> 
	<logic:equal name="<%=Constants.SUBMITTED_FOR%>" value="AddNew">
		<% isAddNew=true;%>
	</logic:equal>


	<tr>

		<td class="buttonbg">
		  <%
			if(operation.equals(Constants.ADD))
			{
	   	 %>
			<html:button
				styleClass="blue_ar_b" property="submitButton"
				title="<%=Constants.SPECIMEN_BUTTON_TIPS[0]%>"
				value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[0][0]%>"
				onclick="updateStorageContainerValue();onDeriveSubmit()">
			</html:button>
		 <%	
			}
		    else
			{
				
		 %>	
				<html:button
					styleClass="blue_ar_b" property="submitButton"
					title="<%=Constants.SPECIMEN_BUTTON_TIPS[0]%>"
					value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[0][0]%>"
					onclick="updateStorageContainerValue();onDeriveSubmit()">
				</html:button>
		 <%
			}
		 %>     			
		
		
		
		<!-- Commented as per bug 2115. Mandar. 10-July-06
											</tr>
											<tr>							
											<logic:notEqual name="<%=Constants.PAGE_OF%>" value="<%=Constants.QUERY%>">
												<td class="formFieldNoBorders" nowrap>
													<html:button styleClass="actionButton"  
															property="submitPage"
															title="<%=Constants.SPECIMEN_BUTTON_TIPS[2]%>"
															value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[2][0]%>" 
															disabled="<%=isAddNew%>" 
															onclick="<%=addEventsSubmit%>">
						  				     	    
											     	</html:button>
												</td>
-->
		
			|&nbsp;<html:button
				styleClass="blue_ar_c" property="moreButton"
				title="<%=Constants.SPECIMEN_BUTTON_TIPS[3]%>"
				value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[3][0]%>"
				disabled="<%=isAddNew%>" onclick="<%=addMoreSubmit%>"
			>
			</html:button>
		


		</logic:notEqual>
			
		
		<%
 						String	organizeTarget = "ajaxTreeGridInitCall('Are you sure you want to delete this specimen from the list?','List contains specimens, Are you sure to delete the selected list?','SpecimenListTag','SpecimenListTagItem')";
						
						if(showSpecList.equals("true"))
						{
 %>
						| <input type="button" value="Add To Specimen List"
							onclick="<%=organizeTarget%> " class="blue_ar_c">
	<%}%>
		<input type="checkbox" name="objCheckbox"  id="objCheckbox" style="display:none" value="team" checked/>
		</td>
	</tr>
</table>
<!-- action buttons end -->
<%
 	String speciId = String.valueOf(form.getId());
 	String	assignTargetCall = "giveCall('AssignTagAction.do?entityTag=SpecimenListTag&entityTagItem=SpecimenListTagItem&objChkBoxString="+speciId+"','Select at least one existing list or create a new list.','No specimen has been selected to assign.','"+speciId+"')";
 %>
 <%@ include file="/pages/content/manageBioSpecimen/SpecimenTagPopup.jsp" %>
