 <!-- Data Grid for Participant Details Starts -->
         	<tr>
				<td colspan="2" class="bottomtd"></td>
			</tr>
			<%
				if (request.getAttribute(edu.wustl.simplequery.global.Constants.SPREADSHEET_DATA_LIST) != null
									&& dataList.size() > 0) {
								isRegisterButton = true;
								if (request.getAttribute(Constants.SUBMITTED_FOR) != null
								&& request.getAttribute(Constants.SUBMITTED_FOR)
										.equals("AddNew")) {
									isRegisterButton = false;
								}
			%>
			<tr>
				<td colspan="2">
				<table width="100%" summary="" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="black_ar_b" height="25"><bean:message
							key="participant.lookup" /></td>
					</tr>
					<tr height=110 valign=top >
						<td valign=top class="formFieldAllBorders">
						<script>
					function participant(id)
					{
						var cl = mygrid.cells(id,mygrid.getColumnCount()-1);
						var pid = cl.getValue();
						 document.forms[0].submittedFor.value = "AddNew";
						 var pageOf = "<%=pageOf%>";
						if(pageOf == "<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>")
						{
							window.location.href = 'CPQueryParticipantSelect.do?submittedFor=AddNew&operation=add&participantId='+pid
						}
						else
						{
							window.location.href = 'ParticipantLookup.do?submittedFor=AddNew&operation=add&participantId='+pid
						}						
					} 				
					/* 
						to be used when you want to specify another javascript function for row selection.
						useDefaultRowClickHandler =1 | any value other than 1 indicates you want to use another row click handler.
						useFunction = "";  Function to be used. 	
					*/
					var useDefaultRowClickHandler =2;
					var useFunction = "participant";	
					var wdt = getWidth(90);
					if(wdt>1000)
					wdt=getWidth(63.5);
					
					//alert(wdt);
					wdt = 990;
			</script> 
			<%@ include file="/pages/content/search/AdvanceGrid.jsp"%>
			</td>
					</tr>
					<tr>
						<td align="center" colspan="7" class="formFieldWithNoTopBorder">
						<INPUT TYPE='RADIO' NAME='chkName' value="Add"
							onclick="CreateNewClick()"> <font size="2">Ignore
						matches and create new participant </font> </INPUT>&nbsp;&nbsp; <INPUT
							TYPE='RADIO' NAME='chkName' value="Lookup"
							onclick="LookupAgain()" checked=true> <font size="2">Lookup
						again </font> </INPUT></td>
					</tr>
				</table>
				</td>
			</tr>
			<%
					}
					%>
			 <!-- Data Grid for Participant Details Ends-->