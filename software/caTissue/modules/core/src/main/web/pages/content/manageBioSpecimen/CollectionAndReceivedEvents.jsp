<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%
	// Patch_Id: Improve_Space_Usability_On_Specimen_Page_2
	// variable to get the current display style of collect recieve event table
	String crDispType1=request.getParameter("crDispType");
%>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<table summary="" cellpadding="3" cellspacing="0" border="0" width="700" id="collectionEvent">					
					<!-- Mandar AutoEvents start -->		

					<tr>
						<!-- Patch ends here -->
						<td colspan="3" width="550" class="formTitle">
							<bean:message key="specimen.collectedevents.title"/>	
						</td>
						<td colspan="3" width="450" class="formTitle">
							<bean:message key="specimen.receivedevents.title"/>						
						</td>
					</tr>
					<tr>					
						<!-- CollectionEvent fields -->	
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="collectionprocedure">
								<bean:message key="collectioneventparameters.collectionprocedure"/> 
							</label>
						</td>
						<td class="formField">
								<autocomplete:AutoCompleteTag property="collectionEventCollectionProcedure"
										  optionsList = "<%=request.getAttribute(Constants.PROCEDURE_LIST)%>"
										  initialValue="<%=form.getCollectionEventCollectionProcedure()%>"
						/>							
						</td>						
						
						<!-- RecievedEvent fields -->
						<td class="formRequiredNotice" width="5"rowspan="2">*</td>
						<td class="formRequiredLabel"rowspan="2"> 
							<label for="quality">
								<bean:message key="receivedeventparameters.receivedquality"/> 
							</label>
						</td>						
						<!-- receivedeventparameters.receivedquality -->
						<td class="formField"rowspan="2">
							<autocomplete:AutoCompleteTag property="receivedEventReceivedQuality"
										  optionsList = "<%=request.getAttribute(Constants.RECEIVED_QUALITY_LIST)%>"
										  initialValue="<%=form.getReceivedEventReceivedQuality()%>"
						/>
						</td>
					</tr>
					
					<!-- CollectionEvent fields -->	
					<tr>							
							<td class="formRequiredNotice" width="5">*</td>
							<td class="formRequiredLabel">
								<label for="container">
									<bean:message key="collectioneventparameters.container"/> 
								</label>
							</td>
							<td class="formField">
								<autocomplete:AutoCompleteTag property="collectionEventContainer"
										  optionsList = "<%=request.getAttribute(Constants.CONTAINER_LIST)%>"
										  initialValue="<%=form.getCollectionEventContainer()%>"
						    />
							</td>
					</tr>	
					
					<!-- comments -->
					<tr>	
						<!-- CollectionEvent fields -->	
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="comments">
								<bean:message key="eventparameters.comments"/> 
							</label>
						</td>
						<td class="formField">
							<html:textarea styleClass="formFieldSized20"  styleId="collectionEventComments" property="collectionEventComments" />
						</td>						
						
						<!-- RecievedEvent fields -->
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="comments">
								<bean:message key="eventparameters.comments"/> 
							</label>
						</td>						
						<td class="formField">
							<html:textarea styleClass="formFieldSized20"  styleId="receivedEventComments" property="receivedEventComments" />
						</td>						
					</tr> 
				<!-- Mandar: 11-July-06 AutoEvents end  -->
</table>	
				
