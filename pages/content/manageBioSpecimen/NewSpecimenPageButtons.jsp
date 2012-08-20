
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
		
		
		
		|&nbsp;<html:button
				styleClass="blue_ar_c" property="addToCart"  styleId="addToCart"
				title="<%=Constants.SPECIMEN_BUTTON_TIPS[0]%>"
				onclick="onAddToCart()">
				<bean:message key="buttons.addToCart"/>
		</html:button> 
		<%
 						String	organizeTarget = "ajaxTreeGridInitCall('Are you sure you want to delete this specimen from the list?','List contains specimens, Are you sure to delete the selected list?','SpecimenListTag','SpecimenListTagItem')";
 %>
						| <input type="button" value="Add To Specimen List"
							onclick="<%=organizeTarget%> " class="blue_ar_c">
		</td>
	</tr>
</table>
<!-- action buttons end -->
<div id="blanket" style="display: none;"></div>
<div id="popUpDiv" style="display: none; top: 100px; left: 210.5px;">

					<a onclick="popup('popUpDiv')"><img style="float: right;"
						height='23' width='24' src='images/close_button.gif'
						border='0'> </a>
					<table class=" manage tags" width="100%" cellspacing="0"
						cellpadding="5" border="0">

						<tbody>
							<tr valign="center" height="35" bgcolor="#d5e8ff">
								<td width="28%" align="left"
									style="font-size: .82em; font-family: verdana;">
									<p>
										&nbsp&nbsp&nbsp&nbsp<b> Specimen Lists</b>
									</p>
								</td>
							</tr>
					</table>


					<div id="treegridbox"
						style="width: 530px; height: 237px; background-color: white;"></div>




					<p>
						&nbsp&nbsp&nbsp<label width="28%" align="left"
							style="font-size: .82em; font-family: verdana;"><b> List Name
								: </b> </label> <input type="text" id="newTagName" name="newTagName"
							size="20" onclick="this.value='';" maxlength="50" /><br>
					</p>
					<p>
						<%
						String specId = String.valueOf(form.getId());
 String	assignTarget = "giveCall('AssignTagAction.do?entityTag=SpecimenListTag&entityTagItem=SpecimenListTagItem&objChkBoxString="+specId+"','Select at least one existing list or create a new list.','No query has been selected to assign.','"+specId+"')";
 %>
						<input type="button" value="ASSIGN" onclick="<%=assignTarget%> "
							onkeydown="<%=assignTarget%> " class="btn3">
							
							<input type="checkbox" name="objCheckbox"  id="objCheckbox" style="display:none" value="team" checked>Spurs<br>
					</p>
				</div>
			</div>
			<script>
	
function doInitGrid()
{
	grid = new dhtmlXGridObject('mygrid_container');
	grid.setImagePath("deploytempCatissuecore/AdvanceQuery/dhtml/imgs/");
 	grid.setHeader("My Specimen Lists");
 	grid.setInitWidths("175");
 	grid.setColAlign("left");
 	grid.setSkin("dhx_skyblue");
 	grid.setEditable(false);
   	grid.attachEvent("onRowSelect", doOnRowSelected);
 	grid.init();
 	grid .load ("TagGridInItAction.do");
}
function doOnRowSelected(rId)
{
	submitTagName(rId);	 
}	
function giveCall(url,msg,msg1,id)
{
	
	document.getElementById('objCheckbox').value=id;
	ajaxAssignTagFunctionCall(url,msg,msg1);
}

			var popupmygrid;
function doInItTreeGrid1()
{
	popupmygrid = new dhtmlXGridObject('treegridbox');
	popupmygrid.selMultiRows = true;
	popupmygrid.imgURL = "dhtmlx_suite/dhtml_pop/imgs/";
	popupmygrid.setHeader(",<div style='text-align:center;'>My Specimen Lists</div>,");
	//popupmygrid.setNoHeader(true);
	popupmygrid.setInitWidths("25,*,40");
	popupmygrid.setColAlign("left,left,left");
	popupmygrid.setColTypes("txt,tree,txt");
	popupmygrid.setColSorting("str,str,str");
	popupmygrid.attachEvent("onRowSelect", doOnTreeGridRowSelected);
	popupmygrid.setEditable(false);
	popupmygrid.init();
	//popupmygrid.setOnOpenHandler(expand);
	popupmygrid.setSkin("dhx_skyblue");
	doInitParseTree();
	//	alert(popupmygrid.getTree(1));
}
function doOnTreeGridRowSelectedaa(rId)
{
	ajaxTreeGridRowSelectCall(rId); 
}
 function expand(id,mode)
 {
 alert('ddd');
 alert(id);
 }
function doInitParseTree()
{
	popupmygrid.loadXML("TreeTagAction.do?entityTag=SpecimenListTag");

}

			</script>